/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

/**
 * <p>
 * This exception is thrown by CustomResultSet when null is found when the
 * caller requests a primitive type value.
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> This class is not thread safe because its
 * base class is not thread safe.
 * </p>
 *
 * @author saarixx, suhugo
 * @version 2.0
 * @since 2.0
 */
@SuppressWarnings("serial")
public class NullColumnValueException extends Exception {

    /**
     * <p>
     * Creates a new instance of NullColumnValueException.
     * </p>
     */
    public NullColumnValueException() {
    }

    /**
     * <p>
     * Creates a new instance of NullColumnValueException with the given
     * message.
     * </p>
     *
     * @param message
     *            the detailed error message of this exception
     */
    public NullColumnValueException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new instance of NullColumnValueException with the given message
     * and cause.
     * </p>
     *
     * @param message
     *            the detailed error message of this exception
     * @param cause
     *            the inner cause of this exception
     */
    public NullColumnValueException(String message, Exception cause) {
        super(message, cause);
    }
}
