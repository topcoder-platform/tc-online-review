/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.memoryusage;


/**
 * <p>
 * A mock implementation of <code>BaseAnalyzer</code> for failure tests.
 * </p>
 * <p>
 * Thread-Safety: Thread-safe since it has no state.
 * </p>
 *
 * @author Thinfox
 * @version 2.0
 */
public class FailureTestAnalyzer extends BaseAnalyzer {

    /**
     * <p>
     * Creates a new instance of this analyzer.
     * </p>
     */
    public FailureTestAnalyzer() {
        super("", "");
    }

    /**
     * <p>
     * Detects if this implementation applies to the running JVM.
     * </p>
     *
     * @return if this implementation applies to the running JVM
     */
    public boolean matchesJVM() {
        return false;
    }

    /**
     * <p>
     * Analyze memory usage for an object (with or without the embedded objects).
     * </p>
     *
     * @param obj the object to get the memory usage for
     * @param goDeep are embedded objects considered
     * @param listener the memory usage listener
     * @return the memory usage result
     * @throws MemoryUsageException if there were any issues
     */
    public MemoryUsageResult analyze(Object obj, boolean goDeep, MemoryUsageListener listener)
        throws MemoryUsageException {
        return super.analyze(obj, goDeep, listener);
    }

    /**
     * <p>
     * Get the size of a class field of the given class type.
     * </p>
     *
     * @param cls the type of the field
     * @return the size in bytes
     * @throws IllegalArgumentException if the object is null
     */
    public int getFieldSize(Class cls) {
        return super.getFieldSize(cls);
    }

    /**
     * <p>
     * Get the size of an array (shallow).
     * </p>
     *
     * @param arobj the array object
     * @return the size in bytes
     * @throws IllegalArgumentException if the object is not an array or if it is null
     */
    public int getArraySize(Object arobj) {
        return super.getArraySize(arobj);
    }
}
