/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.stresstests;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.handlers.ResolveAppealHandler;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


/**
 * Stress test for class <code>ResolveAppealHandler</code>.
 *
 * @author PE
 * @version 1.0
 */
public class ResolveAppealHandlerStressTest extends TestCase {
    /** Represents the handler to test. */
    private ResolveAppealHandler handler;

    /**
     * Set up the environment.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        StressTestHelper.addConfig();
        handler = new ResolveAppealHandler();
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
     * Stress Test for the functionality of class <code>ResolveAppealHandler</code>.
     */
    public void testResolveAppealHandler() {
        // prepare the parameters
        Map parameters = new HashMap();
        parameters.put("ReviewId", "1");
        parameters.put("ItemId", "1");
        parameters.put("Status", "Succeeded");
        parameters.put("Answer", "4");
        parameters.put("Text", "Yes, I will raise the score.");

        // create the request
        AjaxRequest request = new AjaxRequest("ResolveAppeal", parameters);

        StressTestHelper.start();

        AjaxResponse response = null;

        for (int i = 0; i < StressTestHelper.TIMES; i++) {
            response = handler.service(request, new Long(3));
        }

        StressTestHelper.printResult("testResolveAppealHandler");

        // verify the response
        assertEquals("The status should be success.", "Success", response.getStatus());
    }
}
