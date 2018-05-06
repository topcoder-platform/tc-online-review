/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.accuracytests;

import com.topcoder.management.deliverable.latetracker.notification.NotRespondedLateDeliverablesNotifier;
import com.topcoder.management.deliverable.latetracker.notification.NotRespondedLateDeliverablesNotificationJobRunner;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.ScheduledJobRunner;
import com.topcoder.util.scheduler.scheduling.JobType;

/**
 * Accuracy tests for <code>{@link NotRespondedLateDeliverablesNotificationJobRunner}</code> class.
 *
 * @author KLW
 * @version 1.2
 * @since 1.2
 */
public class NotRespondedLateDeliverablesNotificationJobRunnerAccTests extends AccuracyHelper {
    /**
     * The config file path.
     */
    private static final String CONFIG_FILE_PATH = "accuracy/config/NotRespondedLateDeliverablesNotifier.xml";

    /**
     * The instance for test.
     */
    private NotRespondedLateDeliverablesNotificationJobRunner instance;

    /**
     * The config instance used for test.
     */
    private ConfigurationObject config;

    /**
     * Set up the environment.
     *
     * @throws Exception
     *             if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        config = getConfigurationObject(CONFIG_FILE_PATH,NotRespondedLateDeliverablesNotifier.class.getName());
        instance = new NotRespondedLateDeliverablesNotificationJobRunner();
        instance.configure(config);
    }

    /**
     * Tear down the environment.
     *
     * @throws Exception
     *             if any error occurs.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        instance=null;
        NotRespondedLateDeliverablesNotificationJobRunner.setConfig(null);
    }

    /**
     * The accuracy test method  for
     * {@link NotRespondedLateDeliverablesNotificationJobRunner#NotRespondedLateDeliverablesNotificationJobRunner()}
     * constructor.
     *
     * @throws Exception
     *             if any error occurs.
     */
    public void test_NotRespondedLateDeliverablesNotificationJobRunner() throws Exception {
        instance = new NotRespondedLateDeliverablesNotificationJobRunner();
        assertNotNull("the instance should be created", instance);
    }

    /**
     * The accuracy test method for
     * {@link NotRespondedLateDeliverablesNotificationJobRunner#configure(ConfigurationObject)} method.
     * 
     * @throws Exception
     *             if any error occurs.
     */
    public void test_Configure() throws Exception {
        assertNotNull("the log should be created.", getField(instance, "log"));
        
        //the log is null
        config.removeProperty("loggerName");
        instance.configure(config);
        assertNull("The log should be null", getField(instance, "log"));
    }
 
    /**
     * The accuracy test method  for {@link NotRespondedLateDeliverablesNotificationJobRunner#getJob()} method.
     */
    public void test_getJob() {
        assertNull("The job should be null.", getField(instance, "job"));
        
        Job job = new Job("name", JobType.JOB_TYPE_EXTERNAL, "cmd");
        instance.setJob(job);
        assertSame("the job is incorrect.", job, getField(instance, "job"));
    }

    /**
     * The accuracy test method  for {@link NotRespondedLateDeliverablesNotificationJobRunner#setJob(Job)} method.
     */
    public void test_setJob() {
        Job job = new Job("name", JobType.JOB_TYPE_EXTERNAL, "cmd");
        instance.setJob(job);

        assertSame("'setJob' should be correct.", job, getField(instance, "job"));
    }
 
    /**
     * The accuracy test method  for {@link NotRespondedLateDeliverablesNotificationJobRunner#getMessageData()} method.
     */
    public void test_getMessageData(){
        assertEquals("An empty Nodelist should be return.", 0, instance.getMessageData().getNodes().length);
    }

    /**
     * The accuracy test method  for {@link NotRespondedLateDeliverablesNotificationJobRunner#getRunningStatus()} method.
     *
     */
    public void test_getRunningStatus(){
        assertEquals("The status should be not started.",ScheduledJobRunner.NOT_STARTED, instance.getRunningStatus());
    }

    /**
     * The accuracy test method  for {@link NotRespondedLateDeliverablesNotificationJobRunner#getStatus()} method.
     */
    public void test_getStatus() {
        assertEquals("'getStatus' should be correct.", ScheduledJobRunner.NOT_STARTED, instance.getStatus());
        
        instance.run();
        assertEquals("'run' should be correct.", ScheduledJobRunner.SUCCESSFUL, instance.getRunningStatus());
    }

    /**
     * The accuracy test method  for {@link NotRespondedLateDeliverablesNotificationJobRunner#run()} method.
     * The status should be updated to successful.
     */
    public void test_run() {
        instance.run();
        assertTrue("done", instance.isDone());
        assertEquals("'run' should be correct.", ScheduledJobRunner.SUCCESSFUL, instance.getRunningStatus());
    }

    /**
     * The accuracy test method  for {@link NotRespondedLateDeliverablesNotificationJobRunner#isDone()} method.
     *
     * @throws Exception
     *             if any error occurs.
     */
    public void test_isDone() {
        instance.run();
        assertTrue("the resule should be true.", instance.isDone());
        //close
        instance.close();
        assertFalse("the result should be false.", instance.isDone());
    }

    /**
     * The accuracy test method  for {@link NotRespondedLateDeliverablesNotificationJobRunner#close()} method.
     * </p>
     * <p>
     * The status should be set to not started.
     * </p>
     *
     * @throws Exception
     *             if any error occurs.
     */
    public void test_close() {
        instance.run();
        instance.close();
        assertEquals("'close' should be correct.", ScheduledJobRunner.NOT_STARTED, instance.getRunningStatus());
    }


    /**
     * <p>
     * The accuracy test method  for {@link NotRespondedLateDeliverablesNotificationJobRunner#getJobName()} method.
     * </p>
     *
     * @throws Exception
     *             if any error occurs.
     */
    public void test_getJobName() {
        assertNull("jobName' should be null.", instance.getJobName());
        
        Job job = new Job("name", JobType.JOB_TYPE_EXTERNAL, "cmd");
        instance.setJob(job);

        assertEquals("'getJobName' should be correct.", "name", instance.getJobName());
    }

    /**
     * The accuracy test method  for {@link NotRespondedLateDeliverablesNotificationJobRunner#setJobName(String)} method.
     *
     * @throws Exception
     *             if any error occurs.
     */
    public void test_setJobName() {
        instance.setJobName("name");
        assertNull("jobName should be null.", instance.getJobName());
        //set the job and the job name
        Job job = new Job("name", JobType.JOB_TYPE_EXTERNAL, "cmd");
        instance.setJob(job);
        instance.setJobName("test");
        assertEquals("jobname is incorrect.", "test", instance.getJobName());
    }

     
    /**
     * The accuracy test method  for
     * {@link NotRespondedLateDeliverablesNotificationJobRunner#setConfig(ConfigurationObject)} method.
     *
     */
    public void test_setConfig() {
        NotRespondedLateDeliverablesNotificationJobRunner.setConfig(config);
        assertSame("The config is incorrect.", config, getField(instance, "config"));
    }
}
