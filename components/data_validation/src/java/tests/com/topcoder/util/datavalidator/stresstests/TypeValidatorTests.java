/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.stresstests;

import junit.framework.TestCase;

import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.TypeValidator;

/**
 * This tests TypeValidator.
 *
 * @author TCSDEVELOPER, Psyho
 * @version 1.1
 * @since 1.0
 */
public class TypeValidatorTests extends TestCase {

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
     * Tests TypeValidator.
     */
    public void testTypeValidator() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            TypeValidator validator = new TypeValidator(IntegerValidator.isOdd(), Integer.class);
            assertTrue(validator.valid(new Integer(7)));
            assertFalse(validator.valid("7"));

            // if(true) return;
            assertNull("Integer TypeValidator error 1", validator.getMessage(new Integer(7)));
            assertNotNull("Integer TypeValidator error 2", validator.getMessage("7"));

            validator = new TypeValidator(Integer.class);
            assertTrue("Integer TypeValidator error 3", validator.valid(new Integer(-1)));
            assertFalse("Integer TypeValidator error 4", validator.valid("-1"));

            assertNull("Integer TypeValidator error 5", validator.getMessage(new Integer(-1)));
            assertNotNull("Integer TypeValidator error 6", validator.getMessage("-1"));
        }
        long end = System.currentTimeMillis();
        System.out.println("Test TypeValidator time cost:" + (end - start) + "ms");

    }

}
