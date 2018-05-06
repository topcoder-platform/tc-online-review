/*
 * Copyright (C) 2010-2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.retrievers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.DeliverableChecker;
import com.topcoder.management.deliverable.DeliverableManager;
import com.topcoder.management.deliverable.PersistenceDeliverableManager;
import com.topcoder.management.deliverable.latetracker.Helper;
import com.topcoder.management.deliverable.latetracker.LateDeliverable;
import com.topcoder.management.deliverable.latetracker.LateDeliverableType;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesRetrievalException;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesRetriever;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackerConfigurationException;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;
import com.topcoder.management.deliverable.persistence.DeliverablePersistence;
import com.topcoder.management.deliverable.persistence.DeliverablePersistenceException;
import com.topcoder.management.deliverable.search.DeliverableFilterBuilder;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectFilterUtility;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.util.errorhandling.ExceptionUtils;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.objectfactory.ObjectFactory;

/**
 * <p>
 * This class is an implementation of <code>LateDeliverablesRetriever</code> that uses pluggable
 * <code>ProjectManager</code>, <code>PhaseManager</code> and <code>DeliverableManager</code> instances to
 * access data in persistence. It looks for all active projects with &quot;Track Late Deliverables&quot; property set
 * to true, and then retrieves all incomplete deliverables for all late phases or open Final Fix phases that go
 * directly after Final Review. This class performs the logging of errors and debug information using Logging Wrapper
 * component.
 * </p>
 *
 * <p>
 * <em>Change in 1.1:</em>
 * <ol>
 * <li>In the new version this class calculates a compensated deadline for all phases with length not exceeding the
 * configured value. For such phases the compensated deadline differs with the real one when direct dependency phase
 * ended earlier than expected (e.g. Appeals Response phase can be compensated when Appeals phase ended earlier due to
 * "Complete Appeals" feature). For such phases the deliverable is late only after the compensated deadline is
 * reached.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Removed useless code.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <em>Changes in version 1.3:</em>
 * <ol>
 * <li>Supporting retrieval of late deliverables of requested types only.</li>
 * <li>Added support of Rejected Final Fix late deliverables.</li>
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
 *     name=&quot;com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl&quot;&gt;
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
 *     &lt;/Config&gt;
 * &lt;/CMConfig&gt;
 * </pre>
 *
 * </p>
 * <p>
 * Thread Safety: This class is not thread safe since it uses <code>ProjectManager</code>,
 * <code>PhaseManager</code>, <code>DeliverableManager</code> and <code>ResourceManager</code> instances that
 * are not thread safe. It's assumed that {@link #configure(ConfigurationObject)} method will be called just once
 * right after instantiation, before calling any business methods.
 * </p>
 *
 * @author saarixx, myxgyy, sparemax
 * @version 1.3.2
 */
public class LateDeliverablesRetrieverImpl implements LateDeliverablesRetriever {
    /**
     * <p>
     * Represents the name of this class used for logging.
     * </p>
     */
    private static final String CLASS_NAME = LateDeliverablesRetrieverImpl.class.getName();

    /**
     * <p>
     * Represents default value for &quot;maxDurationOfPhaseWithCompensatedDeadline&quot; property key in
     * configuration.
     * </p>
     *
     * @since 1.1
     */
    private static final long DEFAULT_MAX_DURATION_WITH_COMPENSATED_DEADLINE = 86400000;

    /**
     * <p>
     * Represents &quot;projectManagerKey&quot; property key in configuration.
     * </p>
     */
    private static final String PROJECT_MANAGER_KEY = "projectManagerKey";

    /**
     * <p>
     * Represents &quot;phaseManagerKey&quot; property key in configuration.
     * </p>
     */
    private static final String PHASE_MANAGER_KEY = "phaseManagerKey";

    /**
     * <p>
     * Represents &quot;deliverablePersistenceKey&quot; property key in configuration.
     * </p>
     */
    private static final String DELIVERABLE_PERSISTENCE_KEY = "deliverablePersistenceKey";

