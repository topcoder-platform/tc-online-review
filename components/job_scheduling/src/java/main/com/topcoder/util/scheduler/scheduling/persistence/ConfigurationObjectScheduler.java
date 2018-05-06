/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling.persistence;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.InvalidConfigurationException;
import com.topcoder.configuration.SynchronizedConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.util.file.Template;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.scheduling.ConfigurationException;
import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.Dependence;
import com.topcoder.util.scheduler.scheduling.EmailEventHandler;
import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.Scheduler;
import com.topcoder.util.scheduler.scheduling.SchedulingException;
import com.topcoder.util.scheduler.scheduling.Util;

/**
 * <p>
 * This class implements the <code>Scheduler</code> interface, and it manages
 * the jobs and job groups in a <code>ConfigurationObject</code> object from
 * the Configuration API TCS component.
 * </p>
 * <p>
 * This scheduler will perform all CRUD operations from
 * <code>ConfigurationObject</code> aspect to <code>Job</code> and
 * <code>JobGroup</code>.
 * </p>
 * <p>
 * It will also use the Log from Logging Wrapper TCS component to log the method
 * invocation details and thrown exceptions.
 * </p>
 * <p>
 * The format of the jobs and job groups persisted in the configuration will be
 * the same as those detailed in the section 3.2.1 in Component Specification.
 * </p>
 * <p>
 * Thread-safety: The passed-in configuration object is wrapped into a
 * <code>SychronizedConfigurationObject</code> if it's not instance-of
 * <code>SychronizedConfigurationObject</code> to ensure the
 * access/modification to configuration is thread-safe. And user is required not
 * to change the passed-in configuration object externally. The logging wrapper
 * TCS component is thread-safe, so this class is thread-safe.
 * </p>
 * @author Standlove, fuyun
 * @version 3.1
 * @since 3.1
 */
public class ConfigurationObjectScheduler implements Scheduler {

    /**
     * <p>
     * Represents the <b>Subject</b> property name.
     * </p>
     */
    private static final String SUBJECT = "Subject";

    /**
     * <p>
     * Represents the <b>FromAddress</b> property name.
     * </p>
     */
    private static final String FROM_ADDRESS = "FromAddress";

    /**
     * <p>
     * Represents the <b>ModificationDate</b> property name.
     * </p>
     */
    private static final String MODIFICATION_DATE = "ModificationDate";

    /**
     * <p>
     * Represents the <b>DateUnitMonth</b> property name.
     * </p>
     */
    private static final String DATE_UNIT_MONTH = "DateUnitMonth";

    /**
     * <p>
     * Represents the <b>DateUnitWeek</b> property name.
     * </p>
     */
    private static final String DATE_UNIT_WEEK = "DateUnitWeek";

    /**
     * <p>
     * Represents the <b>DateUnitDays</b> property name.
     * </p>
     */
    private static final String DATE_UNIT_DAYS = "DateUnitDays";

    /**
     * <p>
     * Represents the <b>Type</b> property name.
     * </p>
     */
    private static final String TYPE = "Type";

    /**
     * <p>
     * Represents the <b>Unit</b> property name.
     * </p>
     */
    private static final String UNIT = "Unit";

    /**
     * <p>
     * Represents the <b>Value</b> property name.
     * </p>
     */
    private static final String VALUE = "Value";

    /**
     * <p>
     * Represents the <b>Interval</b> property name.
     * </p>
     */
    private static final String INTERVAL = "Interval";

    /**
     * <p>
     * Represents the <b>Recurrence</b> property name.
     * </p>
     */
    private static final String RECURRENCE = "Recurrence";

    /**
     * <p>
     * Represents the <b>Active</b> property name.
     * </p>
     */
    private static final String ACTIVE = "Active";

    /**
     * <p>
     * Represents the <b>AsyncMode</b> property name.
     * </p>
     */
    private static final String ASYNC_MODE = "AsyncMode";

    /**
     * <p>
     * Represents the <b>EndDate</b> property name.
     * </p>
     */
    private static final String END_DATE = "EndDate";

    /**
     * <p>
     * Represents the <b>StartTime</b> property name.
     * </p>
     */
    private static final String START_TIME = "StartTime";

    /**
     * <p>
     * Represents the <b>StartDate</b> property name.
     * </p>
     */
    private static final String START_DATE = "StartDate";

    /**
     * <p>
     * Represents the <b>TemplateText</b> property name.
     * </p>
     */
    private static final String TEMPLATE_TEXT = "TemplateText";

    /**
     * <p>
     * Represents the <b>Delay</b> property name.
     * </p>
     */
    private static final String DELAY = "Delay";

    /**
     * <p>
     * Represents the <b>Status</b> property name.
     * </p>
     */
    private static final String STATUS = "Status";

    /**
     * <p>
     * Represents the <b>Dependence</b> property name.
     * </p>
     */
    private static final String DEPENDENCE = "Dependence";

    /**
     * <p>
     * Represents the <b>Recipients</b> property name.
     * </p>
     */
    private static final String RECIPIENTS = "Recipients";

    /**
     * <p>
     * Represents the <b>Messages</b> property name.
     * </p>
     */
    private static final String MESSAGES = "Messages";

    /**
     * <p>
     * Represents the <b>Jobs</b> property name.
     * </p>
     */
    private static final String JOBS = "Jobs";

    /**
     * <p>
     * Represents the <b>DefinedGroups</b> property name.
     * </p>
     */
    private static final String DEFINED_GROUPS = "DefinedGroups";

    /**
     * <p>
     * Represents the <b>JobType</b> property name.
     * </p>
     */
    private static final String JOB_TYPE = "JobType";

    /**
     * <p>
     * Represents the <b>JobCommand</b> property name.
     * </p>
     */
    private static final String JOB_CMD = "JobCommand";

    /**
     * <p>
     * Represents the <b>Logger</b> property name.
     * </p>
     */
    private static final String LOGGER = "Logger";

    /**
     * <p>
     * Represents the <code>Log</code> that is used to report method
     * entry/exit, as well as errors, in this class. It is created in the
     * constructor, and will not be <code>null</code> or change.
     * </p>
     */
    private final Log log;

    /**
     * <p>
     * Represents the default template of the alert email.
     * </p>
     * <p>
     * It is used when template is not specified in an email alert.
     * </p>
     * <p>
     * It is initialized in the constructor and never changed later.
     * </p>
     * <p>
     * It can't be <code>null</code>.
     * </p>
     */
    private final Template defaultTemplate;

    /**
     * <p>
     * Represents the <code>ConfigurationObject</code> to persist the jobs and
     * job groups. Initialized in the constructor, and its reference is never
     * changed afterwards. It must be non-null. And its content is
     * accessed/modified in all its methods.
     * </p>
     */
    private final ConfigurationObject configuration;

