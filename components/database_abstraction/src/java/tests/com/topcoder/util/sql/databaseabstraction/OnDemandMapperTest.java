/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;

import com.topcoder.util.sql.databaseabstraction.ondemandconversion.BigDecimalConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.BlobConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.BooleanConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ByteArrayConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ByteConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ClobConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.DateConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.DoubleConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.FloatConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.IntConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.LongConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ShortConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.StringConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.TimeConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.TimestampConverter;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.URLConverter;

/**
 * Test case for {@link OnDemandMapper}.
 *
 * @author justforplay, suhugo
 * @version 2.0
 * @since 1.1
 */
public class OnDemandMapperTest extends TestCase {

    /**
     * Instance of CustomResultSetMetaData for unit test.
     */
    private static final CustomResultSetMetaData TEST_CRSMD = UnitTestHelper.getCustomRMD();

    /**
     * Instance of OnDemandMapper for unit test.
     */
    private OnDemandMapper mapper;

    /**
     * Initialize mapper.
     */
    public void setUp() {
        mapper = new OnDemandMapper();
    }

    /**
     * <p>
     * Test OnDemandMapper().
     * </p>
     * <p>
     * Verify: OndemandMapper can be instantiated correctly.
     * </p>
     */
    public void testOnDemandMapper1() {
        assertNotNull("Unable to instantiate OnDemandMapper.", mapper);
    }

    /**
     * <p>
     * Test OnDemandMapper(OnDemandConverter[] converters).
     * </p>
     * <p>
     * Verify: OndemandMapper can be instantiated correctly.
     * </p>
     */
    public void testOnDemandMapper2() {
        OnDemandConverter[] converters = new OnDemandConverter[1];
        converters[0] = new BigDecimalConverter();
        OnDemandMapper mapper2 = new OnDemandMapper(converters);
        assertNotNull("Unable to instantiate OnDemandMapper.", mapper2);
        OnDemandConverter[] real = mapper2.getConverters();
        assertEquals("Mapper is not initialized correctly.", converters.length, real.length);
        assertEquals("Mapper is not initialized correctly.", converters[0], real[0]);
    }

