/*
 * Copyright (C) 2010-2014 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.processors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.db.connectionfactory.ConfigurationException;
import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.latetracker.EmailSendingException;
import com.topcoder.management.deliverable.latetracker.EmailSendingUtility;
import com.topcoder.management.deliverable.latetracker.Helper;
import com.topcoder.management.deliverable.latetracker.LateDeliverable;
import com.topcoder.management.deliverable.latetracker.LateDeliverableProcessor;
import com.topcoder.management.deliverable.latetracker.LateDeliverableType;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesProcessingException;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackerConfigurationException;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.util.errorhandling.ExceptionUtils;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.objectfactory.ObjectFactory;

/**
 * <p>
 * This class is an implementation of <code>LateDeliverableProcessor</code> that uses pluggable
 * <code>ResourceManager</code> and <code>UserRetrieval</code> instances to retrieve an additional information
 * about the user who has a late deliverable, and DB Connection Factory component to perform auditing of all late
 * deliverables and last sent notifications in the database. To send warning email messages to the users this class
 * uses <code>EmailSendingUtility</code>. This class performs the logging of errors and debug information using
 * Logging Wrapper component.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Added support for delay column in late_deliverables table. Now delay column is updated even when notification
 * is not sent to the user.</li>
 * <li>Using DB current time instead of JVM one.</li>
 * <li>COMPENSATED_DEADLINE and COMPENSATED_AND_REAL_DEADLINES_DIFFER parameters are supported in email templates.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Added support for EXPLANATION, EXPLANATION_DEADLINE and EXPLANATION_CAN_BE_SENT parameters in email
 *     templates.</li>
 * <li>Delay is computed based on the compensated deadline if exists (previously was always computed based on the
 * real deadline).</li>
 * <li>Storing compensated deadline in the database.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <em>Changes in version 1.3:</em>
 * <ol>
 * <li>Added support of Rejected Final Fix late deliverable type.</li>
 * </ol>
 * </p>
 *
 * <p>
 * Sample config:
 *
 * <pre>
 * &lt;?xml version=&quot;1.0&quot;?&gt;
 * &lt;CMConfig&gt;
 *     &lt;Config
 *     name=&quot;com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl&quot;&gt;
 *      &lt;Property name=&quot;loggerName&quot;&gt;
 *          &lt;Value&gt;myLogger&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;connectionName&quot;&gt;
 *          &lt;Value&gt;informix_connection&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;objectFactoryConfig&quot;&gt;
 *          &lt;property name=&quot;resourceManager&quot;&gt;
 *              &lt;property name=&quot;type&quot;&gt;
 *                  &lt;value&gt;com.topcoder.management.resource.persistence.PersistenceResourceManager&lt;/value&gt;
 *              &lt;/property&gt;
 *              &lt;Property name=&quot;params&quot;&gt;
 *                  &lt;Property name=&quot;param1&quot;&gt;
 *                      &lt;Property name=&quot;name&quot;&gt;
 *                          &lt;Value&gt;ResourcePersistence&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                  &lt;/Property&gt;
 *                  &lt;Property name=&quot;param2&quot;&gt;
 *                      &lt;Property name=&quot;name&quot;&gt;
 *                          &lt;Value&gt;SearchBundleManager&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                  &lt;/Property&gt;
 *              &lt;/Property&gt;
 *          &lt;/property&gt;
 *          &lt;property name=&quot;ResourcePersistence&quot;&gt;
 *              &lt;property name=&quot;type&quot;&gt;
 *                  &lt;value&gt;com.topcoder.management.resource.persistence.sql.SqlResourcePersistence&lt;/value&gt;
 *              &lt;/property&gt;
 *              &lt;Property name=&quot;params&quot;&gt;
 *                  &lt;Property name=&quot;param1&quot;&gt;
 *                      &lt;Property name=&quot;name&quot;&gt;
 *                          &lt;Value&gt;DBConnectionFactory&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                  &lt;/Property&gt;
 *              &lt;/Property&gt;
 *          &lt;/property&gt;
 *          &lt;property name=&quot;DBConnectionFactory&quot;&gt;
 *              &lt;property name=&quot;type&quot;&gt;
 *                  &lt;value&gt;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&lt;/value&gt;
 *              &lt;/property&gt;
 *              &lt;Property name=&quot;params&quot;&gt;
 *                  &lt;Property name=&quot;param1&quot;&gt;
 *                      &lt;Property name=&quot;type&quot;&gt;
 *                          &lt;Value&gt;String&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                      &lt;Property name=&quot;value&quot;&gt;
 *                          &lt;Value&gt;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                  &lt;/Property&gt;
 *              &lt;/Property&gt;
 *          &lt;/property&gt;
 *          &lt;property name=&quot;SearchBundleManager&quot;&gt;
 *              &lt;property name=&quot;type&quot;&gt;
 *                  &lt;value&gt;com.topcoder.search.builder.SearchBundleManager&lt;/value&gt;
 *              &lt;/property&gt;
 *              &lt;Property name=&quot;params&quot;&gt;
 *                  &lt;Property name=&quot;param1&quot;&gt;
 *                      &lt;Property name=&quot;type&quot;&gt;
 *                          &lt;Value&gt;String&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                      &lt;Property name=&quot;value&quot;&gt;
 *                          &lt;Value&gt;com.topcoder.search.builder.SearchBundleManager&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                  &lt;/Property&gt;
 *              &lt;/Property&gt;
 *          &lt;/property&gt;
 *          &lt;property name=&quot;userRetrieval&quot;&gt;
 *              &lt;property name=&quot;type&quot;&gt;
 *                  &lt;value&gt;com.cronos.onlinereview.external.impl.DBUserRetrieval&lt;/value&gt;
 *              &lt;/property&gt;
 *              &lt;Property name=&quot;params&quot;&gt;
 *                  &lt;Property name=&quot;param1&quot;&gt;
 *                      &lt;Property name=&quot;type&quot;&gt;
 *                          &lt;Value&gt;String&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                      &lt;Property name=&quot;value&quot;&gt;
 *                          &lt;Value&gt;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                  &lt;/Property&gt;
 *              &lt;/Property&gt;
 *          &lt;/property&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;dbConnectionFactoryConfig&quot;&gt;
 *          &lt;Property name=&quot;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&quot;&gt;
 *              &lt;Property name=&quot;connections&quot;&gt;
 *                  &lt;Property name=&quot;informix_connection&quot;&gt;
 *                      &lt;Property name=&quot;producer&quot;&gt;
 *                      &lt;Value&gt;com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                      &lt;Property name=&quot;parameters&quot;&gt;
 *                              &lt;Property name=&quot;jdbc_driver&quot;&gt;
 *                                  &lt;Value&gt;com.informix.jdbc.IfxDriver&lt;/Value&gt;
 *                              &lt;/Property&gt;
 *                              &lt;Property name=&quot;jdbc_url&quot;&gt;
 *                      &lt;Value&gt;jdbc:informix-sqli://localhost:1526/test:informixserver=ol_topcoder&lt;/Value&gt;
 *                              &lt;/Property&gt;
 *                              &lt;Property name=&quot;user&quot;&gt;
 *                                  &lt;Value&gt;informix&lt;/Value&gt;
 *                              &lt;/Property&gt;
 *                              &lt;Property name=&quot;password&quot;&gt;
 *                                  &lt;Value&gt;123456&lt;/Value&gt;
 *                              &lt;/Property&gt;
 *                      &lt;/Property&gt;
 *                  &lt;/Property&gt;
 *              &lt;/Property&gt;
 *          &lt;/Property&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;missedDeadlineEmailSubjectForDeliverable3&quot;&gt;
 *          &lt;Value&gt;WARNING\: You are late when providing a deliverable for %PROJECT_NAME%&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;missedDeadlineEmailBodyForDeliverable3&quot;&gt;
 *          &lt;Value&gt;test_files/warn_email_template.html&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;missedDeadlineNotificationDeliverableIds&quot;&gt;
 *          &lt;Value&gt;4&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;defaultMissedDeadlineEmailSubjectTemplateText&quot;&gt;
 *          &lt;Value&gt;WARNING\: You are late when providing a deliverable for %PROJECT_NAME%&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;defaultMissedDeadlineEmailBodyTemplatePath&quot;&gt;
 *          &lt;Value&gt;test_files/warn_email_template.html&lt;/Value&gt;
 *      &lt;/Property&gt;
 *         &lt;Property name=&quot;rejectedFinalFixEmailSubjectTemplateText&quot;&gt;
 *           &lt;Value&gt;WARNING\: You need to explain why your Final Fix for %PROJECT_NAME% was rejected&lt;/Value&gt;
 *         &lt;/Property&gt;
 *         &lt;Property name=&quot;rejectedFinalFixEmailBodyTemplatePath&quot;&gt;
 *           &lt;Value&gt;test_files/rejected_ff_email_template.html&lt;/Value&gt;
 *         &lt;/Property&gt;
 *      &lt;Property name=&quot;emailSender&quot;&gt;
 *          &lt;Value&gt;service@topcoder.com&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;resourceManagerKey&quot;&gt;
 *          &lt;Value&gt;resourceManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;userRetrievalKey&quot;&gt;
 *          &lt;Value&gt;userRetrieval&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;timestampFormat&quot;&gt;
 *          &lt;Value&gt;yyyy-MM-dd HH:mm:ss&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;notificationInterval&quot;&gt;
 *          &lt;Value&gt;10&lt;/Value&gt;
 *      &lt;/Property&gt;
 *         &lt;Property name=&quot;explanationDeadlineIntervalInHours&quot;&gt;
 *           &lt;Value&gt;24&lt;/Value&gt;
 *         &lt;/Property&gt;
 *         &lt;Property name=&quot;lateDeliverableTypeIds&quot;&gt;
 *           &lt;Value&gt;Missed Deadline=1,Rejected Final Fix=2&lt;/Value&gt;
 *         &lt;/Property&gt;
 *         &lt;Property name=&quot;sendRejectedFinalFixNotifications&quot;&gt;
 *           &lt;Value&gt;true&lt;/Value&gt;
 *         &lt;/Property&gt;
 *     &lt;/Config&gt;
 * &lt;/CMConfig&gt;
 * </pre>
 *
 * </p>
 * <p>
 * Thread Safety: This class is not thread safe since it uses <code>ResourceManager</code> instance that is not
 * thread safe. It's assumed that{@link #configure(ConfigurationObject)} method will be called just once right after
 * instantiation, before calling any business methods. <code>LateDeliverableProcessorImpl</code> uses transactions
 * when inserting or updating data in persistence.
 * </p>
 *
 * @author saarixx, myxgyy, sparemax
 * @version 1.3.3
 */