    /**
     * <p>
     * Represents &quot;searchBundleManagerNamespace&quot; property key in configuration.
     * </p>
     */
    private static final String SEARCH_BUNDLE_MANAGER_NAMESPACE = "searchBundleManagerNamespace";

    /**
     * <p>
     * Represents &quot;missedDeadlineTrackingDeliverableIds&quot; property key in configuration.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Renamed from "TRACKING_DELIVERABLE_IDS".</li>
     * <li>Changed property "trackingDeliverableIds" to "missedDeadlineTrackingDeliverableIds".</li>
     * </ol>
     * </p>
     */
    private static final String MISSED_DEADLINE_TRACKING_DELIVERABLE_IDS = "missedDeadlineTrackingDeliverableIds";

    /**
     * <p>
     * Represents &quot;deliverableName&quot; property key in configuration.
     * </p>
     */
    private static final String DELIVERABLE_NAME = "deliverableName";

    /**
     * <p>
     * Represents &quot;deliverableChecker&quot; prefix of property key in configuration.
     * </p>
     */
    private static final String DELIVERABLE_CHECKER = "deliverableChecker";

    /**
     * <p>
     * Represents &quot;deliverableCheckerKey&quot; property key in configuration.
     * </p>
     */
    private static final String DELIVERABLE_CHECKER_KEY = "deliverableCheckerKey";

    /**
     * <p>
     * Represents &quot;maxDurationOfPhaseWithCompensatedDeadline&quot; property key in configuration.
     * </p>
     *
     * @since 1.1
     */
    private static final String MAX_DURATION_WITH_COMPENSATED_DEADLINE = "maxDurationOfPhaseWithCompensatedDeadline";

    /**
     * <p>
     * Represents open phase status.
     * </p>
     */
    private static final String PHASE_STATUS_OPEN = "Open";

    /**
     * <p>
     * Represents active project status.
     * </p>
     */
    private static final String PROJECT_STATUS_ACTIVE = "Active";

    /**
     * <p>
     * Represents project property &quot;Track Late Deliverables&quot;.
     * </p>
     */
    private static final String TRACK_LATE_DELIVERABLES = "Track Late Deliverables";

    /**
     * <p>
     * Represents property value &quot;true&quot;.
     * </p>
     */
    private static final String TRUE = "true";

    /**
     * <p>
     * The project manager to be used by this class.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after
     * that.
     * </p>
     * <p>
     * Cannot be null after initialization.
     * </p>
     * <p>
     * Is used in <code>retrieve(Set&lt;LateDeliverableType&gt;)</code>.
     * </p>
     */
    private ProjectManager projectManager;

    /**
     * <p>
     * The phase manager to be used by this class.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after
     * that.
     * </p>
     * <p>
     * Cannot be null after initialization.
     * </p>
     * <p>
     * Is used in <code>retrieve(Set&lt;LateDeliverableType&gt;)</code>.
     * </p>
     */
    private PhaseManager phaseManager;

    /**
     * <p>
     * The deliverable manager to be used by this class.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after
     * that.
     * </p>
     * <p>
     * Cannot be null after initialization.
     * </p>
     * <p>
     * Is used in <code>retrieve(Set&lt;LateDeliverableType&gt;)</code>.
     * </p>
     */
    private DeliverableManager deliverableManager;

    /**
     * <p>
     * The logger used by this class for logging errors and debug information.
     * </p>
     * <p>
     * Is initialized in the {@link #configure(ConfigurationObject)} method and never
     * changed after that.
     * </p>
     * <p>
     * If is null after initialization, logging is not performed.
     * </p>
     * <p>
     * Is used in <code>retrieve(Set&lt;LateDeliverableType&gt;)</code>.
     * </p>
     */
    private Log log;

    /**
     * <p>
     * The set of deliverable IDs for which tracking must be performed.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that.
     * </p>
     * <p>
     * Can not be null or empty, cannot contain null or not positive element after initialization.
     * </p>
     * <p>
     * Is used in <code>retrieve(Set&lt;LateDeliverableType&gt;)</code>.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Renamed from "trackingDeliverableIds".</li>
     * </ol>
     * </p>
     */
    private Set<Long> missedDeadlineTrackingDeliverableIds;

