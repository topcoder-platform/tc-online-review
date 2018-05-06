/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.failuretests;

import java.io.File;
import java.lang.reflect.Method;

import servletunit.struts.MockStrutsTestCase;

import com.cronos.onlinereview.login.ConfigurationException;
import com.cronos.onlinereview.login.authenticator.SecurityManagerAuthenticator;
import com.topcoder.security.authenticationfactory.InvalidPrincipalException;
import com.topcoder.security.authenticationfactory.MissingPrincipalKeyException;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.util.config.ConfigManager;


/**
 * The failure tests for the SecurityManagerAuthResponseParser class.
 *
 * @author slion
 * @version 1.0
 */
public class SecurityManagerAuthenticatorFailureTests extends MockStrutsTestCase {
    /**
     * Represents the invalid configuration for constructing SecurityManagerAuthenticator.
     */
    private static final String INVALID_CONFIG_FILE =
        "test_files/failure/SecurityManagerAuthenticator_Invalid.xml";
    
    /**
     * Represents the invalid configuration for constructing SecurityManagerAuthenticator.
     */
    private static final String INVALID_NAMESPACE1 =
        "com.cronos.onlinereview.login.authenticator.SecurityManagerAuthenticator.NullLoginBeanName";
    
    /**
     * Represents the invalid configuration for constructing SecurityManagerAuthenticator.
     */
    private static final String INVALID_NAMESPACE2 =
        "com.cronos.onlinereview.login.authenticator.SecurityManagerAuthenticator.NoLoginBeanName";

    /**
     * Represents the configuration for JNDIUtils.
     */
    private static final String JNDIUTILS = "test_files/failure/JNDIUtils.properties";

    /**
     * Default Constructor.
     * @param testName the name of test.
     */
    public SecurityManagerAuthenticatorFailureTests(String testName) {
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
        CMHelper.loadConfig(INVALID_CONFIG_FILE);
    }
    
    /**
     * If you override tearDown(), you must explicitly call super.tearDown()
     *
     * @throws Exception to JUnit
     */
    public void tearDown() throws Exception {
        super.tearDown();
        CMHelper.clearCM();
    }
    
    /**
     * Test constructor when the invalid configuration.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_InvalidConfig1() throws Exception {
        try {
            new SecurityManagerAuthenticator(INVALID_NAMESPACE1);
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
            new SecurityManagerAuthenticator(INVALID_NAMESPACE2);
            fail("Failed to construct.");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test constructor when the null namespace.
     * IAE is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_NullNS() throws Exception {
        try {
            new SecurityManagerAuthenticator(null);
            fail("Failed to construct.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test constructor when the empty namespace.
     * IAE is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_EmptyNS() throws Exception {
        try {
            new SecurityManagerAuthenticator("  ");
            fail("Failed to construct.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test constructor when the unknown namespace.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_UnknownNS() throws Exception {
        try {
            new SecurityManagerAuthenticator("Unknown");
            fail("Failed to construct.");
        } catch (com.topcoder.security.authenticationfactory.ConfigurationException e) {
            // pass
        }
    }
    
    /**
     * Test doAuthenticate method with failure situation.
     * MissingPrincipalKeyException is expected.
     * @throws Exception to JUnit.
     */
    public void testDoAuthenticate_MissingPrincipalKeyException() throws Exception {
        try {
            ConfigManager.getInstance().add("com.topcoder.naming.jndiutility",
                    new File(JNDIUTILS).getAbsolutePath(),
                    ConfigManager.CONFIG_PROPERTIES_FORMAT);
            SecurityManagerAuthenticator authenticator =
                new SecurityManagerAuthenticator(SecurityManagerAuthenticator.class.getName());
            Method method =
                authenticator.getClass().getDeclaredMethod("doAuthenticate", new Class[]{Principal.class});
            Principal principal = new Principal("test");
            method.setAccessible(true);
            method.invoke(authenticator, new Object[]{principal});
            fail("Failed to construct.");
        } catch (java.lang.reflect.InvocationTargetException e) {
            assertTrue(e.getCause() instanceof MissingPrincipalKeyException);
        }
    }

    /**
     * Test doAuthenticate method with failure situation.
     * InvalidPrincipalException is expected.
     * @throws Exception to JUnit.
     */
    public void testDoAuthenticate_InvalidPrincipalException() throws Exception {
        try {
            ConfigManager.getInstance().add("com.topcoder.naming.jndiutility",
                    new File(JNDIUTILS).getAbsolutePath(),
                    ConfigManager.CONFIG_PROPERTIES_FORMAT);
            SecurityManagerAuthenticator authenticator =
                new SecurityManagerAuthenticator(SecurityManagerAuthenticator.class.getName());
            Method method =
                authenticator.getClass().getDeclaredMethod("doAuthenticate", new Class[]{Principal.class});
            Principal principal = new Principal("test");
            principal.addMapping("userName", new Long(10));
            method.setAccessible(true);
            method.invoke(authenticator, new Object[]{principal});
            fail("Failed to construct.");
        } catch (java.lang.reflect.InvocationTargetException e) {
            assertTrue(e.getCause() instanceof InvalidPrincipalException);
        }
    }
}
