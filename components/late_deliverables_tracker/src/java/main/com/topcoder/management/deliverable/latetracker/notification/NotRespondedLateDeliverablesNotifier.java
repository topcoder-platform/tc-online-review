/*
 * Copyright (C) 2011-2014 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.notification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.late.LateDeliverableManagementException;
import com.topcoder.management.deliverable.late.LateDeliverableManager;
import com.topcoder.management.deliverable.late.search.LateDeliverableFilterBuilder;
import com.topcoder.management.deliverable.latetracker.EmailSendingException;
import com.topcoder.management.deliverable.latetracker.EmailSendingUtility;
import com.topcoder.management.deliverable.latetracker.Helper;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackerConfigurationException;
import com.topcoder.management.deliverable.persistence.DeliverablePersistence;
import com.topcoder.management.deliverable.persistence.DeliverablePersistenceException;
import com.topcoder.management.phase.PhasePersistence;
import com.topcoder.management.phase.PhasePersistenceException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.util.errorhandling.ExceptionUtils;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.objectfactory.ObjectFactory;

/**
 * <p>
 * This class provides a programmatic API for sending email notification about explained but not responded late
 * deliverables to the managers. It provides just a single execute() method that sends notifications for all such late
 * deliverables. Note that a single email message is sent to each manager, and this email message contains information
 * about all late deliverables to be responded by this manager. This class performs the logging of errors and debug
 * information using Logging Wrapper component.
 * </p>
 *
 * <p>
 * <em>Sample Configuration:</em>
 *
 * <pre>
 * &lt;?xml version=&quot;1.0&quot;?&gt;
 * &lt;CMConfig&gt;
 *     &lt;Config name=&quot;
 *         com.topcoder.management.deliverable.latetracker.notification.NotRespondedLateDeliverablesNotifier&quot;&gt;
 *         &lt;Property name=&quot;loggerName&quot;&gt;
 *             &lt;Value&gt;myLogger&lt;/Value&gt;
 *         &lt;/Property&gt;
 *         &lt;Property name=&quot;objectFactoryConfig&quot;&gt;
 *         &lt;property name=&quot;lateDeliverableManager&quot;&gt;
 *             &lt;property name=&quot;type&quot;&gt;
 *               &lt;value&gt;com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl&lt;/value&gt;
 *             &lt;/property&gt;
 *         &lt;/property&gt;
 *             &lt;property name=&quot;resourceManager&quot;&gt;
 *             &lt;property name=&quot;type&quot;&gt;
 *               &lt;value&gt;com.topcoder.management.resource.persistence.PersistenceResourceManager&lt;/value&gt;
 *             &lt;/property&gt;
 *             &lt;Property name=&quot;params&quot;&gt;
 *               &lt;Property name=&quot;param1&quot;&gt;
 *                 &lt;Property name=&quot;name&quot;&gt;
 *                   &lt;Value&gt;ResourcePersistence&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *               &lt;/Property&gt;
 *               &lt;Property name=&quot;param2&quot;&gt;
 *                 &lt;Property name=&quot;name&quot;&gt;
 *                   &lt;Value&gt;SearchBundleManager&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *               &lt;/Property&gt;
 *             &lt;/Property&gt;
 *         &lt;/property&gt;
 *         &lt;property name=&quot;ResourcePersistence&quot;&gt;
 *             &lt;property name=&quot;type&quot;&gt;
 *               &lt;value&gt;com.topcoder.management.resource.persistence.sql.SqlResourcePersistence&lt;/value&gt;
 *             &lt;/property&gt;
 *             &lt;Property name=&quot;params&quot;&gt;
 *               &lt;Property name=&quot;param1&quot;&gt;
 *                 &lt;Property name=&quot;name&quot;&gt;
 *                   &lt;Value&gt;DBConnectionFactory&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *               &lt;/Property&gt;
 *             &lt;/Property&gt;
 *         &lt;/property&gt;
 *         &lt;property name=&quot;SearchBundleManager&quot;&gt;
 *             &lt;property name=&quot;type&quot;&gt;
 *               &lt;value&gt;com.topcoder.search.builder.SearchBundleManager&lt;/value&gt;
 *             &lt;/property&gt;
 *             &lt;Property name=&quot;params&quot;&gt;
 *               &lt;Property name=&quot;param1&quot;&gt;
 *                 &lt;Property name=&quot;type&quot;&gt;
 *                   &lt;Value&gt;String&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name=&quot;value&quot;&gt;
 *                   &lt;Value&gt;com.topcoder.search.builder.SearchBundleManager&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *               &lt;/Property&gt;
 *             &lt;/Property&gt;
 *         &lt;/property&gt;
 *             &lt;property name=&quot;userRetrieval&quot;&gt;
 *                     &lt;property name=&quot;type&quot;&gt;
 *                     &lt;value&gt;com.cronos.onlinereview.external.impl.DBUserRetrieval&lt;/value&gt;
 *                 &lt;/property&gt;
 *                 &lt;Property name=&quot;params&quot;&gt;
 *                     &lt;Property name=&quot;param1&quot;&gt;
 *                         &lt;Property name=&quot;type&quot;&gt;
 *                             &lt;Value&gt;String&lt;/Value&gt;
 *                         &lt;/Property&gt;
 *                         &lt;Property name=&quot;value&quot;&gt;
 *                             &lt;Value&gt;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&lt;/Value&gt;
 *                         &lt;/Property&gt;
 *                     &lt;/Property&gt;
 *                   &lt;/Property&gt;
 *             &lt;/property&gt;
 *             &lt;property name=&quot;projectManager&quot;&gt;
 *                 &lt;property name=&quot;type&quot;&gt;
 *                     &lt;value&gt;com.topcoder.management.project.ProjectManagerImpl&lt;/value&gt;
 *                 &lt;/property&gt;
 *             &lt;/property&gt;
 *             &lt;property name=&quot;phasePersistence&quot;&gt;
 *                 &lt;property name=&quot;type&quot;&gt;
 *                     &lt;value&gt;com.topcoder.management.phase.db.InformixPhasePersistence&lt;/value&gt;
 *                 &lt;/property&gt;
 *                 &lt;Property name=&quot;params&quot;&gt;
 *                     &lt;Property name=&quot;param1&quot;&gt;
 *                         &lt;Property name=&quot;type&quot;&gt;
 *                             &lt;Value&gt;String&lt;/Value&gt;
 *                         &lt;/Property&gt;
 *                         &lt;Property name=&quot;value&quot;&gt;
 *                             &lt;Value&gt;com.topcoder.management.phase.db.InformixPhasePersistence&lt;/Value&gt;
 *                         &lt;/Property&gt;
 *                     &lt;/Property&gt;
 *                 &lt;/Property&gt;
 *             &lt;/property&gt;
 *             &lt;property name=&quot;deliverablePersistence&quot;&gt;
 *                 &lt;property name=&quot;type&quot;&gt;
 *                     &lt;value&gt;
 *                         com.topcoder.management.deliverable.persistence.sql.SqlDeliverablePersistence&lt;/value&gt;
 *                 &lt;/property&gt;
 *                 &lt;Property name=&quot;params&quot;&gt;
 *                     &lt;Property name=&quot;param1&quot;&gt;
 *                         &lt;Property name=&quot;name&quot;&gt;
 *                             &lt;Value&gt;DBConnectionFactory&lt;/Value&gt;
 *                         &lt;/Property&gt;
 *                     &lt;/Property&gt;
 *                 &lt;/Property&gt;
 *             &lt;/property&gt;
 *             &lt;property name=&quot;DBConnectionFactory&quot;&gt;
 *                     &lt;property name=&quot;type&quot;&gt;
 *                     &lt;value&gt;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&lt;/value&gt;
 *                 &lt;/property&gt;
 *                 &lt;Property name=&quot;params&quot;&gt;
 *                     &lt;Property name=&quot;param1&quot;&gt;
 *                         &lt;Property name=&quot;type&quot;&gt;
 *                             &lt;Value&gt;String&lt;/Value&gt;
 *                         &lt;/Property&gt;
 *                         &lt;Property name=&quot;value&quot;&gt;
 *                             &lt;Value&gt;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&lt;/Value&gt;
 *                         &lt;/Property&gt;
 *                     &lt;/Property&gt;
 *                   &lt;/Property&gt;
 *             &lt;/property&gt;
 *         &lt;/Property&gt;
 *     &lt;Property name=&quot;lateDeliverableManagerKey&quot;&gt;
 *         &lt;Value&gt;lateDeliverableManager&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;managerResourceRoleIds&quot;&gt;
 *         &lt;Value&gt;3&lt;/Value&gt;
 *         &lt;Value&gt;4&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;resourceManagerKey&quot;&gt;
 *         &lt;Value&gt;resourceManager&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;userRetrievalKey&quot;&gt;
 *         &lt;Value&gt;userRetrieval&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;emailSubjectTemplateText&quot;&gt;
 *         &lt;Value&gt;
 *           WARNING\: You have explained late deliverable(s) to be responded
 *         &lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;emailBodyTemplatePath&quot;&gt;
 *         &lt;Value&gt;test_files/pm_notification_email_template.html&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;emailSender&quot;&gt;
 *         &lt;Value&gt;service@topcoder.com&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;timestampFormat&quot;&gt;
 *         &lt;Value&gt;yyyy-MM-dd HH:mm:ss&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;projectManagerKey&quot;&gt;
 *         &lt;Value&gt;projectManager&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;phasePersistenceKey&quot;&gt;
 *         &lt;Value&gt;phasePersistence&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;deliverablePersistenceKey&quot;&gt;
 *         &lt;Value&gt;deliverablePersistence&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;/Config&gt;
 * &lt;/CMConfig&gt;
 * </pre>
 *
 * </p>
 *
 * <p>
 * <em>Sample Code:</em>
 *
 * <pre>
 * // Prepare configuration for NotRespondedLateDeliverablesNotifier
 * ConfigurationObject notRespondedLateDeliverablesNotifierConfig =
 *     getConfigurationObject(&quot;config/NotRespondedLateDeliverablesNotifier.xml&quot;,
 *         NotRespondedLateDeliverablesNotifier.class.getName());
 *
 * // Create an instance of NotRespondedLateDeliverablesNotifier
 * NotRespondedLateDeliverablesNotifier notRespondedLateDeliverablesNotifier =
 *     new NotRespondedLateDeliverablesNotifier(notRespondedLateDeliverablesNotifierConfig);
 *
 * // Send notifications for explained but not responded late deliverables
 * notRespondedLateDeliverablesNotifier.execute();
 * </pre>
 *
 * </p>
 *
 * <p>
 * <em>Changes in version 1.3:</em>
 * <ol>
 * <li>Added support of "LATE_DELIVERABLE_TYPE" email template field.</li>
 * </ol>
 * </p>
 *
 * <p>
 * Thread Safety: This class is immutable, but not thread safe since it uses ProjectManager, ResourceManager and
 * DeliverablePersistence instances that are not guaranteed to be thread safe.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.3.3
 * @since 1.2
 */
