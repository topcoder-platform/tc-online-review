/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.exec.AsynchronousExecutorHandle;
import com.topcoder.util.exec.Exec;
import com.topcoder.util.exec.ExecutionException;
import com.topcoder.util.exec.ExecutionResult;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;

/**
 * <p>
 * This class is the specific job instance to schedule.
 * </p>
 * <p>
 * A Job must have either a scheduled date time or a task dependency but not both.
 * </p>
 * <p>
 * A job can belong to one or more groups.
 * </p>
 * <p>
 * A job can have event handlers listening on it's events. The handlers are used to send email alert and trigger another
 * job to start or whatever the event handler can do.
 * </p>
 * <p>
 * Changes in version 3.1:
 * <ol>
 * <li>Two new job types are supported, and when the job type is
 * <code>JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>, the configured runCommand represents the namespace
 * to create the running object using Object Factory with configured values loaded from this namespace via Configuration
 * Manager. when the job type is <code>JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>, the configured
 * runCommand represents the factory name to get the <code>ScheduledEnableObjectFactory</code> from the
 * <code>ScheduledEnableObjectFactoryManager</code> to create the running object. Only
 * <code>setRunCommand(String)</code> method is affected.</li>
 * <li>A new <code>ScheduledJobRunner</code> is added, which extends the <code>ScheduledEnable</code>, and the
 * <code>setJob(this)</code> is called in this class if the running object is instance of
 * <code>ScheduledJobRunner</code>. The <code>setRunCommand(String)</code> method is affected.</li>
 * <li>Attributes management APIs are added, so that the running-object can store "extra information" (or execution
 * result) through these APIs, in order to be accessed later by user.</li>
 * </ol>
 * </p>
 * <p>
 * Thread Safety : This class is mutable and not thread-safe. But the access of handlers and groups list are
 * synchronized, so that the user can add event handler or group to this job at runtime of the scheduler. And the added
 * attributes in version 3.1 is declared as synchronized map, so it could be safely used in multiple threads
 * environment.
 * </p>
 *
 * @author dmks, singlewood
 * @author argolite, TCSDEVELOPER
 * @author Standlove, fuyun
 * @version 3.1
 * @since 1.0
 */
public class Job {

    /**
     * Represents the property key to get the namespace to create the object factory instance.
     *
     * @since 3.1
     */
    private static final String OBJECTY_FACTORY_NS = "ObjectFactoryNamespace";

    /**
     * Represents the property key to get the key to create <code>ScheduledEnable</code> object using object factory.
     *
     * @since 3.1
     */
    private static final String SCHEDULED_ENABLE_OBJ_KEY = "ScheduledEnableObjectKey";

    /**
     * <p>
     * The date the job is to start running.
     * </p>
     * <p>
     * This value is mutable and is managed using the getter and setter. Once set, will not be null.
     * </p>
     */
    private GregorianCalendar startDate;

    /**
     * <p>
     * The job name.
     * </p>
     * <p>
     * This value is mutable and is managed using the getter and setter.
     * </p>
     * <p>
     * Can't be null and is set in the constructor.
     * </p>
     */
    private String name;

    /**
     * <p>
     * The date the job is to stop running.
     * </p>
     * <p>
     * This value is mutable and is managed using the getter and setter. Once set, will not be null.
     * </p>
     */
    private GregorianCalendar stopDate;

    /**
     * <p>
     * The interval value.
     * </p>
     * <p>
     * It combined with the intervalUnit indicates how often a task repeats. For example, an intervalValue of 2 and an
     * intervalUnit of <code>Year</code> indicates the job repeats every two years.
     * </p>
     * <p>
     * This value is mutable and is managed using the getter and setter.
     * </p>
     */
    private int intervalValue;

    /**
     * <p>
     * Represents the interval unit and is one of the existing <code>DateUnit</code> classes.
     * </p>
     * <p>
     * This value is mutable and is managed using the getter and setter. Once set, will not be null.
     * </p>
     * <p>
     * Note, in version 3.0, the type of this variable is modified to <code>DateUnit</code> compared to <code>int</code>
     * in version 2.0.
     * </p>
     */
    private DateUnit intervalUnit;

    /**
     * <p>
     * Either <code>JobType.JOB_TYPE_EXTERNAL</code> or <code>JobType.JOB_TYPE_JAVA_CLASS</code>.
     * </p>
     * <p>
     * This value is mutable and is managed using the getter and setter, but is also set in the constructor and will not
     * be null.
     * </p>
     * <p>
     * Changes in version 3.1: It can also be <code>JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code> or
     * <code>JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>.
     * </p>
     *
     * @since 3.0
     */
    private JobType jobType;

    /**
     * <p>
     * The command used to invoke/run the job.
     * </p>
     * <p>
     * In the case of a job type of <code>JOB_TYPE_JAVA_CLASS</code>, is the name of the class to run. In the case of a
     * <code>JOB_TYPE_EXTERNAL</code>, is the executable file/script to run.
     * </p>
     * <p>
     * This value is mutable and is managed using the getter and setter, but is set in the constructor and will not be
     * null.
     * </p>
     */
    private String runCommand;

    /**
     * <p>
     * If the job type not <code>JOB_TYPE_EXTERNAL</code>, runnableObject store the object implemented Schedulable and
     * Runnable interface, for version 1.0, or ScheduledEnable interface, for version 2.0, or the ScheduledJobRunner
     * interface in version 3.1.
     * </p>
     *
     * @since 2.0
     */
    private Object runnableObject;

    /**
     * <p>
     * The date/time the job was last run.
     * </p>
     * <p>
     * Null if the job has never been run.
     * </p>
     */
    private GregorianCalendar lastRun;

