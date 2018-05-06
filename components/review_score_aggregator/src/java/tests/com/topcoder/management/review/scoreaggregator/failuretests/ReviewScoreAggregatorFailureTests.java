/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.failuretests;

import java.util.Iterator;

import junit.framework.TestCase;

import com.topcoder.management.review.scoreaggregator.AggregatedSubmission;
import com.topcoder.management.review.scoreaggregator.InconsistentDataException;
import com.topcoder.management.review.scoreaggregator.ReviewScoreAggregator;
import com.topcoder.management.review.scoreaggregator.ReviewScoreAggregatorConfigException;
import com.topcoder.management.review.scoreaggregator.Submission;
import com.topcoder.management.review.scoreaggregator.impl.AveragingAggregationAlgorithm;
import com.topcoder.management.review.scoreaggregator.impl.StandardPlaceAssignment;
import com.topcoder.management.review.scoreaggregator.impl.StandardTieBreaker;
import com.topcoder.management.review.scoreaggregator.impl.StandardTieDetector;
import com.topcoder.util.config.ConfigManager;


/**
 * <p>
 * Failure tests for <tt>ReviewScoreAggregator</tt>.
 * </p>
 *
 * @author GavinWang
 * @version 1.0
 */
public class ReviewScoreAggregatorFailureTests extends TestCase {
    /** An instance of ReviewScoreAggregator for testing. */
    private ReviewScoreAggregator aggregator;

    /**
     * <p>
     * Set up each test.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        aggregator = new ReviewScoreAggregator();

        // configure
        ConfigManager.getInstance().add("failure/scoreaggregator.xml");
    }

    /**
     * <p>
     * Clean up each test.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        for (Iterator iter = ConfigManager.getInstance().getAllNamespaces(); iter.hasNext();) {
            ConfigManager.getInstance().removeNamespace((String) iter.next());
        }
    }

    /**
     * Failure test for ReviewScoreAggregator(). Inputs: nothing. Expectation: should be ok.
     */
    public void testReviewScoreAggregator() {
        assertNotNull("Unable to instantiate ReviewScoreAggregator.", this.aggregator);
    }

