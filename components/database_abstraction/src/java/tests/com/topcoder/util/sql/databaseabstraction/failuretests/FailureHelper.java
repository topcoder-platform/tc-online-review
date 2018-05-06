/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.failuretests;

import java.sql.Types;

import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;

/**
 * <p>
 * A helper class for the failure test.
 * </p>
 *
 * @author biotrail
 * @version 1.1
 * @since 1.1
 */
public class FailureHelper {
    /**
     * <p>
     * This private constructor prevents to create a new instance.
     * </p>
     */
    private FailureHelper() {

    }

    /**
     * <p>
     * Return a new CustomResultSetMetaData instance for testing.
     * </p>
     *
     * @return a new CustomResultSetMetaData instance for testing.
     *
     * @throws Exception to JUnit
     */
    public static CustomResultSetMetaData getCustomResultSetMetaDataInstance() throws Exception {
        SimpleResultSetMetaData srsmd = new SimpleResultSetMetaData();
        srsmd.addColumn("ColumnId", Types.INTEGER, "INTEGER", "java.lang.Integer", 0, 0);
        srsmd.addColumn("ColumnName", Types.VARCHAR, "VARCHAR(255)", "java.lang.String", 0, 0);
        return new CustomResultSetMetaData(srsmd);
    }

}
