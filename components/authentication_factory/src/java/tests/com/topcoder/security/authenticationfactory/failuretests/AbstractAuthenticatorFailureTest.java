/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */

package com.topcoder.security.authenticationfactory.failuretests;

import com.topcoder.security.authenticationfactory.AbstractAuthenticator;
import com.topcoder.security.authenticationfactory.InvalidPrincipalException;
import com.topcoder.security.authenticationfactory.MissingPrincipalKeyException;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.http.basicimpl.HTTPBasicAuthenticator;
import com.topcoder.util.config.ConfigManager;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;

/**
 * <p>A failure test for <code>AbstractAuthenticator</code> class. Tests the proper handling of invalid input data by
 * the methods. Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class AbstractAuthenticatorFailureTest extends FailureTestCase {
    /**
     * <p>A <code>String</code> providing the configuration namespace that is used to configure the instance of tested
     * class.</p>
     */
    public static final String NAMESPACE = "com.topcoder.security.http.HTTPBasicAuthenticator";

    /**
     * <p>An instance of <code>AbstractAuthenticator</code> which is tested.</p>
     */
    private AbstractAuthenticator testedInstance = null;

    /**
     * <p>Gets the test suite for <code>AbstractAuthenticator</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>AbstractAuthenticator</code> class.
     */
    public static Test suite() {
        return new TestSuite(AbstractAuthenticatorFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        releaseNamespaces();

        loadConfiguration(new File(new File(FAILURE_ROOT, "authenticator"), "Good.xml"));

        testedInstance = new AbstractAuthenticatorSubclass(NAMESPACE);
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        testedInstance = null;
        releaseNamespaces();
    }

    /**
     * <p>Tests the <code>AbstractAuthenticator(String)</code> constructor for proper handling invalid input data.
     * Passes the <code>null</code> namespace and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_1() {
        try {
            new AbstractAuthenticatorSubclass(null);
            fail("NullPointerException should be thrown in response to NULL namespace");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>AbstractAuthenticator(String)</code> constructor for proper handling invalid input data.
     * Passes the zero-length namespace and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_2() {
        try {
            new AbstractAuthenticatorSubclass("");
            fail("IllegalArgumentException should be thrown in response to zero-length namespace");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>AbstractAuthenticator(String)</code> constructor for proper handling invalid input data.
     * Passes the empty namespace and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_3() {
        try {
            new AbstractAuthenticatorSubclass("        ");
            fail("IllegalArgumentException should be thrown in response to empty namespace");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>authenticate(Principal)</code> method for proper handling invalid input data. Passes  the
     * <code>null</code> principal and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testAuthenticate_Principal_1() {
        try {
            testedInstance.authenticate(null);
            fail("NullPointerException should be thrown in response to NULL principal");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }
}
