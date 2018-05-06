/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;


/**
 * <p>
 * Tests ConfigurationException class, really dummy test, it should throw valid error message and
 * have proper cause.
 * </p>
 *
 * <p>
 * <b> Change in Version 1.1 </b><br>
 * Adds tests which use the empty(including trimmed empty) String and null arguments to test.
 * </p>
 *
 * @author qiucx0161, magicpig
 * @version 1.1
 *
 * @since 1.0
 */
public class ConfigurationExceptionTest extends TestCase {
    /**
     * An exception instance used to create the ConfigurationException.
     */
    private final Exception cause = new IllegalArgumentException();

    /**
     * Tests constructor with error message and the properties can be retrieved correctly later.
     */
    public void testCtor1() {
        BaseException be = new ConfigurationException("dummy");
        assertTrue("The error message should match", be.getMessage().indexOf("dummy") >= 0);
    }

    /**
     * Tests constructor with null error message. All works well.
     *
     * @since 1.1
     */
    public void testCtor1WithNullMessage() {
        // no error
        new ConfigurationException((String) null);
    }

    /**
     * Tests constructor with empty error message. All works well.
     *
     * @since 1.1
     */
    public void testCtor1WithEmptyMessage() {
        // no error
        new ConfigurationException("");
    }

    /**
     * Tests constructor with trimmed empty error message. All works well.
     *
     * @since 1.1
     */
    public void testCtor1WithTrimmedEmptyMessage() {
        // no error
        new ConfigurationException("  ");
    }

    /**
     * Tests constructor with error message and non-null inner exception, both properties can be
     * retrieved correctly later.
     */
    public void testCtor2() {
        BaseException bre = new ConfigurationException("dummy", cause);

        assertTrue("The inner exception should match",
            bre.getCause() instanceof IllegalArgumentException);

        assertTrue("The error message should match", bre.getMessage().indexOf("dummy") >= 0);
    }

    /**
     * Tests constructor with null error message and non-null inner exception. All works well.
     *
     * @since 1.1
     */
    public void testCtor2WithNullMessage() {
        // no error
        new ConfigurationException((String) null, cause);
    }

    /**
     * Tests constructor with empty error message and non-null inner exception. All works well.
     *
     * @since 1.1
     */
    public void testCtor2WithEmptyMessage() {
        // no error
        new ConfigurationException("", cause);
    }

    /**
     * Tests constructor with trimmed empty error message and non-null inner exception. All works
     * well.
     *
     * @since 1.1
     */
    public void testCtor2WithTrimmedEmptyMessage() {
        // no error
        new ConfigurationException("  ", cause);
    }

    /**
     * Tests constructor with error message and null inner exception. All works well.
     *
     * @since 1.1
     */
    public void testCtor2WithNullInnerException() {
        // no error
        new ConfigurationException("dummy", null);
    }

    /**
     * Tests constructor non-null inner exception, the cause can be retrieved correctly later.
     */
    public void testCtor3() {
        BaseException bre = new ConfigurationException(cause);

        assertTrue("The inner exception should match",
            bre.getCause() instanceof IllegalArgumentException);
    }

    /**
     * Tests constructor with null inner exception. All works well.
     *
     * @since 1.1
     */
    public void testCtor3WithNullInnerException() {
        // no error
        new ConfigurationException((Throwable) null);
    }
}
