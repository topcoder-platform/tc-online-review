/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.ondemandconversion;

import java.math.BigDecimal;
import java.sql.Date;

import junit.framework.TestCase;

import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;
import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.UnitTestHelper;

/**
 * Test case for BigDecimalConverter.
 *
 * @author justforplay
 * @version 1.1
 * @since 1.1
 */
public class BigDecimalConverterTest extends TestCase {

    /**
     * Instance of CustomResultSetMetaData used in this unit test case as
     * parameter to call canConvert and convert methods.
     */
    private static final CustomResultSetMetaData TEST_CRSMD = UnitTestHelper.getCustomRMD();

    /**
     * Instance of BigDecimal used in this unit test case as value parameter.
     */
    private static final BigDecimal TEST_VALUE = new BigDecimal("2.34560");

    /**
     * Instance of BigDecimalConverter used in this unit test case.
     */
    private BigDecimalConverter converter = new BigDecimalConverter();

    /**
     * <p>
     * Test BigDecimalConverter().
     * </p>
     * <p>
     * Verify:BigDecimalConverter can be instantiated correctly.
     * </p>
     */
    public void testBigDecimalConverter() {
        assertNotNull("Unable to instantiate BigDecimalConverter.", converter);
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
            converter.canConvert(TEST_VALUE, 1, null, Byte.class);
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
            converter.canConvert(TEST_VALUE, 0, TEST_CRSMD, Byte.class);
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
            converter.canConvert(TEST_VALUE, -1, TEST_CRSMD, Byte.class);
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
            converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount() + 1, TEST_CRSMD, Byte.class);
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
        assertEquals("canConvert is incorrect.", converter.canConvert(null, 1, TEST_CRSMD, Byte.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When value is not a BigDecimal, false is returned.
     * </p>
     */
    public void testCanConvert7() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(new String("Not BigDecimal"), 1, TEST_CRSMD, Byte.class), false);
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
        assertEquals("canConvert is incorrect.", converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, Date.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Byte, true is returned.
     * </p>
     */
    public void testCanConvert9() {
        assertEquals("canConvert is incorrect.", converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, Byte.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Short, true is returned.
     * </p>
     */
    public void testCanConvert10() {
        assertEquals("canConvert is incorrect.", converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, Short.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Integer, true is returned.
     * </p>
     */
    public void testCanConvert11() {
        assertEquals("canConvert is incorrect.", converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, Integer.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Long, true is returned.
     * </p>
     */
    public void testCanConvert12() {
        assertEquals("canConvert is incorrect.", converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, Long.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Float, true is returned.
     * </p>
     */
    public void testCanConvert13() {
        assertEquals("canConvert is incorrect.", converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, Float.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Double, true is returned.
     * </p>
     */
    public void testCanConvert14() {
        assertEquals("canConvert is incorrect.", converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, Double.class), true);
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
    public void testCanConvert15() {
        assertEquals("canConvert is incorrect.", converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, String.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is boolean, false is returned.
     * </p>
     */
    public void testCanConvert16() {
        assertEquals("canConvert is incorrect.", converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, boolean.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is BigDecimal, false is returned.
     * </p>
     */
    public void testCanConvert17() {
        assertEquals("canConvert is incorrect.", converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, BigDecimal.class),
                false);
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
            converter.canConvert(TEST_VALUE, 1, null, Byte.class);
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
            converter.canConvert(TEST_VALUE, 0, TEST_CRSMD, Byte.class);
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
            converter.canConvert(TEST_VALUE, -1, TEST_CRSMD, Byte.class);
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
            converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount() + 1, TEST_CRSMD, Byte.class);
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
            converter.convert(null, 1, TEST_CRSMD, Byte.class);
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
     * Verify:When value is not a BigDecimal, IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert7() {
        try {
            converter.convert(new String("Not BigDecimal"), 1, TEST_CRSMD, Byte.class);
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
            converter.convert(TEST_VALUE, 1, TEST_CRSMD, Date.class);
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
     * Verify:When desiredType is BigDecimal, IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert8_1() {
        try {
            converter.convert(TEST_VALUE, 1, TEST_CRSMD, BigDecimal.class);
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
     * Verify:When desiredType is Byte, returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert9() throws Exception {
        Object obj = converter.convert(TEST_VALUE, 1, TEST_CRSMD, Byte.class);
        assertTrue("convert is incorrect.", obj instanceof Byte);
        assertEquals("convert is incorrect.", obj, new Byte(TEST_VALUE.byteValue()));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Short, returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert10() throws Exception {
        Object obj = converter.convert(TEST_VALUE, 1, TEST_CRSMD, Short.class);
        assertTrue("convert is incorrect.", obj instanceof Short);
        assertEquals("convert is incorrect.", obj, new Short(TEST_VALUE.shortValue()));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Integer, returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert11() throws Exception {
        Object obj = converter.convert(TEST_VALUE, 1, TEST_CRSMD, Integer.class);
        assertTrue("convert is incorrect.", obj instanceof Integer);
        assertEquals("convert is incorrect.", obj, new Integer(TEST_VALUE.intValue()));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Long, returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert12() throws Exception {
        Object obj = converter.convert(TEST_VALUE, 1, TEST_CRSMD, Long.class);
        assertTrue("convert is incorrect.", obj instanceof Long);
        assertEquals("convert is incorrect.", obj, new Long(TEST_VALUE.longValue()));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Float, returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert13() throws Exception {
        Object obj = converter.convert(TEST_VALUE, 1, TEST_CRSMD, Float.class);
        assertTrue("convert is incorrect.", obj instanceof Float);
        assertEquals("convert is incorrect.", obj, new Float(TEST_VALUE.floatValue()));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Double, returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert14() throws Exception {
        Object obj = converter.convert(TEST_VALUE, 1, TEST_CRSMD, Double.class);
        assertTrue("convert is incorrect.", obj instanceof Double);
        assertEquals("convert is incorrect.", obj, new Double(TEST_VALUE.doubleValue()));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is String, returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert15() throws Exception {
        Object obj = converter.convert(TEST_VALUE, 1, TEST_CRSMD, String.class);
        assertTrue("convert is incorrect.", obj instanceof String);
        assertEquals("convert is incorrect.", obj, TEST_VALUE.toString());
    }

}
