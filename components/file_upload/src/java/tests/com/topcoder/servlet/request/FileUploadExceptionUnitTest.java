/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;


/**
 * <p>
 * Unit test cases for <code>FileUploadException</code>.
 * </p>
 *
 * <p>
 * This class is pretty simple. The test cases simply verifies the exception can be instantiated with the error message
 * and cause properly propagated, and that it comes with correct inheritance.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class FileUploadExceptionUnitTest extends TestCase {
    /**
     * <p>
     * The error message used for testing.
     * </p>
     */
    private static final String ERROR_MESSAGE = "test error message";

    /**
     * <p>
     * Creation test.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testFileUploadException1() {
        FileUploadException ce = new FileUploadException(ERROR_MESSAGE);

        assertNotNull("Unable to instantiate FileUploadException.", ce);
        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, ce.getMessage());
    }

    /**
     * <p>
     * Creation test.
     * </p>
     *
     * <p>
     * Verifies the error message and the cause are properly propagated.
     * </p>
     */
    public void testFileUploadException2() {
        Exception cause = new Exception();
        FileUploadException ce = new FileUploadException(ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate FileUploadException.", ce);
        assertEquals("Cause is not properly propagated to super class.", cause, ce.getCause());
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     *
     * <p>
     * Verifies FileUploadException subclasses BaseException.
     * </p>
     */
    public void testFileUploadExceptionInheritance1() {
        assertTrue("FileUploadException does not subclass BaseException.",
            new FileUploadException(ERROR_MESSAGE) instanceof BaseException);
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     *
     * <p>
     * Verifies FileUploadException subclasses BaseException.
     * </p>
     */
    public void testFileUploadExceptionInheritance2() {
        assertTrue("FileUploadException does not subclass BaseException.",
            new FileUploadException(ERROR_MESSAGE, new Exception()) instanceof BaseException);
    }
}
