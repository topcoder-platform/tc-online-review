/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * This is a utility class that provides static methods for executing retrieval and DML queries, committing and
 * rolling back transactions. If SQLException is thrown, this utility wraps it to the persistence exception specified
 * by the caller.
 * </p>
 *
 * <p>
 * <em>API Usage: </em> A part of a sample IssuesTracker#updateIssue() method that uses most of the utilities defined
 * in this component.
 * <pre>
 * private final Log log = LogManager.getLog(getClass().getName());
 *
 * public boolean updateIssue(String issueName, int priority, File paramFile) throws IssueTrackingException {
 *     // Save method entrance timestamp
 *     Date enterTimestamp = new Date();
 *     // Prepare method signature that will appear in logged messages
 *     String signature = &quot;IssuesTracker#updateIssue(String issueName, int priority, File paramFile)&quot;;
 *
 *     // Log method entrance
 *     LoggingWrapperUtility.logEntrance(log, signature,
 *         new String[] {&quot;issueName&quot;, &quot;priority&quot;, &quot;paramFile&quot;},
 *         new Object[] {issueName, priority, paramFile});
 *
 *     try {
 *         // Check input arguments
 *         ParameterCheckUtility.checkNotNullNorEmpty(issueName, &quot;issueName&quot;);
 *         ParameterCheckUtility.checkPositive(priority, &quot;priority&quot;);
 *         ParameterCheckUtility.checkNotNull(paramFile, &quot;paramFile&quot;);
 *         ParameterCheckUtility.checkIsFile(paramFile, &quot;paramFile&quot;);
 *
 *         // Load additional parameters
 *         Properties properties = loadProperties(paramFile);
 *
 *         // Get user ID from the properties
 *         Long userId = PropertiesUtility.getLongProperty(properties, &quot;userId&quot;, true,
 *             IssueTrackingException.class);
 *         // Check whether user ID is positive
 *         ValidationUtility.checkPositive(userId, &quot;The property 'userId'&quot;, IssueTrackingException.class);
 *
 *         boolean result;
 *
 *         // Get cached DB connection
 *         Connection connection = getConnection();
 *
 *         try {
 *             // Retrieve issue type ID from DB
 *             Object[][] queryResult = JDBCUtility.executeQuery(connection,
 *                 &quot;SELECT id FROM issue_types WHERE name = ?&quot;,
 *                 new int[] {Types.VARCHAR},
 *                 new Object[] {issueName},
 *                 new Class&lt;?&gt;[] {Long.class},
 *                 IssueTrackingException.class);
 *
 *             result = (queryResult.length &gt; 0);
 *
 *             if (result) {
 *                 long issueTypeId = (Long) queryResult[0][0];
 *
 *                 // Update issues in DB
 *                 int updatedNum = JDBCUtility.executeUpdate(connection,
 *                     &quot;UPDATE issues SET priority = ? WHERE issue_type_id = ? AND user_id = ?&quot;,
 *                     new int[] {Types.INTEGER, Types.BIGINT, Types.INTEGER},
 *                     new Object[] {priority, issueTypeId, userId},
 *                     IssueTrackingException.class);
 *
 *                 // Commit transaction
 *                 JDBCUtility.commitTransaction(connection, IssueTrackingException.class);
 *
 *                 result = (updatedNum &gt; 0);
 *             }
 *         } catch (IssueTrackingException e) {
 *             try {
 *                 // Rollback transaction
 *                 JDBCUtility.rollbackTransaction(connection, IssueTrackingException.class);
 *             } catch (IssueTrackingException e2) {
 *                 // Log exception at WARN level
 *                 LoggingWrapperUtility.logException(log, signature, e2, false, Level.WARN);
 *
 *                 // Ignore this exception
 *             }
 *             throw e;
 *         } finally {
 *             releaseConnection(connection);
 *         }
 *
 *         // Log method exit
 *         LoggingWrapperUtility.logExit(log, signature, new Object[] {result}, enterTimestamp);
 *
 *         return result;
 *     } catch (IllegalArgumentException e) {
 *         // Log and re-throw the exception
 *         throw LoggingWrapperUtility.logException(log, signature, e);
 *     } catch (IssueTrackingException e) {
 *         // Log and re-throw the exception
 *         throw LoggingWrapperUtility.logException(log, signature, e);
 *     }
 * }
 * </pre>
 *
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is immutable and thread safe when connection and array parameters
 * passed to it are used by the caller in thread safe manner.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0
 */
