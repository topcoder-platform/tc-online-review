/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all test cases for <code>Submission</code>.
 * </p>
 *
 * @author daiwb
 * @version 1.0
 */
public class SubmissionTest extends TestCase {
    /**
     * Returns the test suite of this test case.
     *
     * @return the test suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(SubmissionTest.class);
    }

    /**
     * Test constructor <code>Submission(long, float[])</code>.
     */
    public void testSubmission_Accuracy() {
        Submission sub = new Submission(1, new float[] {0.0f, 90.0f});
        assertNotNull("The instance should be created successfully.", sub);
        assertEquals("The id field value should be set successfully.", 1, sub.getId());
        assertEquals("There should be two scores in this submission.", 2, sub.getScores().length);
        assertEquals("The first should be 0.0.", 0.0f, sub.getScores()[0], 1e-8);
        assertEquals("The second should be 90.0.", 90.0f, sub.getScores()[1], 1e-8);
    }

    /**
     * Test constructor <code>Submission(long, float[])</code> with negative <code>id</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testSubmission_NegativeId() {
        try {
            new Submission(-1, new float[] {0.0f, 90.0f});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>Submission(long, float[])</code> with zero <code>id</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testSubmission_ZeroId() {
        try {
            new Submission(0, new float[] {0.0f, 90.0f});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>Submission(long, float[])</code> with null <code>scores</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testSubmission_NullScores() {
        try {
            new Submission(1, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>Submission(long, float[])</code> with <code>scores</code> containing negative element.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testSubmission_ScoresWithNegativeElement() {
        try {
            new Submission(1, new float[] {0.0f, -90.0f});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>Submission(long, float[])</code> with <code>scores</code> containing invalid element.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testSubmission_ScoresWithInvalidElement_1() {
        try {
            new Submission(1, new float[] {0.0f, Float.NaN});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>Submission(long, float[])</code> with <code>scores</code> containing invalid element.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testSubmission_ScoresWithInvalidElement_2() {
        try {
            new Submission(1, new float[] {0.0f, Float.POSITIVE_INFINITY});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>Submission(long, float[])</code> with <code>scores</code> containing invalid element.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testSubmission_ScoresWithInvalidElement_3() {
        try {
            new Submission(1, new float[] {0.0f, Float.NEGATIVE_INFINITY});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>getScores()</code>.
     */
    public void testGetScores_Accuracy() {
        Submission sub = new Submission(1, new float[] {0.0f, 90.0f});
        assertEquals("There should be two scores in this submission.", 2, sub.getScores().length);
        assertEquals("The first should be 0.0.", 0.0f, sub.getScores()[0], 1e-8);
        assertEquals("The second should be 90.0.", 90.0f, sub.getScores()[1], 1e-8);
    }

    /**
     * Test method <code>getId()</code>.
     */
    public void testGetId_Accuracy() {
        Submission sub = new Submission(1, new float[] {0.0f, 90.0f});
        assertEquals("The id field value should be returned.", 1, sub.getId());
    }

}