public class NotRespondedLateDeliverablesNotifier {
    /**
     * <p>
     * Represents the name of this class used for logging.
     * </p>
     */
    private static final String CLASS_NAME = NotRespondedLateDeliverablesNotifier.class.getName();

    /**
     * <p>
     * Represents the default time format pattern.
     * </p>
     */
    private static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    /**
     * <p>
     * Represents the property key 'lateDeliverableManagerKey'.
     * </p>
     */
    private static final String KEY_DELIVERABLE_MANAGER = "lateDeliverableManagerKey";

    /**
     * <p>
     * Represents the property key 'managerResourceRoleIds'.
     * </p>
     */
    private static final String KEY_MANAGER_ROLE_IDS = "managerResourceRoleIds";

    /**
     * <p>
     * Represents the property key 'resourceManagerKey'.
     * </p>
     */
    private static final String KEY_RESOURCE_MANAGER = "resourceManagerKey";

    /**
     * <p>
     * Represents the property key 'userRetrievalKey'.
     * </p>
     */
    private static final String KEY_USER_RETRIEVAL = "userRetrievalKey";

    /**
     * <p>
     * Represents the property key 'emailSubjectTemplateText'.
     * </p>
     */
    private static final String KEY_SUBJECT_TEMPLATE_TEXT = "emailSubjectTemplateText";

