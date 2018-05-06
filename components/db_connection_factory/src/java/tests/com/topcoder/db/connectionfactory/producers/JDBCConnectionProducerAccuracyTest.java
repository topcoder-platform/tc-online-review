/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.producers;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import com.topcoder.db.connectionfactory.TestHelper;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

import java.io.File;

import java.sql.Connection;

import java.util.Properties;

/**
 * <p>
 * Accuracy test of JDBCConnectionProducer class. It will create five JDBCConnectionProducer
 * instances with different constructors and the two createConnection methods will tested.
 * </p>
 * <p>
 * <b>Changes in version 1.1: </b><br>
 * 1) Some of duplicate from version 1.0 is removed. <br>
 * 2) Adds tests for creation of all the constructor. <br>
 * 3) New methods are all tested. <br>
 * 4) Adds tests for public fields. <br>
 * </p>
 *
 * @author qiucx0161, magicpig
 * @version 1.1
 * @since 1.0
 */
public class JDBCConnectionProducerAccuracyTest extends TestCase {
    /**
     * The namespace from which to read the configuration values from the ConfigManager.
     */
    private static final String NAMESPACE = "com.topcoder.db.connectionfactory.TestHelper";

    /**
     * The namespace for invalid property.
     *
     * @since 1.1
     */
    private static final String INVALID_PROPERTY = "invalid_property";

    /**
     * The ConfigManager instance to load the config file.
     */
    private static ConfigManager cm = ConfigManager.getInstance();

    /**
     * The TestHelper instance is used to load config file data for getting a property instance.
     */
    private TestHelper helper = new TestHelper();

    /**
     * Represent the jdbc url, it will be initialized in setup().
     *
     * @since 1.0
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
     * ConfigurationObject instance for testing.
     *
     * @since 1.1
     */
    private ConfigurationObject configurationObject;

    /**
     * SetUp the test environment.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }

        // added in version 1.1
        if (cm.existsNamespace(INVALID_PROPERTY)) {
            cm.removeNamespace(INVALID_PROPERTY);
        }

        try {
            cm.add(NAMESPACE, "test.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
            cm.add(INVALID_PROPERTY, "v1_1" + File.separator + "invalid_property.xml",
                ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
        } catch (Exception e) {
            throw e;
        }

        jdbcUrl = cm.getString(NAMESPACE, "JDBC.jdbc_url");
        username = cm.getString(NAMESPACE, "JDBC.user");
        password = cm.getString(NAMESPACE, "JDBC.password");

        configurationObject = new DefaultConfigurationObject("root");

        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue(JDBCConnectionProducer.JDBC_URL_PROPERTY, jdbcUrl);
        parameters.setPropertyValue(JDBCConnectionProducer.JDBC_DRIVER_CLASS_PROPERTY, cm.getString(NAMESPACE,
            "JDBC.jdbc_driver"));
        parameters.setPropertyValue(JDBCConnectionProducer.USERNAME_PROPERTY, username);
        parameters.setPropertyValue(JDBCConnectionProducer.PASSWORD_PROPERTY, password);
        configurationObject.addChild(parameters);

        // Driver should be loaded before creating connection.
        Class.forName("com.mysql.jdbc.Driver");
    }

    /**
     * SetUp the test environment.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.0
     */
    protected void tearDown() throws Exception {
        helper.releaseSource();
    }

