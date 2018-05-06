/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import com.topcoder.management.phase.autopilot.logging.LogMessage;
import com.topcoder.util.commandline.ArgumentValidationException;
import com.topcoder.util.commandline.CommandLineUtility;
import com.topcoder.util.commandline.IllegalSwitchException;
import com.topcoder.util.commandline.IntegerValidator;
import com.topcoder.util.commandline.Switch;
import com.topcoder.util.commandline.UsageException;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.log.log4j.Log4jLogFactory;
import com.topcoder.util.scheduler.Job;
import com.topcoder.util.scheduler.JobActionException;
import com.topcoder.util.scheduler.Scheduler;

import org.apache.log4j.PropertyConfigurator;

/**
 * <p>
 * This class  provides the command line entry for the AutoPilot tool. See {@link #main(String[])}
 * for more detailed doc.
 * </p>
 *
 * <p>
 * Version 1.0.1 (Online Review Build and Deploy Scripts 2 Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added support for <code>guardFile</code> configuration property.</li>
 *   </ol>
 * </p>
 *
 * @author sindu, abelli, VolodymyrK
 * @version 1.0.2
 */
public class AutoPilotUtility  {

    /**
     * <p>Defines the property key in the config manager that must contain the log4j config file name.</p>
     *
     * @since 1.0.2
     */
    private static final String CONFIG_LOG4J_CONFIG_FILE_KEY = "log4jConfigFile";

    /**
     * <p>
     * A static scheduler instance used to schedule AutoPilotJob. It's lazily initialized in
     * schedule and immutable afterwards. It can be retrieved with the getter.
     * </p>
     */
    private static Scheduler scheduler;

    /**
     * <p>A <code>Thread</code> running the <code>AutoPilotJobStopper</code>.</p>
     *
     * @since 1.0.1
     */
    private static Thread stopper;

    /**
     * <p>This is a Command Line switch that is used to retrieve the poll interval in seconds.</p>
     *
     * @since 1.0.2
     */
    private static Switch pollSwitch;

    /**
     * <p>This is a Command Line switch that is used to retrieve the list of project IDs to be processed.</p>
     *
     * @since 1.0.2
     */
    private static Switch projectSwitch;

