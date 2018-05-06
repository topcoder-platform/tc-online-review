/**
 *
 * Copyright (c) 2012, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.review.assignment;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.management.review.assignment.accuracytests.AccuracyTests;

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
//        suite.addTest(UnitTests.suite());
        
        //accuracy tests
        suite.addTest(AccuracyTests.suite());
        
        return suite;
    }

}
