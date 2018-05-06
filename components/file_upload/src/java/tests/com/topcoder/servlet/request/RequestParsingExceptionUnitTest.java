/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import junit.framework.TestCase;


/**
 * <p>
 * Unit test cases for <code>RequestParsingException</code>.
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
public class RequestParsingExceptionUnitTest extends TestCase {
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
    public void testRequestParsingException1() {
        RequestParsingException ce = new RequestParsingException(ERROR_MESSAGE);

        assertNotNull("Unable to instantiate RequestParsingException.", ce);
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
    public void testRequestParsingException2() {
        Exception cause = new Exception();
        RequestParsingException ce = new RequestParsingException(ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate RequestParsingException.", ce);
        assertEquals("Cause is not properly propagated to super class.", cause, ce.getCause());
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     *
     * <p>
     * Verifies RequestParsingException subclasses FileUploadException.
     * </p>
     */
    public void testRequestParsingExceptionInheritance1() {
        assertTrue("RequestParsingException does not subclass FileUploadException.",
            new RequestParsingException(ERROR_MESSAGE) instanceof FileUploadException);
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     *
     * <p>
     * Verifies RequestParsingException subclasses FileUploadException.
     * </p>
     */
    public void testRequestParsingExceptionInheritance2() {
        assertTrue("RequestParsingException does not subclass FileUploadException.",
            new RequestParsingException(ERROR_MESSAGE, new Exception()) instanceof FileUploadException);
    }
}
