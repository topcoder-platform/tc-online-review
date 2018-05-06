/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Test case for {@link CustomResultSet}.
 *
 * @author justforplay, suhugo
 * @version 2.0
 * @since 1.1
 */
public class CustomResultSetTest extends TestCase {

    /**
     * Instance of Connection.
     */
    private Connection conn;

    /**
     * Instance of java.sql.ResultSet in order to create CustomResultSet.
     */
    private ResultSet testRS;

    /**
     * Instance of CustomResultSet for unit test.
     */
    private CustomResultSet testCRS;

    /**
     * Initialized instance of CustomResultSet for unit test.This method will
     * initialize the Connection without closing it.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    protected void setUp() throws Exception {
        conn = UnitTestHelper.getDatabaseConnection();
        testRS = UnitTestHelper.getResultSet(conn);
        testCRS = new CustomResultSet(testRS, null, OnDemandMapper.createDefaultOnDemandMapper());
    }

    /**
     * Close connection initialized in detUp method.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    protected void tearDown() throws Exception {
        testRS.close();
        UnitTestHelper.closeConnection(conn);
    }

    /**
     * <p>
     * Test CustomResultSet(ResultSet resultSet).
     * </p>
     * <p>
     * Verify: When resultSet is null, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCustomResultSetResultSet1() throws Exception {
        try {
            new CustomResultSet(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test CustomResultSet(ResultSet resultSet).
     * </p>
     * <p>
     * Verify: CustomResultSet can be instantiated correctly.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCustomResultSetResultSet2() throws Exception {
        CustomResultSet customRS = new CustomResultSet(testRS);
        assertNotNull("Unable to instantiate CustomResultSet.", customRS);
    }

    /**
     * <p>
     * Test CustomResultSet(ResultSet resultSet, Mapper mapper).
     * </p>
     * <p>
     * Verify: When resultSet is null, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCustomResultSetResultSet3() throws Exception {
        try {
            new CustomResultSet(null, (Mapper) null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test CustomResultSet(ResultSet resultSet, Mapper mapper) .
     * </p>
     * <p>
     * Verify: CustomResultSet can be instantiated correctly.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCustomResultSetResultSet4() throws Exception {
        CustomResultSet customRS = new CustomResultSet(testRS, (Mapper) null);
        assertNotNull("Unable to instantiate CustomResultSet.", customRS);
    }

    /**
     * <p>
     * Test CustomResultSet(ResultSet resultSet, Mapper mapper, OnDemandMapper
     * onDemandMapper).
     * </p>
     * <p>
     * Verify: When resultSet is null, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCustomResultSetResultSet7() throws Exception {
        try {
            new CustomResultSet(null, null, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test CustomResultSet(ResultSet resultSet, Mapper mapper, OnDemandMapper
     * onDemandMapper) .
     * </p>
     * <p>
     * Verify: CustomResultSet can be instantiated correctly.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCustomResultSetResultSet8() throws Exception {
        CustomResultSet customRS = new CustomResultSet(testRS, null, null);
        assertNotNull("Unable to instantiate CustomResultSet.", customRS);
    }

    /**
     * <p>
     * Test absolute(int row).
     * </p>
     * <p>
     * Verify: absolute() is correct.
     * </p>
     */
    public void testAbsolute1() {
        assertFalse("absolute() is incorrect.", testCRS.absolute(0));
        try {
            testCRS.getLong(1);
        } catch (InvalidCursorStateException e) {
            // good
        } catch (NullColumnValueException e) {
            fail("InvalidCursorStateException expected.");
        }
    }

    /**
     * <p>
     * Test absolute(int row).
     * </p>
     * <p>
     * Verify: absolute() is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testAbsolute2() throws Exception {
        assertFalse("absolute() is incorrect.", testCRS.absolute(testRS.getMetaData().getColumnCount() + 1));
        try {
            testCRS.getLong(1);
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test absolute(int row).
     * </p>
     * <p>
     * Verify: absolute() is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testAbsolute3() throws Exception {
        assertTrue("absolute() is incorrect.", testCRS.absolute(1));
        assertEquals(testCRS.getLong(1), 50);
    }

    /**
     * <p>
     * Test absolute(int row).
     * </p>
     * <p>
     * Verify: absolute() is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testAbsolute4() throws Exception {
        assertTrue("absolute() is incorrect.", testCRS.absolute(-1));
        assertEquals("absolute() is incorrect.", testCRS.getLong(1), 150);
    }

    /**
     * <p>
     * Test afterLast().
     * </p>
     * <p>
     * Verify: afterLast() is correct.
     * </p>
     */
    public void testAfterLast() {
        testCRS.afterLast();
        try {
            testCRS.getLong(1);
        } catch (InvalidCursorStateException e) {
            // good
        } catch (NullColumnValueException e) {
            fail("InvalidCursorStateException expected.");
        }
    }

    /**
     * <p>
     * Test beforeFirst().
     * </p>
     * <p>
     * Verify: beforeFirst() is correct.
     * </p>
     */
    public void testBeforeFirst() {
        testCRS.beforeFirst();
        try {
            testCRS.getLong(1);
        } catch (InvalidCursorStateException e) {
            // good
        } catch (NullColumnValueException e) {
            fail("InvalidCursorStateException expected.");
        }
    }

    /**
     * <p>
     * Test findColumn(String).
     * </p>
     * <p>
     * Verify:findColumn is correct.
     * </p>
     */
    public void testFindColumn1() {
        assertEquals("findColumn is incorrect.", testCRS.findColumn("name"), 2);
    }

    /**
     * <p>
     * Test findColumn(String).
     * </p>
     * <p>
     * Verify:findColumn is correct.
     * </p>
     */
    public void testFindColumn2() {
        assertEquals("findColumn is incorrect.", testCRS.findColumn(null), 0);
    }

    /**
     * <p>
     * Test findColumn(String).
     * </p>
     * <p>
     * Verify:findColumn is correct.
     * </p>
     */
    public void testFindColumn3() {
        assertEquals("findColumn is incorrect.", testCRS.findColumn("Not_Exist_Column"), 0);
    }

