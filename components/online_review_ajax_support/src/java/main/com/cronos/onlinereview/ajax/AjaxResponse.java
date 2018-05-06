/*
 * Copyright (C) 2006-2012 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

/**
 * <p>
 * Represents an Ajax response produced by an Ajax request handler in order to be sent back to the requesting client.
 * This response consists of:
 * <ul>
 * <li>Type &ndash; the type of the Ajax response which is the same as the Ajax request type.</li>
 * <li>Status &ndash; the status of the operation which could be "success" for successful operation
 *              or any other status like error statuses.</li>
 * <li>Data &ndash; which could represent a result data, or an error message; this field is optional.</li>
 * </ul>
 *
 * Transforming an Ajax response to an XML document is implemented by this class.
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
public final class AjaxResponse {

    /**
     * <p>
     * Represents the type of this Ajax response.
     * This variable is immutable, it is initialized by the constructor to a not null and not empty String;
     * the value of this variable is retrieved using its corresponding getter method.
     * </p>
     */
    private final String type;

    /**
     * <p>
     * The status of the operation which could be "success" for successful operation
     * or any other status like error statuses.
     * This variable is immutable, it is initialized by the constructor to a not null and not empty String;
     * the value of this variable is retrieved using its corresponding getter method.
     * </p>
     */
    private final String status;

    /**
     * <p>
     * The optional data object, it could represent the result of the operation , or an error message.
     * This variable is immutable, it is initialized by the constructor to null or an Object;
     * the value of this variable is retrieved using its corresponding getter method.
     * </p>
     */
    private final Object data;

    /**
     * <p>
     * Creates a new instance of this class, and initialises its internal state.
     * </p>
     *
     * @param type the type of the response
     * @param status the status of the response
     * @param data the data returned by the response
     * @throws IllegalArgumentException if type,or status is null or empty string
     */
    public AjaxResponse(String type, String status, Object data) {
        if (type == null) {
            throw new IllegalArgumentException("The type should not be null.");
        }
        if (type.trim().length() == 0) {
            throw new IllegalArgumentException("The type should not be empty.");
        }
        if (status == null) {
            throw new IllegalArgumentException("The status should not be null.");
        }
        if (status.trim().length() == 0) {
            throw new IllegalArgumentException("The status should not be empty.");
        }

        this.type = type;
        this.status = status;
        this.data = data;
    }

    /**
     * <p>
     * Returns the type of this Ajax response.
     * </p>
     *
     * @return the response type
     */
    public String getType() {
        return type;
    }

    /**
     * <p>
     * Returns the status of the operation which could be "success" for successful operation
     * or any other status like error statuses.
     * </p>
     *
     * @return the response status
     */
    public String getStatus() {
        return status;
    }

    /**
     * <p>
     * Returns the optional data object, it could represent the result of the operation , or an error message.
     * </p>
     *
     * @return the response data
     */
    public Object getData() {
        return data;
    }

    /**
     * <p>
     * Transforms this Ajax response to its XML representation, and optionally include the XML header.
     * </p>
     *
     * @return the XML representation of the response
     * @param withHeader generate XML header or not
     */
    public String toXml(boolean withHeader) {

        // use a StringBuffer to create the xml contents
        StringBuilder sb = new StringBuilder();

        // if withHeader is true, add the header
        if (withHeader) {
            sb.append("<?xml version=\"1.0\" ?>");
        }

        // start the response element
        sb.append("<response ");

        // write the type attribute
        sb.append("type=\"");
        sb.append(type);
        sb.append("\">");

        // write the result child element
        sb.append("<result status=\"");
        sb.append(status);
        sb.append("\"");

        // write the optional data
        if (data != null) {
            sb.append(">");
            sb.append(data);
            sb.append("</result>");
        } else {
            sb.append("/>");
        }

        sb.append("</response>");

        return sb.toString();
    }
}
