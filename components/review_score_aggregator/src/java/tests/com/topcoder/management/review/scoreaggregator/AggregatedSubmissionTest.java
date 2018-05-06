/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all test cases for <code>AggregatedSubmission</code>.
 * </p>
 *
 * @author daiwb
 * @version 1.0
 */
public class AggregatedSubmissionTest extends TestCase {
    /** A <code>Submission</code> instance used for tests. */
    private Submission sub = null;

    /**
     * Returns the test suite of this test case.
     *
     * @return the test suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(AggregatedSubmissionTest.class);
    }

    /**
     * Set up.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        sub = new Submission(1, new float[] {0.0f, 90.0f});
    }

    /**
     * Test constructor <code>AggregatedSubmission(long, float[], float)</code>.
     */
    public void testAggregatedSubmissionLongFloatArrayFloat_Accuracy() {
        AggregatedSubmission aggregatedSub = new AggregatedSubmission(1, new float[] {0.0f, 90.0f}, 45.0f);
        assertNotNull("The instance should be created successfully.", aggregatedSub);
        assertEquals("The id field value should be set successfully.", 1, aggregatedSub.getId());
        assertEquals("There should be two scores in this submission.", 2, aggregatedSub.getScores().length);
        assertEquals("The first should be 0.0.", 0.0f, aggregatedSub.getScores()[0], 1e-8);
        assertEquals("The second should be 90.0.", 90.0f, aggregatedSub.getScores()[1], 1e-8);
        assertEquals("The aggregatedScore field value should be set successfully.", 45.0f, aggregatedSub
                .getAggregatedScore(), 1e-8);
    }

    /**
     * Test constructor <code>AggregatedSubmission(long, float[], float)</code> with negative <code>id</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionLongFloatArrayFloat_NegativeId() {
        try {
            new AggregatedSubmission(-1, new float[] {0.0f, 90.0f}, 45.0f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(long, float[], float)</code> with zero <code>id</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionLongFloatArrayFloat_ZeroId() {
        try {
            new AggregatedSubmission(0, new float[] {0.0f, 90.0f}, 45.0f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(long, float[], float)</code> with null <code>scores</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionLongFloatArrayFloat_NullScores() {
        try {
            new AggregatedSubmission(1, null, 45.0f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(long, float[], float)</code> with <code>scores</code> containing
     * negative element. <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionLongFloatArrayFloat_ScoresWithNegativeElement() {
        try {
            new AggregatedSubmission(1, new float[] {0.0f, -90.0f}, 45.0f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(long, float[], float)</code> with <code>scores</code> containing
     * invalid element. <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionLongFloatArrayFloat_ScoresWithInvalidElement_1() {
        try {
            new AggregatedSubmission(1, new float[] {Float.NaN}, 45.0f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(long, float[], float)</code> with <code>scores</code> containing
     * invalid element. <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionLongFloatArrayFloat_ScoresWithInvalidElement_2() {
        try {
            new AggregatedSubmission(1, new float[] {Float.POSITIVE_INFINITY}, 45.0f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(long, float[], float)</code> with <code>scores</code> containing
     * invalid element. <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionLongFloatArrayFloat_ScoresWithInvalidElement_3() {
        try {
            new AggregatedSubmission(1, new float[] {Float.NEGATIVE_INFINITY}, 45.0f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(long, float[], float)</code> with negative
     * <code>aggregatedScore</code>. <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionLongFloatArrayFloat_NegativeAggregatedScore() {
        try {
            new AggregatedSubmission(1, new float[] {0.0f, 90.0f}, -45.0f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(long, float[], float)</code> with invalid
     * <code>aggregatedScore</code>. <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionLongFloatArrayFloat_InvalidAggregatedScore_1() {
        try {
            new AggregatedSubmission(1, new float[] {0.0f, 90.0f}, Float.NaN);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(long, float[], float)</code> with invalid
     * <code>aggregatedScore</code>. <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionLongFloatArrayFloat_InvalidAggregatedScore_2() {
        try {
            new AggregatedSubmission(1, new float[] {0.0f, 90.0f}, Float.POSITIVE_INFINITY);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(long, float[], float)</code> with invalid
     * <code>aggregatedScore</code>. <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionLongFloatArrayFloat_InvalidAggregatedScore_3() {
        try {
            new AggregatedSubmission(1, new float[] {0.0f, 90.0f}, Float.NEGATIVE_INFINITY);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(Submission, float)</code>.
     */
    public void testAggregatedSubmissionSubmissionFloat_Accracy() {
        AggregatedSubmission aggregatedSub = new AggregatedSubmission(sub, 45.0f);
        assertNotNull("The instance should be created successfully.", aggregatedSub);
        assertEquals("The id field value should be set successfully.", 1, aggregatedSub.getId());
        assertEquals("There should be two scores in this submission.", 2, aggregatedSub.getScores().length);
        assertEquals("The first should be 0.0.", 0.0f, aggregatedSub.getScores()[0], 1e-8);
        assertEquals("The second should be 90.0.", 90.0f, aggregatedSub.getScores()[1], 1e-8);
        assertEquals("The aggregatedScore field value should be set successfully.", 45.0f, aggregatedSub
                .getAggregatedScore(), 1e-8);
    }

