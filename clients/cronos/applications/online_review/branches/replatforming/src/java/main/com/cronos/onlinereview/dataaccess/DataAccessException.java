/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.dataaccess;

import com.topcoder.util.errorhandling.BaseRuntimeException;

/**
 * <p>A runtime unchecked exception to be thrown in case there is an unexpected error encountered while running the
 * data access queries.</p>
 *
 * @author isv
 * @version 1.0
 */
public class DataAccessException extends BaseRuntimeException {

    /**
     * Constructs a new <code>DataAccessException</code>.
     */
    public DataAccessException() {
    }

    /**
     * Constructs a new <code>DataAccessException</code>, with the given message.
     *
     * @param message descriptive message.
     */
    public DataAccessException(String message) {
        super(message);
    }

    /**
     * Constructs a new <code>DataAccessException</code>, with the given message and cause.
     *
     * @param message descriptive message.
     * @param cause <code>Throwable</code> cause of this exception.
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new <code>DataAccessException</code>, with the given cause.
     *
     * @param cause <code>Throwable</code> cause of this exception.
     */
    public DataAccessException(Throwable cause) {
        super(cause);
    }
}
