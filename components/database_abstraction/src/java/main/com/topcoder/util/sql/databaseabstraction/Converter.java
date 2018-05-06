/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

/**
 * <p>
 * The Converter interface defines how explicit conversion is done. In order to
 * use the version 1.0 explicit conversion capabilities of this component, a
 * user must create a class implementing the Converter interface, register it
 * with a Mapper and register the Mapper with a DatabaseAbstractor.
 * </p>
 * <p>
 * This interface is the same with version 1.0.
 * </p>
 * <p>
 * This interface makes no demands on immutability or thread safety.
 * </p>
 *
 * @author argolite, aubergineanode, WishingBone, justforplay
 * @version 1.1
 * @since 1.0
 */
public interface Converter {
    /**
     * Converts data from one type to another. The return type is defined by the
     * implementation, as opposed to the contract for OnDemandConverter.
     *
     * @return the converted result (may be null)
     * @param value
     *            the original object value
     * @param column
     *            the column the value came from
     * @param metaData
     *            metadata for the result set
     * @throws IllegalMappingException
     *             when mapping is illegal (implementation defined - for
     *             example, invalid input)
     */
    public Object convert(Object value, int column, CustomResultSetMetaData metaData) throws IllegalMappingException;
}
