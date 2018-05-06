/*
 * Copyright (C) 2006-2012 TopCoder Inc., All Rights Reserved.
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
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;
import com.topcoder.util.scheduler.Job;
import com.topcoder.util.scheduler.JobActionException;
import com.topcoder.util.scheduler.Schedulable;
import com.topcoder.util.scheduler.Scheduler;

import org.apache.log4j.PropertyConfigurator;

/**
 * <p>
 * Represents an auto pilot job that is to be executed using Job Scheduling component. A new
 * instance of this class will be created and executed (in a separate thread) by the Scheduler at a
 * certain interval.
 * </p>
 * <p>
 * This class may not be thread-safe, the variable done is mutable by the 'run' method. However this
 * class will be run in its own thread by the Scheduler, and only a single thread will execute the
 * run method, so in the context of the scheduler, it's thread safe. The internal scheduler is
 * instantiated in schedule() in a synchronized block. It's also thread safe in the context of the
 * command-line because only one thread is active.
 * </p>
 * @author sindu, abelli, TCSDEVELOPER
 * @version 1.0.2
 */
public class AutoPilotJob implements Runnable, Schedulable {

    /**
     * <p>The log used by this class for logging errors and debug information.</p>
     */
    private final Log log;

    /**
     * <p>
     * Defines the property key in the config manager that can optionally contains the operator name
     * used to do auditing.
     * </p>
     */
    public static final String CONFIG_OPERATOR_KEY = "Operator";

    /**
     * <p>
     * Represents the default operator name that is used to do auditing if none is specified in the
     * configuration files.
     * </p>
     */
    public static final String DEFAULT_OPERATOR = "AutoPilotJob";

    /**
     * <p>
     * Represents the status to return when a job is running.
     * </p>
     */
    public static final String STATUS_RUNNING = "RUNNING";

    /**
     * <p>
     * Represents the status to return when the job is completed.
     * </p>
     */
    public static final String STATUS_DONE = "DONE";

    /**
     * <p>
     * The specified namespace plus this postfix is used as namespace to initialize the object factory.
     * </p>
     */
    public static final String OBJECT_FACTORY_POSTFIX = ".factory";

    /**
     * <p>
     * Represents the AutoPilot instance that is used to do the job. This variable is initially
     * null, initialized in constructor using object factory and immutable afterwards. It can be
     * retrieved with the getter. It is cached by the class so it only gets constructed once.
     * </p>
     */
    private static AutoPilot autoPilot = null;

    /**
     * <p>
     * Represents the operator name that is used to do auditing. This variable is initially null,
     * initialized in constructor using values from configuration and immutable afterwards. It can
     * be retrieved with the getter. It is cached by the class so it only gets constructed once.
     * </p>
     */
    private static String operator = null;

    /**
     * <p>
     * Represents a flag that indicates whether the job is completed. This variable is initially
     * false, after run() is complete, it'll be set to true. It is referenced by the isDone() method
     * to inform the scheduler that the job is completed.
     * </p>
     */
    private boolean done = false;

    /**
     * <p>
     * Constructs a new instance of AutoPilotJob class. This will initialize the AutoPilot instance
     * using object factory. The object factory is initialized with AutoPilotJob full name as its
     * configuration namespace. Inside this namespace, a property with the key of AutoPilot's full
     * name is used to retrieve the AutoPilot instance. The namespace can optionally contains a key
     * of "Operator" which defines the operator name that will be used to do auditing. If it's not
     * defined, DEFAULT_OPERATOR is used.
     * </p>
     * @throws ConfigurationException if any error occurs instantiating the object factory or the
     *             auto pilot instance
     */
    public AutoPilotJob() throws ConfigurationException {
        this(AutoPilotJob.class.getName(), AutoPilot.class.getName());
    }

