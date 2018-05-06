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
 * This test case aggregates all test cases for <code>ReviewScoreAggregator</code>.
 * </p>
 *
 * @author daiwb
 * @version 1.0
 */
public class ReviewScoreAggregatorTest extends TestCase {
    /**
     * Configuration file for ObjectFactory.
     */
    private static final String CONFIG_FILE = "test_files" + File.separator + "Standard.xml";

    /**
     * Default namespace that external clients can use when configuring this component.
     */
    private static final String NAMESPACE = ReviewScoreAggregator.DEFAULT_NAMESPACE;

    /**
     * Scores used for tests.
     */
    private static float[][] scores = new float[][] {
        {97.28f, 97.44f, 93.47f},
        {89.25f, 98.47f, 94.47f},
        {86.81f, 96.75f, 93.59f},
        {92.81f, 90.94f, 91.50f},
        {78.03f, 86.63f, 86.44f},
        {78.41f, 82.50f, 79.03f},
        {60.81f, 71.22f, 70.97f}};

    /**
     * A <code>ScoreAggregationAlgorithm</code> instance used for tests.
     */
    private ScoreAggregationAlgorithm aggregationAlgo = null;

    /**
     * A <code>tieDetector</code> instance used for tests.
     */
    private TieDetector tieDetector = null;

    /**
     * A <code>TieBreaker</code> instance used for tests.
     */
    private TieBreaker tieBreaker = null;

    /**
     * A <code>PlaceAssignmentAlgorithm</code> instance used for tests.
     */
    private PlaceAssignmentAlgorithm placeAssignmentAlgo = null;

    /**
     * A <code>ReviewScoreAggregator</code> instance used for tests.
     */
    private ReviewScoreAggregator aggregator = null;

    /**
     * Average scores for each submission.
     */
    private float[] avers = null;

    /**
     * Array of <code>Submission</code> used for tests.
     */
    private Submission[] submissions = null;

    /**
     * Returns the test suite of this test case.
     *
     * @return the test suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(ReviewScoreAggregatorTest.class);
    }

    /**
     * Set up.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        aggregationAlgo = new AveragingAggregationAlgorithm();
        tieDetector = new StandardTieDetector();
        tieBreaker = new StandardTieBreaker();
        placeAssignmentAlgo = new StandardPlaceAssignment();

        aggregator = new ReviewScoreAggregator(aggregationAlgo, tieDetector, tieBreaker, placeAssignmentAlgo);

        avers = new float[7];

        for (int i = 0; i < 7; ++i) {
            float sum = 0;

            for (int j = 0; j < 3; ++j) {
                sum += scores[i][j];
            }

            avers[i] = sum / 3;
        }

        submissions = new Submission[7];
        for (int i = 0; i < 7; ++i) {
            submissions[i] = new Submission(i + 1, scores[i]);
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
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        aggregationAlgo = null;
        tieDetector = null;
        tieBreaker = null;
        placeAssignmentAlgo = null;
        aggregationAlgo = null;

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
     * Test constructor
     * <code>ReviewScoreAggregator(ScoreAggregationAlgorithm, TieDetector, TieBreaker, PlaceAssignmentAlgorithm)</code>.
     */
    public void testReviewScoreAggregatorScoreAggregationAlgorithmDetectorBreakerPlaceAssignmentAlgorithm_Accuracy() {
        assertNotNull("The instance should be created successfully.", aggregator);
    }

