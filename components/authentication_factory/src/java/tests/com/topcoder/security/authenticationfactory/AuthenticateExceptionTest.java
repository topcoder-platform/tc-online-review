/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * This class contains the unit tests for AuthenticateException.java.</p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class AuthenticateExceptionTest extends TestCase {

    /**
     * <p>
     * Creates a test suite of the tests contained in this class.</p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(AuthenticateExceptionTest.class);
    } // end suite()

    /**
     * <p>
     * Tests accuracy of the method AuthenticateException(String message).</p>
     *
     */
    public void testAuthenticateExceptionTwoAccuracy() {
        AuthenticateException ae = new AuthenticateException("ae");
        assertTrue(ae instanceof BaseException);
        assertTrue(ae.getMessage().indexOf("ae") != -1);
    } // end testAuthenticateExceptionTwoAccuracy()

    /**
     * <p>
     * Tests accuracy of the method
     * AuthenticateException(String message, Throwable cause).</p>
     *
     */
    public void testAuthenticateExceptionFourAccuracy() {
        AuthenticateException ae =
            new AuthenticateException("ae", new IllegalArgumentException());

        assertTrue(ae.getMessage().indexOf("ae") != -1);
        assertTrue(ae.getCause() instanceof IllegalArgumentException);
    } // end testAuthenticateExceptionFourAccuracy()
} // end AuthenticateExceptionTest
