/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import junit.framework.TestCase;


/**
 * <p>
 * Unit test cases for OperationNotSupportedException.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class OperationNotSupportedExceptionTests extends TestCase {
    /** The test message. */
    private static final String ERROR_MESSAGE = "test exception message";
    /**
     * the cause Exception.
     */
    private final Exception cause = new NullPointerException();

    /**
     * test the excption constructor with ERROR_MESSAGE.
     */
    public void testOperationNotSupportedException1() {
        OperationNotSupportedException de = new OperationNotSupportedException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate OperationNotSupportedException.", de);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, de.getMessage());
        assertTrue("The error message should match.", de.getMessage().indexOf(ERROR_MESSAGE) >= 0);
    }

    /**
     * test the excption constructor with ERROR_MESSAGE and throwable.
     */
    public void testOperationNotSupportedException2() {
        OperationNotSupportedException de = new OperationNotSupportedException(ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate OperationNotSupportedException.", de);

        assertEquals("Cause is not properly propagated to super class.", cause, de.getCause());
    }

    /**
     * Inheritance test.
     */
    public void testOperationNotSupportedException3() {
        assertTrue("OperationNotSupportedException does not subclass SearchBuilderException.",
            new OperationNotSupportedException(ERROR_MESSAGE) instanceof SearchBuilderException);

        assertTrue("OperationNotSupportedException does not subclass SearchBuilderException.",
            new OperationNotSupportedException(ERROR_MESSAGE, cause) instanceof SearchBuilderException);
    }

}
