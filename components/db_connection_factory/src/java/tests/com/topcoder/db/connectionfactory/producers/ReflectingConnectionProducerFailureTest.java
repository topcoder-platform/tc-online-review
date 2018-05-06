/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.producers;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import com.topcoder.util.config.Property;

import junit.framework.TestCase;

import java.io.Serializable;

/**
 * <p>
 * Failure test for <code>ReflectingConnectionProducer</code>. It simply test the constructor of
 * ReflectingConnectionProducer
 * </p>
 * <p>
 * <b>Changes in version 1.1: </b><br>
 * The new constructor are tested.
 * </p>
 *
 * @author qiucx0161, magicpig
 * @version 1.1
 * @since 1.0
 */
public class ReflectingConnectionProducerFailureTest extends TestCase {
    /**
     * Test Constructor with null dataSourceClass parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor1() throws Exception {
        try {
            new ReflectingConnectionProducer((String) null);
            fail("Should throw IllegalArgumentException for null dataSourceClass.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with empty dataSourceClass parameter. IAE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor2() throws Exception {
        try {
            new ReflectingConnectionProducer(" ");
            fail("Should throw IAE for empty dataSourceClass.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with null dataSourceClass parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor3() throws Exception {
        try {
            new ReflectingConnectionProducer(null, "username", "password");
            fail("Should throw IllegalArgumentException for null dataSourceClass.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with empty dataSourceClass parameter. IAE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor4() throws Exception {
        try {
            new ReflectingConnectionProducer(" ", "username", "password");
            fail("Should throw IAE for empty dataSourceClass.");
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
            new ReflectingConnectionProducer("com.topcoder.db.connectionfactory.producers.DataSourceImpl", null,
                "password");
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
            new ReflectingConnectionProducer("com.topcoder.db.connectionfactory.producers.DataSourceImpl",
                "username", null);
            fail("Should throw NPE for null password.");
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
            new DataSourceConnectionProducer((Property) null);
            fail("Should throw IllegalArgumentException for null properties.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with invalid properties who do not contain dataSourceClass field. IAE is
     * expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor8() throws Exception {
        try {
            Property proper = new Property("tcs");
            new JNDIConnectionProducer(proper);
            fail("Should throw IAE for the absence of dataSourceClass.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: ReflectingConnectionProducer(ConfigurationObject). <br>
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
            new ReflectingConnectionProducer((String) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: ReflectingConnectionProducer(ConfigurationObject). <br>
     * Failure cause: configurationObject misses child named "parameters". <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectMissingParametersChild() throws Exception {
        try {
            new ReflectingConnectionProducer(new DefaultConfigurationObject("test"));
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: ReflectingConnectionProducer(ConfigurationObject). <br>
     * Failure cause: configurationObject misses "datasource_class" property. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectMissingDataSourceClassProperty() throws Exception {
        try {
            ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
            ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
            configurationObject.addChild(parameters);
            new ReflectingConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: ReflectingConnectionProducer(ConfigurationObject). <br>
     * Failure cause: the value of "datasource_class" property is null. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectInvalidDatasourceClassProperty1() throws Exception {
        try {
            ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
            ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
            parameters.setPropertyValue("datasource_class", null);
            configurationObject.addChild(parameters);

            new ReflectingConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: ReflectingConnectionProducer(ConfigurationObject). <br>
     * Failure cause: the value of "datasource_class" property is not String instance. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectInvalidDatasourceClassProperty2() throws Exception {
        try {
            ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
            ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
            parameters.setPropertyValue("datasource_class", new Serializable() {
            });
            configurationObject.addChild(parameters);

            new ReflectingConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: ReflectingConnectionProducer(ConfigurationObject). <br>
     * Failure cause: the value of "datasource_class" property is not a valid class name. <br>
     * Expected: ClassNotFoundException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectInvalidDatasourceClassProperty3() throws Exception {
        try {
            ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
            ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
            parameters.setPropertyValue("datasource_class", "noSuchClass");
            configurationObject.addChild(parameters);
            new ReflectingConnectionProducer(configurationObject);
            fail("ClassNotFoundException is expected.");
        } catch (ClassNotFoundException cnfe) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: ReflectingConnectionProducer(ConfigurationObject). <br>
     * Failure cause: the value of "datasource_class" property is not a valid class name. <br>
     * Expected: ClassNotFoundException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectInvalidDatasourceClassProperty4() throws Exception {
        try {
            ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
            ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
            parameters.setPropertyValue("datasource_class", "noSuchClass");
            configurationObject.addChild(parameters);
            new ReflectingConnectionProducer(configurationObject);
            fail("ClassNotFoundException is expected.");
        } catch (ClassNotFoundException cnfe) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: ReflectingConnectionProducer(ConfigurationObject). <br>
     * Failure cause: the created instance from "datasource_class" property is not a DataSource.
     * <br>
     * Expected: ClassCastException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectInvalidDatasourceClassProperty5() throws Exception {
        try {
            ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
            ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
            parameters.setPropertyValue("datasource_class", "java.lang.StringBuffer");
            configurationObject.addChild(parameters);
            new ReflectingConnectionProducer(configurationObject);
            fail("ClassCastException is expected.");
        } catch (ClassCastException cce) {
            // Pass
        }
    }
}
