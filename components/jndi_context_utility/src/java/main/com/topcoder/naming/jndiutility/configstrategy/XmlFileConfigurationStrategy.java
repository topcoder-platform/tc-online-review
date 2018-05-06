/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0
 *
 * @ XmlFileConfigurationStrategy.java
 */
package com.topcoder.naming.jndiutility.configstrategy;

import com.topcoder.naming.jndiutility.ConfigurationException;
import com.topcoder.naming.jndiutility.ConfigurationStrategy;
import com.topcoder.naming.jndiutility.Helper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/**
 * <p>This is a specific implementation of the ConfigurationStrategy interface contract for dealing with am xml
 * file with configuration data.</p>
 *  <p>Here we will be able to read/write and commit string properties.</p>
 *  <p><strong>Thread-Safety</strong></p>
 *  <p>Implementation is NOT thread-safe.</p>
 *
 * @author AleaActaEst, Charizard
 * @version 2.0
 */
public class XmlFileConfigurationStrategy implements ConfigurationStrategy {
    /** Name of the document element. */
    private static final String PROPERTIES = "Properties";

    /** Name of the property node. */
    private static final String PROPERTY = "Property";

    /** Name of the property attribute. */
    private static final String NAME = "name";

    /**
     * <p>This represents the current view of the xml DOM Document which represents the configuration
     * information. This is initialized in the constructor(s) and is basically read from the input file/stream.</p>
     *  <p>It is mutable since users can add new properties to the document.</p>
     */
    private final Document xmlDocument;

    /**
     * <p>This represents the source file from which we have read the xmlDocument. It also represents the
     * destination file when changes are committed through the commitChanges() method.</p>
     *  <p>This file name is obtained through the File input based constructor. If the other constructor is
     * used then this will be null and there will not be an ability to store changes.</p>
     */
    private final String fileName;

    /** Tag whether the properties has been changed. Introduced to avoid unnecessary file output. */
    private boolean changed;

    /**
     * <p>Constructs an instance with given <code>File</code> instance. The given file will be used to read xml
     * document, and be written when <code>commitChanges()</code> is called.</p>
     *
     * @param xmlSource xml file source
     *
     * @throws IllegalArgumentException if <code>xmlSource</code> is <code>null</code>
     * @throws ConfigurationException if error occurs during reading the file
     */
    public XmlFileConfigurationStrategy(File xmlSource)
        throws ConfigurationException {
        Helper.checkObject(xmlSource, "xmlSource");

        try {
            xmlDocument = createDocumentBuilder().parse(xmlSource);
        } catch (SAXException e) {
            throw new ConfigurationException("error occurred during parsing file", e);
        } catch (IOException e) {
            throw new ConfigurationException("error occurred during reading file", e);
        }

        fileName = xmlSource.getAbsolutePath();
    }

    /**
     * <p>Constructs ant instance with given input stream. The input stream will be used to read configuration
     * data. Note the instances created by this constructor cannot commit changes since there's no destination file
     * available.</p>
     *
     * @param streamInputSource input stream to the source xml data
     *
     * @throws IllegalArgumentException if <code>streamInputSource</code> is <code>null</code>
     * @throws ConfigurationException if error occurs during reading the input stream
     */
    public XmlFileConfigurationStrategy(InputStream streamInputSource)
        throws ConfigurationException {
        Helper.checkObject(streamInputSource, "streamInputSource");

        try {
            xmlDocument = createDocumentBuilder().parse(streamInputSource);
        } catch (SAXException e) {
            throw new ConfigurationException("error occurred during parsing file", e);
        } catch (IOException e) {
            throw new ConfigurationException("error occurred during reading file", e);
        }

        fileName = null;
    }

    /**
     * Get a xml document builder. Common operations of the two constructors.
     *
     * @return created document builder
     *
     * @throws ConfigurationException if error occurs during creating document builder
     */
    private DocumentBuilder createDocumentBuilder() throws ConfigurationException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            builder.setErrorHandler(new XMLFilterImpl());

