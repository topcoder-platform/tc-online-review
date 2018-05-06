/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.cactus.ServletTestCase;
import org.apache.cactus.WebRequest;
import org.apache.cactus.WebResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.topcoder.util.config.ConfigManager;

/**
 * Test the class <code>AjaxSupportServlet</code>.
 * The configuration files used in this case are :
 * test_files/default.xml, test_files/objectfactory.xml.
 *
 * @author assistant
 * @version 1.0
 */
public class AjaxSupportServletTest extends ServletTestCase {

    /**
     * Represents the servlet to test.
     */
    private AjaxSupportServlet servlet;

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

        servlet = new AjaxSupportServlet();
        request.setCharacterEncoding("iso-8859-1");
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
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxSupportServlet#destroy()}.
     * @throws Exception to JUnit
     */
    public void testDestroy() throws Exception {
        servlet.destroy();

        assertNull("The attribute name id should be null.", getPrivateFieldValue(AjaxSupportServlet.class,
                "userIdAttributeName", servlet));
        Map handlers = (Map) getPrivateFieldValue(AjaxSupportServlet.class, "handlers", servlet);
        assertTrue("The handlers should be empty.", handlers.isEmpty());
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxSupportServlet#AjaxSupportServlet()}.
     */
    public void testAjaxSupportServlet() {
        assertNotNull("The servlet should not be null.", servlet);
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxSupportServlet#init(javax.servlet.ServletConfig)}.
     * @throws Exception to JUnit
     */
    public void testInitAccuracy() throws Exception {
        servlet.init(config);

        // verify the user attribute id
        assertEquals("The user attribute id is not right.", "UserId", getPrivateFieldValue(AjaxSupportServlet.class,
                "userIdAttributeName", servlet));

        Map handlers = (Map) getPrivateFieldValue(AjaxSupportServlet.class, "handlers", servlet);

        // there must be 5 handlers
        assertEquals("There should be five handlers.", 5, handlers.size());

        // verify each
        assertNotNull("There should be SetScorecardStatus", handlers.get("SetScorecardStatus"));
        assertNotNull("There should be LoadTimelineTemplate", handlers.get("LoadTimelineTemplate"));
        assertNotNull("There should be SetTimelineNotification", handlers.get("SetTimelineNotification"));
        assertNotNull("There should be PlaceAppeal", handlers.get("PlaceAppeal"));
        assertNotNull("There should be ResolveAppeal", handlers.get("ResolveAppeal"));
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxSupportServlet#init(javax.servlet.ServletConfig)}.
     * In this case, there is no attribute name id in the configuration file.
     * Expected exception : ServletException.
     * @throws Exception to JUnit
     */
    public void testInitNoAttributeNameId() throws Exception {
        try {
            // first remove the original configuration
            ConfigManager cm = ConfigManager.getInstance();
            cm.removeNamespace("com.cronos.onlinereview.ajax");

            cm.add("noattrnameid.xml");

            servlet.init(config);

        } catch (ServletException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxSupportServlet#init(javax.servlet.ServletConfig)}.
     * In this case, there is an empty handler.
     * Expected exception : ServletException.
     * @throws Exception to JUnit
     */
    public void testInitEmptyHandler() throws Exception {
        try {
            // first remove the orginal configuration
            ConfigManager cm = ConfigManager.getInstance();
            cm.removeNamespace("com.cronos.onlinereview.ajax");

            cm.add("emptyhandler.xml");

            servlet.init(config);

        } catch (ServletException e) {
            // should land here
        }
    }

    /**
     * Prepare for test doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse).
     * @param request the request
     *
     * @throws Exception to JUnit
     */
    public void beginDoPostAccuracy(WebRequest request) throws Exception {
        // the test xml string
        String xml = "<?xml version=\"1.0\" ?>"
                    + "<request type=\"SetScorecardStatus\">"
                    + "<parameters>"
                    + "<parameter name=\"ScorecardId\">1</parameter>"
                    + "<parameter name=\"Status\">Active</parameter>"
                    + "</parameters>"
                    + "</request>";

        request.setContentType("text/html; charset=iso-8859-1");
        request.setUserData(new ByteArrayInputStream(xml.getBytes("iso-8859-1")));
    }

    /**
     * Test method for doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse).
     * @throws Exception to JUnit
     */
    public void testDoPostAccuracy() throws Exception {
        session.setAttribute("UserId", new Long(1));
        servlet.init(config);
        servlet.doPost(request, response);
    }

    /**
     * Verify for doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse).
     * @param response the web response
     * @throws Exception to JUnit
     */
    public void endDoPostAccuracy(WebResponse response) throws Exception {
        String xml = response.getText();

        System.out.println(xml);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        // verify the result
        Element root = doc.getDocumentElement();
        assertEquals("The name should be response.", "response", root.getNodeName());

        assertEquals("The type should be SetScorecardStatus.", "SetScorecardStatus", root.getAttribute("type"));

        NodeList children = root.getElementsByTagName("result");
        assertEquals("The length should be 1.", 1, children.getLength());

        Node result = children.item(0);
        assertEquals("The name should be result.", "result", result.getNodeName());
        assertEquals("The status should be success.", "Success", ((Element) result).getAttribute("status"));
    }

    /**
     * Prepare for test doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse).
     * @param request the request
     *
     * @throws Exception to JUnit
     */
    public void beginDoPostBadId(WebRequest request) throws Exception {
        // the test xml string
        String xml = "<?xml version=\"1.0\" ?>"
                    + "<request type=\"SetScorecardStatus\">"
                    + "<parameters>"
                    + "<parameter name=\"ScorecardId\">1</parameter>"
                    + "<parameter name=\"Status\">Active</parameter>"
                    + "</parameters>"
                    + "</request>";

        request.setUserData(new ByteArrayInputStream(xml.getBytes()));
    }

    /**
     * Test method for doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse).
     * In this case, the user id is not a long value.
     * Expected exception : ServletException
     * @throws Exception to JUnit
     */
    public void testDoPostBadId() throws Exception {
        session.setAttribute("UserId", "topcoder");
        servlet.init(config);
        try {
            servlet.doPost(request, response);
            fail("SevletException expected.");
        } catch (ServletException e) {
            // should land here
        }
    }

    /**
     * Prepare for test doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse).
     * @param request the request
     *
     * @throws Exception to JUnit
     */
    public void beginDoPostNoSuchHandler(WebRequest request) throws Exception {
        // the test xml string
        String xml = "<?xml version=\"1.0\" ?>"
                    + "<request type=\"NoSuchHandler\">"
                    + "<parameters>"
                    + "<parameter name=\"ScorecardId\">1</parameter>"
                    + "<parameter name=\"Status\">Active</parameter>"
                    + "</parameters>"
                    + "</request>";

        request.setUserData(new ByteArrayInputStream(xml.getBytes()));
    }

    /**
     * Test method for doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse).
     * @throws Exception to JUnit
     */
    public void testDoPostNoSuchHandler() throws Exception {
        session.setAttribute("UserId", new Long(1));
        servlet.doPost(request, response);
    }

    /**
     * Verify for doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse).
     * @param response the web response
     * @throws Exception to JUnit
     */
    public void endDoPostNoSuchHandler(WebResponse response) throws Exception {
        String xml = response.getText();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        // verify the result
        Element root = doc.getDocumentElement();
        assertEquals("The name should be response.", "response", root.getNodeName());
        assertEquals("The type should be NoSuchHandler.", "NoSuchHandler", root.getAttribute("type"));

        NodeList children = root.getElementsByTagName("result");
        assertEquals("The length should be 1.", 1, children.getLength());

        Node result = children.item(0);
        assertEquals("The name should be result.", "result", result.getNodeName());
        assertEquals("The status should be request error.", "Request error", ((Element) result).getAttribute("status"));
    }

    /**
     * Prepare for test doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse).
     * @param request the request
     *
     * @throws Exception to JUnit
     */
    public void beginDoPostInvalidRequest(WebRequest request) throws Exception {
        // the test xml string
        String xml = "I.am.an.invlaid.request.";

        request.setUserData(new ByteArrayInputStream(xml.getBytes()));
    }

    /**
     * Test method for doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse).
     * @throws Exception to JUnit
     */
    public void testDoPostInvalidRequest() throws Exception {
        session.setAttribute("UserId", new Long(1));
        servlet.doPost(request, response);
    }

    /**
     * Verify for doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse).
     * @param response the web response
     * @throws Exception to JUnit
     */
    public void endDoPostInvalidRequest(WebResponse response) throws Exception {
        String xml = response.getText();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        // verify the result
        Element root = doc.getDocumentElement();
        assertEquals("The name should be response.", "response", root.getNodeName());
        assertEquals("The type should be test.", "Unknown", root.getAttribute("type"));

        NodeList children = root.getElementsByTagName("result");
        assertEquals("The length should be 1.", 1, children.getLength());

        Node result = children.item(0);
        assertEquals("The name should be result.", "result", result.getNodeName());
        assertEquals("The status should be invalid request error.",
                "invalid request error", ((Element) result).getAttribute("status"));
    }

    /**
     * Get private field.
     *
     * @param type the class of the object to get
     * @param name the name of the field.
     * @param obj the object whose field is to be get
     * @return the field value
     * @throws Exception to invoker
     */
    private Object getPrivateFieldValue(Class type, String name, Object obj)
        throws Exception {
        Field field = type.getDeclaredField(name);
        try {
            field.setAccessible(true);
            return field.get(obj);
        } finally {
            field.setAccessible(false);
        }

    }

}