    /**
     * Test constructor <code>AggregatedSubmission(Submission, float)</code> with null <code>sub</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionSubmissionFloat_NullSub() {
        try {
            new AggregatedSubmission(null, 45.0f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(Submission, float)</code> with negative
     * <code>aggregatedScore</code>. <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionSubmissionFloat_NegativeAggregatedScore() {
        try {
            new AggregatedSubmission(sub, -45.0f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(Submission, float)</code> with invalid <code>aggregatedScore</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionSubmissionFloat_InvalidAggregatedScore_1() {
        try {
            new AggregatedSubmission(sub, Float.NaN);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(Submission, float)</code> with invalid <code>aggregatedScore</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionSubmissionFloat_InvalidAggregatedScore_2() {
        try {
            new AggregatedSubmission(sub, Float.POSITIVE_INFINITY);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>AggregatedSubmission(Submission, float)</code> with invalid <code>aggregatedScore</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testAggregatedSubmissionSubmissionFloat_InvalidAggregatedScore_3() {
        try {
            new AggregatedSubmission(sub, Float.NEGATIVE_INFINITY);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>getAggregatedScore()</code>.
     */
    public void testGetAggregatedScore() {
        AggregatedSubmission aggregatedSub = new AggregatedSubmission(sub, 45.0f);
        assertEquals("The aggregatedScore field value should be set successfully.", 45.0f, aggregatedSub
                .getAggregatedScore(), 1e-8);
    }

    /**
     * Test method <code>compareTo(Object)</code>.
     */
    public void testCompareTo_Accuracy() {
        AggregatedSubmission aggregatedSub1 = new AggregatedSubmission(sub, 0.0f);
        AggregatedSubmission aggregatedSub2 = new AggregatedSubmission(sub, 45.0f);
        AggregatedSubmission aggregatedSub3 = new AggregatedSubmission(sub, 0.0f);

        assertEquals("Compare result mismatches.", -1, aggregatedSub1.compareTo(aggregatedSub2));
        assertEquals("Compare result mismatches.", 1, aggregatedSub2.compareTo(aggregatedSub1));
        assertEquals("Compare result mismatches.", 0, aggregatedSub1.compareTo(aggregatedSub3));
    }

    /**
     * Test method <code>compareTo(Object)</code> with null <code>obj</code>. <code>IllegalArgumentException</code>
     * is expected.
     */
    public void testCompareTo_NullArg() {
        try {
            AggregatedSubmission aggregatedSub = new AggregatedSubmission(sub, 0.0f);
            aggregatedSub.compareTo(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>compareTo(Object)</code> with <code>obj</code> not of AggregatedSubmission type.
     * <code>ClassCastException</code> is expected.
     */
    public void testCompareTo_InvalidObj() {
        try {
            AggregatedSubmission aggregatedSub = new AggregatedSubmission(sub, 0.0f);
            aggregatedSub.compareTo("fsfds");
            fail("ClassCastException is expected.");
        } catch (ClassCastException cce) {
            // Success
        }
    }
}
