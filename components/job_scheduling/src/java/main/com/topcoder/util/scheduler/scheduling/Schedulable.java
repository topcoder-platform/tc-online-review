/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

/**
 * <p>
 * The Schedulabe interface is one of two required interfaces (the other being java.lang.Runnable)
 * for classes to be run as a job.
 * </p>
 *
 * <p>
 * Version 2.0: the implementations of the Schedulable should be thread-safe, that means when the
 * job is running, the isDone, getRunningStatus, etc methods can be called in thread-safe way.
 * </p>
 *
 * @author argolite, TCSDEVELOPER
 * @version 3.0
 * @since 3.0
 */
public interface Schedulable {
    /**
     * <p>
     * Returns whether the job is done.
     * </p>
     *
     * @return whether the job is done
     */
    public boolean isDone();

    /**
     * <p>
     * Closes the job if the job is running, if not running, do nothing.
     * </p>
     */
    public void close();

    /**
     * <p>
     * Gets the job name.
     * </p>
     *
     * @return the job name.
     *
     * @since 3.0
     */
    public String getJobName();

    /**
     * <p>
     * Sets the job name.
     * </p>
     *
     * @param jobName The new job name.
     * @throws IllegalArgumentException If jobName is null/empty
     *
     * @since 3.0
     */
    public void setJobName(String jobName);

    /**
     * <p>
     * Gets the status of the job. It is deprecated in 2.0, using getRunningStatus instead.
     * </p>
     *
     * @return the status of job
     * @deprecated since 2.0
     */
    public String getStatus();
}
