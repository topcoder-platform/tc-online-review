/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;


/**
 * <p>
 * Test the functionality of class <code>OrValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class OrValidatorTestCases extends AbstractAssociativeObjectValidatorTestCases {
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
        validator = new OrValidator(validator1, validator2);
    }

    /**
     * <p>
     * Failure test case for method 'OrValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testOrValidator_ObjectValidatorObjectValidator_Null1()
        throws Exception {
        try {
            new OrValidator(null, validator2);
            fail("Test failure for OrValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'OrValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testOrValidator_ObjectValidatorObjectValidator_Null2()
        throws Exception {
        try {
            new OrValidator(validator1, null);
            fail("Test failure for OrValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'OrValidator()'.<br>
     * </p>
     */
    public void testOrValidator_ObjectValidatorObjectValidator_Accuracy1() {
        assertTrue("Test accuracy for method OrValidator() failed.",
            validator instanceof AbstractAssociativeObjectValidator);
    }

    /**
     * <p>
     * Accuracy test case for method 'OrValidator()'.<br>
     * </p>
     */
    public void testOrValidator_Accuracy1() {
        OrValidator validator = new OrValidator();
        assertTrue("Test accuracy for method OrValidator() failed.",
            validator instanceof AbstractAssociativeObjectValidator);
    }

    /**
     * <p>
     * Failure test case for method 'getMessage()'.<br> The argument is null, validation fails.
     * </p>
     */
    public void testGetMessage_Object_NullAccuracy() {
        String expected = "invalid Integer AND invalid Integer";
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
     * Accuracy test case for method 'getMessage()'.<br> Validation fail when the value is 251.
     * </p>
     */
    public void testGetMessage_Object_Accuracy2() {
        String message = validator.getMessage("251");
        assertNotNull("Test accuracy for method getMessage() failed.", message);
        assertEquals("Test accuracy for method getMessage() failed.", "not even AND not less than or equal to 200",
            message);
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
        assertFalse("Test accuracy for method getMessage() failed.", validator.valid(new Integer(251)));
    }

    /**
     * <p>
     * Failure test case for method 'getMessages()'.<br> The argument is null, validation fails.
     * </p>
     */
    public void testGetMessages_Object_NullAccuracy() {
        String expected = "invalid Integer";
        String[] str = validator.getMessages(null);
        assertEquals("Test for getMessages() failed.", 2, str.length);
        assertEquals("Test for getMessages() failed.", expected, str[0]);
        assertEquals("Test for getMessages() failed.", expected, str[1]);
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
     * Accuracy test case for method 'getMessages()'.<br> Validation fail when the value is 251.
     * </p>
     */
    public void testGetMessages_Object_Accuracy2() {
        String[] message = validator.getMessages("251");
        assertNotNull("Test accuracy for method getMessages() failed.", message);

        assertEquals("Test accuracy for method getMessages() failed.", 2, message.length);
        assertEquals("Test accuracy for method getMessages() failed.", "not even", message[0]);
        assertEquals("Test accuracy for method getMessages() failed.", "not less than or equal to 200", message[1]);
    }

    /**
     * <p>
     * Failure test case for method 'getAllMessages()'.<br> The argument is null, validation fails.
     * </p>
     */
    public void testGetAllMessages_Object_NullAccuracy() {
        String expected = "invalid Integer";
        String[] str = validator.getAllMessages(null);
        assertEquals("Test for getAllMessages() failed.", 3, str.length);
        assertEquals("Test for getAllMessages() failed.", expected, str[0]);
        assertEquals("Test for getAllMessages() failed.", expected, str[1]);
        assertEquals("Test for getAllMessages() failed.", expected, str[2]);
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
     * Accuracy test case for method 'getAllMessages()'.<br> Validation fail when the value is 251.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy2() {
        String[] message = validator.getAllMessages("251");
        assertNotNull("Test accuracy for method getAllMessages() failed.", message);
        assertEquals("Test accuracy for method getAllMessages() failed.", 2, message.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not even", message[0]);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not less than or equal to 200", message[1]);
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
     * Accuracy test case for method 'getAllMessages()'.<br> Validation fail when the value is 251.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy2() {
        String[] message = validator.getAllMessages("251", 1);
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
        String[] message = validator.getAllMessages("271", 5);
        assertNotNull("Test accuracy for method getAllMessages() failed.", message);

        assertEquals("Test accuracy for method getAllMessages() failed.", 2, message.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not even", message[0]);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not less than or equal to 200", message[1]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br> Validation fail when the value is 272.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy6() {
        validator = new OrValidator(IntegerValidator.isNegative(),
                new AndValidator(IntegerValidator.isPositive(), IntegerValidator.isOdd()));

        String[] message = validator.getAllMessages("0", 5);
        assertNotNull("Test accuracy for method getAllMessages() failed.", message);

        assertEquals("Test accuracy for method getAllMessages() failed.", 3, message.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not less than 0", message[0]);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not greater than 0", message[1]);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not odd", message[2]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br> Validation fail when the value is 272.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy7() {
        validator = new OrValidator(IntegerValidator.isNegative(),
                new AndValidator(IntegerValidator.isPositive(), IntegerValidator.isOdd()));

        String[] message = validator.getAllMessages("0", 2);
        assertNotNull("Test accuracy for method getAllMessages() failed.", message);

        assertEquals("Test accuracy for method getAllMessages() failed.", 2, message.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not less than 0", message[0]);
        assertEquals("Test accuracy for method getAllMessages() failed.", "not greater than 0", message[1]);
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
        // corresponding constructor not supported on OrValidator
    }

    /**
     * <p>
     * Creates an instance of <code>AbstractObjectValidator.</code>
     * </p>
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator() {
        return new OrValidator(validator1, validator2);
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
