/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.InvalidConfigurationException;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

/**
 * <p>
 * This implementation of ConfigurationPersistence may be used to save and read configuration from xml files of a format
 * defined by the included Configuration_Manager_Schema.xsd, which is copied directly from the TopCoder Configuration
 * Manager component. The functionality encapsulated in this class is very similar to functionality provided in the
 * XMLConfigProperties class from the Configuration Manager component.
 * </p>
 * An example xml config file:
 *
 * <pre>
 * &lt;?xml version=&quot;1.0&quot;?&gt;
 * &lt;CMConfig&gt;
 *     &lt;Config name=&quot;com.topcoder.yyy&quot;&gt;
 *         &lt;property name=&quot;h&quot;&gt;
 *             &lt;value&gt;valueh&lt;/value&gt;
 *         &lt;/property&gt;
 *         &lt;property name=&quot;i&quot;&gt;
 *             &lt;property name=&quot;j&quot;&gt;
 *                 &lt;value&gt;valuej&lt;/value&gt;
 *             &lt;/property&gt;
 *         &lt;/property&gt;
 *     &lt;/Config&gt;
 *     &lt;property name=&quot;a&quot;&gt;
 *         &lt;value&gt;valuea&lt;/value&gt;
 *     &lt;/property&gt;
 *     &lt;property name=&quot;b&quot;&gt;
 *         &lt;value&gt;valueb&lt;/value&gt;
 *         &lt;property name=&quot;c&quot;&gt;
 *             &lt;value&gt;valuec&lt;/value&gt;
 *         &lt;/property&gt;
 *     &lt;/property&gt;
 *     &lt;Config name=&quot;com.topcoder.xxx&quot;&gt;
 *         &lt;property name=&quot;f&quot;&gt;
 *             &lt;value&gt;valuef&lt;/value&gt;
 *             &lt;property name=&quot;g&quot;&gt;
 *                 &lt;value&gt;valueg&lt;/value&gt;
 *             &lt;/property&gt;
 *         &lt;/property&gt;
 *         &lt;property name=&quot;e&quot;&gt;
 *             &lt;value&gt;valuee1&lt;/value&gt;
 *         &lt;/property&gt;
 *         &lt;property name=&quot;m&quot;&gt;
 *             &lt;property name=&quot;n&quot;&gt;
 *                 &lt;value&gt;valuen1&lt;/value&gt;
 *             &lt;/property&gt;
 *         &lt;/property&gt;
 *         &lt;property name=&quot;m&quot;&gt;
 *             &lt;property name=&quot;n&quot;&gt;
 *                 &lt;value&gt;valuen2&lt;/value&gt;
 *             &lt;/property&gt;
 *         &lt;/property&gt;
 *     &lt;/Config&gt;
 *     &lt;Config name=&quot;com.topcoder.xxx&quot;&gt;
 *         &lt;property name=&quot;e&quot;&gt;
 *             &lt;value&gt;valuee2&lt;/value&gt;
 *         &lt;/property&gt;
 *     &lt;/Config&gt;
 * &lt;/CMConfig&gt;
 * </pre>
 *
 * <p>
 * This class is stateless and thread safe.
 * </p>
 *
 * @author bendlund, rainday
 * @version 1.0
 */
public class XMLFilePersistence implements ConfigurationPersistence {
    /** An StringBuffer used to save all output the properties. */
    private StringBuffer content = null;

    /** The property node name in the xml file. */
    private final String namespaceNodeName = "config";

    /** Default namespace name in the configuration file. */
    private final String defaultNamespace = "default";

    /** The name attribute name in the xml file. */
    private final String nameAttribute = "name";

    /** The property node name in the xml file. */
    private final String propertyNodeName = "property";

    /** The value node name in the xml file. */
    private final String valueNodeName = "value";

    /**
     * <p>
     * Default constructor, do nothing.
     * </p>
     */
    public XMLFilePersistence() {
        // empty
    }

