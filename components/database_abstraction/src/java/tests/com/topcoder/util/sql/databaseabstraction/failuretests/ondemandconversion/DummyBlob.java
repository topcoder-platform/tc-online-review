/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * <p>
 * This class implements Blob interface and is used for testing.
 * </p>
 *
 * @author biotrail
 * @version 1.1
 * @since 1.1
 */
public class DummyBlob implements Blob {
    /**
     * <p>
     * Default Constructor.
     * </p>
     */
    public DummyBlob() {
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     *
     * @return 0
     */
    public long length() {
        return 0;
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     *
     * @param len len
     */
    public void truncate(long len) {
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     *
     * @param pos pos
     * @param length length
     *
     * @return null
     */
    public byte[] getBytes(long pos, int length) {
        return null;
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     *
     * @param pos pos
     * @param bytes bytes
     *
     * @return 0
     */
    public int setBytes(long pos, byte[] bytes) {
        return 0;
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     *
     * @param pos pos
     * @param bytes bytes
     * @param offset offset
     * @param len len
     *
     * @return 0
     */
    public int setBytes(long pos, byte[] bytes, int offset, int len) {
        return 0;
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     *
     * @param pattern pattern
     * @param start start
     *
     * @return 0
     */
    public long position(byte[] pattern, long start) {
        return 0;
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     *
     * @return null
     */
    public InputStream getBinaryStream() {
        return null;
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     *
     * @param pos pos
     *
     * @return null
     */
    public OutputStream setBinaryStream(long pos) {
        return null;
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     *
     * @param pattern pattern
     * @param start start
     *
     * @return 0
     */
    public long position(Blob pattern, long start) {
        return 0;
    }
    
    public InputStream getBinaryStream(long pos, long length) throws SQLException
    {
        return null;
    }

    public void free() throws SQLException {

    }

}
