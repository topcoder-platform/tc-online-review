/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

/**
 * <p>
 * The OnDemandConverter interface is the counterpart of the Converter
 * interface, updated to allow for version 1.1. on-demand conversion. In order
 * to support this, the conversion method takes in an extra parameter, the
 * desired returned type. This interface also exposes a method to determine
 * whether a conversion can be done.
 * </p>
 * <p>
 * <strong>Changes in 2.0:</strong>
 * <ol>
 * <li>Specified generic parameters for generic types.</li>
 * </ol>
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> This interface makes no thread-safety or
 * mutability requirements.
 * </p>
 *
 * @author aubergineanode, saarixx, justforplay, suhugoersion 2.0
 * @since 1.1
 */
public interface OnDemandConverter {
    /**
     * <p>
     * Determines whether this converter can convert the given input object into
     * an instance of desiredType (or implementation of, if desiredType is an
     * interface). If this method returns true, then a call to convert with the
     * same arguments should always succeed.
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
     *            The type that we want to determine if conversion is capable
     *            for
     * @return True if the converter can make the conversion, false otherwise.
     * @throws IllegalArgumentException
     *             If metaData or desiredType argument is null
     */
    public boolean canConvert(Object value, int column, CustomResultSetMetaData metaData, Class<?> desiredType);

    /**
     * <p>
     * Converts value into an instance of desiredType (or implementation of it,
     * if desiredType is an interface). The return value from this function
     * should always be of the type desiredType (or a subclass or an interface
     * implementation, depending on whether desiredType represents a class or an
     * interface).
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
        throws IllegalMappingException;
}