    /**
     * <p>
     * Constructs a new instance of AutoPilotJob class using the given namespace/autoPilotKey. This
     * will initialize the AutoPilot instance using object factory. The object factory is
     * initialized with namespace + '.factory' as its configuration namespace. Inside the object
     * factory, a property with the key of autoPilotKey is used to retrieve the AutoPilot instance.<br>
     * <br>
     * The namespace (no '.factory') can optionally contain a key of "Operator" which defines the
     * operator name that will be used to do auditing. If it's not defined, DEFAULT_OPERATOR is
     * used.
     * </p>
     * @param namespace the namespace + '.factory' to initialize object factory, and the namespace
     *            (no '.factory') to retrieve other configuration
     * @param autoPilotKey the key defining the AutoPilot instance to use
     * @throws IllegalArgumentException if any of the argument is null or empty (trimmed) string
     * @throws ConfigurationException if any error occurs instantiating the object factory or the
     *             auto pilot instance
     */
    public AutoPilotJob(String namespace, String autoPilotKey) throws ConfigurationException {
        // Check arguments.
        if (null == namespace) {
            throw new IllegalArgumentException("namespace cannot be null");
        }
        if (namespace.trim().length() < 1) {
            throw new IllegalArgumentException("namespace cannot be empty");
        }
        if (null == autoPilotKey) {
            throw new IllegalArgumentException("autoPilotKey cannot be null");
        }
        if (autoPilotKey.trim().length() < 1) {
            throw new IllegalArgumentException("autoPilotKey cannot be empty");
        }

        this.log = LogManager.getLog("AutoPilot");
        
        log.log(Level.DEBUG,
        		"Create AutoPilotJob with namespace:" + namespace + " and autoPilotKey:" + autoPilotKey);

        if (autoPilot == null) {
            // Create object factory.
            ObjectFactory of;
            Object objAutoPilot;
            try {
                of = new ObjectFactory(new ConfigManagerSpecificationFactory(namespace + OBJECT_FACTORY_POSTFIX));
                log.log(Level.DEBUG, "create Objectfactory from namespace: " + namespace + OBJECT_FACTORY_POSTFIX);
                // Create autoPilot from object factory.
                objAutoPilot = of.createObject(autoPilotKey);
                if (!AutoPilot.class.isInstance(objAutoPilot)) {
                    log.log(Level.FATAL, "fail to create AutoPilot object cause of bad type:" + objAutoPilot);
                    throw new ConfigurationException("fail to create AutoPilot object cause of bad type:" + objAutoPilot);
                }
                log.log(Level.DEBUG, "create AutoPilot from objectfactory with autoPilotkey:" + autoPilotKey);
            } catch (InvalidClassSpecificationException e) {
                log.log(Level.FATAL,
                        "fail to create auto pilot cause of invalid class specification exception \n"
                        + LogMessage.getExceptionStackTrace(e));
                throw new ConfigurationException(
                    "fail to create auto pilot cause of invalid class specification exception", e);
            } catch (SpecificationConfigurationException e) {
                log.log(Level.FATAL,
                        "fail to create object factory instance cause of specification configuration exception \n"
                        + LogMessage.getExceptionStackTrace(e));
                throw new ConfigurationException(
                    "fail to create object factory instance cause of specification configuration exception",
                    e);
            } catch (IllegalReferenceException e) {
                log.log(Level.FATAL,
                        "fail to create object factory instance cause of illegal reference exception \n"
                        + LogMessage.getExceptionStackTrace(e));
                throw new ConfigurationException(
                    "fail to create object factory instance cause of illegal reference exception", e);
            }

            autoPilot = (AutoPilot) objAutoPilot;
        }

        if (operator == null) {
            // Get operator with Operator key.
            String oper;
            try {
                oper = ConfigManager.getInstance().getString(namespace, CONFIG_OPERATOR_KEY);
            } catch (UnknownNamespaceException e) {
                log.log(Level.FATAL,
                        "fail to get operator cause of unknown namespace '" + namespace + "' \n"
                        + LogMessage.getExceptionStackTrace(e));
                throw new ConfigurationException("fail to get operator cause of unknown namespace '"
                    + namespace + "'", e);
            }

            log.log(Level.DEBUG, "read property " + CONFIG_OPERATOR_KEY + " with value : "
                    + oper + "(if null, the default operator will be used) from namespace: " + namespace);

            // Use default operator name if not specified.
            if (oper != null && oper.trim().length() > 0) {
                operator = oper;
            } else {
                operator = DEFAULT_OPERATOR;
            }

        }
    }

    /**
     * <p>
     * Constructs a new instance of AutoPilotJob class using the given AutoPilot instance and
     * operator name.
     * </p>
     * @param autoPilot the AutoPilot instance to use
     * @param operator the operator name for auditing
     * @param log the Log instance
     * @throws IllegalArgumentException if any of the argument is null, or operator is empty string
     *             (trimmed).
     */
    public AutoPilotJob(AutoPilot autoPilot, String operator, Log log) {
        // Check arguments.
        if (null == autoPilot) {
            throw new IllegalArgumentException("autoPilot cannot be null");
        }
        if (null == operator) {
            throw new IllegalArgumentException("operator cannot be null");
        }
        if (operator.trim().length() < 1) {
            throw new IllegalArgumentException("operator cannot be empty");
        }
        if (null == log) {
            throw new IllegalArgumentException("log cannot be null");
        }

        this.log = log;
        this.autoPilot = autoPilot;
        this.operator = operator;
        log.log(Level.DEBUG, "Instantiate AutoPilotJob with AutoPilot and operator:" + operator);
    }

