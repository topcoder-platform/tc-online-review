/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.registry;

import com.topcoder.util.errorhandling.BaseException;

/**
 * This is the base exception for the exceptions in the registry package. It is also thrown by the FileSystemRegistry's
 * methods if an exception occured while performing the operations.
 * @author Luca, FireIce
 * @version 1.0
 */
public class RegistryException extends BaseException {

    /**
     * Creates an instance with the given argument.
     * @param message
     *            a descriptive message
     */
    public RegistryException(String message) {
        super(message);
    }

    /**
     * Creates an instance with the given arguments.
     * @param message
     *            a descriptive message
     * @param cause
     *            the exception cause
     */
    public RegistryException(String message, Exception cause) {
        super(message, cause);
    }
}
