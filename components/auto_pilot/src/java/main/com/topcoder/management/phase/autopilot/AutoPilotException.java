/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * This is the base exception of all other exceptions thrown by this component. It's not thrown
 * directly by any classes.<br>
 * This class is thread-safe because it's stateless.
 * </p>
 * @author sindu, abelli
 * @version 1.0
 */
public class AutoPilotException extends BaseException {

    /**
     * <p>
     * Constructs a new instance of AutoPilotException.
     * </p>
     */
    public AutoPilotException() {
        super();
    }

    /**
     * <p>
     * Constructs a new instance of AutoPilotException with the given error message.
     * </p>
     * @param message the error message
     */
    public AutoPilotException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new instance of AutoPilotException with the given error message and inner cause.
     * </p>
     * @param message the error message
     * @param cause the inner cause
     */
    public AutoPilotException(String message, Throwable cause) {
        super(message, cause);
    }
}
