/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import junit.framework.TestCase;

/**
 * <p>
 * This tests {@link CustomResultSetMetaData}. Using ODBC.
 * </p>
 *
 * @author WishingBone, justforplay, suhugo
 * @version 2.0
 * @since 1.0
 */
public class CustomResultSetMetaDataODBCTest extends TestCase {

    /**
     * Instance of Connection.
     */
    private Connection connection;

    /**
     * Instance of java sql statement.
     */
    private Statement statement;

    /**
     * Set up database connection.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    protected void setUp() throws Exception {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        connection = DriverManager.getConnection("jdbc:odbc:Tester");
        statement = connection.createStatement();
    }

    /**
     * Close database connection.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    protected void tearDown() throws Exception {
        connection.close();
    }

    /**
     * Test CustomResultSetMetaData functionality.
     */
    public void testCustomResultSetMetaData() {
        try {
            ResultSet rs = statement.executeQuery("select * from tests");
            ResultSetMetaData rsmd = rs.getMetaData();
            CustomResultSetMetaData crsmd = new CustomResultSetMetaData(rsmd);
            assertTrue(crsmd.getColumnCount() == 6);
            assertTrue(crsmd.getColumnLabel(1).equals("B_Memo"));
            assertTrue(crsmd.getColumnLabel(2).equals("C_Number"));
            assertTrue(crsmd.getColumnLabel(3).equals("D_Text_To_Number"));
            assertTrue(crsmd.getColumnLabel(4).equals("E_Date/Time"));
            assertTrue(crsmd.getColumnLabel(5).equals("F_Currency"));
            assertTrue(crsmd.getColumnLabel(6).equals("H_Boolean"));
            assertNull(crsmd.getColumnLabel(0));
            assertNull(crsmd.getColumnLabel(7));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
