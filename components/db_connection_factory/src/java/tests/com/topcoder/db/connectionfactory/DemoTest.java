/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.db.connectionfactory.producers.DataSourceConnectionProducer;
import com.topcoder.db.connectionfactory.producers.DataSourceImpl;
import com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer;
import com.topcoder.db.connectionfactory.producers.JNDIConnectionProducer;
import com.topcoder.db.connectionfactory.producers.ReflectingConnectionProducer;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

import java.io.File;
import java.sql.Connection;

import java.util.Iterator;

import javax.sql.DataSource;

/**
 * <p>
 * Component demonstration for DB Connection Factory. It include the demo both in version 1 and
 * version 1.1 which demonstrates the new features.
 * </p>
 *
 * @author qiucx0161, magicpig
 * @version 1.1
 * @since 1.0
 */
public class DemoTest extends TestCase {
    /**
     * <p>
     * The namespace from which to read the configuration values from the ConfigManager.
     * </p>
     */
    private static final String NAMESPACE = DBConnectionFactoryImpl.class.getName();

    /**
     * <p>
     * The ConfigManager instance to load the config file.
     * </p>
     */
    private static ConfigManager cm = ConfigManager.getInstance();

    /**
     * <p>
     * The DBConnectionFactoryImpl instance is used to call its method for test.
     * </p>
     */
    private DBConnectionFactory factory = null;

    /**
     * Represent the full class name of TestHelper.
     *
     * @since 1.1
     */
    private String helperNamespace = TestHelper.class.getName();

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
     * Represent the jndi name, it will be initialized in setup().
     *
     * @since 1.1
     */
    private String jndiName = null;

