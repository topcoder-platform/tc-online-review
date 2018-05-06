/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved.
 */
package com.cronos.onlinereview.deliverables;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;

/**
 * <p>
 * The SubmitterCommentDeliverableChecker class subclasses the SingleQuerySqlDeliverableChecker class. The SQL
 * query that it executes checks whether the resource (i.e. the submitter) has attached an approved or rejected
 * comment to the review. If so, it marks the deliverable as completed, using the date that the comment was
 * updated.
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
public class SubmitterCommentDeliverableChecker extends SingleQuerySqlDeliverableChecker {

    /**
     * Creates a new SubmitterCommentDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @throws IllegalArgumentException If connectionFactory is null.
     */
    public SubmitterCommentDeliverableChecker(DBConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    /**
     * Creates a new SubmitterCommentDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @param connectionName The name of the connection to use. Can be null.
     * @throws IllegalArgumentException If the connectionFactory is <code>null</code> or the connectionName
     * is the empty string.
     */
    public SubmitterCommentDeliverableChecker(DBConnectionFactory connectionFactory, String connectionName) {
        super(connectionFactory, connectionName);
    }

    /**
     * <p>
     * Given a PreparedStatement representation of the SQL query returned by the getSqlQuery method, this method
     * extracts submission_id and resource_id values from the deliverable and sets them as
     * parameters of the PreparedStatement.
     * </p>
     *
     *
     * @param deliverable The deliverable from which to get any needed parameters to set on the PreparedStatement.
     * @param statement The PreparedStatement representation of the SQL query returned by getSqlQuery.
     * @throws SQLException if error occurs while filling the statement.
     * @throws DeliverableCheckingException if deliverable is not per-submission.
     */
    protected void fillInQueryParameters(Deliverable deliverable, PreparedStatement statement)
        throws SQLException, DeliverableCheckingException {
        statement.setLong(1, deliverable.getResource());
        statement.setLong(2, deliverable.getPhase());
    }

    /**
     * <p>
     * Gets the SQL query string to check that a submitter comment has been made. The returned query will have
     * two placeholders for the submission_id and resource_id values.
     * </p>
     *
     * @return The SQL query string to execute.
     */
    protected String getSqlQuery() {
        return "SELECT MAX(review_comment.modify_date), review.submission_id FROM review_comment "
                + "INNER JOIN comment_type_lu ON review_comment.comment_type_id = comment_type_lu.comment_type_id "
                + "INNER JOIN review ON review.review_id = review_comment.review_id "
                + "INNER JOIN resource ON review.resource_id = resource.resource_id "
                + "INNER JOIN phase_dependency ON resource.project_phase_id = phase_dependency.dependency_phase_id "
                + "WHERE review_comment.resource_id = ? "
                + "AND phase_dependency.dependent_phase_id = ? "
                + "AND comment_type_lu.name = 'Submitter Comment' "
                + "AND (review_comment.extra_info = 'Approved' OR review_comment.extra_info = 'Rejected') "
                + "GROUP BY review.submission_id";
    }
}
