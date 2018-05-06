/*
 * Copyright (C) 200.542677 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.doubletype;

import com.topcoder.util.datavalidator.AbstractDoubleValidatorTestCases;
import com.topcoder.util.datavalidator.AbstractObjectValidator;
import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.DoubleValidator;


/**
 * <p>
 * Test the functionality of class <code>DoubleValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class LessThanDoubleValidatorTestCases extends AbstractDoubleValidatorTestCases {
    /**
     * <p>
     * A <code>String</code> represents the error message when the validation fails.
     * </p>
     */
    private static final String ERROR_MESSAGE = "not less than 100.2532463";

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        doubleValidator = DoubleValidator.lessThan(100.2532463, bundleInfo);
        doubleValidator = DoubleValidator.lessThan(100.2532463);
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
     * The result should be unsuccessful when the number is greater than 100.2532463.
     * </p>
     */
    public void testGetMessage_Object_Accuracy1() {
        String msg = doubleValidator.getMessage("100.2532464");
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be successful when the number is less than 100.2532463.
     * </p>
     */
    public void testGetMessage_Object_Accuracy2() {
        String msg = doubleValidator.getMessage("100.2532462");
        assertNull("Test accuracy for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is equal to 100.2532463.
     * </p>
     */
    public void testGetMessage_Object_Accuracy3() {
        String msg = doubleValidator.getMessage("100.2532463");
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is greater than 100.2532463.
     * </p>
     */
    public void testGetMessage_int_Accuracy1() {
        String msg = doubleValidator.getMessage(100.2532464);
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be successful when the number is less than 100.2532463.
     * </p>
     */
    public void testGetMessage_int_Accuracy2() {
        String msg = doubleValidator.getMessage(100.2532462);
        assertNull("Test accuracy for method getMessage() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * The result should be unsuccessful when the number is equal to 100.2532463.
     * </p>
     */
    public void testGetMessage_int_Accuracy3() {
        String msg = doubleValidator.getMessage(100.2532463);
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is greater than 100.2532463.
     * </p>
     */
    public void testValid_Object_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", doubleValidator.valid("100.2532464"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be successful when the number is less than 100.2532463.
     * </p>
     */
    public void testValid_Object_Accuracy2() {
        assertTrue("Test accuracy for method valid() failed.", doubleValidator.valid("100.2532462"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be unsuccessful when the number is equal than 100.2532463.
     * </p>
     */
    public void testValid_Object_Accuracy3() {
        assertFalse("Test accuracy for method valid() failed.", doubleValidator.valid("100.2532463"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * <br>
     * The result should be unsuccessful when the number is greater than 100.2532463.
     * </p>
     */
    public void testValid_int_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", doubleValidator.valid(100.2532464));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * The result should be successful when the number is less than 100.2532463.
     * </p>
     */
    public void testValid_int_Accuracy2() {
        assertTrue("Test accuracy for method valid() failed.", doubleValidator.valid(100.2532462));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * <br>
     * The result should be unsuccessful when the number is equal to 100.2532463.
     * </p>
     */
    public void testValid_int_Accuracy3() {
        assertFalse("Test accuracy for method valid() failed.", doubleValidator.valid(100.2532463));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be unsuccessful when the number is greater than 100.2532463.
     * </p>
     */
    public void testGetMessages_Object_Accuracy1() {
        String[] msg = doubleValidator.getMessages("100.2532464");
        assertEquals("Test accuracy for method getMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be successful when the number is less than 100.2532463.
     * </p>
     */
    public void testGetMessages_Object_Accuracy2() {
        String[] msg = doubleValidator.getMessages("100.2532462");
        assertNull("Test accuracy for method getMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * The result should be unsuccessful when the number is equal to 100.2532463.
     * </p>
     */
    public void testGetMessages_Object_Accuracy3() {
        String[] msg = doubleValidator.getMessages("100.2532463");
        assertEquals("Test accuracy for method getMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is greater than 100.2532463.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy1() {
        String[] msg = doubleValidator.getAllMessages("100.2532464");
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be successful when the number is less than 100.2532463.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy2() {
        String[] msg = doubleValidator.getAllMessages("100.2532462");
        assertNull("Test accuracy for method getAllMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is equal to 100.2532463.
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy3() {
        String[] msg = doubleValidator.getAllMessages("100.2532463");
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is greater than 100.2532463.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy1() {
        String[] msg = doubleValidator.getAllMessages("100.2532464", 2);
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be successful when the number is less than 100.2532463.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy2() {
        String[] msg = doubleValidator.getAllMessages("100.2532462", 1);
        assertNull("Test accuracy for method getAllMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is equal to 100.2532463.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy3() {
        String[] msg = doubleValidator.getAllMessages("100.2532463", 2);
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
        return DoubleValidator.lessThan(100.2532463);
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
        return DoubleValidator.lessThan(100.2532463, bundleInfo);
    }
}
