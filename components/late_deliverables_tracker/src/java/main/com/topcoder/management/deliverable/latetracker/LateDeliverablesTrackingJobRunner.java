/*
 * Copyright (C) 2010-2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import com.topcoder.configuration.ConfigurationObject;

import com.topcoder.util.errorhandling.ExceptionUtils;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.ScheduledJobRunner;

/**
 * <p>
 * This class is an implementation of <code>ScheduledJobRunner</code> that aggregates an
 * instance of <code>LateDeliverablesTracker</code> and can be used for scheduling the
 * late deliverables tracking with use of Job Scheduling and Job Processor components.
 * This job runner doesn't allow two jobs to be executed at the same time, thus if the
 * previous job is not yet finished, a new one is not started.
 * </p>
 * <p>
 * Thread Safety: This class is mutable, but it uses additional synchronization when
 * accessing any mutable attribute (except lateDeliverablesTracker and log attributes that
 * are assumed to be immutable after initialization). It's assumed that {@link
 * #configure(ConfigurationObject)} method will be called just once right after
 * instantiation, before calling any business methods. This class uses a not thread safe
 * LateDeliverablesTracker instance, but it guarantees that it will be accessed from one
 * thread only at a time (but not allowing to run two simultaneous jobs).
 * </p>
 *
 * @author saarixx, myxgyy
 * @version 1.3.2
 */
public class LateDeliverablesTrackingJobRunner implements Configurable, ScheduledJobRunner {
    /**
     * <p>
     * The configuration object used to configure this class when executing from command line.
     * </p>
     * <p>
     * It will be set in the main method of <code>LateDeliverablesTrackingUtility</code>.
     * </p>
     * <p>
     * Is used in {@link #run()} and {@link #setConfig(ConfigurationObject)}.
     * </p>
     */
    private static volatile ConfigurationObject config;

    /**
     * <p>
     * Represents the name of this class used for logging.
     * </p>
     */
    private static final String CLASS_NAME = LateDeliverablesTrackingJobRunner.class.getName();

    /**
     * <p>
     * The late deliverables tracker to be used by this job runner.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after
     * that.
     * </p>
     * <p>
     * Cannot be <code>null</code> after initialization.
     * </p>
     * <p>
     * Is used in {@link #run()}.
     * </p>
     */
    private LateDeliverablesTracker lateDeliverablesTracker;

    /**
     * <p>
     * The job instance associated with this running object.
     * </p>
     * <p>
     * Cannot be <code>null</code> after initialization via the setter.
     * </p>
     * <p>
     * It has getter and setter.
     * </p>
     */
    private Job job;

    /**
     * <p>
     * The status of this job.
     * </p>
     * <p>
     * Cannot be <code>null</code> or empty.
     * </p>
     * <p>
     * It has getter. It is modified in {@link #run()} and {@link #close()} methods.
     * </p>
     */
    private String status = ScheduledJobRunner.NOT_STARTED;

    /**
     * <p>
     * The logger used by this class for logging errors and debug information.
     * </p>
     * <p>
     * Is initialized in the {@link #configure(ConfigurationObject)} method and never
     * changed after that.
     * </p>
     * <p>
     * If is null after initialization, logging is not performed.
     * </p>
     * <p>
     * Is used in {@link #run()} and {@link #close()}.
     * </p>
     */
    private Log log;

    /**
     * Creates an instance of <code>LateDeliverablesTrackingJobRunner</code>.
     */
    public LateDeliverablesTrackingJobRunner() {
    }

    /**
     * Configures this instance with use of the given configuration object.
     *
     * @param config
     *            the configuration object.
     * @throws IllegalArgumentException
     *             if <code>config</code> is <code>null</code>.
     * @throws LateDeliverablesTrackerConfigurationException
     *             if some error occurred when initializing an instance using the given
     *             configuration.
     */
    public void configure(ConfigurationObject config) {
        ExceptionUtils.checkNull(config, null, null, "The parameter 'config' should not be null.");

        String loggerName = Helper.getPropertyValue(config, Helper.LOGGER_NAME_KEY, false, false);
        this.log = (loggerName == null) ? null : LogManager.getLog(loggerName);
        lateDeliverablesTracker = new LateDeliverablesTracker(config);
    }

