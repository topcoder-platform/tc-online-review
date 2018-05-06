/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.failuretests;

import com.topcoder.management.phase.OperationCheckResult;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.framework.TestCase;

/**
 * <p>
 * Failure test cases for OperationCheckResult.
 * </p>
 *
 * @author victorsam
 * @version 1.1
 */
public class OperationCheckResultFailureTests extends TestCase {

    /**
     * <p>
     * Return all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(OperationCheckResultFailureTests.class);
    }

    /**
     * <p>
     * Tests ctor OperationCheckResult#OperationCheckResult(boolean,String) for failure.
     * It tests the case that when success and message != null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_Fail1() {
        try {
            new OperationCheckResult(true, "message");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor OperationCheckResult#OperationCheckResult(boolean,String) for failure.
     * It tests the case that when not success and message == null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_Fail2() {
        try {
            new OperationCheckResult(false, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor OperationCheckResult#OperationCheckResult(boolean,String) for failure.
     * It tests the case that when not success and message is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_Fail3() {
        try {
            new OperationCheckResult(false, " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor OperationCheckResult#OperationCheckResult(String) for failure.
     * It tests the case that when message is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullMessage2() {
        try {
            new OperationCheckResult(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor OperationCheckResult#OperationCheckResult(String) for failure.
     * It tests the case that when message is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_EmptyMessage2() {
        try {
            new OperationCheckResult(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

}