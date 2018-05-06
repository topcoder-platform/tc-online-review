/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

/**
 * This is a contract for a utility which deals with memory utilization by a given object. This means that
 * this interface deals with the definition for a memory utilization discovery (i.e. how much space, in bytes,
 * does an object take up in memory).
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public interface MemoryUtilizationHandler {

    /**
     * Calculates the deep memory utilization of input object. This function can never return a size < 0.
     *
     * @param  object object that we want to find out the size for.
     * @return size of the input object in bytes.
     * @throws NullPointerException if the argument object is null.
     * @throws MemoryUtilizationException if there are issues with processing the request.
     */
    long getObjectSize(Object object);
}
