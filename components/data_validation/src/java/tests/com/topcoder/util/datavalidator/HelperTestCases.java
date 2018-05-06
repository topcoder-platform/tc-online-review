/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;


/**
 * <p>
 * Test the functionality of class <code>Helper</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class HelperTestCases extends TestCase {
    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * <p>
     * Tear down the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>
     * Test method for 'Helper.checkNull(Object, String)'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testCheckNull() {
        try {
            Helper.checkNull(null, "parameter");
            fail("Test failure for Helper.checkNull() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Test method for 'Helper.checkNull(Object, String)'.<br> The argument is not null, checking is successful.
     * </p>
     */
    public void testCheckNull_succ() {
        try {
            Helper.checkNull(null, "parameter");

            // Success
        } catch (IllegalArgumentException iae) {
            fail("Test failure for Helper.checkNull() failed, no exception should be thrown.");
        }
    }

    /**
     * Test method for 'Helper.checkString(String, String)'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     */
    public void testCheckString_null() {
        try {
            Helper.checkString(null, "parameter");
            fail("Test failure for Helper.checkNull() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method for 'Helper.checkString(String, String)'.<br>
     * The argument is an empty <code>String</code>, <code>IllegalArgumentException</code> should be thrown.
     */
    public void testCheckString_empty() {
        try {
            Helper.checkString("   ", "parameter");
            fail("Test failure for Helper.checkNull() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method for 'Helper.checkString(String, String)'.<br>
     * The argument is not neither null or empty, checking is successful.
     */
    public void testCheckString_succ() {
        try {
            Helper.checkString("   ", "parameter");

            // Success
        } catch (IllegalArgumentException iae) {
            fail("Test failure for Helper.checkNull() failed, no exception should be thrown.");
        }
    }
}