    /**
     * Test createConnection method. It is used to create Connection instance, and should return
     * Connection successfully.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testCreateConnection1() throws Exception {
        JDBCConnectionProducer producer = new JDBCConnectionProducer(jdbcUrl, username, password);

        Object conn = producer.createConnection();
        assertTrue(conn instanceof Connection);

        ((Connection) conn).close();
    }

    /**
     * Test createConnection method. It is used to create Connection instance, and should return
     * Connection successfully. The data which is used to connection database is passed from
     * constructor via the Properties instance.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testCreateConnection3() throws Exception {
        Properties pro = new Properties();
        pro.put("user", username);
        pro.put("password", password);

        JDBCConnectionProducer jdbc = new JDBCConnectionProducer(jdbcUrl, pro);

        Object conn = jdbc.createConnection();
        assertTrue(conn instanceof Connection);

        ((Connection) conn).close();
    }

    /**
     * Test createConnection method. It is used to create Connection instance, and should return
     * Connection successfully.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testCreateConnection5() throws Exception {
        JDBCConnectionProducer producer = new JDBCConnectionProducer(helper.getJdbcProperty());

        Object conn = producer.createConnection();
        assertTrue(conn instanceof Connection);

        ((Connection) conn).close();
    }

    /**
     * <p>
     * Accuracy for constructor: JDBCConnectionProducer(String). <br>
     * Target: tests the creation.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorWithJdbcUrl() throws Exception {
        assertNotNull("Failed to create JDBCConnectionProducer.", new JDBCConnectionProducer(jdbcUrl));
    }

    /**
     * <p>
     * Accuracy for constructor: JDBCConnectionProducer(String, String, String). <br>
     * Target: tests the creation.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorWithJdbcUrlUserNamePassWord() throws Exception {
        assertNotNull("Failed to create JDBCConnectionProducer.", new JDBCConnectionProducer(jdbcUrl, username,
            password));
    }

    /**
     * <p>
     * Accuracy for constructor: JDBCConnectionProducer(String, Properties). <br>
     * Target: tests the creation.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorWithJdbcUrlProperties() throws Exception {
        assertNotNull("Failed to create JDBCConnectionProducer.", new JDBCConnectionProducer(jdbcUrl,
            new Properties()));
    }

    /**
     * <p>
     * Accuracy for constructor: JDBCConnectionProducer(Property). <br>
     * Target: tests the creation.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorWithProperty() throws Exception {
        assertNotNull("Failed to create JDBCConnectionProducer.", new JDBCConnectionProducer(helper
            .getJdbcProperty()));
    }

    /**
     * <p>
     * Accuracy for constructor: JDBCConnectionProducer(ConfigurationObject). <br>
     * Target: tests the creation.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorWithConfigurationObject() throws Exception {
        assertNotNull("Failed to create JDBCConnectionProducer.", new JDBCConnectionProducer(configurationObject));
    }

    /**
     * <p>
     * Accuracy for method: createConnection(). <br>
     * Target: tests the result with the instance created by ConfigurationObject
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionByInstanceFromConfigurationObject() throws Exception {
        Connection connection = new JDBCConnectionProducer(configurationObject).createConnection();
        assertTrue("The method createConnection is not correctly implemented.", connection instanceof Connection);

        ((Connection) connection).close();
    }

    /**
     * <p>
     * Accuracy for method: createConnection(String, String). <br>
     * Target: tests the result.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionWithNameAndPassword1() throws Exception {
        Connection connection = new JDBCConnectionProducer(configurationObject).createConnection(username,
            password);
        assertTrue("The method createConnection is not correctly implemented.", connection instanceof Connection);

        ((Connection) connection).close();
    }

    /**
     * <p>
     * Accuracy for method: createConnection(String, String). <br>
     * Target: tests the result.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionWithNameAndPassword2() throws Exception {
        ConfigurationObject newConfigurationObject = new DefaultConfigurationObject("new");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue(JDBCConnectionProducer.JDBC_URL_PROPERTY, jdbcUrl);
        parameters.setPropertyValue(JDBCConnectionProducer.JDBC_DRIVER_CLASS_PROPERTY, jdbcUrl);
        newConfigurationObject.addChild(parameters);

        Connection connection = new JDBCConnectionProducer(configurationObject).createConnection(username,
            password);
        assertTrue("The method createConnection is not correctly implemented.", connection instanceof Connection);

        ((Connection) connection).close();
    }

    /**
     * <p>
     * Test the value of public field USERNAME_PROPERTY.
     * </p>
     *
     * @since 1.1
     */
    public void testUSERNAME_PROPERTY() {
        assertEquals("The public field USERNAME_PROPERTY is not valid", "user",
            JDBCConnectionProducer.USERNAME_PROPERTY);
    }

    /**
     * <p>
     * Test the value of public field PASSWORD_PROPERTY.
     * </p>
     *
     * @since 1.1
     */
    public void testPASSWORD_PROPERTY() {
        assertEquals("The public field PASSWORD_PROPERTY is not valid", "password",
            JDBCConnectionProducer.PASSWORD_PROPERTY);
    }

    /**
     * <p>
     * Test the value of public field JDBC_URL_PROPERTY.
     * </p>
     *
     * @since 1.1
     */
    public void testJDBC_URL_PROPERTY() {
        assertEquals("The public field JDBC_URL_PROPERTY is not valid", "jdbc_url",
            JDBCConnectionProducer.JDBC_URL_PROPERTY);
    }

    /**
     * <p>
     * Test the value of public field JDBC_DRIVER_CLASS_PROPERTY.
     * </p>
     *
     * @since 1.1
     */
    public void testJDBC_DRIVER_CLASS_PROPERTY() {
        assertEquals("The public field JDBC_DRIVER_CLASS_PROPERTY is not valid", "jdbc_driver",
            JDBCConnectionProducer.JDBC_DRIVER_CLASS_PROPERTY);
    }
}
