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
 * The AggregationDeliverableChecker class subclasses the SingleQuerySqlDeliverableChecker class. The SQL query
 * that it executes checks whether a given resource (in this case a reviewer) has committed a review. (Note that,
 * unlike the CommittedReviewDeliverableChecker class, the committed review is per project and not per submission).
 * If there is a committed aggregation review, it marks the deliverable as completed, using the date the review was
 * last modified.
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
public class AggregationDeliverableChecker extends SingleQuerySqlDeliverableChecker {

    /**
     * Creates a new AggregationDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @throws IllegalArgumentException If connectionFactory is null.
     */
    public AggregationDeliverableChecker(DBConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    /**
     * Creates a new AggregationDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @param connectionName The name of the connection to use. Can be null.
     * @throws IllegalArgumentException If the connectionFactory is <code>null</code> or the connectionName
     * is the empty string.
     */
    public AggregationDeliverableChecker(DBConnectionFactory connectionFactory, String connectionName) {
        super(connectionFactory, connectionName);
    }

    /**
     * <p>
     * Given a PreparedStatement representation of the SQL query returned by the getSqlQuery method, this method
     * extracts resource id and project id values from the deliverable and sets them as
     * parameters of the PreparedStatement.
     * </p>
     *
     * @param deliverable The deliverable from which to get any needed parameters to set on the PreparedStatement.
     * @param statement The PreparedStatement representation of the SQL query returned by getSqlQuery.
     * @throws SQLException if any error occurs while setting the values to statement.
     */
    protected void fillInQueryParameters(Deliverable deliverable, PreparedStatement statement) throws SQLException {
        statement.setLong(1, deliverable.getResource());
    }

    /**
     * <p>
     * Gets the SQL query string to select the modification date of the aggregation review scorecard. Returned
     * query will have two placeholders for the resource_id and project_id values.
     * </p>
     *
     * @return The SQL query string to execute.
     */
    protected String getSqlQuery() {
        return "SELECT modify_date FROM review WHERE committed = 1 AND resource_id = ?";
    }
}
