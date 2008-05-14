/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.util.errorhandling.BaseRuntimeException;

/**
 * This class extends BaseRuntimeException used for testing.
 * 
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ManagerCreationException extends BaseRuntimeException {

    /**
     * <p>
     * Constructs the exception with error message.
     * </p>
     *
     * @param message
     *            the error message
     */
    public ManagerCreationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs the exception with error message and inner cause.
     * </p>
     *
     * @param message
     *            the error message
     * @param cause
     *            the cause of this exception
     */
    public ManagerCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
