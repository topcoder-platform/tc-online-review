/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.persistence;

import java.io.ByteArrayInputStream;

import com.topcoder.file.transfer.persistence.InputStreamBytesIterator;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy Test for InputStreamBytesIterator class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class InputStreamBytesIteratorAccuracyTest extends TestCase {
    /**
     * <p>
     * Represents the datas used in input data stream.
     * </p>
     */
    private byte[] datas;

    /**
     * <p>
     * Represents the length of the datas.
     * </p>
     */
    private int len = 1333;

    /**
     * <p>
     * Represents block size to iterator.
     * </p>
     */
    private int blockSize = 10;

    /**
     * <p>
     * Represents an instance of InputStreamBytesIterator, used to test it's
     * member.
     * </p>
     */
    private InputStreamBytesIterator itor;

    /**
     * <p>
     * Set up for each test.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        datas = new byte[len];
        for (int i = 0; i < len; i++) {
            datas[i] = (byte) (i % blockSize);
        }
    }

    /**
     * <p>
     * Test ctor InputStreamBytesIterator(InputStream inputStream, int
     * iteratorByteSize), the instance should be created.
     * </p>
     */
    public void testCtor() {
        InputStreamBytesIterator itor = new InputStreamBytesIterator(
                new ByteArrayInputStream(datas), blockSize);
        assertNotNull("Failed to create instance of InputStreamBytesIterator",
                itor);
    }

    /**
     * <p>
     * Test hasNextBytes(), if contains datas, return true.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testHasNextBytes() throws Exception {
        InputStreamBytesIterator itor = new InputStreamBytesIterator(
                new ByteArrayInputStream(datas), blockSize);
        assertTrue("Failed to get the correct result.", itor.hasNextBytes());
    }

    /**
     * <p>
     * Test nextBytes(), it should return the next block data.
     * </p>
     * 
     * @throws Exception
     */
    public void testNextBytes() throws Exception {
        InputStreamBytesIterator itor = new InputStreamBytesIterator(
                new ByteArrayInputStream(datas), blockSize);
        byte[] datas = itor.nextBytes();
        assertEquals("Failed to get the correct data", blockSize, datas.length);
    }

    /**
     * <p>
     * Test ctor InputStreamBytesIterator(InputStream inputStream, int
     * iteratorByteSize),
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testIterator() throws Exception {
        InputStreamBytesIterator itor = new InputStreamBytesIterator(
                new ByteArrayInputStream(datas), blockSize);
        int returnedLen = 0;
        while (itor.hasNextBytes()) {
            returnedLen += itor.nextBytes().length;
        }
        assertEquals("The iterator not implemented properly", len, returnedLen);
    }

    /**
     * <p>
     * Test dispose(), simple call it.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testDispose() throws Exception {
        InputStreamBytesIterator itor = new InputStreamBytesIterator(
                new ByteArrayInputStream(datas), blockSize);
        itor.dispose();
    }
}