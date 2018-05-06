/**
 *
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
 package com.cronos.onlinereview.ajax.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.cronos.onlinereview.ajax.failuretests.handlers.CommonHandlerFailureTest;
import com.cronos.onlinereview.ajax.failuretests.handlers.LoadTimelineTemplateHandlerFailureTest;
import com.cronos.onlinereview.ajax.failuretests.handlers.PlaceAppealHandlerFailureTest;
import com.cronos.onlinereview.ajax.failuretests.handlers.ReviewCommonHandlerFailureTest;
import com.cronos.onlinereview.ajax.failuretests.handlers.SetScorecardStatusHandlerFailureTest;
import com.cronos.onlinereview.ajax.failuretests.handlers.SetTimelineNotificationHandlerFailureTest;
import com.cronos.onlinereview.ajax.failuretests.handlers.ResolveAppealHandlerFailureTest;

/**
 * <p>This test case aggregates all Failure test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class FailureTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(AjaxRequestFailureTest.suite());
        suite.addTest(AjaxResponseFailureTest.suite());
        suite.addTest(AjaxSupportServletFailureTest.suite());
        suite.addTest(CommonHandlerFailureTest.suite());
        suite.addTest(LoadTimelineTemplateHandlerFailureTest.suite());
        suite.addTest(PlaceAppealHandlerFailureTest.suite());
        suite.addTest(ResolveAppealHandlerFailureTest.suite());
        suite.addTest(ReviewCommonHandlerFailureTest.suite());
        suite.addTest(SetScorecardStatusHandlerFailureTest.suite());
        suite.addTest(SetTimelineNotificationHandlerFailureTest.suite());

        return suite;
    }

}
