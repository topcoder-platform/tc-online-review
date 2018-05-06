/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.cronos.onlinereview.ajax.AjaxSupportServlet;
import com.cronos.onlinereview.ajax.handlers.LoadTimelineTemplateHandler;
import com.cronos.onlinereview.ajax.handlers.PlaceAppealHandler;
import com.cronos.onlinereview.ajax.handlers.ResolveAppealHandler;
import com.cronos.onlinereview.ajax.handlers.SetScorecardStatusHandler;
import com.cronos.onlinereview.ajax.handlers.SetTimelineNotificationHandler;

import org.apache.cactus.ServletTestCase;
import org.apache.cactus.WebRequest;
import org.apache.cactus.WebResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import java.lang.reflect.Method;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Tests for AjaxSupportServlet class.
 *
 * @author assistant
 * @version 1.0
 */
public class TestAjaxSupportServlet extends ServletTestCase {
    /** AjaxSupportServlet instance used for test. */
    private AjaxSupportServlet servlet;

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        TestHelper.clearNamespaces();
        TestHelper.loadFile("test_files/accuracy/accuracy.xml");
        TestHelper.loadFile("test_files/accuracy/scorecalculator.xml");
        servlet = new AjaxSupportServlet();
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
     * Tests AjaxSupportServlet() method with accuracy state.
     */
    public void testAjaxSupportServletAccuracy() {
        assertNotNull("creating AjaxSupportServlet fails.", servlet);
    }

    /**
     * Tests init(ServletConfig config) method with accuracy state.
     *
     * @throws Exception to JUNIt.
     */
    public void testInitAccuracy() throws Exception {
        // Initial the vairables.
        servlet.init(config);
        assertEquals("init fails.", "UserId",
            TestHelper.getVariable(AjaxSupportServlet.class, "userIdAttributeName", servlet));

        Map handlers = (Map) TestHelper.getVariable(AjaxSupportServlet.class, "handlers", servlet);
        assertEquals("destroy fails", 5, handlers.size());

        assertTrue("init fails",
            handlers.get("SetScorecardStatus") instanceof SetScorecardStatusHandler);
        assertTrue("init fails",
            handlers.get("LoadTimelineTemplate") instanceof LoadTimelineTemplateHandler);
        assertTrue("init fails",
            handlers.get("SetTimelineNotification") instanceof SetTimelineNotificationHandler);
        assertTrue("init fails", handlers.get("PlaceAppeal") instanceof PlaceAppealHandler);
        assertTrue("init fails", handlers.get("ResolveAppeal") instanceof ResolveAppealHandler);
    }

    /**
     * Before testing doPost.
     *
     * @param request the request
     *
     * @throws Exception to JUnit
     */
    public void beginDoPostAccuracy(WebRequest request)
        throws Exception {
        String xml = "<?xml version=\"1.0\" ?><request type=\"SetScorecardStatus\">" +
            "<parameters><parameter name=\"ScorecardId\">999990</parameter>" +
            "<parameter name=\"Status\">Inactive</parameter></parameters></request>";

        request.setUserData(new ByteArrayInputStream(xml.getBytes()));
    }

    /**
     * Test doPost method with accuracy case.
     *
     * @throws Exception to JUnit.
     */
    public void testDoPostAccuracy() throws Exception {
        Method dopostPrivate = servlet.getClass().getDeclaredMethod("doPost",
                new Class[] { HttpServletRequest.class, HttpServletResponse.class });

        dopostPrivate.setAccessible(true);
        session.setAttribute("UserId", new Long(999990));
        servlet.init(config);
        dopostPrivate.invoke(servlet, new Object[] { request, response });
    }

    /**
     * After testing doPost.
     *
     * @param response the web response
     *
     * @throws Exception to JUnit
     */
    public void endDoPostAccuracy(WebResponse response)
        throws Exception {
        String xml = response.getText();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));

        Element root = doc.getDocumentElement();
        assertEquals("endDoPostAccuracy fails.", "response", root.getNodeName());
    }

    /**
     * Tests destroy() method with accuracy state.
     *
     * @throws Exception to JUNIt.
     */
    public void testDestroyAccuracy() throws Exception {
        // Initial the vairables.
        servlet.init(config);
        assertEquals("init fails.", "UserId",
            TestHelper.getVariable(AjaxSupportServlet.class, "userIdAttributeName", servlet));

        Map handlers = (Map) TestHelper.getVariable(AjaxSupportServlet.class, "handlers", servlet);
        assertEquals("destroy fails", 5, handlers.size());

        // destroy the variables.
        servlet.destroy();

        assertNull("destroy fails.",
            TestHelper.getVariable(AjaxSupportServlet.class, "userIdAttributeName", servlet));

        Map handlers1 = (Map) TestHelper.getVariable(AjaxSupportServlet.class, "handlers", servlet);
        assertTrue("init fails.", handlers1.isEmpty());
    }
}
