/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;

/**
 * <p>
 * This is the central class of this component. It maintains an internal representation of the state
 * of configuration represented in any number of files when those files were last accessed, and it
 * keeps track of which configuration belongs to which file.
 * </p>
 * <p>
 * It relies on implementations of ConfigurationPersistence to actually read and write configuration
 * to files. Two implementations of this interface are included in this component, which read and
 * write files compatible with those defined in the ConfigurationFileManager component, but the user
 * may define additional configuration files without difficulty.
 * </p>
 * <p>
 * This class is mutable and not thread safe.
 * </p>
 *
 * @author bendlund, rainday
 * @version 1.0
 *
 */
public class ConfigurationFileManager {

    /**
     * <p>
     * Static final field specifying the default file path of the configuration of the
     * ConfigurationFileManager.
     * </p>
     *
     */
    public static final String DEFAULT_CONFIG_PATH = "com/topcoder/configuration/persistence/ConfigurationFileManager";

    /**
     * <p>
     * Static final field specifying the default file format of configuration for
     * ConfigurationFileManager.
     * </p>
     *
     */
    public static final String DEFAULT_CONFIG_FILE_TYPE = ".properties";

    /**
     * Used for thread synchronize.
     */
    private static Integer threadLock = new Integer(0);

    /**
     * the config persistence values' length in configuration object, there first value should be
     * the file extension and the second value will be the persistence object.
     */
    private static final int CONFIG_PERSISTENCE_VALUES_LENGTH = 2;

    /**
     * <p>
     * Maps supported file types to ConfigurationPersistence objects used to persist them.
     * </p>
     * <p>
     * Keys will be String objects, values will be ConfigurationPersistence objects. Null is not
     * permitted for either keys or values. Instantiated in the constructor neither the map nor it's
     * contents change.
     * </p>
     *
     */
    private final Map persistenceMap = new HashMap();

    /**
     * namespaceToFileMap maps each namespace to the file that contains it. Each key will be a
     * non-null, non-empty String representing a namespace and each value will be a File object.
     */
    private final Map namespaceToFileMap = new HashMap();

    /**
     * configurationMap contains the configuration maintained by this component. Each key is a
     * string representing a namespace, which may be empty but never null, and each value is a
     * non-null ConfigurationObject.
     */
    private final Map configurationMap = new HashMap();

    /**
     * <p>
     * Create the configuration file manager with the default config file.
     * </p>
     *
     * @throws IOException
     *             if a file I/O problem occurs
     * @throws ConfigurationParserException
     *             if any specified file could not be parsed by the associated
     *             ConfigurationPersistence
     * @throws UnrecognizedFileTypeException
     *             if the given file, or any file listed configured to be loaded, has a FileType for
     *             which no ConfigurationPersistence object is configured
     * @throws NamespaceConflictException
     *             if two or more files are associated with the same namespace
     *
     */
    public ConfigurationFileManager() throws IOException, ConfigurationParserException, UnrecognizedFileTypeException,
            NamespaceConflictException {
        this(DEFAULT_CONFIG_PATH + DEFAULT_CONFIG_FILE_TYPE);
    }

    /**
     * <p>
     * Instantiates and configures a ConfigurationFileManager object based on configuration read
     * from a file. The default persistence objects are used.
     * </p>
     *
     * @param file
     *            configuration file which contains namespaces and files to load
     *
     * @throws IllegalArgumentException
     *             if the file is null
     * @throws IOException
     *             if thrown by the ConfigurationPersistence object
     * @throws ConfigurationParserException
     *             if any specified file could not be parsed by the associated
     *             ConfigurationPersistence
     * @throws UnrecognizedFileTypeException
     *             if the given file, or any file listed configured to be loaded, has a FileType for
     *             which no ConfigurationPersistence object is configured
     * @throws NamespaceConflictException
     *             if two or more files are associated with the same namespace
     */
    public ConfigurationFileManager(String file) throws IOException, ConfigurationParserException,
            NamespaceConflictException, UnrecognizedFileTypeException {
        Helper.checkNotNullOrEmpty(file, "file");
        setDefaultPersistenceMap();
        loadConfigFile(file);
    }

