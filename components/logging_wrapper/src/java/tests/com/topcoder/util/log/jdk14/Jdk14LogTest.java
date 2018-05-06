/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.jdk14;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.log.Level;

/**
 * <p>
 * Unit tests for {@link Jdk14Log} class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class Jdk14LogTest extends TestCase {

    /**
     * <p>
     * Name of the log used for testing.
     * </p>
     */
    private static final String NAME_OF_LOG = "name";

    /**
     * <p>
     * Jdk14Log instance used for testing.
     * </p>
     */
    private Jdk14Log log;

    /**
     * <p>
     * System.err instance used for testing.
     * </p>
     */
    private PrintStream err = System.err;

    /**
     * <p>
     * ByteArrayOutputStream instance used for testing.
     * </p>
     */
    private ByteArrayOutputStream byteStream;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(Jdk14LogTest.class);
    }

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     */
    protected void setUp() {
        log = new Jdk14Log(NAME_OF_LOG);
        Logger logger = Logger.getLogger(NAME_OF_LOG);
        logger.setLevel(java.util.logging.Level.INFO);
        // change the System.err
        byteStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(byteStream));
        logger.addHandler(new ConsoleHandler());
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     */
    protected void tearDown() {
        log = null;
        System.setErr(err);
        Logger logger = Logger.getLogger(NAME_OF_LOG);
        // clear the handlers
        Handler[] handlers = logger.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            logger.removeHandler(handlers[i]);
        }
    }

    /**
     * <p>
     * Tests constructor for Jdk14Log(String) for accuracy.
     * </p>
     *
     * <p>
     * Verify that Jdk14Log(String) is correct.
     * </p>
     */
    public void testJdk14Log() {
        assertNotNull("The Jdk14Log instance should not be null.", log);
        assertEquals("The name of the log should be " + NAME_OF_LOG + ".", NAME_OF_LOG, log.getName());
    }

    /**
     * <p>
     * Tests constructor for Jdk14Log(String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the name is null and verifies that Jdk14Log(String) is correct.
     * </p>
     */
    public void testJdk14LogWithNullName() {
        log = new Jdk14Log(null);
        assertNotNull("The Jdk14Log instance should not be null.", log);
        assertNull("The name of the log should be null", log.getName());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, String, Object) is correct.
     * </P>
     */
    public void testLogLevelStringObject() {
        log.log(Level.FATAL, "message {0}", new Integer(10));
        assertTrue("The 'message 10' should be printed.", byteStream.toString().indexOf("message 10") != -1);
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the message is null and verifies that log(Level, String, Object) is correct.
     * </P>
     */
    public void testLogLevelStringObjectWithNullMessage() {
        log.log(Level.FATAL, (String) null, new Integer(10));
        assertEquals("The message should not be printed.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies that log(Level, String, Object[]) is correct.
     * </P>
     */
    public void testLogLevelStringObjectWithNullLevel() {
        log.log(null, "message", new Integer(10));
        assertEquals("The message should not be printed.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is OFF and verifies that log(Level, String, Object[]) is correct.
     * </P>
     */
    public void testLogLevelStringObjectWithOFFlLevel() {
        log.log(Level.OFF, "message", new Integer(10));
        assertEquals("The message should not be printed.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, String, Object[]) is correct.
     * </P>
     */
    public void testLogLevelStringObjectArray() {
        log.log(Level.FATAL, "message {0} {1}", new Object[] {new Integer(10), new Integer(20)});
        assertTrue("The 'message 10 20' should be printed.", byteStream.toString().indexOf("message 10 20") != -1);
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the message is null and verifies that log(Level, String, Object[]) is correct.
     * </P>
     */
    public void testLogLevelStringObjectArrayWithNullMessage() {
        log.log(Level.FATAL, (String) null, new Object[] {new Integer(10), new Integer(20)});
        assertEquals("The message should not be printed.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies that log(Level, String, Object[]) is correct.
     * </P>
     */
    public void testLogLevelStringObjectArrayWithNullLevel() {
        log.log(null, "message", new Object[] {new Integer(10), new Integer(20)});
        assertEquals("The message should not be printed.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is OFF and verifies that log(Level, String, Object[]) is correct.
     * </P>
     */
    public void testLogLevelStringObjectArrayWithOFFLevel() {
        log.log(Level.OFF, "message", new Object[] {new Integer(10), new Integer(20)});
        assertEquals("The message should not be printed.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Throwable, String) is correct.
     * </P>
     */
    public void testLogLevelThrowableString() {
        log.log(Level.FATAL, new Exception(), "message");
        assertTrue("The 'message' should be printed.", byteStream.toString().indexOf("message") != -1);
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the exception is null and verifies that log(Level, Throwable, String) is correct.
     * </P>
     */
    public void testLogLevelThrowableStringWithNullException() {
        log.log(Level.FATAL, (Throwable) null, "message");
        String string = byteStream.toString();
        int index = string.indexOf("message");
        assertTrue("The 'message' should be logged.", index != -1);
        // check the stack trace of the throwable
        assertEquals("The throwable should not be logged.", 0,
                string.substring(index + "Integer".length(), string.length()).trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the message is null and verifies that log(Level, Throwable, String) is correct.
     * </P>
     */
    public void testLogLevelThrowableStringWithNullMessage() {
        log.log(Level.FATAL, new Exception(), (String) null);
        assertEquals("The message should not be printed.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies that log(Level, Throwable, String) is correct.
     * </P>
     */
    public void testLogLevelThrowableStringWithNullLevel() {
        log.log(null, new Exception(), "message");
        assertEquals("The message should not be printed.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is OFF and verifies that log(Level, Throwable, String) is correct.
     * </P>
     */
    public void testLogLevelThrowableStringWithOFFLevel() {
        log.log(Level.OFF, new Exception(), "message");
        assertEquals("The message should not be printed.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for isEnabled(Level) for accuracy.
     * </p>
     *
     * <p>
     * Verify that isEnabled(Level is correct.
     * </p>
     */
    public void testIsEnabled() {
        assertTrue(log.isEnabled(Level.ERROR));
        assertTrue(log.isEnabled(Level.FATAL));
        assertTrue(log.isEnabled(Level.OFF));
        assertTrue(log.isEnabled(Level.WARN));
        assertTrue(log.isEnabled(Level.INFO));

        assertFalse(log.isEnabled(Level.ALL));
        assertFalse(log.isEnabled(Level.FINEST));
        assertFalse(log.isEnabled(Level.TRACE));
        assertFalse(log.isEnabled(Level.CONFIG));
        assertFalse(log.isEnabled(Level.DEBUG));
    }

    /**
     * <p>
     * Tests method for isEnabled(Level) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies that isEnabled(Level) is correct and false should be
     * returned.
     * </p>
     */
    public void testIsEnabledWithNullLevel() {
        assertFalse(log.isEnabled(null));
    }
}
