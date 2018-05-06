/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot;

/**
 * <p>
 * This exception is thrown by AutoPilotSource implementations when an error occurs while retrieving
 * project ids for the source of an auto pilot.
 * </p>
 * <p>
 * This class is thread-safe because it's immutable.
 * </p>
 * @author sindu, abelli
 * @version 1.0
 */
public class AutoPilotSourceException extends AutoPilotException {

    /**
     * <p>
     * Constructs a new instance of AutoPilotSourceException.
     * </p>
     */
    public AutoPilotSourceException() {
    }

    /**
     * <p>
     * Constructs a new instance of AutoPilotSourceException with the given error message.
     * </p>
     * @param message the error message
     */
    public AutoPilotSourceException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new instance of AutoPilotSourceException with the given error message and inner
     * cause.
     * </p>
     * @param message the error message
     * @param cause the inner cause
     */
    public AutoPilotSourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