public class LateDeliverableProcessorImpl implements LateDeliverableProcessor {
    /**
     * <p>
     * Represents the name of this class used for logging.
     * </p>
     */
    private static final String CLASS_NAME = LateDeliverableProcessorImpl.class.getName();

    /**
     * <p>
     * Represents the boolean values "true"/"false".
     * </p>
     *
     * @since 1.3
     */
    private static final List<String> BOOLEAN_VALUES = Arrays.asList("true", "false");

    /**
     * <p>
     * Represents the default value for 'explanationDeadlineIntervalInHours'.
     * </p>
     *
     * @since 1.2
     */
    private static final int DEFAULT_EXPLANATION_DEADLINE_INTERVAL = 24;

    /**
     * <p>
     * Represents the default time format pattern.
     * </p>
     */
    private static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    /**
     * <p>
     * Represents the sql statement to get last dead line for the late deliverable.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Updated.</li>
     * </ol>
     * </p>
     */
    private static final String SELECT_MAX_DEADLINE_SQL = "select max(deadline), count(*) from late_deliverable"
        + " where project_phase_id = ? and resource_id = ? and deliverable_id = ? and late_deliverable_type_id = ?";

    /**
     * <p>
     * Represents the sql statement to get ID of late deliverable with maximum deadline.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Updated.</li>
     * </ol>
     * </p>
     */
    private static final String SELECT_LATE_DELIVERABLE_ID_WITH_MAX_DEADLINE_SQL = "select late_deliverable_id "
        + " from late_deliverable ld where ld.late_deliverable_type_id = ? and ld.project_phase_id = ?"
        + " and ld.resource_id = ? and ld.deliverable_id = ? and ld.deadline = (select max(deadline)"
        + " from late_deliverable ld2 where ld2.late_deliverable_type_id = ld.late_deliverable_type_id"
        + " and ld2.project_phase_id = ld.project_phase_id and ld2.resource_id = ld.resource_id"
        + " and ld2.deliverable_id = ld.deliverable_id)";

    /**
     * <p>
     * Represents the sql statement to get ID of late deliverable.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Updated.</li>
     * </ol>
     * </p>
     *
     * @since 1.3
     */
    private static final String SELECT_LATE_DELIVERABLE_ID_WITH_REJECTED_FF_SQL = "select late_deliverable_id"
        + " from late_deliverable ld where ld.late_deliverable_type_id = ? and ld.project_phase_id = ?"
        + " and ld.resource_id = ? and ld.deliverable_id = ?";

    /**
     * <p>
     * Represents the sql statement to retrieve last notified time and forgive flag.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Added columns for retrieving late deliverable explanation and creation date from DB.</li>
     * </ol>
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Updated.</li>
     * </ol>
     * </p>
     */
    private static final String SELECT_LAST_NOTIFICATION_TIME_FORGIVE_SQL = "select last_notified,"
        + " forgive_ind, explanation, create_date from late_deliverable where project_phase_id = ?"
        + " and resource_id = ? and deadline = ? and deliverable_id = ? and late_deliverable_type_id = ?";

    /**
     * <p>
     * Represents the sql statement to update the late deliverable.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Delay is computed based on the compensated deadline if exists (previously was always computed based on the
     * real deadline).</li>
     * </ol>
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Updated</li>
     * </ol>
     * </p>
     */
    private static final String UPDATE_SQL = "update late_deliverable set %1$sdelay ="
        + " (current - nvl(compensated_deadline, deadline))::interval second(9) to second::char(16)::decimal(16,0)"
        + " where project_phase_id = ? and resource_id = ? and deadline = ? and deliverable_id = ?"
        + " and late_deliverable_type_id = ?";

    /**
     * <p>
     * Represents &quot;connectionName&quot; property key in configuration.
     * </p>
     */
    private static final String CONNECTION_NAME_KEY = "connectionName";

    /**
     * <p>
     * Represents &quot;dbConnectionFactoryConfig&quot; child configuration key in
     * configuration.
     * </p>
     */
    private static final String DB_CONNECTION_FACTORY_CONFIG = "dbConnectionFactoryConfig";

    /**
     * <p>
     * Represents &quot;missedDeadlineNotificationDeliverableIds&quot; property key in configuration.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Renamed from "NOTIFICATION_DELIVERABLE_IDS".</li>
     * <li>Changed the value "notificationDeliverableIds" to "missedDeadlineNotificationDeliverableIds".</li>
     * </ol>
     * </p>
     */
    private static final String MISSED_DEADLINE_NOTIFICATION_DELIVERABLE_IDS =
        "missedDeadlineNotificationDeliverableIds";

    /**
     * <p>
     * Represents &quot;defaultMissedDeadlineEmailSubjectTemplateText&quot; property key in
     * configuration.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Renamed from "DEFAULT_EMAIL_SUBJECT_TEMPLATE_TEXT".</li>
     * <li>Changed the value "defaultEmailSubjectTemplateText" to "defaultMissedDeadlineEmailSubjectTemplateText".</li>
     * </ol>
     * </p>
     */
    private static final String DEFAULT_MISSED_DEADLINE_EMAIL_SUBJECT_TEMPLATE_TEXT =
        "defaultMissedDeadlineEmailSubjectTemplateText";

    /**
     * <p>
     * Represents &quot;defaultMissedDeadlineEmailBodyTemplatePath&quot; property key in configuration.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Renamed from "DEFAULT_EMAIL_BODY_TEMPLATE_PATH".</li>
     * <li>Changed the value "defaultEmailBodyTemplatePath" to "defaultMissedDeadlineEmailBodyTemplatePath".</li>
     * </ol>
     * </p>
     */
    private static final String DEFAULT_MISSED_DEADLINE_EMAIL_BODY_TEMPLATE_PATH =
        "defaultMissedDeadlineEmailBodyTemplatePath";

    /**
     * <p>
     * Represents &quot;emailSender&quot; property key in configuration.
     * </p>
     */
    private static final String EMAIL_SENDER_KEY = "emailSender";

    /**
     * <p>
     * Represents &quot;resourceManagerKey&quot; property key in configuration.
     * </p>
     */
    private static final String RESOURCE_MANAGER_KEY = "resourceManagerKey";

    /**
     * <p>
     * Represents &quot;userRetrievalKey&quot; property key in configuration.
     * </p>
     */
    private static final String USER_RETRIEVAL_KEY = "userRetrievalKey";

    /**
     * <p>
     * Represents &quot;timestampFormat&quot; property key in configuration.
     * </p>
     */
    private static final String TIMESTAMP_FORMAT_KEY = "timestampFormat";

    /**
     * <p>
     * Represents &quot;notificationInterval&quot; property key in configuration.
     * </p>
     */
    private static final String NOTIFICATION_INTERVAL_KEY = "notificationInterval";

