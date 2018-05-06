/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor.accuracytests;

import com.topcoder.util.scheduler.processor.JobProcessorException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit test cases for JobProcessorException class.
 * </p>
 *
 * @author bbskill
 * @version 1.0
 */
public class JobProcessorExceptionTest extends TestCase {
    /**
     * <p>
     * A error message used for testing.
     * </p>
     */
    private static final String ERROR_MESSAGE = "error message";

    /**
     * <p>
     * A Throwable instance used for testing.
     * </p>
     */
    private static final Throwable CAUSE = new Throwable();

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(JobProcessorExceptionTest.class);
    }

    /**
     * <p>
     * Tests constructor JobProcessorException(String) for accuracy.
     * </p>
     *
     * <p>
     * Verify : the JobProcessorException(message) is correct.
     * </p>
     */
    public void testCtor() {
        JobProcessorException e = new JobProcessorException(ERROR_MESSAGE);
        assertNotNull("The JobProcessorException instance should not be null.", e);
        assertEquals("The error message is incorrect.", ERROR_MESSAGE, e.getMessage());
        assertNull("The inner CAUSE should be null", e.getCause());
    }

    /**
     * <p>
     * Tests constructor JobProcessorException(String, Throwable) for accuracy.
     * </p>
     *
     * <p>
     * Verify: the JobProcessorException(String, Throwable) is correct.
     * </p>
     */
    public void testCtor2() {
        JobProcessorException e = new JobProcessorException(ERROR_MESSAGE, CAUSE);
        assertNotNull("The JobProcessorException instance should not be null.", e);
        assertTrue("The error message is incorrect.", e.getMessage().startsWith(ERROR_MESSAGE));
        assertSame("The inner CAUSE is incorrect", CAUSE, e.getCause());
    }
}