public class JDBCUtility {
    /**
     * <p>
     * Represents the supported types (Date.class, Double.class, Float.class, Long.class, Integer.class, String.class,
     * Boolean.class, Object.class).
     * </p>
     */
    private static final List<Class<?>> SUPPORTED_TYPES = Arrays.asList(new Class<?>[] {Date.class, Double.class,
        Float.class, Long.class, Integer.class, String.class, Boolean.class, Object.class});

    /**
     * <p>
     * Empty private constructor.
     * </p>
     */
    private JDBCUtility() {
        // Empty
    }

    /**
     * <p>
     * Executes the SQL query and parses the query result from the ResultSet.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown if some error occurred.
     * @param columnTypes
     *            the result column types (can be one of Date.class, Double.class, Float.class, Long.class,
     *            Integer.class, String.class, Boolean.class, Object.class).
     * @param sql
     *            the SQL statement.
     * @param connection
     *            the database connection to be used.
     * @param argumentValues
     *            the argument values.
     * @param exceptionClass
     *            the class of persistence exception to be thrown if some error occurs.
     * @param argumentTypes
     *            the argument types (see java.sql.Types).
     *
     * @return the SQL query result; outer array represents rows; inner arrays represent columns (not null).
     *
     * @throws T
     *             if some error occurred when executing query or parsing result.
     */
    public static <T extends Throwable> Object[][] executeQuery(Connection connection, String sql,
        int[] argumentTypes, Object[] argumentValues, Class<?>[] columnTypes, Class<T> exceptionClass) throws T {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            // Set values
            setParameterValues(preparedStatement, argumentTypes, argumentValues);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Get metadata from the result set:
            ResultSetMetaData metaData = resultSet.getMetaData();
            // Get the number of columns in the result set:
            int columnCount = metaData.getColumnCount();
            if (columnTypes.length != columnCount) {
                throw ExceptionHelper.constructException(exceptionClass, "The column types length ["
                    + columnTypes.length + "] does not match the result set column count [" + columnCount + "].");
            }

            // Check the column types
            for (int i = 0; i < columnCount; i++) {
                if (!SUPPORTED_TYPES.contains(columnTypes[i])) {
                    throw ExceptionHelper.constructException(exceptionClass, "Unsupported column type is used: "
                        + columnTypes[i].getName());
                }
            }

            // Get the data
            return getData(resultSet, columnCount, columnTypes);
        } catch (SQLException e) {
            throw ExceptionHelper.constructException(exceptionClass, Helper.concat(
                "Error occurred while executing the SQL statement [", sql, "] using the query arguments ",
                    Arrays.asList(argumentValues).toString(), "."), e);
        } finally {
            // Close the statement
            // (the result set will be automatically closed)
            closeStatement(preparedStatement);
        }
    }

    /**
     * <p>
     * Executes the DML SQL query.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown if some error occurred.
     * @param sql
     *            the SQL statement.
     * @param connection
     *            the database connection to be used.
     * @param argumentValues
     *            the argument values.
     * @param exceptionClass
     *            the class of persistence exception to be thrown if some error occurs.
     * @param argumentTypes
     *            the argument types (see java.sql.Types).
     *
     * @return the number of affected rows.
     *
     * @throws T
     *             if some error occurred when executing query.
     */
    public static <T extends Throwable> int executeUpdate(Connection connection, String sql,
        int[] argumentTypes, Object[] argumentValues, Class<T> exceptionClass) throws T {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            // Set values
            setParameterValues(preparedStatement, argumentTypes, argumentValues);

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHelper.constructException(exceptionClass, Helper.concat(
                "Error occurred while executing the SQL statement [", sql, "] using the arguments ",
                    Arrays.asList(argumentValues).toString(), "."), e);
        } finally {
            // Close the statement
            closeStatement(preparedStatement);
        }
    }

    /**
     * <p>
     * Commits the transaction.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown if some error occurred.
     * @param connection
     *            the database connection to be used.
     * @param exceptionClass
     *            the class of persistence exception to be thrown if some error occurs.
     *
     * @throws T
     *             if some error occurred.
     */
    public static <T extends Throwable> void commitTransaction(Connection connection, Class<T> exceptionClass)
        throws T {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw ExceptionHelper.constructException(exceptionClass,
                "Error occurred when committing the transaction.", e);
        }
    }

    /**
     * <p>
     * Performs a rollback operation for the transaction. Does nothing if connection is null.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown if some error occurred.
     * @param connection
     *            the database connection to be used.
     * @param exceptionClass
     *            the class of persistence exception to be thrown if some error occurs.
     *
     * @throws T
     *             if some error occurred.
     */
    public static <T extends Throwable> void rollbackTransaction(Connection connection, Class<T> exceptionClass)
        throws T {
        if (connection == null) {
            return;
        }
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw ExceptionHelper.constructException(exceptionClass,
                "Error occurred when rolling back the transaction.", e);
        }
    }

    /**
     * <p>
     * Gets the data from the result set.
     * </p>
     *
     * @param resultSet
     *            the result set.
     * @param columnCount
     *            the column count.
     * @param columnTypes
     *            the result column types (can be one of Date.class, Double.class, Float.class, Long.class,
     *            Integer.class, String.class, Boolean.class, Object.class).
     *
     * @return the retrieved data.
     *
     * @throws SQLException
     *             if a database access error occurs.
     */
    private static Object[][] getData(ResultSet resultSet, int columnCount, Class<?>[] columnTypes)
        throws SQLException {
        // Create a list for retrieved rows:
        List<Object> result = new ArrayList<Object>();

        while (resultSet.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                Object cellValue;
                if (columnTypes[i] == Date.class) {
                    // Date
                    Timestamp timestamp = resultSet.getTimestamp(i + 1);
                    cellValue = (timestamp == null) ? null : new Date(timestamp.getTime());
                } else if (columnTypes[i] == String.class) {
                    // String
                    cellValue = resultSet.getString(i + 1);
                } else if (columnTypes[i] == Object.class) {
                    // Object
                    cellValue = resultSet.getObject(i + 1);
                } else {
                    if (columnTypes[i] == Double.class) {
                        // Double
                        cellValue = resultSet.getDouble(i + 1);
                    } else if (columnTypes[i] == Float.class) {
                        // Float
                        cellValue = resultSet.getFloat(i + 1);
                    } else if (columnTypes[i] == Long.class) {
                        // Long
                        cellValue = resultSet.getLong(i + 1);
                    } else if (columnTypes[i] == Integer.class) {
                        // Integer
                        cellValue = resultSet.getInt(i + 1);
                    } else {
                        // Boolean
                        cellValue = resultSet.getBoolean(i + 1);
                    }

                    if (resultSet.wasNull()) {
                        cellValue = null;
                    }
                }
                rowData[i] = cellValue;
            }
            // Add row to the result list:
            result.add(rowData);
        }

        return result.toArray(new Object[result.size()][columnCount]);
    }

    /**
     * <p>
     * Sets the parameter values.
     * </p>
     *
     * @param preparedStatement
     *            the prepared statement.
     * @param argumentTypes
     *            the argument types.
     * @param argumentValues
     *            the argument values.
     *
     * @throws SQLException
     *             if a database access error occurs.
     */
    private static void setParameterValues(PreparedStatement preparedStatement, int[] argumentTypes,
        Object[] argumentValues) throws SQLException {
        int argumentTypesLen = argumentTypes.length;

        for (int i = 0; i < argumentTypesLen; i++) {
            preparedStatement.setObject(i + 1, argumentValues[i], argumentTypes[i]);
        }
    }

    /**
     * <p>
     * Closes the statement.
     * </p>
     *
     * @param statement
     *            the statement.
     */
    private static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // Ignore
            }
        }
    }
}
