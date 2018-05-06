/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.management.review.data.Review;

/**
 * <p>
 * Thrown to indicate that the review is incorrect/inconsistent with the scorecard and cannot be processed.
 * </p>
 *
 * <p>
 * <b>Thread Safety</b>: This class is thread safe.
 * </p>
 *
 * @author      nicka81, UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class ReviewStructureException extends CalculationException {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Fields

    /**
     * The Review instance that is incorrected and generated this exception.
     */
    private final Review review;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Creates a new ReviewStructureException, with the specified details and review.
     *
     * @param   details
     *          A message describing what caused the exception to be thrown.
     * @param   review
     *          The Review instance that is incorrect and caused the exception to be thrown.
     */
    public ReviewStructureException(String details, Review review) {
        this(details, null, review);
    }

    /**
     * Creates a new ReviewStructureException, with the specified details, root cause, and review.
     *
     * @param   details
     *          A message describing what caused the exception to be thrown.
     * @param   cause
     *          The exception that prompted this exception to be thrown.
     * @param   review
     *          The Review instance that is incorrect and caused the exception to be thrown.
     */
    public ReviewStructureException(String details, Throwable cause, Review review) {
        super(details, cause);
        this.review = review;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Accessors

    /**
     * Gets the Review instance that is incorrect and generated this exception.
     *
     * @return  The Review instance that is incorrect and generated this exception.
     */
    public Review getReview() {
        return review;
    }
}
