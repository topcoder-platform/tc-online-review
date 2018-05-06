/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

/**
 * <p>
 * The RowDataValue class is used to store the two data values associated with a
 * row and column - the original data value and the currently mapped value. This
 * class is simply a container for these two variables and contains only simple
 * getters and setters.
 * </p>
 * <p>
 * Thread Safety: - This class is mutable, and not thread-safe.
 * </p>
 *
 * @author aubergineanode, justforplay
 * @version 1.1
 * @since 1.1
 */
class RowDataValue {

    /**
     * <p>
     * The current explicitly mapped value for the data. This field is
     * initialized in the constructor to the same value as originalValue, but
     * can be changed by the CustomResultSet when a remap operation is applied.
     * This value can be null. This value can be accessed through the
     * get/setMappedValue methods.
     * </p>
     */
    private Object mappedValue;

    /**
     * <p>
     * The original value for the data entry. This value comes directly from the
     * JDBC result set and does not change (i.e. this field is immutable). This
     * field can be null and the value can be retrieved from the
     * getOriginalValue method.
     * </p>
     */
    private final Object originalValue;

    /**
     * <p>
     * Creates a new RowDataValue. Both the originalValue field and the
     * mappedValue field are set to the given value.
     * </p>
     *
     * @param originalValue
     *            The value from the JDBC result set (may be null)
     */
    RowDataValue(Object originalValue) {
        this.originalValue = originalValue;
        mappedValue = originalValue;
    }

    /**
     * <p>
     * Sets the explicitly mapped data that is stored in this class.
     * </p>
     *
     * @param value
     *            The new value for the explicitly mapped data (may be null).
     */
    void setMappedValue(Object value) {
        mappedValue = value;
    }

    /**
     * <p>
     * Gets the currently explicitly mapped value for the data in the row.
     * </p>
     *
     * @return The current explicitly mapped value for the data
     */
    Object getMappedValue() {
        return mappedValue;
    }

    /**
     * <p>
     * Gets the original JDBC object value for the row data.
     * </p>
     *
     * @return The original JDBC returned object value
     */
    Object getOriginalValue() {
        return originalValue;
    }
}
