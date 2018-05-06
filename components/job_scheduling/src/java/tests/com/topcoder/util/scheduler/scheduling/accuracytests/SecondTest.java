/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.Second;

/**
 * <p>
 * Accuracy tests of <code>{@link Second}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class SecondTest extends TestCase {
    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(SecondTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Second#Second()}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     * <p>
     * Also checks the inheritance of the class.
     * </p>
     */
    public void testSecond() {
        Second second = new Second();
        assertNotNull("failed to create the Second.", second);

        // inheritance
        assertTrue("Second should inherit from DateUnit", second instanceof DateUnit);
    }

}
