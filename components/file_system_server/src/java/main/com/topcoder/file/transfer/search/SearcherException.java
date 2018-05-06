/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

import com.topcoder.util.errorhandling.BaseException;

/**
 * This is the base class for all the exceptions in the search package.
 * @author Luca, FireIce
 * @version 1.0
 */
public class SearcherException extends BaseException {

    /**
     * Creates an instance with the given argument.
     * @param message
     *            a descriptive message
     */
    public SearcherException(String message) {
        super(message);
    }

    /**
     * Creates an instance with the given arguments.
     * @param message
     *            a descriptive message
     * @param cause
     *            the exception cause
     */
    public SearcherException(String message, Exception cause) {
        super(message, cause);
    }
}
