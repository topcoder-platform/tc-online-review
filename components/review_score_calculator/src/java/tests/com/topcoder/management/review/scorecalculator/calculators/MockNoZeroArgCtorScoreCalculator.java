/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator.calculators;

import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.scorecalculator.ScoreCalculator;
import com.topcoder.management.review.scorecalculator.ScoreCalculatorException;
import com.topcoder.management.scorecard.data.Question;

/**
 * A mock implementation of the ScoreCalculator interface that doesn't have a public zero argument constructor.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class MockNoZeroArgCtorScoreCalculator implements ScoreCalculator {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Create a new MockNoZeroArgCtorScoreCalculator.
     */
    private MockNoZeroArgCtorScoreCalculator() {
        // Do nothing.
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ScorecardMatrixBuilder Methods

    /**
     * Empty implementation.
     *
     * @param   item
     *          Ignored.
     * @param   question
     *          Ignored.
     *
     * @return  Always 0.
     *
     * @throws  ScoreCalculatorException
     *          Only declared to match interface.
     */
    public float evaluateItem(Item item, Question question) throws ScoreCalculatorException {
        return 0.0f;
    }
}
