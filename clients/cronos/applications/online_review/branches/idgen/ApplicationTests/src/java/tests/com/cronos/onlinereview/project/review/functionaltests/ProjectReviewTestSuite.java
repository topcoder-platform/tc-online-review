/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author TCSTester
 * @version 1.0
 */
public class ProjectReviewTestSuite extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(EditAnyScorecardFunctionalTest.class);
        suite.addTestSuite(EditMyAppealResponseduringAppealsResponseFunctionalTest.class);
        suite.addTestSuite(EditMyReviewduringReviewFunctionalTest.class);
        suite.addTestSuite(PerformAggregationFunctionalTest.class);
        suite.addTestSuite(PerformAggregationReviewFunctionalTest.class);
        suite.addTestSuite(PerformAppealsFunctionalTest.class);
        suite.addTestSuite(PerformAppealsResponseFunctionalTest.class);
        suite.addTestSuite(PerformApprovalFunctionalTest.class);
        suite.addTestSuite(PerformFinalReviewFunctionalTest.class);
        suite.addTestSuite(PerformReviewFunctionalTest.class);
        suite.addTestSuite(PerformScreeningFunctionalTest.class);
        suite.addTestSuite(SubmitScorecardCommentFunctionalTest.class);
        suite.addTestSuite(ViewAggregationFunctionalTest.class);
        suite.addTestSuite(ViewAggregationReviewFunctionalTest.class);
        suite.addTestSuite(ViewAppealsFunctionalTest.class);
        suite.addTestSuite(ViewAppealsResponseFunctionalTest.class);
        suite.addTestSuite(ViewApprovalFunctionalTest.class);
        suite.addTestSuite(ViewCompositeReviewFunctionalTest.class);
        suite.addTestSuite(ViewFinalReviewFunctionalTest.class);
        suite.addTestSuite(ViewReviewerReviewsFunctionalTest.class);
        suite.addTestSuite(ViewReviewsFunctionalTest.class);
        suite.addTestSuite(ViewScreeningFunctionalTest.class);
        return suite;
    }
}
