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
 * The BooleanConverter class handles on demand conversions from the Boolean
 * type to various other data types. It implements the OnDemandConverter
 * interface and has no state.
 * </p>
 * <p>
 * When a value is converted by BooleanConverter, the condition that value can
 * be converted is: value is a Boolean and is not null, and desiredType is Byte,
 * Short, Integer, Long, Float, Double, BigDecimal, or String.
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
public class BooleanConverter implements OnDemandConverter {

    /**
     * Creates a new BooleanConverter.
     */
    public BooleanConverter() {
        // do nothing
    }

    /**
     * <p>
     * Tells whether value can be converted to desiredType. The condition that
     * value can be converted is: value is a Boolean and is not null, and
     * desiredType is Byte, Short, Integer, Long, Float, Double, BigDecimal, or
     * String.
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
        return AbstractionHelper.canConvert(value, Boolean.class, desiredType, new Class<?>[] { Byte.class,
            Short.class, Integer.class, Long.class, Float.class, Double.class, BigDecimal.class, String.class });
    }

    /**
     * <p>
     * Converts value into an instance of desiredType. If value is a Boolean and
     * is not null, and desiredType is Byte, Short, Integer, Long, Float,
     * Double, BigDecimal, or String, value is converted to the corresponding
     * type and returned.
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
                    + " -- by BooleanConverter.");
        }
        String valueStr = ((Boolean) value).booleanValue() ? "1" : "0";
        if (desiredType.isAssignableFrom(Byte.class)) {
            return new Byte(valueStr);
        } else if (desiredType.isAssignableFrom(Short.class)) {
            return new Short(valueStr);
        } else if (desiredType.isAssignableFrom(Integer.class)) {
            return new Integer(valueStr);
        } else if (desiredType.isAssignableFrom(Long.class)) {
            return new Long(valueStr);
        } else if (desiredType.isAssignableFrom(Float.class)) {
            return new Float(valueStr);
        } else if (desiredType.isAssignableFrom(Double.class)) {
            return new Double(valueStr);
        } else if (desiredType.isAssignableFrom(BigDecimal.class)) {
            return new BigDecimal(valueStr);
        } else { // String
            return value.toString();
        }
    }
}
