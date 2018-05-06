/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.accuracytests;

import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.FileHandler;

import com.topcoder.util.log.accuracytests.AccuracyTestsHelper;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.jdk14.Jdk14Log;
import com.topcoder.util.log.jdk14.Jdk14LogFactory;


import junit.framework.TestCase;

/**
 * <p>
 * Accuracy tests for <code>Jdk14Log</code>.
 * </p>
 *
 * @author tianniu
 * @version 2.0
 */
public class Jdk14LogAccuracyTests extends TestCase {

    /** Represents the log name. */
    private static String NAME = "jdk14log";

    /** Represents the file path. */
    private static String FILE = "test_files/Jdk14Log.txt";

    /** <code>Jdk14LogFactory</code> instance used for testing. */
    private Jdk14LogFactory logFactory;

    /** <code>Jdk14Log</code> instance used for testing. */
    private Jdk14Log log;

    /** <code>FileHandler</code> instance used for testing. */
    private FileHandler fileHandler;

    /**
     * <p>
     * Set up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        // clear file
        AccuracyTestsHelper.clearFile(FILE);
        
        logFactory = new Jdk14LogFactory();

        log = (Jdk14Log)logFactory.createLog(NAME);

        // clear all the handlers of the root logger
        Logger root = Logger.getLogger("");
        Handler[] handlers = root.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            root.removeHandler(handlers[i]);
        }

        // set up the configuration of corresponding logger
        Logger logger = Logger.getLogger(NAME);
        fileHandler = new FileHandler(FILE);
        logger.addHandler(fileHandler);
        logger.setLevel(java.util.logging.Level.ALL);
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

        // close the FileHandler
        Logger logger = Logger.getLogger(NAME);
        logger.removeHandler(fileHandler);
        if (fileHandler != null) {
            fileHandler.close();
        }

        // clear file
        AccuracyTestsHelper.clearFile(FILE);
    }

    /**
     * <p>
     * Accuracy test of constructor <code>Jdk14Log(String name)</code>.
     * With name.
     * Should create the instance successfully.
     * </p>
     */
    public void testCtorAccuracy() {
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

        logger.setLevel(java.util.logging.Level.CONFIG);
        assertTrue(log.isEnabled(Level.CONFIG));
        assertFalse(log.isEnabled(Level.TRACE));

        logger.setLevel(java.util.logging.Level.FINER);
        assertTrue(log.isEnabled(Level.FATAL));
        assertTrue(log.isEnabled(Level.TRACE));
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
     * Should return directly.
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
        //assertTrue(result.indexOf("some error") < 0);
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
     * Accuracy test of method <code>log(Level level, String messageFormat, Object[] args)</code>.
     * With valid parameters.
     * Should log the message.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracy3() throws Exception {
        // log the message
        log.log(Level.ALL, "This is a {0}.", new Object[]{"test"});

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("This is a test.") >= 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, String messageFormat, Object[] args)</code>.
     * With null level.
     * Should return directly.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyNullLevel3() throws Exception {
        // log the message
        log.log(null, "This is a {0}.", new Object[]{"test"});

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("This is a test.") < 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, String messageFormat, Object[] args)</code>.
     * With Level.OFF.
     * Should return directly.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyLevelOFF3() throws Exception {
        // log the message
        log.log(Level.OFF, "This is a {0}.", new Object[]{"test"});

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("This is a test.") < 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, String messageFormat, Object[] args)</code>.
     * With null messageFormat.
     * Should return directly.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLog_Accuracy_NullMessageFormat_3() throws Exception {
        // log the message
        log.log(Level.ALL, (String) null, new Object[]{"test"});

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("This is a test.") < 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, String messageFormat, Object[] args)</code>.
     * With null args.
     * Should log the message.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyNullArgs() throws Exception {
        // log the message
        log.log(Level.ALL, "This is a {0}.", (Object[]) null);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("This is a {0}.") >= 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, String messageFormat, Object[] args)</code>.
     * With empty args.
     * Should log the message.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyEmptyArgs() throws Exception {
        // log the message
        log.log(Level.ALL, "This is a {0}.", new Object[0]);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("This is a {0}.") >= 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, String messageFormat, Object[] args)</code>.
     * With args contains null elements, messageFormat not require args.
     * Should log the message.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyArgsContainsNull1() throws Exception {
        // log the message
        log.log(Level.ALL, "This is a test.", new Object[]{null});

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("This is a test.") >= 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, String messageFormat, Object[] args)</code>.
     * With args contains null elements, but messageFormat requires args.
     * Should not log the message.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLogAccuracyArgsContainsNull2() throws Exception {
        // log the message
        log.log(Level.ALL, "This is a {0}.", new Object[]{null});

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("This is a {0}.") < 0);
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, String messageFormat, Object arg1)</code>.
     * With valid parameters.
     * Should log the message.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLog2Accuracy() throws Exception {
        // log the message
        log.log(Level.ALL, "This is a {0}.", "test");

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("This is a test.") >= 0);
    }
}
