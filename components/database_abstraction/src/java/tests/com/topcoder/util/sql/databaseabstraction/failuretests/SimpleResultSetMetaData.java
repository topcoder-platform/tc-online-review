/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.failuretests;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A ResultSetMetaData implementation for testing purposes.  Not robust nor
 * generally useful outside its intended domain.
 * </p>
 *
 * @author ThinMan
 * @author biotrail
 * @version 1.1
 * @since 1.0
 */
public class SimpleResultSetMetaData implements ResultSetMetaData {
    /**
     * <p>
     * Stores the column meta data.
     * The element in columnData is of SimpleColumnData type.
     * </p>
     *
     * @since 1.0
     */
    private List<SimpleColumnData> columnData = new ArrayList<SimpleColumnData>();

    /**
     * <p>
     * Add a column meta data.
     * </p>
     *
     * @param name name
     * @param type type
     * @param typeName typeName
     * @param className className
     * @param scale scale
     * @param precision precision
     */
    void addColumn(String name, int type, String typeName, String className, int scale, int precision) {
        columnData.add(new SimpleColumnData(name, type, typeName, className, scale, precision));
    }

    /**
     * <p>
     * Returns the column count.
     * </p>
     *
     * @return column count.
     *
     * @since 1.0
     */
    public int getColumnCount() {
        return columnData.size();
    }

    /**
     * <p>
     * Implements the isAutoIncrement() method.
     * </p>
     *
     * @param column column
     * @return a boolean value
     */
    public boolean isAutoIncrement(int column) {
        return false;
    }

    /**
     * <p>
     * Implements the isCaseSensitive() method.
     * </p>
     *
     * @param column column
     * @return a boolean value
     */
    public boolean isCaseSensitive(int column) {
        return true;
    }

    /**
     * <p>
     * Implements the isCaseSensitive() method.
     * </p>
     *
     * @param column column
     * @return a boolean value
     */
    public boolean isSearchable(int column) {
        return true;
    }

    /**
     * <p>
     * Implements the isCurrency() method.
     * </p>
     *
     * @param column column
     * @return a boolean value
     */
    public boolean isCurrency(int column) {
        return false;
    }

    /**
     * <p>
     * Implements the isNullable() method.
     * </p>
     *
     * @param column column
     * @return a boolean value
     */
    public int isNullable(int column) {
        return ResultSetMetaData.columnNullableUnknown;
    }

    /**
     * <p>
     * Implements the isSigned() method.
     * </p>
     *
     * @param column column
     * @return a boolean value
     */
    public boolean isSigned(int column) {
        return false;
    }

    /**
     * <p>
     * Implements the getColumnDisplaySize() method.
     * </p>
     *
     * @param column column
     * @return 10
     */
    public int getColumnDisplaySize(int column) {
        return 10;
    }

    /**
     * <p>
     * Implements the getColumnLabel() method.
     * </p>
     *
     * @param column column
     * @return column label
     */
    public String getColumnLabel(int column) {
        return getColumnName(column);
    }

    /**
     * <p>
     * Implements the getColumnName() method.
     * </p>
     *
     * @param column column
     * @return column name
     */
    public String getColumnName(int column) {
        return ((SimpleColumnData) columnData.get(column - 1)).name;
    }

    /**
     * <p>
     * Implements the getSchemaName() method.
     * </p>
     *
     * @param column column
     * @return an empty string
     */
    public String getSchemaName(int column) {
        return "";
    }

    /**
     * <p>
     * Implements the getPrecision() method.
     * </p>
     *
     * @param column column
     * @return precision value
     */
    public int getPrecision(int column) {
        return ((SimpleColumnData) columnData.get(column - 1)).precision;
    }

    /**
     * <p>
     * Implements the getScale() method.
     * </p>
     *
     * @param column column
     * @return scale value
     */
    public int getScale(int column) {
        return ((SimpleColumnData) columnData.get(column - 1)).scale;
    }

    /**
     * <p>
     * Implements the getTableName() method.
     * </p>
     *
     * @param column column
     * @return an empty string
     */
    public String getTableName(int column) {
        return "";
    }

    /**
     * <p>
     * Implements the getCatalogName() method.
     * </p>
     *
     * @param column column
     * @return an empty string
     */
    public String getCatalogName(int column) {
        return "";
    }

    /**
     * <p>
     * Implements the getColumnType() method.
     * </p>
     *
     * @param column column
     * @return type value
     */
    public int getColumnType(int column) {
        return ((SimpleColumnData) columnData.get(column - 1)).type;
    }

    /**
     * <p>
     * Implements the getColumnTypeName() method.
     * </p>
     *
     * @param column column
     * @return column type name
     */
    public String getColumnTypeName(int column) {
        return ((SimpleColumnData) columnData.get(column - 1)).typeName;
    }

    /**
     * <p>
     * Implements the isReadOnly() method.
     * </p>
     *
     * @param column column
     * @return a boolean value
     */
    public boolean isReadOnly(int column) {
        return true;
    }

    /**
     * <p>
     * Implements the isWritable() method.
     * </p>
     *
     * @param column column
     * @return a boolean value
     */
    public boolean isWritable(int column) {
        return false;
    }

    /**
     * <p>
     * Implements the isDefinitelyWritable() method.
     * </p>
     *
     * @param column column
     * @return a boolean value
     */
    public boolean isDefinitelyWritable(int column) {
        return false;
    }

    /**
     * <p>
     * Implements the getColumnClassName() method.
     * </p>
     *
     * @param column column
     * @return column calss name value
     */
    public String getColumnClassName(int column) {
        return ((SimpleColumnData) columnData.get(column - 1)).className;
    }

	public Object unwrap(java.lang.Class iface) throws java.sql.SQLException { return null; }

    public boolean isWrapperFor(java.lang.Class iface) throws java.sql.SQLException { return false; }    
}

/**
 * <p>
 * A data class for represents a column in database.
 * </p>
 *
 *
 * @author ThinMan
 * @author biotrail
 * @version 1.1
 * @since 1.0
 */
class SimpleColumnData {
    /**
     * <p>
     * Column name.
     * </p>
     *
     * @since 1.0
     */
    String name;

    /**
     * Column type.
     *
     * @since 1.0
     */
    int type;

    /**
     * <p>
     * Column type name.
     * </p>
     *
     * @since 1.0
     */
    String typeName;

    /**
     * <p>
     * The class name.
     * </p>
     *
     * @since 1.0
     */
    String className;

    /**
     * <p>
     * Scale.
     * </p>
     *
     * @since 1.0
     */
    int scale;

    /**
     * <p>
     * Precision.
     * </p>
     *
     * @since 1.0
     */
    int precision;

    /**
     * <p>
     * Constructs a new SimpleColumnData instance.
     * </p>
     *
     * @param name name
     * @param type type
     * @param typeName typeName
     * @param className className
     * @param scale scale
     * @param precision precision
     *
     * @since 1.0
     */
    SimpleColumnData(String name, int type, String typeName, String className, int scale, int precision) {
        this.name = name;
        this.type = type;
        this.typeName = typeName;
        this.className = className;
        this.scale = scale;
        this.precision = precision;
    }

}
