/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.failuretests;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

import java.util.Iterator;


/**
 * Helper class for failure tests. Contains methods to load and unload configurations.
 *
 * @author skatou
 * @version 1.0
 */
public class FailureTestsHelper extends TestCase {
    /** File that contains configurations for basic logger. */
    protected static final String BASIC_LOGGER_CONFIG = "failuretests/BasicLogger.xml";

    /** File that contains configurations for AutoPilot component. */
    protected static final String CONFIG = "failuretests/AutoPilot.xml";

    /**
     * Sets up the test environment. Unloads all configuration namespaces.
     *
     * @throws Exception pass to JUnit.
     */
    protected void setUp() throws Exception {
        unloadConfig();
    }

    /**
     * Cleans up the test environment. Unloads all configuration namespaces.
     *
     * @throws Exception pass to JUnit.
     */
    protected void tearDown() throws Exception {
        unloadConfig();
    }

    /**
     * Loads necessary configurations into ConfigManager.
     *
     * @throws Exception pass to JUnit.
     */
    protected void loadConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(CONFIG);
        cm.add(BASIC_LOGGER_CONFIG);
    }

    /**
     * Unloads all configurations. All namespaces in ConfigManager are removed.
     *
     * @throws Exception pass to JUnit.
     */
    protected void unloadConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator it = cm.getAllNamespaces(); it.hasNext();) {
            cm.removeNamespace(it.next().toString());
        }
    }
}
