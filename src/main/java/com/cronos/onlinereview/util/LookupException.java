/*
 * Copyright (C) 2012 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import com.topcoder.util.errorhandling.BaseException;

/**
 * This class extends BaseException and is used to signal about any error
 * occurred when retrieving entities in LookupHelper class.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class LookupException extends BaseException {

    /**
     * <p>
     * Constructs the exception with error message.
     * </p>
     *
     * @param message
     *            the error message
     */
    public LookupException(String message) {
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
    public LookupException(String message, Throwable cause) {
        super(message, cause);
    }
}
