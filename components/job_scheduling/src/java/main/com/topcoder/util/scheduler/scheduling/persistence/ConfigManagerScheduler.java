/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.persistence;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
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
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.config.Property;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.file.Template;

/**
 * <p>
 * This class implements the interface <code>Scheduler</code> using ConfigManager as the persistent data source.
 * </p>
 *
 * <p>
 * This scheduler will perform all CRUD operations to <code>Job</code> and <code>JobGroup</code>.
 * </p>
 *
 * <p>
 * This class reads data from configuration file, here is a sample config file:
 *&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
 *&lt;CMConfig&gt;
 *
 *&lt;Property name=&quot;jobName07&quot;&gt;
 * &lt;Property name=&quot;StartDate&quot;&gt;
 *   &lt;Value&gt;May 5, 2007 05:00:00 AM&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name=&quot;StartTime&quot;&gt;
 *   &lt;Value&gt;3000000&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name=&quot;EndDate&quot;&gt;
 *   &lt;Value&gt;May 5, 2007 05:00:00 AM&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name=&quot;JobType&quot;&gt;
 *   &lt;Value&gt;JOB_TYPE_EXTERNAL&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name=&quot;JobCommand&quot;&gt;
 *   &lt;Value&gt;dir&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name=&quot;Active&quot;&gt;
 *  &lt;Value&gt;True&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name=&quot;AsyncMode&quot;&gt;
 *  &lt;Value&gt;True&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name=&quot;Recurrence&quot;&gt;
 *   &lt;Value&gt;5&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name=&quot;Interval&quot;&gt;
 * &lt;Property name=&quot;Value&quot;&gt;
 *   &lt;Value&gt;2&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name=&quot;Unit&quot;&gt;
 * &lt;Property name=&quot;Type&quot;&gt;
 *   &lt;Value&gt;com.topcoder.util.scheduler.scheduling.Week&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 *&lt;/Property&gt;
 *
 * &lt;Property name=&quot;ModificationDate&quot;&gt;
 *    &lt;Value&gt;Jan 5, 2007 05:00:00 AM&lt;/Value&gt;
 * &lt;/Property&gt;
 *&lt;/Property&gt;
 *
 *&lt;!-- Defined Groups --&gt;
 * &lt;Property name=&quot;DefinedGroups&quot;&gt;
 * &lt;Property name=&quot;group_1&quot;&gt;
 * &lt;!-- group name --&gt;
 * &lt;Property name=&quot;Jobs&quot;&gt;
 * &lt;!-- must have one job --&gt;
 *  &lt;Value&gt;newJobName01&lt;/Value&gt;
 *  &lt;Value&gt;jobName07&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name=&quot;Messages&quot;&gt;
 * &lt;!-- Email Event handlers in this group, optional --&gt;
 * &lt;Property name=&quot;SUCCESSFUL&quot;&gt;
 * &lt;Property name=&quot;TemplateText&quot;&gt;
 *  &lt;Value&gt;
 *  Hi,
 *  This email notifies you that the job %JobName% has the status %JobStatus% now...
 *  &lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name=&quot;Recipients&quot;&gt;
 *  &lt;Value&gt;rec1@topcoder.com&lt;/Value&gt;
 *  &lt;Value&gt;rec2@topcoder.com&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name=&quot;FromAddress&quot;&gt;
 *  &lt;Value&gt;admin@topcoder.com&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name=&quot;Subject&quot;&gt;
 *  &lt;Value&gt;Notification&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 *&lt;/Property&gt;
 *&lt;/Property&gt;
 *&lt;/Property&gt;
 *&lt;/CMConfig&gt;
 * </p>
 *
 * <p>
 * Note, this class is migrated from class <code>Scheduler</code> in version 2.0.
 * </p>
 *
 * <p>Changes in version 3.1:
 * As two new job types are added, so when checking the job types, the two new ones should be taken
 * into consideration.
 * </p>
 *
 * <p>
 * Thread Safety : This class is immutable and thread-safe.
 * </p>
 *
 * @author dmks, singlewood
 * @author argolite, TCSDEVELOPER
 * @author Standlove, fuyun
 * @version 3.1
 * @since 3.0
 */
public class ConfigManagerScheduler implements Scheduler {
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
     * Represents the dot used as a separator of properties.
     * </p>
     */
    private static final String DOT = ".";

    /**
     * <p>
     * Represents the name of the package.
     * </p>
     */
    private static final String PACKAGE_NAME = "com.topcoder.util.scheduler.scheduling.persistence";

    /**
     * <p>
     * Represents the namespace that contains the configured Jobs and will be used to read them.
     * </p>
     *
     * <p>
     * Set in the constructor from the Config Manager and will not change.
     * </p>
     *
     * <p>
     * Will not be null/empty.
     * </p>
     */
    private final String configNamespace;

    /**
     * <p>
     * Represents the <code>Log</code> that is used to report method entry/exit, as well as errors,
     * in this class.
     * <p>
     *
     * <p>
     * It is created in the constructor, and will not be null or change.
     * </p>
     */
    private final Log log;

