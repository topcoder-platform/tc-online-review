/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.scorecard;

import junit.framework.*;

public class AllScorecardTestsTestSuite extends TestSuite
{
    public AllScorecardTestsTestSuite(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        TestSuite suite = new AllScorecardTestsTestSuite("Scorecard Tests");
        
        suite.addTestSuite(CreateScorecardTest.class);
        suite.addTestSuite(EditScorecardTest.class);
        suite.addTestSuite(ViewScorecardTest.class);
        
        return suite;
    }
    
}
