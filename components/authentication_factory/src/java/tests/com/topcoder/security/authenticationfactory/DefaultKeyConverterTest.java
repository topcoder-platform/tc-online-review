/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * This class contains the unit tests for DefaultKeyConverter.</p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class DefaultKeyConverterTest extends TestCase {
    /**
     * <p>Default namespace to load DefaultKeyConverter.</p>
     */
    private static final String NAMESPACE = "com.topcoder.security.authenticationfactory.DefaultKeyConverter";

    /**
     *<p>The file to load config.</p>
     */
    private static final String FILENAME = "converter.xml";

    /**
     * <p>The DefaultKeyConverter instance.</p>
     */
    private DefaultKeyConverter converter = null;

    /**
     *<p>
     * Creates a test suite of the tests contained in this class.</p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(DefaultKeyConverterTest.class);
    }

    /**
     * <p>Setup the fixture, load the config.</p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        if (cm.existsNamespace(NAMESPACE)) {
            cm.removeNamespace(NAMESPACE);
        }
        cm.add(NAMESPACE, FILENAME, ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
    }

    /**
     * Clean up for each test cases.
     */
    protected void tearDown() {
        ConfigHelper.clearAllNamespace();
    }

    /**
     * <p>Test construct with null namespace, should throw NullPointerException.</p>
     */
    public void testDefaultKeyConverterNPE() {
        try {
            converter = new DefaultKeyConverter(null);
            fail("should throw NullPointerException");
        } catch (NullPointerException npe) {
            // pass
        } catch (ConfigurationException ce) {
            fail("should throw NullPointerException");
        }
    }

    /**
     * <p>Test construct with empty string, should throw IllegalArgumentException.</p>
     */
    public void testDefaultKeyConverterIAE() {
        try {
            converter = new DefaultKeyConverter("  ");
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // pass
        } catch (ConfigurationException ce) {
            fail("should throw IllegalArgumentException");
        }
    }

    /**
     * <p>Test constructor with error config file, will throw ConfigurationException.</p>
     */
    public void testDefaultKeyConverterCE() {
        try {
            final String error = "com.topcoder.security.authenticationfactory.ErrorConverter";
            ConfigManager cm = ConfigManager.getInstance();
            if (cm.existsNamespace(error)) {
                cm.removeNamespace(error);
            }
            cm.add(error, FILENAME, ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);

            converter = new DefaultKeyConverter(error);
            fail("should throw ConfigurationException");
        } catch (ConfigurationException ce) {
            // pass
        } catch (Exception e) {
            fail("should throw ConfigurationException");
        }
    }

    /**
     * <p>Test constructor with non-exist namespace, will throw ConfigurationException.</p>
     */
    public void testDefaultKeyConverterCE2() {
        try {
            // construct with non-exist namespace, throw ConfigurationException.
            converter = new DefaultKeyConverter("com.topcoder.security.authenticationfactory.blahblah");
            fail("should throw ConfigurationException");
        } catch (ConfigurationException ce) {
            // pass
        } catch (Exception e) {
            fail("should throw ConfigurationException");
        }
    }

    /**
     * <p>Test constructor and convert method.</p>
     */
    public void testDefaultKeyConverter() {
        try {
            converter = new DefaultKeyConverter(NAMESPACE);

            // convert exist string, will return corresponding string
            assertEquals(converter.convert("username"), "UserName");
            assertEquals(converter.convert("password"), "Pwd");

            // convert non-exist string, return itself
            assertEquals(converter.convert("pass"), "pass");
        } catch (IllegalArgumentException iae) {
            // pass
        } catch (ConfigurationException ce) {
            fail("should throw ConfigurationException");
        }
    }

    /**
     * <p>Test convert with invalid parameter, throw NullPointerException/IllegalArgumentException.</p>
     * @throws ConfigurationException to JUnit
     *
     */
    public void testConvertNPE() throws ConfigurationException {
        converter = new DefaultKeyConverter(NAMESPACE);

        try {
            converter.convert(null);
            fail("null is not acceptable, throw NullPointerException");
        } catch (NullPointerException npe) {
            // pass
        }
        try {
            converter.convert(" ");
            fail("empty string is not acceptable, throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }
}