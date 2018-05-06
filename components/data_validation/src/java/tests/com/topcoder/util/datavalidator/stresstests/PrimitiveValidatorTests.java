/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.stresstests;

import junit.framework.TestCase;

import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.PrimitiveValidator;

/**
 * This tests PrimitiveValidator.
 *
 * @author TCSDEVELOPER, Psyho
 * @version 1.1
 * @since 1.0
 */
public class PrimitiveValidatorTests extends TestCase {

    /**
     * Sets up testing environment.
     */
    public void setUp() {
        // nothing
    }

    /**
     * Tear down testing environment.
     */
    public void tearDown() {
        // nothing
    }

    /**
     * Tests PrimitveValidator.
     */
    public void testPrimitiveValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            PrimitiveValidator validator = new PrimitiveValidator(IntegerValidator.isOdd());
            assertTrue(validator.valid(7));
            assertFalse(validator.valid(8));
            assertTrue(validator.valid((long) 7));
            assertFalse(validator.valid((long) 8));
            assertTrue(validator.valid((byte) 7));
            assertFalse(validator.valid((byte) 8));
            assertTrue(validator.valid((short) 7));
            assertFalse(validator.valid((short) 8));
            assertTrue(validator.valid((float) 7));
            assertFalse(validator.valid((float) 8));
            assertTrue(validator.valid((double) 7));
            assertFalse(validator.valid((double) 8));
            assertTrue(validator.valid("7"));
            assertFalse(validator.valid("8"));

            assertNull(validator.getMessage(7));
            assertNotNull(validator.getMessage(8));
            assertNull(validator.getMessage((long) 7));
            assertNotNull(validator.getMessage((long) 8));
            assertNull(validator.getMessage((byte) 7));
            assertNotNull(validator.getMessage((byte) 8));
            assertNull(validator.getMessage((short) 7));
            assertNotNull(validator.getMessage((short) 8));
            assertNull(validator.getMessage((float) 7));
            assertNotNull(validator.getMessage((float) 8));
            assertNull(validator.getMessage((double) 7));
            assertNotNull(validator.getMessage((double) 8));
            assertNull(validator.getMessage("7"));
            assertNotNull(validator.getMessage("8"));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test PrimitiveValidator time cost:" + (end - start) + "ms");
    }

}
