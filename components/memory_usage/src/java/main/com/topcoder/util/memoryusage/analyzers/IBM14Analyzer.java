/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage.analyzers;

import com.topcoder.util.memoryusage.MemoryUsageHelper;
import com.topcoder.util.memoryusage.BaseAnalyzer;

/**
 * <p>
 * Memory usage analyzer implementation for the IBM JVMs 1.4.
 * Compared to the base memory model, the 1.4 memory model does not pad each
 * field to 4 bytes, but uses its native size.
 * </p>
 * <p><b>Thread safety:</b> This class is thread safe since it has no state. </p>
 *
 * @author AleaActaEst, TexWiller
 * @version 2.0
 * @since 2.0
 */
public class IBM14Analyzer extends BaseAnalyzer {

    /**
     * Creates a new instance of this analyzer.
     */
    public IBM14Analyzer() {
        super("IBM Corporation", "JDK 1.4.x");
    }

    /**
     * Detects the running JVM is the IBM 1.4 JVM.
     *
     * @return <code>true</code> if the running JVM is the IBM 1.4 JVM; <code>false</code>otherwise.
     */
    public boolean matchesJVM() {
        return MemoryUsageHelper.checkVendorVersion("IBM Corporation", "48.");
    }

    /**
     * Get the size of a class field of the given class type. In IBM JVM 1.4,
     * this is the actual size of the fields, instead of its padding to 4 bytes.
     *
     * @param cls The type of the field
     * @return Size occupied by the field, in bytes
     * @throws IllegalArgumentException If the <i>cls</i> parameter is <code>null</code>
     */
    public int getFieldSize(Class cls) {
        return MemoryUsageHelper.getFieldSize(cls);
    }
}
