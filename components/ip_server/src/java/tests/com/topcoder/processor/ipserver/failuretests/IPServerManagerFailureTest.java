/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */

package com.topcoder.processor.ipserver.failuretests;

import com.topcoder.processor.ipserver.ConfigurationException;
import com.topcoder.processor.ipserver.IPServer;
import com.topcoder.processor.ipserver.IPServerManager;
import com.topcoder.util.config.ConfigManager;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;

/**
 * <p>A failure test for <code>IPServerManager</code> class. Tests the proper handling of invalid input data by the
 * methods. Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv, brain_cn
 * @version 2.0
 */
public class IPServerManagerFailureTest extends FailureTestCase {
    /**
     * <p>An <code>IPServer</code> representing the sample  IP server to be used for testing.</p>
     */
    public static IPServer IP_SERVER = null;

    /**
     * <p>A <code>String</code> providing the valid name of a server to  be used for testing.</p>
     */
    public static final String SERVER_NAME = "failure_test_server";

    /**
     * <p>An instance of <code>IPServerManager</code> which is tested.</p>
     */
    private IPServerManager testedInstance = null;

    static {
        try {
            IP_SERVER = new IPServer("127.0.0.1", 1024, 10, 10, MESSAGE_NAMESPACE);
        } catch (ConfigurationException e) {            
            e.printStackTrace();
        }
    }

