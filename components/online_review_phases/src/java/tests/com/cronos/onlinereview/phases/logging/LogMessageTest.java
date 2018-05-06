/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.logging;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test for <code>{@link LogMessage}</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.4
 */
public class LogMessageTest extends TestCase {

    /**
     * <p>
     * Represents the phase id.
     * </p>
     */
    private static final Long PHASE_ID = 1l;

    /**
     * <p>
     * Represents the operator name.
     * </p>
     */
    private static final String OPERATOR = "ivern";

    /**
     * <p>
     * Represents the log message.
     * </p>
     */
    private static final String MESSAGE = "checkpoint";

    /**
     * <p>
     * Represents the error exception.
     * </p>
     */
    private static final Throwable ERROR = new IOException("Unit Test");

    /**
     * <p>
     * Represents the <code>LogMessage</code> instance.
     * </p>
     */
    private LogMessage logMessage;

    /**
     * <p>
     * Setup the testing environment.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        logMessage = new LogMessage(PHASE_ID, OPERATOR, MESSAGE, ERROR);
    }

    /**
     * <p>
     * Tear down the testing environment.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    @Override
    protected void tearDown() throws Exception {
        logMessage = null;
        super.tearDown();
    }

    /**
     * <p>
     * Tests the <code>LogMessage(Long, String, String, Throwable)</code> constructor.
     * </p>
     * <p>
     * values should be set internally.
     * </p>
     */
    public void testCtor1() {
        // constructed in setup
        assertNotNull("instance should be created.", logMessage);

        assertEquals("phase id not set", PHASE_ID, logMessage.getPhaseId());
        assertEquals("operator not set", OPERATOR, logMessage.getOperator());
        assertEquals("message not set", MESSAGE, logMessage.getMessage());
        assertSame("error not set", ERROR, logMessage.getError());
    }

    /**
     * <p>
     * Tests the <code>LogMessage(Long, String, String)</code> constructor.
     * </p>
     * <p>
     * values should be set internally.
     * </p>
     */
    public void testCtor2() {
        logMessage = new LogMessage(PHASE_ID, OPERATOR, MESSAGE);

        assertNotNull("instance should be created.", logMessage);

        assertEquals("phase id not set", PHASE_ID, logMessage.getPhaseId());
        assertEquals("operator not set", OPERATOR, logMessage.getOperator());
        assertEquals("message not set", MESSAGE, logMessage.getMessage());
        assertNull("error not set", logMessage.getError());
    }

    /**
     * <p>
     * Tests the <code>getError()</code> method.
     * </p>
     * <p>
     * It should return the value set in constructor.
     * </p>
     */
    public void testGetError_accuracy() {
        assertSame("error not set", ERROR, logMessage.getError());
    }

    /**
     * <p>
     * Tests the <code>getMessage()</code> method.
     * </p>
     * <p>
     * It should return the value set in constructor.
     * </p>
     */
    public void testGetMessage_accuracy() {
        assertEquals("message not set", MESSAGE, logMessage.getMessage());
    }

    /**
     * <p>
     * Tests the <code>getOperator()</code> method.
     * </p>
     * <p>
     * It should return the value set in constructor.
     * </p>
     */
    public void testGetOperator_accuracy() {
        assertEquals("operator not set", OPERATOR, logMessage.getOperator());
    }

    /**
     * <p>
     * Tests the <code>getPhaseId()</code> method.
     * </p>
     * <p>
     * It should return the value set in constructor.
     * </p>
     */
    public void testGetPhaseId_accuracy() {
        assertEquals("phase id not set", PHASE_ID, logMessage.getPhaseId());
    }

    /**
     * <p>
     * Tests the <code>getLogMessage()</code> method.
     * </p>
     * <p>
     * It should return the string representation of the log message.
     * </p>
     */
    public void testGetLogMessage() {
        String message = logMessage.getLogMessage();
        assertTrue("phase id part is missing", message.contains(PHASE_ID.toString()));
        assertTrue("operator part is missing", message.contains(OPERATOR.toString()));
        assertTrue("message part is missing", message.contains(MESSAGE.toString()));
        assertTrue("error part is missing", message.contains(LogMessage.getExceptionStackTrace(ERROR)));
        assertTrue("error part is missing", message.contains(ERROR.getMessage()));
    }

    /**
     * <p>
     * Tests the <code>toString()</code> method.
     * </p>
     * <p>
     * It should return the string representation of the log message.
     * </p>
     */
    public void testToString() {
        String message = logMessage.toString();
        assertTrue("phase id part is missing", message.contains(PHASE_ID.toString()));
        assertTrue("operator part is missing", message.contains(OPERATOR.toString()));
        assertTrue("message part is missing", message.contains(MESSAGE.toString()));
        assertTrue("error part is missing", message.contains(LogMessage.getExceptionStackTrace(ERROR)));
        assertTrue("error part is missing", message.contains(ERROR.getMessage()));
    }

    /**
     * <p>
     * Tests the <code>getExceptionStackTrace(Throwable)</code> method.
     * </p>
     * <p>
     * It should return the string representation of the exception stack trace.
     * </p>
     */
    public void testGetExceptionStackTrace_accuracy() {
        String stackTrace = LogMessage.getExceptionStackTrace(ERROR);

        assertNotNull("should never null", stackTrace);
        assertTrue("exception message should be present", stackTrace.contains(ERROR.getMessage()));
    }

    /**
     * <p>
     * Tests the <code>getExceptionStackTrace(Throwable)</code> method.
     * </p>
     * <p>
     * The given cause is null, IllegalArgumentException expected.
     * </p>
     * @since 1.4
     */
    public void testGetExceptionStackTrace_failure() {
        try {
            LogMessage.getExceptionStackTrace(null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}
