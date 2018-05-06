/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.failuretests;

import com.topcoder.util.sql.databaseabstraction.Converter;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;
import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;
import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.InvalidCursorStateException;
import com.topcoder.util.sql.databaseabstraction.Mapper;
import com.topcoder.util.sql.databaseabstraction.NullColumnValueException;
import com.topcoder.util.sql.databaseabstraction.OnDemandMapper;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.StringConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit TestCase that exercises the failure behavior of the Database
 * Abstraction component's CustomResultSet class.
 *
 * @author ThinMan, biotrail, gjw99
 * @version 2.0
 * @since 1.0
 */
public class CustomResultSetFailureTests extends TestCase {
    /**
     * <p>
     * An array that contains all the available type names.
     * </p>
     *
     * @since 1.0
     */
    private String[] typeNames = new String[] {"Object", "Array", "AsciiStream", "BigDecimal", "BinaryStream", "Blob",
        "Boolean", "Byte", "Bytes", "CharacterStream", "Clob", "Date", "Double", "Float", "Int", "Long", "Ref",
        "Short", "String", "Struct", "Time", "Timestamp"};

    /**
     * <p>
     * Creates a new CustomResultSetFailureTests configured to run the named
     * test.
     * </p>
     *
     * @param  testName a <code>String</code> containing the name of the test
     *         to run
     * @since 1.0
     */
    public CustomResultSetFailureTests(String testName) {
        super(testName);
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
        return new TestSuite(CustomResultSetFailureTests.class);
    }

    /**
     * <p>
     * tests the behavior of the one-arg CustomResultSet constructor when the
     * argument is null.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.0
     */
    public void testConstructor1_NullResultSet() throws Exception {
        try {
            new CustomResultSet(null);
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // The expected case
        }
    }

    /**
     * <p>
     * tests the behavior of the one-arg CustomResultSet constructor when an
     * SQLException is thrown during construction.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.0
     */
    public void testConstructor1_SQLException() throws Exception {
        try {
            new CustomResultSet(new ExceptionalResultSet());
            fail("Expected an SQLException");
        } catch (SQLException sqle) {
            // The expected case
        }
    }

    /**
     * <p>
     * tests the behavior of the two-arg CustomResultSet constructor when the
     * first argument is null.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.0
     */
    public void testConstructor2_NullResultSet() throws Exception {
        try {
            new CustomResultSet(null, new Mapper());
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // The expected case
        }
    }

    /**
     * <p>
     * tests the behavior of the two-arg CustomResultSet constructor when an
     * SQLException is thrown during construction.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.0
     */
    public void testConstructor2_SQLException() throws Exception {
        try {
            new CustomResultSet(new ExceptionalResultSet(), new Mapper());
            fail("Expected an SQLException");
        } catch (SQLException sqle) {
            // The expected case
        }
    }

    /**
     * <p>
     * tests the behavior of the three-arg CustomResultSet constructor when the
     * first argument is null.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.1
     */
    public void testConstructor3_NullResultSet() throws Exception {
        try {
            new CustomResultSet(null, new Mapper(), new OnDemandMapper());
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // The expected case
        }
    }

    /**
     * <p>
     * tests the behavior of the three-arg CustomResultSet constructor when an
     * SQLException is thrown during construction.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.1
     */
    public void testConstructor3_SQLException() throws Exception {
        try {
            new CustomResultSet(new ExceptionalResultSet(), new Mapper(), new OnDemandMapper());
            fail("Expected an SQLException");
        } catch (SQLException sqle) {
            // The expected case
        }
    }

    /**
     * <p>
     * tests the behavior of the remap method when a value cannot be cast to
     * the type specified by the new map.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.0
     */
    public void testRemap() throws Exception {
        SimpleResultSetMetaData srsmd = new SimpleResultSetMetaData();
        SimpleResultSet srs = new SimpleResultSet(srsmd);
        List<Integer> row = new ArrayList<Integer>();
        CustomResultSet crs;
        Mapper mapper = new Mapper();
        HashMap<String, Converter> map = new HashMap<String, Converter>();

        map.put("INTEGER", new Converter() {
            public Object convert(Object value, int column, CustomResultSetMetaData md) throws IllegalMappingException {
                throw new IllegalMappingException();
            }
        });
        mapper.setMap(map);

        srsmd.addColumn("column 1", Types.INTEGER, "INTEGER", "java.lang.Integer", 0, 0);
        row.add(new Integer(0));
        srs.addRow(row);
        crs = new CustomResultSet(srs);
        try {
            crs.remap(mapper);
            fail("Expected an IllegalMappingException");
        } catch (IllegalMappingException ime) {
            // The expected case
        }
    }

