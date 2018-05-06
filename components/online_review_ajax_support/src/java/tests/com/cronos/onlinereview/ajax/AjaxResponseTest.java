/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import junit.framework.TestCase;

/**
 * Test the class <code>AjaxResponse</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class AjaxResponseTest extends TestCase {

    /**
     * Represents the type of this response.
     */
    private static final String TYPE = "test";

    /**
     * Represents the status of this response.
     */
    private static final String STATUS = "success";

    /**
     * Represents the response to test.
     */
    private AjaxResponse response;

    /**
     * Set up the environment.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();

        response = new AjaxResponse(TYPE, STATUS, null);
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxResponse#AjaxResponse(String, String, Object)}.
     */
    public void testAjaxResponse() {
        assertNotNull("The constructor doesn't work.", response);

        assertEquals("The type is not set.", TYPE, response.getType());
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxResponse#AjaxResponse(String, String, Object)}.
     * In this case, the type is null.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testAjaxResponseNullType() {
        try {
            new AjaxResponse(null, STATUS, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxResponse#AjaxResponse(String, String, Object)}.
     * In this case, the type is empty.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testAjaxResponseEmptyType() {
        try {
            new AjaxResponse(" ", STATUS, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxResponse#AjaxResponse(String, String, Object)}.
     * In this case, the status is null.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testAjaxResponseNullStatus() {
        try {
            new AjaxResponse(TYPE, null, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxResponse#AjaxResponse(String, String, Object)}.
     * In this case, the status is empty.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testAjaxResponseEmptyStatus() {
        try {
            new AjaxResponse(TYPE, " ", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxResponse#getType()}.
     */
    public void testGetType() {
        assertEquals("The type is not right.", TYPE, response.getType());
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxResponse#getStatus()}.
     */
    public void testGetStatus() {
        assertEquals("The status is not right.", STATUS, response.getStatus());
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxResponse#getData()}.
     */
    public void testGetData() {
        assertNull("The data should be null.", response.getData());
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxResponse#toXml(boolean)}.
     * In this case, the response with no data.
     * @throws Exception to JUnit
     */
    public void testToXmlAccuracy1() throws Exception {
        String xml = response.toXml(true);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        // verify the result
        Element root = doc.getDocumentElement();
        assertEquals("The name should be response.", "response", root.getNodeName());
        assertEquals("The type should be test.", TYPE, root.getAttribute("type"));

        NodeList children = root.getElementsByTagName("result");
        assertEquals("The length should be 1.", 1, children.getLength());

        Node result = children.item(0);
        assertEquals("The name should be result.", "result", result.getNodeName());
        assertEquals("The status should be success.", "success", ((Element) result).getAttribute("status"));

        NodeList beNothing = result.getChildNodes();
        assertEquals("There is no data.", 0, beNothing.getLength());
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.AjaxResponse#toXml(boolean)}.
     * In this case, the response has data.
     * @throws Exception to JUnit
     */
    public void testToXmlAccuracy2() throws Exception {

        response = new AjaxResponse(TYPE, STATUS, new Double(1.1));

        String xml = response.toXml(true);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        // verify the result
        Element root = doc.getDocumentElement();
        assertEquals("The name should be response.", "response", root.getNodeName());
        assertEquals("The type should be test.", TYPE, root.getAttribute("type"));

        NodeList children = root.getElementsByTagName("result");
        assertEquals("The length should be 1.", 1, children.getLength());

        Node result = children.item(0);
        assertEquals("The name should be result.", "result", result.getNodeName());
        assertEquals("The status should be success.", "success", ((Element) result).getAttribute("status"));

        Node data = result.getChildNodes().item(0);
        assertEquals("There should be data.", "1.1", data.getNodeValue());
    }

}