    /**
     * <p>
     * Constructs the <code>ConfigurationObjectScheduler</code> from the given
     * <code>ConfigurationObject</code>.
     * </p>
     * <p>
     * If the given <code>ConfigurationObject</code> is not type of
     * <code>SychronizedConfigurationObject</code>, it will wrap it with
     * <code>SychronizedConfigurationObject</code> to ensure the thread safety
     * of this class.
     * </p>
     * <p>
     * The logger's name will be retrieved from the given configuration with the
     * name &quot;Logger&quot;. If it does not exist, the fully-qualified name
     * of this class will be used as the logger name to get logger from
     * <code>LogManager</code>.
     * </p>
     * @param configuration the <code>ConfigurationObject</code> to persist
     *            the jobs and job groups.
     * @throws IllegalArgumentException if the configuration argument is
     *             <code>null</code>.
     * @throws ConfigurationException if there is any configuration issue, for example,
     *         the Logger's value is empty.
     */
    public ConfigurationObjectScheduler(ConfigurationObject configuration) throws ConfigurationException {

        Util.checkObjectNotNull(configuration, "configuration");

        // wrap it with SynchronizedConfigurationObject if it is not thread
        // safe.
        if (!(configuration instanceof SynchronizedConfigurationObject)) {
            this.configuration = new SynchronizedConfigurationObject(
                    configuration);
        } else {
            this.configuration = configuration;
        }

        // the default log name
        String loggerName = ConfigurationObjectScheduler.class.getName();

        // try to get the Logger value from configuration.
        String[] values;
        try {
            values = getStringProperties(this.configuration, LOGGER, false,
                    true);
        } catch (SchedulingException se) {
            // here we do not need SchedulingException. Just use the message and
            // its inner cause
            throw new ConfigurationException(se.getMessage(), se.getCause());
        }
        if (values != null) {
            // if the logger name is defined, use it
            loggerName = values[0];
        }

        // get the logger.
        this.log = LogManager.getLog(loggerName);

        // create the default template
        defaultTemplate = SchedulerHelper.createDefaultTemplate();

    }

    /**
     * <p>
     * Adds the given job to the scheduler.
     * </p>
     * <p>
     * Note, the modification date of the given job will set to now before
     * adding to configuration object.
     * </p>
     * @param job The job to add.
     * @throws IllegalArgumentException if the added job is null or has a same
     *             name with existing job or has an invalid dependence or has
     *             both a schedule time and a dependence
     * @throws SchedulingException If the scheduler is unable to add this job in
     *             configuration
     */
    public void addJob(Job job) throws SchedulingException {

        log.log(Level.TRACE,
                "Enter ConfigurationObjectScheduler#addJob method.");
        try {
            // validate the given job to add
            checkJobValid(job, false);

            // update the modification date
            job.setModificationDate(new GregorianCalendar());

            // add the job to the configuration object.
            updateConfigObject(null, job);
        } catch (ConfigurationAccessException cae) {
            log.log(Level.ERROR, cae.getMessage());
            throw new SchedulingException(
                    "ConfigurationAccessException occurs while adding a job.",
                    cae);
        } catch (SchedulingException se) {
            log.log(Level.ERROR, se.getMessage());
            throw se;
        } catch (IllegalArgumentException iae) {
            log.log(Level.ERROR, iae.getMessage());
            throw iae;
        } catch (InvalidConfigurationException ice) {
            log.log(Level.ERROR, ice.getMessage());
            throw new SchedulingException(
                    "InvalidConfigurationException occurs while adding a job.",
                    ice);
        } finally {
            log.log(Level.TRACE,
                    "Exit ConfigurationObjectScheduler#addJob method.");
        }

    }

    /**
     * <p>
     * Updates the given job in the scheduler.
     * </p>
     * <p>
     * Note, the modification date of the given job will set it now before
     * adding to configuration object.
     * </p>
     * @param job The job to update.
     * @throws IllegalArgumentException if the added job is <code>null</code>
     *             or doesn't have a same name with existing job or has an
     *             invalid dependence or has both a schedule time and a
     *             dependence
     * @throws SchedulingException If the scheduler is unable to add this job in
     *             configuration
     */
    public void updateJob(Job job) throws SchedulingException {
        log.log(Level.TRACE,
                "Enter ConfigurationObjectScheduler#updateJob method.");
        try {
            // validate the given job to update
            checkJobValid(job, true);

            // update the job to the configuration object.
            updateConfigObject(job.getName(), job);
        } catch (ConfigurationAccessException cae) {
            log.log(Level.ERROR, cae.getMessage());
            throw new SchedulingException(
                    "ConfigurationAccessException occurs while updating a job.",
                    cae);
        } catch (InvalidConfigurationException ice) {
            log.log(Level.ERROR, ice.getMessage());
            throw new SchedulingException(
                    "InvalidConfigurationException occurs while updating a job.",
                    ice);
        } catch (SchedulingException se) {
            log.log(Level.ERROR, se.getMessage());
            throw se;
        } catch (IllegalArgumentException iae) {
            log.log(Level.ERROR, iae.getMessage());
            throw iae;
        } finally {
            log.log(Level.TRACE,
                    "Exit ConfigurationObjectScheduler#updateJob method.");
        }
    }

    /**
     * <p>
     * Deletes the job identified by the name of the given <code>job</code>
     * from the scheduler.
     * </p>
     * @param job The job to delete
     * @throws IllegalArgumentException if the job is null
     * @throws SchedulingException if the job can not be found in configuration
     *             or fails to delete the job.
     */
    public void deleteJob(Job job) throws SchedulingException {
        log.log(Level.TRACE,
                "Enter ConfigurationObjectScheduler#deleteJob method.");

        try {
            Util.checkObjectNotNull(job, "job");

            String jobName = job.getName();

            // the job name must be present in the configuration object
            if (checkJobExist(jobName)) {
                // delete the job from configuration object
                updateConfigObject(jobName, null);
            } else {
                // throw SchedulingException if the job name is not present
                throw new SchedulingException("The job [" + jobName
                        + "] can not be found.");
            }
        } catch (InvalidConfigurationException ice) {
            log.log(Level.ERROR, ice.getMessage());
            throw new SchedulingException(
                    "InvalidConfigurationException occurs while deleting a job.",
                    ice);
        } catch (ConfigurationAccessException cae) {
            log.log(Level.ERROR, cae.getMessage());
            throw new SchedulingException(
                    "ConfigurationAccessException occurs while deleting a job.",
                    cae);
        } catch (SchedulingException se) {
            log.log(Level.ERROR, se.getMessage());
            throw se;
        } catch (IllegalArgumentException iae) {
            log.log(Level.ERROR, iae.getMessage());
            throw iae;
        } finally {
            log.log(Level.TRACE,
                    "Exit ConfigurationObjectScheduler#deleteJob method.");
        }
    }

