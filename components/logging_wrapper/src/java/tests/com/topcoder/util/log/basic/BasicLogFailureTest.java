/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.basic;

import java.io.PrintStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This Junit Test suite contains the failure test cases for {@link BasicLog} class. The failure test cases
 * gives all invalid inputs to the methods/constructors and checks for expected exceptions
 * </p>
 * 
 * @author evilisneo
 * @version 1.0
 */
public class BasicLogFailureTest extends TestCase {
    /**
     * <p>
     * Aggregates all tests in this class.
     * </p>
     * 
     * @return Test suite aggregating all tests.
     */
    public static Test suite() {
        return new TestSuite(BasicLogFailureTest.class);
    }

    /**
     * <p>
     * Failure test for {@link BasicLog#BasicLog(String, PrintStream)} method.
     * </p>
     * <p>
     * For the following inputs:
     * </p>
     * 
     * <pre>
     * Input
     *      String name : Valid value
     *      PrintStream printStream : null value
     * </pre>
     * 
     * <p>
     * Expected {@link IllegalArgumentException}.
     * </p>
     * 
     * @throws Exception
     *             throw exception to JUnit.
     */
    public void test_failure_log() throws Exception {
        try {
            new BasicLog("test", null);
            fail("IllegalArgumentException Expected.");
        } catch (IllegalArgumentException e) {
            // As Expected
        }
    }
}
