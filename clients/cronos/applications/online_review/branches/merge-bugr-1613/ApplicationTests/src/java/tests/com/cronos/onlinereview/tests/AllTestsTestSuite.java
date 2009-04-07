/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests;

import com.cronos.onlinereview.tests.projects.AllProjectTestsTestSuite;
import com.cronos.onlinereview.tests.scorecard.AllScorecardTestsTestSuite;
import junit.framework.*;

public class AllTestsTestSuite extends TestCase
{
    public AllTestsTestSuite(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite("All Tests");
        
        suite.addTest(AllScorecardTestsTestSuite.suite());
        suite.addTest(AllProjectTestsTestSuite.suite());
        
        return suite;
    }
    
    public static void main(String[] args)
    {
        junit.textui.TestRunner.runAndWait(suite());
    }
}
