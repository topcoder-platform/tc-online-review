/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late.impl.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.db.connectionfactory.ConfigurationException;
import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;
import com.topcoder.management.deliverable.late.Helper;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableManagementConfigurationException;
import com.topcoder.management.deliverable.late.LateDeliverableType;
import com.topcoder.management.deliverable.late.impl.LateDeliverableNotFoundException;
import com.topcoder.management.deliverable.late.impl.LateDeliverablePersistence;
import com.topcoder.management.deliverable.late.impl.LateDeliverablePersistenceException;
import com.topcoder.util.log.Log;

/**
 * <p>
 * This class is an implementation of LateDeliverablePersistence that updates late deliverables and retrieves all late
 * deliverable types in/from database persistence using JDBC and DB Connection Factory component. This class uses
 * Logging Wrapper component to log errors and debug information.
 * </p>
 *
 * <p>
 * <em>Changes in version 1.0.6:</em>
 * <ol>
 * <li>Added getLateDeliverableTypes() method.</li>
 * <li>Updated throws documentation of update() method.</li>
 * <li>Updated class documentation.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is mutable, but thread safe when configure() method is called just once
 * right after instantiation and entities passed to it are used by the caller in thread safe manner. It uses thread
 * safe DBConnectionFactory and Log instances. This class uses transactions when updating data in the database.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0.6
 */
public class DatabaseLateDeliverablePersistence implements LateDeliverablePersistence {
    /**
     * <p>
     * Represents the class name.
     * </p>
     */
    private static final String CLASS_NAME = DatabaseLateDeliverablePersistence.class.getName();

    /**
     * <p>
     * Represents the child key 'dbConnectionFactoryConfig'.
     * </p>
     */
    private static final String KEY_DBCF_CONFIG = "dbConnectionFactoryConfig";

    /**
     * <p>
     * Represents the property key 'connectionName'.
     * </p>
     */
    private static final String KEY_CONN_NAME = "connectionName";

    /**
     * <p>
     * Represents the SQL string to query late deliverable types.
     * </p>
     *
     * @since 1.0.6
     */
    private static final String SQL_QUERY_LATE_DELIVERABLE_TYPE =
        "SELECT late_deliverable_type_id, name, description FROM late_deliverable_type_lu";

    /**
     * <p>
     * Represents the SQL string to update late deliverable.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.0.6:</em>
     * <ol>
     * <li>Added field to update the late deliverable type id.</li>
     * </ol>
     * </p>
     */
    private static final String SQL_UPDATE_LATE_DELIVERABLE = "UPDATE late_deliverable SET project_phase_id = ?,"
        + " resource_id = ?, deliverable_id = ?, deadline = ?, compensated_deadline = ?, create_date = ?,"
        + " forgive_ind = ?, last_notified = ?, delay = ?, explanation = ?, explanation_date = ?, response = ?,"
        + " response_user = ?, response_date = ?, late_deliverable_type_id = ? WHERE late_deliverable_id = ?";

    /**
     * <p>
     * The database connection factory to be used.
     * </p>
     *
     * <p>
     * Is initialized in the configure() method and never changed after that. Cannot be null after initialization. Is
     * used in update().
     * </p>
     */
    private DBConnectionFactory dbConnectionFactory;

    /**
     * <p>
     * The connection name to be used.
     * </p>
     *
     * <p>
     * Is initialized in the configure() method and never changed after that. Cannot be empty after initialization. If
     * null, the default connection is used. Is used in update().
     * </p>
     */
    private String connectionName;

    /**
     * <p>
     * The logger used by this class for logging errors and debug information.
     * </p>
     *
     * <p>
     * Is null if logging is not required. Is initialized in the configure() method and never changed after that. Is
     * used in update().
     * </p>
     */
    private Log log;

    /**
     * <p>
     * Creates an instance of DatabaseLateDeliverablePersistence.
     * </p>
     */
    public DatabaseLateDeliverablePersistence() {
        // Empty
    }

