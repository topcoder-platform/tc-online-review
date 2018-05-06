/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.producers;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import com.topcoder.db.connectionfactory.DBConnectionException;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;

import junit.framework.TestCase;

import java.io.File;
import java.io.Serializable;

import java.util.Properties;

/**
 * <p>
 * Failure test for <code>JDBCConnectionProducer</code> class. Test the constructor of
 * JDBCConnectionProducer who is given invalid parameters and the createConnection in the condition
 * for DBConnectionException to throw.
 * </p>
 * <p>
 * <b>Changes in version 1.1: </b><br>
 * 1) All the test for NullPointerException are replaced by IllegalArgumentException. <br>
 * 2) Some minor modification made in codes to fix some mistake and make the code more clear. <br>
 * 3) Adds a test for constructor with Property when the value of JDBC_URL_PROPERTY is empty. <br>
 * 4) Adds a test for constructor with Property for ClassNotFoundException. <br>
 * 5) Adds failure tests for JDBCConnectionProducer(String, Properties) which is missing in version
 * 1.0. <br>
 * 6) Adds failure tests for the new constructor with ConfigurationObject and new method
 * createConnection(String, String). <br>
 * </p>
 *
 * @author qiucx0161, magicpig
 * @version 1.1
 * @since 1.0
 */
public class JDBCConnectionProducerFailureTest extends TestCase {
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
     * Represent the jdbc url, it will be initialized in setup().
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
     * Represent the invalid jdbc url, it will be initialized in setup().
     *
     * @since 1.1
     */
    private String invalidJdbcurl = null;

    /**
     * Represent the invalid user name, it will be initialized in setup().
     *
     * @since 1.1
     */
    private String invalidUsername = null;

    /**
     * Represent the invalid password, it will be initialized in setup().
     *
     * @since 1.1
     */
    private String invalidPassword = null;

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

        cm.add(NAMESPACE, "test.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
        cm.add(INVALID_PROPERTY, "v1_1" + File.separator + "invalid_property.xml",
            ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);

        jdbcUrl = cm.getString(NAMESPACE, "JDBC.jdbc_url");
        username = cm.getString(NAMESPACE, "JDBC.user");
        password = cm.getString(NAMESPACE, "JDBC.password");
        invalidJdbcurl = cm.getString(INVALID_PROPERTY, "invalid_jdbc_url");
        invalidUsername = cm.getString(INVALID_PROPERTY, "invalid_username");
        invalidPassword = cm.getString(INVALID_PROPERTY, "invalid_password");

        // Driver should be loaded before creating connection.
        Class.forName("com.mysql.jdbc.Driver");
    }

