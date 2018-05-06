/*
 * @(#)FailureTests.java  1.0  4/26/2003
 *
 * Copyright  2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.compression.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all failure test cases.</p>
 *
 * @author kolanovic
 * @version 1.0
 */
public class FailureTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(CompressionUtilityTestCase.suite());
        return suite;
    }
}








