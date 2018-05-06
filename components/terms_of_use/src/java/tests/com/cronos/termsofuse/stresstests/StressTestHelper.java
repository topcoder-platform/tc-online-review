/**
 * Copyright (c) 2011, TopCoder, Inc. All rights reserved
 */
package com.cronos.termsofuse.stresstests;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>A helper class used for stress tests.</p>
 *
 * @author jmn
 * @version 1.0
 */
final class StressTestHelper {

    /**
     * <p>Represents the number of loops that each tests will be performed.</p>
     */
    public static final int THREADS = 5;

    /**
     * <p>Represents the number of loops that each tests will be performed.</p>
     */
    public static final int LOOPS = 10;

    /**
     * <p>Represents the path to the file that inits the database.</p>
     */
    private static final String STRESSTESTS_INIT_SQL = "test_files/stresstests/init.sql";

    /**
     * <p>Represents the path to the file that clears the database.</p>
     */
    private static final String STRESSTESTS_CLEAR_SQL = "test_files/stresstests/clear.sql";

    /**
     * <p>Creates new instance of {@link StressTestHelper} class.</p>
     *
     * <p>Private constructor prevents from instantiation outside this class.</p>
     */
    private StressTestHelper() {
        // empty constructor
    }

    /**
     * <p>Creates the stress test context for the test with given name.</p>
     *
     * @param testName the name of test
     *
     * @return the stress test context
     */
    static StressTestContext testExecutionStarted(String testName) {

        return new StressTestContext(testName, System.currentTimeMillis());
    }

    /**
     * <p>Notifies about end of test execution.</p>
     *
     * <p>By default prints out the test execution time.</p>
     *
     * @param stressTestContext the stress test context
     */
    static void testExecutionCompleted(StressTestContext stressTestContext) {

        printMessage(String.format("Test %s execution completed in %d ms.", stressTestContext.getTestName(),
                System.currentTimeMillis() - stressTestContext.getTestExecutionStartTime()));
    }

    /**
     * <p>Prints out the message.</p>
     *
     * @param msg the message to print
     */
    private static void printMessage(String msg) {

        System.out.println(msg);
    }

    /**
     * <p>Sets up the database.</p>
     *
     * @param connection the database connection to use
     *
     * @throws IOException  if any error occurs during I/O operation
     * @throws SQLException if any error occurs during database operation
     */
    static void initDatabase(Connection connection) throws IOException, SQLException {

        executeBatch(connection, STRESSTESTS_INIT_SQL);
    }

    /**
     * <p>Clears the database.</p>
     *
     * @param connection the database connection to use
     *
     * @throws IOException  if any error occurs during I/O operation
     * @throws SQLException if any error occurs during database operation
     */
    static void clearDatabase(Connection connection) throws IOException, SQLException {

        executeBatch(connection, STRESSTESTS_CLEAR_SQL);
    }

    /**
     * <p>Executes the given sql statement.</p>
     *
     * @param connection the {@link Connection} to use
     * @param update     the sql statement to execute
     *
     * @throws SQLException if any error occurs during database execution
     */
    static void executeUpdate(Connection connection, String update) throws SQLException {

        Statement statement = null;

        try {
            statement = connection.createStatement();

            statement.executeUpdate(update);
        } finally {

            close(statement);

            close(connection);
        }
    }

    /**
     * <p>Executes the given sql query.</p>
     *
     * @param connection the {@link Connection} to use
     * @param sql        the sql query to execute
     *
     * @return query result
     *
     * @throws SQLException if any error occurs during database execution
     */
    static int queryInt(Connection connection, String sql) throws SQLException {

        Statement statement = null;
        ResultSet resultSet = null;
        int index = 1;
        try {
            statement = connection.createStatement();

            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {

                return resultSet.getInt(index++);
            }

            return -1;

        } finally {

            close(statement);

            close(connection);
        }
    }

    /**
     * <p>Executes the given sql query and return the only single result as string.</p>
     *
     * @param connection the {@link Connection} to use
     * @param sql        the sql query to execute
     *
     * @return query result
     *
     * @throws SQLException if any error occurs during database execution
     */
    static String queryString(Connection connection, String sql) throws SQLException {

        Statement statement = null;
        ResultSet resultSet = null;
        int index = 1;
        try {
            statement = connection.createStatement();

            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {

                return resultSet.getString(index++);
            }

            return null;
        } finally {

            close(statement);

            close(connection);
        }
    }

