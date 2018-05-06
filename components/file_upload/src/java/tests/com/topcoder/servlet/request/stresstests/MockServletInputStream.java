/**
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.stresstests;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;


/**
 * Delegating implementation of ServletInputStream.
 *
 * @author brain_cn
 */
public class MockServletInputStream extends ServletInputStream {
    private final InputStream sourceStream;

    /**
     * Create a new DelegatingServletInputStream.
     *
     * @param sourceStream the sourceStream InputStream
     */
    public MockServletInputStream(InputStream sourceStream) {
        this.sourceStream = sourceStream;
    }

    /**
     * Return sourceStream.
     *
     * @return sourceStream
     */
    public InputStream getSourceStream() {
        return sourceStream;
    }

    /**
     * Read from sourceStream
     *
     * @return read from sourcestream
     *
     * @throws IOException if error occurs
     */
    public int read() throws IOException {
        return this.sourceStream.read();
    }

    /**
     * Close the source stream.
     *
     * @throws IOException if error occurs
     */
    public void close() throws IOException {
        super.close();
        this.sourceStream.close();
    }
}
