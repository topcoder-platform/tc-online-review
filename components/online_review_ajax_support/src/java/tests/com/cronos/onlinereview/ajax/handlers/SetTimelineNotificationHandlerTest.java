/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.TestHelper;
import com.topcoder.management.project.MockProjectManager;
import com.topcoder.util.config.ConfigManager;

/**
 * Test the class <code>SetTimelineNotificationHandler</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class SetTimelineNotificationHandlerTest extends TestCase {

    /**
     * Represents the handler to test.
     */
    private SetTimelineNotificationHandler handler;

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

        handler = new SetTimelineNotificationHandler();
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
     * Test method for SetTimelineNotificationHandler().
     * @throws Exception to JUnit
     */
    public void testSetTimelineNotificationHandler() throws Exception {
        // verify the manager
        assertTrue("The project manager is not right.",
                TestHelper.getPrivateFieldValue(SetTimelineNotificationHandler.class, "projectManager", handler)
                instanceof MockProjectManager);

        // verify the id
        assertEquals("The timelineNotificationId is not right.", "1",
                TestHelper.getPrivateFieldValue(SetTimelineNotificationHandler.class,
                        "timelineNotificationId", handler).toString());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     */
    public void testServiceAccuracy1() {
        Map parameters = new HashMap();
        parameters.put("ProjectId", "1");
        parameters.put("Status", "On");

        AjaxRequest request = new AjaxRequest("SetTimelineNotification", parameters);
        AjaxResponse response = handler.service(request, new Long(1));

        assertEquals("The status should be success.", "Success", response.getStatus());
        assertEquals("The type should be SetTimelineNotification.", "SetTimelineNotification", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the id is not a long.
     * we should get Invalid parameter error.
     */
    public void testServiceAccuracy2() {
        Map parameters = new HashMap();
        parameters.put("ProjectId", "abc");
        parameters.put("Status", "On");

        AjaxRequest request = new AjaxRequest("SetTimelineNotification", parameters);
        AjaxResponse response = handler.service(request, new Long(1));

        assertEquals("The status should be Invalid parameter error.", "Invalid parameter error", response.getStatus());
        assertEquals("The type should be SetTimelineNotification.", "SetTimelineNotification", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the status is not valid.
     * we should get Invalid parameter error.
     */
    public void testServiceAccuracy3() {
        Map parameters = new HashMap();
        parameters.put("ProjectId", "1");
        parameters.put("Status", "abc");

        AjaxRequest request = new AjaxRequest("SetTimelineNotification", parameters);
        AjaxResponse response = handler.service(request, new Long(1));

        assertEquals("The status should be Invalid parameter error.", "Invalid parameter error", response.getStatus());
        assertEquals("The type should be SetTimelineNotification.", "SetTimelineNotification", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the user id is null.
     * we should get Login error.
     */
    public void testServiceAccuracy4() {
        Map parameters = new HashMap();
        parameters.put("ProjectId", "1");
        parameters.put("Status", "On");

        AjaxRequest request = new AjaxRequest("SetTimelineNotification", parameters);
        AjaxResponse response = handler.service(request, null);

        assertEquals("The status should be Login error.", "Login error", response.getStatus());
        assertEquals("The type should be SetTimelineNotification.", "SetTimelineNotification", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the user don't have global access.
     * we should get success since the project is public.
     */
    public void testServiceAccuracy5() {
        Map parameters = new HashMap();
        parameters.put("ProjectId", "1");
        parameters.put("Status", "On");

        AjaxRequest request = new AjaxRequest("SetTimelineNotification", parameters);
        AjaxResponse response = handler.service(request, new Long(2));

        assertEquals("The status should be Success.", "Success", response.getStatus());
        assertEquals("The type should be SetTimelineNotification.", "SetTimelineNotification", response.getType());
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
