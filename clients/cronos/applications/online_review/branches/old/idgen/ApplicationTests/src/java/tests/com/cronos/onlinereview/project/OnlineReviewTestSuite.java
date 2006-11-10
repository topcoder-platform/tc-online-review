/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.cronos.onlinereview.project.review.functionaltests.ProjectReviewTestSuite;
import com.cronos.onlinereview.project.admin.functionaltests.ProjectAdminAndDetailsTestSuite;

/**
 * @author TCSTester
 * @version 1.0
 */
public class OnlineReviewTestSuite extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(ProjectReviewTestSuite.suite());
        suite.addTest(ProjectAdminAndDetailsTestSuite.suite());
        return suite;
    }
}
