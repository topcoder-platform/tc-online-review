/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling;

import com.topcoder.util.file.fieldconfig.NodeList;

/**
 * This is a simple class implementing the <code>ScheduledJobRunner</code>
 * used for testing purpose.
 * @author fuyun
 * @version 3.1
 */
public class CustomScheduledJobRunner implements ScheduledJobRunner {

    /**
     * A flag denoting if the task is done.
     */
    private boolean done = false;

    /**
     * the job to interact with.
     */
    private Job job = null;

    /**
     * The job name.
     */
    private String jobName;

    /**
     * Returns <code>true</code> if the job is done, <code>false</code>
     * otherwise.
     * @return if the job is done
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * Closes the job.
     */
    public void close() {
    }

    /**
     * Gets the status of this job, Successful or Running.
     * @return the running status
     */
    public String getStatus() {
        return this.done ? SUCCESSFUL : RUNNING;
    }

    /**
     * Gets the message data.
     * @return always null.
     */
    public NodeList getMessageData() {
        return null;
    }

    /**
     * Gets the running status, Successful or Running.
     * @return the running status
     */
    public String getRunningStatus() {
        return getStatus();
    }

    /**
     * Set the job to interact with.
     * @param job the job to interact with
     */
    public void setJob(Job job) {
        this.job = job;
    }

    /**
     * Gets the job to interact with.
     * @return the job to interact with
     */
    public Job getJob() {
        return this.job;
    }

    /**
     * Runs the job.
     */
    public void run() {
        // put a attribute into job's attribute.
        job.setAttribute("name", "value");
        this.done = true;
    }

    /**
     * Gets the job name.
     * @return the job name.
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Sets the job name.
     * @param jobName the job name to set.
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

}
