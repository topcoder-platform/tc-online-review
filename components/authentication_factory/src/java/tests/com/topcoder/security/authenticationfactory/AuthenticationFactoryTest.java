/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import com.topcoder.security.authenticationfactory.http.basicimpl.HTTPBasicAuthenticator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This class contains the unit tests for AuthenticationFactory.</p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class AuthenticationFactoryTest extends TestCase {

    /**
     * <p>The AuthenticationFactory instance.</p>
     */
    private AuthenticationFactory factory = null;

    /**
     * <p>
     * Creates a test suite of the tests contained in this class.</p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(AuthenticationFactoryTest.class);
    }

    /**
     * Setup up the fixture.
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        ConfigHelper.cleanAndLoadConfig("factory.xml");
        factory = AuthenticationFactory.getInstance();
    }

    /**
     * Clean up for each test cases.
     */
    protected void tearDown() {
        ConfigHelper.clearAllNamespace();
    }

    /**
     * Test getAuthenticator with null parameter, should throw NullPointerException.
     */
    public void testGetAuthenticatorNPE() {
        try {
            factory.getAuthenticator(null);
            fail("should throw NullPointerException");
        } catch (NullPointerException npe) {
            // pass
        }
    }

    /**
     * Test getAuthenticator with empty String, should throw IllegalArgumentException.
     */
    public void testGetAuthenticatorIAE() {
        try {
            factory.getAuthenticator(" ");
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Test getAuthenticator.
     * @throws AuthenticateException to JUnit
     */
    public void testGetAuthenticator() throws AuthenticateException {
        Authenticator auth = factory.getAuthenticator("http");
        Principal p = new Principal(new Object());

        // change the username and password
        p.addMapping(HTTPBasicAuthenticator.FILE_KEY, "/webapp1/index.html");
        p.addMapping(HTTPBasicAuthenticator.HOST_KEY, "localhost");
        p.addMapping(HTTPBasicAuthenticator.PORT_KEY, new Integer(ConfigHelper.getPort()));
        p.addMapping("UserName", "user1");
        p.addMapping("Pwd", "pass1".toCharArray());

        com.topcoder.security.authenticationfactory.Response res = auth.authenticate(p);
        assertEquals(res.isSuccessful(), true);
    }

    /**
     * Test getAuthenticator with non-exist Authenticator.
     * @throws AuthenticateException to JUnit
     */
    public void testGetAuthenticator2() throws AuthenticateException {
        Authenticator auth = factory.getAuthenticator("blah");
        assertNull("should get a null Authenticator", auth);
    }

    /**
     * Test refresh functionality.
     *
     * @throws ConfigurationException
     *             to JUnit.
     *
     */
    public void testRefresh() throws ConfigurationException {
        final String auth1 = "http";
        final String auth2 = "http2";

        final String auth3 = "https";
        final String auth4 = "https2";

        // in original config file, there are two authenticator 'http' and 'http2'
        assertNotNull(factory.getAuthenticator(auth1));
        assertNotNull(factory.getAuthenticator(auth2));
        assertNull(factory.getAuthenticator(auth3));
        assertNull(factory.getAuthenticator(auth4));

        // reload config file, there are two authenticator 'https' and 'https2' in this
        // config file
        ConfigHelper.cleanAndLoadConfig("refresh.xml");
        factory.refresh();
        // after refresh
        assertNull(factory.getAuthenticator(auth1));
        assertNull(factory.getAuthenticator(auth2));
        assertNotNull(factory.getAuthenticator(auth3));
        assertNotNull(factory.getAuthenticator(auth4));
    }
}