/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 *
 * TCS Memory Usage Version 2.0 Accuracytests.
 *
 * @ MockListener.java
 */
package com.topcoder.util.memoryusage.accuracytests;

import com.topcoder.util.memoryusage.MemoryUsageListener;


/**
 * <p>
 * The mock class used for accuracy test.
 * It is extending from <code>MemoryUsageListener</code>.
 * </p>
 *
 * @author zmg
 * @version 2.0
 */
public class MockListener implements MemoryUsageListener {
    /**
     * <p>
     * Base constructor.
     * </p>
     */
    public MockListener() {
        // do nothing.
    }

    /**
     * <p>
     * Event called when a new object is reached.
     * Here simply return true.
     * </p>
     *
     * @param obj the reached object.
     *
     * @return true if the traversal should go into the embedded objects of the object, false otherwise.
     */
    public boolean objectReached(Object obj) {
        return true;
    }
}
