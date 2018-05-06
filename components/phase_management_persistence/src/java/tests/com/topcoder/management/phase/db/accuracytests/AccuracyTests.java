/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.db.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Accuracy test cases.</p>
 *
 * @author TCDEVELOPER
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    /**
     * Suite all the accuracy tests.
     * @return a suite of tests
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(InformixPhasePersistenceAccuracyTest.class);

        return suite;
    }

}