    /**
     * <p>
     * Represents the property key 'emailBodyTemplatePath'.
     * </p>
     */
    private static final String KEY_BODY_TEMPLATE_PATH = "emailBodyTemplatePath";

    /**
     * <p>
     * Represents the property key 'emailSender'.
     * </p>
     */
    private static final String KEY_SENDER = "emailSender";

    /**
     * <p>
     * Represents the property key 'timestampFormat'.
     * </p>
     */
    private static final String KEY_TIMESTAMP_FORMAT = "timestampFormat";

    /**
     * <p>
     * Represents the property key 'projectManagerKey'.
     * </p>
     */
    private static final String KEY_PROJECT_MANAGER = "projectManagerKey";

    /**
     * <p>
     * Represents the property key 'phasePersistenceKey'.
     * </p>
     */
    private static final String KEY_PHASE_PERSISTENCE = "phasePersistenceKey";

    /**
     * <p>
     * Represents the property key 'deliverablePersistenceKey'.
     * </p>
     */
    private static final String KEY_DELIVERABLE_PERSISTENCE = "deliverablePersistenceKey";

    /**
     * <p>
     * The late deliverable manager used by this class for retrieving explained, but not responded late deliverables.
     * </p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Cannot be null. Is used in execute().
     * </p>
     */
    private final LateDeliverableManager lateDeliverableManager;

    /**
     * <p>
     * The set of IDs of the manager resource roles that represent managers to be notified about not responded late
     * deliverables.
     * </p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Cannot be null. Cannot contain null. Contains
     * positive elements only. Is used in execute().
     * </p>
     */
    private final Set<Long> managerResourceRoleIds;

    /**
     * <p>
     * The resource manager to be used by this class for retrieving user ID by resource ID.
     * </p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Cannot be null. Is used in execute().
     * </p>
     */
    private final ResourceManager resourceManager;

    /**
     * <p>
     * The user retrieval service to be used by this class for retrieving user email address by user ID. Is
     * initialized in the constructor and never changed after that. Cannot be null. Is used in execute().
     * </p>
     */
    private final UserRetrieval userRetrieval;

    /**
     * <p>
     * The email subject template text to be used for constructing messages to be sent to the managers.
     * </p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Cannot be null. Is used in execute().
     * </p>
     */
    private final String emailSubjectTemplateText;

    /**
     * <p>
     * The email body template path (resource path or file path) to be used for constructing messages to be sent to
     * the managers.
     * </p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Cannot be null or empty. Is used in execute().
     * </p>
     */
    private final String emailBodyTemplatePath;

    /**
     * <p>
     * The email sending utility to be used by this class for sending email messages.
     * </p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Cannot be null. Is used in execute().
     * </p>
     */
    private final EmailSendingUtility emailSendingUtility;

    /**
     * <p>
     * The timestamp format to be used for formatting timestamps in the email message.
     * </p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Cannot be null. Is used in execute().
     * </p>
     */
    private final DateFormat timestampFormat;

    /**
     * <p>
     * The project manager to be used by this class.
     * </p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Cannot be null. Is used in execute().
     * </p>
     */
    private final ProjectManager projectManager;

    /**
     * <p>
     * The phase persistence to be used by this class for retrieving phase name by its ID.
     * </p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Cannot be null. Is used in execute().
     * </p>
     */
    private final PhasePersistence phasePersistence;

    /**
     * <p>
     * The deliverable persistence used by this class for retrieving deliverable name by its ID.
     * </p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Cannot be null. Is used in execute().
     * </p>
     */
    private final DeliverablePersistence deliverablePersistence;

    /**
     * <p>
     * The logger used by this class for logging errors and debug information.
     * </p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. If is null, logging is not performed. Is used
     * in execute().
     * </p>
     */
    private final Log log;

