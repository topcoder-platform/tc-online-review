/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;
import com.topcoder.management.deliverable.persistence.DeliverablePersistence;
import com.topcoder.management.deliverable.persistence.DeliverablePersistenceException;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> The PersistenceDeliverableManager class implements the DeliverableManager interface. It ties together a
 * persistence mechanism and two Search Builder instances (for searching for various types of data). The methods in this
 * class use a SearchBundle to execute a search and then use the persistence to load an object for each of the ids found
 * from the search. </p>
 *
 * <p><strong>Thread Safety:</strong> This class is immutable and hence thread-safe. </p>
 *
 * @author aubergineanode, saarixx, singlewood, TCSDEVELOPER
 * @version 1.1
 */
public class PersistenceDeliverableManager implements DeliverableManager {

    /**
     * <p> The name under which the deliverable search bundle should appear in the SearchBundleManager, if the
     * SearchBundleManager constructor is used. </p>
     */
    public static final String DELIVERABLE_SEARCH_BUNDLE_NAME = "Deliverable Search Bundle";

    /**
     * <p> The name under which the deliverable with submissions search bundle should appear in the SearchBundleManager,
     * if the SearchBundleManager constructor is used. </p>
     */
    public static final String DELIVERABLE_WITH_SUBMISSIONS_SEARCH_BUNDLE_NAME =
            "Deliverable With Submission Search Bundle";

    /**
     * <p> Logger instance using the class name as category. </p>
     *
     * <p>Changes in 1.1: Renamed from Logger.</p>
     */
    private static final Log LOGGER = LogFactory.getLog(PersistenceDeliverableManager.class.getName());

    /**
     * <p> The persistence store for Deliverables. This field is set in the constructor, is immutable, and can never be
     * null. </p>
     */
    private final DeliverablePersistence persistence;

    /**
     * <p> The search bundle that is used when searching for deliverables. This field is set in the constructor, is
     * immutable, and can never be null. </p>
     */
    private final SearchBundle deliverableSearchBundle;

    /**
     * <p> The checkers that are used to determine whether a deliverable is complete or not. This field is set in the
     * constructor and can never be null. </p>
     */
    private final Map deliverableCheckers = new HashMap();

    /**
     * <p> The search bundle that is used when searching for deliverables combined with submissions. This field is set
     * in the constructor, is immutable, and can never be null. </p>
     */
    private final SearchBundle deliverableWithSubmissionsSearchBundle;

    /**
     * <p>Creates a new PersistenceDeliverableManager, initializing all fields to the given values. The
     * deliverableCheckers map is cloned.</p>
     *
     * @param persistence             The persistence for Deliverables
     * @param deliverableCheckers     The checkers to run on the deliverables to see if each is complete. Map from
     *                                String - DeliverableChecker.
     * @param deliverableSearchBundle The search bundle for deliverables
     * @param submissionSearchBundle  The search bundle for deliverables with submissions
     *
     * @throws IllegalArgumentException If any argument is null
     * @throws IllegalArgumentException If deliverableCheckers is not a Map of non-null String - DeliverableChecker
     */
    public PersistenceDeliverableManager(DeliverablePersistence persistence, Map deliverableCheckers,
                                         SearchBundle deliverableSearchBundle, SearchBundle submissionSearchBundle) {
        DeliverableHelper.checkObjectNotNull(persistence, "persistence");
        DeliverableHelper.checkObjectNotNull(deliverableCheckers, "deliverableCheckers");
        DeliverableHelper.checkObjectNotNullFullDesp(deliverableSearchBundle,
                "deliverableSearchBundle is null, or searchBundleManager doesn't contain"
                        + " SearchBundle of required names DELIVERABLE_SEARCH_BUNDLE_NAME");
        DeliverableHelper.checkObjectNotNullFullDesp(submissionSearchBundle,
                "submissionSearchBundle is null, or searchBundleManager doesn't contain"
                        + " SearchBundle of required names DELIVERABLE_WITH_SUBMISSIONS_SEARCH_BUNDLE_NAME");

        // Check if deliverableCheckers is a Map of non-null String - DeliverableChecker
        DeliverableHelper.checkDeliverableCheckers(deliverableCheckers);

        // Set the searchable fields of SearchBundle.
        DeliverableHelper.setSearchableFields(deliverableSearchBundle,
                DeliverableHelper.DELIVERABLE_SEARCH_BUNDLE);
        DeliverableHelper.setSearchableFields(submissionSearchBundle,
                DeliverableHelper.DELIVERABLE_WITH_SUBMISSIONS_SEARCH_BUNDLE);

        LOGGER.log(Level.INFO,
                "Instantiate PersistenceDeliverableManager with DeliverablePersistence, deliverableCheckers map,"
                        + "deliverableSearchBundle and submissionSearchBundle.");
        this.persistence = persistence;
        this.deliverableCheckers.putAll(deliverableCheckers);
        this.deliverableSearchBundle = deliverableSearchBundle;
        this.deliverableWithSubmissionsSearchBundle = submissionSearchBundle;
    }

