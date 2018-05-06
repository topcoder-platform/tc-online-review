/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling;


/**
 * <p>
 * This interface defines the contract to create the scheduled enable object
 * (instance of <code>ScheduledEnable</code>).
 * </p>
 * <p>
 * It is used by the Job class to create the running object when its job type is
 * set to <code>JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>.
 * </p>
 * <p>
 * Thread-safety: Implementations should be thread-safe.
 * </p>
 * @author Standlove, fuyun
 * @version 3.1
 * @since 3.1
 */
public interface ScheduledEnableObjectFactory {

    /**
     * <p>
     * Create the running object (instance of <code>ScheduledEnable</code>)
     * to return.
     * </p>
     * @throws ScheduledEnableObjectCreationException if fails to create the
     *             running object.
     * @return the created running object.
     */
    public ScheduledEnable createScheduledEnableObject()
        throws ScheduledEnableObjectCreationException;
}
