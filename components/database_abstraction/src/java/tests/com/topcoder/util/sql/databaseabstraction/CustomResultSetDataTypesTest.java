/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;

import junit.framework.TestCase;

/**
 * <p>
 * This tests {@link CustomResultSet} data types.
 * </p>
 *
 * @author WishingBone, justforplay, suhugo
 * @version 2.0
 * @since 1.0
 */
public class CustomResultSetDataTypesTest extends TestCase {

    /**
     * Connection.
     */
    private Connection connection;

    /**
     * Statement.
     */
    private Statement statement;

    /**
     * BufferedReader.
     */
    private BufferedReader br;

    /**
     * Set up database connection.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        br = new BufferedReader(new FileReader("test_files/unittest.cfg"));
        Class.forName(br.readLine());
        connection = DriverManager.getConnection(br.readLine());
        statement = connection.createStatement();
    }

    /**
     * Close database connection.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        connection.close();
    }

    /**
     * Test result set data type.
     */
    public void testResultSetDataType() {
        try {
            ResultSet rs = statement.executeQuery("select * from tests");
            CustomResultSet crs = new CustomResultSet(rs);
            int n = crs.getRecordCount();
            for (;;) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                StringTokenizer st = new StringTokenizer(line, " ");
                int columnIndex = Integer.parseInt(st.nextToken());
                String columnName = st.nextToken();
                String className = st.nextToken();
                crs.beforeFirst();
                for (int i = 1; i <= n; ++i) {
                    crs.next();
                    if (className.equals("Array")) {
                        assertTrue(crs.getArray(columnIndex) == crs.getObject(columnIndex));
                        assertTrue(crs.getArray(columnName) == crs.getObject(columnName));
                    } else if (className.equals("InputStream")) {
                        assertTrue(crs.getAsciiStream(columnIndex) == crs.getObject(columnIndex));
                        assertTrue(crs.getAsciiStream(columnName) == crs.getObject(columnName));
                        assertTrue(crs.getBinaryStream(columnIndex) == crs.getObject(columnIndex));
                        assertTrue(crs.getBinaryStream(columnName) == crs.getObject(columnName));
                    } else if (className.equals("BigDecimal")) {
                        assertTrue(crs.getBigDecimal(columnIndex) == crs.getObject(columnIndex));
                        assertTrue(crs.getBigDecimal(columnName) == crs.getObject(columnName));
                    } else if (className.equals("Blob")) {
                        assertTrue(crs.getBlob(columnIndex) == crs.getObject(columnIndex));
                        assertTrue(crs.getBlob(columnName) == crs.getObject(columnName));
                    } else if (className.equals("Boolean")) {
                        assertTrue(
                                crs.getBoolean(columnIndex) == ((Boolean) crs.getObject(columnIndex)).booleanValue());
                        assertTrue(crs.getBoolean(columnName) == ((Boolean) crs.getObject(columnName)).booleanValue());
                    } else if (className.equals("Byte")) {
                        assertTrue(crs.getByte(columnIndex) == ((Byte) crs.getObject(columnIndex)).byteValue());
                        assertTrue(crs.getByte(columnName) == ((Byte) crs.getObject(columnName)).byteValue());
                    } else if (className.equals("byte[]")) {
                        assertTrue(crs.getBytes(columnIndex) == crs.getObject(columnIndex));
                        assertTrue(crs.getBytes(columnName) == crs.getObject(columnName));
                    } else if (className.equals("Reader")) {
                        assertTrue(crs.getCharacterStream(columnIndex) == crs.getObject(columnIndex));
                        assertTrue(crs.getCharacterStream(columnName) == crs.getObject(columnName));
                    } else if (className.equals("Clob")) {
                        assertTrue(crs.getClob(columnIndex) == crs.getObject(columnIndex));
                        assertTrue(crs.getClob(columnName) == crs.getObject(columnName));
                    } else if (className.equals("Date")) {
                        assertTrue(crs.getDate(columnIndex) == crs.getObject(columnIndex));
                        assertTrue(crs.getDate(columnName) == crs.getObject(columnName));
                    } else if (className.equals("Double")) {
                        assertTrue(crs.getDouble(columnIndex) == ((Double) crs.getObject(columnIndex)).doubleValue());
                        assertTrue(crs.getDouble(columnName) == ((Double) crs.getObject(columnName)).doubleValue());
                    } else if (className.equals("Float")) {
                        assertTrue(crs.getFloat(columnIndex) == ((Float) crs.getObject(columnIndex)).floatValue());
                        assertTrue(crs.getFloat(columnName) == ((Float) crs.getObject(columnName)).floatValue());
                    } else if (className.equals("Integer")) {
                        assertTrue(crs.getInt(columnIndex) == ((Integer) crs.getObject(columnIndex)).intValue());
                        assertTrue(crs.getInt(columnName) == ((Integer) crs.getObject(columnName)).intValue());
                    } else if (className.equals("Long")) {
                        assertTrue(crs.getLong(columnIndex) == ((Long) crs.getObject(columnIndex)).longValue());
                        assertTrue(crs.getLong(columnName) == ((Long) crs.getObject(columnName)).longValue());
                    } else if (className.equals("Ref")) {
                        assertTrue(crs.getRef(columnIndex) == crs.getObject(columnIndex));
                        assertTrue(crs.getRef(columnName) == crs.getObject(columnName));
                    } else if (className.equals("Short")) {
                        assertTrue(crs.getShort(columnIndex) == ((Short) crs.getObject(columnIndex)).shortValue());
                        assertTrue(crs.getShort(columnName) == ((Short) crs.getObject(columnName)).shortValue());
                    } else if (className.equals("String")) {
                        assertTrue(crs.getString(columnIndex) == crs.getObject(columnIndex));
                        assertTrue(crs.getString(columnName) == crs.getObject(columnName));
                    } else if (className.equals("Struct")) {
                        assertTrue(crs.getStruct(columnIndex) == crs.getObject(columnIndex));
                        assertTrue(crs.getStruct(columnName) == crs.getObject(columnName));
                    } else if (className.equals("Time")) {
                        assertTrue(crs.getTime(columnIndex) == crs.getObject(columnIndex));
                        assertTrue(crs.getTime(columnName) == crs.getObject(columnName));
                    } else if (className.equals("Timestamp")) {
                        assertTrue(crs.getTimestamp(columnIndex) == crs.getObject(columnIndex));
                        assertTrue(crs.getTimestamp(columnName) == crs.getObject(columnName));
                    }
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
