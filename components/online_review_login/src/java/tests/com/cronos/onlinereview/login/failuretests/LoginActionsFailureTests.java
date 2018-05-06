/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.failuretests;

import java.io.File;

import com.cronos.onlinereview.login.ConfigurationException;
import com.cronos.onlinereview.login.LoginActions;
import com.topcoder.util.config.ConfigManager;

import servletunit.struts.MockStrutsTestCase;


/**
 * The failure tests for the LoginActions class.
 *
 * @author slion, akinwale
 * @version 1.1
 * @since 1.0
 */
public class LoginActionsFailureTests extends MockStrutsTestCase {
    /**
     * Represents the invalid configuration for constructing LoginActions.
     */
    private static final String INVALID_CONFIG1 = "test_files/failure/LoginActions_InvalidAuthenticatorName.xml";

    /**
     * Represents the invalid configuration for constructing LoginActions.
     */
    private static final String INVALID_CONFIG2 = "test_files/failure/LoginActions_InvalidParser.xml";

    /**
     * Represents the invalid configuration for constructing LoginActions.
     */
    private static final String INVALID_CONFIG3 = "test_files/failure/LoginActions_InvalidParserNamespace.xml";

    /**
     * Represents the invalid configuration for constructing LoginActions.
     */
    private static final String INVALID_CONFIG4 = "test_files/failure/LoginActions_NoAuthenticatorProperty.xml";

    /**
     * Represents the invalid configuration for constructing LoginActions.
     */
    private static final String INVALID_CONFIG5 = "test_files/failure/LoginActions_NoParserClassProperty.xml";

    /**
     * Represents the invalid configuration for constructing LoginActions.
     */
    private static final String INVALID_CONFIG6 = "test_files/failure/LoginActions_NoParserNamespaceProperty.xml";

    /**
     * Represents the invalid configuration for constructing LoginActions.
     */
    private static final String INVALID_CONFIG7 = "test_files/failure/LoginActions_NullAuthenticatorName.xml";

    /**
     * Represents the invalid configuration for constructing LoginActions.
     */
    private static final String INVALID_CONFIG8 = "test_files/failure/LoginActions_NullParserClass.xml";

    /**
     * Represents the invalid configuration for constructing LoginActions.
     */
    private static final String INVALID_CONFIG9 = "test_files/failure/LoginActions_NullParserNamespace.xml";

    /**
     * Represents the invalid configuration for constructing LoginActions.
     */
    private static final String INVALID_CONFIG10 = "test_files/failure/LoginActions_NoParserProperty.xml";

    /**
     * Represents the configuration for JNDIUtils.
     */
    private static final String JNDIUTILS = "test_files/failure/JNDIUtils.properties";

    /**
     * Represents the configuration for JNDIUtils.
     */
    private static final String ONLINE_REVIEW_LOGIN = "test_files/failure/OnlineReviewLogin.xml";

    /**
     * Default Constructor.
     * @param testName the name of test.
     */
    public LoginActionsFailureTests(String testName) {
        super(testName);
    }

    /**
     * If you override setUp(), you must explicitly call super.setUp()
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
        CMHelper.clearCM();
    }

    /**
     * If you override tearDown(), you must explicitly call super.tearDown()
     *
     * @throws Exception to JUnit
     */
    public void tearDown() throws Exception {
        CMHelper.clearCM();
        super.tearDown();
    }

