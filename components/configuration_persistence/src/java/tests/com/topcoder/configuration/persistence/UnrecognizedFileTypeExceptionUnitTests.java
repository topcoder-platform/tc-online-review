/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.configuration.persistence;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test for the UnrecognizedFileTypeException class.
 * </p>
 *
 * @author rainday
 * @version 1.0
 */
public class UnrecognizedFileTypeExceptionUnitTests extends TestCase {

    /**
     * <p>
     * Accuracy test the <code>UnrecognizedFileTypeException(String, FileType)</code> constructor.
     * UnrecognizedFileTypeException instance should be created.
     * </p>
     */
    public void testUnrecognizedFileTypeExceptionString() {
        UnrecognizedFileTypeException exception = new UnrecognizedFileTypeException("Failed.", null);
        assertNotNull("Create UnrecognizedFileTypeException incorrectly.", exception);
        assertEquals("Create UnrecognizedFileTypeException incorrectly.", "Failed.", exception.getMessage());
        assertEquals("Create UnrecognizedFileTypeException incorrectly.", null, exception.getUnrecognizedType());
    }

    /**
     * <p>
     * Accuracy test the <code>getUnrecognizedType()</code> method. unrecognizedType field should
     * be returned.
     * </p>
     */
    public void testUnrecognizedFileTypeExceptionStringThrowable() {
        String testType = ".tc";
        UnrecognizedFileTypeException exception = new UnrecognizedFileTypeException("Failed.", testType);
        assertEquals("Create UnrecognizedFileTypeException incorrectly.", testType, exception.getUnrecognizedType());
    }

}
