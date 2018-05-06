/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * <p>
 * An extension of <code>ConfigProperties</code> to be used to maintain the properties list using .xml files.
 * </p>
 *
 * <p>
 * <em>Changes in 2.2:</em>
 * <ol>
 * <li>Made save() and load() methods synchronized.</li>
 * <li>Added support for "IsRefreshable" configuration parameter.</li>
 * <li>Added thread safety information.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is mutable, but partially thread safe since its save() and load()
 * methods are synchronized. I.e. its save() and load() methods can be safely called from multiple threads at a time.
 * </p>
 *
 * @author ilya, debedeb, danno, isv, WishingBone, saarixx, sparemax
 * @version 2.2
 */
class XMLConfigProperties extends ConfigProperties implements ErrorHandler {

    /**
     * An XML document.
     */
    private Document document = null;

    /**
     * A source .xml file containing the configuration properties.
     *
     * @since Configuration Manager 2.1
     */
    private URL source = null;

    /**
     * A namespace of configuration properties contained within this <code>
     * XMLConfigProperties</code>.
     *
     * @since Configuration Manager 2.1
     */
    private String namespace = null;

    /**
     * An <code>PrintWriter</code> used to output the properties.
     */
    private PrintWriter writer = null;

    /**
     * A private no-arg constructor (for clone).
     */
    private XMLConfigProperties() {
    }

    /**
     * Creates a new <code>XMLConfigProperties</code> that holds the properties for given namespace read from given
     * InputStream.
     *
     * If given <code>namespace</code> is <code>null</code>, assumes that given stream is a single-namespace XML file.
     * Otherwise, reads data corresponding to given <code>namespace</code> assuming that given stream represents a
     * multi-namespace XML file.
     *
     * @param source
     *            a file containing properties data
     * @param namespace
     *            a namespace to get the properties for
     * @throws ConfigParserException
     *             if a parsing error occurs, that is the given stream's format cannot be parsed by the implementing
     *             parser
     * @throws IOException
     *             if any I/O error occurs while reading from given <code>source</code>
     */
    XMLConfigProperties(URL source, String namespace) throws IOException {
        this.source = source;
        this.namespace = namespace;
        load();
    }

    /**
     * Creates a new <code>XMLConfigProperties</code> that holds the properties read from given InputStream. Assumes
     * that given stream is a single-namespace XML file.
     *
     * @param source
     *            a file containing properties data
     * @throws ConfigParserException
     *             if a parsing error occurs, that is the given stream's format cannot be parsed by the implementing
     *             parser.
     * @throws IOException
     *             if any I/O error occurs while reading from given <code>source</code>
     */
    XMLConfigProperties(URL source) throws IOException {
        this.source = source;
        load();
    }

    /**
     * Sets document variable to doc.
     *
     * @param doc
     *            document for this XMLConfigProperties.
     */
    void setDocument(Document doc) {
        this.document = doc;
    }

    /**
     * Returns the document for this XMLConfigProperties.
     *
     * @return document for this XMLConfigProperties.
     */
    Document getDocument() {
        return document;
    }

    /**
     * Update DOM tree with property tree.
     *
     * @param node
     *            DOM tree node
     * @param property
     *            property node
     */
    private void updateProperty(Node node, Property property) {
        // comment first
        List<String> comments = property.getComments();
        if (comments != null) {
            for (Iterator<String> itr = comments.iterator(); itr.hasNext();) {
                node.appendChild(document.createComment((String) itr.next()));
            }
        }

        // the property itself if it contains values or subproperties
        String[] values = property.getValues();
        List<Property> subproperties = property.list();
        if ((values != null && values.length > 0) || !subproperties.isEmpty()) {
            Element element = document.createElement("Property");
            element.setAttribute("name", property.getName());

            if (values != null) {
                for (int i = 0; i < values.length; ++i) {
                    Node value = document.createElement("Value");
                    value.appendChild(document.createTextNode(values[i]));
                    element.appendChild(value);
                }
            }

            // here goes the subproperties
            for (Iterator<Property> itr = subproperties.iterator(); itr.hasNext();) {
                updateProperty(element, itr.next());
            }

            node.appendChild(element);
        }
    }

