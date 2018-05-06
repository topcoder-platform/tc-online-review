/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import com.topcoder.util.memoryusage.MemoryUsage;
import com.topcoder.util.memoryusage.MemoryUsageResult;

/**
 * This is a default implementation of the MemoryUtilizationHandler interface. This implementation
 * delegates the memory utilization processing to the TopCoder Memory Usage component.
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public class SimpleMemoryUtilizationHandler implements MemoryUtilizationHandler {

    /**
     * Calculates a deep memory usage for the input object in bytes of memory.
     *
     * @param  object object that we would like to know the size of.
     * @return the size of the object in bytes.
     * @throws NullPointerException if the argument object is null.
     * @throws MemoryUtilizationException if there are issues with processing the request. Any exception caught
     *         from MemoryUsage.getDeepMemoryUsage will be wrapped into this exception.
     */
    public long getObjectSize(Object object) {
        // check that argument is not null.
        if (object == null) {
            throw new NullPointerException("'object' can not be null.");
        }

        // use MemoryUsage to get deep memory usage. Any exception caught will be wrapped into
        // MemoryUtilizationException.
        long result = 0;
        try {
            MemoryUsage memoryUsage = new MemoryUsage();
            MemoryUsageResult mur = memoryUsage.getDeepMemoryUsage(object);
            result = mur.getUsedMemory();
        } catch (Exception ex) {
            throw new MemoryUtilizationException("MemoryUsage has thrown exception", ex);
        }
        return result;
    }
}
