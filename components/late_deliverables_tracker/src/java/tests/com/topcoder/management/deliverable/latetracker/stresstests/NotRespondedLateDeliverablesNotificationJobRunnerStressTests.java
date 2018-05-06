/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.deliverable.latetracker.stresstests;

import java.util.Date;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.management.deliverable.latetracker.notification.NotRespondedLateDeliverablesNotificationJobRunner;
import com.topcoder.management.deliverable.latetracker.notification.NotRespondedLateDeliverablesNotifier;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.scheduler.scheduling.ScheduledJobRunner;

/**
 * <p>
 * Stress test case of the LateDeliverablesTrackingJobRunner.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class NotRespondedLateDeliverablesNotificationJobRunnerStressTests extends BaseStressTest {
    /**
     * Constant for database connection factory configuration file.
     */
    private static final String DB_FACTORY_CONFIG_FILE = "stress/DB_Factory.xml";

    /**
     * Array of all the stress file names for various dependency components.
     */
    private static final String[] COMPONENT_FILE_NAMES = new String[] {
            "stress/Project_Management.xml", "stress/Phase_Management.xml",
            "stress/Upload_Resource_Search.xml", "stress/InformixPhasePersistence.xml",
            "stress/SearchBuilderCommon.xml", "stress/SearchBundleManager.xml",
            "stress/LateDeliverableManagerImpl.xml" };

    /**
     * The NotRespondedLateDeliverablesNotificationJobRunner instance used to run as a job.
     */
    private NotRespondedLateDeliverablesNotificationJobRunner target;

    /**
     * The ConfigurationObject instance used to run as a job.
     */
    private ConfigurationObject config;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception
     *             to jUnit.
     */
    public void setUp() throws Exception {
        super.setUp();

        addConfig();

        NotRespondedLateDeliverablesNotificationJobRunner.setConfig(null);

        config = getConfigurationObject("stress/NotRespondedLateDeliverablesNotifier.xml",
                NotRespondedLateDeliverablesNotifier.class.getName());

        target = new NotRespondedLateDeliverablesNotificationJobRunner();
        target.configure(config);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception
     *             to jUnit.
     */
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(
                NotRespondedLateDeliverablesNotificationJobRunnerStressTests.class);

        return suite;
    }

    /**
     * <p>
     * Stress test for method <code>run()</code>.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_run() throws Exception {
        try {
            for (int i = 0; i < 100; i++) {
                target.run();
                assertEquals(ScheduledJobRunner.SUCCESSFUL, target.getRunningStatus());
            }
        } finally {
            System.out.println("Run test: test_execute for 100 times takes "
                    + (new Date().getTime() - start) + "ms");
        }
    }

    /**
     * Clears all namespace under config manager.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void clearNamespace() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();

        Iterator<?> iter = configManager.getAllNamespaces();
        while (iter.hasNext()) {
            String ns = (String) iter.next();
            if (!"com.topcoder.util.log".equals(ns)) {
                configManager.removeNamespace(ns);
            }
        }
    }

    /**
     * Adds all required configuration to config manager.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void addConfig() throws Exception {
        clearNamespace();

        ConfigManager configManager = ConfigManager.getInstance();
        configManager.add(DB_FACTORY_CONFIG_FILE);
        for (String config : COMPONENT_FILE_NAMES) {
            configManager.add(config);
        }
    }

    /**
     * Gets the configuration object from the given file with the given namespace.
     *
     * @param file
     *            the configuration file.
     * @param namespace
     *            the namespace.
     * @return the configuration object.
     * @throws Exception
     *             to JUnit.
     */
    public static ConfigurationObject getConfigurationObject(String file, String namespace)
            throws Exception {
        ConfigurationFileManager manager = new ConfigurationFileManager();
        manager.loadFile("root", file);
        ConfigurationObject config = manager.getConfiguration("root");
        return config.getChild(namespace);
    }
}
