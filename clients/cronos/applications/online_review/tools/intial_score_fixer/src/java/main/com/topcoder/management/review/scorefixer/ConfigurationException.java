/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorefixer;

/**
 * This exception should be thrown by constructors of {@link InitialScoreFixer} and the various
 * interface implementations if any error occurs configuring itself.
 * <p>
 * This class is thread-safe because it's stateless.
 * </p>
 *
 * @author George1
 * @version 1.0
 */
public class ConfigurationException extends ScoreFixerException {

    /**
     * Automatically generated unique ID for use with serialization.
     */
    private static final long serialVersionUID = 8250462933277926669L;

    /**
     * Constructs a new instance of ConfigurationException.
     */
    public ConfigurationException() {
    }

    /**
     * Constructs a new instance of ConfigurationException with the given error message.
     *
     * @param message
     *            the error message
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance of ConfigurationException with the given error message and inner
     * cause.
     *
     * @param message
     *            the error message
     * @param cause
     *            the inner cause
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
