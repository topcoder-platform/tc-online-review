/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

/**
 * <p>
 * This exception is thrown by the ReviewScoreAggregator class when the data sent into one of its methods is not
 * considered consistent. This usually occurs if there are different numbers of review scores per submission.
 * </p>
 *
 * @author dplass, daiwb
 * @version 1.0
 */
public class InconsistentDataException extends ReviewScoreAggregatorException {

    /**
     * <p>
     * Construct this exception with the given message.
     * </p>
     *
     * @param message
     *            the message to include with this exception, describing what data is inconsistent.
     */
    public InconsistentDataException(String message) {
        super(message);
    }
}
