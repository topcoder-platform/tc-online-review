/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 *
 * @(#) DumpContextTestCase.java
 *
 * 1.0 08/09/2003
 */
package com.topcoder.naming.jndiutility;

import javax.naming.Context;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * This test case tests that the dump() methods work correctly.
 *
 * @author preben
 * @version 1.0 08/09/2003
 */
public class DumpContextTestCase extends TestCase {
    /** The file separator. */
    static final String FS = System.getProperty("file.separator");

    /** A slash. */
    static final String SLASH = "/";

    /** Shall we print warnings. */
    static final boolean WARNING = false;

    /** Constant for failure message. */
    static final String IAE = "did not throw IllegalArgumentException";

    /** Constant for failure message. */
    static final String INE = "did not throw InvalidNameException";

    /** Constant for failure message. */
    static final String NE = "did not throw NamingException";

    /**
     * Creates a new DumpContextTestCase object.
     *
     * @param name a String
     */
    public DumpContextTestCase(String name) {
        super(name);
    }

    /**
     * Constructor.
     *
     * @return a new Test
     */
    public static Test suite() {
        return new TestSuite(DumpContextTestCase.class);
    }

    /**
     * Create a context that we can work with.
     *
     * @throws Exception if any exception occurs
     */
    public void setUp() throws Exception {
        Context def = JNDIUtils.getDefaultContext();
        Context context = JNDIUtils.createSubcontext(def, "this");
        context.bind("topic", new TopicImpl("topic"));
        context.bind("queue", new QueueImpl("queue"));
    }

    /**
     * Destroy the context that we created in setUp().
     *
     * @throws Exception if any exception occurs
     */
    public void tearDown() throws Exception {
        Context def = JNDIUtils.getDefaultContext();
        Context context = JNDIUtils.createSubcontext(def, "this");
        context.unbind("topic");
        context.unbind("queue");
        def.destroySubcontext("this");
    }

    /**
     * Test the dump functionality.
     *
     * @throws Exception if any exception occurs
     */
    public void testDump() throws Exception {
        Context def = JNDIUtils.getContext("default");

        ContextXMLRenderer renderer = new ContextXMLRenderer();
        JNDIUtils.dump(def, renderer, false);

        Document doc = renderer.getDocument();

        //Check the structure of the document
        Element root = doc.getDocumentElement();
        assertEquals(root.getTagName(), "Context");
        assertTrue(root.hasAttribute("name"));
        assertTrue(root.hasAttribute("fullname"));
        assertEquals(root.getAttribute("name"), "");
        assertEquals(root.getAttribute("fullname"), def.getNameInNamespace());

        Element child = (Element) root.getFirstChild();
        assertEquals((child.getChildNodes()).getLength(), 2);
        assertEquals(child.getTagName(), "Binding");

        Node name = child.getFirstChild();
        Node nameValue = name.getFirstChild();

        Node clazz = child.getLastChild();
        Node clazzValue = clazz.getFirstChild();

        assertEquals(nameValue.getNodeValue(), "this");
        assertEquals(clazzValue.getNodeValue(), "javax.naming.Context");
    }
}
