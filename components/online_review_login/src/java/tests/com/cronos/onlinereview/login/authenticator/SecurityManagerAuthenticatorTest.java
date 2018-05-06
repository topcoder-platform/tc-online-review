/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */



package com.cronos.onlinereview.login.authenticator;

import com.cronos.onlinereview.login.ConfigurationException;
import com.cronos.onlinereview.login.TestUtil;

import com.topcoder.security.UserPrincipal;
import com.topcoder.security.authenticationfactory.InvalidPrincipalException;
import com.topcoder.security.authenticationfactory.MissingPrincipalKeyException;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;

import servletunit.struts.MockStrutsTestCase;

/**
 * Unit tests for <code>SecurityManagerAuthenticator</code>.
 *
 * @author maone
 * @version 1.0
 */
public class SecurityManagerAuthenticatorTest extends MockStrutsTestCase {

    /**
     * A <code>SecurityManagerAuthenticator</code> instance to test against.
     */
    private SecurityManagerAuthenticator authenticator = null;

    /**
     * Represents a <code>Principal</code> instance created during tests. It should be removed in
     * <code>tearDown()</code> if it is not null.
     */
    private UserPrincipal userPrincipal = null;

    /**
     * Set up.
     * <p>
     * Load the configurations and create the authenticator.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        TestUtil.loadAllConfigurations();
        authenticator = new SecurityManagerAuthenticator(
            "com.cronos.onlinereview.login.authenticator.SecurityManagerAuthenticator");
    }

    /**
     * Tear down. Clear the configurations.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        if (userPrincipal != null) {
            TestUtil.removeUser(userPrincipal);
        }

        TestUtil.clearAllConfigurations();

        super.tearDown();
    }

    /**
     * Test construtor to validate the loginRemoteHome field is initialized.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_Valid() throws Exception {
        assertNotNull("loginRemoteHome shouldn't be null.", TestUtil.getFieldValue(authenticator, "loginRemoteHome"));
    }

    /**
     * Test constructor with null <code>namespace</code> param.
     * <p>
     * It should throw IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_NullNamespace() throws Exception {
        try {
            new SecurityManagerAuthenticator(null);
            fail("Should throw IllegalArgumentException for null namespace.");
        } catch (IllegalArgumentException e) {

            // pass
        }
    }

    /**
     * Test constructor with empty <code>namespace</code> param.
     * <p>
     * It should throw IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_EmptyNamespace() throws Exception {
        try {
            new SecurityManagerAuthenticator("  ");
            fail("Should throw IllegalArgumentException for null namespace.");
        } catch (IllegalArgumentException e) {

            // pass
        }
    }

    /**
     * Test constructor when the given <code>namespace</code> does not exist.
     * <p>
     * It should throw com.topcoder.security.authenticationfactory.ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_NotExistNamespace() throws Exception {
        try {
            new SecurityManagerAuthenticator("namespace.does.not.exist");
            fail("Should throw com.topcoder.security.authenticationfactory.ConfigurationException"
                 + " for invalid namespace.");
        } catch (com.topcoder.security.authenticationfactory.ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test constructor when the given <code>namespace</code> contains invalid context name
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_InvalidContextName() throws Exception {
        try {
            new SecurityManagerAuthenticator(
                "com.cronos.onlinereview.login.authenticator.SecurityManagerAuthenticator.invalid_context_name");
            fail("Should throw com.topcoder.security.authenticationfactory.ConfigurationException"
                 + " for invalid context name.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test constructor when the given <code>namespace</code> doesn't contain login bean name.
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_MissLoginBeanName() throws Exception {
        try {
            new SecurityManagerAuthenticator(
                "com.cronos.onlinereview.login.authenticator.SecurityManagerAuthenticator.miss_loginbean_name");
            fail("Should throw com.topcoder.security.authenticationfactory.ConfigurationException"
                 + " for invalid context name.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test <code>doAuthenticate</code> with valid username/password combination.
     * <p>
     * Successful response should be returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDoAuthenticate_Successful() throws Exception {
        userPrincipal = TestUtil.createUser("myname", "mypw");

        Principal principal = new Principal("dummy id");

        principal.addMapping("userName", "myname");
        principal.addMapping("password", "mypw");

        Response response = authenticator.doAuthenticate(principal);

        assertTrue("Authenticate should be successful.", response.isSuccessful());
        assertEquals("The message should be Succeeded.", "Succeeded", response.getMessage());
    }

    /**
     * Test <code>doAuthenticate</code> with invlaid username/password combination.
     * <p>
     * Unsuccessful response should be returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDoAuthenticate_Unsuccessful() throws Exception {
        userPrincipal = TestUtil.createUser("myname", "mypw");

        Principal principal = new Principal("dummy id");

        principal.addMapping("userName", "myname");
        principal.addMapping("password", "invalidpw");

        Response response = authenticator.doAuthenticate(principal);

        assertFalse("Authenticate should be unsuccessful.", response.isSuccessful());
        assertEquals("The message should be Failed.", "Username and/or password are incorrect", response.getMessage());
    }

    /**
     * Test <code>doAuthenticate</code> when the principal doesn't contain userName.
     * <p>
     * It should throw MissingPrincipalKeyException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDoAuthenticate_MissUsername() throws Exception {
        Principal principal = new Principal("dummy id");

        principal.addMapping("password", "mypw");

        try {
            authenticator.doAuthenticate(principal);
            fail("Should throw MissingPrincipalKeyException for missing userName.");
        } catch (MissingPrincipalKeyException e) {

            // pass
        }
    }

    /**
     * Test <code>doAuthenticate</code> when the principal doesn't contain password.
     * <p>
     * It should throw MissingPrincipalKeyException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDoAuthenticate_MissPassword() throws Exception {
        Principal principal = new Principal("dummy id");

        principal.addMapping("userName", "myname");

        try {
            authenticator.doAuthenticate(principal);
            fail("Should throw MissingPrincipalKeyException for missing password.");
        } catch (MissingPrincipalKeyException e) {

            // pass
        }
    }

    /**
     * Test <code>doAuthenticate</code> when the principal contains invalid userName type.
     * <p>
     * It should throw InvalidPrincipalException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDoAuthenticate_InvalidUsername() throws Exception {
        Principal principal = new Principal("dummy id");

        principal.addMapping("userName", new Integer(1234));

        try {
            authenticator.doAuthenticate(principal);
            fail("Should throw InvalidPrincipalException for invalid userName.");
        } catch (InvalidPrincipalException e) {

            // pass
        }
    }

    /**
     * Test <code>doAuthenticate</code> when the principal contains invalid password type.
     * <p>
     * It should throw InvalidPrincipalException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDoAuthenticate_InvalidPassword() throws Exception {
        Principal principal = new Principal("dummy id");

        principal.addMapping("password", new Double(12.34));

        try {
            authenticator.doAuthenticate(principal);
            fail("Should throw InvalidPrincipalException for invalid password.");
        } catch (InvalidPrincipalException e) {

            // pass
        }
    }
}
