/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.failuretests.ondemandconversion;

import java.net.URL;
import java.sql.Time;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.failuretests.FailureHelper;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.URLConverter;

/**
 * <p>
 * Failure test cases for URLConverter.
 * </p>
 *
 * @author gjw99
 * @version 2.0
 * @since 2.0
 */
public class URLConverterFailureTests extends TestCase {
    /**
     * <p>
     * The value object for testing.
     * </p>
     */
    private Object value;

    /**
     * <p>
     * URLConverter instance for testing.
     * </p>
     */
    private URLConverter converter;

    /**
     * <p>
     * Setup test environment.
     * </p>
     * @throws Exception if any error
     */
    protected void setUp() throws Exception {
        value = new URL("http://test.com");
        converter = new URLConverter();
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    protected void tearDown() {
        converter = null;
        value = null;
    }

    /**
     * <p>
     * Return all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(URLConverterFailureTests.class);
    }

    /**
     * <p>
     * Tests URLConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when value is null and expects IllegalMappingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_NullValue() throws Exception {
        try {
            converter.convert(null, 1, FailureHelper.getCustomResultSetMetaDataInstance(), Double.class);
            fail("IllegalMappingException expected.");
        } catch (IllegalMappingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests URLConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_InvalidColumn1() throws Exception {
        try {
            converter.convert(value, 0, null, Double.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests URLConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_InvalidColumn2() throws Exception {
        try {
            converter.convert(value, 45, null, Double.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests URLConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when metaData is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_NullMetaData() throws Exception {
        try {
            converter.convert(value, 1, null, Double.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests URLConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when desiredType is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_NullDesiredType() throws Exception {
        try {
            converter.convert(value, 1, FailureHelper.getCustomResultSetMetaDataInstance(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests URLConverter#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * Expects IllegalMappingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_UnknownDesiredType() throws Exception {
        try {
            converter.convert(value, 1, FailureHelper.getCustomResultSetMetaDataInstance(), Time.class);
            fail("IllegalMappingException expected.");
        } catch (IllegalMappingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests URLConverter#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCanConvert_InvalidColumn1() throws Exception {
        try {
            converter.canConvert(value, 0, FailureHelper.getCustomResultSetMetaDataInstance(), Double.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests URLConverter#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCanConvert_InvalidColumn2() throws Exception {
        try {
            converter.canConvert(value, 45, FailureHelper.getCustomResultSetMetaDataInstance(), Double.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests URLConverter#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when metaData is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCanConvert_NullMetaData() {
        try {
            converter.canConvert(value, 1, null, Double.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests URLConverter#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when desiredType is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCanConvert_NullDesiredType() throws Exception {
        try {
            converter.canConvert(value, 1, FailureHelper.getCustomResultSetMetaDataInstance(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }
}