/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved.
 */
package com.cronos.onlinereview.deliverables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.deliverable.DeliverableChecker;

/**
 * <p>
 * The SqlDeliverableChecker class is the base class for any DeliverableChecker whose checking process involves
 * querying a database to determine the completion date. Although it implements the DeliverableChecker interface,
 * the actual checking method and logic must be provided by each subclass. This class simply holds the
 * DBConnectionFactory and connection name used, and provides subclasses a Connection to the database when
 * requested.
 * </p>
 *
 * <p>
 * This class is immutable.
 * </p>
 *
 * @author aubergineanode
 * @author kr00tki
 * @version 1.0
 */
public abstract class SqlDeliverableChecker implements DeliverableChecker {

    /**
     * connectionName: The name of the connection producer to use when a connection to the database is retrieved
     * from the DBConnectionFactory. This field is immutable and can be null or non-null. When non-null, no
     * restrictions are applied to the field. When this field is null, the createConnection() method is used to get
     * a connection. When it is non-null, the createConnection(String) method is used to get a connection. This
     * field is not exposed by this class; it is used in the createConnection method.
     *
     */
    private final String connectionName;

    /**
     * connectionFactory: The connection factory to use when a connection to the database is needed to get the
     * check. This field is immutable and must be non-null. This field is not exposed by this class. Instead,
     * subclasses can requests a connection by calling the createConnection method.
     *
     */
    private final DBConnectionFactory connectionFactory;

    /**
     * Creates a new SqlDeliverableChecker. The connectionName field is set to null.
     *
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @throws IllegalArgumentException If connectionFactory is null.
     */
    protected SqlDeliverableChecker(DBConnectionFactory connectionFactory) {
        this(connectionFactory, null);
    }

    /**
     * Creates a new SqlDeliverableChecker. All fields are set to the given values.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @param connectionName The name of the connection to use. Can be null.
     * @throws IllegalArgumentException If the connectionFactory is <code>null</code> or the connectionName
     * is the empty string.
     */
    protected SqlDeliverableChecker(DBConnectionFactory connectionFactory, String connectionName) {
        if (connectionFactory == null) {
            throw new IllegalArgumentException("connectionFactory cannot be null.");
        }

        if ((connectionName != null) && (connectionName.trim().length() == 0)) {
            throw new IllegalArgumentException("connection name cannot be empty.");
        }

        this.connectionFactory = connectionFactory;
        this.connectionName = connectionName;
    }

    /**
     * <p>
     * Allows a subclass to get a connection to the database. It is the responsibility of the subclass to close the
     * connection.
     * </p>
     *
     *
     * @return An opened connection to the database that is used for checking if the deliverable is finished.
     * @throws DBConnectionException If there is an error creating the connection.
     */
    protected Connection createDatabaseConnection() throws DBConnectionException {
        if (connectionName == null) {
            return connectionFactory.createConnection();
        }

        return connectionFactory.createConnection(connectionName);
    }

    /**
     * Closes the given resources in a safe fashion.
     *
     * @param conn the database connection.
     * @param pstmt the statement.
     * @param rs the result set.
     */
    static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                // ignore
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException ex) {
                // ignore
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                // ignore
            }
        }
    }

}
