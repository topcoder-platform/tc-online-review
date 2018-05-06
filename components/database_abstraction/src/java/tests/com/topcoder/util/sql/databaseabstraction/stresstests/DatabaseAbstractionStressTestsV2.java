/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.stresstests;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.sql.databaseabstraction.CustomResultSet;
import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;
import com.topcoder.util.sql.databaseabstraction.DatabaseAbstractor;
import com.topcoder.util.sql.databaseabstraction.OnDemandMapper;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.URLConverter;


/**
 * The stress test cases for version 2.
 *
 * @author TCSDEVELOPER, mumujava
 * @version 2
 */
public class DatabaseAbstractionStressTestsV2 extends TestCase {
    private static Throwable error;
    /** The name of the driver used to connect to the database. */
    private static final String DRIVER_NAME = "sun.jdbc.odbc.JdbcOdbcDriver";

    /** The source name used to connect to this database. */
    private static final String SOURCE_NAME = "jdbc:odbc:Tester";

    /** The SQL statement used to select all records from stress_tests table. */
    private static final String SELECT_ALL_SQL = "SELECT * FROM stress_tests";

    /** The SQL statement used to delete all records from stress_tests table. */
    private static final String DELETE_ALL_SQL = "DELETE * FROM stress_tests";

    /** The SQL statement used to insert a record into stress_tests table. */
    private static final String INSERT_SQL = "INSERT INTO stress_tests (a, b, c, d, e, f, g, h, i, j, k, l, m, o) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /** The constant string used for test. */
    private static final String STRING;

    /** The increment constant of records in each iteration. */
    private static final int RECORDS_INCREMENT = 1000;

    /** The increment constant of threads in each iteration. */
    private static final int THREADS_INCREMENT = 10;

    /** The times of the iteration. */
    private static final int ITERATIONS = 3;

    static {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < 25; i++) {
            buffer.append("asdfghjkl;'");
        }

