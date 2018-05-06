/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.accuracytests;

import com.topcoder.util.sql.databaseabstraction.ondemandconversion.TimestampConverter;

import junit.framework.TestCase;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Map;


/**
 * <p>
 * Accuracy Test cases for the class TimestampConverter.
 * </p>
 *
 * @author PE
 * @version 1.1
 */
public class TimestampConverterAccuracyTestV11 extends TestCase {
    /** Represents the Timestamp instance for testing. */
    private static final Timestamp VALUE = new Timestamp(new java.util.Date().getTime());

    /** Represents the TimestampConverter instance for testing. */
    private TimestampConverter converter = new TimestampConverter();

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy1() throws Exception {
        assertEquals("canConvert is incorrect.", false,
            converter.canConvert(null, 1, AccuracyTestHelper.getCRMD(), String.class));
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
            converter.canConvert("aa", 1, AccuracyTestHelper.getCRMD(), String.class));
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
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Time.class));
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
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Date.class));
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
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), Long.class));
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
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), String.class));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy1() throws Exception {
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), Date.class);
        assertTrue("convert is incorrect.", obj instanceof Date);
        assertEquals("convert is incorrect.", obj, new Date(VALUE.getTime()));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy2() throws Exception {
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), Time.class);
        assertTrue("convert is incorrect.", obj instanceof Time);
        assertEquals("convert is incorrect.", obj, new Time(VALUE.getTime()));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy3() throws Exception {
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), Long.class);
        assertTrue("convert is incorrect.", obj instanceof Long);
        assertEquals("convert is incorrect.", obj, new Long(VALUE.getTime()));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy4() throws Exception {
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), String.class);
        assertTrue("convert is incorrect.", obj instanceof String);
        assertEquals("convert is incorrect.", obj, new SimpleDateFormat().format(VALUE));
    }
}
