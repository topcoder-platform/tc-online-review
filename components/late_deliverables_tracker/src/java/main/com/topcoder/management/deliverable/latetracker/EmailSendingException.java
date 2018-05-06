/*
 * Copyright (C) 2010, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is thrown by <code>LateDeliverableProcessorImpl</code> and <code>EmailSendingUtility</code> when
 * some error occurs while sending a notification email message.
 * </p>
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Moved from com.topcoder.management.deliverable.latetracker.processors to
 * com.topcoder.management.deliverable.latetracker package.</li>
 * </ol>
 * </p>
 *
 * <p>
 * Thread Safety: This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author saarixx, myxgyy, sparemax
 * @version 1.2
 */
public class EmailSendingException extends LateDeliverablesProcessingException {
    /**
     * The generated serial version UID.
     */
    private static final long serialVersionUID = -3777785189833683771L;

    /**
     * Creates a new instance of this exception with the given message.
     *
     * @param message
     *            the detailed error message of this exception.
     */
    public EmailSendingException(String message) {
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
    public EmailSendingException(String message, Throwable cause) {
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
    public EmailSendingException(String message, ExceptionData data) {
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
    public EmailSendingException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
