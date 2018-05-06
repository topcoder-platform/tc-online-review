/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.failuretests;

import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.OnDemandConverter;
import com.topcoder.util.sql.databaseabstraction.OnDemandMapper;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.StringConverter;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Failure test cases for OnDemandMapper.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class OnDemandMapperFailureTests extends TestCase {
    /**
     * <p>
     * OnDemandMapper instance for testing.
     * </p>
     */
    private OnDemandMapper mapper;

    /**
     * <p>
     * Setup test environment.
     * </p>
     *
     */
    protected void setUp() {
        mapper = new OnDemandMapper();
        mapper.addConverter(new StringConverter());
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    protected void tearDown() {
        mapper = null;
    }

    /**
     * <p>
     * Return all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(OnDemandMapperFailureTests.class);
    }

    /**
     * <p>
     * Tests ctor OnDemandMapper#OnDemandMapper(OnDemandConverter[]) for failure.
     * It tests the case that when converters is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullConverters() {
        try {
            new OnDemandMapper(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor OnDemandMapper#OnDemandMapper(OnDemandConverter[]) for failure.
     * It tests the case that when converters contains null element and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullConverterInConverters() {
        try {
            new OnDemandMapper(new OnDemandConverter[] {null, new StringConverter()});
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests OnDemandMapper#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column index is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_InvalidColumn1() throws Exception {
        try {
            mapper.convert("88", 0, FailureHelper.getCustomResultSetMetaDataInstance(), Long.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests OnDemandMapper#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column index is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_InvalidColumn2() throws Exception {
        try {
            mapper.convert("88", 34, FailureHelper.getCustomResultSetMetaDataInstance(), Long.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests OnDemandMapper#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when metaData is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_NullMetaData() throws Exception {
        try {
            mapper.convert("88", 2, null, Long.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests OnDemandMapper#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when desiredType is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_NullDesiredType() throws Exception {
        try {
            mapper.convert("value", 2, FailureHelper.getCustomResultSetMetaDataInstance(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests OnDemandMapper#convert(Object,int,CustomResultSetMetaData,Class) for failure.
     * Expects IllegalMappingException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConvert_IllegalMappingException() throws Exception {
        try {
            mapper.convert("value", 2, FailureHelper.getCustomResultSetMetaDataInstance(), Long.class);
            fail("IllegalMappingException expected.");
        } catch (IllegalMappingException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests OnDemandMapper#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column index is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCanConvert_InvalidColumn1() throws Exception {
        try {
            mapper.canConvert("88", 0, FailureHelper.getCustomResultSetMetaDataInstance(), Long.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests OnDemandMapper#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when column index is invalid and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCanConvert_InvalidColumn2() throws Exception {
        try {
            mapper.canConvert("88", 34, FailureHelper.getCustomResultSetMetaDataInstance(), Long.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests OnDemandMapper#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when metaData is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCanConvert_NullMetaData() {
        try {
            mapper.canConvert("88", 2, null, Long.class);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests OnDemandMapper#canConvert(Object,int,CustomResultSetMetaData,Class) for failure.
     * It tests the case that when desiredType is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCanConvert_NullDesiredType() throws Exception {
        try {
            mapper.canConvert("88", 2, FailureHelper.getCustomResultSetMetaDataInstance(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests OnDemandMapper#addConverter(OnDemandConverter) for failure.
     * It tests the case that when converter is null and expects IllegalArgumentException.
     * </p>
     */
    public void testAddConverter_NullConverter() {
        try {
            mapper.addConverter(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests OnDemandMapper#removeConverter(OnDemandConverter) for failure.
     * It tests the case that when converter is null and expects IllegalArgumentException.
     * </p>
     */
    public void testRemoveConverter_NullConverter() {
        try {
            mapper.removeConverter(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

}