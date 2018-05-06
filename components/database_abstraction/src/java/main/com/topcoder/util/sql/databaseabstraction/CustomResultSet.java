/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * The CustomResultSet class is similar to the JDBC ResultSet class in the
 * public API that it exposes. This is intentional, in order to facility
 * familiarity and ease of use of the class.
 * </p>
 * <p>
 * Version 1.1 uses a different data structure to store the row data. 1.0. In
 * version 1.0, data was stored in a rows list, which each entry being a list
 * that contained the column values for the row. In version 1.1, both the
 * currently mapped value and the original JDBC value need to be preserved
 * through the use of the RowDataValue class. By keeping track of the original
 * value, the component allows for on demand conversions to be applied to both
 * the original or mapped value.
 * </p>
 * <p>
 * <strong>Changes in 2.0:</strong>
 * <ol>
 * <li>Specified generic parameters for generic types.</li>
 * <li>Added getURL() methods.</li>
 * <li>getXXX() methods for primitive types and getXXX(...,Calendar) methods
 * throw IllegalArgumentException instead of NullPointerException when
 * columnIndex is invalid or column with the specified name doesn't exist.</li>
 * <li>getXXX() methods for primitive types and getXXX(...,Calendar) methods
 * throw NullColumnValueException instead of NullPointerException when null
 * object is found.</li>
 * <li>Add missing throws return for getMetaData().</li>
 * </ol>
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> This class is immutable and thread safe.
 * </p>
 *
 * @author argolite, aubergineanode, saarixx, WishingBone, justforplay, suhugo
 * @version 2.0
 * @since 1.0
 */
public class CustomResultSet {

    /**
     * <p>
     * The data that makes up the CustomResultSet. The contents of this field
     * are also Lists, and the contents of these Lists are all RowDataValue
     * objects. Items in both lists are required to be non-null. The number of
     * items in this list is the number of rows in the ResultSet that was used
     * in constructing the CustomResultSet, and each list in this list contains
     * as many RowDataValue items as there are columns.
     * </p>
     * <p>
     * This field is immutable, although its contents can be changed, through
     * mapping or sorting.
     * </p>
     * <p>
     * This field is the main data storage for this class, and as such is used
     * in almost every method.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameter for the collection type.</li>
     * </ol>
     * </p>
     */
    private final List<List<RowDataValue>> rows = new ArrayList<List<RowDataValue>>();

    /**
     * The current row that the result set is positioned at. The current row
     * accessed in the rows List is given by currentRow - 1. (The -1 is needed
     * because the CustomResultSet uses JDBC 1-based indexing while the rows
     * list uses java-style 0-based indexing.) A value of 0 means that the
     * result set is positioned before the first row, while a value >
     * rows.size() means that the result set is positioned after all rows. This
     * field is used in all the get methods of this class, the positioning
     * methods (afterLast, beforeFirst, absolute). It is also set to 0 in the
     * sortAscending/Descending methods. This field is mutable.
     */
    private int currentRow = 0;

    /**
     * <p>
     * The metadata for the result set. This field is set in the constructor (to
     * a newly created CustomResultSetMetaData), and is immutable and never
     * null. The value of this field can be retrieved through the getMetaData
     * method. The metadata is not actively used by any part of this class. It
     * is only passed to converters or returned through the getMetaData method.
     * </p>
     */
    private final CustomResultSetMetaData metaData;

    /**
     * The mapper for converting values from one type to another on the fly.
     * This field is set in the constructor and is immutable. It can be null, in
     * which case on-demand conversion is not available (in this case the
     * CustomResultSet behaves exactly like version 1.0 for a completely
     * backwards compatible API). This field is used indirectly from all the
     * getXXX methods, but is used directly in the getObject(int/String, Class)
     * and isAvailable(int/String, Class) methods.
     *
     * @since 1.1
     */
    private final OnDemandMapper onDemandMapper;

    /**
     * <p>
     * Creates a new CustomResultSet from the given JDBC result set.
     * </p>
     * <p>
     * Version 1.1 Added documentation about NullPointerException which would be
     * thrown, but was not documented in version 1.0.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Renamed parameter "rs" to "resultSet" to meet TopCoder standards.</li>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * resultSet is null.</li>
     * </ol>
     * </p>
     *
     * @param resultSet
     *            the result set to construct from
     * @throws IllegalArgumentException
     *             If resultSet is null.
     * @throws SQLException
     *             when SQL exception takes place in fetching the data
     */
    public CustomResultSet(ResultSet resultSet) throws SQLException {
        // In order to avoid introducing IllegalMappingException in this
        // constructor.
        // It doesn't invoke this(resultSet, null).
        AbstractionHelper.checkNull(resultSet, "resultSet");

        metaData = new CustomResultSetMetaData(resultSet.getMetaData());
        setRowsFromRs(resultSet);
        onDemandMapper = OnDemandMapper.createDefaultOnDemandMapper();
    }

    /**
     * <p>
     * Creates a CustomResultSet from the given Constructor from result set and
     * mapper.
     * </p>
     * <p>
     * Version 1.1 Added documentation about NullPointerException which would be
     * thrown, but was not documented in version 1.0.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Renamed parameter "rs" to "resultSet" to meet TopCoder standards.</li>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * resultSet is null.</li>
     * </ol>
     * </p>
     *
     * @param resultSet
     *            the result set to construct from
     * @param mapper
     *            the explicit mapper
     * @throws IllegalArgumentException
     *             If resultSet is null.
     * @throws IllegalMappingException
     *             when mapping is illegal
     * @throws SQLException
     *             when SQL exception takes place in fetching the data
     */
    public CustomResultSet(ResultSet resultSet, Mapper mapper) throws IllegalMappingException, SQLException {
        this(resultSet, mapper, OnDemandMapper.createDefaultOnDemandMapper());
    }

    /**
     * <p>
     * Creates a new CustomResultSet that does explicit mapping of values using
     * mapper and also allows values to be accessed (and converted on the fly)
     * through the use of the onDemandMapper.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Renamed parameter "rs" to "resultSet" to meet TopCoder standards.</li>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * resultSet is null.</li>
     * </ol>
     * </p>
     *
     * @param resultSet
     *            The result set to load data from
     * @param mapper
     *            The explicit mapping to apply to the column values - can be
     *            null
     * @param onDemandMapper
     *            The mapper to use for on-demand conversion - can be null
     * @throws IllegalArgumentException
     *             If resultSet is null.
     * @throws IllegalMappingException
     *             when mapping is illegal.
     * @throws SQLException
     *             When there is an error reading the data
     * @since 1.1
     */
    public CustomResultSet(ResultSet resultSet, Mapper mapper, OnDemandMapper onDemandMapper) throws SQLException,
            IllegalMappingException {
        AbstractionHelper.checkNull(resultSet, "resultSet");

        metaData = new CustomResultSetMetaData(resultSet.getMetaData());
        setRowsFromRs(resultSet);
        remap(mapper);
        this.onDemandMapper = onDemandMapper;
    }

    /**
     * <p>
     * Positions the result set to the given absolute row number. Negative value
     * specifies that the number is relative from the end of the list.
     * </p>
     *
     * @return whether the row exists
     * @param row
     *            the row to move to
     */
    public boolean absolute(int row) {
        if (row < 0) {
            row += rows.size() + 1;
        }
        if (row < 1) {
            currentRow = 0;
            return false;
        } else if (row > rows.size()) {
            currentRow = rows.size() + 1;
            return false;
        } else {
            currentRow = row;
            return true;
        }
    }

    /**
     * <p>
     * Positions the result set on the row after the final row in the result
     * set.
     * </p>
     */
    public void afterLast() {
        currentRow = rows.size() + 1;
    }

    /**
     * <p>
     * Positions the result set before the first row of the results.
     * </p>
     */
    public void beforeFirst() {
        currentRow = 0;
    }