    /**
     * <p>Creates a new PersistenceDeliverableManager.</p>
     *
     * @param persistence         The persistence for Deliverables
     * @param deliverableCheckers The checkers to run on the deliverables to see if each is complete. Map from String -
     *                            DeliverableChecker.
     * @param searchBundleManager The manager containing the various SearchBundles needed
     *
     * @throws IllegalArgumentException If any argument is null
     * @throws IllegalArgumentException If any search bundle is not available under the required names
     * @throws IllegalArgumentException If deliverableCheckers is not a Map of non-null String - DeliverableChecker
     */
    public PersistenceDeliverableManager(DeliverablePersistence persistence, Map deliverableCheckers,
                                         SearchBundleManager searchBundleManager) {
        // Check if searchBundleManager is null, if not, get the SearchBundles, then delegate to
        // the first constructor.
        this(persistence,
                deliverableCheckers,
                (DeliverableHelper.checkObjectNotNull(searchBundleManager, "searchBundleManager"))
                        ? searchBundleManager.getSearchBundle(DELIVERABLE_SEARCH_BUNDLE_NAME)
                        : null,
                searchBundleManager.getSearchBundle(DELIVERABLE_WITH_SUBMISSIONS_SEARCH_BUNDLE_NAME));
    }

    /**
     * <p>Searches the deliverables in the persistence store using the given filter. The filter can be formed using the
     * field names and utility methods in DeliverableFilterBuilder. The return will always be a non-null (possibly 0
     * item) array. This method is designed to be used with submission id filters, and returns only deliverables that
     * are "per submission".</p>
     *
     * @param filter   The filter to use
     * @param complete True to include only those deliverables that have been completed, false to include only those
     *                 deliverables that are not complete, null to include both complete and incomplete deliverables
     *
     * @return The Deliverables meeting the filter and complete conditions
     *
     * @throws IllegalArgumentException     If filter is null
     * @throws DeliverablePersistenceException
     *                                      If there is an error reading the persistence
     * @throws SearchBuilderException       If there is an error executing the filter
     * @throws SearchBuilderConfigurationException
     *                                      If the manager is not properly configured with a correct SearchBundle for
     *                                      searching
     * @throws DeliverableCheckingException If there is an error when determining whether a Deliverable has been
     *                                      completed or not
     */
    public Deliverable[] searchDeliverablesWithSubmissionFilter(Filter filter, Boolean complete)
            throws DeliverablePersistenceException, SearchBuilderException, DeliverableCheckingException {
        // Delegate to searchDeliverablesHelper. 4 indicates that there are four columns in the
        // CustomResultSet.
        LOGGER.log(Level.INFO, "search deliverables with submission filter.");
        return searchDeliverablesHelper(filter, complete, 4);
    }

    /**
     * <p>Searches the deliverables in the persistence store using the given filter. The filter can be formed using the
     * field names and utility methods in DeliverableFilterBuilder. The return will always be a non-null (possibly 0
     * item) array. This method should not be used with submission id filters.</p>
     *
     * @param filter   The filter to use
     * @param complete True to include only those deliverables that have been completed, false to include only those
     *                 deliverables that are not complete, null to include both complete and incomplete deliverables
     *
     * @return The Deliverables meeting the Filter and complete conditions
     *
     * @throws IllegalArgumentException     If filter is null
     * @throws DeliverablePersistenceException
     *                                      If there is an error reading the persistence store
     * @throws SearchBuilderException       If there is an error executing the filter
     * @throws SearchBuilderConfigurationException
     *                                      If the manager is not properly configured with a correct SearchBundle for
     *                                      searching
     * @throws DeliverableCheckingException If there is an error when determining whether a Deliverable has been
     *                                      completed or not
     */
    public Deliverable[] searchDeliverables(Filter filter, Boolean complete)
            throws DeliverablePersistenceException, SearchBuilderException, DeliverableCheckingException {
        // Delegate to searchDeliverablesHelper. 3 indicates that there are three columns in the
        // CustomResultSet.
        LOGGER.log(Level.INFO, "Search deliverables filter.");
        return searchDeliverablesHelper(filter, complete, 3);
    }

