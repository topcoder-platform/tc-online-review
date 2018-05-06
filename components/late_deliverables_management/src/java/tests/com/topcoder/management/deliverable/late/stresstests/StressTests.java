/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Stress test cases.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class StressTests extends TestCase {
    /**
     * <p>
     * All unit test cases.
     * </p>
     *
     * @return The test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(DatabaseLateDeliverablePersistenceStressTest.class);
        suite.addTestSuite(LateDeliverableManagerImplStressTest.class);

        return suite;
    }
}