    /**
     * <p>
     * Creates an instance of NotRespondedLateDeliverablesNotifier.
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
    public NotRespondedLateDeliverablesNotifier(ConfigurationObject config) {
        ExceptionUtils.checkNull(config, null, null, "The parameter 'config' should not be null.");

        // Create log
        String loggerName = Helper.getPropertyValue(config, Helper.LOGGER_NAME_KEY, false, false);
        log = (loggerName == null) ? null : LogManager.getLog(loggerName);

        // create object factory
        ObjectFactory objectFactory = Helper.createObjectFactory(config);

        // Create late deliverable manager
        lateDeliverableManager = Helper.createObject(config, objectFactory, KEY_DELIVERABLE_MANAGER,
            LateDeliverableManager.class);

        // Get manager resource role IDs from the configuration
        managerResourceRoleIds = getRoleIds(config);

        // Create resource manager
        resourceManager = Helper.createObject(config, objectFactory, KEY_RESOURCE_MANAGER, ResourceManager.class);
        userRetrieval = Helper.createObject(config, objectFactory, KEY_USER_RETRIEVAL, UserRetrieval.class);

        emailSubjectTemplateText = Helper.getPropertyValue(config, KEY_SUBJECT_TEMPLATE_TEXT, true, true);
        emailBodyTemplatePath = Helper.getPropertyValue(config, KEY_BODY_TEMPLATE_PATH, true, false);
        // Get email sender from config:
        String emailSender = Helper.getPropertyValue(config, KEY_SENDER, true, false);
        emailSendingUtility = new EmailSendingUtility(emailSender, log);

        // Get timestamp format to be used in the email
        String timestampFormatStr = Helper.getPropertyValue(config, KEY_TIMESTAMP_FORMAT, false, false);

        try {
            timestampFormat = new SimpleDateFormat(
                (timestampFormatStr == null) ? DEFAULT_TIME_FORMAT : timestampFormatStr);
        } catch (IllegalArgumentException e) {
            throw new LateDeliverablesTrackerConfigurationException("The timestamp format '" + timestampFormatStr
                + "' is invalid.", e);
        }

        projectManager = Helper.createObject(config, objectFactory, KEY_PROJECT_MANAGER, ProjectManager.class);
        phasePersistence = Helper.createObject(config, objectFactory, KEY_PHASE_PERSISTENCE, PhasePersistence.class);
        deliverablePersistence = Helper.createObject(config, objectFactory, KEY_DELIVERABLE_PERSISTENCE,
            DeliverablePersistence.class);
    }

    /**
     * <p>
     * Executes a notification operation. Retrieves all late deliverables that are explained, but not responded. Then
     * sends email notifications to the project managers of these late deliverables.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Added support of "LATE_DELIVERABLE_TYPE" email template field.</li>
     * <li>Using "N/A" when deadline or delay property is null.</li>
     * </ol>
     * </p>
     *
     * @throws NotRespondedLateDeliverablesNotificationException
     *             if some error occurred when retrieving not responded late deliverables or sending email
     *             notifications to managers.
     */
    public void execute() throws NotRespondedLateDeliverablesNotificationException {
        long start = System.currentTimeMillis();
        final String method = CLASS_NAME + ".execute()";

        Helper.logEntrance(log, method, null, null);

        try {
            // Get a mapping from user ID to the list of late deliverables to be responded by this user:
            Map<Long, List<LateDeliverableDetails>> lateDeliverableDetailsByUser = getLateDeliverableDetailsByUser();

            for (Entry<Long, List<LateDeliverableDetails>> entry : lateDeliverableDetailsByUser.entrySet()) {
                // Send warning email to the user:
                sendEmail(entry.getKey(), entry.getValue());
            }
        } catch (LateDeliverableManagementException e) {
            // Log exception
            throw Helper.logException(log, method, new NotRespondedLateDeliverablesNotificationException(
                "A late deliverable management error occurred.", e));
        } catch (SearchBuilderException e) {
            // Log exception
            throw Helper.logException(log, method, new NotRespondedLateDeliverablesNotificationException(
                "A search builder error occurred.", e));
        } catch (ResourcePersistenceException e) {
            // Log exception
            throw Helper.logException(log, method, new NotRespondedLateDeliverablesNotificationException(
                "A resource persistence error occurred.", e));
        } catch (PersistenceException e) {
            // Log exception
            throw Helper.logException(log, method, new NotRespondedLateDeliverablesNotificationException(
                "A project persistence error occurred.", e));
        } catch (PhasePersistenceException e) {
            // Log exception
            throw Helper.logException(log, method, new NotRespondedLateDeliverablesNotificationException(
                "A phase persistence error occurred.", e));
        } catch (DeliverablePersistenceException e) {
            // Log exception
            throw Helper.logException(log, method, new NotRespondedLateDeliverablesNotificationException(
                "A deliverable persistence error occurred.", e));
        } catch (RetrievalException e) {
            // Log exception
            throw Helper.logException(log, method, new NotRespondedLateDeliverablesNotificationException(
                "A user detail Retrieval error occurred.", e));
        } catch (EmailSendingException e) {
            // Log exception
            throw Helper.logException(log, method, new NotRespondedLateDeliverablesNotificationException(
                "An email sending error occurred.", e));
        } catch (NotRespondedLateDeliverablesNotificationException e) {
            // Log exception
            throw Helper.logException(log, method, e);
        } catch (Exception e) {
            // Log exception (ignored)
            Helper.logException(log, method, e);
        }

        Helper.logExit(log, method, null, start);
    }