    /**
     * <p>
     * Test OnDemandMapper(OnDemandConverter[] converters).
     * </p>
     * <p>
     * Verify: When converters is null, IllegalArgumentException is thrown.
     * </p>
     */
    public void testOnDemandMapper3() {
        try {
            new OnDemandMapper(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test OnDemandMapper(OnDemandConverter[] converters).
     * </p>
     * <p>
     * Verify: When converters contains null element, IllegalArgumentException
     * is thrown.
     * </p>
     */
    public void testOnDemandMapper4() {
        try {
            new OnDemandMapper(new OnDemandConverter[] { new BigDecimalConverter(), null });
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test addConverter(OnDemandConverter converter).
     * </p>
     * <p>
     * Verify: When converter is null, IllegalArgumentException is thrown.
     * </p>
     */
    public void testAddConverter1() {
        try {
            mapper.addConverter(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test addConverter(OnDemandConverter converter).
     * </p>
     * <p>
     * Verify: When converter does not exist in mapper, true is returned.
     * </p>
     */
    public void testAddConverter2() {
        OnDemandConverter converter = new BigDecimalConverter();
        assertEquals("addConverter is incorrect.", mapper.addConverter(converter), true);
        OnDemandConverter[] real = mapper.getConverters();
        assertEquals("addConverter is incorrect.", 1, real.length);
        assertEquals("addConverter is incorrect.", converter, real[0]);
    }

    /**
     * <p>
     * Test addConverter(OnDemandConverter converter).
     * </p>
     * <p>
     * Verify: When converter exists in mapper, false is returned.
     * </p>
     */
    public void testAddConverter3() {
        OnDemandConverter converter = new BigDecimalConverter();
        assertEquals("addConverter is incorrect.", mapper.addConverter(converter), true);
        assertEquals("addConverter is incorrect.", mapper.addConverter(converter), false);
        OnDemandConverter[] real = mapper.getConverters();
        assertEquals("addConverter is incorrect.", 1, real.length);
        assertEquals("addConverter is incorrect.", converter, real[0]);
    }

    /**
     * <p>
     * Test removeConverter(OnDemandConverter converter).
     * </p>
     * <p>
     * Verify: When converter is null, IllegalArgumentException is thrown.
     * </p>
     */
    public void testRemoveConverter1() {
        try {
            mapper.removeConverter(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test removeConverter(OnDemandConverter converter).
     * </p>
     * <p>
     * Verify: When converter does not exist in mapper, false is returned.
     * </p>
     */
    public void testRemoveConverter2() {
        OnDemandConverter converter = new BigDecimalConverter();
        assertEquals("removeConverter is incorrect.", mapper.removeConverter(converter), false);
    }

    /**
     * <p>
     * Test removeConverter(OnDemandConverter converter).
     * </p>
     * <p>
     * Verify: When converter exists in mapper, false is returned.
     * </p>
     */
    public void testRemoveConverter3() {
        OnDemandConverter converter = new BigDecimalConverter();
        mapper.addConverter(converter);
        assertEquals("removeConverter is incorrect.", mapper.removeConverter(converter), true);
        OnDemandConverter[] real = mapper.getConverters();
        assertEquals("removeConverter is incorrect.", 0, real.length);

    }

    /**
     * <p>
     * Test getConverters().
     * </p>
     * <p>
     * Verify: getConverters is correct.
     * </p>
     */
    public void testGetConverters1() {
        OnDemandConverter[] real = mapper.getConverters();
        assertEquals("getConverters is incorrect.", 0, real.length);
    }

    /**
     * <p>
     * Test getConverters().
     * </p>
     * <p>
     * Verify: getConverters is correct.
     * </p>
     */
    public void testGetConverters2() {
        OnDemandConverter converter = new BigDecimalConverter();
        mapper.addConverter(converter);
        OnDemandConverter[] real = mapper.getConverters();
        assertEquals("getConverters is incorrect.", 1, real.length);
        assertEquals("getConverters is incorrect.", converter, real[0]);

    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify: When metaData is null, IllegalArgumentException is thrown.
     * </p>
     */
    public void testCanConvert1() {
        try {
            mapper.canConvert(new Long("100"), 1, null, Integer.class);
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
     * Verify: When desiredType is null, IllegalArgumentException is thrown.
     * </p>
     */
    public void testCanConvert2() {
        try {
            mapper.canConvert(new Long("100"), 1, TEST_CRSMD, null);
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
     * Verify: canConvert is correct.
     * </p>
     */
    public void testCanConvert3() {
        assertFalse("canConvert is incorrect.", mapper.canConvert(new BigDecimal("100"), 1, TEST_CRSMD, Integer.class));
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify: canConvert is correct.
     * </p>
     */
    public void testCanConvert4() {
        mapper.addConverter(new BigDecimalConverter());
        assertFalse("canConvert is incorrect.", mapper.canConvert(new Long("100"), 1, TEST_CRSMD, Integer.class));
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify: canConvert is correct.
     * </p>
     */
    public void testCanConvert5() {
        try {
            mapper.addConverter(new BigDecimalConverter());
            assertFalse("canConvert is incorrect.",
                    mapper.canConvert(new BigDecimal("100"), 0, TEST_CRSMD, Integer.class));
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
     * Verify: canConvert is correct.
     * </p>
     */
    public void testCanConverter6() {
        try {
            mapper.addConverter(new BigDecimalConverter());
            assertFalse("canConvert is incorrect.", mapper.canConvert(new BigDecimal("100"),
                    TEST_CRSMD.getColumnCount() + 1, TEST_CRSMD, Integer.class));
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
     * Verify: canConvert is correct.
     * </p>
     */
    public void testCanConverter7() {
        mapper.addConverter(new BigDecimalConverter());
        assertTrue("canConvert is incorrect.", mapper.canConvert(new BigDecimal("100"), 1, TEST_CRSMD, Integer.class));
    }

    /**
     * <p>
     * Test canConvert(Object value, int column, CustomResultSetMetaData
     * metaData, Class desiredType).
     * </p>
     * <p>
     * Verify: canConvert is correct.
     * </p>
     */
    public void testCanConverter8() {
        mapper.addConverter(new BigDecimalConverter());
        assertFalse("canConvert is incorrect.", mapper.canConvert(new BigDecimal("100"), 1, TEST_CRSMD, Date.class));
    }

    /**
     * <p>
     * Test convert(Object value, int column, CustomResultSetMetaData metaData,
     * Class desiredType).
     * </p>
     * <p>
     * Verify: When metaData is null, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert1() throws Exception {
        try {
            mapper.convert(new BigDecimal("100"), 1, null, Integer.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test convert(Object value, int column, CustomResultSetMetaData metaData,
     * Class desiredType).
     * </p>
     * <p>
     * Verify: When desiredType is null, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert2() throws Exception {
        try {
            mapper.convert(new BigDecimal("100"), 1, TEST_CRSMD, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test convert(Object value, int column, CustomResultSetMetaData metaData,
     * Class desiredType).
     * </p>
     * <p>
     * Verify: convert is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert3() throws Exception {
        try {
            mapper.addConverter(new BigDecimalConverter());
            mapper.convert(null, 1, TEST_CRSMD, Integer.class);
            fail("IllegalMappingException expected.");
        } catch (IllegalMappingException e) {
            // good
        }
    }

    /**
     * <p>
     * Test convert(Object value, int column, CustomResultSetMetaData metaData,
     * Class desiredType).
     * </p>
     * <p>
     * Verify: convert is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert4() throws Exception {
        mapper.addConverter(new BigDecimalConverter());
        Object result = mapper.convert(new BigDecimal("100"), 1, TEST_CRSMD, Integer.class);
        assertEquals("convert() is incorrect.", result, new Integer(100));
    }

    /**
     * <p>
     * Test convert(Object value, int column, CustomResultSetMetaData metaData,
     * Class desiredType).
     * </p>
     * <p>
     * Verify: convert is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvert5() throws Exception {
        mapper.addConverter(new BigDecimalConverter());
        mapper.addConverter(new StringConverter(new SimpleDateFormat("yy:MM:dd 'e'")));
        Object result = mapper.convert(new String("2006:06:09 e"), 1, TEST_CRSMD, Date.class);
        assertEquals("convert() is incorrect.", result, Date.valueOf("2006-06-09"));
    }

    /**
     * <p>
     * Test clearConverters().
     * </p>
     * <p>
     * Verify: clearConverters is correct.
     * </p>
     */
    public void testClearConverters() {
        OnDemandConverter converter = new BigDecimalConverter();
        mapper.addConverter(converter);
        mapper.clearConverters();
        OnDemandConverter[] real = mapper.getConverters();
        assertEquals("removeConverter is incorrect.", 0, real.length);
    }

    /**
     * <p>
     * Test createDefaultOnDemandMapper().
     * </p>
     * <p>
     * Verify: createDefaultOnDemandMapper is correct.
     * </p>
     */
    public void testCreateDefaultOnDemandMapper() {
        OnDemandMapper mapper2 = OnDemandMapper.createDefaultOnDemandMapper();
        assertNotNull("createDefaultOnDemandMapper is incorrect.", mapper2);
        OnDemandConverter[] real = mapper2.getConverters();
        assertEquals("createDefaultOnDemandMapper is incorrect.", real.length, 16);
        Class<?>[] expected = new Class<?>[] { BigDecimalConverter.class, BlobConverter.class, BooleanConverter.class,
            ByteArrayConverter.class, ByteConverter.class, ClobConverter.class, DateConverter.class,
            DoubleConverter.class, FloatConverter.class, IntConverter.class, LongConverter.class,
            ShortConverter.class, StringConverter.class, TimeConverter.class, TimestampConverter.class,
            URLConverter.class };
        for (Class<?> element : expected) {
            int j;
            for (j = 0; j < real.length; j++) {
                if (real[j].getClass().equals(element)) {
                    break;
                }
            }
            if (j == real.length) {
                fail("createDefaultOnDemandMapper is incorrect, not contain " + element.getName());
            }
        }
    }
}