    /**
     * <p>
     * Find the column index with specified column name, returning 0 if no
     * column has the given name.
     * </p>
     *
     * @return the column index, 0 if not found
     * @param columnName
     *            the column name to find
     */
    public int findColumn(String columnName) {
        if (columnName != null) {
            for (int i = 1; i <= metaData.getColumnCount(); ++i) {
                if (columnName.equals(metaData.getColumnLabel(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    /**
     * <p>
     * Positions the result set on the first row of the results.
     * </p>
     *
     * @return whether the first row exists
     */
    public boolean first() {
        currentRow = 1;
        return rows.size() > 0;
    }

    /**
     * <p>
     * Get data as an Array. If columnIndex is invalid (&lt;= 0 or &gt; column
     * count), null is returned.
     * </p>
     *
     * @return the data as an Array
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Array
     */
    public Array getArray(int columnIndex) throws InvalidCursorStateException {
        return (Array) getObject(columnIndex, Array.class);
    }

    /**
     * <p>
     * Get data as an Array.If column does not exist, null is returned.
     * </p>
     *
     * @return the data as Array
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to an Array
     */
    public Array getArray(String columnName) throws InvalidCursorStateException {
        return getArray(findColumn(columnName));
    }

    /**
     * <p>
     * Get data as an ASCII stream. If columnIndex is invalid (&lt;= 0 or &gt;
     * column count), null is returned.
     * </p>
     *
     * @return the data as an InputStream
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a InputStream
     */
    public InputStream getAsciiStream(int columnIndex) throws InvalidCursorStateException {
        return (InputStream) getObject(columnIndex, InputStream.class);
    }

    /**
     * <p>
     * Get data as an ASCII stream. If column does not exist, null is returned.
     * </p>
     *
     * @return the data as an InputStream
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to an InputStream
     */
    public InputStream getAsciiStream(String columnName) throws InvalidCursorStateException {
        return getAsciiStream(findColumn(columnName));
    }

    /**
     * <p>
     * Get data as a BigDecimal.If columnIndex is invalid (&lt;= 0 or &gt;
     * column count), null is returned.
     * </p>
     *
     * @return the data as BigDecimal
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a BigDecimal
     */
    public BigDecimal getBigDecimal(int columnIndex) throws InvalidCursorStateException {
        return (BigDecimal) getObject(columnIndex, BigDecimal.class);
    }

    /**
     * <p>
     * Get data as a BigDecimal.If column does not exist, null is returned.
     * </p>
     *
     * @return the data as BigDecimal
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a BigDecimal
     */
    public BigDecimal getBigDecimal(String columnName) throws InvalidCursorStateException {
        return getBigDecimal(findColumn(columnName));
    }

    /**
     * <p>
     * Get data as a binary stream. If columnIndex is invalid (&lt;= 0 or &gt;
     * column count),null is returned.
     * </p>
     *
     * @return the data as an InputStream
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a stream
     */
    public InputStream getBinaryStream(int columnIndex) throws InvalidCursorStateException {
        return (InputStream) getObject(columnIndex, InputStream.class);
    }

    /**
     * <p>
     * Get data as a binary stream. If column does not exist, null is returned.
     * </p>
     *
     * @return the data as an InputStream
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a stream
     */
    public InputStream getBinaryStream(String columnName) throws InvalidCursorStateException {
        return getBinaryStream(findColumn(columnName));
    }

    /**
     * <p>
     * Get data as a Blob. If columnIndex is invalid (&lt;= 0 or &gt; column
     * count), null is returned.
     * </p>
     *
     * @return the data as Blob
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Blob
     */
    public Blob getBlob(int columnIndex) throws InvalidCursorStateException {
        return (Blob) getObject(columnIndex, Blob.class);
    }

    /**
     * <p>
     * Get data as a Blob. If column does not exist, null is returned.
     * </p>
     *
     * @return the data as Blob
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Blob
     */
    public Blob getBlob(String columnName) throws InvalidCursorStateException {
        return getBlob(findColumn(columnName));
    }

    /**
     * <p>
     * Get data as a boolean.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * columnIndex is invalid.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Boolean
     * @param columnIndex
     *            index of the column to get
     * @throws IllegalArgumentException
     *             If columnIndex is invalid(<= 0 or > column count)
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow <= 0 or >
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a boolean
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public boolean getBoolean(int columnIndex) throws InvalidCursorStateException, NullColumnValueException {
        return ((Boolean) getObjectWithChecking(columnIndex, Boolean.class)).booleanValue();
    }

    /**
     * <p>
     * Get data as a boolean.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * column doesn't exist.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Boolean
     * @param columnName
     *            name of the column to get
     * @throws IllegalArgumentException
     *             If column does not exist
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a boolean
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public boolean getBoolean(String columnName) throws InvalidCursorStateException, NullColumnValueException {
        return getBoolean(findColumnWithChecking(columnName));
    }

    /**
     * <p>
     * Get data as a byte.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * columnIndex is invalid.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Byte
     * @param columnIndex
     *            index of the column to get
     * @throws IllegalArgumentException
     *             If columnIndex is invalid(<= 0 or > column count)
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a byte
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public byte getByte(int columnIndex) throws InvalidCursorStateException, NullColumnValueException {
        return ((Byte) getObjectWithChecking(columnIndex, Byte.class)).byteValue();
    }

    /**
     * <p>
     * Get data as a byte.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * column doesn't exist.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Byte
     * @param columnName
     *            name of the column to get
     * @throws IllegalArgumentException
     *             If column does not exist
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a byte
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public byte getByte(String columnName) throws InvalidCursorStateException, NullColumnValueException {
        return getByte(findColumnWithChecking(columnName));
    }

    /**
     * <p>
     * Get data as a byte[]. If columnIndex is invalid (&lt;= 0 or &gt; column
     * count), null is returned.
     * </p>
     *
     * @return the data as a byte[]
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a byte[]
     */
    public byte[] getBytes(int columnIndex) throws InvalidCursorStateException {
        return (byte[]) getObject(columnIndex, byte[].class);
    }

    /**
     * <p>
     * Get data as a byte[].If column does not exist, null is returned.
     *
     * @return the data as a byte[]
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a byte[]
     */
    public byte[] getBytes(String columnName) throws InvalidCursorStateException {
        return getBytes(findColumn(columnName));
    }

    /**
     * <p>
     * Get data as a character stream.If columnIndex is invalid (&lt;= 0 or &gt;
     * column count), null is returned.
     * </p>
     *
     * @return the data as CharacterStream
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Reader
     */
    public Reader getCharacterStream(int columnIndex) throws InvalidCursorStateException {
        return (Reader) getObject(columnIndex, Reader.class);
    }

    /**
     * <p>
     * Get data as a character stream. If column does not exist, null is
     * returned.
     * </p>
     *
     * @return the data as CharacterStream
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Reader
     */
    public Reader getCharacterStream(String columnName) throws InvalidCursorStateException {
        return getCharacterStream(findColumn(columnName));
    }

    /**
     * <p>
     * Get data as a Clob. If columnIndex is invalid (&lt;= 0 or &gt; column
     * count), null is returned.
     * </p>
     *
     * @return the data as Clob
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to Clob
     */
    public Clob getClob(int columnIndex) throws InvalidCursorStateException {
        return (Clob) getObject(columnIndex, Clob.class);
    }

    /**
     * <p>
     * Get data as a Clob.If column does not exist, null is returned.
     * </p>
     *
     * @return the data as Clob
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Clob
     */
    public Clob getClob(String columnName) throws InvalidCursorStateException {
        return getClob(findColumn(columnName));
    }

    /**
     * <p>
     * Get data as a Date.If columnIndex is invalid (&lt;= 0 or &gt; column
     * count), null is returned.
     * </p>
     *
     * @return the data as Date
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Date
     */
    public Date getDate(int columnIndex) throws InvalidCursorStateException {
        return (Date) getObject(columnIndex, Date.class);
    }

    /**
     * <p>
     * Get data as a Date.If column does not exist, null is returned.
     * </p>
     *
     * @return the data as Date
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Date
     */
    public Date getDate(String columnName) throws InvalidCursorStateException {
        return getDate(findColumn(columnName));
    }

    /**
     * <p>
     * Get data as a Date converted into the given calendar basis.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * columnIndex is invalid, or calendar is null.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Date
     * @param columnIndex
     *            index of the column to get
     * @param calendar
     *            the specified Calendar
     * @throws IllegalArgumentException
     *             If columnIndex is invalid(<= 0 or > column count), or
     *             calendar is null.
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Date
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public Date getDate(int columnIndex, Calendar calendar) throws InvalidCursorStateException,
            NullColumnValueException {
        AbstractionHelper.checkNull(calendar, "calendar");
        calendar.setTime((Date) getObjectWithChecking(columnIndex, Date.class));
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * <p>
     * Get data as a Date converted it into the given calendar basis.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * column doesn't exist, or calendar is null.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Date
     * @param columnName
     *            name of the column to get
     * @param calendar
     *            the specified calendar
     * @throws IllegalArgumentException
     *             If column does not exist, or calendar is null.
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a date
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public Date getDate(String columnName, Calendar calendar) throws InvalidCursorStateException,
            NullColumnValueException {
        return getDate(findColumnWithChecking(columnName), calendar);
    }

    /**
     * <p>
     * Get data as a double.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * columnIndex is invalid.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Double
     * @param columnIndex
     *            index of the column to get
     * @throws IllegalArgumentException
     *             If columnIndex is invalid(<= 0 or > column count)
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a double
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public double getDouble(int columnIndex) throws InvalidCursorStateException, NullColumnValueException {
        return ((Double) getObjectWithChecking(columnIndex, Double.class)).doubleValue();
    }

    /**
     * <p>
     * Get data as a double.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * column doesn't exist.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Double
     * @param columnName
     *            name of the column to get
     * @throws IllegalArgumentException
     *             If column does not exist
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a double
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public double getDouble(String columnName) throws InvalidCursorStateException, NullColumnValueException {
        return getDouble(findColumnWithChecking(columnName));
    }

    /**
     * <p>
     * Get data as a float.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * columnIndex is invalid.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Float
     * @param columnIndex
     *            index of the column to get
     * @throws IllegalArgumentException
     *             If columnIndex is invalid(<= 0 or > column count)
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a float
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public float getFloat(int columnIndex) throws InvalidCursorStateException, NullColumnValueException {
        return ((Float) getObjectWithChecking(columnIndex, Float.class)).floatValue();
    }

    /**
     * <p>
     * Get data as a float.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * column doesn't exist.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Float
     * @param columnName
     *            name of the column to get
     * @throws IllegalArgumentException
     *             If column does not exist
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a float
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public float getFloat(String columnName) throws InvalidCursorStateException, NullColumnValueException {
        return getFloat(findColumnWithChecking(columnName));
    }

    /**
     * <p>
     * Get data as an int.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * columnIndex is invalid.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Integer
     * @param columnIndex
     *            index of the column to get
     * @throws IllegalArgumentException
     *             If columnIndex is invalid(<= 0 or > column count)
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to an int
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public int getInt(int columnIndex) throws InvalidCursorStateException, NullColumnValueException {
        return ((Integer) getObjectWithChecking(columnIndex, Integer.class)).intValue();
    }

    /**
     * <p>
     * Get data as an int.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * column doesn't exist.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Integer
     * @param columnName
     *            name of the column to get
     * @throws IllegalArgumentException
     *             If column does not exist
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to an int
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public int getInt(String columnName) throws InvalidCursorStateException, NullColumnValueException {
        return getInt(findColumnWithChecking(columnName));
    }

    /**
     * <p>
     * Get data as a long.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * columnIndex is invalid.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Long
     * @param columnIndex
     *            index of the column to get
     * @throws IllegalArgumentException
     *             If columnIndex is invalid(<= 0 or > column count)
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a long
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public long getLong(int columnIndex) throws InvalidCursorStateException, NullColumnValueException {
        return ((Long) getObjectWithChecking(columnIndex, Long.class)).longValue();
    }

    /**
     * <p>
     * Get data as a long.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * column doesn't exist.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Long
     * @param columnName
     *            name of the column to get
     * @throws IllegalArgumentException
     *             If column does not exist
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a long
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public long getLong(String columnName) throws InvalidCursorStateException, NullColumnValueException {
        return getLong(findColumnWithChecking(columnName));
    }

    /**
     * <p>
     * Get data as an Object. Return the mapped value for the given column for
     * the current row. If columnIndex is &lt;= 0 or &gt; column count, null is
     * returned.Note: Only mapped value is retrieved.
     * </p>
     *
     * @return the data as Object
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to an Object
     */
    public Object getObject(int columnIndex) throws InvalidCursorStateException {
        if (currentRow < 1 || currentRow > rows.size()) {
            throw new InvalidCursorStateException("currentRow of CustomResultSet -- " + currentRow + " -- is invalid.");
        }
        if (columnIndex < 1 || columnIndex > metaData.getColumnCount()) {
            return null;
        }
        return rows.get(currentRow - 1).get(columnIndex - 1).getMappedValue();
    }

    /**
     * <p>
     * Get data as an Object. Return the mapped value for the given column for
     * the current row.If column does not exist, null is returned. Note: Only
     * mapped value is retrieved.
     * </p>
     *
     * @return the data as Object
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to an Object
     */
    public Object getObject(String columnName) throws InvalidCursorStateException {
        return getObject(findColumn(columnName));
    }

    /**
     * Get the number of records in the custom result set .
     *
     * @return the count number of records
     */
    public int getRecordCount() {
        return rows.size();
    }

    /**
     * <p>
     * Get data as an Ref.If columnIndex is invalid (&lt;= 0 or &gt; column
     * count), null is returned.
     * </p>
     *
     * @return the data as Ref
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to Ref
     */
    public Ref getRef(int columnIndex) throws InvalidCursorStateException {
        return (Ref) getObject(columnIndex, Ref.class);
    }

    /**
     * <p>
     * Get data as an Ref.If column does not exist, null is returned.
     * </p>
     *
     * @return the data as Ref
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Ref
     */
    public Ref getRef(String columnName) throws InvalidCursorStateException {
        return getRef(findColumn(columnName));
    }

    /**
     * Get current row number.
     *
     * @return current row number
     */
    public int getRow() {
        return currentRow;
    }

    /**
     * <p>
     * Get data as an short.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * columnIndex is invalid.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Short
     * @param columnIndex
     *            index of the column to get
     * @throws IllegalArgumentException
     *             If columnIndex is invalid(<= 0 or > column count)
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a short
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public short getShort(int columnIndex) throws InvalidCursorStateException, NullColumnValueException {
        return ((Short) getObjectWithChecking(columnIndex, Short.class)).shortValue();
    }

    /**
     * <p>
     * Get data as an short.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * column doesn't exist.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Short
     * @param columnName
     *            name of the column to get
     * @throws IllegalArgumentException
     *             If column does not exist
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a short
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public short getShort(String columnName) throws InvalidCursorStateException, NullColumnValueException {
        return getShort(findColumnWithChecking(columnName));
    }

    /**
     * <p>
     * Get data as an String. If columnIndex is invalid (&lt;= 0 or &gt; column
     * count), null is returned.
     * </p>
     *
     * @return the data as String
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a String
     */
    public String getString(int columnIndex) throws InvalidCursorStateException {
        return (String) getObject(columnIndex, String.class);
    }

    /**
     * <p>
     * Get data as an String.If column does not exist, null is returned.
     *
     * @return the data as String
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a String
     */
    public String getString(String columnName) throws InvalidCursorStateException {
        return getString(findColumn(columnName));
    }

    /**
     * <p>
     * Get data as an Struct.If columnIndex is invalid (&lt;= 0 or &gt; column
     * count), null is returned.
     * </p>
     *
     * @return the data as Struct
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Struct
     */
    public Struct getStruct(int columnIndex) throws InvalidCursorStateException {
        return (Struct) getObject(columnIndex, Struct.class);
    }

    /**
     * <p>
     * Get data as an Struct.If column does not exist, null is returned.
     *
     * @return the data as Struct
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Struct
     */
    public Struct getStruct(String columnName) throws InvalidCursorStateException {
        return getStruct(findColumn(columnName));
    }

    /**
     * <p>
     * Get data as an Time. If columnIndex is invalid (&lt;= 0 or &gt; column
     * count),null is returned.
     * </p>
     *
     * @return the data as Time
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Time
     */
    public Time getTime(int columnIndex) throws InvalidCursorStateException {
        return (Time) getObject(columnIndex, Time.class);
    }

    /**
     * <p>
     * Get data as an Time.If column does not exist, null is returned.
     *
     * @return the data as Time
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Time
     */
    public Time getTime(String columnName) throws InvalidCursorStateException {
        return getTime(findColumn(columnName));
    }

    /**
     * <p>
     * Get data as an Time converted into the given calendar basis (corrects for
     * time zone, daylight savings time, etc).
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * columnIndex is invalid, or calendar is null.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Time
     * @param columnIndex
     *            index of the column to get
     * @param calendar
     *            the specified Calendar
     * @throws IllegalArgumentException
     *             If columnIndex is invalid(<= 0 or > column count), or
     *             calendar is null.
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Time
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public Time getTime(int columnIndex, Calendar calendar) throws InvalidCursorStateException,
            NullColumnValueException {
        AbstractionHelper.checkNull(calendar, "calendar");
        calendar.setTime((Time) getObjectWithChecking(columnIndex, Time.class));
        return new Time(calendar.getTimeInMillis());
    }

    /**
     * <p>
     * Get data as an Time converted into the given calendar basis (corrects for
     * time zone, daylight savings time, etc).
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * column doesn't exist, or calendar is null.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Time
     * @param columnName
     *            name of the column to get
     * @param calendar
     *            the specified Calendar
     * @throws IllegalArgumentException
     *             If column does not exist, or calendar is null.
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Time
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public Time getTime(String columnName, Calendar calendar) throws InvalidCursorStateException,
            NullColumnValueException {
        return getTime(findColumnWithChecking(columnName), calendar);
    }

    /**
     * <p>
     * Get data as an Timestamp. If columnIndex is invalid (&lt;= 0 or &gt;
     * column count), null is returned.
     * </p>
     *
     * @return the data as Timestamp
     * @param columnIndex
     *            index of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Timestamp
     */
    public Timestamp getTimestamp(int columnIndex) throws InvalidCursorStateException {
        return (Timestamp) getObject(columnIndex, Timestamp.class);
    }

    /**
     * <p>
     * Get data as an Timestamp.If column does not exist, null is returned.
     *
     * @return the data as Timestamp
     * @param columnName
     *            name of the column to get
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Timestamp
     */
    public Timestamp getTimestamp(String columnName) throws InvalidCursorStateException {
        return getTimestamp(findColumn(columnName));
    }

    /**
     * <p>
     * Get data as an Timestamp converted into the given calendar basis
     * (corrects for time zone, daylight savings time, etc).
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * columnIndex is invalid, or calendar is null.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Timestamp
     * @param columnIndex
     *            index of the column to get
     * @param calendar
     *            the specified Calendar
     * @throws IllegalArgumentException
     *             If columnIndex is invalid(<= 0 or > column count), or
     *             calendar is null.
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Timestamp
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public Timestamp getTimestamp(int columnIndex, Calendar calendar) throws InvalidCursorStateException,
            NullColumnValueException {
        AbstractionHelper.checkNull(calendar, "calendar");
        calendar.setTime((Timestamp) getObjectWithChecking(columnIndex, Timestamp.class));
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * <p>
     * Get data as an Timestamp converted into the given calendar basis
     * (corrects for time zone, daylight savings time, etc).
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * column doesn't exist, or calendar is null.</li>
     * <li>Throws NullColumnValueException instead of NullPointerException when
     * null object is found.</li>
     * </ol>
     * </p>
     *
     * @return the data as Timestamp
     * @param columnName
     *            name of the column to get
     * @param calendar
     *            the specified Calendar
     * @throws IllegalArgumentException
     *             If column does not exist, or calendar is null.
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a Timestamp
     * @throws NullColumnValueException
     *             If null object is found.
     */
    public Timestamp getTimestamp(String columnName, Calendar calendar) throws InvalidCursorStateException,
            NullColumnValueException {
        return getTimestamp(findColumnWithChecking(columnName), calendar);
    }

    /**
     * <p>
     * Get data as an URL. If columnIndex is invalid (<= 0 or > column count),
     * null is returned.
     * </p>
     *
     * @param columnIndex
     *            index of the column to get
     * @return the data as URL
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a URL
     * @since 2.0
     */
    public URL getURL(int columnIndex) throws InvalidCursorStateException {
        return (URL) getObject(columnIndex, URL.class);
    }

    /**
     * <p>
     * Get data as an URL. If column does not exist, null is returned.
     * </p>
     *
     * @param columnName
     *            name of the column to get
     * @return the data as URL
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a URL
     * @since 2.0
     */
    public URL getURL(String columnName) throws InvalidCursorStateException {
        return getURL(findColumn(columnName));
    }

    /**
     * Whether the current row is after the last row in the result set.
     *
     * @return whether the current row is after the last one
     */
    public boolean isAfterLast() {
        return currentRow > rows.size();
    }

    /**
     * Whether the current row is before the first one.
     *
     * @return whether the current row is before the first one
     */
    public boolean isBeforeFirst() {
        return currentRow < 1;
    }

    /**
     * Whether the current row is the first one.
     *
     * @return whether the current row is the first one
     */
    public boolean isFirst() {
        return currentRow == 1 && rows.size() > 0;
    }

    /**
     * Whether the current row is the last one.
     *
     * @return whether the current row is the last one
     */
    public boolean isLast() {
        return currentRow > 0 && currentRow == rows.size();
    }

    /**
     * Move the current row to the last.
     *
     * @return whether the last row exists
     */
    public boolean last() {
        currentRow = rows.size();
        return currentRow > 0;
    }

    /**
     * Move the current row to the next.
     *
     * @return whether the next row exists
     */
    public boolean next() {
        if (currentRow <= rows.size()) {
            ++currentRow;
        }
        return currentRow <= rows.size();
    }

    /**
     * Move the current row to the previous.
     *
     * @return whether the previous row exists
     */
    public boolean previous() {
        if (currentRow >= 1) {
            --currentRow;
        }
        return currentRow >= 1 && rows.size() > 0;
    }

    /**
     * Move to relative row count.
     *
     * @return whether the row exists
     * @param row
     *            the relative row count
     */
    public boolean relative(int row) {
        return absolute(currentRow + row);
    }

    /**
     * <p>
     * Remap the data in the result set with specified mapper. For each column
     * in the CustomResultSet that has corresponding converter(with key equal to
     * the column type name) in mapper, its value is converted and stored. If
     * mapper is null, nothing is done.
     * </p>
     * <p>
     * In version 1.1 remapping will reserve the original value using
     * DataRowValue to store the original and mapped value.
     * </p>
     *
     * @param mapper
     *            the mapper to use (may be null, in which case nothing is done)
     * @throws IllegalMappingException
     *             when mapping is illegal
     */
    public void remap(Mapper mapper) throws IllegalMappingException {
        if (mapper == null || mapper.getMap() == null) {
            return;
        }
        Map<String, Converter> map = mapper.getMap();
        for (int i = 1; i <= metaData.getColumnCount(); ++i) {
            if (metaData.getColumnTypeName(i) == null) {
                continue;
            }
            Converter converter = map.get(metaData.getColumnTypeName(i).toLowerCase());
            if (converter != null) {
                Iterator<List<RowDataValue>> itr = rows.iterator();
                for (int j = 0; j < rows.size(); ++j) {
                    List<RowDataValue> vrow = itr.next();
                    try {
                        RowDataValue dataValue = vrow.get(i - 1);
                        dataValue.setMappedValue(converter.convert(dataValue.getMappedValue(), i, metaData));
                    } catch (IndexOutOfBoundsException e) {
                        throw new IllegalMappingException("mapping is illegal.", e);
                    } catch (NullPointerException e) {
                        throw new IllegalMappingException("mapping is illegal.", e);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalMappingException("mapping is illegal.", e);
                    }
                }
            }
        }
    }

    /**
     * <p>
     * Sort the rows in ascending order based on the values in the given column.
     * Also re-position the result set to before the first row. If columnIndex
     * is not within the range of the columns in this table, the result of the
     * sort is unspecified. The rows are sorted based on the current mapped data
     * values, not the original values.
     * </p>
     *
     * @param columnIndex
     *            index of the column to sort on
     * @throws ClassCastException
     *             when the items in the given column do not implement the
     *             Comparable interface
     */
    public void sortAscending(int columnIndex) {
        Collections.sort(rows, new SimpleComparator(columnIndex, null, false));
        currentRow = 0;
    }

    /**
     * <p>
     * Sort the rows in ascending order based on the values in the given column.
     * Also re-position the result set to before the first row. If columnName is
     * not the name of a column in this table, the result of the sort is
     * unspecified. The rows are sorted based on the current mapped data values,
     * not the original values.
     * </p>
     *
     * @param columnName
     *            name of the column to sort on
     * @throws ClassCastException
     *             when the items in the given column do not implement the
     *             Comparable interface
     */
    public void sortAscending(String columnName) {
        sortAscending(findColumn(columnName));
    }

    /**
     * <p>
     * Sort the rows in ascending order using the given comparator. Also
     * re-position the result set to before the first row. If columnIndex is not
     * within the range of the columns in this table, the result of the sort is
     * unspecified. The rows are sorted based on the current mapped data values,
     * not the original values.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameter for Comparator type.</li>
     * </ol>
     * </p>
     *
     * @param columnIndex
     *            index of the column to sort on
     * @param comparator
     *            the comparator to use. If null, uses the Comparable interface
     *            of the underlying objects.
     * @throws ClassCastException
     *             When the comparator can not compare the objects
     */
    public void sortAscending(int columnIndex, Comparator<Object> comparator) {
        Collections.sort(rows, new SimpleComparator(columnIndex, comparator, false));
        currentRow = 0;
    }

    /**
     * <p>
     * Sort the rows in ascending order using the given comparator. Also
     * re-position the result set to before the first row. If columnName is not
     * a name of a column in this table, the result of the sort is unspecified.
     * The rows are sorted based on the current mapped data values, not the
     * original values.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameter for Comparator type.</li>
     * </ol>
     * </p>
     *
     * @param columnName
     *            name of the column to sort on
     * @param comparator
     *            the comparator to use
     * @throws ClassCastException
     *             When the comparator can not compare the objects
     */
    public void sortAscending(String columnName, Comparator<Object> comparator) {
        sortAscending(findColumn(columnName), comparator);
    }

    /**
     * <p>
     * Sort the rows in ascending order. The sort is done on the column given by
     * the first element of columnIndices, rows whose value are the same in the
     * first column are then sorted by the second element of columnIndices. Rows
     * that are still tied are then sorted by the third element of
     * columnIndices, and so forth. Also re-position the result set to before
     * the first row. The rows are sorted based on the current mapped data
     * values, not the original values.
     * </p>
     *
     * @param columnIndices
     *            indices of the columns to sort on
     * @throws ClassCastException
     *             when the items in the given column do not implement the
     *             Comparable interface
     */
    public void sortAscending(int[] columnIndices) {
        Collections.sort(rows, new ArrayComparator(columnIndices, null, false));
        currentRow = 0;
    }

    /**
     * <p>
     * Sort the rows in ascending order. The sort is done on the column given by
     * the first element of columnNames, rows whose value are the same in the
     * first column are then sorted by the second element of columnNames. Rows
     * that are still tied are then sorted by the third element of columnNames,
     * and so forth. Also re-position the result set to before the first row.
     * The rows are sorted based on the current mapped data values, not the
     * original values.
     * </p>
     *
     * @param columnNames
     *            names of the columns to sort on
     * @throws ClassCastException
     *             when the items in the given column do not implement the
     *             Comparable interface
     */
    public void sortAscending(String[] columnNames) {
        int[] columnIndices = new int[columnNames.length];
        for (int i = 0; i < columnNames.length; ++i) {
            columnIndices[i] = findColumn(columnNames[i]);
        }
        sortAscending(columnIndices);
    }

    /**
     * <p>
     * Sort the rows in ascending order. The sort is done on the column given by
     * the first element of columnIndices, rows whose value are the same in the
     * first column (according to the first element of comparator) are then
     * sorted by the second element of columnIndices. Rows that are still tied
     * are then sorted by the third element of columnIndices, and so forth. Also
     * re-position the result set to before the first row. The rows are sorted
     * based on the current mapped data values, not the original values.
     * </p>
     * <p>
     * In version 1.1. IndexOutOfBoundsException is documented, but it could
     * previously be thrown by the code in version 1.0.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameter for Comparator type.</li>
     * </ol>
     * </p>
     *
     * @param columnIndices
     *            indices of the columns to sort on
     * @param comparators
     *            the comparators to use in sorting for the given columns. Null
     *            items in the array will cause the Comparable interface of the
     *            items in that column to be used.
     * @throws ClassCastException
     *             when the items in one of the columns are not appropriate for
     *             the comparator in use for that column
     * @throws IndexOutOfBoundsException
     *             if comparators is a shorter array than columnIndices
     */
    public void sortAscending(int[] columnIndices, Comparator<Object>[] comparators) {
        Collections.sort(rows, new ArrayComparator(columnIndices, comparators, false));
        currentRow = 0;
    }

    /**
     * <p>
     * Sort the rows in ascending order. The sort is done on the column given by
     * the first element of columnNames, rows whose value are the same in the
     * first column (according to the first comparator in the array) are then
     * sorted by the second element of columnNames. Rows that are still tied are
     * then sorted by the third element of columnNames, and so forth. Also
     * re-position the result set to before the first row. The rows are sorted
     * based on the current mapped data values, not the original values.
     * </p>
     * <p>
     * In version 1.1. IndexOutOfBoundsException is documented, but it could
     * previously be thrown by the code in version 1.0.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameter for Comparator type.</li>
     * </ol>
     * </p>
     *
     * @param columnNames
     *            names of the columns to sort on
     * @param comparators
     *            the comparators to use in sorting for the given columns. Null
     *            items in the array will cause the Comparable interface of the
     *            items in that column to be used.
     * @throws ClassCastException
     *             when the items in one of the columns are not appropriate for
     *             the comparator in use for that column
     * @throws IndexOutOfBoundsException
     *             if comparators is a shorter array than columnNames
     */
    public void sortAscending(String[] columnNames, Comparator<Object>[] comparators) {
        int[] columnIndices = new int[columnNames.length];
        for (int i = 0; i < columnNames.length; ++i) {
            columnIndices[i] = findColumn(columnNames[i]);
        }
        sortAscending(columnIndices, comparators);
    }

    /**
     * <p>
     * Sort the rows in descending order based on the values in the given
     * column. Also re-position the result set to before the first row. If
     * columnIndex is not within the range of the columns in this table, the
     * result of the sort is unspecified.The rows are sorted based on the
     * current mapped data values, not the original values.
     * </p>
     *
     * @param columnIndex
     *            index of the column to sort on
     * @throws ClassCastException
     *             when the items in the given column do not implement the
     *             Comparable interface
     */
    public void sortDescending(int columnIndex) {
        Collections.sort(rows, new SimpleComparator(columnIndex, null, true));
        currentRow = 0;
    }

    /**
     * <p>
     * Sort the rows in descending order based on the values in the given
     * column. Also re-position the result set to before the first row. If
     * columnName is not the name of a column in this table, the result of the
     * sort is unspecified.The rows are sorted based on the current mapped data
     * values, not the original values.
     * </p>
     *
     * @param columnName
     *            name of the column to sort on
     * @throws ClassCastException
     *             when the items in the given column do not implement the
     *             Comparable interface
     */
    public void sortDescending(String columnName) {
        sortDescending(findColumn(columnName));
    }

    /**
     * <p>
     * Sort the rows in descending order using the given comparator. Also
     * re-position the result set to before the first row. If columnIndex is not
     * within the range of the columns in this table, the result of the sort is
     * unspecified.The rows are sorted based on the current mapped data values,
     * not the original values.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameter for Comparator type.</li>
     * </ol>
     * </p>
     *
     * @param columnIndex
     *            index of the column to sort on
     * @param comparator
     *            the comparator to use
     * @throws ClassCastException
     *             When the comparator can not compare the objects
     */
    public void sortDescending(int columnIndex, Comparator<Object> comparator) {
        Collections.sort(rows, new SimpleComparator(columnIndex, comparator, true));
        currentRow = 0;
    }

    /**
     * <p>
     * Sort the rows in descending order using the given comparator. Also
     * re-position the result set to before the first row. If columnName is not
     * a name of a column in this table, the result of the sort is
     * unspecified.The rows are sorted based on the current mapped data values,
     * not the original values.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameter for Comparator type.</li>
     * </ol>
     * </p>
     *
     * @param columnName
     *            name of the column to sort on
     * @param comparator
     *            the comparator to use
     * @throws ClassCastException
     *             When the comparator can not compare the objects
     */
    public void sortDescending(String columnName, Comparator<Object> comparator) {
        sortDescending(findColumn(columnName), comparator);
    }

    /**
     * <p>
     * Sort the rows in descending order. The sort is done on the column given
     * by the first element of columnIndices, rows whose value are the same in
     * the first column are then sorted by the second element of columnIndices.
     * Rows that are still tied are then sorted by the third element of
     * columnIndices, and so forth. Also re-position the result set to before
     * the first row.The rows are sorted based on the current mapped data
     * values, not the original values.
     * </p>
     *
     * @param columnIndices
     *            indices of the columns to sort on
     * @throws ClassCastException
     *             when the items in the given column do not implement the
     *             Comparable interface
     */
    public void sortDescending(int[] columnIndices) {
        Collections.sort(rows, new ArrayComparator(columnIndices, null, true));
        currentRow = 0;
    }

    /**
     * <p>
     * Sort the rows in descending order. The sort is done on the column given
     * by the first element of columnNames, rows whose value are the same in the
     * first column are then sorted by the second element of columnNames. Rows
     * that are still tied are then sorted by the third element of columnNames,
     * and so forth. Also re-position the result set to before the first row.The
     * rows are sorted based on the current mapped data values, not the original
     * values.
     * </p>
     *
     * @param columnNames
     *            names of the columns to sort on
     * @throws ClassCastException
     *             when the items in the given column do not implement the
     *             Comparable interface
     */
    public void sortDescending(String[] columnNames) {
        int[] columnIndices = new int[columnNames.length];
        for (int i = 0; i < columnNames.length; ++i) {
            columnIndices[i] = findColumn(columnNames[i]);
        }
        sortDescending(columnIndices);
    }

    /**
     * <p>
     * Sort the rows in descending order. The sort is done on the column given
     * by the first element of columnIndices, rows whose value are the same in
     * the first column (according to the first element of comparator) are then
     * sorted by the second element of columnIndices. Rows that are still tied
     * are then sorted by the third element of columnIndices, and so forth. Also
     * re-position the result set to before the first row.The rows are sorted
     * based on the current mapped data values, not the original values.
     * </p>
     * <p>
     * In version 1.1. IndexOutOfBoundsException is documented, but it could
     * previously be thrown by the code in version 1.0.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameter for Comparator type.</li>
     * </ol>
     * </p>
     *
     * @param columnIndices
     *            indices of the columns to sort on
     * @param comparators
     *            the comparators to use in sorting for the given columns. Null
     *            items in the array will cause the Comparable interface of the
     *            items in that column to be used.
     * @throws ClassCastException
     *             when the items in one of the columns are not appropriate for
     *             the comparator in use for that column
     * @throws IndexOutOfBoundsException
     *             if comparators is a shorter array than columnIndices
     */
    public void sortDescending(int[] columnIndices, Comparator<Object>[] comparators) {
        Collections.sort(rows, new ArrayComparator(columnIndices, comparators, true));
        currentRow = 0;
    }

    /**
     * <p>
     * Sort the rows in descending order. The sort is done on the column given
     * by the first element of columnNames, rows whose value are the same in the
     * first column (according to the first comparator in the array) are then
     * sorted by the second element of columnNames. Rows that are still tied are
     * then sorted by the third element of columnNames, and so forth. Also
     * re-position the result set to before the first row.The rows are sorted
     * based on the current mapped data values, not the original values.
     * <p>
     * In version 1.1. IndexOutOfBoundsException is documented, but it could
     * previously be thrown by the code in version 1.0.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameter for Comparator type.</li>
     * </ol>
     * </p>
     *
     * @param columnNames
     *            names of the columns to sort on
     * @param comparators
     *            the comparators to use in sorting for the given columns. Null
     *            items in the array will cause the Comparable interface of the
     *            items in that column to be used.
     * @throws ClassCastException
     *             when the items in one of the columns are not appropriate for
     *             the comparator in use for that column
     * @throws IndexOutOfBoundsException
     *             if comparators is a shorter array than columnNames
     */
    public void sortDescending(String[] columnNames, Comparator<Object>[] comparators) {
        int[] columnIndices = new int[columnNames.length];
        for (int i = 0; i < columnNames.length; ++i) {
            columnIndices[i] = findColumn(columnNames[i]);
        }
        sortDescending(columnIndices, comparators);
    }

    /**
     * <p>
     * Gets the object in the given column for the current row that the result
     * set is positioned on, returning the object converted to the desired type.
     * This method tries the 4-part method of finding a suitable return as
     * outlined in the component specification.
     * </p>
     * <p>
     * 1) If the getMappedValue of the data in the current row and given column
     * is of desiredType, return it.<br>
     * 2) If the getOriginalValue of the data in the current row and given
     * column is of desiredType, return it.<br>
     * 3) If onDemandMapper.canConvert the original value, run the convert
     * method and return the result <br>
     * 4) If onDemandMapper.canConvert the mapped value, run the convert method
     * and return the result
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Changed type of "desiredType" parameter from "Class" to "Class<?>".</li>
     * </ol>
     * </p>
     *
     * @param columnIndex
     *            The index of the column to retrieve data from
     * @param desiredType
     *            The type of the desired return value
     * @return The value in the column as the desired type, or null if the
     *         column is &lt;= 0 or &gt; column count
     * @throws ClassCastException
     *             If an on-demand converter indicates that it can handle the
     *             conversion but fails or value can not be converted to the
     *             desired type.
     * @throws IllegalArgumentException
     *             If desiredType is null
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @since 1.1
     */
    public Object getObject(int columnIndex, Class<?> desiredType) throws InvalidCursorStateException {
        if (desiredType == null) {
            throw new IllegalArgumentException("desiredType should not be null.");
        }
        // Validate currentRow.
        if (currentRow < 1 || currentRow > rows.size()) {
            throw new InvalidCursorStateException("current row of CustomResultSet -- " + currentRow + " is invalid.");
        }
        // Validate columnIndex.
        if (columnIndex < 1 || columnIndex > metaData.getColumnCount()) {
            return null;
        }
        RowDataValue dataValue = rows.get(currentRow - 1).get(columnIndex - 1);
        if (dataValue.getMappedValue() == null || desiredType.isAssignableFrom(dataValue.getMappedValue().getClass())) {
            // If mapped value is null or desiredType is assignable from mapped
            // value, return the mapped
            // value.
            return dataValue.getMappedValue();

        } else if (dataValue.getOriginalValue() == null
                || desiredType.isAssignableFrom(dataValue.getOriginalValue().getClass())) {
            // If original value is null or desiredType is assignable from
            // original value, return the
            // original value.
            return dataValue.getOriginalValue();
        }
        if (onDemandMapper != null) {
            try {
                if (onDemandMapper.canConvert(dataValue.getOriginalValue(), columnIndex, metaData, desiredType)) {
                    // If original value can be converted to desired type,
                    // return converted value.
                    return onDemandMapper.convert(dataValue.getOriginalValue(), columnIndex, metaData, desiredType);
                } else if (onDemandMapper.canConvert(dataValue.getMappedValue(), columnIndex, metaData, desiredType)) {
                    // If mapped value can be converted to desired type, return
                    // converted value.
                    return onDemandMapper.convert(dataValue.getMappedValue(), columnIndex, metaData, desiredType);
                }
            } catch (IllegalMappingException e) {
                throw new ClassCastException("fail to cast value to desired type -- " + desiredType + ","
                        + e.getMessage());
            }
        }
        throw new ClassCastException("fail to cast value to desired type -- " + desiredType
                + ", no converter is found.");
    }

    /**
     * Gets the object in the given column for the current row that the result
     * set is positioned on, returning the object converted to the desired
     * type.This method tries the 4-part method of finding a suitable return as
     * outlined in the component specification.
     * <p>
     * 1) If the getMappedValue of the data in the current row and given column
     * is of desiredType, return it.<br>
     * 2) If the getOriginalValue of the data in the current row and given
     * column is of desiredType, return it.<br>
     * 3) If onDemandMapper.canConvert the original value, run the convert
     * method and return the result <br>
     * 4) If onDemandMapper.canConvert the mapped value, run the convert method
     * and return the result
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Changed type of "desiredType" parameter from "Class" to "Class<?>".</li>
     * </ol>
     * </p>
     *
     * @param columnName
     *            The name of the column to get the data from
     * @param desiredType
     *            The type the return should be
     * @return The value in the current row and given column converted to
     *         desiredType
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             If an on-demand converter indicates that it can handle the
     *             conversion but fails or value can not be converted to the
     *             desired type.
     * @throws IllegalArgumentException
     *             If desiredType is null
     * @since 1.1
     */
    public Object getObject(String columnName, Class<?> desiredType) throws InvalidCursorStateException {
        return getObject(findColumn(columnName), desiredType);
    }

    /**
     * Determines whether the data in the given column is of the desired type or
     * can be converted (on demand) to the given type. If columnIndex is &lt;= 0
     * or &gt; column count, false is returned.This method tries the 5-part
     * method of determining whether it can be converted.
     * <p>
     * 1) If the getMappedValue of the data in the current row and given column
     * is of desiredType, return true.<br>
     * 2) If the getOriginalValue of the data in the current row and given
     * column is of desiredType, return true.<br>
     * 3) If onDemandMapper can convert the original value, return true <br>
     * 4) If onDemandMapper can convert the mapped value, return true<br>
     * 5) else return false.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Changed type of "desiredType" parameter from "Class" to "Class<?>".</li>
     * </ol>
     * </p>
     *
     * @param columnIndex
     *            The index of the column to determine availability of
     * @param desiredType
     *            The desired type to learn about convertibility
     * @return True if the data could be converted, false otherwise
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws IllegalArgumentException
     *             If desiredType is null.
     * @since 1.1
     */
    public boolean isAvailable(int columnIndex, Class<?> desiredType) throws InvalidCursorStateException {
        if (desiredType == null) {
            throw new IllegalArgumentException("desiredType should not be null.");
        }
        // Validate currentRow.
        if (currentRow < 1 || currentRow > rows.size()) {
            throw new InvalidCursorStateException("current row of CustomResultSet -- " + currentRow + " is invalid.");
        }
        // Validate columnIndex.
        if (columnIndex < 1 || columnIndex > metaData.getColumnCount()) {
            return false;
        }
        RowDataValue dataValue = rows.get(currentRow - 1).get(columnIndex - 1);
        if (dataValue.getMappedValue() == null || desiredType.isAssignableFrom(dataValue.getMappedValue().getClass())) {
            // If mapped value is null or desiredType is assignable from mapped
            // value, return true.
            return true;
        } else if (dataValue.getOriginalValue() == null
                || desiredType.isAssignableFrom(dataValue.getOriginalValue().getClass())) {
            // If original value is null or desiredType is assignable from
            // original value, then return true.
            return true;
        }
        if (onDemandMapper != null) {
            if (onDemandMapper.canConvert(dataValue.getOriginalValue(), columnIndex, metaData, desiredType)) {
                // If original value can be converted to the desired type,
                // return true.
                return true;
            } else if (onDemandMapper.canConvert(dataValue.getMappedValue(), columnIndex, metaData, desiredType)) {
                // If mapped value can be converted to the desired type, return
                // true.
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether the data in the given column is of the desired type or
     * can be converted (on demand) to the given type.This method tries the
     * 5-part method of determining whether it can be converted.
     * <p>
     * 1) If the getMappedValue of the data in the current row and given column
     * is of desiredType, return true.<br>
     * 2) If the getOriginalValue of the data in the current row and given
     * column is of desiredType, return true.<br>
     * 3) If onDemandMapper can convert the original value, return true <br>
     * 4) If onDemandMapper can convert the mapped value, return true<br>
     * 5) else return false.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Changed type of "desiredType" parameter from "Class" to "Class<?>".</li>
     * </ol>
     * </p>
     *
     * @param columnName
     *            The name of the column to determine availability about
     * @param desiredType
     *            The desired type to learn about convertibility
     * @return True if the data could be converted, false otherwise
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws IllegalArgumentException
     *             If desiredType is null.
     * @since 1.1
     */
    public boolean isAvailable(String columnName, Class<?> desiredType) throws InvalidCursorStateException {
        return isAvailable(findColumn(columnName), desiredType);
    }

    /**
     * <p>
     * The metadata for the result set. This field is set in the constructor (to
     * a newly created CustomResultSetMetaData), and is immutable and never
     * null. The value of this field can be retrieved through the getMetaData
     * method. The metadata is not actively used by any part of this class. It
     * is only passed to converters or returned through the getMetaData method.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Added the missing return tag.</li>
     * </ol>
     * </p>
     *
     * @return the metadata for the result set
     */
    public CustomResultSetMetaData getMetaData() {
        return metaData;
    }

    /**
     * <p>
     * Add values from ResultSet to rows. This is private function only invoked
     * by constructors.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Renamed parameter "rs" to "resultSet" to meet TopCoder standards.</li>
     * </ol>
     * </p>
     *
     * @param resultSet
     *            The result set to load data from
     * @throws SQLException
     *             When there is an error reading the data
     */
    private void setRowsFromRs(ResultSet resultSet) throws SQLException {
        for (; resultSet.next();) {
            List<RowDataValue> vrow = new ArrayList<RowDataValue>();
            for (int i = 1; i <= metaData.getColumnCount(); ++i) {
                vrow.add(new RowDataValue(resultSet.getObject(i)));
            }
            rows.add(vrow);
        }
    }

    /**
     * <p>
     * Checks whether the {@link #getObject(int, Class)} method's return is
     * valid. If null object is found, NullColumnValueException will be thrown.
     * </p>
     *
     * @param columnIndex
     *            index of the column to get
     * @param desiredType
     *            The type of the desired return value
     * @return The value in the column as the desired type
     * @throws IllegalArgumentException
     *             If columnIndex is invalid(<= 0 or > column count)
     * @throws InvalidCursorStateException
     *             when cursor state is invalid (i.e. currentRow &lt;= 0 or &gt;
     *             rows.size)
     * @throws ClassCastException
     *             when value can not be converted to a float
     * @throws NullColumnValueException
     *             If null object is found.
     * @since 2.0
     */
    private Object getObjectWithChecking(int columnIndex, Class<?> desiredType) throws InvalidCursorStateException,
            NullColumnValueException {
        AbstractionHelper.checkColumnIndex(columnIndex, metaData.getColumnCount());

        Object value = getObject(columnIndex, desiredType);
        if (value == null) {
            throw new NullColumnValueException("The column value is equal to null.");
        }

        return value;
    }

    /**
     * <p>
     * Checks whether the {@link #findColumn(String)} method's return is valid.
     * If the column does not exist, IllegalArgumentException will be thrown.
     * </p>
     *
     * @return the column index
     * @param columnName
     *            the column name to find
     * @throws IllegalArgumentException
     *             If column does not exist
     * @since 2.0
     */
    private int findColumnWithChecking(String columnName) {
        int columnIndex = findColumn(columnName);
        if (columnIndex == 0) {
            throw new IllegalArgumentException("The column value does no exist.");
        }
        return columnIndex;
    }

    /**
     * <p>
     * Simple Comparator.This Comparator holds only one comparator.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameter for the extended type
     * (Comparator&lt;List&lt;RowDataValue&gt;&gt;).</li>
     * <li>Specified generic parameters for all other generic types in the code.
     * </li>
     * <li>Changed visibility to private.</li>
     * </ol>
     * </p>
     * <p>
     * <strong>Thread Safety:</strong> This class is immutable and thread safe
     * when collections passed to it are used by the caller in thread safe
     * manner.
     * </p>
     *
     * @author argolite, aubergineanode, saarixx, WishingBone, justforplay,
     *         suhugo
     * @version 2.0
     * @since 1.0
     */
    private class SimpleComparator implements Comparator<List<RowDataValue>> {

        /**
         * <p>
         * Whether it is descending.
         * </p>
         * <p>
         * <strong>Changes in 2.0:</strong>
         * <ol>
         * <li>Made the field final.</li>
         * </ol>
         * </p>
         */
        private final boolean isDescending;

        /**
         * <p>
         * The column indices to compare.
         * </p>
         * <p>
         * <strong>Changes in 2.0:</strong>
         * <ol>
         * <li>Made the field final.</li>
         * </ol>
         * </p>
         */
        private final int columnIndex;

        /**
         * <p>
         * The comparator to use, null for Comparable.
         * </p>
         * <p>
         * <strong>Changes in 2.0:</strong>
         * <ol>
         * <li>Made the field final.</li>
         * <li>Specified generic parameter for the type.</li>
         * </ol>
         * </p>
         */
        private final Comparator<Object> comparator;

        /**
         * <p>
         * Constructor with column index, comparator and whether descending.
         * </p>
         * <p>
         * <strong>Changes in 2.0:</strong>
         * <ol>
         * <li>Specified generic parameter for comparators type.</li>
         * </ol>
         * </p>
         *
         * @param columnIndex
         *            column index
         * @param comparator
         *            the comparator the use, null for Comparable
         * @param isDescending
         *            whether it is descending
         */
        public SimpleComparator(int columnIndex, Comparator<Object> comparator, boolean isDescending) {
            this.columnIndex = columnIndex;
            this.comparator = comparator;
            this.isDescending = isDescending;
        }

        /**
         * <p>
         * Compare method.Compare specified two objects using stored comparator.
         * </p>
         * <p>
         * <strong>Changes in 2.0:</strong>
         * <ol>
         * <li>Renamed o1 and o2 to rows1 and rows2 respectively.</li>
         * <li>Updated parameter types from Object to List&lt;RowDataValue&gt;.</li>
         * </ol>
         * </p>
         *
         * @return a positive value when row1 is earlier then row2, a negative
         *         value when row1 is later than row2, or 0 if they equals each
         *         other
         * @param row1
         *            the first row
         * @param row2
         *            the second row
         * @throws ClassCastException
         *             if row1 is not null and is not Comparable.
         */
        public int compare(List<RowDataValue> row1, List<RowDataValue> row2) {
            if (isDescending) {
                List<RowDataValue> tmp = row1;
                row1 = row2;
                row2 = tmp;
            }
            if (columnIndex > 0 && columnIndex <= metaData.getColumnCount()) {
                Object value1 = row1.get(columnIndex - 1).getMappedValue();
                Object value2 = row2.get(columnIndex - 1).getMappedValue();
                if (value1 != null && value2 != null) {
                    if (comparator != null) {
                        return comparator.compare(value1, value2);
                    } else if (value1 instanceof Comparable<?>) {
                        return ((Comparable<Object>) value1).compareTo(value2);
                    } else if (value1 != null) {
                        throw new ClassCastException();
                    }
                }
            }
            return 0;
        }
    }

    /**
     * <p>
     * Array Comparator.This Comparator holds many comparators stored in array,
     * when comparing objects, the first(i.e, index in array is zero) comparator
     * is used first, the second comparator is used when unable to determine the
     * order only using the first comparator, ..... and so on.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameter for the extended type
     * (Comparator&lt;List&lt;RowDataValue&gt;&gt;).</li>
     * <li>Specified generic parameters for all other generic types in the code.
     * </li>
     * <li>Changed visibility to private.</li>
     * </ol>
     * </p>
     * <p>
     * <strong>Thread Safety:</strong> This class is immutable and thread safe
     * when collections passed to it are used by the caller in thread safe
     * manner.
     * </p>
     *
     * @author argolite, aubergineanode, saarixx, WishingBone, justforplay,
     *         suhugo
     * @version 2.0
     * @since 1.0
     */
    private class ArrayComparator implements Comparator<List<RowDataValue>> {

        /**
         * <p>
         * Whether it is descending.
         * </p>
         * <p>
         * <strong>Changes in 2.0:</strong>
         * <ol>
         * <li>Made the field final.</li>
         * </ol>
         * </p>
         */
        private final boolean isDescending;

        /**
         * <p>
         * The column indices to compare.
         * </p>
         * <p>
         * <strong>Changes in 2.0:</strong>
         * <ol>
         * <li>Made the field final.</li>
         * </ol>
         * </p>
         */
        private final int[] columnIndices;

        /**
         * <p>
         * The comparators to use.
         * </p>
         * <p>
         * <strong>Changes in 2.0:</strong>
         * <ol>
         * <li>Made the field final.</li>
         * <li>Specified generic parameter for the type.</li>
         * </ol>
         * </p>
         */
        private final Comparator<Object>[] comparators;

        /**
         * <p>
         * Constructor with column indices, comparators and whether descending.
         * </p>
         * <p>
         * <strong>Changes in 2.0:</strong>
         * <ol>
         * <li>Specified generic parameter for comparators type.</li>
         * </ol>
         * </p>
         *
         * @param columnIndices
         *            column indices
         * @param comparators
         *            the comparators the use, null for Comparable
         * @param isDescending
         *            whether it is descending
         */
        public ArrayComparator(int[] columnIndices, Comparator<Object>[] comparators, boolean isDescending) {
            this.columnIndices = columnIndices;
            this.comparators = comparators;
            this.isDescending = isDescending;
        }

        /**
         * <p>
         * Compare method. Compare values using comparator in comparators[]
         * orderly.
         * </p>
         * <p>
         * <strong>Changes in 2.0:</strong>
         * <ol>
         * <li>Renamed o1 and o2 to rows1 and rows2 respectively.</li>
         * <li>Updated parameter types from Object to List&lt;RowDataValue&gt;.</li>
         * </ol>
         * </p>
         *
         * @return a positive value when row1 is earlier then row2, a negative
         *         value when row1 is later than row2, or 0 if they equals each
         *         other
         * @param row1
         *            the first row
         * @param row2
         *            the second row
         * @throws ClassCastException
         *             if row1 is not null and is not Comparable.
         * @throws IndexOutOfBoundsException
         *             if comparators is a shorter array than column length.
         */
        public int compare(List<RowDataValue> row1, List<RowDataValue> row2) {
            if (isDescending) {
                List<RowDataValue> tmp = row1;
                row1 = row2;
                row2 = tmp;
            }
            for (int i = 0; i < columnIndices.length; ++i) {
                int columnIndex = columnIndices[i];
                Comparator<Object> comparator = null;
                if (comparators != null) {
                    comparator = comparators[i];
                }
                if (columnIndex > 0 && columnIndex <= metaData.getColumnCount()) {
                    Object value1 = row1.get(columnIndex - 1).getMappedValue();
                    Object value2 = row2.get(columnIndex - 1).getMappedValue();
                    if (value1 != null && value2 != null) {
                        if (comparator != null) {
                            int ret = comparator.compare(value1, value2);
                            if (ret != 0) {
                                return ret;
                            }
                        } else if (value1 instanceof Comparable<?>) {
                            int ret = ((Comparable<Object>) value1).compareTo(value2);
                            if (ret != 0) {
                                return ret;
                            }
                        } else if (value1 != null) {
                            throw new ClassCastException();
                        }
                    }
                }
            }
            return 0;
        }
    }
}
