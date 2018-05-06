/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import com.topcoder.db.connectionfactory.producers.DataSourceConnectionProducerAccuracyTest;
import com.topcoder.db.connectionfactory.producers.DataSourceConnectionProducerFailureTest;
import com.topcoder.db.connectionfactory.producers.JDBCConnectionProducerAccuracyTest;
import com.topcoder.db.connectionfactory.producers.JDBCConnectionProducerFailureTest;
import com.topcoder.db.connectionfactory.producers.JNDIConnectionProducerAccuracyTest;
import com.topcoder.db.connectionfactory.producers.JNDIConnectionProducerFailureTest;
import com.topcoder.db.connectionfactory.producers.ReflectingConnectionProducerAccuracyTest;
import com.topcoder.db.connectionfactory.producers.ReflectingConnectionProducerFailureTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author magicpig
 * @version 1.1
 */
public class UnitTests extends TestCase {
    /**
     * Aggregates all Unit test cases.
     *
     * @return the test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // adds tests for exceptions
        suite.addTestSuite(DBConnectionExceptionTest.class);
        suite.addTestSuite(ConfigurationExceptionTest.class);
        suite.addTestSuite(UnknownConnectionExceptionTest.class);
        suite.addTestSuite(NoDefaultConnectionExceptionTest.class);

        // adds tests for producers
        suite.addTestSuite(JDBCConnectionProducerFailureTest.class);
        suite.addTestSuite(JDBCConnectionProducerAccuracyTest.class);

        suite.addTestSuite(ReflectingConnectionProducerAccuracyTest.class);
        suite.addTestSuite(ReflectingConnectionProducerFailureTest.class);

        suite.addTestSuite(JNDIConnectionProducerFailureTest.class);
        suite.addTestSuite(JNDIConnectionProducerAccuracyTest.class);

        suite.addTestSuite(DataSourceConnectionProducerFailureTest.class);
        suite.addTestSuite(DataSourceConnectionProducerAccuracyTest.class);

        // adds tests for DBConnectionFactoryImpl
        suite.addTestSuite(DBConnectionFactoryImplFailureTest.class);
        suite.addTestSuite(DBConnectionFactoryImplAccuracyTest.class);

        // adds tests for helper class
        suite.addTestSuite(DBConnectionFactoryHelperUnitTests.class);

        // adds demo
        suite.addTestSuite(DemoTest.class);
        return suite;
    }
}
