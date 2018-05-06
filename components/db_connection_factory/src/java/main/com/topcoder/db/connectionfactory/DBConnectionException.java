/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * An exception to be thrown by <code>DBConnectionFactory</code> methods if a requested connection
 * can not be created for some reason. It may also be thrown from implementations of
 * ConnectionProducer interface if a requested connection can not be created for some reason.
 * Usually this exception will wrap the original, implementation-specific exception which caused the
 * operation to fail. Such a cause may be obtained through {@link #getCause()} method.
 * </p>
 * <p>
 * This class is a base class for all implementation-specific exceptions which are thrown if a
 * requested connection can not be created for whatever reason.
 * </p>
 * <p>
 * Side Note : Empty String means the length of string after trimming equals to zero.
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> this class is thread safe since it has no internal state and the
 * base exception <code>BaseException</code> is thread safe.
 * </p>
 *
 * @author isv, qiucx0161, nhzp339, magicpig
 * @version 1.1
 * @since 1.0
 */
public class DBConnectionException extends BaseException {
    /**
     * <p>
     * Constructs new <code>DBConnectionException</code> with the given message providing the
     * details of the error occurred.
     * </p>
     *
     * @param message
     *            a <code>String</code> providing the details of the error occurred while
     *            obtaining a connection to a database; it can be any <code>String</code> instance
     *            including null and empty string.
     */
    public DBConnectionException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new <code>DBConnectionException</code>, with the given message and cause.
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
    public DBConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Constructs new <code>DBConnectionException</code> with the given cause.
     * </p>
     *
     * @param cause
     *            a <code>Throwable</code> representing the original cause of this exception; it
     *            can be any <code>Throwable</code> instance including null.
     */
    public DBConnectionException(Throwable cause) {
        super(cause);
    }
}