    /**
     * <p>
     * The date/time this job is next scheduled to run.
     * </p>
     * <p>
     * If this job will no longer be run in the future, this value will be null.
     * </p>
     */
    private GregorianCalendar nextRun;

    /**
     * <p>
     * A string value representing the job's status as of the last run.
     * </p>
     * <p>
     * If the job is currently executing, will represent the latest status returned by the job during execution.
     * </p>
     * <P>
     * This value is mutable and is managed using the getter and setter. Once set, will not be null/empty.
     * </p>
     */
    private String status;

    /**
     * <p>
     * Represents event handlers listening at this job.
     * </p>
     * <p>
     * This property can be accessed by the addHandler, removeHandler, getHandlers and fireEvent method.
     * </p>
     * <p>
     * The access of it is synchronized. It can't be null but can be empty. Will only contain non-null EventHandler
     * instances.
     * </p>
     *
     * @since 2.0
     */
    private List handlers = Collections.synchronizedList(new ArrayList());

    /**
     * <p>
     * Represents the inner running job instance.
     * </p>
     * <p>
     * It can be <code>AsynchronousExecutorHandle</code> if this job is external or a java object implemented
     * <code>Schedulable</code>(in v1.0) or <code>ScheduledEnable</code> interface.
     * </p>
     * <p>
     * It can be only accessed by the inner of class, never exposed to external.
     * </p>
     *
     * @since 2.0
     */
    private Object runningJob;

    /**
     * <p>
     * Represents the dependence the job have.
     * </p>
     * <p>
     * A Job must have either a scheduled date time or a task dependency but not both in current version.
     * </p>
     * <p>
     * It can be null if the job have a schedule time.
     * </p>
     *
     * @since 2.0
     */
    private Dependence dependence;

    /**
     * <p>
     * Represents groups this job belongs to.
     * </p>
     * <p>
     * It can be accessed in the getGroups method. It's a list of JobGroup class.
     * </p>
     * <p>
     * Can't be null but can be empty. Can't contain null.
     * </p>
     *
     * @since 2.0
     */
    private List groups = Collections.synchronizedList(new ArrayList());

    /**
     * <p>
     * Represents the number of times this Job is to be run.
     * </p>
     * <p>
     * Each time it is run, this value should be tracked and decremented in the executionCount.
     * </p>
     *
     * @since 3.0
     */
    private int recurrence;

    /**
     * <p>
     * The time the job is to start running.
     * </p>
     * <p>
     * The number represents the number of milliseconds since midnight, since it is just for one day, an int is
     * sufficient.
     * </p>
     * <p>
     * This value is mutable and is managed using the getter and setter.
     * </p>
     *
     * @since 3.0
     */
    private int startTime;

    /**
     * <p>
     * The execution count.
     * </p>
     * <p>
     * It is used to decrement remaining counts for it to recur.
     * </p>
     * <p>
     * An external processor should decrement it each time it is run.
     * </p>
     * <p>
     * This value is mutable and is managed using the getter and setter.
     * </p>
     * <p>
     * This value will also be automatically set to the recurrence value when that field is set.
     * </p>
     *
     * @since 3.0
     */
    private int executionCount;

    /**
     * <p>
     * Represents a flag showing whether this Job is active or disabled.
     * </p>
     * <p>
     * This value is mutable and is managed using the getter and setter.
     * </p>
     *
     * @since 3.0
     */
    private boolean active;

    /**
     * <p>
     * Represents the date this Job was last updated.
     * </p>
     * <p>
     * It is mutable and managed with the getter and setter. However, the setter is here for the purpose of the
     * scheduler setting it when it is loaded form persistence. When setting the job to persistence, the scheduler will
     * always write the current date to memory.
     * </p>
     * <p>
     * Once set, will not be null.
     * </p>
     *
     * @since 3.0
     */
    private GregorianCalendar modificationDate;

    /**
     * <p>
     * Represents a map of attributes for the running object to store execution result(extra information).
     * </p>
     * <p>
     * The key must be non-null, non-empty String, and the value must be non-null Object. It is initialized to
     * <code>Collections.synchronizedMap(new HashMap())</code> when declared, its reference is never changed afterwards.
     * </p>
     * <p>
     * Its content will be changed / accessed in corresponding get/remove/clear/set methods. It must be non-null. It can
     * be empty Map.
     * </p>
     * <p>
     * NOTE: The attributes are dynamically generated, and they should NEVER be stored in the persistence(scheduler.)
     * </p>
     *
     * @since 3.1
     */
    private final Map attributes = Collections.synchronizedMap(new HashMap());

    /**
     * <p>
     * Indicates whether the job running mode is async.
     * </p>
     * <p>
     * This value is mutable and is managed using the getter and setter.
     * </p>
     * <p>
     * It is used in start() method.
     * </p>
     * <p>
     * The default value is true.
     * </p>
     *
     * @since BUGR-1577
     */
    private boolean asyncMode = true;

