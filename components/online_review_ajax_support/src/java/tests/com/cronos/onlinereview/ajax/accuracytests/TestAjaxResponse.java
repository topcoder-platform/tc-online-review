/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.cronos.onlinereview.ajax.AjaxResponse;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Tests for AjaxResponse class.
 *
 * @author assistant
 * @version 1.0
 */
public class TestAjaxResponse extends TestCase {
    /** AjaxResponse instance used for test. */
    private AjaxResponse response = null;

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        response = new AjaxResponse("typeValue", "Active", "param");
    }

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        response = null;
    }

    /**
     * Tests AjaxResponse(String type, String status, Object data) method with accuracy
     * state.
     */
    public void testAjaxResponseAccuracy() {
        assertNotNull("Creating AjaxResponse fails.", response);
        assertEquals("Creating AjaxResponse fails.", "typeValue", response.getType());
        assertEquals("Creating AjaxResponse fails.", "Active", response.getStatus());
        assertEquals("Creating AjaxResponse fails.", "param", (String) response.getData());
    }

    /**
     * Tests getType() method with accuracy state.
     */
    public void testGetTypeAccuracy() {
        assertEquals("getType fails.", "typeValue", response.getType());
    }

    /**
     * Tests getStatus() method with accuracy state.
     */
    public void testGetStatusAccuracy() {
        assertEquals("getStatus fails.", "Active", response.getStatus());
    }

    /**
     * Tests getData() method with accuracy state.
     */
    public void testGetDataAccuracy() {
        assertEquals("getData fails.", "param", (String) response.getData());
    }

    /**
     * Tests toXml(boolean withHeader) method with accuracy state.
     *
     * @throws Exception to JUnit.
     */
    public void testToXmlAccuracy1() throws Exception {
        String xml = response.toXml(true);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));

        Element root = doc.getDocumentElement();
        assertEquals("toXml fails", "response", root.getNodeName());
        assertEquals("toXml fails", "typeValue", root.getAttribute("type"));

        NodeList firstNode = root.getElementsByTagName("result");
        assertEquals("toXml fails", 1, firstNode.getLength());

        Node secondNode = firstNode.item(0);
        assertEquals("toXml fails", "result", secondNode.getNodeName());
        assertEquals("toXml fails", "Active",
            ((Element) secondNode).getAttribute("status"));
    }

    /**
     * Tests toXml(boolean withHeader) method with accuracy state.
     *
     * @throws Exception to JUnit.
     */
    public void testToXmlAccuracy2() throws Exception {
        String xml = response.toXml(false);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));

        Element root = doc.getDocumentElement();
        assertEquals("toXml fails", "response", root.getNodeName());
        assertEquals("toXml fails", "typeValue", root.getAttribute("type"));

        NodeList firstNode = root.getElementsByTagName("result");
        assertEquals("toXml fails", 1, firstNode.getLength());

        Node secondNode = firstNode.item(0);
        assertEquals("toXml fails", "result", secondNode.getNodeName());
        assertEquals("toXml fails", "Active",
            ((Element) secondNode).getAttribute("status"));
    }
}
