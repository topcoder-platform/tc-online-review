/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.log.log4j.Log4jLogFactory;

/**
 * <p>
 * Unit tests for {@link LoggingWrapperUtility} class.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class LoggingWrapperUtilityUnitTests {
    /**
     * <p>
     * Represents the logger.
     * </p>
     */
    private Log log;

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
    private Object[] value;

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
        return new JUnit4TestAdapter(LoggingWrapperUtilityUnitTests.class);
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
        PropertyConfigurator.configure("test_files/log4j.properties");

        LogManager.setLogFactory(new Log4jLogFactory());
        log = LogManager.getLog(this.getClass().getName());

        paramNames = new String[] {"p1", "p2"};
        paramValues = new Object[] {123, "abc"};

        value = new Object[] {"result"};
        exception = new Exception("Exception for testing.");
    }

    /**
     * <p>
     * Cleans up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @After
    public void tearDown() throws Exception {
        org.apache.log4j.LogManager.shutdown();
        new File(TestsHelper.LOG_FILE).delete();
    }

    /**
     * <p>
     * Accuracy test for the method <code>logEntrance(Log log, String signature, String[] paramNames,
     * Object[] paramValues)</code> with no parameter.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logEntrance1_NoParameter() throws Exception {
        paramNames = null;
        LoggingWrapperUtility.logEntrance(log, signature, paramNames, paramValues);

        TestsHelper.checkLog("logEntrance",
            "DEBUG",
            "Entering method [method_signature].");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logEntrance(Log log, String signature, String[] paramNames,
     * Object[] paramValues)</code> with one parameter.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logEntrance1_OneParameter() throws Exception {
        paramNames = new String[] {"p1"};
        paramValues = new Object[] {123};
        LoggingWrapperUtility.logEntrance(log, signature, paramNames, paramValues);

        TestsHelper.checkLog("logEntrance",
            "DEBUG",
            "Entering method [method_signature].",
            "Input parameters [p1:123]");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logEntrance(Log log, String signature, String[] paramNames,
     * Object[] paramValues)</code> with two parameters.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logEntrance1_TwoParameters() throws Exception {
        LoggingWrapperUtility.logEntrance(log, signature, paramNames, paramValues);

        TestsHelper.checkLog("logEntrance",
            "DEBUG",
            "Entering method [method_signature].",
            "Input parameters [p1:123, p2:abc]");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logEntrance(Log log, String signature, String[] paramNames,
     * Object[] paramValues)</code> with log is <code>null</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logEntrance1_NoLogging() throws Exception {
        log = null;
        LoggingWrapperUtility.logEntrance(log, signature, paramNames, paramValues);

        assertEquals("'logEntrance' should be correct.", 0, TestsHelper.readFile(TestsHelper.LOG_FILE).length());
    }

    /**
     * <p>
     * Accuracy test for the method <code>logEntrance(Log log, String signature, String[] paramNames,
     * Object[] paramValues, boolean addTimestamp, Level level)</code> with no parameter.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logEntrance2_NoParameter() throws Exception {
        paramNames = null;
        LoggingWrapperUtility.logEntrance(log, signature, paramNames, paramValues, true, Level.INFO);

        TestsHelper.checkLog("logEntrance",
            "INFO",
            "] Entering method [method_signature].");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logEntrance(Log log, String signature, String[] paramNames,
     * Object[] paramValues, boolean addTimestamp, Level level)</code> with one parameter.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logEntrance2_OneParameter() throws Exception {
        paramNames = new String[] {"p1"};
        paramValues = new Object[] {123};
        LoggingWrapperUtility.logEntrance(log, signature, paramNames, paramValues, true, Level.INFO);

        TestsHelper.checkLog("logEntrance",
            "INFO",
            "] Entering method [method_signature].",
            "Input parameters [p1:123]");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logEntrance(Log log, String signature, String[] paramNames,
     * Object[] paramValues, boolean addTimestamp, Level level)</code> with two parameters.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logEntrance2_TwoParameters() throws Exception {
        LoggingWrapperUtility.logEntrance(log, signature, paramNames, paramValues, true, Level.INFO);

        TestsHelper.checkLog("logEntrance",
            "INFO",
            "] Entering method [method_signature].",
            "Input parameters [p1:123, p2:abc]");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logEntrance(Log log, String signature, String[] paramNames,
     * Object[] paramValues, boolean addTimestamp, Level level)</code> with log is <code>null</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logEntrance2_NoLogging() throws Exception {
        log = null;
        LoggingWrapperUtility.logEntrance(log, signature, paramNames, paramValues, true, Level.INFO);

        assertEquals("'logEntrance' should be correct.", 0, TestsHelper.readFile(TestsHelper.LOG_FILE).length());
    }

    /**
     * <p>
     * Accuracy test for the method <code>logExit(Log log, String signature, Object[] value)</code> with no
     * return value.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logExit1_NoValue() throws Exception {
        value = null;
        LoggingWrapperUtility.logExit(log, signature, value);

        TestsHelper.checkLog("logExit",
            "DEBUG",
            "Exiting method [method_signature].");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logExit(Log log, String signature, Object[] value)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logExit1() throws Exception {
        paramNames = new String[] {"p1"};
        paramValues = new Object[] {123};
        LoggingWrapperUtility.logExit(log, signature, value);

        TestsHelper.checkLog("logExit",
            "DEBUG",
            "Exiting method [method_signature].",
            "Output parameter: result");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logExit(Log log, String signature, Object[] value)</code> with
     * log is <code>null</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logExit1_NoLogging() throws Exception {
        log = null;
        LoggingWrapperUtility.logExit(log, signature, value);

        assertEquals("'logExit' should be correct.", 0, TestsHelper.readFile(TestsHelper.LOG_FILE).length());
    }

    /**
     * <p>
     * Accuracy test for the method <code>logExit(Log log, String signature, Object[] value,
     * Date entranceTimestamp)</code> with no return value.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logExit2_NoValue() throws Exception {
        value = null;
        LoggingWrapperUtility.logExit(log, signature, value, new Date());

        TestsHelper.checkLog("logExit",
            "DEBUG",
            "Exiting method [method_signature], time spent in the method: ");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logExit(Log log, String signature, Object[] value,
     * Date entranceTimestamp)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logExit2() throws Exception {
        paramNames = new String[] {"p1"};
        paramValues = new Object[] {123};
        LoggingWrapperUtility.logExit(log, signature, value, new Date());

        TestsHelper.checkLog("logExit",
            "DEBUG",
            "Exiting method [method_signature], time spent in the method: ",
            "Output parameter: result");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logExit(Log log, String signature, Object[] value,
     * Date entranceTimestamp)</code> with log is <code>null</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logExit2_NoLogging() throws Exception {
        log = null;
        LoggingWrapperUtility.logExit(log, signature, value, new Date());

        assertEquals("'logExit' should be correct.", 0, TestsHelper.readFile(TestsHelper.LOG_FILE).length());
    }

    /**
     * <p>
     * Accuracy test for the method <code>logExit(Log log, String signature, Object[] value,
     * Date entranceTimestamp, boolean addTimestamp, Level level)</code> with no return value.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logExit3_NoValue() throws Exception {
        value = null;
        LoggingWrapperUtility.logExit(log, signature, value, new Date(), true, Level.INFO);

        TestsHelper.checkLog("logExit",
            "INFO",
            "] Exiting method [method_signature], time spent in the method: ");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logExit(Log log, String signature, Object[] value,
     * Date entranceTimestamp, boolean addTimestamp, Level level)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logExit3() throws Exception {
        paramNames = new String[] {"p1"};
        paramValues = new Object[] {123};
        LoggingWrapperUtility.logExit(log, signature, value, new Date(), true, Level.INFO);

        TestsHelper.checkLog("logExit",
            "INFO",
            "] Exiting method [method_signature], time spent in the method: ",
            "Output parameter: result");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logExit(Log log, String signature, Object[] value,
     * Date entranceTimestamp, boolean addTimestamp, Level level)</code> with log is <code>null</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logExit3_NoLogging() throws Exception {
        log = null;
        LoggingWrapperUtility.logExit(log, signature, value, new Date(), true, Level.INFO);

        assertEquals("'logExit' should be correct.", 0, TestsHelper.readFile(TestsHelper.LOG_FILE).length());
    }

    /**
     * <p>
     * Accuracy test for the method <code>logException(Log log, String signature, T exception)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logException1() throws Exception {
        Exception res = LoggingWrapperUtility.logException(log, signature, exception);

        assertSame("'logException' should be correct.", exception, res);

        TestsHelper.checkLog("logException",
            "ERROR",
            "Error in method [method_signature], details: Exception for testing.",
            "java.lang.Exception: Exception for testing.");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logException(Log log, String signature, T exception)</code> with
     * log is <code>null</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logException1_NoLogging() throws Exception {
        log = null;
        Exception res = LoggingWrapperUtility.logException(log, signature, exception);

        assertSame("'logException' should be correct.", exception, res);

        assertEquals("'logException' should be correct.", 0, TestsHelper.readFile(TestsHelper.LOG_FILE).length());
    }

    /**
     * <p>
     * Accuracy test for the method <code>logException(Log log, String signature, T exception,
     * boolean addTimestamp, Level level)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logException2() throws Exception {
        Exception res = LoggingWrapperUtility.logException(log, signature, exception, true, Level.WARN);

        assertSame("'logException' should be correct.", exception, res);

        TestsHelper.checkLog("logException",
            "WARN",
            "] Error in method [method_signature], details: Exception for testing.",
            "java.lang.Exception: Exception for testing.");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logException(Log log, String signature, T exception,
     * boolean addTimestamp, Level level)</code> with log is <code>null</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logException2_NoLogging() throws Exception {
        log = null;
        Exception res = LoggingWrapperUtility.logException(log, signature, exception, true, Level.WARN);

        assertSame("'logException' should be correct.", exception, res);

        assertEquals("'logException' should be correct.", 0, TestsHelper.readFile(TestsHelper.LOG_FILE).length());
    }
}