    /**
     * SetUp the test environment.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    protected void tearDown() throws Exception {
        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }

        if (cm.existsNamespace(INVALID_PROPERTY)) {
            cm.removeNamespace(INVALID_PROPERTY);
        }
    }

    /**
     * Test Constructor with null jdbcUrl parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor1() throws Exception {
        try {
            new JDBCConnectionProducer((String) null);
            fail("Should throw IllegalArgumentException for null jdbcUrl.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with empty jdbcUrl parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor2() throws Exception {
        try {
            new JDBCConnectionProducer(" ");
            fail("Should throw IllegalArgumentException for empty jdbcUrl.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with null jdbcUrl parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor3() throws Exception {
        try {
            new JDBCConnectionProducer(null, username, password);
            fail("Should throw IllegalArgumentException for null jdbcUrl.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with empty jdbcUrl parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor4() throws Exception {
        try {
            new JDBCConnectionProducer(" ", username, password);
            fail("Should throw IllegalArgumentException for empty jdbcUrl.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with null username parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor5() throws Exception {
        try {
            new JDBCConnectionProducer(jdbcUrl, null, password);
            fail("Should throw IllegalArgumentException for null username.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with null password parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor6() throws Exception {
        try {
            new JDBCConnectionProducer(jdbcUrl, username, null);
            fail("Should throw IllegalArgumentException for null password.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with null properties parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor7() throws Exception {
        try {
            new JDBCConnectionProducer((Property) null);
            fail("Should throw IllegalArgumentException for null properties.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with invalid properties who do not contain jdbcUrl field.
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor8() throws Exception {
        try {
            Property proper = new Property("tcs");
            new JDBCConnectionProducer(proper);
            fail("Should throw IllegalArgumentException for the absence of jdbcUrl.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test createConnection with invalid jdbc url. DBConnectionException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCreateConnectionFail1() throws Exception {
        try {
            // Create JDBCConnectionProducer instance with invalid
            // jdbc url.
            JDBCConnectionProducer jdbc = new JDBCConnectionProducer(cm.getString(INVALID_PROPERTY,
                "invalid_jdbc_url"));

            jdbc.createConnection();

            fail("Should throw DBConnectionException for the invalid jdbcUrl.");
        } catch (DBConnectionException dbe) {
            // Pass
        }
    }

    /**
     * Test createConnection with invalid password. DBConnectionException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCreateConnectionFail2() throws Exception {
        try {
            // Create JDBCConnectionProducer instance with invalid
            // password.
            JDBCConnectionProducer jdbc = new JDBCConnectionProducer(jdbcUrl, username, invalidPassword);

            jdbc.createConnection();

            fail("Should throw DBConnectionException for the invalid password.");
        } catch (DBConnectionException dbe) {
            // Pass
        }
    }

    /**
     * Test createConnection with invalid user. The user and password properties will be put into
     * Properties instance. DBConnectionException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCreateConnectionFail3() throws Exception {
        try {
            // Initialize the Properties instance with invalid user
            // and password.
            Properties pro = new Properties();
            pro.put("user", invalidUsername);
            pro.put("password", password);

            JDBCConnectionProducer jdbc = new JDBCConnectionProducer(jdbcUrl, pro);

            jdbc.createConnection();

            fail("Should throw DBConnectionException for the invalid user.");
        } catch (DBConnectionException dbe) {
            // Pass
        }
    }



    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(Property properties). <br>
     * Failure cause: the value of JDBC_URL_PROPERTY is empty. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorPropertyWithEmptyJDBCUrl() throws Exception {
        try {
            Property proper = cm.getPropertyObject(INVALID_PROPERTY, "invalid_jdbc_url");
            new JDBCConnectionProducer(proper);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(Property properties). <br>
     * Failure cause: the value of JDBC_DRIVER_CLASS_PROPERTY is not a correct class of jdbc-driver.
     * <br>
     * Expected: ClassNotFoundException
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorPropertyWithInvalidDriverValue() throws Exception {
        try {
            Property proper = cm.getPropertyObject(INVALID_PROPERTY, "invalid_jdbc_driver");
            new JDBCConnectionProducer(proper);
            fail("ClassNotFoundException is expected.");
        } catch (ClassNotFoundException cnfe) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(String jdbcUrl, Properties properties).
     * <br>
     * Failure cause: null jdbcUrl. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @since 1.1
     */
    public void testCtorStringPropertiesWithNullJdbcUrl() {
        try {
            new JDBCConnectionProducer(null, new Properties());
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(String jdbcUrl, Properties properties).
     * <br>
     * Failure cause: empty jdbcUrl. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @since 1.1
     */
    public void testCtorStringPropertiesWithEmptyJdbcUrl() {
        try {
            new JDBCConnectionProducer("", new Properties());
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(String jdbcUrl, Properties properties).
     * <br>
     * Failure cause: trimmed empty jdbcUrl. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @since 1.1
     */
    public void testCtorStringPropertiesWithTrimmedEmptyJdbcUrl() {
        try {
            new JDBCConnectionProducer("  ", new Properties());
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(String jdbcUrl, Properties properties).
     * <br>
     * Failure cause: null properties. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @since 1.1
     */
    public void testCtorStringPropertiesWithNullProperties() {
        try {
            new JDBCConnectionProducer(jdbcUrl, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(ConfigurationObject
     * configurationObject). <br>
     * Failure cause: null configurationObject. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithNullConfigurationObject() throws Exception {
        try {
            new JDBCConnectionProducer((ConfigurationObject) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(ConfigurationObject
     * configurationObject). <br>
     * Failure cause: configurationObject miss parameters child. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithConfigurationObjectMissingParametersChild() throws Exception {
        try {
            new JDBCConnectionProducer(new DefaultConfigurationObject("root"));
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(ConfigurationObject
     * configurationObject). <br>
     * Failure cause: configurationObject miss jdbcUrl property . <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithConfigurationObjectMissingJdbcUrlProperty() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        configurationObject.addChild(parameters);

        try {
            new JDBCConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(ConfigurationObject
     * configurationObject). <br>
     * Failure cause: configurationObject contains empty value of jdbcUrl property . <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithConfigurationObjectContainsEmptyJdbcUrlProperty() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue(JDBCConnectionProducer.JDBC_URL_PROPERTY, "");
        configurationObject.addChild(parameters);

        try {
            new JDBCConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(ConfigurationObject
     * configurationObject). <br>
     * Failure cause: configurationObject contains trimmed empty value of jdbcUrl property . <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithConfigurationObjectContainsTrimmedEmptyJdbcUrlProperty()
        throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue(JDBCConnectionProducer.JDBC_URL_PROPERTY, "   ");
        configurationObject.addChild(parameters);

        try {
            new JDBCConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(ConfigurationObject
     * configurationObject). <br>
     * Failure cause: configurationObject contains non-string value of jdbcUrl property . <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithConfigurationObjectContainsNonStringValueOfJdbcUrlProperty()
        throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue(JDBCConnectionProducer.JDBC_URL_PROPERTY, new Serializable() {
        });
        configurationObject.addChild(parameters);

        try {
            new JDBCConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(ConfigurationObject
     * configurationObject). <br>
     * Failure cause: configurationObject contains invalid jdbc driver name . <br>
     * Expected: ClassNotFoundException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithConfigurationObjectContainsInvalidJDBCDriverName() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue(JDBCConnectionProducer.JDBC_URL_PROPERTY, jdbcUrl);
        parameters.setPropertyValue(JDBCConnectionProducer.JDBC_DRIVER_CLASS_PROPERTY, "noSuchClass");
        configurationObject.addChild(parameters);

        try {
            new JDBCConnectionProducer(configurationObject);
            fail("ClassNotFoundException is expected.");
        } catch (ClassNotFoundException cnfe) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(ConfigurationObject
     * configurationObject). <br>
     * Failure cause: configurationObject contains non-null non-string of jdbc driver property .
     * <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithConfigurationObjectContainsNonStringValueJDBCDriverProperty()
        throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue(JDBCConnectionProducer.JDBC_URL_PROPERTY, jdbcUrl);
        parameters.setPropertyValue(JDBCConnectionProducer.JDBC_DRIVER_CLASS_PROPERTY, new Serializable() {
        });
        configurationObject.addChild(parameters);

        try {
            new JDBCConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException cnfe) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: JDBCConnectionProducer(ConfigurationObject
     * configurationObject). <br>
     * Failure cause: configurationObject contains non-null non-string value of other property .
     * <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithConfigurationObjectContainsNonStringValueOtherProperties()
        throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue(JDBCConnectionProducer.JDBC_URL_PROPERTY, jdbcUrl);
        parameters.setPropertyValue(JDBCConnectionProducer.JDBC_DRIVER_CLASS_PROPERTY + "other",
            new Serializable() {
            });
        configurationObject.addChild(parameters);

        try {
            new JDBCConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException cnfe) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for method: createConnection(String username, String password). <br>
     * Failure cause: jdbc url is invalid. <br>
     * Expected: DBConnectionException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionWithInvalidUrl() throws Exception {
        try {
            new JDBCConnectionProducer(invalidJdbcurl).createConnection(username, password);
            fail("DBConnectionException is expected.");
        } catch (DBConnectionException dbe) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for method: createConnection(String username, String password). <br>
     * Failure cause: password is invalid. <br>
     * Expected: DBConnectionException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionWithInvalidPassword() throws Exception {
        try {
            new JDBCConnectionProducer(jdbcUrl).createConnection(username, invalidPassword);
            fail("DBConnectionException is expected.");
        } catch (DBConnectionException dbe) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for method: createConnection(String username, String password). <br>
     * Failure cause: username is invalid. <br>
     * Expected: DBConnectionException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionWithInvalidUserName() throws Exception {
        try {
            new JDBCConnectionProducer(jdbcUrl).createConnection(invalidUsername, password);

            fail("DBConnectionException is expected.");
        } catch (DBConnectionException dbe) {
            // Pass
        }
    }
}