    /**
     * <p>
     * Builds a new Job which is a copy of the passed Job.
     * </p>
     * <p>
     * Note, the handlers and groups properties are shallow copied.
     * </p>
     *
     * @param job
     *            The job to copy.
     * @throws IllegalArgumentException
     *             if the argument is <code>null</code>.
     * @since 2.0
     */
    public Job(Job job) {
        Util.checkObjectNotNull(job, "job");

        this.name = job.name;
        this.jobType = job.jobType;
        this.runCommand = job.runCommand;
        this.nextRun = job.nextRun;
        this.lastRun = job.lastRun;
        this.status = job.status;
        this.asyncMode = job.asyncMode;

        this.startDate = job.startDate;
        this.stopDate = job.stopDate;
        this.intervalUnit = job.intervalUnit;
        this.intervalValue = job.intervalValue;

        this.dependence = job.dependence;

        this.handlers.addAll(job.handlers);
        this.groups.addAll(job.groups);

        this.runnableObject = job.runnableObject;
        this.runningJob = null;

        // note, the following properties are since version 3.0
        this.recurrence = job.recurrence;
        this.startTime = job.startTime;
        this.executionCount = job.executionCount;
        this.active = job.active;
        this.modificationDate = job.modificationDate;
    }

    /**
     * <p>
     * Constructor for a Job object.
     * </p>
     * <p>
     * In version 2.0: If the job is internal, we need to check the java class implemented <code>ScheduledEnable</code>
     * interface.
     * </p>
     *
     * @param jobName
     *            The unique name to give this job.
     * @param jobType
     *            Indicates whether this job is an external operating system command (value of
     *            Scheduler.JOB_TYPE_EXTERNAL) or an internal, java class that will be loaded using reflection
     *            (Scheduler.JOB_TYPE_JAVA_CLASS).
     * @param runCommand
     *            The command is used to invoke/run the job. In the case of a job type of
     *            <code>JOB_TYPE_JAVA_CLASS</code>, is the name of the class to run. In the case of a
     *            <code>JOB_TYPE_EXTERNAL</code>, is the executable file/script to run. In the case of
     *            <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>, the configured runCommand represents the
     *            namespace to create the running object using Object Factory with configured values loaded from this
     *            namespace via Configuration Manager. In the case of
     *            <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>, the configured runCommand represents the
     *            factory name to get the ScheduledEnableObjectFactory from the ScheduledEnableObjectFactoryManager to
     *            create the running object.
     * @throws IllegalArgumentException
     *             If jobName or runCommand is null/empty, or jobType is null
     * @since 2.0
     */
    public Job(String jobName, JobType jobType, String runCommand) {
        setName(jobName);
        setJobType(jobType);
        setRunCommand(runCommand);
    }

    /**
     * <p>
     * The date and time the job is to start running.
     * </p>
     *
     * @return the job start time.
     * @since 2.0
     */
    public GregorianCalendar getStartDate() {
        return (startDate == null) ? null : (GregorianCalendar) startDate.clone();
    }

    /**
     * <p>
     * The date and time the job is to start running.
     * </p>
     *
     * @param startDate
     *            The job start time to be set.
     * @throws IllegalArgumentException
     *             if start date is null or if the start date is not before the end date
     * @since 2.0
     */
    public void setStartDate(GregorianCalendar startDate) {
        Util.checkObjectNotNull(startDate, "startDate");
        if (stopDate != null) {
            if (startDate.after(stopDate)) {
                throw new IllegalArgumentException("start date must be before stop date.");
            }
        }

        this.startDate = (GregorianCalendar) startDate.clone();
    }

    /**
     * <p>
     * Gets the job name.
     * </p>
     *
     * @return the job name.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Sets the job name.
     * </p>
     *
     * @param name
     *            The new job name.
     * @throws IllegalArgumentException
     *             If jobName is null/empty
     * @since 2.0
     */
    public void setName(String name) {
        Util.checkStringNotNullAndEmpty(name, "name");

        this.name = name;
    }

    /**
     * <p>
     * Gets the date the job is to stop.
     * </p>
     *
     * @return the date the job is to stop.
     * @since 2.0
     */
    public GregorianCalendar getStopDate() {
        return (stopDate == null) ? null : (GregorianCalendar) stopDate.clone();
    }

    /**
     * <p>
     * Set the job stop date/time after which this job should no longer be scheduled.
     * </p>
     * <p>
     * If null indicates the job will never stop being scheduled.
     * </p>
     *
     * @param stopDate
     *            the date the job is to stop.
     * @throws IllegalArgumentException
     *             if the stop date is not after the start date
     * @since 2.0
     */
    public void setStopDate(GregorianCalendar stopDate) {
        if (startDate != null) {
            if (startDate.after(stopDate)) {
                throw new IllegalArgumentException("stop date must be after start date.");
            }
        }

        this.stopDate = (stopDate == null) ? null : (GregorianCalendar) stopDate.clone();
    }

    /**
     * <p>
     * Gets the interval value.
     * </p>
     * <p>
     * The interval value combined with the intervalUnit indicates how often a task repeats. For example, an
     * intervalValue of 2 and an intervalUnit of <code>Week</code> indicates the job repeats every two weeks.
     * </p>
     *
     * @return the job interval value.
     */
    public int getIntervalValue() {
        return intervalValue;
    }

    /**
     * <p>
     * Sets the interval value.
     * </p>
     * <p>
     * The interval value combined with the intervalUnit indicates how often a task repeats. For example, an
     * intervalValue of 2 and an intervalUnit of <code>Week</code> indicates the job repeats every two weeks.
     * </p>
     *
     * @param intervalValue
     *            The new interval value to set
     * @throws IllegalArgumentException
     *             if intervalValue &lt= 0
     * @since 2.0
     */
    public void setIntervalValue(int intervalValue) {
        if (intervalValue <= 0) {
            throw new IllegalArgumentException("intervalValue should > 0");
        }

        this.intervalValue = intervalValue;
    }

