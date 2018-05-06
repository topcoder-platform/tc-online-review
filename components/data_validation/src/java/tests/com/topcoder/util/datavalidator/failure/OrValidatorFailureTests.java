/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.failure;

import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.datavalidator.OrValidator;

import junit.framework.TestCase;


/**
 * <p>
 * Test the functionality of class <code>OrValidator</code>.
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
public class OrValidatorFailureTests extends TestCase {
    /**
     * <p>
     * Represents an instance of <code>ObjectValidator</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private ObjectValidator objectValidator1 = null;

    /**
     * <p>
     * Represents an instance of <code>ObjectValidator</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private ObjectValidator objectValidator2 = null;

    /**
     * <p>
     * Represents an instance of <code>OrValidator</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private OrValidator orValidator = null;

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
        objectValidator1 = IntegerValidator.isEven();
        objectValidator2 = IntegerValidator.inRange(10, 20);
        orValidator = new OrValidator(objectValidator1, objectValidator2);
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
        objectValidator1 = null;
        objectValidator2 = null;
        orValidator = null;
        super.tearDown();
    }

    /**
     * <p>
     * Failure test case for method 'OrValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testOrValidator_ObjectValidatorObjectValidator_Null1() {
        try {
            new OrValidator(null, objectValidator2);
            fail("Test failure for OrValidator() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'OrValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testOrValidator_ObjectValidatorObjectValidator_Null2() {
        try {
            new OrValidator(objectValidator1, null);
            fail("Test failure for OrValidator() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }
}
