/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * <p>
 * The abstract test suite for class <code>DoubleValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public abstract class AbstractDoubleValidatorTestCases extends AbstractObjectValidatorTestCases {
    /**
     * <p>
     * A <code>String</code> represents the invalid message.
     * </p>
     */
    protected String INVALID_MESSAGE = "invalid Double";

    /**
     * <p>
     * An instance of <code>DoubleValidator</code> for testing.<br>
     * </p>
     */
    protected DoubleValidator doubleValidator = null;

    /**
     * <p>
     * Accuracy test case for method 'setEpsilon()'.<br>The field 'epsilon' should be set properly.
     * </p>
     */
    public void testSetEpsilon_double_Accuracy() {
        double epsilon = 1e-10;
        doubleValidator.setEpsilon(epsilon);

        double value = doubleValidator.getEpsilon();
        assertEquals("Test accuracy for methods setEpsilon() / getEpsilon() failed, the value should be return properly.",
            epsilon, value, 1e-12);
    }

    /**
     * <p>
     * Accuracy test case for method 'DoubleValidator()'.<br>
     * </p>
     */
    public void testDoubleValidator_BundleInfo_Accuracy1() {
        assertNotNull("Test accuracy for method DoubleValidator() failed.", doubleValidator);
    }

    /**
     * <p>
     * Failure test case for method 'getMessage()'.<br>
     * The argument is a <code>String</code>, but could not be parsed to an <code>Double</code>, validation result is
     * false.
     * </p>
     */
    public void testGetMessage_Object_StringNotNumber() {
        assertEquals("Test failure for getMessage() failed, validation result should be false.", INVALID_MESSAGE,
            doubleValidator.getMessage("12.244.t"));
    }

    /**
     * <p>
     * Failure test case for method 'valid()'.<br>
     * The argument is a <code>String</code>, but could not be parsed to an <code>Double</code>, validation result is
     * false.
     * </p>
     */
    public void testValid_Object_StringNotNumber() {
        assertFalse("Test failure for valid() failed, validation result should be false.",
            doubleValidator.valid("12.244.t"));
    }

    /**
     * Returns the DoubleValidator under test
     *
     * @return the DoubleValidator
     */
    public AbstractObjectValidator getObjectValidator() {
	return doubleValidator;
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        // corresponding constructor not supported on DoubleValidator
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This version always causes a
     * test failure, as DoubleValidator does not provide a corresponding constructor.
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

