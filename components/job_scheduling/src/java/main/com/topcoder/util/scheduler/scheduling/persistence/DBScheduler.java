/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.persistence;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.scheduling.ConfigurationException;
import com.topcoder.util.scheduler.scheduling.Dependence;
import com.topcoder.util.scheduler.scheduling.EmailEventHandler;
import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.Scheduler;
import com.topcoder.util.scheduler.scheduling.SchedulingException;
import com.topcoder.util.scheduler.scheduling.Util;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.file.Template;
import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * <p>
 * This class implements the interface <code>Scheduler</code> using the database as the persistent data source.
 * </p>
 *
 * <p>
 * This scheduler will perform all CRUD operations to <code>Job</code> and <code>JobGroup</code>.
 * </p>
 *
 * <p>Changes for version 3.1:
 * As two new job types are added, so when checking the job types, the two new ones should be taken
 * into consideration. The datatype of JOB.JobType db table column is changed from VARCHAR(20) to VARCHAR(50)
 * to hold the new job types.
 * </p>
 *
 * <p>
 * Thread Safety : This class is immutable and thread-safe.
 * </p>
 *
 * @author argolite, TCSDEVELOPER
 * @author Standlove, fuyun
 * @version 3.1
 * @since 3.0
 */
public class DBScheduler implements Scheduler {
    /**
     * <p>
     * Represents the <b>ConnectionFactoryNamespace</b> property name.
     * </p>
     */
    private static final String CONNECTION_FACTORY_NAMESPACE = "ConnectionFactoryNamespace";

    /**
     * <p>
     * Represents the <b>ConnectionFactoryClassName</b> property name.
     * </p>
     */
    private static final String CONNECTION_FACTORY_CLASS_NAME = "ConnectionFactoryClassName";

    /**
     * <p>
     * Represents the <b>Logger</b> property name.
     * </p>
     */
    private static final String LOGGER = "Logger";

    /**
     * <p>
     * Represents the <b>IDGenClassName</b> property name.
     * </p>
     */
    private static final String IDGEN_CLASS_NAME = "IDGenClassName";

    /**
     * <p>
     * Represents the <b>IDGenSeqName</b> property name.
     * </p>
     */
    private static final String IDGEN_SEQ_NAME = "IDGenSeqName";

    /**
     * <p>
     * Represents the <b>ConnectionName</b> property name.
     * </p>
     */
    private static final String CONNECTION_NAME = "ConnectionName";

    /**
     * <p>
     * Represents the sql expression to select the job id by the name.
     * </p>
     */
    private static final String SELECT_JOB_ID = "select jobid from job where name = ?";

    /**
     * <p>
     * Represents the sql expression to insert a job to database.
     * </p>
     */
    private static final String INSERT_JOB = "insert into job (jobid, name, startdate, starttime, "
        + "enddate, dateunit, dateunitdays, dateunitweek, dateunitmonth, job.interval, recurrence, "
        + "active, jobtype, jobcommand, dependencejobname, dependencejobstatus, dependencejobdelay, asyncmode) "
        + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * <p>
     * Represents the sql expression to insert a message to database.
     * </p>
     */
    private static final String INSERT_MESSAGE = "insert into message (messageid, ownerid, name, "
        + "fromaddress, subject, templatetext, recipients) values (?, ?, ?, ?, ?, ?, ?)";

    /**
     * <p>
     * Represents the sql expression to select all the group ids with any one of the names given.
     * </p>
     */
    private static final String SELECT_GROUPS = "select groupid, name from group where name in";

    /**
     * <p>
     * Represents the sql expression to insert a record to GroupJob table.
     * </p>
     */
    private static final String INSERT_GROUP_JOB = "insert into groupjob (groupid, jobid) values (?, ?)";

    /**
     * <p>
     * Represents the sql expression to select a job from database.
     * </p>
     */
    private static final String SELECT_JOB = "select jobid, startdate, starttime, enddate, dateunit, "
        + "dateunitdays, dateunitweek, dateunitmonth, job.interval, recurrence, active, jobtype, jobcommand, "
        + "dependencejobname, dependencejobstatus, dependencejobdelay, asyncmode from job where name = ?";

    /**
     * <p>
     * Represents the sql expression to select a message from database.
     * </p>
     */
    private static final String SELECT_MESSAGE = "select name, fromaddress, subject, "
        + "templatetext, recipients from message where ownerid = ?";

    /**
     * <p>
     * Represents the sql expression to insert a record to GroupJob table.
     * </p>
     */
    private static final String INSERT_JOB_GROUP = "insert into groupjob (groupid, jobid) values (?, ?)";

    /**
     * <p>
     * Represents the sql expression to insert a record to group table.
     * </p>
     */
    private static final String INSERT_GROUP = "insert into group (groupid, name) values (?, ?)";

    /**
     * <p>
     * Represents the sql expression to select a group id with the name given.
     * </p>
     */
    private static final String SELECT_GROUP_ID = "select groupid from group where name = ?";

    /**
     * <p>
     * Represents the sql expression to delete the records from GroupJob table with group id given.
     * </p>
     */
    private static final String DELETE_JOB_GROUP = "delete from groupjob where groupid = ?";

    /**
     * <p>
     * Represents the sql expression to delete the record form Group table with group id given.
     * </p>
     */
    private static final String DELETE_GROUP = "delete from group where groupid = ?";

    /**
     * <p>
     * Represents the sql expression to select the Job records that belong to the given group id.
     * </p>
     */
    private static final String SELECT_JOB_GROUP = "select job.name, job.jobtype, job.jobcommand "
        + "from job, groupjob where job.jobid = groupjob.jobid and groupjob.groupid = ?";

    /**
     * <p>
     * Represents the sql expression to select all group ids in Group table.
     * </p>
     */
    private static final String SELECT_ALL_GROUPS = "select groupid, name from group";

    /**
     * <p>
     * Represents the sql expression to delete the GroupJob records with job id given.
     * </p>
     */
    private static final String DELETE_JOB_GROUP_BY_JOB_ID = "delete from groupjob where jobid = ?";

    /**
     * <p>
     * Represents the sql expression to delete the Message records with job id given.
     * </p>
     */
    private static final String DELETE_MESSAGE = "delete from message where ownerid = ?";

    /**
     * <p>
     * Represents the sql expression to delete the Job record with job id given.
     * </p>
     */
    private static final String DELETE_JOB = "delete from job where jobid = ?";

