/*
 * Copyright (C) 2006-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * Represents an Ajax request sent by a client to the server;
 * this request consists of:
 * <ol>
 * <li>Type &ndash; the type of the Ajax request,
 *     which is used by the AjaxSupportServlet class to find the correct handler in order to service the request.</li>
 * <li>Parameters &ndash; zero or more request parameters; each parameter consists of:
 * <ul>
 * <li>A unique name which is a not null, and not empty String object.</li>
 * <li>A value which is a String object, or an empty String.</li>
 * </ul>
 * </li>
 * </ol>
 *
 * This class defines some helper methods to get a parameter value as a Date object, or a long data type.<br><br>
 * Parsing an Ajax request is implemented by this class.
 * </p>
 *
 * <p>
 * <strong>Thread Safety:</strong>
 * This class is immutable and thread safe.
 * </p>
 *
 * @author topgear
 * @author assistant
 * @version 1.1
 */
public final class AjaxRequest {

    /**
     * Represents the xsd file.
     */
    private static final String XSD_FILE = "com/cronos/onlinereview/ajax/AjaxRequestSchema.xsd";

    /**
     * Represents the data format.
     */
    private static final String DATE_FORMAT = "MM.dd.yyyy h:mm a";

    /**
     * Represents the schema language used for jaxp.
     */
    private static final String JAXP_SCHEMA_LANGUAGE =
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /**
     * Represents the schema source used for jaxp.
     */
    private static final String JAXP_SCHEMA_SOURCE =
        "http://java.sun.com/xml/jaxp/properties/schemaSource";

    /**
     * Represents the xml schema.
     */
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    /**
     * <p>
     * The type of the request.
     * </p>
     */
    private final String type;

    /**
     * <p>
     * Represents the parameters map containing zero or more request parameters.
     * This variable is immutable, both the variable and its content.
     * It is filled by the constructor with parameters data;
     * the values contained in this variable are retrieved using its corresponding getter methods.
     * <ul>
     * <li>Map Keys - are of type String, they can't be null,
     *                or empty strings, they represents parameters names</li>
     * <li>Map Values - are of type String, they can't be null,
     *                but empty Strings are accepted, they represents parameters values</li>
     * </ul>
     * </p>
     */
    private final Map<String, String> parameters = new HashMap<String, String>();

    /**
     * <p>
     * Creates a new instance of this class, and initialises its internal state.
     * </p>
     *
     * @param type the type of the request
     * @param parameters the parameters map of the request
     * @throws IllegalArgumentException if type is null, or empty string,
     *                                  or parameters is null or (keys/values) aren't of type String
     */
    public AjaxRequest(String type, Map<String, String> parameters) {

        // validate the parameters
        if (type == null) {
            throw new IllegalArgumentException("The type should not be null.");
        }
        if (type.trim().length() == 0) {
            throw new IllegalArgumentException("The type should not be empty.");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("The parameters should not be null.");
        }


        for (Map.Entry<String,String> entry : parameters.entrySet()) {
            // the key can't be null or empty
            if (entry.getKey().trim().length() == 0) {
                throw new IllegalArgumentException("The key should not be empty.");
            }

            // value can't be null but can be empty
            if (entry.getValue() == null) {
                throw new IllegalArgumentException("The value must not be null.");
            }
        }

        this.type = type;
        this.parameters.putAll(parameters);
    }

    /**
     * <p>
     * Creates a new AjaxRequest by parsing a character reader.
     * </p>
     *
     * @return the parsed AjaxRequest
     *
     * @param input the character reader from where to parse the request
     *
     * @throws RequestParsingException if the XML of the request is malformed or invalid
     * @throws IOException if there is an IO exception
     * @throws IllegalArgumentException if the reader is null
     */
    public static AjaxRequest parse(Reader input) throws RequestParsingException, IOException {
        if (input == null) {
            throw new IllegalArgumentException("The input should not be null.");
        }

        // creating a SAX factory
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);

        // defining the SAX handler
        AjaxRequestParser handler = new AjaxRequestParser();