    /**
     * <p>
     * The maximum duration of the phase in milliseconds (not inclusive) for which compensated deadline should be
     * calculated.
     * </p>
     *
     * <p>
     * Is initialized in configure() and never changed after that. Cannot be negative. Is used in
     * getCompensatedDeadline().
     * </p>
     *
     * @since 1.1
     */
    private long maxDurationOfPhaseWithCompensatedDeadline;

    /**
     * Creates an instance of <code>LateDeliverablesRetrieverImpl</code>.
     */
    public LateDeliverablesRetrieverImpl() {
    }

    /**
     * <p>
     * Configures this instance with use of the given configuration object.
     * </p>
     *
     * <p>
     * <em>Change in 1.1:</em>
     * <ol>
     * <li>Added step for reading maxDurationOfPhaseWithCompensatedDeadline.</li>
     * </ol>
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Renamed property and configuration parameter.</li>
     * </ol>
     * </p>
     *
     * @param config
     *            the configuration object.
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

        String missedDeadlineTrackingDeliverableIdsStr = Helper.getPropertyValue(config,
            MISSED_DEADLINE_TRACKING_DELIVERABLE_IDS, true, false);
        String[] missedDeadlineTrackingDeliverableIdsArray =
            missedDeadlineTrackingDeliverableIdsStr.split(Helper.COMMA);

        if (missedDeadlineTrackingDeliverableIdsArray.length == 0) {
            throw new LateDeliverablesTrackerConfigurationException("Property["
                + MISSED_DEADLINE_TRACKING_DELIVERABLE_IDS + "] should contain comma separated values.");
        }

        missedDeadlineTrackingDeliverableIds = new HashSet<Long>(missedDeadlineTrackingDeliverableIdsArray.length);

        for (String missedDeadlineTrackingDeliverableIdStr : missedDeadlineTrackingDeliverableIdsArray) {
            missedDeadlineTrackingDeliverableIds.add(Helper.parseLong(missedDeadlineTrackingDeliverableIdStr,
                MISSED_DEADLINE_TRACKING_DELIVERABLE_IDS, 1));
        }

        // create object factory
        ObjectFactory objectFactory = Helper.createObjectFactory(config);

        // create project manager via object factory
        projectManager = Helper.createObject(config, objectFactory, PROJECT_MANAGER_KEY,
            ProjectManager.class);

        // create phase manager via object factory
        phaseManager = Helper.createObject(config, objectFactory, PHASE_MANAGER_KEY, PhaseManager.class);

        Map<String, DeliverableChecker> deliverableCheckers = getDeliverableCheckers(config,
            objectFactory);

        // Create deliverable persistence with OF
        DeliverablePersistence deliverablePersistence = Helper.createObject(config, objectFactory,
            DELIVERABLE_PERSISTENCE_KEY, DeliverablePersistence.class);

        // Get search bundle manager namespace
        String searchBundleManagerNamespace = Helper.getPropertyValue(config,
            SEARCH_BUNDLE_MANAGER_NAMESPACE, true, false);

        // Create search bundle manager with this namespace
        SearchBundleManager searchBundleManager;

        try {
            searchBundleManager = new SearchBundleManager(searchBundleManagerNamespace);
        } catch (SearchBuilderConfigurationException e) {
            throw new LateDeliverablesTrackerConfigurationException(
                "Fails to create search bundle manager.", e);
        }

        // Create deliverable manager
        deliverableManager = new PersistenceDeliverableManager(deliverablePersistence,
            deliverableCheckers, searchBundleManager);

        // Read maxDurationOfPhaseWithCompensatedDeadline
        String maxDurationOfPhaseWithCompensatedDeadlineStr = Helper.getPropertyValue(config,
            MAX_DURATION_WITH_COMPENSATED_DEADLINE, false, false);
        maxDurationOfPhaseWithCompensatedDeadline = (maxDurationOfPhaseWithCompensatedDeadlineStr == null)
            ? DEFAULT_MAX_DURATION_WITH_COMPENSATED_DEADLINE
            : Helper.parseLong(maxDurationOfPhaseWithCompensatedDeadlineStr, MAX_DURATION_WITH_COMPENSATED_DEADLINE, 0);
    }

    /**
     * Creates <code>DeliverableChecker</code> from configuration.
     *
     * @param config
     *            the configuration object.
     * @param objectFactory
     *            the object factory to create the <code>DeliverableChecker</code>
     *            instance.
     * @return the map contains all name and <code>DeliverableChecker</code> instance.
     */
    private static Map<String, DeliverableChecker> getDeliverableCheckers(ConfigurationObject config,
        ObjectFactory objectFactory) {
        Map<String, DeliverableChecker> deliverableCheckers = new HashMap<String, DeliverableChecker>();
        String[] childNames = getAllChildrenNames(config);

        for (String childName : childNames) {
            if (childName.startsWith(DELIVERABLE_CHECKER)) {
                // Get deliverable checker details sub configuration
                ConfigurationObject deliverableCheckerConfig = Helper.getChildConfig(config, childName);

                // Get deliverable name from the config
                String deliverableName = Helper.getPropertyValue(deliverableCheckerConfig,
                    DELIVERABLE_NAME, true, false);

                // Create deliverable checker with OF
                DeliverableChecker deliverableChecker = Helper.createObject(deliverableCheckerConfig,
                    objectFactory, DELIVERABLE_CHECKER_KEY, DeliverableChecker.class);
                // Put deliverable checker to the map
                deliverableCheckers.put(deliverableName, deliverableChecker);
            }
        }

        if (deliverableCheckers.isEmpty()) {
            // at least one is required
            throw new LateDeliverablesTrackerConfigurationException(
                "At least one deliverable checker should be specified.");
        }

        return deliverableCheckers;
    }

