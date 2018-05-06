/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence;

/**
 * <p>
 * This exception is thrown by <c>ConfigurationFileManager</c> when an attempt is made to load more
 * than one file into a single namespace.
 * </p>
 * <p>
 * This class derives from <c>ConfigurationPersistenceException</c>, which is mutable and not
 * thread safe. It is up to the application to handle exceptions in a thread safe manner.
 * </p>
 *
 * @author bendlund, rainday
 * @version 1.0
 *
 */
public class NamespaceConflictException extends ConfigurationPersistenceException {

    /**
     * <p>
     * This is a single-argument constructor for this exception that provides a message.
     * </p>
     *
     * @param message
     *            A string representing the message for this exception
     */
    public NamespaceConflictException(String message) {
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
    public NamespaceConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