    /**
     * <p>
     * Represents &quot;missedDeadlineEmailSubjectForDeliverable&quot; prefix of property in
     * configuration.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Renamed from "EMAIL_SUBJECT_PREFIX".</li>
     * <li>Changed the value "emailSubjectForDeliverable" to "missedDeadlineEmailSubjectForDeliverable".</li>
     * </ol>
     * </p>
     */
    private static final String MISSED_DEADLINE_EMAIL_SUBJECT_PREFIX = "missedDeadlineEmailSubjectForDeliverable";

    /**
     * <p>
     * Represents &quot;missedDeadlineEmailBodyForDeliverable&quot; prefix of property in configuration.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Renamed from "EMAIL_BODY_PREFIX".</li>
     * <li>Changed the value "emailBodyForDeliverable" to "missedDeadlineEmailBodyForDeliverable".</li>
     * </ol>
     * </p>
     */
    private static final String MISSED_DEADLINE_EMAIL_BODY_PREFIX = "missedDeadlineEmailBodyForDeliverable";

    /**
     * <p>
     * Represents &quot;explanationDeadlineIntervalInHours&quot; property key in configuration.
     * </p>
     *
     * @since 1.2
     */
    private static final String EXPLANATION_DEADLINE_INTERVAL = "explanationDeadlineIntervalInHours";

    /**
     * <p>
     * Represents &quot;rejectedFinalFixEmailSubjectTemplateText&quot; property key in configuration.
     * </p>
     *
     * @since 1.3
     */
    private static final String REJECTED_FF_EMAIL_SUBJECT_TEMPLATE_TEXT = "rejectedFinalFixEmailSubjectTemplateText";

    /**
     * <p>
     * Represents &quot;rejectedFinalFixEmailBodyTemplatePath&quot; property key in configuration.
     * </p>
     *
     * @since 1.3
     */
    private static final String REJECTED_FF_EMAIL_BODY_TEMPLATE_PATH = "rejectedFinalFixEmailBodyTemplatePath";

    /**
     * <p>
     * Represents &quot;lateDeliverableTypeIds&quot; property key in configuration.
     * </p>
     *
     * @since 1.3
     */
    private static final String LATE_DELIVERABLE_TYPE_IDS = "lateDeliverableTypeIds";

    /**
     * <p>
     * Represents &quot;sendRejectedFinalFixNotifications&quot; property key in configuration.
     * </p>
     *
     * @since 1.3
     */
    private static final String SEND_REJECTED_FF_NOTIFICATIONS = "sendRejectedFinalFixNotifications";

    /**
     * <p>
     * The database connection factory to be used by this class.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after
     * that.
     * </p>
     * <p>
     * Cannot be null after initialization.
     * </p>
     * <p>
     * Is used in {@link #processLateDeliverable(LateDeliverable)}.
     * </p>
     */
    private DBConnectionFactory dbConnectionFactory;

    /**
     * <p>
     * The connection name to be passed to the connection factory.
     * </p>
     * <p>
     * If null, the default connection is used.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after
     * that.
     * </p>
     * <p>
     * Cannot be empty after initialization.
     * </p>
     * <p>
     * Is used in {@link #processLateDeliverable(LateDeliverable)}.
     * </p>
     */
    private String connectionName;

    /**
     * <p>
     * The set of deliverable IDs for which sending of notifications must be performed. Is used for late deliverables
     * of Missed Deadline type
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that.
     * </p>
     * <p>
     * Cannot be null, cannot contain null or not positive element after initialization.
     * </p>
     * <p>
     * Is used in {@link #processLateDeliverable(LateDeliverable)}.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Renamed from "notificationDeliverableIds".</li>
     * </ol>
     * </p>
     */
    private Set<Long> missedDeadlineNotificationDeliverableIds;

    /**
     * <p>
     * The mapping from deliverable ID to email subject template text to be used for late deliverables with this ID.
     * Is used for late deliverables of Missed Deadline type.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that.
     * </p>
     * <p>
     * Cannot be null, cannot contain null/not positive key or null value after initialization.
     * </p>
     * <p>
     * Is used in {@link #processLateDeliverable(LateDeliverable)}.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Renamed from "emailSubjectTemplateTexts".</li>
     * </ol>
     * </p>
     */
    private Map<Long, String> missedDeadlineEmailSubjectTemplateTexts;

    /**
     * <p>
     * The mapping from deliverable ID to email body template path (resource path or file path) to be used for late
     * deliverables with this ID. Is used for late deliverables of Missed Deadline type.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that.
     * </p>
     * <p>
     * Cannot be null, cannot contain null/not positive key or null/empty value after initialization.
     * </p>
     * <p>
     * Is used in {@link #processLateDeliverable(LateDeliverable)}.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Renamed from "emailBodyTemplatePaths".</li>
     * </ol>
     * </p>
     */
    private Map<Long, String> missedDeadlineEmailBodyTemplatePaths;

    /**
     * <p>
     * The default email subject template text to be used for all deliverable IDs not configured in
     * missedDeadlineEmailSubjectTemplateTexts collection. Is used for late deliverables of Missed Deadline type.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that.
     * </p>
     * <p>
     * Cannot be null after initialization.
     * </p>
     * <p>
     * Is used in {@link #processLateDeliverable(LateDeliverable)}.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Renamed from "defaultEmailSubjectTemplateText".</li>
     * </ol>
     * </p>
     */
    private String defaultMissedDeadlineEmailSubjectTemplateText;

    /**
     * <p>
     * The default email body template path (resource path or file path) to be used for all deliverable IDs not
     * configured in missedDeadlineEmailBodyTemplatePaths collection. Is used for late deliverables of Missed Deadline
     * type.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that.
     * </p>
     * <p>
     * Cannot be null/empty after initialization.
     * </p>
     * <p>
     * Is used in {@link #processLateDeliverable(LateDeliverable)}.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Renamed from "defaultEmailBodyTemplatePath".</li>
     * </ol>
     * </p>
     */
    private String defaultMissedDeadlineEmailBodyTemplatePath;

    /**
     * <p>
     * The email subject template text to be used for sending notifications about Rejected Final Fix late
     * deliverables.
     * </p>
     *
     * <p>
     * Is initialized in configure() and never changed after that. Cannot be null after initialization. Is used in
     * processLateDeliverable().
     * </p>
     *
     * @since 1.3
     */
    private String rejectedFinalFixEmailSubjectTemplateText;

    /**
     * <p>
     * The email body template path (resource path or file path) to be used for sending notifications about Rejected
     * Final Fix late deliverables.
     * </p>
     *
     * <p>
     * Is initialized in configure() and never changed after that. Cannot be null after initialization. Is used in
     * processLateDeliverable().
     * </p>
     *
     * @since 1.3
     */
    private String rejectedFinalFixEmailBodyTemplatePath;

    /**
     * <p>
     * The email sending utility to be used by this class.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after
     * that.
     * </p>
     * <p>
     * Cannot be null after initialization.
     * </p>
     * <p>
     * Is used in {@link #processLateDeliverable(LateDeliverable)}.
     * </p>
     */
    private EmailSendingUtility emailSendingUtility;

    /**
     * <p>
     * The resource manager to be used by this class for retrieving user ID by resource
     * ID.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after
     * that.
     * </p>
     * <p>
     * Cannot be null after initialization.
     * </p>
     * <p>
     * Is used in {@link #getEmailAddressForResource(long)}.
     * </p>
     */
    private ResourceManager resourceManager;

    /**
     * <p>
     * The user retrieval service to be used by this class for retrieving user email
     * address by user ID.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after
     * that.
     * </p>
     * <p>
     * Cannot be null after initialization.
     * </p>
     * <p>
     * Is used in {@link #getEmailAddressForResource(long)}.
     * </p>
     */
    private UserRetrieval userRetrieval;

    /**
     * <p>
     * The timestamp format to be used for formatting timestamp in the email message.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after
     * that.
     * </p>
     * <p>
     * Cannot be null after initialization.
     * </p>
     * <p>
     * Is used in {@link #prepareParameters(Project, Phase, Deliverable)}.
     * </p>
     */
    private DateFormat timestampFormat;

    /**
     * <p>
     * The interval in seconds between sending notifications to the user about the same
     * late deliverable. Note that notifications are not sent when &quot;forgive_ind&quot;
     * flag in the database is set for the late deliverable to &quot;true&quot;.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after
     * that.
     * </p>
     * <p>
     * Cannot be negative after initialization. If equal to 0, notifications are not sent
     * repeatedly.
     * </p>
     * <p>
     * Is used in {@link #processLateDeliverable(LateDeliverable)}.
     * </p>
     */
    private long notificationInterval;

