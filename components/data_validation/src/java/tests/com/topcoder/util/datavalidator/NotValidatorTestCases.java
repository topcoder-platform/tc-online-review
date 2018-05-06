/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;


/**
 * <p>
 * Test the functionality of class <code>NotValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class NotValidatorTestCases extends AbstractObjectValidatorTestCases {
    /**
     * <p>
     * Represents the error message.
     * </p>
     */
    private static final String ERROR_MESSAGE = "NOT failure";

    /**
     * <p>
     * An instance of <code>ObjectValidator</code> for testing.<br>
     * </p>
     */
    private ObjectValidator objectValidator = null;

    /**
     * <p>
     * An instance of <code>NotValidator</code> for testing.<br>
     * </p>
     */
    private NotValidator notValidator = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        objectValidator = IntegerValidator.inRange(100, 200);
        notValidator = new NotValidator(objectValidator);
    }

    /**
     * <p>
     * Failure test case for method 'NotValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testNotValidator_ObjectValidatorBundleInfo_Null1()
        throws Exception {
        try {
            new NotValidator(null);
            fail("Test failure for NotValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'NotValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testNotValidator_ObjectValidatorBundleInfo_Null2()
        throws Exception {
        try {
            new NotValidator(objectValidator, null);
            fail("Test failure for NotValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'NotValidator()'.<br>
     * </p>
     */
    public void testNotValidator_ObjectValidatorBundleInfo_Accuracy1() {
        assertTrue("Test accuracy for method NotValidator() failed.", notValidator instanceof ObjectValidator);
    }

    /**
     * <p>
     * Failure test case for method 'NotValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testNotValidator_ObjectValidator_Null1()
        throws Exception {
        try {
            new NotValidator(null);
            fail("Test failure for NotValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'NotValidator()'.<br>
     * </p>
     */
    public void testNotValidator_ObjectValidator_Accuracy1() {
        assertTrue("Test accuracy for method NotValidator() failed.",
            new NotValidator(objectValidator) instanceof ObjectValidator);
    }

    /**
     * <p>
     * Failure test case for method 'getMessage()'.<br> The argument is null, validation fails.
     * </p>
     */
    public void testGetMessage_Object_Null1() {
        String msg = notValidator.getMessage(null);
        assertNull("Test for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_Object_Accuracy1() {
        String msg = notValidator.getMessage(new Integer(150));
        assertEquals("Test for method getMessage() failed.", ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br> Validation success if the number is 30
     * </p>
     */
    public void testGetMessage_Object_Accuracy2() {
        String msg = notValidator.getMessage(new Integer(30));
        assertNull("Test for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Failure test case for method 'valid()'.<br> The argument is null, validation fails.
     * </p>
     */
    public void testValid_Object_Null1() {
        assertTrue("Test for method valid() failed.", notValidator.valid(null));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_Object_Accuracy1() {
        assertTrue("Test accuracy for method valid() failed.", notValidator.valid("99"));
        assertTrue("Test accuracy for method valid() failed.", notValidator.valid("201"));
        assertFalse("Test accuracy for method valid() failed.", notValidator.valid("150"));
        assertFalse("Test accuracy for method valid() failed.", notValidator.valid("200"));
        assertFalse("Test accuracy for method valid() failed.", notValidator.valid("100"));
    }

    /**
     * <p>
     * Failure test case for method 'getMessages()'.<br> The argument is null, validation fails.
     * </p>
     */
    public void testGetMessages_Object_Null1() {
        String[] msgs = notValidator.getMessages(null);

        assertNull("Test for method getAllMessages() failed.", msgs);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>The value of field 'messages' should be return properly.
     * </p>
     */
    public void testGetMessages_Object_Accuracy() {
        String[] msgs = notValidator.getMessages(new Integer(150));

        assertEquals("Test for method getMessages() failed.", 1, msgs.length);
        assertEquals("Test for method getMessages() failed.", ERROR_MESSAGE, msgs[0]);
    }

    /**
     * <p>
     * Failure test case for method 'getAllMessages()'.<br>
     * The argument is invalid, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testGetAllMessages_Objectint_IllegalArgumentException1() {
        try {
            notValidator.getAllMessages("165", -1);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'getAllMessages()'.<br>
     * </p>
     */
    public void testGetAllMessages_Objectint_nullAccuracy() {
        String[] msgs = notValidator.getAllMessages(null);

        assertNull("Test for method getAllMessages() failed.", msgs);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>The value of field 'allMessages' should be return properly.
     * </p>
     */
    public void testGetAllMessages_Objectint_Accuracy() {
        String[] msgs = notValidator.getAllMessages(new Integer(150), 3);

        assertEquals("Test for method getAllMessages() failed.", 1, msgs.length);
        assertEquals("Test for method getAllMessages() failed.", ERROR_MESSAGE, msgs[0]);
    }

    /**
     * <p>
     * Failure test case for method 'getAllMessages()'.<br> The argument is null, validation fails.
     * </p>
     */
    public void testGetAllMessages_Objectint_Null1() {
        String[] msgs = notValidator.getAllMessages(null, 150);

        assertNull("Test for method getAllMessages() failed.", msgs);
    }

    /**
     * <p>
     * Creates an instance of <code>AbstractObjectValidator.</code>
     * </p>
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator() {
        return new NotValidator(objectValidator);
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        // corresponding constructor not supported on NotValidator
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This version always causes a
     * test failure, as AbstractAssociativeObjectValidator does not provide a corresponding constructor.
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
     * Create an instance of <code>AbstractObjectValidator.</code>
     * </p>
     *
     * @param bundleInfo name of the bundle to use
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator(BundleInfo bundleInfo) {
        return new NotValidator(objectValidator, bundleInfo);
    }

    /**
     * <p>
     * Get the <code>AbstractObjectValidator</code> instance.<br>
     * </p>
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator getObjectValidator() {
        return notValidator;
    }
}
