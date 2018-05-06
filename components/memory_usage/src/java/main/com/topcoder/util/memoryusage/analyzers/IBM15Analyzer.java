/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage.analyzers;

import com.topcoder.util.memoryusage.BaseAnalyzer;
import com.topcoder.util.memoryusage.MemoryUsageHelper;

/**
 * <p>
 * Memory usage analyzer implementation for the IBM JVMs 1.5.
 * Compared to the base memory model, the 1.5 memory model does not pad each
 * field to 4 bytes, but uses its native size. Moreover, the object base size
 * is 16 bytes instead of 8.
 * </p>
 * <p><b>Thread safety:</b> This class is thread safe since it has no state. </p>
 *
 * @author AleaActaEst, TexWiller
 * @version 2.0
 * @since 2.0
 */
public class IBM15Analyzer extends BaseAnalyzer {

    /**
     * The object base value for this JVM.
     */
    private static final int OBJECT_BASE = 16;

    /**
     * Creates a new instance of this analyzer.
     */
    public IBM15Analyzer() {
        super("IBM Corporation", "JDK 1.5.x");
    }

    /**
     * Detects the running JVM is the IBM 1.5 JVM.
     *
     * @return <code>true</code> if the running JVM is the IBM 1.5 JVM; <code>false</code>otherwise.
     */
    public boolean matchesJVM() {
        return MemoryUsageHelper.checkVendorVersion("IBM Corporation", "49.");
    }

    /**
     * Get the size of a class field of the given class type. In IBM JVM 1.5,
     * this is the actual size of the fields, instead of its padding to 4 bytes.
     *
     * @param cls The type of the field
     * @return Size occupied by the field, in bytes
     * @throws IllegalArgumentException If the <i>cls</i> parameter is <code>null</code>
     */
    public int getFieldSize(Class cls) {
        return MemoryUsageHelper.getFieldSize(cls);
    }

    /**
     * Get the overhead for any object. In IBM JVM 1.5, this
     * value is 16 bytes instead of 8.
     *
     * @return The object overhead
     */
    public int getObjectBase() {
        return OBJECT_BASE;
    }
}