    /**
     * <p>
     * Instantiates and configures a ConfigurationFileManager object based on configuration read
     * from a file. The default persistence objects are used.
     * </p>
     *
     * @param file
     *            configuration file which contains namespaces and files to load
     * @param persistenceMap
     *            Map having FileType keys and ConfigurationPersistence values
     *
     * @throws IllegalArgumentException
     *             if either argument is null, or if persistenceMap contains any nulls, any keys
     *             that aren't String objects, or any values that aren't ConfigurationPersistence
     *             objects, or if persistenceMap is empty
     * @throws IOException
     *             if thrown by the ConfigurationPersistence object
     * @throws ConfigurationParserException
     *             if any specified file could not be parsed by the associated
     *             ConfigurationPersistence
     * @throws UnrecognizedFileTypeException
     *             if the given file, or any file listed configured to be loaded, has a FileType for
     *             which no ConfigurationPersistence object is configured
     * @throws NamespaceConflictException
     *             if two or more files are associated with the same namespace
     */
    public ConfigurationFileManager(String file, Map persistenceMap) throws IOException, ConfigurationParserException,
            NamespaceConflictException, UnrecognizedFileTypeException {
        addConfigurationPersistence(persistenceMap);
        loadConfigFile(file);
    }

    /**
     * <p>
     * Configures this ConfigurationFileManager based on the configuration in the given
     * ConfigurationObject.The given ConfigurationObject should have a child ConfigurationObject
     * named "files" and optionally a second child ConfigurationObject named "persistence". The
     * "files" will have two values for each key: one String for namespace, and one String for file path.
     * The"persistence" ConfigurationObject, which should be processed first if it is present, will
     * have two values for each key: one String, and one ConfigurationPersistence. The keys don't
     * particularly matter, except that each is associated with exactly two values. Each
     * ConfigurationPersistence will be associated with the corresponding FileType in the
     * persistenceMap.
     * </p>
     *
     * @param config
     *            a ConfigurationObject containing a list of namespaces and the corresponding files
     *            and optionally a number of ConfigurationPersistence objects to associate with
     *            specified FileType objects
     *
     * @throws IllegalArgumentException
     *             if the given ConfigurationObject is null or doesn't contain a child named "files"
     * @throws IOException
     *             if thrown by the ConfigurationPersistence object
     * @throws ConfigurationParserException
     *             if any specified file could not be parsed by the associated
     *             ConfigurationPersistence
     * @throws UnrecognizedFileTypeException
     *             if any specified file has a FileType for which no ConfigurationPersistence object
     *             is configured
     * @throws NamespaceConflictException
     *             if two or more files are associated with the same namespace
     */
    public ConfigurationFileManager(ConfigurationObject config) throws IOException, ConfigurationParserException,
            NamespaceConflictException, UnrecognizedFileTypeException {
        Helper.checkNotNull(config, "config");
        try {
            ConfigurationObject persistenceObj = config.getChild("persistence");
            if (persistenceObj == null || persistenceObj.getAllPropertyKeys().length == 0) {
                setDefaultPersistenceMap();
            } else {
                String[] keys = persistenceObj.getAllPropertyKeys();
                for (int i = 0; i < keys.length; ++i) {
                    Object[] objs = persistenceObj.getPropertyValues(keys[i]);
                    if (objs.length < CONFIG_PERSISTENCE_VALUES_LENGTH) {
                        throw new IllegalArgumentException("Incorrect persistence values.");
                    }
                    if (!(objs[0] instanceof String)) {
                        throw new IllegalArgumentException("Incorrect extension key in persistence map.");
                    }
                    if (!(objs[1] instanceof ConfigurationPersistence)) {
                        throw new IllegalArgumentException(
                                "Incorrect ConfigurationPersistence type value in persistence map.");
                    }
                    persistenceMap.put(objs[0], objs[1]);
                }
            }
            ConfigurationObject filesObj = config.getChild("files");
            if (filesObj == null) {
                throw new IllegalArgumentException("config object must contain 'files' child");
            }
            // load all config files
            String[] files = filesObj.getAllPropertyKeys();
            for (int i = 0; i < files.length; ++i) {
                Object[] path = filesObj.getPropertyValues(files[i]);
                loadFile((String) path[0], (String) path[1]);
            }
        } catch (ConfigurationAccessException cae) {
            throw new ConfigurationParserException("Load config object error", cae);
        }
    }

