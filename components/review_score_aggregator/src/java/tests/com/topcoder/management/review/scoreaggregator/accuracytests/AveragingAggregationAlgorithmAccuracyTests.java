/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.accuracytests;

import com.topcoder.management.review.scoreaggregator.impl.AveragingAggregationAlgorithm;

import junit.framework.TestCase;


/**
 * <p>
 * Accuracy tests of the AveragingAggregationAlgorithm.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public class AveragingAggregationAlgorithmAccuracyTests extends TestCase {
    /**
     * Test of constructor.
     */
    public void testCtor() {
        assertNotNull("Unable to create AveragingAggregationAlgorithm.",
            new AveragingAggregationAlgorithm());
    }

    /**
     * Test of calculateAggregateScore.
     */
    public void testCalculateAggregateScore1() {
        float[] scores = new float[100];

        // score is from 0 to 99
        for (int i = 0; i < 100; i++) {
            scores[i] = i;
        }

        // the average score is 49.5
        assertEquals("Fails to calculate AggregateScore.", 49.5f,
            new AveragingAggregationAlgorithm().calculateAggregateScore(scores), 1e-3);
    }

    /**
     * Test of calculateAggregateScore.
     */
    public void testCalculateAggregateScore2() {
        assertEquals("Fails to calculate AggregateScore.", 0f,
            new AveragingAggregationAlgorithm().calculateAggregateScore(new float[0]), 1e-5);
    }

    /**
     * Test of calculateAggregateScore.
     */
    public void testCalculateAggregateScore3() {
        float[] scores = new float[] {0, 0, 1, 1, 0, 0};

        // the average score is 49.5
        assertEquals("Fails to calculate AggregateScore.", 0.333333f,
            new AveragingAggregationAlgorithm().calculateAggregateScore(scores), 1e-5);
    }
}
