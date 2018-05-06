/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * This exception will be thrown from the
 * <code>ScheduledEnableObjectFactory</code> interface's implementations
 * if any error occurs when creating the running object.
 * </p>
 * <p>
 * Thread-Safety: This class is not thread safe because its parent is not thread
 * safe.
 * </p>
 * @author Standlove, fuyun
 * @version 3.1
 * @since 3.1
 */
public class ScheduledEnableObjectCreationException extends BaseException {

    /**
     * <p>
     * Constructs the exception taking a message as to why it was thrown.
     * </p>
     * @param message A descriptive message of why it was thrown.
     */
    public ScheduledEnableObjectCreationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs the exception taking a message as to why it was thrown, as
     * well as an original cause.
     * </p>
     * <p>
     * The message needs to be meaningful, so that the user will benefit from
     * meaningful messages.
     * </p>
     * <p>
     * The cause is the inner exception.
     * </p>
     * @param message A descriptive message of why it was thrown.
     * @param cause The exception or error that originally caused this to be
     *            thrown.
     */
    public ScheduledEnableObjectCreationException(String message,
            Throwable cause) {
        super(message, cause);
    }
}
