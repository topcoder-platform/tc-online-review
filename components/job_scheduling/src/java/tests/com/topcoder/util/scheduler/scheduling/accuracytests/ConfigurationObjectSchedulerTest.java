/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.SynchronizedConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.util.scheduler.scheduling.Day;
import com.topcoder.util.scheduler.scheduling.Dependence;
import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.SchedulingException;
import com.topcoder.util.scheduler.scheduling.persistence.ConfigurationObjectScheduler;

/**
 * <p>
 * Accuracy test for ConfigurationObjectScheduler.
 * </p>
 *
 *
 * @author peony
 * @version 3.1
 */
public class ConfigurationObjectSchedulerTest extends TestCase {
    /**
     * <p>
     * The ConfigurationObjectScheduler instance used for testing.
     * </p>
     */
    private ConfigurationObjectScheduler scheduler;

    /**
     * <p>
     * The ConfigurationObject instance used for testing.
     * </p>
     */
    private ConfigurationObject configuration;

    /**
     * <p>
     * The jobs used for testing.
     * </p>
     */
    private List jobs;

    /**
     * <p>
     * The Job instance used for testing.
     * </p>
     */
    private Job job;

    /**
     * <p>
     * The JobGroup instance used for testing.
     * </p>
     */
    private JobGroup jobGroup;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     *
     */
    protected void setUp() throws Exception {
        configuration = new DefaultConfigurationObject("accuracy");

        job = new Job("accuracyJob", JobType.JOB_TYPE_EXTERNAL, "mkdir");

        Dependence dependence = new Dependence("jobName07", EventHandler.SUCCESSFUL, 1000);
        job.setDependence(dependence);
        job.setRecurrence(8);
        job.setIntervalUnit(new Day());
        job.setIntervalValue(5);

        jobs = new ArrayList();
        jobs.add(job);
        jobGroup = new JobGroup("jobGroup", jobs);

        // set log configuration
        configuration.setPropertyValue("Logger", "logger");
        configuration.addChild(getJobName07());
        configuration.addChild(getNewJobName01());
        configuration.addChild(getDefinedGroups());

        scheduler = new ConfigurationObjectScheduler(configuration);
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     *
     */
    protected void tearDown() throws Exception {
        // empty
    }

    /**
     * <p>
     * Retrieves the ConfigurationObject instance which represents a job named "jobName07". It is mainly from
     * the sample_config_persistence.xml.
     * </p>
     *
     * @return the ConfigurationObject instance
     * @throws Exception to JUnit
     */
    private ConfigurationObject getJobName07() throws Exception {
        ConfigurationObject jobName07 = new DefaultConfigurationObject("jobName07");
        jobName07.setPropertyValue("StartDate", "May 5, 2007 05:00:00 AM");
        jobName07.setPropertyValue("StartTime", "3000000");
        jobName07.setPropertyValue("EndDate", "May 5, 2007 05:00:00 AM");
        jobName07.setPropertyValue("JobType", "JOB_TYPE_EXTERNAL");
        jobName07.setPropertyValue("JobCommand", "dir");
        jobName07.setPropertyValue("Active", "True");
        jobName07.setPropertyValue("Recurrence", "5");
        ConfigurationObject interval = new DefaultConfigurationObject("Interval");
        interval.setPropertyValue("Value", "2");
        ConfigurationObject unit = new DefaultConfigurationObject("Unit");
        unit.setPropertyValue("Type", "com.topcoder.util.scheduler.scheduling.Week");
        interval.addChild(unit);
        jobName07.addChild(interval);
        jobName07.setPropertyValue("ModificationDate", "Jan 5, 2007 05:00:00 AM");

        return jobName07;
    }

    /**
     * <p>
     * Retrieves the ConfigurationObject instance which represents a job named "newJobName01". It is mainly
     * from the sample_config_persistence.xml.
     * </p>
     *
     * @return the ConfigurationObject instance
     * @throws Exception to JUnit
     */
    private ConfigurationObject getNewJobName01() throws Exception {
        ConfigurationObject newJobName01 = new DefaultConfigurationObject("newJobName01");
        newJobName01.setPropertyValue("JobType", "JOB_TYPE_JAVA_CLASS");
        newJobName01.setPropertyValue("JobCommand", "com.topcoder.util.scheduler.scheduling.MyJob");
        newJobName01.setPropertyValue("Active", "True");
        newJobName01.setPropertyValue("ModificationDate", "Jan 5, 2007 05:00:00 AM");
        newJobName01.setPropertyValue("Recurrence", "4");

        ConfigurationObject interval = new DefaultConfigurationObject("Interval");
        interval.setPropertyValue("Value", "2");
        ConfigurationObject unit = new DefaultConfigurationObject("Unit");
        unit.setPropertyValue("Type", "com.topcoder.util.scheduler.scheduling.Week");
        interval.addChild(unit);
        newJobName01.addChild(interval);

        ConfigurationObject dependence = new DefaultConfigurationObject("Dependence");
        ConfigurationObject jobName07 = new DefaultConfigurationObject("jobName07");
        jobName07.setPropertyValue("Status", "SUCCESSFUL");
        jobName07.setPropertyValue("Delay", "10000");
        dependence.addChild(jobName07);
        newJobName01.addChild(dependence);

        ConfigurationObject messages = new DefaultConfigurationObject("Messages");

        ConfigurationObject successful = new DefaultConfigurationObject("SUCCESSFUL");
        successful.setPropertyValue("TemplateText",
            "Hi,\nThis email notifies you that the job %JobName% has the status %JobStatus% now...");
        successful.setPropertyValues("Recipients", new String[] {"rec1@topcoder.com", "rec2@topcoder.com"});
        successful.setPropertyValue("FromAddress", "admin@topcoder.com");
        successful.setPropertyValue("Subject", "Notification");
        messages.addChild(successful);

        ConfigurationObject failed = new DefaultConfigurationObject("FAILED");
        failed.setPropertyValue("TemplateText",
            "Hi,\nThis email notifies you that the job %JobName% has the status %JobStatus% now...");
        failed.setPropertyValues("Recipients", new String[] {"rec1@topcoder.com", "rec2@topcoder.com"});
        failed.setPropertyValue("FromAddress", "admin@topcoder.com");
        failed.setPropertyValue("Subject", "Notification");
        messages.addChild(failed);

        ConfigurationObject notStarted = new DefaultConfigurationObject("NOT_STARTED");
        notStarted.setPropertyValue("TemplateText",
            "Hi,\nThis email notifies you that the job %JobName% has the status %JobStatus% now...");
        notStarted.setPropertyValues("Recipients", new String[] {"rec1@topcoder.com"});
        notStarted.setPropertyValue("FromAddress", "admin@topcoder.com");
        notStarted.setPropertyValue("Subject", "Notification");
        messages.addChild(notStarted);

        newJobName01.addChild(messages);

        return newJobName01;
    }

    /**
     * <p>
     * Retrieves the ConfigurationObject instance which represents a DefinedGroup. It is mainly from the
     * sample_config_persistence.xml.
     * </p>
     *
     * @return the ConfigurationObject instance
     * @throws Exception to JUnit
     */
    private ConfigurationObject getDefinedGroups() throws Exception {
        ConfigurationObject definedGroups = new DefaultConfigurationObject("DefinedGroups");
        ConfigurationObject group1 = new DefaultConfigurationObject("group_1");
        group1.setPropertyValues("Jobs", new String[] {"newJobName01", "jobName07"});

        ConfigurationObject messages = new DefaultConfigurationObject("Messages");

        ConfigurationObject successful = new DefaultConfigurationObject("SUCCESSFUL");
        successful.setPropertyValue("TemplateText",
            "Hi,\nThis email notifies you that the job %JobName% has the status %JobStatus% now...");
        successful.setPropertyValues("Recipients", new String[] {"rec1@topcoder.com", "rec2@topcoder.com"});
        successful.setPropertyValue("FromAddress", "admin@topcoder.com");
        successful.setPropertyValue("Subject", "Notification");
        messages.addChild(successful);

        ConfigurationObject failed = new DefaultConfigurationObject("FAILED");
        failed.setPropertyValue("TemplateText",
            "Hi,\nThis email notifies you that the job %JobName% has the status %JobStatus% now...");
        failed.setPropertyValues("Recipients", new String[] {"rec1@topcoder.com", "rec2@topcoder.com"});
        failed.setPropertyValue("FromAddress", "admin@topcoder.com");
        failed.setPropertyValue("Subject", "Notification");
        messages.addChild(failed);

        ConfigurationObject notStarted = new DefaultConfigurationObject("NOT_STARTED");
        notStarted.setPropertyValue("TemplateText",
            "Hi,\nThis email notifies you that the job %JobName% has the status %JobStatus% now...");
        notStarted.setPropertyValues("Recipients", new String[] {"rec1@topcoder.com"});
        notStarted.setPropertyValue("FromAddress", "admin@topcoder.com");
        notStarted.setPropertyValue("Subject", "Notification");
        messages.addChild(notStarted);

        group1.addChild(messages);
        definedGroups.addChild(group1);

        return definedGroups;
    }

    /**
     * <p>
     * Accuracy tests for Constructor.
     * </p>
     *
     *
     * @throws Exception to JUnit
     */
    public void testCtorA() throws Exception {
        assertNotNull("Failed to create a new ConfigurationObjectScheduler instance.", scheduler);
    }

    /**
     * <p>
     * Accuracy tests for Constructor. null loggerName is accepted.
     * </p>
     *
     *
     * @throws Exception to JUnit
     */
    public void testCtorB() throws Exception {
        configuration.setPropertyValue("Logger", null);
        scheduler = new ConfigurationObjectScheduler(configuration);
        assertNotNull("Failed to create a new ConfigurationObjectScheduler instance.", scheduler);
    }

    /**
     * <p>
     * Accuracy tests for Constructor. The parameter is a SynchronizedConfigurationObject instance.
     * </p>
     *
     *
     * @throws Exception to JUnit
     */
    public void testCtorC() throws Exception {
        scheduler = new ConfigurationObjectScheduler(new SynchronizedConfigurationObject(configuration));
        assertNotNull("Failed to create a new ConfigurationObjectScheduler instance.", scheduler);
    }

    /**
     * <p>
     * Accuracy tests for <code>addJob(Job)</code>.
     * </p>
     *
     *
     * @throws SchedulingException to JUnit
     */
    public void testAddJob() throws SchedulingException {
        scheduler.addJob(job);

        Job newJob = scheduler.getJob("accuracyJob");
        assertEquals("Failed to add the job correctly.", 8, newJob.getRecurrence());
        assertEquals("Failed to add the job correctly.", 5, newJob.getIntervalValue());
    }

    /**
     * <p>
     * Accuracy tests for <code>updateJob(Job)</code>.
     * </p>
     *
     *
     * @throws SchedulingException to JUnit
     */
    public void testUpdateJob() throws SchedulingException {
        scheduler.addJob(job);
        job.setIntervalValue(10);
        job.setRecurrence(20);

        scheduler.updateJob(job);

        Job newJob = scheduler.getJob("accuracyJob");
        assertEquals("Failed to update the job correctly.", job.getActive(), newJob.getActive());
        assertEquals("Failed to update the job correctly.", job.getExecutionCount(), newJob
            .getExecutionCount());
        assertEquals("Failed to update the job correctly.", job.getJobType(), newJob.getJobType());
        assertEquals("Failed to update the job correctly.", job.getName(), newJob.getName());
        assertEquals("Failed to update the job correctly.", job.getRunCommand(), newJob.getRunCommand());
        assertEquals("Failed to update the job correctly.", 20, newJob.getRecurrence());
        assertEquals("Failed to update the job correctly.", 10, newJob.getIntervalValue());
    }

    /**
     * <p>
     * Accuracy tests for <code>deleteJob(Job)</code>.
     * </p>
     *
     *
     * @throws SchedulingException to JUnit
     */
    public void testDeleteJob() throws SchedulingException {
        scheduler.addJob(job);
        scheduler.deleteJob(job);

        assertNull("Failed to delete the job correctly.", scheduler.getJob("accuracyJob"));
    }

    /**
     * <p>
     * Accuracy tests for <code>getJob(String)</code>.
     * </p>
     *
     *
     * @throws SchedulingException to JUnit
     */
    public void testGetJob() throws SchedulingException {
        scheduler.addJob(job);

        Job newJob = scheduler.getJob("accuracyJob");
        assertEquals("Failed to get the job correctly.", 8, newJob.getRecurrence());
        assertEquals("Failed to get the job correctly.", 5, newJob.getIntervalValue());
    }

    /**
     * <p>
     * Accuracy tests for <code>getJobList()</code>.
     * </p>
     *
     *
     * @throws SchedulingException to JUnit
     */
    public void testGetJobList() throws SchedulingException {
        Job[] jobList = scheduler.getJobList();

        assertEquals("Failed to get the job list correctly.", 2, jobList.length);
        assertEquals("Failed to get the job list correctly.", 4, jobList[0].getRecurrence());
        assertEquals("Failed to get the job list correctly.", 2, jobList[0].getIntervalValue());
    }

    /**
     * <p>
     * Accuracy tests for <code>addGroup(JobGroup)</code>.
     * </p>
     *
     *
     * @throws SchedulingException to JUnit
     */
    public void testAddGroup() throws SchedulingException {
        scheduler.addJob(job);
        scheduler.addGroup(jobGroup);

        JobGroup newGroup = scheduler.getGroup("jobGroup");
        assertEquals("Failed to add the group correctly.", 1, newGroup.getJobs().length);
        assertEquals("Failed to add the group correctly.", "accuracyJob", newGroup.getJobs()[0].getName());
    }

    /**
     * <p>
     * Accuracy tests for <code>updateGroup(JobGroup)</code>.
     * </p>
     *
     *
     * @throws SchedulingException to JUnit
     */
    public void testUpdateGroup() throws SchedulingException {
        scheduler.addJob(job);
        scheduler.addGroup(jobGroup);

        jobs.add(job);
        jobGroup = new JobGroup("group", jobs);
        scheduler.updateGroup(jobGroup);

        assertEquals("Failed to update the group correctly.", 2, scheduler.getGroup("group").getJobs().length);
    }

    /**
     * <p>
     * Accuracy tests for <code>deleteGroup(String)</code>.
     * </p>
     *
     *
     * @throws SchedulingException to JUnit
     */
    public void testDeleteGroup() throws SchedulingException {
        scheduler.addJob(job);
        scheduler.addGroup(jobGroup);
        scheduler.deleteGroup("group");

        assertNull("Failed to add the group correctly.", scheduler.getGroup("group"));
    }

    /**
     * <p>
     * Accuracy tests for <code>getAllGroups()</code>.
     * </p>
     *
     *
     * @throws SchedulingException to JUnit
     */
    public void testGetAllGroups() throws SchedulingException {
        JobGroup[] newGroups = scheduler.getAllGroups();
        assertEquals("Failed to get all the groups correctly.", 1, newGroups.length);
        assertEquals("Failed to get all the groups correctly.", "group_1", newGroups[0].getName());
    }

    /**
     * <p>
     * Accuracy tests for <code>getAllDependentJobs(Job)</code>.
     * </p>
     *
     *
     * @throws SchedulingException to JUnit
     */
    public void testGetAllDependentJobs() throws SchedulingException {
        scheduler.addJob(job);
        assertEquals("Failed to get all dependent jobs correctly.", 0,
            scheduler.getAllDependentJobs(job).length);
        job.setName("jobName07");
        // the accuracyJob and newJobName01 depends on it.
        assertEquals("Failed to get all dependent jobs correctly.", 2,
            scheduler.getAllDependentJobs(job).length);

    }

    /**
     * <p>
     * Accuracy tests <code>getGroup(String)<code>.
     * </p>
     *
     * @throws SchedulingException
     *             to JUnit
     */
    public void testGetGroup() throws SchedulingException {
        scheduler.addJob(job);
        scheduler.addGroup(jobGroup);

        JobGroup newGroup = scheduler.getGroup("jobGroup");
        assertEquals("Failed to get the group correctly.", "jobGroup", newGroup.getName());
        assertEquals("Failed to get the group correctly.", 1, newGroup.getJobs().length);
        assertEquals("Failed to get the group correctly.", "accuracyJob", newGroup.getJobs()[0].getName());
    }
}
