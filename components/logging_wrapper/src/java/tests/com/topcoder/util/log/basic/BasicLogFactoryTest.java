/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.basic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.topcoder.util.log.AbstractLog;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit tests for {@link BasicLogFactory} class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class BasicLogFactoryTest extends TestCase {
    /**
     * <p>
     * BasicLogFactory instance used for testing.
     * </p>
     */
    private BasicLogFactory logFactory;

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
        return new TestSuite(BasicLogFactoryTest.class);
    }

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     */
    protected void setUp() {
        byteStream = new ByteArrayOutputStream();
        logFactory = new BasicLogFactory(new PrintStream(byteStream));
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     */
    protected void tearDown() {
        byteStream = null;
        logFactory = null;
    }

    /**
     * <p>
     * Tests constructor for BasicLogFactory() for accuracy.
     * </p>
     *
     * <p>
     * Verify that BasicLogFactory() is correct.
     * </p>
     */
    public void testBasicLogFactory() {
        logFactory = new BasicLogFactory();
        assertNotNull("The BasicLogFactory should not be null.", logFactory);
        Log log = logFactory.createLog("name");
        assertTrue("The log should be of BasicLog", log instanceof BasicLog);
        assertEquals("The name of the log should be 'name'.", "name", ((AbstractLog) log).getName());
    }

    /**
     * <p>
     * Tests method for BasicLogFactory(PrintStream) for accuracy.
     * </p>
     *
     * <p>
     * Verify that BasicLogFactory(PrintStream) is correct.
     * </p>
     */
    public void testBasicLogFactoryPrintStream() {
        assertNotNull("The BasicLogFactory should not be null.", logFactory);
        Log log = logFactory.createLog("name");
        assertTrue("The log should be of BasicLog", log instanceof BasicLog);
        assertEquals("The name of the log should be 'name'.", "name", ((AbstractLog) log).getName());
        // check the print stream
        log.log(Level.INFO, (Throwable) null, "message");
        assertTrue("The message should be printed.", byteStream.toString().startsWith("message"));
    }

    /**
     * <p>
     * Tests method for BasicLogFactory(PrintStream) for failure.
     * </p>
     *
     * <p>
     * It tests the case that PrintStream is null and expects IllegalArgumentException.
     * </p>
     */
    public void testBasicLogFactoryPrintStreamWithNullPrintStream() {
        try {
            new BasicLogFactory(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method for createLog(String) for accuracy.
     * </p>
     *
     * <p>
     * Verify that createLog(String) is correct.
     * </p>
     */
    public void testCreateLog() {
        Log log = logFactory.createLog("name");
        assertTrue("The log should be of BasicLog", log instanceof BasicLog);
        assertEquals("The name of the log should be 'name'.", "name", ((AbstractLog) log).getName());
        // check the print stream
        log.log(Level.INFO, (Throwable) null, "message");
        assertTrue("The message should be printed.",  byteStream.toString().startsWith("message"));
    }
}