        STRING = buffer.toString();
    }

    /** The instance of Connection used for test. */
    private Connection connection;

    /** The instance of Statement used for test. */
    private Statement statement;

    /**
     * Returns the stress test cases.
     *
     * @return the stress test cases.
     */
    public static Test suite() {
        return new TestSuite(DatabaseAbstractionStressTestsV2.class);
    }

    /**
     * Set up the environment.
     *
     * @throws Exception to JUnit.
     */
    @Override
    protected void setUp() throws Exception {
        Class.forName(DRIVER_NAME);
        connection = DriverManager.getConnection(SOURCE_NAME);
        statement = connection.createStatement();
        clearTable();
    }

    /**
     * Clean up the environment.
     *
     * @throws Exception to JUnit.
     */
    @Override
    protected void tearDown() throws Exception {
        clearTable();
        statement.close();
        connection.close();
    }

    /**
     * Deletes all items from the table.
     *
     * @throws Exception to JUnit.
     */
    private void clearTable() throws Exception {
        statement.executeUpdate(DELETE_ALL_SQL);
    }

    /**
     * Test major functions with a table that gets progressively larger. Times each function (converting a ResultSet to
     * a CustomResultSet, navigating through that table and getting at its data, and sorting) separately.
     *
     * @throws Exception to JUnit.
     */
    public void testLargeTable() throws Exception {
        DatabaseAbstractor databaseAbstractor = new DatabaseAbstractor();

        // set On-Demand Mapper of DatabaseAbstractor.
        databaseAbstractor.setOnDemandMapper(OnDemandMapper.createDefaultOnDemandMapper());

        // try test, add more rows, continue
        for (int i = 0; i < ITERATIONS; i++) {
            long start = 0;
            long stop = 0;
            long time = 0;

            addRecords(RECORDS_INCREMENT);

            int records = RECORDS_INCREMENT * (i + 1);
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL);

            // convert
            start = System.currentTimeMillis();

            CustomResultSet customResultSet = databaseAbstractor.convertResultSet(resultSet);

            int recordCount = customResultSet.getRecordCount();
            assertEquals("The number of records is invalid.", records, recordCount);

            stop = System.currentTimeMillis();
            time = stop - start;

            System.out.println("Time to convert (" + records + " records): " + time + "ms");
            assertTrue("Too long time to convert.", time < records);

            // navigate and get
            start = System.currentTimeMillis();

            customResultSet.beforeFirst();

            int count = 0;

            while (customResultSet.next()) {
                customResultSet.getBigDecimal(1);
                customResultSet.getString(3);
                customResultSet.getString(5);
                customResultSet.getDouble(7);
                customResultSet.getFloat(10);
                count++;
            }

            assertEquals("The number of records is invalid.", records, recordCount);

            stop = System.currentTimeMillis();
            time = stop - start;

            System.out.println("Time to navigate and get (" + records + " records): " + time + "ms");
            assertTrue("Too long time to navigate and get.", time < records);

            resultSet.close();
        }

        System.out.println();
    }

    /**
     * Tests major functions when run on many concurrent threads.
     *
     * @throws Exception to JUnit.
     */
    public void testManyThreads() throws Exception {
        int records = 500;
        int threadcount = 10;

        addRecords(500);

        for (int i = 0; i < ITERATIONS; i++) {
            long start = 0;
            long stop = 0;
            double time = 0;

            Thread[] threads = new Thread[threadcount];

            for (int j = 0; j < threadcount; j++) {
                threads[j] = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Statement statement = connection.createStatement();

                                    ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL);

                                    DatabaseAbstractor databaseAbstractor = new DatabaseAbstractor();

                                    databaseAbstractor.setOnDemandMapper(OnDemandMapper.createDefaultOnDemandMapper());

                                    CustomResultSet customResultSet = databaseAbstractor.convertResultSet(resultSet);

                                    while (customResultSet.next()) {
                                        customResultSet.getBigDecimal(1);
                                        customResultSet.getString(3);
                                        customResultSet.getString(5);
                                        customResultSet.getDouble(7);
                                        customResultSet.getFloat(10);
                                    }

                                    customResultSet.sortDescending(2);

                                    statement.close();
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

            System.out.println(threadcount + " threads: " + time + "s");
            assertTrue("Too long time to finish the threads.", time < (records * threadcount));

            threadcount += THREADS_INCREMENT;
        }

        System.out.println();
    }

    /**
     * The helper method used to add many rows into the table.
     *
     * @param rows the number of rows to add to the table
     *
     * @throws SQLException thrown if problem with the database
     */
    private void addRecords(int rows) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_SQL);

        for (int i = 0; i < rows; i++) {
            ps.setInt(1, i * 10000);
            ps.setString(2, STRING);
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
     * Tests URLConverter when run on many concurrent threads.
     * @throws Throwable
     */
    public void testURLConverter() throws Throwable {
        int threadcount = 10;

        addRecords(500);
        final URLConverter convert = new URLConverter();
        ResultSetMetaData data = connection.createStatement().executeQuery(SELECT_ALL_SQL).getMetaData();
        final CustomResultSetMetaData meta = new CustomResultSetMetaData(data );
        error = null;
        for (int i = 0; i < ITERATIONS; i++) {
            long start = 0;
            long stop = 0;
            double time = 0;

            Thread[] threads = new Thread[threadcount];

            for (int j = 0; j < threadcount; j++) {
                threads[j] = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    assertEquals("http://www.topcoder.com", convert.convert(new URL("http://www.topcoder.com"), 14, meta, String.class));
                                } catch (Throwable e) {
                                    error = e;
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

            if (error != null) {
                throw error;
            }
            stop = System.currentTimeMillis();

            time = (stop - start) / 1000.0;

            System.out.println(threadcount + " threads: " + time + "s");

            threadcount += THREADS_INCREMENT;
        }

        System.out.println();
    }
}
