/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.registry;

/**
 * This exception is thrown by the FileSystemXmlRegistry's constructors if an exception occurs while initializing the
 * object.
 * @author Luca, FireIce
 * @version 1.0
 */
public class RegistryConfigurationException extends RegistryException {

    /**
     * Creates an instance with the given argument.
     * @param message
     *            a descriptive message
     */
    public RegistryConfigurationException(String message) {
        super(message);
    }

    /**
     * Creates an instance with the given arguments. Calls .
     * @param message
     *            a descriptive message
     * @param cause
     *            the exception cause
     */
    public RegistryConfigurationException(String message, Exception cause) {
        super(message, cause);
    }
}
