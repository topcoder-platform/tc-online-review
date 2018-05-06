/*
 * Copyright (C) 2010-2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.utility;

import java.io.File;
import java.io.IOException;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.configuration.persistence.ConfigurationParserException;
import com.topcoder.configuration.persistence.NamespaceConflictException;
import com.topcoder.configuration.persistence.UnrecognizedFileTypeException;
import com.topcoder.configuration.persistence.UnrecognizedNamespaceException;
import com.topcoder.management.deliverable.latetracker.Helper;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackerConfigurationException;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackingJobRunner;
import com.topcoder.management.deliverable.latetracker.notification.NotRespondedLateDeliverablesNotificationJobRunner;
import com.topcoder.util.commandline.ArgumentValidationException;
import com.topcoder.util.commandline.CommandLineUtility;
import com.topcoder.util.commandline.IllegalSwitchException;
import com.topcoder.util.commandline.IntegerValidator;
import com.topcoder.util.commandline.Switch;
import com.topcoder.util.commandline.UsageException;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.log.log4j.Log4jLogFactory;
import com.topcoder.util.scheduler.processor.JobProcessor;
import com.topcoder.util.scheduler.scheduling.ConfigurationException;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.Scheduler;
import com.topcoder.util.scheduler.scheduling.SchedulingException;
import com.topcoder.util.scheduler.scheduling.Second;
import com.topcoder.util.scheduler.scheduling.persistence.ConfigurationObjectScheduler;

import org.apache.log4j.PropertyConfigurator;

/**
 * <p>
 * This is the main class of the standalone command line application that performs periodical late deliverables
 * tracking. It uses <code>LateDeliverablesTrackingJobRunner</code> and
 * <code>NotRespondedLateDeliverablesNotificationJobRunner</code>, and schedules its repetitive execution with use
 * of Job Scheduling and Job Processor components. This utility reads a configuration from a file using Configuration
 * Persistence and Configuration API components. <code>LateDeliverablesTrackingUtility</code> performs the logging
 * of errors and debug information using Logging Wrapper.
 * </p>
 *
 * <p>
 * Version 1.0.1 (SVN Automation and Late Deliverables Tracker Integration Assembly Assembly 1.0) Change notes:
 * <ol>
 * <li>Updated the class so it could be launched and run in background thread and stopped if a guard file exists.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>"interval" switch was renamed to "trackingInterval".</li>
 * <li>Added support for "notificationInterval" switch.</li>
 * <li>Added support for scheduling not responded late deliverables notification job.</li>
 * </ol>
 * </p>
 *
 * <p>
 * Sample command line utility:<br>
 *
 * This command line can be used to print out the usage string:
 *
 * <pre>
 * java com.topcoder.management.deliverable.latetracker.utility.LateDeliverablesTrackingUtility
 * -help
 * </pre>
 *
 * If configuration for the utility is stored in the default namespace of the default configuration file, then the
 * application can be executed in background with the following arguments:
 *
 * <pre>
 * java com.topcoder.management.deliverable.latetracker.utility.LateDeliverablesTrackingUtility
 * -guardFile guard.tmp -background true
 * </pre>
 *
 * To use the custom configuration file the user can provide "-c" switch:
 *
 * <pre>
 * java com.topcoder.management.deliverable.latetracker.utility.LateDeliverablesTrackingUtility
 * -c custom_config.properties -guardFile guard.tmp -background true
 * </pre>
 *
 * The user can specify custom import files utility configuration file name and namespace:
 *
 * <pre>
 * java com.topcoder.management.deliverable.latetracker.utility.LateDeliverablesTrackingUtility
 * -c custom_config.properties -ns custom_namespace -guardFile guard.tmp -background true
 * </pre>
 *
 * The user can specify interval between late deliverable checks and interval between sending PM notifications in the
 * command line (in this example deliverables will be checked every 5 minutes, and notifications will be sent every
 * hour):
 *
 * <pre>
 * java com.topcoder.management.deliverable.latetracker.utility.LateDeliverablesTrackingUtility
 * -trackingInterval 300 -notificationInterval 3600 -guardFile guard.tmp -background true
 * </pre>
 *
 * Sample config: please refer CS 4.3.3.
 * </p>
 * <p>
 * Thread Safety: This class is immutable and thread safe. But it's not safe to execute multiple instances of
 * LateDeliverablesTrackingUtility command line application (configured to use the same persistence) at a time.
 * </p>
 *
 * @author saarixx, myxgyy, isv, sparemax
 * @version 1.3.2
 */
