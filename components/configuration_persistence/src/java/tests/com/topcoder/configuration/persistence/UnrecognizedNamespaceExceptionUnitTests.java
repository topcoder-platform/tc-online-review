/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.configuration.persistence;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test for the UnrecognizedNamespaceException class.
 * </p>
 *
 * @author rainday
 * @version 1.0
 */
public class UnrecognizedNamespaceExceptionUnitTests extends TestCase {

    /**
     * <p>
     * Accuracy test the <code>UnrecognizedNamespaceException(String)</code> constructor.
     * UnrecognizedNamespaceException instance should be created.
     * </p>
     */
    public void testUnrecognizedNamespaceExceptionString() {
        UnrecognizedNamespaceException exception = new UnrecognizedNamespaceException("Failed.");
        assertNotNull("Create UnrecognizedNamespaceException incorrectly.", exception);
        assertEquals("Create UnrecognizedNamespaceException incorrectly.", "Failed.", exception.getMessage());
    }

    /**
     * <p>
     * Accuracy test the <code>UnrecognizedNamespaceException(String, Throwable)</code>
     * constructor. UnrecognizedNamespaceException instance should be created.
     * </p>
     */
    public void testUnrecognizedNamespaceExceptionStringThrowable() {
        IllegalArgumentException exp = new IllegalArgumentException();
        UnrecognizedNamespaceException exception = new UnrecognizedNamespaceException("Failed.", exp.getCause());
        assertNotNull("Create UnrecognizedNamespaceException incorrectly.", exception);
        assertEquals("Create UnrecognizedNamespaceException incorrectly.", "Failed.", exception.getMessage());
        assertEquals("Create UnrecognizedNamespaceException incorrectly.", exp.getCause(), exception.getCause());
    }

}
