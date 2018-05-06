/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>This test case aggregates all Stress test cases.</p>
 *
 * @author magicpig
 * @version 1.0
 */
public class StressTests extends TestCase {
    /**
     * Aggregates all the stress tests.
     *
     * @return the test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(CheckMethodsStressTest.class);

        return suite;
    }
}
