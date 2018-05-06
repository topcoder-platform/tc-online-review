/*
 * Copyright (C) 2006-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login;

import com.topcoder.util.errorhandling.BaseException;

/**
 * This exception is thrown by the constructors of the classes in TCS Online Review Login Component which failed to load
 * the configuration values from the configuration file, or the configuration values are invalid.
 * <p>
 * Currently, it can be thrown from the constructors of <code>LoginActions</code>,
 * <code>SecurityManagerAuthenticator</code>, and <code>SecurityManagerAuthResponseParser</code>.
 * </p>
 *
 * @author woodjohn, maone, TCSASSEMBLER
 * @version 2.0
 */
public class ConfigurationException extends BaseException {

    /**
     * Create an instance with error message.
     *
     * @param msg
     *            the error message
     */
    public ConfigurationException(String msg) {
        super(msg);
    }

    /**
     * Create an instance with error message and cause exception.
     *
     * @param msg
     *            the error message
     * @param cause
     *            the cause exception
     */
    public ConfigurationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
