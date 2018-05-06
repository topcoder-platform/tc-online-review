/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import com.topcoder.configuration.BaseConfigurationObject;
import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer;
import com.topcoder.db.connectionfactory.producers.JNDIConnectionProducer;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

import java.io.File;
import java.io.Serializable;

/**
 * <p>
 * Failure test for <code>DBConnectionFactoryImpl</code>. It tests all the failure cases of
 * DBConnectionFactoryImpl class.
 * </p>
 * <p>
 * <b> Change in Version 1.1 </b><br>
 * The failure tests for constructors with ConfigurationObject and the two new createConnection
 * methods are added.
 * </p>
 *
 * @author qiucx0161, magicpig
 * @version 1.1
 * @since 1.0
 */
public class DBConnectionFactoryImplFailureTest extends TestCase {
    /**
     * The namespace for invalid property.
     *
     * @since 1.1
     */
    private static final String INVALID_PROPERTY = "invalid_property";

    /**
     * The namespace from which to read the configuration values from the ConfigManager.
     */
    private static final String NAMESPACE = DBConnectionFactoryImplFailureTest.class.getName();

    /**
     * The ConfigManager instance to load the config file.
     */
    private static ConfigManager cm = ConfigManager.getInstance();

    /**
     * The folder which the config file is in.
     */
    private static final String CONFIG_DIR = "missing/";

    /**
     * The DBConnectionFactoryImpl instance is used to call its methods and it will be initialized
     * in setUp().
     */
    private DBConnectionFactoryImpl factory = null;

    /**
     * The ConnectionProducer instance is used to test add method. It will be initialized in
     * setUp().
     */
    private ConnectionProducer producer = null;

