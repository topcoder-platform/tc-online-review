/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

/**
 * <p>
 * This is an exception which indicates any errors related to configuration, such as missing required properties or
 * invalid property values (e.g. not a number).
 * </p>
 *
 * <p>
 * This exception is thrown from the constructors accepting a namespace.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b> This class is immutable and therefore thread safe.
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public class ConfigurationException extends FileUploadException {
    /**
     * <p>
     * Creates a new instance of <code>ConfigurationException</code> class with a detail error message.
     * </p>
     *
     * @param message a detail error message describing the error.
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new instance of <code>ConfigurationException</code> class with a detail error message and the original
     * exception causing the error.
     * </p>
     *
     * @param message a detail error message describing the error.
     * @param cause an exception representing the cause of the error.
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
