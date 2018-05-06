/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review;

import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * This exception will be thrown by ReviewManager, ReviewPersistence and their implementations if any error occurs
 * during the management of review entities.
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Extends BaseCriticalException instead of BaseException.</li>
 * <li>Renamed "msg" parameters to "message".</li>
 * <li>Added constructors that accept ExceptionData.</li>
 * <li>Added thread safety information.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author woodjhon, urtks, saarixx, sparemax
 * @version 1.2
 */
@SuppressWarnings("serial")
public class ReviewManagementException extends BaseCriticalException {
    /**
     * Creates a new instance of this exception with the given message.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Renamed "msg" parameter to "message".</li>
     * </ol>
     * </p>
     *
     * @param message
     *            the detailed error message of this exception.
     */
    public ReviewManagementException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of this exception with the given message and cause.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Renamed "msg" parameter to "message".</li>
     * </ol>
     * </p>
     *
     * @param message
     *            the detailed error message of this exception.
     * @param cause
     *            the inner cause of this exception.
     */
    public ReviewManagementException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance of this exception with the given message and exception data.
     *
     * @param message
     *            the detailed error message of this exception.
     * @param data
     *            the exception data.
     *
     * @since 1.2
     */
    public ReviewManagementException(String message, ExceptionData data) {
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
     *
     * @since 1.2
     */
    public ReviewManagementException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