        try {
            // creating a parser
            SAXParser parser = factory.newSAXParser();

            // setting the schema language to XSD
            parser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            // setting the schema source
            parser.setProperty(JAXP_SCHEMA_SOURCE, getFile(XSD_FILE));

            // creating the source from the XML message
            InputSource source = new InputSource(input);

            // parsing and validating the request XML
            parser.parse(source, handler);

            // getting the Ajax request from the handler
            return handler.getRequest();

        } catch (ParserConfigurationException e) {
            throw new RequestParsingException("Can't create parser.", e);
        } catch (SAXException e) {
            throw new RequestParsingException("Error happens when parsing the xml.", e);
        }
    }

    /**
     * This parser parses the ajax request from the client.
     *
     * @author topgear, assistant
     * @version 1.0
     */
    static class AjaxRequestParser extends DefaultHandler {

        /**
         * Represents the request type attribute.
         */
        private String requestType;

        /**
         * Represents the request parameters.
         */
        private final Map<String, String> requestParameters = new HashMap<String, String>();

        /**
         * Represents the request parameter name.
         */
        private String parameterName;

        /**
         * Represents buffer used to collect the parameter value.
         */
        private StringBuffer sb;

        /**
         * Represents the parsed AjaxRequest object.
         */
        private AjaxRequest request;

        /**
         * The default constructor.
         *
         */
        public AjaxRequestParser() {
            sb = new StringBuffer();
        }

        /**
         * Handle the start of an XML element.
         *
         * @param uri the uri
         * @param localName the local name of the element
         * @param qName the qName of the element
         * @param attributes the attributes of this element
         * @throws SAXException if any error
         */
        public void startElement(String uri, String localName,
                String qName, Attributes attributes) throws SAXException {

            if (qName.equals("request")) {
                if (attributes != null) {
                    requestType = attributes.getValue("type");
                }
            } else if (qName.equals("parameter")) {
                if (attributes != null) {
                    parameterName = attributes.getValue("name");
                }
            }
            sb.setLength(0);
        }

        /**
         * Handle the content of an XML element.
         *
         * @param ch the characters to handle
         * @param start the start index
         * @param length the length of the content
         * @throws SAXException if any error
         */
        public void characters(char[] ch, int start, int length)
            throws SAXException {
            sb.append(ch, start, length);
        }

        /**
         * Handle the end of an XML element.
         *
         * @param uri the uri
         * @param localName the local name of the element
         * @param qName the qName of the element
         * @throws SAXException if any error
         */
        public void endElement(String uri, String localName,
                String qName) throws SAXException {
            if (qName.equals("request")) {
                request = new AjaxRequest(requestType, requestParameters);
            } else if (qName.equals("parameter")) {
                requestParameters.put(parameterName, sb.toString());
            }
        }

        /**
         * Throw an exception for any XML validation error.
         * @param e the exception raised
         * @throws SAXException if any error happens
         */
        public void error(SAXParseException e) throws SAXException {
            throw e;
        }

        /**
         * Returns the parsed AjaxRequest object.
         *
         * @return the parsed AjaxRequest object
         */
        public AjaxRequest getRequest() {
            return request;
        }
    }

    /**
     * <p>
     * Returns the type of the request.
     * </p>
     *
     * @return the request type
     */
    public String getType() {
        return type;
    }

    /**
     * <p>
     * Returns all the parameter names.
     * </p>
     *
     * @return all the parameter names.
     */
    public Set getAllParameterNames() {
        return new HashSet<String>(parameters.keySet());
    }

    /**
     * <p>
     * Check if the specified parameter exists or not.
     * </p>
     *
     * @return true if contains, otherwise false
     * @param name the name of the parameter to check for
     * @throws IllegalArgumentException if name is null or empty
     */
    public boolean hasParameter(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The name should not be null.");
        }
        if (name.trim().length() == 0) {
            throw new IllegalArgumentException("The name should not be empty.");
        }
        return parameters.containsKey(name);
    }

    /**
     * <p>
     * Returns the value of a parameter.
     * </p>
     *
     * @return the value of the parameter
     * @param name the name of the parameter to get its value
     * @throws IllegalArgumentException if name is null, or empty string
     */
    public String getParameter(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The name should not be null.");
        }
        if (name.trim().length() == 0) {
            throw new IllegalArgumentException("The name should not be empty.");
        }
        return (String) parameters.get(name);
    }

    /**
     * <p>
     * Returns the value of a parameter as a long data type.
     * When you use this method please catch the NumberFormatException to detected that a parameter is not of type long.
     * </p>
     *
     * @return the value of the parameter as a long
     * @param name the name of the parameter to get its value
     * @throws IllegalArgumentException if name is null, or empty string
     * @throws NumberFormatException if the parameter could not being parsed as a long number
     */
    public long getParameterAsLong(String name) {
        return Long.parseLong(getParameter(name));
    }

    /**
     * <p>
     * Returns the value of a parameter as a Date object.
     * </p>
     *
     * @return the value of the parameter as a Date object
     * @param name the name of the parameter to get its value
     * @throws ParseException if the parameter could not being parsed as a Date
     * @throws IllegalArgumentException if name is null, or empty string
     */
    public Date getParameterAsDate(String name) throws ParseException {

        // get the string representation of the date
        String dateStr = getParameter(name);
        if (dateStr == null || dateStr.trim().length() == 0) {
            return null;
        }

        // creating a simple date formatter
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

        // to parse a date from a string
        return formatter.parse(dateStr);
    }

    /**
     * Get an file of schema.
     *
     * @param filePath the path of the file
     * @return the file object
     */
    private static File getFile(String filePath) {
        // get the URL of the resource
        URL url = AjaxRequest.class.getClassLoader().getResource(filePath);

        // return the file
        return new File(url.getFile());
    }
}
