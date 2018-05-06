/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception indicates given property key, property value, child name or child instance is not acceptable by
 * specific ConfigurationObject implementation, like a cycle occurs after adding some child. For example, the default
 * implementation requires all the property values are instances of Serialiable.
 * </p>
 *
 * <p>
 * It can be thrown from methods updating properties and adding children. In the default implementation, this
 * exception is thrown for non-Serializable property values.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
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
public class InvalidConfigurationException extends ConfigurationException {
    /**
     * Constructs a new InvalidConfigurationException with the specified detail message.
     *
     * @param message
     *            the detail message
     */
    public InvalidConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidConfigurationException with the specified detail message and cause.
     *
     * @param message
     *            the detail message
     * @param cause
     *            the cause of this exception
     */
    public InvalidConfigurationException(String message, Throwable cause) {
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
    public InvalidConfigurationException(String message, ExceptionData data) {
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
    public InvalidConfigurationException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
