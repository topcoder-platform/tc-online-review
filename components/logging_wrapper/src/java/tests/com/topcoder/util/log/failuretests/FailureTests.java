/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.log.basic.BasicLogFactoryFailureTest;
import com.topcoder.util.log.basic.BasicLogFailureTest;

/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 * 
 * @author evilisneo
 * @version 1.0
 */
public class FailureTests extends TestCase {

    /**
     * Aggregates all the failure test cases.
     * 
     * @return the suite containing all the failure test cases.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(BasicLogFactoryFailureTest.suite());
        suite.addTest(LogManagerFailureTest.suite());
        suite.addTest(BasicLogFailureTest.suite());
        return suite;
    }

}
