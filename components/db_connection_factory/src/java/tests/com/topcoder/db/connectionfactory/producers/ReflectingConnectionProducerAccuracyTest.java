/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.producers;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import com.topcoder.db.connectionfactory.TestHelper;

import junit.framework.TestCase;

import java.sql.Connection;

import javax.sql.DataSource;


/**
 * <p>
 * Accuracy test of ReflectingConnectionProducer class. It will create three
 * ReflectingConnectionProducer instances with different constructor, and the methods will be call
 * via the created instance.
 * </p>
 *
 * <p>
 * <b>Changes in version 1.1: </b><br> The new constructor and createConnetion methods are tested.
 * </p>
 *
 * @author qiucx0161, magicpig
 * @version 1.1
 *
 * @since 1.0
 */
public class ReflectingConnectionProducerAccuracyTest extends TestCase {
    /**
     * The class name is used to reflect the producer class.
     */
    private static final String CLASSNAME = "com.topcoder.db.connectionfactory.producers.DataSourceImpl";

    /**
     * The ReflectingConnectionProducer instance which will be initialized by the constructor who
     * has property parameter.
     */
    private TestHelper helper = new TestHelper();

    /**
     * Release the resource.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        helper.releaseSource();
    }

    /**
     * Test createConnection method. It is used to create Connection instance, and should return
     * Connection successfully.
     *
     * @throws Exception to Junit.
     */
    public void testCreateConnection1() throws Exception {
        ReflectingConnectionProducer stringParam = new ReflectingConnectionProducer(CLASSNAME);

        ReflectingConnectionProducer propertyParam = new ReflectingConnectionProducer(helper.getJndiProperty());

        // The default getConnection method of DataSourceImpl
        // is used to create Connection instance.
        Object conn = stringParam.createConnection();
        assertTrue(conn instanceof Connection);
        ((Connection) conn).close();

        Object conn1 = propertyParam.createConnection();
        assertTrue(conn1 instanceof Connection);
        ((Connection) conn1).close();
    }

    /**
     * Test createConnection. The DataSource will be initialized by the DataSourceImpl. The method
     * getConnection(...) of DataSourceImpl will create Connection only whenn the username is
     * 'test' and password is 'test', return null otherwise. This method will return null.
     *
     * @throws Exception to JUnit
     */
    public void testCreateConnection2() throws Exception {
        ReflectingConnectionProducer threeParams = new ReflectingConnectionProducer(CLASSNAME,
                "test", "password");

        // Create the DataSource instance with its implementation.
        DataSource ds = new DataSourceImpl();

        assertNull(threeParams.createConnection());
    }

    /**
     * <p>
     * Test createConnection. The DataSource will be initialized by the DataSourceImpl. The method
     * getConnection(...) of DataSourceImpl will create Connection only whenn the username is
     * 'test' and password is 'test', return null otherwise. This method will return Connection
     * instance.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCreateConnection3() throws Exception {
        // Create the DataSource instance with its implementation.
        DataSource ds = new DataSourceImpl();

        ReflectingConnectionProducer pro = new ReflectingConnectionProducer(CLASSNAME, "test",
                "test");

        Object conn = pro.createConnection();
        assertTrue(conn instanceof Connection);
        ((Connection) conn).close();
    }

    /**
     * <p>
     * Accuracy for constructor: ReflectingConnectionProducer(ConfigurationObject). <br>
     * Target: tests the creation.
     * </p>
     *
     * @throws Exception when error occurs
     *
     * @since 1.1
     */
    public void testCtorConfigurationObjectAccuracy() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue("datasource_class", CLASSNAME);
        configurationObject.addChild(parameters);
        assertNotNull("Failed to create ReflectingConnectionProducer",
            new ReflectingConnectionProducer(configurationObject));
    }

    /**
     * <p>
     * Accuracy for method: createConnection(). <br>
     * Target: tests the functionality of the instance created by constructor with
     * ConfigurationObject.
     * </p>
     *
     * @throws Exception when error occurs
     *
     * @since 1.1
     */
    public void testCreateConnectionAccuracy1() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue("datasource_class", CLASSNAME);
        configurationObject.addChild(parameters);

        Object conn = new ReflectingConnectionProducer(configurationObject).createConnection();
        assertTrue("Failed to create connection.", conn instanceof Connection);
        ((Connection) conn).close();
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
    public void testCreateConnectionAccuracy2() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
        ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
        parameters.setPropertyValue("datasource_class", CLASSNAME);
        configurationObject.addChild(parameters);

        Object conn = new ReflectingConnectionProducer(configurationObject).createConnection("test",
                "test");
        assertTrue("Failed to create connection.", conn instanceof Connection);
        ((Connection) conn).close();
    }
}
