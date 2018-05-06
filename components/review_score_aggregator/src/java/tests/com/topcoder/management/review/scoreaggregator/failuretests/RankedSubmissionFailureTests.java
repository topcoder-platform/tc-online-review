/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.failuretests;

import junit.framework.TestCase;

import com.topcoder.management.review.scoreaggregator.AggregatedSubmission;
import com.topcoder.management.review.scoreaggregator.RankedSubmission;
import com.topcoder.management.review.scoreaggregator.Submission;


/**
 * <p>
 * Failure tests for <tt>RankedSubmission</tt>.
 * </p>
 *
 * @author GavinWang
 * @version 1.0
 */
public class RankedSubmissionFailureTests extends TestCase {
    /**
     * Failure test for RankedSubmission(AggregatedSubmission, int). Inputs: null AggregatedSubmission. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testRankedSubmissionNullSubmission() {
        try {
            new RankedSubmission(null, 1);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for RankedSubmission(AggregatedSubmission, int). Inputs: negative rank. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testRankedSubmissionNegativeRank() {
        try {
            new RankedSubmission(new AggregatedSubmission(new Submission(1L, new float[] {94.0f}), 94.0f), -1);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for RankedSubmission(AggregatedSubmission, int). Inputs: zero rank. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testRankedSubmissionZeroRank() {
        try {
            new RankedSubmission(new AggregatedSubmission(new Submission(1L, new float[] {94.0f}), 94.0f), 0);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
