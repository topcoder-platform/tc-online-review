/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * The CustomResultSetMetaData class is the counterpart of the JDBC
 * ResultSetMetaData interface. It stores information about the columns in the
 * CustomResultSet and exposes them through an interface very similar to the
 * ResultSetMetaData interface. In addition to imitating the ResultSetMetaData
 * interface, this class allows some pieces of data to for each column to be set
 * in addition to being retrieved.
 * </p>
 * <p>
 * <strong>Changes in 2.0:</strong>
 * <ol>
 * <li>Specified generic parameters for generic types.</li>
 * </ol>
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> This class is mutable since it holds a
 * collection of mutable Column instances, and thus it is not thread-safe.
 * </p>
 *
 * @author argolite, aubergineanode, saarixx, WishingBone, justforplay,
 *         suhugo
 * @version 2.0
 * @since 1.0
 */
public class CustomResultSetMetaData {

    /**
     * <p>
     * The collection of metadata information about each column in the result
     * set. All items in the list will be non-null and of the Column type.
     * </p>
     * <p>
     * Version 1.1. This field has been changed to a List type to better reflect
     * its semantics and the actual version 1.0 code (where it is always an
     * ArrayList).
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameter for the collection type.</li>
     * </ol>
     * </p>
     */
    private final List<Column> columns;

