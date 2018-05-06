/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.failuretests;

import com.topcoder.util.log.LogFactory;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.log.basic.BasicLog;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This Junit Test suite contains the accuracy and failure test cases for {@link LogManager} class.
 * The accuracy test cases gives all valid inputs to the methods/constructors and checks for inconsistencies
 * The failure test cases gives all invalid inputs to the methods/constructors and checks for expected exceptions
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class LogManagerFailureTest extends TestCase {
    /**
     * <p>
     * Aggregates all tests in this class.
     * </p>
     *
     * @return Test suite aggregating all tests.
     */
    public static Test suite() {
        return new TestSuite(LogManagerFailureTest.class);
    }

    /**
     * <p>
     * Failure test for  {@link LogManager#setLogFactory(LogFactory)} method.
     * </p>
     * <p>
     * For the following inputs:
     * </p>
     * <pre>
     * Input
     *      LogFactory logFactory : null value
     * </pre>
     * <p>
     * Expected
     *         {@link IllegalArgumentException}.
     * </p>
     *
     * @throws Exception
     *             throw exception to JUnit.
     */
    public void test_failure_setLogFactory() throws Exception {
        try {
            LogManager.setLogFactory(null);
            fail("IllegalArgumentException Expected.");
        } catch (IllegalArgumentException e) {
            // As expected
        }
    }

    /**
     * <p>
     * Accuracy test for  {@link LogManager#getLogWithExceptions(String)} method.
     * </p>
     * <p>
     *  Checks whether by default a basic log instance is returned.
     * </p>
     *
     * @throws Exception
     *             throw exception to JUnit.
     */
    public void test_accuracy_getLogWithExceptions1() throws Exception {
           assertTrue(LogManager.getLogWithExceptions("test") instanceof BasicLog);
    }

    /**
     * <p>
     * Accuracy test for  {@link LogManager#getLogWithExceptions()} method.
     * </p>
     * <p>
     *  Checks whether by default a basic log instance is returned.
     * </p>
     *
     * @throws Exception
     *             throw exception to JUnit.
     */
    public void test_accuracy_getLogWithExceptions() throws Exception {
           assertTrue(LogManager.getLogWithExceptions() instanceof BasicLog);
    }
}
