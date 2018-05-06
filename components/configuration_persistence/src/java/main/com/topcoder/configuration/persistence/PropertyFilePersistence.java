/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileLock;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.InvalidConfigurationException;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

/**
 * <p>
 * This implementation of ConfigurationPersistence may be used to save and read configuration from property files that
 * are compatible with those defined in TopCoder Configuration Manager component. The functionality encapsulated in this
 * class is very similar to functionality provided in the PropConfigProperties class from the Configuration Manager
 * component.
 * </p>
 * An example properties config file: <code>
 * a=valuea
 * f=valuef1;valuef2
 * b=valueb
 * e=valuee1;valuee2
 * h.g=valueg
 * b.c=valuec
 * h.g.i=valuei
 * </code>
 * <p>
 * This class is stateless and thread safe.
 * </p>
 *
 * @author bendlund, rainday
 * @version 1.0
 *
 */
public class PropertyFilePersistence implements ConfigurationPersistence {

    /**
     * The configuration object name for properties file.
     */
    private final String configObjName = "default";

    /**
     * Default delimiter in the properties file.
     */
    private final String defaultDelimiter = ";";

    /**
     * List delimiter string in the properties file.
     */
    private final String delimiterString = "listdelimiter";

    /**
     * <p>
     * Default constructor, do nothing.
     * </p>
     *
     */
    public PropertyFilePersistence() {
        // empty
    }

    /**
     * <p>
     * Saves the given ConfigurationObject to the specified property file. Any data currently in the specified file will
     * be overwritten. There should be a "default" child of the configuration object, all config properties in it will
     * be saved.
     * </p>
     * 
     * @param file
     *            a file to save configuration to
     * @param config
     *            an abstract path name for the configuration file to update.  The file is sought first among the
     *            resources accessible to the context ClassLoader, and if not found there then the file system is
     *            checked.  Saving will fail if the file is not writable; in particular, it will fail if the file is
     *            inside an archive file.
     *
     * @throws IOException
     *             indicates that an I/O problem occurred in reading from the specified file
     * @throws IllegalArgumentException
     *             if either argument is null
     * @throws ConfigurationParserException
     *             if any problem occurred in retrieving value from the configuration object
     */
    public void saveFile(File file, ConfigurationObject config) throws IOException, ConfigurationParserException {
        Helper.checkNotNull(file, "file");
        Helper.checkNotNull(config, "config object");
        String delimiter = defaultDelimiter;
        RandomAccessFile outputFile = null;
        FileLock locker = null;
        try {
            StringBuffer content = new StringBuffer();
            outputFile = new RandomAccessFile(file, "rw");
            locker = outputFile.getChannel().tryLock();
            if (locker == null) {
                throw new IOException("Can't access the properties file.");
            }
            // if file exists, read the delimiter from the original file
            if (file.exists()) {
                delimiter = getListDelimiter(outputFile);
                outputFile.seek(0);
            }
            outputFile.setLength(0);
            ConfigurationObject obj = config.getChild(configObjName);
            if (obj == null) {
                throw new ConfigurationParserException("There must be 'default' children in the configuration object");
            }
            // writer all properties
            List list = new LinkedList();
            list.add("");
            list.add(obj);
            // bfs process, restore namespace for each property, then write it to the file
            while (!list.isEmpty()) {
                String namespace = (String) list.remove(0);
                ConfigurationObject curObj = (ConfigurationObject) list.remove(0);
                String[] propertyKeys = curObj.getAllPropertyKeys();
                for (int i = 0; i < propertyKeys.length; ++i) {
                    // get namespace
                    if (namespace.equals("")) {
                        content.append(propertyKeys[i]);
                    } else {
                        content.append(namespace + "." + propertyKeys[i]);
                    }
                    content.append('=');
                    // write all values
                    Object[] values = curObj.getPropertyValues(propertyKeys[i]);
                    if (values.length > 0) {
                        content.append(values[0].toString());
                    }
                    for (int j = 1; j < values.length; ++j) {
                        content.append(delimiter);
                        content.append(values[j].toString());
                    }
                    content.append(Helper.RETURN_STRING);
                }
                String[] childrenNames = curObj.getAllChildrenNames();
                for (int j = 0; j < childrenNames.length; ++j) {
                    if (namespace.equals("")) {
                        list.add(childrenNames[j]);
                    } else {
                        list.add(namespace + "." + childrenNames[j]);
                    }
                    list.add(curObj.getChild(childrenNames[j]));
                }
            }
            outputFile.write(new String(content).getBytes("UTF-8"));
        } catch (ConfigurationAccessException cae) {
            throw new ConfigurationParserException("Write property file error.", cae);
        } finally {
            if (locker != null) {
                locker.release();
            }
            if (outputFile != null) {
                outputFile.close();
            }
        }
    }

