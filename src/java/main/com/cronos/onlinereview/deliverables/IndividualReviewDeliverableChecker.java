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
 * The CommittedReviewDeliverableChecker class subclasses the SingleQuerySqlDeliverableChecker class. The SQL query
 * that it executes checks whether a given resource (in this case a reviewer) has completed a review for the
 * project and submission. If so, it marks the deliverable as complete, using the date the review was last changed.
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
public class IndividualReviewDeliverableChecker extends SingleQuerySqlDeliverableChecker {

    /**
     * Creates a new CommittedReviewDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @throws IllegalArgumentException If connectionFactory is null.
     */
    public IndividualReviewDeliverableChecker(DBConnectionFactory connectionFactory) {
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
    public IndividualReviewDeliverableChecker(DBConnectionFactory connectionFactory, String connectionName) {
        super(connectionFactory, connectionName);
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
        statement.setLong(1, deliverable.getResource());
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
        return "SELECT review.modify_date FROM review, resource_submission WHERE committed = 1 AND review.resource_id = ? "
                + "AND review.resource_id = resource_submission.resource_id AND review.submission_id = resource_submission.submission_id";
    }
}