    /**
     * <p>
     * A main static method to provide command line functionality to the component. Basically, the
     * command line provides 2 functionalities:<br>
     * <br> - run once (given a list of project ids or retrieve ids from AutoPilotSource)<br>
     * <br> - poll mode (scheduled in background to run Auto Pilot every some intervals)<br>
     * <br>
     * The command line syntax is:<br>
     * java AutoPilotJob [-config configFile] [-namespace ns [-autopilot apKey]] (-poll [interval]
     * [-jobname jobname] | -project [Id[, ...]])<br>
     * <br> - configFile specifies the path to config files to be loaded into configuration manager.
     * if not specified it's assumed the config file is preloaded<br>
     * <br> - ns and apKey are optional, it's used to instantiate AutoPilotJob for project mode. ns
     * is also used to instantiate the Scheduler. The default values are AutoPilotJob's full name &
     * AutoPilot's full name respectively.<br>
     * <br> - poll or project These next options are mutually exclusive (to indicate two kinds of
     * run-mode): Both poll/projects are specified, or none are specified is not allowed.<br>
     * <br>
     * A) Poll-mode<br>
     * <br> - poll is used to define the interval in seconds, if interval is not specified, a
     * default of 60 seconds is used. The autopilot job will be executed every this interval starting
     * from midnight.<br>
     * <br> - jobname is the job name, Job Scheduling will use this job name. The default value is
     * 'AutoPilotJob'. It is optional and can only be specified if poll is specified.<br>
     * <br> - ns is used to instantiate the Scheduler. Optional. The default value is AutoPilotJob's full
     * name.<br>
     * <br> - apKey is ignored.<br>
     * <br>
     * <br>
     * B) Project mode<br>
     * <br> - project can be specified to process projects with the given ids. The project ids will
     * be processed once and the application terminates, it doesn't go into poll mode. If no ids are
     * given, AutoPilotSource is used instead.<br>
     * <br> - ns and apKey, must be specified or not at the same time, default to AutoPilotJob's
     * full name and AutoPilot's full name respectively.<br>
     * <br>
     * </p>
     * @param args the command line arguments
     * @throws IllegalArgumentException if argument is invalid, i.e.
     *             <ul>
     *             <li>specifying namespace without apKey (and vice versa) for project mode,</li>
     *             <li>specifying both poll/project, or no poll/project is given,</li>
     *             <li>specifying jobname without poll,</li>
     *             <li>poll interval cannot be converted to long</li>
     *             <li>poll interval &lt;= 0,</li>
     *             <li>project id cannot be converted to long</li>
     *             <li><code>IllegalSwitchException</code> occurs</li>
     *             <li><code>ArgumentValidationException</code> occurs</li>
     *             <li><code>UsageException</code> occurs</li>
     *             </ul>
     * @throws ConfigurationException if any error occurs loading config file or instantiating the
     *             AutoPilotJob or while configuring the job scheduler
     * @throws AutoPilotSourceException if any error occurs retrieving project ids from
     *             AutoPilotSource
     * @throws PhaseOperationException if any error occurs while ending/starting a phase
     * @throws RuntimeException if runtime exceptions occurs while executing the command line.
     */
    public static void main(String[] args) throws ConfigurationException, AutoPilotSourceException,
        PhaseOperationException {
        if (null == args) {
            throw new IllegalArgumentException("args cannot be null");
        }

        // Build command line parser.
        CommandLineUtility clu = new CommandLineUtility();
        if (args.length < 1 || !parseArgs(clu, args)) {
            showUsage();
            return;
        }

        String namespace = clu.getSwitch("namespace").getValue();
        try {
            // Get config file. Assume pre-loaded if not specified.
            String configFile = clu.getSwitch("config").getValue();
            if (configFile != null) {
                ConfigManager cfg = ConfigManager.getInstance();
                cfg.add(configFile);
            }

            // Load log4j configuration
            String log4JConfigFile = ConfigManager.getInstance().getString(namespace, CONFIG_LOG4J_CONFIG_FILE_KEY);
            if (log4JConfigFile != null && log4JConfigFile.trim().length() > 0) {
                PropertyConfigurator.configure(log4JConfigFile);
            }
            LogManager.setLogFactory(new Log4jLogFactory(true));
        } catch (ConfigManagerException e) {
            System.out.println("fail to load namespace cause of config manager exception:"
                    + "\n" + LogMessage.getExceptionStackTrace(e));
            throw new ConfigurationException("fail to load namespace cause of config manager exception", e);
        }

        List validSwitches = clu.getValidSwitches();
        Log log = LogManager.getLog("AutoPilot");

        try {
            // One of project and poll can exist.
            if (validSwitches.contains(pollSwitch)
                && validSwitches.contains(projectSwitch)) {
                // Both poll and project exists.
                showUsage();
                throw new IllegalArgumentException("either project or poll can exist");

            } else if (!validSwitches.contains(pollSwitch)
                && !validSwitches.contains(projectSwitch)) {
                // Neither poll nor project exists.
                showUsage();
                throw new IllegalArgumentException("either project or poll must exist");

            } else if (validSwitches.contains(projectSwitch)) {
                // Deal with project mode.
                dealProject(clu, namespace);

            } else if (validSwitches.contains(pollSwitch)) {
                // Deal with poll mode.
                dealPoll(clu, namespace, log);
            }
        } catch (RuntimeException t) {
        	log.log(Level.ERROR, "fail to build command line cause of illegal switch:"
        			+ "\n" + LogMessage.getExceptionStackTrace(t));
            showUsage();
            throw t;
        }
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
            pollSwitch = new Switch("poll", false, 0, 1, new IntegerValidator(), "Poll interval in seconds");
            projectSwitch = new Switch("project", false, 0, -1, new IntegerValidator(), "Project id");
            Switch namespaceSwitch = new Switch("namespace", false, 1, 1, null, "Namespace configuration");
            Switch configSwitch = new Switch("config", false, 1, 1, null, "The configuration file to load");
            Switch autoPilotSwitch = new Switch("autopilot", false, 1, 1, null, "AutoPilot key");
            Switch jobNameSwitch = new Switch("jobname", false, 1, 1, null, "Job name");
            
            commandLineUtility.addSwitch(configSwitch);
            commandLineUtility.addSwitch(namespaceSwitch);
            commandLineUtility.addSwitch(autoPilotSwitch);
            commandLineUtility.addSwitch(pollSwitch);
            commandLineUtility.addSwitch(jobNameSwitch);
            commandLineUtility.addSwitch(projectSwitch);
        } catch (IllegalSwitchException e) {
            // never happens
        }

