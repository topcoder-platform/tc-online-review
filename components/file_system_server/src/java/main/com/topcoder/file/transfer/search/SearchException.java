/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

/**
 * This exception is thrown if an error occurs while performing a search.
 * @author Luca, FireIce
 * @version 1.0
 */
public class SearchException extends SearcherException {

    /**
     * Creates an instance with the given argument.
     * @param message
     *            a descriptive message
     */
    public SearchException(String message) {
        super(message);
    }

    /**
     * Creates an instance with the given arguments.
     * @param message
     *            a descriptive message
     * @param cause
     *            the exception cause
     */
    public SearchException(String message, Exception cause) {
        super(message, cause);
    }
}
