/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import junit.framework.TestCase;


/**
 * <p>
 * Unit test cases for <code>InvalidContentTypeException</code>.
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
public class InvalidContentTypeExceptionUnitTest extends TestCase {
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
    public void testInvalidContentTypeException1() {
        InvalidContentTypeException ce = new InvalidContentTypeException(ERROR_MESSAGE);

        assertNotNull("Unable to instantiate InvalidContentTypeException.", ce);
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
    public void testInvalidContentTypeException2() {
        Exception cause = new Exception();
        InvalidContentTypeException ce = new InvalidContentTypeException(ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate InvalidContentTypeException.", ce);
        assertEquals("Cause is not properly propagated to super class.", cause, ce.getCause());
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     *
     * <p>
     * Verifies InvalidContentTypeException subclasses RequestParsingException.
     * </p>
     */
    public void testInvalidContentTypeExceptionInheritance1() {
        assertTrue("InvalidContentTypeException does not subclass RequestParsingException.",
            new InvalidContentTypeException(ERROR_MESSAGE) instanceof RequestParsingException);
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     *
     * <p>
     * Verifies InvalidContentTypeException subclasses RequestParsingException.
     * </p>
     */
    public void testInvalidContentTypeExceptionInheritance2() {
        assertTrue("InvalidContentTypeException does not subclass RequestParsingException.",
            new InvalidContentTypeException(ERROR_MESSAGE, new Exception()) instanceof RequestParsingException);
    }
}
