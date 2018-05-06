/**
 *
 * Copyright ?2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.net.ldap.sdkinterface.accuracytests;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Aggregates all Accuracy Tests
 *
 * @author BryanChen
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    /**
     * Returns suite of tests
     * @return suite of tests
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(EntryAccuracyTests.suite());
        suite.addTest(UpdateAccuracyTests.suite());
        suite.addTest(ValuesAccuracyTests.suite());
        suite.addTest(NetscapeConnectionTests.suite());

        return suite;
    }
}
