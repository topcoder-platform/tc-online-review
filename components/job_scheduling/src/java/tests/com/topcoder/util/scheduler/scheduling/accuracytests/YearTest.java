/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.Year;

/**
 * <p>
 * Accuracy tests of <code>{@link Year}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class YearTest extends TestCase {
    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(YearTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Year#Year()}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     * <p>
     * Also checks the inheritance of the class.
     * </p>
     */
    public void testYear() {
        Year year = new Year();
        assertNotNull("failed to create the Year.", year);

        // inheritance
        assertTrue("Year should inherit from DateUnit", year instanceof DateUnit);
    }

}
