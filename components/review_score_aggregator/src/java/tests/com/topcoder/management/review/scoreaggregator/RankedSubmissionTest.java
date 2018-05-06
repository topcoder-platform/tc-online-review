/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all test cases for <code>RankedSubmission</code>.
 * </p>
 *
 * @author daiwb
 * @version 1.0
 */
public class RankedSubmissionTest extends TestCase {
    /**
     * An <code>AggregatedSubmission</code> instance used for tests.
     */
    private static final AggregatedSubmission AGGREGATED_SUB =
        new AggregatedSubmission(1, new float[] {0.0f, 90.0f}, 45.0f);

    /**
     * Returns the test suite of this test case.
     *
     * @return the test suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(RankedSubmissionTest.class);
    }

    /**
     * Set up.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
    }

    /**
     * Tear down.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
    }

    /**
     * Test constructor <code>RankedSubmission(AggregatedSubmission, int)</code>.
     */
    public void testRankedSubmission_Accuracy() {
        RankedSubmission rankerSub = new RankedSubmission(AGGREGATED_SUB, 2);
        assertNotNull("The instance should be created successfully.", rankerSub);
        assertEquals("The id field value should be set successfully.", 1, rankerSub.getId());
        assertEquals("There should be two scores in this submission.", 2, rankerSub.getScores().length);
        assertEquals("The first should be 0.0.", 0.0f, rankerSub.getScores()[0], 1e-8);
        assertEquals("The second should be 90.0.", 90.0f, rankerSub.getScores()[1], 1e-8);
        assertEquals("The aggregatedScore field value should be set successfully.", 45.0f, rankerSub
                .getAggregatedScore(), 1e-8);
        assertEquals("The rank field value should be set successfully.", 2, rankerSub.getRank());
    }

    /**
     * Test constructor <code>RankedSubmission(AggregatedSubmission, int)</code> with null <code>sub</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testRankedSubmission_NullSub() {
        try {
            new RankedSubmission(null, 2);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>RankedSubmission(AggregatedSubmission, int)</code> with negative <code>rank</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testRankedSubmission_NegativeRank() {
        try {
            new RankedSubmission(AGGREGATED_SUB, -2);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>RankedSubmission(AggregatedSubmission, int)</code> with zero <code>rank</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testRankedSubmission_ZeroRank() {
        try {
            new RankedSubmission(AGGREGATED_SUB, 0);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>getRank()</code>.
     */
    public void testGetRank_Accuracy() {
        RankedSubmission rankerSub = new RankedSubmission(AGGREGATED_SUB, 2);
        assertEquals("The rank field value should be returned.", 2, rankerSub.getRank());
    }

}