public class LateDeliverablesTrackingUtility {
    /**
     * <p>
     * Represents the signature of main() method used for logging.
     * </p>
     */
    private static final String MAIN_SIGNATURE = LateDeliverablesTrackingUtility.class.getName()
        + ".main(String[] args)";

    /**
     * <p>
     * Represents the default configuration file path.
     * </p>
     */
    private static final String DEFAULT_CONFIG_FILE = "com/topcoder/management/deliverable"
        + "/latetracker/utility/LateDeliverablesTrackingUtility.properties";

    /**
     * <p>
     * Represents the default configuration namespace.
     * </p>
     */
    private static final String DEFAULT_NAME_SPACE = "com.topcoder.management.deliverable.latetracker"
        + ".utility.LateDeliverablesTrackingUtility";

    /**
     * <p>
     * Represents switch name &quot;c&quot;.
     * </p>
     */
    private static final String CONFIG = "c";

    /**
     * <p>
     * Represents switch name &quot;ns&quot;.
     * </p>
     */
    private static final String NAMESPACE = "ns";

    /**
     * <p>
     * Represents switch name &quot;trackingInterval&quot;.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>"interval" switch was renamed to "trackingInterval".</li>
     * <li>"INTERVAL" was renamed to "TRACKING_INTERVAL".</li>
     * </ol>
     * </p>
     */
    private static final String TRACKING_INTERVAL = "trackingInterval";

    /**
     * <p>
     * Represents switch name &quot;notificationInterval&quot;.
     * </p>
     *
     * @since 1.2
     */
    private static final String NOTIFICATION_INTERVAL = "notificationInterval";

    /**
     * <p>
     * Represents switch name &quot;guardFile&quot;.
     * </p>
     */
    private static final String GUARD_FILE = "guardFile";

    /**
     * <p>
     * Represents switch name &quot;background&quot;.
     * </p>
     */
    private static final String BACKGROUND = "background";

    /**
     * <p>
     * Represents the usage of help.
     * </p>
     */
    private static final String HELP_USAGE = "        -? -h -help      print this help message";

    /**
     * <p>
     * Represents &quot;-?&quot; argument name.
     * </p>
     */
    private static final String HELP_ONE = "-?";

    /**
     * <p>
     * Represents &quot;-h&quot; argument name.
     * </p>
     */
    private static final String HELP_TWO = "-h";

    /**
     * <p>
     * Represents &quot;-help&quot; argument name.
     * </p>
     */
    private static final String HELP_THREE = "-help";

    /**
     * <p>
     * Represents the usage of config switch.
     * </p>
     */
    private static final String CONFIG_SWITCH_USAGE = "<file_name> Optional. Provides the"
        + " name of the configuration file for this command line application. This file is read with"
        + " use of Configuration Persistence component. Default is \"com/topcoder/management/"
        + "deliverable/latetracker/utility/LateDeliverablesTrackingUtility.properties\".";

    /**
     * <p>
     * Represents the usage of namespace switch.
     * </p>
     */
    private static final String NAMESPACE_SWITCH_USAGE = "<namespace> Optional. The"
        + " namespace in the specified configuration file that contains configuration for this"
        + " command line application. Default is \"com.topcoder.management.deliverable.latetracker"
        + ".utility. LateDeliverablesTrackingUtility\".";

    /**
     * <p>
     * Represents the usage of tracking interval switch.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>"INTERVAL_SWITCH_USAGE" was renamed to "TRACKING_INTERVAL_SWITCH_USAGE".</li>
     * </ol>
     * </p>
     */
    private static final String TRACKING_INTERVAL_SWITCH_USAGE = "<interval_in_sec> Optional. The"
        + " interval in seconds between checks of projects for late deliverables. If not specified,"
        + " the value from the scheduler configuration is used.";

