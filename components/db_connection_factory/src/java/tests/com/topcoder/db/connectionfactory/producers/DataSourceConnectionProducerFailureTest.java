/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.producers;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.TestHelper;

import com.topcoder.util.config.Property;

import junit.framework.TestCase;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * <p>
 * Failure test for DataSourceConnectionProducer. it will create DataSourceConnectionProducer
 * instance whose parameter is a DataSource instance which is created via the data from config file.
 * And them the method will be call to test via the create DataSourceConnectionProducer instance.
 * </p>
 * <p>
 * <b>Changes in version 1.1: </b><br>
 * </p>
 * <p>
 * Adds tests for the new constructor and the new createConnection methods.
 * </p>
 *
 * @author qiucx0161, magicpig
 * @version 1.1
 * @since 1.0
 */
public class DataSourceConnectionProducerFailureTest extends TestCase {
    /**
     * DataSource instance is used to test and will be initialized in setUp().
     */
    private DataSource ds = null;

    /**
     * TestHelper instance is used to call its methods for testing.
     */
    private TestHelper helper = null;

    /**
     * DataSourceConnectionProducer instance is used to call its methods, and it will be initialized
     * in setUp().
     */
    private DataSourceConnectionProducer prod = null;

    /**
     * SetUp the test environment.
     *
     * @throws Exception
     *             to Junit.
     */
    protected void setUp() throws Exception {
        helper = new TestHelper();
        ds = helper.getDataSource();
        prod = new DataSourceConnectionProducer(ds);
    }

    /**
     * Release the resource.
     *
     * @throws Exception
     *             to Junit.
     */
    protected void tearDown() throws Exception {
        helper.releaseSource();
    }

    /**
     * Test Constructor with null properties parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor1() throws Exception {
        try {
            new DataSourceConnectionProducer((Property) null);
            fail("Should throw IllegalArgumentException for null properties.");
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
    public void testConstructor2() throws Exception {
        try {
            new DataSourceConnectionProducer((DataSource) null);
            fail("Should throw IllegalArgumentException for null DataSource.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test createConnection with null datasource. DBE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCreateConnectionFail() throws Exception {
        DataSourceConnectionProducer pro = new DataSourceConnectionProducer();

        try {
            pro.createConnection();
            fail("Should throw DBE for null datasource.");
        } catch (DBConnectionException dbe) {
            // Pass
        }
    }

    /**
     * Test setDataSource with null datasource. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testSetDataSource() throws Exception {
        try {
            prod.setDataSource(null);
            fail("Should throw IllegalArgumentException for null datasource.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test setUsername with null datasource. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testSetUsername() throws Exception {
        try {
            prod.setUsername(null);
            fail("Should throw IllegalArgumentException for null username.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test setPassword with null datasource. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testSetPassword() throws Exception {
        try {
            prod.setPassword(null);
            fail("Should throw IllegalArgumentException for null password.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DataSourceConnectionProducer(ConfigurationObject
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
            new DataSourceConnectionProducer((ConfigurationObject) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DataSourceConnectionProducer(ConfigurationObject
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
            new DataSourceConnectionProducer(new DefaultConfigurationObject("root"));
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DataSourceConnectionProducer(ConfigurationObject
     * configurationObject). <br>
     * Failure cause: configurationObject contains non-string value of password property . <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithConfigurationObjectContainsNonStringValueOfPasswordProperty()
        throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue(DataSourceConnectionProducer.PASSWORD_PROPERTY, new Serializable() {
        });
        configurationObject.addChild(parameters);

        try {
            new DataSourceConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DataSourceConnectionProducer(ConfigurationObject). <br>
     * Failure cause: configurationObject contains non-string value of name property . <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithConfigurationObjectContainsNonStringValueOfNameProperty()
        throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue(DataSourceConnectionProducer.USERNAME_PROPERTY, new Serializable() {
        });
        configurationObject.addChild(parameters);

        try {
            new DataSourceConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for method: createConnection(String, String). <br>
     * Failure cause: dataSource is missing. <br>
     * Expected: DBConnectionException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionStringStringWhenDataSourceIsMissing() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        configurationObject.addChild(parameters);

        try {
            new DataSourceConnectionProducer(configurationObject).createConnection("name", "password");
            fail("DBConnectionException is expected.");
        } catch (DBConnectionException dbe) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for method: createConnection(String, String). <br>
     * Failure cause: dataSource.getConnection throw SQLException. Expected: DBConnectionException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionObjectStringStringWhenErrorOccurDuringGettingConnection() throws Exception {
        DataSourceConnectionProducer producer = new DataSourceConnectionProducer();
        producer.setDataSource(new DataSourceImpl() {
            public Connection getConnection(String username, String password) throws SQLException {
                throw new SQLException("For test");
            }
        });
        try {
            producer.createConnection("name", "password");
            fail("DBConnectionException is expected.");
        } catch (DBConnectionException dbe) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for method: createConnection(). <br>
     * Failure cause: dataSource.getConnection throw SQLException. Expected: DBConnectionException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionObjectWhenErrorOccurDuringGettingConnection() throws Exception {
        DataSourceConnectionProducer producer = new DataSourceConnectionProducer();
        producer.setDataSource(new DataSourceImpl() {
            public Connection getConnection() throws SQLException {
                throw new SQLException("For test");
            }
        });
        try {
            producer.createConnection();
            fail("DBConnectionException is expected.");
        } catch (DBConnectionException dbe) {
            // Pass
        }
    }
}
