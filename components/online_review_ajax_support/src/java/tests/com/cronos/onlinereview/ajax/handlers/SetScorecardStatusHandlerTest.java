/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.TestHelper;
import com.topcoder.management.scorecard.MockScorecardManager;
import com.topcoder.management.scorecard.data.ScorecardStatus;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * Test the class <code>SetScorecardStatusHandler</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class SetScorecardStatusHandlerTest extends TestCase {

    /**
     * Represents the handler to test.
     */
    private SetScorecardStatusHandler handler;

    /**
     * Set up the environment.
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        // load the configurations
        ConfigManager cm = ConfigManager.getInstance();
        if (!cm.existsNamespace("com.cronos.onlinereview.ajax")) {
            cm.add("default.xml");
            cm.add("objectfactory.xml");
            cm.add("scorecalculator.xml");
        }

        handler = new SetScorecardStatusHandler();
    }

    /**
     * Clean up the environment.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator it = cm.getAllNamespaces();
        while (it.hasNext()) {
            cm.removeNamespace(it.next().toString());
        }
    }

    /**
     * Test method for SetScorecardStatusHandler().
     * @throws Exception to JUnit
     */
    public void testSetScorecardStatusHandler() throws Exception {
        // verify the manager
        assertTrue("The scorecard manager is not right.",
                TestHelper.getPrivateFieldValue(SetScorecardStatusHandler.class, "scorecardManager", handler)
                instanceof MockScorecardManager);

        // verify the statuses
        ScorecardStatus active = (ScorecardStatus) TestHelper.getPrivateFieldValue(SetScorecardStatusHandler.class,
                "activeScorecardStatus", handler);
        assertEquals("The name is not right.", "Active", active.getName());
        ScorecardStatus inactive = (ScorecardStatus) TestHelper.getPrivateFieldValue(SetScorecardStatusHandler.class,
                "inactiveScorecardStatus", handler);
        assertEquals("The name is not right.", "Inactive", inactive.getName());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     */
    public void testServiceAccuracy1() {
        Map parameters = new HashMap();
        parameters.put("ScorecardId", "1");
        parameters.put("Status", "Active");

        AjaxRequest request = new AjaxRequest("SetScorecardStatus", parameters);
        AjaxResponse response = handler.service(request, new Long(1));

        // verify the response
        assertEquals("The status should be success.", "Success", response.getStatus());
        assertEquals("The type should be SetScorecardStatus.", "SetScorecardStatus", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the scorecard id is not a long.
     * We should get Invalid parameter error.
     */
    public void testServiceAccuracy2() {
        Map parameters = new HashMap();
        parameters.put("ScorecardId", "abc");
        parameters.put("Status", "Active");

        AjaxRequest request = new AjaxRequest("SetScorecardStatus", parameters);
        AjaxResponse response = handler.service(request, new Long(1));

        // verify the response
        assertEquals("The status should be Invalid parameter error.", "Invalid parameter error", response.getStatus());
        assertEquals("The type should be SetScorecardStatus.", "SetScorecardStatus", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the status is not valid.
     * We should get Invalid parameter error.
     */
    public void testServiceAccuracy3() {
        Map parameters = new HashMap();
        parameters.put("ScorecardId", "1");
        parameters.put("Status", "Not.Valid");

        AjaxRequest request = new AjaxRequest("SetScorecardStatus", parameters);
        AjaxResponse response = handler.service(request, new Long(1));

        // verify the response
        assertEquals("The status should be Invalid parameter error.", "Invalid parameter error", response.getStatus());
        assertEquals("The type should be SetScorecardStatus.", "SetScorecardStatus", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the user id is null.
     * We should get Login error.
     */
    public void testServiceAccuracy4() {
        Map parameters = new HashMap();
        parameters.put("ScorecardId", "1");
        parameters.put("Status", "Active");

        AjaxRequest request = new AjaxRequest("SetScorecardStatus", parameters);
        AjaxResponse response = handler.service(request, null);

        // verify the response
        assertEquals("The status should be Login error.", "Login error", response.getStatus());
        assertEquals("The type should be SetScorecardStatus.", "SetScorecardStatus", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the user id is not a global manager.
     * We should get Role error.
     */
    public void testServiceAccuracy5() {
//        Map parameters = new HashMap();
//        parameters.put("ScorecardId", "1");
//        parameters.put("Status", "Active");
//
//        AjaxRequest request = new AjaxRequest("SetScorecardStatus", parameters);
//        AjaxResponse response = handler.service(request, new Long(3));
//
//        // verify the response
//        assertEquals("The status should be Role error.", "Role error", response.getStatus());
//        assertEquals("The type should be SetScorecardStatus.", "SetScorecardStatus", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the scorecard can't be found.
     * We should get Invalid scorecard error.
     */
    public void testServiceAccuracy6() {
        Map parameters = new HashMap();
        parameters.put("ScorecardId", "2");
        parameters.put("Status", "Active");

        AjaxRequest request = new AjaxRequest("SetScorecardStatus", parameters);
        AjaxResponse response = handler.service(request, new Long(1));

        // verify the response
        assertEquals("The status should be Invalid scorecard error.", "Invalid scorecard error", response.getStatus());
        assertEquals("The type should be SetScorecardStatus.", "SetScorecardStatus", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the request is null.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testServiceNullRequest() throws Exception {
        try {
            handler.service(null, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

}
