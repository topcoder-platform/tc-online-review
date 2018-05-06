/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.topcoder.util.scheduler.scheduling.Dependence;
import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Hour;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.Scheduler;
import com.topcoder.util.scheduler.scheduling.persistence.ConfigManagerScheduler;

/**
 * <p>
 * Abstracted accuracy tests of <code>{@link Scheduler}</code> interface.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public abstract class SchedulerTest extends TestCase {

    /**
     * <p>
     * Represents the <code>{@link Scheduler}</code> instance for testing.
     * </p>
     */
    protected Scheduler scheduler;

    /**
     * <p>
     * Represents the <code>{@link Job}</code> instance used for testing.
     * </p>
     */
    private Job job;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception to junit.
     */
    protected void setUp() throws Exception {
        AccuracyHelper.loadConfig();
        job = new Job("folderCreate", JobType.JOB_TYPE_EXTERNAL, "mkdir");
        Dependence dependence = new Dependence("jobName07", EventHandler.SUCCESSFUL, 100);
        job.setDependence(dependence);
        job.setActive(true);
        job.setIntervalUnit(new Hour());
        job.setIntervalValue(3);
        job.setRecurrence(5);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ConfigManagerScheduler#ConfigManagerScheduler(String)}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     * <p>
     * Also checks the inheritance of the class.
     * </p>
     */
    public void testConfigManagerScheduler() {
        assertNotNull("failed to create ConfigManagerScheduler", scheduler);
        // inheritance
        assertTrue("ConfigManagerScheduler should inherit from Scheduler", scheduler instanceof Scheduler);
    }

    /**
     * <p>
     * Accuracy test for
     * <code>{@link ConfigManagerScheduler#addJob(com.topcoder.util.scheduler.scheduling.Job)}</code>,
     * <code>{@link ConfigManagerScheduler#deleteJob(Job)}</code>,
     * <code>{@link ConfigManagerScheduler#updateJob(Job)}</code> and
     * <code>{@link ConfigManagerScheduler#getJob(String)}</code>.
     * </p>
     * <p>
     * Validates all job operations.
     * </p>
     *
     * @throws Exception to junit.
     */
    public void testJobOperations() throws Exception {
        // added the job
        scheduler.addJob(job);
        // check the same job is present.
        Job actual = scheduler.getJob("folderCreate");
        assertEquals("failed to add/get job from scheduler", job.getName(), actual.getName());
        assertEquals("failed to add/get job from scheduler", job.getJobType(), actual.getJobType());
        assertEquals("failed to add/get job from scheduler", job.getRunCommand(), actual.getRunCommand());
        // dependence
        assertEquals("failed to add/get job from scheduler", job.getDependence().getDependentJobName(),
            actual.getDependence().getDependentJobName());
        assertEquals("failed to add/get job from scheduler", job.getActive(), actual.getActive());
        assertEquals("failed to add/get job from scheduler", job.getIntervalValue(), actual
            .getIntervalValue());
        assertEquals("failed to add/get job from scheduler", job.getRecurrence(), actual.getRecurrence());

        // update job
        job.setActive(false);
        job.setRecurrence(3);
        scheduler.updateJob(job);

        actual = scheduler.getJob("folderCreate");
        assertEquals("failed to update job from scheduler", job.getActive(), actual.getActive());
        assertEquals("failed to update job from scheduler", job.getRecurrence(), actual.getRecurrence());

        // delete the job
        scheduler.deleteJob(job);
        actual = scheduler.getJob("folderCreate");
        assertNull("failed to delete the job", actual);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ConfigManagerScheduler#getJobList()}</code>
     * </p>
     * <p>
     * Checks the count and name of jobs which is already persisted.
     * </p>
     *
     * @throws Exception to junit.
     */
    public void testGetJobList() throws Exception {
        Job[] jobs = scheduler.getJobList();
        assertEquals("failed to get the job list", 2, jobs.length);
        assertEquals("failed to get the job list", "jobName07", jobs[0].getName());
        assertEquals("failed to get the job list", "newJobName01", jobs[1].getName());
    }

    /**
     * <p>
     * Accuracy test for
     * <code>{@link ConfigManagerScheduler#addGroup(com.topcoder.util.scheduler.scheduling.JobGroup)}</code>,
     * <code>{@link ConfigManagerScheduler#deleteGroup(String))}</code>,
     * <code>{@link ConfigManagerScheduler#updateGroup(com.topcoder.util.scheduler.scheduling.JobGroup)}</code>
     * and <code>{@link ConfigManagerScheduler#getGroup(String))}</code>.
     * </p>
     * <p>
     * Validates all job group operations.
     * </p>
     *
     * @throws Exception to junit.
     */
    public void testGroupOperations() throws Exception {
        scheduler.addJob(job);
        List jobs = new ArrayList();

        jobs.add(job);
        JobGroup group = new JobGroup("folderGrp", jobs);
        // adds the group
        scheduler.addGroup(group);

        // check the same group is present
        JobGroup actual = scheduler.getGroup("folderGrp");
        assertEquals("failed to add/get job group from scheduler", "folderGrp", actual.getName());
        assertEquals("failed to add/get job group from scheduler", 1, actual.getJobs().length);

        // update the group
        // no handlers present.
        assertEquals(0, actual.getHandlers().length);

        jobs.add(job);
        group = new JobGroup("folderGrp", jobs);
        scheduler.updateGroup(group);
        actual = scheduler.getGroup("folderGrp");

        assertEquals("failed to update job group", 2, actual.getJobs().length);

        scheduler.deleteJob(job);
        // delete the group
        scheduler.deleteGroup("folderGrp");
        actual = scheduler.getGroup("folderGrp");
        assertNull("failed to delete the job", actual);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ConfigManagerScheduler#getAllGroups()}</code>
     * </p>
     * <p>
     * Checks the count and name of job groups which is already persisted.
     * </p>
     *
     * @throws Exception to junit.
     */
    public void testGetAllGroups() throws Exception {
        JobGroup[] groups = scheduler.getAllGroups();
        assertEquals("failed to get all job groups", 1, groups.length);
        Job[] jobs = groups[0].getJobs();
        assertEquals("failed to get the jobs in the group", "jobName07", jobs[0].getName());
        assertEquals("failed to get the jobs in the group", "newJobName01", jobs[1].getName());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ConfigManagerScheduler#getAllDependentJobs(Job)}</code>
     * </p>
     * <p>
     * Checks the count and name of the dependent jobs.
     * </p>
     *
     * @throws Exception to junit.
     */
    public void testGetAllDependentJobs() throws Exception {
        scheduler.addJob(job);
        Job persisted = scheduler.getJob("jobName07");
        Job[] deps = scheduler.getAllDependentJobs(persisted);
        assertEquals("failed to get all dependent jobs", 2, deps.length);
        assertEquals("failed to get all dependent jobs", "newJobName01", deps[0].getName());
        assertEquals("failed to get all dependent jobs", "folderCreate", deps[1].getName());
        scheduler.deleteJob(job);
    }
}
