/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

/**
 * <p>
 * The issue tracking exception.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
@SuppressWarnings("serial")
public class IssueTrackingException extends Exception {
    /**
     * <p>
     * Constructs a new <code>IssueTrackingException</code> instance with error message.
     * </p>
     *
     * @param message
     *            the error message.
     */
    public IssueTrackingException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new <code>IssueTrackingException</code> instance with error message and inner cause.
     * </p>
     *
     * @param message
     *            the error message.
     * @param cause
     *            the inner cause.
     */
    public IssueTrackingException(String message, Throwable cause) {
        super(message, cause);
    }
}
