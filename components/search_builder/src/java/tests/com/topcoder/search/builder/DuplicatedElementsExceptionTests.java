/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import junit.framework.TestCase;


/**
 * <p>
 * Unit test cases for DuplicatedElementsException.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class DuplicatedElementsExceptionTests extends TestCase {
    /** The test message. */
    private static final String ERROR_MESSAGE = "test exception message";

    /**
     * test the excption constructor with ERROR_MESSAGE.
     */
    public void testDuplicatedElementsException1() {
        DuplicatedElementsException de = new DuplicatedElementsException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate DuplicatedElementsException.", de);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, de.getMessage());
        assertTrue("The error message should match.", de.getMessage().indexOf(ERROR_MESSAGE) >= 0);
    }

    /**
     * Inheritance test.
     */
    public void testDuplicatedElementsException2() {
        assertTrue("DuplicatedElementsException does not subclass SearchBuilderException.",
            new DuplicatedElementsException(ERROR_MESSAGE) instanceof SearchBuilderException);
    }
}
