/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * <p>
 * The abstract test suite for class <code>LongValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public abstract class AbstractLongValidatorTestCases extends AbstractObjectValidatorTestCases {
    /**
     * <p>
     * A <code>String</code> represents the invalid message.
     * </p>
     */
    protected String INVALID_MESSAGE = "invalid Long";

    /**
     * <p>
     * An instance of <code>LongValidator</code> for testing.<br>
     * </p>
     */
    protected LongValidator longValidator = null;

    /**
     * <p>
     * Accuracy test case for method 'LongValidator()'.<br>
     * </p>
     */
    public void testLongValidator_BundleInfo_Accuracy1() {
        assertNotNull("Test accuracy for method LongValidator() failed.", longValidator);
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
            longValidator.valid("12244t"));
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
            longValidator.getMessage("12244t"));
    }

    /**
     * Returns the LongValidator under test
     *
     * @return the LongValidator
     */
    public AbstractObjectValidator getObjectValidator() {
	return longValidator;
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        // corresponding constructor not supported on LongValidator
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This version always causes a
     * test failure, as LongValidator does not provide a corresponding constructor.
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

