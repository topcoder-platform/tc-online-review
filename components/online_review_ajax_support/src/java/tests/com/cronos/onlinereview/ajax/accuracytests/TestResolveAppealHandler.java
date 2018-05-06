/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.handlers.ResolveAppealHandler;

import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.scorecalculator.CalculationManager;
import com.topcoder.management.scorecard.ScorecardManager;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


/**
 * Tests for ResolveAppealHandler class.
 *
 * @author assistant
 * @version 1.0
 */
public class TestResolveAppealHandler extends TestCase {
    /** ResolveAppealHandler instance used for test. */
    private ResolveAppealHandler handler = null;

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
        TestHelper.loadFile("test_files/scorecalculator.xml");
        handler = new ResolveAppealHandler();

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
     * Tests ResolveAppealHandler() method with accuracy state.
     *
     * @throws Exception to JUNit.
     */
    public void testResolveAppealHandlerAccuracy() throws Exception {
        assertNotNull(handler);
        assertTrue(TestHelper.getVariable(ResolveAppealHandler.class,
                "calculationManager", handler) instanceof CalculationManager);
        assertTrue(TestHelper.getVariable(ResolveAppealHandler.class,
                "scorecardManager", handler) instanceof ScorecardManager);

        assertEquals(2,
            ((CommentType) TestHelper.getVariable(ResolveAppealHandler.class,
                "appealResponseCommentType", handler)).getId());

        assertTrue(TestHelper.getVariable(ResolveAppealHandler.class, "reviewPhaseTypeId", handler)
                             .equals(new Long(999990)));
        assertTrue(TestHelper.getVariable(ResolveAppealHandler.class, "appealsResponsePhaseTypeId",
                handler).equals(new Long(999992)));
        assertTrue(TestHelper.getVariable(ResolveAppealHandler.class, "openPhaseStatusId", handler)
                             .equals(new Long(999990)));
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the review id is
     * invalid.
     */
    public void testServiceAccuracy1() {
        params.put("ReviewId", "invalid");
        params.put("ItemId", "999991");
        params.put("Status", "Failed");

        AjaxRequest request = new AjaxRequest("ResolveAppeal", params);

        AjaxResponse response = handler.service(request, new Long(777770));

        assertEquals("service fails.", "Invalid parameter error", response.getStatus());
        assertEquals("service fails.", "ResolveAppeal", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the item id is
     * invalid.
     */
    public void testServiceAccuracy2() {
        params.put("ReviewId", "999990");
        params.put("ItemId", "invalid");
        params.put("Status", "Failed");

        AjaxRequest request = new AjaxRequest("ResolveAppeal", params);

        AjaxResponse response = handler.service(request, new Long(777770));

        assertEquals("service fails.", "Invalid parameter error", response.getStatus());
        assertEquals("service fails.", "ResolveAppeal", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the Status is
     * invalid.
     */
    public void testServiceAccuracy3() {
        params.put("ReviewId", "999990");
        params.put("ItemId", "999991");
        params.put("Status", "invalid");

        AjaxRequest request = new AjaxRequest("ResolveAppeal", params);

        AjaxResponse response = handler.service(request, new Long(777770));

        assertEquals("service fails.", "Invalid parameter error", response.getStatus());
        assertEquals("service fails.", "ResolveAppeal", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the user id is
     * null.
     */
    public void testServiceAccuracy4() {
        params.put("ReviewId", "999990");
        params.put("ItemId", "999991");
        params.put("Status", "Failed");
        params.put("Text", "Appeal Response");

        AjaxRequest request = new AjaxRequest("ResolveAppeal", params);

        AjaxResponse response = handler.service(request, null);

        assertEquals("service fails.", "Login error", response.getStatus());
        assertEquals("service fails.", "ResolveAppeal", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the user id is
     * null.
     */
    public void testServiceAccuracy5() {
//        params.put("ReviewId", "999990");
//        params.put("ItemId", "999991");
//        params.put("Status", "Failed");
//
//        AjaxRequest request = new AjaxRequest("ResolveAppeal", params);
//
//        AjaxResponse response = handler.service(request, new Long(777779));
//
//        assertEquals("service fails.", "Role error", response.getStatus());
//        assertEquals("service fails.", "ResolveAppeal", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state.
     */
    public void testServiceAccuracy6() {
        params.put("ReviewId", "999990");
        params.put("ItemId", "999991");
        params.put("Status", "Failed");
        params.put("Text", "Appeal Response");

        AjaxRequest request = new AjaxRequest("ResolveAppeal", params);

        AjaxResponse response = handler.service(request, new Long(777771));

        assertEquals("service fails.", "Role error", response.getStatus());
    }
}
