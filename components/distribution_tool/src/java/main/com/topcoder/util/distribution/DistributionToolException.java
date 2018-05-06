/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is a base class for all other non-runtime custom exceptions
 * defined in this component. It is never thrown directly, subclasses are used
 * instead.
 * </p>
 *
 * <p>
 * Thread Safety: This class is not thread safe because its base class is not
 * thread safe.
 * </p>
 *
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class DistributionToolException extends BaseCriticalException {
    /**
     * <p>
     * Creates exception with the given error message.
     * </p>
     *
     * @param message
     *            the error message
     */
    public DistributionToolException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates exception with the given error message and cause exception.
     * </p>
     *
     * @param message
     *            the error message
     * @param cause
     *            the cause exception
     */
    public DistributionToolException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Creates exception with the given error message and exception data.
     * </p>
     *
     * @param message
     *            the error message
     * @param data
     *            the exception data
     */
    public DistributionToolException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * <p>
     * Creates exception with the given error message, cause exception and
     * exception data.
     * </p>
     *
     * @param message
     *            the error message
     * @param cause
     *            the cause exception
     * @param data
     *            the exception data
     */
    public DistributionToolException(String message, Throwable cause,
            ExceptionData data) {
        super(message, cause, data);
    }
}
