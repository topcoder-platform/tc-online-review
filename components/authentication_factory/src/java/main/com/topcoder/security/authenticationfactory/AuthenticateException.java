/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * AuthenticateException is thrown from the Authenticator's authenticate method if some
 * unexpected errors occur during the authentication. It is used to encapsulate the exception
 * thrown from the underlying authentication implementation, such as the network fails for the
 * http authentication.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public class AuthenticateException extends BaseException {
    /**
     * <p>
     * Create an AuthenticateException instance with error message.
     * </p>
     *
     * @param message The String with the exception message.
     */
    public AuthenticateException(String message) {
        super(message);
    }

    /**
     * <p>
     * Create an AuthenticateException with error message and inner cause.
     * </p>
     *
     * @param message The String with the exception message.
     * @param cause The Throwable with the cause. The cause is the exception that led to this
     *        exception being thrown.
     */
    public AuthenticateException(String message, Throwable cause) {
        super(message, cause);
    }
}
