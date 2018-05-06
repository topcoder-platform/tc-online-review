/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.string;

import com.topcoder.util.datavalidator.AbstractStringValidatorTestCases;
import com.topcoder.util.datavalidator.AbstractObjectValidator;
import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.StringValidator;


/**
 * <p>
 * Test the functionality of class <code>StringValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class EndsWithStringValidatorTestCases extends AbstractStringValidatorTestCases {
    /**
     * <p>
     * Represents the error message.
     * </p>
     */
    private static final String ERROR_MESSAGE = "does not end with \"coder\"";

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        validator = StringValidator.endsWith("coder");
    }

    /**
     * <p>
     * Tear down the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        bundleInfo = null;
        validator = null;
        super.tearDown();
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_Object_Accuracy1() {
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, validator.getMessage("topooder"));
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE,
            validator.getMessage("  topcoder   "));
        assertNull("Test accuracy for method getMessage() failed.", validator.getMessage("topcoder"));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_Object_Accuracy1() {
        assertFalse("Test accuracy for method getMessage() failed.", validator.valid("topooder"));
        assertFalse("Test accuracy for method getMessage() failed.", validator.valid("   topcoder   "));
        assertTrue("Test accuracy for method getMessage() failed.", validator.valid("topcoder"));
    }

    /**
     * <p>
     * Failure test case for method 'valid()'.<br> The argument is null.
     * </p>
     */
    public void testValid_String_Null1() {
        assertFalse("Test accuracy for method getMessage() failed.", validator.valid((Object) null));
    }

    /**
     * <p>
     * Failure test case for method 'valid()'.<br> The argument is an empty <code>String</code>.
     * </p>
     */
    public void testValid_String_Empty1() {
        assertFalse("Test accuracy for method getMessage() failed.", validator.valid("   "));
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy1() {
        String[] msg = validator.getAllMessages("topooder");
        assertEquals("Test accuracy for method getAllMessages() failed.", 1, msg.length);
        assertEquals("Test accuracy for method getAllMessages() failed.", ERROR_MESSAGE, msg[0]);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * </p>
     */
    public void testGetAllMessages_Object_Accuracy2() {
        String[] msg = validator.getAllMessages("topcoder");
        assertNull("Test accuracy for method getAllMessages() failed.", msg);
    }

    /**
     * <p>
     * Accuracy test case for method 'getAllMessages()'.<br>
     * The result should be unsuccessful when the number is not equal to 100.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_Accuracy1() {
        String[] msg = validator.getAllMessages("topooder", 2);
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
        String[] msg = validator.getAllMessages("topcoder", 1);
        assertNull("Test accuracy for method getAllMessages() failed.", msg);
    }

    /**
     * <p>
     * Failure test case for method 'getAllMessages()'.<br> IllegalArgumentException should be thrown.
     * </p>
     */
    public void testGetAllMessages_ObjectInt_failure() {
        try {
            validator.getAllMessages("topcoder", 0);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Creates an instance of <code>AbstractObjectValidator.</code>
     * </p>
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator() {
        return StringValidator.endsWith("coder");
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
        return StringValidator.endsWith("coder", bundleInfo);
    }
}
