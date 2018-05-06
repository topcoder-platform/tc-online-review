/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

/**
 * <p>
 * This exception is thrown when there is a configuration problem somewhere in the Review Score Aggregator component.
 * </p>
 *
 * @author dplass, daiwb
 * @version 1.0
 */
public class ReviewScoreAggregatorConfigException extends ReviewScoreAggregatorException {

    /**
     * <p>
     * Construct this exception with the given message.
     * </p>
     *
     * @param message
     *            the message to include in the exception.
     */
    public ReviewScoreAggregatorConfigException(String message) {
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
    public ReviewScoreAggregatorConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
