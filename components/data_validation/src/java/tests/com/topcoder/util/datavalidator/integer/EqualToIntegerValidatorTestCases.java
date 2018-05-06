/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.integer;

import com.topcoder.util.datavalidator.AbstractIntegerValidatorTestCases;
import com.topcoder.util.datavalidator.AbstractObjectValidator;
import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.IntegerValidator;


/**
 * <p>
 * Test the functionality of class <code>IntegerValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class EqualToIntegerValidatorTestCases extends AbstractIntegerValidatorTestCases {
    /**
     * <p>
     * A <code>String</code> represents the error message when the validation fails.
     * </p>
     */
    private static final String ERROR_MESSAGE = "not equal to 100";

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        integerValidator = IntegerValidator.equalTo(100, bundleInfo);
        integerValidator = IntegerValidator.equalTo(100);
    }

    /**
     * <p>
     * Tear down the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>
     * Failure test case for method 'IntegerValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testIntegerValidator_BundleInfo_Null1() {
        try {
            integerValidator.equalTo(5, null);
            fail("Test failure for IntegerValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'IntegerValidator()'.<br>
     * </p>
     */
    public void testIntegerValidator_BundleInfo_Accuracy() {
        integerValidator.equalTo(5, bundleInfo);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is not equal to 100.
     * </p>
     */
    public void testGetMessage_Object_Accuracy1() {
        String msg = integerValidator.getMessage("1023");
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>The result should be successful only when the number is 100.
     * </p>
     */
    public void testGetMessage_Object_Accuracy2() {
        String msg = integerValidator.getMessage("100");
        assertNull("Test accuracy for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is not equal to 100.
     * </p>
     */
    public void testGetMessage_int_Accuracy1() {
        String msg = integerValidator.getMessage(1023);
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br> The result should be successful only when the number is 100.
     * </p>
     */
    public void testGetMessage_int_Accuracy2() {
        String msg = integerValidator.getMessage(100);
        assertNull("Test accuracy for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is not equal to 100.
     * </p>
     */
    public void testValid_Object_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", integerValidator.valid("1023"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br> The result should be successful only when the number is 100.
     * </p>
     */
    public void testValid_Object_Accuracy2() {
        assertTrue("Test accuracy for method valid() failed.", integerValidator.valid("100"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * <br>
     * The result should be unsuccessful when the number is not equal to 100.
     * </p>
     */
    public void testValid_int_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", integerValidator.valid(1023));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br> The result should be successful only when the number is 100.
     * </p>
     */
    public void testValid_int_Accuracy2() {
        assertTrue("Test accuracy for method valid() failed.", integerValidator.valid(100));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be unsuccessful when the number is not equal to 100.
     * </p>
     */
    public void testGetMessages_Object_Accuracy1() {
        String[] msg = integerValidator.getMessages("1023");
        assertEquals("Test accuracy for method getMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br> The result should be successful only when the number is 100.
     * </p>
     */
    public void testGetMessages_Object_Accuracy2() {
        String[] msg = integerValidator.getMessages("100");
        assertNull("Test accuracy for method getMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is not equal to 100.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy1() {
        String[] msg = integerValidator.getAllMessages("1023");
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be successful only when the number is 100.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy2() {
        String[] msg = integerValidator.getAllMessages("100");
        assertNull("Test accuracy for method getAllMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is not equal to 100.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy1() {
        String[] msg = integerValidator.getAllMessages("1023", 2);
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be successful only when the number is 100.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy2() {
        String[] msg = integerValidator.getAllMessages("100", 1);
        assertNull("Test accuracy for method getAllMessages() failed.", msg);
    }

    /**
     * <p>
     * Creates an instance of <code>AbstractObjectValidator.</code>
     * </p>
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator() {
        return IntegerValidator.equalTo(100);
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
        return IntegerValidator.equalTo(100, bundleInfo);
    }
}
