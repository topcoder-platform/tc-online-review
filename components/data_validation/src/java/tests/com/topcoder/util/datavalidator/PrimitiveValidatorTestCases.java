/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;


/**
 * <p>
 * Test the functionality of class <code>PrimitiveValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class PrimitiveValidatorTestCases extends TestCase {
    /**
     * <p>
     * A String represent error message.
     * </p>
     */
    private static final String ERROR_MESSAGE = "invalid Integer";

    /**
     * <p>
     * An instance of <code>ObjectValidator</code> for testing.<br>
     * </p>
     */
    private ObjectValidator objectValidator = null;

    /**
     * <p>
     * An instance of <code>BundleInfo</code> for testing.<br>
     * </p>
     */
    private BundleInfo bundleInfo = null;

    /**
     * <p>
     * An instance of <code>PrimitiveValidator</code> for testing.<br>
     * </p>
     */
    private PrimitiveValidator validator = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        objectValidator = IntegerValidator.isEven();
        validator = new PrimitiveValidator(objectValidator);
    }

    /**
     * <p>
     * Tear down the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        objectValidator = null;
        bundleInfo = null;
        validator = null;
        super.tearDown();
    }

    /**
     * <p>
     * Failure test case for method 'PrimitiveValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testPrimitiveValidator_ObjectValidatorBundleInfo_Null1()
        throws Exception {
        try {
            new PrimitiveValidator(null);
            fail("Test failure for PrimitiveValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'PrimitiveValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testPrimitiveValidator_ObjectValidatorBundleInfo_Null2()
        throws Exception {
        try {
            new PrimitiveValidator(objectValidator, null);
            fail("Test failure for PrimitiveValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'PrimitiveValidator()'.<br>
     * </p>
     */
    public void testPrimitiveValidator_ObjectValidatorBundleInfo_Accuracy1() {
        assertTrue("Test accuracy for method PrimitiveValidator() failed.", validator instanceof ObjectValidator);
    }

    /**
     * <p>
     * Failure test case for method 'PrimitiveValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testPrimitiveValidator_ObjectValidator_Null1()
        throws Exception {
        try {
            new PrimitiveValidator(null);
            fail("Test failure for PrimitiveValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_char_Accuracy1() {
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, validator.getMessage('a'));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_double_Accuracy() {
        assertEquals("Test accuracy for method getMessage() failed.", "not even",
                validator.getMessage((double) 33.134));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_float_Accuracy1() {
        assertEquals("Test accuracy for method getMessage() failed.", "not even", validator.getMessage((float) 33.1));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_long_Accuracy1() {
        assertEquals("Test accuracy for method getMessage() failed.", "not even", validator.getMessage((long) 12341));
        assertNull("Test accuracy for method getMessage() failed.", validator.getMessage((long) 12342));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_int_Accuracy1() {
        assertEquals("Test accuracy for method getMessage() failed.", "not even", validator.getMessage(1235541));
        assertNull("Test accuracy for method getMessage() failed.", validator.getMessage(1255342));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_short_Accuracy1() {
        assertEquals("Test accuracy for method getMessage() failed.", "not even", validator.getMessage((short) 12341));
        assertNull("Test accuracy for method getMessage() failed.", validator.getMessage((short) 12342));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_byte_Accuracy1() {
        assertEquals("Test accuracy for method getMessage() failed.", "not even", validator.getMessage((byte) 121));
        assertNull("Test accuracy for method getMessage() failed.", validator.getMessage((byte) 122));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_boolean_Accuracy1() {
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, validator.getMessage(true));
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, validator.getMessage(false));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_Object_Accuracy1() {
        assertEquals("Test accuracy for method getMessage() failed.", "not even", validator.getMessage("555"));
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, validator.getMessage("#34"));
        assertEquals("Test accuracy for method getMessage() failed.", "not even", validator.getMessage("557"));

        assertNull("Test accuracy for method getMessage() failed.", validator.getMessage("556"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_char_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", validator.valid('a'));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_double_Accuracy() {
        assertFalse("Test accuracy for method valid() failed.", validator.valid((double) 33.1));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_float_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", validator.valid((float) 33.1));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_long_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", validator.valid((long) 12341));
        assertTrue("Test accuracy for method valid() failed.", validator.valid((long) 12342));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_int_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", validator.valid(1235541));
        assertTrue("Test accuracy for method valid() failed.", validator.valid(1255342));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_short_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", validator.valid((short) 12341));
        assertTrue("Test accuracy for method valid() failed.", validator.valid((short) 12342));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_byte_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", validator.valid((byte) 121));
        assertTrue("Test accuracy for method valid() failed.", validator.valid((byte) 122));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_boolean_Accuracy1() {
        assertFalse("Test accuracy for method valid() failed.", validator.valid(true));
        assertFalse("Test accuracy for method valid() failed.", validator.valid(false));
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessages()'.<br>
     * </p>
     */
    public void testGetMessages_Object_Accuracy1() {
        String[] msgs = validator.getMessages(null);

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
            validator.getAllMessages(null, 0);
            fail("Test failure for getAllMessages() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'getAllMessages()'.<br>
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetAllMessages_Objectint_Null1() throws Exception {
        String[] msgs = validator.getAllMessages(null, 5);

        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msgs.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", ERROR_MESSAGE, msgs[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * </p>
     */
    public void testGetAllMessages_Objectint_Accuracy1() {
        String[] msgs = validator.getAllMessages("54.65", 1);

        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msgs.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", ERROR_MESSAGE, msgs[0]);
    }

    /**
     * <p>
     * Failure test case for method 'getAllMessages()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testGetAllMessages_Object_Null1() {
        String[] msgs = validator.getAllMessages(null);

        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msgs.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", ERROR_MESSAGE, msgs[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy1() {
        String[] msgs = validator.getAllMessages("54.65");

        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msgs.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", ERROR_MESSAGE, msgs[0]);
    }
}
