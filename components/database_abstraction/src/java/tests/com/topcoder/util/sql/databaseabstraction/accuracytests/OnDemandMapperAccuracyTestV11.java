/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.accuracytests;

import java.math.BigDecimal;

import junit.framework.TestCase;

import com.topcoder.util.sql.databaseabstraction.OnDemandConverter;
import com.topcoder.util.sql.databaseabstraction.OnDemandMapper;
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
 * <p>
 * Accuracy Test cases for the class OnDemandMapper.
 * </p>
 *
 * @author PE,Beijing2008
 * @version 1.1
 */
public class OnDemandMapperAccuracyTestV11 extends TestCase {
    /** Represents the OnDemandMapper instance for testing. */
    private OnDemandMapper mapper = null;

    /**
     * Initialize mapper.
     */
    @Override
    public void setUp() {
        mapper = new OnDemandMapper();
    }

    /**
     * <p>
     * Accuracy test for method OnDemandMapper(OnDemandConverter[]).
     * </p>
     */
    public void testOnDemandMapper_Accuracy() {
        OnDemandConverter[] converters = new OnDemandConverter[] { new BigDecimalConverter() };
        mapper = new OnDemandMapper(converters);

        assertNotNull("Unable to instantiate OnDemandMapper.", mapper);

        OnDemandConverter[] real = mapper.getConverters();
        assertEquals("Mapper is not initialized correctly.", converters.length, real.length);
        assertEquals("Mapper is not initialized correctly.", converters[0], real[0]);
    }

    /**
     * <p>
     * Accuracy test for method OnDemandMapper(OnDemandConverter).
     * </p>
     */
    public void testAddConverter_Accuracy1() {
        OnDemandConverter converter = new BigDecimalConverter();
        assertEquals("addConverter is incorrect.", mapper.addConverter(converter), true);

        OnDemandConverter[] real = mapper.getConverters();
        assertEquals("addConverter is incorrect.", 1, real.length);
        assertEquals("addConverter is incorrect.", converter, real[0]);
    }

    /**
     * <p>
     * Accuracy test for method OnDemandMapper(OnDemandConverter).
     * </p>
     */
    public void testAddConverter_Accuracy2() {
        OnDemandConverter converter = new BigDecimalConverter();
        assertEquals("addConverter is incorrect.", mapper.addConverter(converter), true);
        assertEquals("addConverter is incorrect.", mapper.addConverter(converter), false);

        OnDemandConverter[] real = mapper.getConverters();
        assertEquals("addConverter is incorrect.", 1, real.length);
        assertEquals("addConverter is incorrect.", converter, real[0]);
    }

    /**
     * <p>
     * Accuracy test for method removeConverter(OnDemandConverter).
     * </p>
     */
    public void testRemoveConverter_Accuracy1() {
        OnDemandConverter converter = new BigDecimalConverter();
        assertEquals("removeConverter is incorrect.", mapper.removeConverter(converter), false);

        OnDemandConverter[] real = mapper.getConverters();
        assertEquals("removeConverter is incorrect.", 0, real.length);
    }

    /**
     * <p>
     * Accuracy test for method removeConverter(OnDemandConverter).
     * </p>
     */
    public void testRemoveConverter_Accuracy2() {
        OnDemandConverter converter = new BigDecimalConverter();
        mapper.addConverter(converter);
        assertEquals("removeConverter is incorrect.", mapper.removeConverter(converter), true);

        OnDemandConverter[] real = mapper.getConverters();
        assertEquals("removeConverter is incorrect.", 0, real.length);
    }

    /**
     * <p>
     * Accuracy test for method getConverters().
     * </p>
     */
    public void testGetConverters_Accuracy() {
        OnDemandConverter converter = new BigDecimalConverter();
        mapper.addConverter(converter);

        OnDemandConverter[] real = mapper.getConverters();
        assertEquals("getConverters is incorrect.", 1, real.length);
        assertEquals("getConverters is incorrect.", converter, real[0]);
    }

    /**
     * <p>
     * Accuracy test for method canConvert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testCanConvert_Accuracy() throws Exception {
        assertFalse("canConvert is incorrect.",
            mapper.canConvert(new BigDecimal("100"), 1, AccuracyTestHelper.getCRMD(), Integer.class));
    }

    /**
     * <p>
     * Accuracy test for method convert(Object, int, CustomResultSetMetaData, Class).
     * </p>
     *
     * @throws Exception into Junit.
     */
    public void testConvert_Accuracy() throws Exception {
        mapper.addConverter(new BigDecimalConverter());

        Object result = mapper.convert(new BigDecimal("100"), 1, AccuracyTestHelper.getCRMD(), Integer.class);
        assertEquals("convert() is incorrect.", result, new Integer(100));
    }

    /**
     * <p>
     * Accuracy test for method clearConverters().
     * </p>
     */
    public void testClearConverters_Accuracy() {
        mapper = OnDemandMapper.createDefaultOnDemandMapper();
        mapper.clearConverters();

        OnDemandConverter[] real = mapper.getConverters();
        assertEquals("ClearConverters is incorrect.", 0, real.length);
    }

    /**
     * <p>
     * Accuracy test for method createDefaultOnDemandMapper().
     * </p>
     */
    public void testCreateDefaultOnDemandMapper_Accuracy() {
        mapper = OnDemandMapper.createDefaultOnDemandMapper();

        OnDemandConverter[] real = mapper.getConverters();
        assertEquals("createDefaultOnDemandMapper is incorrect.", real.length, 16);

        Class[] expected = new Class[] {
                BigDecimalConverter.class, BlobConverter.class, BooleanConverter.class, ByteArrayConverter.class,
                ByteConverter.class, ClobConverter.class, DateConverter.class, DoubleConverter.class,
                FloatConverter.class, IntConverter.class, LongConverter.class, ShortConverter.class,
                StringConverter.class, TimeConverter.class, TimestampConverter.class,URLConverter.class
            };

        for (int i = 0; i < expected.length; i++) {
            boolean success = false;

            for (int j = 0; j < real.length; j++) {
                if (real[j].getClass().equals(expected[i])) {
                    success = true;
                }
            }

            if (!success) {
                fail("createDefaultOnDemandMapper is incorrect, not contain " + expected[i].getName());
            }
        }
    }
}
