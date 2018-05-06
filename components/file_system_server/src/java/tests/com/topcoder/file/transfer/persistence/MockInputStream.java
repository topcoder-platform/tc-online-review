/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.persistence;

import java.io.IOException;
import java.io.InputStream;

/**
 * Mock implementation of InputStream class, only used for testing.
 * @author FireIce
 * @version 1.0
 */
public class MockInputStream extends InputStream {

    /**
     * Reads the next byte of data from the input stream. As used for testing, always throw IOException.
     * @return the byte.
     * @throws IOException
     *             always thrown.
     */
    public int read() throws IOException {
        throw new IOException();
    }

    /**
     * Close the input stream.
     * @throws IOException
     *             always thrown.
     */
    public void close() throws IOException {
        throw new IOException();
    }
}
