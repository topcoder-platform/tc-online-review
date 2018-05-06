/*
 * Copyright (C) 2003, 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0 (Unit Tests)
 *
 * @ ContextRendererTestCase.java
 */
package com.topcoder.naming.jndiutility;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;


/**
 * This is a test covering tests of the two implementations of the ContextRender interface, ContextXMLRenderer
 * and ContextConsoleRenderer.
 *
 * @author preben
 * @author Charizard
 * @version 2.0
 *
 * @since 1.0
 */
public class ContextRendererTestCase extends TestCase {
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

    /** Constant for failure message. */
    static final String CRE = "did not throw ContextRendererException";

    /**
     * Creates a new ContextRenderTestCase object.
     *
     * @param name a String
     */
    public ContextRendererTestCase(String name) {
        super(name);
    }

    /**
     * Return a new Test.
     *
     * @return a new Test
     */
    public static Test suite() {
        return new TestSuite(ContextRendererTestCase.class);
    }

    /**
     * Test ContextXMLRenderer.
     *
     * @throws Exception if any Exception occurs
     */
    public void testContextXMLRenderer() throws Exception {
        ContextRenderer renderer = new ContextXMLRenderer();

        //endContext() is called before startContext()
        try {
            renderer.endContext("C:/this/is/a/test", "");
            fail(CRE);
        } catch (ContextRendererException e) {
            // should land here
        }

        //bindingFound() is called 'outside a Context
        try {
            renderer.bindingFound("test", "java.lang.File");
            fail(CRE);
        } catch (ContextRendererException e) {
            // should land here
        }

        //Pass null as arguments
        try {
            renderer.startContext(null, null);
            fail(CRE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        //Pass null as arguments
        try {
            renderer.endContext(null, null);
            fail(CRE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        //Pass null as arguments
        try {
            renderer.bindingFound(null, null);
            fail(CRE);
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test ContextConsoleRenderer.
     *
     * @throws Exception if any Exception occurs
     */
    public void testContextConsoleRenderer() throws Exception {
        ContextRenderer renderer = new ContextConsoleRenderer();

        //endContext() is called before startContext()
        try {
            renderer.endContext("C:/this/is/a/test", "");
            fail(CRE);
        } catch (ContextRendererException e) {
            // should land here
        }

        //bindingFound() is called 'outside a Context
        try {
            renderer.bindingFound("test", "java.lang.File");
            fail(CRE);
        } catch (ContextRendererException e) {
            // should land here
        }

        //Pass null as arguments
        try {
            renderer.startContext(null, null);
            fail(CRE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        //Pass null as arguments
        try {
            renderer.endContext(null, null);
            fail(CRE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        //Pass null as arguments
        try {
            renderer.bindingFound(null, null);
            fail(CRE);
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link ContextXMLRenderer#startContext(String, String)}. Failure case, add a child to
     * the document before calling.
     */
    public void testContextXMLRendererStartContextFailure() {
        ContextXMLRenderer renderer = new ContextXMLRenderer();
        Document doc = renderer.getDocument();
        doc.appendChild(doc.createElement(TestHelper.generateString()));

        try {
            renderer.startContext(TestHelper.generateString(), TestHelper.generateString());
            fail("exception has not been thrown");
        } catch (ContextRendererException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link ContextXMLRenderer#endContext(String, String)}. Failure case, call with
     * inconsistent names.
     */
    public void testContextXMLRendererEndContextFailure() {
        ContextRenderer renderer = new ContextXMLRenderer();
        renderer.startContext(TestHelper.generateString(), TestHelper.generateString());

        try {
            renderer.endContext(TestHelper.generateString(), TestHelper.generateString());
            fail("exception has not been thrown");
        } catch (ContextRendererException e) {
            // should land here
        }
    }
}