    /**
     * <p>
     * Gets the job with the given job name.
     * </p>
     * <p>
     * When the given job name isn't present, then null will be returned.
     * </p>
     * <p>
     * Note, in this method, the associated groups with the job to get will be
     * retrieved too. But the Jobs of the associated groups will not got
     * recursively.
     * </p>
     * @param jobName the name of job to get
     * @return the job with the given name, null if the specified job doesn't
     *         exist.
     * @throws IllegalArgumentException if the jobName is null or empty
     * @throws SchedulingException If the scheduler is unable to get this job
     *             from configuration
     */
    public Job getJob(String jobName) throws SchedulingException {
        log.log(Level.TRACE,
                "Enter ConfigurationObjectScheduler#getJob method.");

        try {
            Util.checkStringNotNullAndEmpty(jobName, "jobName");

            ConfigurationObject jobConfig = configuration.getChild(jobName);

            // the given job is not present
            if (jobConfig == null) {
                return null;
            }

            SchedulerHelper helper = new SchedulerHelper();
            helper.setJobName(jobName);

            // begin to set the job's attributes.
            String jobType = getStringProperties(jobConfig, JOB_TYPE, true,
                    true)[0];
            helper.setJobType(jobType);

            String jobCommand = getStringProperties(jobConfig, JOB_CMD, true,
                    true)[0];
            helper.setJobCommand(jobCommand);

            String[] startDate = getStringProperties(jobConfig, START_DATE,
                    false, true);
            helper.setStartDate(startDate == null ? null : startDate[0]);

            String[] endDate = getStringProperties(jobConfig, END_DATE, false,
                    true);
            helper.setEndDate(endDate == null ? null : endDate[0]);

            String[] startTime = getStringProperties(jobConfig, START_TIME,
                    false, true);
            helper.setStartTime(startTime == null ? null : startTime[0]);

            String[] asyncMode = getStringProperties(jobConfig, ASYNC_MODE,
                    false, true);
            helper.setAsyncMode(asyncMode == null ? null : asyncMode[0]);

            String active = getStringProperties(jobConfig, ACTIVE, true, true)[0];
            helper.setActive(active);

            String recurrence = getStringProperties(jobConfig, RECURRENCE,
                    true, true)[0];
            helper.setRecurrence(recurrence);

            ConfigurationObject interval = getRequiredChild(jobConfig, INTERVAL);

            String intervalValue = getStringProperties(interval, VALUE, true,
                    true)[0];
            helper.setInterval(intervalValue);

            String modificationDate = getStringProperties(jobConfig,
                    MODIFICATION_DATE, true, true)[0];
            helper.setModificationDate(modificationDate);

            ConfigurationObject unit = getRequiredChild(interval, UNIT);

            String type = getStringProperties(unit, TYPE, true, true)[0];
            helper.setDateUnit(type);

            String[] days = getStringProperties(unit, DATE_UNIT_DAYS, false,
                    true);
            helper.setDateUnitDays(days == null ? null : days[0]);

            String[] week = getStringProperties(unit, DATE_UNIT_WEEK, false,
                    true);
            helper.setDateUnitWeek(week == null ? null : week[0]);

            String[] month = getStringProperties(unit, DATE_UNIT_MONTH, false,
                    true);
            helper.setDateUnitMonth(month == null ? null : month[0]);

            // try to set the dependence if it is not null
            ConfigurationObject dependence = jobConfig.getChild(DEPENDENCE);
            if (dependence != null) {

                ConfigurationObject[] dependentJobs = dependence
                        .getAllChildren();

                if (dependentJobs.length != 0) {
                    // Create a new Dependence.
                    ConfigurationObject dependentJob = dependentJobs[0];

                    // Get job name.
                    helper.setDependenceJobName(dependentJob.getName());

                    helper.setDependenceJobStatus(getStringProperties(
                            dependentJob, STATUS, true, true)[0]);
                    String[] delay = getStringProperties(dependentJob, DELAY,
                            false, true);
                    helper
                            .setDependenceJobDelay(delay == null ? "0"
                                    : delay[0]);
                }
            }

            Job job = helper.createJob();

            // set the event handlers if it is configured.
            List messages = readEmailEventHandler(jobConfig.getChild(MESSAGES));
            if (messages != null) {
                for (Iterator it = messages.iterator(); it.hasNext();) {
                    EventHandler handler = (EventHandler) it.next();
                    job.addHandler(handler);
                }
            }

            // begin to load the groups.
            loadJobGroups(job);

            return job;
        } catch (ConfigurationAccessException cae) {
            log.log(Level.ERROR, cae.getMessage());
            throw new SchedulingException(
                    "ConfigurationAccessException occurs while getting a job.",
                    cae);
        } catch (SchedulingException se) {
            log.log(Level.ERROR, se.getMessage());
            throw se;
        } catch (IllegalArgumentException iae) {
            log.log(Level.ERROR, iae.getMessage());
            throw iae;
        } finally {
            log.log(Level.TRACE,
                    "Exit ConfigurationObjectScheduler#getJob method.");
        }
    }

    /**
     * <p>
     * Gets all the jobs in this <code>Scheduler</code> in an array.
     * </p>
     * <p>
     * Note, the return array will never be null, but may be empty.
     * </p>
     * @return an array containing all Jobs in this scheduler.
     * @throws SchedulingException If the scheduler is unable to get these jobs
     *             from configuration
     */
    public Job[] getJobList() throws SchedulingException {
        log.log(Level.TRACE,
                "Enter ConfigurationObjectScheduler#getJobList method.");

        List jobs = new ArrayList();

        try {

            ConfigurationObject[] children = configuration.getAllChildren();

            for (int i = 0, size = children.length; i < size; i++) {
                String key = children[i].getName();

                if (DEFINED_GROUPS.equals(key) || LOGGER.equals(key)) {
                    continue;
                }

                jobs.add(getJob(key));
            }

            return (Job[]) jobs.toArray(new Job[jobs.size()]);

        } catch (ConfigurationAccessException cae) {
            log.log(Level.ERROR, cae.getMessage());
            throw new SchedulingException(
                    "ConfigurationAccessException occurs while getting all the jobs.",
                    cae);
        } catch (SchedulingException se) {
            log.log(Level.ERROR, se.getMessage());
            throw se;
        } catch (IllegalArgumentException iae) {
            log.log(Level.ERROR, iae.getMessage());
            throw iae;
        } finally {
            log.log(Level.TRACE,
                    "Exit ConfigurationObjectScheduler#getJobList method.");
        }
    }