    /**
     * <p>
     * Gets the run command.
     * </p>
     * <p>
     * The command is used to invoke/run the job. In the case of a job type of <code>JOB_TYPE_JAVA_CLASS</code>, is the
     * name of the class to run. In the case of a <code>JOB_TYPE_EXTERNAL</code>, is the executable file/script to run.
     * </p>
     *
     * @return the running command of this job.
     */
    public String getRunCommand() {
        return runCommand;
    }

    /**
     * <p>
     * Sets the run command.
     * </p>
     * <p>
     * The command is used to invoke/run the job. In the case of a job type of <code>JOB_TYPE_JAVA_CLASS</code>, is the
     * name of the class to run. In the case of a <code>JOB_TYPE_EXTERNAL</code>, is the executable file/script to run.
     * In the case of <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>, the configured runCommand represents the
     * namespace to create the running object using Object Factory with configured values loaded from this namespace via
     * Configuration Manager. In the case of <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>, the configured
     * runCommand represents the factory name to get the ScheduledEnableObjectFactory from the
     * ScheduledEnableObjectFactoryManager to create the running object.
     * </p>
     * <p>
     * Changes in version 3.1: two new job types are taken into consideration to create the running object. And If the
     * created running object is instance of <code>ScheduledJobRunner</code>, call its <code>setJob(this)</code> method
     * to set this job to it.
     * </p>
     * <p>
     * If it is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>, the configuration file should contain two
     * required properties:
     * <ol>
     * <li>ObjectFactoryNamespace: The namespace to create object factory.</li>
     * <li>ScheduledEnableObjectKey: The key to create object from object factory.</li>
     * </ol>
     * </p>
     *
     * @param runCommand
     *            The running command to be set.
     * @throws IllegalArgumentException
     *             if running object can not be instantiated by the given runCommand when jobType is not
     *             <code>JOB_TYPE_EXTERNAL</code>.
     * @since 2.0
     */
    public void setRunCommand(String runCommand) {
        Util.checkStringNotNullAndEmpty(runCommand, "runCommand");

        if (jobType == JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE) {
            runnableObject = getRunnableObjectFromObjectNamespace(runCommand);
        } else if (jobType == JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY) {
            runnableObject = getRunnableObjectFromOFManager(runCommand);
        } else if (jobType == JobType.JOB_TYPE_JAVA_CLASS) {
            runnableObject = getRunnableObject(runCommand);
        }

        this.runCommand = runCommand;

        // call setJob if it is instance of ScheduledJobRunner
        if (runnableObject != null && (runnableObject instanceof ScheduledJobRunner)) {
            ((ScheduledJobRunner) runnableObject).setJob(this);
        }

    }

    /**
     * <p>
     * Gets the last run date.
     * </p>
     * <p>
     * Note, it may be null.
     * </p>
     *
     * @return the date time of the last run
     * @since 2.0
     */
    public GregorianCalendar getLastRun() {
        return (lastRun == null) ? null : (GregorianCalendar) lastRun.clone();
    }

    /**
     * <p>
     * Sets the last run date.
     * </p>
     * <p>
     * It may be null if the job has never been run.
     * </p>
     *
     * @param lastRun
     *            the date time of the last run
     */
    public void setLastRun(GregorianCalendar lastRun) {
        this.lastRun = (lastRun == null) ? null : (GregorianCalendar) lastRun.clone();
    }

    /**
     * <p>
     * The date/time this job is next scheduled to run. If this job will no longer be run in the future, this value will
     * be null.
     * </p>
     *
     * @return the next run time.
     * @since 2.0
     */
    public GregorianCalendar getNextRun() {
        return (nextRun == null) ? null : (GregorianCalendar) nextRun.clone();
    }

    /**
     * <p>
     * The date/time this job is next scheduled to run. If this job will no longer be run in the future, this value will
     * be null.
     * </p>
     *
     * @param nextRun
     *            The next run time to be set.
     * @since 2.0
     */
    public void setNextRun(GregorianCalendar nextRun) {
        this.nextRun = (nextRun == null) ? null : (GregorianCalendar) nextRun.clone();
    }

    /**
     * <p>
     * A string value representing the job's status as of the last run. If the job is currently executing, will
     * represent the latest status returned by the job during execution.
     * </p>
     *
     * @return the job status.
     * @deprecated since 2.0, use getRunningStatus instead.
     */
    @Deprecated
    public String getStatus() {
        return status;
    }

    /**
     * <p>
     * A string value representing the job's status as of the last run. If the job is currently executing, will
     * represent the latest status returned by the job during execution.
     * </p>
     *
     * @param status
     *            the job status.
     * @throws IllegalArgumentException
     *             if status is null or empty
     * @deprecated since 2.0
     */
    @Deprecated
    public void setStatus(String status) {
        Util.checkStringNotNullAndEmpty(status, "status");

        this.status = status;
    }

    /**
     * <p>
     * Starts the job.
     * </p>
     *
     * @throws SchedulingException
     *             if fails to start.
     */
    public void start() throws SchedulingException {
        try {
            if (getJobType() == JobType.JOB_TYPE_EXTERNAL) {
                // If the job is external.
                runningJob = Exec.executeAsynchronously(new String[] { getRunCommand() });
            } else {
                runningJob = this.runnableObject;
                if (asyncMode) {
                    new Thread((Runnable) runningJob).start();
                } else {
                    ((Runnable) runningJob).run();
                }
            }
        } catch (ExecutionException e) {
            throw new SchedulingException("Execution failed", e);
        }
    }

    /**
     * <p>
     * Stops the job if it is started, otherwise, nothing happen.
     * </p>
     */
    public void stop() {
        if (runningJob != null) {
            if (getJobType() == JobType.JOB_TYPE_EXTERNAL) {
                ((AsynchronousExecutorHandle) runningJob).halt();
            } else {
                ((Schedulable) runningJob).close();
            }

            // Set runningJob to null.
            runningJob = null;
        }
    }

