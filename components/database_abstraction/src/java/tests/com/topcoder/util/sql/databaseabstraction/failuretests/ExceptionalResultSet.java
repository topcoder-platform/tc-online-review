/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.failuretests;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * <p>
 * A ResultSet implementation all of whose methods always throw SQLException.
 * Used in failure testing.
 * </p>
 *
 * @author ThinMan
 * @author biotrail
 * @version 1.1
 * @since 1.0
 */
public class ExceptionalResultSet implements ResultSet {
    /**
     * <p>
     * Implements next() for failure test.
     * </p>
     *
     * @return never
     * @throws SQLException always
     */
    public boolean next() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements close() for failure test.
     * </p>
     *
     * @throws SQLException always
     */
    public void close() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements wasNull() for failure test.
     * </p>
     *
     * @return never
     * @throws SQLException always
     */
    public boolean wasNull() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getString() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public String getString(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getBoolean() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public boolean getBoolean(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getByte() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public byte getByte(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getShort() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public short getShort(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getInt() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public int getInt(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getLong() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public long getLong(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getFloat() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public float getFloat(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getDouble() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public double getDouble(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getBigDecimal() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param scale scale
     * @return never
     *
     * @throws SQLException always
     */
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getBytes() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public byte[] getBytes(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getDate() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public Date getDate(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getTime() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public Time getTime(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getTimestamp() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getAsciiStream() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getUnicodeStream() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getBinaryStream() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getString() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     *
     * @throws SQLException always
     */
    public String getString(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getBoolean() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     *
     * @throws SQLException always
     */
    public boolean getBoolean(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getByte() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     *
     * @throws SQLException always
     */
    public byte getByte(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getShort() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     *
     * @throws SQLException always
     */
    public short getShort(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getInt() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     * @throws SQLException always
     */
    public int getInt(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getLong() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     *
     * @throws SQLException always
     */
    public long getLong(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getFloat() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     *
     * @throws SQLException always
     */
    public float getFloat(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getDouble() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     *
     * @throws SQLException always
     */
    public double getDouble(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getBigDecimal() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param scale scale
     * @return never
     *
     * @throws SQLException always
     */
    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getBytes() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     *
     * @throws SQLException always
     */
    public byte[] getBytes(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getDate() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     * @throws SQLException always
     */
    public Date getDate(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getTime() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     *
     * @throws SQLException always
     */
    public Time getTime(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getTimestamp() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     *
     * @throws SQLException always
     */
    public Timestamp getTimestamp(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getAsciiStream() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     *
     * @throws SQLException always
     */
    public InputStream getAsciiStream(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getUnicodeStream() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     *
     * @throws SQLException always
     */
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getBinaryStream() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     *
     * @throws SQLException always
     */
    public InputStream getBinaryStream(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getWarnings() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public SQLWarning getWarnings() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements clearWarnings() for failure test.
     * </p>
     *
     * @throws SQLException always
     */
    public void clearWarnings() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getCursorName() for failure test.
     * </p>
     *
     * @return null
     *
     * @throws SQLException always
     */
    public String getCursorName() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getMetaData() for failure test.
     * </p>
     *
     * @return null
     *
     * @throws SQLException always
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getObject() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return null
     *
     * @throws SQLException always
     */
    public Object getObject(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getObject() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return null
     *
     * @throws SQLException always
     */
    public Object getObject(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements findColumn() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return null
     *
     * @throws SQLException always
     */
    public int findColumn(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getCharacterStream() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return null
     *
     * @throws SQLException always
     */
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getCharacterStream() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return null
     *
     * @throws SQLException always
     */
    public Reader getCharacterStream(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getBigDecimal() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return null
     *
     * @throws SQLException always
     */
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getBigDecimal() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return null
     *
     * @throws SQLException always
     */
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements isBeforeFirst() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public boolean isBeforeFirst() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements isAfterLast() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public boolean isAfterLast() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements isFirst() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public boolean isFirst() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements isLast() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public boolean isLast() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements beforeFirst() for failure test.
     * </p>
     *
     * @throws SQLException always
     */
    public void beforeFirst() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements afterLast() for failure test.
     * </p>
     *
     * @throws SQLException always
     */
    public void afterLast() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements first() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public boolean first() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements last() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public boolean last() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getRow() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public int getRow() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements absolute() for failure test.
     * </p>
     *
     * @param row row
     * @return never
     *
     * @throws SQLException always
     */
    public boolean absolute(int row) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements relative() for failure test.
     * </p>
     *
     * @param rows rows
     * @return never
     *
     * @throws SQLException always
     */
    public boolean relative(int rows) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements previous() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public boolean previous() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements setFetchDirection() for failure test.
     * </p>
     *
     * @param direction direction
     *
     * @throws SQLException always
     */
    public void setFetchDirection(int direction) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getFetchDirection() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public int getFetchDirection() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements setFetchSize() for failure test.
     * </p>
     *
     * @param rows rows
     *
     * @throws SQLException always
     */
    public void setFetchSize(int rows) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getFetchSize() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public int getFetchSize() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getType() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public int getType() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getConcurrency() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public int getConcurrency() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements rowUpdated() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public boolean rowUpdated() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements rowInserted() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public boolean rowInserted() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements rowDeleted() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public boolean rowDeleted() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateNull() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     *
     * @throws SQLException always
     */
    public void updateNull(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateBoolean() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateByte() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateShort() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateInt() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateLong() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateFloat() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateDouble() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateBigDecimal() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateString() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateString(int columnIndex, String x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateBytes() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateBytes(int columnIndex, byte x[]) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateDate() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateTime() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateTimestamp() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateAsciiStream() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     * @param length length
     *
     * @throws SQLException always
     */
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateBinaryStream() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     * @param length length
     *
     * @throws SQLException always
     */
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateCharacterStream() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     * @param length length
     *
     * @throws SQLException always
     */
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateObject() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     * @param scale scale
     *
     * @throws SQLException always
     */
    public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateObject() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateNull() for failure test.
     * </p>
     *
     * @param columnName columnName
     *
     * @throws SQLException always
     */
    public void updateNull(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateBoolean() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateBoolean(String columnName, boolean x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateByte() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateByte(String columnName, byte x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateShort() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateShort(String columnName, short x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateInt() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateInt(String columnName, int x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateLong() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateLong(String columnName, long x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateFloat() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateFloat(String columnName, float x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateDouble() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateDouble(String columnName, double x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateBigDecimal() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateString() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateString(String columnName, String x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateBytes() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateBytes(String columnName, byte x[]) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateDate() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateDate(String columnName, Date x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateTime() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateTime(String columnName, Time x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateTimestamp() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateAsciiStream() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     * @param length length
     *
     * @throws SQLException always
     */
    public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateBinaryStream() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     * @param length length
     *
     * @throws SQLException always
     */
    public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateCharacterStream() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param reader reader
     * @param length length
     *
     * @throws SQLException always
     */
    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateObject() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     * @param scale scale
     *
     * @throws SQLException always
     */
    public void updateObject(String columnName, Object x, int scale) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateObject() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateObject(String columnName, Object x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements insertRow() for failure test.
     * </p>
     *
     * @throws SQLException always
     */
    public void insertRow() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateRow() for failure test.
     * </p>
     *
     * @throws SQLException always
     */
    public void updateRow() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements deleteRow() for failure test.
     * </p>
     *
     * @throws SQLException always
     */
    public void deleteRow() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements refreshRow() for failure test.
     * </p>
     *
     * @throws SQLException always
     */
    public void refreshRow() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements cancelRowUpdates() for failure test.
     * </p>
     *
     * @throws SQLException always
     */
    public void cancelRowUpdates() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements moveToInsertRow() for failure test.
     * </p>
     *
     * @throws SQLException always
     */
    public void moveToInsertRow() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements moveToCurrentRow() for failure test.
     * </p>
     *
     * @throws SQLException always
     */
    public void moveToCurrentRow() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getStatement() for failure test.
     * </p>
     *
     * @return never
     *
     * @throws SQLException always
     */
    public Statement getStatement() throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getObject() for failure test.
     * </p>
     *
     * @param i i
     * @param map map
     *
     * @return never
     *
     * @throws SQLException always
     */
    @SuppressWarnings("unchecked")
	public Object getObject(int i, Map map) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getRef() for failure test.
     * </p>
     *
     * @param i i
     *
     * @return never
     *
     * @throws SQLException always
     */
    public Ref getRef(int i) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getBlob() for failure test.
     * </p>
     *
     * @param i i
     *
     * @return never
     *
     * @throws SQLException always
     */
    public Blob getBlob(int i) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getClob() for failure test.
     * </p>
     *
     * @param i i
     *
     * @return never
     *
     * @throws SQLException always
     */
    public Clob getClob(int i) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getArray() for failure test.
     * </p>
     *
     * @param i i
     *
     * @return never
     *
     * @throws SQLException always
     */
    public Array getArray(int i) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getObject() for failure test.
     * </p>
     *
     * @param colName colName
     * @param map map
     *
     * @return never
     *
     * @throws SQLException always
     */
    @SuppressWarnings("unchecked")
	public Object getObject(String colName, Map map) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getRef() for failure test.
     * </p>
     *
     * @param colName colName
     *
     * @return never
     *
     * @throws SQLException always
     */
    public Ref getRef(String colName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getBlob() for failure test.
     * </p>
     *
     * @param colName colName
     *
     * @return never
     *
     * @throws SQLException always
     */
    public Blob getBlob(String colName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getClob() for failure test.
     * </p>
     *
     * @param colName colName
     *
     * @return never
     *
     * @throws SQLException always
     */
    public Clob getClob(String colName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getArray() for failure test.
     * </p>
     *
     * @param colName colName
     *
     * @return never
     *
     * @throws SQLException always
     */
    public Array getArray(String colName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getDate() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param cal calendar
     * @return never
     *
     * @throws SQLException always
     */
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getDate() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param cal calendar
     * @return never
     *
     * @throws SQLException always
     */
    public Date getDate(String columnName, Calendar cal) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getTime() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param cal calendar
     * @return never
     *
     * @throws SQLException always
     */
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getTime() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param cal calendar
     * @return never
     *
     * @throws SQLException always
     */
    public Time getTime(String columnName, Calendar cal) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getTimestamp() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param cal calendar
     * @return never
     *
     * @throws SQLException always
     */
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getTimestamp() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param cal calendar
     * @return never
     *
     * @throws SQLException always
     */
    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getURL() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return never
     *
     * @throws SQLException always
     */
    public java.net.URL getURL(int columnIndex) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements getURL() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @return never
     *
     * @throws SQLException always
     */
    public java.net.URL getURL(String columnName) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateRef() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateRef() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateRef(String columnName, Ref x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateBlob() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateBlob() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateBlob(String columnName, Blob x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements updateClob() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements next() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateClob(String columnName, Clob x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements next() for failure test.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateArray(int columnIndex, Array x) throws SQLException {
        throw new SQLException();
    }

    /**
     * <p>
     * Implements next() for failure test.
     * </p>
     *
     * @param columnName columnName
     * @param x x
     *
     * @throws SQLException always
     */
    public void updateArray(String columnName, Array x) throws SQLException {
        throw new SQLException();
    }
    
    /*
     * tberthelot - for Java 1.6
    public RowId getRowId(int columnIndex) throws SQLException { throw new SQLException(); }
    
    public RowId getRowId(String columnLabel) throws SQLException { throw new SQLException(); }
    
    public void updateRowId(int columnIndex, RowId x) throws SQLException { throw new SQLException(); }
    
    public void updateRowId(String columnLabel, RowId x) throws SQLException { throw new SQLException(); }
    
    public int getHoldability() throws SQLException { throw new SQLException(); }
    public boolean isClosed() throws SQLException { throw new SQLException(); }
    public void updateNString(int columnIndex, String nString) throws SQLException { throw new SQLException(); }
    
    public void updateNString(String columnLabel, String nString) throws SQLException { throw new SQLException(); }
    
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException { throw new SQLException(); }
    
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException { throw new SQLException(); }
    
    public NClob getNClob(int columnIndex) throws SQLException { throw new SQLException(); }
    
    public NClob getNClob(String columnLabel) throws SQLException { throw new SQLException(); }
    
    public SQLXML getSQLXML(int columnIndex) throws SQLException { throw new SQLException(); }

    public SQLXML getSQLXML(String columnLabel) throws SQLException { throw new SQLException(); }
    
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException { throw new SQLException(); }
    
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException { throw new SQLException(); }
    
    public String getNString(int columnIndex) throws SQLException { throw new SQLException(); }
    
    public String getNString(String columnLabel) throws SQLException { throw new SQLException(); }
    
    public java.io.Reader getNCharacterStream(int columnIndex) throws SQLException { throw new SQLException(); }
    
    public java.io.Reader getNCharacterStream(String columnLabel) throws SQLException { throw new SQLException(); }
    
    public void updateNCharacterStream(int columnIndex,
			     java.io.Reader x,
			     long length) throws SQLException { throw new SQLException(); }
    
    public void updateNCharacterStream(String columnLabel,
			     java.io.Reader reader,
			     long length) throws SQLException { throw new SQLException(); }
               
    public void updateBinaryStream(int columnIndex, 
			    java.io.InputStream x,
			    long length) throws SQLException { throw new SQLException(); }
    
    public void updateCharacterStream(int columnIndex,
			     java.io.Reader x,
			     long length) throws SQLException { throw new SQLException(); }
    
    public void updateAsciiStream(int columnIndex,
			   java.io.InputStream x) throws SQLException { throw new SQLException(); }

    public void updateAsciiStream(int columnIndex,
			   java.io.InputStream x, 
			   long length) throws SQLException { throw new SQLException(); }
    
    public void updateAsciiStream(String columnLabel, 
			   java.io.InputStream x, 
			   long length) throws SQLException { throw new SQLException(); }
    
    public void updateBinaryStream(String columnLabel, 
			    java.io.InputStream x,
			    long length) throws SQLException { throw new SQLException(); }
    
    public void updateCharacterStream(String columnLabel,
			     java.io.Reader reader,
			     long length) throws SQLException { throw new SQLException(); }
    
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException { throw new SQLException(); }
    
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException { throw new SQLException(); }
    
    public void updateClob(int columnIndex,  Reader reader, long length) throws SQLException { throw new SQLException(); }
    
    public void updateClob(String columnLabel,  Reader reader, long length) throws SQLException { throw new SQLException(); }
    
    public void updateNClob(int columnIndex,  Reader reader, long length) throws SQLException { throw new SQLException(); }
    
    public void updateNClob(String columnLabel,  Reader reader, long length) throws SQLException { throw new SQLException(); }
    
    public void updateNCharacterStream(int columnIndex,
			     java.io.Reader x) throws SQLException { throw new SQLException(); }
    
    public void updateNCharacterStream(String columnLabel,
			     java.io.Reader reader) throws SQLException{}    
    
    public void updateCharacterStream(int columnIndex,
			     java.io.Reader x) throws SQLException { throw new SQLException(); }
        
    public void updateAsciiStream(String columnLabel,
			   java.io.InputStream x) throws SQLException { throw new SQLException(); }

    public void updateBinaryStream(int columnIndex,
			    java.io.InputStream x) throws SQLException { throw new SQLException(); }
    
    public void updateBinaryStream(String columnLabel,
			    java.io.InputStream x) throws SQLException { throw new SQLException(); }

    public void updateCharacterStream(String columnLabel,
			     java.io.Reader reader) throws SQLException {}
    
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {}

    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {}
    
    public void updateClob(int columnIndex,  Reader reader) throws SQLException {}

    public void updateClob(String columnLabel,  Reader reader) throws SQLException {}
    
    public void updateNClob(int columnIndex,  Reader reader) throws SQLException {}
    public void updateNClob(String columnLabel,  Reader reader) throws SQLException {  }
    
    public boolean isWrapperFor(java.lang.Class iface) throws java.sql.SQLException {
        return false;
    }
    
    public Object unwrap(java.lang.Class iface) throws java.sql.SQLException {
        throw new SQLException();
    }
    */
}
