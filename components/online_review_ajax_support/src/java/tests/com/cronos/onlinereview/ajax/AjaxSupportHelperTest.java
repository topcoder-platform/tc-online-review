/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

import com.topcoder.util.config.ConfigManager;
import org.apache.cactus.JspTestCase;
import org.apache.cactus.WebResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Test the class <code>AjaxSupportHelper</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class AjaxSupportHelperTest extends JspTestCase {

    /**
     * Set up the environment.
     * @throws Exception to JUnit
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

//    /**
//     * Prepare environment for responseAndLogError(String, String, String, HttpServletResponse).
//     * @param request the request
//     */
//    public void beginResponseAndLogError(WebRequest request) {
//        // do nothing
//    }
//
//    /**
//     * Test method for responseAndLogError(String, String, String, HttpServletResponse).
//     * @throws Exception to JUnit
//     */
//    public void testResponseAndLogError() throws Exception {
//        AjaxSupportHelper.responseAndLogError("test", "success", "a test", response);
//    }
//
//
//    /**
//     * Verify result for test method for responseAndLogError(String, String, String, HttpServletResponse).
//     * @param response the response
//     * @throws Exception to JUnit
//     */
//    public void endResponseAndLogError(WebResponse response) throws Exception {
//        String text = response.getText();
//
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document doc = builder.parse(new ByteArrayInputStream(text.getBytes()));
//
//        // verify the result
//        Element root = doc.getDocumentElement();
//        assertEquals("The name should be response.", "response", root.getNodeName());
//        assertEquals("The type should be test.", "test", root.getAttribute("type"));
//
//        NodeList children = root.getElementsByTagName("result");
//        assertEquals("The length should be 1.", 1, children.getLength());
//
//        Node result = children.item(0);
//        assertEquals("The name should be result.", "result", result.getNodeName());
//        assertEquals("The status should be success.", "success", ((Element) result).getAttribute("status"));
//    }

    /**
     * Test method for responseAndLogError(String, String, String, HttpServletResponse).
     * In this case, the type is null.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testResponseAndLogErrorNullType() throws Exception {
        try {
            AjaxSupportHelper.responseAndLogError(null, "success", "a test", response);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for responseAndLogError(String, String, String, HttpServletResponse).
     * In this case, the type is empty.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testResponseAndLogErrorEmptyType() throws Exception {
        try {
            AjaxSupportHelper.responseAndLogError(" ", "success", "a test", response);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for responseAndLogError(String, String, String, HttpServletResponse).
     * In this case, the status is null.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testResponseAndLogErrorNullStatus() throws Exception {
        try {
            AjaxSupportHelper.responseAndLogError("test", null, "a test", response);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for responseAndLogError(String, String, String, HttpServletResponse).
     * In this case, the status is empty.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testResponseAndLogErrorEmptyStatus() throws Exception {
        try {
            AjaxSupportHelper.responseAndLogError("test", " ", "a test", response);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for createAndLogError(java.lang.String, java.lang.String, java.lang.String, java.lang.Object).
     */
    public void testCreateAndLogError() {
        AjaxResponse response = AjaxSupportHelper.createAndLogError("test", "success", "a test", null);
        assertEquals("The type is not right.", "test", response.getType());
        assertEquals("The status is not right.", "success", response.getStatus());
    }

    /**
     * Test method for CreateAndLogError(String, String, String, HttpServletResponse).
     * In this case, the type is null.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testCreateAndLogErrorNullType() throws Exception {
        try {
            AjaxSupportHelper.createAndLogError(null, "success", "a test", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for CreateAndLogError(String, String, String, HttpServletResponse).
     * In this case, the type is empty.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testCreateAndLogErrorEmptyType() throws Exception {
        try {
            AjaxSupportHelper.createAndLogError(" ", "success", "a test", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for CreateAndLogError(String, String, String, HttpServletResponse).
     * In this case, the status is null.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testCreateAndLogErrorNullStatus() throws Exception {
        try {
            AjaxSupportHelper.createAndLogError("test", null, "a test", response);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for CreateAndLogError(String, String, String, HttpServletResponse).
     * In this case, the status is empty.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testCreateAndLogErrorEmptyStatus() throws Exception {
        try {
            AjaxSupportHelper.createAndLogError("test", " ", "a test", response);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for createAndLogSucceess(String, String, String, Object, Object).
     */
    public void testCreateAndLogSucceess() {
        AjaxResponse response = AjaxSupportHelper.createAndLogSucceess("test", "success", "a test", null, null);
        assertEquals("The type is not right.", "test", response.getType());
        assertEquals("The status is not right.", "success", response.getStatus());
    }

    /**
     * Test method for createAndLogSucceess(String, String, String, HttpServletResponse).
     * In this case, the type is null.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testcreateAndLogSucceessNullType() throws Exception {
        try {
            AjaxSupportHelper.createAndLogSucceess(null, "success", "a test", null, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for createAndLogSucceess(String, String, String, HttpServletResponse).
     * In this case, the type is empty.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testcreateAndLogSucceessEmptyType() throws Exception {
        try {
            AjaxSupportHelper.createAndLogSucceess(" ", "success", "a test", null, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for createAndLogSucceess(String, String, String, HttpServletResponse).
     * In this case, the status is null.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testcreateAndLogSucceessNullStatus() throws Exception {
        try {
            AjaxSupportHelper.createAndLogSucceess("test", null, "a test", response, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for createAndLogSucceess(String, String, String, HttpServletResponse).
     * In this case, the status is empty.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testcreateAndLogSucceessStatus() throws Exception {
        try {
            AjaxSupportHelper.createAndLogSucceess("test", " ", "a test", response, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for doResponse(javax.servlet.http.HttpServletResponse, com.cronos.onlinereview.ajax.AjaxResponse).
     * @throws Exception to JUnit
     */
    public void testDoResponse() throws Exception {
        AjaxResponse ajax = AjaxSupportHelper.createAndLogError("test", "success", "a test", null);
        AjaxSupportHelper.doResponse(response, ajax);
    }

    /**
     * Verify result for testing method for
     * doResponse(javax.servlet.http.HttpServletResponse, com.cronos.onlinereview.ajax.AjaxResponse).
     *
     * @param response the web response
     * @throws Exception to JUnit
     */
    public void endDoResponse(WebResponse response) throws Exception {
        String text = response.getText();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(text.getBytes()));

        // verify the result
        Element root = doc.getDocumentElement();
        assertEquals("The name should be response.", "response", root.getNodeName());
        assertEquals("The type should be test.", "test", root.getAttribute("type"));

        NodeList children = root.getElementsByTagName("result");
        assertEquals("The length should be 1.", 1, children.getLength());

        Node result = children.item(0);
        assertEquals("The name should be result.", "result", result.getNodeName());
        assertEquals("The status should be success.", "success", ((Element) result).getAttribute("status"));
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxSupportHelper#dateToString(java.util.Date)}.
     * @throws Exception to JUnit
     */
    public void testDateToString() throws Exception {
        Date date = new Date();
        date.setSeconds(0);
        String str = AjaxSupportHelper.dateToString(date);

        // creating a simple date formatter
        SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy h:mm a");

        // to parse a date from a string
        Date newdate = formatter.parse(str);

        assertEquals("The date is not right.", date.toLocaleString(), newdate.toLocaleString());
    }

}
