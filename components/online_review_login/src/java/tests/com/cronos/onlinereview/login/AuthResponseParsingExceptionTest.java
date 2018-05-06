/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */



package com.cronos.onlinereview.login;

import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test cases for <code>AuthResponseParsingException</code>.
 * </p>
 * <p>
 * This class is pretty simple. The test cases simply verifies the exception can be instantiated with the error message
 * and cause properly propagated, and that it comes with correct inheritance.
 * </p>
 *
 * @author maone
 * @version 1.0
 */
public class AuthResponseParsingExceptionTest extends TestCase {

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
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testAuthResponseParsingException1() {
        AuthResponseParsingException ce = new AuthResponseParsingException(ERROR_MESSAGE);

        assertNotNull("Unable to instantiate AuthResponseParsingException.", ce);
        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, ce.getMessage());
    }

    /**
     * <p>
     * Creation test.
     * </p>
     * <p>
     * Verifies the error message and the cause are properly propagated.
     * </p>
     */
    public void testAuthResponseParsingException2() {
        Exception cause = new Exception();
        AuthResponseParsingException ce = new AuthResponseParsingException(ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate AuthResponseParsingException.", ce);
        assertEquals("Cause is not properly propagated to super class.", cause, ce.getCause());
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     * <p>
     * Verifies AuthResponseParsingException subclasses BaseException.
     * </p>
     */
    public void testAuthResponseParsingExceptionInheritance() {
        assertTrue("AuthResponseParsingException does not subclass BaseException.",
                   new AuthResponseParsingException(ERROR_MESSAGE) instanceof BaseException);
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     * <p>
     * Verifies AuthResponseParsingException subclasses BaseException.
     * </p>
     */
    public void testAuthResponseParsingExceptionInheritance2() {
        assertTrue("AuthResponseParsingException does not subclass BaseException.",
                   new AuthResponseParsingException(ERROR_MESSAGE, new Exception()) instanceof BaseException);
    }
}
