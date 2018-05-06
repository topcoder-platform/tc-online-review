/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.stresstests;

import junit.framework.TestCase;

import com.topcoder.util.datavalidator.AndValidator;
import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.NotValidator;
import com.topcoder.util.datavalidator.NullValidator;
import com.topcoder.util.datavalidator.OrValidator;

/**
 * This tests CompositeValidator.
 *
 * @author TCSDEVELOPER, Psyho
 * @version 1.1
 * @since 1.0
 */
public class CompositeValidatorTests extends TestCase {

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
     * Tests NullValidator.
     */
    public void testNullValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 20000; i++) {
            NullValidator validator = new NullValidator();
            assertTrue(validator.valid(null));
            assertFalse(validator.valid(new Object()));

            assertNull(validator.getMessage(null));
            assertNotNull(validator.getMessage(new Object()));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test Null Validator time cost:" + (end - start) + "ms");
    }

    /**
     * Tests NotValidator.
     */
    public void testNotValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 20000; i++) {
            NotValidator validator = new NotValidator(IntegerValidator.isOdd());
            assertTrue(validator.valid("8"));
            assertFalse(validator.valid("7"));

            assertNull(validator.getMessage("8"));
            assertNotNull(validator.getMessage("7"));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test Not Validator time cost:" + (end - start) + "ms");
    }

    /**
     * Tests AndValidator.
     */
    public void testAndValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 20000; i++) {
            AndValidator validator = new AndValidator(IntegerValidator.isOdd(), IntegerValidator.isPositive());
            assertTrue(validator.valid("7"));
            assertFalse(validator.valid("8"));
            assertFalse(validator.valid("-1"));
            assertFalse(validator.valid("-2"));

            assertNull(validator.getMessage("7"));
            assertNotNull(validator.getMessage("8"));
            assertNotNull(validator.getMessage("-1"));
            assertNotNull(validator.getMessage("-2"));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test And Validator time cost:" + (end - start) + "ms");
    }

    /**
     * Tests OrValidator.
     */
    public void testOrValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 20000; i++) {
            OrValidator validator = new OrValidator(IntegerValidator.isOdd(), IntegerValidator.isPositive());
            assertTrue(validator.valid("7"));
            assertTrue(validator.valid("8"));
            assertTrue(validator.valid("-1"));
            assertFalse(validator.valid("-2"));

            assertNull(validator.getMessage("7"));
            assertNull(validator.getMessage("8"));
            assertNull(validator.getMessage("-1"));
            assertNotNull(validator.getMessage("-2"));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test Or Validator time cost:" + (end - start) + "ms");
    }

}
