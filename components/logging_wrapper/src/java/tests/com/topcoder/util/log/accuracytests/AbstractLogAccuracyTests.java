/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.accuracytests;

import com.topcoder.util.log.AbstractLog;
import com.topcoder.util.log.Level;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy tests for <code>AbstractLog</code>.
 * </p>
 *
 * @author tianniu
 * @version 2.0
 */
public class AbstractLogAccuracyTests extends TestCase {

    /**
     * <code>AbstractLog</code> instance used for testing.
     */
    private AbstractLog log;

    /**
     * a log name string for test.
     */
    private String testLogName = "testLogname";

    /**
     * <p>
     * Set up the test environment.
     * </p>
     */
    protected void setUp() {
        log = new LogForTest(testLogName);
    }

    /**
     * <p>
     * Release the test environment.
     * </p>
     */
    protected void tearDown() {
        log = null;
    }

    /**
     * <p>
     * Accuracy test of constructor <code>AbstractLog(String name)</code>.
     * With name.
     * Should create the instance successfully.
     * </p>
     */
    public void testCtorAccuracy() {
        assertNotNull("Should create the instance successfully.", log);
        assertEquals(testLogName, log.getName());
    }

    /**
     * <p>
     * Accuracy test of method <code>getName()</code>.
     * Should return the proper value.
     * </p>
     */
    public void testGetNameAccuracy() {
        assertEquals("Should return the proper value.", testLogName, log.getName());
    }

    /**
     * <p>
     * Accuracy test of method
     * <code>log(Level level, String messageFormat, Object arg1, Object arg2)</code>.
     * Message should be logged.
     * </p>
     */
    public void testLogArrayAccuracy2() {
        log.log(Level.ALL, "This is a {0}.", "test1", "test2");
        assertTrue("Message should be logged.", ((LogForTest) log).isLogged());
    }

    /**
     * <p>
     * Accuracy test of method
     * <code>log(Level level, String messageFormat, Object arg1, Object arg2, Object arg3)</code>.
     * Message should be logged.
     * </p>
     */
    public void testLogArrayAccuracy3() {
        log.log(Level.ALL, "This is a {0}.", "test1", "test2", "test3");
        assertTrue("Message should be logged.", ((LogForTest) log).isLogged());
    }

    /**
     * <p>
     * Accuracy test of method
     * <code>log(Level level, String messageFormat, Object[] args)</code>.
     * Message should be logged.
     * </p>
     */
    public void testLogArrayAccuracy4() {
        log.log(Level.ALL, "This is a {0}.", new Object[]{"test"});
        assertTrue("Message should be logged.", ((LogForTest) log).isLogged());
    }

    /**
     * <p>
     * Accuracy test of method
     * <code>log(Level level, Throwable cause, String messageFormat, Object arg1)</code>.
     * Message should be logged.
     * </p>
     */
    public void testLogArrayAccuracy5() {
        log.log(Level.ALL, new Exception(),
                "This is a {0}.", "test1");
        assertTrue("Message should be logged.", ((LogForTest) log).isLogged());
    }

    /**
     * <p>
     * Accuracy test of method
     * <code>log(Level level, Throwable cause, String messageFormat, Object arg1, Object arg2)</code>.
     * Message should be logged.
     * </p>
     */
    public void testLogArrayAccuracy6() {
        log.log(Level.ALL, new Exception(),
                "This is a {0}.", "test1", "test2");
        assertTrue("Message should be logged.", ((LogForTest) log).isLogged());
    }

    /**
     * <p>
     * Accuracy test of method
     * <code>log(Level level, Throwable cause, String messageFormat, Object arg1, Object arg2, Object arg3)</code>.
     * Message should be logged.
     * </p>
     */
    public void testLogArrayAccuracy7() {
        log.log(Level.ALL, new Exception(),
                "This is a {0}.", "test1", "test2", "test3");
        assertTrue("Message should be logged.", ((LogForTest) log).isLogged());
    }

    /**
     * <p>
     * Accuracy test of method
     * <code>log(Level level, Throwable cause, String messageFormat, Object[] args)</code>.
     * With null level.
     * Nothing should be logged.
     * </p>
     */
    public void testLogArrayAccuracyNullLevel8() {
        log.log(null, new Exception(),
                "This is a {0}.", new Object[]{"test"});
        assertFalse("Nothing should be logged.", ((LogForTest) log).isLogged());
    }

    /**
     * <p>
     * Accuracy test of method
     * <code>log(Level level, Throwable cause, String messageFormat, Object[] args)</code>.
     * With null cause.
     * Message should be logged.
     * </p>
     */
    public void testLogArrayAccuracyNullCuase8() {
        log.log(Level.ALL, (Throwable) null,
                "This is a {0}.", new Object[]{"test"});
        assertTrue("Message should be logged.", ((LogForTest) log).isLogged());
    }

    /**
     * <p>
     * Accuracy test of method
     * <code>log(Level level, Throwable cause, String messageFormat, Object[] args)</code>.
     * With null messageFormat.
     * Nothing should be logged.
     * </p>
     */
    public void testLogArrayAccuracyNullMessageFormat8() {
        log.log(Level.ALL, new Exception(),
                null, new Object[]{"test"});
        assertFalse("Nothing should be logged.", ((LogForTest) log).isLogged());
    }

    /**
     * <p>
     * Accuracy test of method
     * <code>log(Level level, Throwable cause, String messageFormat, Object[] args)</code>.
     * With invalid messageFormat.
     * Nothing should be logged. This is because <tt>MessageFormat#format</tt> throws exception.
     * </p>
     */
    public void testLogArrayAccuracyEmptyMessageFormat8() {
        log.log(Level.ALL, new Exception(),
                "{", new Object[]{"test"});
        assertFalse("Nothing should be logged.", ((LogForTest) log).isLogged());
    }

    /**
     * <p>
     * Accuracy test of method
     * <code>log(Level level, Throwable cause, String messageFormat, Object[] args)</code>.
     * With empty args.
     * Message should be logged.
     * </p>
     */
    public void testLogArrayAccuracyEmptyArgs8() {
        log.log(Level.ALL, new Exception(),
                "This is a {0}", new Object[0]);
        assertTrue("Message should be logged.", ((LogForTest) log).isLogged());
    }

    /**
     * <p>
     * Accuracy test of method
     * <code>log(Level level, Throwable cause, String messageFormat, Object[] args)</code>.
     * With args contains null elements.
     * Message should be logged.
     * </p>
     */
    public void testLogArrayAccuracyArgsContainsNull8() {
        log.log(Level.ALL, new Exception(),
                "This is a {0}", new Object[]{null});
        assertTrue("Message should be logged.", ((LogForTest) log).isLogged());
    }

    /**
     * <p>
     * Accuracy test of method
     * <code>log(Level level, Throwable cause, String messageFormat, Object[] args)</code>.
     * Message should be logged.
     * </p>
     */
    public void testLogArrayAccuracy8() {
        log.log(Level.ALL, new Exception(),
                "This is a {0}.", new Object[]{"test"});
        assertTrue("Message should be logged.", ((LogForTest) log).isLogged());
    }

}