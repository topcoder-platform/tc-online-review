/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.handlers.SetScorecardStatusHandler;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


/**
 * Tests for SetScorecardStatusHandler class.
 *
 * @author assistant
 * @version 1.0
 */
public class TestSetScorecardStatusHandler extends TestCase {
    /** SetScorecardStatusHandler instance used for test. */
    private SetScorecardStatusHandler handler = null;

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
        handler = new SetScorecardStatusHandler();
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
     * Tests SetScorecardStatusHandler() method with accuracy state.
     */
    public void testSetScorecardStatusHandlerAccuracy() {
        assertNotNull("creating SetScorecardStatusHandler fails", handler);
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state.  ScorecardId can
     * not be parsed.
     */
    public void testServiceAccuracy1() {
        params.put("ScorecardId", "invalid");
        params.put("Status", "Active");

        AjaxRequest request = new AjaxRequest("Status Request", params);
        AjaxResponse response = handler.service(request, new Long(999999));

        assertEquals("service fails.", "Invalid parameter error", response.getStatus());
        assertEquals("service fails.", "Status Request", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. Status is not
     * Active or InActive.
     */
    public void testServiceAccuracy2() {
        params.put("ScorecardId", "999990");
        params.put("Status", "Non-Active");

        AjaxRequest request = new AjaxRequest("Status Request", params);
        AjaxResponse response = handler.service(request, new Long(999990));

        assertEquals("service fails.", "Invalid parameter error", response.getStatus());
        assertEquals("service fails.", "Status Request", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state.
     */
    public void testServiceAccuracy3() {
//        params.put("ScorecardId", "-1");
//        params.put("Status", "Active");
//
//        AjaxRequest request = new AjaxRequest("Status Request", params);
//        AjaxResponse response = handler.service(request, new Long(999990));
//
//        assertEquals("service fails.", "Invalid scorecard error", response.getStatus());
//        assertEquals("service fails.", "Status Request", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state.
     */
    public void testServiceAccuracy4() {
//        params.put("ScorecardId", "999990");
//        params.put("Status", "Active");
//
//        AjaxRequest request = new AjaxRequest("Status Request", params);
//        AjaxResponse response = handler.service(request, new Long(999999));
//
//        assertEquals("service fails.", "Invalid scorecard error", response.getStatus());
//        assertEquals("service fails.", "Status Request", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the user id is
     * null.
     */
    public void testServiceAccuracy5() {
        params.put("ScorecardId", "999990");
        params.put("Status", "Active");

        AjaxRequest request = new AjaxRequest("Status Request", params);
        AjaxResponse response = handler.service(request, null);

        assertEquals("service fails.", "Login error", response.getStatus());
        assertEquals("service fails.", "Status Request", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state.
     */
    public void testServiceAccuracy6() {
//        params.put("ScorecardId", "999990");
//        params.put("Status", "Active");
//
//        AjaxRequest request = new AjaxRequest("Status Request", params);
//        AjaxResponse response = handler.service(request, new Long(999990));
//
//        assertEquals("service fails.", "Invalid scorecard error", response.getStatus());
//        assertEquals("service fails.", "Status Request", response.getType());
    }
}
