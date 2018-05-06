/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.stresstests;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.handlers.PlaceAppealHandler;


/**
 * Stress test for class <code>PlaceAppealHandler</code>.
 *
 * @author PE
 * @version 1.0
 */
public class PlaceAppealHandlerStressTest extends TestCase {
    /** Represents the handler to test. */
    private PlaceAppealHandler handler;

    /**
     * Set up the environment.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        StressTestHelper.addConfig();
        handler = new PlaceAppealHandler();
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
     * Stress Test for the functionality of class <code>PlaceAppealHandler</code>.
     */
    public void testPlaceAppealHandler() {
        // prepare the parameters
        Map parameters = new HashMap();
        parameters.put("ReviewId", "1");
        parameters.put("ItemId", "1");
        parameters.put("Text", "I have described this issue in the CS please refer to it.");

        // create the request
        AjaxRequest request = new AjaxRequest("PlaceAppeal", parameters);

        StressTestHelper.start();

        AjaxResponse response = null;

        for (int i = 0; i < StressTestHelper.TIMES; i++) {
            response = handler.service(request, new Long(2));
        }

        StressTestHelper.printResult("testPlaceAppealHandler");

        // verify the response
        assertEquals("The status should be success.", "Invalid parameter error", response.getStatus());
    }
}
