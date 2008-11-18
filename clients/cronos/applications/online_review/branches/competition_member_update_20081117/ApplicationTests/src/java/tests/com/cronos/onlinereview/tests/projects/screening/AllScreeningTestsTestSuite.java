/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.projects.screening;

import junit.framework.*;

public class AllScreeningTestsTestSuite extends TestSuite
{
    public AllScreeningTestsTestSuite(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        TestSuite suite = new AllScreeningTestsTestSuite("Screening Tests");
        
        suite.addTestSuite(PerformScreeningTest.class);
        suite.addTestSuite(ViewScreeningTest.class);
        
        return suite;
    }
    
}
