/**
 *
 * Copyright 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.file;

import com.topcoder.util.file.accuracytests.AccuracyTests;
import com.topcoder.util.file.failuretests.FailureTests;
import com.topcoder.util.file.stresstests.StressTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AllTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        
        //unit tests
        suite.addTest(UnitTests.suite());
        suite.addTest(StressTests.suite());
        suite.addTest(AccuracyTests.suite());
        suite.addTest(FailureTests.suite());
        
        return suite;
    }

}