    /**
     * <p>
     * The mapping from LateDeliverableType enum value to its corresponding record ID in the database.
     * </p>
     *
     * <p>
     * Is initialized in configure() and never changed after that. Cannot be null, cannot contain null key or null/not
     * positive value, must contain exactly two entries after initialization. Is used in processLateDeliverable().
     * </p>
     *
     * @since 1.3
     */
    private Map<LateDeliverableType, Long> lateDeliverableTypeIds;

    /**
     * <p>
     * The value indicating whether notifications must be sent for Rejected Final Fix late deliverables.
     * </p>
     *
     * <p>
     * Is initialized in configure() and never changed after that. Is used in processLateDeliverable().
     * </p>
     *
     * @since 1.3
     */
    private boolean sendRejectedFinalFixNotifications;

    /**
     * <p>
     * The logger used by this class for logging errors and debug information.
     * </p>
     * <p>
     * Is initialized in the {@link #configure(ConfigurationObject)} method and never
     * changed after that. If is null after initialization, logging is not performed.
     * </p>
     * <p>
     * Is used in {@link #processLateDeliverable(LateDeliverable)}.
     * </p>
     */
    private Log log;

    /**
     * <p>
     * The time interval between the late record creation date and the explanation deadline (in hours).
     * </p>
     *
     * <p>
     * Is initialized in configure() and never changed after that. Must be positive after initialization. Is used in
     * prepareParameters().
     * </p>
     */
    private int explanationDeadlineIntervalInHours;

    /**
     * Creates an instance of <code>LateDeliverableProcessorImpl</code>.
     */
    public LateDeliverableProcessorImpl() {
        // Empty
    }

    /**
     * Configures this instance with use of the given configuration object.
     *
     * <p>
     * <em>Change in 1.2:</em>
     * <ol>
     * <li>Added step for reading "explanationDeadlineIntervalInHours".</li>
     * </ol>
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Updated property and configuration parameter names.</li>
     * <li>Added steps for reading "rejectedFinalFixEmailSubjectTemplateText",
     * "rejectedFinalFixEmailBodyTemplatePath", "lateDeliverableTypeIds" and "sendRejectedFinalFixNotifications"
     * properties.</li>
     * </ol>
     * </p>
     *
     * @param config
     *            the configuration object
     * @throws IllegalArgumentException
     *             if <code>config</code> is <code>null</code>.
     * @throws LateDeliverablesTrackerConfigurationException
     *             if some error occurred when initializing an instance using the given configuration.
     */
    public void configure(ConfigurationObject config) {
        ExceptionUtils.checkNull(config, null, null, "The parameter 'config' should not be null.");

        // create log
        String loggerName = Helper.getPropertyValue(config, Helper.LOGGER_NAME_KEY, false, false);
        this.log = (loggerName == null) ? null : LogManager.getLog(loggerName);

        this.connectionName = Helper.getPropertyValue(config, CONNECTION_NAME_KEY, false, false);

        // Create database connection factory using the extracted configuration
        dbConnectionFactory = createDBConnectionFactory(config);

        // Get notification deliverable IDs string from config
        String notificationDeliverableIdsStr = Helper.getPropertyValue(config,
            MISSED_DEADLINE_NOTIFICATION_DELIVERABLE_IDS, false, true);
        // Create set for parsed notification deliverable IDs
        missedDeadlineNotificationDeliverableIds = new HashSet<Long>();

        // Split ID substrings in the comma separated string
        if ((notificationDeliverableIdsStr != null) && (notificationDeliverableIdsStr.trim().length() > 0)) {
            String[] notificationDeliverableIdsArray = notificationDeliverableIdsStr.split(Helper.COMMA);

            for (String notificationDeliverableIdStr : notificationDeliverableIdsArray) {
                // Parse and add ID to the set
                missedDeadlineNotificationDeliverableIds.add(Helper.parseLong(notificationDeliverableIdStr,
                    MISSED_DEADLINE_NOTIFICATION_DELIVERABLE_IDS, 1));
            }
        }

        // Create map for Missed Deadline email subject templates:
        missedDeadlineEmailSubjectTemplateTexts = new HashMap<Long, String>();
        // Create map for Missed Deadline email body templates
        missedDeadlineEmailBodyTemplatePaths = new HashMap<Long, String>();

        String[] allKeys = getAllKeys(config);

        for (String propertyKey : allKeys) {
            initMap(config, missedDeadlineEmailSubjectTemplateTexts, propertyKey, MISSED_DEADLINE_EMAIL_SUBJECT_PREFIX);
            initMap(config, missedDeadlineEmailBodyTemplatePaths, propertyKey, MISSED_DEADLINE_EMAIL_BODY_PREFIX);
        }

        // Get default email subject template from config
        defaultMissedDeadlineEmailSubjectTemplateText = Helper.getPropertyValue(config,
            DEFAULT_MISSED_DEADLINE_EMAIL_SUBJECT_TEMPLATE_TEXT, true, true);
        // Get default email body template path from config
        defaultMissedDeadlineEmailBodyTemplatePath = Helper.getPropertyValue(config,
            DEFAULT_MISSED_DEADLINE_EMAIL_BODY_TEMPLATE_PATH, true, false);

        rejectedFinalFixEmailSubjectTemplateText = Helper.getPropertyValue(config,
            REJECTED_FF_EMAIL_SUBJECT_TEMPLATE_TEXT, true, true);
        rejectedFinalFixEmailBodyTemplatePath = Helper.getPropertyValue(config,
            REJECTED_FF_EMAIL_BODY_TEMPLATE_PATH, true, false);

        // Get email sender from config
        String emailSender = Helper.getPropertyValue(config, EMAIL_SENDER_KEY, true, false);
        // Create email sending utility
        emailSendingUtility = new EmailSendingUtility(emailSender, log);

        // create object factory
        ObjectFactory objectFactory = Helper.createObjectFactory(config);

        // Create resource manager with OF
        resourceManager = Helper.createObject(config, objectFactory, RESOURCE_MANAGER_KEY,
            ResourceManager.class);

        // Create user retrieval with OF
        userRetrieval = Helper.createObject(config, objectFactory, USER_RETRIEVAL_KEY,
            UserRetrieval.class);

        // Get timestamp format to be used in the email
        timestampFormat = getTimestampFormat(config);

        // Read notification interval
        notificationInterval = getNotificationInterval(config);

        // Read and parse explanation deadline interval:
        explanationDeadlineIntervalInHours = getExplanationDeadlineInterval(config);

        lateDeliverableTypeIds = getLateDeliverableTypeIds(config);

        // Get the value indicating whether notifications must be sent for Rejected Final Fix late deliverables:
        sendRejectedFinalFixNotifications = getSendRejectedFinalFixNotifications(config);
    }

    /**
     * Gets the notification interval.
     *
     * @param config
     *            the configuration object.
     *
     * @return the notification interval.
     *
     * @throws LateDeliverablesTrackerConfigurationException
     *             if some error occurred.
     *
     * @since 1.3
     */
    private static long getNotificationInterval(ConfigurationObject config) {
        // Read and parse notification interval
        String notificationIntervalStr = Helper.getPropertyValue(config, NOTIFICATION_INTERVAL_KEY,
            false, false);

        if (notificationIntervalStr != null) {
            return Helper.parseLong(notificationIntervalStr,
                NOTIFICATION_INTERVAL_KEY, 1);
        }
        return 0;
    }

    /**
     * Gets the timestamp format.
     *
     * @param config
     *            the configuration object.
     *
     * @return the timestamp format.
     *
     * @throws LateDeliverablesTrackerConfigurationException
     *             if some error occurred.
     *
     * @since 1.3
     */
    private static DateFormat getTimestampFormat(ConfigurationObject config) {
        // Get timestamp format to be used in the email
        String timestampFormatStr = Helper.getPropertyValue(config, TIMESTAMP_FORMAT_KEY, false, false);

        try {
            return new SimpleDateFormat((timestampFormatStr == null) ? DEFAULT_TIME_FORMAT : timestampFormatStr);
        } catch (IllegalArgumentException e) {
            throw new LateDeliverablesTrackerConfigurationException("Invaid timestamp format value.", e);
        }
    }


