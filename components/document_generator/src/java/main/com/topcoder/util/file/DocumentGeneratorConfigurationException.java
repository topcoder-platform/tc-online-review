/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.errorhandling.BaseNonCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * Thrown by DocumentGeneratorFacotry to indicate that any error occurs during configure DocumentGenerator.
 * <p>
 * Thread Safety : this is not thread safe, since base class is not thread safe.
 * </p>
 * @author gniuxiao, TCSDEVELOPER
 * @version 3.0
 */
public class DocumentGeneratorConfigurationException extends BaseNonCriticalException {
    /**
     * Creates an instance with the specified detail message.
     * @param message
     *            the exception message
     */
    public DocumentGeneratorConfigurationException(String message) {
        super(message);
    }

    /**
     * Creates an instance with the inner cause.
     * @param cause
     *            the cause of the exception
     */
    public DocumentGeneratorConfigurationException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates an instance with the specified detail message and inner cause.
     * @param message
     *            the exception message
     * @param cause
     *            the cause of the exception
     */
    public DocumentGeneratorConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an instance with the specified detail message, the inner cause, and the additional data to
     * attach to the exception.
     * @param data
     *            the additional data to attach to the exception
     * @param message
     *            the exception message
     * @param cause
     *            the cause of the exception
     */
    public DocumentGeneratorConfigurationException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
