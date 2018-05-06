/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.failuretests;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.topcoder.util.sql.databaseabstraction.Converter;
import com.topcoder.util.sql.databaseabstraction.DatabaseAbstractor;
import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;
import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.Mapper;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * A JUnit TestCase that exercises the failure behavior of the Database
 * Abstraction component's DatabaseAbstractor class.
 * </p>
 *
 * @author ThinMan, biotrail, gjw99
 * @version 2.0
 * @since 1.0
 */
public class DatabaseAbstractorFailureTests extends TestCase {
    /**
     * <p>
     * DatabaseAbstractor instance for testing.
     * </p>
     */
    private DatabaseAbstractor abstractor;

    /**
     * <p>
     * Creates a new DatabaseAbstractorFailureTests configured to run the named
     * test.
     * </p>
     *
     * @param  testName a <code>String</code> containing the name of the test
     *         to run
     *
     * @since 1.0
     */
    public DatabaseAbstractorFailureTests(String testName) {
        super(testName);
    }

    /**
     * <p>
     * Prepares this <code>DatabaseAbstractorFailureTests</code> instance for use.
     * </p>
     *
     * @throws Exception to JUnit.
     *
     * @since 1.0
     */
    public void setUp() throws Exception {
        abstractor = new DatabaseAbstractor();
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @since 1.1
     */
    protected void tearDown() {
        abstractor = null;
    }

    /**
     * <p>
     * Creates and returns a <code>Test</code> suitable for running all the
     * tests defined by this class.
     * </p>
     *
     * @return a <code>Test</code> that runs all the tests defined by this class
     * @since 1.0
     */
    public static Test suite() {
        return new TestSuite(DatabaseAbstractorFailureTests.class);
    }

    /**
     * <p>
     * Tests the behavior of the DatabaseAbstractor class when it is asked to
     * convert a null ResultSet.
     * </p>
     *
     * @throws Exception to JUnit.
     * @since 1.0
     */
    public void testConvert_Null() throws Exception {
        try {
            abstractor.convertResultSet(null);
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // The expected case
        }
    }

    /**
     * Tests the behavior of the DatabaseAbstractor class when asked to cvonvert
     * a ResultSet, during which procedure an SQLException is thrown.
     *
     * @throws Exception to JUnit.
     * @since 1.0
     */
    public void testConvert_Exception() throws Exception {
        try {
            abstractor.convertResultSet(new ExceptionalResultSet());
            fail("Expected an SQLException");
        } catch (SQLException sqle) {
            // The expected case
        }
        try {
            abstractor.convertResultSet(new ExceptionalResultSet(), new Mapper());
            fail("Expected an SQLException");
        } catch (SQLException sqle) {
            // The expected case
        }
    }

    /**
     * <p>
     * Tests the behavior of the DatabaseAbstractor class when asked to convert
     * a ResultSet with the aid of a specified Mapper, and an illegal mapping
     * is encountered.
     * </p>
     *
     * @throws Exception to JUnit.
     * @since 1.0
     */
    public void testConcvert_WithMapper_BadMapping() throws Exception {
        SimpleResultSetMetaData srsmd = new SimpleResultSetMetaData();
        SimpleResultSet srs = new SimpleResultSet(srsmd);
        List<Integer> row = new ArrayList<Integer>();
        Mapper mapper = new Mapper();
        Map<String, Converter> map = new HashMap<String, Converter>();

        map.put("INTEGER", new Converter() {
            public Object convert(Object value, int column, CustomResultSetMetaData md) throws IllegalMappingException {
                throw new IllegalMappingException();
            }
        });
        mapper.setMap(map);

        srsmd.addColumn("column 1", Types.INTEGER, "INTEGER", "java.lang.Integer", 0, 0);
        row.add(new Integer(0));
        srs.addRow(row);
        try {
            abstractor.convertResultSet(srs, mapper);
            fail("Expected an IllegalMappingException");
        } catch (IllegalMappingException ime) {
            // The expected case
        }
    }
}
