/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.producers;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import com.topcoder.util.config.Property;

import junit.framework.TestCase;

import java.io.Serializable;

import java.util.Hashtable;

import javax.naming.NamingException;

/**
 * <p>
 * Failure test for JNDIConnectionProducer. It simply tests the constructor of
 * JNDIConnectionProducer with invalid parameters.
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
public class JNDIConnectionProducerFailureTest extends TestCase {
    /**
     * Test Constructor with null jndiName parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor1() throws Exception {
        try {
            new JNDIConnectionProducer((String) null);
            fail("Should throw IllegalArgumentException for null jndiName.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with empty jndiName parameter. IAE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor2() throws Exception {
        try {
            new JNDIConnectionProducer(" ");
            fail("Should throw IAE for empty jndiName.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with null jndiName parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor3() throws Exception {
        try {
            new JNDIConnectionProducer(null, "username", "password");
            fail("Should throw IllegalArgumentException for null jndiName.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with empty jndiName parameter. IAE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor4() throws Exception {
        try {
            new JNDIConnectionProducer(" ", "username", "password");
            fail("Should throw IAE for empty jndiName.");
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
            new JNDIConnectionProducer("jdbc/tcs", null, "password");
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
            new JNDIConnectionProducer("jdbc/tcs", "username", null);
            fail("Should throw IllegalArgumentException for null password.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with null jndiName parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor7() throws Exception {
        try {
            new JNDIConnectionProducer(null, new Hashtable());
            fail("Should throw IllegalArgumentException for null jndiName.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with empty jndiName parameter. IAE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor8() throws Exception {
        try {
            new JNDIConnectionProducer(" ", new Hashtable());
            fail("Should throw IAE for empty jndiName.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with null jndiName parameter. IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor9() throws Exception {
        try {
            new JNDIConnectionProducer(null, new Hashtable(), "username", "password");
            fail("Should throw IllegalArgumentException for null jndiName.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with empty jndiName parameter. IAE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor10() throws Exception {
        try {
            new JNDIConnectionProducer(" ", new Hashtable(), "username", "password");
            fail("Should throw IAE for empty jndiName.");
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
    public void testConstructor11() throws Exception {
        try {
            new JNDIConnectionProducer("jdbc/tcs", new Hashtable(), null, "password");
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
    public void testConstructor12() throws Exception {
        try {
            new JNDIConnectionProducer("jdbc/tcs", new Hashtable(), "username", null);
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
    public void testConstructor13() throws Exception {
        try {
            new DataSourceConnectionProducer((Property) null);
            fail("Should throw IllegalArgumentException for null properties.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * Test Constructor with invalid properties who do not contain jndiName field. IAE is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor14() throws Exception {
        try {
            Property proper = new Property("tcs");
            new JNDIConnectionProducer(proper);
            fail("Should throw IAE for the absence of jndiName.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: JNDIConnectionProducer(ConfigurationObject). <br>
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
            new JNDIConnectionProducer((String) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: JNDIConnectionProducer(ConfigurationObject). <br>
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
            new JNDIConnectionProducer(new DefaultConfigurationObject("test"));
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: JNDIConnectionProducer(ConfigurationObject). <br>
     * Failure cause: configurationObject misses "jndi_name" property. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectMissingJndiNameProperty() throws Exception {
        try {
            ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
            ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
            configurationObject.addChild(parameters);
            new JNDIConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: JNDIConnectionProducer(ConfigurationObject). <br>
     * Failure cause: the value of "jndi_name" property is null. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectInvalidJndiNameProperty1() throws Exception {
        try {
            ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
            ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
            parameters.setPropertyValue("jndi_name", null);
            configurationObject.addChild(parameters);

            new JNDIConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: JNDIConnectionProducer(ConfigurationObject). <br>
     * Failure cause: the value of "jndi_name" property is not String instance. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectInvalidJndiNameProperty2() throws Exception {
        try {
            ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
            ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
            parameters.setPropertyValue("jndi_name", new Serializable() {
            });
            configurationObject.addChild(parameters);

            new JNDIConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: JNDIConnectionProducer(ConfigurationObject). <br>
     * Failure cause: one of the sub-properties in "parameters" is not String. <br>
     * Expected: IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectInvalidConfigurationInConfigurationObject() throws Exception {
        try {
            ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
            ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
            parameters.setPropertyValue("jndi_name", "java:comp/env/jdbc/test");
            parameters.setPropertyValue("invalid", new Serializable() {
            });
            configurationObject.addChild(parameters);

            new JNDIConnectionProducer(configurationObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Pass
        }
    }

    /**
     * <p>
     * Failure test for Constructor: JNDIConnectionProducer(ConfigurationObject). <br>
     * Failure cause: MockContext throw NamingException as expected . <br>
     * Expected: NamingException.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     * @since 1.1
     */
    public void testCtorConfigurationObjectForNamingException() throws Exception {
        try {
            ConfigurationObject configurationObject = new DefaultConfigurationObject("test");
            ConfigurationObject parameters = new DefaultConfigurationObject("parameters");
            parameters.setPropertyValue("jndi_name", "throwNamingException");
            configurationObject.addChild(parameters);

            new JNDIConnectionProducer(configurationObject);
            fail("NamingException is expected.");
        } catch (NamingException ne) {
            // Pass
        }
    }
}
