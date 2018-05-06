/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.producers;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import com.topcoder.db.connectionfactory.MockInitialContextFactory;

import com.topcoder.util.config.Property;

import junit.framework.TestCase;

import java.sql.Connection;

import java.util.Hashtable;


/**
 * <p>
 * Accuracy test of JNDIConnectionProducer class. It will create five JNDIConnectionProducer
 * instances with different constructor, and the methods will be call via the created instance.
 * </p>
 *
 * <p>
 * <b>Changes in version 1.1: </b><br>
 * The new createConnection methods and the new constructor are tested.
 * </p>
 *
 * @author qiucx0161, magicpig
 * @version 1.1
 *
 * @since 1.0
 */
public class JNDIConnectionProducerAccuracyTest extends TestCase {
    /**
     * Represent the jndi name.
     */
    private String jndiName = "java:comp/env/jdbc/tcs";

    /**
     * Test createConnection method. It is used to create Connection instance, and should return
     * Connection successfully.
     *
     * @throws Exception to Junit.
     */
    public void testCreateConnection1() throws Exception {
        JNDIConnectionProducer producer = new JNDIConnectionProducer(jndiName);

        Object conn = producer.createConnection();
        assertTrue(conn instanceof Connection);

        ((Connection) conn).close();
    }

    /**
     * Test createConnection method. It is used to create Connection instance, and should return
     * Connection successfully.
     *
     * @throws Exception to Junit.
     */
    public void testCreateConnection2() throws Exception {
        Hashtable env = new Hashtable();
        JNDIConnectionProducer twoParams = new JNDIConnectionProducer(jndiName, env);
        JNDIConnectionProducer threeParams = new JNDIConnectionProducer(jndiName, "username",
                "password");
        JNDIConnectionProducer fourParams = new JNDIConnectionProducer(jndiName, env, "username",
                "password");

        Property properties = new Property("pro");

        JNDIConnectionProducer propertyParam = new JNDIConnectionProducer(jndiName, env);

        // Create the Connection instance via the different way
        Object conn = twoParams.createConnection();
        assertTrue(conn instanceof Connection);

        ((Connection) conn).close();

        Object conn2 = threeParams.createConnection();
        assertTrue(conn2 instanceof Connection);

        ((Connection) conn2).close();

        Object conn3 = fourParams.createConnection();
        assertTrue(conn3 instanceof Connection);

        ((Connection) conn3).close();

        Object conn4 = propertyParam.createConnection();
        assertTrue(conn4 instanceof Connection);

        ((Connection) conn4).close();
    }

    /**
     * <p>
     * Accuracy for constructor: JNDIConnectionProducer(ConfigurationObject). <br>
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
        parameters.setPropertyValue("jndi_name", "correct_url");
        configurationObject.addChild(parameters);
        assertNotNull("Failed to create JNDIConnectionProducer",
            new JNDIConnectionProducer(configurationObject));
    }

    /**
     * <p>
     * Accuracy for constructor: JNDIConnectionProducer(ConfigurationObject). <br>
     * Target: tests the creation with some special configuration.
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
        parameters.setPropertyValue("jndi_name", "correct_url");

        String emptyString = "  ";

        // empty string value
        parameters.setPropertyValue("empty", emptyString);

        // underline key
        parameters.setPropertyValue("_._", "underline");

        // normal string value
        parameters.setPropertyValue("normal", "normal");

        // null value(ignored)
        parameters.setPropertyValue("null", null);

        configurationObject.addChild(parameters);

        // clear for testing the created hashTable
        MockInitialContextFactory.env = null;

        assertNotNull("Failed to create JNDIConnectionProducer",
            new JNDIConnectionProducer(configurationObject));

        // five pairs are expected, another one is set from test_files/jndi.properties
        assertEquals("Failed to create JNDIConnectionProducer", 5,
            MockInitialContextFactory.env.size());
        assertEquals("Failed to create JNDIConnectionProducer", "correct_url",
            MockInitialContextFactory.env.get("jndi.name"));
        assertEquals("Failed to create JNDIConnectionProducer", emptyString,
            MockInitialContextFactory.env.get("empty"));
        assertEquals("Failed to create JNDIConnectionProducer", "underline",
            MockInitialContextFactory.env.get("..."));
        assertEquals("Failed to create JNDIConnectionProducer", "normal",
            MockInitialContextFactory.env.get("normal"));
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
        parameters.setPropertyValue("jndi_name", "correct_url");
        configurationObject.addChild(parameters);

        Object conn = new JNDIConnectionProducer(configurationObject).createConnection();
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
        parameters.setPropertyValue("jndi_name", "correct_url");
        configurationObject.addChild(parameters);

        Object conn = new JNDIConnectionProducer(configurationObject).createConnection("test",
                "test");
        assertTrue("Failed to create connection.", conn instanceof Connection);
        ((Connection) conn).close();
    }
}
