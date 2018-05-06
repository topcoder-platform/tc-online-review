/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.accuracytests;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import junit.framework.TestCase;

import com.topcoder.util.sql.databaseabstraction.CustomResultSet;
import com.topcoder.util.sql.databaseabstraction.DatabaseAbstractor;
import com.topcoder.util.sql.databaseabstraction.Mapper;
import com.topcoder.util.sql.databaseabstraction.OnDemandMapper;


/**
 * <p>
 * Accuracy Test cases for the class DatabaseAbstractor.
 * </p>
 *
 * @author PE
 * @version 1.1
 */
public class DatabaseAbstractorAccuracyTestV11 extends TestCase {
    /** Represents the DatabaseAbstractor instance for testing. */
    private DatabaseAbstractor abstractor;

    /** Represents the Connection instance for testing. */
    private Connection conn;

    /** Represents the ResultSet instance for testing. */
    private ResultSet rs;

    /**
     * Creates the testing instances.
     *
     * @throws Exception exception to JUnit.
     */
    public void setUp() throws Exception {
        abstractor = new DatabaseAbstractor();
        conn = AccuracyTestHelper.getConnection();
        rs = AccuracyTestHelper.getResultSet(conn);
    }

    /**
     * Close connection initialized in setUp method.
     *
     * @throws Exception exception to JUnit.
     */
    public void tearDown() throws Exception {
        rs.close();
        conn.close();
    }

    /**
     * <p>
     * Accuracy test for method DatabaseAbstractor().
     * </p>
     */
    public void testDatabaseAbstractor_Accuracy1() {
        assertNotNull("Unable to instantiate DatabaseAbstractor.", abstractor);
        assertNull("DatabaseAbstractor is not initialized correctly.", abstractor.getMapper());
        assertNull("DatabaseAbstractor is not initialized correctly.", abstractor.getOnDemandMapper());
    }

    /**
     * <p>
     * Accuracy test for method DatabaseAbstractor(Mapper).
     * </p>
     */
    public void testDatabaseAbstractor_Accuracy2() {
        Mapper mapper = new Mapper();
        abstractor = new DatabaseAbstractor(mapper);
        assertNotNull("Unable to instantiate DatabaseAbstractor.", abstractor);
        assertEquals("DatabaseAbstractor is not initialized correctly.", abstractor.getMapper(), mapper);
        assertNull("DatabaseAbstractor is not initialized correctly.", abstractor.getOnDemandMapper());
    }

    /**
     * <p>
     * Accuracy test for method DatabaseAbstractor(Mapper,OnDemandMapper).
     * </p>
     */
    public void testDatabaseAbstractor_Accuracy3() {
        Mapper mapper = new Mapper();
        OnDemandMapper onDemandMapper = new OnDemandMapper();
        abstractor = new DatabaseAbstractor(mapper, onDemandMapper);
        assertNotNull("Unable to instantiate DatabaseAbstractor.", abstractor);
        assertEquals("DatabaseAbstractor is not initialized correctly.", abstractor.getMapper(), mapper);
        assertEquals("DatabaseAbstractor is not initialized correctly.", abstractor.getOnDemandMapper(), onDemandMapper);
    }

    /**
     * <p>
     * Accuracy test for method convertResultSet(ResultSet).
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testConvertResultSet_Accuracy1() throws Exception {
        CustomResultSet crs = abstractor.convertResultSet(rs);
        crs.absolute(1);
        assertEquals("convertResultSet is incorrect.", crs.getInt(2), 1);
    }

    /**
     * <p>
     * Accuracy test for method convertResultSet(ResultSet, Mapper).
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testConvertResultSet_Accuracy2() throws Exception {
        CustomResultSet crs = abstractor.convertResultSet(rs, null);
        crs.absolute(1);
        assertEquals("convertResultSet is incorrect.", crs.getInt(2), 1);
    }

    /**
     * <p>
     * Accuracy test for method getMapper().
     * </p>
     */
    public void testGetMapper_Accuracy() {
        Mapper mapper = new Mapper(new HashMap());

        abstractor = new DatabaseAbstractor(mapper);
        assertEquals("getMapper is not correct.", abstractor.getMapper(), mapper);
    }

    /**
     * <p>
     * Accuracy test for method setMapper(Mapper).
     * </p>
     */
    public void testSetMapper_Accuracy() {
        Mapper mapper = new Mapper(new HashMap());
        abstractor.setMapper(mapper);
        assertEquals("setMapper is not correct.", abstractor.getMapper(), mapper);
    }

    /**
     * <p>
     * Accuracy test for method getOnDemandMapper().
     * </p>
     */
    public void testGetOnDemandMapper_Accuracy() {
        OnDemandMapper mapper = new OnDemandMapper();
        abstractor = new DatabaseAbstractor(null, mapper);
        assertEquals("getOnDemandMapper is not correct.", abstractor.getOnDemandMapper(), mapper);
    }

    /**
     * <p>
     * Accuracy test for method setOnDemandMapper(OnDemandMapper).
     * </p>
     */
    public void testSetOnDemandMapper_Accuracy() {
        OnDemandMapper mapper = new OnDemandMapper();
        abstractor.setOnDemandMapper(mapper);
        assertEquals("setMapper is not correct.", abstractor.getOnDemandMapper(), mapper);
    }
}
