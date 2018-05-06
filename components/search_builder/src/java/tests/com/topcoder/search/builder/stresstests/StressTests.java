/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.search.builder.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Stress test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class StressTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        
        suite.addTest(AndFilterStressTest.suite());
        suite.addTest(BetweenFilterStressTest.suite());
        suite.addTest(EqualToFilterStressTest.suite());
        suite.addTest(GreaterThanFilterStressTest.suite());
        suite.addTest(GreaterThanOrEqualToFilterStressTest.suite());
        suite.addTest(InFilterStressTest.suite());
        suite.addTest(LessThanFilterStressTest.suite());
        suite.addTest(LessThanOrEqualFilterStressTest.suite());
        suite.addTest(NotFilterStressTest.suite());
        suite.addTest(OrFilterStressTest.suite());
        suite.addTest(NullFilterStressTest.suite());

        return suite;
    }
}