    /**
     * <p>
     * Return the auto pilot instance used by this class.
     * </p>
     * @return the auto pilot instance used by this class
     */
    public AutoPilot getAutoPilot() {
        return this.autoPilot;
    }

    /**
     * <p>
     * Return the operator name used to do auditing.
     * </p>
     * @return the operator name used to do auditing
     */
    public String getOperator() {
        return this.operator;
    }

    /**
     * <p>
     * This method implements 'run' in the Runnable interface. It's invoked to start the job.
     * </p>
     * @throws RuntimeException - if fail to execute the auto pilot job. AutoPilotSourceException
     *             and PhaseOperationException will be wrapped in RuntimeException.
     */
    public void run() {
        // to prevent the potential if the same job instance will be executed more than once.
        done = false;

        try {
            execute();
        } catch (AutoPilotSourceException e) {
        	log.log(Level.ERROR, "fail to advance projects with " + getOperator()
                    + " cause of auto pilot source exception \n" + LogMessage.getExceptionStackTrace(e));
            e.printStackTrace(System.err);
            throw new RuntimeException("fail to advance projects with " + getOperator()
                + " cause of auto pilot source exception", e);
        } catch (PhaseOperationException e) {
        	log.log(Level.ERROR, "fail to advance project " + e.getProjectId() + " phase "
                    + e.getPhase() + " with " + getOperator()
                    + " cause of phase operation exception \n" + LogMessage.getExceptionStackTrace(e));
            e.printStackTrace(System.err);
            throw new RuntimeException("fail to advance project " + e.getProjectId() + " phase "
                + e.getPhase() + " with " + getOperator() + " cause of phase operation exception",
                e);
        }

        done = true;
    }

    /**
     * <p>
     * This method is invoked by the scheduled job to process all active projects.
     * </p>
     * @return an array of AutoPilotResult representing result of auto-pilot (never null, but can be
     *         empty)
     * @throws AutoPilotSourceException if any error occurs retrieving project ids from
     *             AutoPilotSource
     * @throws PhaseOperationException if any error occurs while ending/starting a phase
     */
    public AutoPilotResult[] execute() throws AutoPilotSourceException, PhaseOperationException {
        log.log(Level.INFO, new LogMessage(null, getOperator(), "AutoPilot job iteration."));
        AutoPilotResult[] ret = autoPilot.advanceProjects(getOperator());
        log.log(Level.DEBUG, new LogMessage(null, getOperator(), "AutoPilot job iteration - end."));
        return ret;
    }

    /**
     * <p>
     * This method is invoked by command line interface to process a given list of project ids.
     * </p>
     * @param projectId a list of project id to process
     * @return an array of AutoPilotResult representing result of auto-pilot (never null, but can be
     *         empty)
     * @throws IllegalArgumentException if projectId is null
     * @throws AutoPilotSourceException if any error occurs retrieving project ids from
     *             AutoPilotSource
     * @throws PhaseOperationException if any error occurs while ending/starting a phase
     */
    public AutoPilotResult[] run(long[] projectId) throws AutoPilotSourceException,
        PhaseOperationException {
        log.log(Level.INFO, new LogMessage(null, getOperator(), "AutoPilot iteration."));
        AutoPilotResult[] ret = autoPilot.advanceProjects(projectId, getOperator());
        log.log(Level.DEBUG, new LogMessage(null, getOperator(), "AutoPilot job iteration - end."));
        return ret;
    }

    /**
     * <p>
     * This method implements 'isDone' in the Schedulable interface. It's invoked by scheduler to
     * check whether the job is completed.
     * </p>
     * @return true if job is completed, false otherwise
     */
    public boolean isDone() {
        return done;
    }

    /**
     * <p>
     * This is invoked by the Scheduler when the Scheduler is stopped. We simply do nothing here. If
     * the job is still running, we'll let it run until it's finished. Since it's running in its own
     * thread, we can just do nothing here so that we don't block the scheduler's thread.
     * </p>
     */
    public void close() {
        // your code here
    }

    /**
     * <p>
     * This should return the job status. Application can poll this status using Scheduler's Job.
     * </p>
     * @return STATUS_DONE or STATUS_RUNNING depending whether the job has completed
     */
    public String getStatus() {
        return isDone() ? STATUS_DONE : STATUS_RUNNING;
    }
}
