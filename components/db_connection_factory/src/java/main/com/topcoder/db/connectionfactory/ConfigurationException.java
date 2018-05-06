/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * An exception to be thrown by <code>DBConnectionFactoryImpl</code> being initialized from a
 * parameters provided by a configuration namespace if any error occurs while reading the
 * configuration parameters, instantiating and initializing the <code>ConnectionProducers</code>.
 * This exception can also be thrown from the two constructors in
 * <code>DBConnectionFactoryImpl</code> class which accepts a <code>ConfigurationObject</code>
 * parameter to initialize the factory. Usually this exception will wrap the original exception
 * which caused the operation to fail. Such a cause may be obtained through {@link #getCause()}
 * method.
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
public class ConfigurationException extends BaseException {
    /**
     * <p>
     * Constructs new <code>ConfigurationException</code> with the given message providing the
     * details of the error occurred.
     * </p>
     *
     * @param message
     *            a <code>String</code> providing the details of the error occurred; it can be any
     *            <code>String</code> instance including null and empty string.
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new <code>ConfigurationException</code>, with the given message and cause.
     * </p>
     *
     * @param message
     *            a <code>String</code> providing the details of the error occurred; it can be any
     *            <code>String</code> instance including null and empty string.
     * @param cause
     *            a <code>Throwable</code> representing the original cause of this exception; it
     *            can be any <code>Throwable</code> instance including null.
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Constructs new <code>ConfigurationException</code> with the given cause.
     * </p>
     *
     * @param cause
     *            a <code>Throwable</code> representing the original cause of this exception; it
     *            can be any <code>Throwable</code> instance including null.
     */
    public ConfigurationException(Throwable cause) {
        super(cause);
    }
}
