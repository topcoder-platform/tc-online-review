/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * The base exception for the Review Scorecard Calculator component, which all custom exceptions inherit.
 * </p>
 *
 * <p>
 * This exception is also used as a wrapper for all "foreign" exceptions (e.g. any exceptions caught from the
 * Weighted Calculator component.
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
public class CalculationException extends BaseException {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Creates a new CalculationException, with the specified details.
     *
     * @param   details
     *          A message describing what caused the exception to be thrown.
     */
    public CalculationException(String details) {
        super(details);
    }

    /**
     * Creates a new CalculationException, with the specified details and root cause.
     *
     * @param   details
     *          A message describing what caused the exception to be thrown.
     * @param   cause
     *          The exception that prompted this exception to be thrown.
     */
    public CalculationException(String details, Throwable cause) {
        super(details, cause);
    }
}
