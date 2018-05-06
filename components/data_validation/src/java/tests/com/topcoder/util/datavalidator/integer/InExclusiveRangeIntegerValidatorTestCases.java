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
public class InExclusiveRangeIntegerValidatorTestCases extends AbstractIntegerValidatorTestCases {
    /**
     * <p>
     * A <code>String</code> represents the error message when the validation fails.
     * </p>
     */
    private static final String DOWN_ERROR_MESSAGE = "not greater than 100";

    /**
     * <p>
     * A <code>String</code> represents the error message when the validation fails.
     * </p>
     */
    private static final String UP_ERROR_MESSAGE = "not less than 200";

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        integerValidator = IntegerValidator.inExclusiveRange(100, 200, bundleInfo);
        integerValidator = IntegerValidator.inExclusiveRange(100, 200);
    }

    /**
     * <p>
     * Failure test for constructor.<br>
     * the lower bound is greater than upper bound, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testConstructor_failure1() {
        try {
            IntegerValidator.inExclusiveRange(20, 10);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test for constructor.<br>
     * the lower bound is equal to upper bound, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testConstructor_failure2() {
        try {
            IntegerValidator.inExclusiveRange(20, 20);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
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
     * The result should be unsuccessful when the number is less than 100.
     * </p>
     */
    public void testGetMessage_Object_Accuracy1() {
        String msg = integerValidator.getMessage("99");
        assertEquals("Test accuracy for method getMessage() failed.", DOWN_ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is greater than 200.
     * </p>
     */
    public void testGetMessage_Object_Accuracy2() {
        String msg = integerValidator.getMessage("201");
        assertEquals("Test accuracy for method getMessage() failed.", UP_ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is equal to 100.
     * </p>
     */
    public void testGetMessage_Object_Accuracy3() {
        String msg = integerValidator.getMessage("100");
        assertEquals("Test accuracy for method getMessage() failed.", DOWN_ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is equal to 200.
     * </p>
     */
    public void testGetMessage_Object_Accuracy4() {
        String msg = integerValidator.getMessage("200");
        assertEquals("Test accuracy for method getMessage() failed.", UP_ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be successful when the number is equal to 150.
     * </p>
     */
    public void testGetMessage_Object_Accuracy5() {
        String msg = integerValidator.getMessage("150");
        assertNull("Test accuracy for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is less than 100.
     * </p>
     */
    public void testGetMessage_int_Accuracy1() {
        String msg = integerValidator.getMessage(99);
        assertEquals("Test accuracy for method getMessage() failed.", DOWN_ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is greater than 200.
     * </p>
     */
    public void testGetMessage_int_Accuracy2() {
        String msg = integerValidator.getMessage(201);
        assertEquals("Test accuracy for method getMessage() failed.", UP_ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is equal to 100.
     * </p>
     */
    public void testGetMessage_int_Accuracy3() {
        String msg = integerValidator.getMessage(100);
        assertEquals("Test accuracy for method getMessage() failed.", DOWN_ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is equal to 200.
     * </p>
     */
    public void testGetMessage_int_Accuracy4() {
        String msg = integerValidator.getMessage(200);
        assertEquals("Test accuracy for method getMessage() failed.", UP_ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be successful when the number is equal to 150.
     * </p>
     */
    public void testGetMessage_int_Accuracy5() {
        String msg = integerValidator.getMessage(150);
        assertNull("Test accuracy for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br> The result should be unsuccessful when the number is less than 100.
     * </p>
     */
    public void testValid_Object_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", integerValidator.valid("99"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is greater than 200.
     * </p>
     */
    public void testValid_Object_Accuracy2() {
        assertFalse("Test accuracy for method valid() failed.", integerValidator.valid("201"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is equal than 100.
     * </p>
     */
    public void testValid_Object_Accuracy3() {
        assertFalse("Test accuracy for method valid() failed.", integerValidator.valid("100"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is equal than 200.
     * </p>
     */
    public void testValid_Object_Accuracy4() {
        assertFalse("Test accuracy for method valid() failed.", integerValidator.valid("200"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is equal than 100.
     * </p>
     */
    public void testValid_Object_Accuracy5() {
        assertTrue("Test accuracy for method valid() failed.", integerValidator.valid("150"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * <br>
     * The result should be unsuccessful when the number is less than 100.
     * </p>
     */
    public void testValid_int_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", integerValidator.valid(99));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is greater than 200.
     * </p>
     */
    public void testValid_int_Accuracy2() {
        assertFalse("Test accuracy for method valid() failed.", integerValidator.valid(201));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * <br>
     * The result should be unsuccessful when the number is equal to 100.
     * </p>
     */
    public void testValid_int_Accuracy3() {
        assertFalse("Test accuracy for method valid() failed.", integerValidator.valid(100));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * <br>
     * The result should be unsuccessful when the number is equal to 200.
     * </p>
     */
    public void testValid_int_Accuracy4() {
        assertFalse("Test accuracy for method valid() failed.", integerValidator.valid(200));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * <br>
     * The result should be successful when the number is equal to 150.
     * </p>
     */
    public void testValid_int_Accuracy5() {
        assertTrue("Test accuracy for method valid() failed.", integerValidator.valid(150));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be unsuccessful when the number is less than 100.
     * </p>
     */
    public void testGetMessages_Object_Accuracy1() {
        String[] msg = integerValidator.getMessages("99");
        assertEquals("Test accuracy for method getMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getMessages() failed.", DOWN_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be unsuccessful when the number is greater than 200.
     * </p>
     */
    public void testGetMessages_Object_Accuracy2() {
        String[] msg = integerValidator.getMessages("201");
        assertEquals("Test accuracy for method getMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getMessages() failed.", UP_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be unsuccessful when the number is equal to 100.
     * </p>
     */
    public void testGetMessages_Object_Accuracy3() {
        String[] msg = integerValidator.getMessages("100");
        assertEquals("Test accuracy for method getMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getMessages() failed.", DOWN_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be unsuccessful when the number is equal to 200.
     * </p>
     */
    public void testGetMessages_Object_Accuracy4() {
        String[] msg = integerValidator.getMessages("200");
        assertEquals("Test accuracy for method getMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getMessages() failed.", UP_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be successful when the number is equal to 150.
     * </p>
     */
    public void testGetMessages_Object_Accuracy5() {
        String[] msg = integerValidator.getMessages("150");
        assertNull("Test accuracy for method getMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is less than 100.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy1() {
        String[] msg = integerValidator.getAllMessages("99");
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", DOWN_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is greater than 200.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy2() {
        String[] msg = integerValidator.getAllMessages("201");
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", UP_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is equal to 100.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy3() {
        String[] msg = integerValidator.getAllMessages("100");
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", DOWN_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is equal to 200.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy4() {
        String[] msg = integerValidator.getAllMessages("200");
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", UP_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be successful when the number is equal to 150.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy5() {
        String[] msg = integerValidator.getAllMessages("150");

        assertNull("Test accuracy for method getAllMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is less than 100.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy1() {
        String[] msg = integerValidator.getAllMessages("99", 2);
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", DOWN_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is greater than 200.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy2() {
        String[] msg = integerValidator.getAllMessages("201", 1);
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", UP_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is equal to 100.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy3() {
        String[] msg = integerValidator.getAllMessages("100", 2);
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", DOWN_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is equal to 200.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy4() {
        String[] msg = integerValidator.getAllMessages("200", 2);
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", UP_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be successful when the number is equal to 150.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy5() {
        String[] msg = integerValidator.getAllMessages("150", 2);
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
        return IntegerValidator.inExclusiveRange(100, 200);
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
        return IntegerValidator.inExclusiveRange(100, 200, bundleInfo);
    }
}
