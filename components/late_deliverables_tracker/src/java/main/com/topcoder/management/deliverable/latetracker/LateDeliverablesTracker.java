/*
 * Copyright (C) 2010-2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.errorhandling.ExceptionUtils;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.objectfactory.ObjectFactory;

/**
 * <p>
 * This class provides a programmatic API for tracking late deliverables of the configured types. It provides just a
 * single {@link #execute()} method that uses pluggable <code>LateDeliverablesRetriever</code> and
 * <code>LateDeliverableProcessor</code> instances to find and process all late deliverables of the configured
 * types. This class performs the logging of errors and debug information using Logging Wrapper component.
 * </p>
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Log unexpected exceptions in execute().</li>
 * </ol>
 * </p>
 *
 * <p>
 * <em>Changes in version 1.3:</em>
 * <ol>
 * <li>Added support of configurable list of late deliverable types to be tracked.</li>
 * </ol>
 * </p>
 *
 * <p>
 * Sample usage:
 *
 * <pre>
 * // Prepare configuration for LateDeliverablesRetriever
 * ConfigurationObject lateDeliverablesRetrieverConfig = getConfigurationObject(
 *     &quot;config/LateDeliverablesRetrieverImpl.xml&quot;, LateDeliverablesRetrieverImpl.class.getName());
 *
 * // Prepare configuration for LateDeliverableProcessor
 * ConfigurationObject lateDeliverableProcessorConfig = getConfigurationObject(
 *     &quot;config/LateDeliverableProcessorImpl.xml&quot;, LateDeliverableProcessorImpl.class.getName());
 *
 * // Create an instance of LateDeliverablesRetrieverImpl and configure it
 * LateDeliverablesRetriever lateDeliverablesRetriever = new LateDeliverablesRetrieverImpl();
 * lateDeliverablesRetriever.configure(lateDeliverablesRetrieverConfig);
 *
 * // Create an instance of LateDeliverableProcessorImpl and configure it
 * LateDeliverableProcessor lateDeliverableProcessor = new LateDeliverableProcessorImpl();
 * lateDeliverableProcessor.configure(lateDeliverableProcessorConfig);
 *
 * // Get logger
 * Log log = LogFactory.getLog(&quot;my_logger&quot;);
 *
 * // Create LateDeliverablesTracker
 * Set&lt;LateDeliverableType&gt; trackedLateDeliverableTypes = EnumSet.allOf(LateDeliverableType.class);
 * LateDeliverablesTracker lateDeliverablesTracker = new LateDeliverablesTracker(lateDeliverablesRetriever,
 *     lateDeliverableProcessor, trackedLateDeliverableTypes, log);
 * // Track for late deliverables
 * lateDeliverablesTracker.execute();
 * </pre>
 *
 * Sample config:
 *
 * <pre>
 * &lt;?xml version=&quot;1.0&quot;?&gt;
 * &lt;CMConfig&gt;
 *     &lt;Config name=&quot;com.topcoder.management.deliverable.latetracker.LateDeliverablesTracker&quot;&gt;
 *      &lt;Property name=&quot;loggerName&quot;&gt;
 *          &lt;Value&gt;myLogger&lt;/Value&gt;
 *      &lt;/Property&gt;
 *
 *      &lt;Property name=&quot;objectFactoryConfig&quot;&gt;
 *          &lt;property name=&quot;lateDeliverablesRetriever&quot;&gt;
 *              &lt;property name=&quot;type&quot;&gt;
 *   &lt;value&gt;com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl&lt;/value&gt;
 *              &lt;/property&gt;
 *          &lt;/property&gt;
 *          &lt;property name=&quot;lateDeliverableProcessor&quot;&gt;
 *              &lt;property name=&quot;type&quot;&gt;
 *   &lt;value&gt;com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl&lt;/value&gt;
 *              &lt;/property&gt;
 *          &lt;/property&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;lateDeliverableProcessorKey&quot;&gt;
 *          &lt;Value&gt;lateDeliverableProcessor&lt;/Value&gt;
 *      &lt;/Property&gt;
 *
 *      &lt;Property name=&quot;lateDeliverablesRetrieverKey&quot;&gt;
 *          &lt;Value&gt;lateDeliverablesRetriever&lt;/Value&gt;
 *      &lt;/Property&gt;
 *
 *      &lt;Property name=&quot;lateDeliverablesRetrieverConfig&quot;&gt;
 *      &lt;Property name=&quot;loggerName&quot;&gt;
 *          &lt;Value&gt;myLogger&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;resourceManagerKey&quot;&gt;
 *           &lt;Value&gt;resourceManager&lt;/Value&gt;
 *         &lt;/Property&gt;
 *      &lt;Property name=&quot;missedDeadlineTrackingDeliverableIds&quot;&gt;
 *          &lt;Value&gt;3,4&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;objectFactoryConfig&quot;&gt;
 *        &lt;property name=&quot;resourceManager&quot;&gt;
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
 *           &lt;/property&gt;
 *           &lt;property name=&quot;ResourcePersistence&quot;&gt;
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
 *           &lt;/property&gt;
 *           &lt;property name=&quot;SearchBundleManager&quot;&gt;
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
 *           &lt;/property&gt;
 *          &lt;property name=&quot;projectManager&quot;&gt;
 *              &lt;property name=&quot;type&quot;&gt;
 *                  &lt;value&gt;com.topcoder.management.project.ProjectManagerImpl&lt;/value&gt;
 *              &lt;/property&gt;
 *          &lt;/property&gt;
 *          &lt;property name=&quot;phaseManager&quot;&gt;
 *              &lt;property name=&quot;type&quot;&gt;
 *                  &lt;value&gt;com.topcoder.management.phase.DefaultPhaseManager&lt;/value&gt;
 *              &lt;/property&gt;
 *              &lt;Property name=&quot;params&quot;&gt;
 *                  &lt;Property name=&quot;param1&quot;&gt;
 *                      &lt;Property name=&quot;type&quot;&gt;
 *                          &lt;Value&gt;String&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                      &lt;Property name=&quot;value&quot;&gt;
 *                          &lt;Value&gt;com.topcoder.management.phase.DefaultPhaseManager&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                  &lt;/Property&gt;
 *              &lt;/Property&gt;
 *          &lt;/property&gt;
 *          &lt;property name=&quot;deliverablePersistence&quot;&gt;
 *              &lt;property name=&quot;type&quot;&gt;
 *              &lt;value&gt;com.topcoder.management.deliverable.persistence.sql.SqlDeliverablePersistence&lt;/value&gt;
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
 *          &lt;property name=&quot;screeningDeliverableChecker&quot;&gt;
 *              &lt;property name=&quot;type&quot;&gt;
 *                  &lt;value&gt;com.topcoder.management.deliverable.latetracker.MockDeliverableChecker&lt;/value&gt;
 *              &lt;/property&gt;
 *          &lt;/property&gt;
 *          &lt;property name=&quot;reviewDeliverableChecker&quot;&gt;
 *              &lt;property name=&quot;type&quot;&gt;
 *                  &lt;value&gt;com.topcoder.management.deliverable.latetracker.MockDeliverableChecker&lt;/value&gt;
 *              &lt;/property&gt;
 *          &lt;/property&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;projectManagerKey&quot;&gt;
 *          &lt;Value&gt;projectManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;phaseManagerKey&quot;&gt;
 *          &lt;Value&gt;phaseManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;deliverablePersistenceKey&quot;&gt;
 *          &lt;Value&gt;deliverablePersistence&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;deliverableChecker1&quot;&gt;
 *          &lt;Property name=&quot;deliverableName&quot;&gt;
 *              &lt;Value&gt;Screening Scorecard&lt;/Value&gt;
 *          &lt;/Property&gt;
 *          &lt;Property name=&quot;deliverableCheckerKey&quot;&gt;
 *              &lt;Value&gt;screeningDeliverableChecker&lt;/Value&gt;
 *          &lt;/Property&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;deliverableChecker2&quot;&gt;
 *          &lt;Property name=&quot;deliverableName&quot;&gt;
 *              &lt;Value&gt;Review Scorecard&lt;/Value&gt;
 *          &lt;/Property&gt;
 *          &lt;Property name=&quot;deliverableCheckerKey&quot;&gt;
 *              &lt;Value&gt;reviewDeliverableChecker&lt;/Value&gt;
 *          &lt;/Property&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;searchBundleManagerNamespace&quot;&gt;
 *          &lt;Value&gt;com.topcoder.search.builder.SearchBundleManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *     &lt;/Property&gt;
 *
 *     &lt;Property name=&quot;lateDeliverableProcessorConfig&quot;&gt;
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
 *                        &lt;Value&gt;jdbc:informix-sqli://localhost:1526/test:informixserver=ol_topcoder&lt;/Value&gt;
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
 *           &lt;Value&gt;
 *             WARNING\: You need to explain why your Final Fix for %PROJECT_NAME% was rejected
 *           &lt;/Value&gt;
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
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;lateDeliverableTypes&quot;&gt;
 *       &lt;Value&gt;Missed Deadline&lt;/Value&gt;
 *       &lt;Value&gt;Rejected Final Fix&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;/Config&gt;
 * &lt;/CMConfig&gt;
 * </pre>
 *
 * </p>
 * <p>
 * Thread Safety: This class is immutable, but not thread safe since it uses <code>LateDeliverablesRetriever</code>
 * and <code>LateDeliverableProcessor</code> instances that are not guaranteed to be thread safe.
 * </p>
 *
 * @author saarixx, myxgyy, sparemax
 * @version 1.3.2
 */
