/*
 * Copyright (C) 2006 - 2010 TopCoder Inc., All Rights Reserved.
 */



package com.cronos.onlinereview.login;

import com.cronos.onlinereview.login.authenticator.SecurityManagerAuthResponseParser;

import com.topcoder.security.UserPrincipal;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.basic.BasicLog;

import servletunit.struts.MockStrutsTestCase;

import java.io.File;
import java.util.Iterator;

/**
 * Unit tests for <code>LoginActions</code>.
 *
 * @author maone, TCSDEVELOPER
 * @version 1.1
 * @since 1.0
 */
public class LoginActionsTest extends MockStrutsTestCase {

    /**
     * Represents the default namespace used by <code>LoginActions</code>.
     */
    private static final String NAMESPACE = LoginActions.class.getName();

    /**
     * Represents a <code>Principal</code> instance created during tests. It should be removed in
     * <code>tearDown()</code> if it is not null.
     */
    private UserPrincipal principal = null;

    /**
     * Set up.
     * <p>
     * Load all the configurations.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        TestUtil.loadAllConfigurations();
    }

    /**
     * Tear down. Clear the configurations.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        if (principal != null) {
            TestUtil.removeUser(principal);
        }

        TestUtil.clearAllConfigurations();

        super.tearDown();
    }

    /**
     * Test constructor to validate all the fields are initialized properly.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_Valid() throws Exception {
        LoginActions actions = new LoginActions();

        assertTrue("Failed to initialize authenticator.",
                   TestUtil.getFieldValue(actions, "authenticator") instanceof MockAuthenticator);
        assertTrue("Failed to initialize authResponseParser",
                   TestUtil.getFieldValue(actions, "authResponseParser") instanceof SecurityManagerAuthResponseParser);
        assertTrue("Failed to initialize logger.", TestUtil.getFieldValue(actions, "logger") instanceof BasicLog);

    }

    /**
     * Test constructor when the namespace doesn't contain parser configuration.
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_NoParser() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator it = cm.getAllNamespaces(); it.hasNext();) {
            cm.removeNamespace((String) it.next());
        }
        cm.add(new File(TestUtil.TEST_DIR + "LoginActions_no_parser.xml").getAbsolutePath());

        try {
            new LoginActions();
            fail("Should throw ConfigurationException for missing parser.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test constructor when the namespace doesn't contain AuthCookieManager configuration.
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     * @since 1.1
     */
    public void testConstructor_NoAuthCookieManager() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        cm.removeNamespace(NAMESPACE);
        cm.add(new File(TestUtil.TEST_DIR + "LoginActions_no_AuthCookieManager.xml").getAbsolutePath());

        try {
            new LoginActions();
            fail("Should throw ConfigurationException for missing parser.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test constructor when the namespace contains invalid authenticator configuration.
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_InvalidAuthenticator() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        cm.removeNamespace(NAMESPACE);
        cm.add(new File(TestUtil.TEST_DIR + "LoginActions_invalid_authenticator.xml").getAbsolutePath());

        try {
            new LoginActions();
            fail("Should throw ConfigurationException for invalid authenticator.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test constructor when the default namespace doesn't exist.
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_MissNamespace() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        cm.removeNamespace(NAMESPACE);

        try {
            new LoginActions();
            fail("Should throw ConfigurationException for missing namespace.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test <code>login</code> method with valid username/password combination.
     * <p>
     * It should forward to success, and the user id will be put into session.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testLogin_Success() throws Exception {
        principal = TestUtil.createUser("myname", "mypw");

        addRequestParameter("userName", "myname");
        addRequestParameter("password", "mypw");
        addRequestParameter("method", "login");

        setRequestPathInfo("/login");
        actionPerform();

        verifyForward("success");
        assertTrue("The userId should be put into sesseion.", getSession().getAttribute("topcoder") instanceof Long);
        verifyNoActionMessages();
    }

    /**
     * Test <code>login</code> method with valid username/password combination.
     * <p>
     * It should forward to failure, but no action messages should be generated.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testLogin_Failure() throws Exception {
        principal = TestUtil.createUser("myname", "mypw");

        addRequestParameter("userName", "myname");
        addRequestParameter("password", "invalidpw");
        addRequestParameter("method", "login");
        setRequestPathInfo("/login");
        actionPerform();
        verifyForward("failure");
        assertNull("No userId should be put into sesseion.", getSession().getAttribute("topcoder"));
        verifyNoActionMessages();
    }

    /**
     * Test <code>login</code> method when it throws AuthenticateException.
     * <p>
     * The corresponding action message should be logged.
     * </p>
     */
    public void testLogin_AuthenticateException() {
        addRequestParameter("userName", "my@name");
        addRequestParameter("password", "mypw");
        addRequestParameter("method", "login");
        setRequestPathInfo("/login");
        actionPerform();

        verifyActionErrors(new String[] {
            "exception.com.cronos.onlinereview.login.LoginActions.login.AuthenticateException" });
    }

    /**
     * Test <code>login</code> method when it throws MissingPrincipalKeyException.
     * <p>
     * The corresponding action message should be logged.
     * </p>
     */
    public void testLogin_MissingPrincipalKeyException() {
        addRequestParameter("userName", "my#name");
        addRequestParameter("password", "mypw");
        addRequestParameter("method", "login");
        setRequestPathInfo("/login");
        actionPerform();

        verifyActionErrors(new String[] {
            "exception.com.cronos.onlinereview.login.LoginActions.login.MissingPrincipalKeyException" });
    }

    /**
     * Test <code>login</code> method when it throws InvalidPrincipalException.
     * <p>
     * The corresponding action message should be logged.
     * </p>
     */
    public void testLogin_InvalidPrincipalException() {
        addRequestParameter("userName", "my$name");
        addRequestParameter("password", "mypw");
        addRequestParameter("method", "login");
        setRequestPathInfo("/login");
        actionPerform();

        verifyActionErrors(new String[] {
            "exception.com.cronos.onlinereview.login.LoginActions.login.InvalidPrincipalException" });
    }

    /**
     * Test <code>logout</code> method.
     * <p>
     * It should forward to login page.
     * </p>
     */
    public void testLogout() {
        addRequestParameter("method", "logout");
        setRequestPathInfo("/logout");
        actionPerform();

        verifyForward("logout");
        verifyNoActionMessages();
    }
}
