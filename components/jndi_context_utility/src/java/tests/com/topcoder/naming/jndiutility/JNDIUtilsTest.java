/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0 (Unit Tests)
 *
 * @ JNDIUtilsTest.java
 */
package com.topcoder.naming.jndiutility;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;


/**
 * Junit test class for JNDIUtils. Since its test in version 1.0 is not sufficient, this class (only) tests the
 * remaining part of it.
 *
 * @author Charizard
 * @version 2.0
 */
public class JNDIUtilsTest extends TestCase {
    /** Name to get the default context. */
    private static final String DEFAULT_NAME = "default";

    /**
     * Test method for {@link JNDIUtils#createName(String, char)}. Failure case, call with blank separator.
     *
     * @throws Exception if error occurs
     */
    public void testCreateNameStringCharFailure() throws Exception {
        try {
            JNDIUtils.createName(TestHelper.generateString(), ' ');
            fail("exception has not been thrown");
        } catch (InvalidNameException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtils#getObject(String)}. Failure case, call with an inexistent name.
     *
     * @throws Exception if error occurs
     */
    public void testGetObjectStringFailure() throws Exception {
        try {
            JNDIUtils.getObject("inexistent object");
            fail("exception has not been thrown");
        } catch (NameNotFoundException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtils#getObject(Name)}. Failure case, call with an inexistent name.
     *
     * @throws Exception if error occurs
     */
    public void testGetObjectNameFailure() throws Exception {
        try {
            JNDIUtils.getObject(JNDIUtils.createName(JNDIUtils.getDefaultContext(), "inexistent object"));
            fail("exception has not been thrown");
        } catch (NameNotFoundException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtils#dump(ContextRenderer, boolean)}. Just run the method since the
     * content of default initial context in test environment is unknown.
     *
     * @throws Exception if error occurs
     */
    public void testDumpContextRendererBoolean() throws Exception {
        JNDIUtils.dump(new ContextXMLRenderer(), false);
    }

    /**
     * Test method for {@link JNDIUtils#dump(Name, ContextRenderer, boolean)}. Just run the method since the
     * content of default initial context in test environment is unknown.
     *
     * @throws Exception if error occurs
     */
    public void testDumpNameContextRendererBoolean() throws Exception {
        JNDIUtils.dump(JNDIUtils.createName(JNDIUtils.getDefaultContext(), ""), new ContextXMLRenderer(), false);
    }

    /**
     * Test method for {@link JNDIUtils#dump(String, ContextRenderer, boolean)}. Just run the method since
     * the content of default initial context in test environment is unknown.
     *
     * @throws Exception if error occurs
     */
    public void testDumpStringContextRendererBoolean()
        throws Exception {
        JNDIUtils.dump("", new ContextXMLRenderer(), false);
    }

    /**
     * Test method for {@link JNDIUtils#main(String[])}. Accuracy case 1, call with three incorrect arguments
     * and check the output.
     *
     * @throws Exception if error occurs
     */
    public void testMainAccuracy1() throws Exception {
        // Replace System.out and check
        PrintStream oldOut = System.out;
        ByteArrayOutputStream tempStream = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(tempStream);
        System.setOut(newOut);

        try {
            JNDIUtils.main(new String[] {"some", "wrong", "arguments"});
            newOut.flush();

            String output = new String(tempStream.toByteArray());
            assertTrue("wrong output", output.startsWith("Usage:"));
        } finally {
            System.setOut(oldOut);
        }
    }

    /**
     * Test method for {@link JNDIUtils#main(String[])}. Accuracy case 2, try to dump a file (not context).
     *
     * @throws Exception if error occurs
     */
    public void testMainAccuracy2() throws Exception {
        // Replace System.err and check
        PrintStream oldErr = System.out;
        ByteArrayOutputStream tempStream = new ByteArrayOutputStream();
        PrintStream newErr = new PrintStream(tempStream);
        System.setErr(newErr);

        try {
            JNDIUtils.main(new String[] {DEFAULT_NAME, "file.txt"});
            newErr.flush();

            String output = new String(tempStream.toByteArray());
            assertTrue("wrong output", output.startsWith("file.txt"));
        } finally {
            System.setErr(oldErr);
        }
    }
}
