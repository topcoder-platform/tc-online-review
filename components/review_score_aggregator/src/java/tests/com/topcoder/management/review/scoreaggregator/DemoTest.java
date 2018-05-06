/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

import com.topcoder.management.review.scoreaggregator.impl.AveragingAggregationAlgorithm;
import com.topcoder.management.review.scoreaggregator.impl.StandardPlaceAssignment;
import com.topcoder.management.review.scoreaggregator.impl.StandardTieBreaker;
import com.topcoder.management.review.scoreaggregator.impl.StandardTieDetector;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;

import java.util.Iterator;


/**
 * <p>
 * This test case aggregates all demo test cases.
 * </p>
 *
 * @author daiwb
 * @version 1.0
 */
public class DemoTest extends TestCase {
    /** Configuration file for ObjectFactory. */
    private static final String CONFIG_FILE = "test_files" + File.separator + "Standard.xml";

    /** Default namespace that external clients can use when configuring this component. */
    private static final String NAMESPACE = ReviewScoreAggregator.DEFAULT_NAMESPACE;

    /** Scores used for tests. */
    private static final float[][] SCORES = new float[][] {
        {97.28f, 97.44f, 93.47f},
        {89.25f, 98.47f, 94.47f},
        {86.81f, 96.75f, 93.59f},
        {92.81f, 90.94f, 91.50f},
        {78.03f, 86.63f, 86.44f},
        {78.41f, 82.50f, 79.03f},
        {60.81f, 71.22f, 70.97f}
    };

    /** Array of <code>AggregatedSubmission</code> instances used for tests. */
    private AggregatedSubmission[] aggregatedSubs = null;

    /** Array of <code>Submission</code> instances used for tests. */
    private Submission[] subs = null;

    /**
     * Returns the test suite of this test case.
     *
     * @return the test suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(DemoTest.class);
    }

    /**
     * Set up.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        aggregatedSubs = new AggregatedSubmission[SCORES.length];
        subs = new Submission[SCORES.length];

        for (int i = 0; i < SCORES.length; ++i) {
            aggregatedSubs[i] = new AggregatedSubmission(i + 1, SCORES[i],
                    (new AveragingAggregationAlgorithm()).calculateAggregateScore(SCORES[i]));

            subs[i] = new Submission(i + 1, SCORES[i]);
        }

        ConfigManager cm = ConfigManager.getInstance();
        File file = new File(CONFIG_FILE);
        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }
        cm.add(file.getAbsolutePath());
    }

    /**
     * Tear down.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator iter = cm.getAllNamespaces();

        while (iter.hasNext()) {
            try {
                cm.removeNamespace((String) iter.next());
            } catch (UnknownNamespaceException e) {
                // Ignore.
            }
        }
    }

    /**
     * Demo test for <code>ScoreAggregationAlgorithm</code>.
     */
    public void testScoreAggregationAlgorithmDemo() {
        ScoreAggregationAlgorithm algo = new AveragingAggregationAlgorithm();

        for (int i = 0; i < SCORES.length; ++i) {
            System.out.println(algo.calculateAggregateScore(SCORES[i]));
        }
    }

    /**
     * Demo test for <code>PlaceAssignmentAlgorithm</code>.
     */
    public void testPlaceAssignmentAlgorithmDemo() {
        PlaceAssignmentAlgorithm algo = new StandardPlaceAssignment();
        int[] original = new int[] {1, 2, 3, 2, 1, 7, 6, 4, 5};
        int[] received = algo.assignPlacements(original);

        for (int i = 0; i < received.length; ++i) {
            System.out.println(received[i]);
        }
    }

    /**
     * Demo test for <code>TieBreaker</code>.
     *
     * @throws Exception to JUnit
     */
    public void testTieBreakerDemo() throws Exception {
        TieBreaker breaker = new StandardTieBreaker();
        int[] received = breaker.breakTies(aggregatedSubs);

        for (int i = 0; i < received.length; ++i) {
            System.out.println(received[i]);
        }
    }

    /**
     * Demo test for <code>TieDetector</code>.
     */
    public void testTieDetectorDemo() {
        TieDetector detector = new StandardTieDetector();
        detector = new StandardTieDetector(0.3f);
        System.out.println(detector.tied(1, 25.1f));
        System.out.println(detector.tied(0.99f, 3));
        System.out.println(detector.tied(1.1f, 4));
        System.out.println(detector.tied(1, 3.9f));
        System.out.println(detector.tied(1, 1.1f));
    }

    /**
     * Demo test for <code>Submission</code>.
     */
    public void testSubmissionDemo() {
        Submission sub = new Submission(1, SCORES[0]);
        sub.getId();
        sub.getScores();
    }

    /**
     * Demo test for <code>AggregatedSubmission</code>.
     */
    public void testAggregatedSubmissionDemo() {
        Submission sub = new Submission(1, SCORES[0]);
        AggregatedSubmission aggregatedSub1 = new AggregatedSubmission(sub, 90.0f);
        AggregatedSubmission aggregatedSub2 = new AggregatedSubmission(2, SCORES[0], 80.0f);
        aggregatedSub1.getId();
        aggregatedSub2.getScores();
        aggregatedSub1.compareTo(aggregatedSub2);
    }

    /**
     * Demo test for <code>RankedSubmission</code>.
     */
    public void testRankedSubmissionDemo() {
        AggregatedSubmission aggregatedSub = new AggregatedSubmission(2, SCORES[0], 80.0f);
        RankedSubmission rankedSub = new RankedSubmission(aggregatedSub, 1);
        rankedSub.getRank();
    }

    /**
     * Demo test for <code>ReviewScoreAggregator</code>.
     *
     * @throws Exception to JUnit
     */
    public void testReviewScoreAggregatorDemo() throws Exception {
        ReviewScoreAggregator aggregator = new ReviewScoreAggregator();
        aggregator = new ReviewScoreAggregator(new AveragingAggregationAlgorithm(), new StandardTieDetector(),
                new StandardTieBreaker(), new StandardPlaceAssignment());
        aggregator = new ReviewScoreAggregator(ReviewScoreAggregator.DEFAULT_NAMESPACE);

        aggregator.setScoreAggregationAlgorithm(new AveragingAggregationAlgorithm());
        aggregator.getScoreAggregationAlgorithm();

        aggregator.setPlaceAssignmentAlgorithm(new StandardPlaceAssignment());
        aggregator.getPlaceAssignmentAlgorithm();

        aggregator.setTieBreaker(new StandardTieBreaker());
        aggregator.getTieBreaker();

        aggregator.setTieDetector(new StandardTieDetector(0.3f));
        aggregator.getTieDetector();

        aggregator.aggregateScores(SCORES);
        aggregator.aggregateScores(subs);

        aggregator.calcPlacements(SCORES);
        aggregator.calcPlacements(aggregatedSubs);
    }
}