    /**
     * Retrieves the job instance associated with this running object.
     *
     * @return the job instance associated with this running object.
     */
    public Job getJob() {
        synchronized (this) {
            return job;
        }
    }

    /**
     *
     *
     * Sets the job instance associated with this running object.
     *
     * @param job
     *            the job instance associated with this running object.
     * @throws IllegalArgumentException
     *             if argument is null.
     */
    public void setJob(Job job) {
        ExceptionUtils.checkNull(job, null, null, "The parameter 'job' should not be null.");

        synchronized (this) {
            this.job = job;
        }
    }

    /**
     * Retrieves the job message data. Not used in this implementation. Returns an empty
     * node list.
     *
     * @return an empty <code>NodeList</code> instance.
     */
    public synchronized NodeList getMessageData() {
        return new NodeList(new Field[] {});
    }

    /**
     * Retrieves the job status.
     *
     * @return the status of the job.
     */
    public String getRunningStatus() {
        synchronized (this) {
            return status;
        }
    }

    /**
     * Runs the job to perform the late deliverables tracking.
     *
     * @throws IllegalStateException
     *             if this class was not configured properly with use of {@link
     *             #configure(ConfigurationObject)} method ({@link #lateDeliverablesTracker}
     *             is <code>null</code>).
     */
    public synchronized void run() {
        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME + ".run()";
        Helper.logEntrance(log, signature, null, null);

        if (lateDeliverablesTracker == null && config != null) {
            configure(config);
        }

        Helper.checkState(lateDeliverablesTracker, "lateDeliverablesTracker", log, signature);

        synchronized (this) {
            if (status == ScheduledJobRunner.RUNNING) {
                return;
            }

            status = ScheduledJobRunner.RUNNING;
        }

        try {
            lateDeliverablesTracker.execute();
        } catch (LateDeliverablesTrackingException e) {
            synchronized (this) {
                Helper.logException(log, signature, e);
                status = ScheduledJobRunner.FAILED;
                return;
            }
        }

        synchronized (this) {
            status = ScheduledJobRunner.SUCCESSFUL;
        }

        Helper.logExit(log, signature, null, start);
    }

    /**
     * Retrieves the value that indicates whether the job is done.
     *
     * @return true is job is done, false otherwise.
     */
    public synchronized boolean isDone() {
        synchronized (this) {
            return ((status == ScheduledJobRunner.SUCCESSFUL) || (status == ScheduledJobRunner.FAILED));
        }
    }

    /**
     * Closes the job.
     */
    public void close() {
        long start = System.currentTimeMillis();
        final String signature = CLASS_NAME + ".close()";
        Helper.logEntrance(log, signature, null, null);

        synchronized (this) {
            status = ScheduledJobRunner.NOT_STARTED;
        }

        Helper.logExit(log, signature, null, start);
    }

    /**
     * Retrieves the name of the job.
     *
     * @return the name of the job.
     */
    public String getJobName() {
        synchronized (this) {
            return (job == null) ? null : job.getName();
        }
    }

    /**
     * Sets the job name. Does nothing if job is not assigned.
     *
     * @param jobName
     *            the name of the job.
     * @throws IllegalArgumentException
     *             if argument is null or empty.
     */
    public void setJobName(String jobName) {
        ExceptionUtils.checkNullOrEmpty(jobName, null, null,
            "The parameter 'jobName' should not be null or empty.");

        synchronized (this) {
            if (job != null) {
                job.setName(jobName);
            }
        }
    }

    /**
     * Retrieves the status of this job runner.
     *
     * @return the status of the job.
     */
    public synchronized String getStatus() {
        return getRunningStatus();
    }

    /**
     * Setter for the {@link #config} field.
     *
     * @param config the config to set.
     */
    public static void setConfig(ConfigurationObject config) {
        LateDeliverablesTrackingJobRunner.config = config;
    }
}