    /**
     * Gets the send rejected final fix notifications flag.
     *
     * @param config
     *            the configuration object.
     *
     * @return the send rejected final fix notifications flag.
     *
     * @throws LateDeliverablesTrackerConfigurationException
     *             if some error occurred.
     *
     * @since 1.3
     */
    private static boolean getSendRejectedFinalFixNotifications(ConfigurationObject config) {
        // Get the value indicating whether notifications must be sent for Rejected Final Fix late deliverables:
        String sendRejectedFinalFixNotificationsStr = Helper.getPropertyValue(config, SEND_REJECTED_FF_NOTIFICATIONS,
            false, false);
        boolean sendRejectedFinalFixNotifications;
        if (sendRejectedFinalFixNotificationsStr == null) {
            sendRejectedFinalFixNotifications = true;
        } else {
            int booleanValueIndex = BOOLEAN_VALUES.indexOf(sendRejectedFinalFixNotificationsStr);
            if (booleanValueIndex == -1) {
                throw new LateDeliverablesTrackerConfigurationException(
                    "The 'sendRejectedFinalFixNotifications' property value should be 'true' or 'false'.");
            }
            sendRejectedFinalFixNotifications = (booleanValueIndex == 0);
        }
        return sendRejectedFinalFixNotifications;
    }

    /**
     * Gets the late deliverable type ids.
     *
     * @param config
     *            the configuration object.
     *
     * @return the late deliverable type ids.
     *
     * @throws LateDeliverablesTrackerConfigurationException
     *             if some error occurred.
     *
     * @since 1.3
     */
    private static Map<LateDeliverableType, Long> getLateDeliverableTypeIds(ConfigurationObject config) {
        Map<LateDeliverableType, Long> lateDeliverableTypeIds = new EnumMap<LateDeliverableType, Long>(
            LateDeliverableType.class);
        String[] lateDeliverableTypeIdsArray = Helper
            .getPropertyValue(config, LATE_DELIVERABLE_TYPE_IDS, true, false).split(",");
        for (String lateDeliverableTypeIdsStr : lateDeliverableTypeIdsArray) {
            String[] lateDeliverableTypeIdPair = lateDeliverableTypeIdsStr.split("=");
            if (lateDeliverableTypeIdPair.length != 2) {
                throw new LateDeliverablesTrackerConfigurationException("The late deliverable type value '"
                    + lateDeliverableTypeIdsStr + "' is invalid.");
            }
            String lateDeliverableTypeStr = lateDeliverableTypeIdPair[0];
            String lateDeliverableTypeIdStr = lateDeliverableTypeIdPair[1];

            LateDeliverableType lateDeliverableType = LateDeliverableType.fromString(lateDeliverableTypeStr);
            if (lateDeliverableType == null) {
                throw new LateDeliverablesTrackerConfigurationException("The late deliverable type value '"
                    + lateDeliverableTypeStr + "' is undefined.");
            }
            long lateDeliverableTypeId = Helper.parseLong(lateDeliverableTypeIdStr, LATE_DELIVERABLE_TYPE_IDS, 1);

            lateDeliverableTypeIds.put(lateDeliverableType, lateDeliverableTypeId);
        }

        return lateDeliverableTypeIds;
    }

    /**
     * <p>
     * Gets the value of 'explanationDeadlineIntervalInHours'.
     * </p>
     *
     * @param config
     *            the configuration object.
     *
     * @return the value of 'explanationDeadlineIntervalInHours'.
     *
     * @throws LateDeliverablesTrackerConfigurationException
     *             if any error occurs.
     *
     * @since 1.2
     */
    private static int getExplanationDeadlineInterval(ConfigurationObject config) {

        // Read explanation deadline interval:
        String valueStr = Helper.getPropertyValue(config, EXPLANATION_DEADLINE_INTERVAL, false, false);
        int value;
        try {
            if (valueStr != null) {
                value = Integer.parseInt(valueStr);

                if (value <= 0) {
                    throw new LateDeliverablesTrackerConfigurationException(
                        "The property 'explanationDeadlineIntervalInHours' should be a positive integer.");
                }
            } else {
                value = DEFAULT_EXPLANATION_DEADLINE_INTERVAL;
            }

        } catch (NumberFormatException e) {
            throw new LateDeliverablesTrackerConfigurationException(
                "The property 'explanationDeadlineIntervalInHours' should be a positive integer.", e);
        }

        return value;

    }

    /**
     * Initializes the given map. The map will be email subject templates or email body
     * templates.
     *
     * @param config
     *            the configuration object.
     * @param map
     *            the map to be initialized.
     * @param propertyKey
     *            the property key.
     * @param key
     *            the prefix of the key to get value.
     * @throws LateDeliverablesTrackerConfigurationException
     *             if configuration value is invalid.
     */
    private static void initMap(ConfigurationObject config, Map<Long, String> map, String propertyKey,
        String key) {
        if (propertyKey.startsWith(key)) {
            // Parse deliverable ID
            long deliverableId = Helper.parseLong(propertyKey.substring(key.length()),
                key + propertyKey, 1);

            // Get email subject/body template text for this deliverable ID
            // will always exist, not matter required or not
            String templateText = Helper.getPropertyValue(config, propertyKey, true, false);
            // Put (deliverable ID; subject/body template) pair to the map
            map.put(deliverableId, templateText);
        }
    }

    /**
     * Creates <code>DBConnectionFactory</code> from configuration.
     *
     * @param config
     *            the configuration object.
     * @return the created <code>DBConnectionFactory</code> instance.
     * @throws LateDeliverablesTrackerConfigurationException
     *             if any error occurs when creating <code>DBConnectionFactory</code>.
     */
    private static DBConnectionFactory createDBConnectionFactory(ConfigurationObject config) {
        ConfigurationObject dbConnectionFactoryConfig = Helper.getChildConfig(config,
            DB_CONNECTION_FACTORY_CONFIG);

        // Create database connection factory using the extracted configuration
        try {
            return new DBConnectionFactoryImpl(dbConnectionFactoryConfig);
        } catch (UnknownConnectionException e) {
            throw new LateDeliverablesTrackerConfigurationException(
                "Fails to create database connection factory.", e);
        } catch (ConfigurationException e) {
            throw new LateDeliverablesTrackerConfigurationException(
                "Fails to create database connection factory.", e);
        }
    }

    /**
     * Gets all property keys.
     *
     * @param config
     *            the configuration object.
     * @return all property keys in configuration.
     * @throws LateDeliverablesTrackerConfigurationException
     *             if error occurs when accessing the configuration object.
     */
    private static String[] getAllKeys(ConfigurationObject config) {
        try {
            return config.getAllPropertyKeys();
        } catch (ConfigurationAccessException e) {
            throw new LateDeliverablesTrackerConfigurationException(
                "Fails to access the configuration object.", e);
        }
    }

    /**
     * Processes the given late deliverable. The actual actions to be performed depend on the implementation.
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>UPDATE and INSERT statements were updated to use current DB time instance of current JVM time.</li>
     * <li>UPDATE and INSERT statements were updated to support "delay" field.</li>
     * <li>UPDATE statement is now executed when canSendNotification is false too.</li>
     * </ol>
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Additionally storing compensated deadline in the database.</li>
     * <li>Delay is computed based on the compensated deadline if exists (previously was always computed based on the
     * real deadline).</li>
     * <li>Added steps for retrieving late deliverable explanation and creation date from DB (to be used when
     * preparing email parameters).</li>
     * </ol>
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Added support of REJECTED_FINAL_FIX late deliverable type.</li>
     * </ol>
     * </p>
     *
     * @param lateDeliverable
     *            the late deliverable to be processed.
     * @throws IllegalArgumentException
     *             if lateDeliverable is null, or any one of phase, project, deliverable and type of lateDeliverable
     *             is null, or the scheduled end date of phase is null.
     * @throws IllegalStateException
     *             if this class was not configured properly with use of {@link #configure(ConfigurationObject)}
     *             method.
     * @throws EmailSendingException
     *             if some error occurred when sending a notification email message.
     * @throws LateDeliverablesProcessingException
     *             if some other error occurred when processing a late deliverable.
     */
    public void processLateDeliverable(LateDeliverable lateDeliverable)
        throws LateDeliverablesProcessingException {
        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME + ".processLateDeliverable(LateDeliverable lateDeliverable)";
        Helper.logEntrance(log, signature, new String[] {"lateDeliverable"},
            new Object[] {lateDeliverable});

        // check argument
        checkArgument(lateDeliverable, signature);
        // check state
        checkState(signature);

        Deliverable deliverable = lateDeliverable.getDeliverable();
        Phase phase = lateDeliverable.getPhase();
        Date currentDeadline = phase.getScheduledEndDate();
        long deliverableId = deliverable.getId();
        boolean needToNotify;
        if (lateDeliverable.getType() == LateDeliverableType.MISSED_DEADLINE) {
            // Check if warning email should be sent for this deliverable:
            needToNotify = missedDeadlineNotificationDeliverableIds.contains(deliverableId);
        } else {
            needToNotify = sendRejectedFinalFixNotifications;
        }

        Connection connection = getConnection(signature);

        try {
            connection.setAutoCommit(false);

            long phaseId = phase.getId();
            long resourceId = deliverable.getResource();

            // Delegate to the helper method
            processLateDeliverable(connection, lateDeliverable, currentDeadline, deliverableId, needToNotify,
                phaseId, resourceId);

            connection.commit();

        } catch (SQLException e) {
            rollback(connection, signature);
            throw Helper.logException(log, signature, new LateDeliverablesProcessingException(
                "SQL error occurs when processing the late deliverable.", e));
        } catch (LateDeliverablesProcessingException e) {
            rollback(connection, signature);
            throw Helper.logException(log, signature, e);
        } finally {
            closeConnection(connection, signature);
        }

        Helper.logExit(log, signature, null, start);
    }

