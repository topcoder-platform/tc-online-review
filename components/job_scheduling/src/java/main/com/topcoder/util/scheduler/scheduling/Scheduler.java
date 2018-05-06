/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling;

/**
 * <p>
 * This interface defines the CRUD operations for Jobs and JobGroups. It defines two
 * extensions: One for reading-only data from the ConfigManager, and the second for
 * reading from and writing to a database.
 * </p>
 *
 * <p>
 * Thread Safety: the classes that implement this should be immutable and thread-safe.
 * </p>
 *
 * @author argolite, TCSDEVELOPER
 * @version 3.0
 * @since 3.0
 */
public interface Scheduler {
    /**
     * <p>
     * Adds the given job to the scheduler.
     * </p>
     *
     * @param job The job to add.
     *
     * @throws IllegalArgumentException if the added job is null or has a same name with existing
     * job or has an invalid dependence or has both a schedule time and a dependence
     * @throws SchedulingException If the scheduler is unable to add this job in persistence
     * @throws UnsupportedOperationException If writing to persistence is not supported
     */
    public void addJob(Job job) throws SchedulingException;

    /**
     * <p>
     * Updates the given job in the scheduler.
     * </p>
     *
     * @param job The job to update.
     *
     * @throws IllegalArgumentException if the added job is null or has a same name with existing
     * job or has an invalid dependence or has both a schedule time and a dependence
     * @throws SchedulingException If the scheduler is unable to update this job in persistence
     * @throws UnsupportedOperationException If writing to persistence is not supported
     */
    public void updateJob(Job job) throws SchedulingException;

    /**
     * <p>
     * Deletes the job identified by Job from the scheduler.
     * </p>
     *
     * @param job The job to delete
     *
     * @throws IllegalArgumentException if job is null
     * @throws SchedulingException if the job can not be found in persistence
     * @throws UnsupportedOperationException If writing to persistence is not supported
     */
    public void deleteJob(Job job) throws SchedulingException;

    /**
     * <p>
     * Gets the job with the given name.
     * </p>
     *
     * @return the job with the given name, null if the specified job doesn't exist.
     * @param jobName the name of job to get
     *
     * @throws IllegalArgumentException if the jobName is null or empty
     * @throws SchedulingException If the scheduler is unable to get this job from persistence
     */
    public Job getJob(String jobName) throws SchedulingException;

    /**
     * <p>
     * Returns a list of all jobs in the Scheduler.
     * </p>
     *
     * @return an array containing all Jobs in this scheduler.
     * @throws SchedulingException If the scheduler is unable to get these jobs from persistence
     */
    public Job[] getJobList() throws SchedulingException;

    /**
     * <p>
     * Adds the group to this scheduler.
     * </p>
     *
     * @param group the group to add
     *
     * @throws IllegalArgumentException if the added job is null or has a same name with existing
     * job or has an invalid dependence or has both a schedule time and a dependence
     * @throws SchedulingException If the scheduler is unable to add this group to persistence
     * @throws UnsupportedOperationException If writing to persistence is not supported
     */
    public void addGroup(JobGroup group) throws SchedulingException;

    /**
     * <p>
     * Updates the group in this scheduler.
     * </p>
     *
     * @param group the group to update
     * @throws IllegalArgumentException if the added job is null or doesn't have a same name with existing job
     * or has an invalid dependence or has both a schedule time and a dependence
     * @throws SchedulingException If the scheduler is unable to update this group in persistence
     * @throws UnsupportedOperationException If writing to persistence is not supported
     */
    public void updateGroup(JobGroup group) throws SchedulingException;

    /**
     * <p>
     * Removes the group with the given name.
     * </p>
     *
     * @param name the name of the group to remove
     *
     * @throws IllegalArgumentException if name is null or empty
     * @throws SchedulingException If the scheduler is unable to delete this group from persistence
     * @throws UnsupportedOperationException If writing to persistence is not supported
     */
    public void deleteGroup(String name) throws SchedulingException;

    /**
     * <p>
     * Returns all the groups defined in this scheduler.
     * </p>
     *
     * @return all the groups defined in this scheduler
     * @throws SchedulingException If the scheduler is unable to retrieve groups from persistence
     */
    public JobGroup[] getAllGroups() throws SchedulingException;

    /**
     * <p>
     * Returns the group with the given name.
     * </p>
     *
     * @return the group with the given name.
     * @param name the name of group to get
     *
     * @throws IllegalArgumentException if name is null or empty
     * @throws SchedulingException If the scheduler is unable to retrieve this group from persistence
     */
    public JobGroup getGroup(String name) throws SchedulingException;

    /**
     * <p>
     * Returns all the jobs dependant on the given job.
     * </p>
     *
     * @return all the jobs depend on the given job
     * @param job the given job to query
     *
     * @throws IllegalArgumentException if the job is null or the job doesn't exist in this scheduler.
     * @throws SchedulingException If the scheduler is unable to get the jobs from persistence
     */
    public Job[] getAllDependentJobs(Job job) throws SchedulingException;
}
