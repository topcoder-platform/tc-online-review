/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is a base class for all other custom checked exceptions defined in this
 * component. It is never thrown directly, subclasses are used instead.
 * </p>
 * <p>
 * Thread Safety: This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author saarixx, myxgyy
 * @version 1.0
 */
public class LateDeliverablesTrackingException extends BaseCriticalException {
    /**
     * The generated serial version UID.
     */
    private static final long serialVersionUID = 2319808728564106L;

    /**
     * Creates a new instance of this exception with the given message.
     *
     * @param message
     *            the detailed error message of this exception
     */
    public LateDeliverablesTrackingException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of this exception with the given message and cause.
     *
     * @param message
     *            the detailed error message of this exception.
     * @param cause
     *            the inner cause of this exception.
     */
    public LateDeliverablesTrackingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance of this exception with the given message and exception data.
     *
     * @param message
     *            the detailed error message of this exception.
     * @param data
     *            the exception data.
     */
    public LateDeliverablesTrackingException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * Creates a new instance of this exception with the given message, cause and
     * exception data.
     *
     * @param message
     *            the detailed error message of this exception.
     * @param cause
     *            the inner cause of this exception.
     * @param data
     *            the exception data.
     */
    public LateDeliverablesTrackingException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
