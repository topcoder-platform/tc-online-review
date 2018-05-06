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
 * The IndividualReviewDeliverableChecker class subclasses the SingleQuerySqlDeliverableChecker class. The SQL query
 * that it executes checks whether a given resource (in this case a reviewer) has completed a review for the
 * project and the only submission he/she is associated with. If so, it marks the deliverable as complete,
 * using the date the review was last changed.  In addition, the submission is is filled because such deliverable
 * isn't per submission.
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
     * Creates a new IndividualReviewDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @throws IllegalArgumentException If connectionFactory is null.
     */
    public IndividualReviewDeliverableChecker(DBConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    /**
     * Creates a new IndividualReviewDeliverableChecker.
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
     * extracts resource_id value from the deliverable and sets them as the parameter of the PreparedStatement.
     * </p>
     *
     * @param deliverable The deliverable from which to get any needed parameters to set on the PreparedStatement.
     * @param statement The PreparedStatement representation of the SQL query returned by getSqlQuery.
     * @throws SQLException if any error occurs while setting the values to statement.
     */
    protected void fillInQueryParameters(Deliverable deliverable, PreparedStatement statement)
        throws SQLException {
        statement.setLong(1, deliverable.getResource());
    }

    /**
     * <p>
     * Gets the SQL query string to select the last modification date for the review scorecard for the
     * resource(reviewer). Returned query will have 1 placeholder for the resource_id value.
     * </p>
     *
     * @return The SQL query string to execute.
     */
    protected String getSqlQuery() {
        return "SELECT review.modify_date, resource_submission.submission_id FROM resource_submission "
                + "LEFT JOIN review ON review.resource_id = resource_submission.resource_id "
                + "AND review.submission_id = resource_submission.submission_id AND committed = 1 "
                + "WHERE resource_submission.resource_id = ?";
    }
}
