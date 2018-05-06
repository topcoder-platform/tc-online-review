/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.ondemandconversion;

import java.math.BigDecimal;

import com.topcoder.util.sql.databaseabstraction.AbstractionHelper;
import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;
import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.OnDemandConverter;

/**
 * <p>
 * The IntConverter class handles on demand conversions from the Integer type to
 * various other data types. It implements the OnDemandConverter interface and
 * has no state.
 * </p>
 * <p>
 * When a value is converted by IntConverter, the condition that value can be
 * converted is: value is a Integer and not null, false, and desiredType is
 * Byte, Short, BigDecimal, Long, Float, Double, or String.
 * </p>
 * <p>
 * <strong>Changes in 2.0:</strong>
 * <ol>
 * <li>Specified generic parameters for all generic types in the code.</li>
 * </ol>
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> This class is immutable, and therefore
 * thread-safe.
 * </p>
 *
 * @author aubergineanode, saarixx, justforplay, suhugo
 * @version 2.0
 * @since 1.1
 */
public class IntConverter implements OnDemandConverter {

    /**
     * Create a new IntConverter.
     */
    public IntConverter() {
        // do nothing
    }

    /**
     * <p>
     * Tells whether value can be converted to desiredType. The condition that
     * value can be converted is: value is a Integer and not null, false, and
     * desiredType is Byte, Short, BigDecimal, Long, Float, Double, or String.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Changed type of desiredType parameter from Class to Class<?>.</li>
     * </ol>
     * </p>
     *
     * @param value
     *            The value to determine if conversion is possible (may be null)
     * @param column
     *            The column the value came from
     * @param metaData
     *            The metaData for the result set (can be used to obtain info
     *            about the column)
     * @param desiredType
     *            The desired return type for the conversion
     * @return True if this converter can do the conversion, false otherwise
     * @throws IllegalArgumentException
     *             If metaData or desiredType is null or column &lt;= 0 or &gt;
     *             column count of metadata.
     */
    public boolean canConvert(Object value, int column, CustomResultSetMetaData metaData, Class<?> desiredType) {
        AbstractionHelper.checkNull(metaData, "metaData");
        AbstractionHelper.checkNull(desiredType, "desiredType");
        AbstractionHelper.checkColumnIndex(column, metaData.getColumnCount());
        return AbstractionHelper.canConvert(value, Integer.class, desiredType, new Class<?>[] { Byte.class,
            Short.class, BigDecimal.class, Long.class, Float.class, Double.class, String.class });
    }

    /**
     * <p>
     * Converts value into an instance of desiredType.If value is a Integer and
     * not null, false, and desiredType is Byte, Short, BigDecimal, Long, Float,
     * Double, or String, value is converted to the corresponding type and
     * returned.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Changed type of desiredType parameter from Class to Class<?>.</li>
     * </ol>
     * </p>
     *
     * @param value
     *            The object we want to convert (may be null)
     * @param column
     *            The index of the column that the value is in the
     *            CustomResultSet. This can be used to retrieve metadata about
     *            the column
     * @param metaData
     *            The metadata for the result set the object comes from
     * @param desiredType
     *            The type that we want to convert the object to
     * @return value converted into an instance of desiredType (must be
     *         non-null)
     * @throws IllegalArgumentException
     *             If metaData or desiredType is null or column is &lt;= 0 or
     *             &gt; column count of metadata
     * @throws IllegalMappingException
     *             If the conversion can not be made
     */
    public Object convert(Object value, int column, CustomResultSetMetaData metaData, Class<?> desiredType)
        throws IllegalMappingException {
        // Validation of metaData, desiredType and column is done at
        // canConvert().
        if (!canConvert(value, column, metaData, desiredType)) {
            throw new IllegalMappingException("value can't be converted to the desired type -- " + desiredType
                    + " -- by IntConverter.");
        }
        Integer intValue = (Integer) value;
        if (desiredType.isAssignableFrom(Byte.class)) {
            return new Byte(intValue.byteValue());
        } else if (desiredType.isAssignableFrom(Short.class)) {
            return new Short(intValue.shortValue());
        } else if (desiredType.isAssignableFrom(Long.class)) {
            return new Long(intValue.longValue());
        } else if (desiredType.isAssignableFrom(Float.class)) {
            return new Float(intValue.floatValue());
        } else if (desiredType.isAssignableFrom(Double.class)) {
            return new Double(intValue.doubleValue());
        } else if (desiredType.isAssignableFrom(BigDecimal.class)) {
            return new BigDecimal(intValue.intValue());
        } else { // String
            return value.toString();
        }
    }
}
