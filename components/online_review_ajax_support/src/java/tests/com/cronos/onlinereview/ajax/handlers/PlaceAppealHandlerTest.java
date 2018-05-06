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
import com.topcoder.management.deliverable.MockUploadManager;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.util.config.ConfigManager;

/**
 * Test the class <code>PlaceAppealHandler</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class PlaceAppealHandlerTest extends TestCase {

    /**
     * Represents the handler to test.
     */
    private PlaceAppealHandler handler;

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

        handler = new PlaceAppealHandler();
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
     * Test method for {@link com.cronos.onlinereview.ajax.handlers.PlaceAppealHandler#PlaceAppealHandler()}.
     * @throws Exception to JUnit
     */
    public void testPlaceAppealHandlerAccuracy() throws Exception {
        assertNotNull("The constructor doesn't work.", handler);

        // verify the upload manager
        assertTrue("The manager should be of type MockUploadManager.",
                TestHelper.getPrivateFieldValue(PlaceAppealHandler.class, "uploadManager", handler)
                instanceof MockUploadManager);

        // verify the ids
        assertEquals("The reviewPhaseTypeId is not right.", "1",
                TestHelper.getPrivateFieldValue(PlaceAppealHandler.class, "reviewPhaseTypeId", handler).toString());
        assertEquals("The appealsPhaseTypeId is not right.", "2",
                TestHelper.getPrivateFieldValue(PlaceAppealHandler.class, "appealsPhaseTypeId", handler).toString());
        assertEquals("The openPhaseStatusId is not right.", "1",
                TestHelper.getPrivateFieldValue(PlaceAppealHandler.class, "openPhaseStatusId", handler).toString());

        // verify comment type
        CommentType type = (CommentType) TestHelper.getPrivateFieldValue(PlaceAppealHandler.class,
                "appealCommentType", handler);
        assertEquals("The comment type is not right.", "Appeal", type.getName());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     */
    public void testServiceAccuracy1() {
        // prepare the ajax request
        Map map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "2");
        map.put("Text", "appeal text");
        map.put("TextLength", "" + ((String)map.get("Text")).length());        

        AjaxRequest request = new AjaxRequest("PlaceAppeal", map);

        // do the serve
        AjaxResponse response = handler.service(request, new Long(2));

        // verify the result
        assertEquals("The status should be success.", "Success", response.getStatus());
        assertEquals("The type should be PlaceAppeal.", "PlaceAppeal", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the request is not ok for the user id is null.
     * We should get the response with "login error".
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy2() throws Exception {

        // prepare the parameters
        Map parameters = new HashMap();
        parameters.put("ReviewId", "1");
        parameters.put("ItemId", "1");
        parameters.put("Text", "appeal text");
        parameters.put("TextLength", "" + "" + ((String)parameters.get("Text")).length());

        // create the request
        AjaxRequest request = new AjaxRequest("PlaceAppeal", parameters);

        // serve the request
        AjaxResponse response = handler.service(request, null);

        // verify the response
        assertEquals("The status should be Login error.", "Login error", response.getStatus());
        assertEquals("The type should be PlaceAppeal.", "PlaceAppeal", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the request is not ok for there is no such template name.
     * We should get the response with "Invalid template name error".
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy3() throws Exception {

        // prepare the parameters
        Map parameters = new HashMap();
        parameters.put("ReviewId", "abc");
        parameters.put("ItemId", "1");
        parameters.put("Text", "appeal text");
        parameters.put("TextLength", "" + ((String)parameters.get("Text")).length());

        // create the request
        AjaxRequest request = new AjaxRequest("PlaceAppeal", parameters);

        // serve the request
        AjaxResponse response = handler.service(request, new Long(1));

        // verify the response
        assertEquals("The status should be Invalid parameter error error.",
                "Invalid parameter error", response.getStatus());
        assertEquals("The type should be PlaceAppeal.", "PlaceAppeal", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the request is not ok for there is no such template name.
     * We should get the response with "Invalid template name error".
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy4() throws Exception {

        // prepare the parameters
        Map parameters = new HashMap();
        parameters.put("ReviewId", "1");
        parameters.put("ItemId", "abc");
        parameters.put("Text", "appeal text");
        parameters.put("TextLength", "" + ((String)parameters.get("Text")).length());

        // create the request
        AjaxRequest request = new AjaxRequest("PlaceAppeal", parameters);

        // serve the request
        AjaxResponse response = handler.service(request, new Long(1));

        // verify the response
        assertEquals("The status should be Invalid parameter error error.",
                "Invalid parameter error", response.getStatus());
        assertEquals("The type should be PlaceAppeal.", "PlaceAppeal", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the user doesn't has the role "Submitter".
     * We should get the response with "Role error".
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy5() throws Exception {

        // prepare the parameters
        Map parameters = new HashMap();
        parameters.put("ReviewId", "1");
        parameters.put("ItemId", "1");
        parameters.put("Text", "appeal text");
        parameters.put("TextLength", "" + ((String)parameters.get("Text")).length());

        // create the request
        AjaxRequest request = new AjaxRequest("PlaceAppeal", parameters);

        // serve the request
        AjaxResponse response = handler.service(request, new Long(3));

        // verify the response
        assertEquals("The status should be Role error.",
                "Role error", response.getStatus());
        assertEquals("The type should be PlaceAppeal.", "PlaceAppeal", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the review can't be found.
     * We should get the response with "Invalid review error".
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy6() throws Exception {

        // prepare the parameters
        Map parameters = new HashMap();
        parameters.put("ReviewId", "11");
        parameters.put("ItemId", "1");
        parameters.put("Text", "appeal text");
        parameters.put("TextLength", "" + ((String)parameters.get("Text")).length());

        // create the request
        AjaxRequest request = new AjaxRequest("PlaceAppeal", parameters);

        // serve the request
        AjaxResponse response = handler.service(request, new Long(1));

        // verify the response
        assertEquals("The status should be Invalid review error.",
                "Invalid review error", response.getStatus());
        assertEquals("The type should be PlaceAppeal.", "PlaceAppeal", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the item can't be found.
     * We should get the response with "Invalid item error".
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy7() throws Exception {

        // prepare the parameters
        Map parameters = new HashMap();
        parameters.put("ReviewId", "1");
        parameters.put("ItemId", "3");
        parameters.put("Text", "appeal text");
        parameters.put("TextLength", "" + ((String)parameters.get("Text")).length());

        // create the request
        AjaxRequest request = new AjaxRequest("PlaceAppeal", parameters);

        // serve the request
        AjaxResponse response = handler.service(request, new Long(2));

        // verify the response
        assertEquals("The status should be Invalid item error.",
                "Invalid item error", response.getStatus());
        assertEquals("The type should be PlaceAppeal.", "PlaceAppeal", response.getType());
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
