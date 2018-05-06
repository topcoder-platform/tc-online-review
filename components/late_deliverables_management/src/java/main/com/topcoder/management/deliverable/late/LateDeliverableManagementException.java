/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late;

import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is thrown by implementations of LateDeliverableManager when some not expected error occurred. Also
 * this exception is used as a base class for other specific custom exceptions.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LateDeliverableManagementException extends BaseCriticalException {

    /**
     * <p>
     * Constructor with error message.
     * </p>
     *
     * @param message
     *            the explanation of the error. Can be empty string or <code>null</code> (useless, but allowed).
     */
    public LateDeliverableManagementException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructor with error message and inner cause.
     * </p>
     *
     * @param message
     *            the explanation of the error. Can be empty string or <code>null</code> (useless, but allowed).
     * @param cause
     *            the underlying cause of the error. Can be <code>null</code>, which means that initial exception is
     *            nonexistent or unknown.
     */
    public LateDeliverableManagementException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Constructor with error message and exception data.
     * </p>
     *
     * @param message
     *            the explanation of the error. Can be empty string or <code>null</code> (useless, but allowed).
     * @param data
     *            the additional data to attach to the exception. If this is <code>null</code>, a new ExceptionData()
     *            will be automatically used instead.
     */
    public LateDeliverableManagementException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * <p>
     * Constructor with error message, inner cause and exception data.
     * </p>
     *
     * @param message
     *            the explanation of the error. Can be empty string or <code>null</code> (useless, but allowed).
     * @param cause
     *            the underlying cause of the error. Can be <code>null</code>, which means that initial exception is
     *            nonexistent or unknown.
     * @param data
     *            the additional data to attach to the exception. If this is <code>null</code>, a new ExceptionData()
     *            will be automatically used instead.
     */
    public LateDeliverableManagementException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
