/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.projects.finalfix;

import junit.framework.*;

public class AllFinalFixTestsTestSuite extends TestSuite
{
    public AllFinalFixTestsTestSuite(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        TestSuite suite = new AllFinalFixTestsTestSuite("Final Fix Tests");
        
        suite.addTestSuite(PerformFinalFixTest.class);
        suite.addTestSuite(DownloadFinalFixTest.class);
        
        return suite;
    }
    
}
