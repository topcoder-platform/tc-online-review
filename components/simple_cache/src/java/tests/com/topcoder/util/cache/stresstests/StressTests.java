/*
 * StressTests.java
 *
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Stress test cases.
 * </p>
 *
 * @author colau
 * @version 2.0
 */
public class StressTests extends TestCase {
    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return a TestSuite for this test case
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(PutObjectStressTest.suite());
        suite.addTest(RemoveObjectStressTest.suite());

        return suite;
    }
}
