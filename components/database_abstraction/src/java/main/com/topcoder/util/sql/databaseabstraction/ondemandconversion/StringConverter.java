/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.ondemandconversion;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;

import com.topcoder.util.sql.databaseabstraction.AbstractionHelper;
import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;
import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.OnDemandConverter;

/**
 * <p>
 * The StringConverter class handles on demand conversions from the String type
 * to various other data types. It implements the OnDemandConverter interface
 * and has no state.
 * </p>
 * <p>
 * When a value is converted by StringConverter, the condition that value can be
 * converted is: value is a String and not null, and desiredType is
 * java.io.InputStream and characters in String are all &gt;=0 and &lt;128,or
 * desiredType is java.io.Reader, or desiredType is Byte, Short, Integer, Long,
 * Double, Float, or BigDecimal and String value can be parsed to the desired
 * type, or desiredType is Date, Timestamp, or Time, and String value can be
 * converted to corresponding type(date format is used to parse the String is it
 * is not null).
 * </p>
 * <p>
 * <strong>Changes in 2.0:</strong>
 * <ol>
 * <li>Specified generic parameters for all generic types in the code.</li>
 * <li>Added URL support.</li>
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
public class StringConverter implements OnDemandConverter {

    /**
     * value of max char allowed if converted to InputStream.
     */
    private static final int MAX_CHAR_VALUE = 127;

    /**
     * The format used when converting a String value into a Date. If it is
     * null, the valueOf methods will be used to parse dates instead. This field
     * is set in the constructor, is immutable, and can be null.
     */
    private final DateFormat dateFormat;

    /**
     * Create a new StringConverter.
     */
    public StringConverter() {
        dateFormat = null;
    }

    /**
     * Creates a new StringConverter.
     *
     * @param dateFormat
     *            The format to use when parsing dates
     * @throws IllegalArgumentException
     *             If dateFormat is null
     */
    public StringConverter(DateFormat dateFormat) {
        AbstractionHelper.checkNull(dateFormat, "dateFormat");
        this.dateFormat = dateFormat;
    }

    /**
     * <p>
     * Tells whether value can be converted to desiredType.The condition that
     * value can be converted is: value is a String and not null, and
     * desiredType is java.io.InputStream and characters in String are all
     * &gt;=0 and &lt;128,or desiredType is java.io.Reader, or desiredType is
     * Byte, Short, Integer, Long, Double, Float, or BigDecimal and String value
     * can be parsed to the desired type, or desiredType is Date, Timestamp, or
     * Time, and String value can be converted to corresponding type(date format
     * is used to parse the String is it is not null).
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Changed type of desiredType parameter from Class to Class<?>.</li>
     * <li>Added URL support.</li>
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
        if (value == null || !String.class.isAssignableFrom(value.getClass())) {
            return false;
        }
        String valueStr = (String) value;
        try {
            if (desiredType.isAssignableFrom(InputStream.class)) {
                for (int i = 0; i < valueStr.length(); i++) {
                    if (valueStr.charAt(i) < 0 || valueStr.charAt(i) > MAX_CHAR_VALUE) {
                        return false;
                    }
                }
                return true;
            } else if (desiredType.isAssignableFrom(URL.class)) {
                return true;
            } else if (desiredType.isAssignableFrom(Reader.class)) {
                return true;
            } else if (desiredType.isAssignableFrom(Byte.class)) {
                Byte.parseByte(valueStr);
                return true;
            } else if (desiredType.isAssignableFrom(Short.class)) {
                Short.parseShort(valueStr);
                return true;
            } else if (desiredType.isAssignableFrom(Integer.class)) {
                Integer.parseInt(valueStr);
                return true;
            } else if (desiredType.isAssignableFrom(Long.class)) {
                Long.parseLong(valueStr);
                return true;
            } else if (desiredType.isAssignableFrom(Double.class)) {
                Double.parseDouble(valueStr);
                return true;
            } else if (desiredType.isAssignableFrom(Float.class)) {
                Float.parseFloat(valueStr);
                return true;
            } else if (desiredType.isAssignableFrom(BigDecimal.class)) {
                new BigDecimal(valueStr);
                return true;
            } else {
                try {
                    if (desiredType.isAssignableFrom(Date.class)) {
                        if (dateFormat != null) {
                            dateFormat.parse(valueStr);
                        } else {
                            Date.valueOf(valueStr);
                        }
                        return true;
                    } else if (desiredType.isAssignableFrom(Time.class)) {
                        if (dateFormat != null) {
                            dateFormat.parse(valueStr);
                        } else {
                            Time.valueOf(valueStr);
                        }
                        return true;
                    } else if (desiredType.isAssignableFrom(Timestamp.class)) {
                        if (dateFormat != null) {
                            dateFormat.parse(valueStr);
                        } else {
                            Timestamp.valueOf(valueStr);
                        }
                        return true;
                    }
                } catch (ParseException e) {
                    // thrown by dateFormat.parse()
                    return false;
                } catch (IllegalArgumentException e) {
                    // thrown by *.valueOf()
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            // thrown by parse method of Byte,Short, Integer, Long, Double,
            // Float
            // and constructor of BigDecimal.
            return false;
        }
        return false;
    }

    /**
     * <p>
     * Converts value into an instance of desiredType. If value is a String and
     * not null, and desiredType is java.io.InputStream and characters in String
     * are all &gt;=0 and &lt;128;or desiredType is java.io.Reader; or
     * desiredType is Byte, Short, Integer, Long, Double, Float, or BigDecimal
     * and String value can be parsed to the desired type; or desiredType is
     * Date, Timestamp, or Time, and String value can be converted to
     * corresponding type(date format is used to parse the String is it is not
     * null),value is converted to desired type and returned.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Changed type of desiredType parameter from Class to Class<?>.</li>
     * <li>Added URL support.</li>
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
        AbstractionHelper.checkNull(metaData, "metaData");
        AbstractionHelper.checkNull(desiredType, "desiredType");
        AbstractionHelper.checkColumnIndex(column, metaData.getColumnCount());
        if (value == null || !String.class.isAssignableFrom(value.getClass())) {
            throw new IllegalMappingException("value can't be converted to the desired type.");
        }
        // canConvert is not invoked here, for invoking it will parse the value
        // twice.
        String valueStr = (String) value;
        if (desiredType.isAssignableFrom(InputStream.class)) {
            byte[] btArr = new byte[valueStr.length()];
            for (int i = 0; i < valueStr.length(); i++) {
                if (valueStr.charAt(i) >= 0 && valueStr.charAt(i) <= MAX_CHAR_VALUE) {
                    btArr[i] = (byte) valueStr.charAt(i);
                } else {
                    throw new IllegalMappingException("value can't be converted to the desired type -- " + desiredType
                            + " by StringConverter, character in String is <0 or >127.");
                }
            }
            return new ByteArrayInputStream(btArr);
        } else if (desiredType.isAssignableFrom(Reader.class)) {
            return new StringReader(valueStr);
        } else if (desiredType.isAssignableFrom(URL.class)) {
            try {
                return new URL(valueStr);
            } catch (MalformedURLException e) {
                throw new IllegalMappingException("value can't be converted to the desired type -- " + desiredType
                        + " -- by StringConverter", e);
            }
        } else {
            try {
                if (desiredType.isAssignableFrom(Byte.class)) {
                    return Byte.valueOf(valueStr);
                } else if (desiredType.isAssignableFrom(Short.class)) {
                    return Short.valueOf(valueStr);
                } else if (desiredType.isAssignableFrom(Integer.class)) {
                    return Integer.valueOf(valueStr);
                } else if (desiredType.isAssignableFrom(Long.class)) {
                    return Long.valueOf(valueStr);
                } else if (desiredType.isAssignableFrom(Double.class)) {
                    return Double.valueOf(valueStr);
                } else if (desiredType.isAssignableFrom(Float.class)) {
                    return Float.valueOf(valueStr);
                } else if (desiredType.isAssignableFrom(BigDecimal.class)) {
                    return new BigDecimal(valueStr);
                } else { // Date,Time, Timestamp
                    try {
                        if (desiredType.isAssignableFrom(Date.class)) {
                            if (dateFormat == null) {
                                return Date.valueOf(valueStr);
                            } else {
                                return new Date(dateFormat.parse(valueStr).getTime());
                            }
                        } else if (desiredType.isAssignableFrom(Time.class)) {
                            if (dateFormat == null) {
                                return Time.valueOf(valueStr);
                            } else {
                                return new Time(dateFormat.parse(valueStr).getTime());
                            }

                        } else if (desiredType.isAssignableFrom(Timestamp.class)) {
                            if (dateFormat == null) {
                                return Timestamp.valueOf(valueStr);
                            } else {
                                return new Timestamp(dateFormat.parse(valueStr).getTime());
                            }

                        }

                    } catch (ParseException e) {
                        throw new IllegalMappingException("value can't be converted to the desired type -- "
                                + desiredType + " -- by StringConverter", e);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalMappingException("value can't be converted to the desired type -- "
                                + desiredType + " -- by StringConverter", e);
                    }
                }
            } catch (NumberFormatException e) {
                throw new IllegalMappingException("value can't be converted to the desired type -- " + desiredType
                        + " -- by StringConverter", e);
            }
        }
        throw new IllegalMappingException("value can't be converted to the desired type -- " + desiredType
                + " -- by StringConverter");
    }
}