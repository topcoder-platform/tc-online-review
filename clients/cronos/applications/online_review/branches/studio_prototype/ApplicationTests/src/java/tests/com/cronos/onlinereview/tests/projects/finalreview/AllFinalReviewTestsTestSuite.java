/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.projects.finalreview;

import junit.framework.*;

public class AllFinalReviewTestsTestSuite extends TestSuite
{
    public AllFinalReviewTestsTestSuite(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        TestSuite suite = new AllFinalReviewTestsTestSuite("Final Review Tests");
        
        suite.addTestSuite(PerformFinalReviewTest.class);
        suite.addTestSuite(ViewFinalReviewTest.class);
        
        return suite;
    }
    
}
