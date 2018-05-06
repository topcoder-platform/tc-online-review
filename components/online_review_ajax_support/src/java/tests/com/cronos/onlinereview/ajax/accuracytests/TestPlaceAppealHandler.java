/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.handlers.PlaceAppealHandler;

import com.topcoder.management.review.data.CommentType;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


/**
 * Tests for PlaceAppealHandler class.
 *
 * @author assistant
 * @version 1.0
 */
public class TestPlaceAppealHandler extends TestCase {
    /** PlaceAppealHandler instance used for test. */
    private PlaceAppealHandler handler = null;

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
        handler = new PlaceAppealHandler();

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
     * Tests PlaceAppealHandler() method with accuracy state.
     *
     * @throws Exception to JUnit.
     */
    public void testPlaceAppealHandlerAccuracy() throws Exception {
        assertNotNull(handler);
        assertTrue(
                TestHelper.getVariable(PlaceAppealHandler.class,
                        "uploadManager", handler) instanceof MockUploadManager);
        assertEquals(1,
            ((CommentType) TestHelper.getVariable(PlaceAppealHandler.class, "appealCommentType",
                handler)).getId());

        assertTrue(TestHelper.getVariable(PlaceAppealHandler.class, "reviewPhaseTypeId", handler)
                             .equals(new Long(999990)));
        assertTrue(TestHelper.getVariable(PlaceAppealHandler.class, "appealsPhaseTypeId", handler)
                             .equals(new Long(999990)));
        assertTrue(TestHelper.getVariable(PlaceAppealHandler.class, "openPhaseStatusId", handler)
                             .equals(new Long(999990)));
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the review id is
     * invalid.
     */
    public void testServiceAccuracy1() {
        params.put("ReviewId", "invalid");
        params.put("ItemId", "888881");
        params.put("Text", "Appeal");

        AjaxRequest request = new AjaxRequest("PlaceAppeal", params);

        AjaxResponse response = handler.service(request, new Long(777770));

        assertEquals("service fails.", "Invalid parameter error", response.getStatus());
        assertEquals("service fails.", "PlaceAppeal", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the item id is
     * invalid.
     */
    public void testServiceAccuracy2() {
        params.put("ReviewId", "888880");
        params.put("ItemId", "invalid");
        params.put("Text", "Appeal");

        AjaxRequest request = new AjaxRequest("PlaceAppeal", params);

        AjaxResponse response = handler.service(request, new Long(777770));

        assertEquals("service fails.", "Invalid parameter error", response.getStatus());
        assertEquals("service fails.", "PlaceAppeal", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the user id is
     * null.
     */
    public void testServiceAccuracy3() {
        params.put("ReviewId", "888880");
        params.put("ItemId", "888881");
        params.put("Text", "Appeal");

        AjaxRequest request = new AjaxRequest("PlaceAppeal", params);

        AjaxResponse response = handler.service(request, null);

        assertEquals("service fails.", "Login error", response.getStatus());
        assertEquals("service fails.", "PlaceAppeal", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the review is
     * invalid.
     */
    public void testServiceAccuracy4() {
        params.put("ReviewId", "-1");
        params.put("ItemId", "888881");
        params.put("Text", "Appeal");

        AjaxRequest request = new AjaxRequest("PlaceAppeal", params);

        AjaxResponse response = handler.service(request, new Long(777770));

        assertEquals("service fails.", "Invalid parameter error", response.getStatus());
        assertEquals("service fails.", "PlaceAppeal", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. the review id is
     * invalid.
     */
    public void testServiceAccuracy5() {
        params.put("ReviewId", "888880");
        params.put("ItemId", "888881");
        params.put("Text", "Appeal");

        AjaxRequest request = new AjaxRequest("PlaceAppeal", params);

        AjaxResponse response = handler.service(request, new Long(777779));

        assertEquals("service fails.", "Invalid parameter error", response.getStatus());
        assertEquals("service fails.", "PlaceAppeal", response.getType());
    }
}