    /**
     * <p>
     * Configures this ConfigurationFileManager from the given Map. The map should contain a number
     * of namespace keys mapped to File values.
     * </p>
     *
     * @param config
     *            a Map with namespace string keys and File values
     *
     * @throws IllegalArgumentException
     *             if the config is null, contains any nulls, has any non-string or empty string
     *             keys, or contains any non-File values
     * @throws IOException
     *             if thrown by the ConfigurationPersistence object
     * @throws ConfigurationParserException
     *             if any specified file could not be parsed by the associated
     *             ConfigurationPersistence
     * @throws UnrecognizedFileTypeException
     *             if any specified file has a FileType for which no ConfigurationPersistence object
     *             is configured
     * @throws NamespaceConflictException
     *             if two or more files are associated with the same namespace
     */
    public ConfigurationFileManager(Map config) throws IOException, ConfigurationParserException,
            NamespaceConflictException, UnrecognizedFileTypeException {
        setDefaultPersistenceMap();
        loadConfigFiles(config);
    }

    /**
     * <p>
     * Configures this ConfigurationFileManager from the given Maps. The first map should contain a
     * number of namespace keys mapped to File values. The second map will provide the
     * ConfigurationPersistence objects to use for any FileTypes
     * </p>
     *
     * @param config
     *            a Map with namespace string keys and File values
     * @param persistenceMap
     *            a Map having FileType keys and ConfigurationPersistence values
     *
     * @throws IllegalArgumentException
     *             if either argument is null, contains any nulls, or if the first argument has any
     *             non-string or empty string keys, or contains any non-File values, or if the
     *             second argument contains any nulls, or has any non-FileType keys or
     *             non-ConfigurationPersistence values, or if persistenceMap is empty
     * @throws IOException
     *             if thrown by the ConfigurationPersistence object
     * @throws ConfigurationParserException
     *             if any specified file could not be parsed by the associated
     *             ConfigurationPersistence
     * @throws UnrecognizedFileTypeException
     *             if any specified file has a FileType for which no
     *
     * @throws NamespaceConflictException
     *             if two or more files are associated with the same namespace
     */
    public ConfigurationFileManager(Map config, Map persistenceMap) throws IOException, ConfigurationParserException,
            NamespaceConflictException, UnrecognizedFileTypeException {
        addConfigurationPersistence(persistenceMap);
        loadConfigFiles(config);
    }

    /**
     * <p>
     * Get all of the configuration loaded into this manager. Returns a shallow copy of
     * configurationMap.
     * </p>
     *
     * @return a shallow copy of configurationMap
     *
     */
    public Map getConfiguration() {
        synchronized (threadLock) {
            return new HashMap(configurationMap);
        }
    }

    /**
     * <p>
     * Get a copy of the configuration loaded into this manager under the specified namespace.
     * </p>
     *
     * @param namespace
     *            a namespace specifying the subset of the loaded configuration to return
     * @return the Configuration object correspond to the namespace
     *
     * @throws IllegalArgumentException
     *             if the argument is empty or null
     * @throws UnrecognizedNamespaceException
     *             if the specified namespace isn't in the currentConfiguration
     */
    public ConfigurationObject getConfiguration(String namespace) throws UnrecognizedNamespaceException {
        Helper.checkNotNullOrEmpty(namespace, "namespace");
        synchronized (threadLock) {
            if (!configurationMap.containsKey(namespace)) {
                throw new UnrecognizedNamespaceException("Can't find namespace: " + namespace);
            }
            return (ConfigurationObject) ((ConfigurationObject) configurationMap.get(namespace)).clone();
        }
    }