    /**
     * <p>
     * Fires the handlers listening on the event of this job.
     * </p>
     *
     * @param event
     *            the event raised by the job
     * @throws IllegalStateException
     *             if it is not a version 2.0 job.
     * @since 2.0
     */
    public void fireEvent(String event) {
        functionAvailableCheck();
        synchronized (handlers) {
            for (Iterator iter = handlers.iterator(); iter.hasNext();) {
                EventHandler handler = (EventHandler) iter.next();
                handler.handle(this, event);
            }
        }

        synchronized (groups) {
            for (Iterator iter = groups.iterator(); iter.hasNext();) {
                JobGroup group = (JobGroup) iter.next();
                group.fireEvent(this, event);
            }
        }
    }

    /**
     * <p>
     * Returns the running status of the job.
     * </p>
     * <p>
     * If the job is not started, <code>ScheduledEnable.NOTSTARTED</code> is returned,
     * </p>
     *
     * @return running status of the job
     */
    public String getRunningStatus() {
        if (runningJob == null) {
            return ScheduledEnable.NOT_STARTED;
        }

        if (getJobType() == JobType.JOB_TYPE_EXTERNAL) {
            if (((AsynchronousExecutorHandle) runningJob).isDone()) {
                try {
                    // get the execution result
                    ExecutionResult result = ((AsynchronousExecutorHandle) runningJob).getExecutionResult();
                    if (result.getExitStatus() != 0) {
                        return ScheduledEnable.FAILED;
                    } else {
                        return ScheduledEnable.SUCCESSFUL;
                    }
                } catch (IllegalStateException e) {
                    return ScheduledEnable.FAILED;
                }
            } else {
                return ScheduledEnable.RUNNING;
            }
        }

        if (isScheduledEnable()) {
            // If it is an inner ver2.0 job class, just return it's status.
            return ((ScheduledEnable) runningJob).getRunningStatus();
        } else {
            // If it is an inner ver1.0 job class, judge it's status by isDone
            // method.
            if (((Schedulable) runningJob).isDone()) {
                return ScheduledEnable.SUCCESSFUL;
            } else {
                return ScheduledEnable.RUNNING;
            }
        }
    }

    /**
     * <p>
     * Returns the message data to generate email message.
     * </p>
     *
     * @return the message data to generate email message
     * @throws IllegalStateException
     *             if it is not a version 2.0 job.
     * @since 2.0
     */
    public NodeList getMessageData() {
        functionAvailableCheck();

        if ((getJobType() == JobType.JOB_TYPE_JAVA_CLASS)) {
            if (runningJob != null) {
                return ((ScheduledEnable) runningJob).getMessageData();
            }
        }

        return null;
    }

    /**
     * <p>
     * Returns the date unit of this job.
     * </p>
     *
     * @return the date unit of this job.
     * @since 3.0
     */
    public DateUnit getIntervalUnit() {
        return intervalUnit;
    }

    /**
     * <p>
     * Sets the date unit of this job.
     * </p>
     *
     * @param intervalUnit
     *            the new date unit to set
     * @throws IllegalArgumentException
     *             if intervalUnit is null
     * @since 3.0
     */
    public void setIntervalUnit(DateUnit intervalUnit) {
        Util.checkObjectNotNull(intervalUnit, "intervalUnit");

        this.intervalUnit = intervalUnit;
    }

    /**
     * <p>
     * Returns the dependence of the job.
     * </p>
     *
     * @return the dependence of the job. For ver1.0 job, return null.
     * @since 2.0
     */
    public Dependence getDependence() {
        return dependence;
    }

    /**
     * <p>
     * Sets the dependence of the job.
     * </p>
     * <p>
     * If this job already have a scheduled time, the scheduled time will be disabled, the executing time will be
     * determined by the dependency job.
     * </p>
     *
     * @param dependence
     *            the dependence to set
     * @throws IllegalStateException
     *             if it is not a version 2.0 job.
     * @since 2.0
     */
    public void setDependence(Dependence dependence) {
        functionAvailableCheck();

        this.dependence = dependence;
    }

    /**
     * <p>
     * Adds an event handler to the handlers list.
     * </p>
     *
     * @param handler
     *            the event handler to add
     * @throws IllegalArgumentException
     *             if the handler is null
     * @throws IllegalStateException
     *             if this job is internal and the java class didn't implement ScheduledEnable interface
     * @since 2.0
     */
    public void addHandler(EventHandler handler) {
        functionAvailableCheck();
        Util.checkObjectNotNull(handler, "handler");

        handlers.add(handler);
    }

    /**
     * <p>
     * Removes an event handler from the handlers list.
     * </p>
     *
     * @param handler
     *            the event handler to remove
     * @throws IllegalArgumentException
     *             if the handler is null.
     * @throws IllegalStateException
     *             if it is not a version 2.0 job.
     * @since 2.0
     */
    public void removeHandler(EventHandler handler) {
        functionAvailableCheck();
        Util.checkObjectNotNull(handler, "handler");

        handlers.remove(handler);
    }

    /**
     * <p>
     * Returns the event handlers in this job.
     * </p>
     *
     * @return the event handlers in this job
     * @throws IllegalStateException
     *             if it is not a version 2.0 job.
     * @since 2.0
     */
    public EventHandler[] getHandlers() {
        functionAvailableCheck();

        return (EventHandler[]) handlers.toArray(new EventHandler[0]);
    }

