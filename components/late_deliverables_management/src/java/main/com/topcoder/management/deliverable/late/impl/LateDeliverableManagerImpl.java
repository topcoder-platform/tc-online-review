/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.configuration.persistence.ConfigurationPersistenceException;
import com.topcoder.management.deliverable.late.Helper;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableManagementConfigurationException;
import com.topcoder.management.deliverable.late.LateDeliverableManagementException;
import com.topcoder.management.deliverable.late.LateDeliverableManager;
import com.topcoder.management.deliverable.late.LateDeliverableType;
import com.topcoder.search.builder.PersistenceOperationException;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.NullFilter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.util.log.Log;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.SpecificationFactoryException;
import com.topcoder.util.objectfactory.impl.ConfigurationObjectSpecificationFactory;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;
import com.topcoder.util.sql.databaseabstraction.InvalidCursorStateException;
import com.topcoder.util.sql.databaseabstraction.NullColumnValueException;

/**
 * <p>
 * This class is an implementation of LateDeliverableManager that uses Search Builder component to retrieve by ID or
 * search for late deliverables in persistence and pluggable LateDeliverablePersistence instance to update late
 * deliverables in persistence. This class uses Logging Wrapper component to log errors and debug information.
 * </p>
 *
 * <p>
 * <em>Sample Configuration:</em>
 *
 * <pre>
 * &lt;?xml version=&quot;1.0&quot;?&gt;
 * &lt;CMConfig&gt;
 *   &lt;Config name=&quot;com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl&quot;&gt;
 *     &lt;Property name=&quot;loggerName&quot;&gt;
 *       &lt;Value&gt;myLogger&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;objectFactoryConfig&quot;&gt;
 *       &lt;Property name=&quot;DatabaseLateDeliverablePersistence&quot;&gt;
 *         &lt;Property name=&quot;type&quot;&gt;
 *           &lt;Value&gt;
 *             com.topcoder.management.deliverable.late.impl.persistence.DatabaseLateDeliverablePersistence
 *           &lt;/Value&gt;
 *         &lt;/Property&gt;
 *       &lt;/Property&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;searchBundleManagerNamespace&quot;&gt;
 *       &lt;Value&gt;LateDeliverableManagerImpl.SearchBuilderManager&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;nonRestrictedSearchBundleName&quot;&gt;
 *       &lt;Value&gt;Non-restricted Late Deliverable Search Bundle&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;restrictedSearchBundleName&quot;&gt;
 *       &lt;Value&gt;Restricted Late Deliverable Search Bundle&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;persistenceKey&quot;&gt;
 *       &lt;Value&gt;DatabaseLateDeliverablePersistence&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name=&quot;persistenceConfig&quot;&gt;
 *       &lt;Property name=&quot;loggerName&quot;&gt;
 *         &lt;Value&gt;myLogger&lt;/Value&gt;
 *       &lt;/Property&gt;
 *       &lt;Property name=&quot;dbConnectionFactoryConfig&quot;&gt;
 *         &lt;Property name=&quot;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&quot;&gt;
 *           &lt;Property name=&quot;connections&quot;&gt;
 *             &lt;Property name=&quot;default&quot;&gt;
 *               &lt;Value&gt;myConnection&lt;/Value&gt;
 *             &lt;/Property&gt;
 *             &lt;Property name=&quot;myConnection&quot;&gt;
 *               &lt;Property name=&quot;producer&quot;&gt;
 *                   &lt;Value&gt;com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer&lt;/Value&gt;
 *               &lt;/Property&gt;
 *               &lt;Property name=&quot;parameters&quot;&gt;
 *                 &lt;Property name=&quot;jdbc_driver&quot;&gt;
 *                   &lt;Value&gt;com.informix.jdbc.IfxDriver&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name=&quot;jdbc_url&quot;&gt;
 *                   &lt;Value&gt;
 *                     jdbc:informix-sqli://localhost:1526/tcs_catalog:informixserver=ol_topcoder
 *                   &lt;/Value&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name=&quot;SelectMethod&quot;&gt;
 *                   &lt;Value&gt;cursor&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name=&quot;user&quot;&gt;
 *                   &lt;Value&gt;informix&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name=&quot;password&quot;&gt;
 *                   &lt;Value&gt;123456&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *               &lt;/Property&gt;
 *             &lt;/Property&gt;
 *           &lt;/Property&gt;
 *         &lt;/Property&gt;
 *       &lt;/Property&gt;
 *       &lt;Property name=&quot;connectionName&quot;&gt;
 *         &lt;Value&gt;myConnection&lt;/Value&gt;
 *       &lt;/Property&gt;
 *     &lt;/Property&gt;
 *   &lt;/Config&gt;
 * &lt;/CMConfig&gt;
 * </pre>
 *
 * </p>
 *
 * <p>
 * <em>Sample Code:</em>
 *
 * <pre>
 * // Create an instance of LateDeliverableManagerImpl using custom configuration
 * ConfigurationObject configuration = TestsHelper.getConfig();
 * LateDeliverableManagerImpl lateDeliverableManager = new LateDeliverableManagerImpl(configuration);
 *
 * // Create an instance of LateDeliverableManagerImpl using custom config file and namespace
 * lateDeliverableManager = new LateDeliverableManagerImpl(LateDeliverableManagerImpl.DEFAULT_CONFIG_FILENAME,
 *     LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE);
 *
 * // Create an instance of LateDeliverableManagerImpl using default config file
 * lateDeliverableManager = new LateDeliverableManagerImpl();
 *
 * // Retrieve the late deliverable with ID=1
 * LateDeliverable lateDeliverable = lateDeliverableManager.retrieve(1);
 * // lateDeliverable.getId() must be 1
 * // lateDeliverable.getProjectPhaseId() must be 101
 * // lateDeliverable.getResourceId() must be 1001
 * // lateDeliverable.getDeliverableId() must be 4
 * // lateDeliverable.isForgiven() must be false
 * // lateDeliverable.getExplanation() must be null
 * // lateDeliverable.getType().getId() must be 1
 * // lateDeliverable.getType().getName() must be &quot;Missed Deadline&quot;
 * // lateDeliverable.getType().getDescription() must be &quot;Missed Deadline&quot;
 *
 * // Update the late deliverable by changing its forgiven flag and explanation
 * lateDeliverable.setForgiven(true);
 * lateDeliverable.setExplanation(&quot;OR didn't work&quot;);
 * lateDeliverableManager.update(lateDeliverable);
 *
 * // Search for all forgiven late deliverables for project with ID=100000
 * Filter forgivenFilter = LateDeliverableFilterBuilder.createForgivenFilter(true);
 * Filter projectIdFilter = LateDeliverableFilterBuilder.createProjectIdFilter(100000);
 * Filter compositeFilter = new AndFilter(forgivenFilter, projectIdFilter);
 *
 * List&lt;LateDeliverable&gt; lateDeliverables = lateDeliverableManager.searchAllLateDeliverables(compositeFilter);
 * // lateDeliverables.size() must be 1
 * // lateDeliverables.get(0).getId() must be 1
 * // lateDeliverables.get(0).getProjectPhaseId() must be 101
 * // lateDeliverables.get(0).getResourceId() must be 1001
 * // lateDeliverables.get(0).getDeliverableId() must be 4
 * // lateDeliverables.get(0).isForgiven() must be true
 * // lateDeliverables.get(0).getExplanation() must be &quot;OR didn't work&quot;
 * // lateDeliverables.get(0).getType().getId() must be 1
 * // lateDeliverables.get(0).getType().getName() must be &quot;Missed Deadline&quot;
 * // lateDeliverables.get(0).getType().getDescription() must be &quot;Missed Deadline&quot;
 *
 * // Search for all late deliverables from design category for all active projects
 * // to which user with ID=3 has a manager/copilot access
 * Filter categoryFilter = LateDeliverableFilterBuilder.createProjectCategoryIdFilter(1);
 * Filter activeProjectFilter = LateDeliverableFilterBuilder.createProjectStatusIdFilter(1);
 * compositeFilter = new AndFilter(categoryFilter, activeProjectFilter);
 * lateDeliverables = lateDeliverableManager.searchRestrictedLateDeliverables(compositeFilter, 3);
 * // lateDeliverables.size() must be 1
 * // lateDeliverables.get(0).getId() must be 2
 * // lateDeliverables.get(0).getProjectPhaseId() must be 102
 * // lateDeliverables.get(0).getResourceId() must be 1002
 * // lateDeliverables.get(0).getDeliverableId() must be 3
 * // lateDeliverables.get(0).isForgiven() must be false
 * // lateDeliverables.get(0).getExplanation() must be null
 * // lateDeliverables.get(0).getType().getId() must be 1
 * // lateDeliverables.get(0).getType().getName() must be &quot;Missed Deadline&quot;
 * // lateDeliverables.get(0).getType().getDescription() must be &quot;Missed Deadline&quot;
 *
 * // Retrieve all late deliverable types
 * List&lt;LateDeliverableType&gt; lateDeliverableTypes = lateDeliverableManager.getLateDeliverableTypes();
 * // lateDeliverableTypes.size() must be 2
 * // lateDeliverableTypes.get(0).getId() must be 1
 * // lateDeliverableTypes.get(0).getName() must be &quot;Missed Deadline&quot;
 * // lateDeliverableTypes.get(0).getDescription() must be &quot;Missed Deadline&quot;
 * // lateDeliverableTypes.get(1).getId() must be 2
 * // lateDeliverableTypes.get(1).getName() must be &quot;Rejected Final Fix&quot;
 * // lateDeliverableTypes.get(1).getDescription() must be &quot;Rejected Final Fix&quot;
 * </pre>
 *
 * </p>
 *
 * <p>
 * <em>Changes in version 1.0.6:</em>
 * <ol>
 * <li>Added getLateDeliverableTypes() method.</li>
 * <li>Updated throws documentation of update() method.</li>
 * <li>Updated getLateDeliverables() method.</li>
 * <li>Updated class documentation.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is immutable and thread safe when entities passed to it are used by the
 * caller in thread safe manner. It uses thread safe SearchBundle, LateDeliverablePersistence and Log instances.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0.6
 */
