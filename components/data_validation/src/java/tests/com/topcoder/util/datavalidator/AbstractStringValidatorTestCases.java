/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * <p>
 * The abstract test suite for class <code>StringValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public abstract class AbstractStringValidatorTestCases extends AbstractObjectValidatorTestCases {
    /**
     * <p>
     * A <code>String</code> represents the invalid message.
     * </p>
     */
    protected String INVALID_MESSAGE = "invalid String";

    /**
     * <p>
     * An instance of <code>StringValidator</code> for testing.<br>
     * </p>
     */
    protected StringValidator validator = null;

    /**
     * <p>
     * Accuracy test case for method 'StringValidator()'.<br>
     * </p>
     */
    public void testStringValidator_BundleInfo_Accuracy1() {
        assertNotNull("Test accuracy for method StringValidator() failed.", validator);
    }

    /**
     * <p>
     * Failure test case for method 'getMessage()'.<br>
     * The argument is a <code>null</code>, validation result is false.
     * </p>
     */
    public void testGetMessage_Object_StringNull() {
        assertEquals("Test failure for getMessage() failed, validation result should be false.", "object is null",
            validator.getMessage((Object) null));
    }

    /**
     * <p>
     * Failure test case for method 'valid()'.<br> The argument is a <code>null</code>, validation result is false.
     * </p>
     */
    public void testValid_Object_StringNull() {
        assertFalse("Test failure for valid() failed, validation result should be false.",
            validator.valid((Object) null));
    }

    /**
     * <p>
     * Failure test case for method 'getMessage()'.<br>
     * The argument is a not a <code>String</code>, validation result is false.
     * </p>
     */
    public void testGetMessage_Object_NotString() {
        assertEquals("Test failure for getMessage() failed, validation result should be false.",
            "not instance of java.lang.String", validator.getMessage(new Object[0]));
    }

    /**
     * <p>
     * Failure test case for method 'valid()'.<br>
     * The argument is a not <code>String</code>, validation result is false.
     * </p>
     */
    public void testValid_Object_StringNotString() {
        assertFalse("Test failure for valid() failed, validation result should be false.",
            validator.valid(new Object[0]));
    }

    /**
     * Returns the StringValidator under test
     *
     * @return the StringValidator
     */
    public AbstractObjectValidator getObjectValidator() {
	return validator;
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        // corresponding constructor not supported on StringValidator
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This version always causes a
     * test failure, as StringValidator does not provide a corresponding constructor.
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

