/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.accuracytests;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import java.sql.Clob;
import java.sql.SQLException;


/**
 * <p>
 * A mock class which implements the Clob class for testing.
 * </p>
 *
 * @author PE
 * @version 1.1
 */
public class MockClob implements Clob {
    /** Represents the string for testing. */
    private String value = "test";

    /** Represents the InputStream for testing. */
    private InputStream inputStream = new ByteArrayInputStream(value.getBytes());

    /** Represents the reader for testing. */
    private Reader reader = new InputStreamReader(inputStream);

    /**
     * Mock method, do nothing.
     *
     * @return the number of bytes in the Clob value designated by this Clob object.
     *
     * @throws SQLException if there is an error accessing the length of the Clob
     */
    public long length() throws SQLException {
        return 0;
    }

    /**
     * Mock method, do nothing.
     *
     * @param len the length, in bytes, to which the Clob value that this Clob object.
     *
     * @throws SQLException if there is an error accessing the Clob value.
     */
    public void truncate(long len) throws SQLException {
    }

    /**
     * Mock method, do nothing.
     *
     * @return a java.io.InputStream object containing the CLOB data
     *
     * @throws SQLException if there is an error accessing the CLOB value
     */
    public InputStream getAsciiStream() throws SQLException {
        return inputStream;
    }

    /**
     * Mock method, do nothing.
     *
     * @param pos the position in the Clob value at which to start writing
     *
     * @return a java.io.OutputStream object to which data can be written
     *
     * @throws SQLException if there is an error accessing the Clob value
     */
    public OutputStream setAsciiStream(long pos) throws SQLException {
        return null;
    }

    /**
     * Mock method, do nothing.
     *
     * @return a java.io.Reader object containing the CLOB data
     *
     * @throws SQLException if there is an error accessing the CLOB value
     */
    public Reader getCharacterStream() throws SQLException {
        return this.reader;
    }

    /**
     * Mock method, do nothing.
     *
     * @param pos the position in the Clob value at which to start writing
     *
     * @return a java.io.Writer object to which data can be written
     *
     * @throws SQLException if there is an error accessing the Clob value
     */
    public Writer setCharacterStream(long pos) throws SQLException {
        return null;
    }

    /**
     * Mock method, do nothing.
     *
     * @param pos the first character of the substring to be extracted. The first character is at position 1.
     * @param length the number of consecutive characters to be copied
     *
     * @return a String that is the specified substring in the CLOB value designated by this Clob object
     *
     * @throws SQLException if there is an error accessing the CLOB value
     */
    public String getSubString(long pos, int length) throws SQLException {
        return this.value.substring((int) pos - 1, (int) pos - 1 + length);
    }

    /**
     * Mock method, do nothing.
     *
     * @param pos the position at which to start writing to the CLOB value that this Clob object represents
     * @param str the string to be written to the CLOB value that this Clob designates
     *
     * @return the number of characters written
     *
     * @throws SQLException if there is an error accessing the CLOB value
     */
    public int setString(long pos, String str) throws SQLException {
        return 0;
    }

    /**
     * Mock method, do nothing.
     *
     * @param pos the position at which to start writing to this CLOB object
     * @param str the string to be written to the CLOB value that this Clob object represents
     * @param offset the offset into str to start reading the characters to be written
     * @param len the number of characters to be written
     *
     * @return the number of characters written
     *
     * @throws SQLException if there is an error accessing the CLOB value
     */
    public int setString(long pos, String str, int offset, int len)
        throws SQLException {
        return 0;
    }

    /**
     * Mock method, do nothing.
     *
     * @param pattern the string array for which to search
     * @param start the position at which to begin searching; the first position is 1
     *
     * @return the position at which the pattern appears, else -1
     *
     * @throws SQLException if there is an error accessing the Clob
     */
    public long position(String pattern, long start) throws SQLException {
        return 0;
    }

    /**
     * Mock method, do nothing.
     *
     * @param pattern the Clob object designating the Clob value for which to search
     * @param start the position in the Clob value at which to begin searching; the first position is 1
     *
     * @return the position at which the pattern begins, else -1
     *
     * @throws SQLException if there is an error accessing the Clob value
     */
    public long position(Clob pattern, long start) throws SQLException {
        return 0;
    }
    
    public Reader getCharacterStream(long pos, long length) throws SQLException{
        return null;
    }

    public void free() throws SQLException {

    }    
}