    /**
     * <p>
     * Loads configuration from the specified file, and adds it to currentConfiguration in the
     * location specified by namespace.
     * </p>
     *
     * @param namespace
     *            namespace of current configuration object
     * @param file
     *            the configuration file read.  The file is sought first among the resources accessible to the context
     *            ClassLoader, and if not found there then the file system is checked.
     *
     * @throws IllegalArgumentException
     *             if either argument is null, or if namespace is empty
     * @throws IOException
     *             if thrown by the ConfigurationPersistence object
     * @throws ConfigurationParserException
     *             if any specified file could not be parsed by the associated
     *             ConfigurationPersistence
     * @throws UnrecognizedFileTypeException
     *             if the given file, or any file listed configured to be loaded, has a FileType for
     *             which no ConfigurationPersistence object is configured
     * @throws NamespaceConflictException
     *             if another file is already associated with the specified namespace
     */
    public void loadFile(String namespace, String file) throws IOException, ConfigurationParserException,
            UnrecognizedFileTypeException, NamespaceConflictException {
        Helper.checkNotNullOrEmpty(file, "file");
        synchronized (threadLock) {
            loadFile(namespace, getFile(file));
        }
    }

    /**
     * <p>
     * Creates a file containing the configuration specified by the given ConfigurationObject and
     * adds the given ConfigurationObject to the currentConfiguration in the location specified by
     * the namespace argument.
     * </p>
     * <p>
     * If the file or any containing directories doesn't exist, it will be created. If it does
     * exist, it will be overwritten.
     * </p>
     *
     * @param namespace
     *            the namespace of the file
     * @param file
     *            the file to write the configuration object
     * @param config
     *            configuration object to write
     *            
     * @throws IOException -
     *             if an I/O problem occurs while writing the file
     * @throws IllegalArgumentException -
     *             if any argument is null, or if namespace is empty
     * @throws NamespaceConflictException -
     *             if a file is already associated with the specified namespace
     * @throws UnrecognizedFileTypeException -
     *             if the given file, or any file listed configured to be loaded, has a FileType for
     *             which no ConfigurationPersistence object is configured
     * @throws ConfigurationParserException
     *             if any problem occur in retrieving value from the configuration object
     */
    public void createFile(String namespace, String file, ConfigurationObject config) throws IOException,
            NamespaceConflictException, UnrecognizedFileTypeException, ConfigurationParserException {
        Helper.checkNotNullOrEmpty(namespace, "namespace");
        Helper.checkNotNullOrEmpty(file, "file");
        Helper.checkNotNull(config, "config");
        synchronized (threadLock) {
            if (configurationMap.containsKey(namespace)) {
                throw new NamespaceConflictException("A file is already associated with " + namespace);
            }
            getPersistence(file).saveFile(new File(file), config);
            configurationMap.put(namespace, config.clone());
            namespaceToFileMap.put(namespace, new File(file));
        }
    }

    /**
     * <p>
     * Reloads all configuration, so that it is up to date with any recent changes to the underlying
     * files.
     * </p>
     *
     * @throws IOException
     *             if thrown by the ConfigurationPersistence object
     * @throws ConfigurationParserException
     *             if any specified file could not be parsed by the associated
     *             ConfigurationPersistence
     *
     */
    public void refresh() throws IOException, ConfigurationParserException {
        synchronized (threadLock) {
            try {
                Map files = new HashMap(namespaceToFileMap);
                namespaceToFileMap.clear();
                configurationMap.clear();
                String namespace = null;
                for (Iterator iter = files.keySet().iterator(); iter.hasNext();) {
                    namespace = (String) iter.next();
                    File file = (File) files.get(namespace);
                    loadFile(namespace, file);
                }
            } catch (NamespaceConflictException nce) {
                // there will be no conflict namespace
            } catch (UnrecognizedFileTypeException ufte) {
                // all file is recognized
            }
        }
    }

