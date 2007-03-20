/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.projects.approval;

import junit.framework.*;

public class AllApprovalTestsTestSuite extends TestSuite
{
    public AllApprovalTestsTestSuite(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        TestSuite suite = new AllApprovalTestsTestSuite("Approval Tests");

        suite.addTestSuite(PerformApprovalTest.class);
        suite.addTestSuite(ViewApprovalTest.class);
        
        return suite;
    }
    
}
