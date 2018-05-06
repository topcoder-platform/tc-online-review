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
 * The ShortConverter class handles on demand conversions from the Short type to
 * various other data types. It implements the OnDemandConverter interface and
 * has no state.
 * </p>
 * <p>
 * When a value is converted by ShortConverter, the condition that value can be
 * converted is: value is a Short and not null, and desiredType is Byte,
 * BigDecimal, Integer, Long, Float, Double, or String.
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
public class ShortConverter implements OnDemandConverter {

    /**
     * Create a new ShortConverter.
     */
    public ShortConverter() {
        // do nothing
    }

    /**
     * <p>
     * Tells whether value can be converted to desiredType. The condition that
     * value can be converted is: value is a Short and not null, and desiredType
     * is Byte, BigDecimal, Integer, Long, Float, Double, or String.
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
        return AbstractionHelper.canConvert(value, Short.class, desiredType, new Class<?>[] { Byte.class,
            Integer.class, BigDecimal.class, Long.class, Float.class, Double.class, String.class });
    }

    /**
     * <p>
     * Converts value into an instance of desiredType. If value is a Short and
     * not null, and desiredType is Byte, BigDecimal, Integer, Long, Float,
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
                    + " -- by ShortConverter.");
        }
        Short shortValue = (Short) value;
        if (desiredType.isAssignableFrom(Byte.class)) {
            return new Byte(shortValue.byteValue());
        } else if (desiredType.isAssignableFrom(Long.class)) {
            return new Long(shortValue.longValue());
        } else if (desiredType.isAssignableFrom(Integer.class)) {
            return new Integer(shortValue.intValue());
        } else if (desiredType.isAssignableFrom(Float.class)) {
            return new Float(shortValue.floatValue());
        } else if (desiredType.isAssignableFrom(Double.class)) {
            return new Double(shortValue.doubleValue());
        } else if (desiredType.isAssignableFrom(BigDecimal.class)) {
            return new BigDecimal(shortValue.shortValue());
        } else { // String
            return value.toString();
        }
    }
}