    /**
     * <p>
     * Sends the late deliverable warning email to the user.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Added support of "LATE_DELIVERABLE_TYPE" email template field.</li>
     * <li>Using "N/A" when deadline or delay property is null.</li>
     * </ol>
     * </p>
     *
     * @param userId
     *            the user id.
     * @param lateDeliverableDetailsList
     *            the list of late deliverables to be responded by this user.
     *
     * @throws RetrievalException
     *             if a user details retrieval error occurs.
     * @throws EmailSendingException
     *             if an email sending error occurs.
     */
    private void sendEmail(Long userId, List<LateDeliverableDetails> lateDeliverableDetailsList)
        throws RetrievalException, EmailSendingException {
        // Retrieve user details by user ID:
        ExternalUser user = userRetrieval.retrieveUser(userId);
        // Get email address of the user:
        String email = user.getEmail();

        // Create map for parameters:
        Map<String, Object> params = new HashMap<String, Object>();

        // Put user ID to the map:
        params.put("USER_ID", userId.toString());

        // Put late deliverable records number to the map:
        params.put("RECORDS_NUM", Integer.toString(lateDeliverableDetailsList.size()));

        // Create a list for late deliverables parameters:
        // each element in the list will represent a single late deliverable
        List<Map<String, String>> lateDeliverablesParams = new ArrayList<Map<String, String>>();
        for (LateDeliverableDetails lateDeliverableDetails : lateDeliverableDetailsList) {
            // Create a map for late deliverable parameters:
            Map<String, String> lateDeliverableParams = new HashMap<String, String>();

            // Get deadline:
            Date deadline = lateDeliverableDetails.getDeadline();
            // Get compensated deadline:
            Date compensatedDeadline = lateDeliverableDetails.getCompensatedDeadline();

            // Put parameters specific to the next late deliverable to the map:
            lateDeliverableParams.put("LATE_DELIVERABLE_ID",
                Long.toString(lateDeliverableDetails.getLateDeliverableId()));

            lateDeliverableParams.put("LATE_DELIVERABLE_TYPE", lateDeliverableDetails.getLateDeliverableTypeName());

            lateDeliverableParams.put("PROJECT_NAME", lateDeliverableDetails.getProjectName());
            lateDeliverableParams.put("PROJECT_VERSION", lateDeliverableDetails.getProjectVersion());
            lateDeliverableParams.put("PROJECT_ID", Long.toString(lateDeliverableDetails.getProjectId()));
            lateDeliverableParams.put("PHASE_NAME", lateDeliverableDetails.getPhaseName());
            lateDeliverableParams.put("DELIVERABLE_NAME", lateDeliverableDetails.getDeliverableName());
            lateDeliverableParams.put("DEADLINE", formatTimestamp(deadline));
            lateDeliverableParams.put("LATE_MEMBER_HANDLE", lateDeliverableDetails.getLateMemberHandle());

            Long delay = lateDeliverableDetails.getDelay();
            lateDeliverableParams.put("DELAY",
                Helper.delayToString((delay == null) ? null : (delay * Helper.THOUSAND)));
            lateDeliverableParams.put("COMPENSATED_DEADLINE",
                compensatedDeadline == null ? formatTimestamp(deadline) : timestampFormat.format(compensatedDeadline));
            lateDeliverableParams.put("COMPENSATED_AND_REAL_DEADLINES_DIFFER",
                Boolean.toString(compensatedDeadline != null));

            // Add map with late deliverable parameters to the list:
            lateDeliverablesParams.add(lateDeliverableParams);
        }

        // Put the list to the parameters map:
        params.put("RECORDS", lateDeliverablesParams);

        // Send warning email to the user:
        emailSendingUtility.sendEmail(emailSubjectTemplateText, emailBodyTemplatePath, email, params);
    }

    /**
     * Formats the timestamp.
     *
     * @param timestamp
     *            the timestamp.
     *
     * @return the string representing the timestamp or "N/A" if the timestamp is <code>null</code>.
     *
     * @since 1.3
     */
    private String formatTimestamp(Date timestamp) {
        return timestamp == null ? "N/A" : timestampFormat.format(timestamp);
    }

