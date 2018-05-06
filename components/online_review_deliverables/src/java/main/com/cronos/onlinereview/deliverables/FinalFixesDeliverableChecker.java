/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved.
 */
package com.cronos.onlinereview.deliverables;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.deliverable.Deliverable;

/**
 * <p>
 * The FinalFixesDeliverableChecker class subclasses the SingleQuerySqlDeliverableChecker class. The SQL query that
 * it executes checks whether a submitter has uploaded the final fixes for the component. If so, it marks the
 * deliverable as completed, using the date of the last final fixes upload.
 * </p>
 *
 * <p>
 * This class is immutable.
 * </p>
 *
 * @author aubergineanode
 * @author kr00tki
 * @version 1.0.4
 */
public class FinalFixesDeliverableChecker extends SingleQuerySqlDeliverableChecker {

    /**
     * Creates a new FinalFixesDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @throws IllegalArgumentException If connectionFactory is null.
     */
    public FinalFixesDeliverableChecker(DBConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    /**
     * Creates a new FinalFixesDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @param connectionName The name of the connection to use. Can be null.
     * @throws IllegalArgumentException If the connectionFactory is <code>null</code> or the connectionName
     * is the empty string.
     */
    public FinalFixesDeliverableChecker(DBConnectionFactory connectionFactory, String connectionName) {
        super(connectionFactory, connectionName);
    }

    /**
     * <p>
     * Given a PreparedStatement representation of the SQL query returned by the getSqlQuery method, this method
     * extracts project_id and resource_id values from the deliverable and sets them as
     * parameters of the PreparedStatement.
     * </p>
     *
     * @param deliverable The deliverable from which to get any needed parameters to set on the PreparedStatement.
     * @param statement The PreparedStatement representation of the SQL query returned by getSqlQuery.
     * @throws SQLException if error occurs while filling the statement.
     */
    protected void fillInQueryParameters(Deliverable deliverable, PreparedStatement statement) throws SQLException {
        statement.setLong(1, deliverable.getPhase());
        statement.setLong(2, deliverable.getResource());
    }

    /**
     * <p>
     * Gets the SQL query string to select the last final fixes upload date. The returned query will have 2
     * placeholders for the project_phase_id and resource_id values.
     * </p>
     *
     * @return The SQL query string to execute.
     */
    protected String getSqlQuery() {
        return "SELECT MAX(upload.modify_date) FROM upload "
                + "INNER JOIN upload_type_lu ON upload.upload_type_id = upload_type_lu.upload_type_id "
                + "WHERE upload_type_lu.name = 'Final Fix' "
                + "AND upload.project_phase_id = ? AND upload.resource_id = ?";
    }
}
