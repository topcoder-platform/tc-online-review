/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

/**
 * This class provides detailed memory usage for the instances of a specific class (including primitive types and
 * arrays). Provides methods for getting the class represented by the memory usage detail, the number of instances of
 * that class and the memory occupied by all the instances together.  Note, that this class does not do any
 * memory analysis of its own.  It basically holds calculations obtained in the MemoryUsage classes.
 * Instances of this class will be created by {@link MemoryUsageAnalyzer} implementations.
 *
 * <p><b>Thread safety:</b> This class is thread safe. The only method able to modify its
 * contents is the <code>cumulate</code> method, which is thread safe.
 *
 * @author BryanChen
 * @author AleaActaEst, TexWiller
 * @version 2.0
 * @see MemoryUsageAnalyzer#getShallowMemoryUsage(Object)
 * @see MemoryUsageAnalyzer#getDeepMemoryUsage(Object, MemoryUsageListener)
 */
public class MemoryUsageDetail {
    /** The class represented by the memory usage detail. */
    private final Class cls;

    /** The memory in bytes used by all the instances of the class, together. */
    private long usedMemory = 0L;

    /** The instance count. */
    private int objectCount = 0;

    /**
     * Creates a new, empty MemoryUsageDetail, based upon a specific Class.
     *
     * @param cls The class this MemoryUsageDetail contains details about
     *
     * @throws IllegalArgumentException If argument is <code>null</code>
     */
    public MemoryUsageDetail(Class cls) {
        MemoryUsageHelper.checkObjectNotNull(cls, "cls");
        this.cls = cls;
    }

    /**
     * Get the class represented by this memory usage detail.
     *
     * @return The non-<code>null</code> Class this MemoryUsageDetail contains details about
     */
    public Class getDetailClass() {
        return cls;
    }

    /**
     * Get the memory in bytes used by all the instances of the class, together.
     *
     * @return The total used memory in bytes; will be >= 0
     */
    public long getUsedMemory() {
        return usedMemory;
    }

    /**
     * Get the total number of object instances of the contained class found
     * with memory usage analysis.
     *
     * @return The instance count; will be >= 0
     */
    public int getObjectCount() {
        return objectCount;
    }

    /**
     * Cumulate values. Called by the analyzers to add new data to the
     * global count.
     *
     * @param count The instance count to add to the total
     * @param usedMemory The memory used by all the new instances
     * @throws IllegalArgumentException If either of the two parameters is <= 0
     * @since 2.0
     */
    public void cumulate(int count, long usedMemory) {
        MemoryUsageHelper.checkValuePositive(count, "count");
        MemoryUsageHelper.checkValuePositive(usedMemory, "usedMemory");

        this.objectCount += count;
        this.usedMemory += usedMemory;
    }
}
