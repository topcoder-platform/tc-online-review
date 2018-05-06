/*
 * Copyright (C) 2006-2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * Represents an exception detected during Ajax request parsing.
 * the originator error could be due to:
 * <ul>
 * <li>The XML document of the request is malformed.</li>
 * <li>The XML document of the request is well formed, but it is invalid against the schema.</li>
 * </ul>
 * <br>
 * This exception is thrown by AjaxRequest's "parse" method.
 * </p>
 *
 * @author topgear
 * @author assistant
 * @author George1
 * @version 1.0.1
 */
public class RequestParsingException extends BaseException {

    /**
     * <p>
     * Creates a new instance of this exception class.
     * </p>
     */
    public RequestParsingException() {
        // do nothing
    }

    /**
     * <p>
     * Creates a new instance of this exception class with a descriptive message.
     * </p>
     *
     * @param message a descriptive message about the exception
     */
    public RequestParsingException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new instance of this exception class with the cause of the exception.
     * </p>
     *
     * @param cause the cause of this exception
     */
    public RequestParsingException(Throwable cause) {
        super(cause);
    }

    /**
     * <p>
     * Creates a new instance of this exception class with a descriptive message, and the cause of the exception.
     * </p>
     *
     * @param message a descriptive message about the exception
     * @param cause the cause of this exception
     */
    public RequestParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
