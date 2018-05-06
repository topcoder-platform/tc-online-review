/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.failure;

import java.util.Locale;

import com.topcoder.util.datavalidator.BundleInfo;

import junit.framework.TestCase;


/**
 * <p>
 * Test the functionality of class <code>BundleInfo</code>.
 * </p>
 * 
 * <p>
 * This test suite contains multiple failure test cases that addressed different aspects for each public methods and constructors.<br>
 * Various real data is used to ensure that the invalid inputs are handled properly as defined in the
 * documentation.<br>
 * </p>
 *
 * @author lyt
 * @version 1.0
 */
public class BundleInfoFailureTests extends TestCase {
	/**
	 * <p>
	 * Represents the bundle name.
	 * </p>
	 */
    private static final String BUNDLE_NAME = "failure/language";
	/**
     * <p>
     * Represents an instance of <code>BundleInfo</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private BundleInfo bundleInfo = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     * 
     * <p>
     * The test instance is initialized and all the need configuration are loaded.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        bundleInfo = new BundleInfo();
    }

    /**
     * <p>
     * Tear down the test environment.
     * </p>
     * 
     * <p>
     * The test instance is released and the configuration is clear.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        bundleInfo = null;
        super.tearDown();
    }

    /**
     * <p>
     * Failure test case for method 'setMessageKey()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetMessageKey_String_Null1() {
        try {
            bundleInfo.setMessageKey(null);
            fail("Test failure for setMessageKey() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setMessageKey()'.<br>
     * The argument is an empty <code>String</code>, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetMessageKey_String_Empty1() {
        try {
            bundleInfo.setMessageKey("     ");
            fail("Test failure for setMessageKey() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setDefaultMessage()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetDefaultMessage_String_Null1() {
        try {
            bundleInfo.setDefaultMessage(null);
            fail("Test failure for setDefaultMessage() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setDefaultMessage()'.<br>
     * The argument is an empty <code>String</code>, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetDefaultMessage_String_Empty1() {
        try {
            bundleInfo.setDefaultMessage("     ");
            fail("Test failure for setDefaultMessage() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setBundle()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetBundle_StringLocale_Null1() {
        try {
            bundleInfo.setBundle(null, Locale.ENGLISH);
            fail("Test failure for setBundle() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setBundle()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetBundle_StringLocale_Null2() {
        try {
            bundleInfo.setBundle(BUNDLE_NAME, null);
            fail("Test failure for setBundle() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setBundle()'.<br>
     * The argument is an empty <code>String</code>, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetBundle_StringLocale_Empty1() {
        try {
            bundleInfo.setBundle("	", Locale.ENGLISH);
            fail("Test failure for setBundle() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setBundle()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetBundle_String_Null1() {
        try {
            bundleInfo.setBundle(null);
            fail("Test failure for setBundle() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setBundle()'.<br>
     * The argument is an empty <code>String</code>, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testSetBundle_String_Empty1() {
        try {
            bundleInfo.setBundle("     ");
            fail("Test failure for setBundle() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }
}
