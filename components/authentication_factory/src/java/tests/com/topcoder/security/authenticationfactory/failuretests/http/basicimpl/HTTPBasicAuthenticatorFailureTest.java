/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */

package com.topcoder.security.authenticationfactory.failuretests.http.basicimpl;

import com.topcoder.security.authenticationfactory.InvalidPrincipalException;
import com.topcoder.security.authenticationfactory.MissingPrincipalKeyException;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.AuthenticateException;
import com.topcoder.security.authenticationfactory.failuretests.FailureTestCase;
import com.topcoder.security.authenticationfactory.http.basicimpl.HTTPBasicAuthenticator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;

/**
 * <p>A failure test for <code>HTTPBasicAuthenticator</code> class. Tests the proper handling of invalid input data by
 * the methods. Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class HTTPBasicAuthenticatorFailureTest extends FailureTestCase {
    /**
     * <p>A <code>String</code> providing the configuration namespace that is used to configure the instance of tested
     * class.</p>
     */
    public static final String NAMESPACE = "com.topcoder.security.http.HTTPBasicAuthenticator";

    /**
     * <p>An instance of <code>HTTPBasicAuthenticator</code> which is tested.</p>
     */
    private HTTPBasicAuthenticatorSubclass testedInstance = null;

    /**
     * <p>Gets the test suite for <code>HTTPBasicAuthenticator</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>HTTPBasicAuthenticator</code> class.
     */
    public static Test suite() {
        return new TestSuite(HTTPBasicAuthenticatorFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        releaseNamespaces();

        loadConfiguration(new File(new File(FAILURE_ROOT, "authenticator"), "Good.xml"));

        testedInstance = new HTTPBasicAuthenticatorSubclass(NAMESPACE);
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
     * <p>Tests the <code>HTTPBasicAuthenticator(String)</code> constructor for proper handling invalid input data.
     * Passes the <code>null</code> namespace and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_1() {
        try {
            new HTTPBasicAuthenticator(null);
            fail("NullPointerException should be thrown in response to NULL namespace");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>HTTPBasicAuthenticator(String)</code> constructor for proper handling invalid input data.
     * Passes the zero-length namespace and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_2() {
        try {
            new HTTPBasicAuthenticator("");
            fail("IllegalArgumentException should be thrown in response to zero-length namespace");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>HTTPBasicAuthenticator(String)</code> constructor for proper handling invalid input data.
     * Passes the empty namespace and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_3() {
        try {
            new HTTPBasicAuthenticator("        ");
            fail("IllegalArgumentException should be thrown in response to empty namespace");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>doAuthenticate(Principal)</code> method for proper handling invalid input data. Passes the
     * <code>null</code> principal and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testDoAuthenticate_Principal_1() {
        try {
            testedInstance.doAuthenticate(null);
            fail("NullPointerException should be thrown in response to NULL principal");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>doAuthenticate(Principal)</code> method for proper handling invalid input data. Passes the
     * <code>Principal</code> missing to provide a required key and expects the <code>MissingPrincipalKeyException
     * </code> to be thrown.</p>
     */
    public void testDoAuthenticate_Principal_2() {
        Principal principal = new Principal("ID");

        try {
            testedInstance.doAuthenticate(principal);
            fail("MissingPrincipalKeyException should be thrown since a required key is missing");
        } catch (MissingPrincipalKeyException e) {
            // expected behavior
        } catch (Exception e) {
            fail("MissingPrincipalKeyException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>doAuthenticate(Principal)</code> method for proper handling invalid input data. Passes the
     * <code>Principal</code> providing an invalid key and expects the <code>InvalidPrincipalException</code> to be
     * thrown.</p>
     */
    public void testDoAuthenticate_Principal_3() {
        Principal principal = new Principal("ID");
        principal.addMapping("port", "port");
        try {
            testedInstance.doAuthenticate(principal);
            fail("InvalidPrincipalException should be thrown since a principal does not provide a valid value for key");
        } catch (InvalidPrincipalException e) {
            // expected behavior
        } catch (Exception e) {
            fail("InvalidPrincipalException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>doAuthenticate(Principal)</code> method for proper handling invalid input data. Passes the
     * principal providing a port which is not serviced by a target host and expects the <code>AuthenticateException
     * </code> to be thrown.</p>
     */
    public void testDoAuthenticate_Principal_4() {
        Principal principal = new Principal("ID");
        principal.addMapping("port", new Integer(47354));
        principal.addMapping("User", "isv");
        principal.addMapping("Pwd", "failure".toCharArray());

        try {
            testedInstance.doAuthenticate(principal);
            fail("AuthenticateException should be thrown since an error occurs at authentication implementation level");
        } catch (AuthenticateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("AuthenticateException was expected but the original exception is : " + e);
        }
    }

}
