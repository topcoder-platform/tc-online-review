/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.failuretests;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * A ResultSet implementation wrapping a List of Lists rather than real
 * database query results.  This ResultSet implementation may not fully abide
 * by the ResultSet contract with regard to datatype conversions.
 * </p>
 *
 * @author ThinMan
 * @author biotrail
 * @version 1.1
 * @since 1.0
 */
class SimpleResultSet extends ExceptionalResultSet {
    /**
     * <p>
     * A ResultSetMetaData instance to store meta data.
     * </p>
     *
     * @since 1.0
     */
    private ResultSetMetaData metaData;

    /**
     * <p>
     * Stores all the row data.
     * The element in rows is of List type.
     * </p>
     *
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
    private List rows;

    /**
     * <p>
     * The current position.
     * </p>
     *
     * @since 1.0
     */
    private int current = -1;

    /**
     * <p>
     * The current row data.
     * </p>
     *
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
    private List currentRow = null;

    /**
     * <p>
     * A flag to identify whether the last column read can be null or not.
     * </p>
     *
     * @since 1.0
     */
    private boolean nullFlag = false;

    /**
     * <p>
     * A mapping from column name to its position.
     * </p>
     *
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
    private Map nameMap = null;

    /**
     * <p>
     * Create a new SimpleResultSet instance.
     * </p>
     *
     * @param rsmd result set meta data
     */
    @SuppressWarnings("unchecked")
    SimpleResultSet(ResultSetMetaData rsmd) {
        metaData = rsmd;
        rows = new ArrayList();
    }

    /**
     * <p>
     * Add a row record.
     * </p>
     *
     * @param values a new row data.
     */
    @SuppressWarnings("unchecked")
    public void addRow(List values) {
        rows.add(values);
    }

