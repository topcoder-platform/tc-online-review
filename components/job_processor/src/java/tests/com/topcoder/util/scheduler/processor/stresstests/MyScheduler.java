/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor.stresstests;

import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.Scheduler;
import com.topcoder.util.scheduler.scheduling.SchedulingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Mock implementation of Scheduler, providing the function to get the dependent jobs for a job and retrieve all job.
 * @author zhuzeyuan
 * @version 1.0
 */
public class MyScheduler implements Scheduler {
    /** Dependent jobs map, Job as key and List of dependent jobs as value. */
    private HashMap depJobs = new HashMap();

    /** Jobs map, job names as key and Jobs as value. */
    private HashMap jobs = new HashMap();

    /**
     * Adds a dependent job to a job.
     * @param job
     *            job
     * @param depJob
     *            dependent job
     */
    public void addDependentJob(Job job, Job depJob) {
        List list = (List) depJobs.get(job.getName());
        
        if (list == null) {
            list = new ArrayList();
            depJobs.put(job.getName(), list);
        }

        list.add(depJob);
    }

    /**
     * Not implemented.
     * @param group
     *            Not implemented.
     * @throws SchedulingException
     *             Not implemented.
     */
    public void addGroup(JobGroup group) {
    }

    /**
     * Adds job.
     * @param job
     *            job to add
     * @throws SchedulingException
     *             ignore this
     */
    public void addJob(Job job) {
        jobs.put(job.getName(), job);
    }

    /**
     * Not implemented.
     * @param name
     *            Not implemented.
     * @throws SchedulingException
     *             Not implemented.
     */
    public void deleteGroup(String name) {
    }

    /**
     * Not implemented.
     * @param job
     *            Not implemented.
     * @throws SchedulingException
     *             Not implemented.
     */
    public void deleteJob(Job job) {
    }

    /**
     * Gets all dependent jobs of a job.
     * @param job
     *            job
     * @return all dependent jobs of the job
     * @throws SchedulingException
     *             ignore this
     */
    public Job[] getAllDependentJobs(Job job) {
        List list = (List) depJobs.get(job.getName());

        if (list == null) {
            return new Job[0];
        } else {
            return (Job[]) list.toArray(new Job[list.size()]);
        }
    }

    /**
     * Not implemented.
     * @return Not implemented.
     * @throws SchedulingException
     *             Not implemented.
     */
    public JobGroup[] getAllGroups() {
        return null;
    }

    /**
     * Not implemented.
     * @param name
     *            Not implemented.
     * @return Not implemented.
     * @throws SchedulingException
     *             Not implemented.
     */
    public JobGroup getGroup(String name) {
        return null;
    }

    /**
     * Gets the job by name.
     * @param jobName
     *            name of job
     * @return Job
     * @throws SchedulingException
     *             ignore this
     */
    public Job getJob(String jobName) {
        return (Job) this.jobs.get(jobName);
    }

    /**
     * Gets all jobs.
     * @return all jobs
     * @throws SchedulingException
     *             ignore this
     */
    public Job[] getJobList() {
        return (Job[]) this.jobs.values().toArray(new Job[0]);
    }

    /**
     * Not implemented.
     * @param group
     *            Not implemented.
     * @throws SchedulingException
     *             Not implemented.
     */
    public void updateGroup(JobGroup group) {
    }

    /**
     * Not implemented.
     * @param job
     *            Not implemented.
     * @throws SchedulingException
     *             Not implemented.
     */
    public void updateJob(Job job) {
    }
}
