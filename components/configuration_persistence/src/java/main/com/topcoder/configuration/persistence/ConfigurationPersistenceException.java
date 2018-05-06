/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * This class is a base exception for the custom exceptions defined in this component. It is not
 * thrown directly by any class.
 * </p>
 * <p>
 * This class derives from TopCoder <c>BaseException</c>, which is mutable and not thread safe. It
 * is up to the application to handle exceptions in a thread safe manner.
 * </p>
 *
 * @author bendlund, rainday
 * @version 1.0
 *
 */
public class ConfigurationPersistenceException extends BaseException {
    /**
     * <p>
     * This is a single-argument constructor for this exception that provides a message.
     * </p>
     *
     * @param message
     *            A string representing the message for this exception
     */
    public ConfigurationPersistenceException(String message) {
        super(message);
    }

    /**
     * <p>
     * This is a two-argument constructor for this exception that provides a message and a cause.
     * </p>
     *
     * @param message
     *            A string representing the message for this exception
     * @param cause
     *            An exception representing the cause of this exception
     */
    public ConfigurationPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
