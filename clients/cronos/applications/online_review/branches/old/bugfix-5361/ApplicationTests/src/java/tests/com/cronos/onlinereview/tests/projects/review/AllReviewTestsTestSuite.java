/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.projects.review;

import junit.framework.*;

public class AllReviewTestsTestSuite extends TestSuite
{
    public AllReviewTestsTestSuite(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        TestSuite suite = new AllReviewTestsTestSuite("Review Tests");
        
        suite.addTestSuite(PerformReviewTest.class);
        suite.addTestSuite(UploadTestCasesTest.class);
        suite.addTestSuite(EditReviewTest.class);
        suite.addTestSuite(ViewReviewTest.class);
        
        return suite;
    }
    
}