public class LateDeliverablesTracker {
    /**
     * <p>
     * Represents the name of this class used for logging.
     * </p>
     */
    private static final String CLASS_NAME = LateDeliverablesTracker.class.getName();

    /**
     * <p>
     * Represents &quot;lateDeliverablesRetrieverKey&quot; property key in configuration.
     * </p>
     */
    private static final String RETRIEVER_KEY = "lateDeliverablesRetrieverKey";

    /**
     * <p>
     * Represents &quot;lateDeliverableProcessorKey&quot; property key in configuration.
     * </p>
     */
    private static final String PROCESSOR_KEY = "lateDeliverableProcessorKey";

    /**
     * <p>
     * Represents &quot;lateDeliverableTypes&quot; property key in configuration.
     * </p>
     *
     * @since 1.3
     */
    private static final String TYPES_KEY = "lateDeliverableTypes";

    /**
     * <p>
     * Represents &quot;lateDeliverablesRetrieverConfig&quot; child configuration name in configuration.
     * </p>
     */
    private static final String RETRIEVER_CONFIG = "lateDeliverablesRetrieverConfig";

    /**
     * <p>
     * Represents &quot;lateDeliverableProcessorConfig&quot; child configuration name in configuration.
     * </p>
     */
    private static final String PROCESSOR_CONFIG = "lateDeliverableProcessorConfig";

