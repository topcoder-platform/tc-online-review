/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

/**
 * Interface that abstracts an analyzer for the memory usage of an object. It exposes a method to detect whether the
 * implementation applies to the running JVM and to methods for analyzing the memory usage of an object (including or
 * not embedded objects recursively).
 *
 * @author BryanChen
 * @author AleaActaEst, TexWiller
 * @version 2.0
 */
public interface MemoryUsageAnalyzer {
    /**
     * Detects if this implementation applies to the running JVM.
     *
     * @return <code>true</code> if this implementation applies to the running JVM;
     * <code>false</code> otherwise
     */
    public boolean matchesJVM();

    /**
     * Retrieve the shallow memory usage for an object (not including embedded
     * objects memory usage, but counting the memory consumed by the object to keep
     * references to the embedded objects).
     *
     * @param obj The object to get the memory usage for; can be <code>null</code>
     * @return The shallow memory usage result of the specified object
     * @throws MemoryUsageException If exceptions occurred while traversing the object; typically,
     * security-related exceptions can be raised
     */
    public MemoryUsageResult getShallowMemoryUsage(Object obj) throws MemoryUsageException;

    /**
     * Get the deep memory usage for an object (including embedded objects memory
     * usage recursively). The user can specify a MemoryUsageListener in order to
     * prevent some of the embedded objects to be analyzed. The object can be <code>null</code>.
     *
     * @param obj The object to get the memory usage for; can be <code>null</code>
     * @param listener The MemoryUsageListener to specify which objects should
     * be included in the memory usage sum, and which should not. Can be <code>null</code>,
     * meaning that all the objects will be included.
     * @return The deep memory usage result of the specified object
     * @throws MemoryUsageException If exceptions occurred while traversing the object; typically,
     * security-related exceptions can be raised
     */
    public MemoryUsageResult getDeepMemoryUsage(Object obj, MemoryUsageListener listener)
        throws MemoryUsageException;
}

