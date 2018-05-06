/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.Properties;

import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * The issues tracker. Provides a part of a sample IssuesTracker#updateIssue() method that uses most of the utilities
 * defined in this component. Note that only some methods of ValidationUtility, ParameterCheckUtility and
 * PropertiesUtility are demonstrated since their usage is very similar. The sample below uses LoggingWrapperUtility
 * for logging. Log4jUtility can be used similarly.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class IssuesTracker {
    /**
     * <p>
     * The logger.
     * </p>
     */
    private final Log log = LogManager.getLog(getClass().getName());

    /**
     * <p>
     * Creates an instance of IssuesTracker.
     * </p>
     */
    public IssuesTracker() {
        // Empty
    }

    /**
     * <p>
     * Updates the issue.
     * </p>
     *
     * @param issueName
     *            the issue name.
     * @param priority
     *            the priority.
     * @param paramFile
     *            the parameter file.
     *
     * @return <code>true</code> issue is updated; <code>false</code> otherwise.
     *
     * @throws IllegalArgumentException
     *             if issueName is null or empty, priority is not positive, or paramFile is null or not a file.
     * @throws IssueTrackingException
     *             if any error occurs.
     */
    public boolean updateIssue(String issueName, int priority, File paramFile) throws IssueTrackingException {
        // Save method entrance timestamp
        Date enterTimestamp = new Date();
        // Prepare method signature that will appear in logged messages
        String signature = "IssuesTracker#updateIssue(String issueName, int priority, File paramFile)";

        // Log method entrance
        LoggingWrapperUtility.logEntrance(log, signature,
            new String[] {"issueName", "priority", "paramFile"},
            new Object[] {issueName, priority, paramFile});

        try {
            // Check input arguments
            ParameterCheckUtility.checkNotNullNorEmpty(issueName, "issueName");
            ParameterCheckUtility.checkPositive(priority, "priority");
            ParameterCheckUtility.checkNotNull(paramFile, "paramFile");
            ParameterCheckUtility.checkIsFile(paramFile, "paramFile");

            // Load additional parameters
            Properties properties = loadProperties(paramFile);

            // Get user ID from the properties
            Long userId = PropertiesUtility.getLongProperty(properties, "userId", true, IssueTrackingException.class);
            // Check whether user ID is positive
            ValidationUtility.checkPositive(userId, "The property 'userId'", IssueTrackingException.class);

            boolean result;

            // Get cached DB connection
            Connection connection = getConnection();

            try {
                // Retrieve issue type ID from DB
                Object[][] queryResult = JDBCUtility.executeQuery(connection,
                    "SELECT id FROM issue_types WHERE name = ?",
                    new int[] {Types.VARCHAR},
                    new Object[] {issueName},
                    new Class<?>[] {Long.class},
                    IssueTrackingException.class);

                result = (queryResult.length > 0);

                if (result) {
                    long issueTypeId = (Long) queryResult[0][0];

                    // Update issues in DB
                    int updatedNum = JDBCUtility.executeUpdate(connection,
                        "UPDATE issues SET priority = ? WHERE issue_type_id = ? AND user_id = ?",
                        new int[] {Types.INTEGER, Types.BIGINT, Types.INTEGER},
                        new Object[] {priority, issueTypeId, userId},
                        IssueTrackingException.class);

                    // Commit transaction
                    JDBCUtility.commitTransaction(connection, IssueTrackingException.class);

                    result = (updatedNum > 0);
                }
            } catch (IssueTrackingException e) {
                try {
                    // Rollback transaction
                    JDBCUtility.rollbackTransaction(connection, IssueTrackingException.class);
                } catch (IssueTrackingException e2) {
                    // Log exception at WARN level
                    LoggingWrapperUtility.logException(log, signature, e2, false, Level.WARN);

                    // Ignore this exception
                }
                throw e;
            } finally {
                releaseConnection(connection);
            }

            // Log method exit
            LoggingWrapperUtility.logExit(log, signature, new Object[] {result}, enterTimestamp);

            return result;
        } catch (IllegalArgumentException e) {
            // Log and re-throw the exception
            throw LoggingWrapperUtility.logException(log, signature, e);
        } catch (IssueTrackingException e) {
            // Log and re-throw the exception
            throw LoggingWrapperUtility.logException(log, signature, e);
        }
    }

    /**
     * <p>
     * Loads the properties.
     * </p>
     *
     * @param paramFile
     *            the properties file.
     *
     * @return the properties.
     *
     * @throws IssueTrackingException
     *             if any error occurs.
     */
    public static Properties loadProperties(File paramFile) throws IssueTrackingException {
        try {
            return TestsHelper.loadProperties(paramFile);
        } catch (IOException e) {
            throw new IssueTrackingException("Cannot load parameters file.", e);
        }
    }

    /**
     * <p>
     * Gets the connection.
     * </p>
     *
     * @return the connection.
     *
     * @throws IssueTrackingException
     *             if any error occurs.
     */
    private static Connection getConnection() throws IssueTrackingException {
        try {
            Connection connection = TestsHelper.getConnection();
            connection.setAutoCommit(false);
            return connection;
        } catch (ClassNotFoundException e) {
            throw new IssueTrackingException("Cannot load driver class.", e);
        } catch (SQLException e) {
            throw new IssueTrackingException("Cannot create a connection.", e);
        }
    }

    /**
     * <p>
     * Releases the connection.
     * </p>
     *
     * @param connection
     *            the connection.
     */
    private static void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Ignore
            }
        }
    }
}
