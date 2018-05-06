/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage.analyzers;

import com.topcoder.util.memoryusage.BaseAnalyzer;
import com.topcoder.util.memoryusage.MemoryUsageHelper;

/**
 * <p>
 * Memory usage analyzer implementation for the Sun JVMs 1.3.
 * There are no differences between the 1.3 memory model and the one in the
 * BaseAnalyzer.
 * </p>
 * <p><b>Thread safety:</b> This class is thread safe since it has no state. </p>
 *
 * @author AleaActaEst, TexWiller
 * @version 2.0
 * @since 2.0
 */
public class Sun13Analyzer extends BaseAnalyzer {

    /**
     * Creates a new instance of this analyzer.
     */
    public Sun13Analyzer() {
        super("Sun Microsystems, Inc.", "JDK 1.3.x");
    }

    /**
     * Detects the running JVM is the Sun 1.3 JVM.
     *
     * @return <code>true</code> if the running JVM is the Sun 1.3 JVM; <code>false</code>otherwise.
     */
    public boolean matchesJVM() {
        return MemoryUsageHelper.checkVendorVersion("Sun Microsystems", "47.");
    }
}
