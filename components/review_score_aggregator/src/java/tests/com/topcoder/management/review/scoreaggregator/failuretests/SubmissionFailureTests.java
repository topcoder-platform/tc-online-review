/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.failuretests;

import junit.framework.TestCase;

import com.topcoder.management.review.scoreaggregator.Submission;


/**
 * <p>
 * Failure tests for <tt>Submission</tt>.
 * </p>
 *
 * @author GavinWang
 * @version 1.0
 */
public class SubmissionFailureTests extends TestCase {
    /**
     * Failure test for Submission(long, float[]). Inputs: negative id, say -1. Expectation: IllegalArgumentException
     * should be thrown.
     */
    public void testSubmissionNegativeId() {
        try {
            new Submission(-1L, new float[] {90.5f, 88.5f, 95.5f});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for Submission(long, float[]). Inputs: zero id. Expectation: IllegalArgumentException should be
     * thrown.
     */
    public void testSubmissionZeroId() {
        try {
            new Submission(0L, new float[] {90.5f, 88.5f, 95.5f});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for Submission(long, float[]). Inputs: null score array. Expectation: IllegalArgumentException
     * should be thrown.
     */
    public void testSubmissionNullScores() {
        try {
            new Submission(1L, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for Submission(long, float[]). Inputs: score array containing negative ones. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testSubmissionNegativeScores() {
        try {
            new Submission(1L, new float[] {90.5f, -1.5f, 95.5f});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for Submission(long, float[]). Inputs: score array containing zero ones. Expectation: should pass.
     */
    public void testSubmissionZeroScores() {
        new Submission(1L, new float[] {90.5f, 0.0f, 95.5f});
    }

    /**
     * Failure test for Submission(long, float[]). Inputs: score array containing NaN. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testSubmissionNaNScores() {
        try {
            new Submission(1L, new float[] {90.5f, Float.NaN, 95.5f});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for Submission(long, float[]). Inputs: score array containing positive infinitive ones.
     * Expectation: IllegalArgumentException should be thrown.
     */
    public void testSubmissionPositiveInfScores() {
        try {
            new Submission(1L, new float[] {90.5f, Float.POSITIVE_INFINITY, 95.5f});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for Submission(long, float[]). Inputs: score array containing negative infinitive ones.
     * Expectation: IllegalArgumentException should be thrown.
     */
    public void testSubmissionNegativeInfScores() {
        try {
            new Submission(1L, new float[] {90.5f, Float.NEGATIVE_INFINITY, 95.5f});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
