/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

import com.topcoder.management.review.scoreaggregator.impl.AveragingAggregationAlgorithmTest;
import com.topcoder.management.review.scoreaggregator.impl.StandardPlaceAssignmentTest;
import com.topcoder.management.review.scoreaggregator.impl.StandardTieBreakerTest;
import com.topcoder.management.review.scoreaggregator.impl.StandardTieDetectorTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author daiwb
 * @version 1.0
 */
public class UnitTests extends TestCase {
    /**
     * Returns the test suite of unit test cases.
     *
     * @return the test suite of unit test cases.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // Package "com.topcoder.management.review.scoreaggregator"
        suite.addTest(SubmissionTest.suite());
        suite.addTest(AggregatedSubmissionTest.suite());
        suite.addTest(RankedSubmissionTest.suite());
        suite.addTest(ReviewScoreAggregatorTest.suite());
        suite.addTest(ReviewScoreAggregatorExceptionTest.suite());
        suite.addTest(ReviewScoreAggregatorConfigExceptionTest.suite());
        suite.addTest(InconsistentDataExceptionTest.suite());
        suite.addTest(DemoTest.suite());

        // Package "com.topcoder.management.review.scoreaggregator.impl"
        suite.addTest(AveragingAggregationAlgorithmTest.suite());
        suite.addTest(StandardPlaceAssignmentTest.suite());
        suite.addTest(StandardTieBreakerTest.suite());
        suite.addTest(StandardTieDetectorTest.suite());

        return suite;
    }
}
