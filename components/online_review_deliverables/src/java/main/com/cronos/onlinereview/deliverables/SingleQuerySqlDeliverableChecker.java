/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved.
 */
package com.cronos.onlinereview.deliverables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;

/**
 * <p>
 * The SingleQuerySqlDeliverableChecker class templates the process of checking a deliverable whose completion date
 * can be found by executing a single SQL query which returns a single row containing a single column of the DATETIME
 * type. This class handles most of the process of running the query: opening the database connection,
 * executing the query, extracting the results from the query, setting the completion date on the deliverable, and
 * closing the database connection. Abstract methods are provided to allow subclasses to template the SQL query to
 * execute to set any needed parameters or the query.
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
public abstract class SingleQuerySqlDeliverableChecker extends SqlDeliverableChecker {

    /**
     * Creates a new SingleQuerySqlDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @throws IllegalArgumentException If connectionFactory is null.
     */
    protected SingleQuerySqlDeliverableChecker(DBConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    /**
     * Creates a new SingleQuerySqlDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @param connectionName The name of the connection to use. Can be null.
     * @throws IllegalArgumentException If the connectionFactory is <code>null</code> or the connectionName
     * is the empty string.
     */
    protected SingleQuerySqlDeliverableChecker(DBConnectionFactory connectionFactory, String connectionName) {
        super(connectionFactory, connectionName);
    }

    /**
     * <p>
     * Checks the given deliverable to see if it is complete. This method templates the deliverable checking
     * process. To determine if the deliverable is complete, a single SQL query is run. This is the query returned
     * by the getSqlQuery method.
     * </p>
     *
     * @param deliverable The deliverable to check
     * @throws IllegalArgumentException If deliverable is null
     * @throws DeliverableCheckingException If there is an error checking the deliverable for any implementation
     *         defined reason.
     */
    public void check(Deliverable deliverable) throws DeliverableCheckingException {
        if (deliverable == null) {
            throw new IllegalArgumentException("deliverable cannot be null.");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // get the connection
            conn = createDatabaseConnection();
            // prepare statement
            pstmt = conn.prepareStatement(getSqlQuery());
            fillInQueryParameters(deliverable, pstmt);

            // execute it
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // timestamp is used here because getDate for Informix driver returns value with day precision
                // but all the fields in the database are of milisecond precision
                Timestamp date = rs.getTimestamp(1);
                if (date != null) {
                    deliverable.setCompletionDate(date);
                }
                if (rs.getMetaData().getColumnCount() > 1) {
                    deliverable.setSubmission(rs.getLong(2));
                }
            }
        } catch (SQLException ex) {
            throw new DeliverableCheckingException("Error occurs while database check operation.", ex);
        } catch (DBConnectionException ex) {
            throw new DeliverableCheckingException("Error occurs while creating database connection.", ex);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    /**
     * <p>
     * Given a PreparedStatement representation of the SQL query returned by the getSqlQuery method, this method
     * extracts any needed values from the deliverable (for example, the project or resource) and sets them as
     * parameters of the PreparedStatement.
     * </p>
     *
     * <p>
     * No null checking of arguments is needed in subclasses, as this method is protected and called only in
     * situations in which it is guaranteed (by the calling method) that the arguments will be non-null.
     * </p>
     *
     *
     *
     * @param deliverable The deliverable from which to get any needed parameters to set on the PreparedStatement.
     * @param statement The PreparedStatement representation of the SQL query returned by getSqlQuery.
     * @throws SQLException if error occurs while filling the statement.
     * @throws DeliverableCheckingException if the deliverable is not valid for checker (for example is not
     *         per-submission where checker require this).
     */
    protected abstract void fillInQueryParameters(Deliverable deliverable, PreparedStatement statement)
        throws SQLException, DeliverableCheckingException;

    /**
     * Gets the SQL query string of the query that the subclass wants to execute. The return should be non-null and
     * a valid SQL query. The query should return a single column with a DATE type (or TIME or TIMESTAMP). As this
     * is protected method, it is assumed that all subclasses will properly implement this method, so the return
     * value is not checked in the checkDeliverable method.
     *
     *
     * @return The SQL query string to execute.
     */
    protected abstract String getSqlQuery();
}
