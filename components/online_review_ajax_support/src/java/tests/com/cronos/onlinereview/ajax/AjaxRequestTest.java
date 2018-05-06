/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Test the class <code>AjaxRequest</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class AjaxRequestTest extends TestCase {

    /**
     * Represents the type.
     */
    private static final String TYPE = "Appeals";

    /**
     * Represents the parameters.
     */
    private static final Map PARAMS = new HashMap();

    /**
     * Represents the date.
     */
    private static final Date DATE = new Date();

    /**
     * Represents the object to test.
     */
    private AjaxRequest request;

    /**
     * Set up the environment.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();

        // use the formatter to format date
        SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy h:mm a");
        // ignore the seconds
        DATE.setSeconds(0);

        // put two values into the parameter
        PARAMS.put("key", "value"); // this is a normal string parameter
        PARAMS.put("long", "100");  // this is a long parameter
        PARAMS.put("date", formatter.format(DATE));     // this is a date parameter

        request = new AjaxRequest(TYPE, PARAMS);
    }

    /**
     * Test method for AjaxRequest(java.lang.String, java.util.Map).
     */
    public void testAjaxRequest() {
        assertNotNull("The constructor doesn't work.", request);

        // verify the type
        assertEquals("The type is not set correctly.", TYPE, request.getType());

        // verify the parameters
        assertEquals("The parameter is not set correctly.", "value", request.getParameter("key"));
        assertEquals("The parameter is not set correctly.", "100", request.getParameter("long"));
    }

    /**
     * Test method for AjaxRequest(java.lang.String, java.util.Map).
     * In this case, the type is null.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testAjaxRequestNullType() {
        try {
            new AjaxRequest(null, PARAMS);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for AjaxRequest(java.lang.String, java.util.Map).
     * In this case, the type is empty.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testAjaxRequestEmptyType() {
        try {
            new AjaxRequest(" ", PARAMS);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for AjaxRequest(java.lang.String, java.util.Map).
     * In this case, the parameters is null.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testAjaxRequestNullParameters() {
        try {
            new AjaxRequest(TYPE, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for AjaxRequest(java.lang.String, java.util.Map).
     * In this case, the parameter has some entry which doesn't has type string.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testAjaxRequestNonStringParameters1() {
        try {
            Map newMap = new HashMap();
            newMap.put(new Object(), "test");

            new AjaxRequest(TYPE, newMap);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for AjaxRequest(java.lang.String, java.util.Map).
     * In this case, the parameter has some entry which doesn't has type string.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testAjaxRequestNonStringParameters2() {
        try {
            Map newMap = new HashMap();
            newMap.put("test", new Object());

            new AjaxRequest(TYPE, newMap);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for parse(java.io.Reader).
     * In this case, we have two parameters.
     * @throws Exception to JUnit
     */
    public void testParseAccuracy1() throws Exception {
        // the test xml string
        String xml = "<?xml version=\"1.0\" ?>"
                    + "<request type=\"SetScorecardStatus\">"
                    + "<parameters>"
                    + "<parameter name=\"ScorecardId\">45</parameter>"
                    + "<parameter name=\"Status\">Active</parameter>"
                    + "</parameters>"
                    + "</request>";

        AjaxRequest req = AjaxRequest.parse(new StringReader(xml));

        assertNotNull("The request should not be null.", req);

        // verify the type
        assertEquals("The type is not right.", "SetScorecardStatus", req.getType());

        // verify the parameters
        assertEquals("The scorecardId is not right.", "45", req.getParameter("ScorecardId"));
        assertEquals("The status is not right.", "Active", req.getParameter("Status"));
    }

    /**
     * Test method for parse(java.io.Reader).
     * We have no parameters. This is fine.
     *
     * @throws Exception to JUnit
     */
    public void testParseAccuracy2() throws Exception {
        // the test xml string
        String xml = "<?xml version=\"1.0\" ?>"
                    + "<request type=\"SetScorecardStatus\">"
                    + "<parameters/>"
                    + "</request>";

        AjaxRequest req = AjaxRequest.parse(new StringReader(xml));

        assertNotNull("The request should not be null.", req);

        // verify the type
        assertEquals("The type is not right.", "SetScorecardStatus", req.getType());
    }

    /**
     * Test method for parse(java.io.Reader).
     * In this case, the reader is null.
     * Expected exception : {@link IllegalArgumentException}.
     *
     * @throws Exception to JUnit
     */
    public void testParseNullReader() throws Exception {
        try {
            AjaxRequest.parse(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for parse(java.io.Reader).
     * In this case, the input is invalid xml file.
     * Expected exception : {@link RequestParsingException}.
     *
     * @throws Exception to JUnit
     */
    public void testParseInvalidInput() throws Exception {
        try {
            AjaxRequest.parse(new StringReader("not.an.xml"));
            fail("IllegalArgumentException expected.");
        } catch (RequestParsingException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#getType()}.
     */
    public void testGetType() {
        assertEquals("The type is not get correctly.", TYPE, request.getType());
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#hasParameter(java.lang.String)}.
     */
    public void testHasParameter() {
        assertTrue("Should have parameter key.", request.hasParameter("key"));
        assertFalse("Should not have parameter key1.", request.hasParameter("key1"));
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#hasParameter(java.lang.String)}.
     * In this case, the name is null.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testHasParameterNullName() {
        try {
            request.hasParameter(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#hasParameter(java.lang.String)}.
     * In this case, the name is empty.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testHasParameterEmptyName() {
        try {
            request.hasParameter(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#getParameter(java.lang.String)}.
     */
    public void testGetParameter() {
        assertEquals("The value is not right.", "value", request.getParameter("key"));
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#getParameter(java.lang.String)}.
     * In this case, the name is null.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testGetParameterNullName() {
        try {
            request.getParameter(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#getParameter(java.lang.String)}.
     * In this case, the name is empty.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testGetParameterEmptyName() {
        try {
            request.getParameter(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#getParameterAsLong(java.lang.String)}.
     */
    public void testGetParameterAsLong() {
        assertEquals("The value is not right.", 100, request.getParameterAsLong("long"));
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#getParameterAsLong(java.lang.String)}.
     * In this case, the name is null.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testGetParameterAsLongNullName() {
        try {
            request.getParameterAsLong(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#getParameterAsLong(java.lang.String)}.
     * In this case, the name is empty.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testGetParameterAsLongEmptyName() {
        try {
            request.getParameterAsLong(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#getParameterAsLong(java.lang.String)}.
     * In this case, the value is not a long string.
     * Expected exception : {@link NumberFormatException}.
     */
    public void testGetParameterAsLongNotLong() {
        try {
            request.getParameterAsLong("date");
            fail("NumberFormatException expected.");
        } catch (NumberFormatException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#getParameterAsDate(java.lang.String)}.
     * @throws Exception to JUnit
     */
    public void testGetParameterAsDate() throws Exception {
        Date date = request.getParameterAsDate("date");

        assertEquals("The date is not right.", DATE.toLocaleString(), date.toLocaleString());
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#getParameterAsDate(java.lang.String)}.
     * In this case, the name is null.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testGetParameterAsDateNullName() throws Exception {
        try {
            request.getParameterAsDate(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#getParameterAsDate(java.lang.String)}.
     * In this case, the name is empty.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testGetParameterAsDateEmptyName() throws Exception {
        try {
            request.getParameterAsDate(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxRequest#getParameterAsDate(java.lang.String)}.
     * In this case, the date is not a date string as format.
     * Expected exception : {@link ParseException}.
     * @throws Exception to JUnit
     */
    public void testGetParameterAsDateNotADate() throws Exception {
        try {
            request.getParameterAsDate("long");
            fail("ParseException expected.");
        } catch (ParseException e) {
            // should land here
        }
    }
}
