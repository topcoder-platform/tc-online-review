/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */



package com.cronos.onlinereview.login;

import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test cases for <code>AuthCookieManagementException</code>.
 * </p>
 * <p>
 * This class is pretty simple. The test cases simply verifies the exception can be instantiated with the error message
 * and cause properly propagated, and that it comes with correct inheritance.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class AuthCookieManagementExceptionTest extends TestCase {

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
    public void testAuthCookieManagementException1() {
        AuthCookieManagementException ce = new AuthCookieManagementException(ERROR_MESSAGE);

        assertNotNull("Unable to instantiate AuthCookieManagementException.", ce);
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
    public void testAuthCookieManagementException2() {
        Exception cause = new Exception();
        AuthCookieManagementException ce = new AuthCookieManagementException(ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate AuthCookieManagementException.", ce);
        assertEquals("Cause is not properly propagated to super class.", cause, ce.getCause());
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     * <p>
     * Verifies AuthCookieManagementException subclasses BaseException.
     * </p>
     */
    public void testAuthCookieManagementExceptionInheritance() {
        assertTrue("AuthCookieManagementException does not subclass BaseException.",
                   new AuthCookieManagementException(ERROR_MESSAGE) instanceof BaseException);
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     * <p>
     * Verifies AuthCookieManagementException subclasses BaseException.
     * </p>
     */
    public void testAuthCookieManagementExceptionInheritance2() {
        assertTrue("AuthCookieManagementException does not subclass BaseException.",
                   new AuthCookieManagementException(ERROR_MESSAGE, new Exception()) instanceof BaseException);
    }
}