    /**
     * <p>
     * Configures this instance with use of the given configuration object.
     * </p>
     *
     * @param configuration
     *            the configuration object.
     *
     * @throws IllegalArgumentException
     *             if configuration is null.
     * @throws LateDeliverableManagementConfigurationException
     *             if some error occurred when initializing an instance using the given configuration.
     */
    public void configure(ConfigurationObject configuration) {
        Helper.checkNull(configuration, "configuration");

        // Get logger
        log = Helper.getLog(configuration);

        try {
            // Create database connection factory
            dbConnectionFactory = new DBConnectionFactoryImpl(Helper.getChildConfig(configuration, KEY_DBCF_CONFIG));
        } catch (UnknownConnectionException e) {
            throw new LateDeliverableManagementConfigurationException(
                "Failed to create a database connection factory.", e);
        } catch (ConfigurationException e) {
            throw new LateDeliverableManagementConfigurationException(
                "Failed to create a database connection factory.", e);
        }

        // Get connection name
        connectionName = Helper.getProperty(configuration, KEY_CONN_NAME, false);
    }

    /**
     * <p>
     * Updates the given late deliverable in persistence.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.0.6:</em>
     * <ol>
     * <li>Updated throws documentation for IllegalArgumentException.</li>
     * <li>Added/updated steps to update the late deliverable type id.</li>
     * <li>Added logging for IllegalStateException.</li>
     * </ol>
     * </p>
     *
     * @param lateDeliverable
     *            the late deliverable with updated data.
     *
     * @throws IllegalArgumentException
     *             if lateDeliverable is null, lateDeliverable.getId() &lt;= 0, lateDeliverable.getDeadline() is null,
     *             lateDeliverable.getCreateDate() is null, lateDeliverable.getType() is null,
     *             lateDeliverable.getType().getId() &lt;= 0.
     * @throws IllegalStateException
     *             if persistence was not configured properly (dbConnectionFactory is null).
     * @throws LateDeliverableNotFoundException
     *             if late deliverable with ID equal to lateDeliverable.getId() doesn't exist in persistence.
     * @throws LateDeliverablePersistenceException
     *             if some other error occurred when accessing the persistence.
     */
    public void update(LateDeliverable lateDeliverable) throws LateDeliverablePersistenceException {
        Date enterTimestamp = new Date();
        String signature = getSignature("update(LateDeliverable lateDeliverable)");

        try {
            // Log method entry
            Helper.logEntrance(log, signature,
                new String[] {"lateDeliverable"},
                new Object[] {lateDeliverable});

            Helper.checkNull(lateDeliverable, "lateDeliverable");
            Helper.checkPositive(lateDeliverable.getId(), "lateDeliverable.getId()");
            Helper.checkNull(lateDeliverable.getCreateDate(), "lateDeliverable.getCreateDate()");
            LateDeliverableType type = lateDeliverable.getType();
            Helper.checkNull(type, "lateDeliverable.getType()");
            Helper.checkPositive(type.getId(), "lateDeliverable.getType().getId()");

            Connection connection = null;
            try {
                // Create a connection
                connection = getConnection();

                // Disable the auto commit mode
                connection.setAutoCommit(false);

                // Update the given late deliverable in persistence
                updateLateDeliverable(lateDeliverable, connection, log, signature);
            } catch (DBConnectionException e) {
                throw new LateDeliverablePersistenceException("Failed to create a database connection.", e);
            } catch (SQLException e) {
                throw new LateDeliverablePersistenceException("A database access error occurred.", e);
            } finally {
                // Close the connection:
                closeConnection(connection, log, signature);
            }

            // Log method exit
            Helper.logExit(log, signature, null, enterTimestamp);
        } catch (IllegalArgumentException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "IllegalArgumentException is thrown.");
        } catch (IllegalStateException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "IllegalStateException is thrown.");
        } catch (LateDeliverableNotFoundException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "LateDeliverableNotFoundException is thrown"
                + " when updating the given late deliverable in persistence.");
        } catch (LateDeliverablePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e, "LateDeliverablePersistenceException is thrown"
                + " when updating the given late deliverable in persistence.");
        }
    }

    /**
     * <p>
     * Retrieves all existing late deliverable types from persistence.
     * </p>
     *
     * @return the retrieved late deliverable types (not null, doesn't contain null).
     *
     * @throws IllegalStateException
     *             if persistence was not configured properly (dbConnectionFactory is null).
     * @throws LateDeliverablePersistenceException
     *             if some error occurred when accessing the persistence.
     *
     * @since 1.0.6
     */
    public List<LateDeliverableType> getLateDeliverableTypes() throws LateDeliverablePersistenceException {
        Date enterTimestamp = new Date();
        String signature = getSignature("getLateDeliverableTypes()");

        // Log method entry
        Helper.logEntrance(log, signature, null, null);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create a connection
            connection = getConnection();

            preparedStatement = connection.prepareStatement(SQL_QUERY_LATE_DELIVERABLE_TYPE);
            // Execute the query:
            ResultSet resultSet = preparedStatement.executeQuery();
            // Create a list for result:
            List<LateDeliverableType> result = new ArrayList<LateDeliverableType>();
            while (resultSet.next()) {
                // Create late deliverable type instance:
                LateDeliverableType lateDeliverableType = new LateDeliverableType();

                lateDeliverableType.setId(resultSet.getLong("late_deliverable_type_id"));
                lateDeliverableType.setName(resultSet.getString("name"));
                lateDeliverableType.setDescription(resultSet.getString("description"));

                // Add late deliverable type to the list:
                result.add(lateDeliverableType);
            }

            // Log method exit
            Helper.logExit(log, signature, new Object[] {result}, enterTimestamp);
            return result;
        } catch (IllegalStateException e) {
            throw Helper.logException(log, signature, e, "IllegalStateException is thrown.");
        } catch (DBConnectionException e) {
            throw Helper.logException(log, signature, new LateDeliverablePersistenceException(
                "Failed to create a database connection.", e), "LateDeliverablePersistenceException is thrown"
                + " when retrieving all existing late deliverable types from persistence.");
        } catch (SQLException e) {
            throw Helper.logException(log, signature, new LateDeliverablePersistenceException(
                "A database access error occurred.", e), "LateDeliverablePersistenceException is thrown"
                + " when retrieving all existing late deliverable types from persistence.");
        } finally {
            // Close the prepared statement:
            // (the result set will be automatically closed)
            closeStatement(preparedStatement, log, signature);
            // Close the connection:
            closeConnection(connection, log, signature);
        }
    }

    /**
     * <p>
     * Updates the given late deliverable in persistence.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.0.6:</em>
     * <ol>
     * <li>Added/updated steps to update the late deliverable type id.</li>
     * </ol>
     * </p>
     *
     * @param lateDeliverable
     *            the late deliverable with updated data.
     *
     * @param connection
     *            the connection (not <code>null</code>).
     * @param log
     *            the logger.
     * @param signature
     *            the signature of the method to log.
     *
     * @throws LateDeliverableNotFoundException
     *             if late deliverable with ID equal to lateDeliverable.getId() doesn't exist in persistence.
     * @throws SQLException
     *             if a database access errors.
     */
    private static void updateLateDeliverable(LateDeliverable lateDeliverable, Connection connection, Log log,
        String signature) throws LateDeliverableNotFoundException, SQLException {
        // Create a prepared statement:
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_LATE_DELIVERABLE);
        try {
            int index = 1;

            // Set project phase ID to the prepared statement:
            preparedStatement.setLong(index++, lateDeliverable.getProjectPhaseId());
            // Set resource ID to the prepared statement:
            preparedStatement.setLong(index++, lateDeliverable.getResourceId());
            // Set deliverable ID to the prepared statement:
            preparedStatement.setLong(index++, lateDeliverable.getDeliverableId());
            // Set deadline to the prepared statement:
            preparedStatement.setTimestamp(index++, getTimestamp(lateDeliverable.getDeadline()));
            // Set compensated deadline to the prepared statement:
            preparedStatement.setTimestamp(index++, getTimestamp(lateDeliverable.getCompensatedDeadline()));
            // Set create date to the prepared statement:
            preparedStatement.setTimestamp(index++, new Timestamp(lateDeliverable.getCreateDate().getTime()));
            // Set forgiven flag to the prepared statement:
            preparedStatement.setInt(index++, lateDeliverable.isForgiven() ? 1 : 0);

            // Set last notified to the prepared statement:
            preparedStatement.setTimestamp(index++, getTimestamp(lateDeliverable.getLastNotified()));

            // Get delay from the late deliverable:
            Long delay = lateDeliverable.getDelay();
            if (delay != null) {
                // Set delay to the prepared statement:
                preparedStatement.setLong(index++, delay);
            } else {
                // Set delay equal to null to the prepared statement:
                preparedStatement.setNull(index++, Types.BIGINT);
            }

            // Set explanation to the prepared statement:
            preparedStatement.setString(index++, lateDeliverable.getExplanation());
            // Set explanation date to the prepared statement:
            preparedStatement.setTimestamp(index++, getTimestamp(lateDeliverable.getExplanationDate()));
            // Set response to the prepared statement:
            preparedStatement.setString(index++, lateDeliverable.getResponse());
            // Set response user to the prepared statement:
            preparedStatement.setString(index++, lateDeliverable.getResponseUser());
            // Set response date to the prepared statement:
            preparedStatement.setTimestamp(index++, getTimestamp(lateDeliverable.getResponseDate()));

            // Set late deliverable type ID to the prepared statement:
            preparedStatement.setLong(index++, lateDeliverable.getType().getId());

            // Get ID from the late deliverable:
            long lateDeliverableId = lateDeliverable.getId();
            // Set it to the prepared statement:
            preparedStatement.setLong(index, lateDeliverableId);

            // Execute the UPDATE statement
            if (preparedStatement.executeUpdate() == 0) {
                throw new LateDeliverableNotFoundException(Helper.concat("The late deliverable with ID '",
                    lateDeliverableId, "' doesn't exist in persistence."), lateDeliverableId);
            }

            // Commit the changes
            connection.commit();
        } catch (SQLException e) {
            // Roll back
            rollback(connection, log, signature);

            throw e;
        } finally {
            // Close the prepared statement:
            closeStatement(preparedStatement, log, signature);
        }
    }

    /**
     * <p>
     * Closes the connection.
     * </p>
     *
     * @param connection
     *            the connection.
     * @param log
     *            the logger.
     * @param signature
     *            the signature.
     *
     * @since 1.0.6
     */
    private static void closeConnection(Connection connection, Log log, String signature) {
        if (connection != null) {
            try {
                // Close the connection
                connection.close();
            } catch (SQLException e) {
                // Log exception
                Helper.logException(log, signature, e, "A database access error occurred"
                    + " when closing the connection to persistence (will be ignored).");

                // Ignore
            }
        }
    }

    /**
     * <p>
     * Closes the statement.
     * </p>
     *
     * @param statement
     *            the statement.
     * @param log
     *            the logger.
     * @param signature
     *            the signature.
     *
     * @since 1.0.6
     */
    private static void closeStatement(Statement statement, Log log, String signature) {
        if (statement != null) {
            try {
                // Close the statement:
                statement.close();
            } catch (SQLException e) {
                // Log exception
                Helper.logException(log, signature, e, "A database access error occurred"
                    + " when closing the statement (will be ignored).");

                // Ignore
            }
        }
    }

    /**
     * <p>
     * Creates Timestamp with the date.
     * </p>
     *
     * @param date
     *            the date.
     *
     * @return the Timestamp or null.
     */
    private static Timestamp getTimestamp(Date date) {
        return (date != null) ? new Timestamp(date.getTime()) : null;
    }

    /**
     * <p>
     * Rolls back the current transaction.
     * </p>
     *
     * @param connection
     *            the connection (not <code>null</code>).
     * @param log
     *            the logger.
     * @param signature
     *            the signature of the method to log.
     */
    private static void rollback(Connection connection, Log log, String signature) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            // Log exception
            Helper.logException(log, signature, e,
                "A database access error occurred when rolling back (will be ignored).");

            // Ignore
        }
    }

    /**
     * <p>
     * Gets the Connection.
     * </p>
     *
     * @return the Connection.
     *
     * @throws IllegalStateException
     *             if persistence was not configured properly (dbConnectionFactory is null).
     * @throws DBConnectionException
     *             if any error occurs.
     *
     * @since 1.0.6
     */
    private Connection getConnection() throws DBConnectionException {
        if (dbConnectionFactory == null) {
            throw new IllegalStateException("This persistence was not properly configured.");
        }

        // Create a connection
        return (connectionName == null) ? dbConnectionFactory.createConnection()
            : dbConnectionFactory.createConnection(connectionName);
    }

    /**
     * <p>
     * Gets the signature for given method for logging.
     * </p>
     *
     * @param method
     *            the method name.
     *
     * @return the signature for given method.
     */
    private static String getSignature(String method) {
        return Helper.concat(CLASS_NAME, Helper.DOT, method);
    }
}
