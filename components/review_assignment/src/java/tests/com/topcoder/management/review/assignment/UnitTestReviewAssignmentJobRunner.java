/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.ScheduledJobRunner;

/**
 * Testcases for ReviewAssignmentJobRunner class.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UnitTestReviewAssignmentJobRunner extends BaseTestCase {
    /**
     * The <code>{@link ReviewAssignmentJobRunner}</code> instance used for testing.
     */
    private ReviewAssignmentJobRunner target;

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

        config = getConfigurationObject("test_files/config/config.properties", "assignment",
            ReviewAssignmentManager.class.getName());

        target = new ReviewAssignmentJobRunner();
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
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#ReviewAssignmentJobRunner()} method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor() throws Exception {
        target = new ReviewAssignmentJobRunner();
        assertNull("reviewAssignmentManager field should not be null",
            getField(target, "reviewAssignmentManager"));
        assertNull("job field should not be null", getField(target, "job"));
        assertEquals("status field should not be null", ScheduledJobRunner.NOT_STARTED,
            getField(target, "status"));
        assertNull("log field should not be null", getField(target, "log"));
    }

    /**
     * <p>
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * Verifies all class fields have been set by configuration correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_1() throws Exception {
        assertNotNull("reviewAssignmentManager field should not be null",
            getField(target, "reviewAssignmentManager"));
        assertNotNull("log field should be null", getField(target, "log"));
    }

    /**
     * <p>
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * Verifies all class fields have been set by configuration correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_New() throws Exception {
        config.removeProperty("loggerName");
        target.configure(config);

        assertNotNull("reviewAssignmentManager field should not be null",
            getField(target, "reviewAssignmentManager"));
        assertNull("log field should be null", getField(target, "log"));
    }

    /**
     * <p>
     * Failure test case for the {@link ReviewAssignmentJobRunner#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The given config is <code>null</code>, <code>IllegalArgumentException</code> expected.
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
     * Failure test case for the {@link ReviewAssignmentJobRunner#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The loggerName property is empty in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_3() throws Exception {
        config.setPropertyValue("loggerName", "");

        try {
            target.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link ReviewAssignmentJobRunner#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The loggerName property is not type of String in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_4() throws Exception {
        config.setPropertyValue("loggerName", new Exception());

        try {
            target.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#getJob()} method.
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
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#setJob(Job)} method.
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
     * Failure test case for the {@link ReviewAssignmentJobRunner#setJob(Job)} method.
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
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#getMessageData()} method.
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
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#getRunningStatus()} method.
     * </p>
     * <p>
     * Status should be retrieved correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getRunningStatus() throws Exception {
        assertEquals("status should be retrieved correctly", ScheduledJobRunner.NOT_STARTED,
            target.getRunningStatus());
    }

    /**
     * <p>
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#getStatus()} method.
     * </p>
     * <p>
     * Status should be retrieved correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getStatus() throws Exception {
        assertEquals("status should be retrieved correctly", ScheduledJobRunner.NOT_STARTED,
            target.getStatus());
    }

    /**
     * <p>
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#getJobName()} method.
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
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#getJobName()} method.
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
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#setJobName(String)} method.
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
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#setJobName(String)} method.
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
     * Failure test case for the {@link ReviewAssignmentJobRunner#setJobName(String)} method.
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
     * Failure test case for the {@link ReviewAssignmentJobRunner#setJobName(String)} method.
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
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#isDone()} method.
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
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#close()} method.
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
        assertEquals("status should be not start", ScheduledJobRunner.NOT_STARTED, target.getRunningStatus());
    }

    /**
     * <p>
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#run()} method.
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
        assertEquals("status should be successful", ScheduledJobRunner.SUCCESSFUL, target.getRunningStatus());
        assertTrue("done", target.isDone());
    }

    /**
     * <p>
     * Accuracy test case for the {@link ReviewAssignmentJobRunner#run()} method.
     * </p>
     * <p>
     * The status should be updated to successful.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_run_New1() throws Exception {
        ReviewAssignmentJobRunner.setConfig(config);
        target = new ReviewAssignmentJobRunner();
        target.run();

        assertEquals("status should be successful", ScheduledJobRunner.SUCCESSFUL, target.getRunningStatus());
        assertTrue("done", target.isDone());
    }

    /**
     * <p>
     * Failure test case for the {@link ReviewAssignmentJobRunner#run()} method.
     * </p>
     * <p>
     * The <code>reviewAssignmentManager</code> field is <code>null</code>, <code>IllegalStateException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_run_2() throws Exception {
        setField(ReviewAssignmentJobRunner.class, target, "reviewAssignmentManager", null);
        setField(ReviewAssignmentJobRunner.class, target, "config", null);

        try {
            target.run();
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }
}
