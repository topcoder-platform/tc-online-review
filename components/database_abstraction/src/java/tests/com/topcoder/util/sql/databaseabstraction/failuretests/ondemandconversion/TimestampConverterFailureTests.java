/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion;

import java.sql.Timestamp;

import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.failuretests.FailureHelper;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.TimestampConverter;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Failure test cases for TimestampConverter.
 * </p>
 *
 * @author biotrail
 * @version 1.1
 * @since 1.1
 */
public class TimestampConverterFailureTests extends TestCase {
    /**
     * <p>
     * The value object for testing.
     * </p>
     */
    private static final Object VALUE = new Timestamp(System.currentTimeMillis());

    /**
     * <p>
     * TimestampConverter instance for testing.
     * </p>
     */
    private TimestampConverter converter;

    /**
     * <p>
     * Setup test environment.
     * </p>
     *
     */
    protected void setUp() {
        converter = new TimestampConverter();
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    protected void tearDown() {
        converter = null;
    }

    /**
     * <p>
     * Return all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(TimestampConverterFailureTests.class);
    }

    /**
     * <p>
     * Tests TimestampConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when value is null and expects IllegalMappingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_NullValue() throws Exception {
        try {
            converter.convert(null, 1, FailureHelper.getCustomResultSetMetaDataInstance(), String.class);
            fail("IllegalMappingException expected.");
        } catch (IllegalMappingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests TimestampConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_InvalidColumn1() throws Exception {
        try {
            converter.convert(VALUE, 0, null, String.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TimestampConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_InvalidColumn2() throws Exception {
        try {
            converter.convert(VALUE, 45, null, String.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TimestampConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when metaData is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_NullMetaData() throws Exception {
        try {
            converter.convert(VALUE, 1, null, String.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TimestampConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when desiredType is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_NullDesiredType() throws Exception {
        try {
            converter.convert(VALUE, 1, FailureHelper.getCustomResultSetMetaDataInstance(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TimestampConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * Expects IllegalMappingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_UnknownDesiredType() throws Exception {
        try {
            converter.convert(VALUE, 1, FailureHelper.getCustomResultSetMetaDataInstance(), Double.class);
            fail("IllegalMappingException expected.");
        } catch (IllegalMappingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests TimestampConverter#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCanConvert_InvalidColumn1() throws Exception {
        try {
            converter.canConvert(VALUE, 0, FailureHelper.getCustomResultSetMetaDataInstance(), String.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TimestampConverter#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCanConvert_InvalidColumn2() throws Exception {
        try {
            converter.canConvert(VALUE, 45, FailureHelper.getCustomResultSetMetaDataInstance(), String.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TimestampConverter#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when metaData is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCanConvert_NullMetaData() {
        try {
            converter.canConvert(VALUE, 1, null, String.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TimestampConverter#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when desiredType is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCanConvert_NullDesiredType() throws Exception {
        try {
            converter.canConvert(VALUE, 1, FailureHelper.getCustomResultSetMetaDataInstance(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }
}