    /**
     * <p>
     * Test first().
     * </p>
     * <p>
     * Verify:first() is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testFirst() throws Exception {
        testCRS.first();
        assertEquals("first() is incorrect.", testCRS.getLong(1), 50);

    }

    /**
     * <p>
     * Test getArray(int columnIndex).
     * </p>
     * <p>
     * Verify:When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetArray1() {
        try {
            testCRS.getArray(0);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getArray(int columnIndex).
     * </p>
     * <p>
     * Verify:When value of specified column can not convert to Array,
     * ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetArray2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getArray(1);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getArray(String columnName).
     * </p>
     * <p>
     * Verify:When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetArray3() {
        try {
            testCRS.getArray("Name");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getArray(String columnName).
     * </p>
     * <p>
     * Verify:When value of specified columnName can not convert to Array,
     * ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetArray4() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getArray(1);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getAsciiStream(int columnIndex).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetAsciiStream1() {
        try {
            testCRS.getAsciiStream(1);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getAsciiStream(int columnIndex).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetAsciiStream2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getAsciiStream(1);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getAsciiStream(int columnIndex).
     * </p>
     * <p>
     * Verify: When getAsciiStream is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetAsciiStream3() throws Exception {
        testCRS.absolute(2);
        InputStream is = testCRS.getAsciiStream(4);

        byte[] data = getExpectedInputStreamData();

        byte real[] = new byte[data.length];
        is.read(real);

        assertEquals("getAsciiStream is incorrect.", data.length, real.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals("getAsciiStream is incorrect.", data[i], real[i]);
        }

    }

    /**
     * <p>
     * Test getAsciiStream(String columnName).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetAsciiStream4() {
        try {
            testCRS.getAsciiStream(1);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getAsciiStream(String columnName).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetAsciiStream5() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getAsciiStream("id");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getAsciiStream(String columnName).
     * </p>
     * <p>
     * Verify: When getAsciiStream is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetAsciiStream6() throws Exception {
        testCRS.absolute(2);
        InputStream is = testCRS.getAsciiStream("blob_t");

        byte[] data = getExpectedInputStreamData();

        byte real[] = new byte[data.length];
        is.read(real);

        assertEquals("getAsciiStream is incorrect.", data.length, real.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals("getAsciiStream is incorrect.", data[i], real[i]);
        }

    }

    /**
     * <p>
     * Test getBigDecimal(int columnIndex).
     * </p>
     * <p>
     * Verify:When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetBigDecimal1() {
        try {
            testCRS.getBigDecimal(1);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBigDecimal(int columnIndex).
     * </p>
     * <p>
     * Verify:When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBigDecimal2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getBigDecimal(4);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBigDecimal(int columnIndex).
     * </p>
     * <p>
     * Verify:getBigDecimal is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBigDecimal3() throws Exception {
        testCRS.absolute(1);

        assertEquals("getBigDecimal is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(50));
    }

    /**
     * <p>
     * Test getBigDecimal(String columnName).
     * </p>
     * <p>
     * Verify:When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetBigDecimal4() {
        try {
            testCRS.getBigDecimal("ID");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBigDecimal(String columnName).
     * </p>
     * <p>
     * Verify:When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBigDecimal5() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getBigDecimal("blob_t");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBigDecimal(String columnName).
     * </p>
     * <p>
     * Verify:getBigDecimal is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBigDecimal6() throws Exception {
        testCRS.absolute(1);
        assertEquals("getBigDecimal is incorrect.", testCRS.getBigDecimal("id"), new BigDecimal(50));
    }

    /**
     * <p>
     * Test getBinaryStream(int columnIndex).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetBinaryStream1() {
        try {
            testCRS.getBinaryStream(4);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBinaryStream(int columnIndex).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBinaryStream2() throws Exception {
        testCRS.absolute(2);
        try {
            testCRS.getBinaryStream(1);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBinaryStream(int columnIndex).
     * </p>
     * <p>
     * Verify: getBinaryStream() is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBinaryStream3() throws Exception {
        testCRS.absolute(2);
        InputStream is = testCRS.getBinaryStream(4);

        byte[] data = getExpectedInputStreamData();

        byte real[] = new byte[data.length];
        is.read(real);

        assertEquals("getBinaryStream is incorrect.", data.length, real.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals("getBinaryStream is incorrect.", data[i], real[i]);
        }
    }

    /**
     * <p>
     * Test getBinaryStream(String columnName).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetBinaryStream4() {
        try {
            testCRS.getBinaryStream("BLOB_T");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBinaryStream(String columnName).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBinaryStream5() throws Exception {
        testCRS.absolute(2);
        try {
            testCRS.getBinaryStream("id");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBinaryStream(String columnName).
     * </p>
     * <p>
     * Verify: getBinaryStream() is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBinaryStream6() throws Exception {
        testCRS.absolute(2);
        InputStream is = testCRS.getBinaryStream("blob_t");

        byte[] data = getExpectedInputStreamData();

        byte real[] = new byte[data.length];
        is.read(real);

        assertEquals("getBinaryStream is incorrect.", data.length, real.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals("getBinaryStream is incorrect.", data[i], real[i]);
        }
    }

    /**
     * <p>
     * Test getBlob(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetBlob1() {
        try {
            testCRS.getBlob(4);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBlob(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBlob2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getBlob(1);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBlob(int).
     * </p>
     * <p>
     * Verify: getBlob is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBlob3() throws Exception {
        testCRS.absolute(2);
        Blob blob = testCRS.getBlob(4);
        InputStream is = blob.getBinaryStream();

        byte[] data = getExpectedInputStreamData();

        byte real[] = new byte[data.length];
        is.read(real);

        assertEquals("getBinaryStream is incorrect.", data.length, real.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals("getBinaryStream is incorrect.", data[i], real[i]);
        }
    }

    /**
     * <p>
     * Test getBlob(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetBlob4() {
        try {
            testCRS.getBlob("BLOB_T");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBlob(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBlob5() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getBlob("id");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBlob(int).
     * </p>
     * <p>
     * Verify: getBlob is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBlob6() throws Exception {
        testCRS.absolute(2);
        Blob blob = testCRS.getBlob("blob_t");
        InputStream is = blob.getBinaryStream();

        byte[] data = getExpectedInputStreamData();

        byte real[] = new byte[data.length];
        is.read(real);

        assertEquals("getBinaryStream is incorrect.", data.length, real.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals("getBinaryStream is incorrect.", data[i], real[i]);
        }
    }

    /**
     * <p>
     * Test getBoolean(int).
     * </p>
     * <p>
     * Verify:When columnIndex is invalid, IllegalArgumentException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBoolean1() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getBoolean(0);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBoolean(int).
     * </p>
     * <p>
     * Verify:When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBoolean2() throws Exception {
        try {
            testCRS.getBoolean(3);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBoolean(int).
     * </p>
     * <p>
     * Verify:When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBoolean3() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getBoolean(3);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBoolean(int).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBoolean4() throws Exception {
        testCRS.absolute(3);
        try {
            testCRS.getBoolean(3);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBoolean(String).
     * </p>
     * <p>
     * Verify:When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBoolean5() throws Exception {
        try {
            testCRS.getBoolean("age");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBoolean(String).
     * </p>
     * <p>
     * Verify:When column does not exist, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBoolean6() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getBoolean("AGE");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBoolean(String).
     * </p>
     * <p>
     * Verify:When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBoolean7() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getBoolean("age");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBoolean(String).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBoolean8() throws Exception {
        testCRS.absolute(3);
        try {
            testCRS.getBoolean("age");
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getByte(int).
     * </p>
     * <p>
     * Verify: When columnIndex is invalid, IllegalArgumentException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetByte1() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getByte(0);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getByte(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetByte2() throws Exception {
        try {
            testCRS.getByte(1);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getByte(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetByte3() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getByte(4);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getByte(int).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetByte4() throws Exception {
        testCRS.absolute(3);
        try {
            testCRS.getByte(2);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getByte(int).
     * </p>
     * <p>
     * Verify: getByte is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetByte5() throws Exception {
        testCRS.absolute(1);
        assertEquals("getByte is incorrect.", new Byte(testCRS.getByte(1)), new Byte((byte) 50));
    }

    /**
     * <p>
     * Test getByte(String).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetByte6() throws Exception {
        try {
            testCRS.getByte("id");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getByte(String).
     * </p>
     * <p>
     * Verify: When column does not exist, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetByte7() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getByte("ID");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getByte(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetByte8() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getByte("blob_t");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getByte(String).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetByte9() throws Exception {
        testCRS.absolute(3);
        try {
            testCRS.getByte("blob_t");
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getByte(String).
     * </p>
     * <p>
     * Verify: getByte is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetByte10() throws Exception {
        testCRS.absolute(1);
        assertEquals("getByte is incorrect.", new Byte(testCRS.getByte("id")), new Byte((byte) 50));
    }

    /**
     * <p>
     * Test getBytes(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetBytes1() {
        try {
            testCRS.getBytes(4);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBytes(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBytes2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getBytes(1);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBytes(int).
     * </p>
     * <p>
     * Verify: getBytes is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBytes3() throws Exception {
        testCRS.absolute(2);
        byte[] real = testCRS.getBytes(4);
        byte[] data = getExpectedInputStreamData();

        assertEquals("getBytes is incorrect.", data.length, real.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals("getBytes is incorrect.", data[i], real[i]);
        }
    }

    /**
     * <p>
     * Test getBytes(String).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetBytes4() {
        try {
            testCRS.getBytes("BLOB_T");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBytes(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBytes5() throws Exception {
        testCRS.absolute(2);
        try {
            testCRS.getBytes("id");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getBytes(String).
     * </p>
     * <p>
     * Verify: getBytes is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetBytes6() throws Exception {
        testCRS.absolute(2);
        byte[] real = testCRS.getBytes("blob_t");
        byte[] data = getExpectedInputStreamData();

        assertEquals("getBytes is incorrect.", data.length, real.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals("getBytes is incorrect.", data[i], real[i]);
        }
    }

    /**
     * <p>
     * Test getCharacterStream(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetCharacterStream1() {
        try {
            testCRS.getCharacterStream(5);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getCharacterStream(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetCharacterStream2() throws Exception {
        testCRS.absolute(2);
        try {
            testCRS.getCharacterStream(4);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getCharacterStream(int).
     * </p>
     * <p>
     * Verify: getCharacterStream is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetCharacterStream3() throws Exception {
        testCRS.absolute(2);
        String temp = testCRS.getString(5);
        Reader reader = testCRS.getCharacterStream(5);
        char real[] = new char[temp.length()];
        reader.read(real);
        reader.close();
        byte[] data = getExpectedInputStreamData();
        assertEquals("convert is incorrect.", new String(real), new String(data));

    }

    /**
     * <p>
     * Test getCharacterStream(String).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetCharacterStream4() {
        try {
            testCRS.getCharacterStream("CLOB_T");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getCharacterStream(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetCharacterStream5() throws Exception {
        testCRS.absolute(2);
        try {
            testCRS.getCharacterStream("blob_t");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getCharacterStream(String).
     * </p>
     * <p>
     * Verify: getCharacterStream is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetCharacterStream6() throws Exception {
        testCRS.absolute(2);
        String temp = testCRS.getString(5);
        Reader reader = testCRS.getCharacterStream("clob_t");
        char real[] = new char[temp.length()];
        reader.read(real);
        reader.close();
        byte[] data = getExpectedInputStreamData();
        assertEquals("convert is incorrect.", new String(real), new String(data));

    }

    /**
     * <p>
     * Test getClob(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetClob1() {
        try {
            testCRS.getClob(5);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getClob(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetClob2() throws Exception {
        testCRS.absolute(2);
        try {
            testCRS.getClob(4);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getClob(int).
     * </p>
     * <p>
     * Verify: getClob is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetClob3() throws Exception {
        testCRS.absolute(2);
        String temp = testCRS.getString(5);
        Clob clob = testCRS.getClob(5);
        Reader reader = clob.getCharacterStream();
        char real[] = new char[temp.length()];
        reader.read(real);
        reader.close();
        byte[] data = getExpectedInputStreamData();
        assertEquals("convert is incorrect.", new String(real), new String(data));
    }

    /**
     * <p>
     * Test getClob(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetClob4() {
        try {
            testCRS.getClob("CLOB_T");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getClob(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetClob5() throws Exception {
        testCRS.absolute(2);
        try {
            testCRS.getClob("blob_t");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getClob(int).
     * </p>
     * <p>
     * Verify: getClob is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetClob6() throws Exception {
        testCRS.absolute(2);
        String temp = testCRS.getString(5);
        Clob clob = testCRS.getClob("clob_t");
        Reader reader = clob.getCharacterStream();
        char real[] = new char[temp.length()];
        reader.read(real);
        reader.close();
        byte[] data = getExpectedInputStreamData();
        assertEquals("convert is incorrect.", new String(real), new String(data));
    }

    /**
     * <p>
     * Test getDate(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDate1() throws Exception {
        try {
            testCRS.getDate(6);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDate(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDate2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getDate(4);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDate(int).
     * </p>
     * <p>
     * Verify:getDate is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDate3() throws Exception {
        testCRS.absolute(2);
        Date date = testCRS.getDate(6);
        assertEquals("getDate is incorrect.", date, Date.valueOf("2006-06-19"));
    }

    /**
     * <p>
     * Test getDate(String).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDate4() throws Exception {
        try {
            testCRS.getDate("date_t");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDate(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDate5() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getDate("blob_t");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDate(String).
     * </p>
     * <p>
     * Verify:getDate is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDate6() throws Exception {
        testCRS.absolute(2);
        Date date = testCRS.getDate("date_t");
        assertEquals("getDate is incorrect.", date, Date.valueOf("2006-06-19"));
    }

    /**
     * <p>
     * Test getDate(int, Calendar).
     * </p>
     * <p>
     * Verify: When columnIndex is invalid, IllegalArgumentException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDateCalendar1() throws Exception {
        testCRS.absolute(1);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getDate(0, calendar);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDate(int, Calendar).
     * </p>
     * <p>
     * Verify: When calendar is null, IllegalArgumentException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDateCalendar1_2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getDate(6, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDate(int, Calendar).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDateCalendar2() throws Exception {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getDate(6, calendar);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDate(int, Calendar).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDateCalendar3() throws Exception {
        testCRS.absolute(1);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getDate(4, calendar);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDate(int, Calendar).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDateCalendar4() throws Exception {
        testCRS.absolute(3);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getDate(6, calendar);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDate(int, Calendar).
     * </p>
     * <p>
     * Verify: getDate(int,Calendar) is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDateCalendar5() throws Exception {
        testCRS.absolute(2);
        GregorianCalendar calendar = new GregorianCalendar();
        Date date = testCRS.getDate(6, calendar);
        GregorianCalendar calendar2 = new GregorianCalendar();
        calendar2.setTime(Date.valueOf("2006-06-19"));
        Date expected = new Date(calendar2.getTimeInMillis());
        assertEquals("getDate(int,Calendar) is incorrect.", date, expected);
    }

    /**
     * <p>
     * Test getDate(String, Calendar).
     * </p>
     * <p>
     * Verify: When column does not exist, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDateCalendar6() throws Exception {
        testCRS.absolute(1);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getDate("DATE_T", calendar);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDate(String, Calendar).
     * </p>
     * <p>
     * Verify: When column does not exist, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDateCalendar6_2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getDate("date_t", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDate(String, Calendar).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDateCalendar7() throws Exception {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getDate("date_t", calendar);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDate(String, Calendar).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDateCalendar8() throws Exception {
        testCRS.absolute(1);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getDate("blob_t", calendar);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDate(String, Calendar).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDateCalendar9() throws Exception {
        testCRS.absolute(3);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getDate("date_t", calendar);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDate(String, Calendar).
     * </p>
     * <p>
     * Verify: getDate(int,Calendar) is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDateCalendar10() throws Exception {
        testCRS.absolute(2);
        GregorianCalendar calendar = new GregorianCalendar();
        Date date = testCRS.getDate("date_t", calendar);
        GregorianCalendar calendar2 = new GregorianCalendar();
        calendar2.setTime(Date.valueOf("2006-06-19"));
        Date expected = new Date(calendar2.getTimeInMillis());
        assertEquals("getDate(int,Calendar) is incorrect.", date, expected);
    }

    /**
     * <p>
     * Test getDouble(int).
     * </p>
     * <p>
     * Verify: When columnIndex is invalid, IllegalArgumentException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDouble1() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getDouble(0);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDouble(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDouble2() throws Exception {
        try {
            testCRS.getDouble(2);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDouble(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDouble3() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getDouble(4);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDouble(int).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDouble4() throws Exception {
        testCRS.absolute(3);
        try {
            testCRS.getDouble(2);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDouble(int).
     * </p>
     * <p>
     * Verify: getDouble is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDouble5() throws Exception {
        testCRS.absolute(2);
        assertEquals("getDouble is incorrect.", new Double(testCRS.getDouble(1)), new Double(100));
    }

    /**
     * <p>
     * Test getDouble(String).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDouble6() throws Exception {
        try {
            testCRS.getDouble("id");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDouble(String).
     * </p>
     * <p>
     * Verify: When column does not exist, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDouble7() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getDouble("ID");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDouble(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDouble8() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getDouble("blob_t");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDouble(String).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDouble9() throws Exception {
        testCRS.absolute(3);
        try {
            testCRS.getDouble("blob_t");
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getDouble(String).
     * </p>
     * <p>
     * Verify: getDouble is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetDouble10() throws Exception {
        testCRS.absolute(2);
        assertEquals("getDouble is incorrect.", new Double(testCRS.getDouble("id")), new Double(100));
    }

    /**
     * <p>
     * Test getFloat(int).
     * </p>
     * <p>
     * Verify: When columnIndex is invalid, IllegalArgumentException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetFloate1() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getFloat(0);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getFloat(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetFloate2() throws Exception {
        try {
            testCRS.getFloat(2);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getFloat(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetFloate3() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getFloat(4);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getFloat(int).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetFloate4() throws Exception {
        testCRS.absolute(3);
        try {
            testCRS.getFloat(2);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getFloat(int).
     * </p>
     * <p>
     * Verify: getFloat is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetFloate5() throws Exception {
        testCRS.absolute(2);
        assertEquals("getFloat is incorrect.", new Float(testCRS.getFloat(1)), new Float(100));
    }

    /**
     * <p>
     * Test getFloat(String).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetFloate6() throws Exception {
        try {
            testCRS.getFloat("id");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getFloat(String).
     * </p>
     * <p>
     * Verify: When column does not exist, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetFloate7() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getFloat("ID");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getFloat(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetFloate8() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getFloat("blob_t");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getFloat(String).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetFloate9() throws Exception {
        testCRS.absolute(3);
        try {
            testCRS.getFloat("blob_t");
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getFloat(String).
     * </p>
     * <p>
     * Verify: getFloat is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetFloat10() throws Exception {
        testCRS.absolute(2);
        assertEquals("getFloat is incorrect.", new Float(testCRS.getFloat("id")), new Float(100));
    }

    /**
     * <p>
     * Test getInt(int).
     * </p>
     * <p>
     * Verify: When columnIndex is invalid, IllegalArgumentException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetInt1() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getInt(0);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getInt(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetInt2() throws Exception {
        try {
            testCRS.getInt(2);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getInt(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetInt3() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getInt(4);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getInt(int).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetInt4() throws Exception {
        testCRS.absolute(3);
        try {
            testCRS.getInt(2);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getInt(int).
     * </p>
     * <p>
     * Verify: getInt is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetInt5() throws Exception {
        testCRS.absolute(2);
        assertEquals("getInt is incorrect.", new Integer(testCRS.getInt(1)), new Integer(100));
    }

    /**
     * <p>
     * Test getInt(String).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetInt6() throws Exception {
        try {
            testCRS.getInt("id");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getInt(String).
     * </p>
     * <p>
     * Verify: When column does not exist, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetInt7() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getInt("ID");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getInt(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetInt8() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getInt("blob_t");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getInt(String).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetInt9() throws Exception {
        testCRS.absolute(3);
        try {
            testCRS.getInt("blob_t");
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getInt(String).
     * </p>
     * <p>
     * Verify: getInt is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetInt10() throws Exception {
        testCRS.absolute(2);
        assertEquals("getInt is incorrect.", new Integer(testCRS.getInt("id")), new Integer(100));
    }

    /**
     * <p>
     * Test getLong(int).
     * </p>
     * <p>
     * Verify: When columnIndex is invalid, IllegalArgumentException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetLong1() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getLong(0);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getLong(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetLong2() throws Exception {
        try {
            testCRS.getLong(2);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getLong(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetLong3() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getLong(4);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getLong(int).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetLong4() throws Exception {
        testCRS.absolute(3);
        try {
            testCRS.getLong(2);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getLong(int).
     * </p>
     * <p>
     * Verify: getLong is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetLong5() throws Exception {
        testCRS.absolute(2);
        assertEquals("getLong is incorrect.", new Long(testCRS.getLong(1)), new Long(100));
    }

    /**
     * <p>
     * Test getLong(String).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetLong6() throws Exception {
        try {
            testCRS.getLong("id");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getLong(String).
     * </p>
     * <p>
     * Verify: When column does not exist, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetLong7() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getLong("ID");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getLong(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetLong8() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getLong("blob_t");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getLong(String).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetLong9() throws Exception {
        testCRS.absolute(3);
        try {
            testCRS.getLong("blob_t");
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getLong(String).
     * </p>
     * <p>
     * Verify: getLong is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetLong10() throws Exception {
        testCRS.absolute(2);
        assertEquals("getLong is incorrect.", new Long(testCRS.getLong("id")), new Long(100));
    }

    /**
     * <p>
     * Test getObject(int columnIndex).
     * </p>
     * <p>
     * Verify:When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetObject1() {
        try {
            testCRS.getObject(1);
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getObject(int columnIndex).
     * </p>
     * <p>
     * Verify:When columnIndex is invalid, null is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObject2() throws Exception {
        testCRS.absolute(1);
        assertNull("getObject is incorrect.", testCRS.getObject(0));
    }

    /**
     * <p>
     * Test getObject(int columnIndex).
     * </p>
     * <p>
     * Verify:getObject is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObject3() throws Exception {
        testCRS.absolute(1);
        assertEquals("getObject is incorrect.", testCRS.getObject(1), new BigDecimal(50));
    }

    /**
     * <p>
     * Test getObject(String columnName).
     * </p>
     * <p>
     * Verify:When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     */
    public void testGetObject4() {
        try {
            testCRS.getObject("ID");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getObject(String columnName).
     * </p>
     * <p>
     * Verify:When columnIndex is invalid, null is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObject5() throws Exception {
        testCRS.absolute(1);
        assertNull("getObject is incorrect.", testCRS.getObject("NotExist"));
    }

    /**
     * <p>
     * Test getObject(String columnName).
     * </p>
     * <p>
     * Verify:getObject is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObject6() throws Exception {
        testCRS.absolute(1);
        assertEquals("getObject is incorrect.", testCRS.getObject("id"), new BigDecimal(50));
    }

    /**
     * <p>
     * Test getRecordCount().
     * </p>
     * <p>
     * Verify: getRecordCount() is correct.
     * </p>
     */
    public void testGetRecordCount() {
        assertEquals("getRecordCount is incorrect.", testCRS.getRecordCount(), 3);
    }

    /**
     * <p>
     * Test getRef(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetRef1() throws Exception {
        try {
            testCRS.getRef(1);
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getRef(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetRef2() throws Exception {
        testCRS.absolute(2);
        try {
            testCRS.getRef(0);
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getRef(String).
     * </p>
     * <p>
     * Verify: When current state is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetRef3() throws Exception {
        try {
            testCRS.getRef("ID");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getRef(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetRef4() throws Exception {
        testCRS.absolute(2);
        try {
            testCRS.getRef("ID");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getRow().
     * </p>
     * <p>
     * Verify: getRow is correct.
     * </p>
     */
    public void testGetRow() {
        assertEquals("getRow is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("getRow is incorrect.", testCRS.getRow(), 1);
    }

    /**
     * <p>
     * Test getShort(int).
     * </p>
     * <p>
     * Verify: When columnIndex is invalid, IllegalArgumentException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetShort1() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getShort(0);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getShort(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetShort2() throws Exception {
        try {
            testCRS.getShort(2);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getShort(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetShort3() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getShort(4);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getShort(int).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetShort4() throws Exception {
        testCRS.absolute(3);
        try {
            testCRS.getShort(2);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getShort(int).
     * </p>
     * <p>
     * Verify: getShort is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetShort5() throws Exception {
        testCRS.absolute(2);
        assertEquals("getShort is incorrect.", new Short(testCRS.getShort(1)), new Short((short) 100));
    }

    /**
     * <p>
     * Test getShort(String).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetShort6() throws Exception {
        try {
            testCRS.getShort("id");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getShort(String).
     * </p>
     * <p>
     * Verify: When column does not exist, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetShort7() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getShort("ID");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getShort(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetShort8() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getShort("blob_t");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getShort(String).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetShort9() throws Exception {
        testCRS.absolute(3);
        try {
            testCRS.getShort("blob_t");
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getShort(String).
     * </p>
     * <p>
     * Verify: getShort is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetShort10() throws Exception {
        testCRS.absolute(2);
        assertEquals("getShort is incorrect.", new Short(testCRS.getShort("id")), new Short((short) 100));
    }

    /**
     * <p>
     * Test getString(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetString1() throws Exception {
        try {
            testCRS.getString(2);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getString(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetString2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getString(4);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getString(int).
     * </p>
     * <p>
     * Verify: getString is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetString3() throws Exception {
        testCRS.absolute(2);
        assertEquals("getString is incorrect.", testCRS.getString(2), "TEST");
    }

    /**
     * <p>
     * Test getString(String).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetString4() throws Exception {
        try {
            testCRS.getString("name");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getString(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetString5() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getString("blob_t");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getString(String).
     * </p>
     * <p>
     * Verify: getString is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetString6() throws Exception {
        testCRS.absolute(2);
        assertEquals("getString is incorrect.", testCRS.getString("name"), "TEST");
    }

    /**
     * <p>
     * Test getStruct(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetStruct1() throws Exception {
        testCRS.absolute(2);
        try {
            testCRS.getStruct(0);
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getStruct(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetStruct2() throws Exception {
        try {
            testCRS.getStruct(2);
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getStruct(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetStruct3() throws Exception {
        testCRS.absolute(2);
        try {
            testCRS.getStruct("date");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getStruct(String).
     * </p>
     * <p>
     * Verify: When current state is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetStruct4() throws Exception {
        try {
            testCRS.getStruct("ID");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTime1() throws Exception {
        try {
            testCRS.getTime(6);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTime2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getTime(4);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(int).
     * </p>
     * <p>
     * Verify: getTime is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnist.
     */
    public void testGetTime3() throws Exception {
        testCRS.absolute(2);
        assertEquals("getTime is incorrect.", testCRS.getTime(6), new Time(Date.valueOf("2006-06-19").getTime()));
    }

    /**
     * <p>
     * Test getTime(String).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTime4() throws Exception {
        try {
            testCRS.getTime("date_t");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTime5() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getTime("blob_t");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(String).
     * </p>
     * <p>
     * Verify: getTime is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnist.
     */
    public void testGetTime6() throws Exception {
        testCRS.absolute(2);
        assertEquals("getTime is incorrect.", testCRS.getTime("date_t"),
                new Time(Date.valueOf("2006-06-19").getTime()));
    }

    /**
     * <p>
     * Test getTime(int, Calendar).
     * </p>
     * <p>
     * Verify: When columnIndex is invalid, IllegalArgumentException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimeCalendar1() throws Exception {
        testCRS.absolute(1);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTime(0, calendar);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(int, Calendar).
     * </p>
     * <p>
     * Verify: When calendar is null, IllegalArgumentException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimeCalendar1_2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getTime(6, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(int, Calendar).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimeCalendar2() throws Exception {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTime(6, calendar);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(int, Calendar).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimeCalendar3() throws Exception {
        testCRS.absolute(1);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTime(4, calendar);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(int, Calendar).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimeCalendar4() throws Exception {
        testCRS.absolute(3);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTime(6, calendar);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(int,Calendar).
     * </p>
     * <p>
     * Verify: getTime is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnist.
     */
    public void testGetTimeCalendar5() throws Exception {
        testCRS.absolute(2);
        Calendar calendar = new GregorianCalendar();
        Time result = testCRS.getTime(6, calendar);
        Time expected = new Time(Date.valueOf("2006-06-19").getTime());
        Calendar calendar2 = new GregorianCalendar();
        calendar2.setTime(testCRS.getTime(6));
        expected = new Time(calendar2.getTimeInMillis());
        assertEquals("getTime is incorrect.", result, expected);
    }

    /**
     * <p>
     * Test getTime(String, Calendar).
     * </p>
     * <p>
     * Verify: When column does not exist, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimeCalendar6() throws Exception {
        testCRS.absolute(1);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTime("DATE_T", calendar);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(String, Calendar).
     * </p>
     * <p>
     * Verify: When column does not exist, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimeCalendar6_2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getTime("date_t", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(String, Calendar).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimeCalendar7() throws Exception {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTime("date_t", calendar);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(String, Calendar).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimeCalendar8() throws Exception {
        testCRS.absolute(1);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTime("blob_t", calendar);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(String, Calendar).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimeCalendar9() throws Exception {
        testCRS.absolute(3);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTime("date_t", calendar);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTime(String,Calendar).
     * </p>
     * <p>
     * Verify: getTime is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnist.
     */
    public void testGetTimeCalendar10() throws Exception {
        testCRS.absolute(2);
        Calendar calendar = new GregorianCalendar();
        Time result = testCRS.getTime("date_t", calendar);
        Time expected = new Time(Date.valueOf("2006-06-19").getTime());
        Calendar calendar2 = new GregorianCalendar();
        calendar2.setTime(expected);
        expected = new Time(calendar2.getTimeInMillis());
        assertEquals("getTimestamp is incorrect.", result, expected);
    }

    /**
     * <p>
     * Test getTimestamp(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimestamp1() throws Exception {
        try {
            testCRS.getTimestamp(6);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTimestamp(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimestamp2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getTimestamp(4);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTimestamp(int).
     * </p>
     * <p>
     * Verify: getTimestamp is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnist.
     */
    public void testGetTimestamp3() throws Exception {
        testCRS.absolute(2);
        assertEquals("getTimestamp is incorrect.", testCRS.getTimestamp(6), new Timestamp(Date.valueOf("2006-06-19")
                .getTime()));
    }

    /**
     * <p>
     * Test getTimestamp(String).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimestamp4() throws Exception {
        try {
            testCRS.getTimestamp("date_t");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTimestamp(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimestamp5() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getTimestamp("blob_t");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTimestamp(String).
     * </p>
     * <p>
     * Verify: getTimestamp is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnist.
     */
    public void testGetTimestamp6() throws Exception {
        testCRS.absolute(2);
        assertEquals("getTimestamp is incorrect.", testCRS.getTimestamp("date_t"),
                new Timestamp(Date.valueOf("2006-06-19").getTime()));
    }

    /**
     * <p>
     * Test getTimestamp(int, Calendar).
     * </p>
     * <p>
     * Verify: When columnIndex is invalid, IllegalArgumentException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimestampCalendar1() throws Exception {
        testCRS.absolute(1);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTimestamp(0, calendar);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTimestamp(int, Calendar).
     * </p>
     * <p>
     * Verify: When calendar is null, IllegalArgumentException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimestampCalendar1_2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getTimestamp(6, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTimestamp(int, Calendar).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimestampCalendar2() throws Exception {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTimestamp(6, calendar);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTimestamp(int, Calendar).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimestampCalendar3() throws Exception {
        testCRS.absolute(1);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTimestamp(4, calendar);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTimestamp(int, Calendar).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimestampCalendar4() throws Exception {
        testCRS.absolute(3);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTimestamp(6, calendar);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTimestamp(int,Calendar).
     * </p>
     * <p>
     * Verify: getTime is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnist.
     */
    public void testGetTimestampCalendar5() throws Exception {
        testCRS.absolute(2);
        Calendar calendar = new GregorianCalendar();
        Timestamp result = testCRS.getTimestamp(6, calendar);
        Timestamp expected = new Timestamp(Date.valueOf("2006-06-19").getTime());
        Calendar calendar2 = new GregorianCalendar();
        calendar2.setTime(expected);
        expected = new Timestamp(calendar2.getTimeInMillis());
        assertEquals("getTimestamp is incorrect.", result, expected);
    }

    /**
     * <p>
     * Test getTimestamp(String, Calendar).
     * </p>
     * <p>
     * Verify: When column does not exist, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimestampCalendar6() throws Exception {
        testCRS.absolute(1);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTimestamp("DATE_T", calendar);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTimestamp(String, Calendar).
     * </p>
     * <p>
     * Verify: When column does not exist, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimestampCalendar6_2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getTimestamp("date_t", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTimestamp(String, Calendar).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimestampCalendar7() throws Exception {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTimestamp("date_t", calendar);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTimestamp(String, Calendar).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimestampCalendar8() throws Exception {
        testCRS.absolute(1);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTimestamp("blob_t", calendar);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTimestamp(String, Calendar).
     * </p>
     * <p>
     * Verify:When null object is found, NullColumnValueException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetTimestampCalendar9() throws Exception {
        testCRS.absolute(3);
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            testCRS.getTimestamp("date_t", calendar);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getTimestamp(String,Calendar).
     * </p>
     * <p>
     * Verify: When Time Object retrieved is null, IllegalArgumentException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnist.
     */
    public void testGetTimestampCalendar10() throws Exception {
        testCRS.absolute(2);
        Calendar calendar = new GregorianCalendar();
        try {
            testCRS.getTimestamp(0, calendar);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getURL(int).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetURL1() throws Exception {
        try {
            testCRS.getURL(7);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getURL(int).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetURL2() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getURL(4);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getURL(int).
     * </p>
     * <p>
     * Verify: getURL is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetURL3() throws Exception {
        testCRS.absolute(2);
        assertEquals("getURL is incorrect.", testCRS.getURL(7), new URL("http://test.com"));
    }

    /**
     * <p>
     * Test getURL(String).
     * </p>
     * <p>
     * Verify: When current row is invalid, InvalidCursorStateException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetURL4() throws Exception {
        try {
            testCRS.getURL("url");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getURL(String).
     * </p>
     * <p>
     * Verify: When value can't be converted, ClassCastException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetURL5() throws Exception {
        testCRS.absolute(1);
        try {
            testCRS.getURL("blob_t");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getURL(URL).
     * </p>
     * <p>
     * Verify: getURL is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetURL6() throws Exception {
        testCRS.absolute(2);
        assertEquals("getURL is incorrect.", testCRS.getURL("url"), new URL("http://test.com"));
    }

    /**
     * <p>
     * Test isAfterLast().
     * </p>
     * <p>
     * Verify: isAfterLast is correct.
     * </p>
     */
    public void testIsAfterLast() {
        assertFalse("isAfterLast is incorrect.", testCRS.isAfterLast());
        testCRS.afterLast();
        assertTrue("isAfterLast is incorrect.", testCRS.isAfterLast());
    }

    /**
     * <p>
     * Test isBeforeFirst().
     * </p>
     * <p>
     * Verify: isBeforeFirst is correct.
     * </p>
     */
    public void testIsBeforeFirst() {
        assertTrue("isBeforeFirst is incorrect.", testCRS.isBeforeFirst());
        testCRS.absolute(1);
        assertFalse("isBeforeFirst is incorrect.", testCRS.isBeforeFirst());

    }

    /**
     * <p>
     * Test isFirst().
     * </p>
     * <p>
     * Verify: isFirst is correct.
     * </p>
     */
    public void testIsFirst() {
        assertFalse("isFirst is incorrect.", testCRS.isFirst());
        testCRS.absolute(1);
        assertTrue("isFirst is incorrect.", testCRS.isFirst());

    }

    /**
     * <p>
     * Test isLast().
     * </p>
     * <p>
     * Verify: isLast is correct.
     * </p>
     */
    public void testIsLast() {
        assertFalse("isLast is incorrect.", testCRS.isLast());
        testCRS.absolute(3);
        assertTrue("isLast is incorrect.", testCRS.isLast());

    }

    /**
     * <p>
     * Test last().
     * </p>
     * <p>
     * Verify: last is correct.
     * </p>
     */
    public void testLast() {
        assertTrue("last is incorrect.", testCRS.last());
        assertTrue("last is incorrect.", testCRS.isLast());
    }

    /**
     * <p>
     * Test next().
     * </p>
     * <p>
     * Verify: next is correct.
     * </p>
     */
    public void testNext() {
        assertTrue("next is incorrect.", testCRS.next());
        assertEquals("next is incorrect.", testCRS.getRow(), 1);
        assertTrue("next is incorrect.", testCRS.next());
        assertEquals("next is incorrect.", testCRS.getRow(), 2);
        assertTrue("next is incorrect.", testCRS.next());
        assertEquals("next is incorrect.", testCRS.getRow(), 3);
        assertFalse("next is incorrect.", testCRS.next());
    }

    /**
     * <p>
     * Test previous().
     * </p>
     * <p>
     * Verify: previous is correct.
     * </p>
     */
    public void testPrevious() {
        testCRS.afterLast();
        assertTrue("previous is incorrect.", testCRS.previous());
        assertEquals("previous is incorrect.", testCRS.getRow(), 3);
        assertTrue("previous is incorrect.", testCRS.previous());
        assertEquals("previous is incorrect.", testCRS.getRow(), 2);
        assertTrue("previous is incorrect.", testCRS.previous());
        assertEquals("previous is incorrect.", testCRS.getRow(), 1);
        assertFalse("previous is incorrect.", testCRS.previous());
    }

    /**
     * <p>
     * Test relative().
     * </p>
     * <p>
     * Verify: relative is correct.
     * </p>
     */
    public void testRelative() {
        testCRS.absolute(1);
        assertTrue("relative is incorrect.", testCRS.relative(1));
        assertEquals("relative is incorrect.", testCRS.getRow(), 2);
        assertTrue("relative is incorrect.", testCRS.relative(-1));
        assertEquals("relative is incorrect.", testCRS.getRow(), 1);
        assertFalse("relative is incorrect.", testCRS.relative(3));
    }

    /**
     * <p>
     * Test remap(Mapper).
     * </p>
     * <p>
     * Verify: remap() is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testRemap() throws Exception {
        testCRS.absolute(1);
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter1());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);
        // retrieve mapped value.
        assertEquals("remap() is incorrect.", new Long(testCRS.getLong(1)), new Long(49));
    }

    /**
     * <p>
     * Test remap(Mapper).
     * </p>
     * <p>
     * Verify: remap() is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testRemap2() throws Exception {
        testCRS.remap(null);
        // good
    }

    /**
     * <p>
     * Test remap(Mapper).
     * </p>
     * <p>
     * Verify: remap() is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testRemap3() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("varchar", new MockConverter1());
        Mapper mapper = new Mapper(map);
        try {
            testCRS.remap(mapper);
            fail("IllegalMappingException expected.");
        } catch (IllegalMappingException e) {
            // good
        }
    }

    /**
     * <p>
     * Test sortAscending(int).
     * </p>
     * <p>
     * Verify; sortAscending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortAscending1() throws Exception {
        testCRS.sortAscending(1);
        assertEquals("sortAscending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortAscending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(50));
    }

    /**
     * <p>
     * Test sortAscending(String).
     * </p>
     * <p>
     * Verify; sortAscending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortAscending2() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter2());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);
        testCRS.sortAscending("id");
        assertEquals("sortAscending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortAscending() is incorrect.", new Long(testCRS.getLong(1)), new Long(-150));

    }

    /**
     * <p>
     * Test sortAscending(int, Comparator).
     * </p>
     * <p>
     * Verify; sortAscending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortAscending3() throws Exception {
        testCRS.sortAscending(1, null);
        assertEquals("sortAscending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortAscending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(50));
    }

    /**
     * <p>
     * Test sortAscending(String, Comparator).
     * </p>
     * <p>
     * Verify; sortAscending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortAscending4() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter2());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);

        testCRS.sortAscending("id", null);
        assertEquals("sortAscending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortAscending() is incorrect.", new Long(testCRS.getLong(1)), new Long(-150));
        testCRS.sortAscending("id", null);

    }

    /**
     * <p>
     * Test sortAscending(int[]).
     * </p>
     * <p>
     * Verify; sortAscending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortAscending5() throws Exception {
        testCRS.sortAscending(new int[] { 1, 2 });
        assertEquals("sortAscending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortAscending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(50));
        testCRS.absolute(2);
        assertEquals("sortAscending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(100));

    }

    /**
     * <p>
     * Test sortAscending(String[]).
     * </p>
     * <p>
     * Verify; sortAscending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortAscending6() throws Exception {
        testCRS.sortAscending(new String[] { "name", "id" });
        assertEquals("sortAscending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortAscending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(100));
        testCRS.next();
        assertEquals("sortAscending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(50));

    }

    /**
     * <p>
     * Test sortAscending(int[],Comparator[]).
     * </p>
     * <p>
     * Verify; sortAscending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortAscending7() throws Exception {
        testCRS.sortAscending(new int[] { 1, 2 }, null);
        assertEquals("sortAscending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortAscending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(50));
        testCRS.next();
        assertEquals("sortAscending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(100));
    }

    /**
     * <p>
     * Test sortAscending(String[],Comparator[]).
     * </p>
     * <p>
     * Verify; sortAscending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortAscending8() throws Exception {
        testCRS.sortAscending(new String[] { "name", "id" }, null);
        assertEquals("sortAscending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortAscending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(100));
        testCRS.next();
        assertEquals("sortAscending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(50));
    }

    /**
     * <p>
     * Test sortDescending(int).
     * </p>
     * <p>
     * Verify; sortDescending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortDescending1() throws Exception {
        testCRS.sortDescending(1);
        assertEquals("sortDescending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortDescending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(150));
    }

    /**
     * <p>
     * Test sortDescending(String).
     * </p>
     * <p>
     * Verify; sortDescending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortDescending2() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter2());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);
        testCRS.sortDescending("id");
        assertEquals("sortDescending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortDescending() is incorrect.", new Long(testCRS.getLong(1)), new Long(-50));

    }

    /**
     * <p>
     * Test sortDescending(int, Comparator).
     * </p>
     * <p>
     * Verify; sortDescending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortDescending3() throws Exception {
        testCRS.sortDescending(1, null);
        assertEquals("sortDescending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortDescending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(150));

    }

    /**
     * <p>
     * Test sortDescending(String, Comparator).
     * </p>
     * <p>
     * Verify; sortDescending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortDescending4() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter2());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);

        testCRS.sortDescending("id", null);
        assertEquals("sortDescending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortDescending() is incorrect.", new Long(testCRS.getLong(1)), new Long(-50));

    }

    /**
     * <p>
     * Test sortDescending(int[]).
     * </p>
     * <p>
     * Verify; sortDescending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortDescending5() throws Exception {
        testCRS.sortDescending(new int[] { 1, 2 });
        assertEquals("sortDescending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortDescending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(150));
        testCRS.next();
        assertEquals("sortDescending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(100));
        testCRS.next();
        assertEquals("sortDescending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(50));
    }

    /**
     * <p>
     * Test sortDescending(String[]).
     * </p>
     * <p>
     * Verify; sortDescending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortDescending6() throws Exception {
        testCRS.sortDescending(new String[] { "NAME", "ID" });
        assertEquals("sortDescending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortDescending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(50));
        testCRS.next();
        assertEquals("sortDescending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(100));
    }

    /**
     * <p>
     * Test sortDescending(int[],Comparator[]).
     * </p>
     * <p>
     * Verify; sortDescending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortDescending7() throws Exception {
        testCRS.sortDescending(new int[] { 1, 2 }, null);
        assertEquals("sortDescending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(2);
        assertEquals("sortDescending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(100));
        testCRS.next();
        assertEquals("sortDescending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(50));
    }

    /**
     * <p>
     * Test sortDescending(String[],Comparator[]).
     * </p>
     * <p>
     * Verify; sortDescending is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testSortDescending8() throws Exception {
        testCRS.sortDescending(new String[] { "NAME", "ID" }, null);
        assertEquals("sortDescending() is incorrect.", testCRS.getRow(), 0);
        testCRS.absolute(1);
        assertEquals("sortDescending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(50));
        testCRS.absolute(2);
        assertEquals("sortDescending() is incorrect.", testCRS.getBigDecimal(1), new BigDecimal(100));

    }

    /**
     * <p>
     * Test getObject(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is null, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectClass1() throws Exception {
        try {
            testCRS.getObject(1, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getObject(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is null, InvalidCursorStateException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectClass2() throws Exception {
        try {
            testCRS.getObject(1, Long.class);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getObject(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:When columnIndex is invalid,null is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectClass3() throws Exception {
        testCRS.absolute(1);
        assertNull("getObject(int,Class) is incorrect.", testCRS.getObject(0, Long.class));
    }

    /**
     * <p>
     * Test getObject(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:getObject is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectClass4() throws Exception {
        testCRS.absolute(1);
        assertEquals("getObject(int,Class) is incorrect.", testCRS.getObject(1, BigDecimal.class), new BigDecimal(50));
    }

    /**
     * <p>
     * Test getObject(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:getObject is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectClass5() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter1());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);

        testCRS.absolute(1);
        assertEquals("getObject(int,Class) is incorrect.", testCRS.getObject(1, Long.class), new Long(49));
    }

    /**
     * <p>
     * Test getObject(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:getObject is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectClass6() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("number", new MockConverter1());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);

        testCRS.absolute(1);
        assertEquals("getObject(int,Class) is incorrect.", testCRS.getObject(1, Byte.class), new Byte((byte) 50));
    }

    /**
     * <p>
     * Test getObject(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:getObject is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectClass7() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter3());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);

        testCRS.absolute(1);
        assertEquals("getObject(int,Class) is incorrect.",
                testCRS.getObject(1, Date.class), Date.valueOf("2006-09-01"));
    }

    /**
     * <p>
     * Test getObject(String columnName, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is null, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectStringClass1() throws Exception {
        try {
            testCRS.getObject("ID", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getObject(String columnName, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is null, InvalidCursorStateException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectStringClass2() throws Exception {
        try {
            testCRS.getObject("ID", Long.class);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getObject(String columnName, Class desiredType).
     * </p>
     * <p>
     * Verify:When columnIndex is invalid,null is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectStringClass3() throws Exception {
        testCRS.absolute(1);
        assertNull("getObject(String,Class) is incorrect.", testCRS.getObject("NotExist", Long.class));
    }

    /**
     * <p>
     * Test getObject(String columnName, Class desiredType).
     * </p>
     * <p>
     * Verify:getObject is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectStringClass4() throws Exception {
        testCRS.absolute(1);
        assertEquals("getObject(String,Class) is incorrect.", testCRS.getObject("id", BigDecimal.class),
                new BigDecimal(50));
    }

    /**
     * <p>
     * Test getObject(String columnName, Class desiredType).
     * </p>
     * <p>
     * Verify:getObject is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectStringClass5() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter1());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);

        testCRS.absolute(1);
        assertEquals("getObject(String,Class) is incorrect.", testCRS.getObject("id", Long.class), new Long(49));
    }

    /**
     * <p>
     * Test getObject(String columnName, Class desiredType).
     * </p>
     * <p>
     * Verify:getObject is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectStringClass6() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter1());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);

        testCRS.absolute(1);
        assertEquals("getObject(String,Class) is incorrect.", testCRS.getObject("id", Byte.class), new Byte((byte) 50));
    }

    /**
     * <p>
     * Test getObject(String columnName, Class desiredType).
     * </p>
     * <p>
     * Verify:getObject is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectStringClass7() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter3());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);

        testCRS.absolute(1);
        assertEquals("getObject(String,Class) is incorrect.", testCRS.getObject("id", Date.class),
                Date.valueOf("2006-09-01"));
    }

    /**
     * <p>
     * Test isAvailable(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is null, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAvailableClass1() throws Exception {
        try {
            testCRS.isAvailable(1, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test isAvailable(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is null, InvalidCursorStateException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAvailableClass2() throws Exception {
        try {
            testCRS.isAvailable(1, Long.class);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test isAvailable(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:When columnIndex is invalid,null is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAvailableClass3() throws Exception {
        testCRS.absolute(1);
        assertFalse("IsAvailable(int,Class) is incorrect.", testCRS.isAvailable(0, Long.class));
    }

    /**
     * <p>
     * Test IsAvailable(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:IsAvailable is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAvailableClass4() throws Exception {
        testCRS.absolute(1);
        assertTrue("IsAvailable(int,Class) is incorrect.", testCRS.isAvailable(1, BigDecimal.class));
    }

    /**
     * <p>
     * Test isAvailable(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:IsAvailable is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAvailableClass5() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("number", new MockConverter1());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);

        testCRS.absolute(1);
        assertTrue("IsAvailable(int,Class) is incorrect.", testCRS.isAvailable(1, Long.class));
    }

    /**
     * <p>
     * Test isAvailable(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:IsAvailable is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAvailableClass6() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("number", new MockConverter1());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);

        testCRS.absolute(1);
        assertTrue("IsAvailable(int,Class) is incorrect.", testCRS.isAvailable(1, Byte.class));
    }

    /**
     * <p>
     * Test isAvailable(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:IsAvailable is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAvailableClass7() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter3());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);

        testCRS.absolute(1);
        assertTrue("IsAvailable(int,Class) is incorrect.", testCRS.isAvailable(1, Date.class));
    }

    /**
     * <p>
     * Test isAvailable(String columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is null, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAvailableStringClass1() throws Exception {
        try {
            testCRS.isAvailable("ID", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test isAvailable(String columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is null, InvalidCursorStateException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAvailableStringClass2() throws Exception {
        try {
            testCRS.isAvailable("ID", Long.class);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test isAvailable(String columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:When columnIndex is invalid,null is returned.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAvailableStringClass3() throws Exception {
        testCRS.absolute(1);
        assertFalse("IsAvailable(String,Class) is incorrect.", testCRS.isAvailable("NotExist", Long.class));
    }

    /**
     * <p>
     * Test IsAvailable(String columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:IsAvailable is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAvailableStringClass4() throws Exception {
        testCRS.absolute(1);
        assertTrue("IsAvailable(String,Class) is incorrect.", testCRS.isAvailable("id", BigDecimal.class));
    }

    /**
     * <p>
     * Test isAvailable(String columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:IsAvailable is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAvailableStringClass5() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter1());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);

        testCRS.absolute(1);
        assertTrue("IsAvailable(String,Class) is incorrect.", testCRS.isAvailable("id", Long.class));
    }

    /**
     * <p>
     * Test isAvailable(int columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:IsAvailable is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAvailableStringClass6() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter1());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);

        testCRS.absolute(1);
        assertTrue("IsAvailable(String,Class) is incorrect.", testCRS.isAvailable("id", Byte.class));
    }

    /**
     * <p>
     * Test isAvailable(String columnIndex, Class desiredType).
     * </p>
     * <p>
     * Verify:IsAvailable is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testIsAvailableStringClass7() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter3());
        Mapper mapper = new Mapper(map);
        testCRS.remap(mapper);

        testCRS.absolute(1);
        assertTrue("IsAvailable(String,Class) is incorrect.", testCRS.isAvailable("id", Date.class));
    }

    /**
     * Get expected value of Blob and Clob column in test case.The value is read
     * from file tempFile.txt located in test_files directory.
     *
     * @return expected value.
     * @throws IOException
     *             If error occurs while reading file.
     */
    private byte[] getExpectedInputStreamData() throws IOException {
        File file = new File(UnitTestHelper.INPUT_FILE_DIR + "tempFile.txt");
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            int length = (int) file.length();
            byte[] data = new byte[length];
            // file size is small.
            fin.read(data);
            fin.close();
            return data;
        } finally {
            if (fin != null) {
                fin.close();
            }
        }
    }

}

/**
 * Mocked class implementing interface Converter.
 *
 * @author suhugo
 * @version 1.1
 */
class MockConverter1 implements Converter {
    /**
     * Convert value to Long if it is a BigDecimal, and subtract the value by 1.
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
    public Object convert(Object value, int column, CustomResultSetMetaData metaData) throws IllegalMappingException {
        if (value.getClass().equals(BigDecimal.class)) {
            return new Long(((BigDecimal) value).longValue() - 1);
        } else {
            throw new IllegalMappingException("illegalException.");
        }
    }
}

/**
 * Mocked class implementing interface Converter.
 *
 * @author suhugo
 * @version 1.1
 */
class MockConverter2 implements Converter {
    /**
     * Convert value to Long if it is a BigDecimal, reverse it value.
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
    public Object convert(Object value, int column, CustomResultSetMetaData metaData) throws IllegalMappingException {
        if (value.getClass().equals(BigDecimal.class)) {
            return new Long(-((BigDecimal) value).longValue());
        } else {
            throw new IllegalMappingException("illegalException.");
        }
    }
}

/**
 * Mocked class implementing interface Converter.
 *
 * @author suhugo
 * @version 1.1
 */
class MockConverter3 implements Converter {
    /**
     * Convert value to Date &quot;2006-9-1&quot; if it is a BigDecimal.
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
    public Object convert(Object value, int column, CustomResultSetMetaData metaData) throws IllegalMappingException {
        if (value.getClass().equals(BigDecimal.class)) {
            return Date.valueOf("2006-09-01");
        } else {
            throw new IllegalMappingException("illegalException.");
        }
    }
}