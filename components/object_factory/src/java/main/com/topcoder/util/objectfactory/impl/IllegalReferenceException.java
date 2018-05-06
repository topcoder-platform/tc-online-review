/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

import com.topcoder.util.errorhandling.ExceptionData;
import com.topcoder.util.objectfactory.SpecificationFactoryException;

/**
 * <p>
 * Can be thrown by constructor of SpecificationFactory implementations if
 * cannot properly match specifications to each other, or the properties are
 * malformed.
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
public class IllegalReferenceException extends SpecificationFactoryException {
    /**
     * <p>
     * Constructs a new IllegalReferenceException with the specified detail
     * message.
     * </p>
     *
     * @param message
     *            the detail message
     */
    public IllegalReferenceException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new IllegalReferenceException with the specified detail
     * message and cause.
     * </p>
     *
     * @param message
     *            the detail message
     * @param cause
     *            the cause of this exception
     */
    public IllegalReferenceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Creates a new IllegalReferenceException with the given message and
     * exception data.
     * </p>
     *
     * @param message
     *            the detailed error message of this exception
     * @param data
     *            the exception data
     * @since 2.2
     */
    public IllegalReferenceException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * <p>
     * Creates a new IllegalReferenceException with the given message, cause and
     * exception data.
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
    public IllegalReferenceException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
