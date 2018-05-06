/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import java.util.Locale;

import junit.framework.TestCase;


/**
 * <p>
 * The abstract test suite for class <code>FloatValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public abstract class AbstractFloatValidatorTestCases extends AbstractObjectValidatorTestCases {
    /**
     * <p>
     * A <code>String</code> represents the invalid message.
     * </p>
     */
    protected String INVALID_MESSAGE = "invalid Float";

    /**
     * <p>
     * An instance of <code>FloatValidator</code> for testing.<br>
     * </p>
     */
    protected FloatValidator floatValidator = null;

    /**
     * <p>
     * Accuracy test case for method 'setEpsilon()'.<br>The field 'epsilon' should be set properly.
     * </p>
     */
    public void testSetEpsilon_float_Accuracy() {
        float epsilon = (float) 1e-6;
        floatValidator.setEpsilon(epsilon);

        Float value = (Float) TestHelper.getPrivateField(FloatValidator.class, floatValidator, "eps");
        assertEquals("Test accuracy for method setEpsilon() failed, the value should be set properly.", epsilon,
            value.floatValue(), 1e-12);
    }

    /**
     * <p>
     * Accuracy test case for method 'getEpsilon()'.<br>The value of field 'epsilon' should be return properly.
     * </p>
     */
    public void testGetEpsilon_Accuracy() {
        float epsilon = (float) 1e-6;
        TestHelper.setPrivateField(FloatValidator.class, floatValidator, "eps", new Float(epsilon));

        float value = (float) floatValidator.getEpsilon();
        assertEquals("Test accuracy for method getEpsilon() failed, the value should be return properly.", epsilon,
            value, 1e-12);
    }

    /**
     * <p>
     * Accuracy test case for method 'FloatValidator()'.<br>
     * </p>
     */
    public void testFloatValidator_BundleInfo_Accuracy1() {
        assertNotNull("Test accuracy for method FloatValidator() failed.", floatValidator);
    }

    /**
     * <p>
     * Failure test case for method 'getMessage()'.<br>
     * The argument is a <code>String</code>, but could not be parsed to an <code>Float</code>, validation result is
     * false.
     * </p>
     */
    public void testGetMessage_Object_StringNotNumber() {
        assertEquals("Test failure for getMessage() failed, validation result should be false.", INVALID_MESSAGE,
            floatValidator.getMessage("12.244.t"));
    }

    /**
     * <p>
     * Failure test case for method 'valid()'.<br>
     * The argument is a <code>String</code>, but could not be parsed to an <code>Float</code>, validation result is
     * false.
     * </p>
     */
    public void testValid_Object_StringNotNumber() {
        assertFalse("Test failure for valid() failed, validation result should be false.",
            floatValidator.valid("12.244.t"));
    }
    /**
     * <p>
     * Failure test case for method 'valid()'.<br>
     * The argument is a <code>String</code>, but could not be parsed to an <code>Double</code>, validation result is
     * false.
     * </p>
     */
    public void testValid_Object() {
    	ObjectValidator validator = new AndValidator(FloatValidator.isInteger(), FloatValidator.inRange(10, 20));
    	floatValidator = new FloatValidator.FloatValidatorWrapper(validator);    	
    	assertFalse("Test failure for valid() failed, validation result should be false.",
                floatValidator.valid("12.244"));
        floatValidator = new FloatValidator.FloatValidatorWrapper(validator, bundleInfo);
        floatValidator.valid((float)12.34);
        floatValidator.getMessage("123.453");
        floatValidator.getMessages("123.453");
        floatValidator.getAllMessages("123.453");
    }

    /**
     * Returns the FloatValidator under test
     *
     * @return the FloatValidator
     */
    public AbstractObjectValidator getObjectValidator() {
	return floatValidator;
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        // corresponding constructor not supported on FloatValidator
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

