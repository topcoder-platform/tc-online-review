/*
 * AllTests.java
 *
 * Copyright © 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test class will call each test suite for the ObjectFormatter
 * Component.<p>
 *
 * @author garyk
 * @version 1.0
 **/
public class AllTests extends TestCase {
    
    public AllTests(String str) { 
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

        // test suite of PrimitiveFormatterFactoryTest
        suite.addTest(PrimitiveFormatterFactoryTest.suite());

        // test suite of FormatMethodFactoryTest
        suite.addTest(FormatMethodFactoryTest.suite());

        // test suite of ObjectFormatterFactoryTest
        suite.addTest(ObjectFormatterFactoryTest.suite());				

        // test suite of Stress Tests
        suite.addTest(
            com.topcoder.util.format.accuracytests.AccuracyTests.suite( ) );

        // test suite of Stress Tests
        suite.addTest(
            com.topcoder.util.format.stresstests.StressTests.suite( ) );

        // test suite of FailureTests
        suite.addTest(
            com.topcoder.util.format.failuretests.FailureTests.suite( ) );

        return suite;
    }
}