    /**
     * <p>
     * Adds the given group to this scheduler.
     * </p>
     * @param group the group to add
     * @throws IllegalArgumentException if the group is null
     * @throws SchedulingException If the group name already exists in the
     *             configuration, the jobs referred by the group can not be
     *             found in persistence or the scheduler is unable to add this
     *             group to persistence
     */
    public void addGroup(JobGroup group) throws SchedulingException {
        log.log(Level.TRACE,
                "Enter ConfigurationObjectScheduler#addGroup method.");

        try {
            // check the group argument
            checkJobGroup(group, false);
            // add the group to configuration object
            updateConfigObjectGroups(null, group);
        } catch (ConfigurationAccessException cae) {
            log.log(Level.ERROR, cae.getMessage());
            throw new SchedulingException(
                    "ConfigurationAccessException occurs while adding a group.",
                    cae);
        } catch (InvalidConfigurationException ice) {
            log.log(Level.ERROR, ice.getMessage());
            throw new SchedulingException(
                    "InvalidConfigurationException occurs while adding a group.",
                    ice);
        } catch (SchedulingException se) {
            log.log(Level.ERROR, se.getMessage());
            throw se;
        } catch (IllegalArgumentException iae) {
            log.log(Level.ERROR, iae.getMessage());
            throw iae;
        } finally {
            log.log(Level.TRACE,
                    "Exit ConfigurationObjectScheduler#addGroup method.");
        }
    }

    /**
     * <p>
     * Updates the group in this scheduler.
     * </p>
     * @param group the group to update
     * @throws IllegalArgumentException if the group is null
     * @throws SchedulingException If the group to update does not exist, the
     *             jobs referred by the group can not be found in configuration
     *             or the scheduler is unable to update this group in
     *             configuration
     */
    public void updateGroup(JobGroup group) throws SchedulingException {
        log.log(Level.TRACE,
                "Enter ConfigurationObjectScheduler#updateGroup method.");

        try {
            checkJobGroup(group, true);

            updateConfigObjectGroups(group.getName(), group);
        } catch (ConfigurationAccessException cae) {
            log.log(Level.ERROR, cae.getMessage());
            throw new SchedulingException(
                    "ConfigurationAccessException occurs while updating a group.",
                    cae);
        } catch (SchedulingException se) {
            log.log(Level.ERROR, se.getMessage());
            throw se;
        } catch (IllegalArgumentException iae) {
            log.log(Level.ERROR, iae.getMessage());
            throw iae;
        } catch (InvalidConfigurationException ice) {
            log.log(Level.ERROR, ice.getMessage());
            throw new SchedulingException(
                    "InvalidConfigurationException occurs while updating a group.",
                    ice);
        } finally {
            log.log(Level.TRACE,
                    "Exit ConfigurationObjectScheduler#updateGroup method.");
        }
    }

    /**
     * <p>
     * Removes the group with the given name.
     * </p>
     * @param name the name of the group to remove
     * @throws IllegalArgumentException if name is null or empty
     * @throws SchedulingException If the scheduler is
     *             unable to delete this group from configuration
     */
    public void deleteGroup(String name) throws SchedulingException {
        log.log(Level.TRACE,
                "Enter ConfigurationObjectScheduler#deleteGroup method.");
        try {
            Util.checkStringNotNullAndEmpty(name, "name");
            // check if the group exist.
            if (!checkGroupExist(name)) {
                return;
            }
            updateConfigObjectGroups(name, null);
        } catch (InvalidConfigurationException ice) {
            log.log(Level.ERROR, ice.getMessage());
            throw new SchedulingException(
                    "InvalidConfigurationException occurs while deleting a group.",
                    ice);
        } catch (ConfigurationAccessException cae) {
            log.log(Level.ERROR, cae.getMessage());
            throw new SchedulingException(
                    "ConfigurationAccessException occurs while deleting a group.",
                    cae);
        } catch (IllegalArgumentException iae) {
            log.log(Level.ERROR, iae.getMessage());
            throw iae;
        } finally {
            log.log(Level.TRACE,
                    "Exit ConfigurationObjectScheduler#deleteGroup method.");
        }
    }

    /**
     * <p>
     * Gets all the groups defined in this scheduler.
     * </p>
     * @return all the groups defined in this scheduler
     * @throws SchedulingException If the scheduler is unable to retrieve groups
     *             from configuration
     */
    public JobGroup[] getAllGroups() throws SchedulingException {
        log.log(Level.TRACE,
                "Enter ConfigurationObjectScheduler#getAllGroups method.");

        try {
            // loads all the job groups in the configuration object.
            return loadJobGroups(null);
        } catch (ConfigurationAccessException cae) {
            log.log(Level.ERROR, cae.getMessage());
            throw new SchedulingException(
                    "ConfigurationAccessException occurs while getting all groups.",
                    cae);
        } catch (SchedulingException se) {
            log.log(Level.ERROR, se.getMessage());
            throw se;
        } finally {
            log.log(Level.TRACE,
                    "Exit ConfigurationObjectScheduler#getAllGroups method.");
        }
    }

    /**
     * <p>
     * Gets the group with the given name from this scheduler.
     * </p>
     * <p>
     * If it doesn't exist, null will be returned.
     * </p>
     * @param name the name of group to get
     * @return the group with the given name.
     * @throws IllegalArgumentException if name is null or empty
     * @throws SchedulingException If the scheduler is unable to retrieve this
     *             group from configuration
     */
    public JobGroup getGroup(String name) throws SchedulingException {
        log.log(Level.TRACE,
                "Enter ConfigurationObjectScheduler#getGroup method.");

        try {
            Util.checkStringNotNullAndEmpty(name, "name");

            ConfigurationObject groups = configuration.getChild(DEFINED_GROUPS);
            if (groups == null) {
                return null;
            }

            ConfigurationObject group = groups.getChild(name);

            // the group is not present
            if (group == null) {
                return null;
            }

            return loadJobGroup(group, null);

        } catch (ConfigurationAccessException cae) {
            log.log(Level.ERROR, cae.getMessage());
            throw new SchedulingException(
                    "ConfigurationAccessException occurs while getting a group.",
                    cae);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            log.log(Level.TRACE,
                    "Exit ConfigurationObjectScheduler#getGroup method.");
        }
    }

