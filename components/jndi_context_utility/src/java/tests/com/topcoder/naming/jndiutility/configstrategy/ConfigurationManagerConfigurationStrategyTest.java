/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0
 *
 * @ ConfigurationManagerConfigurationStrategyTest.java
 */
package com.topcoder.naming.jndiutility.configstrategy;

import com.topcoder.naming.jndiutility.ConfigurationException;
import com.topcoder.naming.jndiutility.ConfigurationStrategy;
import com.topcoder.naming.jndiutility.TestHelper;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;


/**
 * Junit test class for ConfigurationManagerConfigurationStrategy.
 *
 * @author Charizard
 * @version 2.0
 */
public class ConfigurationManagerConfigurationStrategyTest extends TestCase {
    /** Name of configuration file used in tests. */
    private static final String CONFIG_FILE = "ConfigurationManagerConfigurationStrategyTest.xml";

    /** Namespace used in tests. */
    private static final String NAMESPACE = ConfigurationManagerConfigurationStrategyTest.class.getName();

    /**
     * Tear down method. Clear configuration.
     *
     * @throws Exception if error occurs
     *
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        TestHelper.clearConfig();
    }

    /**
     * Test method for {@link
     * ConfigurationManagerConfigurationStrategy#ConfigurationManagerConfigurationStrategy(String)}. Accuracy case,
     * try to instantiate.
     */
    public void testConfigurationManagerConfigurationStrategyAccuracy() {
        assertNotNull("failed to instantiate", new ConfigurationManagerConfigurationStrategy(NAMESPACE));
    }

    /**
     * Test method for {@link
     * ConfigurationManagerConfigurationStrategy#ConfigurationManagerConfigurationStrategy(String)}. Failure case 1,
     * call with null.
     */
    public void testConfigurationManagerConfigurationStrategyFailure1() {
        try {
            new ConfigurationManagerConfigurationStrategy(null);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link
     * ConfigurationManagerConfigurationStrategy#ConfigurationManagerConfigurationStrategy(String)}. Failure case 2,
     * call with empty string.
     */
    public void testConfigurationManagerConfigurationStrategyFailure2() {
        try {
            new ConfigurationManagerConfigurationStrategy(" ");
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#getProperty(String)}. Accuracy case,
     * check the result.
     *
     * @throws Exception if error occurs
     */
    public void testGetPropertyAccuracy() throws Exception {
        TestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);
        assertEquals("wrong property", "value1", strategy.getProperty("property1"));
        assertEquals("wrong property", "  ", strategy.getProperty("property2"));
        assertNull("wrong property", strategy.getProperty("property3"));
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#getProperty(String)}. Failure case 1,
     * call with empty name.
     *
     * @throws Exception if error occurs
     */
    public void testGetPropertyFailure1() throws Exception {
        TestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);

        try {
            strategy.getProperty(" ");
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#getProperty(String)}. Failure case 2,
     * call without loading configuration file (unknown namespace).
     *
     * @throws Exception if error occurs
     */
    public void testGetPropertyFailure2() throws Exception {
        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);

        try {
            strategy.getProperty("property1");
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#setProperty(String, String)}. Accuracy
     * case, check the result.
     *
     * @throws Exception if error occurs
     */
    public void testSetPropertyAccuracy() throws Exception {
        TestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);
        String newValue = TestHelper.generateString();
        strategy.setProperty("property1", newValue);

        ConfigManager cm = ConfigManager.getInstance();
        assertEquals("should not commit", "value1", cm.getProperty(NAMESPACE, "property1"));
        assertEquals("wrong result", newValue, cm.getTemporaryString(NAMESPACE, "property1"));
        assertEquals("uncommitted property should be returned", newValue, strategy.getProperty("property1"));
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#setProperty(String, String)}. Failure
     * case 1, call with empty name.
     *
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure1() throws Exception {
        TestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);

        try {
            strategy.setProperty(" ", TestHelper.generateString());
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#setProperty(String, String)}. Failure
     * case 2, call with empty value.
     *
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure2() throws Exception {
        TestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);

        try {
            strategy.setProperty(TestHelper.generateString(), " ");
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#setProperty(String, String)}. Failure
     * case 3, call without loading configuration file (unknown namespace).
     *
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure3() throws Exception {
        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);

        try {
            strategy.setProperty(TestHelper.generateString(), TestHelper.generateString());
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#commitChanges()}. Accuracy case 1,
     * check the result.
     *
     * @throws Exception if error occurs
     */
    public void testCommitChangesAccuracy1() throws Exception {
        TestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);
        String newValue = TestHelper.generateString();
        strategy.setProperty("property4", newValue);
        strategy.commitChanges();

        ConfigManager cm = ConfigManager.getInstance();
        assertEquals("wrong result", newValue, cm.getProperty(NAMESPACE, "property4"));
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#commitChanges()}. Accuracy case 2,
     * check the result with no change at all.
     *
     * @throws Exception if error occurs
     */
    public void testCommitChangesAccuracy2() throws Exception {
        TestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);
        // should not throw exception here
        strategy.commitChanges();
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#commitChanges()}. Failure case, lock
     * the namespace before calling.
     *
     * @throws Exception if error occurs
     */
    public void testCommitChangesFailure() throws Exception {
        TestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);
        String newValue = TestHelper.generateString();
        strategy.setProperty("property4", newValue);

        ConfigManager.getInstance().lock(NAMESPACE, "test");

        try {
            strategy.commitChanges();
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        } finally {
            ConfigManager.getInstance().forceUnlock(NAMESPACE);
        }
    }
}
