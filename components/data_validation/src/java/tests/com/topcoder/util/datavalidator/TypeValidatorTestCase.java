/*
 * TCS Data Validation
 *
 * TypeValidatorTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

import com.topcoder.util.datavalidator.*;
import junit.framework.TestCase;

/**
 * This tests TypeValidator.
 *
 * @author WishingBone
 * @author zimmy
 * @version 1.0
 */
public class TypeValidatorTestCase extends TestCase {

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
     * Tests TypeValidator.
     */
    public void testTypeValidator() {
        TypeValidator validator = new TypeValidator(
                IntegerValidator.isOdd(), Integer.class);
        assertTrue(validator.valid(new Integer(7)));
        assertFalse(validator.valid("7"));

        assertNull(validator.getMessage(new Integer(7)));
        assertEquals(validator.getMessage("7"), "not instance of java.lang.Integer");

        validator = new TypeValidator(Integer.class);
        assertTrue(validator.valid(new Integer(-1)));
        assertFalse(validator.valid("-1"));

        assertNull(validator.getMessage(new Integer(-1)));
        assertEquals(validator.getMessage("-1"), "not instance of java.lang.Integer");
    }

}
