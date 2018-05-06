/*
 * Copyright (c) 2006-2013, TopCoder, Inc. All rights reserved.
 */
package com.cronos.onlinereview.deliverables;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;

/**
 * <p>
 * The CommittedReviewDeliverableChecker class subclasses the SingleQuerySqlDeliverableChecker class. The SQL query
 * that it executes checks whether a given resource (in this case a reviewer) has completed a review for the
 * project and submission. If so, it marks the deliverable as complete, using the date the review was last changed.
 * </p>
 *
 * <p>
 * This class is immutable.
 * </p>
 *
 * @author aubergineanode, isv
 * @author kr00tki
 * @version 1.1.1
 */
public class CommittedReviewDeliverableChecker extends SingleQuerySqlDeliverableChecker {

    /**
     * <p>A <code>boolean</code> flag indicating whether the reviews are expected to be per submission or not.</p>
     *
     * @since 1.1
     */
    private boolean submissionDependent = true;

    /**
     * Creates a new CommittedReviewDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @throws IllegalArgumentException If connectionFactory is null.
     */
    public CommittedReviewDeliverableChecker(DBConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    /**
     * Creates a new CommittedReviewDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @param connectionName The name of the connection to use. Can be null.
     * @throws IllegalArgumentException If the connectionFactory is <code>null</code> or the connectionName is
     *         the empty string.
     */
    public CommittedReviewDeliverableChecker(DBConnectionFactory connectionFactory, String connectionName) {
        super(connectionFactory, connectionName);
    }

    /**
     * Creates a new CommittedReviewDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @throws IllegalArgumentException If connectionFactory is null.
     * @param submissionDependent <code>true</code> if reviews are expected to be done per submission;
     *        <code>false</code> otherwise.
     * @since 1.1
     */
    public CommittedReviewDeliverableChecker(DBConnectionFactory connectionFactory, boolean submissionDependent) {
        super(connectionFactory);
        this.submissionDependent = submissionDependent;
    }

    /**
     * <p>
     * Given a PreparedStatement representation of the SQL query returned by the getSqlQuery method, this method
     * extracts resource_id and submission_id values from the deliverable and sets them as
     * parameters of the PreparedStatement.
     * </p>
     *
     * @param deliverable The deliverable from which to get any needed parameters to set on the PreparedStatement.
     * @param statement The PreparedStatement representation of the SQL query returned by getSqlQuery.
     * @throws SQLException if any error occurs while setting the values to statement.
     * @throws DeliverableCheckingException if the deliverable is not per submission.
     */
    protected void fillInQueryParameters(Deliverable deliverable, PreparedStatement statement)
        throws SQLException, DeliverableCheckingException {
        if (this.submissionDependent) {
            if (!deliverable.isPerSubmission()) {
                throw new DeliverableCheckingException("The deliverable is not per submission and cannot be checked.");
            }
            statement.setLong(1, deliverable.getResource());
            statement.setLong(2, deliverable.getPhase());
            statement.setLong(3, deliverable.getSubmission());
        } else {
            if (deliverable.isPerSubmission()) {
                throw new DeliverableCheckingException("The deliverable is per submission and cannot be checked.");
            }
            statement.setLong(1, deliverable.getResource());
            statement.setLong(2, deliverable.getPhase());
        }
    }

    /**
     * <p>
     * Gets the SQL query string to select the last modification date for the review scorecard for the
     * resource(reviewer)/submission. Returned query will have 2 placeholders for the resource_id and submission_id
     * values.
     * </p>
     *
     * @return The SQL query string to execute.
     */
    protected String getSqlQuery() {
        if (this.submissionDependent) {
            return "SELECT modify_date FROM review WHERE committed = 1 AND resource_id = ? AND project_phase_id = ? " +
                    "AND submission_id = ?";
        } else {
            return "SELECT modify_date FROM review WHERE committed = 1 AND resource_id = ? AND project_phase_id = ? ";
        }
    }
}
