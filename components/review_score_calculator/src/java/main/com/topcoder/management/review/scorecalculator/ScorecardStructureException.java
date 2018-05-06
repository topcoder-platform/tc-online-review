/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.management.scorecard.data.Scorecard;

/**
 * <p>
 * Thrown to indicate that the scorecard is incorrect/inconsistent and cannot be processed.
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
public class ScorecardStructureException extends CalculationException {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Fields

    /**
     * The Scorecard instance that is incorrected and generated this exception.
     */
    private final Scorecard scorecard;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Creates a new ScorecardStructureException, with the specified details and scorecard.
     *
     * @param   details
     *          A message describing what caused the exception to be thrown.
     * @param   scorecard
     *          The Scorecard instance that is incorrect and caused the exception to be thrown.
     */
    public ScorecardStructureException(String details, Scorecard scorecard) {
        this(details, null, scorecard);
    }

    /**
     * Creates a new ScorecardStructureException, with the specified details, root cause, and scorecard.
     *
     * @param   details
     *          A message describing what caused the exception to be thrown.
     * @param   cause
     *          The exception that prompted this exception to be thrown.
     * @param   scorecard
     *          The Scorecard instance that is incorrect and caused the exception to be thrown.
     */
    public ScorecardStructureException(String details, Throwable cause, Scorecard scorecard) {
        super(details, cause);
        this.scorecard = scorecard;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Accessors

    /**
     * Gets the Scorecard instance that is incorrect and generated this exception.
     *
     * @return  The Scorecard instance that is incorrect and generated this exception.
     */
    public Scorecard getScorecard() {
        return scorecard;
    }
}
