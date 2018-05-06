/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.stresstests;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.handlers.SetTimelineNotificationHandler;


/**
 * Stress test for class <code>SetTimelineNotificationHandler</code>.
 *
 * @author PE
 * @version 1.0
 */
public class SetTimelineNotificationHandlerStressTest extends TestCase {
    /** Represents the handler to test. */
    private SetTimelineNotificationHandler handler;

    /**
     * Set up the environment.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        StressTestHelper.addConfig();
        handler = new SetTimelineNotificationHandler();
    }

    /**
     * Clean up the environment.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        StressTestHelper.clearConfig();
    }

    /**
     * Stress Test for the functionality of class <code>SetTimelineNotificationHandler</code>.
     */
    public void testSetTimelineNotificationHandler() {
        // prepare the parameters
        Map parameters = new HashMap();
        parameters.put("ProjectId", "1");
        parameters.put("Status", "On");

        // create the request
        AjaxRequest request = new AjaxRequest("SetTimelineNotification", parameters);

        StressTestHelper.start();

        AjaxResponse response = null;

        for (int i = 0; i < StressTestHelper.TIMES; i++) {
            response = handler.service(request, new Long(1));
        }

        StressTestHelper.printResult("testSetTimelineNotificationHandler");

        // verify the response
        assertEquals("The status should be success.", "Success", response.getStatus());
        assertEquals("The type should be SetTimelineNotification.", "SetTimelineNotification", response.getType());
    }
}
