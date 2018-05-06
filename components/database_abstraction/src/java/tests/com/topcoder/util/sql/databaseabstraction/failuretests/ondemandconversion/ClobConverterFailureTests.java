/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion;

import java.io.InputStream;
import java.sql.Time;

import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.failuretests.FailureHelper;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ClobConverter;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Failure test cases for ClobConverter.
 * </p>
 *
 * @author biotrail
 * @version 1.1
 * @since 1.1
 */
public class ClobConverterFailureTests extends TestCase {
    /**
     * <p>
     * The value object for testing.
     * </p>
     */
    private static final Object VALUE = new DummyClob();

    /**
     * <p>
     * ClobConverter instance for testing.
     * </p>
     */
    private ClobConverter converter;

    /**
     * <p>
     * Setup test environment.
     * </p>
     *
     */
    protected void setUp() {
        converter = new ClobConverter();
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
        return new TestSuite(ClobConverterFailureTests.class);
    }

    /**
     * <p>
     * Tests ClobConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when value is null and expects IllegalMappingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_NullValue() throws Exception {
        try {
            converter.convert(null, 1, FailureHelper.getCustomResultSetMetaDataInstance(), InputStream.class);
            fail("IllegalMappingException expected.");
        } catch (IllegalMappingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ClobConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_InvalidColumn1() throws Exception {
        try {
            converter.convert(VALUE, 0, null, InputStream.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ClobConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_InvalidColumn2() throws Exception {
        try {
            converter.convert(VALUE, 45, null, InputStream.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ClobConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when metaData is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_NullMetaData() throws Exception {
        try {
            converter.convert(VALUE, 1, null, InputStream.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ClobConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
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
     * Tests ClobConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * Expects IllegalMappingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_UnknownDesiredType() throws Exception {
        try {
            converter.convert(VALUE, 1, FailureHelper.getCustomResultSetMetaDataInstance(), Time.class);
            fail("IllegalMappingException expected.");
        } catch (IllegalMappingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests ClobConverter#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCanConvert_InvalidColumn1() throws Exception {
        try {
            converter.canConvert(VALUE, 0, FailureHelper.getCustomResultSetMetaDataInstance(), InputStream.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ClobConverter#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCanConvert_InvalidColumn2() throws Exception {
        try {
            converter.canConvert(VALUE, 45, FailureHelper.getCustomResultSetMetaDataInstance(), InputStream.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ClobConverter#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when metaData is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCanConvert_NullMetaData() {
        try {
            converter.canConvert(VALUE, 1, null, InputStream.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ClobConverter#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
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