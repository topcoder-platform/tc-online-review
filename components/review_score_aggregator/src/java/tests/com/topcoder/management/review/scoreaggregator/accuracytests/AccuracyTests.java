/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public class AccuracyTests extends TestCase {
    /**
     * Aggregates all Accuracy test cases and returns a testSuite.
     *
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(AggregatedSubmissionAccuracyTests.class);
        suite.addTestSuite(AveragingAggregationAlgorithmAccuracyTests.class);
        suite.addTestSuite(RankedSubmissionAccuracyTests.class);
        suite.addTestSuite(ReviewScoreAggregatorAccuracyTests.class);
        suite.addTestSuite(StandardPlaceAssignmentAccuracyTests.class);
        suite.addTestSuite(StandardTieBreakerAccuracyTests.class);
        suite.addTestSuite(StandardTieDetectorAccuracyTests.class);
        suite.addTestSuite(SubmissionAccuracyTests.class);

        return suite;
    }
}
