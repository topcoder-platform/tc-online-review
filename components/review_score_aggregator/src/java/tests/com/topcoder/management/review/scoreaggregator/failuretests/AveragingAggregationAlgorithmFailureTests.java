/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.failuretests;

import junit.framework.TestCase;

import com.topcoder.management.review.scoreaggregator.ScoreAggregationAlgorithm;
import com.topcoder.management.review.scoreaggregator.impl.AveragingAggregationAlgorithm;


/**
 * <p>
 * Failure tests for <tt>AveragingAggregationAlgorithm</tt>.
 * </p>
 *
 * @author GavinWang
 * @version 1.0
 */
public class AveragingAggregationAlgorithmFailureTests extends TestCase {
    /** An instance of AveragingAggregationAlgorithm for testing. */
    private ScoreAggregationAlgorithm algo;

    /**
     * <p>
     * Set up each test.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        algo = new AveragingAggregationAlgorithm();
    }

    /**
     * Failure test for calculateAggregateScore(float[]). Inputs: null scores array. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testCalculateAggregateScoreNullScores() {
        try {
            algo.calculateAggregateScore(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for calculateAggregateScore(float[]). Inputs: scores array containing negative entries.
     * Expectation: IllegalArgumentException should be thrown.
     */
    public void testCalculateAggregateScoreNegativeScores() {
        try {
            algo.calculateAggregateScore(new float[] {95.0f, 94.5f, -1.0f, -2.0f});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
