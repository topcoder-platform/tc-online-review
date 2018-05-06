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
 * Test case for {@link CustomResultSetMetaData}.
 *
 * @author justforplay, suhugo
 * @version 2.0
 * @since 1.1
 */
public class CustomResultSetMetaDataTest extends TestCase {

    /**
     * instance of ResultSetMetaData to create CustomResultSetMetaData.
     */
    private ResultSetMetaData rsmd;

    /**
     * Instance of CustomResultSetMetaData for unit test.
     */
    private CustomResultSetMetaData customRSMD;

    /**
     * Database connection.
     */
    private Connection connection;

    /**
     * Initialized instance of CustomResultSetMetaData for unit test.This method
     * will initialize the Connection without closing it.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    protected void setUp() throws Exception {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        connection = DriverManager.getConnection("jdbc:odbc:Tester");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from tests");
        rsmd = resultSet.getMetaData();
        customRSMD = new CustomResultSetMetaData(rsmd);
    }

    /**
     * Close connection initialized in detUp method.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    protected void tearDown() throws Exception {
        connection.close();
    }

    /**
     * <p>
     * Test Constructor CustomResultSetMetaData(ResultSetMetaData).
     * </p>
     * <p>
     * Verify: CustomResultSetMetaData can be instantiated correctly.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCustomResultSetMetaData() throws Exception {
        assertNotNull("Unable to instantiate CustomResultSetMetaData.", customRSMD);
        assertEquals("CustomResultSetMetaData is not initialized correctly.", customRSMD.getColumnCount(),
                rsmd.getColumnCount());
    }

    /**
     * <p>
     * Test Constructor CustomResultSetMetaData(ResultSetMetaData).
     * </p>
     * <p>
     * Verify: CustomResultSetMetaData can be instantiated when parameter is
     * null.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCustomResultSetMetaData2() throws Exception {
        CustomResultSetMetaData tMetaData = new CustomResultSetMetaData(null);
        assertNotNull("Unable to instantiate CustomResultSetMetaData.", tMetaData);
    }

    /**
     * <p>
     * Test getColumnClassName(int).
     * </p>
     * <p>
     * Verify:columnClassName can be retrieved correctly.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnClassName1() throws Exception {
        assertEquals("getColumnClassName(int) is incorrect.", customRSMD.getColumnClassName(1),
                rsmd.getColumnClassName(1));
    }

    /**
     * <p>
     * Test getColumnClassName(int).
     * </p>
     * <p>
     * Verify:When columnIndex<=0, null is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnClassName2() throws Exception {
        assertNull("getColumnClassName(int) is incorrect.", customRSMD.getColumnClassName(0));
    }

    /**
     * <p>
     * Test getColumnClassName(int).
     * </p>
     * <p>
     * Verify:When columnIndex>column count, null is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnClassName3() throws Exception {
        assertNull("getColumnClassName(int) is incorrect.", customRSMD.getColumnClassName(rsmd.getColumnCount() + 1));
    }

    /**
     * <p>
     * Test getColumnDisplaySize(int).
     * </p>
     * <p>
     * Verify:getColumnDisplaySize is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnDisplaySize1() throws Exception {
        assertEquals("getColumnDisplaySize(int) is incorrect.", customRSMD.getColumnDisplaySize(1),
                rsmd.getColumnDisplaySize(1));
    }

    /**
     * <p>
     * Test getColumnDisplaySize(int).
     * </p>
     * <p>
     * Verify:When columnIndex<=0, 0 is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnDisplaySize2() throws Exception {
        assertEquals("getColumnDisplaySize(int) is incorrect.", customRSMD.getColumnDisplaySize(0), 0);
    }

    /**
     * <p>
     * Test getColumnCount(int).
     * </p>
     * <p>
     * Verify:When columnIndex>column count, 0 is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnDisplaySize3() throws Exception {
        assertEquals("getColumnDisplaySize(int) is incorrect.",
                customRSMD.getColumnDisplaySize(rsmd.getColumnCount() + 1), 0);
    }

    /**
     * <p>
     * Test getColumnCount().
     * </p>
     * <p>
     * Verify: getColumnCount is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnCount() throws Exception {
        assertEquals("getColumnCount() is incorrect.", customRSMD.getColumnCount(), rsmd.getColumnCount());
    }

    /**
     * <p>
     * Test getColumnLabel(int).
     * </p>
     * <p>
     * Verify:getColumnLabel can be retrieved correctly.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnLabel1() throws Exception {
        assertEquals("getColumnLabel(int) is incorrect.", customRSMD.getColumnLabel(1), rsmd.getColumnLabel(1));
    }

    /**
     * <p>
     * Test getColumnLabel(int).
     * </p>
     * <p>
     * Verify:When columnIndex<=0, null is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnLabel2() throws Exception {
        assertNull("getColumnLabel(int) is incorrect.", customRSMD.getColumnLabel(0));
    }

    /**
     * <p>
     * Test getColumnClassName(int).
     * </p>
     * <p>
     * Verify:When columnIndex>column count, null is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnLabel3() throws Exception {
        assertNull("getColumnLabel(int) is incorrect.", customRSMD.getColumnLabel(rsmd.getColumnCount() + 1));
    }

    /**
     * <p>
     * Test getColumnName(int).
     * </p>
     * <p>
     * Verify:getColumnName can be retrieved correctly.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testgetColumnName1() throws Exception {
        assertEquals("getColumnName(int) is incorrect.", customRSMD.getColumnName(1), rsmd.getColumnName(1));
    }

    /**
     * <p>
     * Test getColumnName(int).
     * </p>
     * <p>
     * Verify:When columnIndex<=0, null is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testgetColumnName2() throws Exception {
        assertNull("getColumnLabel(int) is incorrect.", customRSMD.getColumnName(0));
    }

    /**
     * <p>
     * Test getColumnName(int).
     * </p>
     * <p>
     * Verify:When columnIndex>column count, null is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnNamel3() throws Exception {
        assertNull("getColumnLabel(int) is incorrect.", customRSMD.getColumnName(rsmd.getColumnCount() + 1));
    }

    /**
     * <p>
     * Test getColumnType(int).
     * </p>
     * <p>
     * Verify:getColumnType is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnType1() throws Exception {
        assertEquals("getColumnType(int) is incorrect.", customRSMD.getColumnType(1), rsmd.getColumnType(1));
    }

    /**
     * <p>
     * Test getColumnType(int).
     * </p>
     * <p>
     * Verify:When columnIndex<=0, 0 is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnType2() throws Exception {
        assertEquals("getColumnType(int) is incorrect.", customRSMD.getColumnType(0), 0);
    }

    /**
     * <p>
     * Test getColumnType(int).
     * </p>
     * <p>
     * Verify:When columnIndex>column count, 0 is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnType3() throws Exception {
        assertEquals("getColumnType(int) is incorrect.", customRSMD.getColumnType(rsmd.getColumnCount() + 1), 0);
    }

    /**
     * <p>
     * Test getColumnTypeName(int).
     * </p>
     * <p>
     * Verify:getColumnTypeName can be retrieved correctly.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnTypeName1() throws Exception {
        assertEquals("getColumnTypeName(int) is incorrect.",
                customRSMD.getColumnTypeName(1), rsmd.getColumnTypeName(1));
    }

    /**
     * <p>
     * Test getColumnTypeName(int).
     * </p>
     * <p>
     * Verify:When columnIndex<=0, null is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnTypeName2() throws Exception {
        assertNull("getColumnTypeName(int) is incorrect.", customRSMD.getColumnTypeName(0));
    }

    /**
     * <p>
     * Test getColumnTypeName(int).
     * </p>
     * <p>
     * Verify:When columnIndex>column count, null is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnTypeName3() throws Exception {
        assertNull("getColumnTypeName(int) is incorrect.", customRSMD.getColumnTypeName(rsmd.getColumnCount() + 1));
    }

    /**
     * <p>
     * Test getColumnPrecision(int).
     * </p>
     * <p>
     * Verify:getColumnPrecision is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnPrecision1() throws Exception {
        assertEquals("getColumnPrecision(int) is incorrect.", customRSMD.getColumnPrecision(1), rsmd.getPrecision(1));
    }

    /**
     * <p>
     * Test getColumnPrecision(int).
     * </p>
     * <p>
     * Verify:When columnIndex<=0, 0 is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnPrecision2() throws Exception {
        assertEquals("getColumnPrecision(int) is incorrect.", customRSMD.getColumnPrecision(0), 0);
    }

    /**
     * <p>
     * Test getColumnPrecision(int).
     * </p>
     * <p>
     * Verify:When columnIndex>column count, 0 is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnPrecision3() throws Exception {
        assertEquals("getColumnPrecision(int) is incorrect.", customRSMD.getColumnPrecision(rsmd.getColumnCount() + 1),
                0);
    }

    /**
     * <p>
     * Test getColumnScale(int).
     * </p>
     * <p>
     * Verify:getColumnScale is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnScale1() throws Exception {
        assertEquals("getColumnScale(int) is incorrect.", customRSMD.getColumnScale(1), rsmd.getScale(1));
    }

    /**
     * <p>
     * Test getColumnPrecision(int).
     * </p>
     * <p>
     * Verify:When columnIndex<=0, 0 is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnScale2() throws Exception {
        assertEquals("getColumnScale(int) is incorrect.", customRSMD.getColumnScale(0), 0);
    }

    /**
     * <p>
     * Test getColumnScale(int).
     * </p>
     * <p>
     * Verify:When columnIndex>column count, 0 is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetColumnScale3() throws Exception {
        assertEquals("getColumnScale(int) is incorrect.", customRSMD.getColumnScale(rsmd.getColumnCount() + 1), 0);
    }

    /**
     * <p>
     * Test isAutoIncrement(int).
     * </p>
     * <p>
     * Verify:isAutoIncrement is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAutoIncrement1() throws Exception {
        assertEquals("isAutoIncrement(int) is incorrect.", customRSMD.isAutoIncrement(1), rsmd.isAutoIncrement(1));
    }

    /**
     * <p>
     * Test isAutoIncrement(int).
     * </p>
     * <p>
     * Verify:When columnIndex<=0, false is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAutoIncrement2() throws Exception {
        assertEquals("isAutoIncrement(int) is incorrect.", customRSMD.isAutoIncrement(0), false);
    }

    /**
     * <p>
     * Test isAutoIncrement(int).
     * </p>
     * <p>
     * Verify:When columnIndex>column count, false is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAutoIncrement3() throws Exception {
        assertEquals("isAutoIncrement(int) is incorrect.",
                customRSMD.isAutoIncrement(rsmd.getColumnCount() + 1), false);
    }

    /**
     * <p>
     * Test isCurrency(int).
     * </p>
     * <p>
     * Verify:isCurrency is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsCurrency1() throws Exception {
        assertEquals("isCurrency(int) is incorrect.", customRSMD.isCurrency(1), rsmd.isCurrency(1));
    }

    /**
     * <p>
     * Test isCurrency(int).
     * </p>
     * <p>
     * Verify:When columnIndex<=0, false is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsCurrency2() throws Exception {
        assertFalse("isCurrency(int) is incorrect.", customRSMD.isCurrency(0));
    }

    /**
     * <p>
     * Test isCurrency(int).
     * </p>
     * <p>
     * Verify:When columnIndex>column count, false is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsCurrency3() throws Exception {
        assertFalse("isCurrency(int) is incorrect.", customRSMD.isCurrency(rsmd.getColumnCount() + 1));
    }

    /**
     * <p>
     * Test isSigned(int).
     * </p>
     * <p>
     * Verify:isSigned is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsSigned1() throws Exception {
        assertEquals("isSigned(int) is incorrect.", customRSMD.isSigned(1), rsmd.isSigned(1));
    }

    /**
     * <p>
     * Test isSigned(int).
     * </p>
     * <p>
     * Verify:When columnIndex<=0, false is returned..
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsSigned2() throws Exception {
        assertFalse("isSigned(int) is incorrect.", customRSMD.isSigned(0));
    }

    /**
     * <p>
     * Test isSigned(int).
     * </p>
     * <p>
     * Verify:When columnIndex>column count, false is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsSigned3() throws Exception {
        assertFalse("isSigned(int) is incorrect.", customRSMD.isSigned(rsmd.getColumnCount() + 1));
    }

    /**
     * <p>
     * Test setColumnClassName(int, String).
     * </p>
     * <p>
     * Verify: setColumnClassName is correct.
     * </p>
     */
    public void testSetColumnClassName1() {
        customRSMD.setColumnClassName(1, "TEST");
        assertEquals("setColumnClassName is incorrect.", customRSMD.getColumnClassName(1), "TEST");
    }

