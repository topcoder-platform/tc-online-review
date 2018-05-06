/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.notification;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.latetracker.BaseTestCase;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackerConfigurationException;
import com.topcoder.management.deliverable.latetracker.MockLateDeliverableManager;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.ScheduledJobRunner;

/**
 * Unit tests for <code>{@link NotRespondedLateDeliverablesNotificationJobRunner}</code> class.
 *
 * @author sparemax
 * @version 1.2
 * @since 1.2
 */
public class NotRespondedLateDeliverablesNotificationJobRunnerTests extends BaseTestCase {
    /**
     * The <code>{@link NotRespondedLateDeliverablesNotificationJobRunner}</code> instance used for testing.
     */
    private NotRespondedLateDeliverablesNotificationJobRunner target;

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
        NotRespondedLateDeliverablesNotificationJobRunner.setConfig(null);

        config = getConfigurationObject("config/NotRespondedLateDeliverablesNotifier.xml",
            NotRespondedLateDeliverablesNotifier.class.getName());

        target = new NotRespondedLateDeliverablesNotificationJobRunner();
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

        NotRespondedLateDeliverablesNotificationJobRunner.setConfig(null);
    }

    /**
     * <p>
     * Accuracy test case for the
     * {@link NotRespondedLateDeliverablesNotificationJobRunner#NotRespondedLateDeliverablesNotificationJobRunner()}
     * constructor.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor() throws Exception {
        target = new NotRespondedLateDeliverablesNotificationJobRunner();

        assertNull("'notRespondedLateDeliverableNotifier' should not correct.", getField(target,
            "notRespondedLateDeliverableNotifier"));
        assertNull("'job' should not correct.", getField(target, "job"));
        assertEquals("'status' should not correct.", ScheduledJobRunner.NOT_STARTED, getField(target, "status"));
        assertNull("'log' should not correct.", getField(target, "log"));
    }

    /**
     * <p>
     * Accuracy test case for the
     * {@link NotRespondedLateDeliverablesNotificationJobRunner#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * Verifies all class fields have been set by configuration correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_1() throws Exception {
        assertNotNull("'configure' should be correct.", getField(target, "log"));
        assertNotNull("'configure' should be correct.", getField(target, "notRespondedLateDeliverableNotifier"));
    }

    /**
     * <p>
     * Accuracy test case for the
     * {@link NotRespondedLateDeliverablesNotificationJobRunner#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * Verifies all class fields have been set by configuration correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_2() throws Exception {
        config.removeProperty("loggerName");
        target.configure(config);

        assertNull("'configure' should be correct.", getField(target, "log"));
        assertNotNull("'configure' should be correct.", getField(target, "notRespondedLateDeliverableNotifier"));
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotificationJobRunner#configure(ConfigurationObject)} method with config is
     * <code>null</code>.
     * </p>
     *
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_configNull() throws Exception {
        try {
            target.configure(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotificationJobRunner#configure(ConfigurationObject)} method with
     * 'loggerName' is empty.
     * </p>
     *
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_loggerNameEmpty() throws Exception {
        config.setPropertyValue("loggerName", " \t ");

        try {
            target.configure(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotificationJobRunner#configure(ConfigurationObject)} method with
     * 'loggerName' is not a string.
     * </p>
     *
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_loggerNameNotString() throws Exception {
        config.setPropertyValue("loggerName", 1);

        try {
            target.configure(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#getJob()} method.
     * </p>
     * <p>
     * The value should be retrieved correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getJob() throws Exception {
        Job job = new Job("name", JobType.JOB_TYPE_EXTERNAL, "cmd");
        target.setJob(job);

        assertSame("'getJob' should be correct.", job, target.getJob());
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#setJob(Job)} method.
     * </p>
     * <p>
     * The value should be set correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_setJob() throws Exception {
        Job job = new Job("name", JobType.JOB_TYPE_EXTERNAL, "cmd");
        target.setJob(job);

        assertSame("'setJob' should be correct.", job, getField(target, "job"));
    }

    /**
     * <p>
     * Failure test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#setJob(Job)} method with job
     * is <code>null</code>.
     * </p>
     *
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_setJob_jobNull() throws Exception {
        try {
            target.setJob(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#getMessageData()} method.
     * </p>
     * <p>
     * An empty <code>NodeList</code> instance expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getMessageData() throws Exception {
        assertEquals("'getMessageData' should be correct.", 0, target.getMessageData().getNodes().length);
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#getRunningStatus()} method.
     * </p>
     * <p>
     * Status should be retrieved correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getRunningStatus() throws Exception {
        assertEquals("'getRunningStatus' should be correct.",
            ScheduledJobRunner.NOT_STARTED, target.getRunningStatus());
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#getStatus()} method.
     * </p>
     * <p>
     * Status should be retrieved correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getStatus() throws Exception {
        assertEquals("'getStatus' should be correct.",
            ScheduledJobRunner.NOT_STARTED, target.getStatus());
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#run()} method.
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

        assertEquals("'run' should be correct.", ScheduledJobRunner.SUCCESSFUL, target.getRunningStatus());
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#run()} method.
     * </p>
     * <p>
     * The status should be updated to successful.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_run_2() throws Exception {
        NotRespondedLateDeliverablesNotificationJobRunner.setConfig(config);
        target = new NotRespondedLateDeliverablesNotificationJobRunner();
        target.run();

        assertEquals("'run' should be correct.", ScheduledJobRunner.SUCCESSFUL, target.getRunningStatus());
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#run()} method.
     * </p>
     * <p>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_run_3() throws Exception {
        setField(NotRespondedLateDeliverablesNotificationJobRunner.class, target, "status",
            ScheduledJobRunner.RUNNING);
        target.run();

        assertEquals("'run' should be correct.", ScheduledJobRunner.RUNNING, target.getRunningStatus());
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#run()} method.
     * </p>
     * <p>
     * Error occurred when executing, status should be updated to failed.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_run_Failed1() throws Exception {
        config.getChild("objectFactoryConfig").getChild("lateDeliverableManager").setPropertyValue("type",
            MockLateDeliverableManager.class.getName());
        target.configure(config);

        // error when sending email due to invalid subject
        target.run();
        assertEquals("'run' should be correct.", ScheduledJobRunner.FAILED, target.getRunningStatus());
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#run()} method.
     * </p>
     * <p>
     * Error occurred when executing, status should be updated to failed.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_run_Failed2() throws Exception {
        config.getChild("objectFactoryConfig").getChild("lateDeliverableManager").setPropertyValue("type",
            String.class.getName());
        NotRespondedLateDeliverablesNotificationJobRunner.setConfig(config);

        target = new NotRespondedLateDeliverablesNotificationJobRunner();

        // error when sending email due to invalid subject
        target.run();
        assertEquals("'run' should be correct.", ScheduledJobRunner.FAILED, target.getRunningStatus());
    }

    /**
     * <p>
     * Failure test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#run()} method with
     * notRespondedLateDeliverableNotifier is <code>null</code>.
     * </p>
     *
     * <p>
     * <code>IllegalStateException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_run_notRespondedLateDeliverableNotifierNull() throws Exception {
        setField(NotRespondedLateDeliverablesNotificationJobRunner.class, target,
            "notRespondedLateDeliverableNotifier", null);

        try {
            target.run();
            fail("IllegalStateException is expected.");
        } catch (IllegalStateException e) {
            // Good
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#isDone()} method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_isDone_1() throws Exception {
        target.run();

        assertTrue("'isDone' should be correct.", target.isDone());
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#isDone()} method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_isDone_2() throws Exception {
        target.run();
        target.close();

        assertFalse("'isDone' should be correct.", target.isDone());
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#close()} method.
     * </p>
     * <p>
     * The status should be set to not started.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_close() throws Exception {
        target.run();
        target.close();

        assertEquals("'close' should be correct.", ScheduledJobRunner.NOT_STARTED, target.getRunningStatus());
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#getJobName()} method.
     * </p>
     * <p>
     * Job name should be retrieved correctly. In this case, the job is null.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getJobName_1() throws Exception {
        assertNull("'getJobName' should be correct.", target.getJobName());
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#getJobName()} method.
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

        assertEquals("'getJobName' should be correct.", "name", target.getJobName());
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#setJobName(String)} method.
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

        assertNull("'setJobName' should be correct.", target.getJobName());
    }

    /**
     * <p>
     * Accuracy test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#setJobName(String)} method.
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

        assertEquals("'setJobName' should be correct.", "another", target.getJobName());
    }

    /**
     * <p>
     * Failure test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#setJobName(String)} method
     * with jobName is <code>null</code>.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_setJobName_jobNameNull() throws Exception {
        try {
            target.setJobName(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the {@link NotRespondedLateDeliverablesNotificationJobRunner#setJobName(String)} method
     * with jobName is empty.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_setJobName_jobNameEmpty() throws Exception {
        try {
            target.setJobName(" \t ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * <p>
     * Accuracy test case for the
     * {@link NotRespondedLateDeliverablesNotificationJobRunner#setConfig(ConfigurationObject)} method.
     * </p>
     * <p>
     * The value should be set correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_setConfig() throws Exception {
        NotRespondedLateDeliverablesNotificationJobRunner.setConfig(config);

        assertSame("'setConfig' should be correct.", config, getField(target, "config"));
    }
}
