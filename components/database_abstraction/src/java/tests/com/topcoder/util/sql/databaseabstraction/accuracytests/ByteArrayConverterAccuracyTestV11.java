/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.accuracytests;

import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ByteArrayConverter;

import junit.framework.TestCase;

import java.io.InputStream;

import java.util.Map;


/**
 * <p>
 * Accuracy Test cases for the class ByteArrayConverter.
 * </p>
 *
 * @author PE
 * @version 1.1
 */
public class ByteArrayConverterAccuracyTestV11 extends TestCase {
    /** Represents the byte[] instance for testing. */
    private static final byte[] VALUE = new byte[] { 1, 1 };

    /** Represents the ByteArrayConverter instance for testing. */
    private ByteArrayConverter converter = new ByteArrayConverter();

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
            converter.canConvert("aa", 1, AccuracyTestHelper.getCRMD(), InputStream.class));
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
            converter.canConvert(VALUE, 1, AccuracyTestHelper.getCRMD(), InputStream.class));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy1() throws Exception {
        Object obj = converter.convert(VALUE, 1, AccuracyTestHelper.getCRMD(), InputStream.class);
        assertTrue("convert is incorrect.", obj instanceof InputStream);
        AccuracyTestHelper.assertEquals("convert is incorrect.", AccuracyTestHelper.readContent((InputStream) obj),
            VALUE);
    }
}
