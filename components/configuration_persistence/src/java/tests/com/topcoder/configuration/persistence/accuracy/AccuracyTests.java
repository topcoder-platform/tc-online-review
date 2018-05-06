/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence.accuracy;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */

public class AccuracyTests extends TestCase {
    /**
     * Aggregates all the Accuracy test cases.
     *
     * @return suite with tests
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(TestXMLFilePersistenceAccuracy.class);
        suite.addTestSuite(TestPropertyFilePersistenceAccuracy.class);

        suite.addTestSuite(TestConfigurationFileManagerAccuracy.class);
        return suite;
    }
}