    /**
     * <p>
     * Saves the given ConfigurationObject to the specified xml file. Any data currently in the specified file will be
     * overwritten. for all children of the configuration object, if it has the name of 'default', save all propeties
     * into the xml file directly, otherwise, create a &lt;Config&rt; node, and add the name of the child as an
     * attribute for this node.
     * </p>
     *
     * @param file
     *            an abstract path name for the configuration file to update.  The file is sought first among the
     *            resources accessible to the context ClassLoader, and if not found there then the file system is
     *            checked.  Saving will fail if the file is not writable; in particular, it will fail if the file is
     *            inside an archive file.
     * @param config
     *            a ConfigurationObject containing string properties that can be written to a xml file
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

        RandomAccessFile outputFile = null;
        FileLock locker = null;

        try {
            outputFile = new RandomAccessFile(file, "rw");
            locker = outputFile.getChannel().tryLock();

            if (locker == null) {
                throw new IOException("Can't access the file.");
            }

            content = new StringBuffer();
            content.append("<?xml version=\"1.0\"?>");
            content.append(Helper.RETURN_STRING);
            content.append("<CMConfig>");
            content.append(Helper.RETURN_STRING);

            try {
                // write default namespace's properties
                String[] childrenNames = config.getAllChildrenNames();

                for (int i = 0; i < childrenNames.length; ++i) {
                    if (childrenNames[i].equals(defaultNamespace)) {
                        writeProperty(config.getChild(childrenNames[i]), 1);
                    } else {
                        content.append("    <Config name=\"" + childrenNames[i] + "\">");
                        content.append(Helper.RETURN_STRING);
                        writeProperty(config.getChild(childrenNames[i]), 2);
                        content.append("    </Config>");
                        content.append(Helper.RETURN_STRING);
                    }
                }
            } catch (ConfigurationAccessException cae) {
                throw new ConfigurationParserException("Access config object error.", cae);
            }

            content.append("</CMConfig>");
            outputFile.setLength(0);
            outputFile.write(new String(content).getBytes("UTF-8"));
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
     *
     * @return the generated ConfigurationObject for the xml file
     *
     * @throws IOException
     *             if an I/O problem occurred in reading from the specified file
     * @throws IllegalArgumentException
     *             if name is empty or null, or file is null
     * @throws ConfigurationParserException
     *             - the file could not be parsed by this ConfigurationPersistence implementation
     */
    public ConfigurationObject loadFile(String name, File file) throws IOException, ConfigurationParserException {
        Helper.checkNotNull(file, "file");
        Helper.checkNotNullOrEmpty(name, "name");

        // create DOM parser and parse
        String resourcePath = Helper.changeSeparator(file.getPath()); 
        URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource(resourcePath);
        InputStream inputStream; 
        Document document;

        // Obtain a lock if possible, and open an input stream
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
                inputStream = new BufferedInputStream(resourceUrl.openStream());
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
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            document = builder.parse(inputStream);
        } catch (FactoryConfigurationError fce) {
            throw new ConfigurationParserException("Error occurred obtaining Document Builder Factory", fce);
        } catch (ParserConfigurationException pce) {
            throw new ConfigurationParserException("The underlying parser doesn't support the requested features", pce);
        } catch (SAXException saxe) {
            throw new ConfigurationParserException("Load xml file error", saxe);
        } finally {
            inputStream.close();
        }

