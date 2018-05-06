/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    /**
     * Suite for all tests.
     * @return suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(SqlDeliverableCheckerTest.class);
        suite.addTestSuite(SingleQuerySqlDeliverableCheckerTest.class);
        suite.addTestSuite(SubmissionDeliverableCheckerTest.class);
        suite.addTestSuite(CommittedReviewDeliverableCheckerTest.class);
        suite.addTestSuite(TestCasesDeliverableCheckerTest.class);
        suite.addTestSuite(AggregationDeliverableCheckerTest.class);
        suite.addTestSuite(AggregationReviewDeliverableCheckerTest.class);
        suite.addTestSuite(FinalFixesDeliverableCheckerTest.class);
        suite.addTestSuite(SubmitterCommentDeliverableCheckerTest.class);
        suite.addTestSuite(FinalReviewDeliverableCheckerTest.class);
        suite.addTestSuite(AppealResponsesDeliverableCheckerTest.class);
        return suite;
    }
}