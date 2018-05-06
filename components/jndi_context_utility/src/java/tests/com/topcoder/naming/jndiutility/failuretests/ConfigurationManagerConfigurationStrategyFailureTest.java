/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0
 *
 * @ ConfigurationManagerConfigurationStrategyTest.java
 */
package com.topcoder.naming.jndiutility.failuretests;

import junit.framework.TestCase;

import com.topcoder.naming.jndiutility.ConfigurationException;
import com.topcoder.naming.jndiutility.ConfigurationStrategy;
import com.topcoder.naming.jndiutility.configstrategy.ConfigurationManagerConfigurationStrategy;
import com.topcoder.util.config.ConfigManager;

/**
 * Failure test class for ConfigurationManagerConfigurationStrategy.
 * 
 * @author kaqi072821
 * @version 2.0
 */
public class ConfigurationManagerConfigurationStrategyFailureTest extends TestCase {
    /** Name of configuration file used in tests. */
    private static final String CONFIG_FILE = "failuretests/ConfigurationManagerConfigurationStrategyTest.xml";

    /** Namespace used in tests. */
    private static final String NAMESPACE = "ConfigurationManagerConfigurationStrategyTest.failure";

    /**
     * Tears down method. Clear configuration.
     * 
     * @throws Exception if errors occur
     * 
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        FailureTestHelper.clearConfig();
    }

    /**
     * Test method for {@link
     * ConfigurationManagerConfigurationStrategy#ConfigurationManagerConfigurationStrategy(String)}. Failure case 1,
     * call with null.
     * 
     * @throws Exception to junit
     */
    public void testConfigurationManagerConfigurationStrategy_namespaceNull() throws Exception {
        try {
            new ConfigurationManagerConfigurationStrategy(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link
     * ConfigurationManagerConfigurationStrategy#ConfigurationManagerConfigurationStrategy(String)}. Failure case 2,
     * call with empty string.
     * 
     * @throws Exception to junit
     */
    public void testConfigurationManagerConfigurationStrategyFailure_namespaceEmpty() throws Exception {
        try {
            new ConfigurationManagerConfigurationStrategy(" ");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#getProperty(String)}. Failure case 1, call with
     * null name.
     * 
     * @throws Exception if error occurs
     */
    public void testGetPropertyFailure_nameNull() throws Exception {
        FailureTestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);

        try {
            strategy.getProperty(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#getProperty(String)}. Failure case 1, call with
     * empty name.
     * 
     * @throws Exception if error occurs
     */
    public void testGetPropertyFailure_nameEmtpy() throws Exception {
        FailureTestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);

        try {
            strategy.getProperty(" ");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#getProperty(String)}. Failure case 2, call
     * without loading configuration file (unknown namespace).
     * 
     * @throws Exception if error occurs
     */
    public void testGetPropertyFailure_unknownNamespace() throws Exception {
        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy("unknown namespace");

        try {
            strategy.getProperty("property1");
            fail("ConfigurationException expected");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#setProperty(String, String)}. Failure case,
     * call with null name.
     * 
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure_nameNull() throws Exception {
        FailureTestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);

        try {
            strategy.setProperty(null, FailureTestHelper.generateString());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#setProperty(String, String)}. Failure case,
     * call with null name.
     * 
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure_nameEmpty() throws Exception {
        FailureTestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);

        try {
            strategy.setProperty(" ", FailureTestHelper.generateString());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#setProperty(String, String)}. Failure case ,
     * call with null value.
     * 
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure_valueNull() throws Exception {
        FailureTestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);

        try {
            strategy.setProperty(FailureTestHelper.generateString(), null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    // /**
    // * Test method for {@link ConfigurationManagerConfigurationStrategy#setProperty(String, String)}. Failure
    // * case , call with empty value.
    // *
    // * @throws Exception if error occurs
    // */
    // public void testSetPropertyFailure_valueEmpty() throws Exception {
    // FailureTestHelper.loadConfig(CONFIG_FILE);
    //
    // ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);
    //
    // try {
    // strategy.setProperty(FailureTestHelper.generateString(), " ");
    // fail("IllegalArgumentException expected");
    // } catch (IllegalArgumentException e) {
    // //pass
    // }
    // }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#setProperty(String, String)}. Failure case 3,
     * call without loading configuration file (unknown namespace).
     * 
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure3() throws Exception {
        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy("unknown namespace");

        try {
            strategy.setProperty(FailureTestHelper.generateString(), FailureTestHelper.generateString());
            fail("ConfigurationException expected");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test method for {@link ConfigurationManagerConfigurationStrategy#commitChanges()}. Failure case, lock the
     * namespace before calling.
     * 
     * @throws Exception if error occurs
     */
    public void testCommitChangesFailure() throws Exception {
        FailureTestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy strategy = new ConfigurationManagerConfigurationStrategy(NAMESPACE);
        String newValue = FailureTestHelper.generateString();
        strategy.setProperty("property4", newValue);

        ConfigManager.getInstance().lock(NAMESPACE, "test");

        try {
            strategy.commitChanges();
            fail("ConfigurationException expected");
        } catch (ConfigurationException e) {
            // pass
        } finally {
            ConfigManager.getInstance().forceUnlock(NAMESPACE);
        }
    }
}
