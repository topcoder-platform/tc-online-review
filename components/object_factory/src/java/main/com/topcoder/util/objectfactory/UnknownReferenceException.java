/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * The reference passed by the factory, which referes to the type and/or
 * identifier, refers to a mapping that does not exist in the specification
 * factory, or the speficication tree for this reference contains a mapping that
 * does not exist. For example, the type and idnetifier might map to a valid
 * specification, but one of its parameters might not. Thrown by the
 * getObjectSpecification method in the SpecificationFactory.
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
public class UnknownReferenceException extends SpecificationFactoryException {
    /**
     * <p>
     * Constructs a new UnknownReferenceException with the specified detail
     * message.
     * </p>
     *
     * @param message
     *            the detail message
     */
    public UnknownReferenceException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new UnknownReferenceException with the specified detail
     * message and cause.
     * </p>
     *
     * @param message
     *            the detail message
     * @param cause
     *            the cause of this exception
     */
    public UnknownReferenceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Creates a new UnknownReferenceException with the given message and
     * exception data.
     * </p>
     *
     * @param message
     *            the detailed error message of this exception
     * @param data
     *            the exception data
     * @since 2.2
     */
    public UnknownReferenceException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * <p>
     * Creates a new UnknownReferenceException with the given message, cause and
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
    public UnknownReferenceException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
