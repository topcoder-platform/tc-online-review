/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * This exception indicates error with preparing or sending a notification.
 *
 * <p>
 * Thread Safety: This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author gevak, zhongqiangzhang
 * @version 1.0
 */
public class ReviewAssignmentNotificationException extends ReviewAssignmentException {
    /**
     * the serial version id.
     */
    private static final long serialVersionUID = 2567812353229274878L;

    /**
     * Creates a new instance of this exception with the given message.
     *
     * @param message
     *            the detailed error message of this exception
     */
    public ReviewAssignmentNotificationException(String message) {
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
    public ReviewAssignmentNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance of this exception with the given message and exception data.
     *
     * @param message
     *            the detailed error message of this exception
     * @param data
     *            the exception data
     */
    public ReviewAssignmentNotificationException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * Creates a new instance of this exception with the given message, cause and exception data.
     *
     * @param message
     *            the detailed error message of this exception
     * @param cause
     *            the inner cause of this exception
     * @param data
     *            the exception data
     */
    public ReviewAssignmentNotificationException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
