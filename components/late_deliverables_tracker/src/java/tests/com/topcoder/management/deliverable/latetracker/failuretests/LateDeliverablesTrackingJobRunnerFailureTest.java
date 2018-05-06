/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.failuretests;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTracker;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackerConfigurationException;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackingJobRunner;

/**
 * Failure test for LateDeliverablesTrackingJobRunner class.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class LateDeliverablesTrackingJobRunnerFailureTest extends TestCase {

    /**
     * Represents the instance of LateDeliverablesTrackingJobRunner used in test.
     */
    private LateDeliverablesTrackingJobRunner instance;

    /**
     * The ConfigurationObject used to initializing LateDeliverablesTracker.
     */
    private ConfigurationObject config;

    /**
     * Set up for each test.
     *
     * @throws Exception
     *             to jUnit.
     */
    protected void setUp() throws Exception {
        TestHelper.addConfig();
        TestHelper.executeSqlFile("test_files/failure/insert.sql");
        config = TestHelper.getConfigurationObject("failure/LateDeliverablesTracker.xml", LateDeliverablesTracker.class
            .getName());
        instance = new LateDeliverablesTrackingJobRunner();
        instance.configure(config);
    }

    /**
     * Tear down for each test case.
     *
     * @throws Exception
     *             to jUnit.
     */
    protected void tearDown() throws Exception {
        TestHelper.cleanTables();
        TestHelper.clearNamespace();
    }

    /**
     * Failure test configure(ConfigurationObject config). When config is null.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigure_ConfigIsNull() throws Exception {
        try {
            instance.configure(null);
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    /**
     * Failure test configure(ConfigurationObject config). When loggerName is empty.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigure_LoggerNameIsEmpty() throws Exception {
        config.setPropertyValue("loggerName", "");

        try {
            instance.configure(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }

    /**
     * Failure test configure(ConfigurationObject config). When loggerName is empty.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigure_LoggerNameIsInvalidType() throws Exception {
        config.setPropertyValue("loggerName", 1);

        try {
            instance.configure(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }

    /**
     * Test setJob(Job job). When job is null.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetJob_JobIsNull() throws Exception {
        try {
            instance.setJob(null);
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    /**
     * Failure test for run(). When not configured.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testRun_NoConfigured() throws Exception {
        try {
            new LateDeliverablesTrackingJobRunner().run();
            fail("Cannot go here.");
        } catch (IllegalStateException e) {
            // OK
        }
    }

    /**
     * Failure test for setJobName(String jobName). When jobName is null.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetJobName_JobNameIsNull() throws Exception {
        try {
            instance.setJobName(null);
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    /**
     * Failure test for setJobName(String jobName). When jobName is empty.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetJobName_JobNameIsEmpty() throws Exception {
        try {
            instance.setJobName(" ");
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }
}
