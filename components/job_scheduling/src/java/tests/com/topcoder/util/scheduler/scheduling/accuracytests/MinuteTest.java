/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.Minute;

/**
 * <p>
 * Accuracy tests of <code>{@link Minute}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class MinuteTest extends TestCase {
    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(MinuteTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Minute#Minute()}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     * <p>
     * Also checks the inheritance of the class.
     * </p>
     */
    public void testMinute() {
        Minute minute = new Minute();
        assertNotNull("failed to create the Minute.", minute);

        // inheritance
        assertTrue("Minute should inherit from DateUnit", minute instanceof DateUnit);
    }

}