    /**
     * <p>
     * Gets all the jobs dependant on the given job.
     * </p>
     * @return all the jobs depend on the given job
     * @param job the given job to query
     * @throws IllegalArgumentException if the job is null or the job doesn't
     *             exist in this scheduler.
     * @throws SchedulingException If the scheduler is unable to get the jobs
     *             from persistence
     */
    public Job[] getAllDependentJobs(Job job) throws SchedulingException {
        log
                .log(Level.TRACE,
                        "Enter ConfigurationObjectScheduler#getAllDependentJobs method.");

        try {
            Util.checkObjectNotNull(job, "job");
            String jobName = job.getName();

            // the job name is not present
            if (!checkJobExist(jobName)) {
                throw new IllegalArgumentException("The job " + jobName
                        + " isn't present in the configuration object.");
            }

            List res = new ArrayList();

            Job[] jobs = this.getJobList();
            for (int i = 0; i < jobs.length; i++) {
                // If the job has a dependence with the give job, add it to the
                // result list.
                Dependence dependence = jobs[i].getDependence();
                if ((dependence != null)
                        && dependence.getDependentJobName().equals(
                                job.getName())) {
                    res.add(jobs[i]);
                }
            }

            return (Job[]) res.toArray(new Job[res.size()]);
        } catch (ConfigurationAccessException cae) {
            log.log(Level.ERROR, cae.getMessage());
            throw new SchedulingException(
                    "ConfigurationAccessException occurs while getting all dependent jobs.",
                    cae);
        } catch (SchedulingException se) {
            log.log(Level.ERROR, se.getMessage());
            throw se;
        } catch (IllegalArgumentException iae) {
            log.log(Level.ERROR, iae.getMessage());
            throw iae;
        } finally {
            log
                    .log(Level.TRACE,
                            "Exit ConfigurationObjectScheduler#getAllDependentJobs method.");
        }
    }

    /**
     * Gets the property value for the given <code>key</code> in
     * <code>configurationObject</code>.
     * @param configurationObject the configuration object to get configuration.
     * @param key the key of the property to get.
     * @param required if the property is required.
     * @param isSingle if the configuration object only contain single value for
     *            the given key.
     * @return the property value retrieved from the configuration object using
     *         the given key.
     * @throws SchedulingException if fails to get the property, the property
     *             is missing but <code>required</code> is <code>true</code>,
     *             the value is not the instance of the given type or there are
     *             more than one property configured for the key.
     */
    private String[] getStringProperties(
            ConfigurationObject configurationObject, String key,
            boolean required, boolean isSingle) throws SchedulingException {
        try {
            // get the properties values.
            Object[] objs = configurationObject.getPropertyValues(key);

            // throw exception if it is required but not configured.
            if (objs == null || objs.length == 0) {
                if (required) {
                    throw new SchedulingException("The property " + key
                            + " is missing but it is required.");
                } else {
                    return null;
                }
            }

            // throw exception if it is single value but has multiple value
            // configured.
            if (objs.length > 1 && isSingle) {
                throw new SchedulingException(
                        "The property "
                                + key
                                + " should only contains one value but multiple values exist.");
            }

            for (int i = 0, size = objs.length; i < size; i++) {
                if (!(objs[i] instanceof String)) {
                    throw new SchedulingException("The value for the property "
                            + key + " is not type of String.");
                }
                if (((String) objs[i]).trim().length() == 0) {
                    throw new SchedulingException("The value for the property "
                            + key + " should not be empty String.");
                }
            }

            String[] result = new String[objs.length];
            System.arraycopy(objs, 0, result, 0, objs.length);
            return result;
        } catch (ConfigurationAccessException cae) {
            throw new SchedulingException(
                    "Fails to get the property value for the key: " + key, cae);
        }
    }

    /**
     * <p>
     * Checks whether the given job is a valid job.
     * </p>
     * @param job the job to be checked.
     * @param occurs the flag to identify whether the given job should be
     *            present in the configuration object.
     * @throws IllegalArgumentException if if the given job is null, or the job
     *             has a same name with existing job when occurs is
     *             <code>false</code>, or the job doesn't have a same name
     *             with existing job when occurs is <code>true</code>, or the
     *             job has an invalid dependence or has both a schedule time and
     *             a dependence
     * @throws ConfigurationAccessException if fails to get the sub-child to
     *             check the job name.
     */
    private void checkJobValid(Job job, boolean occurs) throws ConfigurationAccessException {
        Util.checkObjectNotNull(job, "job");

        String jobName = job.getName();
        boolean isJobExist = checkJobExist(jobName);
        if (isJobExist && !occurs) {
            throw new IllegalArgumentException("The job " + job.getName()
                    + " has a same name with existing jobs.");
        } else if (!isJobExist && occurs) {
            throw new IllegalArgumentException("The job " + job.getName()
                    + " isn't present in the configuration object.");
        }

        Dependence dep = job.getDependence();
        if (dep != null) {
            String dependentJobName = dep.getDependentJobName();

            // the dependent job name is not present
            if (!checkJobExist(dependentJobName)) {
                throw new IllegalArgumentException("The dependence job "
                        + dependentJobName + " of " + jobName
                        + " does not exist.");
            }

            // both dependence and start date are present
            if (job.getStartDate() != null) {
                throw new IllegalArgumentException(
                        "The job has both a schedule time and a dependence.");
            }
        } else {
            // both dependence and start date are absent
            if (job.getStartDate() == null) {
                throw new IllegalArgumentException(
                        "Both schedule time (start date) and dependence job are absent.");
            }
            // both dependence and end date are absent
            if (job.getStopDate() == null) {
                throw new IllegalArgumentException(
                        "Both schedule time (stop date) and dependence job are absent.");
            }
            // both dependence and end date are absent
            if (job.getStartTime() == 0) {
                throw new IllegalArgumentException(
                        "Both schedule time (start time) and dependence job are absent.");
            }
        }
    }

    /**
     * <p>
     * Checks if the job exists in the configuration object.
     * </p>
     * @param jobName the name of the job to check
     * @return the job is exist in the configuration object or not.
     * @throws ConfigurationAccessException if fails to get the sub-child to
     *             check the job name.
     */
    private boolean checkJobExist(String jobName) throws ConfigurationAccessException {
        return configuration.getChild(jobName) != null;
    }

    /**
     * <p>
     * Checks if the group exists in the configuration object.
     * </p>
     * @param groupName the name of the group to check
     * @return the group is exist in the configuration object or not.
     * @throws ConfigurationAccessException if fails to get the sub-child to
     *             check the job name.
     */
    private boolean checkGroupExist(String groupName) throws ConfigurationAccessException {
        ConfigurationObject definedGroups = configuration
                .getChild(DEFINED_GROUPS);
        if (definedGroups == null) {
            return false;
        }
        return definedGroups.getChild(groupName) != null;
    }

