/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * This exception will be thrown when there is an issue with the persistence of the jobs and groups in the
 * <code>Scheduler</code>.
 * </p>
 *
 * <p>
 * Thread-Safety: This class is thread safe as it has no state and its
 * super class is also thread safe.
 * </p>
 *
 * @author argolite, TCSDEVELOPER
 * @version 3.0
 * @since 3.0
 */
public class SchedulingException extends BaseException {
    /**
     * <p>
     * Constructs the exception taking a message as to why it was thrown.
     * </p>
     *
     * <p>
     * The message needs to be meaningful, so that the user will benefit from
     * meaningful messages.
     * </p>
     *
     * @param message A descriptive message of why it was thrown.
     */
    public SchedulingException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs the exception taking a message as to why it was thrown,
     * as well as an original cause.
     * </p>
     *
     * <p>
     * The message needs to be meaningful, so that the user will benefit from
     * meaningful messages.
     * </p>
     *
     * <p>
     * The cause is the inner exception.
     * </p>
     *
     * @param message A descriptive message of why it was thrown.
     * @param cause The exception or error that originally caused this to be thrown.
     */
    public SchedulingException(String message, Throwable cause) {
        super(message, cause);
    }
}