    /**
     * <p>
     * Reloads that part of the current configuration specified by the namespace argument, so that
     * it is up to date with any recent changes to the underlying files.
     * </p>
     *
     * @param namespace
     *            a namespace specifying some subset of the loaded configuration
     *
     * @throws IllegalArgumentException
     *             if namespace is null or empty
     * @throws UnrecognizedNamespaceException
     *             if the given namespace isn't part of the current configuration
     * @throws IOException
     *             if thrown by the ConfigurationPersistence object
     * @throws ConfigurationParserException
     *             if any specified file could not be parsed by the associated
     *             ConfigurationPersistence
     */
    public void refresh(String namespace) throws IOException, UnrecognizedNamespaceException,
            ConfigurationParserException {
        Helper.checkNotNullOrEmpty(namespace, "namespace");
        synchronized (threadLock) {
            if (!configurationMap.containsKey(namespace)) {
                throw new UnrecognizedNamespaceException("Can't find namespace " + namespace);
            }
            File file = (File) namespaceToFileMap.get(namespace);
            configurationMap.remove(namespace);
            namespaceToFileMap.remove(namespace);
            try {
                loadFile(namespace, file);
            } catch (UnrecognizedFileTypeException ufte) {
                // never throw here
            } catch (NamespaceConflictException nce) {
                // never throw here
            }
        }
    }

    /**
     * <p>
     * Updates the current configuration both in this object and in the underlying files to be in
     * synch with the given Map. The given object represents the entire configuration loaded into
     * the manager - any missing properties or namespaces will be removed. Properties may be added
     * to existing ConfigurationObjects, but no additional ConfigurationObjects may be added that
     * can't be associated with an already loaded file.
     * </p>
     *
     * @param configMap
     *            new configuration map
     *            
     * @throws IllegalArgumentException
     *             if the given configurationMap is null or contains any null or empty keys or any
     *             null values, or any non-String keys or any non-ConfigurationObject values
     * @throws InvalidConfigurationUpdateException
     *             if the given Map contains any namespaces that are not in the configurationMap
     *             field
     * @throws ConfigurationUpdateConflictException
     *             if any file represented in this configuration has changed since it was last
     *             loaded or refreshed - a changed file will not be overwritten
     * @throws IOException -
     *             if an I/O problem occurs while writing the file
     * @throws ConfigurationParserException
     *             if there's a problem reading a ConfigurationObject or creating a
     *             ConfigurationObject from a file
     */
    public void saveConfiguration(Map configMap) throws InvalidConfigurationUpdateException,
            ConfigurationParserException, ConfigurationUpdateConflictException, IOException {
        Helper.checkNotNull(configMap, "config map");
        synchronized (threadLock) {
            for (Iterator iter = configMap.keySet().iterator(); iter.hasNext();) {
                Object namespace = iter.next();
                if (!(namespace instanceof String)) {
                    throw new IllegalArgumentException("The key of the configMap should be string type.");
                }
                Object configObj = configMap.get(namespace);
                if (!(configObj instanceof ConfigurationObject)) {
                    throw new IllegalArgumentException("The value of the configMap must be ConfigurationObject type.");
                }
                saveConfiguration((String) namespace, (ConfigurationObject) configObj);
            }
        }
    }