    /**
     * <p>
     * Represents the sql expression to select all the groups that owns the given job id.
     * </p>
     */
    private static final String SELECT_GROUPS_BY_JOBID = "select group.groupid, group.name from "
        + "groupjob, group where groupjob.groupid = group.groupid and groupjob.jobid = ?";

    /**
     * <p>
     * Represents the sql expression to select all the job names in the job table.
     * </p>
     */
    private static final String SELECT_JOB_NAMES = "select name from job";

    /**
     * <p>
     * Represents the name of the connection that will be used for SQL operations.
     * </p>
     *
     * <p>
     * It is set in the constructor and is immutable.
     * </p>
     *
     * <p>
     * It will never be null or empty.
     * </p>
     */
    private final String connectionName;

    /**
     * <p>
     * Represents the <code>DBConnectionFactory</code> instance that will be used to generate connections.
     * </p>
     *
     * <p>
     * It is instantiated from configuration parameters in the constructor and will never change.
     * </p>
     *
     * <p>
     * Will not be null.
     * </p>
     */
    private final DBConnectionFactory connectionFactory;

    /**
     * <p>
     * Represents the configured <code>IDGenerator</code> that will be used to generate IDs for
     * all tables in the persistence.
     * </p>
     *
     * <p>
     * It is configured in the constructor and is immutable.
     * </p>
     */
    private final IDGenerator idGenerator;

