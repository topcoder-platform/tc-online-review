/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.security.authenticationfactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * This class contains the unit tests for AbstractAuthenticator.</p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class AbstractAuthenticatorTest extends TestCase {
    /**
     * The well formed namespace to load authenticator.
     */
    private static final String NAMESPACE = "com.topcoder.security.http.HTTPBasicAuthenticator";

    /**
     * <p>AbstractAuthenticator instance.</p>
     */
    private SubAbstractAuthenticator auth = null;


    /**
     * <p>
     * Creates a test suite of the tests contained in this class.</p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(AbstractAuthenticatorTest.class);
    }

    /**
     * <p>Setup the fixture.</p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        ConfigHelper.clearAllNamespace();
        cm.add("abstractAuthenticate.xml");
        auth = new SubAbstractAuthenticator(NAMESPACE);
    }

    /**
     * Clean up for each test cases.
     */
    protected void tearDown() {
        ConfigHelper.clearAllNamespace();
    }

    /**
     * <p>
     * test constructor with null parameter, should throw NullPointerException.</p>
     * @throws ConfigurationException to JUnit
     */
    public void testAbstractorAuthenticatorNPE() throws ConfigurationException {
        try {
            auth = new SubAbstractAuthenticator(null);
            fail("should throw NullPointerException");
        } catch (NullPointerException npe) {
            // pass
        }
    }

    /**
     * <p>
     * test constructor with null parameter, should throw IllegalArgumentException .</p>
     * @throws ConfigurationException to JUnit
     */
    public void testAbstractorAuthenticatorIAE() throws ConfigurationException {
        try {
            auth = new SubAbstractAuthenticator("  ");
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * test constructor with error config file(a non-exist PrincipalKeyConverter),
     * should throw ConfigurationException.</p>
     */
    public void testAbstractorAuthenticatorCE() {
        try {
            auth = new SubAbstractAuthenticator("com.topcoder.security.http.ErrorAuthenticator");
            fail("should throw ConfigurationException");
        } catch (ConfigurationException ce) {
            // pass
        }
    }

    /**
     * <p>
     * test constructor with error config file(a non-exist CacheFactory class),
     * should throw ConfigurationException.</p>
     */
    public void testAbstractorAuthenticatorCE2() {
        try {
            auth = new SubAbstractAuthenticator("com.topcoder.security.http.Error2Authenticator");
            fail("should throw ConfigurationException");
        } catch (ConfigurationException ce) {
            // pass
        }
    }

    /**
     * <p>
     * test constructor with a non-exist namespace,
     * should throw ConfigurationException.</p>
     */
    public void testAbstractorAuthenticatorCE3() {
        try {
            auth = new SubAbstractAuthenticator("com.topcoder.security.http.blahblah");
            fail("should throw ConfigurationException");
        } catch (ConfigurationException ce) {
            // pass
        }
    }

    /**
     * <p>Test authenticate with null parameter, should throw NullPointerException.</p>
     * @throws ConfigurationException to JUnit
     * @throws AuthenticateException to JUnit
     */
    public void testAuthenticateNPE() throws ConfigurationException, AuthenticateException {
        try {
            auth.authenticate(null);
            fail("should throw NullPointerException");
        } catch (NullPointerException npe) {
            // pass
        }
    }

    /**
     * <p>Test authenticate.</p>
     * @throws ConfigurationException to JUnit
     * @throws AuthenticateException to JUnit
     */
    public void testAuthenticate() throws ConfigurationException, AuthenticateException {
        Principal p = new Principal(new Object());
        Response r = auth.authenticate(p);
        Response r1 = auth.authenticate(p);
        assertSame("these two should be same because of cache", r, r1);
        Response r2 = auth.authenticate(new Principal(new String("ss")));
        assertNotSame("these two should not be same", r2, r1);
    }

    /**
     * Test authenticate which turn off cache.
     *
     * @throws ConfigurationException to JUnit.
     * @throws AuthenticateException to JUnit.
     */
    public void testAutehnticate2() throws ConfigurationException, AuthenticateException {
        AbstractAuthenticator a = new SubAbstractAuthenticator(
                "com.topcoder.security.http.HTTPBasicAuthenticatorNoCache");
        Principal p = new Principal(new Object());
        Response r = a.authenticate(p);
        Response r1 = a.authenticate(p);

        assertNotNull("response should not null", r);
        assertNotNull("response should not null", r1);
        assertNotSame("turned off the cache simple Principal will produce different response", r, r1);
    }
    /**
     * <p>Test get converter functionality.</p>
     */
    public void testGetConverter() {
        PrincipalKeyConverter converter = auth.getConvert();
        assertEquals(converter.convert("username"), "UserName");
        assertEquals(converter.convert("password"), "Pwd");
    }
    /**
     * <p>
     * An inner class inherits from the AbstractAuthenticator class used for testing all the methods of the
     * AbstractAuthenticator class.
     * </p>
     */
    private class SubAbstractAuthenticator extends AbstractAuthenticator {
        /**
         * <p>
         * An empty constructor.
         * </p>
         * @param namespace the namespace to load config file.
         * @throws ConfigurationException if load config failed.
         */
        SubAbstractAuthenticator(String namespace) throws ConfigurationException {
            super(namespace);
        }

        /**
         * <p>
         * Always return new Response(true).
         * </p>
         *
         * @param principal the object to authenticate.
         * @return Response the authenticate response.
         * @throws AuthenticateException if any exception occur during authentication.
         */
        protected Response doAuthenticate(Principal principal) throws AuthenticateException {
            return new Response(true);
        }

        /**
         * get the converter.
         * @return the converter.
         */
        public PrincipalKeyConverter getConvert() {
            return getConverter();
        }
    }
}
