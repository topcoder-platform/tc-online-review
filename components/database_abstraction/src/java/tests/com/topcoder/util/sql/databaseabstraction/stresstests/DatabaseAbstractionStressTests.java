/**
 * DatabaseAbstractionStressTests.java
 *
 * May 22, 2003
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.sql.databaseabstraction.stresstests;

import com.topcoder.util.sql.databaseabstraction.Converter;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;
import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;
import com.topcoder.util.sql.databaseabstraction.DatabaseAbstractor;
import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;
import com.topcoder.util.sql.databaseabstraction.InvalidCursorStateException;
import com.topcoder.util.sql.databaseabstraction.Mapper;
import com.topcoder.util.sql.databaseabstraction.NullColumnValueException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A suite to test the behavior or the Database Abstraction package
 * with a table with a large number of entries and with several instances
 * running concurrently on separate threads.  See readme.txt for
 * information on the table that must be present in the database
 * in order for these tests to run.
 */
public class DatabaseAbstractionStressTests extends TestCase {

    /* used during testing */
    //private String driverName = "com.mysql.jdbc.Driver";
    //private String sourceName = "jdbc:mysql://localhost/tester?user=topcoder";

    /*
     * match values in unit tests
     */
    /**
     * The driver to use when connecting to the database
     */
    private String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
    /**
     * Connect to this database
     */
    private String sourceName = "jdbc:odbc:Tester";

    private final String query = "SELECT ID, a, b, c, d, e, f, g, h, i, j, k, l, m, o FROM stress_tests";

    private DatabaseAbstractor da;
    private Connection connection;
    private Statement statement;
    private StringBuffer sbShort;
    private Mapper mapper;

    /*
     * @param testName the name of this test
     */
    public DatabaseAbstractionStressTests(String testName) {
        super(testName);
        sbShort = new StringBuffer();
        for (int i = 0; i < 25; i++) {
            sbShort.append("abcdefghij");
        }
    }

    /**
     * @return this test suite
     */
    public static Test suite() {
        TestSuite ts = new TestSuite(DatabaseAbstractionStressTests.class);
        ts.setName("Database Abstraction large table stress tests");
        return ts;
    }

    /**
     * Set up the environment
     */
    @Override
    protected void setUp() {
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(sourceName);
            statement = connection.createStatement();
            da = new DatabaseAbstractor();
        } catch (Exception e) {
            fail("Exception during setUp: " + e);
        }

