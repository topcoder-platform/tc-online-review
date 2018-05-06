/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling;


/**
 * <p>
 * This interface extends <code>ScheduledEnable</code> interface and defines
 * methods to get/set job for this runner, so implementation can interact with
 * the job instance.
 * </p>
 * <p>
 * Thread-safety: The implementations should be thread-safe. (getJob and setJob
 * methods should be properly synchronized).
 * </p>
 * @author Standlove, fuyun
 * @version 3.1
 * @since 3.1
 */
public interface ScheduledJobRunner extends ScheduledEnable {
    /**
     * <p>
     * Return the job instance associated with this running object.
     * </p>
     * @return the job instance associated with this running object.
     */
    public Job getJob();

    /**
     * <p>
     * set the job instance to this running object.
     * </p>
     * @throws IllegalArgumentException if the given argument is <code>null</code>
     * @param job the job to associate with this running object.
     */
    public void setJob(Job job);
}
