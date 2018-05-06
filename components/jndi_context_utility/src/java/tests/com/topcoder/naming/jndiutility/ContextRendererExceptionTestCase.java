/*
 * Copyright (C) 2003, 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0 (Unit Tests)
 *
 * @ ContextRendererExceptionTestCase.java
 */
package com.topcoder.naming.jndiutility;

import com.topcoder.util.errorhandling.BaseRuntimeException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Test the ContextRenderException class.
 *
 * @author preben
 * @author Charizard
 * @version 2.0
 *
 * @since 1.0
 */
public class ContextRendererExceptionTestCase extends TestCase {
    /** Error message used in test. */
    private static final String MESSAGE = "Error Message";

    /** Cause exception used in test. */
    private static final Throwable CAUSE = new Exception("Cause Exception");

    /**
     * Creates a new ContextRenderExceptionTestCase object.
     *
     * @param name a String
     */
    public ContextRendererExceptionTestCase(String name) {
        super(name);
    }

    /**
     * Return a new Test.
     *
     * @return a new Test
     */
    public static Test suite() {
        return new TestSuite(ContextRendererExceptionTestCase.class);
    }

    /**
     * Test the constructors.
     */
    public void testConstructor() {
        Throwable throwable = new IllegalArgumentException();
        String detailString = "detail";
        ContextRendererException madeFromThrowable = new ContextRendererException(throwable);
        ContextRendererException madeFromString = new ContextRendererException(detailString);
        ContextRendererException madeFromDefaultConstructor = new ContextRendererException();

        assertTrue(madeFromThrowable.getCause() instanceof IllegalArgumentException);
        assertEquals(madeFromString.getMessage(), detailString);
        assertNull(madeFromDefaultConstructor.getCause());
    }

    /**
     * Test method for {@link ContextRendererException#ContextRendererException()}. Try to instantiate with it.
     */
    public void testContextRendererExceptionAccuracy() {
        assertNotNull("failed to instantiate", new ContextRendererException());
    }

    /**
     * Test method for {@link ContextRendererException#ContextRendererException(String)}. Try to instantiate
     * with it and check the error message.
     */
    public void testContextRendererExceptionStringAccuracy() {
        ContextRendererException e = new ContextRendererException(MESSAGE);
        assertNotNull("failed to instantiate", e);
        assertEquals("wrong error message", MESSAGE, e.getMessage());
    }

    /**
     * Test method for {@link ContextRendererException#ContextRendererException(Throwable)}.
     */
    public void testContextRendererExceptionThrowableAccuracy() {
        ContextRendererException e = new ContextRendererException(CAUSE);
        assertNotNull("failed to instantiate", e);
        assertEquals("wrong cause", CAUSE, e.getCause());
    }

    /**
     * Test method for {@link ContextRendererException#ContextRendererException(String, Throwable)}.
     */
    public void testContextRendererExceptionStringThrowableAccuracy() {
        ContextRendererException e = new ContextRendererException(MESSAGE, CAUSE);
        assertNotNull("failed to instantiate", e);
        assertEquals("wrong error message", MESSAGE, e.getMessage());
        assertEquals("wrong cause", CAUSE, e.getCause());
    }

    /**
     * Test the inheritance of ContextRendererException. Check whether it extends BaseRuntimeException.
     */
    public void testInheritance() {
        assertTrue("wrong inheritance", new ContextRendererException() instanceof BaseRuntimeException);
    }
}
