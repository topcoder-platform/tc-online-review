/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * This exception is the base class for all custom exceptions thrown by the Review Score Aggregator component.
 * </p>
 *
 * @author dplass, daiwb
 * @version 1.0
 */
public class ReviewScoreAggregatorException extends BaseException {

    /**
     * <p>
     * Construct this exception with the given message.
     * </p>
     *
     * @param message
     *            the message to include in the exception.
     */
    public ReviewScoreAggregatorException(String message) {
        super(message);
    }

    /**
     * <p>
     * Construct this exception with the given message and underlying cause.
     * </p>
     *
     * @param message
     *            the message to include in the exception.
     * @param cause
     *            the underlying reason for this exception.
     */
    public ReviewScoreAggregatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
