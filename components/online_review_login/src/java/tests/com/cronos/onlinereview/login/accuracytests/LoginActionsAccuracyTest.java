/*
 * Copyright (C) 2006-2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.accuracytests;

import java.util.HashSet;

import servletunit.struts.MockStrutsTestCase;

import com.cronos.onlinereview.login.LoginActions;
import com.cronos.onlinereview.login.authenticator.SecurityManagerAuthResponseParser;
import com.cronos.onlinereview.login.authenticator.SecurityManagerAuthenticator;
import com.topcoder.security.TCSubject;
import com.topcoder.security.UserPrincipal;
import com.topcoder.util.log.basic.BasicLog;


/**
 * Accuracy tests for class <code>LoginActions</code>.
 *
 * @author PE, onsky
 * @version 1.1
 * @since 1.0
 */
public class LoginActionsAccuracyTest extends MockStrutsTestCase {
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
     * Tests the accuracy of constructor LoginActions(String).
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testLoginActions_Accuracy() throws Exception {
        LoginActions actions = new LoginActions();
        assertTrue("Failed to initialize authenticator.",
            AccuracyTestHelper.getPrivateField(actions.getClass(), actions, "authenticator") instanceof SecurityManagerAuthenticator);
        assertTrue("Failed to initialize authResponseParser",
            AccuracyTestHelper.getPrivateField(actions.getClass(), actions, "authResponseParser") instanceof SecurityManagerAuthResponseParser);
        assertTrue("Failed to initialize logger.",
            AccuracyTestHelper.getPrivateField(actions.getClass(), actions, "logger") instanceof BasicLog);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>login(ActionMapping, ActionForm, HttpServletRequest,
     * HttpServletResponse)</code> with valid username/password combination.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testLogin_SuccessAccuracy() throws Exception {
        String name = "name";
        String password = "password";
        UserPrincipal userPrincipal = AccuracyTestHelper.createUser(name, password);

        addRequestParameter("userName", name);
        addRequestParameter("password", password);
        addRequestParameter("method", "login");

        setRequestPathInfo("/login");
        actionPerform();

        verifyForward("success");
        assertTrue("The userId should be put into sesseion.", getSession().getAttribute("topcoder") instanceof Long);
        verifyNoActionMessages();

        AccuracyTestHelper.removeUser(userPrincipal, new TCSubject(new HashSet(), userPrincipal.getId()));
    }

    /**
     * <p>
     * Tests the accuracy of method <code>login(ActionMapping, ActionForm, HttpServletRequest,
     * HttpServletResponse)</code> with invalid username/password combination.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testLogin_FailureAccuracy() throws Exception {
        String name = "name";
        String password = "password";
        UserPrincipal userPrincipal = AccuracyTestHelper.createUser(name, password);

        addRequestParameter("userName", name);
        addRequestParameter("password", "invalid");
        addRequestParameter("method", "login");

        setRequestPathInfo("/login");
        actionPerform();

        verifyForward("failure");
        assertNull("No userId should be put into sesseion.", getSession().getAttribute("topcoder"));
        verifyNoActionMessages();

        AccuracyTestHelper.removeUser(userPrincipal, new TCSubject(new HashSet(), userPrincipal.getId()));
    }

    /**
     * <p>
     * Tests the accuracy of method <code>logout(ActionMapping, ActionForm, HttpServletRequest,
     * HttpServletResponse)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testLogout_Accuracy() {
        addRequestParameter("method", "logout");
        setRequestPathInfo("/logout");
        actionPerform();

        verifyForward("logout");
        verifyNoActionMessages();
    }
}