public class LateDeliverableManagerImpl implements LateDeliverableManager {
    /**
     * <p>
     * The default configuration file path for this class. It's an immutable static constant.
     * </p>
     */
    public static final String DEFAULT_CONFIG_FILENAME =
        "com/topcoder/management/deliverable/late/impl/LateDeliverableManagerImpl.properties";

    /**
     * <p>
     * The default configuration namespace for this class. It's an immutable static constant.
     * </p>
     */
    public static final String DEFAULT_CONFIG_NAMESPACE =
        "com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl";

    /**
     * <p>
     * Represents the class name.
     * </p>
     */
    private static final String CLASS_NAME = LateDeliverableManagerImpl.class.getName();

    /**
     * <p>
     * Represents the child key 'objectFactoryConfig'.
     * </p>
     */
    private static final String KEY_OF_CONFIG = "objectFactoryConfig";

    /**
     * <p>
     * Represents the child key 'persistenceConfig'.
     * </p>
     */
    private static final String KEY_PERSISTENCE_CONFIG = "persistenceConfig";

    /**
     * <p>
     * Represents the property key 'searchBundleManagerNamespace'.
     * </p>
     */
    private static final String KEY_SBM_NAMESPACE = "searchBundleManagerNamespace";

    /**
     * <p>
     * Represents the property key 'nonRestrictedSearchBundleName'.
     * </p>
     */
    private static final String KEY_NON_RESTRICTED_SB_NAME = "nonRestrictedSearchBundleName";

