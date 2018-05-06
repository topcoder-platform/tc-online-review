/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late;

import java.util.List;

import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * This interface represents a late deliverable manager. It defines methods for updating/retrieving late deliverable
 * and searching for late deliverables that are matched with the given filter and optionally are restricted to the
 * specified user. Additionally it defines a method for retrieving all late deliverable types.
 * </p>
 *
 * <p>
 * <em>Changes in version 1.0.6:</em>
 * <ol>
 * <li>Added getLateDeliverableTypes() method.</li>
 * <li>Updated throws documentation of update() method.</li>
 * <li>Updated class documentation.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> Implementations of this interface are required to be thread safe when entities
 * passed to them are used by the caller in thread safe manner.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0.6
 */
public interface LateDeliverableManager {
    /**
     * <p>
     * Updates the given late deliverable.
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
     *             lateDeliverable.getCreateDate() is nulll, lateDeliverable.getType() is null,
     *             lateDeliverable.getType().getId() &lt;= 0.
     * @throws LateDeliverableManagementException
     *             if some other error occurred.
     */
    public void update(LateDeliverable lateDeliverable) throws LateDeliverableManagementException;

    /**
     * <p>
     * Retrieves the late deliverable with the given ID.
     * </p>
     *
     *
     * @param lateDeliverableId
     *            the ID of the late deliverable to be retrieved.
     *
     * @return the retrieved late deliverable with the given ID (or null if late deliverable with the given ID doesn't
     *         exist).
     *
     * @throws IllegalArgumentException
     *             if lateDeliverableId &lt;= 0.
     * @throws LateDeliverableManagementException
     *             if some other error occurred.
     */
    public LateDeliverable retrieve(long lateDeliverableId) throws LateDeliverableManagementException;

    /**
     * <p>
     * Searches for all late deliverables that are matched with the given filter. Returns an empty list if none found.
     * </p>
     *
     * @param filter
     *            the filter for late deliverables (null if all late deliverables need to be retrieved).
     *
     * @return the list with found late deliverables that are matched with the given filter (not null, doesn't contain
     *         null).
     *
     * @throws LateDeliverableManagementException
     *             if some other error occurred.
     */
    public List<LateDeliverable> searchAllLateDeliverables(Filter filter) throws LateDeliverableManagementException;

    /**
     * <p>
     * Searches for all late deliverables that are matched with the given filter checking whether the user with the
     * specified ID has owner, manager or cockpit project access to the deliverables. Returns an empty list if none
     * found.
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
     * @throws LateDeliverableManagementException
     *             if some other error occurred.
     */
    public List<LateDeliverable> searchRestrictedLateDeliverables(Filter filter, long userId)
        throws LateDeliverableManagementException;

    /**
     * <p>
     * Retrieves all existing late deliverable types.
     * </p>
     *
     * @return the retrieved late deliverable types (not null, doesn't contain null).
     *
     * @throws LateDeliverableManagementException
     *             if some error occurred.
     *
     * @since 1.0.6
     */
    public List<LateDeliverableType> getLateDeliverableTypes() throws LateDeliverableManagementException;
}
