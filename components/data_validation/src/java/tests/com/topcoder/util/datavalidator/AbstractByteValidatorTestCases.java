/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * <p>
 * The abstract test suite for class <code>ByteValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public abstract class AbstractByteValidatorTestCases extends AbstractObjectValidatorTestCases {

    /**
     * <p>
     * A <code>String</code> represents the invalid message.
     * </p>
     */
    protected String INVALID_MESSAGE = "invalid Byte";

    /**
     * <p>
     * An instance of <code>ByteValidator</code> for testing.<br>
     * </p>
     */
    protected ByteValidator byteValidator = null;

    /**
     * <p>
     * Accuracy test case for method 'ByteValidator()'.<br>
     * </p>
     */
    public void testByteValidator_BundleInfo_Accuracy1() {
        assertNotNull("Test accuracy for method ByteValidator() failed.", byteValidator);
    }

    /**
     * <p>
     * Failure test case for method 'valid()'.<br>
     * The argument is a <code>String</code>, but could not be parsed to an <code>Byte</code>, validation result is
     * false.
     * </p>
     */
    public void testValid_Object_StringNotNumber() {
        assertFalse("Test failure for valid() failed, validation result should be false.",
            byteValidator.valid("12.244.t"));
    }

    /**
     * <p>
     * Failure test case for method 'getMessage()'.<br>
     * The argument is a <code>String</code>, but could not be parsed to an <code>Byte</code>, validation result is
     * false.
     * </p>
     */
    public void testGetMessage_Object_StringNotNumber() {
        assertEquals("Test failure for getMessage() failed, validation result should be false.", INVALID_MESSAGE,
            byteValidator.getMessage("12.244.t"));
    }

    /**
     * Returns the ByteValidator under test
     *
     * @return the ByteValidator
     */
    public AbstractObjectValidator getObjectValidator() {
	return byteValidator;
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        // corresponding constructor not supported on ByteValidator
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This version always causes a
     * test failure, as ByteValidator does not provide a corresponding constructor.
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
