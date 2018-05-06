/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * <p>
 * This class implements Clob interface and is used for testing.
 * </p>
 *
 * @author biotrail
 * @version 1.1
 * @since 1.1
 */
public class DummyClob implements Clob {
    /**
     * <p>
     * Default Constructor.
     * </p>
     */
    public DummyClob() {
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
     * @return null
     */
    public InputStream getAsciiStream() {
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
    public OutputStream setAsciiStream(long pos) {
        return null;
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     *
     * @return null
     */
    public Reader getCharacterStream() {
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
    public Writer setCharacterStream(long pos) {
        return null;
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
    public String getSubString(long pos, int length) {
        return null;
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     *
     * @param pos pos
     * @param str str
     *
     * @return 0
     */
    public int setString(long pos, String str) {
        return 0;
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     *
     * @param pos pos
     * @param str str
     * @param offset offset
     * @param len len
     *
     * @return 0
     */
    public int setString(long pos, String str, int offset, int len) {
        return 0;
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     *
     * @param searchstr searchstr
     * @param start start
     *
     * @return 0
     */
    public long position(String searchstr, long start) {
        return 0;
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     *
     * @param searchstr searchstr
     * @param start start
     *
     * @return 0
     */
    public long position(Clob searchstr, long start) {
        return 0;
    }
    
    public Reader getCharacterStream(long pos, long length) throws SQLException{
        return null;
    }

    public void free() throws SQLException {

    }    
}