    /**
     * <p>
     * Represents the property key 'restrictedSearchBundleName'.
     * </p>
     */
    private static final String KEY_RESTRICTED_SB_NAME = "restrictedSearchBundleName";

    /**
     * <p>
     * Represents the property key 'persistenceKey'.
     * </p>
     */
    private static final String KEY_PERSISTENCE_KEY = "persistenceKey";

    /**
     * <p>
     * The logger used by this class for logging errors and debug information./p>
     *
     * <p>
     * Is null if logging is not required. Is initialized in the constructor and never changed after that. Is used in
     * all methods.
     * </p>
     */
    private final Log log;

    /**
     * <p>
     * The search bundle used by this class when searching for late deliverables with no user restriction./p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Cannot be null. Is used in
     * searchAllLateDeliverables().
     * </p>
     */
    private final SearchBundle nonRestrictedSearchBundle;

    /**
     * <p>
     * The search bundle used by this class when searching for late deliverables with specific user restriction. /p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Cannot be null. Is used in
     * searchRestrictedLateDeliverables().
     * </p>
     */
    private final SearchBundle restrictedSearchBundle;

    /**
     * <p>
     * The late deliverable persistence instance used by this class for updating late deliverables in persistence./p>
     *
     * <p>
     * Is initialized in the constructor and never changed after that. Cannot be null. Is used in update().
     * </p>
     */
    private final LateDeliverablePersistence persistence;

