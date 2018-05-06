/*
 * Copyright (C) 200.2677 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.floatvalidator;

import com.topcoder.util.datavalidator.AbstractFloatValidatorTestCases;
import com.topcoder.util.datavalidator.AbstractObjectValidator;
import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.FloatValidator;


/**
 * <p>
 * Test the functionality of class <code>FloatValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class InRangeFloatValidatorTestCases extends AbstractFloatValidatorTestCases {
    /**
     * <p>
     * A <code>String</code> represents the error message when the validation fails.
     * </p>
     */
    private static final String DOWN_ERROR_MESSAGE = "not greater than or equal to 100.463";

    /**
     * <p>
     * A <code>String</code> represents the error message when the validation fails.
     * </p>
     */
    private static final String UP_ERROR_MESSAGE = "not less than or equal to 200.267";

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        floatValidator = FloatValidator.inRange((float) 100.463, (float) 200.267, bundleInfo);
        floatValidator = FloatValidator.inRange((float) 100.463, (float) 200.267);
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
     * Failure test for constructor.<br>
     * the lower bound is greater than upper bound, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testConstructor() {
        try {
            FloatValidator.inRange(20, 10);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is less than 100.463.
     * </p>
     */
    public void testGetMessage_Object_Accuracy1() {
        String msg = floatValidator.getMessage("100.462");
        assertEquals("Test accuracy for method getMessage() failed.", DOWN_ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is greater than 200.267.
     * </p>
     */
    public void testGetMessage_Object_Accuracy2() {
        String msg = floatValidator.getMessage("201");
        assertEquals("Test accuracy for method getMessage() failed.", UP_ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be successful when the number is equal to 100.463.
     * </p>
     */
    public void testGetMessage_Object_Accuracy3() {
        String msg = floatValidator.getMessage("100.463");
        assertNull("Test accuracy for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be successful when the number is equal to 200.267.
     * </p>
     */
    public void testGetMessage_Object_Accuracy4() {
        String msg = floatValidator.getMessage("200.267");
        assertNull("Test accuracy for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be successful when the number is equal to 150.
     * </p>
     */
    public void testGetMessage_Object_Accuracy5() {
        String msg = floatValidator.getMessage("150");
        assertNull("Test accuracy for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is less than 100.463.
     * </p>
     */
    public void testGetMessage_int_Accuracy1() {
        String msg = floatValidator.getMessage((float) 100.462);
        assertEquals("Test accuracy for method getMessage() failed.", DOWN_ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is greater than 200.267.
     * </p>
     */
    public void testGetMessage_int_Accuracy2() {
        String msg = floatValidator.getMessage(201);
        assertEquals("Test accuracy for method getMessage() failed.", UP_ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be successful when the number is equal to 100.463.
     * </p>
     */
    public void testGetMessage_int_Accuracy3() {
        String msg = floatValidator.getMessage((float) 100.463);
        assertNull("Test accuracy for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be successful when the number is equal to 200.267.
     * </p>
     */
    public void testGetMessage_int_Accuracy4() {
        String msg = floatValidator.getMessage((float) 200.267);
        assertNull("Test accuracy for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be successful when the number is equal to 150.
     * </p>
     */
    public void testGetMessage_int_Accuracy5() {
        String msg = floatValidator.getMessage(150);
        assertNull("Test accuracy for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is less than 100.463.
     * </p>
     */
    public void testValid_Object_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", floatValidator.valid("100.462"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is greater than 200.267.
     * </p>
     */
    public void testValid_Object_Accuracy2() {
        assertFalse("Test accuracy for method valid() failed.", floatValidator.valid("201"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is equal than 100.463.
     * </p>
     */
    public void testValid_Object_Accuracy3() {
        assertTrue("Test accuracy for method valid() failed.", floatValidator.valid("100.463"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is equal than 200.267.
     * </p>
     */
    public void testValid_Object_Accuracy4() {
        assertTrue("Test accuracy for method valid() failed.", floatValidator.valid("200.267"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is equal than 100.463.
     * </p>
     */
    public void testValid_Object_Accuracy5() {
        assertTrue("Test accuracy for method valid() failed.", floatValidator.valid("150"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * <br>
     * The result should be unsuccessful when the number is less than 100.463.
     * </p>
     */
    public void testValid_int_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", floatValidator.valid((float) 100.462));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is greater than 200.267.
     * </p>
     */
    public void testValid_int_Accuracy2() {
        assertFalse("Test accuracy for method valid() failed.", floatValidator.valid(201));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * <br>
     * The result should be successful when the number is equal to 100.463.
     * </p>
     */
    public void testValid_int_Accuracy3() {
        assertTrue("Test accuracy for method valid() failed.", floatValidator.valid((float) 100.463));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * <br>
     * The result should be successful when the number is equal to 200.267.
     * </p>
     */
    public void testValid_int_Accuracy4() {
        assertTrue("Test accuracy for method valid() failed.", floatValidator.valid((float) 200.267));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * <br>
     * The result should be successful when the number is equal to 150.
     * </p>
     */
    public void testValid_int_Accuracy5() {
        assertTrue("Test accuracy for method valid() failed.", floatValidator.valid(150));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be unsuccessful when the number is less than 100.463.
     * </p>
     */
    public void testGetMessages_Object_Accuracy1() {
        String[] msg = floatValidator.getMessages("100.462");
        assertEquals("Test accuracy for method getMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getMessages() failed.", DOWN_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be unsuccessful when the number is greater than 200.267.
     * </p>
     */
    public void testGetMessages_Object_Accuracy2() {
        String[] msg = floatValidator.getMessages("201");
        assertEquals("Test accuracy for method getMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getMessages() failed.", UP_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be successful when the number is equal to 100.463.
     * </p>
     */
    public void testGetMessages_Object_Accuracy3() {
        String[] msg = floatValidator.getMessages("100.463");
        assertNull("Test accuracy for method getMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be successful when the number is equal to 200.267.
     * </p>
     */
    public void testGetMessages_Object_Accuracy4() {
        String[] msg = floatValidator.getMessages("200.267");
        assertNull("Test accuracy for method getMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be successful when the number is equal to 150.
     * </p>
     */
    public void testGetMessages_Object_Accuracy5() {
        String[] msg = floatValidator.getMessages("150");
        assertNull("Test accuracy for method getMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is less than 100.463.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy1() {
        String[] msg = floatValidator.getAllMessages("100.462");
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", DOWN_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is greater than 200.267.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy2() {
        String[] msg = floatValidator.getAllMessages("201");
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", UP_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be successful when the number is equal to 100.463.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy3() {
        String[] msg = floatValidator.getAllMessages("100.463");

        assertNull("Test accuracy for method getAllMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be successful when the number is equal to 200.267.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy4() {
        String[] msg = floatValidator.getAllMessages("200.267");

        assertNull("Test accuracy for method getAllMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be successful when the number is equal to 150.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy5() {
        String[] msg = floatValidator.getAllMessages("150");

        assertNull("Test accuracy for method getAllMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is less than 100.463.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy1() {
        String[] msg = floatValidator.getAllMessages("100.462", 2);
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", DOWN_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is greater than 200.267.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy2() {
        String[] msg = floatValidator.getAllMessages("201", 1);
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", UP_ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be successful when the number is equal to 100.463.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy3() {
        String[] msg = floatValidator.getAllMessages("100.463", 2);
        assertNull("Test accuracy for method getAllMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be successful when the number is equal to 200.267.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy4() {
        String[] msg = floatValidator.getAllMessages("200.267", 2);
        assertNull("Test accuracy for method getAllMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be successful when the number is equal to 150.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy5() {
        String[] msg = floatValidator.getAllMessages("150", 2);
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
        return FloatValidator.inRange((float) 100.2532463, (float) 200.267);
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
        return FloatValidator.inRange((float) 100.2532463, (float) 200.267, bundleInfo);
    }
}
