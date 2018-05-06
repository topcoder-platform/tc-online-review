/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.accuracytests;

import com.topcoder.management.review.scoreaggregator.AggregatedSubmission;
import com.topcoder.management.review.scoreaggregator.Submission;

import junit.framework.TestCase;


/**
 * <p>
 * Accuracy tests of the AggregatedSubmission.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public class AggregatedSubmissionAccuracyTests extends TestCase {
    /**
     * Test of constructor :AggregatedSubmission(long, float[], float).
     */
    public void testCtor1() {
        AggregatedSubmission testAggregatedSubmission = new AggregatedSubmission(1,
                new float[] {80, 80, 80.75f}, 80.25f);

        assertNotNull("Unable to create AggregatedSubmission.", testAggregatedSubmission);
    }

    /**
     * Test of constructor :AggregatedSubmission(Submission, float).
     */
    public void testCtor2() {
        Submission testSubmission = new Submission(1, new float[] {80, 80, 80.75f});
        AggregatedSubmission testAggregatedSubmission = new AggregatedSubmission(testSubmission,
                80.25f);

        assertNotNull("Unable to create AggregatedSubmission.", testAggregatedSubmission);
    }

    /**
     * Test of getAggregatedScore.
     */
    public void testGetAggregatedScore() {
        AggregatedSubmission testAggregatedSubmission = new AggregatedSubmission(1,
                new float[] {80, 80, 80.75f}, 80.25f);

        assertEquals("Fails to get aggregatedScore.", 80.25f,
            testAggregatedSubmission.getAggregatedScore(), 1e-4);
    }

    /**
     * Test of compareTo.
     */
    public void testcompareTo() {
        AggregatedSubmission testAggregatedSubmission1 = new AggregatedSubmission(1,
                new float[] {80, 80, 80.75f}, 80.25f);

        AggregatedSubmission testAggregatedSubmission2 = new AggregatedSubmission(2,
                new float[] {80, 80, 80.12f}, 80.04f);

        AggregatedSubmission testAggregatedSubmission3 = new AggregatedSubmission(3,
                new float[] {80, 80, 80.9f}, 80.30f);

        assertEquals("Fails to invoke compareTo.", 0,
            testAggregatedSubmission1.compareTo(testAggregatedSubmission1));

        assertEquals("Fails to invoke compareTo.", 1,
            testAggregatedSubmission1.compareTo(testAggregatedSubmission2));

        assertEquals("Fails to invoke compareTo.", -1,
            testAggregatedSubmission1.compareTo(testAggregatedSubmission3));
    }
}
