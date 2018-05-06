/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.management.review.data.Item;
import com.topcoder.management.scorecard.data.Question;

/**
 * <p>
 * A simple score calculator interface, which evaluates a given review item against a given scorecard question,
 * and returns a score in the range [0, 1].
 * </p>
 *
 * <p>
 * <b>Thread Safety</b>: All implementations of this interface are expected to be thread safe.
 * </p>
 *
 * @author      nicka81, UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0.4
 */
public interface ScoreCalculator {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Interface Methods

    /**
     * <p>
     * Returns a score in the range [0, 1] for the given review item and scorecard question.
     * </p>
     *
     * <p>
     * This method is expected to be thread safe.
     * </p>
     *
     * @param   item
     *          The review item being evaluated.
     * @param   question
     *          The scorecard question for the item being evaluated.
     *
     * @return  The item's score which is in the range [0, 1].
     *
     * @throws  IllegalArgumentException
     *          The item or question is a null reference.
     * @throws  ScoreCalculatorException
     *          The evaluation cannot be performed successfully by the implementation.
     */
    public double evaluateItem(Item item, Question question) throws ScoreCalculatorException;
}