    /**
     * <p>
     * verifies that the get Methods for the specified type, when invoked on
     * the specified CustomResultSet with the specified column, throw
     * ClassCastExceptions.
     * </p>
     *
     * @param crs the CustomResultSet instance
     * @param type the column type name
     * @param column the column name
     *
     * @throws Exception to JUnit
     * @since 1.0
     */
    void assertGetCCE(CustomResultSet crs, String type, String column) throws Exception {
        String mName = "get" + type;
        Method indexMethod = CustomResultSet.class.getDeclaredMethod(mName, new Class[] {Integer.TYPE});
        Method nameMethod = CustomResultSet.class.getDeclaredMethod(mName, new Class[] {String.class});

        assertTrue("Expected " + crs.getRow() + " <= " + crs.getRecordCount(), crs.getRow() <= crs.getRecordCount());
        try {
            indexMethod.invoke(crs, new Object[] {new Integer(crs.findColumn(column))});
            fail("Expected a ClassCastException for " + type);
        } catch (InvocationTargetException ite) {
            assertTrue("Expected an InvalidCursorStateException",
                ite.getTargetException() instanceof ClassCastException);
        }
        try {
            nameMethod.invoke(crs, new Object[] {column});
            fail("Expected a ClassCastException for " + type);
        } catch (InvocationTargetException ite) {
            assertTrue("Expected an InvalidCursorStateException",
                ite.getTargetException() instanceof ClassCastException);
        }
    }

    /**
     * <p>
     * verifies that the get Methods for the specified type, when invoked on
     * the specified CustomResultSet with the specified column, throw
     * InvalidCursorStateExceptions.
     * </p>
     *
     * @param crs the CustomResultSet instance
     * @param type the column type name
     * @param column the column name
     *
     * @throws Exception to JUnit
     * @since 1.0
     */
    void assertGetICSE(CustomResultSet crs, String type, String column) throws Exception {
        String mName = "get" + type;
        Method indexMethod = CustomResultSet.class.getDeclaredMethod(mName, new Class[] {Integer.TYPE});
        Method nameMethod = CustomResultSet.class.getDeclaredMethod(mName, new Class[] {String.class});

        try {
            indexMethod.invoke(crs, new Object[] {new Integer(crs.findColumn(column))});
            fail("Expected an InvalidCursorStateException for " + type);
        } catch (InvocationTargetException ite) {
            assertTrue("Expected an InvalidCursorStateException",
                ite.getTargetException() instanceof InvalidCursorStateException
                || ite.getTargetException() instanceof IllegalArgumentException);
        }
        try {
            nameMethod.invoke(crs, new Object[] {column});
            fail("Expected an InvalidCursorStateException for " + type);
        } catch (InvocationTargetException ite) {
            assertTrue("Expected an InvalidCursorStateException",
                ite.getTargetException() instanceof InvalidCursorStateException
                || ite.getTargetException() instanceof IllegalArgumentException);
        }
    }

    /**
     * <p>
     * Tests the behavior of all the getXXX methods when the column requested
     * is not castable to the target type.
     * </p>
     *
     * @throws Exception to JUnit.
     * @since 1.0
     */
    public void testClassCastException() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstance2();
        assertTrue("Expected one row, found zero", crs.getRecordCount() == 1);

