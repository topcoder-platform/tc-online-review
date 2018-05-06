package com.topcoder.util.sql.databaseabstraction.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all stress test cases.
 * </p>
 *
 * @author TopCoder Software
 * @version 1.1
 */
public class StressTests extends TestCase {
    /**
     * Returns the stress test suite.
     *
     * @return the stress test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(DatabaseAbstractionStressTests.suite());
        suite.addTest(DatabaseAbstractionStressTestsV2.suite());

        return suite;
    }
}
