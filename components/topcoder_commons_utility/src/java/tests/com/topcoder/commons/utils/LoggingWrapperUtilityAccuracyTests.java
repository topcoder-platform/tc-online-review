/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

/**
 * <p>
 * Unit tests for {@link LoggingWrapperUtility} class.
 * </p>
 *
 * @author dingying131
 * @version 1.0
 */
public class LoggingWrapperUtilityAccuracyTests {

    /**
     * <p>
     * The wrapped logger to be tested.
     * </p>
     */
    private MockLogger log;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(LoggingWrapperUtilityAccuracyTests.class);
    }

    /**
     * <p>
     * Before method that creates a test logger.
     * </p>
     */
    @Before
    public void before() {
        log = new MockLogger();
    }

    /**
     * <p>
     * Accuracy test for {@link LoggingWrapperUtility#logEntrance(Log, String, String[], Object[])}.
     * </p>
     *
     * <p>
     * The exception message should be generated correctly.
     * </p>
     */
    @Test
    public void testLogEntrance1() {
        LoggingWrapperUtility.logEntrance(log, "testMethod", new String[] {"p1", "p2"}, new Object[] {1, "test"});
        Assert.assertEquals("Log level should be correct.", Level.DEBUG, log.getLogLevel());
        Assert.assertEquals("Log level should be correct.", Level.DEBUG, log.getLogLevel());
        Assert.assertEquals("Log message should be correct.", "Entering method [testMethod].", log.getLogObject());
        Assert.assertEquals("Log message should be correct.", "Input parameters [p1:1, p2:test]", log.getLogObject());
    }

    /**
     * <p>
     * Accuracy test for {@link LoggingWrapperUtility#logEntrance(Log, String, String[], Object[], boolean, Level)}.
     * </p>
     *
     * <p>
     * The exception message should be generated correctly.
     * </p>
     */
    @Test
    public void testLogEntrance2() {
        LoggingWrapperUtility.logEntrance(log, "testMethod", new String[] {"p1", "p2"}, new Object[] {1, "test"},
                false, Level.INFO);
        Assert.assertEquals("Log level should be correct.", Level.INFO, log.getLogLevel());
        Assert.assertEquals("Log level should be correct.", Level.INFO, log.getLogLevel());
        Assert.assertEquals("Log message should be correct.", "Entering method [testMethod].", log.getLogObject());
        Assert.assertEquals("Log message should be correct.", "Input parameters [p1:1, p2:test]", log.getLogObject());
    }

    /**
     * <p>
     * Accuracy test for {@link LoggingWrapperUtility#logExit(Log, String, Object[])}.
     * </p>
     *
     * <p>
     * The exception message should be generated correctly.
     * </p>
     */
    @Test
    public void testLogExit1() {
        LoggingWrapperUtility.logExit(log, "testMethod", new Object[] {"test"});
        Assert.assertEquals("Log level should be correct.", Level.DEBUG, log.getLogLevel());
        Assert.assertEquals("Log level should be correct.", Level.DEBUG, log.getLogLevel());
        Assert.assertEquals("Log message should be correct.", "Exiting method [testMethod].", log.getLogObject());
        Assert.assertEquals("Log message should be correct.", "Output parameter: test", log.getLogObject());
    }

    /**
     * <p>
     * Accuracy test for {@link LoggingWrapperUtility#logExit(Log, String, Object[], Date)}.
     * </p>
     *
     * <p>
     * The exception message should be generated correctly.
     * </p>
     */
    @Test
    public void testLogExit2() {
        Date now = new Date();
        LoggingWrapperUtility.logExit(log, "testMethod", new Object[] {"test"}, now);
        Assert.assertEquals("Log level should be correct.", Level.DEBUG, log.getLogLevel());
        Assert.assertEquals("Log level should be correct.", Level.DEBUG, log.getLogLevel());
        Assert.assertTrue("Log message should be correct.", log.getLogObject().toString().startsWith(
                "Exiting method [testMethod], time spent in the method:"));
        Assert.assertEquals("Log message should be correct.", "Output parameter: test", log.getLogObject());
    }

    /**
     * <p>
     * Accuracy test for {@link LoggingWrapperUtility#logExit(Log, String, Object[], Date, boolean, Level)}.
     * </p>
     *
     * <p>
     * The exception message should be generated correctly.
     * </p>
     */
    @Test
    public void testLogExit3() {
        Date now = new Date();
        LoggingWrapperUtility.logExit(log, "testMethod", new Object[] {"test"}, now, false, Level.INFO);
        Assert.assertEquals("Log level should be correct.", Level.INFO, log.getLogLevel());
        Assert.assertEquals("Log level should be correct.", Level.INFO, log.getLogLevel());
        Assert.assertTrue("Log message should be correct.", log.getLogObject().toString().startsWith(
                "Exiting method [testMethod], time spent in the method:"));
        Assert.assertEquals("Log message should be correct.", "Output parameter: test", log.getLogObject());
    }

    /**
     * <p>
     * Accuracy test for {@link LoggingWrapperUtility#logException(Log, String, Throwable)}.
     * </p>
     *
     * <p>
     * The exception message should be generated correctly.
     * </p>
     */
    @Test
    public void testLogEexception1() {
        Throwable t = new IllegalArgumentException("test error");
        Assert.assertSame("Exception should be returned.", t, LoggingWrapperUtility.logException(log, "testMethod", t));
        Assert.assertEquals("Log level should be correct.", Level.ERROR, log.getLogLevel());
        Assert.assertTrue("Log message should be correct.", log.getLogObject().toString().startsWith(
                "Error in method [testMethod], details: test error"));
    }

    /**
     * <p>
     * Accuracy test for {@link LoggingWrapperUtility#logException(Log, String, Throwable, boolean, Level)}.
     * </p>
     *
     * <p>
     * The exception message should be generated correctly.
     * </p>
     */
    @Test
    public void testLogEexception2() {
        Throwable t = new IllegalArgumentException("test error");
        Assert.assertSame("Exception should be returned.", t, LoggingWrapperUtility.logException(log, "testMethod", t,
                false, Level.FATAL));
        Assert.assertEquals("Log level should be correct.", Level.FATAL, log.getLogLevel());
        Assert.assertTrue("Log message should be correct.", log.getLogObject().toString().startsWith(
                "Error in method [testMethod], details: test error"));
    }
}
