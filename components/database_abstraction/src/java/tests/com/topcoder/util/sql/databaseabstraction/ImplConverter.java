/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

/**
 * A trivial implementation of Converter.
 *
 * @author WishingBone,justforplay
 * @version 1.1
 * @since 1.0
 */
public class ImplConverter implements Converter {

    /**
     * Trivial implementation converts String into Integer.
     *
     * @param value
     *            the original object value
     * @param column
     *            the column count of the object
     * @param metaData
     *            meta data of the set
     * @return the converted result
     * @throws IllegalMappingException
     *             when is mapping is illegal
     */
    public Object convert(Object value, int column, CustomResultSetMetaData metaData) throws IllegalMappingException {
        return new Integer((String) value);
    }

}