        HashMap map = new HashMap();
        map.put("integer", new IntToStr());
        map.put("double", new DubToInt());
        mapper = new Mapper(map);
    }

    /**
     * Clean up the environment
     */
    @Override
    protected void tearDown() {
        try {
            statement.executeUpdate("DELETE FROM stress_tests");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            fail("Exception during tearDown: " + e);
        }
    }

    /**
     * Test major functions with a table that gets progressively larger.
     * Times each function (converting a ResultSet to a CustomResultSet,
     * navigating through that table and getting at its data, remapping,
     * and sorting) separately.
     *
     * @throws IllegalMappingException thrown by Database Abstraction
     * @throws InvalidCursorStateException thrown by Database Abstraction
     * @throws SQLException thrown by Database Abstraction
     * @throws NullColumnValueException
     */
    public void testLargeTable() throws IllegalMappingException,
                                        InvalidCursorStateException,
                                        SQLException, NullColumnValueException {
        long start, stop, time;
        int incBy = 1000;
        int iterations = 3;

        //try test, add more rows, continue
        for (int i = 0; i < iterations; i++) {
            addRecords(incBy);
            int records = incBy * (i + 1);
            ResultSet resultSet = statement.executeQuery(query);

            //convert
            start = System.currentTimeMillis();
            CustomResultSet crs = da.convertResultSet(resultSet);
            int recordCount = crs.getRecordCount();
            assertEquals("Wrong number of records", records,
                         recordCount);
            stop = System.currentTimeMillis();
            time = stop - start;
            /* This is almost completely arbitrary - each section
             * is required to run in fewer milliseconds than
             * the number of records.
             */
            assertTrue("Time to convert was too long: " + time,
                       time < records);

            //navigate and get
            start = System.currentTimeMillis();
            crs.beforeFirst();
            int count = 0;
            while (crs.next() && !crs.isLast()) {
                crs.getInt(2);
                crs.getString(3);
                crs.getDouble(15);
                count++;
            }

            assertEquals("Navigated through wrong number of records",
                         records, recordCount);
            stop = System.currentTimeMillis();
            time = stop - start;
            assertTrue("Time to navigate and get was too long: " + time,
                       time < records);

            //remap
            start = System.currentTimeMillis();
            crs.remap(mapper);
            stop = System.currentTimeMillis();
            time = stop - start;
            assertTrue("Time to remap was too long: " + time,
                       time < records);
            try { //test remapping
                crs.first();
                crs.getString(2);
                crs.getInt(15);
            } catch (ClassCastException e) {
                fail("remapping failed: " + e);
            }

            //two simple sorts - ascending by int, then descending by String
            start = System.currentTimeMillis();
            crs.sortAscending(1);
            crs.sortDescending(2);
            stop = System.currentTimeMillis();
            time = stop - start;
            assertTrue("Time to sort was too long: " + time,
                       time < records);
            System.out.println("  sort: " + time + " ms");
            crs.first();
            String last = crs.getString(2);
            while (crs.next() && !crs.isLast()) {
                String cur = crs.getString(2);
                if (cur.compareTo(last) > 0) {
                    fail("Sort failed");
                }
            }
            resultSet.close();
        }
        System.out.println();
    }

    /**
     * Tests major functions when run on many concurrent threads.
     *
     * @throws SQLException thrown by Database Abstraction
     */
    public void testManyThreads() throws SQLException {
        long start, stop;
        double time;
        int threadcount = 10;
        int inc = 10;
        int iterations = 5;
        int records = 500;
        addRecords(records);

        for (int i = 0; i < iterations; i++) {
            Thread threads[] = new Thread[threadcount];

            for (int j = 0; j < threadcount; j++) {
                threads[j] = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Statement statement = connection.
                                createStatement();

                                ResultSet rs = statement.executeQuery(query);
                                DatabaseAbstractor da = new
                                DatabaseAbstractor();

                                CustomResultSet crs = da.convertResultSet(rs);
                                crs.remap(mapper);
                                crs.sortDescending(2);
                            } catch (Exception e) {
                                fail("Unexpected exception: " + e);
                            }
                        }
                    };
            }

            start = System.currentTimeMillis();
            for (int j = 0; j < threadcount; j++) {
                threads[j].start();
            }
            for (int j = 0; j < threadcount; j++) {
                try {
                    threads[j].join();
                } catch (InterruptedException e) {
                    fail("Unexpected exception: " + e);
                }
            }
            stop = System.currentTimeMillis();

            time = (stop - start) / 1000.0;
            /* Abitrary time allowed is equal to the number of threads
             * times the number of records.
             */
            assertTrue("Time to finish was too long: " + time,
                       time < records * threadcount);
            System.out.println(threadcount + " threads: " + time + " seconds");
            threadcount += inc;
        }
        System.out.println();
    }

    /**
     * @param x add x rows to the table
     * @throws SQLException thrown if problem with the database
     */
    private void addRecords(int x) throws SQLException {
        String query = "insert into stress_tests "
        + "(a, b, c, d, e, f, g, h, i, j, k, l, m, o) values "
        + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);

        for (int i = 0; i < x; i++) {
            ps.setInt(1, i * 10000);
            ps.setString(2, sbShort.toString());
            ps.setDouble(3, i * Integer.MAX_VALUE);
            ps.setInt(4, i * 10000);
            ps.setBoolean(5, true);
            ps.setDouble(6, i * Integer.MAX_VALUE);
            ps.setInt(7, i * 10000);
            ps.setBoolean(8, false);
            ps.setDouble(9, i * Integer.MAX_VALUE);
            ps.setInt(10, i * 10000);
            ps.setBoolean(11, true);
            ps.setDouble(12, i * Integer.MAX_VALUE);
            ps.setInt(13, i * 10000);
            ps.setDouble(14, i * Integer.MAX_VALUE);
            ps.executeUpdate();
        }
    }

    /**
     * Convert Integers to Strings
     */
    private class IntToStr implements Converter {

        /**
         * @param value object to convert
         * @param column not used
         * @param metaData not used
         * @return a String
         * @throws IllegalMappingException not used
         */
        public Object convert(Object value,
                              int column,
                              CustomResultSetMetaData metaData)
            throws IllegalMappingException {
            return new String(((Integer) value).intValue() + "");
        }
    }

    /**
     * Convert Doubles to Integers
     */
    private class DubToInt implements Converter {

        /**
         * @param value object to convert
         * @param column not used
         * @param metaData not used
         * @return an Integer
         * @throws IllegalMappingException not used
         */
        public Object convert(Object value,
                              int column,
                              CustomResultSetMetaData metaData)
            throws IllegalMappingException {
            if (value == null) {
                return new Integer(0);
            }
            return new Integer(((Double) value).intValue());
        }
    }
}
