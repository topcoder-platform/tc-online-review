/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config.accuracytests;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * Accuracy Unit test cases for ConfigManager.
 * </p>
 *
 * @author victorsam
 * @version 2.2
 */
public class ConfigManagerAccuracyTests extends TestCase {
    /**
     * <p>ConfigManager instance for testing.</p>
     */
    private ConfigManager instance;

    /**
     * <p>Setup test environment.</p>
     *
     */
    protected void setUp() {
        instance = ConfigManager.getInstance();
    }

    /**
     * <p>Tears down test environment.</p>
     * @throws Exception to JUnit
     *
     */
    protected void tearDown() throws Exception {
        instance = null;
    }

    /**
     * <p>Return all tests.</p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(ConfigManagerAccuracyTests.class);
    }

    /**
     * <p>Tests ctor ConfigManager#ConfigManager() for accuracy.</p>
     */
    public void testCtor1() {
        assertNotNull("Failed to create ConfigManager instance.", instance);
    }

    /**
     * <p>Tests ConfigManager#getInstance() for accuracy.</p>
     */
    public void testGetInstance() {
        assertNotNull("Failed to get instance correctly.", instance.isConfigRefreshableByDefault());
        assertEquals("Failed to get instance correctly.", "com.topcoder.util.config.DefaultConfigManager",
            instance.getClass().getName());
    }

    /**
     * <p>Tests ConfigManager#isConfigRefreshableByDefault() for accuracy.</p>
     */
    public void testIsConfigRefreshableByDefault() {
        instance.setConfigRefreshableByDefault(false);
        assertFalse("Failed to get the refreshable by default correctly.", instance.isConfigRefreshableByDefault());
    }

    /**
     * <p>Tests ConfigManager#setConfigRefreshableByDefault(boolean) for accuracy.</p>
     */
    public void testSetConfigRefreshableByDefault() {
        instance.setConfigRefreshableByDefault(true);
        assertTrue("Failed to set the refreshable by default correctly.", instance.isConfigRefreshableByDefault());
    }

}