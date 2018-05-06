/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot;

/**
 * <p>
 * This exception should be thrown by constructors of AutoPilot/AutoPilotJob and the various
 * interface implementations if any error occurs configuring itself. also thrown by constructors of
 * AutoPilotSource/ProjectPilot implementations.
 * </p>
 * <p>
 * This class is thread-safe because it's stateless.
 * </p>
 * @author sindu, abelli
 * @version 1.0
 */
public class ConfigurationException extends AutoPilotException {

    /**
     * <p>
     * Constructs a new instance of ConfigurationException.
     * </p>
     */
    public ConfigurationException() {
    }

    /**
     * <p>
     * Constructs a new instance of ConfigurationException with the given error message.
     * </p>
     * @param message the error message
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new instance of ConfigurationException with the given error message and inner
     * cause.
     * </p>
     * @param message the error message
     * @param cause the inner cause
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
