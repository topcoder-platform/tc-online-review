/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.projects.appeals;

import junit.framework.*;

public class AllAppealsTestsTestSuite extends TestSuite
{
    public AllAppealsTestsTestSuite(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        TestSuite suite = new AllAppealsTestsTestSuite("Appeals Tests");

        suite.addTestSuite(PerformAppealsTest.class);
        suite.addTestSuite(ViewAppealsTest.class);
        suite.addTestSuite(PerformAppealsResponseTest.class);
        suite.addTestSuite(ViewAppealsResponseTest.class);
        suite.addTestSuite(EditAppealsResponseTest.class);
        
        return suite;
    }
    
}
