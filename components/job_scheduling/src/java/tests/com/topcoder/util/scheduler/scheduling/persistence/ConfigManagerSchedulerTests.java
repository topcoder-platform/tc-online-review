/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.topcoder.util.scheduler.scheduling.ConfigurationException;
import com.topcoder.util.scheduler.scheduling.Day;
import com.topcoder.util.scheduler.scheduling.Dependence;
import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.SchedulingException;
import com.topcoder.util.scheduler.scheduling.TestHelper;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for ConfigManagerScheduler.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class ConfigManagerSchedulerTests extends TestCase {
    /**
     * <p>
     * This constant represents the namespace to be used by this component.
     * </p>
     */
    private static final String NAMESPACE = "com.topcoder.util.scheduler.scheduling.persistence";

    /**
     * <p>
     * This constant represents the file to be used by this component.
     * </p>
     */
    private static final String CONFIGFILE = "test_files" + File.separator + "sample_config_persistence.xml";

    /**
     * <p>
     * The ConfigManagerScheduler instance for testing.
     * </p>
     */
    private ConfigManagerScheduler scheduler;

    /**
     * <p>
     * The jobs list for testing.
     * </p>
     */
    private List jobs;

    /**
     * <p>
     * The Job instance for testing.
     * </p>
     */
    private Job job;

    /**
     * <p>
     * The JobGroup instance for testing.
     * </p>
     */
    private JobGroup jobGroup;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception to JUnit
     *
     */
    @Override
    protected void setUp() throws Exception {
        TestHelper.loadSingleXMLConfig(TestHelper.LOG_NAMESPACE, TestHelper.LOG_CONFIGFILE);
        TestHelper.loadSingleXMLConfig(NAMESPACE, CONFIGFILE);

        job = new Job("NewJobName", JobType.JOB_TYPE_EXTERNAL, "dir");
        Dependence dependence = new Dependence("jobName07", EventHandler.SUCCESSFUL, 1000);
        job.setDependence(dependence);
        job.setRecurrence(8);
        job.setIntervalUnit(new Day());
        job.setIntervalValue(5);
        job.setAsyncMode(true);

        jobs = new ArrayList();
        jobs.add(job);
        jobGroup = new JobGroup("group", jobs);

        scheduler = new ConfigManagerScheduler(NAMESPACE);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception to JUnit
     *
     */
    @Override
    protected void tearDown() throws Exception {
        scheduler = null;
        jobGroup = null;
        jobs = null;
        job = null;

        TestHelper.recoverArchiveFile();
        TestHelper.clearConfigFile(NAMESPACE);
        TestHelper.clearConfigFile(TestHelper.LOG_NAMESPACE);
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(ConfigManagerSchedulerTests.class);
    }

    /**
     * <p>
     * Tests ctor ConfigManagerScheduler#ConfigManagerScheduler(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created ConfigManagerScheduler instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new ConfigManagerScheduler instance.", scheduler);
    }

    /**
     * <p>
     * Tests ctor ConfigManagerScheduler#ConfigManagerScheduler(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws ConfigurationException to JUnit
     */
    public void testCtor_NullNamespace() throws ConfigurationException {
        try {
            new ConfigManagerScheduler(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor ConfigManagerScheduler#ConfigManagerScheduler(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is empty and expects IllegalArgumentException.
     * </p>
     *
     * @throws ConfigurationException to JUnit
     */
    public void testCtor_EmptyNamespace() throws ConfigurationException {
        try {
            new ConfigManagerScheduler(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor ConfigManagerScheduler#ConfigManagerScheduler(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is unknown and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_UnknowNamespace() {
        try {
            new ConfigManagerScheduler("UnknowNamespace");
            fail("ConfigurationException expected.");
        } catch (ConfigurationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#addJob(Job) for accuracy.
     * </p>
     *
     * <p>
     * It verifies ConfigManagerScheduler#addJob(Job) is correct.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testAddJob() throws SchedulingException {
        scheduler.addJob(job);

        Job newJob = scheduler.getJob("NewJobName");
        assertEquals("Failed to add the job correctly.", 8, newJob.getRecurrence());
        assertEquals("Failed to add the job correctly.", 5, newJob.getIntervalValue());
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#addJob(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when job is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testAddJob_NullJob() throws SchedulingException {
        try {
            scheduler.addJob(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#addJob(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when job name configed in the config manager
     * and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testAddJob_ExistJobName() throws SchedulingException {
        job.setName("jobName07");
        try {
            scheduler.addJob(job);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#addJob(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when job dependence doesn't configed in the config manager
     * and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testAddJob_NoExistDependence() throws SchedulingException {
        job.setDependence(new Dependence("jobName", EventHandler.SUCCESSFUL, 1));
        try {
            scheduler.addJob(job);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#addJob(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when the job has both a schedule time and a dependence
     * and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testAddJob_BothDependenceAndSchedule() throws SchedulingException {
        job.setDependence(new Dependence("jobName07", EventHandler.SUCCESSFUL, 10000));
        job.setStartDate(new GregorianCalendar());
        try {
            scheduler.addJob(job);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#addJob(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is unknown and expects SchedulingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testAddJob_UnknownNamespace() throws Exception {
        TestHelper.clearConfigFile(NAMESPACE);
        try {
            scheduler.addJob(job);
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#updateJob(Job) for accuracy.
     * </p>
     *
     * <p>
     * It verifies ConfigManagerScheduler#updateJob(Job) is correct.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testUpdateJob() throws SchedulingException {
        scheduler.addJob(job);
        job.setIntervalValue(10);
        job.setRecurrence(20);
        job.setAsyncMode(false);

        scheduler.updateJob(job);

        Job newJob = scheduler.getJob("NewJobName");
        assertEquals("Failed to update the job correctly.", job.getActive(), newJob.getActive());
        assertEquals("Failed to update the job correctly.", job.getExecutionCount(), newJob.getExecutionCount());
        assertEquals("Failed to update the job correctly.", job.getJobType(), newJob.getJobType());
        assertEquals("Failed to update the job correctly.", job.getName(), newJob.getName());
        assertEquals("Failed to update the job correctly.", job.getRunCommand(), newJob.getRunCommand());
        assertEquals("Failed to update the job correctly.", 20, newJob.getRecurrence());
        assertEquals("Failed to update the job correctly.", 10, newJob.getIntervalValue());
        assertEquals("Failed to update the job correctly.", false, newJob.isAsyncMode());
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#updateJob(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when job is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testUpdateJob_NullJob() throws SchedulingException {
        try {
            scheduler.updateJob(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#updateJob(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when job name doesn't configed in the config manager
     * and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testUpdateJob_NoExistJobName() throws SchedulingException {
        try {
            scheduler.updateJob(job);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#updateJob(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when job dependence doesn't configed in the config manager
     * and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testUpdateJob_NoExistDependence() throws SchedulingException {
        scheduler.addJob(job);
        job.setDependence(new Dependence("jobName", EventHandler.SUCCESSFUL, 1));
        try {
            scheduler.updateJob(job);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#updateJob(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when the job has both a schedule time and a dependence
     * and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testUpdateJob_BothDependenceAndSchedule() throws SchedulingException {
        scheduler.addJob(job);
        job.setDependence(new Dependence("jobName07", EventHandler.SUCCESSFUL, 10000));
        job.setStartDate(new GregorianCalendar());
        try {
            scheduler.updateJob(job);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#updateJob(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is unknown and expects SchedulingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testUpdateJob_UnknownNamespace() throws Exception {
        scheduler.addJob(job);
        TestHelper.clearConfigFile(NAMESPACE);
        try {
            scheduler.updateJob(job);
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#deleteJob(Job) for accuracy.
     * </p>
     *
     * <p>
     * It verifies ConfigManagerScheduler#deleteJob(Job) is correct.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testDeleteJob() throws SchedulingException {
        scheduler.addJob(job);
        scheduler.deleteJob(job);

        assertNull("Failed to delete the job correctly.", scheduler.getJob("NewJobName"));
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#deleteJob(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when job is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testDeleteJob_NullJob() throws SchedulingException {
        try {
            scheduler.deleteJob(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#deleteJob(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when the job can not be found and expects SchedulingException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testDeleteJob_NotFonundJob() throws SchedulingException {
        scheduler.addJob(job);
        job.setName("new");
        try {
            scheduler.deleteJob(job);
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#deleteJob(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is unknown and expects SchedulingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testDeleteJob_UnknownNamespace() throws Exception {
        scheduler.addJob(job);
        TestHelper.clearConfigFile(NAMESPACE);
        try {
            scheduler.deleteJob(job);
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getJob(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies ConfigManagerScheduler#getJob(String) is correct.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testGetJob() throws SchedulingException {
        scheduler.addJob(job);

        Job newJob = scheduler.getJob("NewJobName");
        assertEquals("Failed to get the job correctly.", 8, newJob.getRecurrence());
        assertEquals("Failed to get the job correctly.", 5, newJob.getIntervalValue());
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getJob(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when jobName is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testGetJob_NullJobName() throws SchedulingException {
        try {
            scheduler.getJob(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getJob(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when jobName is empty and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testGetJob_EmptyJobName() throws SchedulingException {
        try {
            scheduler.getJob(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getJob(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is unknown and expects SchedulingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetJob_UnknownNamespace() throws Exception {
        TestHelper.clearConfigFile(NAMESPACE);
        try {
            scheduler.getJob("jobName07");
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getJobList() for accuracy.
     * </p>
     *
     * <p>
     * It verifies ConfigManagerScheduler#getJobList() is correct.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testGetJobList() throws SchedulingException {
        Job[] jobList = scheduler.getJobList();

        assertEquals("Failed to get the job list correctly.", 2, jobList.length);
        assertEquals("Failed to get the job list correctly.", 5, jobList[0].getRecurrence());
        assertEquals("Failed to get the job list correctly.", 2, jobList[0].getIntervalValue());
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getJobList() for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is unknown and expects SchedulingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetJobList_UnknownNamespace() throws Exception {
        TestHelper.clearConfigFile(NAMESPACE);
        try {
            scheduler.getJobList();
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#addGroup(JobGroup) for accuracy.
     * </p>
     *
     * <p>
     * It verifies ConfigManagerScheduler#addGroup(JobGroup) is correct.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testAddGroup() throws SchedulingException {
        scheduler.addJob(job);
        scheduler.addGroup(jobGroup);

        JobGroup newGroup = scheduler.getGroup("group");
        assertEquals("Failed to add the group correctly.", 1, newGroup.getJobs().length);
        assertEquals("Failed to add the group correctly.", "NewJobName", newGroup.getJobs()[0].getName());
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#addGroup(JobGroup) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when group is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testAddGroup_NullGroup() throws SchedulingException {
        try {
            scheduler.addGroup(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#addGroup(JobGroup) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when a job of group is not exist in the scheduler
     * and expects SchedulingException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testAddGroup_NoExistJob() throws SchedulingException {
        try {
            scheduler.addGroup(jobGroup);
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#addGroup(JobGroup) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is unknown and expects SchedulingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testAddGroup_UnknownNamespace() throws Exception {
        TestHelper.clearConfigFile(NAMESPACE);
        try {
            scheduler.addGroup(jobGroup);
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#updateGroup(JobGroup) for accuracy.
     * </p>
     *
     * <p>
     * It verifies ConfigManagerScheduler#updateGroup(JobGroup) is correct.
     * </p>
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
     * Tests ConfigManagerScheduler#updateGroup(JobGroup) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when group is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testUpdateGroup_NullGroup() throws SchedulingException {
        try {
            scheduler.updateGroup(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#updateGroup(JobGroup) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when group does not exist in the scheduler
     * and expects SchedulingException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testUpdateGroup_NoExistGroup() throws SchedulingException {
        try {
            scheduler.updateGroup(jobGroup);
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#updateGroup(JobGroup) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is unknown and expects SchedulingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testUpdateGroup_UnknownNamespace() throws Exception {
        TestHelper.clearConfigFile(NAMESPACE);
        try {
            scheduler.updateGroup(jobGroup);
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#deleteGroup(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies ConfigManagerScheduler#deleteGroup(String) is correct.
     * </p>
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
     * Tests ConfigManagerScheduler#deleteGroup(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when name is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testDeleteGroup_NullName() throws SchedulingException {
        try {
            scheduler.deleteGroup(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#deleteGroup(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when name is empty and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testDeleteGroup_EmptyName() throws SchedulingException {
        try {
            scheduler.deleteGroup(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#deleteGroup(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is unknown and expects SchedulingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testDeleteGroup_UnknownNamespace() throws Exception {
        TestHelper.clearConfigFile(NAMESPACE);
        try {
            scheduler.deleteGroup("group");
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getAllGroups() for accuracy.
     * </p>
     *
     * <p>
     * It verifies ConfigManagerScheduler#getAllGroups() is correct.
     * </p>
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
     * Tests ConfigManagerScheduler#getAllGroups() for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is unknown and expects SchedulingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetAllGroups_UnknownNamespace() throws Exception {
        TestHelper.clearConfigFile(NAMESPACE);
        try {
            scheduler.getAllGroups();
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getAllDependentJobs(Job) for accuracy.
     * </p>
     *
     * <p>
     * It verifies ConfigManagerScheduler#getAllDependentJobs(Job) is correct.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testGetAllDependentJobs() throws SchedulingException {
        scheduler.addJob(job);
        assertEquals("Failed to get all dependent jobs correctly.", 0, scheduler.getAllDependentJobs(job).length);
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getAllDependentJobs(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when job is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testGetAllDependentJobs_NullJob() throws SchedulingException {
        try {
            scheduler.getAllDependentJobs(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getAllDependentJobs(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when job isn't configed in the config manager
     * and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testGetAllDependentJobs_NotExistJob() throws SchedulingException {
        try {
            scheduler.getAllDependentJobs(job);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getAllDependentJobs(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is unknown and expects SchedulingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetAllDependentJobs_UnknownNamespace() throws Exception {
        TestHelper.clearConfigFile(NAMESPACE);
        try {
            scheduler.getAllDependentJobs(job);
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getGroup(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies ConfigManagerScheduler#getGroup(String) is correct.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testGetGroup() throws SchedulingException {
        scheduler.addJob(job);
        scheduler.addGroup(jobGroup);

        JobGroup newGroup = scheduler.getGroup("group");
        assertEquals("Failed to get the group correctly.", "group", newGroup.getName());
        assertEquals("Failed to get the group correctly.", 1, newGroup.getJobs().length);
        assertEquals("Failed to get the group correctly.", "NewJobName", newGroup.getJobs()[0].getName());
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getGroup(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when name is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testGetGroup_NullName() throws SchedulingException {
        try {
            scheduler.getGroup(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getGroup(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when name is empty and expects IllegalArgumentException.
     * </p>
     *
     * @throws SchedulingException to JUnit
     */
    public void testGetGroup_EmptyName() throws SchedulingException {
        try {
            scheduler.getGroup(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ConfigManagerScheduler#getGroup(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is unknown and expects SchedulingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetGroup_UnknownNamespace() throws Exception {
        TestHelper.clearConfigFile(NAMESPACE);
        try {
            scheduler.getGroup("group");
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            //good
        }
    }
}