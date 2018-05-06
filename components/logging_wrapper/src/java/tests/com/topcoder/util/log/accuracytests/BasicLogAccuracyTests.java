/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.accuracytests;

import com.topcoder.util.log.Level;
import com.topcoder.util.log.basic.BasicLog;
import com.topcoder.util.log.basic.BasicLogFactory;

import java.io.FileOutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy tests for <code>BasicLog</code>.
 * </p>
 *
 * @author tianniu
 * @version 2.0
 */
public class BasicLogAccuracyTests extends TestCase {

    /** Represents the log name. */
    private static String NAME = "basiclog";

    /** Represents the file path. */
    private static String FILE = "test_files/BasicLog.txt";

    /** <code>BasicLogFactory</code> instance used for testing. */
    private BasicLogFactory logFactory;

    /** <code>BasicLog</code> instance used for testing. */
    private BasicLog log;

    /** <code>PrintStream</code> instance used for testing. */
    private PrintStream printStream;

    /**
     * <p>
     * Set up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        printStream = new PrintStream(new FileOutputStream(FILE));

        logFactory = new BasicLogFactory(printStream);

        log = (BasicLog) logFactory.createLog(NAME);
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

        // close the print stream
        if (printStream != null) {
            printStream.close();
        }

        // clear file
        AccuracyTestsHelper.clearFile(FILE);
    }

    /**
     * <p>
     * Accuracy test of constructor <code>BasicLog(String name, PrintStream printStream)</code>.
     * With name and printStream.
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
        assertFalse("Should return the proper value.", log.isEnabled(null));
        assertTrue("Should return the proper value.", log.isEnabled(Level.ALL));
    }

    /**
     * <p>
     * Accuracy test of method <code>log(Level level, Throwable cause, String message)</code>.
     * With cause and message.
     * Should log the cause and message.
     * </p>
     */
    public void testLogAccuracy() {
        // log the message
        Exception cause = new Exception("some error");
        String message = "some message";
        log.log(Level.ALL, cause, message);

        // check the result
        String result = AccuracyTestsHelper.getFileContent(FILE);
        assertTrue(result.indexOf("some message") >= 0);
        assertTrue(result.indexOf("some error") >= 0);
    }

}
