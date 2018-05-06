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
 * The TestCasesDeliverableChecker class subclasses the SingleQuerySqlDeliverableChecker class. The SQL query that
 * it executes checks whether a given resource (in this case a reviewer) has uploaded a test suite. If so, it marks
 * the deliverable as completed, using the date of the last test suite upload.
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
public class TestCasesDeliverableChecker extends SingleQuerySqlDeliverableChecker {

    /**
     * Creates a new TestCasesDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @throws IllegalArgumentException If connectionFactory is null.
     */
    public TestCasesDeliverableChecker(DBConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    /**
     * Creates a new TestCasesDeliverableChecker.
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @param connectionName The name of the connection to use. Can be null.
     * @throws IllegalArgumentException If the connectionFactory is <code>null</code> or the connectionName
     * is the empty string.
     */
    public TestCasesDeliverableChecker(DBConnectionFactory connectionFactory, String connectionName) {
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
     * @throws SQLException if an error occurs while filling the statement.
     */
    protected void fillInQueryParameters(Deliverable deliverable, PreparedStatement statement) throws SQLException {
        statement.setLong(1, deliverable.getResource());
    }

    /**
     * <p>
     * Gets the SQL query string to select the last modification date for a test case upload. Returned query
     * will have two placeholders for project_id and resource_id values.
     * </p>
     *
     * @return The SQL query string to execute.
     */
    protected String getSqlQuery() {
        return "SELECT MAX(upload.modify_date) FROM upload "
                + "INNER JOIN upload_type_lu ON upload.upload_type_id = upload_type_lu.upload_type_id "
                + "INNER JOIN upload_status_lu ON upload.upload_status_id = upload_status_lu.upload_status_id "
                + "WHERE upload_type_lu.name = 'Test Case' AND upload_status_lu.name = 'Active' "
                + "AND upload.resource_id = ?";
    }
}
