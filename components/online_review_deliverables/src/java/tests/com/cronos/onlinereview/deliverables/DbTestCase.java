/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.cronos.onlinereview.deliverables;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;

import junit.framework.TestCase;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;

/**
 * Base class for all database tests. It contains common test helper methods.
 *
 * @author kr00tki
 * @version 1.0
 */
public class DbTestCase extends TestCase {
    /**
     * The 'Active' status id value.
     */
    public static final int ACTIVE = 1;

    /**
     * The 'Inactive' status id value.
     */
    public static final int INACTIVE = 2;

    /**
     * The 'Appeal' type id.
     */
    public static final int APPEAL = 4;

    /**
     * The 'Appeal Response' type id.
     */
    public static final int APPEAL_RESPONSE = 5;

    /**
     * The 'Test Case' type id.
     */
    public static final int TEST_CASE_TYPE = 2;

    /**
     * The 'Submission' type id.
     */
    public static final int SUBMISSION = 1;

    /**
     * The 'Final Fix' type id.
     */
    public static final int FINAL_FIX_TYPE = 3;

    /**
     * The 'Submitter Comment' type id.
     */
    public static final int SUBMITTER_COMMENT = 8;

    /**
     * The 'Final Review' type id.
     */
    public static final int FINAL_REVIEW_COMMENT = 10;

    /**
     * The 'Aggregation Review' type id.
     */
    public static final int AGGREGATION_REVIEW_COMMENT = 7;

    /**
     * The 'Approved' extra info value.
     */
    public static final String APPROVED = "Approved";

    /**
     * The 'Rejected' extra info value.
     */
    public static final String REJECTED = "Rejected";

    /**
     * Database connection name.
     */
    protected static final String CONNECTION_NAME = "informix";

    /**
     * Db Connection Factory namespace.
     */
    protected static final String DB_CONN_FACTORY_NAMESPACE = DBConnectionFactoryImpl.class.getName();

    /**
     * Db Connection Factory config file.
     */
    private static final String DB_CONN_FACTORY_CONF = "db_conf.xml";

    /**
     * The DBManager used to manipulate on database.
     */
    protected DbManager testDBManager = null;

    /**
     * Db connection.
     */
    private Connection connection = null;

    /**
     * ResultSet instance.
     */
    private ResultSet resultSet = null;

    /**
     * Statement instance.
     */
    private Statement statement = null;

    /**
     * The database connection factory used in tests.
     */
    private DBConnectionFactory factory = null;

    /**
     * Sets up test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        clearConfigManager();
        ConfigManager.getInstance().add(DB_CONN_FACTORY_CONF);
        // ConfigManager.getInstance().add(CONF_FILE);
        ConfigManager.getInstance().add("db_manager_conf.xml");
        testDBManager = new DbManager();
        testDBManager.clearTables();
        testDBManager.loadDataSet("test_files/dataset.xml");
    }

    /**
     * Clears after test.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        testDBManager.clearTables();
        testDBManager.release();

        if (resultSet != null) {
            resultSet.close();
            resultSet = null;
        }

        if (statement != null) {
            statement.close();
            statement = null;
        }

        if (connection != null) {
            connection.close();
            connection = null;
        }
        clearConfigManager();
    }

    /**
     * Returns the database connection factory used in tests.
     *
     * @return DBConnectionFactory instance.
     * @throws Exception to JUnit.
     */
    protected DBConnectionFactory getConnectionFactory() throws Exception {
        if (factory == null) {
            factory = new DBConnectionFactoryImpl(DB_CONN_FACTORY_NAMESPACE);
        }

        return factory;
    }

