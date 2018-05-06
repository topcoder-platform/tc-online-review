/**
 *
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.db.connectionfactory.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Accuracy test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(ConfigurationExceptionAccuracyTest.class);
        suite.addTestSuite(DataSourceConnectionProducerAccuracyTest.class);
        suite.addTestSuite(DBConnectionExceptionAccuracyTest.class);
        suite.addTestSuite(DBConnectionFactoryImplAccuracyTest.class);
        suite.addTestSuite(JDBCConnectionProducerAccuracyTest.class);
        suite.addTestSuite(JNDIConnectionProducerAccuracyTest.class);
        suite.addTestSuite(NoDefaultConnectionExceptionAccuracyTest.class);
        suite.addTestSuite(ReflectingConnectionProducerAccuracyTest.class);
        suite.addTestSuite(UnknownConnectionExceptionAccuracyTest.class);
        return suite;
    }

}
