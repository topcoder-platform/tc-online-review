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
 * The SubmissionDeliverableChecker class subclasses the SingleQuerySqlDeliverableChecker class. The SQL query that
 * it executes checks whether a given resource (in this case a designer/developer) has uploaded a submission for
 * the project. If so, it marks the deliverable complete, using the date of the last upload.
 * </p>
 *
 * <p>
 * This class is immutable.
 * </p>
 *
 * <p>
 * Version 1.0.1 (Milestone Support Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated the checker to distinguish submission types.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.0.2 (Online Review Replatforming Release 2 ) Change notes:
 *   <ol>
 *     <li>Update sql statement to adapt for the new database schema. The upload_submission table
 *     is dropped.</li>
 *   </ol>
 * </p>
 * 
 * @author aubergineanode
 * @author kr00tki, TCSDEVELOPER
 * @version 1.0.2
 */
public class SubmissionDeliverableChecker extends SingleQuerySqlDeliverableChecker {

    /**
     * <p>A <code>long</code> providing the submission type ID.</p>
     */
    private long submissionTypeId;

    /**
     * Creates a new SubmissionDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @param submissionTypeId a <code>long</code> providing the submission type ID.
     * @throws IllegalArgumentException If connectionFactory is null.
     */
    public SubmissionDeliverableChecker(DBConnectionFactory connectionFactory, long submissionTypeId) {
        super(connectionFactory);
        this.submissionTypeId = submissionTypeId;
    }

    /**
     * Creates a new SubmissionDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @param connectionName The name of the connection to use. Can be null.
     * @param submissionTypeId a <code>long</code> providing the submission type ID.
     * @throws IllegalArgumentException If the connectionFactory is <code>null</code> or the connectionName
     * is the empty string.
     */
    public SubmissionDeliverableChecker(DBConnectionFactory connectionFactory, String connectionName, 
                                        long submissionTypeId) {
        super(connectionFactory, connectionName);
        this.submissionTypeId = submissionTypeId;
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
     * @throws SQLException if any error occurs while setting the values to statement.
     */
    protected void fillInQueryParameters(Deliverable deliverable, PreparedStatement statement) throws SQLException {
        statement.setLong(1, deliverable.getResource());
        statement.setLong(2, this.submissionTypeId);
    }

    /**
     * <p>
     * Gets the SQL query string to select the last date for the submission for the given project / resource
     * (submitter). The returned query will have two placeholders for the project_id and resource_id values.
     * </p>
     *
     * @return The SQL query string to execute.
     */
    protected String getSqlQuery() {
        return "SELECT MAX(upload.modify_date) FROM upload "
                + "INNER JOIN upload_type_lu ON upload.upload_type_id = upload_type_lu.upload_type_id "
                + "INNER JOIN upload_status_lu ON upload.upload_status_id = upload_status_lu.upload_status_id "
                + "LEFT JOIN submission ON upload.upload_id = submission.upload_id "
                + "WHERE upload_type_lu.name = 'Submission' AND upload_status_lu.name = 'Active' "
                + "AND upload.resource_id = ? AND submission.submission_type_id = ?";
    }
}
