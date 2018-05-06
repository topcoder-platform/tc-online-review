/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is thrown by implementations of DistributionScriptParser when
 * some error occurs while parsing a distribution script obtained from the given
 * input stream.
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
public class DistributionScriptParsingException extends
        DistributionToolException {
    /**
     * <p>
     * Creates exception with the given error message.
     * </p>
     *
     * @param message
     *            the error message
     */
    public DistributionScriptParsingException(String message) {
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
    public DistributionScriptParsingException(String message, Throwable cause) {
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
    public DistributionScriptParsingException(String message, ExceptionData data) {
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
    public DistributionScriptParsingException(String message, Throwable cause,
            ExceptionData data) {
        super(message, cause, data);
    }
}
