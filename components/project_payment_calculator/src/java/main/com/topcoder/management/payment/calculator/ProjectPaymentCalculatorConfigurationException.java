/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator;

import com.topcoder.util.errorhandling.BaseRuntimeException;
import com.topcoder.util.errorhandling.ExceptionData;


/**
 * <p>
 * This exception extends <code>BaseRuntimeException</code>. It is thrown by
 * <code>BaseProjectPaymentCalculator</code> and its subclasses when there is some error during configuration
 * (configuration file can not be opened, required configuration parameter not found or has invalid value).
 * </p>
 * <p>
 * <b>Thread Safety:</b><br/>
 * This class is not thread safe as its base class is not thread safe.
 * </p>
 *
 * @author Schpotsky, TCSDEVELOPER
 * @version 1.0
 */
public class ProjectPaymentCalculatorConfigurationException extends BaseRuntimeException {
    /**
     * <p>
     * The serial version UID.
     * </p>
     */
    private static final long serialVersionUID = 3048908716306642270L;

    /**
     * <p>
     * Creates a new instance of this exception with the given error message.
     * </p>
     *
     * @param message
     *            The error message of the exception.
     */
    public ProjectPaymentCalculatorConfigurationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new instance of this exception with the given error message and cause.
     * </p>
     *
     * @param message
     *            The error message of the exception.
     * @param cause
     *            The inner cause of the exception.
     */
    public ProjectPaymentCalculatorConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Creates a new instance of this exception with the given error message and exception data.
     * </p>
     *
     * @param message
     *            The error message of the exception.
     * @param data
     *            The exception data.
     */
    public ProjectPaymentCalculatorConfigurationException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * <p>
     * Creates a new instance of this exception with the given error message, cause and exception data.
     * </p>
     *
     * @param message
     *            The error message of the exception.
     * @param cause
     *            The inner cause of the exception.
     * @param data
     *            The exception data.
     */
    public ProjectPaymentCalculatorConfigurationException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
