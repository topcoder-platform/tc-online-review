/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all test cases for <code>AveragingAggregationAlgorithm</code>.
 * </p>
 *
 * @author daiwb
 * @version 1.0
 */
public class AveragingAggregationAlgorithmTest extends TestCase {
    /**
     * An <code>AveragingAggregationAlgorithm</code> instance used for tests.
     */
    private AveragingAggregationAlgorithm algo = null;

    /**
     * Returns the test suite of this test case.
     *
     * @return the test suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(AveragingAggregationAlgorithmTest.class);
    }

    /**
     * Set up.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        algo = new AveragingAggregationAlgorithm();
    }

    /**
     * Tear down.
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        algo = null;
    }

    /**
     * Test method <code>AveragingAggregationAlgorithm()</code>.
     */
    public void testAveragingAggregationAlgorithm_Accuracy() {
        assertNotNull("The instance should be created successfully.", algo);
    }

    /**
     * Test method <code>calculateAggregateScore(float[])</code>.
     */
    public void testCalculateAggregateScore_Accuracy_1() {
        float[] scores = new float[] {0.0f, 90.0f};
        assertEquals("The aggregated score mismatches.", 45.0f, algo.calculateAggregateScore(scores), 1e-8);
    }

    /**
     * Test method <code>calculateAggregateScore(float[])</code>.
     */
    public void testCalculateAggregateScore_Accuracy_2() {
        float[] scores = new float[0];
        assertEquals("The aggregated score mismatches.", 0.0f, algo.calculateAggregateScore(scores), 1e-8);
    }

    /**
     * Test method <code>calculateAggregateScore(float[])</code> with null <code>scores</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testCalculateAggregateScore_NullScores() {
        try {
            algo.calculateAggregateScore(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>calculateAggregateScore(float[])</code> with <code>scores</code> containing negative element.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testCalculateAggregateScore_ScoresWithNegativeElement() {
        try {
            algo.calculateAggregateScore(new float[] {0.0f, -90.0f});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>calculateAggregateScore(float[])</code> with <code>scores</code> containing invalid element.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testCalculateAggregateScore_ScoresWithInvalidElement_1() {
        try {
            algo.calculateAggregateScore(new float[] {Float.NaN});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>calculateAggregateScore(float[])</code> with <code>scores</code> containing invalid element.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testCalculateAggregateScore_ScoresWithInvalidElement_2() {
        try {
            algo.calculateAggregateScore(new float[] {Float.POSITIVE_INFINITY});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>calculateAggregateScore(float[])</code> with <code>scores</code> containing invalid element.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testCalculateAggregateScore_ScoresWithInvalidElement_3() {
        try {
            algo.calculateAggregateScore(new float[] {Float.NEGATIVE_INFINITY});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

}
