/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.accuracytests;

import com.topcoder.management.review.scoreaggregator.AggregatedSubmission;
import com.topcoder.management.review.scoreaggregator.impl.StandardTieBreaker;

import junit.framework.TestCase;


/**
 * <p>
 * Accuracy tests of the StandardTieBreaker.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public class StandardTieBreakerAccuracyTests extends TestCase {
    /**
     * Test of constructor.
     */
    public void testCtor() {
        assertNotNull("Unable to create StandardTieBreaker.", new StandardTieBreaker());
    }

    /**
     * Test of breakTies.
     *
     * @throws Exception to JUnit
     */
    public void testBreakTies1() throws Exception {
        AggregatedSubmission[] submissions = new AggregatedSubmission[3];

        // No ties needs to be broken.
        for (int i = 0; i < 3; i++) {
            float[] scores = new float[] {i, i + 1, i + 2};
            submissions[i] = new AggregatedSubmission(i + 1, scores, i + 1);
        }

        int[] gotRank = new StandardTieBreaker().breakTies(submissions);

        // check the result
        assertEquals("Fails to invoke breakTies.", 3, gotRank.length);

        // the excepted rank is 1,2,3
        for (int i = 0; i < 3; i++) {
            assertEquals("Fails to invoke breakTies.", 3 - i, gotRank[i]);
        }
    }

    /**
     * Test of breakTies.
     *
     * @throws Exception to JUnit
     */
    public void testBreakTies2() throws Exception {
        AggregatedSubmission[] submissions = new AggregatedSubmission[3];

        /*
         * Three submissions:
         * id           scores                 AggregateScore
         *  1       80.125, 80.125, 80.125         80.125
         *  2       81.125, 81.125, 78.125         80.125
         *  3       82.125, 82.125, 76.125         80.125
         */
        submissions[0] = new AggregatedSubmission(1, new float[] {80.125f, 80.125f, 80.125f},
                80.125f);
        submissions[1] = new AggregatedSubmission(2, new float[] {81.125f, 81.125f, 78.125f},
                80.125f);
        submissions[2] = new AggregatedSubmission(3, new float[] {82.125f, 82.125f, 76.125f},
                80.125f);

        int[] gotRank = new StandardTieBreaker().breakTies(submissions);

        // check the result
        assertEquals("Fails to invoke breakTies.", 3, gotRank.length);

        // the excepted rank is 3,2,1
        for (int i = 0; i < 3; i++) {
            assertEquals("Fails to invoke breakTies.", 3 - i, gotRank[i]);
        }
    }

    /**
     * Test of breakTies.
     *
     * @throws Exception to JUnit
     */
    public void testBreakTies3() throws Exception {
        AggregatedSubmission[] submissions = new AggregatedSubmission[3];

        /*
         * Three submissions:
         * id              scores               AggregateScore
         *  1       80.125, 80.125, 80.125           80.125
         *  2       80.125, 80.125, 80.125           80.125
         *  3       80.125, 80.125, 80.125           80.125
         */
        for (int i = 0; i < 3; i++) {
            float[] scores = new float[] {80.125f, 80.125f, 80.125f};
            submissions[i] = new AggregatedSubmission(i + 1, scores, 80.125f);
        }

        int[] gotRank = new StandardTieBreaker().breakTies(submissions);

        // check the result
        assertEquals("Fails to invoke breakTies.", 3, gotRank.length);

        // the excepted rank is 1,1,1
        for (int i = 0; i < 3; i++) {
            assertEquals("Fails to invoke breakTies", 1, gotRank[i]);
        }
    }

    /**
     * Test of breakTies.
     *
     * @throws Exception to JUnit
     */
    public void testBreakTies4() throws Exception {
        AggregatedSubmission[] submissions = new AggregatedSubmission[3];

        /*
         * Three submissions:
         * id                scores             AggregateScore
         *  1       80.125, 80.125, 80.125           80.125
         *  2       78.125, 81.125, 81.125           80.125
         *  3       78.125, 81.125, 81.125           80.125
         */
        submissions[0] = new AggregatedSubmission(1, new float[] {80.125f, 80.125f, 80.125f},
                80.125f);
        submissions[1] = new AggregatedSubmission(2, new float[] {78.125f, 81.125f, 81.125f},
                80.125f);
        submissions[2] = new AggregatedSubmission(3, new float[] {78.125f, 81.125f, 81.125f},
                80.125f);

        int[] gotRank = new StandardTieBreaker().breakTies(submissions);

        // check the result
        assertEquals("Fails to invoke breakTies.", 3, gotRank.length);

        // the excepted rank is 2,1,1
        assertEquals("Fails to invoke breakTies", 2, gotRank[0]);
        assertEquals("Fails to invoke breakTies", 1, gotRank[1]);
        assertEquals("Fails to invoke breakTies", 1, gotRank[2]);
    }

    /**
     * Test of breakTies.
     *
     * @throws Exception to JUnit
     */
    public void testBreakTies5() throws Exception {
        int[] exceptedRank = new StandardTieBreaker().breakTies(new AggregatedSubmission[0]);

        assertEquals("Fails to invoke breakTies", 0, exceptedRank.length);
    }
}