    /**
     * Gets all children names.
     *
     * @param config
     *            the configuration object.
     * @return all children names.
     */
    private static String[] getAllChildrenNames(ConfigurationObject config) {
        try {
            return config.getAllChildrenNames();
        } catch (ConfigurationAccessException e) {
            throw new LateDeliverablesTrackerConfigurationException("Fails to get all children names.",
                e);
        }
    }

    /**
     * <p>
     * Retrieves information about late deliverables of the requested types. Returns an empty list if no late
     * deliverables are found.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Moved "Filter out Final Fixes for non-winning submitters" step to the beginning of the block (to avoid
     * useless instantiation and population of LateDeliverable instances).</li>
     * <li>Steps with "MOVED in 1.1" existed in the previous version, but was moved to another position inside the
     * block.</li>
     * <li>Added steps for taking the compensated deadline into account and setting it to the late deliverable.</li>
     * </ol>
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Added parameter "types".</li>
     * <li>Added step for setting MISSED_DEADLINE late deliverable type.</li>
     * <li>Added steps for obtaining REJECTED_FINAL_FIX late deliverables.</li>
     * </ol>
     * </p>
     *
     * @param types
     *            the types of late deliverables to be retrieved (null if all late deliverables of all supported types
     *            must be retrieved).
     *
     * @return the list with information about found late deliverables (not null, doesn't contain null).
     *
     * @throws IllegalArgumentException
     *             if types is empty or contains null.
     * @throws IllegalStateException
     *             if this class was not configured properly with use of {@link #configure(ConfigurationObject)}
     *             method (deliverableManager, projectManager, phaseManager or missedDeadlineTrackingDeliverableIds is
     *             null).
     * @throws LateDeliverablesRetrievalException
     *             if some error occurred when retrieving a list of late deliverables.
     */
    public synchronized List<LateDeliverable> retrieve(Set<LateDeliverableType> types)
        throws LateDeliverablesRetrievalException {
        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME + ".retrieve(Set<LateDeliverableType> types)";
        Helper.logEntrance(log, signature, new String[] {"types"}, new Object[] {types});


        // Check
        Helper.checkLateDeliverableTypes(types, "types", log, signature);

        // check state
        Helper.checkState(projectManager, "projectManager", log, signature);
        Helper.checkState(phaseManager, "phaseManager", log, signature);
        Helper.checkState(deliverableManager, "deliverableManager", log, signature);
        Helper.checkState(missedDeadlineTrackingDeliverableIds, "missedDeadlineTrackingDeliverableIds", log, signature);

        // Search for all active projects
        Project[] projects = searchProjects(signature);

        List<LateDeliverable> result;

        if (projects.length == 0) {
            result = new ArrayList<LateDeliverable>();
        } else {
            // Create mapping from project ID to Project instance
            Map<Long, Project> projectById = new HashMap<Long, Project>();

            // Create list for matched project IDs
            long[] projectIds = new long[projects.length];

            for (int i = 0; i < projects.length; i++) {
                long projectId = projects[i].getId();
                projectIds[i] = projectId;
                projectById.put(projectId, projects[i]);
            }

            // Get phase projects for all matched project IDs
            com.topcoder.project.phases.Project[] phaseProjects = getPhaseProjects(projectIds, signature);

            result = getLateDeliverables(types, phaseProjects, projectById, log, signature);
        }

        Helper.logExit(log, signature, result, start);

        return result;
    }

