/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 *
 * @author kinfkong
 * @version 1.0
 */
public class FailureTests extends TestCase {

    /**
     * Returns the test cases.
     *
     * @return the test cases
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(AggregationDeliverableCheckerFailureTest.class);
        suite.addTestSuite(AggregationReviewDeliverableCheckerFailureTest.class);
        suite.addTestSuite(AppealResponsesDeliverableCheckerFailureTest.class);
        suite.addTestSuite(CommittedReviewDeliverableCheckerFailureTest.class);
        suite.addTestSuite(FinalFixesDeliverableCheckerFailureTest.class);
        suite.addTestSuite(FinalReviewDeliverableCheckerFailureTest.class);
        suite.addTestSuite(SingleQuerySqlDeliverableCheckerFailureTest.class);
        suite.addTestSuite(SubmissionDeliverableCheckerFailureTest.class);
        suite.addTestSuite(SubmitterCommentDeliverableCheckerFailureTest.class);
        suite.addTestSuite(TestCasesDeliverableCheckerFailureTest.class);

        return suite;
    }

}
