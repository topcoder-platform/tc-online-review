/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.registry;

/**
 * This exception is thrown by the FileSystemRegistry if an exception occurs while saving changes in the persistent
 * storage.
 * @author Luca, FireIce
 * @version 1.0
 */
public class RegistryPersistenceException extends RegistryException {

    /**
     * Creates an instance with the given argument.
     * @param message
     *            a descriptive message
     */
    public RegistryPersistenceException(String message) {
        super(message);
    }

    /**
     * Creates an instance with the given arguments.
     * @param message
     *            a descriptive message
     * @param cause
     *            the exception cause
     */
    public RegistryPersistenceException(String message, Exception cause) {
        super(message, cause);
    }
}
