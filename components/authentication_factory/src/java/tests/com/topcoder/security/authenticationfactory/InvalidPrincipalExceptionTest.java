/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.errorhandling.BaseRuntimeException;

/**
 * <p>
 * This class contains the unit tests for InvalidPrincipalException.</p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class InvalidPrincipalExceptionTest extends TestCase {

    /**
     *<p>
     * Creates a test suite of the tests contained in this class.</p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(InvalidPrincipalExceptionTest.class);
    } // end suite()

    /**
     *<p>
     * Tests accuracy of the method InvalidPrincipalException(String message).</p>
     *
     */
    public void testInvalidPrincipalExceptionTwoAccuracy() {
        InvalidPrincipalException ipe = new InvalidPrincipalException("ipe");
        assertTrue(ipe instanceof BaseRuntimeException);
        assertTrue(ipe.getMessage().indexOf("ipe") != -1);
    } // end testInvalidPrincipalExceptionTwoAccuracy()
} // end InvalidPrincipalExceptionTest
