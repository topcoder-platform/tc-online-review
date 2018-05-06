/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.accuracytests;

import com.topcoder.management.review.scoreaggregator.AggregatedSubmission;
import com.topcoder.management.review.scoreaggregator.PlaceAssignmentAlgorithm;
import com.topcoder.management.review.scoreaggregator.RankedSubmission;
import com.topcoder.management.review.scoreaggregator.ReviewScoreAggregator;
import com.topcoder.management.review.scoreaggregator.ScoreAggregationAlgorithm;
import com.topcoder.management.review.scoreaggregator.Submission;
import com.topcoder.management.review.scoreaggregator.TieBreaker;
import com.topcoder.management.review.scoreaggregator.TieDetector;
import com.topcoder.management.review.scoreaggregator.impl.AveragingAggregationAlgorithm;
import com.topcoder.management.review.scoreaggregator.impl.StandardPlaceAssignment;
import com.topcoder.management.review.scoreaggregator.impl.StandardTieBreaker;
import com.topcoder.management.review.scoreaggregator.impl.StandardTieDetector;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;


/**
 * <p>
 * Accuracy tests of the ReviewScoreAggregator.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public class ReviewScoreAggregatorAccuracyTests extends TestCase {
    /** ScoreAggregationAlgorithm instance for testing. */
    private ScoreAggregationAlgorithm aggregator = new AveragingAggregationAlgorithm();

    /** TieDetector instance for testing. */
    private TieDetector tieDetector = new StandardTieDetector();

    /** TieBreaker instance for testing. */
    private TieBreaker tieBreaker = new StandardTieBreaker();

    /** PlaceAssignmentAlgorithm instance for testing. */
    private PlaceAssignmentAlgorithm placeAssignmentAlgorithm = new StandardPlaceAssignment();

    /** ReviewScoreAggregator instance for testing. */
    private ReviewScoreAggregator reviewScoreAggregator;

    /**
     * Sets up the testing environment.
     *
     * @throws Exception from configuration manager.
     */
    protected void setUp() throws Exception {
        // creates ReviewScoreAggregator
        ConfigManager cm = ConfigManager.getInstance();
        cm.add("com/topcoder/management/review/scoreaggregator/config_accuracy.xml");
        reviewScoreAggregator = new ReviewScoreAggregator(aggregator, tieDetector, tieBreaker,
                placeAssignmentAlgorithm);
    }

    /**
     * Tears down the testing environment.
     *
     * @throws Exception from configuration manager.
     */
    protected void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        cm.removeNamespace("com.topcoder.management.review.scoreaggregator");
    }

    /**
     * Test of constructor : ReviewScoreAggregator(ScoreAggregationAlgorithm, TieDetector,
     * TieBreaker, PlaceAssignmentAlgorithm).
     */
    public void testCtor1() {
        // just use the ReviewScoreAggregator created in setUp.
        assertNotNull("Unable to create ReviewScoreAggregator.", reviewScoreAggregator);

        assertEquals("Fails to create ReviewScoreAggregator.", placeAssignmentAlgorithm,
            reviewScoreAggregator.getPlaceAssignmentAlgorithm());

        assertEquals("Fails to create ReviewScoreAggregator.", aggregator,
            reviewScoreAggregator.getScoreAggregationAlgorithm());

        assertEquals("Fails to create ReviewScoreAggregator.", tieBreaker,
            reviewScoreAggregator.getTieBreaker());

        assertEquals("Fails to create ReviewScoreAggregator.", tieDetector,
            reviewScoreAggregator.getTieDetector());
    }

    /**
     * Test of constructor : ReviewScoreAggregator(String).
     *
     * @throws Exception to JUnit
     */
    public void testCtor2() throws Exception {
        ReviewScoreAggregator temp = new ReviewScoreAggregator(ReviewScoreAggregator.DEFAULT_NAMESPACE);

        assertNotNull("Unable to create ReviewScoreAggregator.", temp);

        assertTrue("Fails to create ReviewScoreAggregator.",
            temp.getPlaceAssignmentAlgorithm() instanceof StandardPlaceAssignment);
        assertTrue("Fails to create ReviewScoreAggregator.",
            temp.getScoreAggregationAlgorithm() instanceof AveragingAggregationAlgorithm);
        assertTrue("Fails to create ReviewScoreAggregator.",
            temp.getTieBreaker() instanceof StandardTieBreaker);
        assertTrue("Fails to create ReviewScoreAggregator.",
            temp.getTieDetector() instanceof StandardTieDetector);
    }

    /**
     * Test of constructor : ReviewScoreAggregator().
     */
    public void testCtor3() {
        ReviewScoreAggregator temp = new ReviewScoreAggregator();

        assertNotNull("Unable to create ReviewScoreAggregator.", temp);

        assertTrue("Fails to create ReviewScoreAggregator.",
            temp.getPlaceAssignmentAlgorithm() instanceof StandardPlaceAssignment);
        assertTrue("Fails to create ReviewScoreAggregator.",
            temp.getScoreAggregationAlgorithm() instanceof AveragingAggregationAlgorithm);
        assertTrue("Fails to create ReviewScoreAggregator.",
            temp.getTieBreaker() instanceof StandardTieBreaker);
        assertTrue("Fails to create ReviewScoreAggregator.",
            temp.getTieDetector() instanceof StandardTieDetector);
    }

    /**
     * Test of aggregateScores.
     *
     * @throws Exception to JUnit
     */
    public void testAggregateScores1() throws Exception {
        // the initial data
        float[][] scores = new float[][] {
            {1.1f, 2.1f, 3.1f},
            {2.1f, 2.1f, 2.1f},
            {3.1f, 2.1f, 1.1f},
            {3.125f, 2.125f, 1.125f}
        };

        // the expected result
        float[] expectedScore = new float[] {2.1f, 2.1f, 2.1f, 2.125f};

        // invoke aggregateScores
        AggregatedSubmission[] gotResult = reviewScoreAggregator.aggregateScores(scores);

        // check the result
        assertEquals("Fail to invoke aggregateScores", 4, gotResult.length);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals("Fail to invoke aggregateScores.", scores[i][j],
                    gotResult[i].getScores()[j], 1e-4);
            }

            assertEquals("Fail to invoke aggregateScores.", expectedScore[i],
                gotResult[i].getAggregatedScore(), 1e-4);
            assertEquals("Fail to invoke aggregateScores.", i + 1, gotResult[i].getId());
        }
    }

    /**
     * Test of aggregateScores.
     *
     * @throws Exception to JUnit
     */
    public void testAggregateScores2() throws Exception {
        float[][] scores = new float[0][0];
        AggregatedSubmission[] gotResult = reviewScoreAggregator.aggregateScores(scores);
        assertEquals("Fail to invoke aggregateScores.", 0, gotResult.length);
    }

    /**
     * Test of aggregateScores.
     *
     * @throws Exception to JUnit
     */
    public void testAggregateScores3() throws Exception {
        // the initial data
        Submission[] submissions = new Submission[4];
        float[][] scores = new float[][] {
            {3.125f, 2.125f, 1.125f},
            {3.1f, 2.1f, 1.1f},
            {2.1f, 2.1f, 2.1f},
            {1.1f, 2.1f, 3.1f},
        };

        // the expected result
        float[] expectedScore = new float[] {2.125f, 2.1f, 2.1f, 2.1f};

        for (int i = 0; i < 4; i++) {
            submissions[i] = new Submission(i + 1, scores[i]);
        }

        // invoke aggregateScores
        AggregatedSubmission[] gotResult = reviewScoreAggregator.aggregateScores(scores);
        assertEquals("Fail to invoke calcPlacements", 4, gotResult.length);

        // check the result
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals("Fail to invoke aggregateScores.", scores[i][j],
                    gotResult[i].getScores()[j], 1e-4);
            }

            assertEquals("Fail to invoke aggregateScores.", expectedScore[i],
                gotResult[i].getAggregatedScore(), 1e-4);
            assertEquals("Fail to invoke aggregateScores.", i + 1, gotResult[i].getId());
        }
    }

    /**
     * Test of aggregateScores.
     *
     * @throws Exception to JUnit
     */
    public void testAggregateScores4() throws Exception {
        AggregatedSubmission[] gotResult = reviewScoreAggregator.aggregateScores(new Submission[0]);
        assertEquals("Fail to invoke aggregateScores.", 0, gotResult.length);
    }

    /**
     * Test of calcPlacements.
     *
     * @throws Exception to JUnit
     */
    public void testCalcPlacements1() throws Exception {
        // the initial data
        float[][] scores = new float[][] {
            {1.1f, 2.1f, 3.1f},
            {2.1f, 2.1f, 2.1f},
            {3.1f, 2.1f, 1.1f},
            {3.125f, 2.125f, 1.125f}
        };

        // the expected aggregatedScores
        float[] expectedScore = new float[] {2.1f, 2.1f, 2.1f, 2.125f};

        AggregatedSubmission[] submissions = new AggregatedSubmission[4];

        for (int i = 0; i < 4; i++) {
            submissions[i] = new AggregatedSubmission(i + 1, scores[i], expectedScore[i]);
        }

        // the expected rank
        int[] expectedRanked = new int[] {2, 4, 2, 1};

        // invoke calcPlacement
        RankedSubmission[] gotResult = reviewScoreAggregator.calcPlacements(submissions);

        //check the result
        assertEquals("Fail to invoke calcPlacements", 4, gotResult.length);

        for (int i = 0; i < 4; i++) {
            int j = 0;

            for (; j < 4; j++) {
                if (gotResult[j].getId() == (i + 1)) {
                    break;
                }
            }

            assertTrue("Fail to invoke calcPlacements", j < 4);

            assertEquals("Fail to invoke calcPlacements.", expectedScore[i],
                gotResult[j].getAggregatedScore(), 1e-4);

            assertEquals("Fail to invoke aggregateScores.", expectedRanked[i],
                gotResult[j].getRank());

            for (int k = 0; k < 3; k++) {
                assertEquals("Fail to invoke calcPlacements.", scores[i][k],
                    gotResult[j].getScores()[k], 1e-4);
            }
        }
    }

    /**
     * Test of calcPlacements.
     *
     * @throws Exception to JUnit
     */
    public void testCalcPlacements2() throws Exception {
        // the initial data
        float[][] scores = new float[][] {
            {1.1f, 2.1f, 3.1f},
            {2.1f, 2.1f, 2.1f},
            {3.1f, 2.1f, 1.1f},
            {3.125f, 2.125f, 1.125f}
        };

        // the expected result
        float[] expectedScore = new float[] {2.1f, 2.1f, 2.1f, 2.125f};
        int[] expectedRanked = new int[] {2, 4, 2, 1};

        // invoke calcPlacement
        RankedSubmission[] gotResult = reviewScoreAggregator.calcPlacements(scores);

        //check the result
        assertEquals("Fail to invoke calcPlacements", 4, gotResult.length);

        for (int i = 0; i < 4; i++) {
            int j = 0;

            for (; j < 4; j++) {
                if (gotResult[j].getId() == (i + 1)) {
                    break;
                }
            }

            assertTrue("Fail to invoke calcPlacements", j < 4);

            assertEquals("Fail to invoke calcPlacements.", expectedScore[i],
                gotResult[j].getAggregatedScore(), 1e-4);

            assertEquals("Fail to invoke aggregateScores.", expectedRanked[i],
                gotResult[j].getRank());

            for (int k = 0; k < 3; k++) {
                assertEquals("Fail to invoke calcPlacements.", scores[i][k],
                    gotResult[j].getScores()[k], 1e-4);
            }
        }
    }

    /**
     * Test of calcPlacements.
     *
     * @throws Exception to JUnit
     */
    public void testCalcPlacements3() throws Exception {
        // invoke calcPlacement
        RankedSubmission[] gotResult = reviewScoreAggregator.calcPlacements(new float[0][0]);

        //check the result
        assertEquals("Fail to invoke calcPlacements", 0, gotResult.length);
    }

    /**
     * Test of calcPlacements.
     *
     * @throws Exception to JUnit
     */
    public void testCalcPlacements4() throws Exception {
        // invoke calcPlacement
        RankedSubmission[] gotResult = reviewScoreAggregator.calcPlacements(new RankedSubmission[0]);

        //check the result
        assertEquals("Fail to invoke calcPlacements", 0, gotResult.length);
    }

    /**
     * Test of setPlaceAssignmentAlgorighm.
     */
    public void testSetPlaceAssignmentAlgorighm() {
        ReviewScoreAggregator temp = new ReviewScoreAggregator();

        assertNotSame("Fails to create ReviewScoreAggregator.", placeAssignmentAlgorithm,
            temp.getPlaceAssignmentAlgorithm());

        temp.setPlaceAssignmentAlgorithm(placeAssignmentAlgorithm);

        assertEquals("Fails to set PlaceAssignmentAlgorighm.", placeAssignmentAlgorithm,
            temp.getPlaceAssignmentAlgorithm());
    }

    /**
     * Test of setTieDetector.
     */
    public void testSetTieDetector() {
        ReviewScoreAggregator temp = new ReviewScoreAggregator();

        assertNotSame("Fails to create ReviewScoreAggregator.", tieDetector, temp.getTieDetector());

        temp.setTieDetector(tieDetector);

        assertEquals("Fails to set TieDetector.", tieDetector, temp.getTieDetector());
    }

    /**
     * Test of setTieBreaker.
     */
    public void testSetTieBreaker() {
        ReviewScoreAggregator temp = new ReviewScoreAggregator();

        assertNotSame("Fails to create ReviewScoreAggregator.", tieBreaker, temp.getTieBreaker());

        temp.setTieBreaker(tieBreaker);

        assertEquals("Fails to set TieBreaker.", tieBreaker, temp.getTieBreaker());
    }

    /**
     * Test of setScoreAggregationAlgorithm.
     */
    public void testSetScoreAggregationAlgorithm() {
        ReviewScoreAggregator temp = new ReviewScoreAggregator();

        assertNotSame("Fails to create ReviewScoreAggregator", aggregator,
            temp.getScoreAggregationAlgorithm());

        temp.setScoreAggregationAlgorithm(aggregator);

        assertEquals("Fails to create ScoreAggregationAlgorithm", aggregator,
            temp.getScoreAggregationAlgorithm());
    }
}
