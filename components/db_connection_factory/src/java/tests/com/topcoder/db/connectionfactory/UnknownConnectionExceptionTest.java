/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;


/**
 * <p>
 * Tests UnknownConnectionException class, really dummy test, it should throw error message and
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
public class UnknownConnectionExceptionTest extends TestCase {
    /**
     * An exception instance used to create the UnknownConnectionException.
     */
    private final Exception cause = new IllegalArgumentException();

    /**
     * Tests constructor with name and error message, the properties can be retrieved correctly
     * later.
     */
    public void testCtor1() {
        BaseException bre = new UnknownConnectionException("tc", "dummy");
        assertTrue("The error message should match", bre.getMessage().indexOf("dummy") >= 0);
    }

    /**
     * Tests constructor with null name and error message. All works well.
     *
     * @since 1.1
     */
    public void testCtor1WithNullName() {
        // no error
        new UnknownConnectionException(null, "dummy");
    }

    /**
     * Tests constructor with empty name and error message. All works well.
     *
     * @since 1.1
     */
    public void testCtor1WithEmptyName() {
        // no error
        new UnknownConnectionException("", "dummy");
    }

    /**
     * Tests constructor with trimmed empty name and error message. All works well.
     *
     * @since 1.1
     */
    public void testCtor1WithTrimmedEmptyName() {
        // no error
        new UnknownConnectionException("  ", "dummy");
    }

    /**
     * Tests constructor with name and null error message. All works well.
     *
     * @since 1.1
     */
    public void testCtor1WithNullMessage() {
        // no error
        new UnknownConnectionException("tc", null);
    }

    /**
     * Tests constructor with name and empty error message. All works well.
     *
     * @since 1.1
     */
    public void testCtor1WithEmptyMessage() {
        // no error
        new UnknownConnectionException("tc", "");
    }

    /**
     * Tests constructor with name and trimmed empty error message. All works well.
     *
     * @since 1.1
     */
    public void testCtor1WithTrimmedEmptyMessage() {
        // no error
        new UnknownConnectionException("tc", "  ");
    }

    /**
     * Tests constructor with error message and non-null inner exception, both properties can be
     * retrieved correctly later. And the retrieved name should match.
     *
     * @since 1.0
     */
    public void testCtor2() {
        UnknownConnectionException uce = new UnknownConnectionException("tc", "dummy", cause);

        assertTrue("The inner exception should match",
            uce.getCause() instanceof IllegalArgumentException);

        assertTrue("The error message should match", uce.getMessage().indexOf("dummy") >= 0);

        assertEquals("The retrieved name should match.", "tc", uce.getName());
    }

    /**
     * Tests constructor with null name, error message and non-null inner exception. All works
     * well.
     *
     * @since 1.1
     */
    public void testCtor2WithNullName() {
        // no error
        new UnknownConnectionException(null, "dummy", cause);
    }

    /**
     * Tests constructor with empty name, error message and non-null inner exception. All works
     * well.
     *
     * @since 1.1
     */
    public void testCtor2WithEmptyName() {
        // no error
        new UnknownConnectionException("", "dummy", cause);
    }

    /**
     * Tests constructor with trimmed empty name, error message and non-null inner exception. All
     * works well.
     *
     * @since 1.1
     */
    public void testCtor2WithTrimmedEmptyName() {
        // no error
        new UnknownConnectionException("  ", "dummy", cause);
    }

    /**
     * Tests constructor with name, null error message and non-null inner exception. All works
     * well.
     *
     * @since 1.1
     */
    public void testCtor2WithNullMessage() {
        // no error
        new UnknownConnectionException("tc", null, cause);
    }

    /**
     * Tests constructor with name, empty error message and non-null inner exception. All works
     * well.
     *
     * @since 1.1
     */
    public void testCtor2WithEmptyMessage() {
        // no error
        new UnknownConnectionException("tc", "", cause);
    }

    /**
     * Tests constructor with name, trimmed empty error message and non-null inner exception. All
     * works well.
     *
     * @since 1.1
     */
    public void testCtor2WithTrimmedEmptyMessage() {
        // no error
        new UnknownConnectionException("tc", "  ", cause);
    }

    /**
     * Tests constructor with name, error message and null inner exception. All works well.
     *
     * @since 1.1
     */
    public void testCtor2WithNullCause() {
        // no error
        new UnknownConnectionException("tc", "  ", cause);
    }

    /**
     * Test getName method. The name is initialized in constructor, and retrieved by getName
     * method. They should match.
     */
    public void testGetName() {
        UnknownConnectionException e = new UnknownConnectionException("tc", "dummy");
        assertEquals("The retrieved name should match.", "tc", e.getName());
    }
}
