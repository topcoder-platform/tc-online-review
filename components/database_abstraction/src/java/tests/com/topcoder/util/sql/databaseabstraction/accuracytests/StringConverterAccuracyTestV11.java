/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.accuracytests;

import com.topcoder.util.sql.databaseabstraction.ondemandconversion.StringConverter;

import junit.framework.TestCase;

import java.io.InputStream;
import java.io.Reader;

import java.math.BigDecimal;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * Accuracy Test cases for the class StringConverter.
 * </p>
 *
 * @author PE
 * @version 1.1
 */
public class StringConverterAccuracyTestV11 extends TestCase {
    /** Represents the String instance for testing. */
    private static final String VALUE = new String("1");

    /** Represents the String instance for testing. */
    private static final String INVALID_VALUE = new String("ab");

    /** Represents the StringConverter instance for testing. */
    private StringConverter converter = new StringConverter();

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy1() throws Exception {
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(null, 1, AccuracyTestHelper.getCRMD(), InputStream.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy2() throws Exception {
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(new HashMap(), 1, AccuracyTestHelper.getCRMD(), InputStream.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy3() throws Exception {
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Map.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy4() throws Exception {
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Object.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy5() throws Exception {
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Byte.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy6() throws Exception {
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Short.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy7() throws Exception {
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Integer.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy8() throws Exception {
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Long.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy9() throws Exception {
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Float.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy10() throws Exception {
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Double.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy11() throws Exception {
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), BigDecimal.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy12() throws Exception {
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), InputStream.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy13() throws Exception {
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Reader.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy14() throws Exception {
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert("2006-06-19", 1, AccuracyTestHelper.getCRMD(), Date.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy15() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd 'e'");
        converter = new StringConverter(formatter);
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert("2006:06:19 e", 1, AccuracyTestHelper.getCRMD(), Date.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy16() throws Exception {
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert("10:10:10", 1, AccuracyTestHelper.getCRMD(), Time.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy17() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        converter = new StringConverter(formatter);
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert("10:10:10", 1, AccuracyTestHelper.getCRMD(), Time.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy18() throws Exception {
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert("2006-06-26 11:46:04.99", 1, AccuracyTestHelper.getCRMD(), Timestamp.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy19() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        converter = new StringConverter(formatter);
        assertEquals("canConvert is incorrect.", true,
            converter.canConvert("2006-06-26 11:46:04", 1, AccuracyTestHelper.getCRMD(), Timestamp.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy20() throws Exception {
        String invalidStr = new String(new char[] {
                    'a', (char) -32767, (char) -1, 'b', (char) 128, (char) 1024, (char) 32767
                });
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(invalidStr, 1, AccuracyTestHelper.getCRMD(), InputStream.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy21() throws Exception {
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(INVALID_VALUE, 1, AccuracyTestHelper.getCRMD(), Byte.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy22() throws Exception {
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(INVALID_VALUE, 1, AccuracyTestHelper.getCRMD(), Short.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy23() throws Exception {
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(INVALID_VALUE, 1, AccuracyTestHelper.getCRMD(), Integer.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy24() throws Exception {
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(INVALID_VALUE, 1, AccuracyTestHelper.getCRMD(), Long.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy25() throws Exception {
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(INVALID_VALUE, 1, AccuracyTestHelper.getCRMD(), Float.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy26() throws Exception {
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(INVALID_VALUE, 1, AccuracyTestHelper.getCRMD(), Double.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy27() throws Exception {
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(INVALID_VALUE, 1, AccuracyTestHelper.getCRMD(), BigDecimal.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy28() throws Exception {
        converter = new StringConverter(new SimpleDateFormat());
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Date.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy29() throws Exception {
        converter = new StringConverter(new SimpleDateFormat());
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Time.class));
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy30() throws Exception {
        converter = new StringConverter(new SimpleDateFormat());
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Timestamp.class));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy1() throws Exception {
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), Byte.class);
        assertTrue("convert is incorrect.", obj instanceof Byte);
        assertEquals("convert is incorrect.", obj, Byte.valueOf(VALUE));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy2() throws Exception {
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), Short.class);
        assertTrue("convert is incorrect.", obj instanceof Short);
        assertEquals("convert is incorrect.", obj, Short.valueOf(VALUE));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy3() throws Exception {
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), Integer.class);
        assertTrue("convert is incorrect.", obj instanceof Integer);
        assertEquals("convert is incorrect.", obj, Integer.valueOf(VALUE));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy4() throws Exception {
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), Long.class);
        assertTrue("convert is incorrect.", obj instanceof Long);
        assertEquals("convert is incorrect.", obj, Long.valueOf(VALUE));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy5() throws Exception {
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), Float.class);
        assertTrue("convert is incorrect.", obj instanceof Float);
        assertEquals("convert is incorrect.", obj, Float.valueOf(VALUE));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy6() throws Exception {
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), Double.class);
        assertTrue("convert is incorrect.", obj instanceof Double);
        assertEquals("convert is incorrect.", obj, Double.valueOf(VALUE));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy7() throws Exception {
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), BigDecimal.class);
        assertTrue("convert is incorrect.", obj instanceof BigDecimal);
        assertEquals("convert is incorrect.", obj, new BigDecimal(VALUE));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy8() throws Exception {
        String v = "2006-06-19";
        Object obj = converter.convert(v, 1, AccuracyTestHelper.getCRMD(), Date.class);
        assertTrue("convert is incorrect.", obj instanceof Date);
        assertEquals("convert is incorrect.", obj, Date.valueOf(v));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy9() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        converter = new StringConverter(formatter);

        String v = "2006-05-19";
        Object obj = converter.convert(v, 1, AccuracyTestHelper.getCRMD(), Date.class);
        assertTrue("convert is incorrect.", obj instanceof Date);
        assertEquals("convert is incorrect.", obj, new Date(formatter.parse(v).getTime()));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy10() throws Exception {
        String v = "10:10:10";
        Object obj = converter.convert(v, 1, AccuracyTestHelper.getCRMD(), Time.class);
        assertTrue("convert is incorrect.", obj instanceof Time);
        assertEquals("convert is incorrect.", obj, Time.valueOf(v));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy11() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        converter = new StringConverter(formatter);

        String v = "10:10:10";
        Object obj = converter.convert(v, 1, AccuracyTestHelper.getCRMD(), Time.class);
        assertTrue("convert is incorrect.", obj instanceof Time);
        assertEquals("convert is incorrect.", obj, new Time(formatter.parse(v).getTime()));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy12() throws Exception {
        String v = "2006-06-26 11:46:04";
        Object obj = converter.convert(v, 1, AccuracyTestHelper.getCRMD(), Timestamp.class);
        assertTrue("convert is incorrect.", obj instanceof Timestamp);
        assertEquals("convert is incorrect.", obj, Timestamp.valueOf(v));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy13() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        converter = new StringConverter(formatter);

        String v = "2006-06-26 11:46:04";
        Object obj = converter.convert(v, 1, AccuracyTestHelper.getCRMD(), Timestamp.class);
        assertTrue("convert is incorrect.", obj instanceof Timestamp);
        assertEquals("convert is incorrect.", obj, new Timestamp(formatter.parse(v).getTime()));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy14() throws Exception {
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), InputStream.class);
        assertTrue("convert is incorrect.", obj instanceof InputStream);
        AccuracyTestHelper.assertEquals("convert is incorrect.", AccuracyTestHelper.readContent((InputStream) obj),
            VALUE.getBytes());
    }
}
