/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

/**
 * <p>
 * An exception to be thrown by: <br>
 * (1){@link DBConnectionFactory#createConnection(String)} and {@link
 * DBConnectionFactoryImpl#createConnection(String, String, String)} methods if the connection
 * corresponding to specified name is not configured within the factory. <br>
 * (2) the two constructors with <code>ConfigurationObject</code> and the constructor with
 * namespace, if the default producer is not contained. <br>
 * </p>
 * <p>
 * This exception should provide a meaningful message providing the client with the description of
 * the error. This exception will always provide a name of the requested connection which is not
 * configured within the factory (however such a name could be <code>null</code>).
 * </p>
 * <p>
 * Side Note : Empty String means the length of string after trimming equals to zero.
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> this class is thread safe since it is immutable and the base
 * exception <code>DBConnectionException</code> is thread safe.
 * </p>
 *
 * @author isv, qiucx0161, nhzp339, magicpig
 * @version 1.1
 * @since 1.0
 */
public class UnknownConnectionException extends DBConnectionException {
    /**
     * <p>
     * A <code>String</code> representing the name of the requested connection which is not
     * configured within the factory.
     * </p>
     * <p>
     * This variable is initialized within the constructor and is never changed during the lifetime
     * of <code> UnknownConnectionException</code> instance. <code>Null</code> and Empty string
     * values are permitted. It can be gotten via the {@link #getName()} method.
     * </p>
     */
    private final String name;

    /**
     * <p>
     * Constructs new <code>UnknownConnectionException</code> with specified name of the requested
     * connection and a textual description of the error.
     * </p>
     *
     * @param name
     *            a <code>String</code> providing the name of the requested connection which is
     *            not configured within the factory; it can be any <code>String</code> instance
     *            including null and empty string.
     * @param message
     *            a <code>String</code> providing the textual description of the error; it can be
     *            any <code>String</code> instance including null and empty string.
     */
    public UnknownConnectionException(String name, String message) {
        super(message);
        this.name = name;
    }

    /**
     * <p>
     * Constructs new <code>UnknownConnectionException</code> with specified name of the requested
     * connection, a textual description of the error and a cause of error.
     * </p>
     *
     * @param name
     *            a <code>String</code> providing the name of the requested connection which is
     *            not configured within the factory; it can be any <code>String</code> instance
     *            including null and empty string.
     * @param message
     *            a <code>String</code> providing the textual description of the error; it can be
     *            any <code>String</code> instance including null and empty string.
     * @param cause
     *            a <code>Throwable</code> representing the original cause of this exception; it
     *            can be any <code>Throwable</code> instance including null.
     */
    public UnknownConnectionException(String name, String message, Throwable cause) {
        super(message, cause);
        this.name = name;
    }

    /**
     * <p>
     * Gets the name of the requested connection which is not configured within the factory.
     * </p>
     *
     * @return a <code>String</code> providing the name of requested connection which is not
     *         configured within the factory. It can be any string including null or empty string.
     */
    public String getName() {
        return name;
    }
}
