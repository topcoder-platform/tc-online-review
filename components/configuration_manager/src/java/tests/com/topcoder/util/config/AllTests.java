/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.config.accuracytests.AccuracyTests;
import com.topcoder.util.config.failuretests.FailureTests;
import com.topcoder.util.config.stresstests.StressTests;

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
        
        //failure tests
        suite.addTest(FailureTests.suite());
        
        //accuracy tests
        suite.addTest(AccuracyTests.suite());
        
        //stress tests
        suite.addTest(StressTests.suite());
        
        return suite;
    }

}
