/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.failure;

import com.topcoder.util.datavalidator.AbstractObjectValidator;
import com.topcoder.util.datavalidator.BundleInfo;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * <p>
 * Test the functionality of class <code>AbstractObjectValidator</code>.
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
public abstract class AbstractObjectValidatorFailureTests extends TestCase {
    /**
     * <p>
     * Represents the bundle name.
     * </p>
     */
    protected static final String BUNDLE_NAME = "failure/language";

    /**
     * <p>
     * Represents an instance of <code>BundleInfo</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    protected BundleInfo bundleInfo = null;

    /**
     * <p>
     * Represents an instance of <code>TypeValidator</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    protected AbstractObjectValidator validator = null;

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
        bundleInfo.setBundle(BUNDLE_NAME, Locale.ENGLISH);
        bundleInfo.setDefaultMessage("validation fails");
        bundleInfo.setMessageKey("validation");
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
        validator = null;
        super.tearDown();
    }

    /**
     * <p>
     * Failure test case for method 'getAllMessages()'.<br>
     * The value is zero, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testGetAllMessages_Objectint_Failure() {
        try {
            validator.getAllMessages("Validation String", 0);
            fail("Test failure for getAllMessages() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'getAllMessages()'.<br>
     * The value is negative, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testGetAllMessages_Objectint_Failure2() {
        try {
            validator.getAllMessages("Validation String", -1);
            fail("Test failure for getAllMessages() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setId()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetId_String_Null1() {
        try {
            validator.setId(null);
            fail("Test failure for setId() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setId()'.<br>
     * </p>
     */
    public void testSetId_String_Empty1() {
        try {
            validator.setId("    ");

            // Success
        } catch (IllegalArgumentException iae) {
            fail("Test failure for setId() failed.");
        }
    }

    /**
     * <p>
     * Failure test case for method 'setResourceBundleInfo()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetResourceBundleInfo_BundleInfo_Null1() {
        try {
            validator.setResourceBundleInfo(null);
            fail("Test failure for setResourceBundleInfo() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setResourceBundleInfo()'.<br>
     * The argument is invalid, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetResourceBundleInfo_BundleInfo_failure1() {
        bundleInfo = new BundleInfo();

        // BUNDLE_NAME was null
        // bundleInfo.setBundle(BUNDLE_NAME, Locale.ENGLISH);
        bundleInfo.setDefaultMessage("validation fails");
        bundleInfo.setMessageKey("validation");

        try {
            validator.setResourceBundleInfo(bundleInfo);
            fail("Test failure for setResourceBundleInfo() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setResourceBundleInfo()'.<br>
     * The argument is invalid, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetResourceBundleInfo_BundleInfo_failure2() {
        bundleInfo = new BundleInfo();
        bundleInfo.setBundle(BUNDLE_NAME, Locale.ENGLISH);
        bundleInfo.setDefaultMessage("validation fails");

        // MessageKey was null
        // bundleInfo.setMessageKey("validation");
        try {
            validator.setResourceBundleInfo(bundleInfo);
            fail("Test failure for setResourceBundleInfo() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }
}
