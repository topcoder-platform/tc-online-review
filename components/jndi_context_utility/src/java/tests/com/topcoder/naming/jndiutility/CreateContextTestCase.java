/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 *
 * @(#) CreateContextTestCase.java
 *
 * 1.0 08/09/2003
 */
package com.topcoder.naming.jndiutility;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * This test case tests that the createSubcontext() methods work correctly. It also test the getContext() and
 * createName() methds.
 *
 * @author preben
 * @version 1.0 08/09/2003
 */
public class CreateContextTestCase extends TestCase {
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
     * Creates a new CreateContextTestCase object.
     *
     * @param name a String
     */
    public CreateContextTestCase(String name) {
        super(name);
    }

    /**
     * Constructor.
     *
     * @return a new Test
     */
    public static Test suite() {
        return new TestSuite(CreateContextTestCase.class);
    }

    /**
     * Test the createName() methods.
     *
     * @throws Exception if any exception occurs
     */
    public void testCreateName() throws Exception {
        String nameString = "com.topcoder.util";
        String slashString = "com" + SLASH + "topcoder" + SLASH + "util";
        Name name = JNDIUtils.createName(nameString, '.');
        assertEquals(name.toString(), slashString);

        name = JNDIUtils.createName(JNDIUtils.getDefaultContext(), slashString);
        assertEquals(name.toString(), slashString);
        name = JNDIUtils.createName(nameString);
        assertNotNull(name);
    }

    /**
     * Test the createSubcontext() methods throws Exception if any exception is thrown.
     *
     * @throws Exception if any exception occurs
     */
    public void testCreateSubcontext() throws Exception {
        Context defaultContext = JNDIUtils.getDefaultContext();
        Name name = JNDIUtils.createName(defaultContext, "this" + FS + "is" + FS + "a" + FS + "test");
        String nameAsString = name.toString();

        //Check that the name is not bound
        try {
            defaultContext.lookup(name);
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }

        Context context = JNDIUtils.createSubcontext(defaultContext, name);
        assertNotNull(context);

        Object theContext = defaultContext.lookup(name);
        assertNotNull(theContext);
        assertNotNull(JNDIUtils.createSubcontext(defaultContext, name));
        assertTrue(theContext instanceof Context);
        destroySubcontext(defaultContext);

        //Check that the name is not bound
        try {
            defaultContext.lookup(name);
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }

        context = JNDIUtils.createSubcontext(defaultContext, nameAsString);
        assertNotNull(context);
        theContext = defaultContext.lookup(name);
        assertNotNull(theContext);
        assertNotNull(JNDIUtils.createSubcontext(defaultContext, nameAsString));
        assertTrue(theContext instanceof Context);
        destroySubcontext(defaultContext);
    }

    /**
     * Destroys the subcontexts that are used in the test.
     *
     * @param context the root of the context to destroy
     *
     * @throws Exception if any exception occurs
     */
    private void destroySubcontext(Context context) throws Exception {
        context.destroySubcontext("this" + FS + "is" + FS + "a" + FS + "test");
        context.destroySubcontext("this" + FS + "is" + FS + "a");
        context.destroySubcontext("this" + FS + "is");
        context.destroySubcontext("this");
    }
}
