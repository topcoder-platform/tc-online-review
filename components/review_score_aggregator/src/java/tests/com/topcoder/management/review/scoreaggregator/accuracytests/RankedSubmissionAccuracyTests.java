/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.accuracytests;

import com.topcoder.management.review.scoreaggregator.AggregatedSubmission;
import com.topcoder.management.review.scoreaggregator.RankedSubmission;

import junit.framework.TestCase;


/**
 * <p>
 * Accuracy tests of the RankedSubmission.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public class RankedSubmissionAccuracyTests extends TestCase {
    /**
     * Test of the constructor.
     */
    public void testCtor() {
        AggregatedSubmission testAggregatedSubmission = new AggregatedSubmission(1,
                new float[] {80, 80, 80.75f}, 80.25f);

        RankedSubmission testRankedSubmission = new RankedSubmission(testAggregatedSubmission, 1);

        assertNotNull("Unable to create RankedSubmission", testRankedSubmission);
    }

    /**
     * Test of get rank.
     */
    public void testGetRank() {
        AggregatedSubmission testAggregatedSubmission = new AggregatedSubmission(1,
                new float[] {80, 80, 80.75f}, 80.25f);

        RankedSubmission testRankedSubmission = new RankedSubmission(testAggregatedSubmission,
                123456789);

        assertEquals("Fails to invoke getRank", 123456789, testRankedSubmission.getRank());
    }
}