    /**
     * Removes all namespaces from Config Manager.
     *
     * @throws Exception to JUnit.
     */
    protected void clearConfigManager() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        for (Iterator it = cm.getAllNamespaces(); it.hasNext();) {
            String ns = (String) it.next();
            cm.removeNamespace(ns);
        }
    }

    /**
     * Creates db connection.
     *
     * @return db connection.
     * @throws Exception to JUnit.
     */
    protected Connection getConnection() throws Exception {
        if (connection == null) {
            connection = getConnectionFactory().createConnection(CONNECTION_NAME);
        }

        return connection;
    }

    /**
     * Returns the field from the very first base class of given source object.
     *
     * @param source the source object.
     * @param fieldName the name of the field in the super class.
     * @return the field value (may be null).
     *
     * @throws Exception to JUnit.
     */
    protected static Object getFieldFromBaseClass(Object source, String fieldName) throws Exception {
        Class cl = source.getClass();
        while (cl != null) {
            if (cl.getSuperclass() == Object.class) {
                break;
            }

            cl = cl.getSuperclass();
        }
        Field field = cl.getDeclaredField(fieldName);
        field.setAccessible(true);

        return field.get(source);
    }

    /**
     * Returns ResultSet for given query.
     *
     * @param query db query.
     * @return query resul
     * @throws Exception to JUnit.
     */
    protected ResultSet getResultSet(String query) throws Exception {
        Connection conn = getConnection();
        statement = conn.createStatement();
        resultSet = statement.executeQuery(query);
        return resultSet;
    }

    /**
     * Asserts test by query.
     *
     * @param assertionMessage assertion message.
     * @param binarySQLQuery SQL query that should return something or not.
     * @param expected expected true or false.
     * @throws Exception to JUnit.
     */
    protected void assertByQuery(String assertionMessage, String binarySQLQuery, boolean expected)
        throws Exception {
        ResultSet rs = null;
        try {
            rs = getResultSet(binarySQLQuery);
            assertEquals(assertionMessage, expected, rs.next());
        } finally {
            rs.close();
            statement.close();
        }
    }

    /**
     * Helper method to execute SQL command.
     *
     * @param sqlQuery query.
     * @throws Exception to JUnit.
     */
    protected void execute(String sqlQuery) throws Exception {
        statement = getConnection().createStatement();
        statement.execute(sqlQuery);
    }

    /**
     * Creates the entry in the review table.
     *
     * @param id the id of the new review.
     * @param resourceId the resource id of the review.
     * @param submissionId the submission id of the review.
     * @param commited indicates if the review is commited or not.
     *
     * @throws Exception to JUnit.
     */
    protected void createReview(long id, long resourceId, long submissionId, boolean commited) throws Exception {
        PreparedStatement pstmt = null;
        String query = "INSERT INTO review (review_id, resource_id, submission_id, scorecard_id, committed,  "
                + "score, create_user, create_date, modify_user, modify_date) VALUES (?, ?, ?, 1, ?, 10, 'sys', "
                + "CURRENT, 'sys', CURRENT)";
        try {
            pstmt = getConnection().prepareStatement(query);
            pstmt.setLong(1, id);
            pstmt.setLong(2, resourceId);
            pstmt.setLong(3, submissionId);
            pstmt.setBoolean(4, commited);

            pstmt.execute();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    /**
     * Creates the entry in the review_comment table.
     *
     * @param id the id of the entry.
     * @param type the id of the comment type.
     * @param resourceId the id of the resource.
     * @param extraInfo the extra info for the comment.
     * @param reviewId the id of the comment's review.
     *
     * @throws Exception to JUnit.
     */
    protected void createReviewComment(long id, long type, long resourceId, String extraInfo, long reviewId)
        throws Exception {
        String query = "INSERT INTO review_comment (review_comment_id, resource_id, review_id, comment_type_id, "
                + "content, extra_info,  sort, create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, ?, 'content', ?, '1', 'x', CURRENT, 'c', CURRENT)";

        PreparedStatement pstmt = null;
        try {
            pstmt = getConnection().prepareStatement(query);
            pstmt.setLong(1, id);
            pstmt.setLong(2, resourceId);
            pstmt.setLong(3, reviewId);
            pstmt.setLong(4, type);
            pstmt.setString(5, extraInfo);

            pstmt.execute();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    /**
     * Creates the review_item_comment entry in the database.
     *
     * @param id the id of the item comment.
     * @param type the id of the comment type.
     * @param resourceId the id of the resource.
     * @param extraInfo the extra info value for commment item.
     * @param reviewItemId the id of the review item.
     *
     * @throws Exception to JUnit.
     */
    protected void createReviewItemComment(long id, long type, long resourceId, String extraInfo, long reviewItemId)
        throws Exception {
        String query = "INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, "
                + "comment_type_id, content, extra_info,  sort, create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, ?, 'content', ?, '1', 'x', CURRENT, 'c', CURRENT)";

        PreparedStatement pstmt = null;
        try {
            pstmt = getConnection().prepareStatement(query);
            pstmt.setLong(1, id);
            pstmt.setLong(2, resourceId);
            pstmt.setLong(3, reviewItemId);
            pstmt.setLong(4, type);
            pstmt.setString(5, extraInfo);

            pstmt.execute();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    /**
     * Creates the review_item entry in the database.
     *
     * @param id the id of the review item.
     * @param review the id of the review.
     *
     * @throws Exception to JUnit.
     */
    protected void createReviewItem(long id, long review) throws Exception {
        String query = "INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, "
                + "answer,  sort, create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, 1, 'x', '1', 'x', CURRENT, 'c', CURRENT)";

        PreparedStatement pstmt = null;
        try {
            pstmt = getConnection().prepareStatement(query);
            pstmt.setLong(1, id);
            pstmt.setLong(2, review);

            pstmt.execute();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    /**
     * Creates the upload entry in the database.
     *
     * @param id the id of the upload.
     * @param type the id of the upload type.
     * @param status the is of the status.
     * @param project the id of the project.
     * @param resourceId the id of the resource.
     *
     * @throws Exception to JUnit.
     */
    protected void createUpload(long id, long type, long status, long project, long resourceId) throws Exception {
        String query = "INSERT INTO upload (upload_id, project_id, resource_id, upload_type_id, "
                + "upload_status_id, parameter, create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, ?, ?, 'x', 'x', CURRENT, 'c', CURRENT)";

        PreparedStatement pstmt = null;
        try {
            pstmt = getConnection().prepareStatement(query);
            pstmt.setLong(1, id);
            pstmt.setLong(2, project);
            pstmt.setLong(3, resourceId);
            pstmt.setLong(4, type);
            pstmt.setLong(5, status);

            pstmt.execute();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
}