    /**
     * <p>Gets the test suite for <code>IPServerManager</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>IPServerManager</code> class.
     */
    public static Test suite() {
        return new TestSuite(IPServerManagerFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        loadNamespaces();
        // release a singleton instance if it is left initialized by other test suites
        releaseSingletonInstance(IPServerManager.class, "instance");

        // Load valid configuration properties provided with failure tests
        loadConfiguration(IPServerManager.NAMESPACE,
                new File(FAILURE_TEST_FILES_ROOT, "good.properties").getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);


        // Instantiate the singleton instance with valid properties
        testedInstance = IPServerManager.getInstance();
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        // Release the configuration namespace
        releaseNamespace(IPServerManager.NAMESPACE);
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties missing the required <code>servers</code>
     * property, gets the singleton instance and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_3() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "missing_servers.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is missing required 'servers' property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties missing the required <code>&lt;server&gt;_port
     * </code> property, gets the singleton instance and expects the <code>ConfigurationException</code> to be thrown.
     * </p>
     */
    public void testGetInstance_4() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "missing_server_port.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is missing required the '<server>_port' property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties missing the required <code>&lt;server&gt;_handlers
     * </code> property, gets the singleton instance and expects the <code>ConfigurationException</code> to be thrown.
     * </p>
     */
    public void testGetInstance_5() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "missing_server_handlers.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is missing required '<server>_handlers' property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties missing the required <code>
     * &lt;server&gt;_&lt;handler&gt;_class</code> property, gets the singleton instance and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_6() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "missing_server_handler_class.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is missing required '<server>_<handler>_class' property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties providing invalid <code>
     * &lt;server&gt;_&lt;address&gt;</code> property, gets the singleton instance and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_7() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "invalid_server_address.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is providing invalid server address by <server>_address property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties providing negative <code>
     * &lt;server&gt;_&lt;port&gt;</code> property, gets the singleton instance and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_8() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "negative_server_port.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is providing negative port by <server>_port property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties providing out of range <code>
     * &lt;server&gt;_&lt;port&gt;</code> property, gets the singleton instance and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_9() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "outofrange_server_port.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is providing port out of valid range by <server>_port property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties providing two started servers using same port,
     * gets the singleton instance and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_10() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "busy_server_port.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is providing port by <server>_port property which is already taken by another server");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties providing negative <code>
     * &lt;server&gt;_&lt;max_connectionst&gt;</code> property, gets the singleton instance and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_11() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "negative_server_max_connections.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is providing negative value by <server>_max_connections property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties providing unparsable <code>
     * &lt;server&gt;_&lt;max_connectionst&gt;</code> property, gets the singleton instance and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_12() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "unparsable_server_max_connections.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is providing unparsable value by <server>_max_connections property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties providing unparsable <code>
     * &lt;server&gt;_&lt;port&gt;</code> property, gets the singleton instance and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_13() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "invalid_server_port.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is providing unparsable value by <server>_port property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties providing <code>
     * &lt;server&gt;_&lt;handler&gt;_class</code> property referencing non-existing class, gets the singleton instance
     * and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_14() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "non_existing_server_handler_class.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is providing the name of non-existing class by <server>_<handler>_class property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties providing <code>
     * &lt;server&gt;_&lt;handler&gt;_class</code> property referencing non-Handler class, gets the singleton instance
     * and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_15() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "non_handler_server_handler_class.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is providing the name of non-Handler class by <server>_<handler>_class property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties providing <code>
     * &lt;server&gt;_&lt;handler&gt;_class</code> property referencing the class with protected constructor available,
     * gets the singleton instance and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_16() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "protected_server_handler_class.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is providing the name of Handler class with protected constructor available by <server>_<handler>_class property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties providing <code>
     * &lt;server&gt;_&lt;handler&gt;_class</code> property referencing the class with private constructor available,
     * gets the singleton instance and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_17() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "private_server_handler_class.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is providing the name of Handler class with private constructor available by <server>_<handler>_class property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties providing <code>
     * &lt;server&gt;_&lt;handler&gt;_class</code> property referencing the class with package private constructor
     * available, gets the singleton instance and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_18() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "package_private_server_handler_class.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is providing the name of Handler class with package private constructor available by <server>_<handler>_class property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties providing negative <code>
     * &lt;server&gt;_&lt;handler&gt;_max_requests</code> property, gets the singleton instance and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_19() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "negative_server_handler_max_requests.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is providing negative value by <server>_<handler>_max_requests property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getInstance()</code> method for proper handling invalid input data. Initializes the
     * configuration namespace with a set of configuration properties providing unparsable <code>
     * &lt;server&gt;_&lt;started&gt;</code> property, gets the singleton instance and expects the <code>
     * ConfigurationException</code> to be thrown.</p>
     */
    public void testGetInstance_20() {
        File configFile = new File(FAILURE_TEST_FILES_ROOT, "bad_server_started.properties");
        loadConfiguration(IPServerManager.NAMESPACE,
                configFile.getAbsolutePath(),
                ConfigManager.CONFIG_PROPERTIES_FORMAT);

        // Release the singleton instance
        releaseSingletonInstance(IPServerManager.class, "instance");

        try {
            IPServerManager.getInstance();
            fail("ConfigurationException should be thrown since the configuration file is providing non-boolean value by <server>_started property");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>addIPServer(String, IPServer)</code> method for proper handling invalid input data. Passes and
     * expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testAddIPServer_String_IPServer_1() {
        try {
            testedInstance.addIPServer(null, IP_SERVER);
            fail("NullPointerException should be thrown in response to NULL server name");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>addIPServer(String, IPServer)</code> method for proper handling invalid input data. Passes and
     * expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testAddIPServer_String_IPServer_2() {
        try {
            testedInstance.addIPServer(SERVER_NAME, null);
            fail("NullPointerException should be thrown in response to NULL IP server");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>removeIPServer(String)</code> method for proper handling invalid input data. Passes and
     * expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testRemoveIPServer_String_1() {
        try {
            testedInstance.removeIPServer(null);
            fail("NullPointerException should be thrown in response to NULL server name");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>containsIPServer(String)</code> method for proper handling invalid input data. Passes and
     * expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testContainsIPServer_String_1() {
        try {
            testedInstance.containsIPServer(null);
            fail("NullPointerException should be thrown in response to NULL server name");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>getIPServer(String)</code> method for proper handling invalid input data. Passes and expects
     * the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testGetIPServer_String_1() {
        try {
            testedInstance.getIPServer(null);
            fail("NullPointerException should be thrown in response to NULL server name");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

}
