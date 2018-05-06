/**
 *
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.topcoder.configuration.persistence.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Stress test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class StressTests extends TestCase {

    /**
     * Returns the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(ConfigurationFileManagerStressTest.class);
        suite.addTestSuite(PropertyFilePersistenceStressTest.class);
        suite.addTestSuite(XMLFilePersistenceStressTest.class);

        return suite;
    }
}
