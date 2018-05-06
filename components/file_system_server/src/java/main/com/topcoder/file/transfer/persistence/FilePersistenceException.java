/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.persistence;

import com.topcoder.util.errorhandling.BaseException;

/**
 * Thrown by the FilePersistence and BytesIterator's methods if an exception occurs while performing some operation.
 * @author Luca, FireIce
 * @version 1.0
 */
public class FilePersistenceException extends BaseException {

    /**
     * Creates an instance with the given argument.
     * @param message
     *            a descriptive message
     */
    public FilePersistenceException(String message) {
        super(message);
    }

    /**
     * Creates an instance with the given arguments.
     * @param message
     *            a descriptive message
     * @param cause
     *            the exception cause
     */
    public FilePersistenceException(String message, Exception cause) {
        super(message, cause);
    }
}
