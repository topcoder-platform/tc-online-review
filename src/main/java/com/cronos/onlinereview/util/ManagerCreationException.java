/*
 * Copyright (C) 2007 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import com.topcoder.util.errorhandling.BaseRuntimeException;


/**
 * This class extends BaseRuntimeException and is used to signal about any error
 * occurred when creating manager instances in ManagerCreationHelper class.
 *
 * @author TCSASSEMBLER
 * @version 2.0
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
