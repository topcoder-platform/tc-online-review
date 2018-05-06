/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.persistence;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

/**
 * This is the default implementation of the BytesIterator interface. It reads the bytes from an InputStream. The byte
 * arrays returned have the same pre-configured length, but the last array could be smaller (as end of stream could be
 * reached). It is not thread safe as just one thread should access one instance.
 * @author Luca, FireIce
 * @version 1.0
 */
public class InputStreamBytesIterator implements BytesIterator {

    /**
     * Represents the input stream this iterator reads the bytes from. Initialized in the constructor and never changed
     * later. Not null. It will be closed in case an exception occurs, or if dispose() method is called, or when the end
     * of stream is reached.
     */
    private final InputStream inputStream;

    /**
     * Represents the number of bytes each call to nextBytes will return. In case the end of stream is reached, the
     * number of bytes returned could be less than iteratorByteSize. Initialized in the constructor and never changed
     * later. Positive.
     */
    private final int iteratorByteSize;

    /**
     * Represents the nextBytes to return. It is set in the hasNextBytes method and set to null in the nextBytes method.
     * The nextBytes will return this byte array.
     */
    private byte[] nextBytes;

    /**
     * Creates an instance with the given arguments.
     * @param inputStream
     *            the input stream this iterator reads the bytes from
     * @param iteratorByteSize
     *            the number of bytes each call to nextBytes will return
     * @throws NullPointerException
     *             if the object is null
     * @throws IllegalArgumentException
     *             if the int argument is not positive
     */
    public InputStreamBytesIterator(InputStream inputStream, int iteratorByteSize) {
        if (inputStream == null) {
            throw new NullPointerException("inputStream is null");
        }
        this.inputStream = inputStream;
        if (iteratorByteSize <= 0) {
            throw new IllegalArgumentException("iteratorByteSize should be positive");
        }
        this.iteratorByteSize = iteratorByteSize;
    }

    /**
     * Returns true if there are more bytes to return. If an exception occurs inside this method, dispose() is
     * automatically called before rethrowing the exception. If there are no more bytes to return, dispose() is
     * automatically called before returning.
     * @return true if there are more bytes to return
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public boolean hasNextBytes() throws FilePersistenceException {
        if (nextBytes != null) {
            return true;
        }
        // there are bytes to return
        // create a buffer
        ByteArrayOutputStream baos = new ByteArrayOutputStream(iteratorByteSize);
        int bytesToRead = iteratorByteSize;
        boolean endOfStream = false;
        try {
            while (!endOfStream && bytesToRead > 0) {
                byte[] buf = new byte[bytesToRead];
                int bytesRead = inputStream.read(buf);
                if (bytesRead != -1) {
                    // not end of stream
                    baos.write(buf, 0, bytesRead);
                    bytesToRead -= bytesRead;
                } else {
                    // end of stream
                    endOfStream = true;
                }
            }
            if (bytesToRead == iteratorByteSize) {
                // no bytes read
                dispose();
                return false;
            } else {
                nextBytes = baos.toByteArray();
                return true;
            }
        } catch (IOException e) {
            // If an exception occurs inside this method, dispose() is automatically called before rethrowing the
            // exception
            dispose();
            throw new FilePersistenceException("I/O error occur", e);
        }

    }

    /**
     * Returns the next byte array. If an exception occurs inside this method, dispose() is automatically called before
     * rethrowing the exception. If there are no more bytes to return, dispose() is automatically called before
     * returning.
     * @return the next byte array
     * @throws NoSuchElementException
     *             there are no more bytes to return
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public byte[] nextBytes() throws FilePersistenceException {
        if (nextBytes == null && !hasNextBytes()) {
            throw new NoSuchElementException("no next bytes");
        }
        // the nextBytes field has to be set to null
        byte[] auxNextBytes = nextBytes;
        nextBytes = null;
        return auxNextBytes;
    }

    /**
     * Disposes this instance. This method is needed as the connection between the client and server could be closed or
     * some exception could occur, and the resources have to be disposed.
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public void dispose() throws FilePersistenceException {
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new FilePersistenceException("fail to close stream", e);
        }
        nextBytes = null;
    }
}
