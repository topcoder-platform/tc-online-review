/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * <p>
 * Test the functionality of class <code>TypeValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class TypeValidatorTestCases extends AbstractObjectValidatorTestCases {
    /**
     * <p>
     * An instance of <code>ObjectValidator</code> for testing.<br>
     * </p>
     */
    private ObjectValidator objectValidator = null;

    /**
     * <p>
     * An instance of <code>TypeValidator</code> for testing.<br>
     * </p>
     */
    private TypeValidator typeValidator = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        objectValidator = IntegerValidator.greaterThan(10);

        typeValidator = new TypeValidator(objectValidator, Integer.class);
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is invalid, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_Class_IllegalArgumentException() {
        try {
            new TypeValidator(byte.class);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testTypeValidator_Class_Null1() throws Exception {
        try {
            new TypeValidator(null);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'TypeValidator()'.<br>
     * </p>
     */
    public void testTypeValidator_Class_Accuracy1() {
        assertTrue("Test accuracy for method TypeValidator() failed.",
            new TypeValidator(String.class) instanceof ObjectValidator);
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is invalid, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_ClassBundleInfo_IllegalArgumentException() {
        try {
            new TypeValidator(byte.class, bundleInfo);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testTypeValidator_ClassBundleInfo_Null1()
        throws Exception {
        try {
            new TypeValidator(null, bundleInfo);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testTypeValidator_ClassBundleInfo_Null2()
        throws Exception {
        try {
            new TypeValidator(String.class, null);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'TypeValidator()'.<br>
     * </p>
     */
    public void testTypeValidator_ClassBundleInfo_Accuracy() {
        assertTrue("Test accuracy for method TypeValidator() failed.",
            new TypeValidator(Integer.class, bundleInfo) instanceof AbstractObjectValidator);
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is invalid, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_ObjectValidatorClass_IllegalArgumentException() {
        try {
            new TypeValidator(objectValidator, byte.class);

            fail("Test failure for TypeValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testTypeValidator_ObjectValidatorClass_Null1()
        throws Exception {
        try {
            new TypeValidator(null, String.class);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testTypeValidator_ObjectValidatorClass_Null2()
        throws Exception {
        try {
            new TypeValidator(objectValidator, null);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is invalid, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_ObjectValidatorClassBundleInfo_IllegalArgumentException() {
        try {
            new TypeValidator(objectValidator, byte.class, bundleInfo);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testTypeValidator_ObjectValidatorClassBundleInfo_Null1()
        throws Exception {
        try {
            new TypeValidator(null, String.class, bundleInfo);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testTypeValidator_ObjectValidatorClassBundleInfo_Null2()
        throws Exception {
        try {
            new TypeValidator(objectValidator, null, bundleInfo);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testTypeValidator_ObjectValidatorClassBundleInfo_Null3()
        throws Exception {
        try {
            new TypeValidator(objectValidator, String.class, null);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'TypeValidator()'.<br>
     * </p>
     */
    public void testTypeValidator_ObjectValidatorClassBundleInfo_Accuracy1() {
        assertTrue("Test accuracy for method TypeValidator() failed.",
            new TypeValidator(objectValidator, Integer.class, bundleInfo) instanceof ObjectValidator);
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br> The argument is null, validation result is false.
     * </p>
     */
    public void testValid_Object_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", typeValidator.valid(null));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br> The argument is not Integer type, validation result is false.
     * </p>
     */
    public void testValid_Object_Accuracy2() {
        assertFalse("Test accuracy for method valid() failed.", typeValidator.valid("TopCoder"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br> The argument is less than 10, validation result is false.
     * </p>
     */
    public void testValid_Object_Accuracy3() {
        assertFalse("Test accuracy for method valid() failed.", typeValidator.valid(new Integer(9)));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br> The argument is equal to 10, validation result is false.
     * </p>
     */
    public void testValid_Object_Accuracy4() {
        assertFalse("Test accuracy for method valid() failed.", typeValidator.valid(new Integer(10)));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br> The argument is greater than 10, validation result is true.
     * </p>
     */
    public void testValid_Object_Accuracy5() {
        assertTrue("Test accuracy for method valid() failed.", typeValidator.valid(new Integer(11)));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br> The argument is negative, validation result is false.
     * </p>
     */
    public void testValid_Object_Accuracy6() {
        assertFalse("Test accuracy for method valid() failed.", typeValidator.valid(new Integer(-10)));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * </p>
     */
    public void testGetMessages_Object_Accuracy1() {
        String[] msgs = typeValidator.getMessages(new Integer(8));
        assertEquals("Test accuracy for method getAllMessages() failed, the value should be return properly.", 1,
            msgs.length);
        assertEquals("Test accuracy for method getAllMessages() failed, the value should be return properly.",
            "not greater than 10", msgs[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * </p>
     */
    public void testGetMessages_Object_Accuracy2() {
        String[] msgs = typeValidator.getMessages(new Integer(32));
        assertNull("Test accuracy for method getAllMessages() failed, the value should be return properly.", msgs);
    }

    /**
     * <p>
     * Failure test case for method 'getAllMessages()'.<br>
     * The argument is invalid, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testGetAllMessages_Objectint_IllegalArgumentException() {
        try {
            typeValidator.getAllMessages(null, -1);
            fail("Test failure for getAllMessages() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * </p>
     */
    public void testGetAllMessages_Objectint_Accuracy1() {
        String[] msgs = typeValidator.getAllMessages(new Integer(32), 1);
        assertNull("Test accuracy for method getAllMessages() failed, the value should be return properly.", msgs);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * </p>
     */
    public void testGetAllMessages_Objectint_Accuracy2() {
        String[] msgs = typeValidator.getAllMessages(new Integer(8), 1);
        assertEquals("Test accuracy for method getAllMessages() failed, the value should be return properly.", 1,
            msgs.length);
        assertEquals("Test accuracy for method getAllMessages() failed, the value should be return properly.",
            "not greater than 10", msgs[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy1() {
        String[] msgs = typeValidator.getAllMessages(new Integer(32));
        assertNull("Test accuracy for method getAllMessages() failed, the value should be return properly.", msgs);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy2() {
        String[] msgs = typeValidator.getAllMessages(new Integer(8));
        assertEquals("Test accuracy for method getAllMessages() failed, the value should be return properly.", 1,
            msgs.length);
        assertEquals("Test accuracy for method getAllMessages() failed, the value should be return properly.",
            "not greater than 10", msgs[0]);
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        // corresponding constructor not supported on TypeValidator
    }

    /**
     * Returns the TypeValidator under test
     *
     * @return the TypeValidator
     */
    public AbstractObjectValidator getObjectValidator() {
	return typeValidator;
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This version always causes a
     * test failure, as TypeValidator does not provide a corresponding constructor.
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

    /**
     * <p>
     * Creates an instance of <code>AbstractObjectValidator.</code>
     * </p>
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator() {
        return new TypeValidator(Integer.class);
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
        return new TypeValidator(Integer.class, bundleInfo);
    }

}
