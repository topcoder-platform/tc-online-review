/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.accuracytests;

import com.topcoder.util.log.accuracytests.AccuracyTestsHelper;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.log4j.Log4jLog;
import com.topcoder.util.log.log4j.Log4jLogFactory;


import org.apache.log4j.FileAppender;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.Logger;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy tests for <code>Log4jLog</code>.
 * </p>
 *
 * @author tianniu
 * @version 2.0
 */
public class Log4jLogAccuracyTests extends TestCase {

    /** Represents the log name. */
    private static String NAME = "log4jlog";

    /** Represents the file path. */
    private static String FILE = "test_files/Log4jLog.txt";

    /** <code>Log4jLog</code> instance used for testing. */
    private Log4jLog log;

    /** <code>FileAppender</code> instance used for testing. */
    private FileAppender fileAppender;

    /**
     * <p>
     * Set up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        log = createLog4jLog(NAME, true);

        // set up the configuration of the corresponding logger
        fileAppender = new FileAppender(new SimpleLayout(), FILE);
        fileAppender.setImmediateFlush(true);
        Logger logger = Logger.getLogger(NAME);
        logger.removeAllAppenders();
        logger.addAppender(fileAppender);
        logger.setLevel(org.apache.log4j.Level.ALL);
    }

    /**
     * <p>
     * Release the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        log = null;

        // remove all appenders
        //Note: this is very important, files can't be deleted if appenders exist
        Logger logger = Logger.getLogger(NAME);
        logger.removeAllAppenders();

        // clear file
        AccuracyTestsHelper.clearFile(FILE);
    }

    /**
     * <p>
     * private method for create log4jlog intstance by using factory.
     * </p>
     *
     * @param name log name.
     * @param useLoggerLayout flag for if use logger layout.
     * @return Log4jLog object.
     */
    private Log4jLog createLog4jLog(String name, boolean useLoggerLayout) {
        Log4jLogFactory factory = new Log4jLogFactory(useLoggerLayout);
        return (Log4jLog) factory.createLog(name);
    }

    /**
     * <p>
     * Accuracy test of constructor <code>Log4jLog(String name, boolean useLoggerLayout)</code>.
     * Should create the instance successfully.
     * </p>
     */
    public void testCtorAccuracy() {
        assertNotNull("Should create the instance successfully.", log);
        assertEquals(NAME, log.getName());
    }

    /**
     * <p>
     * Accuracy test of constructor <code>Log4jLog(String name, boolean useLoggerLayout)</code>.
     * With null name.
     * Should create the instance successfully.
     * </p>
     */
    public void testCtorAccuracyNullName() {
        log = createLog4jLog(null, true);

        assertNotNull("Should create the instance successfully.", log);
        assertNull(log.getName());
    }

    /**
     * <p>
     * Accuracy test of constructor <code>Log4jLog(String name, boolean useLoggerLayout)</code>.
     * With empty name.
     * Should create the instance successfully.
     * </p>
     */
    public void testCtorAccuracyEmptyName() {
        log = createLog4jLog("", true);
        assertNotNull("Should create the instance successfully.", log);
        assertEquals("", log.getName());
    }

    /**
     * <p>
     * Accuracy test of constructor <code>Log4jLog(String name, boolean useLoggerLayout)</code>.
     * With name only contains whitespace characters.
     * Should create the instance successfully.
     * </p>
     */
    public void testCtorAccuracyBlankName() {
        log = createLog4jLog("  ", true);
        assertNotNull("Should create the instance successfully.", log);
        assertEquals("  ", log.getName());
    }

    /**
     * <p>
     * Accuracy test of constructor <code>Log4jLog(String name, boolean useLoggerLayout)</code>.
     * With false useLoggerLayout.
     * Should create the instance successfully.
     * </p>
     */
    public void testCtorAccuracyFalseUseLoggerLayout() {
        log = createLog4jLog(NAME, false);
        assertNotNull("Should create the instance successfully.", log);
        assertEquals(NAME, log.getName());
    }

