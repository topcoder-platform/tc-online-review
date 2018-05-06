/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * <p>
 * Test the functionality of class <code>BooleanValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class BooleanValidatorTestCases extends AbstractObjectValidatorTestCases {
    /**
     * <p>
     * An instance of <code>BooleanValidator</code> for testing.<br>
     * </p>
     */
    private BooleanValidator booleanValidator = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        booleanValidator = new MockBooleanValidator(bundleInfo);
    }

    /**
     * <p>
     * Failure test case for method 'BooleanValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testBooleanValidator_BundleInfo_Null1() {
        try {
            new MockBooleanValidator(null);
            fail("Test failure for BooleanValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'BooleanValidator()'.<br>
     * </p>
     */
    public void testBooleanValidator_BundleInfo_Accuracy1() {
        assertTrue("Test accuracy for method BooleanValidator() failed.", booleanValidator instanceof ObjectValidator);
    }

    /**
     * <p>
     * Accuracy test case for method 'BooleanValidator()'.<br>
     * </p>
     */
    public void testBooleanValidator_Accuracy1() {
        booleanValidator = new MockBooleanValidator();
        assertTrue("Test accuracy for method BooleanValidator() failed.", booleanValidator instanceof ObjectValidator);
    }

    /**
     * <p>
     * Failure test case for method 'getMessage()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testGetMessage_Object_Null1() {
        try {
            booleanValidator.getMessage(null);

            // Success
        } catch (IllegalArgumentException iae) {
            fail("Test failure for getMessage() failed, IllegalArgumentException should be thrown.");
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>The value of field 'message' should be return properly.
     * </p>
     */
    public void testGetMessage_Object_Accuracy() {
        booleanValidator.getMessage(false);
        booleanValidator.getMessage(true);
        booleanValidator.valid(true);
        booleanValidator.valid(false);
        booleanValidator.valid("true");
        booleanValidator.valid("false");
        booleanValidator.valid("something");
        booleanValidator.getAllMessages("true");
        booleanValidator.getAllMessages("false");
        booleanValidator.getMessages("true");
        booleanValidator.getMessages("false");
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        // corresponding constructor not supported on BooleanValidator
    }

    /**
     * Returns the BooleanValidator under test
     *
     * @return the BooleanValidator
     */
    public AbstractObjectValidator getObjectValidator() {
        return booleanValidator;
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This version always causes a test failure, as
     * BooleanValidator does not provide a corresponding constructor.
     * </p>
     *
     * @param validationMessage This is the validation message to use for the underlying validator.
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator(String validationMessage) {
        fail("constructor not supported");

        return null; // never reached
    }

    /**
     * <p>
     * Creates an instance of <code>AbstractObjectValidator.</code>
     * </p>
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator() {
        return new MockBooleanValidator();    	
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>
     * </p>
     *
     * @param bundleInfo name of the bundle to use
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator(BundleInfo bundleInfo) {
        return new MockBooleanValidator(bundleInfo);
    }

    /**
     * <p>
     * mock implementation of BooleanValidator.
     * </p>
     *
     * @author telly12
     * @version 1.1
     */
    static class MockBooleanValidator extends BooleanValidator {
        /**
         * <p>
         * mock implementation.
         * </p>
         *
         * @param info the bundle information
         */
        private MockBooleanValidator(BundleInfo info) {
            super(info);
        }

        /**
         * <p>
         * mock implementation.
         * </p>
         */
        public MockBooleanValidator() {
            super();
        }

        /**
         * <p>
         * mock implementation.
         * </p>
         *
         * @param value the value to be validated
         *
         * @return true if the value is true, otherwise false
         */
        public boolean valid(boolean value) {
            return value;
        }

        /**
         * <p>
         * mock implementation.
         * </p>
         *
         * @param value the value to be validated
         *
         * @return the validation message
         */
        public String getMessage(boolean value) {
            if (valid(value)) {
                return null;
            }

            // get the message from resource bundle if it exists.
            String message = this.getValidationMessage();

            if (message != null) {
                return message;
            }

            return "invalid boolean";
        }
    }
}
