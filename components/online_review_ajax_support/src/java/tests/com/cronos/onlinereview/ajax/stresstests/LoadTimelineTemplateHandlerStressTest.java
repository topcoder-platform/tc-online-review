/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.stresstests;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.handlers.LoadTimelineTemplateHandler;


/**
 * Stress test for class <code>LoadTimelineTemplateHandler</code>.
 *
 * @author PE
 * @version 1.0
 */
public class LoadTimelineTemplateHandlerStressTest extends TestCase {
    /** Represents the handler to test. */
    private LoadTimelineTemplateHandler handler;

    /**
     * Set up the environment.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        StressTestHelper.addConfig();
        handler = new LoadTimelineTemplateHandler();
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
     * Stress Test for the functionality of class <code>LoadTimelineTemplateHandler</code>.
     */
    public void testLoadTimelineTemplateHandler() {
        // prepare the parameters
        Map parameters = new HashMap();
        parameters.put("TemplateName", "Review");
        parameters.put("StartDate", StressTestHelper.dateToString(new Date()));

        // create the request
        AjaxRequest request = new AjaxRequest("LoadTimelineTemplate", parameters);

        StressTestHelper.start();

        AjaxResponse response = null;

        for (int i = 0; i < StressTestHelper.TIMES; i++) {
            response = handler.service(request, new Long(1));
        }

        StressTestHelper.printResult("testLoadTimelineTemplateHandler");

        // verify the response
        assertEquals("The status should be success.", "Success", response.getStatus());
        assertEquals("The type should be LoadTimelineTemplate.", "LoadTimelineTemplate", response.getType());
    }
}
