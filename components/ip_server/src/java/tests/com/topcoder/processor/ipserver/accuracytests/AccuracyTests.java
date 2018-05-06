/**
 *
 * Copyright (c) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.processor.ipserver.accuracytests;

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

        suite.addTest(new TestSuite(IPServerAccuracyTest.class));
        suite.addTest(new TestSuite(IPServerManagerAccuracyTest.class));

        // These test suites are added for version 2.0.
        suite.addTestSuite(SerializableMessageAccuracyTestsV2.class);
        suite.addTestSuite(DefaultMessageFactoryAccuracyTestsV2.class);
        suite.addTestSuite(IPServerAccuracyTestsV2.class);

        return suite;
    }

}
