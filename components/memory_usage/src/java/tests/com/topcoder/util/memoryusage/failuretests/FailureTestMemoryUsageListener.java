/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.memoryusage.failuretests;

import com.topcoder.util.memoryusage.MemoryUsageListener;

/**
 * <p>
 * A mock implementation of <code>MemoryUsageListener</code> for failure tests.
 * </p>
 * <p>
 * Thread-Safety: Thread-safe since it has no state.
 * </p>
 *
 * @author Thinfox
 * @version 2.0
 */
class FailureTestMemoryUsageListener implements MemoryUsageListener {

    /**
     * Constructor.
     */
    public FailureTestMemoryUsageListener() {
    }

    /**
     * This method will be called when a new object is reached.
     *
     * @param obj the reached object
     * @return always true
     */
    public boolean objectReached(Object obj) {
        return true;
    }
}
