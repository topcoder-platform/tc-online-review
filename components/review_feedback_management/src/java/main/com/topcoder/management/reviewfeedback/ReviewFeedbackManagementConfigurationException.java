/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback;

import com.topcoder.util.errorhandling.BaseRuntimeException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This runtime exception indicates error with component configuration.
 * </p>
 *
 * <p>
 * <strong>Thread safety</strong>: This class inherits from not thread-safe class, so it's not thread-safe.
 * </p>
 *
 * @author gevak, amazingpig
 * @version 1.0
 */
public class ReviewFeedbackManagementConfigurationException extends BaseRuntimeException {
    /**
    * <p>
    * The serial version uid.
    * </p>
    */
    private static final long serialVersionUID = 6414018437810024588L;

    /**
     * Creates instance with specified error message. Simply delegate to base class constructor with same signature.
     *
     * @param message Error message.
     */
    public ReviewFeedbackManagementConfigurationException(String message) {
        super(message);
    }

    /**
     * Creates instance with specified error message and inner cause. Simply delegate to base class constructor
     * with same signature.
     *
     * @param message Error message.
     * @param cause Inner cause.
     */
    public ReviewFeedbackManagementConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates instance with specified error message and exception data. Simply delegate to base class constructor
     * with same signature.
     *
     * @param message Error message.
     * @param data Exception data.
     */
    public ReviewFeedbackManagementConfigurationException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * Creates instance with specified error message, inner cause and exception data. Simply delegate to base class
     * constructor with same signature.
     *
     * @param message Error message.
     * @param cause Inner cause.
     * @param data Exception data.
     */
    public ReviewFeedbackManagementConfigurationException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
