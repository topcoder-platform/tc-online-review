/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.ondemandconversion;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;

import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;
import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.UnitTestHelper;

/**
 * Test case for DateConverter.
 *
 * @author justforplay
 * @version 1.1
 * @since 1.1
 */
public class DateConverterTest extends TestCase {

    /**
     * Instance of CustomResultSetMetaData used in this unit test case as
     * parameter to call canConvert and convert methods.
     */
    private static final CustomResultSetMetaData TEST_CRSMD = UnitTestHelper.getCustomRMD();

    /**
     * Instance of Date used in this unit test case as value parameter.
     */
    private static final Date TEST_VALUE = new Date(100000);

    /**
     * Instance of DateConverter used in this unit test case.
     */
    private DateConverter converter = new DateConverter();

    /**
     * <p>
     * Test DateConverter().
     * </p>
     * <p>
     * Verify:DateConverter can be instantiated correctly.
     * </p>
     */
    public void testDateConverter() {
        assertNotNull("Unable to instantiate DateConverter.", converter);
    }

    /**
     * <p>
     * Test DateConverter(DateFormat dateFormat) {.
     * </p>
     * <p>
     * Verify:when dateFormat is null, IllegalArgumentException is thrown.
     * </p>
     */
    public void testDateConverter2() {
        try {
            new DateConverter(null);
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
            converter.canConvert(TEST_VALUE, 1, null, Time.class);
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
            converter.canConvert(TEST_VALUE, 0, TEST_CRSMD, Time.class);
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
            converter.canConvert(TEST_VALUE, -1, TEST_CRSMD, Time.class);
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
            converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount() + 1, TEST_CRSMD, Time.class);
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
        assertEquals("canConvert is incorrect.", converter.canConvert(null, 1, TEST_CRSMD, Time.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When value is not a Date, false is returned.
     * </p>
     */
    public void testCanConvert7() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(new String("Wrong Type"), 1, TEST_CRSMD, Time.class), false);
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
     * Verify:When desiredType is Byte, false is returned.
     * </p>
     */
    public void testCanConvert9() {
        assertEquals("canConvert is incorrect.", converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, Byte.class), false);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Time, true is returned.
     * </p>
     */
    public void testCanConvert10() {
        assertEquals("canConvert is incorrect.", converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, Time.class), true);
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify:When desiredType is Timestamp, true is returned.
     * </p>
     */
    public void testCanConvert11() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, Timestamp.class), true);
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
     * Verify:When desiredType is BigDecimal, false is returned.
     * </p>
     */
    public void testCanConvert15() {
        assertEquals("canConvert is incorrect.", converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, BigDecimal.class),
                false);
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
    public void testCanConvert16() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount(), TEST_CRSMD, String.class), true);
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
    public void testCanConvert17() {
        assertEquals("canConvert is incorrect.",
                converter.canConvert(TEST_VALUE, 1, TEST_CRSMD, boolean.class), false);
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
            converter.canConvert(TEST_VALUE, 1, null, Time.class);
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
            converter.canConvert(TEST_VALUE, 0, TEST_CRSMD, Time.class);
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
            converter.canConvert(TEST_VALUE, -1, TEST_CRSMD, Time.class);
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
            converter.canConvert(TEST_VALUE, TEST_CRSMD.getColumnCount() + 1, TEST_CRSMD, Time.class);
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
            converter.convert(null, 1, TEST_CRSMD, Time.class);
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
     * Verify:When value is not a Date, IllegalMappingException is thrown.
     * </p>
     */
    public void testConvert7() {
        try {
            converter.convert(new String("Wrong Type"), 1, TEST_CRSMD, Time.class);
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
     * Verify:When desiredType is Time, returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert9() throws Exception {
        Object obj = converter.convert(TEST_VALUE, 1, TEST_CRSMD, Time.class);
        assertTrue("convert is incorrect.", obj instanceof Time);
        assertEquals("convert is incorrect.", obj, new Time(TEST_VALUE.getTime()));
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Short, IllegalMappingException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert10() throws Exception {
        try {
            converter.convert(TEST_VALUE, 1, TEST_CRSMD, Short.class);
            fail("IllegalMappingException expected.");
        } catch (IllegalMappingException e) {
            // good
        }
    }

    /**
     * <p>
     * Test convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     * <p>
     * Verify:When desiredType is Timestamp, returned value is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert11() throws Exception {

        Object obj = converter.convert(TEST_VALUE, 1, TEST_CRSMD, Timestamp.class);
        assertTrue("convert is incorrect.", obj instanceof Timestamp);
        assertEquals("convert is incorrect.", obj, new Timestamp(TEST_VALUE.getTime()));

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
        assertEquals("convert is incorrect.", obj, new Long(TEST_VALUE.getTime()));
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
    public void testConvert13() throws Exception {
        Object obj = converter.convert(TEST_VALUE, 1, TEST_CRSMD, String.class);
        assertTrue("convert is incorrect.", obj instanceof String);
        assertEquals("convert is incorrect.", obj, new SimpleDateFormat().format(TEST_VALUE));
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
    public void testConvert14() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yy:mm:dd 'at' hh:mm:ss");
        DateConverter converter2 = new DateConverter(formatter);
        Object obj = converter2.convert(TEST_VALUE, 1, TEST_CRSMD, String.class);
        assertTrue("convert is incorrect.", obj instanceof String);
        assertEquals("convert is incorrect.", obj, formatter.format(TEST_VALUE));
    }

}
