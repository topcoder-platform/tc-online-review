/*
 * TCS Data Validation
 *
 * CustomValidatorTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

import com.topcoder.util.datavalidator.*;
import junit.framework.TestCase;

/**
 * This tests custom validator.
 *
 * @author WishingBone
 * @author zimmy
 * @version 1.0
 */
public class CustomValidatorTestCase extends TestCase {

    /**
     * Set up testing environment.
     */
    public void setUp() {
    }

    /**
     * Tear down testing environment.
     */
    public void tearDown() {
    }

    /**
     * Tests custom validator.
     */
    public void testCustomValidator() {
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

        assertEquals(validator.getMessage(8), "value is not 7");
        assertEquals(validator.getMessage(new Byte((byte) 8)), "value is not 7");
        assertEquals(validator.getMessage("8"), "value is not 7");
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
     *
     * @return whether it is 7
     */
    public boolean valid(int value) {
        return value == 7;
    }

    /**
     * Get error message.
     *
     * @param value the value to judge
     *
     * @return error message, null if value is 7
     */
    public String getMessage(int value) {
        return value == 7 ? null : "value is not 7";
    }

}
