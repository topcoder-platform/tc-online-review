/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.util.HashSet;
import java.util.Set;

import com.topcoder.util.sql.databaseabstraction.ondemandconversion.BigDecimalConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.BlobConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.BooleanConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ByteArrayConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ByteConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ClobConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.DateConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.DoubleConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.FloatConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.IntConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.LongConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ShortConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.StringConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.TimeConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.TimestampConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.URLConverter;

/**
 * <p>
 * The OnDemandMapper class is the main focal point of the version 1.1 additions
 * to this component. The OnDemandMapper is similar to the Mapper class, but
 * contains a set of OnDemandConverters (and since they can be used on any
 * column, no map from column names is needed). Additionally, it exposes a more
 * type-safe set of methods than the Mapper class.
 * </p>
 * <p>
 * <strong>Changes in 2.0:</strong>
 * <ol>
 * <li>Specified generic parameters for generic types.</li>
 * <li>Added support of URLConverter to createDefaultOnDemandMapper().</li>
 * </ol>
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> This class holds mutable collection, and not
 * thread-safe.
 * </p>
 *
 * @author aubergineanode, saarixx, justforplay, suhugo
 * @version 2.0
 * @since 1.1
 */
public class OnDemandMapper {

    /**
     * <p>
     * The set of all OnDemandConverters associated with this OnDemandMapper.
     * All items in the set are non-null OnDemandConverter instances.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameter for the collection type.</li>
     * </ol>
     * </p>
     */
    private final Set<OnDemandConverter> converters = new HashSet<OnDemandConverter>();

    /**
     * <p>
     * Creates a new OnDemandMapper, which initially has no converters
     * available.
     * </p>
     */
    public OnDemandMapper() {
        // do nothing.
    }

    /**
     * <p>
     * Creates a new OnDemandMapper with the initial list of converters. All of
     * the elements of converters argument are added to the converters field.
     * </p>
     *
     * @param converters
     *            The converters that are initially part of the mapper.
     * @throws IllegalArgumentException
     *             If converters is null or contains null items.
     */
    public OnDemandMapper(OnDemandConverter[] converters) {
        AbstractionHelper.checkArray(converters, "converters");
        for (OnDemandConverter converter : converters) {
            this.converters.add(converter);
        }
    }

    /**
     * <p>
     * Adds a converter to the set of converters in the mapper. Adds the item to
     * the converters set.
     * </p>
     *
     * @param converter
     *            The converter to add
     * @return True if the converter was added. False if already in the mapper
     * @throws IllegalArgumentException
     *             If converter is null
     */
    public boolean addConverter(OnDemandConverter converter) {
        AbstractionHelper.checkNull(converter, "converter");
        return converters.add(converter);
    }

    /**
     * <p>
     * Removes a converter from the set of converters in the mapper.
     * </p>
     *
     * @param converter
     *            The converter to remove
     * @return True if the converter was removed, false if it was not in the
     *         mapper.
     * @throws IllegalArgumentException
     *             If converter is null
     */
    public boolean removeConverter(OnDemandConverter converter) {
        AbstractionHelper.checkNull(converter, "converter");
        return converters.remove(converter);
    }

    /**
     * <p>
     * Gets all converters in the mapper. Returns the converters set as an
     * OnDemandConverter[].
     * </p>
     *
     * @return All items in the converters set, as an array
     */
    public OnDemandConverter[] getConverters() {
        return converters.toArray(new OnDemandConverter[converters.size()]);
    }

    /**
     * <p>
     * Determines whether this mapper can convert a value in a CustomResultSet
     * to the requested type.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Changed type of "desiredType" parameter from "Class" to "Class<?>".</li>
     * </ol>
     * </p>
     *
     * @param value
     *            The value to convert
     * @param column
     *            The column in the result set the value came from
     * @param metaData
     *            The metadata for the result set (allows information about the
     *            column to be looked up)
     * @param desiredType
     *            The type to convert the result to
     * @return True if some converter can handle the conversion, false if none
     *         can
     * @throws IllegalArgumentException
     *             If metaData, desiredType is null
     */
    public boolean canConvert(Object value, int column, CustomResultSetMetaData metaData, Class<?> desiredType) {
        AbstractionHelper.checkNull(metaData, "metaData");
        AbstractionHelper.checkNull(desiredType, "desiredType");
        AbstractionHelper.checkColumnIndex(column, metaData.getColumnCount());
        for (OnDemandConverter converter : converters) {
            if (converter.canConvert(value, column, metaData, desiredType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Converts value into an instance of desiredType (or implementation of it,
     * if desiredType is an interface).
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Changed type of "desiredType" parameter from "Class" to "Class<?>".</li>
     * </ol>
     * </p>
     *
     * @param value
     *            The value to convert
     * @param column
     *            The column in the result set that value came from
     * @param metaData
     *            The metadata for the result set that value came from
     * @param desiredType
     *            The desired return type
     * @return value converted to the desired type
     * @throws IllegalArgumentException
     *             If metaData or desiredType is null
     * @throws IllegalMappingException
     *             If the mapping can not be performed by any converter, or a
     *             converter claims to be able to handle the conversion, but
     *             fails when doing it
     */
    public Object convert(Object value, int column, CustomResultSetMetaData metaData, Class<?> desiredType)
        throws IllegalMappingException {
        AbstractionHelper.checkNull(metaData, "metaData");
        AbstractionHelper.checkNull(desiredType, "desiredType");
        AbstractionHelper.checkColumnIndex(column, metaData.getColumnCount());
        for (OnDemandConverter converter : converters) {
            if (converter.canConvert(value, column, metaData, desiredType)) {
                return converter.convert(value, column, metaData, desiredType);
            }
        }
        throw new IllegalMappingException("No OnDemandConverter can converted the value to the desired type.");
    }

    /**
     * <p>
     * Clears the converters set, removing all converters associated with the
     * mapper.
     * </p>
     */
    public void clearConverters() {
        converters.clear();
    }

    /**
     * <p>
     * Creates an OnDemandMapper configured with the default
     * OnDemandConversions. That is, one of each of the converters in the
     * com.topcoder.util.sql.databaseabstraction.ondemandconversion namespace .
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Added support of URLConverter.</li>
     * </ol>
     * </p>
     *
     * @return An OnDemandMapper configured with the default converters
     */
    public static OnDemandMapper createDefaultOnDemandMapper() {
        OnDemandMapper defaultOnDemandMapper = new OnDemandMapper();
        defaultOnDemandMapper.addConverter(new BigDecimalConverter());
        defaultOnDemandMapper.addConverter(new BlobConverter());
        defaultOnDemandMapper.addConverter(new BooleanConverter());
        defaultOnDemandMapper.addConverter(new ByteArrayConverter());
        defaultOnDemandMapper.addConverter(new ByteConverter());
        defaultOnDemandMapper.addConverter(new ClobConverter());
        defaultOnDemandMapper.addConverter(new DateConverter());
        defaultOnDemandMapper.addConverter(new DoubleConverter());
        defaultOnDemandMapper.addConverter(new FloatConverter());
        defaultOnDemandMapper.addConverter(new IntConverter());
        defaultOnDemandMapper.addConverter(new LongConverter());
        defaultOnDemandMapper.addConverter(new ShortConverter());
        defaultOnDemandMapper.addConverter(new StringConverter());
        defaultOnDemandMapper.addConverter(new TimeConverter());
        defaultOnDemandMapper.addConverter(new TimestampConverter());
        defaultOnDemandMapper.addConverter(new URLConverter());
        return defaultOnDemandMapper;
    }
}
