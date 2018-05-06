/*
 * Copyright (C) 2010, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * This interface represents a late deliverables retriever. It provides a single method for retrieving all late
 * deliverables of the specified types. This interface extends Configurable interface to support configuration via
 * Configuration API component.
 * </p>
 *
 * <p>
 * <em>Changes in version 1.3:</em>
 * <ol>
 * <li>Supporting retrieval of late deliverables of requested types only.</li>
 * </ol>
 * </p>
 *
 * <p>
 * Thread Safety: Implementations of this interface are not required to be thread safe.
 * </p>
 *
 * @author saarixx, myxgyy, sparemax
 * @version 1.3
 */
public interface LateDeliverablesRetriever extends Configurable {
    /**
     * <p>
     * Retrieves information about late deliverables of the requested types. Returns an empty list if no late
     * deliverables are found.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.3:</em>
     * <ol>
     * <li>Added parameter "types".</li>
     * </ol>
     * </p>
     *
     * @param types
     *            the types of late deliverables to be retrieved (null if all late deliverables of all supported types
     *            must be retrieved).
     *
     * @return the list with information about found late deliverables (not <code>null</code>, doesn't contain
     *         <code>null</code>).
     *
     * @throws IllegalArgumentException
     *             if types is empty or contains null.
     * @throws LateDeliverablesRetrievalException
     *             if some error occurred when retrieving a list of late deliverables.
     */
    public List<LateDeliverable> retrieve(Set<LateDeliverableType> types) throws LateDeliverablesRetrievalException;
}
