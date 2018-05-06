/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Unit tests for {@link LoggingUtilityHelper} class.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class LoggingUtilityHelperUnitTests {
    /**
     * <p>
     * Represents the signature.
     * </p>
     */
    private String signature = "method_signature";

    /**
     * <p>
     * Represents the parameter names.
     * </p>
     */
    private String[] paramNames;

    /**
     * <p>
     * Represents the parameter values.
     * </p>
     */
    private Object[] paramValues;

    /**
     * <p>
     * Represents the return value.
     * </p>
     */
    private Object value = "result";

    /**
     * <p>
     * Represents the exception.
     * </p>
     */
    private Exception exception;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(LoggingUtilityHelperUnitTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Before
    public void setUp() throws Exception {
        paramNames = new String[] {"p1", "p2"};
        paramValues = new Object[] {123, "abc"};
        exception = new Exception("Exception for testing.");
    }

    /**
     * <p>
     * Accuracy test for the method <code>getMethodEntranceMessage(String signature)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getMethodEntranceMessage() {
        String res = LoggingUtilityHelper.getMethodEntranceMessage(signature);

        assertEquals("'getMethodEntranceMessage' should be correct.", "Entering method [method_signature].", res);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getInputParametersMessage(String[] paramNames,
     * Object[] paramValues)</code> with one parameter.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getInputParametersMessage_One() {
        paramNames = new String[] {"p1"};
        paramValues = new Object[] {123};
        String res = LoggingUtilityHelper.getInputParametersMessage(paramNames, paramValues);

        assertEquals("'getInputParametersMessage' should be correct.", "Input parameters [p1:123]", res);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getInputParametersMessage(String[] paramNames,
     * Object[] paramValues)</code> with two parameters.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getInputParametersMessage_Two() {
        String res = LoggingUtilityHelper.getInputParametersMessage(paramNames, paramValues);

        assertEquals("'getInputParametersMessage' should be correct.", "Input parameters [p1:123, p2:abc]", res);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getMethodExitMessage(String signature, Date entranceTimestamp)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getMethodExitMessage_1() {
        String res = LoggingUtilityHelper.getMethodExitMessage(signature, null);

        assertEquals("'getMethodExitMessage' should be correct.", "Exiting method [method_signature].", res);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getMethodExitMessage(String signature, Date entranceTimestamp)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getMethodExitMessage_2() {
        String res = LoggingUtilityHelper.getMethodExitMessage(signature, new Date());

        assertTrue("'getMethodExitMessage' should be correct.",
            res.startsWith("Exiting method [method_signature], time spent in the method: "));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getOutputValueMessage(Object value)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getOutputValueMessage() {
        String res = LoggingUtilityHelper.getOutputValueMessage(value);

        assertEquals("'getOutputValueMessage' should be correct.", "Output parameter: result", res);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getExceptionMessage(String signature, Throwable exception)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getExceptionMessage() {
        String res = LoggingUtilityHelper.getExceptionMessage(signature, exception);

        assertTrue("'getExceptionMessage' should be correct.",
            res.startsWith("Error in method [method_signature], details: Exception for testing."));
        assertTrue("'getExceptionMessage' should be correct.",
            res.contains("java.lang.Exception: Exception for testing."));
    }
}
