/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * This is the base exception for all exceptions in the <b>User Project Data Store</b> component.
 * </p>
 * <p>
 * It can be used with or without a message, and with or without an underlying cause.
 * </p>
 *
 * @author dplass, oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class UserProjectDataStoreException extends BaseException {

    /**
     * <p>
     * Creates this exception with no message.
     * </p>
     */
    public UserProjectDataStoreException() {
        super();
    }

    /**
     * <p>
     * Creates this exception with the given message.
     * </p>
     *
     * @param message
     *            The message of this exception. May be null or empty after trim.
     */
    public UserProjectDataStoreException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates this exception with the given message and underlying cause.
     * </p>
     *
     * @param message
     *            The message of this exception. May be null or empty after trim.
     * @param cause
     *            the underlying cause of the exception.
     */
    public UserProjectDataStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