    /**
     * <p>
     * Gets the late deliverables.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Added parameter "types".</li>
     * <li>Added step for setting MISSED_DEADLINE late deliverable type.</li>
     * <li>Added steps for obtaining REJECTED_FINAL_FIX late deliverables.</li>
     * </ol>
     * </p>
     *
     * @param types
     *            the types of late deliverables to be retrieved (null if all late deliverables of all supported types
     *            must be retrieved).
     * @param phaseProjects
     *            the phase projects.
     * @param projectById
     *            the project by id map.
     * @param log
     *            the log.
     * @param signature
     *            the signature.
     *
     * @return the late deliverables.
     *
     * @throws LateDeliverablesRetrievalException
     *             if any error occurs.
     */
    private List<LateDeliverable> getLateDeliverables(Set<LateDeliverableType> types,
        com.topcoder.project.phases.Project[] phaseProjects, Map<Long, Project> projectById, Log log, String signature)
        throws LateDeliverablesRetrievalException {
        List<Long> latePhaseIds = new ArrayList<Long>();
        // Create a list for IDs of Final Fix phases going after Final Review:
        List<Long> finalFixPhaseIds = new ArrayList<Long>();
        Map<Long, Phase> phaseById = new HashMap<Long, Phase>();
        // holds the ids of projects that have late deliverables
        Set<Long> lateProjectIds = new HashSet<Long>();
        Date currentDate = new Date();

        // Check if "Missed Deadline" late deliverables should be retrieved:
        boolean retrieveMissedDeadlines =
            (types == null) || types.contains(LateDeliverableType.MISSED_DEADLINE);
        // Check if "Rejected Final Fix" late deliverables should be retrieved:
        boolean retrieveRejectedFinalFixes =
            (types == null) || types.contains(LateDeliverableType.REJECTED_FINAL_FIX);

        for (com.topcoder.project.phases.Project phaseProject : phaseProjects) {
            // get all phases for the project
            Phase[] phases = phaseProject.getAllPhases();

            for (Phase phase : phases) {
                if (!phase.getPhaseStatus().getName().equals(PHASE_STATUS_OPEN)) {
                    continue;
                }

                if (retrieveMissedDeadlines && currentDate.after(phase.getScheduledEndDate())) {
                    long phaseId = phase.getId();
                    latePhaseIds.add(phaseId);
                    phaseById.put(phaseId, phase);
                    lateProjectIds.add(phaseProject.getId());
                }
                if (retrieveRejectedFinalFixes && "Final Fix".equals(phase.getPhaseType().getName())
                    && dependsOnFinalReviewPhase(phase)) {
                    // Get ID of the phase:
                    long phaseId = phase.getId();
                    // Add phase ID to the list:
                    finalFixPhaseIds.add(phaseId);
                    // Put phase to the map:
                    phaseById.put(phaseId, phase);
                }
            }
        }

        List<LateDeliverable> result = new ArrayList<LateDeliverable>();
        if (!latePhaseIds.isEmpty()) {
            // log IDs of projects that have late phases
            Helper.logInfo(log, "IDs of projects that have late phases : " + lateProjectIds);
            Helper.logInfo(log, "IDs of late phases : " + latePhaseIds);

            Deliverable[] deliverables = searchDeliverables(latePhaseIds, true, false, signature);
            createLateDeliverables(result, deliverables, projectById, phaseById, true, signature);
        }
        if (!finalFixPhaseIds.isEmpty()) {
            Deliverable[] deliverables = searchDeliverables(finalFixPhaseIds, false, null, signature);
            createLateDeliverables(result, deliverables, projectById, phaseById, false, signature);
        }

        return result;
    }

