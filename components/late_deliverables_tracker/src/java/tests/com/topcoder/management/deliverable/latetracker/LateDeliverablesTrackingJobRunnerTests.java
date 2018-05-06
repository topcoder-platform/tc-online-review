/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import com.topcoder.configuration.ConfigurationObject;

import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.ScheduledJobRunner;

/**
 * Unit tests for <code>{@link LateDeliverablesTrackingJobRunner}</code> class.
 *
 * @author myxgyy, sparemax
 * @version 1.1
 */
public class LateDeliverablesTrackingJobRunnerTests extends BaseTestCase {
    /**
     * The <code>{@link LateDeliverablesTrackingJobRunner}</code> instance used for
     * testing.
     */
    private LateDeliverablesTrackingJobRunner target;

    /**
     * The <code>ConfigurationObject</code> instance used for testing.
     */
    private ConfigurationObject config;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception
     *             throws exception if any.
     */
    protected void setUp() throws Exception {
        super.setUp();
        config = getConfigurationObject("config/LateDeliverablesTracker.xml",
            LateDeliverablesTracker.class.getName());
        target = new LateDeliverablesTrackingJobRunner();
        target.configure(config);
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception
     *             throws exception if any.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverablesTrackingJobRunner#LateDeliverablesTrackingJobRunner()} method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor() throws Exception {
        target = new LateDeliverablesTrackingJobRunner();
        assertNull("lateDeliverablesTracker field should not be null", getField(target,
            "lateDeliverablesTracker"));
        assertNull("job field should not be null", getField(target, "job"));
        assertEquals("status field should not be null", ScheduledJobRunner.NOT_STARTED, getField(target,
            "status"));
        assertNull("log field should not be null", getField(target, "log"));
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverablesTrackingJobRunner#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * Verifies all class fields have been set by configuration correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_1() throws Exception {
        assertNotNull("lateDeliverablesTracker field should not be null", getField(target,
            "lateDeliverablesTracker"));
        assertNotNull("log field should be null", getField(target, "log"));
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesTrackingJobRunner#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * Verifies all class fields have been set by configuration correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    public void test_Configure_New() throws Exception {
        config.removeProperty("loggerName");
        target.configure(config);

        assertNotNull("lateDeliverablesTracker field should not be null", getField(target,
            "lateDeliverablesTracker"));
        assertNull("log field should be null", getField(target, "log"));
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTrackingJobRunner#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The given config is <code>null</code>, <code>IllegalArgumentException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_2() throws Exception {
        try {
            target.configure(null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTrackingJobRunner#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The loggerName property is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_3() throws Exception {
        config.setPropertyValue("loggerName", "");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTrackingJobRunner#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The loggerName property is not type of String in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_4() throws Exception {
        config.setPropertyValue("loggerName", new Exception());

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesTrackingJobRunner#getJob()}
     * method.
     * </p>
     * <p>
     * Job should be retrieved correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getJob() throws Exception {
        assertNull("job should be retrieved correctly", target.getJob());
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesTrackingJobRunner#setJob(Job)}
     * method.
     * </p>
     * <p>
     * Job should be set correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_setJob_1() throws Exception {
        Job job = new Job("name", JobType.JOB_TYPE_EXTERNAL, "cmd");
        target.setJob(job);
        assertSame("job should be set correctly", job, target.getJob());
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTrackingJobRunner#setJob(Job)}
     * method.
     * </p>
     * <p>
     * The given job is <code>null</code>, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_setJob_2() throws Exception {
        try {
            target.setJob(null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverablesTrackingJobRunner#getMessageData()} method.
     * </p>
     * <p>
     * An empty <code>NodeList</code> instance expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getMessageData() throws Exception {
        assertEquals("empty node list expected", 0, target.getMessageData().getNodes().length);
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverablesTrackingJobRunner#getRunningStatus()} method.
     * </p>
     * <p>
     * Status should be retrieved correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getRunningStatus() throws Exception {
        assertEquals("status should be retrieved correctly", ScheduledJobRunner.NOT_STARTED, target
            .getRunningStatus());
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesTrackingJobRunner#getStatus()}
     * method.
     * </p>
     * <p>
     * Status should be retrieved correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getStatus() throws Exception {
        assertEquals("status should be retrieved correctly", ScheduledJobRunner.NOT_STARTED, target
            .getStatus());
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesTrackingJobRunner#getJobName()}
     * method.
     * </p>
     * <p>
     * Job name should be retrieved correctly. In this case, the job is null.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getJobName_1() throws Exception {
        assertNull("should be null", target.getJobName());
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesTrackingJobRunner#getJobName()}
     * method.
     * </p>
     * <p>
     * Job name should be retrieved correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getJobName_2() throws Exception {
        Job job = new Job("name", JobType.JOB_TYPE_EXTERNAL, "cmd");
        target.setJob(job);
        assertEquals("job name should be retrieved correctly", "name", target.getJobName());
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverablesTrackingJobRunner#setJobName(String)} method.
     * </p>
     * <p>
     * Job name should be set correctly. In this case, the job is null.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_setJobName_1() throws Exception {
        target.setJobName("name");
        assertNull("should be null", target.getJobName());
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverablesTrackingJobRunner#setJobName(String)} method.
     * </p>
     * <p>
     * Job name should be set correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_setJobName_2() throws Exception {
        Job job = new Job("name", JobType.JOB_TYPE_EXTERNAL, "cmd");
        target.setJob(job);
        target.setJobName("another");
        assertEquals("job name should be set correctly", "another", target.getJobName());
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTrackingJobRunner#setJobName(String)} method.
     * </p>
     * <p>
     * The given job name is <code>null</code>, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_setJobName_3() throws Exception {
        try {
            target.setJobName(null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTrackingJobRunner#setJobName(String)} method.
     * </p>
     * <p>
     * The given job name is empty, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_setJobName_4() throws Exception {
        try {
            target.setJobName("");
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesTrackingJobRunner#isDone()}
     * method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_isDone() throws Exception {
        assertFalse("not done", target.isDone());
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesTrackingJobRunner#close()}
     * method.
     * </p>
     * <p>
     * The status should be set to not started.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_close() throws Exception {
        target.close();
        assertEquals("status should be not start", ScheduledJobRunner.NOT_STARTED, target
            .getRunningStatus());
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesTrackingJobRunner#run()} method.
     * </p>
     * <p>
     * The status should be updated to successful.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_run_1() throws Exception {
        target.run();
        assertEquals("status should be successful", ScheduledJobRunner.SUCCESSFUL, target
            .getRunningStatus());
        assertTrue("done", target.isDone());
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesTrackingJobRunner#run()} method.
     * </p>
     * <p>
     * The status should be updated to successful.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_run_New1() throws Exception {
        LateDeliverablesTrackingJobRunner.setConfig(config);
        target = new LateDeliverablesTrackingJobRunner();
        target.run();

        assertEquals("status should be successful", ScheduledJobRunner.SUCCESSFUL, target
            .getRunningStatus());
        assertTrue("done", target.isDone());
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTrackingJobRunner#run()} method.
     * </p>
     * <p>
     * The <code>lateDeliverablesTracker</code> field is <code>null</code>,
     * <code>IllegalStateException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_run_2() throws Exception {
        setField(LateDeliverablesTrackingJobRunner.class, target, "lateDeliverablesTracker", null);
        setField(LateDeliverablesTrackingJobRunner.class, target, "config", null);

        try {
            target.run();
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTrackingJobRunner#run()} method.
     * </p>
     * <p>
     * Error occurred when executing track, status should be updated to failed.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_run_3() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        target.configure(getConfigurationObject("invalid_config/LateDeliverablesTracker.xml",
            LateDeliverablesTracker.class.getName()));

        // error when sending email due to invalid subject
        target.run();
        assertEquals("status should be failed", ScheduledJobRunner.FAILED, target
            .getRunningStatus());
    }
}