    /**
     * Represent the invalid jdbc url, it will be initialized in setup().
     *
     * @since 1.1
     */
    private String invalidJdbcurl = null;

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
     * Test for method.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        // Remove namespace after running a method.
        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }

        if (cm.existsNamespace(INVALID_PROPERTY)) {
            cm.removeNamespace(INVALID_PROPERTY);
        }

        if (cm.existsNamespace(helperNamespace)) {
            cm.removeNamespace(helperNamespace);
        }

        cm.add("test.xml");
        cm.add("v1_1" + File.separator + "invalid_property.xml");

        // Initialize the DBConnectionFactoryImpl
        // and ConnectionProducer instance
        factory = new DBConnectionFactoryImpl();

        username = cm.getString(helperNamespace, "JDBC.user");
        password = cm.getString(helperNamespace, "JDBC.password");
        invalidJdbcurl = cm.getString(INVALID_PROPERTY, "invalid_jdbc_url");

        String jndiName = "java:comp/env/jdbc/test";
        producer = new JNDIConnectionProducer(jndiName);
    }

    /**
     * <p>
     * Set the test environment.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        // Remove namespaces after running a method.
        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }

        if (cm.existsNamespace(helperNamespace)) {
            cm.removeNamespace(helperNamespace);
        }

        if (cm.existsNamespace(INVALID_PROPERTY)) {
            cm.removeNamespace(INVALID_PROPERTY);
        }
    }

    /**
     * Test loading data from config file. The default connection way is lost. It will be checked in
     * setDefault(). ConfigurationException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testMissingDefaultConnection() throws Exception {
        try {
            // Set the proper namespace for test.
            cm.add(NAMESPACE, CONFIG_DIR + "MissingDefaultConnection.xml",
                ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);

            new DBConnectionFactoryImpl(NAMESPACE);

            fail("Should throw ConfigurationException for missing default connection value.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * Test loading data from config file. The value of producer is lost. CE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testMissingProducerValue() throws Exception {
        try {
            // Set the proper namespace for test.
            cm.add(NAMESPACE, CONFIG_DIR + "MissingProducer.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);

            new DBConnectionFactoryImpl(NAMESPACE);

            fail("Should throw CE for missing value of producer.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * Test loading data from config file. One value of parameters is lost. CE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testMissingParametersValue() throws Exception {
        try {
            // Set the proper namespace for test.
            cm.add(NAMESPACE, CONFIG_DIR + "MissingParametersValue.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);

            new DBConnectionFactoryImpl(NAMESPACE);

            fail("Should throw CE for missing value of parameters.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * Test loading data from config file. The parameters field is lost. CE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testMissingParametersField() throws Exception {
        try {
            // Set the proper namespace for test.
            cm.add(NAMESPACE, CONFIG_DIR + "MissingParametersField.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);

            new DBConnectionFactoryImpl(NAMESPACE);

            fail("Should throw CE for missing default connection value.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * Test loading data from config file. The producer class is invalid. CE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testInvalidProducerClass() throws Exception {
        try {
            // Set the proper namespace for test.
            cm.add(NAMESPACE, CONFIG_DIR + "InvalidProducerClass.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);

            new DBConnectionFactoryImpl(NAMESPACE);

            fail("Should throw CE for invalid producer class name.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * Test Constructor with null namespace parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor1() throws Exception {
        try {
            new DBConnectionFactoryImpl((String) null);
            fail("Should throw IllegalArgumentException for null namespace.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with empty namespace parameter. IAE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor2() throws Exception {
        try {
            new DBConnectionFactoryImpl(" ");
            fail("Should throw IAE for empty namespace.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test createConnection with null connection name parameter. IllegalArgumentException is
     * expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCreateConnection1() throws Exception {
        try {
            factory.createConnection(null);
            fail("Should throw IllegalArgumentException for null name.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test createConnection with empty connection name parameter. IAE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCreateConnection2() throws Exception {
        try {
            factory.createConnection(" ");
            fail("Should throw IAE for empty name.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test add with null connection name parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAdd1() throws Exception {
        try {
            factory.add(null, producer);
            fail("Should throw IllegalArgumentException for null name.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test add with empty connection name parameter. IAE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAdd2() throws Exception {
        try {
            factory.add(" ", producer);
            fail("Should throw IAE for empty name.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test add with null ConnectionProducer instance. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAdd3() throws Exception {
        try {
            factory.add("tc", null);
            fail("Should throw IllegalArgumentException for null producer.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test remove method with null connection name parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testRemove1() throws Exception {
        try {
            factory.remove(null);
            fail("Should throw IllegalArgumentException for null name.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test remove method with empty connection name parameter. IAE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testRemove2() throws Exception {
        try {
            factory.remove(" ");
            fail("Should throw IAE for empty name.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test contains method with null connection name parameter. IllegalArgumentException is
     * expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testContains1() throws Exception {
        try {
            factory.contains(null);
            fail("Should throw IllegalArgumentException for null name.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test contains method with empty connection name parameter. IAE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testContains2() throws Exception {
        try {
            factory.contains(" ");
            fail("Should throw IAE for empty name.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test setDefault method with empty connection name parameter. IAE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testSetDefault2() throws Exception {
        try {
            factory.setDefault(" ");
            fail("Should throw IAE for empty name.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test get method with null connection name parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.0
     */
    public void testGet1() throws Exception {
        try {
            factory.get(null);
            fail("Should throw IllegalArgumentException for null name.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test get method with empty connection name parameter. IAE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGet2() throws Exception {
        try {
            factory.get(" ");
            fail("Should throw IAE for empty name.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: configurationObject is null. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithNullConfigurationObject() throws Exception {
        try {
            new DBConnectionFactoryImpl(null, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);

            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: namespace is null. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithNullNamespace() throws Exception {
        try {
            new DBConnectionFactoryImpl(new DefaultConfigurationObject("root"), null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: namespace is empty. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithEmptyNamespace() throws Exception {
        try {
            new DBConnectionFactoryImpl(new DefaultConfigurationObject("root"), "");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: namespace is empty. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithTrimmedEmptyNamespace() throws Exception {
        try {
            new DBConnectionFactoryImpl(new DefaultConfigurationObject("root"), "   ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: Missing child whose name is the given namespace. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject1() throws Exception {
        try {
            new DBConnectionFactoryImpl(new DefaultConfigurationObject("root"),
                DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause:"connections" child is missing. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject2() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: there is a child which does not contain "producer" property in "connections".
     * <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject3() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        // does not contain producer" property
        ConfigurationObject child3 = new DefaultConfigurationObject("invalid");
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: there is a child which contains invalid value of "producer" property in
     * "connections" and the invalid value is "noSuchClass". <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject4() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        // contain invalid "producer" property
        ConfigurationObject child3 = new DefaultConfigurationObject("invalid");
        child3.setPropertyValue("producer", "noSuchClass");
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: there is a child which contains invalid value of "producer" property in
     * "connections" and the invalid value is "java.lang.StringBuffer". <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject5() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        ConfigurationObject child3 = new DefaultConfigurationObject("test");
        child3.setPropertyValue("producer", "java.lang.StringBuffer");
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: the class to be created does not have a public constructor with
     * ConfigurationObject. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject6() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        ConfigurationObject child3 = new DefaultConfigurationObject("test");
        child3.setPropertyValue("producer", "com.topcoder.db.connectionfactory.MockCreatedClass1");
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: Error occur while creating Object through reflection. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject7() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        ConfigurationObject child3 = new DefaultConfigurationObject("test");
        child3.setPropertyValue("producer", "com.topcoder.db.connectionfactory.MockCreatedClass2");
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: The created class does not implement ConnectionProducer interface. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject8() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        ConfigurationObject child3 = new DefaultConfigurationObject("test");
        child3.setPropertyValue("producer", "com.topcoder.db.connectionfactory.MockCreatedClass3");
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: there is a child which contains invalid value of "producer" property in
     * "connections" and the invalid value is not string instance. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject9() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        // contain invalid "producer" property
        ConfigurationObject child3 = new DefaultConfigurationObject("invalid");
        child3.setPropertyValue("producer", new Serializable() {
        });
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: there is a child which contains invalid value of "producer" property in
     * "connections" and the invalid value is null. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject10() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        // contain invalid "producer" property
        ConfigurationObject child3 = new DefaultConfigurationObject("invalid");
        child3.setPropertyValue("producer", null);
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: the value of the "producer" is non-null, non-String Object. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject11() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        ConfigurationObject child3 = new DefaultConfigurationObject("test");
        child2.addChild(child3);

        child2.setPropertyValue("default", new Serializable() {
        });

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException uce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: there is not producer for the "default" value. <br>
     * Expected: UnknownConnectionException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject12() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child2.setPropertyValue("default", "noSuch");
        child1.addChild(child2);

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("UnknownConnectionException is expected.");
        } catch (UnknownConnectionException uce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: the "default" value is empty string. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject13() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child2.setPropertyValue("default", "  ");
        child1.addChild(child2);

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: the "default" value is non-null non-string instance. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject14() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child2.setPropertyValue("default", new Serializable() {
        });
        child1.addChild(child2);

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: The created class is abstract. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject15() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        ConfigurationObject child3 = new DefaultConfigurationObject("test");
        child3.setPropertyValue("producer", "com.topcoder.db.connectionfactory.MockCreatedClass4");
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject,String). <br>
     * Failure cause: ConfigurationAccessException occurs. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectStringWithInvalidConfigurationObject16() throws Exception {
        ConfigurationObject configurationObject = new BaseConfigurationObject() {
            public ConfigurationObject getChild(String name) throws ConfigurationAccessException {
                throw new ConfigurationAccessException("for test");
            }
        };

        try {
            new DBConnectionFactoryImpl(configurationObject, DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: configurationObject is null. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithNullConfigurationObject() throws Exception {
        try {
            new DBConnectionFactoryImpl((ConfigurationObject) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: Missing child whose name is the given namespace. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject1() throws Exception {
        try {
            new DBConnectionFactoryImpl(new DefaultConfigurationObject("root"));
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause:"connections" child is missing. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject2() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: there is a child which does not contain "producer" property in "connections".
     * <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject3() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        // does not contain producer" property
        ConfigurationObject child3 = new DefaultConfigurationObject("invalid");
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: there is a child which contains invalid value of "producer" property in
     * "connections" and the invalid value is "noSuchClass". <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject4() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        // contain invalid "producer" property
        ConfigurationObject child3 = new DefaultConfigurationObject("invalid");
        child3.setPropertyValue("producer", "noSuchClass");
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: there is a child which contains invalid value of "producer" property in
     * "connections" and the invalid value is "java.lang.StringBuffer". <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject5() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        ConfigurationObject child3 = new DefaultConfigurationObject("test");
        child3.setPropertyValue("producer", "java.lang.StringBuffer");
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: the class to be created does not have a public constructor with
     * ConfigurationObject. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject6() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        ConfigurationObject child3 = new DefaultConfigurationObject("test");
        child3.setPropertyValue("producer", "com.topcoder.db.connectionfactory.MockCreatedClass1");
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: Error occur while creating Object through reflection. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject7() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        ConfigurationObject child3 = new DefaultConfigurationObject("test");
        child3.setPropertyValue("producer", "com.topcoder.db.connectionfactory.MockCreatedClass2");
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: The created class does not implement ConnectionProducer interface. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject8() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        ConfigurationObject child3 = new DefaultConfigurationObject("test");
        child3.setPropertyValue("producer", "com.topcoder.db.connectionfactory.MockCreatedClass3");
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: there is a child which contains invalid value of "producer" property in
     * "connections" and the invalid value is not string instance. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject9() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        // contain invalid "producer" property
        ConfigurationObject child3 = new DefaultConfigurationObject("invalid");
        child3.setPropertyValue("producer", new Serializable() {
        });
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: there is a child which contains invalid value of "producer" property in
     * "connections" and the invalid value is null. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject10() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        // contain invalid "producer" property
        ConfigurationObject child3 = new DefaultConfigurationObject("invalid");
        child3.setPropertyValue("producer", null);
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: the value of the "producer" is non-null, non-String Object. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject11() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        ConfigurationObject child3 = new DefaultConfigurationObject("test");
        child2.addChild(child3);

        child2.setPropertyValue("default", new Serializable() {
        });

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: there is not producer for the "default" value. <br>
     * Expected: UnknownConnectionException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject12() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");

        child2.setPropertyValue("default", "noSuch");
        child1.addChild(child2);

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("UnknownConnectionException is expected.");
        } catch (UnknownConnectionException uce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject, String). <br>
     * Failure cause: the "default" value is empty string. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject13() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child2.setPropertyValue("default", "  ");
        child1.addChild(child2);

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: the "default" value is non-null non-string instance. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject14() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child2.setPropertyValue("default", new Serializable() {
        });
        child1.addChild(child2);

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: The created class is abstract. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject15() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child1 = new DefaultConfigurationObject(DBConnectionFactoryImpl.DEFAULT_NAMESPACE);
        configurationObject.addChild(child1);

        ConfigurationObject child2 = new DefaultConfigurationObject("connections");
        child1.addChild(child2);

        ConfigurationObject child3 = new DefaultConfigurationObject("test");
        child3.setPropertyValue("producer", "com.topcoder.db.connectionfactory.MockCreatedClass4");
        child2.addChild(child3);

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for constructor: DBConnectionFactoryImpl(ConfigurationObject). <br>
     * Failure cause: ConfigurationAccessException occurs. <br>
     * Expected: ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectWithInvalidConfigurationObject16() throws Exception {
        ConfigurationObject configurationObject = new BaseConfigurationObject() {
            public ConfigurationObject getChild(String name) throws ConfigurationAccessException {
                throw new ConfigurationAccessException("for test");
            }
        };

        try {
            new DBConnectionFactoryImpl(configurationObject);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for method: createConnection(String, String). <br>
     * Failure cause: DBConnectionException is thrown by the producer. <br>
     * Expected: DBConnectionException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionStringStringForDBConnectionException() throws Exception {
        factory.add("default", new JDBCConnectionProducer(invalidJdbcurl));
        factory.setDefault("default");

        try {
            factory.createConnection(username, password);
            fail("DBConnectionException is expected.");
        } catch (DBConnectionException dbce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for method: createConnection(String, String). <br>
     * Failure cause: no defaultConnection. <br>
     * Expected: NoDefaultConnectionException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionStringStringForNoDefaultConnectionException() throws Exception {
        factory.add("default", new JDBCConnectionProducer(invalidJdbcurl));

        try {
            factory.createConnection(username, password);
            fail("NoDefaultConnectionException is expected.");
        } catch (NoDefaultConnectionException ndce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for method: createConnection(String, String, String). <br>
     * Failure cause: DBConnectionException is thrown by the producer. <br>
     * Expected: DBConnectionException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionStringStringStringForDBConnectionException() throws Exception {
        factory.add("default", new JDBCConnectionProducer(invalidJdbcurl));

        try {
            factory.createConnection("default", username, password);
            fail("DBConnectionException is expected.");
        } catch (DBConnectionException dbce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for method: createConnection(String, String, String). <br>
     * Failure cause: there is not producer for the given name. <br>
     * Expected: UnknownConnectionException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionStringStringStringForUnknownConnectionException() throws Exception {
        try {
            factory.createConnection("default", username, password);
            fail("UnknownConnectionException is expected.");
        } catch (UnknownConnectionException uce) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for method: createConnection(String, String, String). <br>
     * Failure cause: name is null. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionStringStringStringWithNullName() throws Exception {
        try {
            factory.createConnection(null, username, password);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for method: createConnection(String, String, String). <br>
     * Failure cause: name is empty. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionStringStringStringWithEmptyName() throws Exception {
        try {
            factory.createConnection("", username, password);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for method: createConnection(String, String, String). <br>
     * Failure cause: name is trimmed empty. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCreateConnectionStringStringStringWithTrimmedEmptyName() throws Exception {
        try {
            factory.createConnection("  ", username, password);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }
}
