/*
 * Copyright (C) 2009-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * Represents an exception related to loading configuration settings. Inner exception should be provided to give
 * more details about the error. It is used in PhaseHandler implementation classes.
 * </p>
 * <p>
 * Thread safety: This class is immutable and thread safe.
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>Extends BaseCriticalException instead of BaseException</li>
 * <li>Added new constructors to meet TopCoder standards</li>
 * </ul>
 * </p>
 * @author tuenm, saarixx, bose_java, microsky
 * @version 1.6.1
 */
public class ConfigurationException extends BaseCriticalException {
    /**
     * The generated serial version UID.
     */
    private static final long serialVersionUID = 5786137119616593442L;

    /**
     * Create a new ConfigurationException instance with the specified error message.
     * @param message the error message of the exception
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of this exception with the given message and cause.
     * @param message the error message of the exception
     * @param cause the inner exception
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance of this exception with the given message and exception data.
     * @param message the error message of the exception
     * @param data the exception data
     * @since 1.6.1
     */
    public ConfigurationException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * Creates a new instance of this exception with the given message, cause and exception data.
     * @param message the error message of the exception
     * @param cause the internal exception that caused this exception
     * @param data the exception data
     * @since 1.6.1
     */
    public ConfigurationException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
