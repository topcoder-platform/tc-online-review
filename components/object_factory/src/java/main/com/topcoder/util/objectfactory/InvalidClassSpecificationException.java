/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * Thrown by the ObjectFactory createObject methods if the specification is not
 * valid and can't be used to create an object.
 * </p>
 * <p>
 * <strong>Changes in 2.2:</strong>
 * <ol>
 * <li>Added new constructors to meet TopCoder standards.</li>
 * </ol>
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> This class is not thread safe because its
 * base class is not thread safe.
 * </p>
 *
 * @author codedoc, saarixx, mgmg, TCSDEVELOPER
 * @version 2.2
 */
@SuppressWarnings("serial")
public class InvalidClassSpecificationException extends ObjectCreationException {
    /**
     * <p>
     * Constructs a new InvalidClassSpecificationException with the specified
     * detail message.
     * </p>
     *
     * @param message
     *            the detail message
     */
    public InvalidClassSpecificationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new InvalidClassSpecificationException with the specified
     * detail message and cause.
     * </p>
     *
     * @param message
     *            the detail message
     * @param cause
     *            the cause of this exception
     */
    public InvalidClassSpecificationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Creates a new InvalidClassSpecificationException with the given message
     * and exception data.
     * </p>
     *
     * @param message
     *            the detailed error message of this exception
     * @param data
     *            the exception data
     * @since 2.2
     */
    public InvalidClassSpecificationException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * <p>
     * Creates a new InvalidClassSpecificationException with the given message,
     * cause and exception data.
     * </p>
     *
     * @param message
     *            the detailed error message of this exception
     * @param cause
     *            the inner cause of this exception
     * @param data
     *            the exception data
     * @since 2.2
     */
    public InvalidClassSpecificationException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
