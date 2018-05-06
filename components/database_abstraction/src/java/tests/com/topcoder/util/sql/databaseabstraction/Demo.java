/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import junit.framework.TestCase;

/**
 * <p>
 * This is demo of component Database Abstraction.
 * </p>
 * <p>
 * Get corresponding Objects from retrieved JDBC ResultSet, if mapper is
 * provided, that the value will be converted to the corresponding value using
 * specified converter, when retrieving values, it will looking up it in the
 * mapped value , this is what this component behaves in version 1.0.
 * </p>
 * <p>
 * In version 1.1, the component stores the original value, so it is possible to
 * look up desired values if it is the same type with that of original value.
 * And this component also deals with the situation that the desired type is not
 * found in original or mapped value. Here on-demand converter is configured in
 * DatabseAbstractor. once on-demand converter is provided, this component will
 * use the on-demand converter to dynamically convert the mapped value or
 * original value to the desired type when desired value can't be found in
 * mapped value.
 * </p>
 *
 * @author justforplay, suhugo
 * @version 2.0
 * @since 1.1
 */
public class Demo extends TestCase {
    /**
     * Demo the usage of component Database abstraction.Including creation of
     * CustomResultSet and factory class - DatabaseAbstractor, Retrieving value
     * from original value, mapped value, Retrieving values when On-Demand
     * conversion is needed.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testDemo1() throws Exception {
        // Database configuration(In Oracle):
        // table: ABSTRACTION_TABLE
        // Column name type can-be-null
        // ID NUMBER(10) no
        // NAME VARCHAR2(10) no
        // AGE LONG yes
        // BLOB_T BLOB yes
        // CLOB_T CLOB yes
        // DATE_T DATE yes
        // URL VARCHAR(100) yes
        //
        // Values in table ABSTRACTION_TABLE
        // 50 TOPCODER 10 EMPTY_BLOB EMPTY_CLOB 2006-06-18 http://www.topcoder.com
        // 100 TEST 20 NOT-EMPTY NOT-EMPTY 2006-06-19 http://test.com
        // NOT-EMPTY above means that it contains value

        Connection conn = null;
        try {
            // Create database connection.
            conn = UnitTestHelper.getDatabaseConnection();
            // Retrieve ResultSet for Database.
            ResultSet rs = UnitTestHelper.getResultSet(conn);
            // Create DatabaseAbstractor
            DatabaseAbstractor dbAbstractor = new DatabaseAbstractor();

            // Initialize mapper
            HashMap<String, Converter> map = new HashMap<String, Converter>();
            // Class MockConverterBoolean which converts BigDecimal value to
            // Boolean(true)
            // and throw IllegalMappingException if it is not BigDecimal value.
            class MockConverterBoolean implements Converter {
                // simple implementation.
                /**
                 * Convert value to Boolean(true), if it is a BigDecimal.
                 *
                 * @return the converted result (may be null)
                 * @param value
                 *            the original object value
                 * @param column
                 *            the column the value came from
                 * @param metaData
                 *            metadata for the result set
                 * @throws IllegalMappingException
                 *             when value is not a BigDecimal
                 */
                public Object convert(Object value, int column, CustomResultSetMetaData metaData)
                        throws IllegalMappingException {
                    if (value.getClass().equals(BigDecimal.class)) {
                        return new Boolean(true);
                    } else {
                        throw new IllegalMappingException("illegalException.");
                    }
                }
            }
            // map instance of Converter to the column type named "number"
            map.put("decimal", new MockConverterBoolean());
            // create mapper with one converter which aims to convert number
            // column to boolean.
            Mapper mapper = new Mapper(map);

            // set mapper of DatabaseAbstractor.
            dbAbstractor.setMapper(mapper);

            // Get CustomResultSet.
            CustomResultSet crs = dbAbstractor.convertResultSet(rs);
            while (crs.next()) {
                // Get original value of CustomResultValue.
                crs.getBigDecimal("ID");

                // Get mapped value of CustomResultSet.
                crs.getBoolean(1);

                // Get not exist value will result in ClassCastException.
                // crs.getByte(1);

                // Get URL mapped value of CustomResultSet.
                crs.getURL("URL");
            }

            // Here demos improved function of version 1.1
            DatabaseAbstractor dbAbstractor2 = new DatabaseAbstractor();

            // set On-Demand Mapper of DatabaseAbstractor.
            dbAbstractor2.setOnDemandMapper(OnDemandMapper.createDefaultOnDemandMapper());

            // Set Mapper.
            dbAbstractor2.setMapper(mapper);

            // Get CustomResultSet.
            CustomResultSet crs2 = dbAbstractor2.convertResultSet(rs);
            while (crs2.next()) {
                // Get original value of CustomResultValue.
                crs.getBigDecimal("ID");

                // Get mapped value of CustomResultSet.
                crs.getBoolean(1);

                // Get not exist value in original value or mapped value, but
                // was converted from BigDecimal to
                // Byte.
                crs.getByte("ID");
                // Get not exist value in original value or mapped value, but
                // was converted from BigDecimal to
                // String.
                crs.getString(1);
            }

            // Demo the sort method of CustomResultSet.
            crs2.sortAscending("ID");

            // Retrieve value of sorted CustomResultSet.
            while (crs2.next()) {
                crs.getBigDecimal("ID");
            }
        } finally {
            // Close connection.
            if (conn != null) {
                conn.close();
            }
        }
    }
}
