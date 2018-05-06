/*
 * StressTests.java
 *
 * Copyright © 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test class will call each test suite for the ObjectFormatter
 * Component.<p>
 *
 * @author valeriy
 * @version 1.0
 **/
public class StressTests extends TestCase {
    
    public StressTests(String str) { 
        super(str); 
    }
    
    /**
     * Assembles and returns a test suite
     * containing all known tests.
     *
     * New tests should be added here!
     *
     * @return A non-null test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();    
        suite.addTest(ObjectFormatterStressTest.suite());
        return suite;
    }
}
