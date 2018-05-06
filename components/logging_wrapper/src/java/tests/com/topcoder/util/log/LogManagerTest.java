/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

import com.topcoder.util.format.ObjectFormatter;
import com.topcoder.util.log.basic.BasicLog;
import com.topcoder.util.log.basic.BasicLogFactory;
import com.topcoder.util.log.jdk14.Jdk14Log;
import com.topcoder.util.log.jdk14.Jdk14LogFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit tests for {@link LogManager} class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class LogManagerTest extends TestCase {

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(LogManagerTest.class);
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     */
    protected void tearDown() {
        LogManager.setLogFactory(new BasicLogFactory());
    }

    /**
     * <p>
     * Tests method for setLogFactory(LogFactory) for accuracy.
     * </p>
     *
     * <p>
     * Verify that setLogFactory(LogFactory) is correct.
     * </p>
     */
    public void testSetLogFactory() {
        LogFactory logFactory = new Jdk14LogFactory();
        LogManager.setLogFactory(logFactory);
        Log log = LogManager.getLog();
        assertTrue("The log should be of Jdk14Log.", log instanceof Jdk14Log);
    }

    /**
     * <p>
     * Tests method for setLogFactory(LogFactory) for failure.
     * </p>
     *
     * <p>
     * It tests the case that the LogFactory is null and expects IllegalArgumentException.
     * </p>
     */
    public void testSetLogFactoryWithNullLogFactory() {
        try {
            LogManager.setLogFactory(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method for getObjectFormatter() for accuracy.
     * </p>
     *
     * <p>
     * Verify that getObjectFormatter() is correct and the instance should not be null.
     * </p>
     */
    public void testGetObjectFormatter() {
        ObjectFormatter formatter = LogManager.getObjectFormatter();
        assertNotNull("The ObjectFormatter instance should not be null.",  formatter);
    }

    /**
     * <p>
     * Tests method for getLog() for accuracy.
     * </p>
     *
     * <p>
     * Verify that getLog() is correct.
     * </p>
     */
    public void testGetLog() {
        Log log = LogManager.getLog();
        assertTrue("The log should be of BasicLog.", log instanceof BasicLog);
        assertEquals("The name of the log should be " + LogManager.DEFAULT_NAME,
                LogManager.DEFAULT_NAME, ((AbstractLog) log).getName());
    }

    /**
     * <p>
     * Tests method for getLog() for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that an Exception will be thrown in creating the log and verifies that getLog() is
     * correct.
     * </p>
     */
    public void testGetLogWithException() {
        MockLogFactory logFactory = new MockLogFactory();
        logFactory.setThrownLogException(false);
        // set the MockLogFactory
        LogManager.setLogFactory(new MockLogFactory());
        Log log = LogManager.getLog();
        assertTrue("The log should be of BasicLog.", log instanceof BasicLog);
        assertEquals("The name of the log should be " + LogManager.DEFAULT_NAME,
                LogManager.DEFAULT_NAME, ((AbstractLog) log).getName());
    }

    /**
     * <p>
     * Tests method for getLog(String) for accuracy.
     * </p>
     *
     * <p>
     * Verify that getLog(String) is correct.
     * </p>
     */
    public void testGetLogString() {
        Log log = LogManager.getLog("nameOfLog");
        assertTrue("The log should be of BasicLog.", log instanceof BasicLog);
        assertEquals("The name of the log should be nameOfLog", "nameOfLog", ((AbstractLog) log).getName());
    }

    /**
     * <p>
     * Tests method for getLog(String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that an Exception will be thrown in creating the log and verifies that getLog(String) is
     * correct.
     * </p>
     */
    public void testGetLogStringWithException() {
        MockLogFactory logFactory = new MockLogFactory();
        logFactory.setThrownLogException(false);
        // set the MockLogFactory
        LogManager.setLogFactory(new MockLogFactory());
        Log log = LogManager.getLog("nameOfLog");
        assertTrue("The log should be of BasicLog.", log instanceof BasicLog);
        assertEquals("The name of the log should be nameOfLog", "nameOfLog", ((AbstractLog) log).getName());
    }

    /**
     * <p>
     * Tests method for getLogWithExceptions() for accuracy.
     * </p>
     *
     * <p>
     * Verify that getLogWithExceptions() is correct.
     * </p>
     *
     * @throws LogException to Junit
     */
    public void testGetLogWithExceptions() throws LogException {
        Log log = LogManager.getLogWithExceptions();
        assertTrue("The log should be of BasicLog.", log instanceof BasicLog);
        assertEquals("The name of the log should be " + LogManager.DEFAULT_NAME,
                LogManager.DEFAULT_NAME, ((AbstractLog) log).getName());
    }

    /**
     * <p>
     * Tests method for getLogWithExceptions() for failure.
     * </p>
     *
     * <p>
     * It tests the case that the logFactory#createLog method will throw LogException and expects LogException.
     * </p>
     */
    public void testGetLogWithExceptionsWithLogException() {
        MockLogFactory logFactory = new MockLogFactory();
        logFactory.setThrownLogException(true);
        LogManager.setLogFactory(new MockLogFactory());
        try {
            LogManager.getLogWithExceptions();
            fail("LogException is expected.");
        } catch (LogException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method for getLogWithExceptions() for failure.
     * </p>
     *
     * <p>
     * It tests the case that the logFactory#createLog method will throw RuntimeException and expects LogException.
     * </p>
     */
    public void testGetLogWithExceptionsWithRuntimeException() {
        MockLogFactory logFactory = new MockLogFactory();
        logFactory.setThrownLogException(false);
        LogManager.setLogFactory(logFactory);
        try {
            LogManager.getLogWithExceptions();
            fail("LogException is expected.");
        } catch (LogException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method for getLogWithExceptions(String) for accuracy.
     * </p>
     *
     * <p>
     * Verify that getLogWithExceptions(String) is correct.
     * </p>
     *
     * @throws LogException to Junit
     */
    public void testGetLogWithExceptionsString() throws LogException {
        Log log = LogManager.getLogWithExceptions("NameOfLog");
        assertTrue("The log should be of BasicLog.", log instanceof BasicLog);
        assertEquals("The name of the log should be NameOfLog", "NameOfLog", ((AbstractLog) log).getName());
    }

    /**
     * <p>
     * Tests method for getLogWithExceptions(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that the logFactory#createLog method will throw LogException and expects LogException.
     * </p>
     */
    public void testGetLogWithExceptionsStringWithLogException() {
        MockLogFactory logFactory = new MockLogFactory();
        logFactory.setThrownLogException(true);
        LogManager.setLogFactory(new MockLogFactory());
        try {
            LogManager.getLogWithExceptions("Name");
            fail("LogException is expected.");
        } catch (LogException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method for getLogWithExceptions(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that the logFactory#createLog method will throw RuntimeException and expects LogException.
     * </p>
     */
    public void testGetLogWithExceptionsStringWithRuntimeException() {
        MockLogFactory logFactory = new MockLogFactory();
        logFactory.setThrownLogException(false);
        LogManager.setLogFactory(new MockLogFactory());
        try {
            LogManager.getLogWithExceptions("Name");
            fail("LogException is expected.");
        } catch (LogException e) {
            // good
        }
    }
}