    /**
     * <p>
     * Returns the groups this job belongs to.
     * </p>
     *
     * @return the groups this job belongs to
     * @throws IllegalStateException
     *             if it is not a version 2.0 job.
     * @since 2.0
     */
    public JobGroup[] getGroups() {
        functionAvailableCheck();

        return (JobGroup[]) groups.toArray(new JobGroup[0]);
    }

    /**
     * <p>
     * Adds a group to this job.
     * </p>
     *
     * @param group
     *            the group to add
     * @throws IllegalArgumentException
     *             if group is null
     * @throws IllegalStateException
     *             if it is not a version 2.0 job.
     * @since 2.0
     */
    public void addGroup(JobGroup group) {
        functionAvailableCheck();
        Util.checkObjectNotNull(group, "group");

        groups.add(group);
    }

    /**
     * <p>
     * Removes a group from this job.
     * </p>
     *
     * @param group
     *            the group to remove
     * @throws IllegalArgumentException
     *             if group is null
     * @throws IllegalStateException
     *             if it is not a version 2.0 job.
     * @since 2.0
     */
    public void removeGroup(JobGroup group) {
        functionAvailableCheck();
        Util.checkObjectNotNull(group, "group");

        groups.remove(group);
    }

    /**
     * <p>
     * Gets the start time.
     * </p>
     *
     * @return the start time
     * @since 3.0
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * <p>
     * Sets the start time.
     * </p>
     *
     * @param startTime
     *            the start time
     * @throws IllegalArgumentException
     *             if start time is not positive or larger than 24 hours
     * @since 3.0
     */
    public void setStartTime(int startTime) {
        if (startTime <= 0 || startTime > Util.ONE_DAY) {
            throw new IllegalArgumentException(
                "The start time should be positive or less than 24 hours in milliseconds, but it is" + startTime);
        }

        this.startTime = startTime;
    }

    /**
     * <p>
     * Gets the recurrence number.
     * </p>
     *
     * @return the recurrence number
     * @since 3.0
     */
    public int getRecurrence() {
        return recurrence;
    }

    /**
     * <p>
     * Sets the recurrence number.
     * </p>
     * <p>
     * It resets the execution count to this number too.
     * </p>
     *
     * @param recurrence
     *            the recurrence number
     * @throws IllegalArgumentException
     *             if recurrence is less than 0
     * @since 3.0
     */
    public void setRecurrence(int recurrence) {
        if (recurrence < 0) {
            throw new IllegalArgumentException("The recurrence is less than zero, it is " + recurrence);
        }

        this.recurrence = recurrence;
        this.executionCount = recurrence;
    }

    /**
     * <p>
     * Gets the type of this job.
     * </p>
     * <p>
     * It should be <code>JobType.JOB_TYPE_EXTERNAL</code> or <code>JobType.JOB_TYPE_JAVA_CLASS</code>.
     * </p>
     * <p>
     * Note, in version 3.0, the return type of this method is modified to <code>JobType</code> compared to the one in
     * version 2.0.
     * </p>
     *
     * @return the job type
     */
    public JobType getJobType() {
        return jobType;
    }

    /**
     * <p>
     * Sets the type of this job.
     * </p>
     * <p>
     * It should be <code>JobType.JOB_TYPE_EXTERNAL</code> or <code>JobType.JOB_TYPE_JAVA_CLASS</code>.
     * </p>
     * <p>
     * Note, in version 3.0, the return type of this method is modified to <code>JobType</code> compared to the one in
     * version 2.0.
     * </p>
     *
     * @param jobType
     *            The type of job to be set.
     * @throws IllegalArgumentException
     *             if jobType is null.
     * @since 2.0
     */
    public void setJobType(JobType jobType) {
        Util.checkObjectNotNull(jobType, "jobType");

        this.jobType = jobType;
    }

    /**
     * <p>
     * Gets the execution count.
     * </p>
     *
     * @return the execution count
     * @since 3.0
     */
    public int getExecutionCount() {
        return executionCount;
    }

    /**
     * <p>
     * Sets the execution count.
     * </p>
     *
     * @param executionCount
     *            the execution count
     * @throws IllegalArgumentException
     *             if executionCount is less than zero or large than recurrence
     * @since 3.0
     */
    public void setExecutionCount(int executionCount) {
        if (executionCount < 0 || executionCount > recurrence) {
            throw new IllegalArgumentException("The executionCount is less than zero or large than recurrence, it is "
                + executionCount);
        }

        this.executionCount = executionCount;
    }

    /**
     * <p>
     * Gets the active flag value.
     * </p>
     *
     * @return True if active, false otherwise
     * @since 3.0
     */
    public boolean getActive() {
        return active;
    }

    /**
     * <p>
     * Sets the active flag value.
     * </p>
     *
     * @param active
     *            True if active, false otherwise
     * @since 3.0
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * <p>
     * Gets the modification date.
     * </p>
     *
     * @return the modification date
     * @since 3.0
     */
    public GregorianCalendar getModificationDate() {
        return (modificationDate == null) ? null : (GregorianCalendar) modificationDate.clone();
    }

    /**
     * <p>
     * Sets the modification date.
     * </p>
     * <p>
     * It can be any value.
     * </p>
     *
     * @param modificationDate
     *            the new modification date to set
     * @since 3.0
     */
    public void setModificationDate(GregorianCalendar modificationDate) {
        this.modificationDate = (modificationDate == null) ? null : (GregorianCalendar) modificationDate.clone();
    }

