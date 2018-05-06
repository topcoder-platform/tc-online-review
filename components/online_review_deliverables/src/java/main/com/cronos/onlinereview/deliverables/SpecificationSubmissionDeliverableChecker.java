/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.deliverable.Deliverable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The SpecificationSubmissionDeliverableChecker class subclasses the SingleQuerySqlDeliverableChecker class. The SQL
 * query used checks to see whether the specification is already submitted for phase. If so, it marks the deliverable as
 * completed, using the date of the specification submission.
 *
 * <p>This class is immutable.</p>
 *
 * @author isv
 * @version 1.0.4
 * @since 1.0.2
 */
public class SpecificationSubmissionDeliverableChecker extends SingleQuerySqlDeliverableChecker {

    /**
     * <p>Constructs new <code>SpecificationSubmissionDeliverableChecker</code> instance with specified connection
     * factory.</p>
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @throws IllegalArgumentException If connectionFactory is null.
     */
    public SpecificationSubmissionDeliverableChecker(DBConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    /**
     * <p>Gets the SQL query string to select the last modification date for the specification submission for the
     * project phase. Returned query will have 2 placeholders for the resource_id and project_phase_id values.</p>
     *
     * @return The SQL query string to execute.
     */
    @Override
    protected String getSqlQuery() {
        return "SELECT s.modify_date " +
               "FROM submission s " +
               "INNER JOIN upload u ON s.upload_id = u.upload_id " +
               "WHERE s.submission_status_id <> 5 " +
               "AND s.submission_type_id = 2 " +
               "AND u.resource_id = ? " +
               "AND u.project_phase_id = ?";
    }

    /**
     * <p>Given a PreparedStatement representation of the SQL query returned by the getSqlQuery method, this method
     * extracts resource_id and project_phase_id values from the deliverable and sets them as parameters of the
     * PreparedStatement.</p>
     *
     * @param deliverable The deliverable from which to get any needed parameters to set on the PreparedStatement.
     * @param statement The PreparedStatement representation of the SQL query returned by getSqlQuery.
     * @throws SQLException if any error occurs while setting the values to statement.
     */
    protected void fillInQueryParameters(Deliverable deliverable, PreparedStatement statement) throws SQLException {
        statement.setLong(1, deliverable.getResource());
        statement.setLong(2, deliverable.getPhase());
    }
}