    /**
     * <p>
     * Updates the configuration object according to the <code>oldJobName</code>
     * and <code>newJob</code>.
     * </p>
     * <p>
     * When the given old job name is not <code>null</code>, then that job
     * will be deleted from configuration object. When the given new job is not
     * <code>null</code>, then that job will be added to configuration
     * object.
     * </p>
     * <p>
     * @param oldJobName The name of the old job to be deleted,
     *            <code>null</code> if adding a
     *            job.
     * @param newJob The new job to be added, <code>null</code> if deleting a job.
     *            </p>
     * @throws SchedulingException if the group name referred by the new job is
     *             not present
     * @throws ConfigurationAccessException if fails to update the configuration object
     *         due to configuration access issue.
     * @throws InvalidConfigurationException if fails to update the configuration object
     *         due to invalid configuration.
     */
    private void updateConfigObject(String oldJobName, Job newJob) throws SchedulingException,
        ConfigurationAccessException, InvalidConfigurationException {

        // get the DateFormat instance for formatting the Date instance
        DateFormat df = SchedulerHelper.getDateFormat();

        // Delete old job.
        if (oldJobName != null) {
            configuration.removeChild(oldJobName);
            deleteGroupJobByJobName(oldJobName);
        }

        // Add new Job
        if (newJob != null) {

            ConfigurationObject jobChild = new DefaultConfigurationObject(
                    newJob.getName());
            Dependence dep = newJob.getDependence();

            if (dep == null) {
                // add the start date property when the dependence is not
                // present
                GregorianCalendar startDate = newJob.getStartDate();
                jobChild.setPropertyValue(START_DATE, df.format(startDate
                        .getTime()));

                // add the start time property when the dependence is not
                // present
                int startTime = newJob.getStartTime();
                jobChild
                        .setPropertyValue(START_TIME, String.valueOf(startTime));

                // add the end date property when the dependence is not present
                GregorianCalendar endDate = newJob.getStopDate();
                jobChild.setPropertyValue(END_DATE, df
                        .format(endDate.getTime()));
            }

            // add the job type property
            String jobType = SchedulerHelper.parseJobType(newJob.getJobType());
            jobChild.setPropertyValue(JOB_TYPE, jobType);

            // add the job command property
            String jobCommand = newJob.getRunCommand();
            jobChild.setPropertyValue(JOB_CMD, jobCommand);

            // add the active property
            boolean active = newJob.getActive();
            jobChild.setPropertyValue(ACTIVE, SchedulerHelper
                    .parseBooleanValue(active));

            // add the asyncMode property
            boolean asyncMode = newJob.isAsyncMode();
            jobChild.setPropertyValue(ASYNC_MODE, SchedulerHelper
                    .parseBooleanValue(asyncMode));

            // add the recurrence property
            int recurrence = newJob.getRecurrence();
            jobChild.setPropertyValue(RECURRENCE, String.valueOf(recurrence));

            ConfigurationObject interval = new DefaultConfigurationObject(
                    INTERVAL);

            // add the interval value property
            int intervalValue = newJob.getIntervalValue();
            interval.setPropertyValue(VALUE, String.valueOf(intervalValue));

            DateUnit dateUnit = newJob.getIntervalUnit();
            if (dateUnit != null) {
                // add the date unit properties when it is set
                String[] dateUnits = SchedulerHelper.parseDateUnit(dateUnit);

                ConfigurationObject unit = new DefaultConfigurationObject(UNIT);

                // the type property
                unit.setPropertyValue(TYPE, dateUnits[0]);

                // the days property
                if (dateUnits[1] != null) {
                    unit.setPropertyValue(DATE_UNIT_DAYS, dateUnits[1]);
                }

                // the week property
                if (dateUnits[2] != null) {
                    unit.setPropertyValue(DATE_UNIT_WEEK, dateUnits[2]);
                }

                // the month property
                if (dateUnits[3] != null) {
                    unit.setPropertyValue(DATE_UNIT_MONTH, dateUnits[3]);
                }

                interval.addChild(unit);
            }

            jobChild.addChild(interval);

            // add the modification property
            GregorianCalendar modificationDate = new GregorianCalendar();
            newJob.setModificationDate(modificationDate);
            jobChild.setPropertyValue(MODIFICATION_DATE, df
                    .format(modificationDate.getTime()));

            if (dep != null) {

                ConfigurationObject dependence = new DefaultConfigurationObject(
                        DEPENDENCE);

                ConfigurationObject dependentJob = new DefaultConfigurationObject(
                        dep.getDependentJobName());

                // add the dependence related properties
                dependentJob.setPropertyValue(STATUS, dep.getDependentEvent());
                dependentJob.setPropertyValue(DELAY, String.valueOf(dep
                        .getDelay()));

                dependence.addChild(dependentJob);
                jobChild.addChild(dependence);

            }

            // add the email event information
            EventHandler[] handlers = newJob.getHandlers();
            updateMessageProperty(handlers, jobChild);

            JobGroup[] jobGroups = newJob.getGroups();

            ConfigurationObject definedGroups = configuration
                    .getChild(DEFINED_GROUPS);
            if (definedGroups == null && jobGroups.length > 0) {
                throw new SchedulingException(
                        "The ["
                                + DEFINED_GROUPS
                                + "] configuration object is not defined but the job belongs to some group(s).");
            }

            for (int i = 0; i < jobGroups.length; i++) {
                String groupName = jobGroups[i].getName();

                ConfigurationObject group = definedGroups.getChild(groupName);
                // validate the group name
                if (group == null) {
                    throw new SchedulingException("The group [" + groupName
                            + "] is not defined in the configuration object.");
                }

                // add/update this job to the groups
                List oldJobs = new ArrayList(Arrays.asList(group
                        .getPropertyValues(JOBS)));
                if (!oldJobs.contains(newJob.getName())) {
                    oldJobs.add(newJob.getName());
                }
                group.setPropertyValues(JOBS, oldJobs
                        .toArray(new String[oldJobs.size()]));

            }
            configuration.addChild(jobChild);
        }

    }

    /**
     * Removes the job name from all the groups.
     * @param jobName the job name to remove.
     * @throws ConfigurationAccessException if fails to remove the job name from
     *             groups.
     * @throws InvalidConfigurationException if fails to remove the job name
     *             from groups.
     */
    private void deleteGroupJobByJobName(String jobName)
        throws ConfigurationAccessException, InvalidConfigurationException {
        // get the defined groups
        ConfigurationObject definedGroups = configuration
                .getChild(DEFINED_GROUPS);

        // check if the defined groups is null. It should not happen usually.
        if (definedGroups == null) {
            return;
        }
        // get all the groups.
        ConfigurationObject[] children = definedGroups.getAllChildren();

        // remove the job if exist.
        for (int i = 0; i < children.length; i++) {
            Set oldJobs = new HashSet(Arrays.asList(children[i]
                    .getPropertyValues(JOBS)));
            oldJobs.remove(jobName);
            children[i].setPropertyValues(JOBS, oldJobs
                    .toArray(new String[oldJobs.size()]));
        }
    }