        return readConfigurationObject(name, document);
    }

    /**
     * <p>
     * Returns a ConfigurationObject with the specified name containing the configuration stored in the specified xml
     * document.
     * </p>
     *
     * @param name
     *            name of the ConfigurationObject to return
     * @param document
     *            a xml document to load configuration from
     * @return the generated ConfigurationObject for the resource
     * 
     * @throws ConfigurationParserException
     *             the file could not be parsed by this ConfigurationPersistence implementation
     * @since BUGR-1460
     */
    private ConfigurationObject readConfigurationObject(String name, Document document)
                    throws ConfigurationParserException {
        ConfigurationObject root = new DefaultConfigurationObject(name);
        ConfigurationObject defaultObj = new DefaultConfigurationObject(defaultNamespace);

        try {
            // create list to save xml node and corresponding configuration object in the bfs
            // process
            Node node = document.getDocumentElement();
            List list = getNamespaceNodes(node, root);
            list.add(node);
            list.add(defaultObj);

            ConfigurationObject configObj = null;

            while (!list.isEmpty()) {
                node = (Node) list.remove(0);
                configObj = (ConfigurationObject) list.remove(0);

                NodeList subNodes = node.getChildNodes();

                for (int i = 0; i < subNodes.getLength(); ++i) {
                    Node subNode = subNodes.item(i);

                    // ignore property element node
                    if ((subNode.getNodeType() == Node.ELEMENT_NODE)
                        && subNode.getNodeName().toLowerCase().equals(propertyNodeName)) {
                        // get the property name
                        Node keyNode = subNode.getAttributes().getNamedItem(nameAttribute);

                        if ((keyNode == null) || (keyNode.getNodeValue().trim().length() == 0)) {
                            throw new ConfigurationParserException("The property name can't be null or empty.");
                        }

                        String propertyName = keyNode.getNodeValue().trim();

                        if (hasSubProperty(subNode)) {
                            // push the child config object to the list
                            ConfigurationObject obj = null;

                            // Config elements with duplicate names should also be combined, as a
                            // result, check the configuration first
                            if (configObj.getChild(propertyName) != null) {
                                obj = configObj.getChild(propertyName);
                            } else {
                                obj = new DefaultConfigurationObject(propertyName);
                                configObj.addChild(obj);
                            }

                            list.add(subNode);
                            list.add(obj);
                        }

                        // save property-value pair
                        Collection values = new ArrayList();

                        // save old values
                        Object[] oldValues = configObj.getPropertyValues(propertyName);

                        if (oldValues != null) {
                            for (int j = 0; j < oldValues.length; ++j) {
                                values.add(oldValues[j]);
                            }
                        }

                        NodeList valueNodes = subNode.getChildNodes();

                        for (int j = 0; j < valueNodes.getLength(); ++j) {
                            Node valueNode = valueNodes.item(j);

                            if ((valueNode.getNodeType() == Node.ELEMENT_NODE)
                                && valueNode.getNodeName().toLowerCase().equals(valueNodeName)) {
                                Node childNode = valueNode.getFirstChild();

                                if (childNode == null) {
                                    values.add("");
                                } else {
                                    values.add(valueNode.getFirstChild().getNodeValue());
                                }
                            }
                        }

                        if (values.size() > 0) {
                            configObj.setPropertyValues(propertyName, values.toArray());
                        }
                    }
                }
            }

            // if there is some properties in default namespace, add it to the root
            if ((defaultObj.getAllPropertyKeys().length > 0) || (defaultObj.getAllChildrenNames().length > 0)) {
                root.addChild(defaultObj);
            }
        } catch (ConfigurationAccessException cae) {
            throw new ConfigurationParserException("Access xml file error", cae);
        } catch (InvalidConfigurationException ice) {
            throw new ConfigurationParserException("Load xml file error", ice);
        }

        return root;
    }

    /**
     * Write ConfigurationObject tree to xml file.
     *
     * @param configObj
     *            ConfigurationObject object to write
     * @param indent
     *            indentation
     *
     * @throws IOException
     *             if any problem when write property file
     * @throws ConfigurationParserException
     *             if any problem when access the configuration object
     */
    private void writeProperty(ConfigurationObject configObj, int indent) throws IOException,
                    ConfigurationParserException {
        String indentStr = "";

        for (int i = 0; i < indent; ++i) {
            indentStr += "    ";
        }

        try {
            // write all properties
            String[] propertyKeys = configObj.getAllPropertyKeys();

            // the children and property may have the same name, they should be written once
            Map share = new HashMap();

            for (int i = 0; i < propertyKeys.length; ++i) {
                // write property name
                content.append(indentStr + "<property name=\"" + propertyKeys[i] + "\">");
                content.append(Helper.RETURN_STRING);

                Object[] values = configObj.getPropertyValues(propertyKeys[i]);

                // write all property values
                for (int j = 0; j < values.length; ++j) {
                    content.append(indentStr + "    <value>" + values[j] + "</value>");
                    content.append(Helper.RETURN_STRING);
                }

                if (configObj.getChild(propertyKeys[i]) != null) {
                    share.put(propertyKeys[i], propertyKeys[i]);
                    writeProperty(configObj.getChild(propertyKeys[i]), indent + 1);
                }

                content.append(indentStr + "</property>");
                content.append(Helper.RETURN_STRING);
            }

            // write sub properties
            String[] childrenNames = configObj.getAllChildrenNames();

            for (int i = 0; i < childrenNames.length; ++i) {
                if (share.containsKey(childrenNames[i])) {
                    continue;
                }

                content.append(indentStr + "<property name=\"" + childrenNames[i] + "\">");
                content.append(Helper.RETURN_STRING);
                writeProperty(configObj.getChild(childrenNames[i]), indent + 1);
                content.append(indentStr + "</property>");
                content.append(Helper.RETURN_STRING);
            }
        } catch (ConfigurationAccessException cae) {
            throw new ConfigurationParserException("Write property file error.", cae);
        }
    }

    /**
     * check if the node is has sub-property node.
     *
     * @param node
     *            the node to check
     *
     * @return ture if one of its children contains 'property' sub-node, false otherwise
     */
    private boolean hasSubProperty(Node node) {
        NodeList subNodes = node.getChildNodes();

        for (int i = 0; i < subNodes.getLength(); ++i) {
            Node subNode = subNodes.item(i);

            if ((subNode.getNodeType() == Node.ELEMENT_NODE)
                && subNode.getNodeName().toLowerCase().endsWith("property")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the namespace nodes in the xml file.
     *
     * @param rootNode
     *            the xml config file's root node
     * @param rootObj
     *            the root configuration object for the xml file
     *
     * @return all namespace nodes in the xml file.
     *
     * @throws ConfigurationParserException
     *             if the name of the namespace doesn't exist or null/empty.
     */
    private List getNamespaceNodes(Node rootNode, ConfigurationObject rootObj) throws ConfigurationParserException {
        List list = new LinkedList();

        // keep track all namespace, there may be some same namespaces in the xml config file
        // namespace as key and its configuration object as value
        Map namespaces = new HashMap();

        try {
            // parse root node's children, add config(namespace) node to the list
            for (int i = 0; i < rootNode.getChildNodes().getLength(); ++i) {
                Node subNode = rootNode.getChildNodes().item(i);

                if ((subNode.getNodeType() == Node.ELEMENT_NODE)
                    && subNode.getNodeName().toLowerCase().equals(namespaceNodeName)) {
                    Node nameNode = subNode.getAttributes().getNamedItem(nameAttribute);

                    if ((nameNode == null) || (nameNode.getNodeValue().trim().length() == 0)) {
                        throw new ConfigurationParserException("Incorrect namespace for Config node.");
                    }

                    String namespace = nameNode.getNodeValue().trim();
                    ConfigurationObject obj = null;

                    if (namespaces.containsKey(namespace)) {
                        obj = (ConfigurationObject) namespaces.get(namespace);
                    } else {
                        obj = new DefaultConfigurationObject(namespace);
                        namespaces.put(namespace, obj);
                        rootObj.addChild(obj);
                    }

                    list.add(subNode);
                    list.add(obj);
                }
            }
        } catch (ConfigurationAccessException cae) {
            throw new ConfigurationParserException("Access xml file error", cae);
        } catch (InvalidConfigurationException ice) {
            throw new ConfigurationParserException("Load xml file error", ice);
        }

        return list;
    }
}
