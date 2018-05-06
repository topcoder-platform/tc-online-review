/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.Month;

/**
 * <p>
 * Accuracy tests of <code>{@link Month}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class MonthTest extends TestCase {
    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(MonthTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Month#Month()}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     * <p>
     * Also checks the inheritance of the class.
     * </p>
     */
    public void testMonth() {
        Month month = new Month();
        assertNotNull("failed to create the Month.", month);

        // inheritance
        assertTrue("Month should inherit from DateUnit", month instanceof DateUnit);
    }

}