    /**
     * <p>
     * Represents the <code>Log</code> that is used to report method entry/exit, as well as errors, in this class.
     * </p>
     *
     * <p>
     * It is created in the constructor, immutable and will not be null.
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
     * Constructs a <code>DBScheduler</code> with namespace given.
     * </p>
     *
     * @param namespace The namespace of the properties needed by this class.
     *
     * @throws ConfigurationException If there is an error during the construction of this class.
     * @throws IllegalArgumentException if namespace is null or empty.
     */
    public DBScheduler(String namespace) throws ConfigurationException {
        Util.checkStringNotNullAndEmpty(namespace, "namespace");

        this.connectionName = getPropertyValue(namespace, CONNECTION_NAME, null);
        this.connectionFactory = createDBConnectionFactory(namespace);

        String idSeqName = getPropertyValue(namespace, IDGEN_SEQ_NAME, null);
        String idGenClassName = getPropertyValue(namespace, IDGEN_CLASS_NAME, "");

        try {
            this.idGenerator = (idGenClassName.length() == 0) ? IDGeneratorFactory.getIDGenerator(idSeqName)
                : IDGeneratorFactory.getIDGenerator(idSeqName, idGenClassName);
        } catch (IDGenerationException e) {
            throw new ConfigurationException("Unable to create id generator in DBScheduler.", e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("Unable to create id generator in DBScheduler, class is missing.", e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationException("Unable to create id generator in DBScheduler, method is not found.", e);
        } catch (InstantiationException e) {
            throw new ConfigurationException("Unable to create id generator in DBScheduler, the given "
                + "class cannot be instantiated.", e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationException("Unable to create id generator in DBScheduler "
                + "caused by IllegalAccessException.", e);
        } catch (InvocationTargetException e) {
            throw new ConfigurationException("Unable to create id generator in DBScheduler "
                + "caused by InvocationTargetException.", e);
        }

        String loggerName = getPropertyValue(namespace, LOGGER, DBScheduler.class.getName());
        this.log = LogManager.getLog(loggerName);
        this.defaultTemplate = SchedulerHelper.createDefaultTemplate();
    }

    /**
     * <p>
     * Adds the given job to the scheduler.
     * </p>
     *
     * @param job The job to add.
     *
     * @throws IllegalArgumentException if the added job is null or has a same name with existing job or has an
     * invalid dependence or has both a schedule time and a dependence
     * @throws SchedulingException If the scheduler is unable to add this job in persistence
     */
    public void addJob(Job job) throws SchedulingException {
        log.log(Level.TRACE, "Enter DBScheduler#addJob method.");

        Connection conn = null;

        try {
            conn = getConnection();

            addOrUpdateJob(conn, job, true);

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);

            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("SQLException occurs when adding a job.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            closeConnection(conn);
            log.log(Level.TRACE, "Exit DBScheduler#addJob method.");
        }
    }

    /**
     * <p>
     * Updates the given job in the scheduler.
     * </p>
     *
     * @param job The job to update.
     *
     * @throws IllegalArgumentException if the added job is null or doesn't have a same name with existing job
     * or has an invalid dependence or has both a schedule time and a dependence
     * @throws SchedulingException If the scheduler is unable to update this job in persistence
     */
    public void updateJob(Job job) throws SchedulingException {
        log.log(Level.TRACE, "Enter DBScheduler#updateJob method.");

        Connection conn = null;

        try {
            conn = getConnection();

            addOrUpdateJob(conn, job, false);

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("SQLException occurs when updating a job.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            closeConnection(conn);
            log.log(Level.TRACE, "Exit DBScheduler#updateJob method.");
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
     * @throws IllegalArgumentException if the parameters are invalid.
     * @throws SchedulingException if the job can not be found in persistence
     */
    public void deleteJob(Job job) throws SchedulingException {
        log.log(Level.TRACE, "Enter DBScheduler#deleteJob method.");
        // select jobid from job where name = ?

        // delete from job where jobid = ?
        // delete from message where ownerid = ?
        // delete from groupjob where jobid = ?
        Connection conn = null;

        try {
            Util.checkObjectNotNull(job, "job");

            conn = getConnection();

            Long jobId = getJobId(conn, job.getName());

            if (jobId != null) {
                deleteJob(conn, jobId);
            } else {
                throw new SchedulingException("The job " + job.getName() + " can not be found in persistence.");
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("SQLException occurs when deleting a job.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            closeConnection(conn);
            log.log(Level.TRACE, "Exit DBScheduler#deleteJob method.");
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
     * @return the job with the given name, null if the specified job doesn't exist.
     * @param jobName the name of job to get
     *
     * @throws IllegalArgumentException if the jobName is null or empty
     * @throws SchedulingException If the scheduler is unable to get this job from persistence
     */
    public Job getJob(String jobName) throws SchedulingException {
        log.log(Level.TRACE, "Enter DBScheduler#getJob method.");

        Connection conn = null;
        try {
            Util.checkStringNotNullAndEmpty(jobName, "jobName");

            conn = getConnection();

            return getJob(conn, jobName);

        } catch (SQLException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("SQLException occurs when getting a job.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            closeConnection(conn);
            log.log(Level.TRACE, "Exit DBScheduler#getJob method.");
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
        log.log(Level.TRACE, "Enter DBScheduler#getJobList method.");
        Connection conn = null;

        try {
            conn = getConnection();

            return getJobList(conn);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (SQLException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("SQLException occurs when getting all the jobs.", e);
        } finally {
            closeConnection(conn);
            log.log(Level.TRACE, "Exit DBScheduler#getJobList method.");
        }
    }

    /**
     * <p>
     * Adds the given group to this scheduler.
     * </p>
     *
     * @param group the group to add
     *
     * @throws IllegalArgumentException if the group is invalid.
     * @throws SchedulingException If the scheduler is unable to add this group to persistence
     */
    public void addGroup(JobGroup group) throws SchedulingException {
        log.log(Level.TRACE, "Enter DBScheduler#getJobList method.");

        Connection conn = null;

        try {
            conn = getConnection();

            addOrUpdateJobGroup(conn, group, true);

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("SQLException occurs when adding a group.", e);
        } catch (IDGenerationException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("SQLException occurs when getting a group id.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            closeConnection(conn);
            log.log(Level.TRACE, "Exit DBScheduler#getJobList method.");
        }
    }

    /**
     * <p>
     * Updates the group in this scheduler.
     * </p>
     *
     * @param group the group to update
     * @throws IllegalArgumentException if the group is invalid
     * @throws SchedulingException If the scheduler is unable to update this group in persistence
     */
    public void updateGroup(JobGroup group) throws SchedulingException {
        log.log(Level.TRACE, "Enter DBScheduler#updateGroup method.");
        Connection conn = null;

        try {
            conn = getConnection();

            addOrUpdateJobGroup(conn, group, false);

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("SQLException occurs when updating a group.", e);
        } catch (IDGenerationException e) {
            // this exception will never be thrown, since updateGroup() will never require a new group id
            // so this exception is ignored here.
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            closeConnection(conn);
            log.log(Level.TRACE, "Exit DBScheduler#updateGroup method.");
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
     * @throws IllegalArgumentException if name is null or empty
     * @throws SchedulingException If the scheduler is unable to delete this group from persistence
     */
    public void deleteGroup(String name) throws SchedulingException {
        log.log(Level.TRACE, "Enter DBScheduler#deleteGroup method.");

        Connection conn = null;

        try {
            Util.checkStringNotNullAndEmpty(name, "name");

            conn = getConnection();

            // select groupid from group where name = ?
            Long groupId = getGroupId(conn, name);

            if (groupId != null) {
                deleteGroup(conn, groupId);

                conn.commit();
            }
        } catch (SQLException e) {
            rollback(conn);
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("SQLException occurs when deleting a group.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            closeConnection(conn);
            log.log(Level.TRACE, "Exit DBScheduler#deleteGroup method.");
        }

    }

    /**
     * <p>
     * Gets all the groups defined in this scheduler.
     * </p>
     *
     * @return all the groups defined in this scheduler
     * @throws SchedulingException If the scheduler is unable to retrieve groups from persistence
     */
    public JobGroup[] getAllGroups() throws SchedulingException {
        log.log(Level.TRACE, "Enter DBScheduler#getAllGroups method.");

        Connection conn = null;
        try {
            conn = getConnection();

            Map mapping = getGroupIds(conn);

            JobGroup[] groups = new JobGroup[mapping.size()];
            int i = 0;
            for (Iterator it = mapping.entrySet().iterator(); it.hasNext();) {
                Entry entry = (Entry) it.next();

                Long groupId = (Long) entry.getKey();
                String name = (String) entry.getValue();

                groups[i++] = getGroup(conn, groupId, name);
            }

            return groups;
        } catch (SQLException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("SQLException occurs when getting all the groups.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            closeConnection(conn);

            log.log(Level.TRACE, "Exit DBScheduler#getAllGroups method.");
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
     * @return the group with the given name.
     * @param name the name of group to get
     *
     * @throws IllegalArgumentException if name is null or empty
     * @throws SchedulingException If the scheduler is unable to retrieve this group from persistence
     */
    public JobGroup getGroup(String name) throws SchedulingException {
        log.log(Level.TRACE, "Enter DBScheduler#getGroup method.");

        Connection conn = null;

        try {
            Util.checkStringNotNullAndEmpty(name, "name");

            conn = getConnection();

            // select groupid from group where name = ?
            Long groupId = getGroupId(conn, name);

            if (groupId != null) {
                return getGroup(conn, groupId, name);
            } else {
                return null;
            }

        } catch (SQLException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("SQLException occurs when getting a group.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            closeConnection(conn);
            log.log(Level.TRACE, "Exit DBScheduler#getGroup method.");
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
        log.log(Level.TRACE, "Enter DBScheduler#getAllDependentJobs method.");
        Connection conn = null;

        try {
            Util.checkObjectNotNull(job, "job");

            conn = getConnection();

            String jobName = job.getName();

            // the job name is not present
            if (getJobId(conn, jobName) == null) {
                throw new IllegalArgumentException("The job " + jobName + " isn't configed in the database.");
            }

            List res = new ArrayList();

            Job[] jobs = getJobList(conn);

            for (int i = 0; i < jobs.length; i++) {
                // If the job has a dependence with the give job, add it to the result list.
                Dependence dependence = jobs[i].getDependence();
                if ((dependence != null) && dependence.getDependentJobName().equals(job.getName())) {
                    res.add(jobs[i]);
                }
            }

            return (Job[]) res.toArray(new Job[res.size()]);
        } catch (SQLException e) {
            log.log(Level.ERROR, e.getMessage());
            throw new SchedulingException("SQLException occurs while getting all dependent jobs.", e);
        } catch (SchedulingException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.log(Level.ERROR, e.getMessage());
            throw e;
        } finally {
            closeConnection(conn);
            log.log(Level.TRACE, "Exit DBScheduler#getAllDependentJobs method.");
        }
    }

    /**
     * <p>
     * Returns the value of the property.
     * </p>
     *
     * <p>
     * When the given defaultValue argument is null, it means the property is required, otherwise
     * it means the property is optional.
     * </p>
     *
     * <p>
     * If the property is missing but the property is optional, the default value is returned.
     * </p>
     *
     * @param namespace the namespace to get the property value from.
     * @param propertyName the name of property
     * @param defaultValue the default value for the property, if it is null, it means the property
     * is required, otherwise it means the property is optional
     *
     * @return the value of the property
     *
     * @throws ConfigurationException if fails to load the config values.
     */
    private String getPropertyValue(String namespace, String propertyName, String defaultValue)
        throws ConfigurationException {
        try {
            String property = ConfigManager.getInstance().getString(namespace, propertyName);

            // Property is missing
            if (property == null) {
                if (defaultValue == null) {
                    // the property is required
                    throw new ConfigurationException("The property for [" + propertyName
                        + "] is missing but it is required.");
                } else {
                    // the property is optional
                    return defaultValue;
                }
            }

            // Empty property value is not allowed
            if ((property.trim().length() == 0)) {
                throw new ConfigurationException("Property " + propertyName + " is empty.");
            }

            return property;
        } catch (UnknownNamespaceException e) {
            throw new ConfigurationException("UnknownNamespaceException occurs " + "when accessing ConfigManager.", e);
        }
    }

    /**
     * <p>
     * Creates a new <code>DBConnectionFactory</code> instance from values configed in the given namespace.
     * </p>
     *
     * @param namespace a namespace specifies where to get the values needed
     * @return the new DBConnectionFactory instance
     *
     * @throws ConfigurationException when the creation of DBConnectionFactory instance fails.
     */
    private DBConnectionFactory createDBConnectionFactory(String namespace) throws ConfigurationException {
        // get the configuration value from "ConnectionFactoryClassName" property
        String connectionFactoryClass = getPropertyValue(namespace, CONNECTION_FACTORY_CLASS_NAME,
            DBConnectionFactoryImpl.class.getName());

        //get the configuration value from connectionFactoryNamespace property
        String connectionNamespace = getPropertyValue(namespace, CONNECTION_FACTORY_NAMESPACE, null);

        //use reflection to create DBConnectionFactory instance
        Class[] types = new Class[] {String.class};
        Object[] parameters = new Object[] {connectionNamespace};

        try {
            Object factory = Class.forName(connectionFactoryClass).getConstructor(types).newInstance(parameters);

            if (!(factory instanceof DBConnectionFactory)) {
                throw new ConfigurationException("Class " + connectionFactoryClass
                    + " does not implement DBConnectionFactory.");
            }

            return (DBConnectionFactory) factory;
        } catch (IllegalArgumentException e) {
            throw new ConfigurationException(
                "IllegalArgumentException occurs when creating the db connection factory.", e);
        } catch (SecurityException e) {
            throw new ConfigurationException("SecurityException occurs when creating the db connection factory.", e);
        } catch (InstantiationException e) {
            throw new ConfigurationException("InstantiationException occurs when creating the db connection factory.",
                e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationException("IllegalAccessException occurs when creating the db connection factory.",
                e);
        } catch (InvocationTargetException e) {
            throw new ConfigurationException(
                "InvocationTargetException occurs when creating the db connection factory.", e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationException("NoSuchMethodException occurs when creating the db "
                + "connection factory.", e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("ClassNotFoundException occurs when creating the db connection factory.",
                e);
        }
    }

    /**
     * <p>
     * Generates a connection from connection factory and connection name.
     * </p>
     *
     * @return an open connection from the connection factory defined in the constructor.
     *
     * @throws SchedulingException if fail to create a new connection instance or
     * set the auto commit feature of the connection to false.
     */
    private Connection getConnection() throws SchedulingException {
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection(connectionName);

            // the auto commit feature is turned off
            connection.setAutoCommit(false);

            return connection;
        } catch (DBConnectionException e) {
            throw new SchedulingException("DBConnectionException occurs when creating the database connection.", e);
        } catch (SQLException e) {
            closeConnection(connection);
            throw new SchedulingException("SQLException occurs when creating the database connection.", e);
        }
    }

    /**
     * <p>
     * This method creates a <code>PreparedStatement</code> with the sql expression and paramters given.
     * </p>
     *
     * @param conn the database connection
     * @param sql the sql expression
     * @param params the parameter to fill the <code>PreparedStatement</code> created
     * @return the <code>PreparedStatement</code> instance created
     *
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement setUpPreparedStatement(Connection conn, String sql, List params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);

        // set up all the necessary parameters for executing.
        fillPreparedStatement(ps, params);

        return ps;
    }

    /**
     * <p>
     * This methods fill the <code>PreparedStatement</code> instance with the given parameters.
     * </p>
     *
     * @param pstmt the <code>PreparedStatement</code> instance to fill data
     * @param params the parameter list to fill the <code>PreparedStatement</code> instance
     *
     * @throws SQLException if a database access error occurs
     */
    private void fillPreparedStatement(PreparedStatement pstmt, List params) throws SQLException {
        // set up all the necessary parameters for executing.
        if (params != null) {
            int order = 1;
            for (Iterator it = params.iterator(); it.hasNext();) {
                Object obj = it.next();
                if (obj instanceof SqlType) {
                    SqlType type = (SqlType) obj;
                    pstmt.setNull(order, type.getType());
                } else {
                    setElement(order, obj, pstmt);
                }
                order++;
            }
        }
    }

    /**
     * <p>
     * This function is used to execute an update sql expression in database persistence.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param sql the update sql expression
     * @param param the parameters for executing update in database
     * @return the number of rows affects by the current operation
     * @throws SQLException when exception occurs during the database operation
     */
    private int executeUpdate(Connection conn, String sql, List param) throws SQLException {
        PreparedStatement ps = null;

        try {
            // set up all the necessary parameters for executing.
            ps = setUpPreparedStatement(conn, sql, param);
            return ps.executeUpdate();
        } finally {
            // release database resource
            closeStatement(ps);
        }
    }

    /**
     * <p>
     * Sets the actual value to replace the corresponding question mark.
     * </p>
     *
     * @param order the sequence number of question mark in sql expression
     * @param parameter the actual value to replace the corresponding question mark.
     * @param ps PreparedStatement instance to execute the sql expression
     *
     * @throws SQLException when exception occurs during the database operation
     */
    private void setElement(int order, Object parameter, PreparedStatement ps) throws SQLException {
        // replace the question mark in sql with real value
        if (parameter instanceof String) {
            ps.setString(order, (String) parameter);
        } else if (parameter instanceof Integer) {
            ps.setInt(order, ((Integer) parameter).intValue());
        } else if (parameter instanceof Long) {
            ps.setLong(order, ((Long) parameter).longValue());
        } else if (parameter instanceof Boolean) {
            ps.setBoolean(order, ((Boolean) parameter).booleanValue());
        } else if (parameter instanceof Timestamp) {
            ps.setTimestamp(order, (Timestamp) parameter);
        } else if (parameter instanceof Date) {
            ps.setTimestamp(order, new Timestamp(((Date) parameter).getTime()));
        } else {
            ps.setObject(order, parameter);
        }
    }

    /**
     * <p>
     * Closes the given Statement instance, SQLException will be ignored.
     * </p>
     *
     * @param statement the given Statement instance to close.
     */
    private void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            // Ignore
        }
    }

    /**
     * <p>
     * Closes the given ResultSet instance, SQLException will be ignored.
     * </p>
     *
     * @param rs the given ResultSet instance to close.
     */
    private void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            // Ignore
        }
    }

    /**
     * <p>
     * Closes the given Connection instance, SQLException will be ignored.
     * </p>
     *
     * @param con the given Connection instance to close.
     */
    private void closeConnection(Connection con) {
        try {
            if ((con != null) && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            // Ignore
        }
    }

    /**
     * <p>
     * Checks whether the given job is a valid job.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param job the job to be checked.
     * @param occurs the flag to identify whether the given job should be present on the config file or not
     *
     * @return whether the given job is a valid job.
     *
     * @throws IllegalArgumentException if if the given job is null, or the job has a same name with existing
     * job when occurs is false, or the job doesn't have a same name with existing job when occurs is true,
     * or the job has an invalid dependence or has both a schedule time and a dependence
     *
     * @throws SQLException if a database access error occurs
     */
    private Long checkJobValid(Connection conn, Job job, boolean occurs) throws SQLException {
        Util.checkObjectNotNull(job, "job");

        String jobName = job.getName();
        Long jobId = getJobId(conn, jobName);
        if (jobId != null && !occurs) {
            throw new IllegalArgumentException("The job " + job.getName() + " has a same name with existing jobs.");
        } else if (jobId == null && occurs) {
            throw new IllegalArgumentException("The job " + job.getName() + " doesn't configed in the database.");
        }

        Dependence dep = job.getDependence();
        if (dep != null) {
            String dependentJobName = dep.getDependentJobName();

            Long depJobid = getJobId(conn, dependentJobName);
            // the dependent job name is not configed
            if (depJobid == null) {
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

        return jobId;
    }

    /**
     * <p>
     * Checks whether the given group is valid.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param group the group to be checked.
     *
     * @return whether the given group is valid
     *
     * @throws IllegalArgumentException if the given group is null
     * @throws SchedulingException if the given group refers to any jobs that are not present in the
     * config file
     * @throws SQLException if a database access error occurs
     */
    private Map checkJobGroup(Connection conn, JobGroup group) throws SchedulingException, SQLException {
        Util.checkObjectNotNull(group, "group");

        Job[] jobs = group.getJobs();

        Map mapping = new HashMap();

        // all the referred jobs must be present
        for (int i = 0; i < jobs.length; i++) {
            String jobName = jobs[i].getName();
            Long jobId = getJobId(conn, jobName);
            if (jobId == null) {
                throw new SchedulingException("group contains a job, " + jobs[i].getName()
                    + ", which does not exist in the scheduler.");
            }

            mapping.put(jobName, jobId);
        }

        return mapping;
    }

    /**
     * <p>
     * Rollbacks the current connection if any error occurs while updating the persistence.
     * </p>
     *
     * @param con the given Connection instance to roll back
     */
    private void rollback(Connection con) {
        try {
            if (con != null) {
                con.rollback();
            }
        } catch (SQLException e) {
            // Just ignore
        }
    }

    /**
     * <p>
     * Adds or updates the given <code>Job</code> instance to database.
     * </p>
     *
     * <p>
     * When <code>isAdd</code> is true, then it is an add operation, otherwise it is an update operation.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param job the <code>Job</code> instance to add or update
     * @param isAdd the flag to identify whether it is add operation or update operation
     *
     * @throws SQLException if a database access error occurs
     * @throws SchedulingException if fails to get a new id from id generator
     */
    private void addOrUpdateJob(Connection conn, Job job, boolean isAdd) throws SQLException, SchedulingException {
        Long jobId = checkJobValid(conn, job, !isAdd);

        if (isAdd) {
            try {
                jobId = new Long(idGenerator.getNextID());
            } catch (IDGenerationException e) {
                throw new SchedulingException("IDGenerationException occurs when generating id.", e);
            }
        } else {
            deleteJob(conn, jobId);
        }

        Long[] groupIds = getGroupIds(conn, job.getGroups());

        insertJob(conn, job, jobId);

        try {
            insertMessages(conn, job, jobId);
        } catch (IDGenerationException e) {
            // roll back the transaction, this is because the database has been modified
            rollback(conn);
            throw new SchedulingException("IDGenerationException occurs when generating id.", e);
        }

        insertGroupJobs(conn, jobId, groupIds);
    }

    /**
     * <p>
     * Inserts a record to <b>job</b> table.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param job the <code>Job</code> instance to add to database
     * @param jobId the job id of the <code>Job</code> instance
     *
     * @throws SQLException if a database access error occurs
     * @throws SchedulingException if the date unit property in the job is null
     */
    private void insertJob(Connection conn, Job job, Long jobId) throws SQLException, SchedulingException {
        List params = createJobParameters(job, jobId);

        // insert a record to job table
        this.executeUpdate(conn, INSERT_JOB, params);
    }

    /**
     * <p>
     * This method sets up a <code>List</code> for the insert job sql expression.
     * </p>
     *
     * @param job the <code>Job</code> instance to add to database
     * @param jobId  the job id of the <code>Job</code> instance
     * @return the <code>List</code> instance for the insert job sql expression
     *
     * @throws SchedulingException if the date unit property in the job is null
     */
    private List createJobParameters(Job job, Long jobId) throws SchedulingException {
        List params = new ArrayList();
        params.add(jobId);
        params.add(job.getName());

        // the StartDate property
        GregorianCalendar startDate = job.getStartDate();
        if (startDate == null) {
            params.add(new SqlType(Types.DATE));
        } else {
            params.add(startDate.getTime());
        }

        // the StartTime property
        int startTime = job.getStartTime();
        params.add(new Integer(startTime));

        // the EndData property
        GregorianCalendar endDate = job.getStopDate();
        if (endDate == null) {
            params.add(new SqlType(Types.DATE));
        } else {
            params.add(endDate.getTime());
        }

        // The DateUnit related properties
        String[] dateUnits = SchedulerHelper.parseDateUnit(job.getIntervalUnit());
        params.add(dateUnits[0]);
        for (int i = 1; i < 4; i++) {
            if (dateUnits[i] == null) {
                params.add(new SqlType(Types.VARCHAR));
            } else {
                params.add(dateUnits[i]);
            }
        }

        // The Interval value
        params.add(new Integer(job.getIntervalValue()));

        // The recurrence value
        params.add(new Integer(job.getRecurrence()));

        // The Active flag
        params.add(new Boolean(job.getActive()));

        // The Job Type value
        params.add(SchedulerHelper.parseJobType(job.getJobType()));

        // The Run Command value
        params.add(job.getRunCommand());

        // The Dependence related properties
        Dependence dependence = job.getDependence();
        if (dependence == null) {
            params.add(new SqlType(Types.VARCHAR));
            params.add(new SqlType(Types.VARCHAR));
            params.add(new SqlType(Types.VARCHAR));
        } else {
            params.add(dependence.getDependentJobName());
            params.add(dependence.getDependentEvent());
            params.add(new Integer(dependence.getDelay()));
        }

        // The Async mode value
        params.add(job.isAsyncMode());

        return params;
    }

    /**
     * <p>
     * Gets the job id for the given name in the database.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param name the job name
     * @return the job id in database, may be null if not found
     *
     * @throws SQLException if a database access error occurs
     */
    private Long getJobId(Connection conn, String name) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            List params = new ArrayList();
            params.add(name);

            // select jobid from job where name = ?
            pstmt = this.setUpPreparedStatement(conn, SELECT_JOB_ID, params);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Long(rs.getLong(1));
            } else {
                return null;
            }
        } finally {
            closeResultSet(rs);
            closeStatement(pstmt);
        }
    }

    /**
     * <p>
     * This method gets all the group ids for the given <code>jobGroups</code> array in database.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param jobGroups the <code>JobGroup</code> array
     * @return all the group ids for the given <code>jobGroups</code> array in database.
     *
     * @throws SQLException if a database access error occurs
     * @throws SchedulingException some group name is not present in the database
     */
    private Long[] getGroupIds(Connection conn, JobGroup[] jobGroups) throws SQLException, SchedulingException {
        if (jobGroups.length == 0) {
            return new Long[0];
        }

        StringBuffer sb = new StringBuffer();
        sb.append(" (");
        for (int i = 0; i < jobGroups.length; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("?");
        }
        sb.append(")");

        List params = new ArrayList();
        for (int i = 0; i < jobGroups.length; i++) {
            params.add(jobGroups[i].getName());
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map mapping = new HashMap();
        Long[] ids = new Long[jobGroups.length];

        try {
            // select groupid, name from group where name in (?, ?, ?, ...)
            pstmt = setUpPreparedStatement(conn, SELECT_GROUPS + sb.toString(), params);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long groupId = rs.getLong(1);
                String groupName = rs.getString(2);

                mapping.put(groupName, new Long(groupId));
            }

            for (int i = 0; i < jobGroups.length; i++) {
                Long id = (Long) mapping.get(jobGroups[i].getName());
                if (id == null) {
                    throw new SchedulingException("The job group with name [" + jobGroups[i].getName()
                        + "] can not be found.");
                }

                ids[i] = id;
            }

            return ids;
        } finally {
            closeStatement(pstmt);
        }
    }

    /**
     * <p>
     * Inserts the <code>EmailEventHandler</code> in the given <code>job</code> to database.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param job the <code>Job</code> instance
     * @param jobId the job id of the given <code>Job</code> instance
     *
     * @throws SQLException if a database access error occurs
     * @throws IDGenerationException if it fails to get a new id from id generator
     */
    private void insertMessages(Connection conn, Job job, Long jobId) throws SQLException, IDGenerationException {
        List allParams = new ArrayList();
        EventHandler[] handlers = job.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            if (handlers[i] instanceof EmailEventHandler) {
                EmailEventHandler handler = (EmailEventHandler) handlers[i];

                // creates the parameters from the EmailEventHandler instance
                List params = new ArrayList();
                long msgId = idGenerator.getNextID();
                params.add(new Long(msgId));
                params.add(jobId);
                params.add(handler.getRequiredEvent());
                params.add(handler.getEmailFromAddress());
                params.add(handler.getEmailAlertSubject());
                params.add(handler.getTemplate().getTemplate());
                List recipients = handler.getRecipients();
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < recipients.size(); j++) {
                    if (j != 0) {
                        sb.append(".");
                    }

                    sb.append(recipients.get(i));
                }
                params.add(sb.toString());

                // cache the parameters
                allParams.add(params);
            }
        }

        if (allParams.size() != 0) {
            PreparedStatement pstmt = null;

            try {
                // insert into message (messageid, ownerid, name, fromaddress, subject, templatetext, recipients)
                // values (?, ?, ?, ?, ?, ?, ?)
                pstmt = conn.prepareStatement(INSERT_MESSAGE);

                for (Iterator it = allParams.iterator(); it.hasNext();) {
                    fillPreparedStatement(pstmt, (List) it.next());
                    pstmt.addBatch();
                }

                pstmt.executeBatch();

            } finally {
                closeStatement(pstmt);
            }
        }
    }

    /**
     * <p>
     * Inserts all the group ids with the job id to database.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param jobId the id of the job
     * @param groupIds the group ids of the job
     *
     * @throws SQLException if a database access error occurs
     */
    private void insertGroupJobs(Connection conn, Long jobId, Long[] groupIds) throws SQLException {
        if (groupIds.length != 0) {
            PreparedStatement pstmt = null;
            try {
                // insert into groupjob (groupid, jobid) values (?, ?)
                pstmt = conn.prepareStatement(INSERT_GROUP_JOB);

                for (int i = 0; i < groupIds.length; i++) {
                    List params = new ArrayList();
                    params.add(groupIds[i]);
                    params.add(jobId);

                    fillPreparedStatement(pstmt, params);
                }

                pstmt.executeBatch();

            } finally {
                closeStatement(pstmt);
            }
        }

    }

    /**
     * <p>
     * Deletes the job with the given id from database.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param jobId the id of the job
     *
     * @throws SQLException if a database access error occurs
     */
    private void deleteJob(Connection conn, Long jobId) throws SQLException {
        List params = new ArrayList();
        params.add(jobId);

        // delete from groupjob where jobid = ?
        executeUpdate(conn, DELETE_JOB_GROUP_BY_JOB_ID, params);

        // delete from message where ownerid = ?
        executeUpdate(conn, DELETE_MESSAGE, params);

        // delete from job where jobid = ?
        executeUpdate(conn, DELETE_JOB, params);
    }

    /**
     * <p>
     * Gets all the <code>JobGroup</code> instances for the given job id.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param jobId the id of the job
     * @return all the <code>JobGroup</code> instances for the given job id.
     *
     * @throws SQLException if a database access error occurs
     * @throws SchedulingException if fails to create any <code>JobGroup</code> intance to return
     */
    private JobGroup[] getJobGroups(Connection conn, Long jobId) throws SQLException, SchedulingException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            List params = new ArrayList();
            params.add(jobId);

            // select group.groupid, group.name from groupjob, group where groupjob.groupid = group.groupid
            // and groupjob.jobid = ?
            pstmt = setUpPreparedStatement(conn, SELECT_GROUPS_BY_JOBID, params);
            rs = pstmt.executeQuery();

            Map mapping = new HashMap();
            while (rs.next()) {
                int order = 1;
                long groupId = rs.getLong(order++);
                String name = rs.getString(order);

                mapping.put(new Long(groupId), name);
            }

            JobGroup[] groups = new JobGroup[mapping.size()];
            int i = 0;
            for (Iterator it = mapping.entrySet().iterator(); it.hasNext();) {
                Entry entry = (Entry) it.next();

                Long groupId = (Long) entry.getKey();
                String name = (String) entry.getValue();

                groups[i++] = getGroup(conn, groupId, name);
            }

            return groups;
        } finally {
            closeResultSet(rs);
            closeStatement(pstmt);
        }
    }

    /**
     * <p>
     * This method loads all the <code>EmailEventHandler</code> for the given job id from database.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param jobId the id of the job
     * @return all the <code>EmailEventHandler</code> for the given job id from database.
     *
     * @throws SQLException if a database access error occurs
     * @throws SchedulingException if fails to creates any <code>EmailEventHandler</code> instance
     */
    private List getEmailEventHandlers(Connection conn, Long jobId) throws SQLException, SchedulingException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List result = new ArrayList();
        try {
            List params = new ArrayList();
            params.add(jobId);

            // select name, fromaddress, subject, templatetext, recipients from message
            // where ownerid = ?
            pstmt = setUpPreparedStatement(conn, SELECT_MESSAGE, params);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int order = 1;
                String name = rs.getString(order++);
                String from = rs.getString(order++);
                String subject = rs.getString(order++);
                String templateFileName = rs.getString(order++);
                String recipientsValue = rs.getString(order++);
                List recipients = Arrays.asList(recipientsValue.split(","));

                // If TemplateFileName is missing, use the default template.
                Template template = defaultTemplate;
                if (templateFileName != null) {
                    // Create a new Template.
                    // Read template from file
                    template = SchedulerHelper.readTextToTemplate(templateFileName);
                }

                result.add(new EmailEventHandler(template, recipients, name, from, subject, log));
            }

            return result;
        } finally {
            closeResultSet(rs);
            closeStatement(pstmt);
        }
    }

    /**
     * <p>
     * Gets the <code>Job</code> instance using the given job name from database.
     * <p>
     *
     * @param conn the connection instance for database operation.
     * @param jobName the name of the job
     * @return the <code>Job</code> instance using the given job name from database.
     *
     * @throws SQLException if a database access error occurs
     * @throws SchedulingException if fails to create the <code>Job</code> instance
     */
    private Job getJob(Connection conn, String jobName) throws SQLException, SchedulingException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List params = new ArrayList();
        params.add(jobName);

        try {
            // select jobid, startdate, starttime, enddate, dateunit, dateunitdays, dateunitweek,
            // dateunitmonth, interval, recurrence, active, jobtype, jobcommand, dependencejobname,
            // dependencejobstatus, dependencejobdelay, asyncmode from job where name = ?
            pstmt = setUpPreparedStatement(conn, SELECT_JOB, params);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                SchedulerHelper helper = new SchedulerHelper();
                int order = 1;
                Long jobId = new Long(rs.getLong(order++));
                helper.setStartDate(rs.getDate(order++));
                helper.setStartTime(rs.getString(order++));
                helper.setEndDate(rs.getDate(order++));
                helper.setDateUnit(rs.getString(order++));
                helper.setDateUnitDays(rs.getString(order++));
                helper.setDateUnitWeek(rs.getString(order++));
                helper.setDateUnitMonth(rs.getString(order++));
                helper.setInterval(rs.getString(order++));
                helper.setRecurrence(rs.getString(order++));
                helper.setActive(rs.getBoolean(order++));
                helper.setJobType(rs.getString(order++));
                helper.setJobCommand(rs.getString(order++));
                helper.setDependenceJobName(rs.getString(order++));
                helper.setDependenceJobStatus(rs.getString(order++));
                helper.setDependenceJobDelay(rs.getString(order++));
                helper.setAsyncMode(rs.getString(order++));
                helper.setJobName(jobName);

                Job job = helper.createJob();

                List emailEventHandlers = getEmailEventHandlers(conn, jobId);
                for (Iterator it = emailEventHandlers.iterator(); it.hasNext();) {
                    job.addHandler((EventHandler) it.next());
                }

                JobGroup[] groups = getJobGroups(conn, jobId);
                for (int i = 0; i < groups.length; i++) {
                    job.addGroup(groups[i]);
                }

                return job;
            } else {
                return null;
            }
        } finally {
            closeResultSet(rs);
            closeStatement(pstmt);
        }

    }

    /**
     * <p>
     * Loads all the <code>Job</code> instances from the database.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @return all the <code>Job</code> instances from the database.
     *
     * @throws SQLException if a database access error occurs
     * @throws SchedulingException if fails to create any <code>Job</code> instance
     */
    private Job[] getJobList(Connection conn) throws SQLException, SchedulingException {
        // select jobid from job
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = setUpPreparedStatement(conn, SELECT_JOB_NAMES, null);
            rs = pstmt.executeQuery();

            List names = new ArrayList();
            while (rs.next()) {
                names.add(rs.getString(1));
            }

            Job[] jobs = new Job[names.size()];
            for (int i = 0; i < names.size(); i++) {
                jobs[i] = getJob(conn, (String) names.get(i));
            }

            return jobs;
        } finally {
            closeResultSet(rs);
            closeStatement(pstmt);
        }
    }

    /**
     * <p>
     * Adds or updates the <code>JobGroup</code> instance to the database.
     * </p>
     *
     * <p>
     * If <code>isAdd</code> is true, then it is add operation, otherwise it is update operation.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param group the <code>JobGroup</code> to add or update
     * @param isAdd the flag to identify it is an update operation ro add operation
     *
     * @throws SchedulingException if any group name doesn't present in the database
     * @throws SQLException if a database access error occurs
     * @throws IDGenerationException if fails to get a new id from id generator
     */
    private void addOrUpdateJobGroup(Connection conn, JobGroup group, boolean isAdd) throws SchedulingException,
        SQLException, IDGenerationException {
        Map mapping = checkJobGroup(conn, group);

        Long groupId;
        String name = group.getName();

        if (isAdd) {
            groupId = new Long(idGenerator.getNextID());
        } else {
            groupId = getGroupId(conn, name);

            if (groupId == null) {
                throw new SchedulingException("The group name [" + name + "] doesn't exist in the database.");
            }

            deleteGroup(conn, groupId);
        }

        // insert into group (groupid, name) values (?, ?)
        List params = new ArrayList();
        params.add(groupId);
        params.add(group.getName());

        executeUpdate(conn, INSERT_GROUP, params);

        // insert into groupjob (groupid, jobid) values (?, ?)
        insertJobGroup(conn, group, groupId, mapping);
    }

    /**
     * <p>
     * Inserts the relationships of the jobs contained by the group to database.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param group the <code>Group</code> instance
     * @param groupId the group id
     * @param mapping a mapping from the job name to its id, the key is the job name, of String type
     * and the value is job id, of Long type.
     *
     * @throws SQLException if a database access error occurs
     */
    private void insertJobGroup(Connection conn, JobGroup group, Long groupId, Map mapping) throws SQLException {
        List allParams = new ArrayList();
        Job[] jobs = group.getJobs();
        for (int i = 0; i < jobs.length; i++) {
            List params = new ArrayList();
            params.add(groupId);
            params.add(mapping.get(jobs[i].getName()));

            allParams.add(params);
        }

        if (allParams.size() != 0) {
            PreparedStatement pstmt = null;

            try {
                // insert into groupjob (groupid, jobid) values (?, ?)
                pstmt = conn.prepareStatement(INSERT_JOB_GROUP);

                for (Iterator it = allParams.iterator(); it.hasNext();) {
                    fillPreparedStatement(pstmt, (List) it.next());
                    pstmt.addBatch();
                }

                pstmt.executeBatch();
            } finally {
                closeStatement(pstmt);
            }
        }
    }

    /**
     * <p>
     * Gets the group id for the group name from database.
     * </p>
     *
     * <p>
     * If the group name is not present, then null will be returned.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param name the group name
     * @return the group id, may be null if the name cannot be found
     *
     * @throws SQLException if a database access error occurs
     */
    private Long getGroupId(Connection conn, String name) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            List params = new ArrayList();
            params.add(name);

            // select jobid from job where name = ?
            pstmt = setUpPreparedStatement(conn, SELECT_GROUP_ID, params);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Long(rs.getLong(1));
            } else {
                return null;
            }
        } finally {
            closeResultSet(rs);
            closeStatement(pstmt);
        }
    }

    /**
     * <p>
     * Loads the id and name mapping of all the groups in the database.
     * </p>
     *
     * <p>
     * The key of the return map is the group id, of Long type, the value of the return
     * map is group name, of String type.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @return all the id and name mappings of the groups in the database.
     *
     * @throws SQLException if a database access error occurs
     */
    private Map getGroupIds(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Map mapping = new HashMap();

        try {
            // select groupid, name from group
            pstmt = setUpPreparedStatement(conn, SELECT_ALL_GROUPS, null);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                int order = 1;
                long groupId = rs.getLong(order++);
                String name = rs.getString(order);

                mapping.put(new Long(groupId), name);
            }

            return mapping;
        } finally {
            closeResultSet(rs);
            closeStatement(pstmt);
        }
    }

    /**
     * <p>
     * This method deletes the group from database.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param groupId the group id
     *
     * @throws SQLException if a database access error occurs
     */
    private void deleteGroup(Connection conn, Long groupId) throws SQLException {
        List params = new ArrayList();
        params.add(groupId);

        // delete from groupjob where groupid = ?
        executeUpdate(conn, DELETE_JOB_GROUP, params);

        // delete from group where groupid = ?
        executeUpdate(conn, DELETE_GROUP, params);
    }

    /**
     * <p>
     * This method loads the <code>JobGroup</code> instance for the given group.
     * </p>
     *
     * @param conn the connection instance for database operation.
     * @param groupId the group id
     * @param name the group name
     * @return the <code>JobGroup</code> instance for the given group.
     *
     * @throws SQLException if a database access error occurs
     * @throws SchedulingException if fails to create the <code>JobGroup</code> instance
     */
    private JobGroup getGroup(Connection conn, Long groupId, String name) throws SQLException, SchedulingException {
        // select job.name, job.jobtype, job.jobcommand from job, jobgroup where job.jobid = jobgroup.jobid
        // and jobgroup.groupid = ?
        List jobs = new ArrayList();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            List params = new ArrayList();
            params.add(groupId);

            pstmt = setUpPreparedStatement(conn, SELECT_JOB_GROUP, params);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int order = 1;
                String jobName = rs.getString(order++);
                String type = rs.getString(order++);
                String cmd = rs.getString(order++);

                try {
                    jobs.add(new Job(jobName, SchedulerHelper.parseJobType(type), cmd));
                } catch (IllegalArgumentException e) {
                    throw new SchedulingException("Failed to create the job instance.", e);
                }
            }

            return new JobGroup(name, jobs);
        } finally {
            closeResultSet(rs);
            closeStatement(pstmt);
        }
    }

    /**
     * <p>
     * This is a data class and contains a sql type.
     * </p>
     *
     * <p>
     * This class is used to represents a <code>NULL</code> column value when filling
     * a <code>PreparedStatement</code> instance.
     * </p>
     *
     * <p>
     * Thread Safety : This class is immutable and so is thread safe.
     * </p>
     *
     * @author TCSDEVELOPER
     * @version 1.0
     */
    private class SqlType {
        /**
         * <p>
         * Represents the sql type for a column.
         * </p>
         */
        private int type;

        /**
         * <p>
         * Constructs a <code>SqlType</code> with the sql type given.
         * </p>
         *
         * @param type the sql type
         */
        SqlType(int type) {
            this.type = type;
        }

        /**
         * <p>
         * Gets the sql type for a column.
         * </p>
         *
         * @return the sql type for a column.
         */
        int getType() {
            return type;
        }
    }
}
