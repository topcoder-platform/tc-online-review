/*
 * Copyright (C) 2011-2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.notification;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.latetracker.Configurable;
import com.topcoder.management.deliverable.latetracker.Helper;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackerConfigurationException;
import com.topcoder.util.errorhandling.ExceptionUtils;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.ScheduledJobRunner;

/**
 * <p>
 * This class is an implementation of ScheduledJobRunner that aggregates an instance of
 * NotRespondedLateDeliverablesNotifier and can be used for scheduling the sending of notifications about all
 * explained but not responded late deliverables to the managers with use of Job Scheduling and Job Processor
 * components. This job runner doesn't allow two jobs to be executed at the same time, thus if the previous job is not
 * yet finished, a new one is not started.
 * </p>
 *
 * <p>
 * Thread Safety: This class is mutable, but it uses additional synchronization when accessing any mutable attribute
 * (except notRespondedLateDeliverableNotifier and log attributes that are assumed to be immutable after
 * initialization). It's assumed that configure() method will be called just once right after instantiation or static
 * setConfig() method will be called instead, before calling any business methods. This class uses a not thread safe
 * NotRespondedLateDeliverablesNotifier instance, but it guarantees that it will be accessed from one thread only at a
 * time (but not allowing to run two simultaneous jobs).
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.3.2
 * @since 1.2
 */
public class NotRespondedLateDeliverablesNotificationJobRunner implements ScheduledJobRunner, Configurable {
    /**
     * <p>
     * Represents the name of this class used for logging.
     * </p>
     */
    private static final String CLASS_NAME = NotRespondedLateDeliverablesNotificationJobRunner.class.getName();

    /**
     * <p>
     * The configuration object used to configure this class when executing from command line.
     * </p>
     *
     * <p>
     * It will be set in the main method of LateDeliverablesTrackingUtility. Can be any value. Is used in run() and
     * setConfig(ConfigurationObject).
     * </p>
     */
    private static volatile ConfigurationObject config;

    /**
     * <p>
     * Represents the object used for synchronization.
     * </p>
     */
    private final Object syncObj = new Object();

    /**
     * <p>
     * The notifier for not responded late deliverables to be used by this job runner.
     * </p>
     *
     * <p>
     * Is initialized in configure() and never changed after that. Cannot be null after initialization. Is used in
     * run().
     * </p>
     */
    private NotRespondedLateDeliverablesNotifier notRespondedLateDeliverableNotifier;

    /**
     * <p>
     * The job instance associated with this running object.
     * </p>
     *
     * <p>
     * Cannot be null after initialization via the setter. It has getter and setter.
     * </p>
     */
    private Job job;

    /**
     * <p>
     * The status of this job.
     * </p>
     *
     * <p>
     * Cannot be null or empty. It has getter. It is modified in run() and close() methods.
     * </p>
     */
    private String status = ScheduledJobRunner.NOT_STARTED;

    /**
     * <p>
     * The logger used by this class for logging errors and debug information.
     * </p>
     *
     * <p>
     * Is initialized in the configure() method and never changed after that. If is null after initialization, logging
     * is not performed. Is used in run() and close().
     * </p>
     */
    private Log log;

    /**
     * <p>
     * Creates an instance of NotRespondedLateDeliverablesNotificationJobRunner.
     * </p>
     */
    public NotRespondedLateDeliverablesNotificationJobRunner() {
        // Empty
    }

    /**
     * <p>
     * Configures this instance with use of the given configuration object.
     * </p>
     *
     * @param config
     *            the configuration object.
     *
     * @throws IllegalArgumentException
     *             if config is null.
     * @throws LateDeliverablesTrackerConfigurationException
     *             if some error occurred when initializing an instance using the given configuration.
     */
    public void configure(ConfigurationObject config) {
        ExceptionUtils.checkNull(config, null, null, "The parameter 'config' should not be null.");

        String loggerName = Helper.getPropertyValue(config, Helper.LOGGER_NAME_KEY, false, false);
        log = (loggerName == null) ? null : LogManager.getLog(loggerName);

        // Create not responded late deliverables notifier:
        notRespondedLateDeliverableNotifier = new NotRespondedLateDeliverablesNotifier(config);
    }

    /**
     * <p>
     * Gets the job instance associated with this running object.
     * </p>
     *
     * @return the job instance associated with this running object.
     */
    public Job getJob() {
        synchronized (syncObj) {
            return job;
        }
    }

    /**
     * <p>
     * Sets the job instance associated with this running object.
     * </p>
     *
     * @param job
     *            the job instance associated with this running object.
     *
     * @throws IllegalArgumentException
     *             if job is null.
     */
    public void setJob(Job job) {
        ExceptionUtils.checkNull(job, null, null, "The parameter 'job' should not be null.");

        synchronized (syncObj) {
            this.job = job;
        }
    }

