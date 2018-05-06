/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import java.util.Collections;
import java.util.Map;

/**
 * <p>This class provides the results for the memory usage of an object. It exposes getters for the used memory in bytes
 * and the object count. It also gives the possibility to obtain memory usage details for objects of a particular
 * class.  Note, that this class does not do any memory analysis of its own.  It basically holds calculations
 * obtained in the MemoryUsage classes.</p>
 * <p><b>Thread safety:</b> Once the result is calculated, this class is thread safe since it should be
 * read-only. However, the <code>cummulate()</code> method is not thread safe: multiple threads
 * accessing it should be properly synchronized.</p>
 *
 * @author BryanChen
 * @author AleaActaEst, TexWiller
 * @version 1.0
 */
public class MemoryUsageResult {
    /** The used memory in bytes. */
    private long usedMemory = 0L;

    /** The object count. */
    private int objectCount = 0;

    /**
     * This map holds the MemoryUsageDetail objects
     * for each class encountered during the build-up of this object.
     */
    private Map classToDetailMap = Collections.EMPTY_MAP;

    /**
     * Constructor. The initial state contains no objects and no details.
     */
    public MemoryUsageResult() {
    }

    /**
     * Get the used memory in bytes.
     *
     * @return The used memory in bytes
     */
    public long getUsedMemory() {
        return usedMemory;
    }

    /**
     * Get the object count (if the analysis was shallow then it will return one,
     * that is only the root object).
     *
     * @return The object count
     */
    public int getObjectCount() {
        return objectCount;
    }

    /**
     * Get the MemoryUsageDetail for every found class.
     *
     * @return An array with all the memory usage details
     */
    public MemoryUsageDetail[] getDetails() {
        return (MemoryUsageDetail[]) classToDetailMap.values().toArray(new MemoryUsageDetail[0]);
    }

    /**
     * Get the memory usage detail for a given class. If there were no instances of the
     * given class then <code>null</code> is returned.
     *
     * @param cls The class to get details for
     *
     * @return The MemoryUsageDetail for the given class, or <code>null</code> if no
     * instances of the class were found during memory analysis
     *
     * @throws IllegalArgumentException If the <i>cls</i> argument is <code>null</code>
     */
    public MemoryUsageDetail getDetail(Class cls) {
        MemoryUsageHelper.checkObjectNotNull(cls, "cls");
        return (MemoryUsageDetail) classToDetailMap.get(cls);
    }


    /**
     * Adds to the memory usage counts. Used during traversal of the objects.
     * @param count The number of new objects visited
     * @param usedMemory The total memory used by all the newly visited objects
     * @throws IllegalArgumentException If either of the two parameters is <= 0
     * @since 2.0
     */
    public void cumulate(int count, long usedMemory) {
        MemoryUsageHelper.checkValuePositive(count, "count");
        MemoryUsageHelper.checkValuePositive(usedMemory, "usedMemory");

        this.objectCount += count;
        this.usedMemory += usedMemory;
    }

    /**
     * Sets the Map from Class to MemoryUsageDetail which
     * holds the details of the memory usage.
     * @param map The new Class -> MemoryUsageDetail map which holds the
     * details of memory usage
     * @throws IllegalArgumentException If the <i>map</i> parameter is <code>null</code>
     */
    public void setDetails(Map map) {
        MemoryUsageHelper.checkObjectNotNull(map, "map");
        this.classToDetailMap = map;
    }
}
