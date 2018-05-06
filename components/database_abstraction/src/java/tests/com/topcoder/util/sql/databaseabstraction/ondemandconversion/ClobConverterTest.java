/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.ondemandconversion;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;

import junit.framework.TestCase;

import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;
import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.UnitTestHelper;

/**
 * Test case for ClobConverter.
 *
 * @author justforplay
 * @version 1.1
 * @since 1.1
 */
public class ClobConverterTest extends TestCase {

    /**
     * Instance of CustomResultSetMetaData used in this unit test case as
     * parameter to call canConvert and convert methods.
     */
    private static final CustomResultSetMetaData TEST_CRSMD = UnitTestHelper.getCustomRMD();

    /**
     * Instance of ClobConverter used in this unit test case.
     */
    private ClobConverter converter = new ClobConverter();

    /**
     * Instance of Clob used in this unit test case as value parameter.
     */
    private Clob testValue;

    /**
     * Database connection.
     */
    private Connection conn;

    /**
     * Initialize instance of Clob.This will get database connection, which is
     * not closed here.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void setUp() throws Exception {
        conn = UnitTestHelper.getDatabaseConnection();
        testValue = UnitTestHelper.getClobObj(conn);
    }

    /**
     * Close connection.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void tearDown() throws Exception {
        UnitTestHelper.closeConnection(conn);
    }

    /**
     * <p>
     * Test ClobConverter().
     * </p>
     * <p>
     * Verify:ClobConverter can be instantiated correctly.
     * </p>
     */
    public void testClobConverter() {
        assertNotNull("Unable to instantiate ClobConverter.", converter);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When metaData is null, IllegalArgumentException is thrown.
     * </p>
     */
    public void testCanConvert1() {
        try {
            converter.canConvert(testValue, 1, null, String.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is null, IllegalArgumentException is thrown.
     * </p>
     */
    public void testCanConvert2() {
        try {
            converter.canConvert(testValue, 1, TEST_CRSMD, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When column<=0, IllegalArgumentException is thrown.
     * </p>
     */
    public void testCanConvert3() {
        try {
            converter.canConvert(testValue, 0, TEST_CRSMD, String.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When column<=0, IllegalArgumentException is thrown.
     * </p>
     */
    public void testCanConvert4() {
        try {
            converter.canConvert(testValue, -1, TEST_CRSMD, String.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When column>column count of metaData, IllegalArgumentException is
     * thrown.
     * </p>
     */
    public void testCanConvert5() {
        try {
            converter.canConvert(testValue, TEST_CRSMD.getColumnCount() + 1, TEST_CRSMD, String.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When value is null, false is returned.
     * </p>
     */
    public void testCanConvert6() {
        assertEquals("canConvert is incorrect.", converter.canConvert(null, 1, TEST_CRSMD, String.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When value is not a Clob, false is returned.
     * </p>
     */
    public void testCanConvert7() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(new String("Not Clob"), 1, TEST_CRSMD, String.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Date, false is returned.
     * </p>
     */
    public void testCanConvert8() {
        assertEquals("canConvert is incorrect.", converter.canConvert(testValue, 1, TEST_CRSMD, Date.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is InputStream, true is returned.
     * </p>
     */
    public void testCanConvert9() {
        assertEquals("canConvert is incorrect.", converter.canConvert(testValue, 1, TEST_CRSMD, InputStream.class),
                true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Reader, true is returned.
     * </p>
     */
    public void testCanConvert10() {
        assertEquals("canConvert is incorrect.", converter.canConvert(testValue, 1, TEST_CRSMD, Reader.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is String, true is returned.
     * </p>
     */
    public void testCanConvert11() {
        assertEquals("canConvert is incorrect.", converter.canConvert(testValue, 1, TEST_CRSMD, String.class), true);
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When metaData is null, IllegalArgumentException is thrown.
     * </p>
     */
    public void testConvert1() {
        try {
            converter.canConvert(testValue, 1, null, String.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is null, IllegalArgumentException is thrown.
     * </p>
     */
    public void testConvert2() {
        try {
            converter.canConvert(testValue, 1, TEST_CRSMD, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When column<=0, IllegalArgumentException is thrown.
     * </p>
     */
    public void testConvert3() {
        try {
            converter.canConvert(testValue, 0, TEST_CRSMD, String.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When column<=0, IllegalArgumentException is thrown.
     * </p>
     */
    public void testConvert4() {
        try {
            converter.canConvert(testValue, -1, TEST_CRSMD, String.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When column>column count of metaData, IllegalArgumentException is
     * thrown.
     * </p>
     */
    public void testConvert5() {
        try {
            converter.canConvert(testValue, TEST_CRSMD.getColumnCount() + 1, TEST_CRSMD, String.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When value is null, IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert6() {
        try {
            converter.convert(null, 1, TEST_CRSMD, String.class);
            fail("IllegalMappingException expected");
        } catch (IllegalMappingException e) {
            // good
        }
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When value is not a Blob, IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert7() {
        try {
            converter.convert(new String("Not Blob"), 1, TEST_CRSMD, String.class);
            fail("IllegalMappingException expected");
        } catch (IllegalMappingException e) {
            // good
        }
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Date, IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert8() {
        try {
            converter.convert(testValue, 1, TEST_CRSMD, Date.class);
            fail("IllegalMappingException expected");
        } catch (IllegalMappingException e) {
            // good
        }
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is String, IllegalMappingException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert9() throws Exception {
        Object obj = converter.convert(testValue, 1, TEST_CRSMD, String.class);
        assertTrue("convert is incorrect.", obj instanceof String);
        File file = new File(UnitTestHelper.INPUT_FILE_DIR + "tempFile.txt");
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            int length = (int) file.length();
            byte[] data = new byte[length];
            // file size is small.
            fin.read(data);
            String temp = new String(data);
            assertEquals("convert is incorrect.", obj, temp);
        } finally {
            if (fin != null) {
                fin.close();
            }
        }

    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is InputStream, IllegalMappingException is
     * thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert10() throws Exception {
        Object objTemp = converter.convert(testValue, 1, TEST_CRSMD, String.class);
        Object obj = converter.convert(testValue, 1, TEST_CRSMD, InputStream.class);
        assertTrue("convert is incorrect.", obj instanceof InputStream);
        InputStream is = (InputStream) obj;
        byte real[] = new byte[((String) objTemp).length()];
        is.read(real);
        File file = new File(UnitTestHelper.INPUT_FILE_DIR + "tempFile.txt");
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            int length = (int) file.length();
            byte[] data = new byte[length];
            // file size is small.
            fin.read(data);
            fin.close();
            assertEquals("convert is incorrect.", data.length, real.length);
            for (int i = 0; i < data.length; i++) {
                assertEquals("convert is incorrect.", data[i], real[i]);
            }
        } finally {
            if (fin != null) {
                fin.close();
            }
        }

    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Reader, IllegalMappingException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert11() throws Exception {
        Object objTemp = converter.convert(testValue, 1, TEST_CRSMD, String.class);
        Object obj = converter.convert(testValue, 1, TEST_CRSMD, Reader.class);
        assertTrue("convert is incorrect.", obj instanceof Reader);
        Reader reader = (Reader) obj;
        char real[] = new char[((String) objTemp).length()];
        reader.read(real);
        reader.close();
        File file = new File(UnitTestHelper.INPUT_FILE_DIR + "tempFile.txt");
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            int length = (int) file.length();
            byte[] data = new byte[length];
            // file size is small.
            fin.read(data);
            fin.close();
            assertEquals("convert is incorrect.", new String(real), new String(data));
        } finally {
            if (fin != null) {
                fin.close();
            }
        }
    }

}