    /**
     * Gets phase projects by project ids.
     *
     * @param projectIds
     *            the project ids.
     * @param signature
     *            the method name used for logging.
     * @return the phase projects.
     * @throws LateDeliverablesRetrievalException
     *             if any error occurs when getting phase projects.
     */
    private com.topcoder.project.phases.Project[] getPhaseProjects(long[] projectIds, String signature)
        throws LateDeliverablesRetrievalException {
        try {
            return phaseManager.getPhases(projectIds);
        } catch (PhaseManagementException e) {
            throw Helper.logException(log, signature, new LateDeliverablesRetrievalException(
                "Fails to get phase projects.", e));
        }
    }

    /**
     * Creates <code>LateDeliverable</code> instances from given parameters.
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Updated 'trackingDeliverableIds' to 'missedDeadlineTrackingDeliverableIds'.</li>
     * <li>Added support of Rejected Final Fix late deliverables.</li>
     * </ol>
     * </p>
     *
     * @param result
     *            the list to hold the <code>LateDeliverable</code> instances.
     * @param deliverables
     *            the deliverables to create late deliverables.
     * @param projectById
     *            the map contains projects.
     * @param phaseById
     *            the map contains phases.
     * @param missedDeadlineTracking
     *            the missed deadline tracking flag.
     * @param signature
     *            the signature.
     *
     * @throws LateDeliverablesRetrievalException
     *             if any error occurs.
     */
    private void createLateDeliverables(List<LateDeliverable> result, Deliverable[] deliverables,
        Map<Long, Project> projectById, Map<Long, Phase> phaseById, boolean missedDeadlineTracking, String signature)
        throws LateDeliverablesRetrievalException {
        for (Deliverable deliverable : deliverables) {

            // Get project ID of the deliverable
            long projectId = deliverable.getProject();
            // Get project by its ID from the map
            Project project = projectById.get(projectId);
            // Get phase ID from deliverable
            long phaseId = deliverable.getPhase();
            // Get phase by its ID from the map
            Phase phase = phaseById.get(phaseId);

            LateDeliverable lateDeliverable = new LateDeliverable();

            lateDeliverable.setDeliverable(deliverable);
            // Set project to the late deliverable instance
            lateDeliverable.setProject(project);
            // Set phase to the late deliverable instance
            lateDeliverable.setPhase(phase);

            if (missedDeadlineTracking) {
                // Get the compensated deadline if it differs from the real one
                Date compensatedDeadline = getCompensatedDeadline(phase);
                if ((compensatedDeadline != null) && (System.currentTimeMillis() < compensatedDeadline.getTime())) {
                    continue;
                }
                // Set compensated deadline to the late deliverable instance
                lateDeliverable.setCompensatedDeadline(compensatedDeadline);

                // Set late deliverable type to "Missed Deadline":
                lateDeliverable.setType(LateDeliverableType.MISSED_DEADLINE);
            } else {
                // Set late deliverable type to "Rejected Final Fix":
                lateDeliverable.setType(LateDeliverableType.REJECTED_FINAL_FIX);
            }

            // add to result
            result.add(lateDeliverable);
        }
    }

