/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.security.authenticationfactory.http.basicimpl.HTTPBasicAuthenticator;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;

/**
 * <p>
 * This class contains the unit tests for HTTPBasicAuthenticator.</p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class HTTPBasicAuthenticatorTest extends TestCase {
    /**
     * <p>Reprensents the config filename.</p>
     */
    private static final String LOCATION = "httpauth.xml";

    /**
     * <p>Represents the namespace of config.</p>
     */
    private static final String NAMESPACE = "com.topcoder.security.http.HTTPBasicAuthenticator";

    /**
     * The key in Principal.
     */
    private static final String USER = "UserName";

    /**
     * The key in Principal.
     */
    private static final String PWD = "Pwd";

    /**
     * <p>
     * Represents an instance of HTTPBasicAuthenticator to be used for testing.
     * </p>
     */
    private HTTPBasicAuthenticator httpAuth = null;


    /**
     *<p>
     * Creates a test suite of the tests contained in this class.</p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(HTTPBasicAuthenticatorTest.class);
    }

    /**
     * <p>Setup the fixture.</p>
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        ConfigHelper.cleanAndLoadConfig(LOCATION);
        httpAuth = new HTTPBasicAuthenticator(NAMESPACE);
    }

    /**
     * Clean up for each test cases.
     */
    protected void tearDown() {
        ConfigHelper.clearAllNamespace();
    }

    /**
     *<p>
     * Tests constructor HTTPBasicAuthenticator with null parameter,
     * will throw NullPointerException.
     * </p>
     * @throws ConfigurationException to JUnit
     */
    public void testHTTPBasicAuthenticatorNPE() throws ConfigurationException {
        try {
            new HTTPBasicAuthenticator(null);
            fail("null 'namespace' is not acceptable, will throw NullPointerException.");
        } catch (NullPointerException npe) {
            // pass
        }
    }

    /**
     *<p>
     * Tests constructor HTTPBasicAuthenticator with empty parameter,
     * will throw IllegalArgumentException.
     * </p>
     * @throws ConfigurationException to JUnit
     */
    public void testHTTPBasicAuthenticatorIAE() throws ConfigurationException {
        try {
            new HTTPBasicAuthenticator(" ");
            fail("empty 'namespace' is not acceptable, will throw IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // pass
        }

    }

    /**
     *<p>
     * Tests constructor HTTPBasicAuthenticator with non-exist namespace,
     * will throw ConfigurationException.
     * </p>
     */
    public void testHTTPBasicAuthenticatorCE() {
        try {
            new HTTPBasicAuthenticator("com.topcoder.security.http.blahblah");
            fail("non-exist namespace, will throw ConfigurationException.");
        } catch (ConfigurationException ce) {
            // pass
        }

    }

    /**
     * <p>Test DoAuthenticate with null parameter, should throw NullPointerException.</p>
     * @throws AuthenticateException to JUnit
     */
    public void testDoAuthenticateNPE() throws AuthenticateException {
        try {
            httpAuth.authenticate(null);
            fail("null 'principal' is not acceptable, should throw NullPointerException");
        } catch (NullPointerException npe) {
            // pass
        }
    }

    /**
     * <p>
     * Test DoAuthenticate with a Principal providing a port which is not serviced by a target host and expects the
     * <code>AuthenticateException</code> to be thrown.
     * </p>
     */
    public void testDoAuthenticateAE() {
        Principal principal = new Principal("ID");
        principal.addMapping("port", new Integer(47354));
        principal.addMapping(USER, "blah");
        principal.addMapping(PWD, "blah".toCharArray());

        try {
            httpAuth.authenticate(principal);
            fail("AuthenticateException should be thrown since an error occurs at authentication implementation level");
        } catch (AuthenticateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("AuthenticateException was expected but the original exception is : " + e);
        }
    }
    /**
     * <p>Test DoAuthenticate with InvalidPrincipal values, should throw InvalidPrincipalException.</p>
     * @throws AuthenticateException to JUnit
     */
    public void testDoAuthenticateIPE() throws AuthenticateException {
        Principal p = new Principal(new Object());

        try {
            p.clearMappings();
            // invalid HTTPBasicAuthenticator.FILE_KEY
            p.addMapping(HTTPBasicAuthenticator.FILE_KEY, " ");
            p.addMapping(USER, "user");
            p.addMapping(PWD, "pass".toCharArray());
            httpAuth.authenticate(p);
            fail("FILE_KEY should not be empty");
        } catch (InvalidPrincipalException ipe) {
            System.out.println(ipe);
            // pass
        }

        try {
            p.clearMappings();
            // invalid HTTPBasicAuthenticator.HOST_KEY
            p.addMapping(HTTPBasicAuthenticator.HOST_KEY, "localhost:" + ConfigHelper.getPort());
            p.addMapping(USER, "user");
            p.addMapping(PWD, "pass".toCharArray());
            httpAuth.authenticate(p);
            fail("HOST_KEY should not contain port");
        } catch (InvalidPrincipalException ipe) {
            // pass
            System.out.println(ipe);
        }

        try {
            p.clearMappings();
            // invalid HTTPBasicAuthenticator.PROTOCOL_KEY
            p.addMapping(HTTPBasicAuthenticator.PROTOCOL_KEY, "https");
            p.addMapping(USER, "user");
            p.addMapping(PWD, "pass".toCharArray());
            httpAuth.authenticate(p);
            fail("PROTOCOL_KEY must be 'http'");
        } catch (InvalidPrincipalException ipe) {
            // pass
            System.out.println(ipe);
        }

        try {
            p.clearMappings();
            // invalid HTTPBasicAuthenticator.PORT_KEY, must be integer
            p.addMapping(HTTPBasicAuthenticator.PORT_KEY, new String(" "));
            p.addMapping(USER, "user");
            p.addMapping(PWD, "pass".toCharArray());
            httpAuth.authenticate(p);
            fail("PORT_KEY must be Integer");
        } catch (InvalidPrincipalException ipe) {
            // pass
            System.out.println(ipe);
        }

        try {
            p.clearMappings();
            // invalid HTTPBasicAuthenticator.PORTL_KEY, must &gt;= -1
            p.addMapping(HTTPBasicAuthenticator.PORT_KEY, new Integer(-2));
            p.addMapping(USER, "user");
            p.addMapping(PWD, "pass".toCharArray());
            httpAuth.authenticate(p);
            fail("PORT_KEY must be >= -1");
        } catch (InvalidPrincipalException ipe) {
            // pass
            System.out.println(ipe);
        }

        try {
            p.clearMappings();
            // invalid HTTPBasicAuthenticator.REQUESTPROPERTIES_KEY, must be a map
            p.addMapping(HTTPBasicAuthenticator.REQUESTPROPERTIES_KEY, new Object());
            p.addMapping(USER, "user");
            p.addMapping(PWD, "pass".toCharArray());
            httpAuth.authenticate(p);
            fail("REQUESTPROPERTIES_KEY must be a map");
        } catch (InvalidPrincipalException ipe) {
            // pass
            System.out.println(ipe);
        }

        try {
            p.clearMappings();
            // invalid HTTPBasicAuthenticator.PASSWORD_KEY, must be a array of char
            p.addMapping(PWD, "pass");
            p.addMapping(USER, "user");
            httpAuth.authenticate(p);
            fail("PASSWORD_KEY must be a array of char");
        } catch (InvalidPrincipalException ipe) {
            // pass
            System.out.println(ipe);
        }

        try {
            p.clearMappings();
            // invalid HTTPBasicAuthenticator.USER_NAME_KEY, must be non-null, non-empty String
            p.addMapping(USER, " ");
            p.addMapping(PWD, "pass".toCharArray());
            httpAuth.authenticate(p);
            fail("USER_NAME_KEY, must be non-empty String");
        } catch (InvalidPrincipalException ipe) {
            // pass
            System.out.println(ipe);
        }
    }

    /**
     * <p>Test DoAuthenticate with some key missing, should throw InvalidPrincipalException.</p>
     * @throws ConfigManagerException to JUnit
     * @throws ConfigurationException to JUnit
     * @throws AuthenticateException to JUnit
     */
    public void testDoAuthenticateMPKE() throws ConfigManagerException, ConfigurationException, AuthenticateException {
        ConfigHelper.clearAllNamespace();
        ConfigManager cm = ConfigManager.getInstance();
        cm.add("missingkey.xml");
        Authenticator missingAuth = new HTTPBasicAuthenticator(NAMESPACE);

        Principal p = new Principal(new Object());

        try {
            // missing HOST_KEY
            p.clearMappings();
            p.addMapping(HTTPBasicAuthenticator.PROTOCOL_KEY, "http");
            p.addMapping(HTTPBasicAuthenticator.PORT_KEY, new Integer(ConfigHelper.getPort()));
            p.addMapping(HTTPBasicAuthenticator.FILE_KEY, "/webapp1");
            p.addMapping(USER, "user");
            p.addMapping(PWD, "pass".toCharArray());
            missingAuth.authenticate(p);
            fail("miss HOST_KEY");
        } catch (MissingPrincipalKeyException mpke) {
            System.out.println(mpke.getKey());
        }

        try {
            // missing FILE_KEY
            p.clearMappings();
            p.addMapping(HTTPBasicAuthenticator.PROTOCOL_KEY, "http");
            p.addMapping(HTTPBasicAuthenticator.PORT_KEY, new Integer(ConfigHelper.getPort()));
            p.addMapping(HTTPBasicAuthenticator.HOST_KEY, "localhost");
            p.addMapping(USER, "user");
            p.addMapping(PWD, "pass".toCharArray());
            missingAuth.authenticate(p);
            fail("miss FILE_KEY");
        } catch (MissingPrincipalKeyException mpke) {
            System.out.println(mpke.getKey());
        }

        try {
            // missing USER_NAME_KEY
            p.clearMappings();
            p.addMapping(HTTPBasicAuthenticator.PROTOCOL_KEY, "http");
            p.addMapping(HTTPBasicAuthenticator.PORT_KEY, new Integer(ConfigHelper.getPort()));
            p.addMapping(HTTPBasicAuthenticator.HOST_KEY, "localhost");
            p.addMapping(HTTPBasicAuthenticator.FILE_KEY, "/webapp1");
            p.addMapping(PWD, "pass".toCharArray());
            missingAuth.authenticate(p);
            fail("miss USER_NAME_KEY");
        } catch (MissingPrincipalKeyException mpke) {
            System.out.println(mpke.getKey());
        }

        try {
            // missing PASSWORD_KEY
            p.clearMappings();
            p.addMapping(HTTPBasicAuthenticator.PROTOCOL_KEY, "http");
            p.addMapping(HTTPBasicAuthenticator.PORT_KEY, new Integer(ConfigHelper.getPort()));
            p.addMapping(HTTPBasicAuthenticator.HOST_KEY, "localhost");
            p.addMapping(HTTPBasicAuthenticator.FILE_KEY, "/webapp1");
            p.addMapping(USER, "user");
            missingAuth.authenticate(p);
            fail("miss PASSWORD_KEY");
        } catch (MissingPrincipalKeyException mpke) {
            System.out.println(mpke.getKey());
        }
    }

    /**
     * DoAuthenticate accuracy test.
     * @throws AuthenticateException to JUnit.
     */
    public void testDoAuthenticate() throws AuthenticateException {
        Principal p = new Principal(new Object());

        // change the username and password
        p.addMapping(HTTPBasicAuthenticator.FILE_KEY, "/webapp1/index.html");
        p.addMapping(USER, "user1");
        p.addMapping(PWD, "pass1".toCharArray());

        com.topcoder.security.authenticationfactory.Response res = httpAuth.authenticate(p);
        assertEquals(res.isSuccessful(), true);
    }

    /**
     * DoAuthenticate test, which will authenticate failed.
     * @throws AuthenticateException to JUnit.
     */
    public void testDoAuthenticate2() throws AuthenticateException {
        Principal p = new Principal(new Object());

        // change the username and password with non-exist username
        p.addMapping(HTTPBasicAuthenticator.FILE_KEY, "/webapp1/index.html");
        p.addMapping(USER, "nouser");
        p.addMapping(PWD, "nouser".toCharArray());

        com.topcoder.security.authenticationfactory.Response res = httpAuth.authenticate(p);
        assertEquals(res.isSuccessful(), false);
    }

}
