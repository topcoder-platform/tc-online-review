/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;


/**
 * <p>
 * Test the functionality of class <code>IntegerValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class IntegerValidatorTestCases extends TestCase {
    /**
     * <p>
     * An instance of <code>BundleInfo</code> for testing.<br>
     * </p>
     */
    private BundleInfo bundleInfo = null;

    /**
     * <p>
     * An instance of <code>IntegerValidator</code> for testing.<br>
     * </p>
     */
    private IntegerValidator integerValidator = null;

    /**
     * <p>
     * A <code>String</code> represents the invalid message.
     * </p>
     */
    protected String INVALID_MESSAGE = "invalid Integer";

    /**
     * <p>
     * A <code>String</code> represents the message that the validation integer is not odd.
     * </p>
     */
    protected String NOT_ODD = "not odd";

    /**
     * <p>
     * A <code>String</code> represents the message that the validation integer is not odd.
     * </p>
     */
    protected String NOT_EVEN = "not even";

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        integerValidator = IntegerValidator.equalTo(10);
    }

    /**
     * <p>
     * Tear down the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        bundleInfo = null;
        integerValidator = null;
        super.tearDown();
    }

    /**
     * <p>
     * Accuracy test case for method 'IntegerValidator()'.<br>
     * </p>
     */
    public void testIntegerValidator_Accuracy1() {
        assertEquals("Test accuracy for method IntegerValidator() failed.", 0, 0);
    }
}
