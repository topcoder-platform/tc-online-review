/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.failuretests;

import junit.framework.TestCase;

import com.topcoder.management.review.scoreaggregator.AggregatedSubmission;
import com.topcoder.management.review.scoreaggregator.Submission;


/**
 * <p>
 * Failure tests for <tt>AggregatedSubmission</tt>.
 * </p>
 *
 * @author GavinWang
 * @version 1.0
 */
public class AggregatedSubmissionFailureTests extends TestCase {
    /**
     * Failure test for AggregatedSubmission(long, float[], float). Inputs: negative id, say -1. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testAggregatedSubmissionNegativeId() {
        try {
            new AggregatedSubmission(-1L, new float[] {90.5f, 88.5f, 95.5f}, 94.0f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for AggregatedSubmission(long, float[], float). Inputs: zero id. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testAggregatedSubmissionZeroId() {
        try {
            new AggregatedSubmission(0L, new float[] {90.5f, 88.5f, 95.5f}, 94.0f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for AggregatedSubmission(long, float[], float). Inputs: null score array. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testAggregatedSubmissionNullScores() {
        try {
            new AggregatedSubmission(1L, null, 94.0f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for AggregatedSubmission(long, float[], float). Inputs: score array containing negative ones.
     * Expectation: IllegalArgumentException should be thrown.
     */
    public void testAggregatedSubmissionNegativeScores() {
        try {
            new AggregatedSubmission(1L, new float[] {90.5f, -1.5f, 95.5f}, 94.0f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for AggregatedSubmission(long, float[], float). Inputs: score array containing zero ones.
     * Expectation: should pass.
     */
    public void testAggregatedSubmissionZeroScores() {
        new AggregatedSubmission(1L, new float[] {90.5f, 0.0f, 95.5f}, 94.0f);
    }

    /**
     * Failure test for AggregatedSubmission(long, float[], float). Inputs: score array containing NaN. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testSubmissionNaNScores() {
        try {
            new AggregatedSubmission(1L, new float[] {90.5f, Float.NaN, 95.5f}, 94.0f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for AggregatedSubmission(long, float[], float). Inputs: score array containing positive infinitive
     * ones. Expectation: IllegalArgumentException should be thrown.
     */
    public void testAggregatedSubmissionPositiveInfScores() {
        try {
            new AggregatedSubmission(1L, new float[] {90.5f, Float.POSITIVE_INFINITY, 95.5f}, 94.0f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for AggregatedSubmission(long, float[], float). Inputs: score array containing negative infinitive
     * ones. Expectation: IllegalArgumentException should be thrown.
     */
    public void testAggregatedSubmissionNegativeInfScores() {
        try {
            new AggregatedSubmission(1L, new float[] {90.5f, Float.NEGATIVE_INFINITY, 95.5f}, 94.0f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for AggregatedSubmission(long, float[], float). Inputs: score array containing negative agregated
     * score. Expectation: IllegalArgumentException should be thrown.
     */
    public void testAggregatedSubmissionNegativeAgg() {
        try {
            new AggregatedSubmission(1L, new float[] {90.5f, 94.0f, 95.5f}, -1.0f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for AggregatedSubmission(long, float[], float). Inputs: score array containing zero agregated
     * score. Expectation: should be ok.
     */
    public void testAggregatedSubmissionZeroAgg() {
        new AggregatedSubmission(1L, new float[] {90.5f, 94.0f, 95.5f}, .0f);
    }

    /**
     * Failure test for AggregatedSubmission(long, float[], float). Inputs: score array containing negative infinitive
     * agregated score. Expectation: IllegalArgumentException should be thrown.
     */
    public void testAggregatedSubmissionNegativeInfAgg() {
        try {
            new AggregatedSubmission(1L, new float[] {90.5f, 94.0f, 95.5f}, Float.NEGATIVE_INFINITY);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for AggregatedSubmission(long, float[], float). Inputs: score array containing positive infinitive
     * agregated score. Expectation: IllegalArgumentException should be thrown.
     */
    public void testAggregatedSubmissionPositiveInfAgg() {
        try {
            new AggregatedSubmission(1L, new float[] {90.5f, 94.0f, 95.5f}, Float.POSITIVE_INFINITY);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for AggregatedSubmission(long, float[], float). Inputs: score array containing NaN agregated score.
     * Expectation: IllegalArgumentException should be thrown.
     */
    public void testAggregatedSubmissionNaNAgg() {
        try {
            new AggregatedSubmission(1L, new float[] {90.5f, 94.0f, 95.5f}, Float.NaN);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for AggregatedSubmission(Submission, float). Inputs: null submission. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testAggregatedSubmissionSubmissionFloatNullSubmission() {
        try {
            new AggregatedSubmission(null, 94.0f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for AggregatedSubmission(Submission, float). Inputs: negative agregated score. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testAggregatedSubmissionSubmissionFloatNegativeAgg() {
        try {
            new AggregatedSubmission(new Submission(1L, new float[] {94.0f}), -1.0f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for AggregatedSubmission(Submission, float). Inputs: zero agregated score. Expectation: should be
     * ok.
     */
    public void testAggregatedSubmissionSubmissionFloatZeroAgg() {
        new AggregatedSubmission(new Submission(1L, new float[] {94.0f}), .0f);
    }

    /**
     * Failure test for AggregatedSubmission.compareTo(Object). Inputs: non-AggregatedSubmission object. Expectation:
     * ClassCastException should be thrown.
     */
    public void testCompareToNonAggregatedSubmission() {
        try {
            new AggregatedSubmission(new Submission(1L, new float[] {94.0f}), 94.0f).compareTo(new Object());
            fail("ClassCastException should be thrown.");
        } catch (ClassCastException e) {
            // expected
        }
    }
}
