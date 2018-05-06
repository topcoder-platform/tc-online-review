/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is thrown by <code>LateDeliverablesTracker</code> and implementations
 * of <code>LateDeliverableProcessor</code> when some error occurs while processing a
 * late deliverable. Also this exception is used as a base class for other implementation
 * specific custom exceptions.
 * </p>
 * <p>
 * Thread Safety: This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author saarixx, myxgyy
 * @version 1.0
 */
public class LateDeliverablesProcessingException extends LateDeliverablesTrackingException {
    /**
     * The generated serial version UID.
     */
    private static final long serialVersionUID = 209369387359047457L;

    /**
     * Creates a new instance of this exception with the given message.
     *
     * @param message
     *            the detailed error message of this exception.
     */
    public LateDeliverablesProcessingException(String message) {
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
    public LateDeliverablesProcessingException(String message, Throwable cause) {
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
    public LateDeliverablesProcessingException(String message, ExceptionData data) {
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
    public LateDeliverablesProcessingException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}