    /**
     * <p>
     * Updates the current configuration both in this object and in the underlying files to be in
     * synch with the given ConfigurationObject. The given object represents a subtree of the
     * configuration loaded into the manager - any missing properties or namespaces in the
     * represented part will be removed. Properties may be added to existing ConfigurationObjects,
     * but no additional ConfigurationObjects may be added that can't be associated with an already
     * loaded file.
     * </p>
     *
     * @param namespace
     *            the namespace configuration to save
     * @param config
     *            the new configuration object
     *            
     * @throws IllegalArgumentException
     *             if the given configurationMap is null or contains any null or empty keys or any
     *             null values, or any non-String keys or any non-ConfigurationObject values, if the
     *             given config is null, or if the namespace argument is null or empty
     * @throws InvalidConfigurationUpdateException
     *             if the given Map contains any namespaces that are not in the configurationMap
     *             field
     * @throws ConfigurationUpdateConflictException
     *             if any file represented in this configuration has changed since it was last
     *             loaded or refreshed - a changed file will not be overwritten
     * @throws IOException
     *             if an I/O problem occurs while writing the file
     * @throws ConfigurationParserException
     *             if there's a problem reading a ConfigurationObject or creating a
     *             ConfigurationObject from a file
     */
    public void saveConfiguration(String namespace, ConfigurationObject config)
        throws InvalidConfigurationUpdateException, ConfigurationParserException,
        ConfigurationUpdateConflictException, IOException {
        Helper.checkNotNullOrEmpty(namespace, "namespace");
        Helper.checkNotNull(config, "config object");
        if (!configurationMap.containsKey(namespace)) {
            throw new InvalidConfigurationUpdateException("Can't find namespace in configuration map");
        }

        synchronized (threadLock) {
            try {
                File file = (File) namespaceToFileMap.get(namespace);
                ConfigurationPersistence persistence = getPersistence(file.getName());
                ConfigurationObject newLoad = persistence.loadFile(namespace, file);
                if (!checkConfigObjects((ConfigurationObject) configurationMap.get(namespace), newLoad)) {
                    throw new ConfigurationUpdateConflictException("Current configuration is out of date");
                }
                persistence.saveFile(file, config);
                configurationMap.put(namespace, config.clone());
            } catch (UnrecognizedFileTypeException ufte) {
                // never throw
            }
        }
    }

    /**
     * Load a filename with a URI. This method searches the class path for the specific file.
     *
     * @param filename
     *            the filename to load.
     * @return the uri for the file.
     * @throws IOException
     *             if the file can not be loaded.
     */
    private File getFile(String filename) throws IOException {
        File file = new File(filename);
        
        if ((Thread.currentThread().getContextClassLoader().getResource(filename) == null) 
                && !file.exists()) {
            throw new IOException("can not locate " + filename);
        } else {
            return file;
        }
    }

    /**
     * <p>
     * Loads configuration from the specified file, and adds it to configurationMap and
     * namespaceToFileMap.
     * </p>
     *
     * @param namespace
     *            namespace for the load file
     * @param file
     *            the file to load
     *
     * @throws IOException
     *             if thrown by the ConfigurationPersistence object
     * @throws ConfigurationParserException
     *             if any specified file could not be parsed by the associated
     *             ConfigurationPersistence
     * @throws UnrecognizedFileTypeException
     *             if the given file, or any file listed configured to be loaded, has a FileType for
     *             which no ConfigurationPersistence object is configured
     * @throws NamespaceConflictException
     *             if another file is already associated with the specified namespace
     */
    private void loadFile(String namespace, File file) throws IOException, ConfigurationParserException,
            UnrecognizedFileTypeException, NamespaceConflictException {
        if (namespaceToFileMap.containsKey(namespace)) {
            throw new NamespaceConflictException("Namespace :" + namespace + " already exists.");
        }
        ConfigurationObject newObj = getPersistence(file.getName()).loadFile(namespace, file);
        configurationMap.put(namespace, newObj);
        // trace the namespace and its file
        namespaceToFileMap.put(namespace, file);
    }

