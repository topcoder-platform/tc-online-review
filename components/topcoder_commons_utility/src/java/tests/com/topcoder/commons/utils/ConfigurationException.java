/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

/**
 * <p>
 * The configuration exception.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ConfigurationException extends RuntimeException {
    /**
     * <p>
     * Constructs a new <code>ConfigurationException</code> instance with error message.
     * </p>
     *
     * @param message
     *            the error message.
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new <code>ConfigurationException</code> instance with error message and inner cause.
     * </p>
     *
     * @param message
     *            the error message.
     * @param cause
     *            the inner cause.
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