    /**
     * <p>
     * Creates an instance of LateDeliverableManagerImpl and initializes it from the default configuration file.
     * </p>
     *
     * @throws LateDeliverableManagementConfigurationException
     *             if error occurred while reading the configuration or initializing this class.
     */
    public LateDeliverableManagerImpl() {
        this(DEFAULT_CONFIG_FILENAME, DEFAULT_CONFIG_NAMESPACE);
    }

    /**
     * <p>
     * Creates an instance of LateDeliverableManagerImpl and initializes it from the specified configuration file.
     * </p>
     *
     * @param filePath
     *            the path of the configuration file.
     * @param namespace
     *            the configuration namespace.
     *
     * @throws IllegalArgumentException
     *             if filePath or namespace is null/empty.
     * @throws LateDeliverableManagementConfigurationException
     *             if error occurred while reading the configuration or initializing this class.
     */
    public LateDeliverableManagerImpl(String filePath, String namespace) {
        this(getConfig(filePath, namespace));
    }

    /**
     * <p>
     * Creates an instance of LateDeliverableManagerImpl and initializes it from the given configuration object.
     * </p>
     *
     * @param configuration
     *            the configuration object for this class.
     *
     * @throws IllegalArgumentException
     *             if configuration is null.
     * @throws LateDeliverableManagementConfigurationException
     *             if error occurred while reading the configuration or initializing this class.
     */
    public LateDeliverableManagerImpl(ConfigurationObject configuration) {
        Helper.checkNull(configuration, "configuration");

        // Get logger
        log = Helper.getLog(configuration);

        try {
            // Get search bundle manager namespace from the configuration:
            String searchBundleManagerNamespace = Helper.getProperty(configuration, KEY_SBM_NAMESPACE, true);
            // Create search bundle manager:
            SearchBundleManager searchBundleManager = new SearchBundleManager(searchBundleManagerNamespace);

            // Get non-restricted search bundle from the manager
            nonRestrictedSearchBundle = getSearchBundle(searchBundleManager, configuration, KEY_NON_RESTRICTED_SB_NAME);
            // Get restricted search bundle from the manager
            restrictedSearchBundle = getSearchBundle(searchBundleManager, configuration, KEY_RESTRICTED_SB_NAME);

            // Create object factory
            ObjectFactory objectFactory = new ObjectFactory(
                new ConfigurationObjectSpecificationFactory(Helper.getChildConfig(configuration, KEY_OF_CONFIG)));

            String persistenceKey = Helper.getProperty(configuration, KEY_PERSISTENCE_KEY, true);
            // Create persistence implementation instance with object factory
            persistence = (LateDeliverablePersistence) objectFactory.createObject(persistenceKey);

            // Configure the persistence instance
            persistence.configure(Helper.getChildConfig(configuration, KEY_PERSISTENCE_CONFIG));
        } catch (SearchBuilderConfigurationException e) {
            throw new LateDeliverableManagementConfigurationException(
                "Failed to create an instance of SearchBundleManager.", e);
        } catch (SpecificationFactoryException e) {
            throw new LateDeliverableManagementConfigurationException(
                "Failed to create an instance of ConfigurationObjectSpecificationFactory.", e);
        } catch (InvalidClassSpecificationException e) {
            throw new LateDeliverableManagementConfigurationException(
                "An error occurs when creating a LateDeliverablePersistence with key 'persistenceKey'.", e);
        } catch (ClassCastException e) {
            throw new LateDeliverableManagementConfigurationException(
                "The created object is not of type LateDeliverablePersistence.", e);
        }
    }

