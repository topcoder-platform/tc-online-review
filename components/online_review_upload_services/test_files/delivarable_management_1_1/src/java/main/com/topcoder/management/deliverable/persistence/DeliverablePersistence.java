/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.persistence;

import com.topcoder.management.deliverable.Deliverable;

/**
 * <p> The DeliverablePersistence interface defines the methods for persisting and retrieving the object model in this
 * component. This interface handles the persistence of the deliverable related classes that make up the object model,
 * this consists only of the Deliverable class. Unlike UploadPersistence, the DeliverablePersistence interface
 * (currently) has no support for adding items. Only retrieval is supported. This interface is not responsible for
 * searching the persistence for the various entities. This is instead handled by a DeliverableManager implementation.
 * </p>
 *
 * <p><strong>Thread Safety:</strong> Implementations of this interface are not required to be thread-safe or
 * immutable.</p>
 *
 * @author aubergineanode, singlewood
 * @version 1.1
 */
public interface DeliverablePersistence {

    /**
     * <p>Loads the deliverables associated with the given deliverable id and resource id. There may be more than one
     * deliverable returned if the deliverable is a "per submission" deliverable. Hence the need for an array return
     * type. If there is no matching deliverable in the persistence, an empty array should be returned.</p>
     *
     * @param deliverableId The id of the deliverable
     * @param resourceId    The resource id of deliverable
     * @param phaseId       The phase id of the deliverable
     *
     * @return The matching deliverable (possibly expanded by matching with each active submission, if it is a "per
     *         submission" deliverable), or an empty array
     *
     * @throws IllegalArgumentException If deliverableId is <= 0 or resourceId <=0
     * @throws DeliverablePersistenceException
     *                                  If there is an error reading the persistence
     */
    Deliverable[] loadDeliverables(long deliverableId, long resourceId, long phaseId)
            throws DeliverablePersistenceException;

    /**
     * <p>Loads the deliverable associated with the given submission and resource. The deliverable must be a "per
     * submission" deliverable and the given submission must be "Active". If this is not the case, null is
     * returned.</p>
     *
     * @param deliverableId The id of the deliverable
     * @param resourceId    The resource id of deliverable
     * @param phaseId       The phase id of the deliverable
     * @param submissionId  The id of the submission the deliverable should be associated with
     *
     * @return The deliverable, or null if there is no deliverable for the given id, or submission is not an 'Active'
     *         submission
     *
     * @throws IllegalArgumentException If deliverableId, resourceId or submissionId is <= 0
     * @throws DeliverablePersistenceException
     *                                  If there is an error reading the persistence data
     */
    Deliverable loadDeliverable(long deliverableId, long resourceId, long phaseId, long submissionId)
            throws DeliverablePersistenceException;

    /**
     * <p>Loads all Deliverables with the given ids and resource ids from persistence. May return a 0-length array.</p>
     *
     * @param deliverableIds The ids of deliverables to load
     * @param resourceIds    The resource ids of deliverables to load
     * @param phaseIds       The phase ids of the deliverables to load
     *
     * @return The loaded deliverables
     *
     * @throws IllegalArgumentException if any id is <= 0
     * @throws DeliverablePersistenceException
     *                                  if there is an error reading the persistence data
     */
    Deliverable[] loadDeliverables(long[] deliverableIds, long[] resourceIds, long[] phaseIds)
            throws DeliverablePersistenceException;

    /**
     * <p>Loads per-submission deliverables that have the given deliverable id, resource ids and submission id. The two
     * input arrays must have the same length. Only "per-submission" deliverables will be returned.</p>
     *
     * @param deliverableIds The ids of deliverables to load
     * @param resourceIds    The resource ids of deliverables to load
     * @param phaseIds       The phase ids of the deliverables to load
     * @param submissionIds  The ids of the submission for each deliverable
     *
     * @return The loaded deliverables
     *
     * @throws IllegalArgumentException If the three arguments do not have the same number of elements
     * @throws IllegalArgumentException if any id (in either array) is <= 0
     * @throws DeliverablePersistenceException
     *                                  if there is an error reading the persistence data
     */
    Deliverable[] loadDeliverables(long[] deliverableIds, long[] resourceIds, long[] phaseIds, long[] submissionIds)
            throws DeliverablePersistenceException;
}
