/**
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.review.scoreaggregator.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 *
 * @author GavinWang
 * @version 1.0
 */
public class FailureTests extends TestCase {
    /**
     * <p>
     * Aggregates all failure test cases.
     * </p>
     *
     * @return all all failure test cases
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(SubmissionFailureTests.class);
        suite.addTestSuite(AggregatedSubmissionFailureTests.class);
        suite.addTestSuite(RankedSubmissionFailureTests.class);
        suite.addTestSuite(AveragingAggregationAlgorithmFailureTests.class);
        suite.addTestSuite(StandardPlaceAssignmentFailureTests.class);
        suite.addTestSuite(StandardTieBreakerFailureTests.class);
        suite.addTestSuite(StandardTieDetectorFailureTests.class);
        suite.addTestSuite(ReviewScoreAggregatorFailureTests.class);

        return suite;
    }
}
