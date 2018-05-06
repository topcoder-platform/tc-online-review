/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.db.connectionfactory.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public class FailureTests extends TestCase {
    /**
     * <p>
     * return the test suite.
     * </p>
     *
     * @return the test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(DbConnectionFactoryImplFailureTests.class);
        suite.addTestSuite(ReflectingConnectionProducerFailureTests.class);
        suite.addTestSuite(JNDIConnectionProducerFailureTests.class);
        suite.addTestSuite(DataSourceConnectionProducerFailureTests.class);
        suite.addTestSuite(JDBCConnectionProducerFailureTests.class);

        return suite;
    }
}