    /**
     * Test constructor when the invalid configuration.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_InvalidConfig1() throws Exception {
        try {
            CMHelper.loadConfig(INVALID_CONFIG1);
            new LoginActions();
            fail("Failed to construct.");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test constructor when the invalid configuration.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_InvalidConfig2() throws Exception {
        try {
            CMHelper.loadConfig(INVALID_CONFIG2);
            new LoginActions();
            fail("Failed to construct.");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test constructor when the invalid configuration.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_InvalidConfig3() throws Exception {
        try {
            CMHelper.loadConfig(INVALID_CONFIG3);
            new LoginActions();
            fail("Failed to construct.");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test constructor when the invalid configuration.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_InvalidConfig4() throws Exception {
        try {
            CMHelper.loadConfig(INVALID_CONFIG4);
            new LoginActions();
            fail("Failed to construct.");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test constructor when the invalid configuration.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_InvalidConfig5() throws Exception {
        try {
            CMHelper.loadConfig(INVALID_CONFIG5);
            new LoginActions();
            fail("Failed to construct.");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test constructor when the invalid configuration.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_InvalidConfig6() throws Exception {
        try {
            CMHelper.loadConfig(INVALID_CONFIG6);
            new LoginActions();
            fail("Failed to construct.");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test constructor when the invalid configuration.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_InvalidConfig7() throws Exception {
        try {
            CMHelper.loadConfig(INVALID_CONFIG7);
            new LoginActions();
            fail("Failed to construct.");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test constructor when the invalid configuration.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_InvalidConfig8() throws Exception {
        try {
            CMHelper.loadConfig(INVALID_CONFIG8);
            new LoginActions();
            fail("Failed to construct.");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test constructor when the invalid configuration.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_InvalidConfig9() throws Exception {
        try {
            CMHelper.loadConfig(INVALID_CONFIG9);
            new LoginActions();
            fail("Failed to construct.");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test constructor when the invalid configuration.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_InvalidConfig10() throws Exception {
        try {
            CMHelper.loadConfig(INVALID_CONFIG10);
            new LoginActions();
            fail("Failed to construct.");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test the Action path "/login" for the forward "failure".
     * Forward path: /login.jsp
     * ActionForm: org.apache.struts.validator.DynaValidatorForm
     * @throws Exception to JUnit.
     */
    public void testLoginFailure_InvalidPrincipalException() throws Exception
    {
        ConfigManager.getInstance().add("com.topcoder.naming.jndiutility",
                new File(JNDIUTILS).getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);
        CMHelper.loadConfig(ONLINE_REVIEW_LOGIN);
        setRequestPathInfo("/login");
        addRequestParameter("password", "");
        addRequestParameter("userName", "");
        addRequestParameter("method", "login");
        actionPerform();
        verifyForward("failure");
    }

    /**
     * Test the Action path "/login" for the forward "failure".
     * Forward path: /login.jsp
     * ActionForm: org.apache.struts.validator.DynaValidatorForm
     * @throws Exception to JUnit.
     */
    public void testLoginFailure_AuthenticationException() throws Exception
    {
        ConfigManager.getInstance().add("com.topcoder.naming.jndiutility",
                new File(JNDIUTILS).getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);
        CMHelper.loadConfig(ONLINE_REVIEW_LOGIN);
        setRequestPathInfo("/login");
        addRequestParameter("password", "abc");
        addRequestParameter("userName", "def");
        addRequestParameter("method", "login");
        //actionPerform();
        //verifyForward("failure");

        // When authenticate failed, no error messages recieved here.
        // Please refer to the forum.
    }

    /**
     * <p>
     * Tests that the LoginActions() constructor handles a case where the
     * "auth_cookie_manager.class" configuration value is missing by throwing
     * {@link ConfigurationException}.
     * </p>
     *
     * <p>
     * {@link ConfigurationException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     *
     * @since 1.1
     */
    public void testCtor_Failure_MissingAuthCookieManagerClass() throws Exception {
        CMHelper.loadConfig("test_files/failure/LoginActions.MissingAuthCookieManagerClass.xml");
        try {
            new LoginActions();
            fail("ConfigurationException was expected");
        } catch (ConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the LoginActions() constructor handles a case where the
     * "auth_cookie_manager.class" configuration value is an empty string by throwing
     * {@link ConfigurationException}.
     * </p>
     *
     * <p>
     * {@link ConfigurationException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     *
     * @since 1.1
     */
    public void testCtor_Failure_EmptyStringAuthCookieManagerClass() throws Exception {
        CMHelper.loadConfig("test_files/failure/LoginActions.EmptyStringAuthCookieManagerClass.xml");
        try {
            new LoginActions();
            fail("ConfigurationException was expected");
        } catch (ConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the LoginActions() constructor handles a case where the
     * "auth_cookie_manager.class" configuration value is an invalid class by throwing
     * {@link ConfigurationException}.
     * </p>
     *
     * <p>
     * {@link ConfigurationException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     *
     * @since 1.1
     */
    public void testCtor_Failure_InvalidAuthCookieManagerClass() throws Exception {
        CMHelper.loadConfig("test_files/failure/LoginActions.InvalidAuthCookieManagerClass.xml");
        try {
            new LoginActions();
            fail("ConfigurationException was expected");
        } catch (ConfigurationException e) {
            // success
        }
    }
}
