/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.impl;

import java.io.File;
import java.sql.Connection;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculatorConfigurationException;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculatorException;
import com.topcoder.management.payment.calculator.TestHelper;
import com.topcoder.util.log.Log;


/**
 * <p>
 * Unit test case of {@link BaseProjectPaymentCalculator}.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class BaseProjectPaymentCalculatorTest {
    /**
     * <p>
     * Represents the ConfigurationObject instance used for testing.
     * </p>
     */
    private ConfigurationObject config;

    /**
     * <p>
     * Represents BaseProjectPaymentCalculator instance to test against.
     * </p>
     */
    private BaseProjectPaymentCalculator calc;

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BaseProjectPaymentCalculatorTest.class);
    }

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception
     *             to jUnit.
     */
    @Before
    public void setUp() throws Exception {
        ConfigurationFileManager cfm = new ConfigurationFileManager(TestHelper.CONFIG_FILE);

        ConfigurationObject root = cfm.getConfiguration(DefaultProjectPaymentCalculator.class.getName());
        config = root.getChild(DefaultProjectPaymentCalculator.class.getName());

        calc = new BaseProjectPaymentCalculator(config) {
        };
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception
     *             to jUnit.
     */
    @After
    public void tearDown() throws Exception {
        calc = null;
        config = null;
    }

    /**
     * <p>
     * Accuracy test method for <code>BaseProjectPaymentCalculator(ConfigurationObject)</code>.
     * </p>
     * <p>
     * Verifies that the configuration values are loaded correctly.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testBaseProjectPaymentCalculator() throws Exception {
        Assert.assertNotNull("Unable to instantiate BaseProjectPaymentCalculator", calc);

        Assert.assertNotNull("logger is not created", TestHelper.getFieldValue(calc, "logger"));
        Assert.assertEquals("Incorrect connectionName", config.getPropertyValue("connection_name"), TestHelper
            .getFieldValue(calc, "connectionName"));
        Assert.assertNotNull("dbConnectionFactory is not created", TestHelper.getFieldValue(calc,
            "dbConnectionFactory"));
    }

    /**
     * <p>
     * Failure test method for <code>BaseProjectPaymentCalculator(ConfigurationObject)</code> when
     * <code>configurationObject</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects that <code>IllegalArgumentException</code> is thrown.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBaseProjectPaymentCalculator_NullConfigurationObject() {
        new BaseProjectPaymentCalculator(null) {
        };
    }

    /**
     * <p>
     * Accuracy test method for <code>BaseProjectPaymentCalculator(ConfigurationObject)</code> when
     * <code>configurationObject</code> contains empty <code>logger_name</code> property.
     * </p>
     * <p>
     * Expects that underlying logger is null.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testBaseProjectPaymentCalculator_EmptyLoggerName() throws Exception {
        config.setPropertyValue("logger_name", "  ");
        calc = new BaseProjectPaymentCalculator(config) {
        };
        Assert.assertNull("Shall be null", TestHelper.getFieldValue(calc, "logger"));
    }

    /**
     * <p>
     * Accuracy test method for <code>BaseProjectPaymentCalculator(ConfigurationObject)</code> when
     * <code>configurationObject</code> contains empty <code>connection_name</code> property.
     * </p>
     * <p>
     * Expects that underlying connectionName is null..
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testBaseProjectPaymentCalculator_EmptyConnectionName() throws Exception {
        config.setPropertyValue("connection_name", "  ");
        calc = new BaseProjectPaymentCalculator(config) {
        };
        Assert.assertNull("Shall be null", TestHelper.getFieldValue(calc, "connectionName"));
    }

    /**
     * <p>
     * Failure test method for <code>BaseProjectPaymentCalculator(ConfigurationObject)</code> when configuration
     * for DBConnectionFactory does not contain configuration for default connection producer.
     * </p>
     * <p>
     * Expects that <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testBaseProjectPaymentCalculator_UnknownConnection() throws Exception {
        ConfigurationObject dbConfig = config.getChild("db_connection_factory_config");
        ConfigurationObject dbConnFactoryImplConfig = dbConfig.getChild(DBConnectionFactoryImpl.class.getName());
        ConfigurationObject connections = dbConnFactoryImplConfig.getChild("connections");
        String defaultConn = (String) connections.getPropertyValue("default");
        connections.removeChild(defaultConn);

        new BaseProjectPaymentCalculator(config) {
        };
    }

    /**
     * <p>
     * Failure test method for <code>BaseProjectPaymentCalculator(ConfigurationObject)</code> when fails to
     * configure DBConnectionFactory.
     * </p>
     * <p>
     * Expects that <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testBaseProjectPaymentCalculator_DBConfigurationError() throws Exception {
        ConfigurationObject dbConfig = config.getChild("db_connection_factory_config");
        dbConfig.removeChild(DBConnectionFactoryImpl.class.getName());

        new BaseProjectPaymentCalculator(config) {
        };
    }

    /**
     * <p>
     * Accuracy test method for {@link BaseProjectPaymentCalculator#getLogger()} when <code>logger_name</code> is
     * defined in configuration.
     * </p>
     * <p>
     * Expects the returned value is a non-null instance.
     * </p>
     */
    @Test
    public void testGetLogger() {
        Log logger = calc.getLogger();
        Assert.assertNotNull("logger is not set", logger);
    }

    /**
     * <p>
     * Accuracy test method for {@link BaseProjectPaymentCalculator#getLogger()} when <code>logger_name</code> is
     * not defined in configuration.
     * </p>
     * <p>
     * Expects the returned value is null.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testGetLogger_NoLoggerName() throws Exception {
        config.removeProperty("logger_name");
        calc = new BaseProjectPaymentCalculator(config) {
        };
        Log logger = calc.getLogger();
        Assert.assertNull("logger should not be set", logger);
    }

    /**
     * <p>
     * Accuracy test method for {@link BaseProjectPaymentCalculator#createConnection()} when
     * <code>connection_name</code> is defined in configuration.
     * </p>
     * <p>
     * Expects the connection is created successfully using the provided configuration.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testCreateConnection() throws Exception {
        Connection conn = null;
        try {
            conn = calc.createConnection();
            Assert.assertNotNull("Connection is not created", conn);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * <p>
     * Accuracy test method for {@link BaseProjectPaymentCalculator#createConnection()} when
     * <code>connection_name</code> is not defined in configuration.
     * </p>
     * <p>
     * Expects the connection is created successfully using the default configuration.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testCreateConnection_Default() throws Exception {
        config.removeProperty("connection_name");
        calc = new BaseProjectPaymentCalculator(config) {
        };

        Connection conn = null;
        try {
            conn = calc.createConnection();
            Assert.assertNotNull("Connection is not created", conn);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * <p>
     * Failure test method for {@link BaseProjectPaymentCalculator#createConnection()} when fails to create
     * connection.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorException.class)
    public void testCreateConnection_Fail() throws Exception {
        ConfigurationObject dbConfig = config.getChild("db_connection_factory_config");
        ConfigurationObject dbConnFactoryImplConfig = dbConfig.getChild(DBConnectionFactoryImpl.class.getName());
        ConfigurationObject connections = dbConnFactoryImplConfig.getChild("connections");
        String defaultConn = (String) connections.getPropertyValue("default");

        ConfigurationObject defaultConnConfig = connections.getChild(defaultConn);
        ConfigurationObject connParamsConfig = defaultConnConfig.getChild("parameters");
        connParamsConfig.setPropertyValue("jdbc_url", "jdbc:unknown");

        calc = new BaseProjectPaymentCalculator(config) {
        };
        calc.createConnection();
    }

    /**
     * <p>
     * Accuracy test method for
     * {@link BaseProjectPaymentCalculator#getConfigurationObject(java.lang.String, java.lang.String)}.
     * </p>
     * <p>
     * Expects the configuration is loaded correctly from the file.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test
    public void testGetConfigurationObject() throws Exception {
        ConfigurationObject result =
            BaseProjectPaymentCalculator.getConfigurationObject(TestHelper.CONFIG_FILE,
                DefaultProjectPaymentCalculator.class.getName());

        Assert.assertEquals("Incorrect config", config.getPropertyValue("logger_name"), result
            .getPropertyValue("logger_name"));
    }

    /**
     * <p>
     * Failure test method for
     * {@link BaseProjectPaymentCalculator#getConfigurationObject(java.lang.String, java.lang.String)} when
     * <code>configFilePath</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigurationObject_NullConfigFilePath() throws Exception {
        BaseProjectPaymentCalculator.getConfigurationObject(null, BaseProjectPaymentCalculator.class.getName());
    }

    /**
     * <p>
     * Failure test method for
     * {@link BaseProjectPaymentCalculator#getConfigurationObject(java.lang.String, java.lang.String)} when
     * <code>configFilePath</code> is empty.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigurationObject_EmptyConfigFilePath() throws Exception {
        BaseProjectPaymentCalculator.getConfigurationObject("  ", BaseProjectPaymentCalculator.class.getName());
    }

    /**
     * <p>
     * Failure test method for
     * {@link BaseProjectPaymentCalculator#getConfigurationObject(java.lang.String, java.lang.String)} when
     * <code>namespace</code> is <code>null</code>.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigurationObject_NullNamespace() throws Exception {
        BaseProjectPaymentCalculator.getConfigurationObject(TestHelper.CONFIG_FILE, null);
    }

    /**
     * <p>
     * Failure test method for
     * {@link BaseProjectPaymentCalculator#getConfigurationObject(java.lang.String, java.lang.String)} when
     * <code>namespace</code> is empty.
     * </p>
     * <p>
     * Expects <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetConfigurationObject_EmptyNamespace() throws Exception {
        BaseProjectPaymentCalculator.getConfigurationObject(TestHelper.CONFIG_FILE, "  ");
    }

    /**
     * <p>
     * Failure test method for
     * {@link BaseProjectPaymentCalculator#getConfigurationObject(java.lang.String, java.lang.String)} when the
     * type of <code>configFilePath</code> is not supported.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testGetConfigurationObject_UnregonizedFileType() throws Exception {
        BaseProjectPaymentCalculator.getConfigurationObject("test_files" + File.separator + "unknown.txt",
            BaseProjectPaymentCalculator.class.getName());
    }

    /**
     * <p>
     * Failure test method for
     * {@link BaseProjectPaymentCalculator#getConfigurationObject(java.lang.String, java.lang.String)} when the
     * type of <code>configFilePath</code> is not supported.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testGetConfigurationObject_UnknownNamespace() throws Exception {
        BaseProjectPaymentCalculator.getConfigurationObject(TestHelper.CONFIG_FILE, "UnknownNamespace");
    }

    /**
     * <p>
     * Failure test method for
     * {@link BaseProjectPaymentCalculator#getConfigurationObject(java.lang.String, java.lang.String)} when fails
     * to parse <code>configFilePath</code> as a valid configuration fail.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testGetConfigurationObject_ConfigParseError() throws Exception {
        BaseProjectPaymentCalculator.getConfigurationObject("test_files" + File.separator + "invalid_config"
            + File.separator + "invalid_config.properties", BaseProjectPaymentCalculator.class.getName());
    }

    /**
     * <p>
     * Failure test method for
     * {@link BaseProjectPaymentCalculator#getConfigurationObject(java.lang.String, java.lang.String)} when
     * configuration file that is associated with namespace in <code>configFilePath</code> cannot be found.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testGetConfigurationObject_ConfigNotFound() throws Exception {
        BaseProjectPaymentCalculator.getConfigurationObject("test_files" + File.separator + "invalid_config"
            + File.separator + "missing_file.properties", BaseProjectPaymentCalculator.class.getName());
    }

    /**
     * <p>
     * Failure test method for
     * {@link BaseProjectPaymentCalculator#getConfigurationObject(java.lang.String, java.lang.String)} when there
     * is namespace conflict found in <code>configFilePath</code>.
     * </p>
     * <p>
     * Expects <code>ProjectPaymentCalculatorConfigurationException</code> is thrown.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    @Test(expected = ProjectPaymentCalculatorConfigurationException.class)
    public void testGetConfigurationObject_NamespaceConflict() throws Exception {
        BaseProjectPaymentCalculator.getConfigurationObject("test_files" + File.separator + "invalid_config"
            + File.separator + "namespace_conflict.properties", BaseProjectPaymentCalculator.class.getName());
    }
}