    /**
     * <p>
     * Returns the inner running job instance.
     * </p>
     * <p>
     * Returns the <code>runningJob</code> if the job type is <code>JobType.JOB_TYPE_EXTERNAL</code>, otherwise return
     * the <code>runnableObject</code>.
     * </p>
     *
     * @return the <code>runningJob</code> if the jobType is <code>JOB_TYPE_EXTERNAL</code>, otherwise return the
     *         <code>runnableObject</code>.
     * @since 3.1
     */
    public Object getRunningJob() {
        if (jobType == JobType.JOB_TYPE_EXTERNAL) {
            return runningJob;
        }

        return this.runnableObject;
    }

    /**
     * <p>
     * Returns the value of the attribute identified by the given name.
     * </p>
     * <p>
     * <code>null</code> is returned if the attribute name doesn't exist.
     * </p>
     *
     * @param name
     *            the attribute name.
     * @return the attribute value.
     * @throws IllegalArgumentException
     *             if the given argument is <code>null</code> or empty string.
     * @since 3.1
     */
    public Object getAttribute(String name) {
        Util.checkStringNotNullAndEmpty(name, "name");
        return attributes.get(name);
    }

    /**
     * <p>
     * Sets the attribute name, value pair.
     * </p>
     * <p>
     * If the attribute name already exists, the old value will be overwritten by the given one.
     * </p>
     *
     * @param name
     *            the attribute name.
     * @param value
     *            the attribute value.
     * @throws IllegalArgumentException
     *             if any argument is <code>null</code>, or the name argument is empty string.
     * @since 3.1
     */
    public void setAttribute(String name, Object value) {
        Util.checkStringNotNullAndEmpty(name, "name");
        Util.checkObjectNotNull(value, "value");
        attributes.put(name, value);
    }

    /**
     * <p>
     * Gets all the attribute names to return as a string array.
     * </p>
     * <p>
     * An empty array can be returned if the attributes is empty.
     * </p>
     *
     * @return the attribute names in an string array.
     * @since 3.1
     */
    public String[] getAttributeNames() {
        // for the synchroinzed map, no appropriate sync job could
        // be used, so just use new String[0]
        return (String[]) attributes.keySet().toArray(new String[0]);

    }

    /**
     * <p>
     * Removes the attribute of the given name.
     * </p>
     * <p>
     * If the attribute name doesn't exist, nothing happens.
     * </p>
     *
     * @param name
     *            the attribute name.
     * @throws IllegalArgumentException
     *             if the given argument is <code>null</code> or empty string.
     * @since 3.1
     */
    public void removeAttribute(String name) {
        Util.checkStringNotNullAndEmpty(name, "name");
        attributes.remove(name);
    }

    /**
     * <p>
     * Clears all attributes.
     * </p>
     *
     * @since 3.1
     */
    public void clearAttributes() {
        attributes.clear();
    }

