/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.producers;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import junit.framework.TestCase;

import java.sql.Connection;

import javax.naming.InitialContext;

import javax.sql.DataSource;


/**
 * <p>
 * Accuracy test of DataSourceConnectionProducer class. It will create a
 * DataSourceConnectionProducer instances and the methods will be call via the created instance.
 * </p>
 *
 * <p>
 * <b>Changes in version 1.1: </b><br>
 * The new constructor and the protected field are tested. <br>
 * </p>
 *
 * @author qiucx0161, magicpig
 * @version 1.1
 * @since 1.0
 */
public class DataSourceConnectionProducerAccuracyTest extends TestCase {
    /**
     * <p>
     * The DataSource instance is used to initialize the DataSourceConnectionProducer instance, and
     * it will be initialized in setUp().
     * </p>
     */
    private DataSource ds = null;

    /**
     * The DataSource instance is used to set to the original DataSource and it will be initialized
     * in setUp().
     */
    private DataSource otherDataSource = null;

    /**
     * The DataSourceConnectionProducer instance is used to call its methods and will be
     * initialized in setUp().
     */
    private DataSourceConnectionProducer producer = null;

    /**
     * SetUp the test envrionment.
     *
     * @throws Exception to Junit.
     */
    protected void setUp() throws Exception {
        String jndiName = "java:comp/env/jdbc/tcs";
        String otherJndiName = "java:comp/env/jdbc/otherTcs";

        ds = (DataSource) new InitialContext().lookup(jndiName);
        otherDataSource = (DataSource) new InitialContext().lookup(otherJndiName);
        producer = new DataSourceConnectionProducer(ds);
    }

    /**
     * Test createConnection method. It is used to create Connection instance, and should return
     * Connection successfully.
     *
     * @throws Exception to Junit.
     */
    public void testCreateConnection() throws Exception {
        Object conn = producer.createConnection();
        assertTrue(conn instanceof Connection);

        ((Connection) conn).close();
    }

    /**
     * Test getDataSource method. It should return the proper DataSource : ds.
     */
    public void testGetDataSource() {
        assertEquals("Should return proper DataSource.", ds, producer.getDataSource());
    }

    /**
     * Test setDataSource method. First, it returns the original DataSource. Next, set a new
     * DataSource, and then ,return the new DataSource.
     */
    public void testSetDataSource() {
        assertEquals("Should return proper DataSource.", ds, producer.getDataSource());

        // Set a new DataSource, and then, get it.
        producer.setDataSource(otherDataSource);

        assertEquals("Should return proper DataSource.", otherDataSource, producer.getDataSource());
    }

    /**
     * Test createConnection. The DataSource will be initialized by the DataSourceImpl. The method
     * getConnection(...) of DataSourceImpl will create Connection only whenn the username is
     * 'test' and password is 'test', return null otherwise. This method will return null.
     *
     * @throws Exception to JUnit
     */
    public void testCreateConnection1() throws Exception {
        // Create the DataSource instance with its implementation.
        DataSource datasource = new DataSourceImpl();

        DataSourceConnectionProducer pro = new DataSourceConnectionProducer(datasource, "test",
                "invalid");

        assertNull(pro.createConnection());
    }

    /**
     * Test createConnection. The DataSource will be initialized by the DataSourceImpl. The method
     * getConnection(...) of DataSourceImpl will create Connection only whenn the username is
     * 'test' and password is 'test', return null otherwise. This method will return Connection
     * instance.
     *
     * @throws Exception to JUnit
     */
    public void testCreateConnection2() throws Exception {
        // Create the DataSource instance with its implementation.
        DataSource datasource = new DataSourceImpl();

        DataSourceConnectionProducer pro = new DataSourceConnectionProducer(datasource, "test",
                "test");

        Object conn = pro.createConnection();
        assertTrue(conn instanceof Connection);
    }

    /**
     * <p>
     * Accuracy for constructor: DataSourceConnectionProducer(ConfigurationObject). <br>
     * Target: tests the creation.
     * </p>
     *
     * @throws Exception when error occurs
     *
     * @since 1.1
     */
    public void testCtorConfigurationObjectAccuracy1()
        throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue(DataSourceConnectionProducer.PASSWORD_PROPERTY, "test");
        parameters.setPropertyValue(DataSourceConnectionProducer.USERNAME_PROPERTY, "test");
        configurationObject.addChild(parameters);
        assertNotNull("Failed to create ReflectingConnectionProducer",
            new DataSourceConnectionProducer(configurationObject));
    }

    /**
     * <p>
     * Accuracy for constructor: DataSourceConnectionProducer(ConfigurationObject). <br>
     * Target: tests the creation when DataSourceConnectionProducer.USERNAME_PROPERTY is missing
     * </p>
     *
     * @throws Exception when error occurs
     *
     * @since 1.1
     */
    public void testCtorConfigurationObjectAccuracy2()
        throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue(DataSourceConnectionProducer.PASSWORD_PROPERTY, "test");
        configurationObject.addChild(parameters);
        assertNotNull("Failed to create ReflectingConnectionProducer",
            new DataSourceConnectionProducer(configurationObject));
    }

    /**
     * <p>
     * Accuracy for constructor: DataSourceConnectionProducer(ConfigurationObject). <br>
     * Target: tests the creation when DataSourceConnectionProducer.PASSWORD_PROPERTY is missing
     * </p>
     *
     * @throws Exception when error occurs
     *
     * @since 1.1
     */
    public void testCtorConfigurationObjectAccuracy3()
        throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue(DataSourceConnectionProducer.USERNAME_PROPERTY, "test");
        configurationObject.addChild(parameters);
        assertNotNull("Failed to create ReflectingConnectionProducer",
            new DataSourceConnectionProducer(configurationObject));
    }

    /**
     * <p>
     * Accuracy for constructor: DataSourceConnectionProducer(ConfigurationObject). <br>
     * Target: tests the creation when DataSourceConnectionProducer.PASSWORD_PROPERTY and
     * DataSourceConnectionProducer.USERNAME_PROPERTY are missing.
     * </p>
     *
     * @throws Exception when error occurs
     *
     * @since 1.1
     */
    public void testCtorConfigurationObjectAccuracy4()
        throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        configurationObject.addChild(parameters);
        assertNotNull("Failed to create ReflectingConnectionProducer",
            new DataSourceConnectionProducer(configurationObject));
    }

    /**
     * <p>
     * Test the value of public field PARAMETERS.
     * </p>
     *
     * @since 1.1
     */
    public void testPARAMETERS() {
        assertEquals("The public field PARAMETERS is not valid", "parameters",
            DataSourceConnectionProducer.PARAMETERS);
    }
}
