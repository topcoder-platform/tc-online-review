/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.utility;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.configuration.persistence.ConfigurationParserException;
import com.topcoder.configuration.persistence.NamespaceConflictException;
import com.topcoder.configuration.persistence.UnrecognizedFileTypeException;
import com.topcoder.configuration.persistence.UnrecognizedNamespaceException;
import com.topcoder.management.review.assignment.Helper;
import com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException;
import com.topcoder.management.review.assignment.ReviewAssignmentJobRunner;
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

/**
 * <p>
 * This is the main class of the standalone command line application that performs periodical review
 * assignment. It uses ReviewAssignmentJobRunner, and schedules its repetitive execution with use of Job
 * Scheduling and Job Processor components.
 * </p>
 * <p>
 * This utility reads a configuration from a file using Configuration Persistence and Configuration API
 * components. ReviewAssignmentUtility performs the logging of errors and debug information using Logging
 * Wrapper.
 * </p>
 * <p>
 * Sample command line utility:<br>
 *
 * This command line can be used to print out the usage string:
 *
 * <pre>
 * java com.topcoder.management.review.assignment.utility.ReviewAssignmentUtility
 * -help
 * </pre>
 *
 * If configuration for the utility is stored in the default namespace of the default configuration file, then
 * the application can be executed in background with the following arguments:
 *
 * <pre>
 * java com.topcoder.management.review.assignment.utility.ReviewAssignmentUtility
 * -guardFile guard.tmp -background true
 * </pre>
 *
 * To use the custom configuration file the user can provide "-c" switch:
 *
 * <pre>
 * java com.topcoder.management.review.assignment.utility.ReviewAssignmentUtility
 * -c custom_config.properties -guardFile guard.tmp -background true
 * </pre>
 *
 * The user can specify custom import files utility configuration file name and namespace:
 *
 * <pre>
 * java com.topcoder.management.review.assignment.utility.ReviewAssignmentUtility
 * -c custom_config.properties -ns custom_namespace -guardFile guard.tmp -background true
 * </pre>
 *
 * The user can specify interval between review assignment in the command line:
 *
 * <pre>
 * java com.topcoder.management.review.assignment.utility.ReviewAssignmentUtility
 * -trackingInterval 300 -guardFile guard.tmp -background true
 * </pre>
 *
 * Sample config: please refer CS 4.3.
 * </p>
 * <p>
 * Thread Safety: This class is immutable and thread safe. But it's not safe to execute multiple instances of
 * ReviewAssignmentUtility command line application (configured to use the same persistence) at a time.
 * </p>
 *
 * @author gevak, zhongqiangzhang
 * @version 1.0
 */
public class ReviewAssignmentUtility {
    /**
     * <p>
     * Represents the signature of main() method used for logging.
     * </p>
     */
    private static final String MAIN_SIGNATURE = ReviewAssignmentUtility.class.getName()
        + ".main(String[] args)";

    /**
     * <p>
     * Represents the default configuration file path.
     * </p>
     */
    private static final String DEFAULT_CONFIG_FILE = "com/topcoder/management/review/"
        + "assignment/utility/ReviewAssignmentUtility.properties";

    /**
     * <p>
     * Represents the default configuration namespace.
     * </p>
     */
    private static final String DEFAULT_NAME_SPACE = "com.topcoder.management.review.assignment"
        + ".utility.ReviewAssignmentUtility";

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
     */
    private static final String TRACKING_INTERVAL = "trackingInterval";

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
        + "review/assignment/utility/ReviewAssignmentUtility.properties\".";

    /**
     * <p>
     * Represents the usage of namespace switch.
     * </p>
     */
    private static final String NAMESPACE_SWITCH_USAGE = "<namespace> Optional. The"
        + " namespace in the specified configuration file that contains configuration for this"
        + " command line application. Default is \"com.topcoder.management.review.assignment"
        + ".utility.ReviewAssignmentUtility\".";

