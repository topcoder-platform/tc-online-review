/*
 * Copyright (C) 2007, 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import java.io.Reader;
import java.io.StringReader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for TestUtil.java class.
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class TestUtil extends TestCase {
    /**
     * Aggregates all tests in this class.
     * @return Test suite aggregating all tests.
     */
    public static Test suite() {
        return new TestSuite(TestUtil.class);
    }

    /**
     * Sets up the environment for the TestCase.
     * @throws Exception
     *             throw exception to JUnit.
     */
    protected void setUp() throws Exception {
        // do nothing
    }

    /**
     * Tears down the environment for the TestCase.
     * @throws Exception
     *             throw exception to JUnit.
     */
    protected void tearDown() throws Exception {
        // do nothing
    }

    /**
     * Accuracy test of <code>checkNull(Object arg, String name)</code> method.
     * <p>
     * no exception occurs.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckNull_accuracy() throws Exception {
        Util.checkNull("test", "test");
    }

    /**
     * Failure test of <code>checkNull(Object arg, String name)</code> method.
     * <p>
     * arg is null.
     * </p>
     * <p>
     * Expect IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckNull_failure() throws Exception {
        try {
            Util.checkNull(null, "test");
            fail("Expect IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expect
        }
    }

    /**
     * Accuracy test of <code>checkString(String arg, String name)</code> method.
     * <p>
     * no exception should throw.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckString_accuracy() throws Exception {
        Util.checkString("test", "test");
    }

    /**
     * Failure test of <code>checkString(String arg, String name)</code> method.
     * <p>
     * arg is null.
     * </p>
     * <p>
     * Expect IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckString_failure_null_arg() throws Exception {
        try {
            Util.checkString(null, "test");
            fail("Expect IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>checkString(String arg, String name)</code> method.
     * <p>
     * arg is empty.
     * </p>
     * <p>
     * Expect IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckString_failure_empty_arg() throws Exception {
        try {
            Util.checkString("  ", "test");
            fail("Expect IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expect
        }
    }

    /**
     * Accuracy test of <code>readString(Reader reader)</code> method.
     * @throws Exception
     *             to JUnit.
     */
    public void testReadString_accuracy() throws Exception {
        Reader reader = new StringReader("testasdgcxbujowuireotuio");
        assertEquals("result is incorrect.", "testasdgcxbujowuireotuio", Util.readString(reader));
    }

    /**
     * Failure test of <code>readString(Reader reader)</code> method.
     * <p>
     * reader is null.
     * </p>
     * <p>
     * Expect IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testReadString_failure_null_reader() throws Exception {
        try {
            Util.readString(null);
            fail("Expect IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expect
        }
    }
}
