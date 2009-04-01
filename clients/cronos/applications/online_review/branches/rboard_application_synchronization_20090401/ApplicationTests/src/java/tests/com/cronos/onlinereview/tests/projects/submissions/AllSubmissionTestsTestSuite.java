/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.projects.submissions;

import junit.framework.*;

public class AllSubmissionTestsTestSuite extends TestSuite
{
    public AllSubmissionTestsTestSuite(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        TestSuite suite = new AllSubmissionTestsTestSuite("Submission Tests");
        
        suite.addTestSuite(PerformSubmissionTest.class);
        suite.addTestSuite(ViewSubmissionTest.class);
        suite.addTestSuite(RemoveSubmissionTest.class);
        
        return suite;
    }
    
}
