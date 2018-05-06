/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is thrown by ExecuteProcessCommand when some error occurs
 * while starting an external process.
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
public class ProcessExecutionException extends
        DistributionScriptCommandExecutionException {
    /**
     * <p>
     * Creates exception with the given error message.
     * </p>
     *
     * @param message
     *            the error message
     */
    public ProcessExecutionException(String message) {
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
    public ProcessExecutionException(String message, Throwable cause) {
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
    public ProcessExecutionException(String message, ExceptionData data) {
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
    public ProcessExecutionException(String message, Throwable cause,
            ExceptionData data) {
        super(message, cause, data);
    }
}