    /**
     * <p>
     * Accuracy test of method <code>isEnabled(Level level)</code>.
     * Should return the proper value.
     * </p>
     */
    public void testIsEnabledAccuracy() {
        assertFalse(log.isEnabled(null));

        // retrieve the corresponding logger
        Logger logger = Logger.getLogger(NAME);

        logger.setLevel(org.apache.log4j.Level.ALL);
        assertTrue(log.isEnabled(Level.CONFIG));
        assertTrue(log.isEnabled(Level.TRACE));

        logger.setLevel(org.apache.log4j.Level.ERROR);
        assertTrue(log.isEnabled(Level.FATAL));
        assertFalse(log.isEnabled(Level.TRACE));
        assertFalse(log.isEnabled(Level.FINEST));
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Throwable cause, String message)</code>.
     * With cause and message.
     * Should log the cause and message.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracy1() throws Exception {
        // log the message
        Exception cause = new Exception("some error");
        String message = "some message";
        log.log(Level.ALL, cause, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") >= 0);
        assertTrue(result.indexOf("some error") >= 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Throwable cause, String message)</code>.
     * With null level.
     * Should return directly.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyNullLevel1() throws Exception {
        // log the message
        Exception cause = new Exception("some error");
        String message = "some message";
        log.log(null, cause, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") < 0);
        assertTrue(result.indexOf("some error") < 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Throwable cause, String message)</code>.
     * With Level.OFF.
     * Should return directly.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyLevelOFF1() throws Exception {
        // log the message
        Exception cause = new Exception("some error");
        String message = "some message";
        log.log(Level.OFF, cause, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") < 0);
        assertTrue(result.indexOf("some error") < 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Throwable cause, String message)</code>.
     * With null message.
     * Should log cause.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyNullMessage1() throws Exception {
        // log the message
        Exception cause = new Exception("some error");
        String message = null;
        log.log(Level.ALL, cause, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") < 0);
        assertTrue(result.indexOf("some error") >= 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Throwable cause, String message)</code>.
     * With null cause.
     * Should log the message only.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyNullCause1() throws Exception {
        // log the message
        Exception cause = null;
        String message = "some message";
        log.log(Level.ALL, cause, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") >= 0);
        assertTrue(result.indexOf("some error") < 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Throwable cause, Object message)</code>.
     * With cause and message.
     * Should log the cause and message.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracy3() throws Exception {
        // log the message
        Exception cause = new Exception("some error");
        StringBuffer message = new StringBuffer("some message");
        log.log(Level.ALL, cause, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") >= 0);
        assertTrue(result.indexOf("some error") >= 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Throwable cause, Object message)</code>.
     * With false useLoggerLayout.
     * Should log the cause and message.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyFalseUseLoggerLayout3() throws Exception {
        // create a Log4jLog with false useLoggerLayout
        log = createLog4jLog(NAME, false);

        // log the message
        Exception cause = new Exception("some error");
        StringBuffer message = new StringBuffer("some message");
        log.log(Level.ALL, cause, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") >= 0);
        assertTrue(result.indexOf("some error") >= 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Throwable cause, Object message)</code>.
     * With null level.
     * Should return directly.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyNullLevel3() throws Exception {
        // log the message
        Exception cause = new Exception("some error");
        StringBuffer message = new StringBuffer("some message");
        log.log(null, cause, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") < 0);
        assertTrue(result.indexOf("some error") < 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Throwable cause, Object message)</code>.
     * With Level.OFF.
     * Should return directly.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyLevelOFF3() throws Exception {
        // log the message
        Exception cause = new Exception("some error");
        StringBuffer message = new StringBuffer("some message");
        log.log(Level.OFF, cause, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") < 0);
        assertTrue(result.indexOf("some error") < 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Throwable cause, Object message)</code>.
     * With null message.
     * Should log cause.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyNullMessage3() throws Exception {
        // log the message
        Exception cause = new Exception("some error");
        Object message = null;
        log.log(Level.ALL, cause, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") < 0);
        assertTrue(result.indexOf("some error") >= 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Throwable cause, Object message)</code>.
     * With null cause.
     * Should log the message only.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyNullCause3() throws Exception {
        // log the message
        Exception cause = null;
        StringBuffer message = new StringBuffer("some message");
        log.log(Level.ALL, cause, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") >= 0);
        assertTrue(result.indexOf("some error") < 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Object message)</code>.
     * With message.
     * Should log the message.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracy2() throws Exception {
        // log the message
        StringBuffer message = new StringBuffer("some message");
        log.log(Level.ALL, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") >= 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Object message)</code>.
     * With false useLoggerLayout.
     * Should log the message.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyFalseUseLoggerLayout2() throws Exception {
        // create a Log4jLog with false useLoggerLayout
        log = createLog4jLog(NAME, false);

        // log the message
        StringBuffer message = new StringBuffer("some message");
        log.log(Level.ALL, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") >= 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Object message)</code>.
     * With null level.
     * Should return directly.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyNullLevel2() throws Exception {
        // log the message
        StringBuffer message = new StringBuffer("some message");
        log.log(null, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") < 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Object message)</code>.
     * With Level.OFF.
     * Should return directly.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyLevelOFF2() throws Exception {
        // log the message
        StringBuffer message = new StringBuffer("some message");
        log.log(Level.OFF, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") < 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Object message)</code>.
     * With null message.
     * Should not log message.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyNullMessage2() throws Exception {
        // log the message
        Object message = null;
        log.log(Level.ALL, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") < 0);
    }
}
