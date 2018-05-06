/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

/**
 * <p>
 * The Column class is used by the CustomResultSetMetaData class to store
 * information about each column in a CustomResultSet. This class is a simple
 * data store for the several related values about a column, and no complicated
 * logic is needed. All the methods are simple getters/setters.
 * </p>
 * <p>
 * Thread Safety: - This class is mutable, and not thread-safe.
 * </p>
 *
 * @author argolite, aubergineanode, WishingBone, justforplay
 * @version 1.1
 * @since 1.0
 */
public class Column {

    /**
     * <p>
     * The name of the java class that the data in the column is available as.
     * This field is set in the constructor and can also be set through the
     * setColumnClassName method.
     * </p>
     * <p>
     * Although it would make sense for this field to never be null, in order to
     * be consistent with the previous version, this is not enforced.
     * </p>
     */
    private String columnClassName;

    /**
     * <p>
     * The display size of the column (i.e. the maximum number of characters
     * that can be taken up by the string form of a value in the column). This
     * field is set in the constructor and is immutable. The value can be
     * retrieved through the getColumnDisplaySize method.
     * </p>
     * <p>
     * Although it would make sense to have restrictions on the content of this
     * field, in order to be consistent with the previous version, this is not
     * enforced.
     * </p>
     */
    private final int columnDisplaySize;

    /**
     * <p>
     * The label of the column. This field is set in the constructor and
     * mutable. The value can be retrieved through the getColumnLabel method and
     * set through the setColumnLabel method.
     * </p>
     * <p>
     * Although it would make sense for this field to never be null, in order to
     * be consistent with the previous version, this is not enforced.
     * </p>
     */
    private String columnLabel;

    /**
     * <p>
     * The name of the column. This field is set in the constructor and is
     * immutable. The value can be retrieved through the getColumnName method.
     * </p>
     * <p>
     * Although it would make sense for this field to never be null, in order to
     * be consistent with the previous version, this is not enforced.
     * </p>
     */
    private final String columnName;

    /**
     * <p>
     * The java.sql.Types type of the column. This field is set in the
     * constructor and is immutable. Its value can be retrieved through
     * getColumnType method.
     * </p>
     * <p>
     * Although it would make sense to have restrictions on the content of this
     * field, in order to be consistent with the previous version, this is not
     * enforced.
     * </p>
     */
    private final int columnType;

    /**
     * <p>
     * The database name of the type of data in the column. This field is set in
     * the constructor and is immutable. The value can be retrieved through the
     * getColumnTypeName method.
     * </p>
     * <p>
     * Although it would make sense for this field to never be null, in order to
     * be consistent with the previous version, this is not enforced.
     * </p>
     */
    private final String columnTypeName;

    /**
     * <p>
     * The precision (if a numeric type) of the column. If the column is not
     * numeric, this field should be 0. This field is set in the constructor and
     * is immutable. Its value can be retrieved through getColumnPrecision
     * method.
     * </p>
     * <p>
     * Although it would make sense to have restrictions on the content of this
     * field, in order to be consistent with the previous version, this is not
     * enforced.
     * </p>
     */
    private final int columnPrecision;

    /**
     * <p>
     * The scale (if a numeric type) of the column, or 0 if the column is not
     * numeric. This field is set in the constructor and is immutable. Its value
     * can be retrieved through getColumnScale method.
     * </p>
     * <p>
     * Although it would make sense to have restrictions on the content of this
     * field, in order to be consistent with the previous version, this is not
     * enforced.
     * </p>
     */
    private final int columnScale;

    /**
     * <p>
     * Whether the column (if a numeric type) is auto-incrementing or not. This
     * field is set in the constructor and is immutable. Its value can be
     * retrieved through isAutoIncrement method.
     * </p>
     */
    private final boolean autoIncrement;

    /**
     * Tells whether the column contains currency values. This field is set in
     * the constructor and is immutable. Its value can be retrieved through
     * isCurrency method method.
     */
    private final boolean currency;

    /**
     * <p>
     * Whether the column (if a numeric type) allows signed values or not. This
     * field is set in the constructor and is immutable. Its value can be
     * retrieved through isSigned method method.
     * </p>
     */
    private final boolean signed;

    /**
     * <p>
     * Creates a new Column instance. Simply set the fields to the given values.
     * To be consistent with the existing version 1.0 component, any argument
     * can be null / negative / other values that would typically be invalid
     * under the current design standards for TopCoder.
     * </p>
     * <p>
     * In order to be consistent with the previous version, column name is not
     * initialized(null).
     * </p>
     *
     * @param className
     *            the class name of the column
     * @param displaySize
     *            display size of the column
     * @param label
     *            label of the column
     * @param type
     *            type of the column
     * @param typeName
     *            type name of the column
     * @param precision
     *            precision of the column
     * @param scale
     *            scale of the column
     * @param isAutoIncrement
     *            whether the column is auto-increment
     * @param isCurrency
     *            whether the column is currency
     * @param isSigned
     *            whether the column is signed
     */
    public Column(String className, int displaySize, String label, int type, String typeName, int precision, int scale,
            boolean isAutoIncrement, boolean isCurrency, boolean isSigned) {
        columnClassName = className;
        columnDisplaySize = displaySize;
        columnLabel = label;
        columnType = type;
        columnTypeName = typeName;
        columnPrecision = precision;
        columnScale = scale;
        autoIncrement = isAutoIncrement;
        currency = isCurrency;
        signed = isSigned;
        columnName = null;
    }