    /**
     * Test constructor
     * <code>ReviewScoreAggregator(ScoreAggregationAlgorithm, TieDetector, TieBreaker, PlaceAssignmentAlgorithm)</code>
     * with null <code>aggregator</code>. <code>IllegalArgumentException</code> is expected.
     */
    public void testReviewScoreAggregatorScoreAggregationAlgorithmDetectorBreakerPlaceAssignmentAlgorithm_NullArg_1() {
        try {
            new ReviewScoreAggregator(null, tieDetector, tieBreaker, placeAssignmentAlgo);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>ReviewScoreAggregator(ScoreAggregationAlgorithm, TieDetector, TieBreaker, PlaceAssignmentAlgorithm)</code>
     * with null <code>tieDetector</code>. <code>IllegalArgumentException</code> is expected.
     */
    public void testReviewScoreAggregatorScoreAggregationAlgorithmDetectorBreakerPlaceAssignmentAlgorithm_NullArg_2() {
        try {
            new ReviewScoreAggregator(aggregationAlgo, null, tieBreaker, placeAssignmentAlgo);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>ReviewScoreAggregator(ScoreAggregationAlgorithm, TieDetector, TieBreaker, PlaceAssignmentAlgorithm)</code>
     * with null <code>tieBreaker</code>. <code>IllegalArgumentException</code> is expected.
     */
    public void testReviewScoreAggregatorScoreAggregationAlgorithmDetectorBreakerPlaceAssignmentAlgorithm_NullArg_3() {
        try {
            new ReviewScoreAggregator(aggregationAlgo, tieDetector, null, placeAssignmentAlgo);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>ReviewScoreAggregator(ScoreAggregationAlgorithm, TieDetector, TieBreaker, PlaceAssignmentAlgorithm)</code>
     * with null <code>placeAssignmentAlgorithm</code>. <code>IllegalArgumentException</code> is expected.
     */
    public void testReviewScoreAggregatorScoreAggregationAlgorithmDetectorBreakerPlaceAssignmentAlgorithm_NullArg_4() {
        try {
            new ReviewScoreAggregator(aggregationAlgo, tieDetector, tieBreaker, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>ReviewScoreAggregator(String)</code> with null <code>namespace</code>.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testReviewScoreAggregatorString_NullArg() throws Exception {
        try {
            new ReviewScoreAggregator(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>ReviewScoreAggregator(String)</code> with empty string <code>namespace</code>.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testReviewScoreAggregatorString_EmptyStringArg_1() throws Exception {
        try {
            new ReviewScoreAggregator("");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>ReviewScoreAggregator(String)</code> with empty string <code>namespace</code>.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testReviewScoreAggregatorString_EmptyStringArg_2() throws Exception {
        try {
            new ReviewScoreAggregator("  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>ReviewScoreAggregator(String)</code> with empty string <code>namespace</code>.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testReviewScoreAggregatorString_EmptyStringArg_3() throws Exception {
        try {
            new ReviewScoreAggregator("\t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>ReviewScoreAggregator(String)</code> with empty string <code>namespace</code>.
     * <code>ReviewScoreAggregatorConfigException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testReviewScoreAggregatorString_StringNotExist() throws Exception {
        try {
            new ReviewScoreAggregator("not exist");
            fail("ReviewScoreAggregatorConfigException is expected.");
        } catch (ReviewScoreAggregatorConfigException rsace) {
            // Success
        }
    }

    /**
     * Test constructor <code>ReviewScoreAggregator(String)</code> with invalid configuration.
     * The ScoreAggregationAlgorithm property is missing.
     * <code>ReviewScoreAggregatorConfigException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testReviewScoreAggregatorString_InvalidCfg_1() throws Exception {
        try {
            ConfigManager cm = ConfigManager.getInstance();
            String filename = "test_files" + File.separator + "Invalid1.xml";
            File file = new File(filename);
            cm.add(file.getAbsolutePath());
            new ReviewScoreAggregator("invalid1");
            fail("ReviewScoreAggregatorConfigException is expected.");
        } catch (ReviewScoreAggregatorConfigException rsace) {
            // Success
        }
    }

    /**
     * Test constructor <code>ReviewScoreAggregator(String)</code> with invalid configuration.
     * The TieDetector property is missing.
     * <code>ReviewScoreAggregatorConfigException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testReviewScoreAggregatorString_InvalidCfg_2() throws Exception {
        try {
            ConfigManager cm = ConfigManager.getInstance();
            String filename = "test_files" + File.separator + "Invalid2.xml";
            File file = new File(filename);
            cm.add(file.getAbsolutePath());
            new ReviewScoreAggregator("invalid2");
            fail("ReviewScoreAggregatorConfigException is expected.");
        } catch (ReviewScoreAggregatorConfigException rsace) {
            // Success
        }
    }

    /**
     * Test constructor <code>ReviewScoreAggregator(String)</code> with invalid configuration.
     * The TieBreaker property is missing.
     * <code>ReviewScoreAggregatorConfigException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testReviewScoreAggregatorString_InvalidCfg_3() throws Exception {
        try {
            ConfigManager cm = ConfigManager.getInstance();
            String filename = "test_files" + File.separator + "Invalid3.xml";
            File file = new File(filename);
            cm.add(file.getAbsolutePath());
            new ReviewScoreAggregator("invalid3");
            fail("ReviewScoreAggregatorConfigException is expected.");
        } catch (ReviewScoreAggregatorConfigException rsace) {
            // Success
        }
    }

    /**
     * Test constructor <code>ReviewScoreAggregator(String)</code> with invalid configuration.
     * The PlaceAssignmentAlgorithm property is missing.
     * <code>ReviewScoreAggregatorConfigException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testReviewScoreAggregatorString_InvalidCfg_4() throws Exception {
        try {
            ConfigManager cm = ConfigManager.getInstance();
            String filename = "test_files" + File.separator + "Invalid4.xml";
            File file = new File(filename);
            cm.add(file.getAbsolutePath());
            new ReviewScoreAggregator("invalid4");
            fail("ReviewScoreAggregatorConfigException is expected.");
        } catch (ReviewScoreAggregatorConfigException rsace) {
            // Success
        }
    }

    /**
     * Test constructor <code>ReviewScoreAggregator(String)</code> with <code>namespace</code> not exist.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testReviewScoreAggregatorString_EmptyStringArg_4() throws Exception {
        try {
            new ReviewScoreAggregator("\n");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>ReviewScoreAggregator(String)</code>.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testReviewScoreAggregatorString_Accuracy() throws Exception {
        aggregator = new ReviewScoreAggregator(NAMESPACE);
        assertNotNull("The instance should be created successfully.", aggregator);
        assertNotNull("The aggregationAlgorithm field value should be set successfully.", aggregator
                .getScoreAggregationAlgorithm());
        assertNotNull("The tieDetector field value should be set successfully.", aggregator.getTieDetector());
        assertNotNull("The tieBreaker field value should be set successfully.", aggregator.getTieBreaker());
        assertNotNull("The placeAssignmentAlgorithm field value should be set successfully.", aggregator
                .getPlaceAssignmentAlgorithm());
    }

    /**
     * Test constructor <code>ReviewScoreAggregator()</code>.
     */
    public void testReviewScoreAggregator_Accuracy() {
        aggregator = new ReviewScoreAggregator();
        assertNotNull("The instance should be created successfully.", aggregator);
    }

    /**
     * Test method <code>aggregateScores(float[][])</code>.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAggregateScoresFloatArrayArray_Accuracy() throws Exception {
        AggregatedSubmission[] subs = aggregator.aggregateScores(scores);
        assertEquals("There should be 7 submissions.", 7, subs.length);

        for (int i = 0; i < subs.length; ++i) {
            assertEquals("The id for submission " + i + " should be " + (i + 1), i + 1, subs[i].getId());
            assertEquals("The aggregate score for submission " + (i + 1) + " mismatches.", avers[i], subs[i]
                    .getAggregatedScore(), 1e-8);
        }
    }

    /**
     * Test method <code>aggregateScores(float[][])</code> with empty array.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAggregateScoresFloatArrayArray_EmptyArray() throws Exception {
        AggregatedSubmission[] subs = aggregator.aggregateScores(new float[][] {});
        assertEquals("Empty array should be returned..", 0, subs.length);
    }

    /**
     * Test method <code>aggregateScores(float[][])</code> with null <code>submissions</code>.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAggregateScoresFloatArrayArray_NullSubmissions() throws Exception {
        try {
            aggregator.aggregateScores((float[][]) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>aggregateScores(float[][])</code> with <code>submissions</code> containing null element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAggregateScoresFloatArrayArray_SubmissionsWithNullElement() throws Exception {
        try {
            aggregator.aggregateScores(new float[][] {{0.0f, 90.0f}, null});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>aggregateScores(float[][])</code> with <code>submissions</code> containing negative
     * element. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAggregateScoresFloatArrayArray_SubmissionsWithNegativeElement() throws Exception {
        try {
            aggregator.aggregateScores(new float[][] {{0.0f, 90.0f}, {45.0f, -10.0f}});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>aggregateScores(float[][])</code> with <code>submissions</code> containing invalid element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAggregateScoresFloatArrayArray_SubmissionsWithInvalidElement_1() throws Exception {
        try {
            aggregator.aggregateScores(new float[][] {{0.0f, 90.0f}, {45.0f, Float.NaN}});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>aggregateScores(float[][])</code> with <code>submissions</code> containing invalid element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAggregateScoresFloatArrayArray_SubmissionsWithInvalidElement_2() throws Exception {
        try {
            aggregator.aggregateScores(new float[][] {{0.0f, 90.0f}, {45.0f, Float.POSITIVE_INFINITY}});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>aggregateScores(float[][])</code> with <code>submissions</code> containing invalid element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAggregateScoresFloatArrayArray_SubmissionsWithInvalidElement_3() throws Exception {
        try {
            aggregator.aggregateScores(new float[][] {{0.0f, 90.0f}, {45.0f, Float.NEGATIVE_INFINITY}});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>aggregateScores(float[][])</code> with inconsisitent <code>submissions</code>.
     * <code>InconsistentDataException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAggregateScoresFloatArrayArray_InconsistentSubmissions() throws Exception {
        try {
            aggregator.aggregateScores(new float[][] {{0.0f, 90.0f}, {45.0f, 10.0f, 20.0f}});
            fail("InconsistentDataException is expected.");
        } catch (InconsistentDataException ide) {
            // Success
        }
    }

    /**
     * Test constructor <code>aggregateScores(Submission[])</code>.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAggregateScoresSubmissionArray_Accuracy() throws Exception {
        AggregatedSubmission[] subs = aggregator.aggregateScores(submissions);
        assertEquals("There should be 7 submissions.", 7, subs.length);

        for (int i = 0; i < subs.length; ++i) {
            assertEquals("The id for submission " + i + " should be " + (i + 1), i + 1, subs[i].getId());
            assertEquals("The aggregate score for submission " + (i + 1) + " mismatches.", avers[i], subs[i]
                    .getAggregatedScore(), 1e-8);
        }
    }

    /**
     * Test constructor <code>aggregateScores(Submission[])</code> with empty array.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAggregateScoresSubmissionArray_EmptyArray() throws Exception {
        AggregatedSubmission[] subs = aggregator.aggregateScores(new Submission[0]);
        assertEquals("Empty array should be returned.", 0, subs.length);
    }

    /**
     * Test constructor <code>aggregateScores(Submission[])</code> with null <code>submissions</code>.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAggregateScoresSubmissionArray_NullArg() throws Exception {
        try {
            aggregator.aggregateScores((Submission[]) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>aggregateScores(Submission[])</code> with <code>submissions</code> containing null
     * element. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAggregateScoresSubmissionArray_SubmissionsWithNullElement() throws Exception {
        try {
            aggregator.aggregateScores(new Submission[] {submissions[0], null});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>aggregateScores(Submission[])</code> with inconsistent <code>submissions</code>.
     * <code>InconsistentDataException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAggregateScoresSubmissionArray_InconsistentSubmissions() throws Exception {
        try {
            Submission submission = new Submission(11, new float[] {11.0f, 20.0f});
            aggregator.aggregateScores(new Submission[] {submissions[0], submission});
            fail("InconsistentDataException is expected.");
        } catch (InconsistentDataException ide) {
            // Success
        }
    }

    /**
     * Test method <code>calcPlacements(float[][])</code>.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsFloatArrayArray_Accuracy_1() throws Exception {
        aggregator.setTieDetector(new StandardTieDetector(10.0f));
        RankedSubmission[] rankedSubs = aggregator.calcPlacements(scores);
        int[] expected = new int[] {2, 1, 3, 4, 5, 6, 7};
        assertEquals("Received array not match expected one.", expected.length, rankedSubs.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], rankedSubs[i].getRank());
        }
    }

    /**
     * Test method <code>calcPlacements(float[][])</code>.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsFloatArrayArray_Accuracy_2() throws Exception {
        RankedSubmission[] rankedSubs = aggregator.calcPlacements(scores);
        int[] expected = new int[] {1, 2, 3, 4, 5, 6, 7};
        assertEquals("Received array not match expected one.", expected.length, rankedSubs.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], rankedSubs[i].getRank());
        }
    }

    /**
     * Test method <code>calcPlacements(float[][])</code> with empty array.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsFloatArrayArray_EmptyArray() throws Exception {
        RankedSubmission[] rankedSubs = aggregator.calcPlacements(new float[][] {});
        assertEquals("Empty array should be returned.", 0, rankedSubs.length);
    }

    /**
     * Test method <code>calcPlacements(float[][])</code> with null <code>scores</code>.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsFloatArrayArray_NullArg() throws Exception {
        try {
            aggregator.calcPlacements((float[][]) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>calcPlacements(float[][])</code> with <code>scores</code> containing null element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsFloatArrayArray_ArrayWithNullElement() throws Exception {
        try {
            aggregator.calcPlacements(new float[][] {null});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>calcPlacements(float[][])</code> with <code>scores</code> containing invalid element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsFloatArrayArray_ArrayWithInvalidElement_1() throws Exception {
        try {
            aggregator.calcPlacements(new float[][] {{Float.NaN}});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>calcPlacements(float[][])</code> with <code>scores</code> containing invalid element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsFloatArrayArray_ArrayWithInvalidElement_2() throws Exception {
        try {
            aggregator.calcPlacements(new float[][] {{Float.POSITIVE_INFINITY}});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>calcPlacements(float[][])</code> with <code>scores</code> containing invalid element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsFloatArrayArray_ArrayWithInvalidElement_3() throws Exception {
        try {
            aggregator.calcPlacements(new float[][] {{Float.NEGATIVE_INFINITY}});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>calcPlacements(float[][])</code> with inconsistent <code>scores</code>.
     * <code>InconsistentDataException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsFloatArrayArray_InconsistentArray() throws Exception {
        try {
            aggregator.calcPlacements(new float[][] {{10.0f}, {20.0f, 30.0f}});
            fail("InconsistentDataException is expected.");
        } catch (InconsistentDataException ide) {
            // Success
        }
    }

    /**
     * Test method <code>calcPlacements(AggregatedSubmission[])</code>.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsAggregatedSubmissionArray_Accuracy_1() throws Exception {
        AggregatedSubmission[] subs = new AggregatedSubmission[7];
        for (int i = 0; i < 7; ++i) {
            subs[i] = new AggregatedSubmission(1, scores[i], aggregationAlgo.calculateAggregateScore(scores[i]));
        }
        aggregator.setTieDetector(new StandardTieDetector(10.0f));
        RankedSubmission[] rankedSubs = aggregator.calcPlacements(subs);
        int[] expected = new int[] {2, 1, 3, 4, 5, 6, 7};
        assertEquals("Received array not match expected one.", expected.length, rankedSubs.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], rankedSubs[i].getRank());
        }
    }

    /**
     * Test method <code>calcPlacements(AggregatedSubmission[])</code>.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsAggregatedSubmissionArray_Accuracy_2() throws Exception {
        AggregatedSubmission[] subs = new AggregatedSubmission[7];
        for (int i = 0; i < 7; ++i) {
            subs[i] = new AggregatedSubmission(1, scores[i], aggregationAlgo.calculateAggregateScore(scores[i]));
        }
        RankedSubmission[] rankedSubs = aggregator.calcPlacements(subs);
        int[] expected = new int[] {1, 2, 3, 4, 5, 6, 7};
        assertEquals("Received array not match expected one.", expected.length, rankedSubs.length);
        for (int i = 0; i < expected.length; ++i) {
            // System.out.println(rankedSubs[i].getRank());
            assertEquals("Received array not match expected one.", expected[i], rankedSubs[i].getRank());
        }
    }

    /**
     * Test method <code>calcPlacements(AggregatedSubmission[])</code> with empty array.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsAggregatedSubmissionArray_EmptyArray() throws Exception {
        RankedSubmission[] rankedSubs = aggregator.calcPlacements(new AggregatedSubmission[0]);
        assertEquals("Empty array should be returned.", 0, rankedSubs.length);
    }

    /**
     * Test method <code>calcPlacements(AggregatedSubmission[])</code> with null <code>submissions</code>.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsAggregatedSubmissionArray_NullArg() throws Exception {
        try {
            aggregator.calcPlacements((AggregatedSubmission[]) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>calcPlacements(AggregatedSubmission[])</code> with <code>submissions</code> containing null
     * element. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsAggregatedSubmissionArray_ArrayWithNullElement() throws Exception {
        try {
            aggregator.calcPlacements(new AggregatedSubmission[] {null});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>calcPlacements(AggregatedSubmission[])</code> with inconsistent <code>submissions</code>.
     * <code>InconsistentDataException</code> is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCalcPlacementsAggregatedSubmissionArray_InconsistentArray() throws Exception {
        try {
            AggregatedSubmission sub1 = new AggregatedSubmission(1, new float[] {10.0f}, 10.0f);
            AggregatedSubmission sub2 = new AggregatedSubmission(2, new float[] {20.0f, 40.0f}, 30.0f);
            aggregator.calcPlacements(new AggregatedSubmission[] {sub1, sub2});
            fail("InconsistentDataException is expected.");
        } catch (InconsistentDataException ide) {
            // Success
        }
    }

    /**
     * Test method <code>getScoreAggregationAlgorithm()</code>.
     */
    public void testGetScoreAggregationAlgorithm_Accuracy() {
        assertEquals("The aggregationAlgorithm field value should be returned.", aggregationAlgo, aggregator
                .getScoreAggregationAlgorithm());
    }

    /**
     * Test method <code>setScoreAggregationAlgorithm(ScoreAggregationAlgorithm)</code>.
     */
    public void testSetScoreAggregationAlgorithm_Accuracy() {
        AveragingAggregationAlgorithm algo = new AveragingAggregationAlgorithm();
        aggregator.setScoreAggregationAlgorithm(algo);
        assertEquals("The aggregationAlgorithm field value should be set successfully.", algo, aggregator
                .getScoreAggregationAlgorithm());
    }

    /**
     * Test method <code>setScoreAggregationAlgorithm(ScoreAggregationAlgorithm)</code> with null
     * <code>aggregationAlgorithm</code>. <code>IllegalArgumentException</code> is expected.
     */
    public void testSetScoreAggregationAlgorithm_NullArg() {
        try {
            aggregator.setScoreAggregationAlgorithm(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>getPlaceAssignmentAlgorithm()</code>.
     */
    public void testGetPlaceAssignmentAlgorithm_Accuracy() {
        assertEquals("The placeAssignmentAlgorithm field value should be returned.", placeAssignmentAlgo, aggregator
                .getPlaceAssignmentAlgorithm());
    }

    /**
     * Test method <code>setPlaceAssignmentAlgorighm(PlaceAssignmentAlgorithm)</code>.
     */
    public void testSetPlaceAssignmentAlgorighm_Accuracy() {
        StandardPlaceAssignment algo = new StandardPlaceAssignment();
        aggregator.setPlaceAssignmentAlgorithm(algo);
        assertEquals("The placeAssignmentAlgorithm field value should be set successfully.", algo, aggregator
                .getPlaceAssignmentAlgorithm());
    }

    /**
     * Test method <code>setPlaceAssignmentAlgorighm(PlaceAssignmentAlgorithm)</code> with null
     * <code>placeAssignmentAlgorithm</code>. <code>IllegalArgumentException</code> is expected.
     */
    public void testSetPlaceAssignmentAlgorighm_NullArg() {
        try {
            aggregator.setPlaceAssignmentAlgorithm(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>getTieBreaker()</code>.
     */
    public void testGetTieBreaker_Accuracy() {
        assertEquals("The tieBreaker field value should be returned.", tieBreaker, aggregator.getTieBreaker());
    }

    /**
     * Test method <code>setTieBreaker(TieBreaker)</code>.
     */
    public void testSetTieBreaker_Accuracy() {
        StandardTieBreaker breaker = new StandardTieBreaker();
        aggregator.setTieBreaker(breaker);
        assertEquals("The tieBreaker field value should be set successfully.", breaker, aggregator.getTieBreaker());
    }

    /**
     * Test method <code>setTieBreaker(TieBreaker)</code> with null <code>tieBreaker</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testSetTieBreaker_NullArg() {
        try {
            aggregator.setTieBreaker(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>getTieDetector()</code>.
     */
    public void testGetTieDetector_Accuracy() {
        assertEquals("The tieDetector field value should be returned.", tieDetector, aggregator.getTieDetector());
    }

    /**
     * Test method <code>setTieDetector(TieDetector)</code>.
     */
    public void testSetTieDetector_Accuracy() {
        StandardTieDetector detector = new StandardTieDetector();
        aggregator.setTieDetector(detector);
        assertEquals("The tieDetector field value should be set successfully..", detector, aggregator.getTieDetector());
    }

    /**
     * Test method <code>setTieDetector(TieDetector)</code> with null <code>tieDetector</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testSetTieDetector_NullArg() {
        try {
            aggregator.setTieDetector(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }
}
