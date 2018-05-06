/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.ondemandconversion;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;

import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;
import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.UnitTestHelper;

/**
 * Test case for {@link StringConverter}.
 *
 * @author justforplay, suhugo
 * @version 2.0
 * @since 1.1
 */
public class StringConverterTest extends TestCase {

    /**
     * Instance of CustomResultSetMetaData used in this unit test case as
     * parameter to call canConvert and convert methods.
     */
    private static final CustomResultSetMetaData TEST_CRSMD = UnitTestHelper.getCustomRMD();

    /**
     * Instance of String used in this unit test case as value parameter.
     */
    private static final String TEST_VALUE = new String("1000");

    /**
     * Instance of StringConverter used in this unit test case.
     */
    private StringConverter converter = new StringConverter();

    /**
     * <p>
     * Test StringConverter().
     * </p>
     * <p>
     * Verify:StringConverter can be instantiated correctly.
     * </p>
     */
    public void testStringConverter() {
        assertNotNull("Unable to instantiate StringConverter.", converter);
    }

    /**
     * <p>
     * Test StringConverter(DateFormat dateFormat).
     * </p>
     * <p>
     * Verify:when dateFormat is null,IllegalArgumentException is thrown.
     * </p>
     */
    public void testStringConverter2() {
        try {
            new StringConverter(null);
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
     * Verify:When metaData is null, IllegalArgumentException is thrown.
     * </p>
     */
    public void testCanConvert1() {
        try {
            converter.canConvert(TEST_VALUE, 1, null, Long.class);
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
            converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, null);
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
            converter.canConvert(TEST_VALUE, 0, TEST_CRSMD, Long.class);
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
            converter.canConvert(TEST_VALUE, -1, TEST_CRSMD, Long.class);
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
            converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount() + 1, TEST_CRSMD, Long.class);
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
        assertEquals("canConvert is incorrect.", converter.canConvert(null, 1, TEST_CRSMD, Long.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When value is not a Sting, true is returned.
     * </p>
     */
    public void testCanConvert7() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(new Long(1000), 1, TEST_CRSMD, Long.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is String, false is returned.
     * </p>
     */
    public void testCanConvert8() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, String.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is InputStream and value contains no character
     * invalid, true is returned.
     * </p>
     */
    public void testCanConvert9() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, InputStream.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is InputStream and value contains character less
     * than 0, true is returned.
     * </p>
     */
    public void testCanConvert10() {
        String invalidStr = new String(new char[] { 'a', (char) -32767, (char) -1, 'b', (char) 128, (char) 1024,
            (char) 32767 });
        assertEquals("canConvert is incorrect.",
                converter.canConvert(invalidStr, TEST_CRSMD.getColumnCount(), TEST_CRSMD, InputStream.class), false);
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
    public void testCanConvert11() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, Reader.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Byte, and value can be parsed as Byte, true is
     * returned.
     * </p>
     */
    public void testCanConvert12() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(new String("100"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, Byte.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Byte, and value cann't be parsed as Byte, true
     * is returned.
     * </p>
     */
    public void testCanConvert12_1() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(new String("wrong"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, Byte.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Short, and value can be parsed as Short, true
     * is returned.
     * </p>
     */
    public void testCanConvert13() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, Short.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Short, and value cann't be parsed as Short,
     * true is returned.
     * </p>
     */
    public void testCanConvert13_1() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(new String("wrong"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, Short.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Integer, and value can be parsed as Integer,
     * true is returned.
     * </p>
     */
    public void testCanConvert14() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, Integer.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Integer, and value cann't be parsed as
     * Integer, true is returned.
     * </p>
     */
    public void testCanConvert14_1() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(new String("wrong"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, Integer.class),
                false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Long, and value can be parsed as Long, true is
     * returned.
     * </p>
     */
    public void testCanConvert15() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, Long.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Long, and value cann't be parsed as Long, true
     * is returned.
     * </p>
     */
    public void testCanConvert15_1() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(new String("wrong"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, Long.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Float, and value can be parsed as Float, true
     * is returned.
     * </p>
     */
    public void testCanConvert16() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, Float.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Float, and value cann't be parsed as Float,
     * true is returned.
     * </p>
     */
    public void testCanConvert16_1() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(new String("wrong"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, Float.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Double, and value can be parsed as Double,
     * true is returned.
     * </p>
     */
    public void testCanConvert17() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, Double.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Double, and value cann't be parsed as Double,
     * true is returned.
     * </p>
     */
    public void testCanConvert17_1() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(
                        new String("wrong"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, Double.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is BigDecimal, and value can be parsed as
     * BigDecimal, true is returned.
     * </p>
     */
    public void testCanConvert18() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, BigDecimal.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is BigDecimal, and value cann't be parsed as
     * BigDecimal, true is returned.
     * </p>
     */
    public void testCanConvert18_1() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(new String("wrong"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, BigDecimal.class),
                false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Date, and value can be parsed as Date, true is
     * returned.
     * </p>
     */
    public void testCanConvert19() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert("2006-06-19", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Date.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Date, and value cann't be parsed as Date, true
     * is returned.
     * </p>
     */
    public void testCanConvert19_1() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert("2006:06:19 e", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Date.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Date, and value cann't be parsed as Date, true
     * is returned.
     * </p>
     */
    public void testCanConvert19_2() {
        SimpleDateFormat formatter = new SimpleDateFormat("yy:mm:dd 'e'");
        StringConverter converter2 = new StringConverter(formatter);
        assertEquals("canConvert is incorrect.",
                converter2.canConvert("2006:06:19 e", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Date.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Time, and value can be parsed as Time, true is
     * returned.
     * </p>
     */
    public void testCanConvert20() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert("11:03:00", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Time.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Time, and value cann't be parsed as Time, true
     * is returned.
     * </p>
     */
    public void testCanConvert20_1() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert("2006:06:19 e", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Time.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Time, and value cann't be parsed as Time, true
     * is returned.
     * </p>
     */
    public void testCanConvert20_2() {
        SimpleDateFormat formatter = new SimpleDateFormat("yy:mm:dd 'e'");
        StringConverter converter2 = new StringConverter(formatter);
        assertEquals("canConvert is incorrect.",
                converter2.canConvert("2006:06:19 e", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Time.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Timestamp, and value can be parsed as
     * Timestamp, true is returned.
     * </p>
     */
    public void testCanConvert21() {
        assertEquals("canConvert is incorrect.", converter.canConvert("2006-06-19 11:03:00.0000001",
                TEST_CRSMD.getColumnCount(), TEST_CRSMD, Timestamp.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Timestamp, and value cann't be parsed as
     * Timestamp, true is returned.
     * </p>
     */
    public void testCanConvert21_1() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert("2006:06:19 e", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Timestamp.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Timestamp, and value cann't be parsed as
     * Timestamp, true is returned.
     * </p>
     */
    public void testCanConvert21_2() {
        SimpleDateFormat formatter = new SimpleDateFormat("yy:mm:dd 'e'");
        StringConverter converter2 = new StringConverter(formatter);
        assertEquals("canConvert is incorrect.",
                converter2.canConvert("2006:06:19 e", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Timestamp.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is URL, true is returned.
     * </p>
     *
     * @since 2.0
     */
    public void testCanConvert22() {
        assertEquals("canConvert is incorrect.", true,
                converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, URL.class));
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
            converter.canConvert(TEST_VALUE, 1, null, Long.class);
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
            converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, null);
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
            converter.canConvert(TEST_VALUE, 0, TEST_CRSMD, Long.class);
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
            converter.canConvert(TEST_VALUE, -1, TEST_CRSMD, Long.class);
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
            converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount() + 1, TEST_CRSMD, Long.class);
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
            converter.convert(null, 1, TEST_CRSMD, Long.class);
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
     * Verify:When value is not a String, IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert7() {
        try {
            converter.convert(new Long(1000), 1, TEST_CRSMD, Long.class);
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
     * Verify:When desiredType is InputStream and value contains no character
     * invalid, returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert9() throws Exception {
        Object obj = converter.convert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, InputStream.class);
        assertTrue("convert is incorrect.", obj instanceof InputStream);
        byte[] readValue = new byte[TEST_VALUE.length()];
        ((InputStream) obj).read(readValue);
        assertEquals("convert is incorrect.", readValue.length, TEST_VALUE.length());
        for (int i = 0; i < readValue.length; i++) {
            assertEquals("convert is incorrect.", readValue[i], TEST_VALUE.charAt(i));
        }

    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is InputStream and value contains character less
     * than 0, IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert10() {
        String invalidStr = new String(new char[] { 'a', (char) -32767, (char) -1, 'b', (char) 128, (char) 1024,
            (char) 32767 });
        try {
            converter.convert(invalidStr, TEST_CRSMD.getColumnCount(), TEST_CRSMD, InputStream.class);
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
     * Verify:When desiredType is Reader, returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert11() throws Exception {
        Object obj = converter.convert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, Reader.class);
        assertTrue("convert is incorrect.", obj instanceof Reader);
        char[] readValue = new char[TEST_VALUE.length()];
        ((Reader) obj).read(readValue);
        assertEquals("convert is incorrect.", readValue.length, TEST_VALUE.length());
        for (int i = 0; i < readValue.length; i++) {
            assertEquals("convert is incorrect.", readValue[i], TEST_VALUE.charAt(i));
        }
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Byte, and value can be parsed as Byte,
     * returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert12() throws Exception {
        Object obj = converter.convert(new String("100"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, Byte.class);
        assertTrue("convert is incorrect.", obj instanceof Byte);
        assertEquals("convert is incorrect.", obj, new Byte((byte) 100));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Byte, and value cann't be parsed as Byte,
     * IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert12_1() {
        try {
            converter.convert(new String("wrong"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, Byte.class);
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
     * Verify:When desiredType is Short, and value can be parsed as Short,
     * returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert13() throws Exception {
        Object obj = converter.convert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, Short.class);
        assertTrue("convert is incorrect.", obj instanceof Short);
        assertEquals("convert is incorrect.", obj, new Short(TEST_VALUE));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Short, and value cann't be parsed as Short,
     * IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert13_1() {
        try {
            converter.convert(new String("wrong"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, Short.class);
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
     * Verify:When desiredType is Integer, and value can be parsed as Integer,
     * returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert14() throws Exception {
        Object obj = converter.convert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, Integer.class);
        assertTrue("convert is incorrect.", obj instanceof Integer);
        assertEquals("convert is incorrect.", obj, new Integer(TEST_VALUE));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Integer, and value cann't be parsed as
     * Integer, IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert14_1() {
        try {
            converter.convert(new String("wrong"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, Integer.class);
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
     * Verify:When desiredType is Long, and value can be parsed as Long,
     * returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert15() throws Exception {
        Object obj = converter.convert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, Long.class);
        assertTrue("convert is incorrect.", obj instanceof Long);
        assertEquals("convert is incorrect.", obj, new Long(TEST_VALUE));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Long, and value cann't be parsed as Long,
     * IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert15_1() {
        try {
            converter.convert(new String("wrong"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, Long.class);
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
     * Verify:When desiredType is Float, and value can be parsed as
     * Float,returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert16() throws Exception {
        Object obj = converter.convert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, Float.class);
        assertTrue("convert is incorrect.", obj instanceof Float);
        assertEquals("convert is incorrect.", obj, new Float(TEST_VALUE));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Float, and value cann't be parsed as Float,
     * IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert16_1() {
        try {
            converter.convert(new String("wrong"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, Float.class);
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
     * Verify:When desiredType is Double, and value can be parsed as
     * Double,returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert17() throws Exception {
        Object obj = converter.convert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, Double.class);
        assertTrue("convert is incorrect.", obj instanceof Double);
        assertEquals("convert is incorrect.", obj, new Double(TEST_VALUE));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Double, and value cann't be parsed as Double,
     * IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert17_1() {
        try {
            converter.convert(new String("wrong"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, Double.class);
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
     * Verify:When desiredType is BigDecimal, and value can be parsed as
     * BigDecimal, returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert18() throws Exception {
        Object obj = converter.convert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, BigDecimal.class);
        assertTrue("convert is incorrect.", obj instanceof BigDecimal);
        assertEquals("convert is incorrect.", obj, new BigDecimal(TEST_VALUE));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is BigDecimal, and value cann't be parsed as
     * BigDecimal, IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert18_1() {
        try {
            converter.convert(new String("wrong"), TEST_CRSMD.getColumnCount(), TEST_CRSMD, BigDecimal.class);
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
     * Verify:When desiredType is Date, and value can be parsed as Date,
     * returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert19() throws Exception {
        Object obj = converter.convert("2006-06-19", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Date.class);
        assertTrue("convert is incorrect.", obj instanceof Date);
        assertEquals("convert is incorrect.", obj, Date.valueOf("2006-06-19"));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Date, and value cann't be parsed as Date,
     * IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert19_1() {
        try {
            converter.convert("2006:06:19 e", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Date.class);
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
     * Verify:When desiredType is Date, and value cann't be parsed as Date,
     * returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert19_2() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yy:mm:dd 'e'");
        StringConverter converter2 = new StringConverter(formatter);
        Object obj = converter2.convert("2006:06:19 e", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Date.class);
        assertTrue("convert is incorrect.", obj instanceof Date);
        assertEquals("convert is incorrect.", obj, formatter.parseObject("2006:06:19 e"));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Time, and value can be parsed as Time,
     * returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert20() throws Exception {
        Object obj = converter.convert("23:59:59", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Time.class);
        assertTrue("convert is incorrect.", obj instanceof Time);
        assertEquals("convert is incorrect.", obj, Time.valueOf("23:59:59"));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Time, and value cann't be parsed as Time,
     * IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert20_1() {
        try {
            converter.convert("2006:06:19 e", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Time.class);
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
     * Verify:When desiredType is Time, and value cann't be parsed as Time,
     * returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert20_2() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yy:mm:dd 'e'");
        StringConverter converter2 = new StringConverter(formatter);
        Object obj = converter2.convert("2006:06:19 e", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Time.class);
        assertTrue("convert is incorrect.", obj instanceof Time);
        assertEquals("convert is incorrect.", obj, new Time(formatter.parse("2006:06:19 e").getTime()));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Timestamp, and value can be parsed as
     * Timestamp, returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert21() throws Exception {
        Object obj = converter.convert("2006-06-19 23:59:59.125", TEST_CRSMD.getColumnCount(), TEST_CRSMD,
                Timestamp.class);
        assertTrue("convert is incorrect.", obj instanceof Timestamp);
        assertEquals("convert is incorrect.", obj, Timestamp.valueOf("2006-06-19 23:59:59.125"));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Timestamp, and value cann't be parsed as
     * Timestamp, IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert21_1() {
        try {
            converter.convert("2006:06:19 e", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Timestamp.class);
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
     * Verify:When desiredType is Timestamp, and value cann't be parsed as
     * Timestamp, returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert21_2() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yy:mm:dd 'e'");
        StringConverter converter2 = new StringConverter(formatter);
        Object obj = converter2.convert("2006:06:19 e", TEST_CRSMD.getColumnCount(), TEST_CRSMD, Timestamp.class);
        assertTrue("convert is incorrect.", obj instanceof Timestamp);
        assertEquals("convert is incorrect.", obj, new Timestamp(formatter.parse("2006:06:19 e").getTime()));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is URL, and value can be parsed as URL, returned
     * value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     * @since 2.0
     */
    public void testConvert22() throws Exception {
        Object obj = converter.convert("http://www.topcoder.com", TEST_CRSMD.getColumnCount(), TEST_CRSMD, URL.class);
        assertTrue("convert is incorrect.", obj instanceof URL);
        assertEquals("convert is incorrect.", new URL("http://www.topcoder.com"), obj);
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is URL, and value cann't be parsed as URL,
     * IllegalMappingException is thrown.
     * </p>
     *
     * @since 2.0
     */
    public void testConvert22_1() {
        try {
            converter.convert("wrong://www.topcoder.com", TEST_CRSMD.getColumnCount(), TEST_CRSMD, URL.class);
            fail("IllegalMappingException expected");
        } catch (IllegalMappingException e) {
            // good
        }
    }
}
