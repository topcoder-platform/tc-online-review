/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

/**
 * <p>
 * An exception to be thrown by {@link DBConnectionFactory#createConnection()} and {@link
 * DBConnectionFactory#createConnection(String, String)} methods if the default connection is not
 * configured within the factory. This exception should provide a meaningful message providing the
 * client with the description of the error.
 * </p>
 * <p>
 * Side Note : Empty String means the length of string after trimming equals to zero.
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> this class is thread safe since it has no internal state and the
 * base exception <code>DBConnectionException</code> is thread safe.
 * </p>
 *
 * @author isv, qiucx0161, nhzp339, magicpig
 * @version 1.1
 * @since 1.0
 */
public class NoDefaultConnectionException extends DBConnectionException {
    /**
     * <p>
     * Constructs new <code>NoDefaultConnectionException</code> with specified message providing a
     * description of the error.
     * </p>
     *
     * @param message
     *            a <code>String</code> providing the details of the error occurred while
     *            obtaining a connection to a database; it can be any <code>String</code> instance
     *            including null and empty string.
     */
    public NoDefaultConnectionException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new <code>NoDefaultConnectionException</code>, with the given message and
     * cause.
     * </p>
     *
     * @param message
     *            a <code>String</code> providing the details of the error occurred while
     *            obtaining a connection to a database; it can be any <code>String</code> instance
     *            including null and empty string.
     * @param cause
     *            a <code>Throwable</code> representing the original cause of this exception; it
     *            can be any <code>Throwable</code> instance including null.
     */
    public NoDefaultConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
