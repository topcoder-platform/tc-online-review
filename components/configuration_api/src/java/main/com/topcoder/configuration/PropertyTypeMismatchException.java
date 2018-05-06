/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is thrown by implementations of ConfigurationObject when value cannot be casted to expected type or
 * parsed from string.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.1
 * @since 1.1
 */
@SuppressWarnings("serial")
public class PropertyTypeMismatchException extends ConfigurationException {
    /**
     * <p>
     * Creates a new instance of this exception with the given message.
     * </p>
     *
     * @param message
     *            the detailed error message of this exception.
     */
    public PropertyTypeMismatchException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new instance of this exception with the given message and cause.
     * </p>
     *
     * @param message
     *            the detailed error message of this exception.
     * @param cause
     *            the inner cause of this exception.
     */
    public PropertyTypeMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Creates a new instance of this exception with the given message and exception data.
     * </p>
     *
     * @param data
     *            the exception data.
     * @param message
     *            the detailed error message of this exception.
     */
    public PropertyTypeMismatchException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * <p>
     * Creates a new instance of this exception with the given message, cause and exception data.
     * </p>
     *
     * @param data
     *            the exception data.
     * @param message
     *            the detailed error message of this exception.
     * @param cause
     *            the inner cause of this exception.
     */
    public PropertyTypeMismatchException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
