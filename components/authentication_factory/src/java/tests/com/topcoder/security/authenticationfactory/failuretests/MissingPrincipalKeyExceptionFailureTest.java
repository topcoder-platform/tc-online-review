/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */

package com.topcoder.security.authenticationfactory.failuretests;

import com.topcoder.security.authenticationfactory.MissingPrincipalKeyException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>A failure test for <code>MissingPrincipalKeyException</code> class. Tests the proper handling of invalid input
 * data by the methods. Passes the invalid arguments to the methods and expects the appropriate exception to be
 * thrown.</p>
 *
 * @author isv
 * @version 1.0
 * @since Authentication Factory 2.0
 */
public class MissingPrincipalKeyExceptionFailureTest extends TestCase {

    /**
     * <p>Gets the test suite for <code>MissingPrincipalKeyException</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>MissingPrincipalKeyException</code> class.
     */
    public static Test suite() {
        return new TestSuite(MissingPrincipalKeyExceptionFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
    }

    /**
     * <p>Tests the <code>MissingPrincipalKeyException(String)</code> constructor for proper handling invalid input
     * data. Passes the <code>null</code> key and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_1() {
        try {
            new MissingPrincipalKeyException(null);
            fail("NullPointerException should be thrown in response to NULL key");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>MissingPrincipalKeyException(String)</code> constructor for proper handling invalid input
     * data. Passes the zero-length key and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_2() {
        try {
            new MissingPrincipalKeyException("");
            fail("IllegalArgumentException should be thrown in response to zero-length key");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>MissingPrincipalKeyException(String)</code> constructor for proper handling invalid input
     * data. Passes the empty key and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_3() {
        try {
            new MissingPrincipalKeyException("             ");
            fail("IllegalArgumentException should be thrown in response to empty key");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }
}
