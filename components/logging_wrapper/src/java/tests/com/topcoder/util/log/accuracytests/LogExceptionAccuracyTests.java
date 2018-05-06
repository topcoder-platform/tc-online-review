/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.accuracytests;

import junit.framework.TestCase;
import com.topcoder.util.log.LogException;

/**
 * <p>
 * Accuracy test case for <code>LogException</code>.
 * </p>
 *
 * @author tianniu
 * @version 2.0
 */
public class LogExceptionAccuracyTests extends TestCase {

    /**
     * <p>
     * Test the constructor <code>LogException(Throwable)</code>.
     * </p>
     */
    public void testCtorWithThrowable() {
        Throwable e = new Exception();
        LogException excp = new LogException(e);

        assertNotNull("The instance should be created.", excp);
        assertTrue("The cause field should be set correctly.", excp.getCause().equals(e));
    }

    /**
     * <p>
     * Test the constructor <code>LogException(String)</code>.
     * </p>
     */
    public void testCtorWithString() {
        LogException excp = new LogException("Failed");

        assertNotNull("The instance should be created.", excp);
        assertTrue("The message field should be set correctly.", excp.getMessage().startsWith("Failed"));
    }

    /**
     * <p>
     * Test the constructor <code>LogException(String, Throwable)</code>.
     * </p>
     */
    public void testCtorWithStringAndCause() {
        Throwable e = new Exception();
        LogException excp = new LogException("Failed", e);

        assertNotNull("The instance should be created.", excp);
        assertTrue("The message field should be set correctly.", excp.getMessage().startsWith("Failed"));
        assertTrue("The cause field should be set correctly.", excp.getCause().equals(e));
    }
}
