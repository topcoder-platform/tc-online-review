/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.accuracytests;

import com.topcoder.util.sql.databaseabstraction.Converter;
import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;
import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;


/**
 * A trivial implementation of Converter.
 *
 * @author PE
 * @version 1.1
 */
public class MockConverter implements Converter {
    /**
     * Trivial implementation converts String into Integer.
     *
     * @param value the original object value
     * @param column the column count of the object
     * @param metaData meta data of the set
     *
     * @return the converted result
     *
     * @throws IllegalMappingException when is mapping is illegal
     */
    public Object convert(Object value, int column, CustomResultSetMetaData metaData)
        throws IllegalMappingException {
        return value.toString() + "00000.1";
    }
}
