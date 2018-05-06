/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

/**
 * Listener for events generated during the object graph traversal. When an object is reached then the objectReached
 * event is called to know whether that object should be processed also or the traversal should not go into its
 * embedded objects. The purpose is to achieve a customizable limitation of the graph traversal (especially when the
 * graph is very large).
 *
 * @see MemoryUsage#getDeepMemoryUsage(Object, MemoryUsageListener)
 *
 * @author BryanChen
 * @author AleaActaEst, TexWiller
 * @version 2.0
 */
public interface MemoryUsageListener {
    /**
     * Event called when a new object is reached.
     *
     * @param obj The reached object
     *
     * @return <code>true</code> if the traversal should go into the embedded
     * objects of the object, <code>false</code> if not
     */
    public boolean objectReached(Object obj);
}
