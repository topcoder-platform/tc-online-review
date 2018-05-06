/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.handlers.SetTimelineNotificationHandler;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


/**
 * Tests for SetTimelineNotificationHandler class.
 *
 * @author assistant
 * @version 1.0
 */
public class TestSetTimelineNotificationHandler extends TestCase {
    /** SetTimelineNotificationHandler instance used for test. */
    private SetTimelineNotificationHandler handler = null;

    /** Map instance used for test. */
    private Map params = null;

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        TestHelper.clearNamespaces();
        TestHelper.loadFile("test_files/accuracy/accuracy.xml");
        handler = new SetTimelineNotificationHandler();
        params = new HashMap();
    }

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        TestHelper.clearNamespaces();
    }

    /**
     * Tests SetTimelineNotificationHandler() method with accuracy state.
     */
    public void testSetTimelineNotificationHandlerAccuracy() {
        assertNotNull("creating SetTimelineNotificationHandler fails", handler);
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state.
     */
    public void testServiceAccuracy1() {
        params.put("ProjectId", "999990");
        params.put("Status", "On");

        AjaxRequest request = new AjaxRequest("SetTimelineNotification", params);
        AjaxResponse response = handler.service(request, new Long(999990));

        assertEquals("service fails.", "Success", response.getStatus());
        assertEquals("service fails.", "SetTimelineNotification", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the project id
     * is not number.
     */
    public void testServiceAccuracy2() {
        params.put("ProjectId", "invalid");
        params.put("Status", "On");

        AjaxRequest request = new AjaxRequest("SetTimelineNotification", params);
        AjaxResponse response = handler.service(request, new Long(999990));

        assertEquals("service fails.", "Invalid parameter error", response.getStatus());
        assertEquals("service fails.", "SetTimelineNotification", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the status is
     * invalid.
     */
    public void testServiceAccuracy3() {
        params.put("ProjectId", "999990");
        params.put("Status", "Not-On-or-Off");

        AjaxRequest request = new AjaxRequest("SetTimelineNotification", params);
        AjaxResponse response = handler.service(request, new Long(999990));

        assertEquals("service fails.", "Invalid parameter error", response.getStatus());
        assertEquals("service fails.", "SetTimelineNotification", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the project id
     * is null.
     */
    public void testServiceAccuracy4() {
        params.put("ProjectId", "999990");
        params.put("Status", "On");

        AjaxRequest request = new AjaxRequest("SetTimelineNotification", params);
        AjaxResponse response = handler.service(request, null);

        assertEquals("service fails.", "Login error", response.getStatus());
        assertEquals("service fails.", "SetTimelineNotification", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the project id
     * is -1 so that the Project is null.
     */
    public void testServiceAccuracy5() {
        params.put("ProjectId", "-1");
        params.put("Status", "On");

        AjaxRequest request = new AjaxRequest("SetTimelineNotification", params);
        AjaxResponse response = handler.service(request, new Long(999990));

        assertEquals("service fails.", "Business error", response.getStatus());
        assertEquals("service fails.", "SetTimelineNotification", response.getType());
    }
}