    /**
     * <p>
     * Setup the test environment.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    protected void setUp() throws Exception {
        // Clear the namespace before it is set.
        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }

        // Set the proper namespace for test.
        cm.add(NAMESPACE, "DBConnectionFactoryImpl.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);

        if (cm.existsNamespace(helperNamespace)) {
            cm.removeNamespace(helperNamespace);
        }

        cm.add("test.xml");
        username = cm.getString(helperNamespace, "JDBC.user");
        password = cm.getString(helperNamespace, "JDBC.password");
        jdbcUrl = cm.getString(helperNamespace, "JDBC.jdbc_url");
        jndiName = cm.getString(helperNamespace, "JNDI.jndi_name");

    }

    /**
     * <p>
     * Sets the test environment.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }
        if (cm.existsNamespace(helperNamespace)) {
            cm.removeNamespace(helperNamespace);
        }
    }

    /**
     * Tests some demo methods.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDemo() throws Exception {
        // Instantiate the DBConnectionFactoryImpl providing the configuration namespace.
        // This usage is deprecated in version 1.1.
        factory = new DBConnectionFactoryImpl(NAMESPACE);

        // Obtain a default connection
        Object con = factory.createConnection();
        assertTrue("Should be Connection type.", con instanceof Connection);
        ((Connection) con).close();

        // Obtain a named connection
        Object conn = factory.createConnection("MySqlJDBCConnection");
        assertTrue("Should be Connection type.", conn instanceof Connection);
        ((Connection) con).close();

        // Instantiate an empty factory
        DBConnectionFactoryImpl factoryImpl = new DBConnectionFactoryImpl();

        // Populate the factory with connection producers
        factoryImpl.add("test", new JNDIConnectionProducer(jndiName));

        factoryImpl.add("production", new JDBCConnectionProducer(jdbcUrl, username, password));

        factoryImpl.add("stock", new JNDIConnectionProducer(jndiName, username, password));

        // Set the default producer
        factoryImpl.setDefault("production");

        factory = (DBConnectionFactory) factoryImpl;

        // Obtain a default connection
        Object con1 = factory.createConnection();
        assertTrue("Should be Connection type.", con1 instanceof Connection);
        ((Connection) con1).close();

        // Obtain a named connection
        Object con2 = factory.createConnection("test");
        assertTrue("Should be Connection type.", con1 instanceof Connection);
        ((Connection) con1).close();

        Object con3 = factory.createConnection("production");
        assertTrue("Should be Connection type.", con1 instanceof Connection);
        ((Connection) con1).close();

        Object con4 = factory.createConnection("stock");
        assertTrue("Should be Connection type.", con1 instanceof Connection);
        ((Connection) con1).close();

        // Maintain the state of factory
        factoryImpl.remove("test");
        factoryImpl.setDefault("stock");

        String defaultProducerName = factoryImpl.getDefault();
        assertEquals("Default name should match.", "stock", defaultProducerName);

        ConnectionProducer defaultProducer = factoryImpl.get(defaultProducerName);

        assertTrue(defaultProducer instanceof JNDIConnectionProducer);

        Iterator iterator = factoryImpl.listConnectionProducerNames();

        while (iterator.hasNext()) {
            ConnectionProducer producer = factoryImpl.get((String) iterator.next());

            // Use the gotten ConnectionProducer to create connection.
            Object connection = producer.createConnection();
            assertTrue("Should be Connection type.", connection instanceof Connection);
            ((Connection) connection).close();
        }

        factoryImpl.clear();
    }

    /**
     * Tests ReflectingConnectionProducer method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReflectingConnectionProducer() throws Exception {
        final String CLASSNAME = "com.topcoder.db.connectionfactory.producers.DataSourceImpl";

        // Create the DataSource instance with its implementation.
        DataSource ds = new DataSourceImpl();

        ReflectingConnectionProducer pro = new ReflectingConnectionProducer(CLASSNAME, "test", "test");

        Object conn = pro.createConnection();
        assertTrue(conn instanceof Connection);
        ((Connection) conn).close();
    }

    /**
     * Tests DataSourceConnectionProducer method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDataSourceConnectionProducer() throws Exception {
        // Create the DataSource instance with its implementation.
        DataSource datasource = new DataSourceImpl();

        DataSourceConnectionProducer pro = new DataSourceConnectionProducer(datasource, "test", "test");

        Object conn = pro.createConnection();
        assertTrue(conn instanceof Connection);
    }

    /**
     * <p>
     * This is the demo for the new feature of version 1.1.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testDemoV1() throws Exception {
    	File file = new File("test_files" + File.separator
                + "demopreLoad.properties");
    	System.out.println(file.getAbsolutePath());
        // //////////////////////////////////////////////////////////////////////////
        // / First part: Create DBConnectionFactoryImpl using the new constructors///
        // //////////////////////////////////////////////////////////////////////////
        // In version 1.1 the common usage is to create the factory based on configuration object.
        ConfigurationFileManager manager = new ConfigurationFileManager("test_files" + File.separator
            + "demopreLoad.properties");
        ConfigurationObject rootObject = manager.getConfiguration("demo");

        // Create DBConnectionFactoryImpl using DBConnectionFactoryImpl.DEFAULT_NAMESPACE
        factory = new DBConnectionFactoryImpl(rootObject);
        // Also we can specify the namespace manually to load configuration from the object.
        factory = new DBConnectionFactoryImpl(rootObject,
            "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");

        // //////////////////////////////////////////////////////////////////////////
        // / Second part: demonstration of the operations ///
        // /////////////////////////////////////////////////////////////////////////
        // Obtain a default connection(It will obtain the default connection from
        // MySqlJDBCConnection since that¡¯s the default producer)
        Connection con = factory.createConnection();
        // Obtain a named connection
        con = factory.createConnection("DefaultJNDI");
        // Obtain a connection from the default(MySqlJDBCConnection) producer with user-provided
        // credentials
        con = factory.createConnection(username, password);
        // Obtain a named connection with user-provided credentials
        con = factory.createConnection("DefaultJNDI", "admin", "welcome");

        // Create ReflectingConnectionProducer instance
        ReflectingConnectionProducer pro = new ReflectingConnectionProducer(
            "com.topcoder.db.connectionfactory.producers.DataSourceImpl", "test_user", "test_pswd");

        // Get the default connection from producer directly.
        con = pro.createConnection();

        // Get the connection with user-provided credentials from producer directly.
        con = pro.createConnection("guest", "hello");

        // Instantiate an empty factory
        DBConnectionFactoryImpl factoryImpl = new DBConnectionFactoryImpl();

        // Populate the factory with connection producers
        factoryImpl.add("test", new JNDIConnectionProducer(jndiName));

        factoryImpl.add("production", new JDBCConnectionProducer(jdbcUrl, username, password));

        factoryImpl.add("stock", new JNDIConnectionProducer(jndiName, username, password));

        // Set the default producer.
        factoryImpl.setDefault("production");

        // Obtain a default connection with configured credentials.
        con = factoryImpl.createConnection();

        // Obtain a named connection with specified credentials.
        con = factoryImpl.createConnection("stock", "boss", "custom_password");

        // Remove the "test" producer.
        factoryImpl.remove("test");

        // Check whether that producer is still in the factory
        factoryImpl.contains("test"); // Should return false

        // Set the "stock" producer to be the default one.
        factoryImpl.setDefault("stock");

        // Get the name for the default producer.
        String defaultProducerName = factoryImpl.getDefault();

        // Get the "production" producer
        ConnectionProducer producer = factoryImpl.get("production");

        // Now we want to traverse the producers contained in the factory.
        Iterator iterator = factoryImpl.listConnectionProducerNames();
        while (iterator.hasNext()) {
            producer = factoryImpl.get((String) iterator.next());
        }
        // Remove all the producers in the factory at last.
        factoryImpl.clear();
    }
}
