/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.projects.endphase;

import junit.framework.*;

public class AllEndPhaseTestsTestSuite extends TestSuite
{
    public AllEndPhaseTestsTestSuite(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        TestSuite suite = new AllEndPhaseTestsTestSuite("End Phase Tests");
        
        suite.addTestSuite(EndPhaseSubmissionTest.class);
        suite.addTestSuite(EndPhaseReviewTest.class);
        suite.addTestSuite(EndPhaseAppealsTest.class);
        suite.addTestSuite(EndPhaseAppealsResponseTest.class);
        suite.addTestSuite(EndPhaseAggregationTest.class);
        suite.addTestSuite(EndPhaseAggregationReviewTest.class);
        suite.addTestSuite(EndPhaseFinalFixTest.class);
        suite.addTestSuite(EndPhaseFinalReviewTest.class);
        suite.addTestSuite(EndPhaseApprovalTest.class);
        
        return suite;
    }
    
}
