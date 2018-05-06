/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.failure;

import com.topcoder.util.datavalidator.StringValidator;


/**
 * <p>
 * Test the functionality of class <code>StringValidator</code>.
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
public class StringValidatorFailureTests extends AbstractObjectValidatorFailureTests {
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
        validator = StringValidator.startsWith("abc");
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
        super.tearDown();
    }

    /**
     * <p>
     * Failure test case for method 'endsWith()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testEndsWith_String_Null1() {
        try {
            StringValidator.endsWith(null);
            fail("Test failure for endsWith() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'startsWith()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testStartsWith_String_Null1() {
        try {
            StringValidator.startsWith(null);
            fail("Test failure for startsWith() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'startsWith()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testStartsWith_StringBundleInfo_Null1() {
        try {
            StringValidator.startsWith(null, bundleInfo);
            fail("Test failure for startsWith() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'startsWith()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testStartsWith_StringBundleInfo_Null2() {
        try {
            StringValidator.startsWith("str", null);
            fail("Test failure for startsWith() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'matchesRegexp()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testMatchesRegexp_String_Null1() {
        try {
            StringValidator.matchesRegexp(null);
            fail("Test failure for matchesRegexp() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'containsSubstring()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testContainsSubstring_String_Null1() {
        try {
            StringValidator.containsSubstring(null);
            fail("Test failure for containsSubstring() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'hasLength()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testHasLength_IntegerValidator_Null1() {
        try {
            StringValidator.hasLength(null);
            fail("Test failure for hasLength() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }
}