    /**
     * <p>
     * Represents the usage of tracking interval switch.
     * </p>
     */
    private static final String TRACKING_INTERVAL_SWITCH_USAGE = "<interval_in_sec> Optional. The"
        + " interval in seconds between checks of projects for review assignment. If not specified,"
        + " the value from the scheduler configuration is used.";

    /**
     * Represents the usage documentation for the guardFile switch.
     */
    private static final String GUARD_FILE_SWITCH_USAGE = "Specify the path to guard file which should be used to "
        + "signal to Review Assignment that it has to stop";

    /**
     * Represents the usage documentation for the background switch.
     */
    private static final String BACKGROUND_SWITCH_USAGE = "Set the flag indicating whether the review assignment "
        + "is going to run in background thread or not";

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
     */
    private static Switch trackingIntervalSwitch;

    /**
     * <p>
     * This is a Command Line switch that is used to retrieve the path to guard file.
     * </p>
     */
    private static Switch guardFileSwitch;

    /**
     * <p>
     * This is a Command Line switch that is used to retrieve the flag on running in background thread.
     * </p>
     */
    private static Switch backgroundSwitch;

    /**
     * This is the command line utility which holds all switches.
     */
    private static CommandLineUtility commandLineUtility;

    /**
     * <p>
     * Represents &quot;schedulerConfig&quot; child configuration key in configuration.
     * </p>
     */
    private static final String SCHEDULER_CONFIG = "schedulerConfig";

    /**
     * <p>
     * Represents &quot;jobConfig&quot; child configuration key in configuration.
     * </p>
     */
    private static final String JOB_CONFIG = "jobConfig";

    /**
     * <p>
     * Represents &quot;jobName&quot; child configuration key in configuration.
     * </p>
     */
    private static final String JOB_NAME = "jobName";

    /**
     * <p>
     * Represents &quot;log4jConfigFile&quot; property key in configuration.
     * </p>
     */
    public static final String LOG4J_CONFIG_FILE_KEY = "log4jConfigFile";

    /**
     * Empty private constructor.
     */
    private ReviewAssignmentUtility() {
    }

