/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

/**
 * <p>
 * Test the functionality of class <code>AndValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class AndValidatorTestCases extends AbstractAssociativeObjectValidatorTestCases {
    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        validator1 = IntegerValidator.isEven();
        validator2 = IntegerValidator.inRange(100, 200);
        validator = new AndValidator(validator1, validator2);
    }

    /**
     * <p>
     * Failure test case for method 'AndValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testAndValidator_ObjectValidatorObjectValidator_Null1()
        throws Exception {
        try {
            new AndValidator(null, validator2);
            fail("Test failure for AndValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'AndValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testAndValidator_ObjectValidatorObjectValidator_Null2()
        throws Exception {
        try {
            new AndValidator(validator1, null);
            fail("Test failure for AndValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'AndValidator()'.<br>
     * </p>
     */
    public void testAndValidator_ObjectValidatorObjectValidator_Accuracy1() {
        assertTrue("Test accuracy for method AndValidator() failed.",
            validator instanceof AbstractAssociativeObjectValidator);
    }

    /**
     * <p>
     * Accuracy test case for method 'AndValidator()'.<br>
     * </p>
     */
    public void testAndValidator_Accuracy1() {
        AndValidator andValidator = new AndValidator();
        assertTrue("Test accuracy for method AndValidator() failed.",
            andValidator instanceof AbstractAssociativeObjectValidator);
    }

    /**
     * <p>
     * Failure test case for method 'getMessage()'.<br> The argument is null, validation fails.
     * </p>
     */
    public void testGetMessage_Object_NullAccuracy() {
        String expected = "invalid Integer";
        String str = validator.getMessage(null);
        assertEquals("Test for getMessage() failed.", expected, str);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br> Validation success when the value is 150.
     * </p>
     */
    public void testGetMessage_Object_Accuracy1() {
        assertNull("Test accuracy for method getMessage() failed.", validator.getMessage(new Integer(150)));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br> Validation fail when the value is 151.
     * </p>
     */
    public void testGetMessage_Object_Accuracy2() {
        String message = validator.getMessage("151");
        assertNotNull("Test accuracy for method getMessage() failed.", message);
        assertEquals("Test accuracy for method getMessage() failed.", "not even", message);
    }

    /**
     * <p>
     * Failure test case for method 'valid()'.<br> The argument is null, validation fails.
     * </p>
     */
    public void testValid_Object_Null1() {
        assertFalse("Test failure for valid() failed.", validator.valid(null));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_Object_Accuracy1() {
        assertTrue("Test accuracy for method getMessage() failed.", validator.valid(new Integer(150)));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_Object_Accuracy2() {
        assertFalse("Test accuracy for method getMessage() failed.", validator.valid(new Integer(151)));
    }

    /**
     * <p>
     * Failure test case for method 'getMessages()'.<br> The argument is null, validation fails.
     * </p>
     */
    public void testGetMessages_Object_NullAccuracy() {
        String expected = "invalid Integer";
        String[] str = validator.getMessages(null);
        assertEquals("Test for getMessages() failed.", expected, str[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br> Validation success when the value is 150.
     * </p>
     */
    public void testGetMessages_Object_Accuracy1() {
        assertNull("Test accuracy for method getMessages() failed.", validator.getMessages(new Integer(150)));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br> Validation fail when the value is 151.
     * </p>
     */
    public void testGetMessages_Object_Accuracy2() {
        String[] message = validator.getMessages("51");
        assertNotNull("Test accuracy for method getMessages() failed.", message);

        assertEquals("Test accuracy for method getMessages() failed.", 1, message.length);
        assertEquals("Test accuracy for method getMessages() failed.", "not even", message[0]);
    }

    /**
     * <p>
     * Failure test case for method 'getAllMessages()'.<br> The argument is null, validation fails.
     * </p>
     */
    public void testGetAllMessages_Object_NullAccuracy() {
        String expected = "invalid Integer";
        String[] str = validator.getAllMessages(null);
        assertEquals("Test for getAllMessages() failed.", expected, str[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br> Validation success when the value is 150.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy1() {
        assertNull("Test accuracy for method getAllMessages() failed.", validator.getAllMessages(new Integer(150)));
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br> Validation fail when the value is 151.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy2() {
        String[] message = validator.getAllMessages("151");
        assertNotNull("Test accuracy for method getAllMessages() failed.", message);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not even", message[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br> Validation fail when the value is 51.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy3() {
        String[] message = validator.getAllMessages("51");
        assertNotNull("Test accuracy for method getAllMessages() failed.", message);

        assertEquals("Test accuracy for method getAllMessages() failed.", 2, message.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not even", message[0]);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not greater than or equal to 100",
            message[1]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br> Validation success when the value is 150.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy1() {
        assertNull("Test accuracy for method getAllMessages() failed.", validator.getAllMessages(new Integer(150)));
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br> Validation fail when the value is 151.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy2() {
        String[] message = validator.getAllMessages("151", 100);
        assertNotNull("Test accuracy for method getAllMessages() failed.", message);
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, message.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not even", message[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br> Validation fail when the value is 51.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy3() {
        String[] message = validator.getAllMessages("51", 100);
        assertNotNull("Test accuracy for method getAllMessages() failed.", message);

        assertEquals("Test accuracy for method getAllMessages() failed.", 2, message.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not even", message[0]);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not greater than or equal to 100",
            message[1]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br> Validation fail when the value is 51.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy4() {
        String[] message = validator.getAllMessages("51", 1);
        assertNotNull("Test accuracy for method getAllMessages() failed.", message);

        assertEquals("Test accuracy for method getAllMessages() failed.", 1, message.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not even", message[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br> Validation fail when the value is 51.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy5() {
        String[] message = validator.getAllMessages("270", 5);
        assertNotNull("Test accuracy for method getAllMessages() failed.", message);

        assertEquals("Test accuracy for method getAllMessages() failed.", 1, message.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not less than or equal to 200", message[0]);
    }

    /**
     * <p>
     * Failure test case for method 'getAllMessages()'.<br>
     * The argument is invalid, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testGetAllMessages_Objectint_IllegalArgumentException() {
        try {
            validator.getAllMessages(new Integer(60), -1);
            fail("Test failure for getAllMessages() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(BundleInfo)'
     */
    public void testAbstractObjectValidatorBundleInfo() {
        // corresponding constructor not supported on AndValidator
    }

    /**
     * <p>
     * Creates an instance of <code>AbstractObjectValidator.</code>
     * </p>
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator() {
        return new AndValidator(validator1, validator2);
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
        fail("appropriate constructor not supported");
        return null;
    }
}