    /**
     * <p>
     * Updates the given late deliverable in persistence.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.0.6:</em>
     * <ol>
     * <li>Updated throws documentation for IllegalArgumentException.</li>
     * </ol>
     * </p>
     *
     * @param lateDeliverable
     *            the late deliverable with updated data.
     *
     * @throws IllegalArgumentException
     *             if lateDeliverable is null, lateDeliverable.getId() &lt;= 0, lateDeliverable.getDeadline() is null,
     *             lateDeliverable.getCreateDate() is null, lateDeliverable.getType() is null,
     *             lateDeliverable.getType().getId() &lt;= 0.
     * @throws LateDeliverableNotFoundException
     *             if late deliverable with ID equal to lateDeliverable.getId() doesn't exist in persistence.
     * @throws LateDeliverablePersistenceException
     *             if some other error occurred when accessing the persistence.
     */
    public void update(LateDeliverable lateDeliverable) throws LateDeliverablePersistenceException {
        Date enterTimestamp = new Date();
        String signature = getSignature("update(LateDeliverable lateDeliverable)");

        try {
            // Log method entry
            Helper.logEntrance(log, signature,
                new String[] {"lateDeliverable"},
                new Object[] {lateDeliverable});

            // Delegate execution to persistence
            persistence.update(lateDeliverable);

            // Log method exit
            Helper.logExit(log, signature, null, enterTimestamp);
        } catch (IllegalArgumentException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "IllegalArgumentException is thrown.");
        } catch (LateDeliverableNotFoundException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "LateDeliverableNotFoundException is thrown"
                + " when updating the given late deliverable in persistence.");
        } catch (LateDeliverablePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "LateDeliverablePersistenceException is thrown"
                + " when updating the given late deliverable in persistence.");
        }
    }

    /**
     * <p>
     * Retrieves the late deliverable with the given ID.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.0.6:</em>
     * <ol>
     * <li>The late deliverable types will be retrieved.</li>
     * </ol>
     * </p>
     *
     * @param lateDeliverableId
     *            the ID of the late deliverable to be retrieved.
     *
     * @return the retrieved late deliverable with the given ID (or null if late deliverable with the given ID doesn't
     *         exist).
     *
     * @throws IllegalArgumentException
     *             if lateDeliverableId &lt;= 0.
     * @throws LateDeliverablePersistenceException
     *             if some other error occurred when accessing the persistence.
     * @throws LateDeliverableManagementException
     *             if some other error occurred.
     */
    public LateDeliverable retrieve(long lateDeliverableId) throws LateDeliverableManagementException {
        Date enterTimestamp = new Date();
        String signature = getSignature("retrieve(long lateDeliverableId)");

        try {
            // Log method entry
            Helper.logEntrance(log, signature,
                new String[] {"lateDeliverableId"},
                new Object[] {lateDeliverableId});

            Helper.checkPositive(lateDeliverableId, "lateDeliverableId");

            // Create a filter for matching late deliverable ID:
            Filter filter = new EqualToFilter("id", lateDeliverableId);
            // Search for late deliverable with the specified ID:
            List<LateDeliverable> lateDeliverables = searchAllLateDeliverables(filter);

            LateDeliverable result = lateDeliverables.isEmpty() ? null : lateDeliverables.get(0);

            // Log method exit
            Helper.logExit(log, signature, new Object[] {result}, enterTimestamp);

            return result;
        } catch (IllegalArgumentException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "IllegalArgumentException is thrown.");
        } catch (LateDeliverablePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "LateDeliverablePersistenceException is thrown"
                + " when retrieving the late deliverable.");
        } catch (LateDeliverableManagementException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "LateDeliverableManagementException is thrown"
                + " when retrieving the late deliverable.");
        }
    }

    /**
     * <p>
     * Searches for all late deliverables that are matched with the given filter. Returns an empty list if none found.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.0.6:</em>
     * <ol>
     * <li>The late deliverable types will be retrieved.</li>
     * </ol>
     * </p>
     *
     * @param filter
     *            the filter for late deliverables (null if all late deliverables need to be retrieved).
     *
     * @return the list with found late deliverables that are matched with the given filter (not null, doesn't contain
     *         null).
     *
     * @throws LateDeliverablePersistenceException
     *             if some other error occurred when accessing the persistence.
     * @throws LateDeliverableManagementException
     *             if some other error occurred.
     */
    public List<LateDeliverable> searchAllLateDeliverables(Filter filter) throws LateDeliverableManagementException {
        Date enterTimestamp = new Date();
        String signature = getSignature("searchAllLateDeliverables(Filter filter)");

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"filter"},
            new Object[] {filter});

        try {
            if (filter == null) {
                // Create filter that matches all records:
                filter = new NotFilter(new NullFilter("deliverableId"));
            }
            // Get late deliverables using Search Builder:
            List<LateDeliverable> result = getLateDeliverables(nonRestrictedSearchBundle, filter);

            // Log method exit
            Helper.logExit(log, signature, new Object[] {result}, enterTimestamp);

            return result;
        } catch (LateDeliverablePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "LateDeliverablePersistenceException is thrown"
                + " when searching for late deliverables.");
        } catch (LateDeliverableManagementException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "LateDeliverableManagementException is thrown"
                + " when searching for late deliverables.");
        }
    }

    /**
     * <p>
     * Searches for all late deliverables that are matched with the given filter checking whether the user with the
     * specified ID has owner, manager or cockpit project access to the deliverables. Returns an empty list if none
     * found.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.0.6:</em>
     * <ol>
     * <li>The late deliverable types will be retrieved.</li>
     * </ol>
     * </p>
     *
     * @param userId
     *            the ID of the user.
     * @param filter
     *            the filter for late deliverables (null if deliverables must be filtered by user only).
     *
     * @return the list with found late deliverables that are matched with the given filter and accessed by the
     *         specified user (not null, doesn't contain null).
     *
     * @throws IllegalArgumentException
     *             if userId &lt;= 0.
     * @throws LateDeliverablePersistenceException
     *             if some other error occurred when accessing the persistence.
     * @throws LateDeliverableManagementException
     *             if some other error occurred.
     */
    public List<LateDeliverable> searchRestrictedLateDeliverables(Filter filter, long userId)
        throws LateDeliverableManagementException {
        Date enterTimestamp = new Date();
        String signature = getSignature("searchRestrictedLateDeliverables(Filter filter, long userId)");

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"filter", "userId"},
            new Object[] {filter, userId});

        try {
            Helper.checkPositive(userId, "userId");

            // Create filter for matching manager user ID:
            Filter managerUserIdFilter = new EqualToFilter("managerUserId", userId);
            // Create filter for matching late user ID:
            Filter lateUserIdFilter = new EqualToFilter("lateUserId", userId);
            // Create filter for matching ID of user that has access to the project from TC Direct:
            Filter tcDirectUserIdFilter = new EqualToFilter("tcDirectUserId", userId);

            // Create a composite filter for matching user access:
            Filter userIdFilter = new OrFilter(new OrFilter(managerUserIdFilter, lateUserIdFilter),
                tcDirectUserIdFilter);

            Filter compositeFilter;
            if (filter != null) {
                // Create a general composite filter:
                compositeFilter = new AndFilter(userIdFilter, filter);
            } else {
                compositeFilter = userIdFilter;
            }

            // Get late deliverables using Search Builder:
            List<LateDeliverable> result = getLateDeliverables(restrictedSearchBundle, compositeFilter);

            // Log method exit
            Helper.logExit(log, signature, new Object[] {result}, enterTimestamp);

            return result;
        } catch (IllegalArgumentException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "IllegalArgumentException is thrown.");
        } catch (LateDeliverablePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "LateDeliverablePersistenceException is thrown"
                + " when searching for late deliverables.");
        } catch (LateDeliverableManagementException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "LateDeliverableManagementException is thrown"
                + " when searching for late deliverables.");
        }
    }

    /**
     * <p>
     * Retrieves all existing late deliverable types.
     * </p>
     *
     * @return the retrieved late deliverable types (not null, doesn't contain null).
     *
     * @throws LateDeliverablePersistenceException
     *             if some error occurred when accessing the persistence.
     *
     * @since 1.0.6
     */
    public List<LateDeliverableType> getLateDeliverableTypes() throws LateDeliverablePersistenceException {
        Date enterTimestamp = new Date();
        String signature = getSignature("getLateDeliverableTypes()");

        try {
            // Log method entry
            Helper.logEntrance(log, signature, null, null);

            List<LateDeliverableType> result = persistence.getLateDeliverableTypes();

            // Log method exit
            Helper.logExit(log, signature, new Object[] {result}, enterTimestamp);

            return result;
        } catch (LateDeliverablePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "LateDeliverablePersistenceException is thrown"
                + " when retrieving all existing late deliverable types.");
        }
    }

    /**
     * <p>
     * Retrieves the list of late deliverables with the search bundle and filter.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.0.6:</em>
     * <ol>
     * <li>Added steps to retrieve the late deliverable type.</li>
     * </ol>
     * </p>
     *
     * @param searchBundle
     *            the search bundle.
     * @param filter
     *            the filter.
     *
     * @return the retrieved late deliverables (not null, doesn't contain null).
     *
     * @throws LateDeliverablePersistenceException
     *             if some error occurred when retrieving late deliverables.
     * @throws LateDeliverableManagementException
     *             if some other error occurred.
     */
    private static List<LateDeliverable> getLateDeliverables(SearchBundle searchBundle, Filter filter)
        throws LateDeliverableManagementException {

        try {
            // Perform the search using Search Builder:
            CustomResultSet resultSet = (CustomResultSet) searchBundle.search(filter);

            // Create a list for result
            List<LateDeliverable> result = new ArrayList<LateDeliverable>();
            // Create a map for cached late deliverable types:
            Map<Long, LateDeliverableType> lateDeliverableTypes = new HashMap<Long, LateDeliverableType>();

            while (resultSet.next()) {
                // Create late deliverable instance:
                LateDeliverable lateDeliverable = new LateDeliverable();

                int index = 1;

                // Copy ID to the late deliverable instance:
                lateDeliverable.setId(resultSet.getLong(index++));
                // Copy project ID from the result set to the late deliverable instance:
                lateDeliverable.setProjectId(resultSet.getLong(index++));
                // Copy project phase ID from the result set to the late deliverable instance:
                lateDeliverable.setProjectPhaseId(resultSet.getLong(index++));
                // Copy resource ID from the result set to the late deliverable instance:
                lateDeliverable.setResourceId(resultSet.getLong(index++));
                // Copy deliverable ID from the result set to the late deliverable instance:
                lateDeliverable.setDeliverableId(resultSet.getLong(index++));
                // Copy deadline from the result set to the late deliverable instance:
                lateDeliverable.setDeadline(resultSet.getTimestamp(index++));
                // Copy compensated deadline from the result set to the late deliverable instance:
                lateDeliverable.setCompensatedDeadline(resultSet.getTimestamp(index++));
                // Copy creation date from the result set to the late deliverable instance:
                lateDeliverable.setCreateDate(resultSet.getTimestamp(index++));

                int forgiven = resultSet.getInt(index++);
                // Copy forgiven flag to the late deliverable instance:
                lateDeliverable.setForgiven((forgiven == 0) ? false : true);

                // Copy last notification date from the result set to the late deliverable instance:
                lateDeliverable.setLastNotified(resultSet.getTimestamp(index++));
                // Copy delay from the result set to the late deliverable instance:
                lateDeliverable.setDelay((Long) resultSet.getObject(index++, Long.class));
                // Copy explanation from the result set to the late deliverable instance:
                lateDeliverable.setExplanation(resultSet.getString(index++));
                // Copy explanation date from the result set to the late deliverable instance:
                lateDeliverable.setExplanationDate(resultSet.getTimestamp(index++));
                // Copy response from the result set to the late deliverable instance:
                lateDeliverable.setResponse(resultSet.getString(index++));
                // Copy response user from the result set to the late deliverable instance:
                lateDeliverable.setResponseUser(resultSet.getString(index++));
                // Copy response date from the result set to the late deliverable instance:
                lateDeliverable.setResponseDate(resultSet.getTimestamp(index++));

                // Get late deliverable type ID from the result set:
                long lateDeliverableTypeId = resultSet.getLong(index++);
                // Get cached late deliverable type from the map:
                LateDeliverableType lateDeliverableType = lateDeliverableTypes.get(lateDeliverableTypeId);
                if (lateDeliverableType == null) {
                    // Create new late deliverable type instance:
                    lateDeliverableType = new LateDeliverableType();
                    // Set ID to the late deliverable type:
                    lateDeliverableType.setId(lateDeliverableTypeId);
                    // Copy late deliverable type name from the result set to the late deliverable type instance:
                    lateDeliverableType.setName(resultSet.getString(index++));
                    // Copy late deliverable type description from the result set to the late deliverable type
                    // instance:
                    lateDeliverableType.setDescription(resultSet.getString(index));


                    lateDeliverableTypes.put(lateDeliverableTypeId, lateDeliverableType);
                }
                // Set late deliverable type to the late deliverable instance:
                lateDeliverable.setType(lateDeliverableType);

                // Add late deliverable to the list:
                result.add(lateDeliverable);
            }

            return result;
        } catch (NullColumnValueException e) {
            throw new LateDeliverablePersistenceException("Failed to retrieve the late deliverables.", e);
        } catch (InvalidCursorStateException e) {
            throw new LateDeliverablePersistenceException("Failed to retrieve the late deliverables.", e);
        } catch (PersistenceOperationException e) {
            throw new LateDeliverablePersistenceException("Failed to retrieve the late deliverables.", e);
        } catch (SearchBuilderException e) {
            throw new LateDeliverableManagementException("Failed to search late deliverables with the filter.", e);
        } catch (ClassCastException e) {
            throw new LateDeliverableManagementException(
                "The result is invalid.", e);
        }
    }

    /**
     * <p>
     * Gets a search bundle from the SearchBundleManager object.
     * </p>
     *
     * @param searchBundleManager
     *            the SearchBundleManager object.
     * @param configuration
     *            the configuration.
     * @param key
     *            the key to retrieve the name of search bundle from the configuration.
     *
     * @return the search bundle.
     *
     * @throws LateDeliverableManagementConfigurationException
     *             if any error occurs.
     */
    private static SearchBundle getSearchBundle(SearchBundleManager searchBundleManager,
        ConfigurationObject configuration, String key) {
        String name = Helper.getProperty(configuration, key, true);

        SearchBundle searchBundle = searchBundleManager.getSearchBundle(name);

        if (searchBundle == null) {
            throw new LateDeliverableManagementConfigurationException(
                "There is no search bundle found with the name '" + name + "'.");
        }

        return searchBundle;
    }

    /**
     * <p>
     * Gets configuration object from the configuration file.
     * </p>
     *
     * @param filePath
     *            the path of the configuration file.
     * @param namespace
     *            the configuration namespace.
     *
     * @return the configuration object.
     *
     * @throws IllegalArgumentException
     *             if filePath or namespace is null/empty.
     * @throws LateDeliverableManagementConfigurationException
     *             if error occurred while reading the configuration.
     */
    private static ConfigurationObject getConfig(String filePath, String namespace) {
        checkNullOrEmpty(filePath, "filePath");
        checkNullOrEmpty(namespace, "namespace");

        try {
            // Create configuration file manager:
            ConfigurationFileManager manager = new ConfigurationFileManager(filePath);
            // Get configuration object from the manager:
            ConfigurationObject configuration = manager.getConfiguration(namespace);
            // Get configuration of this class:
            return Helper.getChildConfig(configuration, namespace);
        } catch (ConfigurationPersistenceException e) {
            throw new LateDeliverableManagementConfigurationException(
                "An error occurred when loading the configuration file.", e);
        } catch (IOException e) {
            throw new LateDeliverableManagementConfigurationException("An IO error occurred.", e);
        }
    }

    /**
     * <p>
     * Validates the value of a string. The value can not be <code>null</code> or an empty string.
     * </p>
     *
     * @param value
     *            the value of the variable to be validated.
     * @param name
     *            the name of the variable to be validated.
     *
     * @throws IllegalArgumentException
     *             if the given string is <code>null</code> or an empty string.
     */
    private static void checkNullOrEmpty(String value, String name) {
        Helper.checkNull(value, name);

        if (value.trim().length() == 0) {
            throw new IllegalArgumentException("'" + name + "' should not be an empty string.");
        }
    }

    /**
     * <p>
     * Gets the signature for given method for logging.
     * </p>
     *
     * @param method
     *            the method name.
     *
     * @return the signature for given method.
     */
    private static String getSignature(String method) {
        return Helper.concat(CLASS_NAME, Helper.DOT, method);
    }
}