    /**
     * <p>
     * Represents the default template of the alert email.
     * </p>
     *
     * <p>
     * It is used when template file is not specified in an email alert.
     * </p>
     *
     * <p>
     * It is initialized in the constructor and never changed later.
     * </p>
     *
     * <p>
     * It can't be null.
     * </p>
     */
    private final Template defaultTemplate;

    /**
     * <p>
     * Constructs a <code>ConfigManagerScheduler</code> with namespace given.
     * </p>
     *
     * @param namespace The configuration file namespace.
     *
     * @throws IllegalArgumentException if namespace is null or empty.
     * @throws ConfigurationException If there is an error during the construction of this class.
     */
    public ConfigManagerScheduler(String namespace) throws ConfigurationException {
        Util.checkStringNotNullAndEmpty(namespace, "namespace");

        this.configNamespace = namespace;

        // get the logger name from the configuration file or the fully-qualified name of ConfigManagerScheduler class
        String loggerName = ConfigManagerScheduler.class.getName();
        try {
            String value = ConfigManager.getInstance().getString(namespace, LOGGER);
            if (value != null) {
                loggerName = value;
            }
        } catch (UnknownNamespaceException e) {
            throw new ConfigurationException("UnknownNamespaceException occurs while getting logger name.", e);
        }

        this.log = LogManager.getLog(loggerName);

        defaultTemplate = SchedulerHelper.createDefaultTemplate();
    }

