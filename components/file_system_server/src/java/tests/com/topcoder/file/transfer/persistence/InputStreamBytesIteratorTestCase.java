/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.persistence;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This is used to test InputStreamBytesIterator for correctness.
 * @author FireIce
 * @version 1.0
 */
public class InputStreamBytesIteratorTestCase extends TestCase {

    /**
     * Test Implementation.
     */
    public void testImplentation() {
        String text = "A piece of text";
        byte[] bytes = text.getBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        assertTrue("this class should implement BytesIterator interface",
                new InputStreamBytesIterator(is, 10) instanceof BytesIterator);
    }

    /**
     * Tests <code>InputStreamBytesIterator(InputStream, int)</code> constructor, if inputStream is null, throw
     * NullPointerException.
     */
    public void testCtorNullPointerException() {
        try {
            new InputStreamBytesIterator(null, 22);
            fail("if inputStream is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Tests <code>InputStreamBytesIterator(InputStream, int)</code> constructor, if the int argument is not positive,
     * throw NullPointerException.
     */
    public void testCtorIllegalArgumentException() {
        String text = "A piece of text";
        byte[] bytes = text.getBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        try {
            new InputStreamBytesIterator(is, 0);
            fail("if the int argument is not positive, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            new InputStreamBytesIterator(is, -10);
            fail("if the int argument is not positive, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Tests <code>InputStreamBytesIterator(InputStream, int)</code> constructor, if arguments are valid, successfully
     * created.
     * @throws Exception
     *             if any exception occur.
     */
    public void testCtorSuccess() throws Exception {
        String text = "A piece of text";
        byte[] bytes = text.getBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        BytesIterator iterator = new InputStreamBytesIterator(is, 10);
        assertTrue("should have next bytes", iterator.hasNextBytes());
    }

    /**
     * Test <code>hasNextBytes()</code> method, if IOException occur, throw FilePersistenceException.
     * @throws Exception
     *             if any other exception occur.
     */
    public void testhasNextBytesFilePersistenceException() throws Exception {
        BytesIterator iterator = new InputStreamBytesIterator(new MockInputStream(), 10);
        try {
            iterator.hasNextBytes();
            fail("an exception occurs as inputStream is closed, throw FilePersistenceException");
        } catch (FilePersistenceException e) {
            // good
        }
    }

    /**
     * Test <code>hasNextBytes()</code> method, successfully done, return the flag for hasNextBytes.
     * @throws Exception
     *             if any other exception occur.
     */
    public void testhasNextBytesSuccess() throws Exception {
        String text = "A piece of text";
        byte[] bytes = text.getBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        BytesIterator iterator = new InputStreamBytesIterator(is, bytes.length + 1);
        assertTrue("Should have next Bytes", iterator.hasNextBytes());
        iterator.nextBytes();
        assertFalse("Shouldn't have next Bytes", iterator.hasNextBytes());
        is = new ByteArrayInputStream(new byte[0]);
        iterator = new InputStreamBytesIterator(is, 10);
        assertFalse("Shouldn't have next Bytes", iterator.hasNextBytes());
    }

    /**
     * Test <code>nextBytes()</code> method, if no more bytes can be read, throw NoSuchElementException.
     * @throws Exception
     *             if any other exception occur.
     */
    public void testNextBytesNoSuchElementException() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        BytesIterator iterator = new InputStreamBytesIterator(is, 10);
        try {
            iterator.nextBytes();
            fail("if no more bytes can be read, throw NoSuchElementException");
        } catch (NoSuchElementException e) {
            // good
        }
    }

    /**
     * Test <code>nextBytes()</code> method, if no more bytes can be read, throw NoSuchElementException.
     */
    public void testNextBytesFilePersistenceException() {
        BytesIterator iterator = new InputStreamBytesIterator(new MockInputStream(), 10);
        try {
            iterator.nextBytes();
            fail("if an exception occurs while performing the operation, throw FilePersistenceException");
        } catch (FilePersistenceException e) {
            // good
        }
    }

    /**
     * Test <code>nextBytes()</code> method, if has more bytes can be read, return the nextBytes.
     * @throws Exception
     *             if any Exception occur.
     */
    public void testNextBytesSuccess() throws Exception {
        String text = "A piece of text";
        byte[] bytes = text.getBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        BytesIterator iterator = new InputStreamBytesIterator(is, bytes.length + 1);
        byte[] nextBytes = iterator.nextBytes();
        assertEquals("returned nextBytes's size not correct", nextBytes.length, bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            assertEquals("byte of the returned nextBytes at index " + i + " not correct", nextBytes[i], bytes[i]);
        }
    }

    /**
     * Test <code>dispose()</code> method, if an exception occurs while performing the operation, throw
     * FilePersistenceException.
     */
    public void testDisposeFilePersistenceException() {
        BytesIterator iterator = new InputStreamBytesIterator(new MockInputStream(), 10);
        try {
            iterator.dispose();
            fail("if an exception occurs while performing the operation, throw FilePersistenceException");
        } catch (FilePersistenceException e) {
            // good
        }
    }

    /**
     * Test <code>dispose()</code> method, while successfully dispose with no exception.
     * @throws Exception
     *             if any Exception occur.
     */
    public void testDisposeSuccess() throws Exception {
        String text = "A piece of text";
        byte[] bytes = text.getBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        BytesIterator iterator = new InputStreamBytesIterator(is, bytes.length + 1);
        iterator.dispose();
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(InputStreamBytesIteratorTestCase.class);
    }
}