    /**
     * <p>Executes the given sql query and return the only single result as string.</p>
     *
     * @param connection the {@link Connection} to use
     * @param sql        the sql query to execute
     *
     * @return query result
     *
     * @throws SQLException if any error occurs during database execution
     */
    static void setBlob(Connection connection, String sql, String blob) throws SQLException {

        PreparedStatement statement = null;
        int index = 1;
        try {
            statement = connection.prepareStatement(sql);
            statement.setBytes(index++, blob.getBytes());

            statement.executeUpdate();
        } finally {

            close(statement);

            close(connection);
        }
    }

    /**
     * <p>Executes the given sql query and return the only single result as string.</p>
     *
     * @param connection the {@link Connection} to use
     * @param sql        the sql query to execute
     *
     * @return query result
     *
     * @throws SQLException if any error occurs during database execution
     */
    static String getBlob(Connection connection, String sql) throws SQLException {

        Statement statement = null;
        ResultSet resultSet = null;
        int index = 1;
        byte[] result;
        try {
            statement = connection.createStatement();

            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {

                result = resultSet.getBytes(index++);
                return new String(result);
            }

            return null;
        } finally {

            close(statement);

            close(connection);
        }
    }

    /**
     * <p>Executes batch sql statements stored in file specified by passed parameter.</p>
     *
     * @param connection the database connection
     * @param fileName   path to file containing sql statements
     *
     * @throws IOException  if any error occurs while reading the file
     * @throws SQLException if any error occurs while performing database operation
     */
    static void executeBatch(Connection connection, String fileName) throws IOException, SQLException {

        BufferedReader input = null;
        String line;
        int count = 0;
        int maxCount = 25;

        Statement statement = null;
        try {
            statement = connection.createStatement();

            input = new BufferedReader(new FileReader(fileName));

            while ((line = input.readLine()) != null) {
                line = line.replace(';', ' ');
                if (line.trim().length() != 0) {

                    if (count >= maxCount) {
                        statement.executeBatch();
                        statement.close();
                        statement = connection.createStatement();
                        count = 0;
                    }
                    statement.addBatch(line.trim());

                    count++;
                }
            }

            statement.executeBatch();

        } finally {
            close(statement);

            close(connection);

            close(input);
        }
    }

    /**
     * <p>Closes the passed {@link Closeable} instance.</p>
     *
     * @param input the {@link Closeable} instance to close
     */
    private static void close(Closeable input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                // ignores exception
            }
        }
    }

    /**
     * <p>Closes the passed {@link Connection} instance.</p>
     *
     * @param connection the {@link Connection} to close
     */
    private static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // ignores exception
            }
        }
    }

    /**
     * <p>Closes the passed {@link Statement} instance.</p>
     *
     * @param statement the {@link Statement} to close
     */
    private static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // ignores exception
            }
        }
    }

    /**
     * <p>Blocks the execution of the current thread, until all work thread ends their execution</p>
     *
     * @param executorService the {@link ExecutorService} instance
     *
     * @throws InterruptedException if the current thread is being interrupted
     */
    static void awaitTermination(ExecutorService executorService) throws InterruptedException {
        executorService.shutdown();

        while (!executorService.isTerminated()) {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * <p>Class that stores information of executing unit test.</p>
     *
     * @author jmn
     * @version 1.0
     */
    static class StressTestContext {

        /**
         * <p>Represents the unit test name.</p>
         */
        private final String testName;

        /**
         * <p>Represents the test execution start time.</p>
         */
        private final long testExecutionStartTime;

        /**
         * <p>Creates new instance of {@link StressTestContext} class.</p>
         *
         * @param testName               the test name
         * @param testExecutionStartTime the test execution start time
         */
        StressTestContext(String testName, long testExecutionStartTime) {

            this.testName = testName;
            this.testExecutionStartTime = testExecutionStartTime;
        }

        /**
         * <p>Retrieves the test name.</p>
         *
         * @return the test name
         */
        public String getTestName() {
            return testName;
        }

        /**
         * <p>Retrieves the test execution start time.</p>
         *
         * @return the test execution start time
         */
        public long getTestExecutionStartTime() {
            return testExecutionStartTime;
        }
    }
}
