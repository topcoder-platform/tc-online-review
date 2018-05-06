/*
 * Copyright (C) 2009-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is thrown by a phase handler when the input phase is not the type the handler can handle. For
 * example, ScreeningPhaseHandler can only handle Screening phases.
 * </p>
 * <p>
 * It is used in phase handler classes.
 * </p>
 * <p>
 * Thread safety: This class is immutable and thread safe.
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>Added new constructors to meet TopCoder standards</li>
 * </ul>
 * </p>
 * @author tuenm, saarixx, bose_java, microsky
 * @version 1.6.1
 */
public class PhaseNotSupportedException extends PhaseHandlingException {
    /**
     * The generated serial version UID.
     */
    private static final long serialVersionUID = -2534347268584751918L;

    /**
     * Create a new PhaseNotSupportedException instance with the specified error message.
     * @param message the error message of the exception
     */
    public PhaseNotSupportedException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified message and cause.
     * @param message the error message of the exception
     * @param cause the internal exception that caused this exception
     * @since 1.6.1
     */
    public PhaseNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance of this exception with the given message and exception data.
     * @param message the error message of the exception
     * @param data the exception data
     * @since 1.6.1
     */
    public PhaseNotSupportedException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * Creates a new instance of this exception with the given message, cause and exception data.
     * @param message the error message of the exception
     * @param cause the internal exception that caused this exception
     * @param data the exception data
     * @since 1.6.1
     */
    public PhaseNotSupportedException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