    /**
     * <p>
     * Implements the next() method.
     * </p>
     *
     * @return a boolean value
     * @throws SQLException to JUnit
     */
    @SuppressWarnings("unchecked")
    public boolean next() throws SQLException {
        current = Math.min(current + 1, rows.size());
        if (current < rows.size()) {
            currentRow = (List) rows.get(current);
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>
     * Implements the close() method.
     * </p>
     *
     * @throws SQLException to JUnit
     */
    @SuppressWarnings("unchecked")
    public void close() throws SQLException {
        rows = new ArrayList();
        current = -1;
        currentRow = null;
        nullFlag = false;
    }

    /**
     * <p>
     * Implements the wasNull() method.
     * </p>
     *
     * @return a boolean value
     * @throws SQLException to JUnit
     */
    public boolean wasNull() throws SQLException {
        return nullFlag;
    }

    /**
     * <p>
     * Implements the getString() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a string value
     *
     * @throws SQLException to JUnit
     */
    public String getString(int columnIndex) throws SQLException {
        try {
            return (String) getObject(columnIndex);
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getBoolean() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a boolean value
     * @throws SQLException to JUnit
     */
    public boolean getBoolean(int columnIndex) throws SQLException {
        try {
            Boolean b = (Boolean) getObject(columnIndex);
            return (b != null) && b.booleanValue();
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getByte() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a byte value
     * @throws SQLException to JUnit
     */
    public byte getByte(int columnIndex) throws SQLException {
        try {
            Number n = (Number) getObject(columnIndex);
            return (n == null) ? (byte) 0 : n.byteValue();
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getShort() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a short value
     * @throws SQLException to JUnit
     */
    public short getShort(int columnIndex) throws SQLException {
        try {
            Number n = (Number) getObject(columnIndex);
            return (n == null) ? (short) 0 : n.shortValue();
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getInt() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a int value
     * @throws SQLException to JUnit
     */
    public int getInt(int columnIndex) throws SQLException {
        try {
            Number n = (Number) getObject(columnIndex);
            return (n == null) ? 0 : n.intValue();
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getLong() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a long value
     * @throws SQLException to JUnit
     */
    public long getLong(int columnIndex) throws SQLException {
        try {
            Number n = (Number) getObject(columnIndex);
            return (n == null) ? 0L : n.longValue();
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getFloat() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a float value
     * @throws SQLException to JUnit
     */
    public float getFloat(int columnIndex) throws SQLException {
        try {
            Number n = (Number) getObject(columnIndex);
            return (n == null) ? 0f : n.floatValue();
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getDouble() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a double value
     * @throws SQLException to JUnit
     */
    public double getDouble(int columnIndex) throws SQLException {
        try {
            Number n = (Number) getObject(columnIndex);
            return (n == null) ? 0d : n.doubleValue();
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getBytes() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a byte array value
     * @throws SQLException to JUnit
     */
    public byte[] getBytes(int columnIndex) throws SQLException {
        try {
            return (byte[]) getObject(columnIndex);
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getDate() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a date value
     * @throws SQLException to JUnit
     */
    public Date getDate(int columnIndex) throws SQLException {
        try {
            return (Date) getObject(columnIndex);
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getTime() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a time value
     * @throws SQLException to JUnit
     */
    public Time getTime(int columnIndex) throws SQLException {
        Date d = getDate(columnIndex);

        return (d == null) ? null : new Time(d.getTime());
    }

    /**
     * <p>
     * Implements the getTimestamp() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a time stamp value
     * @throws SQLException to JUnit
     */
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        Date d = getDate(columnIndex);

        return (d == null) ? null : new Timestamp(d.getTime());
    }

    /**
     * <p>
     * Implements the getAsciiStream() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a input stream value
     * @throws SQLException to JUnit
     */
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        try {
            return (InputStream) getObject(columnIndex);
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getBinaryStream() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a input stream value
     * @throws SQLException to JUnit
     */
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        try {
            return (InputStream) getObject(columnIndex);
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getString() method.
     * </p>
     *
     * @param columnName columnName
     * @return a string value
     * @throws SQLException to JUnit
     */
    public String getString(String columnName) throws SQLException {
        return getString(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getBoolean() method.
     * </p>
     *
     * @param columnName columnName
     * @return a boolean value
     * @throws SQLException to JUnit
     */
    public boolean getBoolean(String columnName) throws SQLException {
        return getBoolean(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getBytes() method.
     * </p>
     *
     * @param columnName columnName
     * @return a byte value
     * @throws SQLException to JUnit
     */
    public byte getByte(String columnName) throws SQLException {
        return getByte(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getShort() method.
     * </p>
     *
     * @param columnName columnName
     * @return a short value
     * @throws SQLException to JUnit
     */
    public short getShort(String columnName) throws SQLException {
        return getShort(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getInt() method.
     * </p>
     *
     * @param columnName columnName
     * @return an int value
     * @throws SQLException to JUnit
     */
    public int getInt(String columnName) throws SQLException {
        return getInt(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getLong() method.
     * </p>
     *
     * @param columnName columnName
     * @return a long value
     * @throws SQLException to JUnit
     */
    public long getLong(String columnName) throws SQLException {
        return getLong(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getFloat() method.
     * </p>
     *
     * @param columnName columnName
     * @return a float value
     * @throws SQLException to JUnit
     */
    public float getFloat(String columnName) throws SQLException {
        return getFloat(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getDouble() method.
     * </p>
     *
     * @param columnName columnName
     * @return a double value
     * @throws SQLException to JUnit
     */
    public double getDouble(String columnName) throws SQLException {
        return getDouble(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getBytes() method.
     * </p>
     *
     * @param columnName columnName
     * @return a byte array value
     * @throws SQLException to JUnit
     */
    public byte[] getBytes(String columnName) throws SQLException {
        return getBytes(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getDate() method.
     * </p>
     *
     * @param columnName columnName
     * @return a date value
     * @throws SQLException to JUnit
     */
    public Date getDate(String columnName) throws SQLException {
        return getDate(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getTime() method.
     * </p>
     *
     * @param columnName columnName
     * @return a time value
     * @throws SQLException to JUnit
     */
    public Time getTime(String columnName) throws SQLException {
        return getTime(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getTimestamp() method.
     * </p>
     *
     * @param columnName columnName
     * @return a time stamp value
     * @throws SQLException to JUnit
     */
    public Timestamp getTimestamp(String columnName) throws SQLException {
        return getTimestamp(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getAsciiStream() method.
     * </p>
     *
     * @param columnName columnName
     * @return a input stream value
     * @throws SQLException to JUnit
     */
    public InputStream getAsciiStream(String columnName) throws SQLException {
        return getAsciiStream(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getBinaryStream() method.
     * </p>
     *
     * @param columnName columnName
     * @return a input stream value
     * @throws SQLException to JUnit
     */
    public InputStream getBinaryStream(String columnName) throws SQLException {
        return getBinaryStream(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getWarnings() method.
     * </p>
     *
     * @return null
     * @throws SQLException to JUnit
     */
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    /**
     * <p>
     * Implements the clearWarnings() method.
     * </p>
     *
     */
    public void clearWarnings() {
    }

    /**
     * <p>
     * Implements the getMetaData() method.
     * </p>
     *
     * @return result set meta data
     * @throws SQLException to JUnit
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        return metaData;
    }

    /**
     * <p>
     * Implements the getObject() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return an Object value
     * @throws SQLException to JUnit
     */
    public Object getObject(int columnIndex) throws SQLException {
        if (currentRow == null) {
            throw new SQLException();
        }
        Object o = currentRow.get(columnIndex - 1);

        nullFlag = (o == null);

        return o;
    }

    /**
     * <p>
     * Implements the getObject() method.
     * </p>
     *
     * @param columnName columnName
     * @return an Object value
     * @throws SQLException to JUnit
     */
    public Object getObject(String columnName) throws SQLException {
        return getObject(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the findColumn() method.
     * </p>
     *
     * @param columnName columnName
     * @return the column position
     * @throws SQLException to JUnit
     */
    @SuppressWarnings("unchecked")
    public int findColumn(String columnName) throws SQLException {
        if (nameMap == null) {
            nameMap = new HashMap();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                nameMap.put(metaData.getColumnName(i), new Integer(i));
            }
        }

        Integer in = (Integer) nameMap.get(columnName);
        if (in == null) {
            throw new SQLException("No such column: " + columnName);
        }

        return in.intValue();
    }

    /**
     * <p>
     * Implements the getCharacterStream() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a Reader value
     * @throws SQLException to JUnit
     */
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        try {
            return (Reader) getObject(columnIndex);
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getCharacterStream() method.
     * </p>
     *
     * @param columnName columnName
     * @return a Reader value
     * @throws SQLException to JUnit
     */
    public Reader getCharacterStream(String columnName) throws SQLException {
        return getCharacterStream(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the getBigDecimal() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a BigDecimal value
     * @throws SQLException to JUnit
     */
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        try {
            return (BigDecimal) getObject(columnIndex);
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getBigDecimal() method.
     * </p>
     *
     * @param columnName columnName
     * @return a BigDecimal value
     * @throws SQLException to JUnit
     */
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        return getBigDecimal(findColumn(columnName));
    }

    /**
     * <p>
     * Implements the isBeforeFirst() method.
     * </p>
     *
     * @return a boolean value
     * @throws SQLException to JUnit
     */
    public boolean isBeforeFirst() throws SQLException {
        return (current < 0);
    }

    /**
     * <p>
     * Implements the isAfterLast() method.
     * </p>
     *
     * @return a boolean value
     * @throws SQLException to JUnit
     */
    public boolean isAfterLast() throws SQLException {
        return (current >= rows.size());
    }

    /**
     * <p>
     * Implements the isFirst() method.
     * </p>
     *
     * @return a boolean value
     * @throws SQLException to JUnit
     */
    public boolean isFirst() throws SQLException {
        return ((current == 0) && !isAfterLast());
    }

    /**
     * <p>
     * Implements the isLast() method.
     * </p>
     *
     * @return a boolean value
     * @throws SQLException to JUnit
     */
    public boolean isLast() throws SQLException {
        return ((current == (rows.size() - 1)) && !isBeforeFirst());
    }

    /**
     * <p>
     * Implements the getRow() method.
     * </p>
     *
     * @return the current row number
     * @throws SQLException to JUnit
     */
    public int getRow() throws SQLException {
        return (isBeforeFirst() || isAfterLast()) ? 0 : (current + 1);
    }

    /**
     * <p>
     * Implements the setFetchDirection() method.
     * </p>
     *
     * @param direction direction
     * @throws SQLException to JUnit
     */
    public void setFetchDirection(int direction) throws SQLException {
    }

    /**
     * <p>
     * Implements the getFetchDirection() method.
     * </p>
     *
     * @return ResultSet.FETCH_UNKNOWN
     * @throws SQLException to JUnit
     */
    public int getFetchDirection() throws SQLException {
        return ResultSet.FETCH_UNKNOWN;
    }

    /**
     * <p>
     * Implements the setFetchSize() method.
     * </p>
     *
     * @param rows rows
     * @throws SQLException to JUnit
     */
    public void setFetchSize(int rows) throws SQLException {
    }

    /**
     * <p>
     * Implements the getFetchSize() method.
     * </p>
     *
     * @return 1
     * @throws SQLException to JUnit
     */
    public int getFetchSize() throws SQLException {
        return 1;
    }

    /**
     * <p>
     * Implements the getType() method.
     * </p>
     *
     * @return ResultSet.TYPE_FORWARD_ONLY
     * @throws SQLException to JUnit
     */
    public int getType() throws SQLException {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    /**
     * <p>
     * Implements the getConcurrency() method.
     * </p>
     *
     * @return ResultSet.CONCUR_READ_ONLY
     * @throws SQLException to JUnit
     */
    public int getConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }

    /**
     * <p>
     * Implements the rowUpdated() method.
     * </p>
     *
     * @return false
     * @throws SQLException to JUnit
     */
    public boolean rowUpdated() throws SQLException {
        return false;
    }

    /**
     * <p>
     * Implements the rowInserted() method.
     * </p>
     *
     * @return false
     * @throws SQLException to JUnit
     */
    public boolean rowInserted() throws SQLException {
        return false;
    }

    /**
     * <p>
     * Implements the rowDeleted() method.
     * </p>
     *
     * @return false
     * @throws SQLException to JUnit
     */
    public boolean rowDeleted() throws SQLException {
        return false;
    }

    /**
     * <p>
     * Implements the refreshRow() method.
     * </p>
     *
     * @throws SQLException to JUnit
     */
    public void refreshRow() throws SQLException {
    }

    /**
     * <p>
     * Implements the cancelRowUpdates() method.
     * </p>
     *
     * @throws SQLException to JUnit
     */
    public void cancelRowUpdates() throws SQLException {
    }

    /**
     * <p>
     * Implements the getObject() method.
     * </p>
     *
     * @param i index
     * @param map map
     * @return an Object value
     *
     * @throws SQLException to JUnit
     */
    @SuppressWarnings("unchecked")
    public Object getObject(int i, Map map) throws SQLException {
        return getObject(i);
    }

    /**
     * <p>
     * Implements the getRef() method.
     * </p>
     *
     * @param i index
     * @return a Ref value
     *
     * @throws SQLException to JUnit
     */
    public Ref getRef(int i) throws SQLException {
        try {
            return (Ref) getObject(i);
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getBlob() method.
     * </p>
     *
     * @param i index
     * @return a Blob value
     *
     * @throws SQLException to JUnit
     */
    public Blob getBlob(int i) throws SQLException {
        try {
            return (Blob) getObject(i);
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getClob() method.
     * </p>
     *
     * @param i index
     * @return a Clob value
     *
     * @throws SQLException to JUnit
     */
    public Clob getClob(int i) throws SQLException {
        try {
            return (Clob) getObject(i);
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getArray() method.
     * </p>
     *
     * @param i index
     * @return an Array value
     *
     * @throws SQLException to JUnit
     */
    public Array getArray(int i) throws SQLException {
        try {
            return (Array) getObject(i);
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getStruct() method.
     * </p>
     *
     * @param i index
     * @return a Struct value
     *
     * @throws SQLException to JUnit
     */
    public Struct getStruct(int i) throws SQLException {
        try {
            return (Struct) getObject(i);
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getObject() method.
     * </p>
     *
     * @param colName colName
     * @param map map
     * @return an Object value
     *
     * @throws SQLException to JUnit
     */
    @SuppressWarnings("unchecked")
    public Object getObject(String colName, Map map) throws SQLException {
        return getObject(findColumn(colName), map);
    }

    /**
     * <p>
     * Implements the getRef() method.
     * </p>
     *
     * @param colName colName
     * @return a Ref value
     *
     * @throws SQLException to JUnit
     */
    public Ref getRef(String colName) throws SQLException {
        return getRef(findColumn(colName));
    }

    /**
     * <p>
     * Implements the getBlob() method.
     * </p>
     *
     * @param colName colName
     * @return a Blob value
     *
     * @throws SQLException to JUnit
     */
    public Blob getBlob(String colName) throws SQLException {
        return getBlob(findColumn(colName));
    }

    /**
     * <p>
     * Implements the getClob() method.
     * </p>
     *
     * @param colName colName
     * @return a Clob value
     *
     * @throws SQLException to JUnit
     */
    public Clob getClob(String colName) throws SQLException {
        return getClob(findColumn(colName));
    }

    /**
     * <p>
     * Implements the getArray() method.
     * </p>
     *
     * @param colName colName
     * @return an Array value
     *
     * @throws SQLException to JUnit
     */
    public Array getArray(String colName) throws SQLException {
        return getArray(findColumn(colName));
    }

    /**
     * <p>
     * Implements the getStruct() method.
     * </p>
     *
     * @param colName colName
     * @return a Struct value
     *
     * @throws SQLException to JUnit
     */
    public Struct getStruct(String colName) throws SQLException {
        return getStruct(findColumn(colName));
    }

    /**
     * <p>
     * Implements the getDate() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param cal calendar
     * @return a Date value
     *
     * @throws SQLException to JUnit
     */
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return getDate(columnIndex);
    }

    /**
     * <p>
     * Implements the getDate() method.
     * </p>
     *
     * @param columnName columnName
     * @param cal calendar
     * @return a Date value
     *
     * @throws SQLException to JUnit
     */
    public Date getDate(String columnName, Calendar cal) throws SQLException {
        return getDate(findColumn(columnName), cal);
    }

    /**
     * <p>
     * Implements the getTime() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param cal calendar
     * @return a Time value
     *
     * @throws SQLException to JUnit
     */
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return getTime(columnIndex);
    }

    /**
     * <p>
     * Implements the getTime() method.
     * </p>
     *
     * @param columnName columnName
     * @param cal calendar
     * @return a Time value
     *
     * @throws SQLException to JUnit
     */
    public Time getTime(String columnName, Calendar cal) throws SQLException {
        return getTime(findColumn(columnName), cal);
    }

    /**
     * <p>
     * Implements the getTimestamp() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @param cal calendar
     * @return a Timestamp value
     *
     * @throws SQLException to JUnit
     */
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return getTimestamp(columnIndex);
    }

    /**
     * <p>
     * Implements the getTimestamp() method.
     * </p>
     *
     * @param columnName columnName
     * @param cal calendar
     * @return a Timestamp value
     *
     * @throws SQLException to JUnit
     */
    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        return getTimestamp(findColumn(columnName), cal);
    }

    /**
     * <p>
     * Implements the getURL() method.
     * </p>
     *
     * @param columnIndex columnIndex
     * @return a URL value
     *
     * @throws SQLException to JUnit
     */
    public URL getURL(int columnIndex) throws SQLException {
        try {
            return (URL) getObject(columnIndex);
        } catch (ClassCastException cce) {
            throw new SQLException(cce.getMessage());
        }
    }

    /**
     * <p>
     * Implements the getURL() method.
     * </p>
     *
     * @param columnName columnName
     * @return a URL value
     *
     * @throws SQLException to JUnit
     */
    public URL getURL(String columnName) throws SQLException {
        return getURL(findColumn(columnName));
    }

}