    /**
     * <p>
     * Represents the usage of notification interval switch.
     * </p>
     *
     * @since 1.2
     */
    private static final String NOTIFICATION_INTERVAL_SWITCH_USAGE = "<interval_in_sec> Optional. The interval in"
        + " seconds between sending notifications about explained but not responded late deliverables to the"
        + " managers. If not specified, the value from the scheduler configuration is used.";

    /**
     * Represents the usage documentation for the guardFile switch.
     *
     * @since 1.0.1
     */
    private static final String GUARD_FILE_SWITCH_USAGE = "Specify the path to guard file which should be used to "
        + "signal to Late Deliverables Tracker that it has to stop";

    /**
     * Represents the usage documentation for the background switch.
     *
     * @since 1.0.1
     */
    private static final String BACKGROUND_SWITCH_USAGE = "Set the flag indicating whether the tracker is going to "
        + "run in background thread or not";

    /**
     * <p>
     * Represents the milliseconds of one day, 24x3600x1000.
     * </p>
     */
    private static final int ONE_DAY = 86400000;

    /**
     * <p>
     * Represents the milliseconds to sleep.
     * </p>
     */
    private static final int SLEEP_TIME = 2000;

    /**
     * <p>
     * This is a Command Line switch that is used to retrieve the configuration file.
     * </p>
     */
    private static Switch configSwitch;

    /**
     * <p>
     * This is a Command Line switch that is used to retrieve the namespace.
     * </p>
     */
    private static Switch namespaceSwitch;

    /**
     * <p>
     * This is a Command Line switch that is used to retrieve the tracking interval time.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>"intervalSwitch" was renamed to "trackingIntervalSwitch".</li>
     * </ol>
     * </p>
     */
    private static Switch trackingIntervalSwitch;

    /**
     * <p>
     * This is a Command Line switch that is used to retrieve the notification interval time.
     * </p>
     *
     * @since 1.2
     */
    private static Switch notificationIntervalSwitch;

    /**
     * <p>This is a Command Line switch that is used to retrieve the path to guard file.</p>
     *
     * @since 1.0.1
     */
    private static Switch guardFileSwitch;

    /**
     * <p>This is a Command Line switch that is used to retrieve the flag on running in background thread.</p>
     *
     * @since 1.0.1
     */
    private static Switch backgroundSwitch;

    /**
     * This is the command line utility which holds all switches.
     */
    private static CommandLineUtility commandLineUtility;

    /**
     * <p>
     * Represents &quot;trackingJobConfig&quot; child configuration key in configuration.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>"jobConfig" was renamed to "trackingJobConfig".</li>
     * <li>"JOB_CONFIG" was renamed to "TRACKING_JOB_CONFIG".</li>
     * </ol>
     * </p>
     */
    private static final String TRACKING_JOB_CONFIG = "trackingJobConfig";

    /**
     * <p>
     * Represents &quot;notificationJobConfig&quot; child configuration key in configuration.
     * </p>
     *
     * @since 1.2
     */
    private static final String NOTIFICATION_JOB_CONFIG = "notificationJobConfig";

    /**
     * <p>
     * Represents &quot;schedulerConfig&quot; child configuration key in configuration.
     * </p>
     */
    private static final String SCHEDULER_CONFIG = "schedulerConfig";

    /**
     * <p>
     * Represents &quot;jobName&quot; property key in configuration.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>"jobName" was renamed to "trackingJobName".</li>
     * <li>"JOB_NAME_KEY" was renamed to "TRACKING_JOB_NAME_KEY".</li>
     * </ol>
     * </p>
     */
    private static final String TRACKING_JOB_NAME_KEY = "trackingJobName";

    /**
     * <p>
     * Represents &quot;notificationJobName&quot; property key in configuration.
     * </p>
     *
     * @since 1.2
     */
    private static final String NOTIFICATION_JOB_NAME_KEY = "notificationJobName";

    /**
     * <p>
     * Represents &quot;log4jConfigFile&quot; property key in configuration.
     * </p>
     */
    public static final String LOG4J_CONFIG_FILE_KEY = "log4jConfigFile";

    /**
     * Empty private constructor.
     */
    private LateDeliverablesTrackingUtility() {
        // Empty
    }

