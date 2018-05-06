/*
 * Copyright (C) 2010-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * This exception is thrown by LoginActions when some error occurs while
 * setting, removing or checking the authentication cookie.
 * </p>
 * <p>
 * <b> Thread Safety:</b> This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author saarixx, TCSASSEMBLER
 * @version 2.0
 */
public class AuthCookieManagementException extends BaseException {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 7835688310694350754L;

    /**
     * Creates a new instance of this exception with the given message.
     *
     * @param message
     *            the detailed error message of this exception
     */
    public AuthCookieManagementException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of this exception with the given message and cause.
     *
     * @param message
     *            the detailed error message of this exception
     * @param cause
     *            the inner cause of this exception
     */
    public AuthCookieManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}