            return builder;
        } catch (ParserConfigurationException e) {
            throw new ConfigurationException("error occurred during creating document builder");
        }
    }

    /**
     * Find a property node with the given name. Return null if none found. If the return value is not null,
     * it will always be a valid element node. Common operations in getProperty() and setProperty(). Note name must
     * not be null or empty.
     *
     * @param name name of the property
     *
     * @return property node with the given name, or null if none found
     *
     * @throws ConfigurationException if the document element is not properties, or invalid property node is met.
     */
    private Node findProperty(String name) throws ConfigurationException {
        if (!xmlDocument.getDocumentElement().getNodeName().equals(PROPERTIES)) {
            throw new ConfigurationException("wrong document element name");
        }

        NodeList list = xmlDocument.getElementsByTagName(PROPERTY);

        for (int i = 0; i < list.getLength(); i++) {
            Node child = list.item(i);
            Node nameNode = child.getAttributes().getNamedItem(NAME);

            if (nameNode == null) {
                throw new ConfigurationException("invalid property element with no name");
            }

            if (nameNode.getNodeValue().equals(name)) {
                if ((child.getChildNodes().getLength() != 1)
                        || (child.getFirstChild().getNodeType() != Node.TEXT_NODE)) {
                    throw new ConfigurationException("invalid property element with wrong child nodes");
                }

                return child;
            }
        }

        return null;
    }

    /**
     * <p>This method will fetch the string property for the given name. It will return <code>null</code> if
     * nothing is found.</p>
     *
     * @param name name of the property to fetch
     *
     * @return value of the configured property or <code>null</code> if not found
     *
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code> or empty
     * @throws ConfigurationException if error occurs during the actual process of fetching the property
     */
    public String getProperty(String name) throws ConfigurationException {
        Helper.checkString(name, "name");

        Node property = findProperty(name);

        if (property == null) {
            return null;
        } else {
            return property.getFirstChild().getNodeValue();
        }
    }

    /**
     * <p>This method will write the property with the given name and value into the associated document. If
     * property with the given name does not exist a new one will be created. Note that the changes will not be
     * permanent until the <code>commitChanges()</code> method is called, but uncommitted changes will be returned
     * by <code>getProperty(String name)</code>.</p>
     *
     * @param name property name
     * @param value property value
     *
     * @throws IllegalArgumentException if either <code>name</code> or <code>value</code> is <code>null</code> or
     *         empty
     * @throws ConfigurationException if error occurs during the actual process of setting the property
     */
    public void setProperty(String name, String value)
        throws ConfigurationException {
        Helper.checkString(name, "name");
        Helper.checkString(value, "value");

        Node property = findProperty(name);

        if (property == null) {
            // create a new property
            Element newElement = xmlDocument.createElement(PROPERTY);
            newElement.setAttribute(NAME, name);
            newElement.appendChild(xmlDocument.createTextNode(value));
            xmlDocument.getDocumentElement().appendChild(newElement);
        } else {
            property.getFirstChild().setNodeValue(value);
        }

        changed = true;
    }

    /**
     * <p>Commits any changes recently done to the data back to the configuration file. Note if this instance
     * is created by input stream <code>IllegalStateException</code> will be thrown.</p>
     *
     * @throws IllegalStateException if this instance is constructed with input stream thus no destination file is
     *         available
     * @throws ConfigurationException if error occurs during the actual process of committing the properties
     */
    public void commitChanges() throws ConfigurationException {
        if (fileName == null) {
            throw new IllegalStateException(
                "this instance is constructed with input stream thus no destination file is available");
        } else if (changed) {
            // only write if there're some changes
            try {
                TransformerFactory.newInstance().newTransformer()
                                  .transform(new DOMSource(xmlDocument), new StreamResult(new File(fileName)));
            } catch (TransformerException e) {
                throw new ConfigurationException("error occurred during transforming document", e);
            }

            changed = false;
        }
    }
}
