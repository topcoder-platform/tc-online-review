/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is thrown by <code>LateDeliverablesTracker</code> and implementations
 * of <code>LateDeliverablesRetriever</code> when some error occurs while retrieving a
 * list of late deliverables.
 * </p>
 * <p>
 * Thread Safety: This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author saarixx, myxgyy
 * @version 1.0
 */
public class LateDeliverablesRetrievalException extends LateDeliverablesTrackingException {
    /**
     * The generated serial version UID.
     */
    private static final long serialVersionUID = 7272950210763277633L;

    /**
     * Creates a new instance of this exception with the given message.
     *
     * @param message
     *            the detailed error message of this exception.
     */
    public LateDeliverablesRetrievalException(String message) {
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
    public LateDeliverablesRetrievalException(String message, Throwable cause) {
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
    public LateDeliverablesRetrievalException(String message, ExceptionData data) {
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
    public LateDeliverablesRetrievalException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
