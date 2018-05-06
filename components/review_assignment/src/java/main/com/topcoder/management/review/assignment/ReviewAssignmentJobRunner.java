/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import java.util.Date;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.errorhandling.ExceptionUtils;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.ScheduledJobRunner;

/**
 * <p>
 * This class is an implementation of ScheduledJobRunner that aggregates an instance of
 * ReviewAssignmentManager and can be used for scheduling the review assignment with use of Job Scheduling and
 * Job Processor components.
 * </p>
 * <p>
 * This job runner doesn't allow two jobs to be executed at the same time, thus if the previous job is not yet
 * finished, a new one is not started. This is done in order to avoid concurrency issues.
 * </p>
 * <p>
 * <b>Thread Safety:</b> This class is mutable, but it uses additional synchronization when accessing any
 * mutable attribute (except reviewAssignmentManager and log attributes that are assumed to be immutable after
 * initialization).
 * </p>
 * <p>
 * It's assumed that {@link #configure(ConfigurationObject)} method will be called just once right after
 * instantiation or static {@link #setConfig(ConfigurationObject)} method will be called instead, before
 * calling any business methods.
 * </p>
 * <p>
 * This class uses a not thread safe ReviewAssignmentManager instance, but it guarantees that it will be
 * accessed from one thread only at a time (but not allowing to run two simultaneous jobs).
 * </p>
 *
 * @author gevak, zhongqiangzhang
 * @version 1.0
 */
public class ReviewAssignmentJobRunner implements ScheduledJobRunner, Configurable {
    /**
     * <p>
     * Represents the name of this class used for logging.
     * </p>
     */
    private static final String CLASS_NAME = ReviewAssignmentJobRunner.class.getName();

    /**
     * <p>
     * The configuration object used to configure this class when executing from command line.
     * </p>
     * <p>
     * Can be any value. Is used in {@link #run()} and {@link #setConfig(ConfigurationObject)}.
     * </p>
     */
    private volatile static ConfigurationObject config;

    /**
     * <p>
     * The review assignment manager to be used by this job runner to perform actual review assignment
     * management.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be null
     * after initialization. Is used in {@link #run()}.
     * </p>
     */
    private ReviewAssignmentManager reviewAssignmentManager;

    /**
     * <p>
     * The job instance associated with this running object.
     * </p>
     * <p>
     * Cannot be null after initialization via the setter. It has getter and setter.
     * </p>
     */
    private Job job;

    /**
     * <p>
     * The status of this job.
     * </p>
     * <p>
     * Cannot be null or empty. It has getter. It is modified in {@link #run()} and {@link #close()} methods.
     * </p>
     */
    private String status = ScheduledJobRunner.NOT_STARTED;

    /**
     * <p>
     * The logger used by this class for logging errors and debug information.
     * </p>
     * <p>
     * Is initialized in the {@link #configure(ConfigurationObject)} method and never changed after that. If
     * is null after initialization, logging is not performed. Is used in {@link #run()} and {@link #close()}.
     * </p>
     */
    private Log log;

    /**
     * Creates an instance of <code>ReviewAssignmentJobRunner</code>.
     */
    public ReviewAssignmentJobRunner() {
    }

    /**
     * <p>
     * Configures this instance with use of the given configuration object.
     * </p>
     * <p>
     * See section 3.2 of CS for details about the configuration parameters.
     * </p>
     *
     * @param config
     *            the configuration object
     * @throws IllegalArgumentException
     *             if config is null.
     * @throws ReviewAssignmentConfigurationException
     *             if some error occurred when initializing an instance using the given configuration.
     */
    public void configure(ConfigurationObject config) {
        final String signature = CLASS_NAME + "#configure(ConfigurationObject)";
        ExceptionUtils.checkNull(config, null, null, "The parameter 'config' should not be null.");

        this.log = Helper.getLog(config);
        try {
            this.reviewAssignmentManager = new ReviewAssignmentManager(config);
        } catch (ReviewAssignmentConfigurationException e) {
            throw Helper.logException(log, signature, e);
        }
    }

    /**
     * Retrieves the job instance associated with this running object.
     *
     * @return the job instance associated with this running object
     */
    public Job getJob() {
        synchronized (this) {
            return job;
        }
    }

    /**
     * Sets the job instance associated with this running object.
     *
     * @param job
     *            the job instance associated with this running object
     *
     * @throws IllegalArgumentException
     *             if argument is null
     */
    public void setJob(Job job) {
        ExceptionUtils.checkNull(job, null, null, "The parameter 'job' should not be null.");
        synchronized (this) {
            this.job = job;
        }
    }

    /**
     * Retrieves the job message data. Not used in this implementation.
     *
     * @return an empty NodeList instance
     */
    public synchronized NodeList getMessageData() {
        return new NodeList(new Field[] {});
    }

    /**
     * Retrieves the job status.
     *
     * @return the status of the job
     */
    public String getRunningStatus() {
        synchronized (this) {
            return status;
        }
    }

    /**
     * <p>
     * Runs the job to perform the review assignment tracking.
     * </p>
     * <p>
     * This method should not throw any exceptions, instead it must log errors and set status to FAILED.
     * </p>
     *
     * @throws IllegalStateException
     *             if this class was not configured properly with use of configure() method
     *             (reviewAssignmentManager is null)
     */
    public void run() {
        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME + "#run()";

        if (reviewAssignmentManager == null && config != null) {
            configure(config);
        }

        Helper.logEntrance(log, signature, null, null);
        Helper.logInfo(log, "Start tracking at : " + new Date());

        Helper.checkState(reviewAssignmentManager, "reviewAssignmentManager", log, signature);

        synchronized (this) {
            if (status == ScheduledJobRunner.RUNNING) {
                log.log(Level.DEBUG, "Exiting the method [" + signature
                    + "] for previous job is still running.");
                return;
            }

            status = ScheduledJobRunner.RUNNING;
        }

        try {
            reviewAssignmentManager.execute();
        } catch (ReviewAssignmentException e) {
            Helper.logException(log, signature, e);
            synchronized (this) {
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
     * @return true is job is done, false otherwise
     */
    public boolean isDone() {
        synchronized (this) {
            return ((status == ScheduledJobRunner.SUCCESSFUL) || (status == ScheduledJobRunner.FAILED));
        }
    }

    /**
     * Closes the job.
     */
    public void close() {
        long start = System.currentTimeMillis();
        final String signature = CLASS_NAME + "#close()";
        Helper.logEntrance(log, signature, null, null);

        synchronized (this) {
            status = ScheduledJobRunner.NOT_STARTED;
        }

        Helper.logExit(log, signature, null, start);
    }

    /**
     * Retrieves the name of the job.
     *
     * @return the name of the job
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
     *            the name of the job
     *
     * @throws IllegalArgumentException
     *             if argument is null or empty
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
     * @return the status of the job
     */
    public String getStatus() {
        return getRunningStatus();
    }

    /**
     * Setter for the config field.
     *
     * @param config
     *            the config to set
     */
    public static void setConfig(ConfigurationObject config) {
        ReviewAssignmentJobRunner.config = config;
    }
}