    /**
     * <p>
     * Updates the <tt>Messages</tt> property in configuration object.
     * </p>
     * @param handlers the event handlers.
     * @param config the configuration to add/update this message
     * @throws ConfigurationAccessException if fails to update the configuration
     *             object due to access issue.
     * @throws InvalidConfigurationException if fails to update the
     *             configuration object because the configuration is invalid.
     */
    private void updateMessageProperty(EventHandler[] handlers,
            ConfigurationObject config) throws InvalidConfigurationException,
            ConfigurationAccessException {

        ConfigurationObject messages = new DefaultConfigurationObject(MESSAGES);

        for (int i = 0; i < handlers.length; i++) {
            EventHandler handler = handlers[i];
            if (handler instanceof EmailEventHandler) {
                EmailEventHandler emailHandler = (EmailEventHandler) handler;

                List recipient = emailHandler.getRecipients();
                String event = emailHandler.getRequiredEvent();
                String template = emailHandler.getTemplate().getTemplate();

                ConfigurationObject eventChild = new DefaultConfigurationObject(
                        event);

                // add the template property
                eventChild.setPropertyValue(TEMPLATE_TEXT, template);

                // add the form address property
                eventChild.setPropertyValue(FROM_ADDRESS, emailHandler
                        .getEmailFromAddress());

                // add the subject property
                eventChild.setPropertyValue(SUBJECT, emailHandler
                        .getEmailAlertSubject());

                // add the recipients property
                eventChild.setPropertyValues(RECIPIENTS, recipient
                        .toArray(new String[recipient.size()]));
                messages.addChild(eventChild);
            }
        }

        // first remove the messages child.
        config.removeChild(MESSAGES);
        if (messages.getAllChildren().length != 0) {
            config.addChild(messages);
        }
    }

    /**
     * <p>
     * Updates <tt>DefinedGroups</tt> information in the configuration object.
     * </p>
     * <p>
     * When the given old group name is not null, then that group will be
     * deleted from configuration object. When the given new group is not null,
     * then that group will be added to configuration object.
     * </p>
     * @param oldGroupName the name of the group to delete
     * @param newGroup the group to add
     * @throws ConfigurationAccessException if fails to update the configuration
     *             object due to access issue.
     * @throws InvalidConfigurationException if fails to update the
     *             configuration object because the configuration is invalid.
     */
    private void updateConfigObjectGroups(String oldGroupName, JobGroup newGroup)
        throws InvalidConfigurationException, ConfigurationAccessException {

        ConfigurationObject definedGroup = configuration
                .getChild(DEFINED_GROUPS);

        // remove the old group
        if (oldGroupName != null) {
            if (definedGroup != null) {
                definedGroup.removeChild(oldGroupName);
            }
        }

        // add the new group
        if (newGroup != null) {
            if (definedGroup == null) {
                definedGroup = new DefaultConfigurationObject(DEFINED_GROUPS);
            }
            String groupName = newGroup.getName();

            ConfigurationObject groupNameChild = new DefaultConfigurationObject(
                    groupName);

            // add all the jobs in the group to configuration object
            Job[] jobs = newGroup.getJobs();

            List jobNames = new ArrayList();
            for (int i = 0; i < jobs.length; i++) {
                jobNames.add(jobs[i].getName());
            }
            groupNameChild.setPropertyValues(JOBS, jobNames
                    .toArray(new String[jobNames.size()]));

            // add all the email event handlers to configuration object
            EventHandler[] handlers = newGroup.getHandlers();
            updateMessageProperty(handlers, groupNameChild);
            definedGroup.addChild(groupNameChild);

            if (configuration.getChild(DEFINED_GROUPS) == null) {
                configuration.addChild(definedGroup);
            }

        }

    }

    /**
     * <p>
     * Reads <code>JobGroup</code> instances from configuration object.
     * </p>
     * <p>
     * If the given job is not null, then all the job groups that contain it
     * will be returned, otherwise all the job groups in the configuration
     * object will be returned.
     * </p>
     * <p>
     * Note, the return value will never be null but may be empty.
     * </p>
     * @param job the job to be loaded
     * @return all the job groups that contain it
     * @throws SchedulingException if any error occurs when parsing
     *             configuration object.
     * @throws ConfigurationAccessException if fails to load the groups due to access issue.
     */
    private JobGroup[] loadJobGroups(Job job) throws ConfigurationAccessException,
        SchedulingException {
        ConfigurationObject groups = configuration.getChild(DEFINED_GROUPS);

        if (groups == null) {
            return new JobGroup[0];
        }

        List jobGroups = new ArrayList();
        ConfigurationObject[] jobGroupsChildren = groups.getAllChildren();

        for (int i = 0, size = jobGroupsChildren.length; i < size; i++) {
            JobGroup jobGroup = loadJobGroup(jobGroupsChildren[i], job);
            if (jobGroup != null) {
                jobGroups.add(jobGroup);
                if (job != null) {
                    // add the group to the job.
                    job.addGroup(jobGroup);
                }
            }
        }

        return (JobGroup[]) jobGroups.toArray(new JobGroup[jobGroups.size()]);

    }

    /**
     * <p>
     * This method loads the <code>JobGroup</code> under the given
     * <code>groupConfig</code>
     * </p>
     * <p>
     * If the given <code>job</code> is not <code>null</code>, then if the
     * job group contains the given job, it will loads the job group from
     * configuration object and returns it, otherwise null will be returned.
     * </p>
     * <p>
     * If the given <code>job</code> is null, then the job group will be
     * loaded from configuration object and it will be returned.
     * </p>
     * @param groupConfig the configuration object instance that contains the
     *            information to construct a <code>JobGroup</code>
     * @param job the given job to check whether the job group contains it or
     *            not, may be null
     * @return the job group for the given group configuration, if job is not
     *         contained by the job group when job is not null, then null will
     *         be returned
     * @throws SchedulingException if any error occurs when parsing
     *             configuration object.
     * @throws ConfigurationAccessException if fails to load the job group due
     *             to access issue.
     */
    private JobGroup loadJobGroup(ConfigurationObject groupConfig, Job job)
        throws SchedulingException, ConfigurationAccessException {

        String groupName = groupConfig.getName();

        // Create Message and Jobs property.
        String[] jobNames = getStringProperties(groupConfig, JOBS, true, false);
        ConfigurationObject message = groupConfig.getChild(MESSAGES);

        if (job != null && !findJobName(jobNames, job)) {
            return null;
        }

        String jobName = (job == null) ? null : job.getName();

        List jobList = new ArrayList();
        for (int i = 0; i < jobNames.length; i++) {
            if (jobNames[i].equals(jobName)) {
                jobList.add(job);
            } else {
                jobList.add(loadSimpleJob(getRequiredChild(configuration,
                        jobNames[i])));
            }
        }

        // Create a new JobGroup.
        JobGroup jobGroup = new JobGroup(groupName, jobList);

        if (message != null) {
            List emailHandlerList = readEmailEventHandler(message);
            for (Iterator iterator = emailHandlerList.iterator(); iterator
                    .hasNext();) {
                EventHandler handler = (EventHandler) iterator.next();
                jobGroup.addHandler(handler);
            }
        }

        return jobGroup;
    }

