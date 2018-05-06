/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.stresstests;

import com.topcoder.util.errorhandling.CauseUtils;

import junit.framework.TestCase;


/**
 * Stress test for <code>CauseUtils</code>.
 *
 * @author kzhu
 * @version 2.0
 */
public class CauseUtilsStressTests extends TestCase {
    /** The time to do the stress test. */
    private static final int STRESS_TIME = 200;

    /**
     * Stress test for <code>getCause()</code>.
     */
    public void testGetCause() {
        Exception e = new Exception();

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", CauseUtils.getCause(new Exception(e)));
        }

        System.out.println("Test getCause()" + STRESS_TIME + " times costs: " + (System.currentTimeMillis() - start)
            + "ms.");
    }
}