    /**
     * <p>
     * Returns a ConfigurationObject with the specified name containing the configuration stored in the specified file
     * or resource.  This method should generate a DefaultConfigurationObject.
     * </p>
     *
     * @param name
     *            name of the ConfigurationObject to return
     * @param file
     *            an abstract path name for the configuration file to read.  The file is sought first among the
     *            resources accessible to the context ClassLoader, and if not found there then the file system is
     *            checked.
     * @throws IOException
     *             if an I/O problem occurred in reading from the specified file
     * @throws IllegalArgumentException
     *             if name is empty or null, or file is null
     * @throws ConfigurationParserException
     *             the file could not be parsed by this ConfigurationPersistence implementation
     *
     * @return the generated ConfigurationObject for the property file
     */
    public ConfigurationObject loadFile(String name, File file) throws IOException, ConfigurationParserException {
        Helper.checkNotNullOrEmpty(name, "name");
        Helper.checkNotNull(file, "file");
        String resourcePath = Helper.changeSeparator(file.getPath());
        URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource(resourcePath);
        InputStream inputStream;
        
        if (resourceUrl != null) {
            RandomAccessFile inputFile = Helper.getFile(resourceUrl);
            
            if (inputFile != null) {
                // Configuration is to be read from a file accessible on the file system
                try {
                    inputStream = Helper.bufferFile(inputFile);
                } finally {
                    inputFile.close();
                }
            } else {
                // Configuration is to be read from an accessible resource other than a directly-accessible file
                inputStream = resourceUrl.openStream();
            }
        } else if (file.exists()) {
            // Configuration is to be read from a file accessible on the file system, but not as a resource
            RandomAccessFile inputFile = new RandomAccessFile(file, "rw");
            
            try {
                inputStream = Helper.bufferFile(inputFile);
            } finally {
                inputFile.close();
            }
        } else {
            throw new IOException("File " + resourcePath + " doesn't exist.");
        }
        try {
            return readConfigurationObject(name, new MemoryCacheImageInputStream(inputStream));
        } catch (InvalidConfigurationException ice) {
            throw new ConfigurationParserException("Read property file error.", ice);
        } catch (ConfigurationAccessException cae) {
            throw new ConfigurationParserException("Read property file error.", cae);
        } finally {
            inputStream.close();
        }
    }