    /**
     * <p>
     * Constructor with ResultSetMetaData to convert from. In version 1.1,
     * column name is initialized.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Renamed parameter rsmd to resultSetMetaData to meet TopCoder
     * standards.</li>
     * </ol>
     * </p>
     *
     * @param resultSetMetaData
     *            a result set meta data
     * @throws SQLException
     *             when SQL exception takes place when accessing
     *             resultSetMetaData
     */
    public CustomResultSetMetaData(ResultSetMetaData resultSetMetaData) throws SQLException {
        columns = new ArrayList<Column>();
        if (resultSetMetaData != null) {
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); ++i) {
                columns.add(new Column(
                        resultSetMetaData.getColumnClassName(i),
                        resultSetMetaData.getColumnDisplaySize(i),
                        resultSetMetaData.getColumnLabel(i),
                        resultSetMetaData.getColumnType(i),
                        resultSetMetaData.getColumnTypeName(i),
                        resultSetMetaData.getPrecision(i),
                        resultSetMetaData.getScale(i),
                        resultSetMetaData.isAutoIncrement(i),
                        resultSetMetaData.isCurrency(i),
                        resultSetMetaData.isSigned(i),
                        resultSetMetaData.getColumnName(i)));
            }
        }
    }

    /**
     * <p>
     * Gets the java class name of the type of items that are in the columnIndex
     * column of the result set (this is the type originally returned by the
     * JDBC implementation or that is set by the setColumnClassName method).
     * </p>
     *
     * @return The java name of the object class for the column, or null if
     *         column &lt;= 0 or &gt; column count
     * @param columnIndex
     *            The index of the column to retrieve information about.
     */
    public String getColumnClassName(int columnIndex) {
        if (columnIndex > 0 && columnIndex <= columns.size()) {
            return columns.get(columnIndex - 1).getColumnClassName();
        } else {
            return null;
        }
    }

    /**
     * <p>
     * Gets the number of columns in the result set.
     * </p>
     *
     * @return count number of columns
     */
    public int getColumnCount() {
        return columns.size();
    }

    /**
     * <p>
     * Get the display size of the column - the maximum width it can occupy when
     * written as a character string.
     * </p>
     *
     * @return display size of the column, return 0 when the index is invalid
     *         (columnIndex &lt;= 0 or &gt; column count)
     * @param columnIndex
     *            index of the column
     */
    public int getColumnDisplaySize(int columnIndex) {
        if (columnIndex > 0 && columnIndex <= columns.size()) {
            return columns.get(columnIndex - 1).getColumnDisplaySize();
        } else {
            return 0;
        }
    }

    /**
     * <p>
     * Gets the label for the given column.
     * </p>
     *
     * @return label of the column, return null when the index is invalid
     *         (columnIndex &lt;= 0 or &gt; column count)
     * @param columnIndex
     *            index of the column
     */
    public String getColumnLabel(int columnIndex) {
        if (columnIndex > 0 && columnIndex <= columns.size()) {
            return columns.get(columnIndex - 1).getColumnLabel();
        } else {
            return null;
        }
    }

    /**
     * <p>
     * Gets the name of the given column.
     * </p>
     *
     * @return name of the column, return null when the index is invalid
     *         (columnIndex &lt;= 0 or &gt; column count)
     * @param columnIndex
     *            index of the column
     */
    public String getColumnName(int columnIndex) {
        if (columnIndex > 0 && columnIndex <= columns.size()) {
            return columns.get(columnIndex - 1).getColumnName();
        } else {
            return null;
        }
    }

    /**
     * <p>
     * Get type (java.sql.Types value) for the given column.
     * </p>
     *
     * @return type of the column, return 0 when the index is invalid
     *         (columnIndex &lt;= 0 or &gt; column count)
     * @param columnIndex
     *            index of the column
     */
    public int getColumnType(int columnIndex) {
        if (columnIndex > 0 && columnIndex <= columns.size()) {
            return columns.get(columnIndex - 1).getColumnType();
        } else {
            return 0;
        }
    }

    /**
     * <p>
     * Get the type name of the given column. This is the database type of the
     * column, for example VARCHAR, NUMERIC, etc.
     * </p>
     *
     * @return type name of the column, return null when the index is invalid
     *         (columnIndex &lt;= 0 or &gt; column count)
     * @param columnIndex
     *            index of the column
     */
    public String getColumnTypeName(int columnIndex) {
        if (columnIndex > 0 && columnIndex <= columns.size()) {
            return columns.get(columnIndex - 1).getColumnTypeName();
        } else {
            return null;
        }
    }

    /**
     * <p>
     * Gets the precision of the column (the number of decimal digits it can
     * hold if the column holds numeric values).
     * </p>
     *
     * @return precision of the column, return 0 when the index is invalid
     *         (columnIndex &lt;= 0 or &gt; column count)
     * @param columnIndex
     *            index of the column
     */
    public int getColumnPrecision(int columnIndex) {
        if (columnIndex > 0 && columnIndex <= columns.size()) {
            return columns.get(columnIndex - 1).getColumnPrecision();
        } else {
            return 0;
        }
    }

    /**
     * <p>
     * Gets the scale of the column (the number of decimal digits it can hold to
     * the right of the decimal point, if the column holds numeric values).
     * </p>
     *
     * @return scale of the column, return 0 when the index is invalid
     *         (columnIndex &lt;= 0 or &gt; column count)
     * @param columnIndex
     *            index of the column
     */
    public int getColumnScale(int columnIndex) {
        if (columnIndex > 0 && columnIndex <= columns.size()) {
            return columns.get(columnIndex - 1).getColumnScale();
        } else {
            return 0;
        }
    }

    /**
     * <p>
     * Tells whether the column is auto-incrementing.
     * </p>
     *
     * @return whether the column is auto-increment, return false when the index
     *         is invalid (columnIndex &lt;= 0 or &gt; column count)
     * @param columnIndex
     *            index of the column
     */
    public boolean isAutoIncrement(int columnIndex) {
        if (columnIndex > 0 && columnIndex <= columns.size()) {
            return columns.get(columnIndex - 1).isAutoIncrement();
        } else {
            return false;
        }
    }

    /**
     * <p>
     * Tells whether the column holds currency values.
     * </p>
     *
     * @return whether the column is currency, return false when the index is
     *         invalid (columnIndex &lt;= 0 or &gt; column count)
     * @param columnIndex
     *            index of the column
     */
    public boolean isCurrency(int columnIndex) {
        if (columnIndex > 0 && columnIndex <= columns.size()) {
            return columns.get(columnIndex - 1).isCurrency();
        } else {
            return false;
        }
    }

    /**
     * <p>
     * Determines whether the column (if numeric) supports signed values. Call
     * isSigned on the columnIndexth item in the columns list.
     * </p>
     *
     * @return whether the column is signed, return false when the index is
     *         invalid (columnIndex &lt;= 0 or &gt; column count)
     * @param columnIndex
     *            index of the column
     */
    public boolean isSigned(int columnIndex) {
        if (columnIndex > 0 && columnIndex <= columns.size()) {
            return columns.get(columnIndex - 1).isSigned();
        } else {
            return false;
        }
    }

    /**
     * <p>
     * Set the java class name associated with items in the column. Call
     * setColumnClassName name on the columnIndexth item in the columns list. If
     * columnIndex &lt;= 0 or &gt; column count, do nothing.
     * </p>
     *
     * @param columnIndex
     *            index of the column
     * @param className
     *            class name of the column, may be null or empty string
     */
    public void setColumnClassName(int columnIndex, String className) {
        if (columnIndex > 0 && columnIndex <= columns.size()) {
            columns.get(columnIndex - 1).setColumnClassName(className);
        }
    }

    /**
     * <p>
     * Set the label associated with a column. Call setColumnLabel on the
     * columnIndexth item in the columns list. If columnIndex &lt;= 0 or &gt;
     * column count, do nothing.
     * </p>
     *
     * @param columnIndex
     *            index of the column
     * @param label
     *            label of the column, may be null or empty string
     */
    public void setColumnLabel(int columnIndex, String label) {
        if (columnIndex > 0 && columnIndex <= columns.size()) {
            columns.get(columnIndex - 1).setColumnLabel(label);
        }
    }
}
