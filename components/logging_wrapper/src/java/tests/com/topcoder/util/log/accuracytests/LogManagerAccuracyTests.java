/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.accuracytests;

import com.topcoder.util.log.basic.BasicLogFactory;
import com.topcoder.util.log.basic.BasicLog;
import com.topcoder.util.log.jdk14.Jdk14LogFactory;
import com.topcoder.util.log.jdk14.Jdk14Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.log.Log;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy tests for <code>LogManager</code>.
 * </p>
 *
 * @author tianniu
 * @version 2.0
 */
public class LogManagerAccuracyTests extends TestCase {

    /**
     * <p>
     * Accuracy test of method <code>getObjectFormatter()</code>.
     * Should return the proper value.
     * </p>
     */
    public void testGetObjectFormatterAccuracy() {
        assertNotNull("Should return the proper value.", LogManager.getObjectFormatter());
    }

    /**
     * <p>
     * Accuracy test of method <code>setLogFactory(LogFactory factory)</code>.
     * With valid factory.
     * Should set the logFactory.
     * </p>
     */
    public void testSetLogFactoryAccuracy() {
        // set LogFactory to Jdk14LogFactory
        LogManager.setLogFactory(new Jdk14LogFactory());

        // check the result
        assertTrue(LogManager.getLog() instanceof Jdk14Log);

        // set LogFactory back to BasicLogFactory
        LogManager.setLogFactory(new BasicLogFactory());
    }

    /**
     * <p>
     * Accuracy test of method <code>getLog()</code>.
     * Should return the proper value.
     * </p>
     */
    public void testGetLog1Accuracy() {
        assertTrue(LogManager.getLog() instanceof BasicLog);
        assertEquals(LogManager.DEFAULT_NAME, ((BasicLog) LogManager.getLog()).getName());
    }

    /**
     * <p>
     * Accuracy test of method <code>getLog(String name)</code>.
     * With name.
     * Should return the proper value.
     * </p>
     */
    public void testGetLog2Accuracy() {
        Log log = LogManager.getLog("some name");
        assertTrue(log instanceof BasicLog);
        assertEquals("some name", ((BasicLog) log).getName());
    }

    /**
     * <p>
     * Accuracy test of method <code>getLogWithExceptions()</code>.
     * Should return the proper value.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetLogWithExceptions1Accuracy() throws Exception {
        Log log = LogManager.getLogWithExceptions();
        assertTrue(log instanceof BasicLog);
        assertEquals(LogManager.DEFAULT_NAME, ((BasicLog) log).getName());
    }

    /**
     * <p>
     * Accuracy test of method <code>getLogWithExceptions(String name)</code>.
     * With name.
     * Should return the proper value.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetLogWithExceptions2Accuracy() throws Exception {
        Log log = LogManager.getLogWithExceptions("some name");
        assertTrue(log instanceof BasicLog);
        assertEquals("some name", ((BasicLog) log).getName());
    }

}
