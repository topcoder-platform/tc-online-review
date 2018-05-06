/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.accuracytests;

import java.io.File;
import java.util.Date;

import com.topcoder.naming.jndiutility.ConfigurationStrategy;
import com.topcoder.naming.jndiutility.configstrategy.ConfigurationManagerConfigurationStrategy;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.config.UnknownNamespaceException;

import junit.framework.TestCase;

/**
 * Accuracy test for ConfigurationManagerConfigurationStrategy.
 *
 * @author KKD
 * @version 2.0
 */
public class ConfigurationManagerConfigurationStrategyAccuracyTest extends
        TestCase {
    /**
     * Represents separator of file.
     */
    private static final String FS = File.separator;

    /**
     * Name of configuration file used in accuracy test.
     */
    private static final String CONFIG_FILE = "test_files" + FS
            + "accuracytests" + FS
            + "ConfigurationManagerConfigurationStrategyAccuracyTest.xml";

    /**
     * Namespace used in tests.
     */
    private static final String NAMESPACE = ConfigurationManagerConfigurationStrategyAccuracyTest.class
            .getName();

    /**
     * tear down of the test.
     * @throws Exception exception to JUnit.
     */
    protected void tearDown() throws Exception {
        AccuracyTestHelper.clearConfig();
    }

    /**
     * test the constructor method.
     */
    public void testConstructorAccuracy() {
        assertNotNull("instance should be created.",
                new ConfigurationManagerConfigurationStrategy(NAMESPACE));
    }

    /**
     * test the getProperty method.
     * @throws Exception exception to JUnit.
     */
    public void testgetPropertyAccuracy() throws Exception {
        AccuracyTestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy test = new ConfigurationManagerConfigurationStrategy(
                NAMESPACE);
        assertEquals("invalid property value.", "value 1", test
                .getProperty("property1"));
        assertEquals("invalid property value.", "  ", test
                .getProperty("property2"));
    }

    /**
     * test the setProperty method.
     * @throws Exception exception to JUnit.
     */
    public void testsetPropertyAccuracy() throws Exception {
        AccuracyTestHelper.loadConfig(CONFIG_FILE);
        ConfigManager cm = ConfigManager.getInstance();
        // remove property4 if it's existed
        try {
            cm.createTemporaryProperties(NAMESPACE);
            cm.removeProperty(NAMESPACE, "property4");
            cm.commit(NAMESPACE, "commit");
        } catch (UnknownNamespaceException e) {
            // ignore
        } catch (ConfigManagerException e) {
            //ignore
        }
        ConfigurationStrategy test = new ConfigurationManagerConfigurationStrategy(
                NAMESPACE);
        String newValue = new Date().toString();
        test.setProperty("property1", newValue);

        assertEquals("should not be committed.", "value 1", cm.getProperty(
                NAMESPACE, "property1"));
        assertEquals("should not be committed.", newValue, cm
                .getTemporaryString(NAMESPACE, "property1"));
        assertEquals("temporary property value should be return.", newValue,
                test.getProperty("property1"));

        test.setProperty("property4", newValue);
        assertEquals("should not be committed.", null, cm.getProperty(
                NAMESPACE, "property4"));
        assertEquals("should not be committed.", newValue, cm
                .getTemporaryString(NAMESPACE, "property4"));
        assertEquals("temporary property value should be return.", newValue,
                test.getProperty("property4"));
    }

    /**
     * test the commitChanges method.
     * @throws Exception exception to JUnit.
     */
    public void testcommitChangesAccuracy() throws Exception {
        AccuracyTestHelper.loadConfig(CONFIG_FILE);

        ConfigurationStrategy test = new ConfigurationManagerConfigurationStrategy(
                NAMESPACE);
        String newValue = new Date().toString();
        test.setProperty("property3", newValue);
        test.setProperty("property4", newValue);
        test.commitChanges();

        ConfigManager cm = ConfigManager.getInstance();
        assertEquals("should be committed.", newValue, cm.getProperty(
                NAMESPACE, "property4"));
        assertEquals("invalid property value.", newValue, test
                .getProperty("property4"));
        assertEquals("invalid property value.", newValue, test
                .getProperty("property3"));
    }
}
