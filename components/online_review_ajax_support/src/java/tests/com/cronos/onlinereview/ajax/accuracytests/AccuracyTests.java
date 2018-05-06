/*
 *
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.cronos.onlinereview.ajax.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all accuracy test cases.
 * </p>
 *
 * @author assistant
 * @version 1.0
 */
public class AccuracyTests extends TestCase {
    /**
     * aggregates all accuracy test cases.
     *
     * @return Test instance
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(TestSetTimelineNotificationHandler.class);
        suite.addTestSuite(TestSetScorecardStatusHandler.class);
        suite.addTestSuite(TestRoleResolutionException.class);
        suite.addTestSuite(TestReviewCommonHandler.class);
        suite.addTestSuite(TestResolveAppealHandler.class);
        suite.addTestSuite(TestRequestParsingException.class);
        suite.addTestSuite(TestConfigurationException.class);
        suite.addTestSuite(TestPlaceAppealHandler.class);
        suite.addTestSuite(TestLoadTimelineTemplateHandler.class);
        
        suite.addTestSuite(TestCommonHandler.class);
        suite.addTestSuite(TestAjaxResponse.class);
        suite.addTestSuite(TestAjaxRequest.class);
        suite.addTestSuite(TestAjaxSupportServlet.class);

        return suite;
    }
}
