/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.accuracytests;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.topcoder.util.sql.databaseabstraction.CustomResultSet;
import com.topcoder.util.sql.databaseabstraction.Mapper;
import com.topcoder.util.sql.databaseabstraction.OnDemandMapper;


/**
 * <p>
 * Accuracy Test cases for the class CustomResultSet.
 * </p>
 *
 * @author PE
 * @version 1.1
 */
public class CustomResultSetAccuracyTestV11 extends TestCase {
    /** Represents the Connection instance for testing. */
    private Connection conn;

    /** Represents the ResultSet instance for testing. */
    private ResultSet rs;

    /** Represents the CustomResultSet instance for testing. */
    private CustomResultSet resultSet;

    /** Represents the Map instance for testing. */
    private Map map;

    /**
     * Creates the testing instances.
     *
     * @throws Exception exception to JUnit.
     */
    protected void setUp() throws Exception {
        conn = AccuracyTestHelper.getConnection();
        rs = AccuracyTestHelper.getResultSet(conn);

        map = new HashMap();
        map.put("VARCHAR", new MockConverter());
        resultSet = new CustomResultSet(rs, new Mapper(map));
        rs = AccuracyTestHelper.getResultSet(conn);
    }

    /**
     * Close connection initialized in setUp method.
     *
     * @throws Exception exception to JUnit.
     */
    protected void tearDown() throws Exception {
        rs.close();
        conn.close();
    }

    /**
     * <p>
     * Accuracy test for method getObject(int).
     * </p>
     */
    public void testGetObject_Accuracy1() throws Exception {
        resultSet.absolute(1);
        assertNull("getObject is incorrect.", resultSet.getObject(0));
    }

    /**
     * <p>
     * Accuracy test for method getObject(int).
     * </p>
     */
    public void testGetObject_Accuracy2() throws Exception {
        resultSet.absolute(1);
        assertEquals("getObject is incorrect.", "Bdgidgff" + "00000.1", resultSet.getObject(1));
    }

    /**
     * <p>
     * Accuracy test for method getObject(int).
     * </p>
     */
    public void testGetObject_Accuracy3() throws Exception {
        resultSet.absolute(1);
        assertEquals("getObject is incorrect.", 1, resultSet.getInt(2));
    }

    /**
     * <p>
     * Accuracy test for method getObject(int).
     * </p>
     */
    public void testGetObject_Accuracy4() throws Exception {
        resultSet.absolute(1);
        assertEquals("getObject is incorrect.", "89" + "00000.1", resultSet.getString(3));
    }

    /**
     * <p>
     * Accuracy test for method getObject(int).
     * </p>
     */
    public void testGetObject_Accuracy5() throws Exception {
        resultSet.absolute(1);
        assertEquals("getObject is incorrect.", Date.valueOf("1999-11-20"), resultSet.getTimestamp(4));
    }

    /**
     * <p>
     * Accuracy test for method getObject(int).
     * </p>
     */
    public void testGetObjectClass_Accuracy1() throws Exception {
        resultSet.absolute(1);
        assertEquals("getObject is incorrect.", "Bdgidgff" + "00000.1", resultSet.getObject(1, String.class));
    }

    /**
     * <p>
     * Accuracy test for method getObject(int).
     * </p>
     */
    public void testGetObjectClass_Accuracy2() throws Exception {
        resultSet.absolute(1);
        assertEquals("getObject is incorrect.", new Integer(1), resultSet.getObject(2, Integer.class));
    }

    /**
     * <p>
     * Accuracy test for method getObject(int).
     * </p>
     */
    public void testGetObjectClass_Accuracy3() throws Exception {
        resultSet = new CustomResultSet(rs, new Mapper(map), OnDemandMapper.createDefaultOnDemandMapper());
        resultSet.absolute(1);
        assertEquals("getObject is incorrect.", new Integer(89), resultSet.getObject(3, Integer.class));
    }

    /**
     * <p>
     * Accuracy test for method getObject(int).
     * </p>
     */
    public void testGetObjectClass_Accuracy4() throws Exception {
        resultSet = new CustomResultSet(rs, new Mapper(map), OnDemandMapper.createDefaultOnDemandMapper());
        resultSet.absolute(1);
        assertEquals("getObject is incorrect.", new BigDecimal(89), resultSet.getObject(3, BigDecimal.class));
    }
}
