/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.TestHelper;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.scorecalculator.CalculationManager;
import com.topcoder.management.scorecard.MockScorecardManager;
import com.topcoder.util.config.ConfigManager;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Test the class <code>ResolveAppealHandler</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class ResolveAppealHandlerTest extends TestCase {

    /**
     * Represents the handler to test.
     */
    private ResolveAppealHandler handler;

    /**
     * Set up the environment.
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.tearDown();
        ConfigManager cm = ConfigManager.getInstance();
        cm.add("default.xml");
        cm.add("objectfactory.xml");
        cm.add("scorecalculator.xml");

        handler = new ResolveAppealHandler();
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
     * Test method for {@link com.cronos.onlinereview.ajax.handlers.ResolveAppealHandler#ResolveAppealHandler()}.
     * @throws Exception to JUnit
     */
    public void testResolveAppealHandler() throws Exception {
        // verify the managers
        assertTrue("The calculation manager is not right.",
                TestHelper.getPrivateFieldValue(ResolveAppealHandler.class, "calculationManager", handler)
                instanceof CalculationManager);

        assertTrue("The scorecard manager is not right.",
                TestHelper.getPrivateFieldValue(ResolveAppealHandler.class, "scorecardManager", handler)
                instanceof MockScorecardManager);

        // verify the comment type
        CommentType type = (CommentType) TestHelper.getPrivateFieldValue(ResolveAppealHandler.class,
                "appealResponseCommentType", handler);
        assertEquals("The type is not right.", "Appeal Response", type.getName());

        // verify the ids
        assertEquals("The reviewPhaseTypeId is not right.", "1",
                TestHelper.getPrivateFieldValue(ResolveAppealHandler.class, "reviewPhaseTypeId", handler).toString());
        assertEquals("The appealsPhaseTypeId is not right.", "3",
                TestHelper.getPrivateFieldValue(ResolveAppealHandler.class,
                        "appealsResponsePhaseTypeId", handler).toString());
        assertEquals("The openPhaseStatusId is not right.", "1",
                TestHelper.getPrivateFieldValue(ResolveAppealHandler.class, "openPhaseStatusId", handler).toString());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, all the conditions are met, so we should get a repsonse with status Success.
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy1() throws Exception {
        // prepare the ajax request
        Map map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "1");
        map.put("Status", "Succeeded");
        map.put("Text", "Appeal Response");

        AjaxRequest request = new AjaxRequest("ResolveAppeal", map);

        // do the serve
        AjaxResponse response = handler.service(request, new Long(3));

        // verify the result
        // the reseult should be score card error
        // since we used mock data so there is nothing in the database
        assertEquals("The status should be success.", "Success", response.getStatus());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the review id is not a long.
     * We will get Invalid parameter error
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy2() throws Exception {
        // prepare the ajax request
        Map map = new HashMap();
        map.put("ReviewId", "abc");
        map.put("ItemId", "1");
        map.put("Status", "Succeeded");

        AjaxRequest request = new AjaxRequest("ResolveAppeal", map);

        // do the serve
        AjaxResponse response = handler.service(request, new Long(3));

        // verify the result
        assertEquals("The status should be success.", "Invalid parameter error", response.getStatus());
        assertEquals("The type should be PlaceAppeal.", "ResolveAppeal", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the item id is not a long.
     * We will get Invalid parameter error
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy3() throws Exception {
        // prepare the ajax request
        Map map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "abc");
        map.put("Status", "Succeeded");

        AjaxRequest request = new AjaxRequest("ResolveAppeal", map);

        // do the serve
        AjaxResponse response = handler.service(request, new Long(3));

        // verify the result
        assertEquals("The status should be success.", "Invalid parameter error", response.getStatus());
        assertEquals("The type should be PlaceAppeal.", "ResolveAppeal", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the status is not valid.
     * We will get Invalid parameter error
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy4() throws Exception {
        // prepare the ajax request
        Map map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "1");
        map.put("Status", "not.valid");

        AjaxRequest request = new AjaxRequest("ResolveAppeal", map);

        // do the serve
        AjaxResponse response = handler.service(request, new Long(3));

        // verify the result
        assertEquals("The status should be success.", "Invalid parameter error", response.getStatus());
        assertEquals("The type should be PlaceAppeal.", "ResolveAppeal", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the user id is null.
     * We will get Login error
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy5() throws Exception {
        // prepare the ajax request
        Map map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "1");
        map.put("Status", "Succeeded");
        map.put("Text", "Appeal Response");

        AjaxRequest request = new AjaxRequest("ResolveAppeal", map);

        // do the serve
        AjaxResponse response = handler.service(request, null);

        // verify the result
        assertEquals("The status should be success.", "Login error", response.getStatus());
        assertEquals("The type should be PlaceAppeal.", "ResolveAppeal", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the review can't be found.
     * We will get Invalid review error
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy6() throws Exception {
        // prepare the ajax request
        Map map = new HashMap();
        map.put("ReviewId", "11");
        map.put("ItemId", "1");
        map.put("Status", "Succeeded");
        map.put("Text", "Appeal Response");

        AjaxRequest request = new AjaxRequest("ResolveAppeal", map);

        // do the serve
        AjaxResponse response = handler.service(request, new Long(3));

        // verify the result
        assertEquals("The status should be success.", "Invalid review error", response.getStatus());
        assertEquals("The type should be PlaceAppeal.", "ResolveAppeal", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the item can't be found.
     * We will get Invalid item error
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy7() throws Exception {
        // prepare the ajax request
        Map map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "11");
        map.put("Status", "Succeeded");
        map.put("Text", "Appeal Response");

        AjaxRequest request = new AjaxRequest("ResolveAppeal", map);

        // do the serve
        AjaxResponse response = handler.service(request, new Long(3));

        // verify the result
        assertEquals("The status should be success.", "Invalid item error", response.getStatus());
        assertEquals("The type should be PlaceAppeal.", "ResolveAppeal", response.getType());
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