    /**
     * <p>
     * Test setColumnClassName(int, String).
     * </p>
     * <p>
     * Verify: setColumnClassName is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSetColumnClassName2() throws Exception {
        customRSMD.setColumnClassName(rsmd.getColumnCount(), "  ");
        assertEquals("setColumnClassName is incorrect.", customRSMD.getColumnClassName(rsmd.getColumnCount()), "  ");
    }

    /**
     * <p>
     * Test setColumnClassName(int, String).
     * </p>
     * <p>
     * Verify: setColumnClassName is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSetColumnClassName3() throws Exception {
        customRSMD.setColumnClassName(rsmd.getColumnCount(), null);
        assertNull("setColumnClassName is incorrect.", customRSMD.getColumnClassName(rsmd.getColumnCount()));
    }

    /**
     * <p>
     * Test setColumnLabel(int, String).
     * </p>
     * <p>
     * Verify: setColumnLabel is correct.
     * </p>
     */
    public void testSetColumnLabel1() {
        customRSMD.setColumnLabel(1, "TEST");
        assertEquals("setColumnLabel is incorrect.", customRSMD.getColumnLabel(1), "TEST");
    }

    /**
     * <p>
     * Test setColumnLabel(int, String).
     * </p>
     * <p>
     * Verify: setColumnLabel is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSetColumnLabel2() throws Exception {
        customRSMD.setColumnLabel(rsmd.getColumnCount(), "  ");
        assertEquals("setColumnLabel is incorrect.", customRSMD.getColumnLabel(rsmd.getColumnCount()), "  ");
    }

    /**
     * <p>
     * Test setColumnLabel(int, String).
     * </p>
     * <p>
     * Verify: setColumnLabel is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSetColumnLabel3() throws Exception {
        customRSMD.setColumnLabel(rsmd.getColumnCount(), null);
        assertNull("setColumnLabel is incorrect.", customRSMD.getColumnLabel(rsmd.getColumnCount()));
    }
}
