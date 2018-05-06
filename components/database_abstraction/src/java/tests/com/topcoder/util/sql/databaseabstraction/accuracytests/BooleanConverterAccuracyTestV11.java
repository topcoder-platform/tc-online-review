/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.accuracytests;

import com.topcoder.util.sql.databaseabstraction.ondemandconversion.BooleanConverter;

import junit.framework.TestCase;

import java.math.BigDecimal;

import java.util.Map;


/**
 * <p>
 * Accuracy Test cases for the class BooleanConverter.
 * </p>
 *
 * @author PE
 * @version 1.1
 */
public class BooleanConverterAccuracyTestV11 extends TestCase {
    /** Represents the Boolean instance for testing. */
    private static final Boolean VALUE = new Boolean(true);

    /** Represents the BooleanConverter instance for testing. */
    private BooleanConverter converter = new BooleanConverter();

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
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), Byte.class);
        assertTrue("convert is incorrect.", obj instanceof Byte);
        assertEquals("convert is incorrect.", obj, new Byte("1"));
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
        assertEquals("convert is incorrect.", obj, new Short("1"));
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
        assertEquals("convert is incorrect.", obj, new Integer("1"));
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
        assertEquals("convert is incorrect.", obj, new Long("1"));
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
        assertEquals("convert is incorrect.", obj, new Float("1"));
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
        assertEquals("convert is incorrect.", obj, new Double("1"));
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
        assertEquals("convert is incorrect.", obj, new BigDecimal("1"));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy8() throws Exception {
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), String.class);
        assertTrue("convert is incorrect.", obj instanceof String);
        assertEquals("convert is incorrect.", obj, VALUE.toString());
    }
}