    /**
     * <p>
     * This is the main method of the command line application that periodically performs review assignment.
     * It uses CommandLineUtility class to parse command line arguments.
     * </p>
     * <p>
     * It's assumed that this method will never be used programmatic thus argument checking is not required.
     * </p>
     * <p>
     * See CS 3.2 for details about the configuration parameters used in this method. Please use value
     * constraints provided in that section to check whether values read from configuration object are valid.
     * </p>
     * <p>
     * This method MUST NOT throw any exception. Instead it must print out the detailed error explanation to
     * the standard error output and terminate.
     * </p>
     * <p>
     *
     * @param args
     *            the command line arguments
     *
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

        // configuration switch does not have value and default configuration file does not exist
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

        try {
            Integer trackingInterval = getInterval(trackingIntervalSwitch);

            // Guard file
            String guardFileName = guardFileSwitch.getValue();
            File guardFile = new File(guardFileName);
            if (guardFile.exists()) {
                System.out.println("Guard file '" + guardFileName
                    + "' exists. Review assignment utility exits immediately");
                return;
            }

            // Background flag
            String backgroundFlag = backgroundSwitch.getValue();

            boolean isBackground = "true".equalsIgnoreCase(backgroundFlag);
            if (!isBackground && !"false".equalsIgnoreCase(backgroundFlag)) {
                System.out.println("isBackground setting should true or false.");
                return;
            }

            ConfigurationObject config = loadConfiguration(configFileName, namespace);

            // Load log4j configuration
            String log4JConfigFile = Helper.getPropertyValue(config, LOG4J_CONFIG_FILE_KEY, false);
            if (log4JConfigFile != null) {
                PropertyConfigurator.configure(log4JConfigFile);
            }
            LogManager.setLogFactory(new Log4jLogFactory(true));

            // Create Log.
            Log log = Helper.getLog(config);

            doTrack(config, trackingInterval, log, guardFile, isBackground);
            Helper.logExit(log, MAIN_SIGNATURE, null, start);
        } catch (NumberFormatException e) {
            System.out.println("trackingInterval is not int type for " + e.getMessage());
        } catch (ReviewAssignmentConfigurationException e) {
            System.out.println("Configuration error for " + e.getMessage());
        }
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
     *
     * @throws NumberFormatException
     *             if the string does not contain a parsable integer.
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
            trackingIntervalSwitch = new Switch(TRACKING_INTERVAL, false, 0, 1,
                new IntegerValidator(1, null), TRACKING_INTERVAL_SWITCH_USAGE);

            guardFileSwitch = new Switch(GUARD_FILE, true, 1, 1, null, GUARD_FILE_SWITCH_USAGE);
            backgroundSwitch = new Switch(BACKGROUND, true, 1, 1, null, BACKGROUND_SWITCH_USAGE);
            // create command line utility
            commandLineUtility.addSwitch(configSwitch);
            commandLineUtility.addSwitch(trackingIntervalSwitch);
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
            System.out.println("Fails to validate the value of interval arguments caused by "
                + e.getMessage());

            return false;
        } catch (UsageException e) {
            System.out.println("Fails to parse the arguments caused by " + e.getMessage());

            return false;
        }
    }

    /**
     * <p>
     * Creates job processor and start the tracking job.
     * </p>
     *
     * @param config
     *            the configuration.
     * @param trackingInterval
     *            the tracking interval.
     * @param log
     *            the log to use.
     * @param guardFile
     *            a file to monitor for presence of.
     * @param background
     *            <code>true</code> if job is running in background thread; <code>false</code> otherwise.
     */
    private static void doTrack(ConfigurationObject config, Integer trackingInterval, Log log,
        File guardFile, boolean background) {
        try {
            ConfigurationObject schedulerConfig = Helper.getChildConfig(config, SCHEDULER_CONFIG);

            Scheduler scheduler = new ConfigurationObjectScheduler(schedulerConfig);

            Job trackingJob = getJob(config, scheduler, JOB_NAME, log, MAIN_SIGNATURE);
            ReviewAssignmentJobRunner.setConfig(Helper.getChildConfig(config, JOB_CONFIG));

            if (trackingInterval != null) {
                // Set execution interval value:
                trackingJob.setIntervalValue(trackingInterval);
                // Set interval unit to second
                trackingJob.setIntervalUnit(new Second());
                // Update job in the scheduler
                scheduler.updateJob(trackingJob);
            }

            JobProcessor jobProcessor = new JobProcessor(scheduler, ONE_DAY, log);
            jobProcessor.setExecuteInternal(trackingInterval);
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
                    "Got a signal to stop the entire review assignment utility process by presence of file "
                        + guardFile);
            } else {
                System.out.println("Press Enter to terminate the review assignment utility...");
                System.in.read();
            }

            // Shutdown the job processor
            jobProcessor.shutdown();
        } catch (ReviewAssignmentConfigurationException e) {
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
            printAndExit(log, e, "Required configuration for job is missing.", MAIN_SIGNATURE);
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
     */
    private static Job getJob(ConfigurationObject config, Scheduler scheduler, String jobNameKey, Log log,
        String signature) throws SchedulingException {
        String jobName = Helper.getPropertyValue(config, jobNameKey, true);

        Job job = scheduler.getJob(jobName);
        if (job == null) {
            printAndExit(log, null, "Job with name [" + jobName + "] is not under config.", signature);
        }
        return job;
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
        if (ReviewAssignmentUtility.class.getClassLoader().getResource(DEFAULT_CONFIG_FILE) != null) {
            return true;
        }

        return new File(DEFAULT_CONFIG_FILE).exists();

    }

    /**
     * Loads configuration object from given file and namespace.
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
        } catch (ReviewAssignmentConfigurationException e) {
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
