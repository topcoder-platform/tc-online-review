/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.ondemandconversion;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.topcoder.util.sql.databaseabstraction.AbstractionHelper;
import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;
import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.OnDemandConverter;

/**
 * <p>
 * The DateConverter class handles on demand conversions from the Date type to
 * various other data types. It implements the OnDemandConverter interface and
 * has no state.
 * </p>
 * <p>
 * When a value is converted by DateConverter, the condition that value can be
 * converted is: value is a Date and is not null, and desiredType is Timestamp,
 * Time, Long, or String.
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
public class DateConverter implements OnDemandConverter {

    /**
     * The format used when converting the Date to a string. This field is set
     * in the constructor and is immutable. It can never be null.
     */
    private final DateFormat dateFormat;

    /**
     * Create a new DateConverter. date format used is initialized to default
     * SimpleDateFormat.
     */
    public DateConverter() {
        dateFormat = new SimpleDateFormat();
    }

    /**
     * Creates a new DateCoverter that uses the given format.
     *
     * @param dateFormat
     *            The format to use for writing dates
     * @throws IllegalArgumentException
     *             If dateFormat is null
     */
    public DateConverter(DateFormat dateFormat) {
        AbstractionHelper.checkNull(dateFormat, "dateFormat");
        this.dateFormat = dateFormat;
    }

    /**
     * <p>
     * Tells whether value can be converted to desiredType. The condition that
     * value can be converted is: value is a Date and is not null, and
     * desiredType is Timestamp, Time, Long, or String.
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
     *             If metaData or desiredType is null or column <= 0 or > column
     *             count of metadata.
     */
    public boolean canConvert(Object value, int column, CustomResultSetMetaData metaData, Class<?> desiredType) {
        AbstractionHelper.checkNull(metaData, "metaData");
        AbstractionHelper.checkNull(desiredType, "desiredType");
        AbstractionHelper.checkColumnIndex(column, metaData.getColumnCount());
        return AbstractionHelper.canConvert(value, Date.class, desiredType, new Class<?>[] { Timestamp.class,
            Time.class, Long.class, String.class });
    }

    /**
     * <p>
     * Converts value into an instance of desiredType. If value is a Date and is
     * not null, and desiredType is Timestamp, Time, Long, or String, value is
     * converted to the corresponding type and returned(specially, String value
     * will be formatted using the date format initialized in constructor).
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
     *             If metaData or desiredType is null or column is <= 0 or >
     *             column count of metadata
     * @throws IllegalMappingException
     *             If the conversion can not be made
     */
    public Object convert(Object value, int column, CustomResultSetMetaData metaData, Class<?> desiredType)
        throws IllegalMappingException {
        // Validation of metaData, desiredType and column is done at
        // canConvert().
        if (!canConvert(value, column, metaData, desiredType)) {
            throw new IllegalMappingException("value can't be converted to the desired type -- " + desiredType
                    + " -- by DateConverter.");
        }
        Date dateValue = (Date) value;
        if (desiredType.isAssignableFrom(Time.class)) {
            return new Time(dateValue.getTime());
        } else if (desiredType.isAssignableFrom(Timestamp.class)) {
            return new Timestamp(dateValue.getTime());
        } else if (desiredType.isAssignableFrom(Long.class)) {
            return new Long(dateValue.getTime());
        } else { // String
            return dateFormat.format(value);
        }
    }
}
