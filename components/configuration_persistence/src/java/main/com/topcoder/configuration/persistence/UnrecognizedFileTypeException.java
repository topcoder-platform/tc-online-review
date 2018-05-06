/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence;

/**
 * <p>
 * This exception is thrown by <c>ConfigurationFileManager</c> when an attempt is made to load
 * configuration from a File with a FileType for which the ConfigurationFileManager has not been
 * configured.
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
public class UnrecognizedFileTypeException extends ConfigurationPersistenceException {

    /**
     * <p>
     * Represents the file name for which ConfigurationFileManager was not configured. Set in the
     * constructor and doesn't change. Accessed through the getUnrecognizedType() method. Might be
     * null.
     * </p>
     *
     */
    private final String unrecognizedType;

    /**
     * <p>
     * This is a single-argument constructor for this exception that provides a message and the
     * FileType that was not recognized by ConfigurationFileManager.
     * </p>
     *
     * @param message
     *            A string representing the message for this exception
     * @param type
     *            A file name that was of an unrecognized file type
     */
    public UnrecognizedFileTypeException(String message, String type) {
        super(message);
        unrecognizedType = type;
    }

    /**
     * <p>
     * Returns the extension that caused this exception.
     * </p>
     *
     * @return the unrecognized file name
     */
    public String getUnrecognizedType() {
        return unrecognizedType;
    }
}