    /**
     * <p>
     * Creates a new Column instance. Simply set the fields to the given values.
     * To be consistent with the existing version 1.0 component, any argument
     * can be null / negative / other values that would typically be invalid
     * under the current design standards for TopCoder.
     * </p>
     *
     * @param className
     *            the class name of the column
     * @param displaySize
     *            display size of the column
     * @param label
     *            label of the column
     * @param type
     *            type of the column
     * @param typeName
     *            type name of the column
     * @param precision
     *            precision of the column
     * @param scale
     *            scale of the column
     * @param isAutoIncrement
     *            whether the column is auto-increment
     * @param isCurrency
     *            whether the column is currency
     * @param isSigned
     *            whether the column is signed
     * @param columnName
     *            name of the column
     */
    public Column(String className, int displaySize, String label, int type, String typeName, int precision, int scale,
            boolean isAutoIncrement, boolean isCurrency, boolean isSigned, String columnName) {
        columnClassName = className;
        columnDisplaySize = displaySize;
        columnLabel = label;
        columnType = type;
        columnTypeName = typeName;
        columnPrecision = precision;
        columnScale = scale;
        autoIncrement = isAutoIncrement;
        currency = isCurrency;
        signed = isSigned;
        this.columnName = columnName;
    }

    /**
     * <p>
     * Gets the java class name that the data in the column is available as.
     * </p>
     *
     * @return class name of the column(can be null)
     */
    public String getColumnClassName() {
        return columnClassName;
    }

    /**
     * <p>
     * Sets the java class name that the data in the column is available as.
     * </p>
     *
     * @param className
     *            class name of the column - can be null or empty string
     */
    public void setColumnClassName(String className) {
        columnClassName = className;
    }

    /**
     * <p>
     * Get display size of the column - i.e. the maximum width of the string
     * value of items in the column.
     * </p>
     * <p>
     * The return from this method can be any value (negative included).
     * </p>
     *
     * @return display size of the column
     */
    public int getColumnDisplaySize() {
        return columnDisplaySize;
    }

    /**
     * <p>
     * Gets the label associated with the column.
     * </p>
     * <p>
     * The return from this method can be null.
     * </p>
     *
     * @return label of the column
     */
    public String getColumnLabel() {
        return columnLabel;
    }

    /**
     * <p>
     * Sets the label associated with the column.
     * </p>
     *
     * @param label
     *            label of the column, may be null or empty string
     */
    public void setColumnLabel(String label) {
        columnLabel = label;
    }

    /**
     * <p>
     * Gets the java.sql.Types type of the column.
     * </p>
     * <p>
     * This method can return any value, including negative values.
     * </p>
     *
     * @return type of the column
     */
    public int getColumnType() {
        return columnType;
    }

    /**
     * <p>
     * Gets database name of the type of data in the column.
     * </p>
     * <p>
     * This method can return any value.
     * </p>
     *
     * @return database name of the type of data in the column, may be null or
     *         empty.
     */
    public String getColumnTypeName() {
        return columnTypeName;
    }

    /**
     * <p>
     * Gets the precision of the column (the number of decimal digits it can
     * hold if the column holds numeric values.)
     * </p>
     * <p>
     * This method can return any value, including negative values.
     * </p>
     *
     * @return scale of the column, 0 if the column is not numeric
     */
    public int getColumnPrecision() {
        return columnPrecision;
    }

    /**
     * <p>
     * Gets the scale of the column (the number of decimal digits it can hold to
     * the right of the decimal point, if the column holds numeric values.)
     * </p>
     * <p>
     * This method can return any value, including negative values.
     * </p>
     *
     * @return scale of the column, 0 if the column is not numeric
     */
    public int getColumnScale() {
        return columnScale;
    }

    /**
     * <p>
     * Tells whether the column is auto-incrementing.
     * </p>
     *
     * @return True if the column is auto-increment, false otherwise
     */
    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    /**
     * <p>
     * Tells whether the column is holds currency values.
     * </p>
     *
     * @return True if the column is of a currency type, false otherwise
     */
    public boolean isCurrency() {
        return currency;
    }

    /**
     * <p>
     * Determines whether the column (if numeric) supports signed values.
     * </p>
     *
     * @return True if the column is a numeric type and support signed values.
     */
    public boolean isSigned() {
        return signed;
    }

    /**
     * <p>
     * Gets the name associated with the column.
     * </p>
     * <p>
     * The return from this method can be null.
     * </p>
     *
     * @return label of the column
     */
    public String getColumnName() {
        return columnName;
    }
}
