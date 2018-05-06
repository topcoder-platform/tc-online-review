/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.notification;

import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackingException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is thrown by NotRespondedLateDeliverablesNotifier when some error occurs while retrieving not
 * responded late deliverables or sending email notifications to managers.
 * </p>
 *
 * <p>
 * Thread Safety: This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.2
 * @since 1.2
 */
@SuppressWarnings("serial")
public class NotRespondedLateDeliverablesNotificationException extends LateDeliverablesTrackingException {
    /**
     * Creates a new instance of this exception with the given message.
     *
     * @param message
     *            the detailed error message of this exception.
     */
    public NotRespondedLateDeliverablesNotificationException(String message) {
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
    public NotRespondedLateDeliverablesNotificationException(String message, Throwable cause) {
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
    public NotRespondedLateDeliverablesNotificationException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * Creates a new instance of this exception with the given message, cause and exception data.
     *
     * @param message
     *            the detailed error message of this exception.
     * @param cause
     *            the inner cause of this exception.
     * @param data
     *            the exception data.
     */
    public NotRespondedLateDeliverablesNotificationException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