    /**
     * <p>
     * The late deliverables retriever to be used by this class.
     * </p>
     * <p>
     * Is initialized in the constructor and never changed after that.
     * </p>
     * <p>
     * Cannot be null.
     * </p>
     * <p>
     * Is used in {@link #execute()}.
     * </p>
     */
    private final LateDeliverablesRetriever lateDeliverablesRetriever;

    /**
     * <p>
     * The late deliverable processor to be used by this class.
     * </p>
     * <p>
     * Is initialized in the constructor and never changed after that.
     * </p>
     * <p>
     * Cannot be null.
     * </p>
     * <p>
     * Is used in {@link #execute()}.
     * </p>
     */
    private final LateDeliverableProcessor lateDeliverableProcessor;

    /**
     * <p>
     * The types of late deliverables to be tracked.
     * </p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Is null if all supported late deliverable types
     * must be tracked. Cannot be empty, cannot contain null. Is used in execute().
     * </p>
     *
     * @since 1.3
     */
    private final Set<LateDeliverableType> lateDeliverableTypes;

    /**
     * <p>
     * The logger used by this class for logging errors and debug information.
     * </p>
     * <p>
     * Is initialized in the constructor and never changed after that.
     * </p>
     * <p>
     * If is null, logging is not performed.
     * </p>
     * <p>
     * Is used in {@link #execute()}.
     * </p>
     */
    private final Log log;

    /**
     * Creates an instance of <code>LateDeliverablesTracker</code> with the given parameters.
     *
     * <p>
     * <em>NOTE: </em> Instances of LateDeliverablesRetriever and LateDeliverableProcessor passed to this constructor
     * must be properly configured by the caller using configure() methods.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Added lateDeliverableTypes parameter.</li>
     * </ol>
     * </p>
     *
     * @param retriever
     *            the late deliverables retriever to be used by this class.
     * @param processor
     *            the late deliverable processor to be used by this class.
     * @param lateDeliverableTypes
     *            the types of late deliverables to be tracked (null if all supported late deliverable types must be
     *            tracked)
     * @param log
     *            the logger used by this class for logging errors and debug information.
     * @throws IllegalArgumentException
     *             if <code>retriever</code> or <code>processor</code> is <code>null</code>,
     *             lateDeliverableTypes is empty or contains null.
     */
    public LateDeliverablesTracker(LateDeliverablesRetriever retriever,
        LateDeliverableProcessor processor,
        Set<LateDeliverableType> lateDeliverableTypes, Log log) {
        ExceptionUtils.checkNull(retriever, null, null, "The parameter 'retriever' should not be null.");
        ExceptionUtils.checkNull(processor, null, null, "The parameter 'processor' should not be null.");
        Helper.checkLateDeliverableTypes(lateDeliverableTypes, "lateDeliverableTypes", null, null);

        this.lateDeliverablesRetriever = retriever;
        this.lateDeliverableProcessor = processor;
        this.lateDeliverableTypes =
            (lateDeliverableTypes == null) ? null : new HashSet<LateDeliverableType>(lateDeliverableTypes);
        this.log = log;
    }

