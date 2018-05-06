/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.accuracytests;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.Blob;
import java.sql.SQLException;


/**
 * <p>
 * A mock class which implements the Blob class for testing.
 * </p>
 *
 * @author PE
 * @version 1.1
 */
public class MockBlob implements Blob {
    /** Represents the byte array for testing. */
    private byte[] value = new byte[] { 1, 2, 3, 4, 5 };

    /** Represents the InputStream for testing. */
    private InputStream inputStream = new ByteArrayInputStream(value);

    /**
     * Mock method, do nothing.
     *
     * @return the number of bytes in the BLOB value designated by this Blob object.
     *
     * @throws SQLException if there is an error accessing the length of the BLOB
     */
    public long length() throws SQLException {
        return value.length;
    }

    /**
     * Mock method, do nothing.
     *
     * @param len the length, in bytes, to which the BLOB value that this Blob object.
     *
     * @throws SQLException if there is an error accessing the BLOB value.
     */
    public void truncate(long len) throws SQLException {
    }

    /**
     * Mock method, do nothing.
     *
     * @param pos the ordinal position of the first byte in the BLOB value to be extracted; the first byte is at
     *        position 1.
     * @param length the number of consecutive bytes to be copied.
     *
     * @return a byte array containing up to length consecutive bytes from the BLOB value designated by this Blob
     *         object, starting with the byte at position pos.
     *
     * @throws SQLException if there is an error accessing the BLOB value.
     */
    public byte[] getBytes(long pos, int length) throws SQLException {
        byte[] ret = new byte[length];
        System.arraycopy(value, 0, ret, (int) pos - 1, length);

        return ret;
    }

    /**
     * Mock method, do nothing.
     *
     * @param pos the position in the BLOB object at which to start writing
     * @param bytes the array of bytes to be written to the BLOB value that this Blob object represents
     *
     * @return the number of bytes written
     *
     * @throws SQLException if there is an error accessing the BLOB value
     */
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        return 0;
    }

    /**
     * Mock method, do nothing.
     *
     * @param pos the position in the BLOB object at which to start writing
     * @param bytes the array of bytes to be written to this BLOB object
     * @param offset the offset into the array bytes at which to start reading the bytes to be set
     * @param len the number of bytes to be written to the BLOB value from the array of bytes bytes
     *
     * @return the number of bytes written
     *
     * @throws SQLException if there is an error accessing the BLOB value
     */
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        return 0;
    }

    /**
     * Mock method, do nothing.
     *
     * @param pattern the byte array for which to search
     * @param start the position at which to begin searching; the first position is 1
     *
     * @return the position at which the pattern appears, else -1
     *
     * @throws SQLException if there is an error accessing the BLOB
     */
    public long position(byte[] pattern, long start) throws SQLException {
        return 0;
    }

    /**
     * Mock method, do nothing.
     *
     * @return a stream containing the BLOB data
     *
     * @throws SQLException if there is an error accessing the BLOB value
     */
    public InputStream getBinaryStream() throws SQLException {
        return inputStream;
    }

    /**
     * Mock method, do nothing.
     *
     * @param pos the position in the BLOB value at which to start writing
     *
     * @return a java.io.OutputStream object to which data can be written
     *
     * @throws SQLException if there is an error accessing the BLOB value
     */
    public OutputStream setBinaryStream(long pos) throws SQLException {
        return null;
    }

    /**
     * Mock method, do nothing.
     *
     * @param pattern the Blob object designating the BLOB value for which to search
     * @param start the position in the BLOB value at which to begin searching; the first position is 1
     *
     * @return the position at which the pattern begins, else -1
     *
     * @throws SQLException if there is an error accessing the BLOB value
     */
    public long position(Blob pattern, long start) throws SQLException {
        return 0;
    }
    
    public InputStream getBinaryStream(long pos, long length) throws SQLException
    {
        return null;
    }

    public void free() throws SQLException {

    }
    
}