    /**
     * <p>
     * Checks whether the name of the job is contained in the given job names
     * array.
     * </p>
     * @param jobNames the job names array
     * @param job the job to check
     * @return true if the name of the job is contained in the given job names
     *         array, false otherwise.
     */
    private boolean findJobName(String[] jobNames, Job job) {
        String name = job.getName();

        // search the jobNames array
        for (int i = 0; i < jobNames.length; i++) {
            if (name.equals(jobNames[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * <p>
     * This methods loads a <code>Job</code> instance from the configuration
     * object with only <b>jobName</b>, <b>jobType</b> and <b>jobCommand</b>
     * set.
     * </p>
     * @param jobConfig the configuration contains the information for
     *            constructing a <code>Job</code>
     * @return the <code>Job</code> instance with only <b>jobName</b>,
     *         <b>jobType</b> and <b>jobCommand</b> set.
     * @throws SchedulingException if any of the job type, job command
     *             properties is missing
     */
    private Job loadSimpleJob(ConfigurationObject jobConfig) throws SchedulingException {

        try {
            String jobName = jobConfig.getName();
            JobType jobType = SchedulerHelper.parseJobType(getStringProperties(
                    jobConfig, JOB_TYPE, true, true)[0]);
            String jobCommand = getStringProperties(jobConfig, JOB_CMD, true,
                    true)[0];

            return new Job(jobName, jobType, jobCommand);
        } catch (ConfigurationAccessException cae) {
            throw new SchedulingException(
                    "Fails to get the job name from configuration.", cae);
        }

    }

    /**
     * Returns the required child of the given parent configuration object.
     * @param parent the parent object to get the child configuration.
     * @param name the name of the child configuration object.
     * @return the child configuration object.
     * @throws SchedulingException if the parent does not have such child or
     *             fails to get it because of the access issue.
     */
    private ConfigurationObject getRequiredChild(ConfigurationObject parent,
            String name) throws SchedulingException {

        try {
            ConfigurationObject child = parent.getChild(name);
            // the child does not exist.
            if (child == null) {
                throw new SchedulingException(
                        "The configuration object does not contain child with name ["
                                + name + "].");
            }
            return child;
        } catch (ConfigurationAccessException cae) {
            throw new SchedulingException("Fails to get the child with name ["
                    + name + "].", cae);
        }

    }

    /**
     * <p>
     * Reads email event handler from configuration object.
     * </p>
     * <p>
     * All the email event handlers got from configuration object will be
     * returned as a <code>List</code>.
     * </p>
     * <p>
     * The return list contains only <code>EmailEventHandler</code> instances.
     * </p>
     * <p>
     * Note, if the <code>msgConfig</code> is null, then null will be
     * returned.
     * </p>
     * @param msgConfig the configuration contains email event handler
     *            information.
     * @return the List that contains <code>EmailEventHandler</code>
     *         instances.
     * @throws SchedulingException If the template format is incorrect.
     * @throws ConfigurationAccessException if fails to access the
     *             configuration.
     */
    private List readEmailEventHandler(ConfigurationObject msgConfig)
        throws SchedulingException, ConfigurationAccessException {
        if (msgConfig == null) {
            return null;
        }

        List handlerList = new ArrayList();

        ConfigurationObject[] events = msgConfig.getAllChildren();

        for (int i = 0, size = events.length; i < size; i++) {
            String eventName = events[i].getName();

            String[] templateText = getStringProperties(events[i],
                    TEMPLATE_TEXT, false, true);

            Template template = defaultTemplate;
            if (templateText != null) {
                // Create a new Template.
                // Read template from file
                template = SchedulerHelper.readTextToTemplate(templateText[0]);
            }

            // Get Recipients field.
            String[] recipients = getStringProperties(events[i], RECIPIENTS,
                    true, false);

            String fromAddress = getStringProperties(events[i], FROM_ADDRESS,
                    true, true)[0];
            String subject = getStringProperties(events[i], SUBJECT, true, true)[0];

            EmailEventHandler handler = new EmailEventHandler(template, Arrays
                    .asList(recipients), eventName, fromAddress, subject, log);

            handlerList.add(handler);
        }

        return handlerList;
    }

    /**
     * <p>
     * Checks whether the given group is valid.
     * </p>
     * @param group the group to be checked.
     * @param occurs the flag to identify whether the given group should be
     *            present in the configuration object.
     * @throws IllegalArgumentException if the given group is null
     * @throws SchedulingException if the given group refers to any jobs that
     *             are not present in the configuration object, or the group has
     *             a same name with existing group when occurs is
     *             <code>false</code>, or the group doesn't have a same name
     *             with existing group when occurs is <code>true</code>
     * @throws ConfigurationAccessException if the <code>configNamespace</code>
     *             has not been added to the ConfigManager.
     */
    private void checkJobGroup(JobGroup group, boolean occurs)
        throws SchedulingException, ConfigurationAccessException {
        Util.checkObjectNotNull(group, "group");

        boolean isGroupExist = checkGroupExist(group.getName());
        if (isGroupExist && !occurs) {
            throw new SchedulingException("The group " + group.getName()
                    + " has a same name with existing groups.");
        } else if (!isGroupExist && occurs) {
            throw new SchedulingException("The groups " + group.getName()
                    + " isn't present in the configuration object.");
        }

        Job[] jobs = group.getJobs();

        // all the referred jobs must be present
        for (int i = 0; i < jobs.length; i++) {
            if (!checkJobExist(jobs[i].getName())) {
                throw new SchedulingException("group contains a job, "
                        + jobs[i].getName()
                        + ", which does not exist in the scheduler.");
            }
        }
    }
}