    /**
     * <p>
     * This is the main method of the command line application that periodically performs a late deliverables
     * tracking.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>"interval" switch was renamed to "trackingInterval".</li>
     * <li>Added support for "notificationInterval" switch.</li>
     * <li>Added support for scheduling not responded late deliverables notification job.</li>
     * <li>Fixed a bug of incorrect overriding of scheduling interval specified in the configuration.</li>
     * </ol>
     * </p>
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();

        // create command line utility
        commandLineUtility = new CommandLineUtility();
        if (!parseArgs(commandLineUtility, args)) {
            return;
        }

        // get three values
        String configFileName = configSwitch.getValue();

        // configuration switch does not have value and default
        // configuration file does not exist
        if ((configFileName == null) && !doesDefaultConfigExist()) {
            System.out.println(commandLineUtility.getUsageString());
            System.out.println(HELP_USAGE);

            return;
        }

        if (configFileName == null) {
            configFileName = DEFAULT_CONFIG_FILE;
        }

        String namespace = namespaceSwitch.getValue();

        if (namespace == null) {
            namespace = DEFAULT_NAME_SPACE;
        }

        // UPDATED in 1.2
        Integer trackingInterval = getInterval(trackingIntervalSwitch);
        // NEW in 1.2
        Integer notificationInterval = getInterval(notificationIntervalSwitch);

        // Guard file
        String guardFileName = guardFileSwitch.getValue();
        File guardFile = new File(guardFileName);
        if (guardFile.exists()) {
            System.out.println("Guard file '" + guardFileName
                + "' exists. Late Deliverables Tracker exits immediately");
            return;
        }

        // Background flag
        String backgroundFlag = backgroundSwitch.getValue();
        boolean isBackground = "true".equalsIgnoreCase(backgroundFlag);

        ConfigurationObject config = loadConfiguration(configFileName, namespace);

        // Load log4j configuration
        String log4JConfigFile = Helper.getPropertyValue(config, LOG4J_CONFIG_FILE_KEY, false, false);
        if (log4JConfigFile != null) {
            PropertyConfigurator.configure(log4JConfigFile);
        }
        LogManager.setLogFactory(new Log4jLogFactory(true));

        // Get logger name from the configuration
        String loggerName = Helper.getPropertyValue(config, Helper.LOGGER_NAME_KEY, false, false);

        Log log = (loggerName != null) ? LogManager.getLog(loggerName) : LogManager.getLog();

        doTrack(config, trackingInterval, notificationInterval, log, guardFile, isBackground);
        Helper.logExit(log, MAIN_SIGNATURE, null, start);
    }

    /**
     * <p>
     * Gets the interval.
     * </p>
     *
     * @param intervalSwitch
     *            the switch.
     *
     * @return the interval.
     */
    private static Integer getInterval(Switch intervalSwitch) {
        String intervalStr = intervalSwitch.getValue();

        return (intervalStr == null) ? null : Integer.parseInt(intervalStr);
    }

    /**
     * <p>
     * Parse the arguments.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Updated codes for "trackingInterval".</li>
     * <li>Added support for "notificationInterval" switch.</li>
     * </ol>
     * </p>
     *
     * @param commandLineUtility
     *            the command line utility.
     * @param args
     *            the arguments.
     *
     * @return <code>false</code> if any error occurs; <code>true</code> otherwise.
     */
    private static boolean parseArgs(CommandLineUtility commandLineUtility, String[] args) {
        try {
            // create switches
            configSwitch = new Switch(CONFIG, false, 0, 1, null, CONFIG_SWITCH_USAGE);
            namespaceSwitch = new Switch(NAMESPACE, false, 0, 1, null, NAMESPACE_SWITCH_USAGE);
            trackingIntervalSwitch = new Switch(TRACKING_INTERVAL, false, 0, 1, new IntegerValidator(1, null),
                TRACKING_INTERVAL_SWITCH_USAGE);
            notificationIntervalSwitch = new Switch(NOTIFICATION_INTERVAL, false, 0, 1, new IntegerValidator(1, null),
                NOTIFICATION_INTERVAL_SWITCH_USAGE);
            guardFileSwitch = new Switch(GUARD_FILE, true, 1, 1, null, GUARD_FILE_SWITCH_USAGE);
            backgroundSwitch = new Switch(BACKGROUND, true, 1, 1, null, BACKGROUND_SWITCH_USAGE);
            // create command line utility
            commandLineUtility = new CommandLineUtility();
            commandLineUtility.addSwitch(configSwitch);
            commandLineUtility.addSwitch(trackingIntervalSwitch);
            commandLineUtility.addSwitch(notificationIntervalSwitch);
            commandLineUtility.addSwitch(namespaceSwitch);
            commandLineUtility.addSwitch(guardFileSwitch);
            commandLineUtility.addSwitch(backgroundSwitch);
        } catch (IllegalSwitchException e) {
            // never happens
        }

        // user request help
        for (String arg : args) {
            if (arg.equals(HELP_ONE) || arg.equals(HELP_TWO) || arg.equals(HELP_THREE)) {
                System.out.print(commandLineUtility.getUsageString());
                System.out.println(HELP_USAGE);

                return false;
            }
        }
        try {
            commandLineUtility.parse(args);

            return true;
        } catch (ArgumentValidationException e) {
            System.out.println("Fails to validate the value of interval arguments.");

            return false;
        } catch (UsageException e) {
            System.out.println("Fails to parse the arguments.");

            return false;
        }
    }

