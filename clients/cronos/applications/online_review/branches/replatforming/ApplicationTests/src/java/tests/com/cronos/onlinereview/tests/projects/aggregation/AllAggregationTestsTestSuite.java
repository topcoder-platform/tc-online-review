/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.projects.aggregation;

import junit.framework.*;

public class AllAggregationTestsTestSuite extends TestSuite
{
    public AllAggregationTestsTestSuite(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        TestSuite suite = new AllAggregationTestsTestSuite("Aggregation Tests");

        suite.addTestSuite(PerformAggregationTest.class);
        suite.addTestSuite(ViewAggregationTest.class);
        suite.addTestSuite(PerformAggregationReviewTest.class);
        
        return suite;
    }
    
}
