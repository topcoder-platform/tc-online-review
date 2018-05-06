/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import junit.framework.TestCase;


/**
 * <p>
 * Unit test cases for PersistenceOperationExceptionTests.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class PersistenceOperationExceptionTests extends TestCase {
    /** The test message. */
    private static final String ERROR_MESSAGE = "test exception message";
    /**
     * the cause Exception.
     */
    private final Exception cause = new NullPointerException();

    /**
     * test the excption constructor with ERROR_MESSAGE.
     */
    public void testPersistenceOperationException1() {
        PersistenceOperationException de = new PersistenceOperationException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate PersistenceOperationException.", de);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, de.getMessage());
        assertTrue("The error message should match.", de.getMessage().indexOf(ERROR_MESSAGE) >= 0);
    }

    /**
     * test the excption constructor with ERROR_MESSAGE and throwable.
     */
    public void testPersistenceOperationException2() {
        PersistenceOperationException de = new PersistenceOperationException(ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate PersistenceOperationException.", de);

        assertEquals("Cause is not properly propagated to super class.", cause, de.getCause());
    }

    /**
     * Inheritance test.
     */
    public void testPersistenceOperationException3() {
        assertTrue("PersistenceOperationException does not subclass SearchBuilderException.",
            new PersistenceOperationException(ERROR_MESSAGE) instanceof SearchBuilderException);

        assertTrue("PersistenceOperationException does not subclass SearchBuilderException.",
            new PersistenceOperationException(ERROR_MESSAGE, cause) instanceof SearchBuilderException);
    }

}