    /**
     * Creates an instance of <code>LateDeliverablesTracker</code>.
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Added steps for obtaining lateDeliverableTypes from the configuration.</li>
     * </ol>
     * </p>
     *
     * @param config
     *            the configuration object.
     * @throws IllegalArgumentException
     *             if <code>config</code> is <code>null</code>.
     * @throws LateDeliverablesTrackerConfigurationException
     *             if some error occurred when initializing an instance using the given
     *             configuration.
     */
    public LateDeliverablesTracker(ConfigurationObject config) {
        ExceptionUtils.checkNull(config, null, null, "The parameter 'config' should not be null.");

        // create log
        String loggerName = Helper.getPropertyValue(config, Helper.LOGGER_NAME_KEY, false, false);
        this.log = (loggerName == null) ? null : LogManager.getLog(loggerName);

        // create object factory
        ObjectFactory objectFactory = Helper.createObjectFactory(config);

        // create the lateDeliverablesRetriever via object factory and configure it
        lateDeliverablesRetriever = Helper.createObject(config, objectFactory, RETRIEVER_KEY,
            LateDeliverablesRetriever.class);

        ConfigurationObject lateDeliverablesRetrieverConfig = Helper.getChildConfig(config,
            RETRIEVER_CONFIG);
        lateDeliverablesRetriever.configure(lateDeliverablesRetrieverConfig);

        // create the lateDeliverableProcessor via object factory and configure it
        lateDeliverableProcessor = Helper.createObject(config, objectFactory, PROCESSOR_KEY,
            LateDeliverableProcessor.class);

        ConfigurationObject lateDeliverableProcessorConfig = Helper.getChildConfig(config,
            PROCESSOR_CONFIG);
        lateDeliverableProcessor.configure(lateDeliverableProcessorConfig);

        try {
            // Get late deliverable types property values from the configuration:
            Object[] lateDeliverableTypeValues = config.getPropertyValues(TYPES_KEY);
            if (lateDeliverableTypeValues != null) {
                if (lateDeliverableTypeValues.length == 0) {
                    throw new LateDeliverablesTrackerConfigurationException(
                        "The [lateDeliverableTypes] property value in configuration object [" + config.getName()
                            + "] should not be empty.");
                }

                // Create a set for tracked late deliverable types:
                lateDeliverableTypes = new HashSet<LateDeliverableType>();
                for (Object lateDeliverableTypeValue : lateDeliverableTypeValues) {
                    if (!(lateDeliverableTypeValue instanceof String)) {
                        throw new LateDeliverablesTrackerConfigurationException(
                            "The [lateDeliverableTypes] property value in configuration object [" + config.getName()
                                + "] should be an instance of String[].");
                    }

                    // Get LateDeliverableType from string:
                    LateDeliverableType lateDeliverableType = LateDeliverableType
                        .fromString((String) lateDeliverableTypeValue);
                    if (lateDeliverableType == null) {
                        throw new LateDeliverablesTrackerConfigurationException(
                            "There is no late deliverable type for the value [" + lateDeliverableTypeValue + "].");
                    }
                    // Add late deliverable type to the set:
                    lateDeliverableTypes.add(lateDeliverableType);
                }
                if (lateDeliverableTypes.size() != lateDeliverableTypeValues.length) {
                    throw new LateDeliverablesTrackerConfigurationException(
                        "The [lateDeliverableTypes] property value in configuration object [" + config.getName()
                            + "] should not contain duplicate elements.");
                }
            } else {
                lateDeliverableTypes = null;
            }
        } catch (ConfigurationAccessException e) {
            throw new LateDeliverablesTrackerConfigurationException("Fails on accessing configuration object.", e);
        }
    }

    /**
     * <p>
     * Executes a single deliverable tracking operation. Retrieves all late deliverables and processes them one by
     * one.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Log unexpected exceptions.</li>
     * </ol>
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Added lateDeliverableTypes parameter.</li>
     * </ol>
     * </p>
     *
     * @throws LateDeliverablesRetrievalException
     *             if some error occurred when retrieving a list of late deliverables.
     * @throws LateDeliverablesProcessingException
     *             if some error occurred when processing a late deliverable.
     */
    public void execute() throws LateDeliverablesProcessingException, LateDeliverablesRetrievalException {
        long start = System.currentTimeMillis();
        final String method = CLASS_NAME + ".execute()";
        Helper.logEntrance(log, method, null, null);
        Helper.logInfo(log, "Start tracking at : " + new Date());

        try {
            List<LateDeliverable> lateDeliverables = lateDeliverablesRetriever.retrieve(lateDeliverableTypes);

            for (LateDeliverable lateDeliverable : lateDeliverables) {
                lateDeliverableProcessor.processLateDeliverable(lateDeliverable);
            }
        } catch (LateDeliverablesProcessingException e) {
            throw Helper.logException(log, method, e);
        } catch (LateDeliverablesRetrievalException e) {
            throw Helper.logException(log, method, e);
        } catch (Exception e) {
            // Log exception (ignored)
            Helper.logException(log, method, e);
        }

        Helper.logExit(log, method, null, start);
    }
}