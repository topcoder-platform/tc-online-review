/*
 * Copyright (c) 2007, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.scheduler.processor.accuracytests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.Scheduler;
import com.topcoder.util.scheduler.scheduling.SchedulingException;

/**
 * <p>
 * Mock class for <code>Scheduler</code> used for testing the <code>JobProcessor</code>.
 * </p>
 *
 * @author bbskill
 * @version 1.0
 */
public class MockScheduler implements Scheduler {

    /**
     * <p>
     * Map instance containing the Jobs.
     * </p>
     */
    private HashMap jobs = new HashMap();

    /**
     * <p>
     * Map instance containing the dependent Jobs.
     * </p>
     */

    private HashMap depJobs = new HashMap();

    /**
     * <p>
     * Default constructor.
     * </p>
     */
    public MockScheduler() {
    }

    /**
     * <p>
     * Adds the given job to the scheduler.
     * </p>
     *
     *
     * @param job The job to add.
     * @throws IllegalArgumentException
     * 			   if the added job is null or has a same name with existing job or has an invalid dependence or has
     *             both a schedule time and a dependence
     */
    public void addJob(Job job) throws SchedulingException {
        jobs.put(job.getName(), job);
    }

    /**
     * <p>
     * Updates the given job in the scheduler.
     * </p>
     *
     *
     * @param job
     *            The job to update.
     * @throws IllegalArgumentException
     *             if the added job is null or has a same name with existing job or has an invalid dependence or has
     *             both a schedule time and a dependence
     */
    public void updateJob(Job job)throws SchedulingException{
        jobs.put(job.getName(), job);
    }

    /**
     * <p>
     * Deletes the job identified by Job from the scheduler.
     * </p>
     *
     *
     * @param job
     *            The job to delete
     * @throws IllegalArgumentException
     *             if the parameters are invalid.
     */
    public void deleteJob(Job job)throws SchedulingException{
        jobs.remove(job.getName());
    }

    /**
     * <p>
     * Get the job with the given name.
     * </p>
     *
     *
     * @return the job with the given name, null if the specified job doesn't exist.
     * @param jobName
     *            the name of job to get
     * @throws IllegalArgumentException
     *             if the jobName is null or empty
     */
    public Job getJob(String jobName)throws SchedulingException{
        return (Job)jobs.get(jobName);
    }

    /**
     * <p>
     * Returns a list of all jobs in the Scheduler.
     * </p>
     *
     *
     * @return an array containing all Jobs in this scheduler.
     * @throws SchedulingException
     *             If the scheduler is unable to get these jobs from persistence
     */
    public Job[] getJobList()throws SchedulingException {
        return (Job[])jobs.values().toArray(new Job[0]);
    }

    /**
     * <p>
     * Add the group to this scheduler.
     * </p>
     *
     *
     * @param group the group to add
     */
    public void addGroup(JobGroup group)throws SchedulingException {
        // ignore
    }

    /**
     * <p>
     * Update the group in this scheduler.
     * </p>
     *
     *
     * @param group  the group to update
     */
    public void updateGroup(JobGroup group)throws SchedulingException {
         // ignore
    }

    /**
     * <p>
     * Remove the group with the given name.
     * </p>
     *
     *
     * @param name the name of the group to remove
     */
    public void deleteGroup(String name)throws SchedulingException {
         // ignore
    }

    /**
     * <p>
     * Return all the groups defined in this scheduler.
     * </p>
     *
     *
     * @return all the groups defined in this scheduler
     */
    public JobGroup[] getAllGroups()throws SchedulingException {
        // ignore
        return null;
    }

    /**
     * <p>
     * Return the group with the given name. If it doesn't exist, return null.
     * </p>
     *
     *
     * @return the group with the given name.
     */
    public JobGroup getGroup(String name)throws SchedulingException {
        // ignore
        return null;
    }

    /**
     * <p>
     * Return all the jobs dependant on the given job.
     * </p>
     *
     *
     * @return all the jobs depend on the given job
     * @param job
     *            the given job to query
     * @throws IllegalArgumentException
     *             if the job is null or the job doesn't exist in this scheduler.
     */
    public Job[] getAllDependentJobs(Job job)throws SchedulingException {
        List list = (List)depJobs.get(job);
        if (list == null) {
            return null;
        } else {
            return (Job[])list.toArray(new Job[0]);
        }
    }

    /**
     * <p>
     * Adds a dependent job to a job.
     * </p>
     *
     * @param job job
     * @param depJob the dependent job
     */
    public void addDependentJob(Job job, Job depJob) {
        List list = (List) depJobs.get(job);
        if (list == null) {
            list = new ArrayList();
            depJobs.put(job, list);
        }
        list.add(depJob);
    }
}
