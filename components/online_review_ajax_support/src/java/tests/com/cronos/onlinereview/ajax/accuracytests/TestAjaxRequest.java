/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.cronos.onlinereview.ajax.AjaxRequest;

import junit.framework.TestCase;

import java.io.StringReader;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Tests for AjaxRequest class.
 *
 * @author assistant
 * @version 1.0
 */
public class TestAjaxRequest extends TestCase {
    /** AjaxRequest instance used for test. */
    private AjaxRequest request = null;

    /** Map instance used for test. */
    private Map parameters = null;

    /** Date instance used for test. */
    private Date date = new Date();

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        parameters = new HashMap();
        parameters.put("key", "value");
        parameters.put("longKey", "1");

        SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy h:mm a");
        parameters.put("dateKey", formatter.format(date));
        request = new AjaxRequest("type", parameters);
    }

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        request = null;
    }

    /**
     * Tests AjaxRequest(String type, Map parameters) method with accuracy state.
     */
    public void testAjaxRequestAccuracy() {
        assertNotNull("creating AjaxRequest fails.", request);
        assertEquals("creating AjaxRequest fails.", "type", request.getType());
        assertEquals("creating AjaxRequest fails.", "value", request.getParameter("key"));
    }

    /**
     * Tests parse(Reader input) method with accuracy state.
     *
     * @throws Exception to Junit
     */
    public void testParseAccuracy() throws Exception {
        String xml = "<?xml version=\"1.0\" ?>" + "<request type=\"ScorecardStatus\">" +
            "<parameters>" + "<parameter name=\"ScorecardId\">999990</parameter>" +
            "<parameter name=\"Status\">InActive</parameter>" + "</parameters>" + "</request>";

        AjaxRequest r = AjaxRequest.parse(new StringReader(xml));

        assertNotNull("parse fails.", r);
        assertEquals("parse fails.", "ScorecardStatus", r.getType());
        assertEquals("parse fails.", "999990", r.getParameter("ScorecardId"));
        assertEquals("parse fails.", "InActive", r.getParameter("Status"));
    }

    /**
     * Tests getType() method with accuracy state.
     */
    public void testGetTypeAccuracy() {
        assertEquals("getType fails.", "type", request.getType());
    }

    /**
     * Tests hasParameter(String name) method with accuracy state.
     */
    public void testHasParameterAccuracy() {
        assertTrue("hasParameter fails.", request.hasParameter("key"));
    }

    /**
     * Tests getParameter(String name) method with accuracy state.
     */
    public void testGetParameterAccuracy() {
        assertEquals("getParameter fails.", "value", request.getParameter("key"));
    }

    /**
     * Tests getParameterAsLong(String name) method with accuracy state.
     */
    public void testGetParameterAsLongAccuracy() {
        assertEquals("getParameterAsLong fails", 1, request.getParameterAsLong("longKey"));
    }

    /**
     * Tests getParameterAsDate(String name) method with accuracy state.
     *
     * @throws Exception to Junit.
     */
    public void testGetParameterAsDateAccuracy() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy h:mm a");
        String expected = formatter.format(date);
        String actual = formatter.format(request.getParameterAsDate("dateKey"));
        assertEquals("getParameterAsDate fails", expected, actual);
    }
}
