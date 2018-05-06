/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.stresstests;

import com.topcoder.management.review.scoreaggregator.AggregatedSubmission;
import com.topcoder.management.review.scoreaggregator.ReviewScoreAggregator;
import com.topcoder.management.review.scoreaggregator.Submission;
import com.topcoder.management.review.scoreaggregator.impl.AveragingAggregationAlgorithm;
import com.topcoder.management.review.scoreaggregator.impl.StandardPlaceAssignment;
import com.topcoder.management.review.scoreaggregator.impl.StandardTieBreaker;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all stress test cases for Review Score Aggregator component.
 * </p>
 *
 * @author idx
 * @version 1.0
 */
public class ReviewScoreAggregatorStressTests extends TestCase {
    /** The test count. */
    private static final int TEST_COUNT = 1000;

    /** The review count. */
    private static final int REVIEW_COUNT = 200;

    /**
     * Returns the test suite of this test case.
     *
     * @return the test suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(ReviewScoreAggregatorStressTests.class);
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
     * @throws Exception to JUnit
     */
    public void tearDown() throws Exception {
    }

    /**
     * Stress test for <code>AveragingAggregationAlgorithm</code>.
     */
    public void testAveragingAggregationAlgorithm_Stress() {
        float[] scores = new float[TEST_COUNT];
        for (int i = 0; i < TEST_COUNT; ++i) {
            scores[i] = i * 1.0f;
        }
        AveragingAggregationAlgorithm algo = new AveragingAggregationAlgorithm();
        long current = System.currentTimeMillis();
        algo.calculateAggregateScore(scores);
        System.out.println("Test AveragingAggregationAlgorithm:calculateAggregateScore(float[]).");
        System.out.println("The size of input array is " + TEST_COUNT + ".");
        System.out.println("It took " + (System.currentTimeMillis() - current) + " ms.");
        System.out.println();
    }

    /**
     * Stress test for <code>StandardPlaceAssignment</code>.
     */
    public void testStandardPlaceAssignment_Stress() {
        int[] initialPlacements = new int[TEST_COUNT];
        int cnt = 0, num = 1;
        while (true) {
            int rand = (int) (Math.random() * 5 + 1);
            if (rand >= TEST_COUNT - cnt) {
                rand = TEST_COUNT - cnt;
            }
            for (int i = 0; i < rand; ++i) {
                initialPlacements[cnt++] = num;
            }
            ++num;
            if (cnt == TEST_COUNT) {
                break;
            }
        }
        StandardPlaceAssignment algo = new StandardPlaceAssignment();
        long current = System.currentTimeMillis();
        algo.assignPlacements(initialPlacements);
        System.out.println("Test StandardPlaceAssignment:assignPlacements(int[]).");
        System.out.println("The size of input array is " + TEST_COUNT + ".");
        System.out.println("It took " + (System.currentTimeMillis() - current) + " ms.");
    }

    /**
     * Stress test for <code>StandardTieBreaker</code>.
     * @throws Exception to JUnit
     */
    public void testStandardTieBreaker_Stress() throws Exception {
        AggregatedSubmission[] submissions = new AggregatedSubmission[TEST_COUNT];
        for (int i = 0; i < TEST_COUNT; ++i) {
            float[] scores = new float[REVIEW_COUNT];
            for (int j = 0; j < REVIEW_COUNT; ++j) {
                scores[j] = (float) (Math.random() * 100);
            }
            submissions[i] = new AggregatedSubmission(i + 1, scores, 0.0f);
        }
        StandardTieBreaker breaker = new StandardTieBreaker();
        long current = System.currentTimeMillis();
        breaker.breakTies(submissions);
        System.out.println("Test StandardTieBreaker:breakTies(AggregatedSubmission[]).");
        System.out.println("The size of input array is " + TEST_COUNT + ", the review count is " + REVIEW_COUNT + ".");
        System.out.println("It took " + (System.currentTimeMillis() - current) + " ms.");
    }

    /**
     * Stress test for <code>ReviewScoreAggregator</code>.
     * @throws Exception to JUnit.
     */
    public void testReviewScoreAggregator_Stress() throws Exception {
        ReviewScoreAggregator aggregator = new ReviewScoreAggregator();

        // ReviewScoreAggregator:aggregateScores(float[][])
        float[][] scores = new float[TEST_COUNT][REVIEW_COUNT];
        for (int i = 0; i < TEST_COUNT; ++i) {
            for (int j = 0; j < REVIEW_COUNT; ++j) {
                scores[i][j] = (float) (Math.random() * 100);
            }
        }
        long current = System.currentTimeMillis();
        aggregator.aggregateScores(scores);
        System.out.println("Test ReviewScoreAggregator:aggregateScores(float[][]).");
        System.out.println("The size of input array is " + TEST_COUNT + "x" + REVIEW_COUNT + ".");
        System.out.println("It took " + (System.currentTimeMillis() - current) + " ms.");

        // ReviewScoreAggregator:aggregateScores(Submission[])
        Submission[] submissions = new Submission[TEST_COUNT];
        for (int i = 0; i < TEST_COUNT; ++i) {
            float[] score = new float[REVIEW_COUNT];
            for (int j = 0; j < REVIEW_COUNT; ++j) {
                score[j] = (float) (Math.random() * 100);
            }
            submissions[i] = new Submission(i + 1, score);
        }
        current = System.currentTimeMillis();
        aggregator.aggregateScores(submissions);
        System.out.println("Test ReviewScoreAggregator:aggregateScores(Submission[]).");
        System.out.println("The size of input array is " + TEST_COUNT + ", the review count is " + REVIEW_COUNT + ".");
        System.out.println("It took " + (System.currentTimeMillis() - current) + " ms.");

        // ReviewScoreAggregator:calcPlacements(float[][])
        current = System.currentTimeMillis();
        aggregator.calcPlacements(scores);
        System.out.println("Test ReviewScoreAggregator:calcPlacements(float[][]).");
        System.out.println("The size of input array is " + TEST_COUNT + "x" + REVIEW_COUNT + ".");
        System.out.println("It took " + (System.currentTimeMillis() - current) + " ms.");

        // ReviewScoreAggregator:calcPlacements(AggregatedSubmission[])
        AggregatedSubmission[] aggregatedSubmissions = new AggregatedSubmission[TEST_COUNT];
        for (int i = 0; i < TEST_COUNT; ++i) {
            float[] score = new float[REVIEW_COUNT];
            for (int j = 0; j < REVIEW_COUNT; ++j) {
                score[j] = (float) (Math.random() * 100);
            }
            aggregatedSubmissions[i] = new AggregatedSubmission(i + 1, score,
                    (new AveragingAggregationAlgorithm()).calculateAggregateScore(score));
        }
        current = System.currentTimeMillis();
        aggregator.calcPlacements(aggregatedSubmissions);
        System.out.println("Test ReviewScoreAggregator:calcPlacements(AggregatedSubmission[]).");
        System.out.println("The size of input array is " + TEST_COUNT + ", the review count is " + REVIEW_COUNT + ".");
        System.out.println("It took " + (System.currentTimeMillis() - current) + " ms.");
    }
}