    /**
     * <p>
     * A helper method to process the given late deliverable.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Added support of REJECTED_FINAL_FIX late deliverable type.</li>
     * </ol>
     * </p>
     *
     * @param connection
     *            the connection.
     * @param lateDeliverable
     *            the late deliverable.
     * @param currentDeadline
     *            the current deadline.
     * @param deliverableId
     *            the deliverable id.
     * @param needToNotify
     *            the need to notify flag.
     * @param phaseId
     *            the phase id.
     * @param resourceId
     *            the resource id.
     *
     * @throws SQLException
     *             if a database access error occurs .
     * @throws LateDeliverablesProcessingException
     *             if another error occurs.
     *
     * @since 1.2
     */
    private void processLateDeliverable(Connection connection, LateDeliverable lateDeliverable, Date currentDeadline,
        long deliverableId, boolean needToNotify, long phaseId, long resourceId) throws SQLException,
        LateDeliverablesProcessingException {
        Project project = lateDeliverable.getProject();

        LateDeliverableType lateDeliverableType = lateDeliverable.getType();
        // Get late deliverable type ID:
        long lateDeliverableTypeId = lateDeliverableTypeIds.get(lateDeliverable.getType());

        boolean canSendNotification = false;
        boolean addTrackingRecord = false;
        Date recordDeadline = currentDeadline;
        String explanation = null; // NEW in 1.2
        // Prepare variable that will store late deliverable record creation date:
        Date createDate = new Date(); // NEW in 1.2

        Object[] result = doQuery(SELECT_MAX_DEADLINE_SQL, connection, new Object[] {phaseId, resourceId,
            deliverableId, lateDeliverableTypeId}, true, Timestamp.class, Long.class);
        // Check if records with the same phase ID, resource ID, deliverable and late deliverable type ID exists:
        boolean alreadyTracked = ((Long) result[1] > 0);
        if (alreadyTracked && (lateDeliverableType == LateDeliverableType.REJECTED_FINAL_FIX)) {
            return;
        }

        Date oldDeadline = (Timestamp) result[0];
        if (alreadyTracked && (oldDeadline != null)) {
            if (oldDeadline.getTime() < currentDeadline.getTime()) {
                // deadline was extended, but the user is late again
                addTrackingRecord = true;
                canSendNotification = true;
            } else if (needToNotify && (notificationInterval != 0)) {
                recordDeadline = oldDeadline;
                // Prepare statement for retrieving last notification time and
                // "forgive" flag for this late deliverable
                result = doQuery(SELECT_LAST_NOTIFICATION_TIME_FORGIVE_SQL, connection,
                    new Object[] {phaseId, resourceId, new Timestamp(recordDeadline.getTime()), deliverableId,
                        lateDeliverableTypeId}, false, Timestamp.class, Boolean.class, String.class, Timestamp.class);

                int index = 0;
                // Get the previous notification timestamp
                Date previousNotificationTime = (Timestamp) result[index++];

                if (!((Boolean) result[index++])
                    && (previousNotificationTime != null)
                    && ((System.currentTimeMillis() - previousNotificationTime.getTime())
                        >= (notificationInterval * Helper.THOUSAND))) {
                    // notificationInterval passed, need to send one more
                    // notification
                    canSendNotification = true;
                }

                // Get explanation:
                explanation = (String) result[index++]; // NEW in 1.2
                // Get record creation date:
                createDate = (Timestamp) result[index]; // NEW in 1.2
            }
        } else {
            // the user is late for the first time with this deliverable
            addTrackingRecord = true;
            canSendNotification = true;
        }

        canSendNotification = canSendNotification && needToNotify;

        if (addTrackingRecord) {
            doDMLQuery(buildInsertSQL(lateDeliverableType, canSendNotification), connection,
                buildInsertParams(lateDeliverableTypeId, phaseId, resourceId, deliverableId, lateDeliverableType,
                    currentDeadline, lateDeliverable.getCompensatedDeadline()));

            // log data for record added to late_deliverables table
            Helper.logInfo(log, "late deliverable data : project id[" + project.getId() + "], phase id["
                + phaseId + "]," + " resource id[" + resourceId + "], deliverable id[" + deliverableId + "].");
        } else {
            doDMLQuery(String.format(UPDATE_SQL, canSendNotification ? "last_notified = current, " : ""),
                connection, new Object[] {phaseId, resourceId, new Timestamp(recordDeadline.getTime()), deliverableId,
                    lateDeliverableTypeId});
        }

        if (canSendNotification) {
            // retrieve late deliverable ID
            result = doQuery(
                (lateDeliverableType == LateDeliverableType.MISSED_DEADLINE)
                    ? SELECT_LATE_DELIVERABLE_ID_WITH_MAX_DEADLINE_SQL
                        : SELECT_LATE_DELIVERABLE_ID_WITH_REJECTED_FF_SQL, connection,
                        new Object[] {lateDeliverableTypeId, phaseId, resourceId, deliverableId}, false, Long.class);

            sendEmail(lateDeliverableType, (Long) result[0], lateDeliverable, explanation, createDate);
        }
    }

    /**
     * Builds the parameters for inserting a record.
     *
     * @param lateDeliverableTypeId
     *            the late deliverable type id.
     * @param phaseId
     *            the phase id.
     * @param resourceId
     *            the resource id.
     * @param deliverableId
     *            the deliverable id.
     * @param lateDeliverableType
     *            the late deliverable type.
     * @param currentDeadline
     *            the current deadline.
     * @param compensatedDeadline
     *            the compensated deadline.
     *
     * @return the parameters.
     *
     * @since 1.3
     */
    private static Object[] buildInsertParams(long lateDeliverableTypeId, long phaseId, long resourceId,
        long deliverableId, LateDeliverableType lateDeliverableType, Date currentDeadline,
        Date compensatedDeadline) {
        List<Object> params = new ArrayList<Object>();
        params.add(lateDeliverableTypeId);
        params.add(phaseId);
        params.add(resourceId);
        params.add(deliverableId);
        params.add(false);
        if (lateDeliverableType == LateDeliverableType.MISSED_DEADLINE) {
            Timestamp deadlineTimestamp = new Timestamp(currentDeadline.getTime());
            // Get compensated deadline:
            Timestamp compensatedDeadlineTimestamp = (compensatedDeadline != null) ? new Timestamp(
                compensatedDeadline.getTime()) : null;

            params.add(deadlineTimestamp);
            params.add(compensatedDeadlineTimestamp);
            params.add((compensatedDeadlineTimestamp != null) ? compensatedDeadlineTimestamp : deadlineTimestamp);

        }

        return params.toArray(new Object[] {params.size()});
    }

    /**
     * Constructs the SQL string to insert a record.
     *
     * @param lateDeliverableType
     *            the late deliverable type.
     * @param canSendNotification
     *            the can send notification flag.
     *
     * @return the SQL string to insert a record.
     *
     * @since 1.3
     */
    private static String buildInsertSQL(LateDeliverableType lateDeliverableType, boolean canSendNotification) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("insert into late_deliverable (late_deliverable_type_id, project_phase_id")
            .append(", resource_id, deliverable_id, create_date, forgive_ind");

        StringBuilder valuesStr = new StringBuilder();
        valuesStr.append(") values (?, ?, ?, ?, current, ?");

        if (lateDeliverableType == LateDeliverableType.MISSED_DEADLINE) {
            // Missed deadline

            sqlStr.append(", deadline, compensated_deadline, delay");
            valuesStr.append(", ?, ?, (current - ?)::interval second(9) to second::char(16)::decimal(16,0)");
        }
        if (canSendNotification) {
            // Last notified

            sqlStr.append(", last_notified");
            valuesStr.append(", current");
        }

