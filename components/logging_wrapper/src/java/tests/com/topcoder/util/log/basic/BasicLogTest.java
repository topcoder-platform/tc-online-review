/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.basic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.log.Level;

/**
 * <p>
 * Unit tests for {@link BasicLog} class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class BasicLogTest extends TestCase {

    /**
     * <p>
     * BasicLog instance used for testing.
     * </p>
     */
    private BasicLog log;

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
        return new TestSuite(BasicLogTest.class);
    }

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     */
    protected void setUp() {
        byteStream = new ByteArrayOutputStream();
        log = new BasicLog("Name", new PrintStream(byteStream));
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     */
    protected void tearDown() {
        byteStream = null;
        log = null;
    }



    /**
     * <p>
     * Tests constructor for BasicLog(String, PrintStream) for accuracy.
     * </p>
     *
     * <p>
     * Verify that BasicLog(String, PrintStream) is correct.
     * </p>
     */
    public void testBasicLog() {
        assertNotNull("The BasicLog instance should not be null.", log);
    }

    /**
     * <p>
     * Tests constructor for BasicLog(String, PrintStream) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the printStream is null and expects IllegalArgumentException.
     * </p>
     */
    public void testBasicLogWithNullPrintStream() {
        try {
            new BasicLog("Name", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method for log(Level,Throwable,String) for accuracy.
     * <p>
     *
     * <p>
     * Verify that log(Level,Throwable,String) is correct.
     * </p>
     */
    public void testLogLevelThrowableString() {
        Throwable throwable = new Exception();
        log.log(Level.FATAL, throwable, "message");
        String out = byteStream.toString();
        assertTrue("The message should be printed.", out.startsWith("message"));
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(outStream));
        assertTrue("The stack trace of throwable should be printed.", out.endsWith(outStream.toString()));
    }

    /**
     * <p>
     * Tests method for log(Level,Throwable,String) for accuracy.
     * <p>
     *
     * <p>
     * It tests the case that the Throwable is null and verifies that log(Level,Throwable,String) is correct.
     * </p>
     */
    public void testLogLevelThrowableStringWithNullThrowable() {
        Throwable throwable = new Exception();
        log.log(Level.FATAL, (Throwable) null, "message");
        String out = byteStream.toString();
        assertTrue("The message should be printed.", out.startsWith("message"));
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(outStream));
        assertFalse("The stack trace of throwable should be printed.", out.endsWith(outStream.toString()));
    }

    /**
     * <p>
     * Tests method for isEnabled(Level) for accuracy.
     * </p>
     *
     * <p>
     * Verify that isEnabled(Level) is correct and if the level is not null, true should be returned.
     * </p>
     */
    public void testIsEnabled() {
        assertTrue(log.isEnabled(Level.DEBUG));
    }

    /**
     * <p>
     * Tests method for isEnabled(Level) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies false should be returned, instead of true.
     * </p>
     */
    public void testIsEnabledWithNullLevel() {
        assertFalse(log.isEnabled(null));
    }
}
