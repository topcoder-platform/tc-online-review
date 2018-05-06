/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.log4j;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.or.ObjectRenderer;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RendererSupport;

import com.topcoder.util.log.Level;

/**
 * <p>
 * Unit tests for {@link Log4jLog} class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class Log4jLogTest extends TestCase {


    /**
     * <p>
     * Name of the log used for testing.
     * </p>
     */
    private static final String NAME_OF_LOG = "name";

    /**
     * <p>
     * Log4jLog instance used for testing.
     * </p>
     */
    private Log4jLog log;

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
     * Throwable instance used for testing.
     * </p>
     */
    private Throwable throwable;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(Log4jLogTest.class);
    }

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     */
    protected void setUp() {
        // change the System.err
        byteStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(byteStream));
        log = new Log4jLog(NAME_OF_LOG, false);
        Logger.getRootLogger().removeAllAppenders();
        Logger logger = Logger.getLogger(NAME_OF_LOG);
        ConsoleAppender appender = new ConsoleAppender(new SimpleLayout(), "System.err");
        logger.removeAllAppenders();
        logger.addAppender(appender);
        // set level
        logger.setLevel(org.apache.log4j.Level.INFO);
        throwable = new Throwable();
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     */
    protected void tearDown() {
        log = null;
        System.setErr(err);
        LoggerRepository repository = Logger.getLogger(NAME_OF_LOG).getLoggerRepository();
        RendererSupport rs = (RendererSupport) repository;
        rs.getRendererMap().clear();
        throwable = null;
    }

    /**
     * <p>
     * Tests constructor for Log4jLog(String, boolean) for accuracy.
     * </p>
     *
     * <p>
     * Verify that Log4jLog(String, boolean) is correct.
     * </p>
     */
    public void testLog4jLog() {
        assertNotNull("The Log4jLog instance should not be null.", log);
        assertEquals("The name of the log should be " + NAME_OF_LOG + ".", NAME_OF_LOG, log.getName());
        assertNotNull("The " + NAME_OF_LOG + " logger should exists.", LogManager.exists(NAME_OF_LOG));
    }

    /**
     * <p>
     * Tests constructor for Log4jLog(String, boolean) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the name is null and verifies that Log4jLog(String, boolean) is correct.
     * </p>
     */
    public void testLog4jLogWithNullName() {
        log = new Log4jLog(null, false);
        assertNotNull("The Log4jLog instance should not be null.", log);
        assertNull("The name of the log should be null.", log.getName());
    }

    /**
     * <p>
     * Tests method for log(Level, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Object) is correct. Note that useLoggerLayout is true now.
     * </p>
     */
    public void testLogLevelObjectWithTrueLoggerLayout() {
        log = new Log4jLog(NAME_OF_LOG, true);
        LoggerRepository repository = Logger.getLogger(NAME_OF_LOG).getLoggerRepository();
        RendererSupport rs = (RendererSupport) repository;
        // use the custom MockRenderer to render the Integer, only output the "Integer" String if useLoggerLayout
        // is true
        rs.getRendererMap().put(Integer.class, new MockRenderer());
        log.log(Level.FATAL, new Integer(123456789));
        assertTrue("The 'Integer' should be logged.", byteStream.toString().indexOf("Integer") != -1);
    }

    /**
     * <p>
     * Tests method for log(Level, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Object) is correct. Note that useLoggerLayout is false now.
     * </p>
     */
    public void testLogLevelObjectWithFalseLoggerLayout() {
        LoggerRepository repository = Logger.getLogger(NAME_OF_LOG).getLoggerRepository();
        RendererSupport rs = (RendererSupport) repository;
        // use the custom MockRenderer to render the Integer, only output the "Integer" String if useLoggerLayout
        // is true
        rs.getRendererMap().put(Integer.class, new MockRenderer());
        log.log(Level.FATAL, new Integer(123456789));
        assertTrue("The '123456789' should be logged.", byteStream.toString().indexOf("123456789") != -1);
    }

    /**
     * <p>
     * Tests method for log(Level, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and useLoggerLayout is false and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelObjectWithNullLevelWithFalseLoggerLayout() {
        log.log(null, new Integer(123456789));
        assertEquals("Nothing should be logged.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and useLoggerLayout is true and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelObjectWithNullLevelWithTrueLoggerLayout() {
        log = new Log4jLog(NAME_OF_LOG, true);
        log.log(null, new Integer(123456789));
        assertEquals("Nothing should be logged.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is OFF and useLoggerLayout is false and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelObjectWithOFFLevelWithFalseLoggerLayout() {
        log.log(Level.OFF, new Integer(123456789));
        assertEquals("Nothing should be logged.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is OFF and useLoggerLayout is true and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelObjectWithOFFLevelWithTrueLoggerLayout() {
        log = new Log4jLog(NAME_OF_LOG, true);
        log.log(Level.OFF, new Integer(123456789));
        assertEquals("Nothing should be logged.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the message is null and useLoggerLayout is false and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelObjectWithNullMessageWithFalseLoggerLayout() {
        log.log(Level.FATAL, null);
        assertTrue("The 'null' should be logged.", byteStream.toString().indexOf("null") != -1);
    }

    /**
     * <p>
     * Tests method for log(Level, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the message is null and useLoggerLayout is true and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelObjectWithNullMessageWithTrueLoggerLayout() {
        log = new Log4jLog(NAME_OF_LOG, true);
        log.log(Level.FATAL, null);
        assertTrue("The 'null' should be logged.", byteStream.toString().indexOf("null") != -1);
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Throwable, Object) is correct. Note that useLoggerLayout is true now.
     * </p>
     */
    public void testLogLevelThrowableObjectWithTrueLoggerLayout() {
        log = new Log4jLog(NAME_OF_LOG, true);
        LoggerRepository repository = Logger.getLogger(NAME_OF_LOG).getLoggerRepository();
        RendererSupport rs = (RendererSupport) repository;
        // use the custom MockRenderer to render the Integer, only output the "Integer" String if useLoggerLayout
        // is true
        rs.getRendererMap().put(Integer.class, new MockRenderer());
        log.log(Level.FATAL, throwable, new Integer(123456789));
        assertTrue("The 'Integer' should be logged.", byteStream.toString().indexOf("Integer") != -1);
        // check the stack trace of the throwable
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(stream));
        assertTrue("The throwable should be logged.", byteStream.toString().indexOf(stream.toString()) != -1);
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Throwable, Object) is correct. Note that useLoggerLayout is false now.
     * </p>
     */
    public void testLogLevelThrowableObjectWithFalseLoggerLayout() {
        LoggerRepository repository = Logger.getLogger(NAME_OF_LOG).getLoggerRepository();
        RendererSupport rs = (RendererSupport) repository;
        // use the custom MockRenderer to render the Integer, only output the "Integer" String if useLoggerLayout
        // is true
        rs.getRendererMap().put(Integer.class, new MockRenderer());
        log.log(Level.FATAL, throwable, new Integer(123456789));
        assertTrue("The '123456789' should be logged.", byteStream.toString().indexOf("123456789") != -1);
        // check the stack trace of the throwable
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(stream));
        assertTrue("The throwable should be logged.", byteStream.toString().indexOf(stream.toString()) != -1);
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Throwable, Object) is correct. Note that useLoggerLayout is true now.
     * </p>
     */
    public void testLogLevelThrowableObjectWithTrueLoggerLayoutWithNullThrowable() {
        log = new Log4jLog(NAME_OF_LOG, true);
        LoggerRepository repository = Logger.getLogger(NAME_OF_LOG).getLoggerRepository();
        RendererSupport rs = (RendererSupport) repository;
        // use the custom MockRenderer to render the Integer, only output the "Integer" String if useLoggerLayout
        // is true
        rs.getRendererMap().put(Integer.class, new MockRenderer());
        log.log(Level.FATAL, (Throwable) null, new Integer(123456789));
        String string = byteStream.toString();
        int index = string.indexOf("Integer");
        assertTrue("The 'Integer' should be logged.", index != -1);
        // check the stack trace of the throwable
        assertEquals("The throwable should not be logged.", 0,
                string.substring(index + "Integer".length(), string.length()).trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Throwable, Object) is correct. Note that useLoggerLayout is false now.
     * </p>
     */
    public void testLogLevelThrowableObjectWithFalseLoggerLayoutWithNullThrowable() {
        LoggerRepository repository = Logger.getLogger(NAME_OF_LOG).getLoggerRepository();
        RendererSupport rs = (RendererSupport) repository;
        // use the custom MockRenderer to render the Integer, only output the "Integer" String if useLoggerLayout
        // is true
        rs.getRendererMap().put(Integer.class, new MockRenderer());
        log.log(Level.FATAL, (Throwable) null, new Integer(123456789));
        String string = byteStream.toString();
        int index = string.indexOf("123456789");
        assertTrue("The '123456789' should be logged.", index != -1);
        // check the stack trace of the throwable
        // check the stack trace of the throwable
        assertEquals("The throwable should not be logged.", 0,
                string.substring(index + "123456789".length(), string.length()).trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and useLoggerLayout is false and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelThrowableObjectWithNullLevelWithFalseLoggerLayout() {
        log.log(null, throwable, new Integer(123456789));
        assertEquals("Nothing should be logged.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and useLoggerLayout is true and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelThrowableObjectWithNullLevelWithTrueLoggerLayout() {
        log = new Log4jLog(NAME_OF_LOG, true);
        log.log(null, throwable, new Integer(123456789));
        assertEquals("Nothing should be logged.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is OFF and useLoggerLayout is false and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelThrowableObjectWithOFFLevelWithFalseLoggerLayout() {
        log.log(Level.OFF, throwable, new Integer(123456789));
        assertEquals("Nothing should be logged.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is OFF and useLoggerLayout is true and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelThrowableObjectWithOFFLevelWithTrueLoggerLayout() {
        log = new Log4jLog(NAME_OF_LOG, true);
        log.log(Level.OFF, throwable, new Integer(123456789));
        assertEquals("Nothing should be logged.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the message is null and useLoggerLayout is false and verifies "null" will be logged.
     * </p>
     */
    public void testLogLevelThrowableObjectWithNullMessageWithFalseLoggerLayout() {
        log.log(Level.FATAL, throwable, (Object) null);
        assertTrue("The 'null' should be logged.", byteStream.toString().indexOf("null") != -1);
        // check the stack trace of the throwable
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(stream));
        assertTrue("The throwable should be logged.", byteStream.toString().indexOf(stream.toString()) != -1);
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the message is null and useLoggerLayout is true and verifies "null" will be logged.
     * </p>
     */
    public void testLogLevelThrowableObjectWithNullMessageWithTrueLoggerLayout() {
        log = new Log4jLog(NAME_OF_LOG, true);
        log.log(Level.FATAL, throwable, (Object) null);
        assertTrue("The 'null' should be logged.", byteStream.toString().indexOf("null") != -1);
        // check the stack trace of the throwable
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(stream));
        assertTrue("The throwable should be logged.", byteStream.toString().indexOf(stream.toString()) != -1);
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Throwable, String) is correct. Note that this method desn't use useLoggerLayout variable
     * and always use log4j's layout.
     * </p>
     */
    public void testLogLevelThrowableString() {
        log.log(Level.FATAL, throwable, "message");
        assertTrue("The 'message' should be logged.", byteStream.toString().indexOf("message") != -1);
        // check the stack trace of the throwable
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(stream));
        assertTrue("The throwable should be logged.", byteStream.toString().indexOf(stream.toString()) != -1);
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the throwable is null and verifies that log(Level, Throwable, String) is correct.
     * </p>
     */
    public void testLogLevelThrowableStringWithNullThrowable() {
        log.log(Level.FATAL, (Throwable) null, "message");
        String string = byteStream.toString();
        int index = string.indexOf("message");
        assertTrue("The 'message' should be logged.", index != -1);
        // check the stack trace of the throwable
        assertEquals("The throwable should not be logged.", 0,
                string.substring(index + "message".length(), string.length()).trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelThrowableStringWithNullLevel() {
        log.log(null, throwable, "message");
        assertEquals("Nothing should be logged.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is OFF and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelThrowableStringWithOFFLevel() {
        log.log(Level.OFF, throwable, "message");
        assertEquals("Nothing should be logged.", 0, byteStream.toString().trim().length());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the message is null and verifies "null" will be logged.
     * </p>
     */
    public void testLogLevelThrowableStringWithNullMessage() {
        log.log(Level.FATAL, throwable, (String) null);
        assertTrue("The 'null' should be logged.", byteStream.toString().indexOf("null") != -1);
        // check the stack trace of the throwable
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(stream));
        assertTrue("The throwable should be logged.", byteStream.toString().indexOf(stream.toString()) != -1);
    }


    /**
     * <p>
     * Tests method for isEnabled(Level) for accuracy.
     * </p>
     *
     * <p>
     * Verify that isEnabled(Level) is correct. Note that INFO level has been set already.
     * </p>
     */
    public void testIsEnabled() {
        assertTrue(log.isEnabled(Level.ERROR));
        assertTrue(log.isEnabled(Level.FATAL));
        assertTrue(log.isEnabled(Level.OFF));
        assertTrue(log.isEnabled(Level.WARN));
        assertTrue(log.isEnabled(Level.INFO));
        assertTrue(log.isEnabled(Level.CONFIG));

        assertFalse(log.isEnabled(Level.ALL));
        assertFalse(log.isEnabled(Level.FINEST));
        assertFalse(log.isEnabled(Level.TRACE));
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

    /**
     * <p>
     * Mock class for {@link ObjectRenderer} used for testing.
     * </p>
     *
     * <p>
     * Note that <code>doRender</code> method will always return "Integer" string.
     * </p>
     *
     * @author TCSDEVELOPER
     * @version 2.0
     */
    private class MockRenderer implements ObjectRenderer  {
        /**
         * <p>
         * Renders the object passed as parameter as a String.
         * </p>
         *
         * <p>
         * It will always return "Integer" string.
         * </p>
         *
         * @param o The object to render
         *
         * @return "Integer" string
         */
        public String doRender(Object o) {
            return "Integer";
        }
    }
}
