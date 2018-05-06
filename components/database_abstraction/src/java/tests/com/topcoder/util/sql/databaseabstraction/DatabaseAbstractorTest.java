/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Test case for {@link DatabaseAbstractor}.
 *
 * @author justforplay, suhugoversion 2.0
 * @since 1.1
 */
public class DatabaseAbstractorTest extends TestCase {

    /**
     * Instance of DatabaseAbstractor for unit test.
     */
    private DatabaseAbstractor abstractor;

    /**
     * Database Connection.
     */
    private Connection conn;

    /**
     * Instance of ResultSet for unit test.
     */
    private ResultSet testRS;

    /**
     * Initialized instance of ResultSet for unit test.This method will
     * initialize the Connection without closing it.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void setUp() throws Exception {
        abstractor = new DatabaseAbstractor();
        conn = UnitTestHelper.getDatabaseConnection();
        testRS = UnitTestHelper.getResultSet(conn);
    }

    /**
     * Close connection initialized in detUp method.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void tearDown() throws Exception {
        testRS.close();
        UnitTestHelper.closeConnection(conn);
    }

    /**
     * <p>
     * Test Constructor DatabaseAbstractor().
     * </p>
     * <p>
     * Verify: DataBaseAbstractor can be instantiated correctly.
     * </p>
     */
    public void testDatabaseAbstractor1() {
        assertNotNull("Unable to instantiate DatabaseAbstractor.", abstractor);
        assertNull("DatabaseAbstractor is not initialized correctly.", abstractor.getMapper());
        assertNull("DatabaseAbstractor is not initialized correctly.", abstractor.getOnDemandMapper());
    }

    /**
     * <p>
     * Test DatabaseAbstractor(Mapper).
     * </p>
     * <p>
     * Verify: DataBaseAbstractor can be instantiated correctly.
     * </p>
     */
    public void testDatabaseAbstractor2() {
        Mapper mapper = new Mapper();
        DatabaseAbstractor abstractor2 = new DatabaseAbstractor(mapper);
        assertNotNull("Unable to instantiate DatabaseAbstractor.", abstractor2);
        assertEquals("DatabaseAbstractor is not initialized correctly.", abstractor2.getMapper(), mapper);
        assertNull("DatabaseAbstractor is not initialized correctly.", abstractor2.getOnDemandMapper());
    }

    /**
     * <p>
     * Test DatabaseAbstractor(Mapper,OnDemandMapper).
     * </p>
     * <p>
     * Verify: DataBaseAbstractor can be instantiated correctly.
     * </p>
     */
    public void testDatabaseAbstractor3() {
        Mapper mapper = new Mapper();
        OnDemandMapper onDemandMapper = new OnDemandMapper();
        DatabaseAbstractor abstractor2 = new DatabaseAbstractor(mapper, onDemandMapper);
        assertNotNull("Unable to instantiate DatabaseAbstractor.", abstractor2);
        assertEquals("DatabaseAbstractor is not initialized correctly.", abstractor2.getMapper(), mapper);
        assertEquals("DatabaseAbstractor is not initialized correctly.", abstractor2.getOnDemandMapper(),
                onDemandMapper);
    }

    /**
     * <p>
     * Test convertResultSet(ResultSet).
     * </p>
     * <p>
     * Verify:When resultSet is null, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvertResultSet1() throws Exception {
        try {
            abstractor.convertResultSet(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test convertResultSet(ResultSet).
     * </p>
     * <p>
     * Verify:convertResultSet is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvertResultSet2() throws Exception {
        CustomResultSet crs = abstractor.convertResultSet(testRS);
        assertNotNull("convertResultSet is incorrect.", crs);
        crs.absolute(1);
        assertEquals("convertResultSet is incorrect.", crs.getBigDecimal(1), new BigDecimal(50));
    }

    /**
     * <p>
     * Test convertResultSet(ResultSet,Mapper).
     * </p>
     * <p>
     * Verify:When resultSet is null, IllegalArgumentException is thrown.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvertResultSet3() throws Exception {
        try {
            abstractor.convertResultSet(null, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test convertResultSet(ResultSet,Mapper).
     * </p>
     * <p>
     * Verify:convertResultSet is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvertResultSet4() throws Exception {
        CustomResultSet crs = abstractor.convertResultSet(testRS, null);
        assertNotNull("convertResultSet is incorrect.", crs);
        crs.absolute(1);
        assertEquals("convertResultSet is incorrect.", crs.getBigDecimal(1), new BigDecimal(50));
    }

    /**
     * <p>
     * Test convertResultSet(ResultSet,Mapper).
     * </p>
     * <p>
     * Verify:convertResultSet is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConvertResultSet5() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("decimal", new MockConverter1());
        Mapper mapper = new Mapper(map);

        CustomResultSet crs = abstractor.convertResultSet(testRS, mapper);
        assertNotNull("convertResultSet is incorrect.", crs);
        crs.absolute(1);
        assertEquals("convertResultSet is incorrect.", new Long(crs.getLong(1)), new Long(49));
    }

    /**
     * <p>
     * Test getMapper().
     * </p>
     * <p>
     * Verify: getMapper is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetMapper() throws Exception {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("number", new MockConverter1());
        Mapper mapper = new Mapper(map);

        DatabaseAbstractor abstractor2 = new DatabaseAbstractor(mapper);
        assertEquals("getMapper is correct.", abstractor2.getMapper(), mapper);
    }

    /**
     * <p>
     * Test setMapper(Mapper).
     * </p>
     * <p>
     * Verify:setMapper() is correct.
     * </p>
     */
    public void testSetMapper() {
        Map<String, Converter> map = new HashMap<String, Converter>();
        map.put("number", new MockConverter1());
        Mapper mapper = new Mapper(map);

        DatabaseAbstractor abstractor2 = new DatabaseAbstractor(mapper);
        abstractor2.setMapper(null);
        assertNull("setMapper is correct.", abstractor2.getMapper());
        abstractor2.setMapper(mapper);
        assertEquals("setMapper is correct.", abstractor2.getMapper(), mapper);
    }

    /**
     * <p>
     * Test GetOnDemandMapper().
     * </p>
     * <p>
     * Verify: GetOnDemandMapper is correct.
     * </p>
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetOnDemandMapper() throws Exception {
        OnDemandMapper mapper = new OnDemandMapper();

        DatabaseAbstractor abstractor2 = new DatabaseAbstractor(null, mapper);
        assertEquals("GetOnDemandMapper is correct.", abstractor2.getOnDemandMapper(), mapper);
    }

    /**
     * <p>
     * Test setOnDemandMapper(OndemandMapper).
     * </p>
     * <p>
     * Verify:setOnDemandMapper() is correct.
     * </p>
     */
    public void testSetOnDemandMapper() {
        OnDemandMapper mapper = new OnDemandMapper();
        DatabaseAbstractor abstractor2 = new DatabaseAbstractor(null, mapper);
        abstractor2.setOnDemandMapper(null);
        assertNull("setMapper is correct.", abstractor2.getOnDemandMapper());
        abstractor2.setOnDemandMapper(mapper);
        assertEquals("setMapper is correct.", abstractor2.getOnDemandMapper(), mapper);
    }
}