    /**
     * <p>
     * Adds the given job to the scheduler.
     * </p>
     *
     * <p>
     * Note, the modification date of the given job will set the now before adding to config file.
     * </p>
     *
     * @param job The job to add.
     *
     * @throws IllegalArgumentException if the added job is null or has a same name with existing
     * job or has an invalid dependence or has both a schedule time and a dependence
     * @throws SchedulingException If the scheduler is unable to add this job in persistence
     */
    public void addJob(Job job) throws SchedulingException {
        log.log(Level.TRACE, "Enter ConfigManagerScheduler#addJob method.");
        try {
            // validate the given job to add
            checkJobValid(job, false);

            // update the modification date
            job.setModificationDate(new GregorianCalendar());

            // add the job to the configuration file.
            updateConfigFile(null, job);
        } catch (UnknownNamespaceException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("UnknownNamespaceException occurs while adding a job.", e);
        } catch (ConfigManagerException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("ConfigManagerException occurs while adding a job.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            log.log(Level.TRACE, "Exit ConfigManagerScheduler#addJob method.");
        }
    }

    /**
     * <p>
     * Updates the given job in the scheduler.
     * </p>
     *
     * <p>
     * Note, the modification date of the given job will set the now before adding to config file.
     * </p>
     *
     * @param job The job to update.
     *
     * @throws IllegalArgumentException if the added job is null or doesn't have a same name with existing job
     * or has an invalid dependence or has both a schedule time and a dependence
     * @throws SchedulingException If the scheduler is unable to update this job in persistence
     */
    public void updateJob(Job job) throws SchedulingException {
        log.log(Level.TRACE, "Enter ConfigManagerScheduler#updateJob method.");
        try {
            // validate the given job to update
            checkJobValid(job, true);

            // update the job to the configuration file.
            updateConfigFile(job.getName(), job);
        } catch (UnknownNamespaceException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("UnknownNamespaceException occurs while updating a job.", e);
        } catch (ConfigManagerException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("ConfigManagerException occurs while updating a job.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            log.log(Level.TRACE, "Exit ConfigManagerScheduler#updateJob method.");
        }
    }

    /**
     * <p>
     * Deletes the job identified by the name of the given <code>job</code> from the scheduler.
     * </p>
     *
     * <p>
     * If the given job is not present in the persistence, it does nothing.
     * </p>
     *
     * @param job The job to delete
     *
     * @throws IllegalArgumentException if the job is null
     * @throws SchedulingException if the job can not be found in persistence
     */
    public void deleteJob(Job job) throws SchedulingException {
        log.log(Level.TRACE, "Enter ConfigManagerScheduler#deleteJob method.");

        try {
            Util.checkObjectNotNull(job, "job");

            String jobName = job.getName();

            // the job name must be present in the config file
            if (checkJobExist(jobName)) {
                // delete the job from config file
                updateConfigFile(jobName, null);
            } else {
                // throw SchedulingException if the job name is not present
                throw new SchedulingException("The job [" + jobName + "] can not be found.");
            }
        } catch (UnknownNamespaceException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("UnknownNamespaceException occurs while deleting a job.", e);
        } catch (ConfigManagerException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("ConfigManagerException occurs while deleting a job.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            log.log(Level.TRACE, "Exit ConfigManagerScheduler#deleteJob method.");
        }
    }

    /**
     * <p>
     * Gets the job with the given job name.
     * </p>
     *
     * <p>
     * When the given job name isn't present, then null will be returned.
     * </p>
     *
     * <p>
     * Note, in this method, the associated groups with the job to get will be retrieved too.
     * But the Jobs of the associated groups will not got recursively.
     * </p>
     *
     * @param jobName the name of job to get
     * @return the job with the given name, null if the specified job doesn't exist.
     *
     * @throws IllegalArgumentException if the jobName is null or empty
     * @throws SchedulingException If the scheduler is unable to get this job from persistence
     */
    public Job getJob(String jobName) throws SchedulingException {
        log.log(Level.TRACE, "Enter ConfigManagerScheduler#getJob method.");

        try {
            Util.checkStringNotNullAndEmpty(jobName, "jobName");

            Property jobProp = ConfigManager.getInstance().getPropertyObject(configNamespace, jobName);

            // the given job is not present
            if (jobProp == null) {
                return null;
            }

            SchedulerHelper helper = new SchedulerHelper();
            helper.setJobName(jobName);

            String jobType = getPropertyValue(jobProp, JOB_TYPE, true);
            helper.setJobType(jobType);

            String jobCommand = getPropertyValue(jobProp, JOB_CMD, true);
            helper.setJobCommand(jobCommand);

            String startDate = getPropertyValue(jobProp, START_DATE, false);
            helper.setStartDate(startDate);

            String endDate = getPropertyValue(jobProp, END_DATE, false);
            helper.setEndDate(endDate);

            String startTime = getPropertyValue(jobProp, START_TIME, false);
            helper.setStartTime(startTime);

            String asyncMode = getPropertyValue(jobProp, ASYNC_MODE, false);
            helper.setAsyncMode(asyncMode);

            String active = getPropertyValue(jobProp, ACTIVE, true);
            helper.setActive(active);

            String recurrence = getPropertyValue(jobProp, RECURRENCE, true);
            helper.setRecurrence(recurrence);

            Property intervalProp = getRequiredProperty(jobProp, INTERVAL);

            String intervalValue = getPropertyValue(intervalProp, VALUE, true);
            helper.setInterval(intervalValue);

            String modificationDate = getPropertyValue(jobProp, MODIFICATION_DATE, true);
            helper.setModificationDate(modificationDate);

            Property unitProp = getRequiredProperty(intervalProp, UNIT);

            String type = getPropertyValue(unitProp, TYPE, true);
            helper.setDateUnit(type);

            String days = getPropertyValue(unitProp, DATE_UNIT_DAYS, false);
            helper.setDateUnitDays(days);

            String week = getPropertyValue(unitProp, DATE_UNIT_WEEK, false);
            helper.setDateUnitWeek(week);

            String month = getPropertyValue(unitProp, DATE_UNIT_MONTH, false);
            helper.setDateUnitMonth(month);

            Property dependenceProp = jobProp.getProperty("Dependence");
            if (dependenceProp != null) {
                List subProps = dependenceProp.list();

                if (subProps.size() != 0) {
                    // Create a new Dependence.
                    Property subProp = (Property) subProps.get(0);

                    // Get job name.
                    helper.setDependenceJobName(subProp.getName());

                    helper.setDependenceJobStatus(getRequiredProperty(subProp, STATUS).getValue());
                    helper.setDependenceJobDelay(getRequiredProperty(subProp, DELAY).getValue());
                }
            }

            Job job = helper.createJob();

            List messages = readEmailEventHandler(jobProp.getProperty("Messages"));
            if (messages != null) {
                for (Iterator it = messages.iterator(); it.hasNext();) {
                    EventHandler handler = (EventHandler) it.next();
                    job.addHandler(handler);
                }
            }

            loadJobGroups(job);

            return job;
        } catch (UnknownNamespaceException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("UnknownNamespaceException occurs while getting a job.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            log.log(Level.TRACE, "Exit ConfigManagerScheduler#getJob method.");
        }
    }

    /**
     * <p>
     * Gets all the jobs in this <code>Scheduler</code> in an array.
     * </p>
     *
     * <p>
     * Note, the return array will never be null, but may be empty.
     * </p>
     *
     * @return an array containing all Jobs in this scheduler.
     * @throws SchedulingException If the scheduler is unable to get these jobs from persistence
     */
    public Job[] getJobList() throws SchedulingException {
        log.log(Level.TRACE, "Enter ConfigManagerScheduler#getJobList method.");

        List jobs = new ArrayList();

        try {
            Enumeration enumeration = ConfigManager.getInstance().getPropertyNames(configNamespace);

            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();

                if (DEFINED_GROUPS.equals(key) || LOGGER.equals(key)) {
                    continue;
                }

                jobs.add(getJob(key));
            }

            return (Job[]) jobs.toArray(new Job[jobs.size()]);

        } catch (UnknownNamespaceException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("UnknownNamespaceException occurs while getting all the jobs.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            log.log(Level.TRACE, "Exit ConfigManagerScheduler#getJobList method.");
        }
    }

    /**
     * <p>
     * Adds the given group to this scheduler.
     * </p>
     *
     * @param group the group to add
     *
     * @throws IllegalArgumentException if the group is null
     * @throws SchedulingException If the jobs referred by the group can not be found in persistence or
     * the scheduler is unable to add this group to persistence
     */
    public void addGroup(JobGroup group) throws SchedulingException {
        log.log(Level.TRACE, "Enter ConfigManagerScheduler#addGroup method.");

        try {
            // check the group argument
            checkJobGroup(group);

            // add the group to config file
            updateConfigFileGroups(null, group);
        } catch (UnknownNamespaceException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("UnknownNamespaceException occurs while adding a group.", e);
        } catch (ConfigManagerException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("ConfigManagerException occurs while adding a group.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            log.log(Level.TRACE, "Exit ConfigManagerScheduler#addGroup method.");
        }
    }

    /**
     * <p>
     * Updates the group in this scheduler.
     * </p>
     *
     * @param group the group to update
     *
     * @throws IllegalArgumentException if the group is null
     * @throws SchedulingException If the jobs referred by the group can not be found in persistence or
     * the scheduler is unable to update this group in persistence
     */
    public void updateGroup(JobGroup group) throws SchedulingException {
        log.log(Level.TRACE, "Enter ConfigManagerScheduler#updateGroup method.");

        try {
            checkJobGroup(group);

            updateConfigFileGroups(group.getName(), group);
        } catch (UnknownNamespaceException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("UnknownNamespaceException occurs while updating a group.", e);
        } catch (ConfigManagerException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("ConfigManagerException occurs while updating a group.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            log.log(Level.TRACE, "Exit ConfigManagerScheduler#updateGroup method.");
        }
    }

    /**
     * <p>
     * Removes the group with the given name.
     * </p>
     *
     * <p>
     * If the given name is not present in the persistence, it does nothing.
     * </p>
     *
     * @param name the name of the group to remove
     *
     * @throws IllegalArgumentException if name is null or empty
     * @throws SchedulingException If the scheduler is unable to delete this group from persistence
     */
    public void deleteGroup(String name) throws SchedulingException {
        log.log(Level.TRACE, "Enter ConfigManagerScheduler#deleteGroup method.");

        try {
            Util.checkStringNotNullAndEmpty(name, "name");

            updateConfigFileGroups(name, null);
        } catch (UnknownNamespaceException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("UnknownNamespaceException occurs while deleting a group.", e);
        } catch (ConfigManagerException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("ConfigManagerException occurs while deleting a group.", e);
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            log.log(Level.TRACE, "Exit ConfigManagerScheduler#deleteGroup method.");
        }
    }

    /**
     * <p>
     * Gets all the groups defined in this scheduler.
     * </p>
     *
     * @return all the groups defined in this scheduler
     *
     * @throws SchedulingException If the scheduler is unable to retrieve groups from persistence
     */
    public JobGroup[] getAllGroups() throws SchedulingException {
        log.log(Level.TRACE, "Enter ConfigManagerScheduler#getAllGroups method.");

        try {
            // loads all the job groups in the config file
            return loadJobGroups(null);
        } catch (UnknownNamespaceException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("UnknownNamespaceException occurs while getting all groups.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            log.log(Level.TRACE, "Exit ConfigManagerScheduler#getAllGroups method.");
        }
    }

    /**
     * <p>
     * Gets the group with the given name from this scheduler.
     * </p>
     *
     * <p>
     * If it doesn't exist, null will be returned.
     * </p>
     *
     * @param name the name of group to get
     * @return the group with the given name.
     *
     * @throws IllegalArgumentException if name is null or empty
     * @throws SchedulingException If the scheduler is unable to retrieve this group from persistence
     */
    public JobGroup getGroup(String name) throws SchedulingException {
        log.log(Level.TRACE, "Enter ConfigManagerScheduler#getGroup method.");

        try {
            Util.checkStringNotNullAndEmpty(name, "name");

            Property groupProp = ConfigManager.getInstance().getPropertyObject(configNamespace,
                DEFINED_GROUPS + DOT + name);

            // the group is not present
            if (groupProp == null) {
                return null;
            }

            return loadJobGroup(groupProp, null);

        } catch (UnknownNamespaceException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("UnknownNamespaceException occurs while getting a group.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            log.log(Level.TRACE, "Exit ConfigManagerScheduler#getGroup method.");
        }
    }

    /**
     * <p>
     * Gets all the jobs dependant on the given job.
     * </p>
     *
     * @return all the jobs depend on the given job
     * @param job the given job to query
     *
     * @throws IllegalArgumentException if the job is null or the job doesn't exist in this scheduler.
     * @throws SchedulingException If the scheduler is unable to get the jobs from persistence
     */
    public Job[] getAllDependentJobs(Job job) throws SchedulingException {
        log.log(Level.TRACE, "Enter ConfigManagerScheduler#getAllDependentJobs method.");

        try {
            Util.checkObjectNotNull(job, "job");
            String jobName = job.getName();

            // the job name is not present
            if (!checkJobExist(jobName)) {
                throw new IllegalArgumentException("The job " + jobName + " isn't present in the config manager.");
            }

            List res = new ArrayList();

            Job[] jobs = this.getJobList();
            for (int i = 0; i < jobs.length; i++) {
                // If the job has a dependence with the give job, add it to the result list.
                Dependence dependence = jobs[i].getDependence();
                if ((dependence != null) && dependence.getDependentJobName().equals(job.getName())) {
                    res.add(jobs[i]);
                }
            }

            return (Job[]) res.toArray(new Job[res.size()]);
        } catch (UnknownNamespaceException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("UnknownNamespaceException occurs while getting all dependent jobs.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            log.log(Level.TRACE, "Exit ConfigManagerScheduler#getAllDependentJobs method.");
        }
    }

    /**
     * <p>
     * Updates <tt>DefinedGroups</tt> information in the config file.
     * </p>
     *
     * <p>
     * When the given old group name is not null, then that group will be deleted from config file.
     * When the given new group is not null, then that group will be added to config file.
     * </p>
     *
     * @param oldGroupName the name of the group to delete
     * @param newGroup the group to add
     *
     * @throws UnknownNamespaceException if the <code>configNamespace</code> has not been added to
     * the ConfigManager.
     * @throws ConfigManagerException if any other error on the ConfigManager.
     */
    private void updateConfigFileGroups(String oldGroupName, JobGroup newGroup) throws ConfigManagerException {
        ConfigManager cm = ConfigManager.getInstance();
        cm.refresh(configNamespace);

        cm.createTemporaryProperties(configNamespace);

        // remove the old group
        if (oldGroupName != null) {
            cm.removeProperty(configNamespace, DEFINED_GROUPS + DOT + oldGroupName);
        }

        // add the new group
        if (newGroup != null) {
            String groupName = newGroup.getName();

            // add all the jobs in the group to config file
            Job[] jobs = newGroup.getJobs();
            for (int i = 0; i < jobs.length; i++) {
                cm.addToProperty(configNamespace, DEFINED_GROUPS + DOT + groupName + DOT + JOBS, jobs[i].getName());
            }

            // add all the email event handlers to config file
            EventHandler[] handlers = newGroup.getHandlers();
            updateMessageProperty(handlers, DEFINED_GROUPS + DOT + groupName);
        }

        // Save file.
        cm.commit(configNamespace, PACKAGE_NAME);
    }

    /**
     * <p>
     * Updates the config file according to the <code>oldJobName</code> and <code>newJob</code>.
     * </p>
     *
     * <p>
     * When the given old job name is not null, then that job will be deleted from config file.
     * When the given new job is not null, then that job will be added to config file.
     * </p>
     *
     * <p>
     * @param oldJobName The name of the old job to be deleted, null if adding a job.
     * @param newJob The new job to be added, null if deleting a job.
     * </p>
     *
     * @throws UnknownNamespaceException if the <code>configNamespace</code> has not been added to
     * the ConfigManager.
     * @throws ConfigManagerException if any other error on the ConfigManager.
     * @throws SchedulingException if the group name referred by the new job is not present
     */
    private void updateConfigFile(String oldJobName, Job newJob) throws ConfigManagerException,
        SchedulingException {
        ConfigManager cm = ConfigManager.getInstance();
        cm.refresh(configNamespace);

        // get the DateFormat instance for formatting the Date instance
        DateFormat df = SchedulerHelper.getDateFormat();

        cm.createTemporaryProperties(configNamespace);

        // Delete old job.
        if (oldJobName != null) {
            cm.removeProperty(configNamespace, oldJobName);
        }

        // Add new Job
        if (newJob != null) {
            String jobName = newJob.getName();

            Dependence dep = newJob.getDependence();

            if (dep == null) {
                // add the start date property when the dependence is not present
                GregorianCalendar startDate = newJob.getStartDate();
                cm.addToProperty(configNamespace, jobName + DOT + START_DATE, df.format(startDate.getTime()));

                // add the start time property when the dependence is not present
                int startTime = newJob.getStartTime();
                cm.addToProperty(configNamespace, jobName + DOT + START_TIME, String.valueOf(startTime));

                // add the end date property when the dependence is not present
                GregorianCalendar endDate = newJob.getStopDate();
                cm.addToProperty(configNamespace, jobName + DOT + END_DATE, df.format(endDate.getTime()));
            }

            // add the job type property
            String jobType = SchedulerHelper.parseJobType(newJob.getJobType());
            cm.addToProperty(configNamespace, jobName + DOT + JOB_TYPE, jobType);

            // add the job command property
            String jobCommand = newJob.getRunCommand();
            cm.addToProperty(configNamespace, jobName + DOT + JOB_CMD, jobCommand);

            // add the active property
            boolean active = newJob.getActive();
            cm.addToProperty(configNamespace, jobName + DOT + ACTIVE, SchedulerHelper.parseBooleanValue(active));

            // add the asyncMode property
            boolean asyncMode = newJob.isAsyncMode();
            cm.addToProperty(configNamespace, jobName + DOT + ASYNC_MODE, SchedulerHelper.parseBooleanValue(asyncMode));

            // add the recurrence property
            int recurrence = newJob.getRecurrence();
            cm.addToProperty(configNamespace, jobName + DOT + RECURRENCE, String.valueOf(recurrence));

            // add the interval value property
            int intervalValue = newJob.getIntervalValue();
            cm.addToProperty(configNamespace, jobName + DOT + INTERVAL + DOT + VALUE, String.valueOf(intervalValue));

            DateUnit dateUnit = newJob.getIntervalUnit();
            if (dateUnit != null) {
                // add the date unit properties when it is set
                String[] dateUnits = SchedulerHelper.parseDateUnit(dateUnit);

                // the type property
                cm.addToProperty(configNamespace, jobName + DOT + INTERVAL + DOT + UNIT + DOT + TYPE, dateUnits[0]);

                // the days property
                if (dateUnits[1] != null) {
                    cm.addToProperty(configNamespace, jobName + DOT + INTERVAL + DOT + UNIT + DOT + DATE_UNIT_DAYS,
                        dateUnits[1]);
                }

                // the week property
                if (dateUnits[2] != null) {
                    cm.addToProperty(configNamespace, jobName + DOT + INTERVAL + DOT + UNIT + DOT + DATE_UNIT_WEEK,
                        dateUnits[2]);
                }

                // the month property
                if (dateUnits[3] != null) {
                    cm.addToProperty(configNamespace, jobName + DOT + INTERVAL + DOT + UNIT + DOT + DATE_UNIT_MONTH,
                        dateUnits[3]);
                }
            }

            // add the modification property
            GregorianCalendar modificationDate = new GregorianCalendar();
            newJob.setModificationDate(modificationDate);
            cm.addToProperty(configNamespace, jobName + DOT + MODIFICATION_DATE, df.format(modificationDate.getTime()));

            if (dep != null) {
                // add the dependence related properties
                cm.addToProperty(configNamespace, jobName + DOT + DEPENDENCE + DOT + dep.getDependentJobName() + DOT
                    + STATUS, dep.getDependentEvent());
                cm.addToProperty(configNamespace, jobName + DOT + DEPENDENCE + DOT + dep.getDependentJobName() + DOT
                    + DELAY, String.valueOf(dep.getDelay()));
            }

            // add the email event information
            EventHandler[] handlers = newJob.getHandlers();
            updateMessageProperty(handlers, jobName + "Messages");

            JobGroup[] jobGroups = newJob.getGroups();
            for (int i = 0; i < jobGroups.length; i++) {
                String groupName = jobGroups[i].getName();

                // validate the group name
                if (cm.getProperty(configNamespace, DEFINED_GROUPS + DOT + groupName) == null) {
                    throw new SchedulingException("The group [" + groupName + "] is not defined in the config file.");
                }

                // add this job to the groups
                cm.addToProperty(configNamespace, DEFINED_GROUPS + DOT + groupName + DOT + JOBS, newJob.getName());
            }
        }

        cm.commit(configNamespace, PACKAGE_NAME);
    }

    /**
     * <p>
     * Updates the <tt>Messages</tt> property in config file.
     * </p>
     *
     * @param handlers the event handlers.
     * @param key the property key of the <tt>Messages</tt> property.
     *
     * @throws UnknownNamespaceException if the <code>configNamespace</code> has not been added to
     * the ConfigManager.
     */
    private void updateMessageProperty(EventHandler[] handlers, String key) throws UnknownNamespaceException {
        ConfigManager cm = ConfigManager.getInstance();

        for (int i = 0; i < handlers.length; i++) {
            EventHandler handler = handlers[i];
            if (handler instanceof EmailEventHandler) {
                EmailEventHandler emailHandler = (EmailEventHandler) handler;

                List recipient = emailHandler.getRecipients();
                String event = emailHandler.getRequiredEvent();
                String template = emailHandler.getTemplate().getTemplate();

                // add the template file name property
                cm.addToProperty(configNamespace, key + DOT + MESSAGES + DOT + event + DOT + TEMPLATE_TEXT, template);

                // add the form address property
                cm.addToProperty(configNamespace, key + DOT + MESSAGES + DOT + event + DOT + FROM_ADDRESS,
                    emailHandler.getEmailFromAddress());

                // add the subject property
                cm.addToProperty(configNamespace, key + DOT + MESSAGES + DOT + event + DOT + SUBJECT,
                    emailHandler.getEmailAlertSubject());

                // add the recipients property
                for (Iterator iter = recipient.iterator(); iter.hasNext();) {
                    String element = (String) iter.next();
                    cm.addToProperty(configNamespace, key + DOT + MESSAGES + DOT + event + DOT + RECIPIENTS, element);
                }
            }
        }
    }

    /**
     * <p>
     * Reads <code>JobGroup</code> instances from config file.
     * </p>
     *
     * <p>
     * If the given job is not null, then all the job groups that contain it will be returned, otherwise
     * all the job groups in the config file will be returned.
     * </p>
     *
     * <p>
     * Note, the return value will never be null but may be empty.
     * </p>
     *
     * @param job the job to be loaded
     *
     * @return all the job groups that contain it
     *
     * @throws SchedulingException if any error occurs when parsing config file.
     * @throws UnknownNamespaceException if the <code>configNamespace</code> has not been added to
     * the ConfigManager.
     */
    private JobGroup[] loadJobGroups(Job job) throws SchedulingException, UnknownNamespaceException {
        Property groups = ConfigManager.getInstance().getPropertyObject(configNamespace, DEFINED_GROUPS);
        if (groups == null) {
            return new JobGroup[0];
        }

        List jobGroups = new ArrayList();
        List groupList = groups.list();

        for (Iterator iter = groupList.iterator(); iter.hasNext();) {
            Property group = (Property) iter.next();

            JobGroup jobGroup = loadJobGroup(group, job);
            if (jobGroup != null) {
                jobGroups.add(jobGroup);
                if (job != null) {
                    job.addGroup(jobGroup);
                }
            }
        }

        return (JobGroup[]) jobGroups.toArray(new JobGroup[jobGroups.size()]);
    }

    /**
     * <p>
     * This method loads the <code>JobGroup</code> under the given <code>groupProp</code>
     * </p>
     *
     * <p>
     * If the given <code>job</code> is not null, then if the job group contains the given job, it
     * will loads the job group from config file and returns it, otherwise null will be returned.
     * </p>
     *
     * <p>
     * If the given <code>job</code> is null, then the job group will be loaded from config file
     * and it will be returned.
     * </p>
     *
     * @param groupProp the property instance that contains the information to construct a <code>JobGroup</code>
     * @param job the given job to check whether the job group contains it or not, may be null
     * @return the job group for the given group property, if job is not contained by the job group when job is not
     * null, then null will be returned
     *
     * @throws SchedulingException if any error occurs when parsing config file.
     * @throws UnknownNamespaceException if the <code>configNamespace</code> has not been added to
     * the ConfigManager.
     */
    private JobGroup loadJobGroup(Property groupProp, Job job) throws SchedulingException, UnknownNamespaceException {
        String groupName = groupProp.getName();

        // Create Message and Jobs property.
        Property jobs = getRequiredProperty(groupProp, JOBS);
        Property message = groupProp.getProperty(MESSAGES);

        String[] jobNames = jobs.getValues();

        if (job != null && !findJobName(jobNames, job)) {
            return null;
        }

        String jobName = (job == null) ? null : job.getName();

        List jobList = new ArrayList();
        for (int i = 0; i < jobNames.length; i++) {
            if (jobNames[i].equals(jobName)) {
                jobList.add(job);
            } else {
                jobList.add(loadSimpleJob(getRequiredProperty(jobNames[i])));
            }
        }

        // Create a new JobGroup.
        JobGroup jobGroup = new JobGroup(groupName, jobList);

        if (message != null) {
            List emailHandlerList = readEmailEventHandler(message);
            for (Iterator iterator = emailHandlerList.iterator(); iterator.hasNext();) {
                EventHandler handler = (EventHandler) iterator.next();
                jobGroup.addHandler(handler);
            }
        }

        return jobGroup;
    }

    /**
     * <p>
     * Checks whether the name of the job is contained in the given job names array.
     * </p>
     *
     * @param jobNames the job names array
     * @param job the job to check
     *
     * @return true if the name of the job is contained in the given job names array, false otherwise.
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
     * Reads email event handler from config file.
     * </p>
     *
     * <p>
     * All the email event handlers got from config file will be returned as a <code>List</code>.
     * </p>
     *
     * <p>
     * The return list contains only <code>EmailEventHandler</code> instances.
     * </p>
     *
     * <p>
     * Note, if the <code>msgProp</code> is null, then null will be returned.
     * </p>
     *
     * @param msgProp Property contains email event handler information.
     * @return the List that contains <code>EmailEventHandler</code> instances.
     *
     * @throws SchedulingException If the template format is incorrect.
     */
    private List readEmailEventHandler(Property msgProp) throws SchedulingException {
        if (msgProp == null) {
            return null;
        }

        List handlerList = new ArrayList();
        for (Iterator iter = msgProp.list().iterator(); iter.hasNext();) {
            Property event = (Property) iter.next();

            String eventName = event.getName();

            // Get the template text field.
            String templateText = event.getValue(TEMPLATE_TEXT);

            // If TemplateFileName is missing, use the default template.
            Template template = defaultTemplate;
            if (templateText != null) {
                // Create a new Template.
                // Read template from file
                template = SchedulerHelper.readTextToTemplate(templateText);
            }

            // Get Recipients field.
            String[] recipients = event.getValues(RECIPIENTS);
            if (recipients == null) {
                throw new SchedulingException(MESSAGES + DOT + eventName + DOT + RECIPIENTS + " is missing.");
            }
            for (int i = 0; i < recipients.length; i++) {
                if (recipients[i].trim().length() == 0) {
                    throw new SchedulingException("The property values for " + MESSAGES + DOT + eventName + DOT
                        + RECIPIENTS + " contain empty string.");
                }
            }

            String fromAddress = getPropertyValue(event, FROM_ADDRESS, true);
            String subject = getPropertyValue(event, SUBJECT, true);

            EmailEventHandler handler = new EmailEventHandler(template, Arrays.asList(recipients), eventName,
                fromAddress, subject, log);

            handlerList.add(handler);
        }

        return handlerList;
    }

    /**
     * <p>
     * Checks whether the given job is a valid job.
     * </p>
     *
     * @param job the job to be checked.
     * @param occurs the flag to identify whether the given job should be present on the config file or not
     *
     * @throws IllegalArgumentException if if the given job is null, or the job has a same name with existing
     * job when occurs is false, or the job doesn't have a same name with existing job when occurs is true,
     * or the job has an invalid dependence or has both a schedule time and a dependence
     *
     * @throws UnknownNamespaceException if the <code>configNamespace</code> has not been added to
     * the ConfigManager.
     */
    private void checkJobValid(Job job, boolean occurs) throws UnknownNamespaceException {
        Util.checkObjectNotNull(job, "job");

        String jobName = job.getName();
        boolean isJobExist = checkJobExist(jobName);
        if (isJobExist && !occurs) {
            throw new IllegalArgumentException("The job " + job.getName() + " has a same name with existing jobs.");
        } else if (!isJobExist && occurs) {
            throw new IllegalArgumentException("The job " + job.getName() + " isn't present in the config manager.");
        }

        Dependence dep = job.getDependence();
        if (dep != null) {
            String dependentJobName = dep.getDependentJobName();

            // the dependent job name is not present
            if (!checkJobExist(dependentJobName)) {
                throw new IllegalArgumentException("The dependence job " + dependentJobName + " of " + jobName
                    + " does not exist.");
            }

            // both dependence and start date are present
            if (job.getStartDate() != null) {
                throw new IllegalArgumentException("The job has both a schedule time and a dependence.");
            }
        } else {
            // both dependence and start date are absent
            if (job.getStartDate() == null) {
                throw new IllegalArgumentException("Both schedule time and dependence job are absent.");
            }
        }
    }

    /**
     * <p>
     * Checks if the job is exist in the config file.
     * </p>
     *
     * @param jobName the name of the job to check
     *
     * @return the job is exist in the config file or not
     *
     * @throws UnknownNamespaceException if the <code>configNamespace</code> has not been added to
     * the ConfigManager.
     */
    private boolean checkJobExist(String jobName) throws UnknownNamespaceException {
        return (ConfigManager.getInstance().getPropertyObject(configNamespace, jobName) != null);
    }

    /**
     * <p>
     * Checks whether the given group is valid.
     * </p>
     *
     * @param group the group to be checked.
     *
     * @throws IllegalArgumentException if the given group is null
     * @throws SchedulingException if the given group refers to any jobs that are not present in the
     * config file
     * @throws UnknownNamespaceException if the <code>configNamespace</code> has not been added to
     * the ConfigManager.
     */
    private void checkJobGroup(JobGroup group) throws SchedulingException, UnknownNamespaceException {
        Util.checkObjectNotNull(group, "group");

        Job[] jobs = group.getJobs();

        // all the referred jobs must be present
        for (int i = 0; i < jobs.length; i++) {
            if (!checkJobExist(jobs[i].getName())) {
                throw new SchedulingException("group contains a job, " + jobs[i].getName()
                    + ", which does not exist in the scheduler.");
            }
        }
    }

    /**
     * <p>
     * This methods loads a <code>Job</code> instance from the configuration file with only <b>jobName</b>,
     * <b>jobType</b> and <b>jobCommand</b> set.
     * </p>
     *
     * @param jobProp the property contains the information for constructing a <code>Job</code>
     * @return the <code>Job</code> instance with only <b>jobName</b>, <b>jobType</b> and
     * <b>jobCommand</b> set.
     *
     * @throws SchedulingException if any of the job type, job command properties is missing
     */
    private Job loadSimpleJob(Property jobProp) throws SchedulingException {
        String jobName = jobProp.getName();
        JobType jobType = SchedulerHelper.parseJobType(getPropertyValue(jobProp, JOB_TYPE, true));
        String jobCommand = getPropertyValue(jobProp, JOB_CMD, true);

        return new Job(jobName, jobType, jobCommand);
    }

    /**
     * <p>
     * Gets a required child property from the given parent property with the given name.
     * </p>
     *
     * <p>
     * If the child property is missing, then exception will be thrown.
     * </p>
     *
     * @param prop the parent property
     * @param name the name of the child property
     * @return the child property under the parent property with the given name
     *
     * @throws SchedulingException if the child property is missing
     */
    private Property getRequiredProperty(Property prop, String name) throws SchedulingException {
        Property subProp = prop.getProperty(name);
        if (subProp == null) {
            throw new SchedulingException("The property [ " + name + " ] under [" + prop.getName()
                + "] property is missing.");
        }

        return subProp;
    }

    /**
     * <p>
     * Gets a required child property from the given namespace with the given name.
     * </p>
     *
     * <p>
     * If the property is missing, then exception will be thrown.
     * </p>
     *
     * @param name the name of the property
     * @return the child property under the parent property with the given name
     *
     * @throws SchedulingException if the property is missing
     * @throws UnknownNamespaceException if the <code>configNamespace</code> has not been added to
     * the ConfigManager.
     */
    private Property getRequiredProperty(String name) throws SchedulingException, UnknownNamespaceException {
        Property property = ConfigManager.getInstance().getPropertyObject(configNamespace, name);

        if (property == null) {
            throw new SchedulingException("The property [" + name + "] is missing.");
        }

        return property;
    }

    /**
     * <p>
     * Gets the property value under the given parent parent property with the given name.
     * </p>
     *
     * <p>
     * If the property is missing and required is <code>true</code>, then exception will be thrown.
     * </p>
     *
     * @param prop the parent property
     * @param name the name of the child property
     * @param required the prop is required or not
     *
     * @return the child property value under the parent property with the given name
     *
     * @throws SchedulingException if the property is missing or the value is null or empty string
     * when required is true
     */
    private String getPropertyValue(Property prop, String name, boolean required) throws SchedulingException {
        Property subProp = prop.getProperty(name);

        String value = (subProp == null) ? null : subProp.getValue();

        if (required) {
            if (subProp == null) {
                throw new SchedulingException("The property [ " + name + " ] under [" + prop.getName()
                    + "] property is missing.");
            }

            if (value == null || value.trim().length() == 0) {
                throw new SchedulingException("The value for property [ " + name + " ] under [" + prop.getName()
                    + "] property is null or empty.");
            }
        }

        return (value == null) ? null : value.trim();
    }
}