    /**
     * <p>
     * Gets the job message data. Not used in this implementation. Returns an empty node list.
     * </p>
     *
     * @return an empty NodeList instance.
     */
    public NodeList getMessageData() {
        return new NodeList(new Field[] {});
    }

    /**
     * <p>
     * Gets the status of the job.
     * </p>
     *
     * @return the status of the job.
     */
    public String getRunningStatus() {
        synchronized (syncObj) {
            return status;
        }
    }

    /**
     * <p>
     * Gets the status of this job runner.
     * </p>
     *
     * @return the status of this job runner.
     */
    public String getStatus() {
        return getRunningStatus();
    }

    /**
     * <p>
     * Runs the job to send notifications for not responded late deliverables.
     * </p>
     *
     * <p>
     * This method will not throw any exceptions (except IllegalStateException for incorrect initialization), instead
     * it will log errors and set status to FAILED.
     * </p>
     *
     * @throws IllegalStateException
     *             if this class was not configured properly with use of configure() method
     *             (notRespondedLateDeliverableNotifier is null).
     */
    public void run() {
        long start = System.currentTimeMillis();
        final String signature = CLASS_NAME + ".run()";

        // Log method entry
        Helper.logEntrance(log, signature, null, null);

        try {
            if ((notRespondedLateDeliverableNotifier == null) && (config != null)) {
                configure(config);
            }
            Helper.checkState(notRespondedLateDeliverableNotifier, "notRespondedLateDeliverableNotifier", log,
                signature);

            synchronized (syncObj) {
                if (status == ScheduledJobRunner.RUNNING) {
                    // Log method exit
                    Helper.logExit(log, signature, null, start);
                    return;
                }
                status = ScheduledJobRunner.RUNNING;
            }
            // Execute a notification operation
            notRespondedLateDeliverableNotifier.execute();

            synchronized (syncObj) {
                status = ScheduledJobRunner.SUCCESSFUL;
            }
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Handle the exception
            handleException(e, log, signature);
        } catch (NotRespondedLateDeliverablesNotificationException e) {
            // Handle the exception
            handleException(e, log, signature);
        }

        // Log method exit
        Helper.logExit(log, signature, null, start);
    }

    /**
     * <p>
     * Handles the exception.
     * </p>
     *
     * @param e
     *            the exception.
     * @param log
     *            the log.
     * @param signature
     *            the signature.
     */
    private void handleException(Exception e, Log log, String signature) {
        synchronized (syncObj) {
            // Log exception
            Helper.logException(log, signature, e);

            status = ScheduledJobRunner.FAILED;
        }
    }

    /**
     * <p>
     * Gets the value that indicates whether the job is done.
     * </p>
     *
     * @return <code>true</code> is job is done; <code>false</code> otherwise.
     */
    public boolean isDone() {
        synchronized (syncObj) {
            return (status == ScheduledJobRunner.SUCCESSFUL || status == ScheduledJobRunner.FAILED);
        }
    }

    /**
     * <p>
     * Closes the job.
     * </p>
     */
    public void close() {
        long start = System.currentTimeMillis();
        final String signature = CLASS_NAME + ".close()";

        // Log method entry
        Helper.logEntrance(log, signature, null, null);

        synchronized (syncObj) {
            status = ScheduledJobRunner.NOT_STARTED;
        }

        // Log method exit
        Helper.logExit(log, signature, null, start);
    }

    /**
     * <p>
     * Gets the name of the job.
     * </p>
     *
     * @return the name of the job.
     */
    public String getJobName() {
        synchronized (syncObj) {
            return (job == null) ? null : job.getName();
        }
    }

    /**
     * <p>
     * Sets the the name of the job. Does nothing if job is not assigned.
     * </p>
     *
     * @param jobName
     *            the name of the job.
     *
     * @throws IllegalArgumentException
     *             if jobName is null or empty.
     */
    public void setJobName(String jobName) {
        ExceptionUtils.checkNullOrEmpty(jobName, null, null, "The parameter 'jobName' should not be null or empty.");

        synchronized (syncObj) {
            if (job != null) {
                job.setName(jobName);
            }
        }
    }

    /**
     * <p>
     * Sets the configuration object used to configure this class when executing from command line.
     * </p>
     *
     * @param config
     *            the configuration object used to configure this class when executing from command line.
     */
    public static void setConfig(ConfigurationObject config) {
        NotRespondedLateDeliverablesNotificationJobRunner.config = config;
    }
}
