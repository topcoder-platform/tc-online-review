/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.persistence;

import junit.framework.TestCase;

/**
 * <p>
 * Unit tests for the <code>FileAlreadyLockedException</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class FileAlreadyLockedExceptionTest extends TestCase {
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
     * Tests accuracy of <code>FileAlreadyLockedException(String)</code> constructor.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testCtor1Accuracy() {
        FileAlreadyLockedException exception = new FileAlreadyLockedException(MESSAGE);
        assertNotNull("Unable to instantiate FileAlreadyLockedException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Tests accuracy of <code>FileAlreadyLockedException(String, Throwable)</code> constructor.
     * </p>
     *
     * <p>
     * Verifies the error message and the cause are properly propagated.
     * </p>
     */
    public void testCtor2Accuracy() {
        FileAlreadyLockedException exception = new FileAlreadyLockedException(MESSAGE, CAUSE);
        assertNotNull("Unable to instantiate FileAlreadyLockedException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
        assertEquals("Cause is not properly propagated to super class.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Inheritance test, verifies <code>FileAlreadyLockedException</code> subclasses
     * <code>FilePersistenceException</code>.
     * </p>
     */
    public void testInheritance() {
        assertTrue("FileAlreadyLockedException does not subclass FilePersistenceException.",
                new FileAlreadyLockedException(MESSAGE) instanceof FilePersistenceException);
    }
}