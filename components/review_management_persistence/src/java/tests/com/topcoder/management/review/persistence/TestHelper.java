/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * This is helper class for tests.
 * </p>
 * @author TCSDEVELOPER
 * @version 1.2
 * @since 1.2
 */
class TestHelper {

    /**
     * <p>
     * Represents database connection factory configuration file.
     * </p>
     */
    private static final String DB_CONNECTION_FACTORY_CONFIG_FILE = System.getProperty("user.dir") + File.separator
            + "test_files" + File.separator + "dbfactory.xml";

    /**
     * <p>
     * Prevents from instantiating.
     * </p>
     */
    private TestHelper() {
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     * @throws Exception if any error occurs
     */
    static void tearDownTest() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator<String> it = cm.getAllNamespaces();
        while (it.hasNext()) {
            cm.removeNamespace(it.next());
        }
        // load the configurations for db connection factory
        cm.add("dbfactory.xml");
        // create the connection
        Connection conn = getDBConnectionFactory().createConnection();
        // clear the tables
        Helper.doDMLQuery(conn, "DELETE FROM review_item_comment", new Object[] {});
        Helper.doDMLQuery(conn, "DELETE FROM review_item", new Object[] {});
        Helper.doDMLQuery(conn, "DELETE FROM review_comment", new Object[] {});
        Helper.doDMLQuery(conn, "DELETE FROM review", new Object[] {});
        Helper.doDMLQuery(conn, "DELETE FROM comment_type_lu", new Object[] {});
        Helper.doDMLQuery(conn, "DELETE FROM resource", new Object[] {});
        Helper.doDMLQuery(conn, "DELETE FROM submission", new Object[] {});
        Helper.doDMLQuery(conn, "DELETE FROM upload", new Object[] {});
        Helper.doDMLQuery(conn, "DELETE FROM scorecard", new Object[] {});
        Helper.doDMLQuery(conn, "DELETE FROM scorecard_type_lu", new Object[] {});
        Helper.doDMLQuery(conn, "DELETE FROM scorecard_question", new Object[] {});
        Helper.doDMLQuery(conn, "DELETE FROM id_sequences", new Object[] {});
        // insert data into id_sequences table
        Helper.doDMLQuery(conn, "INSERT INTO id_sequences(name, next_block_start, "
                + "block_size, exhausted) VALUES('review_id_seq', 1, 1, 0)", new Object[] {});
        Helper.doDMLQuery(conn, "INSERT INTO id_sequences(name, next_block_start, "
                + "block_size, exhausted) VALUES('review_item_id_seq', 1, 1, 0);", new Object[] {});
        Helper.doDMLQuery(conn, "INSERT INTO id_sequences(name, next_block_start, "
                + "block_size, exhausted) VALUES('review_comment_id_seq', 1, 1, 0);", new Object[] {});
        Helper.doDMLQuery(conn, "INSERT INTO id_sequences(name, next_block_start, "
                + "block_size, exhausted) VALUES('review_item_comment_id_seq', 1, 1, 0); ", new Object[] {});
        conn.close();
        it = cm.getAllNamespaces();
        while (it.hasNext()) {
            cm.removeNamespace(it.next());
        }
    }

