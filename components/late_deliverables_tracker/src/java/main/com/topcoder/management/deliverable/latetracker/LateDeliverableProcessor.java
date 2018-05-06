/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

/**
 * <p>
 * This interface represents a late deliverable processor. It provides a method for
 * processing a single late deliverables. The actual actions to be performed depend on the
 * implementation. This interface extends Configurable interface to support configuration
 * via Configuration API component.
 * </p>
 * <p>
 * Thread Safety: Implementations of this interface are not required to be thread safe.
 * </p>
 *
 * @author saarixx, myxgyy
 * @version 1.0
 */
public interface LateDeliverableProcessor extends Configurable {
    /**
     * Processes the given late deliverable. The actual actions to be performed depend on
     * the implementation.
     *
     * @param lateDeliverable
     *            the late deliverable to be processed
     * @throws IllegalArgumentException
     *             if <code>lateDeliverable</code> is <code>null</code>.
     * @throws LateDeliverablesProcessingException
     *             if some error occurred when processing a late deliverable.
     */
    public void processLateDeliverable(LateDeliverable lateDeliverable)
        throws LateDeliverablesProcessingException;
}