    /**
     * Write normalized string to xml file.
     *
     * @param string
     *            the string to write
     */
    private void writeNormalized(String string) {
        for (int i = 0; i < string.length(); ++i) {
            char ch = string.charAt(i);
            switch (ch) {
            case '<': {
                writer.print("&lt;");
                break;
            }
            case '>': {
                writer.print("&gt;");
                break;
            }
            case '&': {
                writer.print("&amp;");
                break;
            }
            case '"': {
                writer.print("&quot;");
                break;
            }
            case '\r':
            case '\n': {
                writer.print("&#");
                writer.print(Integer.toString(ch));
                writer.print(';');
                break;
            }
            default: {
                writer.print(ch);
            }
            }
        }
    }

    /**
     * <p>
     * Write DOM tree to xml file.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Added steps for persisting "IsRefreshable" flag.</li>
     * </ol>
     * </p>
     *
     * @param node
     *            DOM tree node
     * @param indent
     *            indentation
     */
    private void writeProperty(Node node, int indent) {
        switch (node.getNodeType()) {
        // document
        case Node.DOCUMENT_NODE: {
            Document documentNode = (Document) node;
            writer.println("<?xml version=\"1.0\"?>");
            if (documentNode.getDoctype() != null) {
                writeProperty(documentNode.getDoctype(), 0);
            }
            if (documentNode.getDocumentElement() != null) {
                writeProperty(documentNode.getDocumentElement(), 0);
            }
            break;
        }

            // doctype
        case Node.DOCUMENT_TYPE_NODE: {
            writeDocument(node);
            break;
        }

            // element
        case Node.ELEMENT_NODE: {
            writeElement(node, indent);
            break;
        }

            // only write text if it is enclosed in the Value tag or ListDelimiter tag
        case Node.TEXT_NODE: {
            Node parent = node.getParentNode();
            if (parent.getNodeType() == Node.ELEMENT_NODE
                && (parent.getNodeName().equals("Value") || parent.getNodeName().equals("ListDelimiter") || parent
                    .getNodeName().equals(Helper.KEY_REFRESHABLE))) {
                writer.print(node.getNodeValue());
            }
            break;
        }

            // write comments
        case Node.COMMENT_NODE: {
            if (namespace == null) {
                writer.print("    ");
            } else {
                writer.print("        ");
            }
            writer.print("<!--");
            writer.print(((Comment) node).getData());
            writer.println("-->");
        }
        default:
            break;
        }
    }

    /**
     * <p>
     * Writes document to xml file.
     * </p>
     *
     * @param node
     *            the element node
     *
     * @since 2.2
     */
    private void writeDocument(Node node) {
        DocumentType doctype = (DocumentType) node;
        writer.print("<!DOCTYPE ");
        writer.print(doctype.getName());
        String publicId = doctype.getPublicId();
        String systemId = doctype.getSystemId();
        if (publicId != null) {
            writer.print(" PUBLIC '");
            writer.print(publicId);
            writer.print("' '");
            writer.print(systemId);
            writer.print('\'');
        } else {
            writer.print(" SYSTEM '");
            writer.print(systemId);
            writer.print('\'');
        }
        String internalSubset = doctype.getInternalSubset();
        if (internalSubset != null) {
            writer.println(" [");
            writer.print(internalSubset);
            writer.print(']');
        }
        writer.println('>');
    }

