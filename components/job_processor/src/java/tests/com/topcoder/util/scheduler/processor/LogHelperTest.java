/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

import com.topcoder.util.log.LogManager;
import junit.framework.TestCase;

import java.util.Iterator;


/**
 * Test case for LogHelper.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class LogHelperTest extends TestCase {
    /** Default log used in this test. */
    private Log log;

    /**
     * Test {@link LogHelper#log(Log, Level, String, Object)}. The message should be log to given logger.
     */
    public void testLog() {
        LogHelper.log(log, Level.ERROR, "someMethod()", "msg");
    }

    /**
     * Test {@link LogHelper#logEntry(Log, String)}. The message should be log to given logger.
     */
    public void testLogEntry() {
        LogHelper.logEntry(log, "someMethod()");
    }

    /**
     * Test {@link LogHelper#logEntry(Log, String)} with null as log, the message should be printed to
     * System.out.
     */
    public void testLogEntryNullLog() {
        LogHelper.logEntry(null, "someMethod()");
    }

    /**
     * Test {@link LogHelper#logExit(Log, String)}. The message should be log to given logger.
     */
    public void testLogExit() {
        LogHelper.logExit(log, "someMethod()");
    }

    /**
     * Test {@link LogHelper#logExit(Log, String)} with null as log, the message should be printed to
     * System.out.
     */
    public void testLogExitNullLog() {
        LogHelper.logExit(null, "someMethod()");
    }

    /**
     * Test {@link LogHelper#log(Log, Level, String, Object)} with null as log, the message should be printed
     * to System.out.
     */
    public void testLogNullLog() {
        LogHelper.log(null, Level.ERROR, "someMethod()", "msg");
    }

    /**
     * Test {@link LogHelper#logError(Log, String, Object)}. The message should be log to given logger.
     */
    public void testloggerror() {
        LogHelper.logError(log, "someMethod()", "msg");
    }

    /**
     * Test {@link LogHelper#logError(Log, String, String)} with null as log, the message should be printed to
     * System.out.
     */
    public void testloggerrorNullLog() {
        LogHelper.logError(null, "someMethod()", "msg");
    }

    /**
     * Sets up test environment.
     *
     * @throws Exception to juint
     */
    protected void setUp() throws Exception {
        super.setUp();
        ConfigManager.getInstance().add("log.xml");
        log = LogManager.getLog();
    }

    /**
     * Clears test environment.
     *
     * @throws Exception to juint
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
    }
}
