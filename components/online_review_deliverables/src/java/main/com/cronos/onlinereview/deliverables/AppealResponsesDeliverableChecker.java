/*
 * Copyright (c) 2006-2013, TopCoder, Inc. All rights reserved.
 */
package com.cronos.onlinereview.deliverables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;

/**
 * <p>
 * The AppealResponsesDeliverableChecker class subclasses the SqlDeliverableChecker class and checks that every
 * <tt>Appeal</tt> comment has been responded to with an <tt>Appeal Response</tt>. If this is the case, the
 * deliverable is marked as complete.
 * </p>
 *
 * <p>
 * This class is immutable.
 * </p>
 *
 * @author aubergineanode
 * @author kr00tki
 * @version 1.1.1s
 */
public class AppealResponsesDeliverableChecker extends SqlDeliverableChecker {

    /**
     * This SQL query returns all the modify dates for all responses with type 'Appeal Response'
     * for given resource id.
     */
    private static final String QUERY = "SELECT appeal_response_comment.modify_date FROM review_item_comment "
            + "appeal_comment "
            + "INNER JOIN comment_type_lu appeal_comment_type ON appeal_comment.comment_type_id = "
            + "appeal_comment_type.comment_type_id AND appeal_comment_type.name = 'Appeal' "
            + "INNER JOIN review_item ON appeal_comment.review_item_id = review_item.review_item_id "
            + "INNER JOIN review ON review_item.review_id = review.review_id AND review.resource_id = ? AND review.submission_id = ? "
            + "LEFT OUTER JOIN (review_item_comment appeal_response_comment "
            + "INNER JOIN comment_type_lu appeal_response_comment_type "
            + "ON appeal_response_comment.comment_type_id = appeal_response_comment_type.comment_type_id "
            + "AND appeal_response_comment_type.name = 'Appeal Response') "
            + "ON appeal_response_comment.review_item_id = appeal_comment.review_item_id";

    /**
     * AppealResponsesDeliverableChecker constructor: Creates a new AppealResponsesDeliverableChecker. Simply calls
     * the superclass constructor.
     *
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @throws IllegalArgumentException If connectionFactory is null.
     */
    public AppealResponsesDeliverableChecker(DBConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    /**
     * AppealResponsesDeliverableChecker constructor: Creates a new AppealResponsesDeliverableChecker. Simply calls
     * the superclass constructor.
     *
     *
     * @param connectionFactory The connection factory to use for getting connections to the database.
     * @param connectionName The name of the connection to use. Can be null.
     * @throws IllegalArgumentException If the connectionFactory is <code>null</code> or the connectionName
     * is the empty string.
     */
    public AppealResponsesDeliverableChecker(DBConnectionFactory connectionFactory, String connectionName) {
        super(connectionFactory, connectionName);
    }

    /**
     * <p>
     * Checks the given deliverable to see if it is complete. This method queries the database to select, for each
     * "Appeal" comment, the "Appeal Response" comment.
     * </p>
     *
     * @param deliverable The deliverable to check
     * @throws IllegalArgumentException If deliverable is null
     * @throws DeliverableCheckingException If there is an error checking the deliverable due to an underlying
     *  database error.
     */
    public void check(Deliverable deliverable) throws DeliverableCheckingException {
        if (deliverable == null) {
            throw new IllegalArgumentException("deliverable cannot be null.");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // get the connection
            conn = createDatabaseConnection();
            pstmt = conn.prepareStatement(QUERY);
            // set resource id and submission id
            pstmt.setLong(1, deliverable.getResource());
            pstmt.setLong(2, deliverable.getSubmission());

            // execute
            rs = pstmt.executeQuery();
            // no appeal at all, fill current date for workaround
            boolean hasAppeals = false;

            // the last update date
            Timestamp lastDate = null;
            while (rs.next()) {
                hasAppeals = true;
                // get the date, if null - just return
                Timestamp temp = rs.getTimestamp(1);
                if (temp == null) {
                    return;
                }

                // if last date is null - just set it; else get the later date
                if (lastDate == null) {
                    lastDate = temp;
                } else if (lastDate.before(temp)) {
                    lastDate = temp;
                }
            }

            // if there was a completion date - set it
            if (!hasAppeals) {
                deliverable.setCompletionDate(new Date());
            } else if (lastDate != null) {
                deliverable.setCompletionDate(lastDate);
            }

        } catch (SQLException ex) {
            throw new DeliverableCheckingException("Error occurs while checking in database.", ex);
        } catch (DBConnectionException ex) {
            throw new DeliverableCheckingException("Error occurs while creating connection.", ex);
        } finally {
            close(conn, pstmt, rs);
        }
    }
}
