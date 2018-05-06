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
 * The FinalReviewDeliverableChecker class subclasses the SingleQuerySqlDeliverableChecker class. The SQL query
 * used checks to see whether the final review for the submission is complete. If so, it marks the deliverable as
 * completed, using the date of the approval/rejection comment.
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
public class FinalReviewDeliverableChecker extends SingleQuerySqlDeliverableChecker {

    /**
     * Creates a new FinalReviewDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @throws IllegalArgumentException If connectionFactory is null.
     */
    public FinalReviewDeliverableChecker(DBConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    /**
     * Creates a new FinalReviewDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @param connectionName The name of the connection to use. Can be null.
     * @throws IllegalArgumentException If the connectionFactory is <code>null</code> or the connectionName
     * is the empty string.
     */
    public FinalReviewDeliverableChecker(DBConnectionFactory connectionFactory, String connectionName) {
        super(connectionFactory, connectionName);
    }

    /**
     * <p>
     * Given a PreparedStatement representation of the SQL query returned by the getSqlQuery method, this method
     * extracts submission_id and resource_id values from the deliverable and sets them as
     * parameters of the PreparedStatement.
     * </p>
     *
     * @param deliverable The deliverable from which to get any needed parameters to set on the PreparedStatement.
     * @param statement The PreparedStatement representation of the SQL query returned by getSqlQuery.
     * @throws SQLException if any error occurs while filling the statement
     * @throws DeliverableCheckingException if deliverable is not per-submission.
     */
    protected void fillInQueryParameters(Deliverable deliverable, PreparedStatement statement)
        throws SQLException, DeliverableCheckingException {
        statement.setLong(1, deliverable.getResource());
    }

    /**
     * <p>
     * Gets the SQL query string to select the modification date for the last final review approval/rejection.
     * The returned query will have 2 placeholders for the submission_id and resource_id values.
     * </p>
     *
     * @return The SQL query string to execute.
     */
    protected String getSqlQuery() {
        return "SELECT MAX(review_comment.modify_date), review.submission_id FROM review_comment "
                + "INNER JOIN comment_type_lu ON review_comment.comment_type_id = comment_type_lu.comment_type_id "
                + "INNER JOIN review ON review.review_id = review_comment.review_id "
                + "WHERE review_comment.resource_id = ? "
                + "AND comment_type_lu.name = 'Final Review Comment' "
                + "AND (review_comment.extra_info = 'Approved' OR review_comment.extra_info = 'Rejected') "
                + "GROUP BY review.submission_id";
    }
}