        return sqlStr.append(valuesStr).append(")").toString();
    }

    /**
     * Checks the given <code>lateDeliverable</code>.
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Added type check.</li>
     * </ol>
     * </p>
     *
     * @param signature
     *            the method name.
     * @param lateDeliverable
     *            the late deliverable to be processed.
     * @throws IllegalArgumentException
     *             if lateDeliverable is null, or any one of phase, project, deliverable and type of lateDeliverable is
     *             null, or the scheduled end date of phase is null.
     */
    private void checkArgument(LateDeliverable lateDeliverable, String signature) {
        try {
            ExceptionUtils.checkNull(lateDeliverable, null, null,
                "The parameter 'lateDeliverable' should not be null.");
            ExceptionUtils.checkNull(lateDeliverable.getPhase(), null, null,
                "The phase of lateDeliverable should not be null.");
            ExceptionUtils.checkNull(lateDeliverable.getProject(), null, null,
                "The project of lateDeliverable should not be null.");
            ExceptionUtils.checkNull(lateDeliverable.getDeliverable(), null, null,
                "The deliverable of lateDeliverable should not be null.");
            ExceptionUtils.checkNull(lateDeliverable.getType(), null, null,
                "The type of lateDeliverable should not be null.");
            ExceptionUtils.checkNull(lateDeliverable.getPhase().getScheduledEndDate(), null, null,
                "The scheduledEndDate of phase of lateDeliverable should not be null.");
        } catch (IllegalArgumentException e) {
            throw Helper.logException(log, signature, e);
        }
    }

    /**
     * Checks all required fields have been set.
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Updated the code.</li>
     * </ol>
     * </p>
     *
     * @param signature
     *            the method name.
     * @throws IllegalStateException
     *             if this class was not configured properly with use of
     *             {@link #configure(ConfigurationObject)} method.
     */
    private void checkState(String signature) {
        Helper.checkState(timestampFormat, "timestampFormat", log, signature);
        Helper.checkState(dbConnectionFactory, "dbConnectionFactory", log, signature);
        Helper.checkState(emailSendingUtility, "emailSendingUtility", log, signature);
        Helper.checkState(missedDeadlineNotificationDeliverableIds, "missedDeadlineNotificationDeliverableIds", log,
            signature);
        Helper.checkState(missedDeadlineEmailSubjectTemplateTexts, "missedDeadlineEmailSubjectTemplateTexts", log,
            signature);
        Helper.checkState(missedDeadlineEmailBodyTemplatePaths, "missedDeadlineEmailBodyTemplatePaths", log,
            signature);
        Helper.checkState(defaultMissedDeadlineEmailSubjectTemplateText,
            "defaultMissedDeadlineEmailSubjectTemplateText", log, signature);
        Helper.checkState(defaultMissedDeadlineEmailBodyTemplatePath, "defaultMissedDeadlineEmailBodyTemplatePath",
            log, signature);
        Helper.checkState(rejectedFinalFixEmailSubjectTemplateText, "rejectedFinalFixEmailSubjectTemplateText", log,
            signature);
        Helper.checkState(rejectedFinalFixEmailBodyTemplatePath, "rejectedFinalFixEmailBodyTemplatePath", log,
            signature);
        Helper.checkState(resourceManager, "resourceManager", log, signature);
        Helper.checkState(userRetrieval, "userRetrieval", log, signature);
        Helper.checkState(lateDeliverableTypeIds, "lateDeliverableTypeIds", log, signature);
    }

    /**
     * Does select query and return result as object array. The result will always contains two elements, if the
     * <code>hasSecondColumn</code> is false, the second element of result is null.
     *
     * @param query
     *            the query statement.
     * @param connection
     *            the database connection.
     * @param parameters
     *            the parameters to set.
     * @param emptyResultSet
     *            whether the query can return an empty result set.
     * @param resultColumnTypes
     *            the expected types of columns in the result set.
     * @return the result from the query.
     * @throws SQLException
     *             if any error occurs.
     * @throws LateDeliverablesProcessingException
     *             if query result does not contain record if <code>hasSecondColumn</code> is <code>true</code>.
     */
    private static Object[] doQuery(String query, Connection connection, Object[] parameters, boolean emptyResultSet,
        Class<?>... resultColumnTypes) throws SQLException, LateDeliverablesProcessingException {
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(query);

            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] instanceof Timestamp) {
                    ps.setTimestamp(i + 1, (Timestamp) parameters[i]);
                } else {
                    ps.setLong(i + 1, (Long) parameters[i]);
                }
            }

            ResultSet rs = ps.executeQuery();

            if (!rs.next() && !emptyResultSet) {
                throw new LateDeliverablesProcessingException("The query should not "
                    + " return an empty result set.");
            }

            Object[] result = new Object[resultColumnTypes.length];

            for (int i = 0; i < resultColumnTypes.length; i++) {
                if (resultColumnTypes[i] == Timestamp.class) {
                    result[i] = rs.getTimestamp(i + 1);
                } else if (resultColumnTypes[i] == Boolean.class) {
                    result[i] = rs.getBoolean(i + 1);
                } else if (resultColumnTypes[i] == Long.class) {
                    result[i] = rs.getLong(i + 1);
                } else {
                    result[i] = rs.getObject(i + 1);
                    if (result[i] != null && !resultColumnTypes[i].isInstance(result[i])) {
                        throw new LateDeliverablesProcessingException("The result set "
                            + " contains a value of not expected type ("
                            + result[i].getClass().getName() + " obtained, but "
                            + resultColumnTypes[i].getName() + " expected)");
                    }
                }
            }

            return result;
        } finally {
            // Close the prepared statement
            closeStatement(ps);
        }
    }

    /**
     * <p>
     * Does update or insert query.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Added support for null value.</li>
     * </ol>
     * </p>
     *
     * @param query
     *            the sql statement to do.
     * @param connection
     *            the database connection.
     * @param parameters
     *            the parameters to set.
     * @throws SQLException
     *             if any error occurs.
     */
    private static void doDMLQuery(String query, Connection connection, Object[] parameters)
        throws SQLException {
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(query);

            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] instanceof Timestamp) {
                    ps.setTimestamp(i + 1, (Timestamp) parameters[i]);
                } else if (parameters[i] instanceof Long) {
                    ps.setLong(i + 1, (Long) parameters[i]);
                } else if (parameters[i] instanceof Boolean) {
                    ps.setBoolean(i + 1, (Boolean) parameters[i]);
                } else {
                    ps.setObject(i + 1, parameters[i]);
                }
            }

            ps.executeUpdate();
        } finally {
            closeStatement(ps);
        }
    }

    /**
     * Closes given statement.
     *
     * @param statement
     *            the statement to close.
     * @throws SQLException
     *             if any error occurs.
     */
    private static void closeStatement(PreparedStatement statement) throws SQLException {
        if (statement != null) {
            statement.close();
        }
    }

    /**
     * Closes the given database connection.
     *
     * @param connection
     *            the database connection (not <code>null</code>).
     * @param signature
     *            the method name.
     * @throws LateDeliverablesProcessingException
     *             if SQL error occurs when closing connection.
     */
    private void closeConnection(Connection connection, String signature) throws LateDeliverablesProcessingException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw Helper.logException(log, signature, new LateDeliverablesProcessingException(
                "Fails to close connection.", e));
        }
    }

    /**
     * Undoes all changes in current transaction.
     *
     * @param connection
     *            the database connection.
     * @param signature
     *            the method name.
     */
    private void rollback(Connection connection, String signature) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            // log and ignore
            Helper.logException(log, signature, e);
        }
    }

    /**
     * Gets necessary information of email and send it.
     *
     * <p>
     * <em>Change in 1.1:</em>
     * <ol>
     * <li>Signature of the method was changed. Now it accepts the only lateDeliverable:LateDeliverable argument
     * instead of (project:Project,phase:Phase,deliverable:Deliverable).</li>
     * </ol>
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Added explanation and createDate parameters.</li>
     * </ol>
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Added support of REJECTED_FINAL_FIX late deliverable type.</li>
     * </ol>
     * </p>
     *
     * @param lateDeliverableType
     *            the type of the late deliverable.
     * @param lateDeliverableId
     *            the id of the late deliverable.
     * @param lateDeliverable
     *            the late deliverable for which parameters should be prepared.
     * @param explanation
     *            the explanation of the late deliverable (null if not provided).
     * @param createDate
     *            the creation date of the late deliverable record.
     *
     * @throws EmailSendingException
     *             if some error occurred when sending a notification email message.
     * @throws LateDeliverablesProcessingException
     *             if fails to get the required information of email.
     */
    private void sendEmail(LateDeliverableType lateDeliverableType, long lateDeliverableId,
        LateDeliverable lateDeliverable, String explanation, Date createDate)
        throws LateDeliverablesProcessingException {
        long resourceId = lateDeliverable.getDeliverable().getResource();
        long deliverableId = lateDeliverable.getDeliverable().getId();

        String recipient = getEmailAddressForResource(resourceId);
        Map<String, Object> params = prepareParameters(lateDeliverableId, lateDeliverable, explanation, createDate);

        String subjectTemplateText;
        String bodyTemplatePath;

        if (lateDeliverableType == LateDeliverableType.MISSED_DEADLINE) {
            if (missedDeadlineEmailSubjectTemplateTexts.containsKey(deliverableId)) {
                // Get email subject template to be used
                subjectTemplateText = missedDeadlineEmailSubjectTemplateTexts.get(deliverableId);
            } else {
                subjectTemplateText = defaultMissedDeadlineEmailSubjectTemplateText;
            }
            if (missedDeadlineEmailBodyTemplatePaths.containsKey(deliverableId)) {
                // Get email body template path to be used:
                bodyTemplatePath = missedDeadlineEmailBodyTemplatePaths.get(deliverableId);
            } else {
                bodyTemplatePath = defaultMissedDeadlineEmailBodyTemplatePath;
            }
        } else {
            subjectTemplateText = rejectedFinalFixEmailSubjectTemplateText;
            bodyTemplatePath = rejectedFinalFixEmailBodyTemplatePath;
        }

        // Send warning email to the user
        emailSendingUtility.sendEmail(subjectTemplateText, bodyTemplatePath, recipient, params);
    }

    /**
     * Gets the database connection.
     *
     * @param signature
     *            the method name.
     * @return the database connection
     * @throws LateDeliverablesProcessingException
     *             if any error occurs.
     */
    private Connection getConnection(String signature) throws LateDeliverablesProcessingException {
        try {
            if (connectionName == null) {
                return this.dbConnectionFactory.createConnection();
            } else {
                return this.dbConnectionFactory.createConnection(connectionName);
            }
        } catch (DBConnectionException e) {
            throw Helper.logException(log, signature, new LateDeliverablesProcessingException(
                "Fails to create database connection.", e));
        }
    }

    /**
     * Retrieves the email address for resource with the given ID.
     *
     * @param resourceId
     *            the ID of the resource.
     * @return the retrieved email address for resource (not null).
     * @throws LateDeliverablesProcessingException
     *             if any error occurred.
     */
    private String getEmailAddressForResource(long resourceId)
        throws LateDeliverablesProcessingException {
        Resource resource;

        try {
            resource = resourceManager.getResource(resourceId);
        } catch (ResourcePersistenceException e) {
            throw new LateDeliverablesProcessingException("Fails to get resource.", e);
        }

        if (resource == null) {
            throw new LateDeliverablesProcessingException("Resource with id[" + resourceId
                + "] not exist.");
        }

        long userId = resource.getUserId();

        ExternalUser user;

        try {
            user = userRetrieval.retrieveUser(userId);
        } catch (RetrievalException e) {
            throw new LateDeliverablesProcessingException("Fails to retrieve external user for id : "
                + userId, e);
        }

        if (user == null) {
            throw new LateDeliverablesProcessingException("External user with id[" + userId
                + "] not exist.");
        }

        String email = user.getEmail();

        if (email == null) {
            throw new LateDeliverablesProcessingException("email address of resource is null.");
        }

        return email;
    }

    /**
     * Prepares the parameters to be used in the email subject/body template.
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Signature of the method was changed. Now it accepts the only lateDeliverable:LateDeliverable argument
     * instead of (project:Project,phase:Phase,deliverable:Deliverable).</li>
     * <li>Added steps for extracting project, phase and deliverable from lateDeliverable.</li>
     * <li>Added steps for putting COMPENSATED_DEADLINE and COMPENSATED_AND_REAL_DEADLINES_DIFFER parameters to the
     * map.</li>
     * </ol>
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Changed return type.</li>
     * <li>Added explanation and createDate parameters.</li>
     * <li>Added steps for preparing EXPLANATION, EXPLANATION_DEADLINE and EXPLANATION_CAN_BE_SENT parameters.</li>
     * </ol>
     * </p>
     *
     * @param lateDeliverableId
     *            the id of the late deliverable
     * @param lateDeliverable
     *            the late deliverable for which parameters should be prepared.
     * @param explanation
     *            the explanation of the late deliverable (null if not provided).
     * @param createDate
     *            the creation date of the late deliverable record.
     *
     * @return the map with prepared parameters (keys are parameter names, map values parameter values; not null,
     *         doesn't contain null/empty key or null value).
     *
     * @throws LateDeliverablesProcessingException
     *             if any error occurred.
     */
    private Map<String, Object> prepareParameters(long lateDeliverableId, LateDeliverable lateDeliverable,
        String explanation, Date createDate)
        throws LateDeliverablesProcessingException {
        // Get deliverable:
        Deliverable deliverable = lateDeliverable.getDeliverable();
        // Get phase:
        Phase phase = lateDeliverable.getPhase();
        // Get project:
        Project project = lateDeliverable.getProject();
        // Get compensated deadline (is null if not differs with the real one):
        Date compensatedDeadline = lateDeliverable.getCompensatedDeadline();
        // Check if compensated deadline differs with the real one:
        boolean deadlinesDiffer = (compensatedDeadline != null);

        Map<String, Object> result = new HashMap<String, Object>(); // UPDATED in 1.2

        // project name
        result.put("PROJECT_NAME", getPropertyValue(project, "Project Name"));

        // project version
        result.put("PROJECT_VERSION", getPropertyValue(project, "Project Version"));

        result.put("PROJECT_ID", String.valueOf(project.getId()));

        PhaseType phaseType = phase.getPhaseType();
        checkNull(phaseType, "phaseType of phase");

        String phaseName = phaseType.getName();
        checkNull(phaseName, "name of phaseType");
        result.put("PHASE_NAME", phaseName);

        // Get deliverable name:
        String deliverableName = deliverable.getName();
        checkNull(deliverableName, "name of deliverable");
        result.put("DELIVERABLE_NAME", deliverableName);

        // Get scheduled end date of the phase (deadline):
        Date phaseEndDate = phase.getScheduledEndDate();
        checkNull(phaseEndDate, "scheduledEndDate of phase");

        // Convert date/time to string:
        result.put("DEADLINE", timestampFormat.format(phaseEndDate));
        // Construct and put delay string to the result map
        result.put("DELAY", Helper.delayToString(System.currentTimeMillis() - phaseEndDate.getTime()));

        // Put the value that indicates whether compensated deadline differs from the real one:
        result.put("COMPENSATED_AND_REAL_DEADLINES_DIFFER", Boolean.toString(deadlinesDiffer));
        // Put compensated deadline string to the map:
        result.put("COMPENSATED_DEADLINE",
            timestampFormat.format((compensatedDeadline == null) ? phaseEndDate : compensatedDeadline));
        // Put late deliverable ID to the map:
        result.put("LATE_DELIVERABLE_ID", String.valueOf(lateDeliverableId));

        // Put late deliverable explanation to the map:
        result.put("EXPLANATION", (explanation == null) ? "" : explanation); // NEW in 1.2
        // Calculate explanation deadline:
        Date explanationDeadline = new Date(createDate.getTime()
            + Helper.MINUTE * Helper.SIXTY * explanationDeadlineIntervalInHours); // NEW in 1.2
        // Put explanation deadline string to the map:
        result.put("EXPLANATION_DEADLINE", timestampFormat.format(explanationDeadline)); // NEW in 1.2
        // Put flag indicating whether explanation can be sent:
        result.put("EXPLANATION_CAN_BE_SENT", Boolean.toString(
            (explanation == null && (System.currentTimeMillis() < explanationDeadline.getTime())))); // NEW in 1.2

        return result;
    }

    /**
     * Gets the project property value for the specified property name.
     *
     * @param project
     *            the project.
     * @param propertyName
     *            the property name.
     * @return the property value.
     * @throws LateDeliverablesProcessingException
     *             if the value is <code>null</code> or the value is not
     *             <code>String</code> type.
     */
    private static String getPropertyValue(Project project, String propertyName)
        throws LateDeliverablesProcessingException {
        try {
            String result = (String) project.getProperty(propertyName);

            if (result == null) {
                throw new LateDeliverablesProcessingException("The property value of [" + propertyName
                    + "] should not be null.");
            }

            return result;
        } catch (ClassCastException e) {
            throw new LateDeliverablesProcessingException("The property value of [" + propertyName
                + "] is not of String type.", e);
        }
    }

    /**
     * Checks the given value for null.
     *
     * @param value
     *            the value to check.
     * @param name
     *            the name of the value.
     * @throws LateDeliverablesProcessingException
     *             if the given value is <code>null</code>.
     */
    private static void checkNull(Object value, String name) throws LateDeliverablesProcessingException {
        if (value == null) {
            throw new LateDeliverablesProcessingException("The " + name + " should not be null.");
        }
    }
}
