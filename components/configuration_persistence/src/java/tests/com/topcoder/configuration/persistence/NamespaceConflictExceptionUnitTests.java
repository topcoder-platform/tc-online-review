/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.configuration.persistence;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test for the NamespaceConflictException class.
 * </p>
 *
 * @author rainday
 * @version 1.0
 */
public class NamespaceConflictExceptionUnitTests extends TestCase {

    /**
     * <p>
     * Accuracy test the <code>NamespaceConflictException(String)</code> constructor.
     * NamespaceConflictException instance should be created.
     * </p>
     */
    public void testNamespaceConflictExceptionString() {
        NamespaceConflictException exception = new NamespaceConflictException("Failed.");
        assertNotNull("Create NamespaceConflictException incorrectly.", exception);
        assertEquals("Create NamespaceConflictException incorrectly.", "Failed.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test the <code>NamespaceConflictException(String, Throwable)</code> constructor.
     * NamespaceConflictException instance should be created.
     * </p>
     */
    public void testNamespaceConflictExceptionStringThrowable() {
        IllegalArgumentException exp = new IllegalArgumentException();
        NamespaceConflictException exception = new NamespaceConflictException("Failed.", exp.getCause());
        assertNotNull("Create NamespaceConflictException incorrectly.", exception);
        assertEquals("Create NamespaceConflictException incorrectly.", "Failed.", exception.getMessage());
        assertEquals("Create NamespaceConflictException incorrectly.", exp.getCause(), exception.getCause());
    }

}
