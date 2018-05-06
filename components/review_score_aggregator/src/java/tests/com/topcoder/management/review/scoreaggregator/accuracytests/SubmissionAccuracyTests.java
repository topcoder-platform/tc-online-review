/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.accuracytests;

import com.topcoder.management.review.scoreaggregator.Submission;

import junit.framework.TestCase;


/**
 * <p>
 * Accuracy tests of the Submission.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public class SubmissionAccuracyTests extends TestCase {
    /** Id of submission for testing. */
    private static final long ID = 12345678912345L;

    /** Float array (scores) for testing. */
    private static float[] scores;

    /** Submission used for testing. */
    private Submission testSubmission;

    /**
     * Sets up the testing environment.
     */
    protected void setUp() {
        // creates the scores array
        scores = new float[] {0, 123, 456, 789};

        // creates Submission
        testSubmission = new Submission(ID, scores);
    }

    /**
     * Test of the constructor.
     */
    public void testCtor1() {
        assertNotNull("Unable to create Submission", testSubmission);
    }

    /**
     * Test of the constructor.
     */
    public void testCtor2() {
        // create Submission with zero-legth scores
        assertNotNull("Unable to create Submission", new Submission(ID, new float[0]));
    }

    /**
     * Test of getScores.
     */
    public void testGetScores() {
        float[] temp = testSubmission.getScores();

        // check  clone of the array (scores)
        assertTrue("Fails to set the scores filed or fails to get the score field.", scores != temp);

        // check the length
        assertEquals("Fails to set the scores filed or fails to get the score field.", 4,
            temp.length);

        // check the value
        for (int i = 0; i < temp.length; i++) {
            assertEquals("Fails to set the scores filed or fails to get the score field.",
                scores[i], temp[i], 1e-3);
        }
    }

    /**
     * Test of getId.
     */
    public void testGetID() {
        // check the value
        assertEquals("Fails to set id or fails to get id.", ID, testSubmission.getId());
    }
}
