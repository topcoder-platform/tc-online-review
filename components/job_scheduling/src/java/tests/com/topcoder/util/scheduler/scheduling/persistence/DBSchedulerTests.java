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
 * Unit test cases for DBScheduler.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class DBSchedulerTests extends TestCase {
    /**
     * <p>
     * This constant represents the namespace to be used by this component.
     * </p>
     */
    private static final String NAMESPACE = "com.topcoder.util.scheduler.scheduling.persistence.DBScheduler";

    /**
     * <p>
     * This constant represents the file to be used by this component.
     * </p>
     */
    private static final String CONFIGFILE = "test_files" + File.separator + "sample_db_persistence.xml";

    /**
     * <p>
     * DBScheduler instance for testing.
     * </p>
     */
    private DBScheduler scheduler;

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
     */
    @Override
    protected void setUp() throws Exception {
        TestHelper.loadXMLConfig(CONFIGFILE);
        TestHelper.setUpDataBase();

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

        scheduler = new DBScheduler(NAMESPACE);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Override
    protected void tearDown() throws Exception {
        TestHelper.tearDownDataBase();
        TestHelper.clearConfig();
        scheduler = null;
        jobGroup = null;
        jobs = null;
        job = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(DBSchedulerTests.class);
    }

    /**
     * <p>
     * Tests ctor DBScheduler#DBScheduler(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created DBScheduler instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new DBScheduler instance.", scheduler);
    }

    /**
     * <p>
     * Tests ctor DBScheduler#DBScheduler(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCtor_NullNamespace() throws Exception {
        try {
            new DBScheduler(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor DBScheduler#DBScheduler(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is empty and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCtor_EmptyNamespace() throws Exception {
        try {
            new DBScheduler(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor DBScheduler#DBScheduler(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when namespace is unknown and expects ConfigurationException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCtor_UnknownNamespace() throws Exception {
        try {
            new DBScheduler("no_namespace");
            fail("ConfigurationException expected.");
        } catch (ConfigurationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests DBScheduler#addJob(Job) for accuracy.
     * </p>
     *
     * <p>
     * It verifies DBScheduler#addJob(Job) is correct.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testAddJob() throws Exception {
        scheduler.addJob(job);

        Job newJob = scheduler.getJob("NewJobName");
        assertEquals("Failed to add the job correctly.", 8, newJob.getRecurrence());
        assertEquals("Failed to add the job correctly.", 5, newJob.getIntervalValue());
        assertEquals("Failed to add the job correctly.", true, newJob.isAsyncMode());
    }

    /**
     * <p>
     * Tests DBScheduler#addJob(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when job is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testAddJob_NullJob() throws Exception {
        try {
            scheduler.addJob(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DBScheduler#addJob(Job) for failure.
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
     * Tests DBScheduler#addJob(Job) for failure.
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
     * Tests DBScheduler#addJob(Job) for failure.
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
     * Tests DBScheduler#updateJob(Job) for accuracy.
     * </p>
     *
     * <p>
     * It verifies DBScheduler#updateJob(Job) is correct.
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
        assertEquals("Failed to update the job correctly.", 20, newJob.getRecurrence());
        assertEquals("Failed to update the job correctly.", 10, newJob.getIntervalValue());
        assertEquals("Failed to update the job correctly.", false, newJob.isAsyncMode());
    }

    /**
     * <p>
     * Tests DBScheduler#updateJob(Job) for failure.
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
     * Tests DBScheduler#updateJob(Job) for failure.
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
     * Tests DBScheduler#updateJob(Job) for failure.
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
     * Tests DBScheduler#updateJob(Job) for failure.
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
     * Tests DBScheduler#deleteJob(Job) for accuracy.
     * </p>
     *
     * <p>
     * It verifies DBScheduler#deleteJob(Job) is correct.
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
     * Tests DBScheduler#deleteJob(Job) for failure.
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
     * Tests DBScheduler#deleteJob(Job) for failure.
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
     * Tests DBScheduler#getJob(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies DBScheduler#getJob(String) is correct.
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
     * Tests DBScheduler#getJob(String) for failure.
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
     * Tests DBScheduler#getJob(String) for failure.
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
     * Tests DBScheduler#getJobList() for accuracy.
     * </p>
     *
     * <p>
     * It verifies DBScheduler#getJobList() is correct.
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
     * Tests DBScheduler#addGroup(JobGroup) for accuracy.
     * </p>
     *
     * <p>
     * It verifies DBScheduler#addGroup(JobGroup) is correct.
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
     * Tests DBScheduler#addGroup(JobGroup) for failure.
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
     * Tests DBScheduler#addGroup(JobGroup) for failure.
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
     * Tests DBScheduler#addGroup(JobGroup) for failure.
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
     * Tests DBScheduler#updateGroup(JobGroup) for accuracy.
     * </p>
     *
     * <p>
     * It verifies DBScheduler#updateGroup(JobGroup) is correct.
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
     * Tests DBScheduler#updateGroup(JobGroup) for failure.
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
     * Tests DBScheduler#updateGroup(JobGroup) for failure.
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
     * Tests DBScheduler#updateGroup(JobGroup) for failure.
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
     * Tests DBScheduler#deleteGroup(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies DBScheduler#deleteGroup(String) is correct.
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
     * Tests DBScheduler#deleteGroup(String) for failure.
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
     * Tests DBScheduler#deleteGroup(String) for failure.
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
     * Tests DBScheduler#getAllGroups() for accuracy.
     * </p>
     *
     * <p>
     * It verifies DBScheduler#getAllGroups() is correct.
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
     * Tests DBScheduler#getAllDependentJobs(Job) for accuracy.
     * </p>
     *
     * <p>
     * It verifies DBScheduler#getAllDependentJobs(Job) is correct.
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
     * Tests DBScheduler#getAllDependentJobs(Job) for failure.
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
     * Tests DBScheduler#getAllDependentJobs(Job) for failure.
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
     * Tests DBScheduler#getGroup(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies DBScheduler#getGroup(String) is correct.
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
     * Tests DBScheduler#getGroup(String) for failure.
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
     * Tests DBScheduler#getGroup(String) for failure.
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
}