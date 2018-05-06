/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor;

import com.topcoder.util.errorhandling.BaseException;


/**
 * <p>This exception indicates a failure start or shutdown of the processor, but not used currently in this
 * component.</p>
 * <p>Thread safety: This class is thread-safe.</p>
 *
 * @author argolite, TCSDEVELOPER
 * @version 1.0
 */
public class JobProcessorException extends BaseException {
    /**
     * <p>
     * Constructor with the error message.
     * </p>
     * @param message the error message
     */
    public JobProcessorException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructor with the error message and cause exception.
     * </p>
     * @param message the error message
     * @param cause the cause exception
     */
    public JobProcessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