    /**
     * <p>
     * Returns a ConfigurationObject with the specified name containing the configuration stored in the specified input
     * stream.
     * </p>
     *
     * @param name
     *            name of the ConfigurationObject to return
     * @param imageInputStream
     *            a input stream to load configuration from
     *
     * @throws IOException
     *             if an I/O problem occurred in reading from the specified file
     * @throws IllegalArgumentException
     *             if name or resource is empty or null
     * @throws ConfigurationParserException
     *             the file could not be parsed by this ConfigurationPersistence implementation
     * @return the generated ConfigurationObject for the resource
     * @since BUGR-1460
     */
    private ConfigurationObject readConfigurationObject(String name, ImageInputStream imageInputStream)
                    throws InvalidConfigurationException, ConfigurationAccessException, ConfigurationParserException,
                    IOException {
        String delimiter = getListDelimiter(imageInputStream);
        imageInputStream.seek(0);
        ConfigurationObject root = new DefaultConfigurationObject(name);
        ConfigurationObject defaultObj = new DefaultConfigurationObject(configObjName);
        root.addChild(defaultObj);
        for (String line = imageInputStream.readLine(); line != null; line = imageInputStream.readLine()) {
            line = line.trim();
            // ignore the blank line
            if (line.length() == 0) {
                continue;
            }
            // ignore comment line which start with '#' or '!'
            if (!line.startsWith("#") && !line.startsWith("!")) {
                // parse line, str[0]:namespace.property str[1]: value
                String seperateChar = null;
                if (line.indexOf("=") > 0) {
                    seperateChar = "[=]";
                } else if (line.indexOf(" ") > 0) {
                    seperateChar = "[ ]";
                } else if (line.indexOf(":") > 0) {
                    seperateChar = "[:]";
                } else {
                    throw new ConfigurationParserException("Incorrect properties file formatting.");
                }
                String[] str = line.split(seperateChar);
                if (str.length != 2 || str[0].trim().length() == 0 || str[1].trim().length() == 0) {
                    throw new ConfigurationParserException("Incorrect properties file formatting.");
                }
                // ignore the list delimiter line
                if (str[0].trim().toLowerCase().endsWith(delimiterString)) {
                    continue;
                }
                ConfigurationObject findObj = defaultObj;
                String propertyName = str[0].trim();
                if (propertyName.endsWith(".")) {
                    throw new ConfigurationParserException("Incorrect key formatting.");
                }
                int dot = str[0].lastIndexOf(".");
                if (dot != -1) {
                    // find the configuration for the namespace, create new child if it doesn't exist
                    String[] names = str[0].substring(0, dot).split("[.]");
                    ConfigurationObject child = null;
                    for (int i = 0; i < names.length; ++i) {
                        child = findObj.getChild(names[i]);
                        if (child == null) {
                            child = new DefaultConfigurationObject(names[i]);
                            findObj.addChild(child);
                        }
                        findObj = child;
                    }
                    propertyName = str[0].substring(dot + 1);
                }
                // get old values
                Object[] oldValues = findObj.getPropertyValues(propertyName);
                // get new values
                String[] newValues = str[1].trim().split("[" + delimiter + "]");
                // trim all values.
                for (int i = 0; i < newValues.length; ++i) {
                    newValues[i] = newValues[i].trim();
                }
                // merge all values
                if (oldValues == null) {
                    findObj.setPropertyValues(propertyName, newValues);
                } else {
                    Object[] allValues = new Object[oldValues.length + newValues.length];
                    System.arraycopy(oldValues, 0, allValues, 0, oldValues.length);
                    System.arraycopy(newValues, 0, allValues, oldValues.length, newValues.length);
                    // set all values
                    findObj.setPropertyValues(propertyName, allValues);
                }
            }
        }
        return root;
    }

    /**
     * Get the list delimiter in the file.
     *
     * @param dataInput
     *            the data to read
     * @throws ConfigurationParserException
     *             if it has more than one list delimiter found in the file or any exception occurs in reading the file,
     *             or the delimiter is invalid
     * @throws IOException
     *             if an I/O problem occurred in reading from the specified file
     * @return list delimiter in the file, return ';' if it doesn't exist in the file
     */
    private String getListDelimiter(DataInput dataInput) throws ConfigurationParserException, IOException {
        String delimiter = null;
        for (String line = dataInput.readLine(); line != null; line = dataInput.readLine()) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            // get list delimiter
            if (line.indexOf('=') > 0
                && line.substring(0, line.indexOf('=')).trim().toLowerCase().equals(delimiterString)) {
                if (delimiter == null) {
                    delimiter = line.substring(line.indexOf('=') + 1).trim();
                    if (delimiter.length() > 1 || delimiter.equals(".") || delimiter.equals("=")) {
                        throw new ConfigurationParserException("Incorrect list delimiter in the file.");
                    }
                } else {
                    throw new ConfigurationParserException("The file can't contains two delimiters.");
                }
            }
        }
        return delimiter == null ? defaultDelimiter : delimiter;
    }
}
