/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */

package com.topcoder.security.authenticationfactory.failuretests;

import com.topcoder.security.authenticationfactory.AuthenticationFactory;
import com.topcoder.security.authenticationfactory.ConfigurationException;
import com.topcoder.util.config.ConfigManager;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;

/**
 * <p>A failure test for <code>AuthenticationFactory</code> class. Tests the proper handling of invalid input data by
 * the methods. Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 2.0
 */
public class AuthenticationFactoryFailureTest extends FailureTestCase {

    /**
     * <p>A <code>String</code> providing the configuration namespace that is used to configure the instance of tested
     * class.</p>
     */
    public static final String NAMESPACE = "com.topcoder.security.authenticationfactory.AuthenticationFactory";

    /**
     * <p>An instance of <code>AuthenticationFactory</code> which is tested.</p>
     */
    private AuthenticationFactory testedInstance = null;

    /**
     * <p>Gets the test suite for <code>AuthenticationFactory</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>AuthenticationFactory</code> class.
     */
    public static Test suite() {
        return new TestSuite(AuthenticationFactoryFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        releaseNamespaces();

        loadConfiguration(new File(new File(FAILURE_ROOT, "factory"), "Good.xml"));

        testedInstance = AuthenticationFactory.getInstance();
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        releaseNamespaces();
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Causes the method to
     * initialize the instance from a namespace missing the 'authenticators' property and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_1() {
        releaseSingletonInstance(AuthenticationFactory.class, "instance");
        releaseNamespaces();

        File configFile = new File(new File(FAILURE_ROOT, "factory"), "MissingAuthenticators.xml");
        loadConfiguration(configFile);

        try {
            AuthenticationFactory.getInstance();
            fail("ConfigurationException should be thrown since 'authenticators' property is missing");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Causes the method to
     * initialize the instance from a namespace providing invalid configuration properties and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_2() {
        releaseSingletonInstance(AuthenticationFactory.class, "instance");
        releaseNamespaces();

        File configFile = new File(new File(FAILURE_ROOT, "factory"), "AuthBadConfig.xml");
        loadConfiguration(configFile);

        try {
            AuthenticationFactory.getInstance();
            fail("ConfigurationException should be thrown since configuration file is invalid");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Causes the method to
     * initialize the instance from a namespace providing invalid configuration properties and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_3() {
        releaseSingletonInstance(AuthenticationFactory.class, "instance");
        releaseNamespaces();

        File configFile = new File(new File(FAILURE_ROOT, "factory"), "AuthClassNotFound.xml");
        loadConfiguration(configFile);

        try {
            AuthenticationFactory.getInstance();
            fail("ConfigurationException should be thrown since configuration file is invalid");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Causes the method to
     * initialize the instance from a namespace providing invalid configuration properties and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_4() {
        releaseSingletonInstance(AuthenticationFactory.class, "instance");
        releaseNamespaces();

        File configFile = new File(new File(FAILURE_ROOT, "factory"), "AuthClassNotSpecified.xml");
        loadConfiguration(configFile);

        try {
            AuthenticationFactory.getInstance();
            fail("ConfigurationException should be thrown since configuration file is invalid");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Causes the method to
     * initialize the instance from a namespace providing invalid configuration properties and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_5() {
        releaseSingletonInstance(AuthenticationFactory.class, "instance");
        releaseNamespaces();

        File configFile = new File(new File(FAILURE_ROOT, "factory"), "AuthNamespaceNotFound.xml");
        loadConfiguration(configFile);

        try {
            AuthenticationFactory.getInstance();
            fail("ConfigurationException should be thrown since configuration file is invalid");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Causes the method to
     * initialize the instance from a namespace providing invalid configuration properties and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_6() {
        releaseSingletonInstance(AuthenticationFactory.class, "instance");
        releaseNamespaces();

        File configFile = new File(new File(FAILURE_ROOT, "factory"), "AuthNamespaceNotSpecified.xml");
        loadConfiguration(configFile);

        try {
            AuthenticationFactory.getInstance();
            fail("ConfigurationException should be thrown since configuration file is invalid");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getAuthenticator(String)</code> method for proper handling invalid input data. Passes the
     * <code>null</code> name and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testGetAuthenticator_String_1() {
        try {
            testedInstance.getAuthenticator(null);
            fail("NullPointerException should be thrown in response to NULL name");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getAuthenticator(String)</code> method for proper handling invalid input data. Passes the
     * zero-length name and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testGetAuthenticator_String_2() {
        try {
            testedInstance.getAuthenticator("");
            fail("IllegalArgumentException should be thrown in response to zero-length name");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getAuthenticator(String)</code> method for proper handling invalid input data. Passes the
     * empty name and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testGetAuthenticator_String_3() {
        try {
            testedInstance.getAuthenticator("        ");
            fail("IllegalArgumentException should be thrown in response to empty name");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>refresh()</code> method for proper handling invalid input data. Causes the method to
     * initialize the instance from a namespace missing the 'authenticators' property and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testRefresh_1() {
        File configFile = new File(new File(FAILURE_ROOT, "factory"), "MissingAuthenticators.xml");
        loadConfiguration(NAMESPACE, configFile.getPath(), ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);

        try {
            testedInstance.refresh();
            fail("ConfigurationException should be thrown since 'authenticators' property is missing");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>refresh()</code> method for proper handling invalid input data. Causes the method to
     * initialize the instance from a namespace providing invalid configuration properties and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testRefresh_2() {
        releaseNamespaces();

        File configFile = new File(new File(FAILURE_ROOT, "factory"), "AuthBadConfig.xml");
        loadConfiguration(configFile);

        try {
            testedInstance.refresh();
            fail("ConfigurationException should be thrown since configuration file is invalid");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>refresh()</code> method for proper handling invalid input data. Causes the method to
     * initialize the instance from a namespace providing invalid configuration properties and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testRefresh_3() {
        releaseNamespaces();

        File configFile = new File(new File(FAILURE_ROOT, "factory"), "AuthClassNotFound.xml");
        loadConfiguration(configFile);

        try {
            testedInstance.refresh();
            fail("ConfigurationException should be thrown since configuration file is invalid");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>refresh()</code> method for proper handling invalid input data. Causes the method to
     * initialize the instance from a namespace providing invalid configuration properties and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testRefresh_4() {
        releaseNamespaces();

        File configFile = new File(new File(FAILURE_ROOT, "factory"), "AuthClassNotSpecified.xml");
        loadConfiguration(configFile);

        try {
            testedInstance.refresh();
            fail("ConfigurationException should be thrown since configuration file is invalid");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>refresh()</code> method for proper handling invalid input data. Causes the method to
     * initialize the instance from a namespace providing invalid configuration properties and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testRefresh_5() {
        releaseNamespaces();

        File configFile = new File(new File(FAILURE_ROOT, "factory"), "AuthNamespaceNotFound.xml");
        loadConfiguration(configFile);

        try {
            testedInstance.refresh();
            fail("ConfigurationException should be thrown since configuration file is invalid");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>refresh()</code> method for proper handling invalid input data. Causes the method to
     * initialize the instance from a namespace providing invalid configuration properties and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testRefresh_6() {
        releaseNamespaces();

        File configFile = new File(new File(FAILURE_ROOT, "factory"), "AuthNamespaceNotSpecified.xml");
        loadConfiguration(configFile);

        try {
            testedInstance.refresh();
            fail("ConfigurationException should be thrown since configuration file is invalid");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }
}
