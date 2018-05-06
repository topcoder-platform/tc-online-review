/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This exception is thrown by DefaultPhaseManager and implementations of PhaseHandler when an error occurs while
 * performing the phase transition or while determining whether the operation can be performed.
 * </p>
 * <p>
 * Changes in 1.1:
 * <ol>
 * <li>Added new constructors to meet TopCoder standards</li>
 * </ol>
 * </p>
 * <p>
 * Thread Safety: This class is not thread safe because its base class is not thread safe.
 * </p>
 * @author saarixx, RachaelLCook, sokol
 * @version 1.1
 */
@SuppressWarnings("serial")
public class PhaseHandlingException extends PhaseManagementException {

    /**
     * Constructs a new exception with the specified message.
     * @param message the reason for the exception
     */
    public PhaseHandlingException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified message and wrapped exception.
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Renamed "ex" parameter to "cause"</li>
     * </ol>
     * </p>
     * @param message the reason for the exception
     * @param cause the wrapped exception
     */
    public PhaseHandlingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance of this exception with the given message and exception data.
     * @param message the detailed error message of this exception
     * @param data the exception data
     * @since 1.1
     */
    public PhaseHandlingException(String message, ExceptionData data) {
        super(message, data);
    }

    /**
     * <p>
     * Creates a new instance of this exception with the given message, cause and exception data.
     * </p>
     * @param message the detailed error message of this exception
     * @param cause the inner cause of this exception
     * @param data the exception data
     * @since 1.1
     */
    public PhaseHandlingException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
