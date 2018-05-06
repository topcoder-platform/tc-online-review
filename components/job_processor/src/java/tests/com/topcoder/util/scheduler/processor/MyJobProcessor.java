/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor;

import com.topcoder.util.log.Log;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.Scheduler;


/**
 * My JobProcessor used in test, providing the function to obtain the last job scheduled by scheduleJob().
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MyJobProcessor extends JobProcessor {
    /** The last scheduled job. */
    private Job job;

    /**
     * Creates a new MyJobProcessor object.
     *
     * @param scheduler scheduler
     * @param reloadInterval scheduler
     * @param log scheduler
     */
    public MyJobProcessor(Scheduler scheduler, int reloadInterval, Log log) {
        super(scheduler, reloadInterval, log);
    }

    /**
     * Gets the last scheduled job.
     *
     * @return the last scheduled job
     */
    public Job getScheduledJob() {
        return job;
    }

    /**
     * Schedules the job.
     *
     * @param job job
     */
    void scheduleJob(Job job) {
        this.job = job;
    }
}