    /**
     * <p>
     * Gets the mapping from user ID to the list of late deliverables to be responded by this user.
     * </p>
     *
     * @return the mapping from user ID to the list of late deliverables to be responded by this user.
     *
     * @throws ResourcePersistenceException
     *             if a resource persistence error occurs.
     * @throws SearchBuilderException
     *             if a search builder error occurs.
     * @throws LateDeliverableManagementException
     *             if a late deliverable management error occurs.
     * @throws PersistenceException
     *             if a project persistence error occurs.
     * @throws PhasePersistenceException
     *             if a phase persistence error occurs.
     * @throws DeliverablePersistenceException
     *             if a deliverable persistence error occurs.
     * @throws NotRespondedLateDeliverablesNotificationException
     *             if any other error occurs.
     */
    private Map<Long, List<LateDeliverableDetails>> getLateDeliverableDetailsByUser()
        throws ResourcePersistenceException, SearchBuilderException,
        LateDeliverableManagementException, PersistenceException, PhasePersistenceException,
        DeliverablePersistenceException, NotRespondedLateDeliverablesNotificationException {
        // Combine filter for matching all explained late deliverables
        // and filter for matching all not responded late deliverables with AND operator:
        Filter filter = new AndFilter(LateDeliverableFilterBuilder.createHasExplanationFilter(true),
            LateDeliverableFilterBuilder.createHasResponseFilter(false));

        // Search all explained, but not responded late deliverables:
        List<com.topcoder.management.deliverable.late.LateDeliverable> lateDeliverables =
            lateDeliverableManager.searchAllLateDeliverables(filter);

        // Create a mapping from user ID to the list of late deliverables to be responded by this user:
        Map<Long, List<LateDeliverableDetails>> lateDeliverableDetailsByUser =
            new HashMap<Long, List<LateDeliverableDetails>>();

        List<Long> lateDeliverableIds = new ArrayList<Long>();

        Filter resourceRoleIdFilter = getRoleIdFilter();
        for (com.topcoder.management.deliverable.late.LateDeliverable lateDeliverable : lateDeliverables) {
            // Get project ID:
            long projectId = lateDeliverable.getProjectId();

            // Get manager user IDs:
            Set<Long> managerUserIds = getManagerUserIds(resourceRoleIdFilter, projectId);
            if (managerUserIds.isEmpty()) {
                continue;
            }

            // Get project by ID:
            Project project = projectManager.getProject(projectId);
            // Get phase by ID:
            Phase phase = phasePersistence.getPhase(lateDeliverable.getProjectPhaseId());
            // Get phase name:
            String phaseName = phase.getPhaseType().getName();

            String lateMemberHandle = null;
            Resource lateResource = resourceManager.getResource(lateDeliverable.getResourceId());

            if (lateResource != null) {
                try {
                    lateMemberHandle = (String) lateResource.getProperty("Handle");
                } catch (ClassCastException e) {
                    throw new NotRespondedLateDeliverablesNotificationException("Handle property value"
                        + " is not type of String.", e);
                }
            }

            lateDeliverableIds.add(lateDeliverable.getId());

            // Get deliverable:
            Deliverable[] deliverables = deliverablePersistence.loadDeliverables(
                lateDeliverable.getDeliverableId(),
                lateDeliverable.getResourceId(),
                lateDeliverable.getProjectPhaseId());
            if (deliverables.length == 0) {
                // Log the error but still continue.
                Helper.logError(log, "There is no matching deliverable in the persistence for resourceID " + lateDeliverable.getResourceId());
            }

            // Create late deliverable details instance:
            LateDeliverableDetails lateDeliverableDetails = getLateDeliverableDetails(lateDeliverable,
                deliverables.length>0 ? deliverables[0] : null, project, projectId, phaseName, lateMemberHandle);

            for (Long managerUserId : managerUserIds) {
                // Get list of late deliverable details for this user:
                List<LateDeliverableDetails> lateDeliverableDetailsList =
                    lateDeliverableDetailsByUser.get(managerUserId);
                if (lateDeliverableDetailsList == null) {
                    // Create a new list for late deliverable details of this user:
                    lateDeliverableDetailsList = new ArrayList<LateDeliverableDetails>();
                    // Add list to the map:
                    lateDeliverableDetailsByUser.put(managerUserId, lateDeliverableDetailsList);
                }
                // Add late deliverable details instance to the list:
                lateDeliverableDetailsList.add(lateDeliverableDetails);
            }
        }

        // Log information
        Helper.logInfo(log, "Late deliverables number: " + lateDeliverableIds.size());
        Helper.logInfo(log, "Late deliverables ids: " + lateDeliverableIds);

        return lateDeliverableDetailsByUser;
    }

    /**
     * <p>
     * Creates a LateDeliverableDetails instance with given values.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Set name of the late deliverable type to the late deliverable details.</li>
     * </ol>
     * </p>
     *
     * @param lateDeliverable
     *            the late deliverable.
     * @param deliverable
     *            the deliverable.
     * @param project
     *            the project.
     * @param projectId
     *            the project id.
     * @param phaseName
     *            the project name.
     * @param lateMemberHandle
     *            the late member handle.
     *
     * @return the LateDeliverableDetails instance.
     */
    private LateDeliverableDetails getLateDeliverableDetails(
        com.topcoder.management.deliverable.late.LateDeliverable lateDeliverable, Deliverable deliverable,
        Project project, long projectId, String phaseName, String lateMemberHandle) {
        // Create late deliverable details instance:
        LateDeliverableDetails lateDeliverableDetails = new LateDeliverableDetails();

        // Set late deliverable ID to the late deliverable details instance:
        lateDeliverableDetails.setLateDeliverableId(lateDeliverable.getId());
        // Set project name to the late deliverable details instance:
        lateDeliverableDetails.setProjectName((String) project.getProperty("Project Name"));
        // Set project version to the late deliverable details instance:
        lateDeliverableDetails.setProjectVersion((String) project.getProperty("Project Version"));
        // Set project ID to the late deliverable details instance:
        lateDeliverableDetails.setProjectId(projectId);
        // Set phase name to the late deliverable details instance:
        lateDeliverableDetails.setPhaseName(phaseName);
        // Set deliverable name to the late deliverable details instance:
        lateDeliverableDetails.setDeliverableName(deliverable!=null ? deliverable.getName() : "N/A");
        // Set deadline to the late deliverable details instance:
        lateDeliverableDetails.setDeadline(lateDeliverable.getDeadline());
        // Set delay to the late deliverable details instance:
        lateDeliverableDetails.setDelay(lateDeliverable.getDelay());
        // Set compensated deadline to the late deliverable details instance:
        lateDeliverableDetails.setCompensatedDeadline(lateDeliverable.getCompensatedDeadline());
        // Set late member handle to the late deliverable details instance:
        lateDeliverableDetails.setLateMemberHandle(lateMemberHandle);

        // Set name of the late deliverable type to the late deliverable details instance:
        lateDeliverableDetails.setLateDeliverableTypeName(lateDeliverable.getType().getName());

        return lateDeliverableDetails;
    }