    /**
     * Get the persistence by the file's extension.
     *
     * @param fileName
     *            the file name
     * @throws UnrecognizedFileTypeException
     *             if no persistence is found
     * @return the persistence correspond to the extension of the file name
     */
    private ConfigurationPersistence getPersistence(String fileName) throws UnrecognizedFileTypeException {
        int dot = fileName.lastIndexOf(".");
        if (dot == -1) {
            throw new UnrecognizedFileTypeException("Can't recognize file " + fileName, "");
        }
        String type = fileName.substring(dot);
        if (!persistenceMap.containsKey(type)) {
            throw new UnrecognizedFileTypeException("Can't find persistence for file " + fileName, type);
        }
        return (ConfigurationPersistence) persistenceMap.get(type);
    }

    /**
     * <p>
     * Load the config properties for this component.
     * </p>
     *
     * @param fileName
     *            the properties file to load
     *            
     * @throws IllegalArgumentException
     *             if the file is null
     * @throws IOException
     *             if thrown by the ConfigurationPersistence object
     * @throws ConfigurationParserException
     *             if any specified file could not be parsed by the associated
     *             ConfigurationPersistence
     * @throws UnrecognizedFileTypeException
     *             if the given file, or any file listed configured to be loaded, has a FileType for
     *             which no ConfigurationPersistence object is configured
     * @throws NamespaceConflictException
     *             if two or more files are associated with the same namespace
     */
    private void loadConfigFile(String fileName) throws IOException, ConfigurationParserException,
            NamespaceConflictException, UnrecognizedFileTypeException {
        Helper.checkNotNullOrEmpty(fileName, "config file name");
        ConfigurationObject files = getPersistence(fileName).loadFile("config", getFile(fileName));
        // process all pre-load files
        List list = new LinkedList();
        try {
            String[] childrenNames = files.getAllChildrenNames();
            for (int i = 0; i < childrenNames.length; ++i) {
                // root property
                list.add("");
                list.add(files.getChild(childrenNames[i]));
            }
            // bfs process, restore namespace for each value,
            // in list , each namespace and correspond configuration object
            while (!list.isEmpty()) {
                String namespace = (String) list.remove(0);
                ConfigurationObject configObj = (ConfigurationObject) list.remove(0);
                // read all properties' value as config file
                String[] propertyKeys = configObj.getAllPropertyKeys();
                for (int i = 0; i < propertyKeys.length; ++i) {
                    if (configObj.getPropertyValues(propertyKeys[i]).length > 1) {
                        throw new NamespaceConflictException("Every namespace should be in a single file.");
                    }
                    if (namespace.equals("")) {
                        loadFile(propertyKeys[i], (String) configObj.getPropertyValue(propertyKeys[i]));
                    } else {
                        loadFile(namespace + "." + propertyKeys[i], (String) configObj
                                .getPropertyValue(propertyKeys[i]));
                    }
                }
                // push sub properties
                childrenNames = configObj.getAllChildrenNames();
                for (int i = 0; i < childrenNames.length; ++i) {
                    if (namespace.equals("")) {
                        list.add(childrenNames[i]);
                    } else {
                        list.add(namespace + "." + childrenNames[i]);
                    }
                    list.add(configObj.getChild(childrenNames[i]));
                }
            }
        } catch (ConfigurationAccessException cae) {
            throw new ConfigurationParserException("Load config file incorrectly.", cae);
        }
    }

    /**
     * <p>
     * Load the config properties for this component.
     * </p>
     *
     * @param config
     *            a Map with namespace string keys and File values
     *            
     * @throws IllegalArgumentException
     *             if the config is null, contains any nulls, has any non-string or empty string
     *             keys, or contains any non-File values
     * @throws IOException
     *             if thrown by the ConfigurationPersistence object
     * @throws ConfigurationParserException
     *             if any specified file could not be parsed by the associated
     *             ConfigurationPersistence
     * @throws UnrecognizedFileTypeException
     *             if the given file, or any file listed configured to be loaded, has a FileType for
     *             which no ConfigurationPersistence object is configured
     * @throws NamespaceConflictException
     *             if two or more files are associated with the same namespace
     */
    private void loadConfigFiles(Map config) throws IOException, ConfigurationParserException,
            NamespaceConflictException, UnrecognizedFileTypeException {
        Helper.checkNotNull(config, "config file map");
        for (Iterator iter = config.keySet().iterator(); iter.hasNext();) {
            Object namespace = iter.next();
            Object file = config.get(namespace);
            if (namespace instanceof String) {
                Helper.checkNotNullOrEmpty((String) namespace, "namespace");
            } else {
                throw new IllegalArgumentException("The key in config must be String type.");
            }

            if (!(file instanceof File)) {
                throw new IllegalArgumentException("The values in config must be File type.");
            }
            loadFile((String) namespace, (File) file);
        }
    }

