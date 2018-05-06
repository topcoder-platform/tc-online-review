/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This class is the base exception of this component. It provides user the supports for dealing with all the
 * exceptions from this component as a whole.
 * </p>
 *
 * <p>
 * Currently, there are three extensions of this exception, ProcessException, ConfigurationAccessException and
 * InvlaidConfigurationException.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Extends BaseCriticalException instead of BaseException</li>
 * <li>Added new constructors to meet TopCoder standards.</li>
 * <li>Fixed thread safety information.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author maone, haozhangr, saarixx, sparemax
 * @version 1.1
 */
@SuppressWarnings("serial")
public class ConfigurationException extends BaseCriticalException {
    /**
     * Constructs a new ConfigurationException with the specified detail message.
     *
     * @param message
     *            the detail message
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConfigurationException with the specified detail message and cause.
     *
     * @param message
     *            the detail message
     * @param cause
     *            the cause of this exception
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Creates a new instance of this exception with the given message and exception data.
     * </p>
     *
     * @param message
     *            the detailed error message of this exception.
     * @param data
     *            the exception data.
     *
     * @since 1.1
     */
    public ConfigurationException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * <p>
     * Creates a new instance of this exception with the given message, cause and exception data.
     * </p>
     *
     *
     * @param message
     *            the detailed error message of this exception.
     * @param cause
     *            the inner cause of this exception.
     * @param data
     *            the exception data.
     *
     * @since 1.1
     */
    public ConfigurationException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