        try {
            commandLineUtility.parse(args);            
            return true;
        } catch (ArgumentValidationException e) {
            System.out.println("Argument validation fails.");
            return false;
        } catch (UsageException e) {
            System.out.println("Fails to parse the arguments.");
            return false;
        }
    }

    /**
     * <p>
     * Create IAE with the message, and init the cause.
     * </p>
     * @param message the message.
     * @param t the cause.
     * @return the IllegalArgumentException.
     */
    private static IllegalArgumentException createIAE(String message, Throwable t) {
        showUsage();

        IllegalArgumentException ex = new IllegalArgumentException(message);
        ex.initCause(t);
        return ex;
    }

    /**
     * <p>
     * Deal with poll mode.
     * </p>
     * @param clu the parsed command line utility.
     * @param namespace the namespace value.
     * @param log the log to use.
     * @throws ConfigurationException - if there is configuration exceptions.
     */
    private static void dealPoll(CommandLineUtility clu, String namespace, Log log)
        throws ConfigurationException {
        // Use 60 seconds if interval not specified.
        int interval = 60;
        String poll = clu.getSwitch("poll").getValue();
        if (null != poll) {
            interval = Integer.parseInt(poll);
        }

        // Get job name and schedule.
        String jobName = clu.getSwitch("jobname").getValue();
        schedule((null == namespace) ? AutoPilotJob.class.getName() : namespace,
            (null == jobName) ? "AutoPilotJob" : jobName, interval, log);
        scheduler.start();
    }

    /**
     * <p>
     * Deal with project mode.
     * </p>
     * @param clu the parsed command line utility.
     * @param namespace the namespace value
     * @throws IllegalArgumentException - if jobname is specified, or namespace specified without
     *             apKey (and vice versa)
     * @throws ConfigurationException - if there is configuration exceptions.
     * @throws AutoPilotSourceException - if there is auto pilot source exceptions.
     * @throws PhaseOperationException - if there is phase operation exceptions.
     */
    private static void dealProject(CommandLineUtility clu, String namespace)
        throws ConfigurationException, AutoPilotSourceException, PhaseOperationException {
        if (null != clu.getSwitch("jobname").getValue()) {
            throw new IllegalArgumentException("jobname cannot be specified for project mode");
        }

        String autoPilot = clu.getSwitch("autopilot").getValue();

        // namespace and autopilot can exist both or neither.
        if ((null == namespace && null != autoPilot)
            || (null != namespace && null == autoPilot)) {
            throw new IllegalArgumentException("ns and apKey cannot be specified only one");
        }

        // Parse project Ids.
        List ids = clu.getSwitch("project").getValues();
        long[] projectId = null;
        if (null != ids && !ids.isEmpty()) {
            projectId = new long[ids.size()];
            int i = -1;
            for (Iterator it = ids.iterator(); it.hasNext();) {
                projectId[++i] = Long.parseLong((String) it.next());
            }
        }

        // Create AutoPilotJob instance.
        AutoPilotJob autoPilotJob = (null == namespace && null == autoPilot) ? new AutoPilotJob()
            : new AutoPilotJob(namespace, autoPilot);

        // run and print the result.
        AutoPilotResult[] result;
        result = (null == projectId) ? autoPilotJob.execute() : autoPilotJob.run(projectId);
        printResult(result);
    }

    /**
     * <p>
     * Show usage of this command line tool.
     * </p>
     */
    private static void showUsage() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                AutoPilotJob.class.getResourceAsStream("usage")));
            String ln = br.readLine();
            while (ln != null) {
                System.err.println(ln);
                ln = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Cannot find usage file!");
        }
    }

    /**
     * <p>
     * Print the auto pilot result.
     * </p>
     * @param result result array.
     */
    private static void printResult(AutoPilotResult[] result) {
        System.out.println("|      ProjectId      |    Ended    |    Started    |");
        final char[] space1 = "                                                    ".toCharArray();
        for (int i = 0; i < result.length; i++) {
            StringBuffer buf = new StringBuffer();

            // Print project Id.
            System.out.print('|');
            String strId = String.valueOf(result[i].getProjectId());
            buf.append(space1, 0, (21 - strId.length()) / 2);
            buf.append(strId);
            buf.append(space1, buf.length(), 21 - buf.length());
            System.out.print(buf);

            // Print ended.
            System.out.print('|');
            buf.delete(0, buf.length());
            String strEnd = String.valueOf(result[i].getPhaseEndedCount());
            buf.append(space1, 0, (13 - strEnd.length()) / 2);
            buf.append(strEnd);
            buf.append(space1, buf.length(), 13 - buf.length());
            System.out.print(buf);

            // Print started.
            System.out.print('|');
            buf.delete(0, buf.length());
            String strStart = String.valueOf(result[i].getPhaseStartedCount());
            buf.append(space1, 0, (15 - strEnd.length()) / 2);
            buf.append(strStart);
            buf.append(space1, buf.length(), 15 - buf.length());
            System.out.print(buf);

            System.out.println('|');
        }
    }

    /**
     * <p>
     * Returns an AutoPilotJob job to be scheduled using Job Scheduling component. The job is
     * created with the given name, starting at midnight and executing every interval seconds until
     * forever.
     * </p>
     * @param jobName the job name
     * @param interval the interval (in seconds)
     * @return a Job to run AutoPilotJob starting at midnight today at every interval seconds
     * @throws IllegalArgumentException if jobName is null or an empty (trimmed) string or interval <=
     *             0
     */
    public static Job createJob(String jobName, int interval) {
        // Check arguments.
        if (null == jobName) {
            throw new IllegalArgumentException("jobName cannot be null");
        }
        if (jobName.trim().length() < 1) {
            throw new IllegalArgumentException("jobName cannot be empty");
        }
        if (interval < 1) {
            throw new IllegalArgumentException("interval cannot be 0 or negative:" + interval);
        }

        GregorianCalendar start = new GregorianCalendar();
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return new Job(jobName, start, null, interval, Calendar.SECOND,
            Scheduler.JOB_TYPE_JAVA_CLASS, AutoPilotJob.class.getName());
    }

    /**
     * <p>
     * Schedules a job using the static scheduler. The scheduler's Timer will prevent application
     * from exiting until it's closed. This will lazily instantiate the Scheduler (if one hasn't
     * been instantiated), then it'll add the AutoPilotJob to the scheduler and start it. The
     * scheduler instantiation is synchronized so that multiple thread trying to schedule jobs will
     * only instantiate one scheduler instance.<br>
     * Cause job name is unique in the scheduler, the job with the same name will be replaced with
     * auto pilot job if there exists the job with the same name. Otherwise the auto pilot job will
     * be added.
     * </p>
     * @param namespace the namespace to configure scheduler with
     * @param jobName the job name
     * @param interval the interval in seconds
     * @param log the log to use.
     * @throws IllegalArgumentException if string parameters are null or empty (trimmed) string, or
     *             interval is non-positive.
     * @throws ConfigurationException if an error occurs configuring the Scheduler
     */
    public static void schedule(String namespace, String jobName, int interval, Log log)
        throws ConfigurationException {
        // Check arguments.
        if (null == namespace) {
            throw new IllegalArgumentException("namespace cannot be null");
        }
        if (namespace.trim().length() < 1) {
            throw new IllegalArgumentException("namespace cannot be empty");
        }
        if (null == jobName) {
            throw new IllegalArgumentException("jobName cannot be null");
        }
        if (jobName.trim().length() < 1) {
            throw new IllegalArgumentException("jobName cannot be empty");
        }
        if (interval < 1) {
            throw new IllegalArgumentException("interval cannot be 0 or negative:" + interval);
        }

        synchronized (AutoPilotJob.class) {
            if (scheduler == null) {
                scheduler = new Scheduler(namespace);
            }
            if (stopper == null) {
                stopper = new Thread(new AutoPilotJobStopper(AutoPilotJob.class.getName()));
            }
        }

        // Add or replace the job with jobName.
        synchronized (scheduler) {
            try {
                // Find old job with same name.
                Job oldJob = findJob(jobName);

                // Create new job if no old job with same name. Replace with new job otherwise.
                Job newJob = createJob(jobName, interval);
                if (null == oldJob) {
                    scheduler.addJob(newJob);
                } else {
                    scheduler.replaceJob(oldJob, newJob);
                }
            } catch (JobActionException e) {
                log.log(Level.ERROR, "fail to add job '" + jobName
                        + "' cause of job action exception \n" + LogMessage.getExceptionStackTrace(e));
                throw new ConfigurationException("fail to add job '" + jobName  + "' cause of job action exception", e);
            } catch (RuntimeException e) {
                log.log(Level.ERROR, "fail to add job '" + jobName
                        + "' cause of run time exception \n" + LogMessage.getExceptionStackTrace(e));
                throw new ConfigurationException("fail to add job '" + jobName + "' cause of run time exception", e);
            }

            if (!scheduler.isSchedulerRunning()) {
                stopper.start();
                scheduler.start();
            }
        }
    }

    /**
     * <p>
     * Find job with the specified name.
     * </p>
     * @param jobName the job name.
     * @return the job with the specified name.
     */
    private static Job findJob(String jobName) {
        Job oldJob = null;
        for (Iterator it = scheduler.getJobList().iterator(); it.hasNext();) {
            Job job = (Job) it.next();
            if (job.getName().equals(jobName)) {
                oldJob = job;
                break;
            }
        }
        return oldJob;
    }

    /**
     * <p>
     * Returns the internal scheduler instance (could be null).
     * </p>
     * @return the internal scheduler instance (could be null if schedule hasn't been called yet)
     */
    public static Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * <p>A task for polling the file system for presence of guard file and stopping the scheduler in case the file is
     * found thus causing the Auto Pilot process to exit.</p>
     *
     * @author isv
     * @version 1.0
     */
    private static class AutoPilotJobStopper implements Runnable {

        /**
         * <p>Defines the property key in the config manager that must contain the guard file name used to signal the job
         * that entire process is to be stopped.</p>
         *
         * @since 1.0.1
         */
        private static final String CONFIG_GUARD_FILE_KEY = "GuardFile";

        /**
         * <p>A <code>File</code> referencing the file on the local file system which presence indicates that the job
         * has to stop and quit.</p>
         *
         * @since 1.0.1
         */
        private File guardFile = null;

        /**
         * <p>The log used by this class for logging errors and debug information.</p>
         */
        private final Log log = LogManager.getLog("AutoPilot");

        /**
         * <p>Constructs new <code>AutoPilotJobStopper</code> instance.</p>
         *
         * @param namespace a <code>String</code> providing the cofniguration namespace.
         * @throws ConfigurationException if an unexpected error occurs.
         */
        private AutoPilotJobStopper(String namespace) throws ConfigurationException {
            // Get watch file with GuardFile key.
            String watchFileName;
            try {
                watchFileName = ConfigManager.getInstance().getString(namespace, CONFIG_GUARD_FILE_KEY);
            } catch (UnknownNamespaceException e) {
                log.log(Level.FATAL,
                        "fail to get watch file name cause of unknown namespace '" + namespace + "' \n"
                        + LogMessage.getExceptionStackTrace(e));
                throw new ConfigurationException("fail to get watch file name cause of unknown namespace '"
                    + namespace + "'", e);
            }
            if (watchFileName == null || watchFileName.trim().length() == 0) {
                throw new ConfigurationException("Watch file name parameter is missing from namespace '"
                    + namespace + "'");
            }
            this.guardFile = new File(watchFileName);
        }

        /**
         * <p>Polls the file system at intervals of 2 seconds for presence of guard file. If file is present then stops
         * the scheduler causing the Auto Pilot process to exit.</p>
         *
         * @see Thread#run()
         */
        public void run() {
            // Check if the job has to stop and quit
            while (!guardFile.exists()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // Ignore
                }
            }
            log.log(Level.INFO, "Got a signal to stop the entire Auto Pilot process by presence of file "
                                   + this.guardFile);
            scheduler.stop();
            log.log(Level.INFO, "Called the scheduler to stop");
        }
    }
}