    /**
     * <p>
     * Writes element to xml file.
     * </p>
     *
     * @param node
     *            the element node
     * @param indent
     *            indentation
     *
     * @since 2.2
     */
    private void writeElement(Node node, int indent) {
        for (int i = 0; i < indent; ++i) {
            writer.print("    ");
        }
        writer.print('<');
        writer.print(node.getNodeName());
        NamedNodeMap map = node.getAttributes();
        for (int i = 0; i < map.getLength(); ++i) {
            Attr attr = (Attr) map.item(i);
            writer.print(' ');
            writer.print(attr.getNodeName());
            writer.print("=\"");
            writeNormalized(attr.getNodeValue());
            writer.print('"');
        }
        writer.print('>');
        if (!node.getNodeName().equals("Value") && !node.getNodeName().equals("ListDelimiter")
            && !node.getNodeName().equals(Helper.KEY_REFRESHABLE)) {
            writer.println();
        }

        Node child = node.getFirstChild();
        while (child != null) {
            writeProperty(child, indent + 1);
            child = child.getNextSibling();
        }

        if (!node.getNodeName().equals("Value") && !node.getNodeName().equals("ListDelimiter")
            && !node.getNodeName().equals(Helper.KEY_REFRESHABLE)) {
            for (int i = 0; i < indent; ++i) {
                writer.print("    ");
            }
        }
        writer.print("</");
        writer.print(node.getNodeName());
        writer.println('>');
    }

    /**
     * <p>
     * Saves the data(properties and their values) from properties tree into persistent storage.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Made this method synchronized.</li>
     * <li>Added steps for persisting "IsRefreshable" flag.</li>
     * </ol>
     * </p>
     *
     * @throws UnsupportedOperationException
     *             if the source is not a physical file
     * @throws IOException
     *             if any exception related to underlying persistent storage occurs
     */
    @Override
    protected synchronized void save() throws IOException {
        if (!source.getProtocol().equals("file")) {
            throw new UnsupportedOperationException("source is not a physical file");
        }

        // remove the properties
        Node node = getNamespaceRootNode(document);
        while (node.getFirstChild() != null) {
            node.removeChild(node.getFirstChild());
        }

        // list delimiter if not default
        char listDelimiter = getListDelimiter();
        if (listDelimiter != ';') {
            Node delimiter = document.createElement("ListDelimiter");
            delimiter.appendChild(document.createTextNode("" + listDelimiter));
            node.appendChild(delimiter);
        }
        // save "IsRefreshable" flag (NEW in 2.2)
        Boolean refreshable = isRefreshable();
        if (refreshable != null) {
            Node refreshableNode = document.createElement(Helper.KEY_REFRESHABLE);
            refreshableNode.appendChild(document.createTextNode(refreshable.toString()));
            node.appendChild(refreshableNode);
        }

        // populate with new properties
        for (Iterator<Property> itr = getRoot().list().iterator(); itr.hasNext();) {
            updateProperty(node, itr.next());
        }
        try {
            writer = new PrintWriter(new FileWriter(Helper.decodeURL(source.getFile())));
            writeProperty(document, 0);
        } finally {
            // Close the writer
            Helper.closeObj(writer);
        }
    }

    /**
     * <p>
     * Get the root node for specified namespace. If namespace is null, return the "CMConfig" node.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Added steps for reading "IsRefreshable" flag.</li>
     * </ol>
     * </p>
     *
     * @param node
     *            the current node
     * @return the respective node, or null if such node does not exist
     * @throws ConfigParserException
     *             if xml format is incorrect
     */
    private Node getNamespaceRootNode(Node node) throws ConfigParserException {
        // check current node
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (namespace == null) {
                if (node.getNodeName().equals("CMConfig")) {
                    return node;
                }
            } else {
                if (node.getNodeName().equals("Config")) {
                    Node value = ((Element) node).getAttributeNode("name");
                    if (value == null) {
                        throw new ConfigParserException("incorrect xml format");
                    } else {
                        if (value.getNodeValue().equals(namespace)) {
                            return node;
                        }
                    }
                }
            }
        }