    /**
     * Add xml and properties files persistence object to the map.
     *
     */
    private void setDefaultPersistenceMap() {
        persistenceMap.put(".xml", new XMLFilePersistence());
        persistenceMap.put(".properties", new PropertyFilePersistence());
    }

    /**
     * <p>
     * Add new ConfigurationPersistences in the passed map to this.persistenceMap.
     * </p>
     *
     * @param persistenceMap
     *            Map having FileType keys and ConfigurationPersistence values
     *            
     * @throws IllegalArgumentException
     *             if argument is null, or if persistenceMap contains any nulls, any keys that
     *             aren't string objects, or any values that aren't ConfigurationPersistence
     *             objects, or if persistenceMap is empty
     */
    private void addConfigurationPersistence(Map persistenceMap) {
        Helper.checkNotNull(persistenceMap, "persistenceMap");
        if (persistenceMap.isEmpty()) {
            throw new IllegalArgumentException("persistenceMap can't be empty.");
        }
        for (Iterator iter = persistenceMap.keySet().iterator(); iter.hasNext();) {
            Object type = iter.next();
            if (!(type instanceof String)) {
                throw new IllegalArgumentException("Incorrect extension key in persistence map.");
            }
            Object configurationPersistence = persistenceMap.get(type);
            if (!(configurationPersistence instanceof ConfigurationPersistence)) {
                throw new IllegalArgumentException("Incorrect ConfigurationPersistence type value in persistence map.");
            }
            this.persistenceMap.put(type, configurationPersistence);
        }
    }

    /**
     * <p>
     * Compare current configuration object with a new loading configuration object from its file.
     * </p>
     *
     * @param current
     *            internal configuration object to compare
     * @param newLoad
     *            new loading configuration object to compare
     * @return true no property is changed, otherwise false
     */
    private boolean checkConfigObjects(ConfigurationObject current, ConfigurationObject newLoad) {
        try {
            // compare name at first
            if (!current.getName().equals(newLoad.getName())) {
                return false;
            }
            // compare properties
            String[] propertyKeys = current.getAllPropertyKeys();
            Object[] currentValues = null;
            Object[] newValues = null;
            for (int i = 0; i < propertyKeys.length; ++i) {
                // compare all values for each properties
                currentValues = current.getPropertyValues(propertyKeys[i]);
                newValues = current.getPropertyValues(propertyKeys[i]);
                if (currentValues.length != newValues.length) {
                    return false;
                } else {
                    for (int k = 0; k < currentValues.length; ++k) {
                        if (!currentValues[k].equals(newValues[k])) {
                            return false;
                        }
                    }
                }
            }
            // compare children
            String[] childrenNames = current.getAllChildrenNames();
            if (childrenNames.length != newLoad.getAllChildrenNames().length) {
                return false;
            }
            for (int i = 0; i < childrenNames.length; ++i) {
                // if non-exist, return false
                if (newLoad.getChild(childrenNames[i]) == null) {
                    return false;
                } else if (!checkConfigObjects(current.getChild(childrenNames[i]),
                        newLoad.getChild(childrenNames[i]))) {
                    return false;
                }
            }
            return true;
        } catch (ConfigurationAccessException cae) {
            // ignore
        }
        return false;
    }
}
