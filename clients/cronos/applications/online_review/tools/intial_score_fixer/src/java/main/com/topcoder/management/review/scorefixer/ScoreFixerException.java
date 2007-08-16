/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorefixer;

import com.topcoder.util.errorhandling.BaseException;

/**
 * This is the base exception of all other exceptions thrown by this application. It is not thrown
 * directly by any classes.
 * <p>
 * This class is thread-safe because it's stateless.
 * </p>
 *
 * @author George1
 * @version 1.0
 */
public class ScoreFixerException extends BaseException {

    /**
     * Automatically generated unique ID for use with serialization.
     */
    private static final long serialVersionUID = -5354720340914299085L;

    /**
     * Constructs a new instance of {@link ScoreFixerException}.
     */
    public ScoreFixerException() {
        super();
    }

    /**
     * Constructs a new instance of {@link ScoreFixerException} with the given error message.
     *
     * @param message
     *            the error message
     */
    public ScoreFixerException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance of {@link ScoreFixerException} with the given error message and
     * inner cause.
     *
     * @param message
     *            the error message
     * @param cause
     *            the inner cause
     */
    public ScoreFixerException(String message, Throwable cause) {
        super(message, cause);
    }
}
