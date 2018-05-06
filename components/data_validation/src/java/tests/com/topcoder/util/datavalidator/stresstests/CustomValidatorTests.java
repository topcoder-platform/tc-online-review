/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.stresstests;

import junit.framework.TestCase;

import com.topcoder.util.datavalidator.IntegerValidator;

/**
 * This tests custom validator.
 *
 * @author TCSDEVELOPER, Psyho
 * @version 1.1
 * @since 1.0
 */
public class CustomValidatorTests extends TestCase {

    /**
     * Sets up testing environment.
     */
    public void setUp() {
        // nothing
    }

    /**
     * Tears down testing environment.
     */
    public void tearDown() {
        // nothing
    }

    /**
     * Tests custom validator.
     */
    public void testCustomValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 20000; i++) {
            IntegerValidator validator = new MyIntegerValidator();
            assertTrue(validator.valid(7));
            assertTrue(validator.valid(new Byte((byte) 7)));
            assertTrue(validator.valid("7"));
            assertNull(validator.getMessage(7));
            assertNull(validator.getMessage(new Byte((byte) 7)));
            assertNull(validator.getMessage("7"));

            assertFalse(validator.valid(8));
            assertFalse(validator.valid(new Byte((byte) 8)));
            assertFalse(validator.valid("8"));

            assertNotNull(validator.getMessage(8));
            assertNotNull(validator.getMessage(new Byte((byte) 8)));
            assertNotNull(validator.getMessage("8"));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test Custom Validator time cost:" + (end - start) + "ms");
    }

}

/**
 * My integer validator.
 */
class MyIntegerValidator extends IntegerValidator {

    /**
     * Whether a value is 7.
     *
     * @param value the value to judge
     * @return whether it is 7
     */
    public boolean valid(int value) {
        return value == 7;
    }

    /**
     * Gets error message.
     *
     * @param value the value to judge
     * @return error message, null if value is 7
     */
    public String getMessage(int value) {
        return value == 7 ? null : "value is not 7";
    }

    /**
     * Gets error message.
     *
     * @param value the value to judge
     * @return error message, null if value is 7
     */
    public String[] getMessages(Object object) {
        String message = getMessage(0);
        return message == null ? null : new String[] {message};
    }

    /**
     * Gets error message.
     *
     * @param value the value to judge
     * @return error message, null if value is 7
     */
    public String[] getAllMessages(Object object) {
        return getMessages(object);
    }

    /**
     * Gets error message.
     *
     * @param value the value to judge
     * @return error message, null if value is 7
     */
    public String[] getAllMessages(Object object, int messageLimit) {
        return getAllMessages(object, Integer.MAX_VALUE);
    }

}
