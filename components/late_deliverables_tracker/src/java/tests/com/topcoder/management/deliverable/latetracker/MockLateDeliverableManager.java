/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import java.util.List;

import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableManagementException;
import com.topcoder.management.deliverable.late.LateDeliverableManager;
import com.topcoder.management.deliverable.late.LateDeliverableType;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * A mock implementation of <code>LateDeliverableManager</code>. Used for testing.
 * </p>
 *
 * <p>
 * <em>Changes in version 1.3:</em>
 * <ol>
 * <li>Added getLateDeliverableTypes() method.</li>
 * </ol>
 * </p>
 *
 * @author sparemax
 * @version 1.3
 * @since 1.2
 */
public class MockLateDeliverableManager implements LateDeliverableManager {
    /**
     * <p>
     * Constructs a new <code>MockLateDeliverableManager</code> instance.
     * </p>
     */
    public MockLateDeliverableManager() {
        // Empty
    }

    /**
     * <p>
     * Updates the given late deliverable.
     * </p>
     *
     * @param lateDeliverable
     *            the late deliverable with updated data.
     */
    public void update(LateDeliverable lateDeliverable) {
        // Empty
    }

    /**
     * <p>
     * Retrieves the late deliverable with the given ID.
     * </p>
     *
     *
     * @param lateDeliverableId
     *            the ID of the late deliverable to be retrieved.
     *
     * @return null.
     *
     */
    public LateDeliverable retrieve(long lateDeliverableId) {
        return null;
    }

    /**
     * <p>
     * Searches for all late deliverables that are matched with the given filter. Returns an empty list if none found.
     * </p>
     *
     * @param filter
     *            the filter for late deliverables (null if all late deliverables need to be retrieved).
     *
     * @return nothing.
     *
     * @throws LateDeliverableManagementException
     *             if some other error occurred.
     */
    public List<LateDeliverable> searchAllLateDeliverables(Filter filter) throws LateDeliverableManagementException {
        throw new LateDeliverableManagementException("LateDeliverableManagementException for testing.");

    }

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
     * @return null.
     */
    public List<LateDeliverable> searchRestrictedLateDeliverables(Filter filter, long userId) {
        return null;
    }

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
     * @since 1.3
     */
    public List<LateDeliverableType> getLateDeliverableTypes() throws LateDeliverableManagementException {
        return null;
    }
}
