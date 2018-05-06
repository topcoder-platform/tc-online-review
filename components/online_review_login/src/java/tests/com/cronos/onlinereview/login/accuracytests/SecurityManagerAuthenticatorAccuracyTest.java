/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.accuracytests;

import servletunit.struts.MockStrutsTestCase;

import com.cronos.onlinereview.login.ConfigurationException;
import com.cronos.onlinereview.login.authenticator.SecurityManagerAuthenticator;
import com.topcoder.security.TCSubject;
import com.topcoder.security.UserPrincipal;
import com.topcoder.security.authenticationfactory.AuthenticateException;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;
import com.topcoder.security.login.LoginRemoteHome;


/**
 * Accuracy tests for class <code>SecurityManagerAuthenticator</code>.
 *
 * @author PE
 * @version 1.0
 */
public class SecurityManagerAuthenticatorAccuracyTest extends MockStrutsTestCase {
    /** Represents the <code>SecurityManagerAuthenticator</code> instance for testing. */
    private MockSecurityManagerAuthenticator authenticator = null;

    /**
     * <p>
     * Sets up the test environment. The test instance is created.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        AccuracyTestHelper.clearConfig();

        AccuracyTestHelper.addConfig();
        authenticator = new MockSecurityManagerAuthenticator(SecurityManagerAuthenticator.class.getName());
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        AccuracyTestHelper.clearConfig();
        super.tearDown();
    }

    /**
     * <p>
     * Tests the accuracy of constructor SecurityManagerAuthenticator(String).
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testSecurityManagerAuthenticator_Accuracy() throws Exception {
        assertTrue("The loginRemoteHome is not properly set.",
            AccuracyTestHelper.getPrivateField(authenticator.getClass().getSuperclass(), authenticator,
                "loginRemoteHome") instanceof LoginRemoteHome);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>doAuthenticate(Principal)</code> with valid username/password combination.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testDoAuthenticate_SuccessfulAccuracy() throws Exception {
        String name = "name";
        String password = "password";
        UserPrincipal userPrincipal = AccuracyTestHelper.createUser(name, password);

        Principal principal = new Principal("dummy id");
        principal.addMapping("userName", name);
        principal.addMapping("password", password);

        Response response = authenticator.doAuthenticate(principal);
        assertTrue("Authenticate should be successful.", response.isSuccessful());
        assertEquals("The message should be Succeeded.", "Succeeded", response.getMessage());

        AccuracyTestHelper.removeUser(userPrincipal, (TCSubject) response.getDetails());
    }

    /**
     * <p>
     * Tests the accuracy of method <code>doAuthenticate(Principal)</code> with invalid username/password combination.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testDoAuthenticate_UnsuccessfulAccuracy() throws Exception {
        String name = "name";
        String password = "password";
        UserPrincipal userPrincipal = AccuracyTestHelper.createUser(name, password);

        Principal principal = new Principal("dummy id");
        principal.addMapping("userName", name);
        principal.addMapping("password", "invalid");

        Response response = authenticator.doAuthenticate(principal);
        assertFalse("Authenticate should be unsuccessful.", response.isSuccessful());
        assertEquals("The message should be Failed.", "Username and/or password are incorrect", response.getMessage());

        AccuracyTestHelper.removeUser(userPrincipal, (TCSubject) response.getDetails());
    }

    class MockSecurityManagerAuthenticator extends SecurityManagerAuthenticator {
        /**
         * A mock constructor.
         *
         * @param namespace the namespace
         *
         * @throws ConfigurationException any configuration exception.
         * @throws com.topcoder.security.authenticationfactory.ConfigurationException any configuration exception from
         *         the authentication component.
         */
        public MockSecurityManagerAuthenticator(String namespace)
            throws ConfigurationException, com.topcoder.security.authenticationfactory.ConfigurationException {
            super(namespace);
        }

        /**
         * A mock method.
         *
         * @param principal the principal to authenticate (expected to be non-null).
         *
         * @return the authentication response.
         */
        public Response doAuthenticate(Principal principal) throws AuthenticateException {
            return super.doAuthenticate(principal);
        }
    }
}
