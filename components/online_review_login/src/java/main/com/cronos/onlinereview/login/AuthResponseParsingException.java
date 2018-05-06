/*
 * Copyright (C) 2006-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login;

import com.topcoder.util.errorhandling.BaseException;

/**
 * This exception is thrown by <code>AuthResponseParser</code> interface and its implementation if any error occurred
 * when they set or unset the login state.
 *
 * @author woodjohn, maone, TCSASSEMBLER
 * @version 2.0
 */
public class AuthResponseParsingException extends BaseException {

    /**
     * Create an instance with error message.
     *
     * @param msg
     *            the error message
     */
    public AuthResponseParsingException(String msg) {
        super(msg);
    }

    /**
     * Create an instance with error message and cause exception.
     *
     * @param msg
     *            the error message
     * @param cause
     *            the cause exception
     */
    public AuthResponseParsingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
