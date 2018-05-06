/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.file.transfer.accuracytests.AccuracyTests;
import com.topcoder.file.transfer.failuretests.FailureTests;
import com.topcoder.file.transfer.stresstests.StressTests;

/**
 * <p>
 * This test case aggregates all test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AllTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // unit tests
        suite.addTest(UnitTests.suite());

        // accuracy tests
        suite.addTest(AccuracyTests.suite());

        // failure tests
        suite.addTest(FailureTests.suite());

        // Please run stress tests separately, it would cause OutOfMemory problem
        // suite.addTest(StressTests.suite());

        return suite;
    }

}
