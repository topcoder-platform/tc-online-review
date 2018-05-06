/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * <p>
 * The abstract test suite for class <code>IntegerValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public abstract class AbstractIntegerValidatorTestCases extends AbstractObjectValidatorTestCases {

    /**
     * <p>
     * A <code>String</code> represents the invalid message.
     * </p>
     */
    protected String INVALID_MESSAGE = "invalid Integer";

    /**
     * <p>
     * An instance of <code>IntegerValidator</code> for testing.<br>
     * </p>
     */
    protected IntegerValidator integerValidator = null;

    /**
     * <p>
     * Accuracy test case for method 'IntegerValidator()'.<br>
     * </p>
     */
    public void testIntegerValidator_BundleInfo_Accuracy1() {
        assertNotNull("Test accuracy for method IntegerValidator() failed.", integerValidator);
    }

    /**
     * <p>
     * Failure test case for method 'valid()'.<br>
     * The argument is a <code>String</code>, but could not be parsed to an <code>Integer</code>, validation result is
     * false.
     * </p>
     */
    public void testValid_Object_StringNotNumber() {
        assertFalse("Test failure for valid() failed, validation result should be false.",
            integerValidator.valid("12.244.t"));
    }

    /**
     * <p>
     * Failure test case for method 'getMessage()'.<br>
     * The argument is a <code>String</code>, but could not be parsed to an <code>Integer</code>, validation result is
     * false.
     * </p>
     */
    public void testGetMessage_Object_StringNotNumber() {
        assertEquals("Test failure for getMessage() failed, validation result should be false.", INVALID_MESSAGE,
            integerValidator.getMessage("12.244.t"));
    }

    /**
     * <p>
     * Failure test case for method 'getMessage()'.<br>
     * The argument is a <code>String</code>, but could not be parsed to an <code>Integer</code>, validation result is
     * false.
     * </p>
     */
    public void testGetMessage_Object_String_bundlemsg() {
        integerValidator = IntegerValidator.inRange(100, 200, bundleInfo);
        assertEquals("Test failure for getMessage() failed, validation result should be false.", "msg",
            integerValidator.getMessage("50"));
    }

    /**
     * Returns the DoubleValidator under test
     *
     * @return the DoubleValidator
     */
    public AbstractObjectValidator getObjectValidator() {
	return integerValidator;
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        // corresponding constructor not supported on IntegerValidator
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This version always causes a
     * test failure, as IntegerValidator does not provide a corresponding constructor.
     * </p>
     *
     * @param validationMessage This is the validation message to use for the underlying validator.
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator(String validationMessage) {
	fail("constructor not supported");

	return null;  // never reached
    }

}

