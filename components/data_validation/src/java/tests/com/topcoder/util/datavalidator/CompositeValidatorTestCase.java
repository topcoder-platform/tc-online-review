/*
 * TCS Data Validation
 *
 * CompositeValidatorTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

import com.topcoder.util.datavalidator.*;
import junit.framework.TestCase;

/**
 * This tests CompositeValidator.
 *
 * @author WishingBone
 * @author zimmy
 * @version 1.0
 */
public class CompositeValidatorTestCase extends TestCase {

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
     * Tests NullValidator.
     */
    public void testNullValidator() {
        NullValidator validator = new NullValidator();
        assertTrue(validator.valid(null));
        assertFalse(validator.valid(new Object()));

        assertNull(validator.getMessage(null));
        assertEquals(validator.getMessage(new Object()), "not null");
    }

    /**
     * Tests NotValidator.
     */
    public void testNotValidator() {
        NotValidator validator = new NotValidator(
                IntegerValidator.isOdd());
        assertTrue(validator.valid("8"));
        assertFalse(validator.valid("7"));

        assertNull(validator.getMessage("8"));
        assertEquals(validator.getMessage("7"), "NOT failure");
    }

    /**
     * Tests AndValidator.
     */
    public void testAndValidator() {
        AndValidator validator = new AndValidator(
                IntegerValidator.isOdd(),
                IntegerValidator.isPositive());
        assertTrue(validator.valid("7"));
        assertFalse(validator.valid("8"));
        assertFalse(validator.valid("-1"));
        assertFalse(validator.valid("-2"));

        assertNull(validator.getMessage("7"));
        assertEquals(validator.getMessage("8"), "not odd");
        assertEquals(validator.getMessage("-1"), "not greater than 0");
        assertEquals(validator.getMessage("-2"), "not odd");
    }

    /**
     * Tests OrValidator.
     */
    public void testOrValidator() {
        OrValidator validator = new OrValidator(
                IntegerValidator.isOdd(),
                IntegerValidator.isPositive());
        assertTrue(validator.valid("7"));
        assertTrue(validator.valid("8"));
        assertTrue(validator.valid("-1"));
        assertFalse(validator.valid("-2"));

        assertNull(validator.getMessage("7"));
        assertNull(validator.getMessage("8"));
        assertNull(validator.getMessage("-1"));
        assertEquals(validator.getMessage("-2"), "not odd AND not greater than 0");
    }

}
