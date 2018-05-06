/**
 *
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.cronos.onlinereview.ajax;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.cronos.onlinereview.ajax.handlers.RoleResolutionExceptionTest;
import com.cronos.onlinereview.ajax.handlers.PlaceAppealHandlerTest;
import com.cronos.onlinereview.ajax.handlers.ReviewCommonHandlerTest;
import com.cronos.onlinereview.ajax.handlers.SetScorecardStatusHandlerTest;
import com.cronos.onlinereview.ajax.handlers.ResolveAppealHandlerTest;
import com.cronos.onlinereview.ajax.handlers.LoadTimelineTemplateHandlerTest;
import com.cronos.onlinereview.ajax.handlers.CommonHandlerTest;
import com.cronos.onlinereview.ajax.handlers.SetTimelineNotificationHandlerTest;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(AjaxRequestTest.class);
        suite.addTestSuite(AjaxResponseTest.class);
        suite.addTestSuite(AjaxSupportHelperTest.class);
        suite.addTestSuite(AjaxSupportServletTest.class);
        suite.addTestSuite(ConfigurationExceptionTest.class);
        suite.addTestSuite(RequestParsingExceptionTest.class);
        suite.addTestSuite(CommonHandlerTest.class);
        suite.addTestSuite(ReviewCommonHandlerTest.class);
        suite.addTestSuite(LoadTimelineTemplateHandlerTest.class);
        suite.addTestSuite(PlaceAppealHandlerTest.class);
        suite.addTestSuite(ResolveAppealHandlerTest.class);
        suite.addTestSuite(RoleResolutionExceptionTest.class);
        suite.addTestSuite(SetScorecardStatusHandlerTest.class);
        suite.addTestSuite(SetTimelineNotificationHandlerTest.class);
        suite.addTestSuite(BugResolutionTestCase.class);
        return suite;
    }

}
