/**
 *
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.cronos.onlinereview.deliverables;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        //suite.addTest(XXX.suite());

        suite.addTestSuite(AggregationDeliverableCheckerTest.class);
        suite.addTestSuite(AggregationReviewDeliverableCheckerTest.class);
        suite.addTestSuite(AppealResponsesDeliverableCheckerTest.class);
        suite.addTestSuite(CommittedReviewDeliverableCheckerTest.class);
        suite.addTestSuite(FinalFixesDeliverableCheckerTest.class);
        suite.addTestSuite(FinalReviewDeliverableCheckerTest.class);
        suite.addTestSuite(SingleQuerySqlDeliverableCheckerTest.class);
        suite.addTestSuite(SqlDeliverableCheckerTest.class);
        suite.addTestSuite(SubmissionDeliverableCheckerTest.class);
        suite.addTestSuite(SubmitterCommentDeliverableCheckerTest.class);
        suite.addTestSuite(TestCasesDeliverableCheckerTest.class);

        suite.addTestSuite(Demo.class);
        return suite;
    }

}
