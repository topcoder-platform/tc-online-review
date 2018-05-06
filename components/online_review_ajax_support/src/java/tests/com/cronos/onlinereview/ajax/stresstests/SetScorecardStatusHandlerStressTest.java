/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.stresstests;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.handlers.SetScorecardStatusHandler;


/**
 * Stress test for class <code>SetScorecardStatusHandler</code>.
 *
 * @author PE
 * @version 1.0
 */
public class SetScorecardStatusHandlerStressTest extends TestCase {
    /** Represents the handler to test. */
    private SetScorecardStatusHandler handler;

    /**
     * Set up the environment.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        StressTestHelper.addConfig();
        handler = new SetScorecardStatusHandler();
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
     * Stress Test for the functionality of class <code>SetScorecardStatusHandler</code>.
     */
    public void testSetScorecardStatusHandler() {
        // prepare the parameters
        Map parameters = new HashMap();
        parameters.put("ScorecardId", "1");
        parameters.put("Status", "Active");

        // create the request
        AjaxRequest request = new AjaxRequest("SetScorecardStatus", parameters);

        StressTestHelper.start();

        AjaxResponse response = null;

        for (int i = 0; i < StressTestHelper.TIMES; i++) {
            response = handler.service(request, new Long(1));
        }

        StressTestHelper.printResult("testSetScorecardStatusHandler");

        // verify the response
        assertEquals("The status should be success.", "Success", response.getStatus());
        assertEquals("The type should be SetScorecardStatus.", "SetScorecardStatus", response.getType());
    }
}