    /**
     * Searches active projects with &quot;Track Late Deliverables&quot; property equal to
     * &quot;true&quot;.
     *
     * @param signature
     *            the method name used for logging.
     * @return all the matched projects.
     * @throws LateDeliverablesRetrievalException
     *             if any error occurs when searching the projects.
     */
    private Project[] searchProjects(String signature) throws LateDeliverablesRetrievalException {
        // Build filter for matching active projects only
        Filter activeStatusFilter = ProjectFilterUtility
            .buildStatusNameEqualFilter(PROJECT_STATUS_ACTIVE);

        // Build filter for matching projects with "Track Late Deliverables" property
        // equal to "true"
        Filter trackLateDeliverablesFilter = ProjectFilterUtility.buildProjectPropertyEqualFilter(
            TRACK_LATE_DELIVERABLES, TRUE);

        // Construct combined filter for filtering project
        AndFilter projectFilter = new AndFilter(activeStatusFilter, trackLateDeliverablesFilter);

        // Search for all active projects
        Project[] projects;

        try {
            projects = projectManager.searchProjects(projectFilter);
        } catch (PersistenceException e) {
            throw Helper.logException(log, signature, new LateDeliverablesRetrievalException(
                "Fails to search projects.", e));
        }

        return projects;
    }

    /**
     * Searches deliverables for the given phases.
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Updated 'trackingDeliverableIds' to 'missedDeadlineTrackingDeliverableIds'.</li>
     * <li>Added support of Rejected Final Fix deliverables.</li>
     * </ol>
     * </p>
     *
     * @param phaseIds
     *            the ids of phases.
     * @param missedDeadlineTracking
     *            the missed deadline tracking flag.
     * @param complete
     *            the complete flag.
     * @param signature
     *            the method name used for logging.
     * @return the deliverables.
     *
     * @throws LateDeliverablesRetrievalException
     *             if any error occurs.
     */
    private Deliverable[] searchDeliverables(List<Long> phaseIds, boolean missedDeadlineTracking, Boolean complete,
        String signature) throws LateDeliverablesRetrievalException {
        Filter filter;

        if (phaseIds.size() == 1) {
            // Create filter for filtering deliverables by the only phase ID
            filter = DeliverableFilterBuilder.createPhaseIdFilter(phaseIds.get(0));
        } else {
            // Create a list for individual phase ID filters:
            List<Filter> individualPhaseIdFilters = new ArrayList<Filter>();

            for (long phaseId : phaseIds) {
                Filter individualPhaseIdFilter = DeliverableFilterBuilder.createPhaseIdFilter(phaseId);
                individualPhaseIdFilters.add(individualPhaseIdFilter);
            }

            filter = new OrFilter(individualPhaseIdFilters);
        }

        if (missedDeadlineTracking) {
            // Missed deadline tracking

            List<Filter> individualDeliverableIdFilters = new ArrayList<Filter>();

            for (Long missedDeadlineTrackingDeliverableId : missedDeadlineTrackingDeliverableIds) {
                Filter deliverableIdFilter = DeliverableFilterBuilder
                    .createDeliverableIdFilter(missedDeadlineTrackingDeliverableId);
                individualDeliverableIdFilters.add(deliverableIdFilter);
            }

            Filter deliverableIdFilter = new OrFilter(individualDeliverableIdFilters);

            filter = new AndFilter(filter, deliverableIdFilter);
        }

        Deliverable[] deliverables;

        try {
            deliverables = deliverableManager.searchDeliverables(filter, complete);
        } catch (DeliverablePersistenceException e) {
            throw Helper.logException(log, signature, new LateDeliverablesRetrievalException(
                "Persistence layer error when searching deliverables.", e));
        } catch (SearchBuilderException e) {
            throw Helper.logException(log, signature, new LateDeliverablesRetrievalException(
                "Search builder related error occurs when searching deliverables.", e));
        } catch (DeliverableCheckingException e) {
            throw Helper.logException(log, signature, new LateDeliverablesRetrievalException(
                "Fails to check the deliverable when searching deliverables.", e));
        }

        return deliverables;
    }

