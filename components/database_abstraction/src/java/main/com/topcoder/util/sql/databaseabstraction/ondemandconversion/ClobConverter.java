/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.ondemandconversion;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

import com.topcoder.util.sql.databaseabstraction.AbstractionHelper;
import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;
import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.OnDemandConverter;

/**
 * <p>
 * The ClobConverter class handles on demand conversions from the Clob type to
 * various other data types. It implements the OnDemandConverter interface and
 * has no state.
 * </p>
 * <p>
 * When a value is converted by ClobConverter, the condition that value can be
 * converted is: value is a Clob and not null, and desiredType is
 * java.io.Reader, java.io.InputStream, or String.
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
public class ClobConverter implements OnDemandConverter {

    /**
     * Create a new ClobConverter.
     */
    public ClobConverter() {
        // do nothing
    }

    /**
     * <p>
     * Tells whether value can be converted to desiredType. The condition that
     * value can be converted is: value is a Clob and not null, and desiredType
     * is java.io.Reader, java.io.InputStream, or String.
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
        return AbstractionHelper.canConvert(value, Clob.class, desiredType, new Class<?>[] { Reader.class,
            InputStream.class, String.class });
    }

    /**
     * <p>
     * Converts value into an instance of desiredType. If value is a Clob and
     * not null, and desiredType is java.io.Reader, java.io.InputStream, or
     * String, value is converted to the corresponding type and returned.
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
     *             If the conversion can not be made (if either of the Clob
     *             calls results in a SQL exception)
     */
    public Object convert(Object value, int column, CustomResultSetMetaData metaData, Class<?> desiredType)
        throws IllegalMappingException {
        // Validation of metaData, desiredType and column is done at
        // canConvert().
        if (!canConvert(value, column, metaData, desiredType)) {
            throw new IllegalMappingException("value can't be converted to the desired type -- " + desiredType
                    + " -- by ClobConverter.");
        }
        try {
            Clob clobValue = (Clob) value;
            if (desiredType.isAssignableFrom(InputStream.class)) {
                return clobValue.getAsciiStream();
            } else if (desiredType.isAssignableFrom(Reader.class)) {
                return clobValue.getCharacterStream();
            } else { // String
                return clobValue.getSubString(1, (int) clobValue.length());
            }
        } catch (SQLException e) {
            throw new IllegalMappingException("fail to convert value to the desired type.", e);
        }
    }
}
