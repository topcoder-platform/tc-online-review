/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import com.topcoder.util.errorhandling.BaseRuntimeException;


/**
 * This exception is thrown if there is any configuration error during initializing.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ConfigurationException extends BaseRuntimeException {
    /**
    * Represents the serialVersionUID.
    */
    private static final long serialVersionUID = -7750319516594275728L;

    /**
    * Create an instance with error message.
    *
    * @param message
    *            the error message
    */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Create an instance with error message and cause.
     *
     * @param message
     *            the error message
     * @param cause the error cause
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