    /**
     * <p>
     * Retrieves the compensated deadline. This method returns null if the compensated deadline equals to the real
     * one, i.e. when the phase is long enough not to be compensated or none of the dependency phases ended earlier
     * than expected.
     * </p>
     *
     * @param phase
     *            the phase for which compensated deadline must be retrieved.
     *
     * @return the retrieved compensated deadline or null if the phase is long enough not to be compensated or the
     *         compensated deadline equals to the real one.
     *
     * @since 1.1
     */
    private Date getCompensatedDeadline(Phase phase) {
        // Get length of the phase in milliseconds:
        long phaseLength = phase.getLength();
        if (phaseLength >= maxDurationOfPhaseWithCompensatedDeadline) {
            return null;
        }
        // Create a list for phase dependencies with "start when ends" type
        List<Dependency> dependencies = new ArrayList<Dependency>();
        // Get all phase dependencies:
        Dependency[] phaseDependencies = phase.getAllDependencies();
        for (Dependency dependency : phaseDependencies) {
            if (dependency.isDependentStart() && (!dependency.isDependencyStart())) {
                // Add dependency to the list:
                dependencies.add(dependency);
            }
        }
        if (dependencies.isEmpty()) {
            return null;
        }
        // Will hold the maximum phase start date computed based on the full dependency phase length
        Date maxOriginalScheduledStartDate = null;
        for (Dependency dependency : dependencies) {
            // Get the next dependency phase:
            Phase dependencyPhase = dependency.getDependency();
            // Get actual start date of the dependency phase:
            Date dependencyStartDate = dependencyPhase.getActualStartDate();
            if (dependencyStartDate == null) {
                return null;
            }

            // Get lag time between phases in milliseconds:
            long lagTime = dependency.getLagTime();
            // Get scheduled (not actual) dependency phase length in milliseconds:
            long dependencyPhaseLength = dependencyPhase.getLength();
            // Get the expected start date of the processed phase:
            Date originalPhaseStartDate = new Date(dependencyStartDate.getTime() + dependencyPhaseLength + lagTime);
            if ((maxOriginalScheduledStartDate == null)
                || (maxOriginalScheduledStartDate.before(originalPhaseStartDate))) {
                maxOriginalScheduledStartDate = originalPhaseStartDate;
            }
        }
        // Get fixed start date of the phase:
        Date fixedStartDate = phase.getFixedStartDate();
        if ((fixedStartDate != null) && (fixedStartDate.after(maxOriginalScheduledStartDate))) {
            maxOriginalScheduledStartDate = fixedStartDate;
        }

        // Get the original scheduled phase end date:
        Date result = new Date(maxOriginalScheduledStartDate.getTime() + phaseLength);

        // Get current scheduled end date of the phase:
        Date scheduledEndDate = phase.getScheduledEndDate();
        if (!result.after(scheduledEndDate)) {
            return null;
        }
        return result;
    }

    /**
     * <p>
     * Checks whether the specified phase depends on Final Review phase.
     * </p>
     *
     * @param phase
     *            the phase.
     *
     * @return true if the specified phase depends on Final Review phase; false otherwise.
     *
     * @since 1.3
     */
    private static boolean dependsOnFinalReviewPhase(Phase phase) {
        // Get all dependencies of the phase:
        Dependency[] dependencies = phase.getAllDependencies();
        for (Dependency dependency : dependencies) {
            // Get dependent phase from dependency data:
            Phase dependentPhase = dependency.getDependent();
            if (dependentPhase != phase) {
                continue;
            }

            // Get type name of the dependency phase:
            String dependencyPhaseTypeName = dependency.getDependency().getPhaseType().getName();
            if ("Final Review".equals(dependencyPhaseTypeName)) {
                return true;
            }
        }
        return false;
    }
}