    /**
     * <p>Help method for methods: searchDeliverables and searchDeliverablesWithSubmissionFilter. Searches the
     * deliverables in the persistence store using the given filter. The filter can be formed using the field names and
     * utility methods in DeliverableFilterBuilder. The return will always be a non-null (possibly 0 item) array. The
     * String operation is used for distinguish call from two methods.</p>
     *
     * @param filter    The filter to use
     * @param complete  True to include only those deliverables that have been completed, false to include only those
     *                  deliverables that are not complete, null to include both complete and incomplete deliverables
     * @param operation the identifier for the caller, 3 is searchDeliverables, 4 is
     *                  searchDeliverablesWithSubmissionFilter
     *
     * @return The Deliverables meeting the Filter and complete conditions
     *
     * @throws DeliverablePersistenceException
     *                                      If there is an error reading the persistence store
     * @throws SearchBuilderException       If there is an error executing the filter
     * @throws SearchBuilderConfigurationException
     *                                      If the manager is not properly configured with a correct SearchBundle for
     *                                      searching
     * @throws DeliverableCheckingException If there is an error when determining whether a Deliverable has been
     *                                      completed or not
     */
    private Deliverable[] searchDeliverablesHelper(Filter filter, Boolean complete, int operation)
            throws SearchBuilderException, DeliverablePersistenceException, DeliverableCheckingException {
        DeliverableHelper.checkObjectNotNull(filter, "filter");

        // Get object with given filter from corresponding search bundle.
        Object obj;
        if (operation == 3) {
            obj = deliverableSearchBundle.search(filter);
        } else {
            obj = deliverableWithSubmissionsSearchBundle.search(filter);
        }

        // Check if the return object is a CustomResultSet, and it's record count is correct too.
        // And retrieve long[][] from correct CustomResultSet.
        long[][] array = DeliverableHelper.checkAndGetCustomResultSetValidDeliverable(obj, operation);

        // Create a List for temporary storage.
        List list = new ArrayList();

        LOGGER.log(Level.INFO, "Deliverable Id Array: " + array.length + ", " + array[0].length);

        // Get Deliverable[] from persistence layer.
        Deliverable[] deliverableArray;
        if (operation == 3) {
            deliverableArray = persistence.loadDeliverables(array[0], array[1], array[2]);
        } else {
            deliverableArray = persistence.loadDeliverables(array[0], array[1], array[2], array[3]);
        }

        // For each deliverable in deliverableArray
        for (int i = 0, n = deliverableArray.length; i < n; i++) {

            LOGGER.log(Level.INFO, "Loading Deliverable Array");

            Deliverable deliverable = deliverableArray[i];
            deliverable.setCompletionDate(new Date());

            // Get the name of deliverable. This can not be null, if it is, throw a
            // DeliverablePersistenceException.
            String name = deliverable.getName();
            DeliverableHelper.checkObjectNotNullFullDesp(name, "name in the deliverable can't be null.");

            // Look up the deliverable checker with the name.
            DeliverableChecker deliverableChecker = (DeliverableChecker) deliverableCheckers.get(name);

            // If DeliverableChecker is found for the name, run the retrieved DeliverableChecker
            // on the deliverable. Otherwise do not check the deliverable.
            if (deliverableChecker != null) {
                deliverableChecker.check(deliverable);
            }

            // If the isCompleted property of the deliverable matches the complete argument
            // (when complete is not null), then add the deliverable to the return array.
            // When complete is null, the deliverable is always added to the return array.
            if ((complete == null) || (complete.booleanValue() == deliverable.isComplete())) {

                LOGGER.log(Level.INFO, "Adding Deliverable To List");
                list.add(deliverable);
            }
        }

        // Convert List to Deliverable[]
        return (Deliverable[]) list.toArray(new Deliverable[deliverableArray.length]);
    }
}
