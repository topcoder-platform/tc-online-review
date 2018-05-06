/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.Hour;

/**
 * <p>
 * Accuracy tests of <code>{@link Hour}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class HourTest extends TestCase {
    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(HourTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Hour#Hour()}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     * <p>
     * Also checks the inheritance of the class.
     * </p>
     */
    public void testHour() {
        Hour hour = new Hour();
        assertNotNull("failed to create the Hour.", hour);

        // inheritance
        assertTrue("Hour should inherit from DateUnit", hour instanceof DateUnit);
    }

}