    /**
     * <p>
     * Gets <code>Runnable</code> object using <code>runCommand</code>.
     * </p>
     *
     * @param runCommand
     *            the class name.
     * @return the <code>Runnable</code> object.
     * @throws IllegalArgumentException
     *             if it fails to instantiate the java class, or the object does not extend the <code>Runnable</code>
     *             and <code>Schedulable</code> interface, or the object does not extend the
     *             <code>ScheduledEnable</code> interface.
     */
    private Object getRunnableObject(String runCommand) {
        try {
            Object obj = Class.forName(runCommand).newInstance();

            if (checkRunnableObjectValid(obj)) {
                // set the job name to the runnable object
                ((Schedulable) obj).setJobName(name);

                return obj;
            }

            throw new IllegalArgumentException("The given Java class " + runCommand + " must implement Runnable and"
                + " Schedulable interface, or ScheduledEnable interface");

        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class " + runCommand + " not found, caused by " + e.getMessage());
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Failed to instantiate the java class " + runCommand + ", caused by "
                + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Failed to instantiate the java class " + runCommand + ", caused by "
                + e.getMessage());
        }
    }

    /**
     * <p>
     * Gets <code>Runnable</code> object using <code>runCommand</code>.
     * </p>
     *
     * @param runCommand
     *            the namespace for the configuration.
     * @return the <code>Runnable</code> object.
     * @throws IllegalArgumentException
     *             if it fails to create the <code>Runnable</code> object, or the object does not extend the
     *             <code>Runnable</code> and <code>Schedulable</code> interface, or the object does not extend the
     *             <code>ScheduledEnable</code> interface.
     * @since 3.1
     */
    private Object getRunnableObjectFromObjectNamespace(String runCommand) {
        String objectFactoryNamespace = getRequiredPropertyValue(runCommand, OBJECTY_FACTORY_NS);
        String key = getRequiredPropertyValue(runCommand, SCHEDULED_ENABLE_OBJ_KEY);
        ObjectFactory factory;
        try {
            factory = new ObjectFactory(new ConfigManagerSpecificationFactory(objectFactoryNamespace));
        } catch (SpecificationConfigurationException sce) {
            // thrown by create ConfigurationObjectSpecificationFactory
            throw new IllegalArgumentException("Fails to create object factory, caused by " + sce.getMessage());
        } catch (IllegalReferenceException ire) {
            throw new IllegalArgumentException("Fails to create object factory, caused by " + ire.getMessage());
        }
        Object obj = this.createObject(factory, key);

        if (checkRunnableObjectValid(obj)) {
            // set the job name to the runnable object
            ((Schedulable) obj).setJobName(name);

            return obj;
        }

        throw new IllegalArgumentException(
            "The runnable object created from object factory in the given the namespace [" + runCommand
                + "[ must implement Runnable and" + " Schedulable interface, or ScheduledEnable interface");
    }

    /**
     * <p>
     * Gets <code>ScheduledEnable</code> object by the <code>ScheduledEnableObjectFactory</code> retrieved from
     * <code>ScheduledEnableObjectFactoryManager</code> using <code>runCommand</code>.
     * </p>
     *
     * @param runCommand
     *            the name of <code>ScheduledEnableObjectFactory</code>.
     * @return the <code>ScheduledEnable</code> object.
     * @throws IllegalArgumentException
     *             if there is not factory for the given name, or the factory fails to create the
     *             <code>ScheduledEnable</code> object.
     * @since 3.1
     */
    private Object getRunnableObjectFromOFManager(String runCommand) {
        ScheduledEnableObjectFactory factory = ScheduledEnableObjectFactoryManager
            .getScheduledEnableObjectFactory(runCommand);

        // check if the factory is null.
        if (factory == null) {
            throw new IllegalArgumentException(
                "The ScheduledEnableObjectFactory could not be got using the argument [ " + runCommand
                    + " ] from ScheduledEnableObjectFactoryManager");

        }

        // create the ScheduledEnable object.
        try {
            ScheduledEnable obj = factory.createScheduledEnableObject();
            obj.setJobName(name);
            return obj;
        } catch (ScheduledEnableObjectCreationException seoce) {
            throw new IllegalArgumentException(
                "Fails to create the runnable object from ScheduledEnableObjectFactory, caused by "
                    + seoce.getMessage());
        }
    }

    /**
     * Checks if the given object is instance of <code>ScheduledEnable</code> or it is instance of both
     * <code>Schedulable</code> and <code>Runnable</code>.
     *
     * @param obj
     *            the object to check.
     * @return <code>true</code> if it is instance of <code>ScheduledEnable</code> or it is instance of both
     *         <code>Schedulable</code> and <code>Runnable</code>. <code>false</code> otherwise.
     * @since 3.1
     */
    private boolean checkRunnableObjectValid(Object obj) {
        if ((obj instanceof ScheduledEnable) || (obj instanceof Schedulable) && (obj instanceof Runnable)) {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * Decides if the inner running job implements ScheduledEnable.
     * </p>
     *
     * @return true if it implements ScheduledEnable and it's a internal job, else false.
     */
    private boolean isScheduledEnable() {
        if (jobType == JobType.JOB_TYPE_EXTERNAL) {
            return false;
        }

        return (runnableObject instanceof ScheduledEnable);
    }

    /**
     * <p>
     * Checks whether the current job is a version 2.0 job.
     * </p>
     * <p>
     * If it is not a ver2.0 job, an exception will be thrown.
     * </p>
     *
     * @throws IllegalStateException
     *             if it is not a version 2.0 job.
     */
    private void functionAvailableCheck() {
        if (jobType == JobType.JOB_TYPE_EXTERNAL) {
            return;
        }

        if (!isScheduledEnable()) {
            throw new IllegalStateException("Error, this job is not compatible with ver2.0,"
                + " this function is unavailable.");
        }
    }

    /**
     * <p>
     * Gets the property value in the configuration with the given name.
     * </p>
     * <p>
     * If the property is missing and <code>required</code> is <code>true</code>, then exception will be thrown.
     * </p>
     *
     * @param namespace
     *            the namespace to get the property value.
     * @param name
     *            the name of the property.
     * @return the property value in the given namespace for the name.
     * @throws IllegalArgumentException
     *             if the property value is missing when required is <code>true</code> or the value empty string or the
     *             property contains multiple values.
     * @since 3.1
     */
    private String getRequiredPropertyValue(String namespace, String name) {

        ConfigManager manager = ConfigManager.getInstance();
        String[] values;
        try {
            // get the string values.
            values = manager.getStringArray(namespace, name);
        } catch (UnknownNamespaceException une) {
            throw new IllegalArgumentException("The namespace [ " + namespace + " ] is unknown.");
        }

        if (values == null) {
            // no value defined, but it is required, throw exception.
            throw new IllegalArgumentException("The property [ " + name + " ] under namespace [ " + namespace
                + " ] is missing.");

        }

        // multiple values exist, throw exception.
        if (values.length > 1) {
            throw new IllegalArgumentException("The property [ " + name + " ] under namespace [ " + namespace
                + " ] could not have multiple value.");
        }

        // get the single value.
        String value = values[0];

        // check the empty value.
        if (value.trim().length() == 0) {
            throw new IllegalArgumentException("The value for property [ " + name + " ] under namespace [" + namespace
                + "] is empty string.");
        }

        return value.trim();
    }

    /**
     * Creates the object from the object factory with the given key.
     *
     * @param of
     *            the object factory to create the object.
     * @param key
     *            the key used to create the object.
     * @return the object created from the object factory with the given key.
     * @throws IllegalArgumentException
     *             if fails to create the object.
     * @since 3.1
     */
    private Object createObject(ObjectFactory of, String key) {

        try {
            Object obj = of.createObject(key);
            return obj;
        } catch (InvalidClassSpecificationException icse) {
            throw new IllegalArgumentException("Fails to create object for key [" + key + "], caused by "
                + icse.getMessage());
        }

    }

    /**
     * <p>
     * Gets whether the job is async mode.
     * </p>
     *
     * @return whether this job is async mode.
     * @since BUGR-1577
     */
    public boolean isAsyncMode() {
        return this.asyncMode;
    }

    /**
     * <p>
     * Sets whether the job is async mode.
     * </p>
     *
     * @param asyncMode
     *            The async mode to set.
     * @since BUGR-1577
     */
    public void setAsyncMode(boolean asyncMode) {
        this.asyncMode = asyncMode;
    }
}
