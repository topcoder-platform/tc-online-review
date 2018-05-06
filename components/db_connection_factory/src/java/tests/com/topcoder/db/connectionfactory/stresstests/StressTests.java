/**
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.db.connectionfactory.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Stress test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class StressTests extends TestCase {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        /**
         * Test the configuration read
         * 1 entry
         * 5 entries
         * 10 entries
         */
        suite.addTestSuite(DBConnectionFactoryStressTest.class);
        return suite;
    }
}

