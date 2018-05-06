/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

import java.lang.reflect.Constructor;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <p>
 * Accuracy test for <code>DBConnectionFactoryImpl</code>.It will read data from config file.
 * </p>
 *
 * <p>
 * <b> Change in Version 1.1 </b><br>
 * The tests for constructor with ConfigurationObject, the two new createConnection methods and
 * the public static field are added.
 * </p>
 *
 * @author qiucx0161, magicpig
 * @version 1.1
 *
 * @since 1.0
 */
public class DBConnectionFactoryImplAccuracyTest extends TestCase {
    /**
     * The namespace from which to read the configuration values from the ConfigManager.
     */
    private static final String NAMESPACE = DBConnectionFactoryImpl.class.getName();

    /**
     * The ConfigManager instance to load the config file.
     */
    private static ConfigManager cm = ConfigManager.getInstance();

    /**
     * The folder which the config file is in.
     */
    private static final String CONFIG_DIR = "test_files/";

    /**
     * The DBConnectionFactoryImpl instance is used to call its method for test. It will be
     * initialized in setUp().
     */
    private DBConnectionFactoryImpl impl = null;

    /**
     * The ConnectionProducer instance is used to test the manipulation. It is initialized in
     * setUp().
     */
    private ConnectionProducer producer = null;

    /**
     * Represent the jdbc url, it will be initialized in setup().
     *
     * @since 1.1
     */
    private String jdbcUrl = null;

    /**
     * Represent the valid user name, it will be initialized in setup().
     *
     * @since 1.1
     */
    private String username = null;

    /**
     * Represent the valid user password, it will be initialized in setup().
     *
     * @since 1.1
     */
    private String password = null;

    /**
     * Represent the full class name of TestHelper.
     *
     * @since 1.1
     */
    private String helperNamespace = TestHelper.class.getName();

    /**
     * <p>
     * Set the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        // Clear the namespace before it is set.
        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }

        if (cm.existsNamespace(helperNamespace)) {
            cm.removeNamespace(helperNamespace);
        }

        cm.add("test.xml");
        username = cm.getString(helperNamespace, "JDBC.user");
        password = cm.getString(helperNamespace, "JDBC.password");
        jdbcUrl = cm.getString(helperNamespace, "JDBC.jdbc_url");

        try {
            // Set the proper namespace for test.
            cm.add(NAMESPACE, "DBConnectionFactoryImpl.xml",
                ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
        } catch (Exception e) {
            throw e;
        }

        impl = new DBConnectionFactoryImpl(NAMESPACE);

        // Create a new producer for test.
        String jndiClass = "com.topcoder.db.connectionfactory.producers.JNDIConnectionProducer";
        String jndi = "java:comp/env/jdbc/tcs";
        Class producerClass = Class.forName(jndiClass);

        Constructor constructor = producerClass.getConstructor(new Class[] {String.class});

        producer = (ConnectionProducer) constructor.newInstance(new Object[] {jndi});
    }

    /**
     * <p>
     * Set the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        // Remove namespace after running a method.
        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }

        if (cm.existsNamespace(helperNamespace)) {
            cm.removeNamespace(helperNamespace);
        }

        // Clear all the Producers after running a mehtod.
        impl.clear();
    }

    /**
     * Test createConnection method. It will create a connection successfully.
     *
     * @throws Exception to JUnit
     */
    public void testCreateConnection1() throws Exception {
        assertTrue(impl.createConnection() instanceof Connection);
    }

    /**
     * Test createConnection method. It will create a connection successfully. The difference of
     * default is that it gets ConnectionProducer via the special name.
     *
     * @throws Exception to JUnit
     */
    public void testCreateConnection2() throws Exception {
        assertTrue(impl.createConnection("MySqlJDBCConnection") instanceof Connection);
    }

    /**
     * Test add method, we add a new producer with new name into map, add method should return true
     * to show the success.
     */
    public void testAdd() {
        assertTrue("Should return true.", impl.add("new", producer));
    }

    /**
     * Test remove method. First, we add a new producer into map, it should return true, and then
     * ,remove it, it should return the removed producer.
     */
    public void testRemove() {
        assertTrue("Should return true.", impl.add("new", producer));
        assertEquals("Producer should match.", producer, impl.remove("new"));
    }

    /**
     * Test contains method. First, we get the default producer. Next, we add a new producer into
     * map, it should return true, and then ,get it, it should reutrn true.
     */
    public void testContains() {
        assertTrue("Should return true.", impl.contains("MySqlJDBCConnection"));

        // Add a new one, and then, get it.
        assertTrue("Should return true.", impl.add("new", producer));
        assertTrue("Should return true.", impl.contains("new"));
    }

    /**
     * Test getDefault method, we will get the default name : MySqlJDBCConnection which is in
     * config file.
     */
    public void testGetDefault() {
        assertEquals("Default name should match.", "MySqlJDBCConnection", impl.getDefault());
    }

