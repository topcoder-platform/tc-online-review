/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.persistence.InputStreamBytesIterator;

import junit.framework.TestCase;

import java.io.FileInputStream;

import java.util.NoSuchElementException;


/**
 * Test <code>InputStreamBytesIterator</code> class for failure.
 *
 * @author fairytale
 * @version 1.0
 */
public class FailureInputStreamBytesIteratorTest extends TestCase {
    /** A empty file. */
    private static final String EMPTY_FILE = "test_files/failure/empty_input_stream";

    /** The main <code>InputStreamBytesIterator</code> instance used to test. */
    private InputStreamBytesIterator iter;

    /**
     * Initialization for all tests here.
     *
     * @throws Exception to Junit.
     */
    protected void setUp() throws Exception {
        iter = new InputStreamBytesIterator(new FileInputStream(EMPTY_FILE), 1024);
    }

    /**
     * Test <code>InputStreamBytesIterator(InputStream, int)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if argument is null.
     */
    public void testInputStreamBytesIteratorForNullPointerException() {
        try {
            new InputStreamBytesIterator(null, 1024);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>InputStreamBytesIterator(InputStream, int)</code> method for failure.
     * <code>IllegalArgumentException</code> should be thrown if the int argument is not positive
     *
     * @throws Exception to Junit.
     */
    public void testInputStreamBytesIteratorForIllegalArgumentException()
        throws Exception {
        try {
            new InputStreamBytesIterator(new FileInputStream(EMPTY_FILE), 0);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>nextBytes()</code> method for failure. <code>NoSuchElementException</code> should be thrown .
     *
     * @throws Exception to Junit.
     */
    public void testNextBytesForNoSuchElementException()
        throws Exception {
        try {
            iter.nextBytes();
            fail("the NoSuchElementException should be thrown!");
        } catch (NoSuchElementException e) {
            // Good
        }
    }
}
