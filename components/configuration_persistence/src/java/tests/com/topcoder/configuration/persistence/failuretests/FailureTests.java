/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 *
 * @author lyt
 * @version 1.0
 */
public class FailureTests extends TestCase {
    /**
     * Aggregates all tests.
     *
     * @return test suite aggregating all tests.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(ConfigurationFileManagerFailureTests.class);
        suite.addTestSuite(XMLFilePersistenceFailureTests.class);
        suite.addTestSuite(PropertyFilePersistenceFailureTests.class);
        return suite;
    }
}