    /**
     * Test setDefault method, we should set the default name to "newDefaultName", and then, get
     * it, they should match.
     *
     * @throws Exception to JUnit
     */
    public void testSetDefault() throws Exception {
        assertEquals("Default name should match.", "MySqlJDBCConnection", impl.getDefault());

        impl.add("newDefaultName", producer);

        // Set the new default name
        impl.setDefault("newDefaultName");
        assertEquals("Default name should match.", "newDefaultName", impl.getDefault());

        // Set the original default name to back
        impl.setDefault("MySqlJDBCConnection");
    }

    /**
     * Test listConnectionProducerNames method. We will add a new producer, and then, get all the
     * names and put them into a list, this list should contains the added name.
     */
    public void testListConnectionProducerNames() {
        // Get the original names.
        List list = new ArrayList();

        for (Iterator it = impl.listConnectionProducerNames(); it.hasNext();) {
            list.add(it.next());
        }

        assertTrue("Should contain the default name.", list.contains("MySqlJDBCConnection"));

        int size = list.size();

        // Add a new producer
        impl.add("newName", producer);

        list.clear();

        // Get all names
        for (Iterator it = impl.listConnectionProducerNames(); it.hasNext();) {
            list.add(it.next());
        }

        assertTrue("Should contain the new name.", list.contains("newName"));
        assertEquals("The count of names should match.", size + 1, list.size());
    }

    /**
     * Test get method. First, it should return null for none producer match with the given name.
     * Second, it should return ConnectionProducer instance matching with the default name. Next,
     * we will add a new producer, and we should get the proper producer.
     */
    public void testGet() {
        assertTrue(impl.get("MySqlJDBCConnection") instanceof ConnectionProducer);

        // Add a new producer, and then get it.
        impl.add("newPro", producer);
        assertEquals("Should match with the added producer.", producer, impl.get("newPro"));
    }

    /**
     * Test clear method. First, we will get the default name. Next, we will add a new name and get
     * it. And then, clear all. We will get nothing.
     */
    public void testClear() {
        // Get the default name.
        assertTrue(impl.contains("MySqlJDBCConnection"));

        impl.add("newName", producer);

        assertTrue(impl.contains("newName"));

        impl.clear();

        // Get nothing.
        assertFalse("Shuold get nothing and return false.", impl.contains("MySqlJDBCConnection"));
        assertFalse("Shuold get nothing and return false.", impl.contains("newName"));
    }

    /**
     * <p>
     * Accuracy for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Target: tests the creation.
     * </p>
     *
     * @throws Exception when error occurs
     *
     * @since 1.1
     */
    public void testCtorConfigurationObjectAccuracy() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        assertNotNull("Failed to create DBConnectionFactoryImpl instance",
            new DBConnectionFactoryImpl(configurationObject));
    }

    /**
     * <p>
     * Accuracy for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Target: tests the creation.
     * </p>
     *
     * @throws Exception when error occurs
     *
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringAccuracy()
        throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        assertNotNull("Failed to create DBConnectionFactoryImpl instance",
            new DBConnectionFactoryImpl(configurationObject,
                DBConnectionFactoryImpl.DEFAULT_NAMESPACE));
    }

    /**
     * <p>
     * Accuracy for method: createConnection(String, String). <br>
     * Target: tests the functionality of the instance created by constructor with
     * ConfigurationObject.
     * </p>
     *
     * @throws Exception when error occurs
     *
     * @since 1.1
     */
    public void testCreateConnectionAccuracy1() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        ConfigurationObject child3 = new DefaultConfigurationObject("default_connection");
        child2.addChild(child3);
        child3.setPropertyValue("producer",
            "com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer");

        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        child3.addChild(parameters);
        parameters.setPropertyValue("jdbc_url", jdbcUrl);
        child2.setPropertyValue("default", "default_connection");

        Connection conn = new DBConnectionFactoryImpl(configurationObject).createConnection(username,
                password);
        assertTrue("The method createConnection(String, String) works out of expect.",
            conn instanceof Connection);
        conn.close();
    }

    /**
     * <p>
     * Accuracy for method: createConnection(String, String, String). <br>
     * Target: tests the functionality of the instance created by constructor with
     * ConfigurationObject.
     * </p>
     *
     * @throws Exception when error occurs
     *
     * @since 1.1
     */
    public void testCreateConnectionAccuracy2() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        ConfigurationObject child3 = new DefaultConfigurationObject("default_connection");
        child2.addChild(child3);
        child3.setPropertyValue("producer",
            "com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer");

        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        child3.addChild(parameters);
        parameters.setPropertyValue("jdbc_url", jdbcUrl);

        Connection conn = new DBConnectionFactoryImpl(configurationObject).createConnection("default_connection",
                username, password);
        assertTrue("The method createConnection(String, String) works out of expect.",
            conn instanceof Connection);
        conn.close();
    }

    /**
     * <p>
     * Test the value of public field DEFAULT_NAMESPACE.
     * </p>
     *
     * @since 1.1
     */
    public void testDEFAULT_NAMESPACE() {
        assertEquals("The public field DEFAULT_NAMESPACE is not valid",
            DBConnectionFactoryImpl.class.getName(), DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
    }
}
