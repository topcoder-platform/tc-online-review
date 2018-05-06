/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor;

import junit.framework.TestCase;


/**
 * Test case for JobProcessorException.
 *
 * @author TCSDEVELOPER
 * @version 1.0
  */
public class JobProcessorExceptionTest extends TestCase {
    /**
     * Test ctor JobProcessorException(String) with string message and test whether it can be obtained later.
     */
    public void testJobProcessorExceptionString() {
        String msg = "msg";
        JobProcessorException e = new JobProcessorException(msg);
        assertTrue("msg should be set and obtained properly", e.getMessage().indexOf(msg) == 0);
    }

    /**
     * Test JobProcessorException(String, Throwable) with msg and cause, and obtain them later.
     */
    public void testJobProcessorExceptionStringThrowable() {
        String msg = "msg";
        Throwable t = new RuntimeException();
        JobProcessorException e = new JobProcessorException(msg, t);
        assertTrue("msg should be set and obtained properly", e.getMessage().indexOf(msg) == 0);
        assertEquals("throwable should be set and obtained properly", t, e.getCause());
    }
}