    /**
     * <p>
     * Creates job processor and start the tracking job.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>"interval" parameter was renamed to "trackingInterval".</li>
     * <li>Added parameter "notificationInterval".</li>
     * <li>Updated implementation.</li>
     * </ol>
     * </p>
     *
     * @param config
     *            the configuration.
     * @param trackingInterval
     *            the tracking interval.
     * @param notificationInterval
     *            the notification interval.
     * @param log
     *            the log to use.
     * @param guardFile
     *            a file to monitor for presence of.
     * @param background
     *            <code>true</code> if tracker is running in background thread; <code>false</code> otherwise.
     */
    private static void doTrack(ConfigurationObject config, Integer trackingInterval, Integer notificationInterval,
        Log log, File guardFile, boolean background) {
        try {
            ConfigurationObject schedulerConfig = Helper.getChildConfig(config, SCHEDULER_CONFIG);

            Scheduler scheduler = new ConfigurationObjectScheduler(schedulerConfig);

            // UPDATED in 1.2
            Job trackingJob = getJob(config, scheduler, TRACKING_JOB_NAME_KEY, log, MAIN_SIGNATURE);
            LateDeliverablesTrackingJobRunner.setConfig(Helper.getChildConfig(config, TRACKING_JOB_CONFIG));

            // NEW in 1.2
            Job notificationJob = getJob(config, scheduler, NOTIFICATION_JOB_NAME_KEY, log, MAIN_SIGNATURE);
            NotRespondedLateDeliverablesNotificationJobRunner.setConfig(
                Helper.getChildConfig(config, NOTIFICATION_JOB_CONFIG));

            // UPDATED in 1.2
            updateJob(scheduler, trackingJob, trackingInterval);
            // NEW in 1.2
            updateJob(scheduler, notificationJob, notificationInterval);

            JobProcessor jobProcessor = new JobProcessor(scheduler, ONE_DAY, log); // MOVED in 1.2
            jobProcessor.start();

            // Wait until the user presses Enter
            if (background) {
                while (!guardFile.exists()) {
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                log.log(Level.INFO,
                        "Got a signal to stop the entire Late Deliverables Tracker process by presence of file "
                        + guardFile);
            } else {
                System.out.println("Press Enter to terminate the late deliverables tracker...");
                System.in.read();
            }

            // Shutdown the job processor
            jobProcessor.shutdown();
        } catch (LateDeliverablesTrackerConfigurationException e) {
            printAndExit(log, e, "Configuration error occurred, error details : " + e.getMessage(),
                MAIN_SIGNATURE);
        } catch (IOException e) {
            printAndExit(log, e, "IO error occurred, error details : " + e.getMessage(), MAIN_SIGNATURE);
        } catch (ConfigurationException e) {
            printAndExit(log, e, "Configuration error occurred, error details : " + e.getMessage(),
                MAIN_SIGNATURE);
        } catch (SchedulingException e) {
            printAndExit(log, e, "Scheduling error occurred, error details : " + e.getMessage(),
                MAIN_SIGNATURE);
        } catch (NullPointerException e) {
            printAndExit(log, e, "Required configuration for job is missing.",
                MAIN_SIGNATURE);
        }
    }

    /**
     * <p>
     * Gets the job with the given key.
     * </p>
     *
     * @param config
     *            the configuration.
     * @param scheduler
     *            the scheduler.
     * @param jobNameKey
     *            the key.
     * @param log
     *            the log to use.
     * @param signature
     *            the method name.
     *
     * @return the job.
     *
     * @throws SchedulingException
     *             if the scheduler is unable to get this job from persistence.
     *
     * @since 1.2
     */
    private static Job getJob(ConfigurationObject config, Scheduler scheduler, String jobNameKey, Log log,
        String signature) throws SchedulingException {
        String jobName = Helper.getPropertyValue(config, jobNameKey, true, false);

        Job job = scheduler.getJob(jobName);
        if (job == null) {
            printAndExit(log, null, "Job with name[" + jobName + "] is not under config.", signature);
        }
        return job;
    }

    /**
     * <p>
     * Updates the job in the scheduler.
     * </p>
     *
     * @param scheduler
     *            the scheduler.
     * @param job
     *            the job.
     * @param interval
     *            the interval.
     *
     * @throws SchedulingException
     *             if the scheduler is unable to update this job in persistence.
     *
     * @since 1.2
     */
    private static void updateJob(Scheduler scheduler, Job job, Integer interval) throws SchedulingException {
        if (interval != null) {
            // Set execution interval value:
            job.setIntervalValue(interval); // NEW in 1.2
            // Set interval unit to second:
            job.setIntervalUnit(new Second()); // NEW in 1.2
            // Update job in the scheduler:
            scheduler.updateJob(job); // NEW in 1.2
        }
    }

    /**
     * Logs the exception and prints out the message exit JVM.
     *
     * @param log
     *            the log to use.
     * @param exception
     *            the exception to log.
     * @param message
     *            the message to print.
     * @param signature
     *            the method name.
     */
    private static void printAndExit(Log log, Exception exception, String message, String signature) {
        System.err.println(message);

        if (exception != null) {
            exception.printStackTrace();

            // Log Exception
            Helper.logException(log, signature, exception);
        }
        System.exit(1);
    }

    /**
     * Checks whether the default configuration file exists or not.
     *
     * @return true if default configuration file exists, false otherwise.
     */
    private static boolean doesDefaultConfigExist() {
        if (LateDeliverablesTrackingUtility.class.getClassLoader().getResource(DEFAULT_CONFIG_FILE) != null) {
            return true;
        }

        if (new File(DEFAULT_CONFIG_FILE).exists()) {
            return true;
        }

        return false;
    }

    /**
     * Loads configuration object from given file and namespace.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Prints out the detailed error explanation if any error occurs.</li>
     * </ol>
     * </p>
     *
     * @param configFileName
     *            the configuration file to load from.
     * @param namespace
     *            the namespace of the configuration object.
     *
     * @return the configuration object load from file.
     */
    private static ConfigurationObject loadConfiguration(String configFileName, String namespace) {
        ConfigurationObject config = null;

        try {
            ConfigurationFileManager manager = new ConfigurationFileManager(configFileName);
            config = manager.getConfiguration(namespace);

            config = Helper.getChildConfig(config, namespace);
        } catch (LateDeliverablesTrackerConfigurationException e) {
            printAndExit(null, e, "Configuration error occurred, error details : " + e.getMessage(), null);
        } catch (ConfigurationParserException e) {
            printAndExit(null, e, "Fails to parse the configuration file.", null);
        } catch (NamespaceConflictException e) {
            printAndExit(null, e, "Namespace conflict.", null);
        } catch (UnrecognizedFileTypeException e) {
            printAndExit(null, e, "Unrecognized file type of the config file.", null);
        } catch (IOException e) {
            printAndExit(null, e, "IO error when reading config file.", null);
        } catch (UnrecognizedNamespaceException e) {
            printAndExit(null, e, "Unrecognized namespace : " + namespace, null);
        }
        return config;
    }
}