    /**
     * Failure test for ReviewScoreAggregator(ScoreAggregationAlgorithm, TieDetector, TieBreaker,
     * PlaceAssignmentAlgorithm). Inputs: null ScoreAggregationAlgorithm. Expectation: IllegalArgumentException should
     * be thrown.
     */
    public void testReviewScoreAggregator4NullScoreAggregationAlgorithm() {
        try {
            new ReviewScoreAggregator(null, new StandardTieDetector(), new StandardTieBreaker(),
                new StandardPlaceAssignment());
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for ReviewScoreAggregator(ScoreAggregationAlgorithm, TieDetector, TieBreaker,
     * PlaceAssignmentAlgorithm). Inputs: null TieDetector. Expectation: IllegalArgumentException should be thrown.
     */
    public void testReviewScoreAggregator4NullTieDetector() {
        try {
            new ReviewScoreAggregator(new AveragingAggregationAlgorithm(), null, new StandardTieBreaker(),
                new StandardPlaceAssignment());
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for ReviewScoreAggregator(ScoreAggregationAlgorithm, TieDetector, TieBreaker,
     * PlaceAssignmentAlgorithm). Inputs: null TieBreaker. Expectation: IllegalArgumentException should be thrown.
     */
    public void testReviewScoreAggregator4NullTieBreaker() {
        try {
            new ReviewScoreAggregator(new AveragingAggregationAlgorithm(), new StandardTieDetector(), null,
                new StandardPlaceAssignment());
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for ReviewScoreAggregator(ScoreAggregationAlgorithm, TieDetector, TieBreaker,
     * PlaceAssignmentAlgorithm). Inputs: null PlaceAssignmentAlgorithm. Expectation: IllegalArgumentException should
     * be thrown.
     */
    public void testReviewScoreAggregator4NullPlaceAssignmentAlgorithm() {
        try {
            new ReviewScoreAggregator(new AveragingAggregationAlgorithm(), new StandardTieDetector(),
                new StandardTieBreaker(), null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for setScoreAggregationAlgorithm(ScoreAggregationAlgorithm). Inputs: null
     * ScoreAggregationAlgorithm. Expectation: IllegalArgumentException should be thrown.
     */
    public void testSetScoreAggregationAlgorithmNull() {
        try {
            this.aggregator.setScoreAggregationAlgorithm(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for setPlaceAssignmentAlgorighm(PlaceAssignmentAlgorithm). Inputs: null PlaceAssignmentAlgorithm.
     * Expectation: IllegalArgumentException should be thrown.
     */
    public void testSetPlaceAssignmentAlgorithmNull() {
        try {
            this.aggregator.setScoreAggregationAlgorithm(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for setTieBreaker(TieBreaker). Inputs: null TieBreaker. Expectation: IllegalArgumentException
     * should be thrown.
     */
    public void testSetTieBreakerNull() {
        try {
            this.aggregator.setTieBreaker(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for setTieDetector(TieDetector). Inputs: null TieDetector. Expectation: IllegalArgumentException
     * should be thrown.
     */
    public void testSetTieDetectorNull() {
        try {
            this.aggregator.setTieDetector(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for aggregateScores(float[][]). Inputs: null scores array. Expectation: IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testAggregateScoresFloatArrayArrayNullScoresArray()
        throws Exception {
        try {
            this.aggregator.aggregateScores((float[][]) null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for aggregateScores(float[][]). Inputs: scores array containing null scores entries. Expectation:
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testAggregateScoresFloatArrayArrayNullScoresEntries()
        throws Exception {
        try {
            this.aggregator.aggregateScores(new float[][] {new float[] {95.0f}, null});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for aggregateScores(float[][]). Inputs: scores array containing negative scores entries.
     * Expectation: IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testAggregateScoresFloatArrayArrayNegativeScoresEntries()
        throws Exception {
        try {
            this.aggregator.aggregateScores(new float[][] {new float[] {95.0f}, new float[] {-0.5f}});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for aggregateScores(float[][]). Inputs: scores array containing inconsistent entries. Expectation:
     * InconsistentDataException should be thrown.
     */
    public void testAggregateScoresFloatArrayArrayInconsistentScoresEntries() {
        try {
            this.aggregator.aggregateScores(new float[][] {new float[] {95.0f}, new float[] {95.5f, 90.9f}});
            fail("InconsistentDataException should be thrown.");
        } catch (InconsistentDataException e) {
            // expected
        }
    }

    /**
     * Failure test for aggregateScores(Submission[]). Inputs: null Submission array. Expectation:
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testAggregateScoresSubmissionArrayNullArray()
        throws Exception {
        try {
            this.aggregator.aggregateScores((Submission[]) null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for aggregateScores(Submission[]). Inputs: array containing null Submission entries. Expectation:
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testAggregateScoresSubmissionArrayNullEntries()
        throws Exception {
        try {
            this.aggregator.aggregateScores(new Submission[] {null});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for aggregateScores(Submission[]). Inputs: array containing inconsistent Submission entries.
     * Expectation: InconsistentDataException should be thrown.
     */
    public void testAggregateScoresSubmissionArrayInconsistentEntries() {
        try {
            this.aggregator.aggregateScores(new Submission[] {
                new Submission(1L, new float[] {95.5f}), new Submission(2L, new float[] {95.5f, 98.5f})
            });
            fail("InconsistentDataException should be thrown.");
        } catch (InconsistentDataException e) {
            // expected
        }
    }

    /**
     * Failure test for calcPlacements(float[][]). Inputs: null scores array. Expectation: IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testCalcPlacementsFloatArrayArrayNullArray()
        throws Exception {
        try {
            this.aggregator.calcPlacements((float[][]) null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for calcPlacements(float[][]). Inputs: scores array containing null scores entries. Expectation:
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testCalcPlacementsFloatArrayArrayNullScoresEntries()
        throws Exception {
        try {
            this.aggregator.calcPlacements(new float[][] {new float[] {95.0f}, null});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for calcPlacements(float[][]). Inputs: scores array containing negative scores entries.
     * Expectation: IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testCalcPlacementsFloatArrayArrayNegativeScoresEntries()
        throws Exception {
        try {
            this.aggregator.calcPlacements(new float[][] {new float[] {95.0f}, new float[] {-0.5f}});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for calcPlacements(float[][]). Inputs: scores array containing inconsistent entries. Expectation:
     * InconsistentDataException should be thrown.
     */
    public void testCalcPlacementsFloatArrayArrayInconsistentScoresEntries() {
        try {
            this.aggregator.aggregateScores(new float[][] {new float[] {95.0f}, new float[] {95.5f, 90.9f}});
            fail("InconsistentDataException should be thrown.");
        } catch (InconsistentDataException e) {
            // expected
        }
    }

    /**
     * Failure test for calcPlacements(AggregatedSubmission[]). Inputs: null Submission array. Expectation:
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testCalcPlacementsAggregatedSubmissionArrayNullArray()
        throws Exception {
        try {
            this.aggregator.calcPlacements((AggregatedSubmission[]) null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for calcPlacements(AggregatedSubmission[]). Inputs: array containing null Submission entries.
     * Expectation: IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testCalcPlacementsAggregatedSubmissionArrayNullEntries()
        throws Exception {
        try {
            this.aggregator.calcPlacements(new AggregatedSubmission[] {null});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for calcPlacements(AggregatedSubmission[]). Inputs: array containing inconsistent Submission
     * entries. Expectation: InconsistentDataException should be thrown.
     * @throws Exception to JUnit
     */
    public void testCalcPlacementsAggregatedSubmissionArrayInconsistentEntries()
        throws Exception {
        try {
            this.aggregator.calcPlacements(new AggregatedSubmission[] {
                new AggregatedSubmission(1L, new float[] {95.5f}, 95.5f),
                new AggregatedSubmission(2L, new float[] {95.0f, 96.0f}, 95.5f),
            });
            fail("InconsistentDataException should be thrown.");
        } catch (InconsistentDataException e) {
            // expected
        }
    }

    /**
     * Failure test for ReviewScoreAggregator(String). Inputs: null namespace. Expectation: IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testReviewScoreAggregatorStringNull() throws Exception {
        try {
            new ReviewScoreAggregator(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for ReviewScoreAggregator(String). Inputs: legal namespace. Expectation: should be ok.
     *
     * @throws Exception to JUnit
     */
    public void testReviewScoreAggregatorStringEmpty()
        throws Exception {
        new ReviewScoreAggregator("com.topcoder.management.review.scoreaggregator.failure");
    }

    /**
     * Failure test for ReviewScoreAggregator(String). Inputs: config missing ScoreAggregationAlgorithm. Expectation:
     * ReviewScoreAggregatorConfigException should be thrown.
     */
    public void testReviewScoreAggregatorStringMissingScoreAggregationAlgorithm() {
        try {
            new ReviewScoreAggregator(
                "com.topcoder.management.review.scoreaggregator.failure.missing_ScoreAggregationAlgorithm");
            fail("ReviewScoreAggregatorConfigException should be thrown.");
        } catch (ReviewScoreAggregatorConfigException e) {
            // expected
        }
    }

    /**
     * Failure test for ReviewScoreAggregator(String). Inputs: config missing TieDetector. Expectation:
     * ReviewScoreAggregatorConfigException should be thrown.
     */
    public void testReviewScoreAggregatorStringMissingTieDetector() {
        try {
            new ReviewScoreAggregator("com.topcoder.management.review.scoreaggregator.failure.missing_TieDetector");
            fail("ReviewScoreAggregatorConfigException should be thrown.");
        } catch (ReviewScoreAggregatorConfigException e) {
            // expected
        }
    }

    /**
     * Failure test for ReviewScoreAggregator(String). Inputs: config missing TieBreaker. Expectation:
     * ReviewScoreAggregatorConfigException should be thrown.
     */
    public void testReviewScoreAggregatorStringMissingTieBreaker() {
        try {
            new ReviewScoreAggregator("com.topcoder.management.review.scoreaggregator.failure.missing_TieBreaker");
            fail("ReviewScoreAggregatorConfigException should be thrown.");
        } catch (ReviewScoreAggregatorConfigException e) {
            // expected
        }
    }

    /**
     * Failure test for ReviewScoreAggregator(String). Inputs: config missing PlaceAssignmentAlgorithm. Expectation:
     * ReviewScoreAggregatorConfigException should be thrown.
     */
    public void testReviewScoreAggregatorStringMissingPlaceAssignmentAlgorithm() {
        try {
            new ReviewScoreAggregator(
                "com.topcoder.management.review.scoreaggregator.failure.missing_PlaceAssignmentAlgorithm");
            fail("ReviewScoreAggregatorConfigException should be thrown.");
        } catch (ReviewScoreAggregatorConfigException e) {
            // expected
        }
    }

    /**
     * Failure test for ReviewScoreAggregator(String). Inputs: config ScoreAggregationAlgorithm not found. Expectation:
     * ReviewScoreAggregatorConfigException should be thrown.
     */
    public void testReviewScoreAggregatorStringScoreAggregationAlgorithmNotFound() {
        try {
            new ReviewScoreAggregator(
                "com.topcoder.management.review.scoreaggregator.failure.ScoreAggregationAlgorithm_notfound");
            fail("ReviewScoreAggregatorConfigException should be thrown.");
        } catch (ReviewScoreAggregatorConfigException e) {
            // expected
        }
    }

    /**
     * Failure test for ReviewScoreAggregator(String). Inputs: config StandardTieDetector not found. Expectation:
     * ReviewScoreAggregatorConfigException should be thrown.
     */
    public void testReviewScoreAggregatorStringStandardTieDetectorNotFound() {
        try {
            new ReviewScoreAggregator(
                "com.topcoder.management.review.scoreaggregator.failure.StandardTieDetector_notfound");
            fail("ReviewScoreAggregatorConfigException should be thrown.");
        } catch (ReviewScoreAggregatorConfigException e) {
            // expected
        }
    }

    /**
     * Failure test for ReviewScoreAggregator(String). Inputs: config StandardTieBreaker not found. Expectation:
     * ReviewScoreAggregatorConfigException should be thrown.
     */
    public void testReviewScoreAggregatorStringStandardTieBreakerNotFound() {
        try {
            new ReviewScoreAggregator(
                "com.topcoder.management.review.scoreaggregator.failure.StandardTieBreaker_notfound");
            fail("ReviewScoreAggregatorConfigException should be thrown.");
        } catch (ReviewScoreAggregatorConfigException e) {
            // expected
        }
    }

    /**
     * Failure test for ReviewScoreAggregator(String). Inputs: config StandardTieBreaker not found. Expectation:
     * ReviewScoreAggregatorConfigException should be thrown.
     */
    public void testReviewScoreAggregatorStringStandardPlaceAssignmentNotFound() {
        try {
            new ReviewScoreAggregator(
                "com.topcoder.management.review.scoreaggregator.failure.StandardPlaceAssignment_notfound");
            fail("ReviewScoreAggregatorConfigException should be thrown.");
        } catch (ReviewScoreAggregatorConfigException e) {
            // expected
        }
    }
}
