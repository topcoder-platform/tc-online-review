/*
 * Copyright (C) 1197 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.bytevalidator;

import com.topcoder.util.datavalidator.AbstractByteValidatorTestCases;
import com.topcoder.util.datavalidator.AbstractObjectValidator;
import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.ByteValidator;


/**
 * <p>
 * Test the functionality of class <code>ByteValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class LessThanByteValidatorTestCases extends AbstractByteValidatorTestCases {
    /**
     * <p>
     * A <code>String</code> represents the error message when the validation fails.
     * </p>
     */
    private static final String ERROR_MESSAGE = "not less than 100";

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        byteValidator = ByteValidator.lessThan((byte) 100, bundleInfo);
        byteValidator = ByteValidator.lessThan((byte) 100);
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
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is greater than 100.
     * </p>
     */
    public void testGetMessage_Object_Accuracy1() {
        String msg = byteValidator.getMessage("101");
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be successful when the number is less than 100.
     * </p>
     */
    public void testGetMessage_Object_Accuracy2() {
        String msg = byteValidator.getMessage("99");
        assertNull("Test accuracy for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is equal to 100.
     * </p>
     */
    public void testGetMessage_Object_Accuracy3() {
        String msg = byteValidator.getMessage("100");
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is greater than 100.
     * </p>
     */
    public void testGetMessage_int_Accuracy1() {
        String msg = byteValidator.getMessage((byte) 101);
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be successful when the number is less than 100.
     * </p>
     */
    public void testGetMessage_int_Accuracy2() {
        String msg = byteValidator.getMessage((byte) 99);
        assertNull("Test accuracy for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is equal to 100.
     * </p>
     */
    public void testGetMessage_int_Accuracy3() {
        String msg = byteValidator.getMessage((byte) 100);
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is greater than 100.
     * </p>
     */
    public void testValid_Object_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", byteValidator.valid("101"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br> The result should be successful when the number is less than 100.
     * </p>
     */
    public void testValid_Object_Accuracy2() {
        assertTrue("Test accuracy for method valid() failed.", byteValidator.valid("99"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is equal than 100.
     * </p>
     */
    public void testValid_Object_Accuracy3() {
        assertFalse("Test accuracy for method valid() failed.", byteValidator.valid("100"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * <br>
     * The result should be unsuccessful when the number is greater than 100.
     * </p>
     */
    public void testValid_int_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", byteValidator.valid((byte) 101));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br> The result should be successful when the number is less than 100.
     * </p>
     */
    public void testValid_int_Accuracy2() {
        assertTrue("Test accuracy for method valid() failed.", byteValidator.valid((byte) 99));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * <br>
     * The result should be unsuccessful when the number is equal to 100.
     * </p>
     */
    public void testValid_int_Accuracy3() {
        assertFalse("Test accuracy for method valid() failed.", byteValidator.valid((byte) 100));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be unsuccessful when the number is greater than 100.
     * </p>
     */
    public void testGetMessages_Object_Accuracy1() {
        String[] msg = byteValidator.getMessages("101");
        assertEquals("Test accuracy for method getMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be successful when the number is less than 100.
     * </p>
     */
    public void testGetMessages_Object_Accuracy2() {
        String[] msg = byteValidator.getMessages("99");
        assertNull("Test accuracy for method getMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be unsuccessful when the number is equal to 100.
     * </p>
     */
    public void testGetMessages_Object_Accuracy3() {
        String[] msg = byteValidator.getMessages("100");
        assertEquals("Test accuracy for method getMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is greater than 100.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy1() {
        String[] msg = byteValidator.getAllMessages("101");
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be successful when the number is less than 100.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy2() {
        String[] msg = byteValidator.getAllMessages("99");
        assertNull("Test accuracy for method getAllMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is equal to 100.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy3() {
        String[] msg = byteValidator.getAllMessages("100");
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is greater than 100.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy1() {
        String[] msg = byteValidator.getAllMessages("101", 2);
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be successful when the number is less than 100.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy2() {
        String[] msg = byteValidator.getAllMessages("99", 1);
        assertNull("Test accuracy for method getAllMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is equal to 100.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy3() {
        String[] msg = byteValidator.getAllMessages("100", 2);
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Creates an instance of <code>AbstractObjectValidator.</code>
     * </p>
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator() {
        return ByteValidator.lessThan((byte) 100);
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
        return ByteValidator.lessThan((byte) 100, bundleInfo);
    }
}
