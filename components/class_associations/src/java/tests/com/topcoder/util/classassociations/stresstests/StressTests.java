/**
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.classassociations.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.TestResult;

/**
 * <p>This test case aggregates all Stress test cases.</p>
 *
 * @author garyk
 * @version 1.0
 */
public class StressTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(RetrieveHandlerStressTests.suite());
        suite.addTest(RetrieveClassHandlerStressTests.suite());

        return suite;
    }
}
