/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * <p>
 * The abstract test suite for class <code>ShortValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public abstract class AbstractShortValidatorTestCases extends AbstractObjectValidatorTestCases {
    /**
     * <p>
     * A <code>String</code> represents the invalid message.
     * </p>
     */
    protected String INVALID_MESSAGE = "invalid Short";

    /**
     * <p>
     * An instance of <code>ShortValidator</code> for testing.<br>
     * </p>
     */
    protected ShortValidator shortValidator = null;

    /**
     * <p>
     * Accuracy test case for method 'ShortValidator()'.<br>
     * </p>
     */
    public void testShortValidator_BundleInfo_Accuracy1() {
        assertNotNull("Test accuracy for method ShortValidator() failed.", shortValidator);
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
            shortValidator.valid("12.244.t"));
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
            shortValidator.getMessage("12.244.t"));
    }

    /**
     * Returns the ShortValidator under test
     *
     * @return the ShortValidator
     */
    public AbstractObjectValidator getObjectValidator() {
        return shortValidator;
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        // corresponding constructor not supported on ShortValidator
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This version always causes a
     * test failure, as ShortValidator does not provide a corresponding constructor.
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
