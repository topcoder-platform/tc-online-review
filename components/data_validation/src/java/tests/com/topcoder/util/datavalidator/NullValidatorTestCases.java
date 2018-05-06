/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;


/**
 * <p>
 * Test the functionality of class <code>NullValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class NullValidatorTestCases extends AbstractObjectValidatorTestCases {
    /**
     * <p>
     * A String represents the error message.
     * </p>
     */
    private static final String ERROR_MESSAGE = "not null";

    /**
     * <p>
     * An instance of <code>NullValidator</code> for testing.<br>
     * </p>
     */
    private NullValidator nullValidator = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        nullValidator = new NullValidator();
    }

    /**
     * <p>
     * Accuracy test case for method 'NullValidator()'.<br>
     * </p>
     */
    public void testNullValidator_Accuracy1() {
        assertTrue("Test accuracy for method NullValidator() failed.", nullValidator instanceof ObjectValidator);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_Object_Accuracy1() {
        String value = nullValidator.getMessage(null);
        assertNull("Test accuracy for method getMessage() failed, the value should be return properly.", value);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_Object_Accuracy2() {
        String value = nullValidator.getMessage(new Object[0]);
        assertEquals("Test accuracy for method getMessage() failed, the value should be return properly.",
            ERROR_MESSAGE, value);
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_Object_Accuracy1() {
        assertTrue("Test accuracy for method NullValidator() failed.", nullValidator.valid(null));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_Object_Accuracy2() {
        assertFalse("Test accuracy for method NullValidator() failed.", nullValidator.valid(new Object[0]));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * </p>
     */
    public void testGetMessages_Object_Accuracy1() {
        String[] msgs = nullValidator.getMessages(null);

        assertNull("Test accuracy for method getMessages() failed.", msgs);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * </p>
     */
    public void testGetMessages_Object_Accuracy2() {
        String[] msgs = nullValidator.getMessages(new Object[0]);

        assertEquals("Test accuracy for method getMessages() failed.", 1, msgs.length);
        assertEquals("Test accuracy for method getMessages() failed.", ERROR_MESSAGE, msgs[0]);
    }

    /**
     * <p>
     * Failure test case for method 'getAllMessages()'.<br>
     * The argument is invalid, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testGetAllMessages_Objectint_IllegalArgumentException() {
        try {
            nullValidator.getAllMessages(null, 0);
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
        String[] msgs = nullValidator.getAllMessages(null, 1);

        assertNull("Test accuracy for method getMessages() failed.", msgs);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * </p>
     */
    public void testGetAllMessages_Objectint_Accuracy2() {
        String[] msgs = nullValidator.getAllMessages(new Object[0], 1);

        assertEquals("Test accuracy for method getMessages() failed.", 1, msgs.length);
        assertEquals("Test accuracy for method getMessages() failed.", ERROR_MESSAGE, msgs[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy1() {
        String[] msgs = nullValidator.getAllMessages(null);

        assertNull("Test accuracy for method getMessages() failed.", msgs);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy2() {
        String[] msgs = nullValidator.getAllMessages(new Object[0]);

        assertEquals("Test accuracy for method getMessages() failed.", 1, msgs.length);
        assertEquals("Test accuracy for method getMessages() failed.", ERROR_MESSAGE, msgs[0]);
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        // corresponding constructor not supported on NullValidator
    }

    /**
     * Returns the TypeValidator under test
     *
     * @return the TypeValidator
     */
    public AbstractObjectValidator getObjectValidator() {
	return nullValidator;
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This version always causes a
     * test failure, as NullValidator does not provide a corresponding constructor.
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
        return new NullValidator();
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
        return new NullValidator(bundleInfo);
    }
}
