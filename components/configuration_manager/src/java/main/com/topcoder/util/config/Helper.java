/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <p>
 * This is helper utility class used by this component. It defines methods that are called by multiple classes from
 * the component.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is immutable and thread safe.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 2.2
 * @since 2.2
 */
class Helper {
    /**
     * <p>
     * Represents the default list delimiter.
     * </p>
     */
    static final char DEFAULT_LIST_DELIMITER = ';';

    /**
     * <p>
     * Represents the key 'IsRefreshable'.
     * </p>
     */
    static final String KEY_REFRESHABLE = "IsRefreshable";

    /**
     * <p>
     * Length of the unicode string.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> this constant was moved from PropConfigProperties.
     * </p>
     */
    private static final int UNICODE_LENGTH = 4;

    /**
     * <p>
     * The base number for 'a' in a hex number.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> this constant was moved from PropConfigProperties.
     * </p>
     */
    private static final int HEX_LETTER_BASE = 10;

    /**
     * <p>
     * The number of bits that every hex number represents.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> this constant was moved from PropConfigProperties.
     * </p>
     */
    private static final int HEX_BIT_COUNT = 4;

    /**
     * <p>
     * Empty private constructor.
     * </p>
     */
    private Helper() {
        // Empty
    }

    /**
     * <p>
     * Closes the Closeable object.
     * </p>
     *
     * @param obj
     *            the Closeable object.
     */
    static void closeObj(Closeable obj) {
        if (obj != null) {
            try {
                // Close the object
                obj.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    /**
     * <p>
     * Loads properties from the url.
     * </p>
     *
     * @param url
     *            the url.
     *
     * @return the properties.
     *
     * @throws ConfigManagerException
     *             if any error occurs.
     *
     * @since 2.2
     */
    static Properties loadProperties(URL url) throws ConfigManagerException {
        Properties properties = new Properties();
        InputStream is = null;
        try {
            is = url.openStream();

            properties.load(is);
        } catch (IOException e) {
            throw new ConfigManagerException("Can not load config file: " + e.getMessage());
        } finally {
            // Close the input stream
            Helper.closeObj(is);
        }
        return properties;
    }

    /**
     * <p>
     * Creates an instance of class retrieved from the properties.
     * </p>
     *
     * @param <T>
     *            the target type.
     * @param targetClass
     *            the target type class.
     * @param properties
     *            the properties.
     * @param key
     *            the property key to retrieve the class name.
     *
     * @return the created instance.
     *
     * @throws ConfigManagerException
     *             if any error occurs.
     *
     * @since 2.2
     */
    @SuppressWarnings("unchecked")
    static <T> T createObj(Class<T> targetClass, Properties properties, String key) throws ConfigManagerException {
        // get class name
        String className = properties.getProperty(key);
        if (className == null) {
            throw new ConfigManagerException("The property '" + key + "' is missing.");
        }
        if (className.trim().length() == 0) {
            throw new ConfigManagerException("The property '" + key + "' is empty.");
        }

        try {
            Class<?> objClass = Class.forName(className);

            if (!targetClass.isAssignableFrom(objClass)) {
                throw new ConfigManagerException("The class '" + className + "' is not of type '"
                    + targetClass.getName() + "'.");
            }
            // Instantiate the class reflectively:
            return (T) objClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new ConfigManagerException("The class '" + className + "' cannot be found : " + e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new ConfigManagerException("The constructor cannot be found : " + e.getMessage());
        } catch (InstantiationException e) {
            throw new ConfigManagerException("The class is an abstract class : " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new ConfigManagerException("The constructor is inaccessible : " + e.getMessage());
        } catch (InvocationTargetException e) {
            throw new ConfigManagerException("Failed to create an object : " + e.getMessage());
        } catch (LinkageError e) {
            throw new ConfigManagerException("Linkage failed : " + e.getMessage());
        } catch (SecurityException e) {
            throw new ConfigManagerException("A security error occurred. : " + e.getMessage());
        }
    }

    /**
     * <p>
     * Decodes the url file into valid file name.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> This method was moved from ConfigManager.
     * </p>
     *
     * @param url
     *            the url to decode.
     *
     * @return the decoded file name.
     *
     * @throws ConfigManagerException
     *             if the url's format is invalid.
     */
    static String decodeURL(String url) throws ConfigManagerException {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            throw new ConfigManagerException("The URL is not encoded with UTF-8.");
        }
    }

    /**
     * <p>
     * Gets the Document object.
     * </p>
     *
     * @param url
     *            the url.
     *
     * @return the Document object.
     *
     * @throws ConfigParserException
     *             for badly formed files.
     */
    static Document getDocument(URL url) throws ConfigParserException {
        // create DOM parser and parse
        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
        } catch (FactoryConfigurationError e) {
            throw new ConfigParserException("Error occurred obtaining Document Builder Factory: " + e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new ConfigParserException("The underlying parser does not support the requested features: "
                + e.getMessage());
        }

        InputStream is = null;
        try {
            is = url.openStream();

            return builder.parse(new InputSource(is));
        } catch (SAXException saxe) {
            throw new ConfigParserException(saxe.getMessage());
        } catch (IOException ioe) {
            throw new ConfigParserException(ioe.getMessage());
        } finally {
            // Close the input stream
            Helper.closeObj(is);
        }
    }

    /**
     * <p>
     * Gets the list of namespaces defined in a multi-namespace file.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> This method was moved from XMLConfigProperties. Added a generic parameter to the return type.
     * </p>
     *
     * @param url
     *            the url to get namespaces from.
     *
     * @return all namespaces stored in filename.
     *
     * @throws ConfigParserException
     *             for badly formed files.
     */
    static Enumeration<String> getNamespaces(URL url) throws ConfigParserException {
        // Parse the document
        Document doc = getDocument(url);

        // retrieve namespaces from document
        Vector<Node> namespaceNodes = new Vector<Node>();
        traverseToGetNamespaceNodes(doc, namespaceNodes);
        Vector<String> namespaces = new Vector<String>();
        for (Enumeration<Node> enu = namespaceNodes.elements(); enu.hasMoreElements();) {
            Node value = ((Element) enu.nextElement()).getAttributeNode("name");
            if (value == null) {
                throw new ConfigParserException("incorrect xml format");
            } else {
                namespaces.add(value.getNodeValue());
            }
        }
        return namespaces.elements();
    }

    /**
     * <p>
     * Parses a string possibly separated with a list delimiter into a list of values.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> This method was moved from PropConfigProperties. Added a generic parameter to the return type.
     * </p>
     *
     * @param value
     *            the value string to parse.
     * @param listDelimiter
     *            the list delimiter.
     *
     * @return the parsed list.
     *
     * @throws ConfigParserException
     *             if the given string is invalid.
     */
    static List<String> parseValueString(String value, char listDelimiter) throws ConfigParserException {
        List<String> list = new ArrayList<String>();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < value.length(); ++i) {
            char ch = value.charAt(i);
            if (ch == '\\') {
                if (i + 1 >= value.length()) {
                    throw new ConfigParserException("Missing escaped character after \'\\\'.");
                }
                i++;
                ch = value.charAt(i);
                String escapes = " !:=\\#";
                if (ch == listDelimiter || escapes.indexOf(ch) >= 0) {
                    buffer.append(ch);
                } else if (ch == 't') {
                    buffer.append('\t');
                } else if (ch == 'r') {
                    buffer.append('\r');
                } else if (ch == 'n') {
                    buffer.append('\n');
                } else if (ch == 'u') {
                    if (i + UNICODE_LENGTH >= value.length()) {
                        throw new ConfigParserException("Invalid escape after \'\\u\'.");
                    }
                    String hexStr = value.substring(i + 1, i + 1 + UNICODE_LENGTH);
                    i += UNICODE_LENGTH;
                    buffer.append(parseHexString(hexStr));
                } else {
                    throw new ConfigParserException("Invalid escape after '\\'.");
                }
            } else if (ch == listDelimiter) {
                list.add(buffer.toString());
                buffer.delete(0, buffer.length());
            } else {
                buffer.append(ch);
            }
        }
        list.add(buffer.toString());
        return list;
    }

    /**
     * <p>
     * Parses the escaped characters in the given string, and returns the parsed string.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> This method was moved from PropConfigProperties.
     * </p>
     *
     * @param s
     *            the string to parse.
     *
     * @return the parsed string.
     *
     * @throws ConfigParserException
     *             if the given string is invalid.
     */
    static String parseString(String s) throws ConfigParserException {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            if (ch == '\\') {
                if (i + 1 >= s.length()) {
                    throw new ConfigParserException("Missing escaped character after \'\\\'.");
                }
                i++;
                ch = s.charAt(i);
                if (ch == 't') {
                    buffer.append('\t');
                } else if (ch == 'r') {
                    buffer.append('\r');
                } else if (ch == 'n') {
                    buffer.append('\n');
                } else if (ch == 'u') {
                    if (i + UNICODE_LENGTH >= s.length()) {
                        throw new ConfigParserException("Invalid escape after \'\\u\'.");
                    }
                    String hexStr = s.substring(i + 1, i + 1 + UNICODE_LENGTH);
                    i += UNICODE_LENGTH;
                    buffer.append(parseHexString(hexStr));
                } else {
                    buffer.append(ch);
                }
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    /**
     * <p>
     * Traverses through an XML node tree and extract the nodes representing namespaces.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> This method was moved from XMLConfigProperties. Added generic parameter for "namespaceNodes"
     * parameter.
     * </p>
     *
     * @param node
     *            a root node to traverse through.
     *
     * @param namespaceNodes
     *            currently built list of namespace nodes. Start as a new Vector.
     */
    private static void traverseToGetNamespaceNodes(Node node, Vector<Node> namespaceNodes) {
        if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("Config")) {
            namespaceNodes.add(node);
        } else {
            NodeList list = node.getChildNodes();
            for (int i = 0; i < list.getLength(); ++i) {
                traverseToGetNamespaceNodes(list.item(i), namespaceNodes);
            }
        }
    }

    /**
     * <p>
     * Parses a hex string to get a character.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> This method was moved from PropConfigProperties.
     * </p>
     *
     * @param s
     *            the hex string to parse.
     *
     * @return the corresponding character of the given hex string.
     *
     * @throws ConfigParserException
     *             if the given string is invalid.
     */
    private static char parseHexString(String s) throws ConfigParserException {
        s = s.trim().toLowerCase();
        int r = 0;
        for (int i = 0; i < s.length(); i++) {
            int t;
            char ch = s.charAt(i);
            if (ch >= '0' && ch <= '9') {
                t = ch - '0';
            } else if (ch >= 'a' && ch <= 'f') {
                t = ch - 'a' + HEX_LETTER_BASE;
            } else {
                throw new ConfigParserException("Invalid hex string.");
            }
            r = (r << HEX_BIT_COUNT) + t;
        }
        return (char) r;
    }
}