    /**
     * <p>
     * Sets up test environment.
     * </p>
     * @throws Exception if any error occurs
     */
    static void setUpTest() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        // load the configurations for db connection factory
        cm.add("dbfactory.xml");
        // load the configurations for search bundle manager
        cm.add("search_bundle_manager.xml");
        // load the configurations for InformixReviewPersistence
        cm.add("informix_persistence.xml");
        // load the configurations for logging
        cm.add("logging.xml");
        // create the connection
        Connection conn = getDBConnectionFactory().createConnection();
        try {
            // insert data into comment_type_lu table
            Helper.doDMLQuery(conn, "INSERT INTO comment_type_lu"
                    + " (comment_type_id, name, description, create_user,"
                    + " create_date, modify_user, modify_date) values"
                    + "(1, 'type 1', 'comment type 1', 'topcoder', CURRENT, 'topcoder', CURRENT)", new Object[] {});
            Helper.doDMLQuery(conn, "INSERT INTO comment_type_lu"
                    + " (comment_type_id, name, description, create_user,"
                    + " create_date, modify_user, modify_date) values"
                    + "(2, 'type 2', 'comment type 2', 'topcoder', CURRENT, 'topcoder', CURRENT)", new Object[] {});
            Helper.doDMLQuery(conn, "INSERT INTO comment_type_lu"
                    + " (comment_type_id, name, description, create_user,"
                    + " create_date, modify_user, modify_date) values"
                    + "(3, 'type 3', 'comment type 3', 'topcoder', CURRENT, 'topcoder', CURRENT)", new Object[] {});
            // insert data into resource table
            insertResources(conn);
            // insert data into submission table
            Helper.doDMLQuery(conn, "INSERT INTO submission (submission_id) values (1)", new Object[] {});
            Helper.doDMLQuery(conn, "INSERT INTO submission (submission_id) values (2)", new Object[] {});
            Helper.doDMLQuery(conn, "INSERT INTO submission (submission_id) values (3)", new Object[] {});
            // insert data into upload table
            Helper.doDMLQuery(conn, "INSERT INTO upload (upload_id) values (1)", new Object[] {});
            Helper.doDMLQuery(conn, "INSERT INTO upload (upload_id) values (2)", new Object[] {});
            Helper.doDMLQuery(conn, "INSERT INTO upload (upload_id) values (3)", new Object[] {});
            // insert data into scorecard_question table
            Helper.doDMLQuery(conn, "INSERT INTO scorecard_question (scorecard_question_id) values (1)",
                    new Object[] {});
            Helper.doDMLQuery(conn, "INSERT INTO scorecard_question (scorecard_question_id) values (2)",
                    new Object[] {});
            Helper.doDMLQuery(conn, "INSERT INTO scorecard_question (scorecard_question_id) values (3)",
                    new Object[] {});
            // insert data into scorecard_type_lu table
            Helper.doDMLQuery(conn, "INSERT INTO scorecard_type_lu (scorecard_type_id) values (1)", new Object[] {});
            Helper.doDMLQuery(conn, "INSERT INTO scorecard_type_lu (scorecard_type_id) values (2)", new Object[] {});
            Helper.doDMLQuery(conn, "INSERT INTO scorecard_type_lu (scorecard_type_id) values (3)", new Object[] {});
            // insert data into scorecard table
            Helper.doDMLQuery(conn, "INSERT INTO scorecard (scorecard_id, scorecard_type_id) values (1, 1)",
                    new Object[] {});
            Helper.doDMLQuery(conn, "INSERT INTO scorecard (scorecard_id, scorecard_type_id) values (2, 2)",
                    new Object[] {});
            Helper.doDMLQuery(conn, "INSERT INTO scorecard (scorecard_id, scorecard_type_id) values (3, 1)",
                    new Object[] {});
            Helper.doDMLQuery(conn, "INSERT INTO scorecard (scorecard_id, scorecard_type_id) values (4, 2)",
                    new Object[] {});
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Inserts data into resource table.
     * </p>
     * @param conn the connection to perform insert
     * @throws Exception if any error occurs
     */
    private static void insertResources(Connection conn) throws Exception {
        Helper.doDMLQuery(conn, "INSERT INTO resource (resource_id, project_id) values (1,1)", new Object[] {});
        Helper.doDMLQuery(conn, "INSERT INTO resource (resource_id, project_id) values (2,1)", new Object[] {});
        Helper.doDMLQuery(conn, "INSERT INTO resource (resource_id, project_id) values (3,1)", new Object[] {});
        Helper.doDMLQuery(conn, "INSERT INTO resource (resource_id) values (4)", new Object[] {});
        Helper.doDMLQuery(conn, "INSERT INTO resource (resource_id) values (5)", new Object[] {});
        Helper.doDMLQuery(conn, "INSERT INTO resource (resource_id) values (6)", new Object[] {});
    }

    /**
     * Get a sample review object to test.
     * @return a sample review object
     */
    static Review getSampleReview() {
        CommentType commentType = new CommentType();
        commentType.setId(1);
        commentType.setName("type 1");
        Review review = createSampleReview();
        // add review comments
        Comment comment = new Comment();
        comment.setAuthor(1);
        comment.setCommentType(commentType);
        comment.setComment("comment 1");
        review.addComment(comment);
        review.addComment(createComment(commentType, 2, "comment 2", "extra info"));
        // add items
        Item item = new Item();
        item.setQuestion(1);
        item.setAnswer("answer 1");
        comment = new Comment();
        comment.setAuthor(3);
        comment.setCommentType(commentType);
        comment.setComment("comment 3");
        item.addComment(comment);
        item.addComment(createComment(commentType, 4, "comment 4", "extra info"));
        review.addItem(item);
        // add items
        item = new Item();
        item.setQuestion(2);
        item.setAnswer("answer 2");
        item.addComment(createComment(commentType, 5, "comment 5", null));
        item.addComment(createComment(commentType, 6, "comment 6", "extra info"));
        review.addItem(item);
        return review;
    }

    /**
     * <p>
     * Creates Comment instance with given values.
     * </p>
     * @param commentType the comment type
     * @param author the author
     * @param commentInfo the comment
     * @param extraInfo the extra info to set
     * @return created Comment instance with given values
     */
    private static Comment createComment(CommentType commentType, long author, String commentInfo, String extraInfo) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setCommentType(commentType);
        comment.setComment(commentInfo);
        comment.setExtraInfo(extraInfo);
        return comment;
    }

    /**
     * <p>
     * Creates sample review with predefined values.
     * </p>
     * @return created sample review with predefined values
     */
    private static Review createSampleReview() {
        Review review = new Review();
        review.setAuthor(1);
        review.setSubmission(2);
        review.setScorecard(3);
        review.setScore(new Float("98.5"));
        review.setCommitted(true);
        return review;
    }

    /**
     * Closes given connection.
     * @param conn the connection to close
     */
    static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    /**
     * <p>
     * Retrieves database connection factory from configuration.
     * </p>
     * @return DBConnectionFactoryImpl connection factory created from configuration
     * @throws Exception if any error occurs
     */
    static DBConnectionFactoryImpl getDBConnectionFactory() throws Exception {
        ConfigurationObject configurationObject =
                new XMLFilePersistence().loadFile("dbConnectionFactoryConfig", new File(
                        DB_CONNECTION_FACTORY_CONFIG_FILE));
        return new DBConnectionFactoryImpl(configurationObject);
    }
}