    /**
     * <p>
     * Gets the manager user IDs.
     * </p>
     *
     * @param resourceRoleIdFilter
     *            the resource role id filter.
     * @param projectId
     *            the project id.
     *
     * @return the manager user IDs.
     *
     * @throws ResourcePersistenceException
     *             if a resource persistence error occurs.
     * @throws SearchBuilderException
     *             if a search builder error occurs.
     * @throws NotRespondedLateDeliverablesNotificationException
     *             if any other error occurs.
     */
    private Set<Long> getManagerUserIds(Filter resourceRoleIdFilter, long projectId)
        throws ResourcePersistenceException, SearchBuilderException,
        NotRespondedLateDeliverablesNotificationException {
        // Create a set for manager user IDs:
        Set<Long> managerUserIds = new HashSet<Long>();

        // Create a filter for matching manager resources for the project:
        Filter projectManagerFilter = new AndFilter(ResourceFilterBuilder.createProjectIdFilter(projectId),
            resourceRoleIdFilter);
        // Get manager resources for the project:
        Resource[] resources = resourceManager.searchResources(projectManagerFilter);

        for (Resource resource : resources) {
            managerUserIds.add(resource.getUserId());
        }

        return managerUserIds;
    }

    /**
     * <p>
     * Creates a filter for filtering resource role ids.
     * </p>
     *
     * @return the created filter.
     */
    private Filter getRoleIdFilter() {
        if (managerResourceRoleIds.size() == 1) {
            // Create filter for filtering resource roles by the only ID:
            return ResourceFilterBuilder.createResourceRoleIdFilter(
                managerResourceRoleIds.iterator().next());
        }

        // Create a list for individual resource role ID filters:
        List<Filter> individualResourceRoleIdFilters = new ArrayList<Filter>();
        for (Long managerResourceRoleId : managerResourceRoleIds) {
            // Create filter for the next resource role ID and add to the list:
            individualResourceRoleIdFilters.add(
                ResourceFilterBuilder.createResourceRoleIdFilter(managerResourceRoleId));
        }
        // Create filter matching resources with manager roles:
        return new OrFilter(individualResourceRoleIdFilters);
    }

    /**
     * <p>
     * Gets the values of 'managerResourceRoleIds'.
     * </p>
     *
     * @param config
     *            the configuration object.
     *
     * @return the values of 'managerResourceRoleIds'.
     *
     * @throws LateDeliverablesTrackerConfigurationException
     *             if any error occurs.
     */
    private static Set<Long> getRoleIds(ConfigurationObject config) {
        try {
            if (!config.containsProperty(KEY_MANAGER_ROLE_IDS)) {
                throw new LateDeliverablesTrackerConfigurationException(
                    "The property 'managerResourceRoleIds' is required.");
            }

            Object[] valuesObj = config.getPropertyValues(KEY_MANAGER_ROLE_IDS);
            int valuesSize = valuesObj.length;

            if (valuesSize == 0) {
                // The value is empty
                throw new LateDeliverablesTrackerConfigurationException(
                    "The property 'managerResourceRoleIds' can not be empty.");
            }

            Set<Long> values = new HashSet<Long>(valuesSize);

            for (int i = 0; i < valuesSize; i++) {
                Object valueObj = valuesObj[i];
                if (!(valueObj instanceof String)) {
                    throw new LateDeliverablesTrackerConfigurationException(
                        "The property 'managerResourceRoleIds' should be a String[].");
                }

                String valueStr = (String) valueObj;

                try {
                    // Parse as a long
                    long value = Long.parseLong(valueStr);

                    if (value <= 0) {
                        throw new LateDeliverablesTrackerConfigurationException(
                            "The property 'managerResourceRoleIds' should contain positive long only.");
                    }

                    values.add(value);
                } catch (NumberFormatException e) {
                    throw new LateDeliverablesTrackerConfigurationException(
                        "The property 'managerResourceRoleIds' should contain positive long only.", e);
                }
            }

            return values;
        } catch (ConfigurationAccessException e) {
            throw new LateDeliverablesTrackerConfigurationException(
                "An error occurred while accessing the configuration.", e);
        }

    }

    /**
     * <p>
     * This is an inner class of NotRespondedLateDeliverablesNotifier. It is a container for information about a
     * single explained but not responded late deliverable. It is a simple JavaBean (POJO) that provides getters and
     * setters for all private attributes and performs no argument validation in the setters.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Added lateDeliverableTypeName property.</li>
     * </ol>
     * </p>
     *
     * <p>
     * Thread Safety: This class is mutable and not thread safe.
     * </p>
     *
     * @author saarixx, sparemax, TCSDEVELOPER
     * @version 1.3
     * @since 1.2
     */
    private class LateDeliverableDetails {
        /**
         * <p>
         * The ID of the late deliverable.
         * </p>
         *
         * <p>
         * Can be any value. Has getter and setter.
         * </p>
         */
        private long lateDeliverableId;

        /**
         * <p>
         * The project name.
         * </p>
         *
         * <p>
         * Can be any value. Has getter and setter.
         * </p>
         */
        private String projectName;

        /**
         * <p>
         * The project version. Can be any value. Has getter and setter.
         * </p>
         */
        private String projectVersion;

        /**
         * <p>
         * The ID of the project.
         * </p>
         *
         * <p>
         * Can be any value. Has getter and setter.
         * </p>
         */
        private long projectId;

        /**
         * <p>
         * The phase name.
         * </p>
         *
         * <p>
         * Can be any value. Has getter and setter.
         * </p>
         */
        private String phaseName;

        /**
         * <p>
         * The deliverable name.
         * </p>
         *
         * <p>
         * Can be any value. Has getter and setter.
         * </p>
         */
        private String deliverableName;

        /**
         * <p>
         * The deadline.
         * </p>
         *
         * <p>
         * Can be any value. Has getter and setter.
         * </p>
         */
        private Date deadline;

        /**
         * <p>
         * The delay in seconds.
         * </p>
         *
         * <p>
         * Is null if not available. Can be any value. Has getter and setter.
         * </p>
         */
        private Long delay;

