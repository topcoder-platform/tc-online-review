/**
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.configuration.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test case aggregates all failure test cases.
 *
 * @author Topcoder
 * @version 1.0
 */
public class FailureTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(SynchronizedConfigurationObjectV11Tests.class);
        suite.addTestSuite(DefaultConfigurationObjectV11Tests.class);
        return suite;
    }

}
