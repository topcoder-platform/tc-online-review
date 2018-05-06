/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */


package com.topcoder.util.memoryusage;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Tests for MemoryUsageException. Simply applies the GenericExceptionTest.
 *
 * @author TexWiller
 * @version 2.0
 */
public class MemoryUsageExceptionTest extends GenericExceptionTest {

    /**
     * Standard TestCase constructor: creates a new MemoryUsageExceptionTest object.
     *
     * @param testName The name to be given to this TestCase.
     */
    public MemoryUsageExceptionTest(String testName) {
        super(MemoryUsageException.class, testName);
    }

    /**
     * Static method expected by JUnit test runners. Returns a Test containing all
     * the testXXX methods in this class.
     *
     * @return A new Test, containing all the testXXX methods in this class.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(MemoryUsageExceptionTest.class);
        return suite;
    }
}