        /**
         * <p>
         * The compensated deadline.
         * </p>
         *
         * <p>
         * Is null if deadline was not compensated. Can be any value. Has getter and setter.
         * </p>
         */
        private Date compensatedDeadline;

        /**
         * <p>
         * The handle of the late member.
         * </p>
         *
         * <p>
         * Can be any value. Has getter and setter.
         * </p>
         */
        private String lateMemberHandle;

        /**
         * <p>
         * The name of the late deliverable type.
         * </p>
         *
         * <p>
         * Can be any value. Has getter and setter.
         * </p>
         *
         * @since 1.3
         */
        private String lateDeliverableTypeName;

        /**
         * <p>
         * Creates an instance of LateDeliverableDetails.
         * </p>
         */
        public LateDeliverableDetails() {
            // Empty
        }

        /**
         * <p>
         * Gets the ID of the late deliverable.
         * </p>
         *
         * @return the ID of the late deliverable.
         */
        public long getLateDeliverableId() {
            return lateDeliverableId;
        }

        /**
         * <p>
         * Sets the ID of the late deliverable.
         * </p>
         *
         * @param lateDeliverableId
         *            the ID of the late deliverable.
         */
        public void setLateDeliverableId(long lateDeliverableId) {
            this.lateDeliverableId = lateDeliverableId;
        }

        /**
         * <p>
         * Gets the project name.
         * </p>
         *
         * @return the project name.
         */
        public String getProjectName() {
            return projectName;
        }

        /**
         * <p>
         * Sets the project name.
         * </p>
         *
         * @param projectName
         *            the project name.
         */
        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        /**
         * <p>
         * Gets the project version.
         * </p>
         *
         * @return the project version.
         */
        public String getProjectVersion() {
            return projectVersion;
        }

        /**
         * <p>
         * Sets the project version.
         * </p>
         *
         * @param projectVersion
         *            the project version.
         */
        public void setProjectVersion(String projectVersion) {
            this.projectVersion = projectVersion;
        }

        /**
         * <p>
         * Gets the ID of the project.
         * </p>
         *
         * @return the ID of the project.
         */
        public long getProjectId() {
            return projectId;
        }

        /**
         * <p>
         * Sets the ID of the project.
         * </p>
         *
         * @param projectId
         *            the ID of the project.
         */
        public void setProjectId(long projectId) {
            this.projectId = projectId;
        }

        /**
         * <p>
         * Gets the phase name.
         * </p>
         *
         * @return the phase name.
         */
        public String getPhaseName() {
            return phaseName;
        }

        /**
         * <p>
         * Sets the phase name.
         * </p>
         *
         * @param phaseName
         *            the phase name.
         */
        public void setPhaseName(String phaseName) {
            this.phaseName = phaseName;
        }

        /**
         * <p>
         * Gets the deliverable name.
         * </p>
         *
         * @return the deliverable name.
         */
        public String getDeliverableName() {
            return deliverableName;
        }

        /**
         * <p>
         * Sets the deliverable name.
         * </p>
         *
         * @param deliverableName
         *            the deliverable name.
         */
        public void setDeliverableName(String deliverableName) {
            this.deliverableName = deliverableName;
        }

        /**
         * <p>
         * Gets the deadline.
         * </p>
         *
         * @return the deadline.
         */
        public Date getDeadline() {
            return deadline;
        }

        /**
         * <p>
         * Sets the deadline.
         * </p>
         *
         * @param deadline
         *            the deadline.
         */
        public void setDeadline(Date deadline) {
            this.deadline = deadline;
        }

        /**
         * <p>
         * Gets the delay in seconds.
         * </p>
         *
         * @return the delay in seconds.
         */
        public Long getDelay() {
            return delay;
        }

        /**
         * <p>
         * Sets the delay in seconds.
         * </p>
         *
         * @param delay
         *            the delay in seconds.
         */
        public void setDelay(Long delay) {
            this.delay = delay;
        }

        /**
         * <p>
         * Gets the compensated deadline.
         * </p>
         *
         * @return the compensated deadline.
         */
        public Date getCompensatedDeadline() {
            return compensatedDeadline;
        }

        /**
         * <p>
         * Sets the compensated deadline.
         * </p>
         *
         * @param compensatedDeadline
         *            the compensated deadline.
         */
        public void setCompensatedDeadline(Date compensatedDeadline) {
            this.compensatedDeadline = compensatedDeadline;
        }

        /**
         * <p>
         * Gets the handle of the late member.
         * </p>
         *
         * @return the handle of the late member.
         */
        public String getLateMemberHandle() {
            return lateMemberHandle;
        }

        /**
         * <p>
         * Sets the handle of the late member.
         * </p>
         *
         * @param lateMemberHandle
         *            the handle of the late member.
         */
        public void setLateMemberHandle(String lateMemberHandle) {
            this.lateMemberHandle = lateMemberHandle;
        }

        /**
         * <p>
         * Retrieves the name of the late deliverable type.
         * </p>
         *
         * @return the name of the late deliverable type.
         *
         * @since 1.3
         */
        public String getLateDeliverableTypeName() {
            return lateDeliverableTypeName;
        }

        /**
         * <p>
         * Sets the name of the late deliverable type.
         * </p>
         *
         * @param lateDeliverableTypeName
         *            the name of the late deliverable type.
         *
         * @since 1.3
         */
        public void setLateDeliverableTypeName(String lateDeliverableTypeName) {
            this.lateDeliverableTypeName = lateDeliverableTypeName;
        }
    }
}
