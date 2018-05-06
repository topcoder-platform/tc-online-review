/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.persistence;

import junit.framework.TestCase;

/**
 * <p>
 * Unit tests for the <code>FileNotYetLockedException</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class FileNotYetLockedExceptionTest extends TestCase {
    /**
     * <p>
     * Represents the error message for testing.
     * </p>
     */
    private static final String MESSAGE = "error message";

    /**
     * <p>
     * Represents the <code>Exception</code> instance used for testing.
     * </p>
     */
    private static final Exception CAUSE = new Exception();

    /**
     * <p>
     * Tests accuracy of <code>FileNotYetLockedException(String)</code> constructor.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testCtor1Accuracy() {
        FileNotYetLockedException exception = new FileNotYetLockedException(MESSAGE);
        assertNotNull("Unable to instantiate FileNotYetLockedException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Tests accuracy of <code>FileNotYetLockedException(String, Throwable)</code> constructor.
     * </p>
     *
     * <p>
     * Verifies the error message and the cause are properly propagated.
     * </p>
     */
    public void testCtor2Accuracy() {
        FileNotYetLockedException exception = new FileNotYetLockedException(MESSAGE, CAUSE);
        assertNotNull("Unable to instantiate FileNotYetLockedException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
        assertEquals("Cause is not properly propagated to super class.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Inheritance test, verifies <code>FileNotYetLockedException</code> subclasses
     * <code>FilePersistenceException</code>.
     * </p>
     */
    public void testInheritance() {
        assertTrue("FileNotYetLockedException does not subclass FilePersistenceException.",
                new FileNotYetLockedException(MESSAGE) instanceof FilePersistenceException);
    }
}