        // check child nodes
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); ++i) {
            Node ret = getNamespaceRootNode(list.item(i));
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }

    /**
     * <p>
     * Load node as a property.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Added steps for reading "IsRefreshable" flag.</li>
     * </ol>
     * </p>
     *
     * @param root
     *            the root property
     * @param node
     *            the current node
     * @param prefix
     *            the prefix of the property
     * @param refreshableAndDelimiter
     *            the refreshable flag and list delimiter.
     *
     * @throws IOException
     *             if any exception related to underlying persistent storage occurs
     */
    private static void loadNode(Property root, Node node, String prefix, Object[] refreshableAndDelimiter)
        throws IOException {
        List<String> comments = new ArrayList<String>();
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); ++i) {
            node = list.item(i);

            // put comment into cache
            if (node.getNodeType() == Node.COMMENT_NODE) {
                comments.add(((Comment) node).getData());
                continue;
            }

            // find a property
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equals("Property")) {
                    Node value = ((Element) node).getAttributeNode("name");
                    if (value == null) {
                        throw new ConfigParserException("incorrect xml format");
                    }
                    String key = prefix + value.getNodeValue();

                    // retrieve all values
                    parseValues(root, comments, node, key, refreshableAndDelimiter);
                } else if (node.getNodeName().equals("ListDelimiter")) {
                    String delim = getTextData(node);
                    if (delim.length() != 1) {
                        throw new ConfigParserException("invalid delimiter");
                    }
                    refreshableAndDelimiter[1] = delim.charAt(0);
                } else if (Helper.KEY_REFRESHABLE.equals(node.getNodeName())) { // NEW in 2.2
                    refreshableAndDelimiter[0] = Boolean.parseBoolean(getTextData(node));
                } else if (!node.getNodeName().equals("Value")) {
                    throw new ConfigParserException("unrecognized element " + node.getNodeName());
                }
            }
        }
    }

    /**
     * <p>
     * Gets property values.
     * </p>
     *
     * @param root
     *            the root.
     * @param comments
     *            the comments.
     * @param node
     *            the node.
     * @param key
     *            the key.
     * @param refreshableAndDelimiter
     *            the refreshable flag and list delimiter.
     *
     * @throws IOException
     *             if any error occurs.
     *
     * @since 2.2
     */
    private static void parseValues(Property root, List<String> comments, Node node, String key,
        Object[] refreshableAndDelimiter) throws IOException {
        List<String> valueList = new ArrayList<String>();
        boolean nested = false;

        NodeList values = node.getChildNodes();
        for (int j = 0; j < values.getLength(); ++j) {
            Node subNode = values.item(j);

            // put comment into cache
            if (subNode.getNodeType() == Node.COMMENT_NODE) {
                comments.add(((Comment) subNode).getData());
                continue;
            }

            if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                // nested element
                if (subNode.getNodeName().equals("Property")) {
                    nested = true;
                } else if (subNode.getNodeName().equals("Value")) {
                    Node text = subNode.getFirstChild();
                    if (text == null) {
                        valueList.add("");
                    } else if (text.getNodeType() == Node.TEXT_NODE) {
                        valueList.addAll(Helper.parseValueString(((Text) text).getData(),
                            (Character) refreshableAndDelimiter[1]));
                    } else {
                        throw new ConfigParserException("invalid subnode");
                    }
                    if (text != null && text.getNextSibling() != null) {
                        throw new ConfigParserException("invalid subnode");
                    }
                } else {
                    throw new ConfigParserException("unrecognized element " + subNode.getNodeName());
                }
            }
        }

        // construct property
        if (valueList.size() > 0) {
            Property property = root.find(key);
            if (property != null && property.getValue() != null) {
                throw new ConfigParserException("contains duplicate property " + key);
            }
            root.setProperty(key, (String[]) valueList.toArray(new String[valueList.size()]));
            if (comments.size() > 0) {
                property = root.find(key);
                for (Iterator<String> itr = comments.iterator(); itr.hasNext();) {
                    property.addComment((String) itr.next());
                }
                comments.clear();
            }
        }
        if (nested) {
            loadNode(root, node, key + ".", refreshableAndDelimiter);
        }
    }

    /**
     * <p>
     * Gets the data of the text node.
     * </p>
     *
     * @param node
     *            the text node.
     *
     * @return the data.
     *
     * @throws ConfigParserException
     *             if any error occurs.
     *
     * @since 2.2
     */
    private static String getTextData(Node node) throws ConfigParserException {
        Node text = node.getFirstChild();
        if (text == null) {
            throw new ConfigParserException("invalid IsRefreshable element");
        } else if (text.getNodeType() == Node.TEXT_NODE) {
            String data = ((Text) text).getData();
            if (data == null) {
                throw new ConfigParserException("invalid data");
            }

            if (text.getNextSibling() != null) {
                throw new ConfigParserException("invalid subnode");
            }

            return data;
        } else {
            throw new ConfigParserException("invalid data");
        }
    }

    /**
     * <p>
     * Loads the properties and their values from persistent storage.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Made this method synchronized.</li>
     * </ol>
     * </p>
     *
     * @throws IOException
     *             if any exception related to underlying persistent storage occurs
     */
    @Override
    protected synchronized void load() throws IOException {
        // Parse the document
        document = Helper.getDocument(source);

        // retrieve namespace root node
        Node node = getNamespaceRootNode(document);
        if (node == null) {
            throw new ConfigParserException("can not locate namespace " + namespace);
        }

        try {
            // load property tree
            Property root = new Property();

            Object[] refreshableAndDelimiter = new Object[] {null, Helper.DEFAULT_LIST_DELIMITER};
            loadNode(root, node, "", refreshableAndDelimiter);
            setRoot(root);

            // Set refreshable flag
            setRefreshable((Boolean) refreshableAndDelimiter[0]);
            // Set list delimiter
            setListDelimiter((Character) refreshableAndDelimiter[1]);
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    /**
     * <p>
     * Receives notification of a warning and outputs message to standard error output.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Renamed "e" to "exception".</li>
     * <li>Added parameter documentation for "exception".</li>
     * </ol>
     * </p>
     *
     * @param exception
     *            the error information encapsulated in a SAX parse exception.
     *
     * @throws SAXException
     *             any SAX exception, possibly wrapping another exception.
     */
    public void warning(SAXParseException exception) throws SAXException {
        System.err.println("warning : " + exception.getMessage());
    }

    /**
     * <p>
     * Receives notification of a recoverable error, outputs message to standard error output and throws new
     * SAXException wrapping caught SAXParseException.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Renamed "e" to "exception".</li>
     * </ol>
     * </p>
     *
     * @param exception
     *            the error information encapsulated in a SAX parse exception.
     *
     * @throws SAXException
     *             wrapping <code>SAXParserException</code> occurred.
     */
    public void error(SAXParseException exception) throws SAXException {
        System.err.println("error : " + exception.getMessage());
        throw exception;
    }

    /**
     * <p>
     * Receives notification of a non-recoverable error, outputs message to standard error output and throws new
     * SAXException wrapping caught SAXParseException
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Renamed "e" to "exception".</li>
     * </ol>
     * </p>
     *
     * @param exception
     *            the error information encapsulated in a SAX parse exception.
     *
     * @throws SAXException
     *             wrapping <code>SAXParserException</code> occurred.
     */
    public void fatalError(SAXParseException exception) throws SAXException {
        System.err.println("fatal error : " + exception.getMessage());
        throw exception;
    }

    /**
     * <p>
     * Gets the clone copy of this object.
     * </p>
     *
     * <p>
     * <em>Changes in 2.2:</em>
     * <ol>
     * <li>Setting refreshable property.</li>
     * </ol>
     * </p>
     *
     * @return a clone copy of this object.
     */
    @Override
    public Object clone() {
        XMLConfigProperties properties = new XMLConfigProperties();
        properties.source = source;
        properties.namespace = namespace;
        properties.document = document;
        properties.setRoot((Property) getRoot().clone());

        properties.setRefreshable(isRefreshable()); // NEW in 2.2

        return properties;
    }

}