        assertTrue("Couldn't move to a valid row", crs.next());
        for (int i = 1; i < typeNames.length; i++) {
            assertGetCCE(crs, typeNames[i], "column1");
        }
    }

    /**
     * <p>
     * Tests the behavior of all the getXXX methods when the cursor is not on a
     * valid row.
     * </p>
     *
     * @throws Exception to JUnit.
     * @since 1.0
     */
    public void testInvalidCursorStateException() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstance();

        // Cursor is before the first row
        for (int i = 0; i < typeNames.length; i++) {
            assertGetICSE(crs, typeNames[i], "column 1");
        }

        // move after the last row
        crs.next();
        crs.next();
        for (int i = 0; i < typeNames.length; i++) {
            assertGetICSE(crs, typeNames[i], "column 1");
        }
    }

    /**
     * <p>
     * tests the ascending and descending sorts by the natural order of a single
     * column.
     * </p>
     *
     * @throws Exception to JUnit.
     * @since 1.0
     */
    public void testSimpleSorts() throws Exception {
        SimpleResultSetMetaData srsmd = new SimpleResultSetMetaData();
        SimpleResultSet srs = new SimpleResultSet(srsmd);
        List<Object> row = new ArrayList<Object>();
        CustomResultSet crs;

        srsmd.addColumn("column 1", Types.OTHER, "Object", "java.lang.Object", 0, 0);
        row.add(new Object());
        srs.addRow(row);
        row.clear();
        row.add(new Object());
        srs.addRow(row);
        crs = new CustomResultSet(srs);

        try {
            crs.sortAscending(1);
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
        try {
            crs.sortAscending("column 1");
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
        try {
            crs.sortDescending(1);
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
        try {
            crs.sortDescending("column 1");
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
    }

    /**
     * <p>
     * tests the ascending and descending sorts by the natural orders of
     * multiple columns.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.0
     */
    public void testMultiColumnSorts() throws Exception {
        SimpleResultSetMetaData srsmd = new SimpleResultSetMetaData();
        SimpleResultSet srs = new SimpleResultSet(srsmd);
        List<Object>  row = new ArrayList<Object>();
        CustomResultSet crs;

        srsmd.addColumn("column 1", Types.OTHER, "Object", "java.lang.Object", 0, 0);
        srsmd.addColumn("column 2", Types.OTHER, "Object", "java.lang.Object", 0, 0);
        row.add(new Integer(0));
        row.add(new Object());
        srs.addRow(row);
        row.clear();
        row.add(new Integer(0));
        row.add(new Object());
        srs.addRow(row);
        crs = new CustomResultSet(srs);

        try {
            crs.sortAscending(new int[] {1, 2});
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
        try {
            crs.sortAscending(new String[] {"column 1", "column 2"});
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
        try {
            crs.sortDescending(new int[] {1, 2});
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
        try {
            crs.sortDescending(new String[] {"column 1", "column 2"});
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
    }

    /**
     * <p>
     * tests the ascending and descending sorts by a single column, according to
     * the order imposed by a Comparator.
     * </p>
     *
     * @throws Exception to JUnit.
     * @since 1.0
     */
    public void testSortWithComparator() throws Exception {
        SimpleResultSetMetaData srsmd = new SimpleResultSetMetaData();
        SimpleResultSet srs = new SimpleResultSet(srsmd);
        List<Object> row = new ArrayList<Object>();
        Comparator<Object> comp = new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                throw new ClassCastException();
            }
        };
        CustomResultSet crs;

        srsmd.addColumn("column 1", Types.OTHER, "Object", "java.lang.Object", 0, 0);
        row.add(new Object());
        srs.addRow(row);
        row.clear();
        row.add(new Object());
        srs.addRow(row);
        crs = new CustomResultSet(srs);

        try {
            crs.sortAscending(1, comp);
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
        try {
            crs.sortAscending("column 1", comp);
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
        try {
            crs.sortDescending(1, comp);
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
        try {
            crs.sortDescending("column 1", comp);
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
    }

    /**
     * <p>
     * tests the ascending and descending sorts by multiple columns, according
     * to the order imposed by multiple Comparators.
     * </p>
     *
     * @throws Exception to JUnit.
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
	public void testSortWithComparators() throws Exception {
        SimpleResultSetMetaData srsmd = new SimpleResultSetMetaData();
        SimpleResultSet srs = new SimpleResultSet(srsmd);
        List<Object> row = new ArrayList<Object>();
        Comparator<Object>[] comps = new Comparator[] {null,
            new Comparator<Object>() {
                public int compare(Object o1, Object o2) {
                    throw new ClassCastException();
                }
            }
        };
        CustomResultSet crs;

        srsmd.addColumn("column 1", Types.OTHER, "Object", "java.lang.Object", 0, 0);
        srsmd.addColumn("column 2", Types.OTHER, "Object", "java.lang.Object", 0, 0);
        row.add(new Integer(0));
        row.add(new Object());
        srs.addRow(row);
        row.clear();
        row.add(new Integer(0));
        row.add(new Object());
        srs.addRow(row);
        crs = new CustomResultSet(srs);

        try {
            crs.sortAscending(new int[] {1, 2}, comps);
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
        try {
            crs.sortAscending(new String[] {"column 1", "column 2"}, comps);
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
        try {
            crs.sortDescending(new int[] {1, 2}, comps);
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
        try {
            crs.sortDescending(new String[] {"column 1", "column 2"}, comps);
            fail("Expected a ClassCastException");
        } catch (ClassCastException cce) {
            // The expected case
        }
    }

    /**
     * <p>
     * Tests ctor CustomResultSet#isAvailable(int,Class) for failure.
     * Expects InvalidCursorStateException.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.1
     */
    public void testIsAvailable1_InvalidCursor() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstance();

        // Cursor is before the first row
        try {
            crs.isAvailable(1, Long.class);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }

        // move after the last row
        crs.next();
        crs.next();
        try {
            crs.isAvailable(1, Long.class);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor CustomResultSet#isAvailable(String,Class) for failure.
     * Expects InvalidCursorStateException.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.1
     */
    public void testIsAvailable2_InvalidCursor() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstance();

        // Cursor is before the first row
        try {
            crs.isAvailable("column1", Long.class);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }

        // move after the last row
        crs.next();
        crs.next();
        try {
            crs.isAvailable("column1", Long.class);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor CustomResultSet#getObject(int,Class) for failure.
     * It tests the case when desiredType is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.1
     */
    public void testGetObject1_NullDesiredType() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstance();
        crs.next();
        try {
            crs.getObject(1, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor CustomResultSet#getObject(int,Class) for failure.
     * It tests the case when the current cursor is invalid and expects InvalidCursorStateException.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.1
     */
    public void testGetObject1_InvalidCursor() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstance();
        try {
            crs.getObject(1, String.class);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor CustomResultSet#getObject(int,Class) for failure.
     * It tests the case when the conversion fails and expects ClassCastException.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.1
     */
    public void testGetObject1_ClassCastException() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstance();
        crs.next();
        try {
            crs.getObject(1, Long.class);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor CustomResultSet#getObject(String,Class) for failure.
     * It tests the case when desiredType is null and expects IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.1
     */
    public void testGetObject2_NullDesiredType() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstance();
        crs.next();
        try {
            crs.getObject("column1", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor CustomResultSet#getObject(String,Class) for failure.
     * It tests the case when the current cursor is invalid and expects InvalidCursorStateException.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.1
     */
    public void testGetObject2_InvalidCursor() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstance();
        try {
            crs.getObject("column1", String.class);
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method CustomResultSet#getObject(String,Class) for failure.
     * It tests the case when the conversion fails and expects ClassCastException.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 1.1
     */
    public void testGetObject2_ClassCastException() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstance();
        crs.next();
        try {
            crs.getObject("column1", Long.class);
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method CustomResultSet#getURL(String) for failure.
     * It tests the case when the current cursor is invalid and expects InvalidCursorStateException.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 2.0
     */
    public void testGetURL1_InvalidCursor() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstance();
        try {
            crs.getURL("column1");
            fail("InvalidCursorStateException expected.");
        } catch (InvalidCursorStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method CustomResultSet#getURL(String) for failure.
     * It tests the case when the conversion fails and expects ClassCastException.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 2.0
     */
    public void testGetURL2_ClassCastException() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstance();
        crs.next();
        try {
            crs.getURL("column1");
            fail("ClassCastException expected.");
        } catch (ClassCastException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method CustomResultSet#getBoolean(int) for failure.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 2.0
     */
    public void testGetBoolean_NullColumnValue() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstanceNullValue();
        crs.next();
        try {
            crs.getBoolean(1);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method CustomResultSet#getByte(int) for failure.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 2.0
     */
    public void testGetByte_NullColumnValue() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstanceNullValue();
        crs.next();
        try {
            crs.getByte(1);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method CustomResultSet#getDate(int) for failure.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 2.0
     */
    public void testGetDate_NullColumnValue() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstanceNullValue();
        crs.next();
        try {
            crs.getDate(1, Calendar.getInstance());
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method CustomResultSet#getDouble(int) for failure.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 2.0
     */
    public void testGetDouble_NullColumnValue() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstanceNullValue();
        crs.next();
        try {
            crs.getDouble(1);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method CustomResultSet#getFloat(int) for failure.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 2.0
     */
    public void testGetFloat_NullColumnValue() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstanceNullValue();
        crs.next();
        try {
            crs.getFloat(1);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method CustomResultSet#getInt(int) for failure.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 2.0
     */
    public void testGetInt_NullColumnValue() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstanceNullValue();
        crs.next();
        try {
            crs.getInt(1);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method CustomResultSet#getLong(int) for failure.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 2.0
     */
    public void testGetLong_NullColumnValue() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstanceNullValue();
        crs.next();
        try {
            crs.getLong(1);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method CustomResultSet#getShort(int) for failure.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 2.0
     */
    public void testGetShort_NullColumnValue() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstanceNullValue();
        crs.next();
        try {
            crs.getShort(1);
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method CustomResultSet#getTime(int, Calendar) for failure.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 2.0
     */
    public void testGetTime_NullColumnValue() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstanceNullValue();
        crs.next();
        try {
            crs.getTime(1, Calendar.getInstance());
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests method CustomResultSet#getTimestamp(int, Calendar) for failure.
     * </p>
     *
     * @throws Exception to JUnit
     * @since 2.0
     */
    public void testGetTimestamp_NullColumnValue() throws Exception {
        CustomResultSet crs = getCumstomResultSetInstanceNullValue();
        crs.next();
        try {
            crs.getTimestamp(1, Calendar.getInstance());
            fail("NullColumnValueException expected.");
        } catch (NullColumnValueException e) {
            // good
        }
    }

    /**
     * <p>
     * Creates a new CustomResultSet instance for testing.
     * </p>
     *
     * @return a new CustomResultSet instance.
     * @throws Exception to JUnit
     * @since 2.0
     */
    private CustomResultSet getCumstomResultSetInstanceNullValue() throws Exception {
        SimpleResultSetMetaData srsmd = new SimpleResultSetMetaData();
        SimpleResultSet srs = new SimpleResultSet(srsmd);
        List<String> row = new ArrayList<String>();

        srsmd.addColumn("column1", Types.VARCHAR, "VARCHAR(255)", "java.lang.String", 0, 0);
        row.add(null);
        srs.addRow(row);
        OnDemandMapper onDemandMapper = new OnDemandMapper();
        onDemandMapper.addConverter(new StringConverter());
        return new CustomResultSet(srs, new Mapper(), onDemandMapper);
    }

    /**
     * <p>
     * Creates a new CustomResultSet instance for testing.
     * </p>
     *
     * @return a new CustomResultSet instance.
     * @throws Exception to JUnit
     * @since 1.1
     */
    private CustomResultSet getCumstomResultSetInstance() throws Exception {
        SimpleResultSetMetaData srsmd = new SimpleResultSetMetaData();
        SimpleResultSet srs = new SimpleResultSet(srsmd);
        List<String> row = new ArrayList<String>();

        srsmd.addColumn("column1", Types.VARCHAR, "VARCHAR(255)", "java.lang.String", 0, 0);
        row.add("value");
        srs.addRow(row);
        OnDemandMapper onDemandMapper = new OnDemandMapper();
        onDemandMapper.addConverter(new StringConverter());
        return new CustomResultSet(srs, new Mapper(), onDemandMapper);
    }

    /**
     * <p>
     * Creates a new CustomResultSet instance for testing.
     * </p>
     *
     * <p>
     * This method constructs a new CustomResultSet instance with null OnDemandMapper.
     * </p>
     *
     * @return a new CustomResultSet instance.
     * @throws Exception to JUnit
     * @since 1.1
     */
    private CustomResultSet getCumstomResultSetInstance2() throws Exception {
        SimpleResultSetMetaData srsmd = new SimpleResultSetMetaData();
        SimpleResultSet srs = new SimpleResultSet(srsmd);
        List<Object> row = new ArrayList<Object>();

        srsmd.addColumn("column1", Types.OTHER, "Object", "java.lang.Object", 0, 0);
        row.add(new Object());
        srs.addRow(row);
        return new CustomResultSet(srs);
    }
}
