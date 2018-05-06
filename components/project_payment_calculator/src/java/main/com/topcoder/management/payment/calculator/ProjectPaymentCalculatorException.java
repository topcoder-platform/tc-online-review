/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator;

import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;


/**
 * <p>
 * This exception extends <code>BaseCriticalException</code>. It is thrown by implementations of
 * <code>ProjectPaymentCalculator</code> when some error occurred during processing.
 * </p>
 * <p>
 * <b>Thread Safety:</b><br/>
 * This class is not thread safe as its base class is not thread safe.
 * </p>
 *
 * @author Schpotsky, TCSDEVELOPER
 * @version 1.0
 */
public class ProjectPaymentCalculatorException extends BaseCriticalException {
    /**
     * <p>
     * The serial version UID.
     * </p>
     */
    private static final long serialVersionUID = -6464692926412058650L;

    /**
     * <p>
     * Creates a new instance of this exception with the given error message.
     * </p>
     *
     * @param message
     *            The error message of the exception.
     */
    public ProjectPaymentCalculatorException(String message) {
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
    public ProjectPaymentCalculatorException(String message, Throwable cause) {
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
    public ProjectPaymentCalculatorException(String message, ExceptionData data) {
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
    public ProjectPaymentCalculatorException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
