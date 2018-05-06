/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * @(#)StressTestHelper.java
 */
package com.topcoder.util.scheduler.scheduling.stresstests;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.topcoder.util.scheduler.scheduling.Day;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.Scheduler;

/**
 * Util class that helps the stress tests.
 *
 * @author  smell
 * @version 3.0
 */
public class StressTestHelper {

    /**
     * Creates a Job instance for test, the job name will be generated based on the given id.
     *
     * @param id    the given id.
     *
     * @return a Job instance.
     */
    public static Job createJob(int id) {
        Job job = new Job("stress" + id, JobType.JOB_TYPE_EXTERNAL, "stress tests");
        job.setStartDate(new GregorianCalendar(2007, 0, 1));
        job.setStopDate(new GregorianCalendar(2008, 0, 1));
        job.setStartTime(12);
        job.setIntervalUnit(new Day());
        job.setIntervalValue(1);
        job.setRecurrence(10);
        return job;
    }

    /**
     * Creates a JobGroup instance for test, the group name will be generated based on the given id.
     *
     * @param id    the given id.
     *
     * @return  a JobGroup instance.
     */
    public static JobGroup createGroup(int id) {
        Job job = createJob(id);
        List jobs = new ArrayList();
        jobs.add(job);
        JobGroup group = new JobGroup("stress_group_" + id, jobs);
        return group;
    }

    /**
     * Adds <code>count</code> jobs into the given Scheduler.
     *
     * @param scheduler the given Scheduler.
     * @param count     the number of jobs to add.
     *
     * @throws Exception if scheduler.addJob throws any exception.
     */
    public static void addJobs(Scheduler scheduler, int count) throws Exception {
        for (int i = 0; i < count; ++i) {
            Job job = createJob(i);
            scheduler.addJob(job);
        }
    }

    /**
     * Deletes <code>count</code> jobs from the given Scheduler.
     *
     * @param scheduler the given Scheduler.
     * @param count     the number of jobs to delete.
     *
     * @throws Exception if scheduler.deleteJob throws any exception.
     */
    public static void deleteJobs(Scheduler scheduler, int count) throws Exception {
        for (int i = 0; i < count; ++i) {
            Job job = createJob(i);
            scheduler.deleteJob(job);
        }
    }

    /**
     * Updates <code>count</code> jobs into the given Scheduler.
     *
     * @param scheduler the given Scheduler.
     * @param count     the number of jobs to update.
     *
     * @throws Exception if scheduler.updateJob throws any exception.
     */
    public static void updateJobs(Scheduler scheduler, int count) throws Exception {
        for (int i = 0; i < count; ++i) {
            Job job = createJob(i);
            job.setRunCommand("update");
            scheduler.updateJob(job);
        }
    }

    /**
     * Reads <code>count</code> jobs into the given Scheduler.
     *
     * @param scheduler the given Scheduler.
     * @param count     the number of jobs to read.
     *
     * @throws Exception if scheduler.getJob throws any exception.
     */
    public static void readJobs(Scheduler scheduler, int count) throws Exception {
        for (int i = 0; i < count; ++i) {
            Job job = scheduler.getJob("stress" + i);
            job.getJobType();
        }
    }

    /**
     * Adds <code>count</code> job groups into the given Scheduler.
     *
     * @param scheduler the given Scheduler.
     * @param count     the number of job groups to add.
     *
     * @throws Exception if scheduler.addGroup throws any exception.
     */
    public static void addJobGroups(Scheduler scheduler, int count) throws Exception {
        for (int i = 0; i < count; ++i) {
            JobGroup group = createGroup(i);
            scheduler.addGroup(group);
        }
    }

    /**
     * Deletes <code>count</code> job groups from the given Scheduler.
     *
     * @param scheduler the given Scheduler.
     * @param count     the number of job groups to delete.
     *
     * @throws Exception if scheduler.deleteGroup throws any exception.
     */
    public static void deleteJobGroups(Scheduler scheduler, int count) throws Exception {
        for (int i = 0; i < count; ++i) {
            scheduler.deleteGroup("stress_group_" + i);
        }
    }

    /**
     * Updates <code>count</code> job groups into the given Scheduler.
     *
     * @param scheduler the given Scheduler.
     * @param count     the number of job groups to update.
     *
     * @throws Exception if scheduler.updateGroup throws any exception.
     */
    public static void updateJobGroups(Scheduler scheduler, int count) throws Exception {
        for (int i = 0; i < count; ++i) {
            JobGroup group = createGroup(i);
            scheduler.updateGroup(group);
        }
    }

    /**
     * Reads <code>count</code> job groups from the given Scheduler.
     *
     * @param scheduler the given Scheduler.
     * @param count     the number of job groups to read.
     *
     * @throws Exception if scheduler.getGroup throws any exception.
     */
    public static void readJobGroups(Scheduler scheduler, int count) throws Exception {
        for (int i = 0; i < count; ++i) {
            JobGroup group = scheduler.getGroup("stress_group_" + i);
            group.getJobs();
        }
    }

    /**
     * Performs combined CRUD operations upon the given Scheduler.
     *
     * @param scheduler the given Scheduler.
     * @param count     the number of job/groups to operate.
     *
     * @throws Exception if method of scheduler throws any exception.
     */
    public static void crud(Scheduler scheduler, int count) throws Exception {
        addJobs(scheduler, count);
        updateJobs(scheduler, count);
        readJobs(scheduler, count);

        addJobGroups(scheduler, count);
        updateJobGroups(scheduler, count);
        readJobGroups(scheduler, count);

        deleteJobGroups(scheduler, count);
        deleteJobs(scheduler, count);
    }
}
