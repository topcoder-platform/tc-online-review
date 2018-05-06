/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback.accuracytests;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.reviewfeedback.ReviewFeedback;
import com.topcoder.management.reviewfeedback.ReviewFeedbackDetail;
import com.topcoder.management.reviewfeedback.impl.JDBCReviewFeedbackManager;
import com.topcoder.util.log.LogManager;


/**
 * Accuracy tests for JDBCReviewFeedbackManager.
 *
 * @author liuliquan
 * @version 2.0
 */
public class JDBCReviewFeedbackManagerAccTests {

    /**
     * <p>
     * The audit type for create.
     * </p>
     */
    private static final long CREATE_AUDIT_TYPE = 1;

    /**
     * <p>
     * The audit type for update.
     * </p>
     */
    private static final long UPDATE_AUDIT_TYPE = 2;

    /**
     * <p>
     * The audit type for delete.
     * </p>
     */
    private static final long DELETE_AUDIT_TYPE = 3;

    /**
     * <p>
     * Represents the configuration file used in tests.
     * </p>
     */
    private static final String CONFIG_FILE = "/config.xml";

    /**
     * <p>
     * Represents the operator used in tests.
     * </p>
     */
    private static final String OPERATOR = "accuracy_reviewer";

    /**
     * <p>
     * Represents the modifier used in tests.
     * </p>
     */
    private static final String MODIFIER = "accuracy_modifier";

    /**
     * <p>
     * Represents the <code>JDBCReviewFeedbackManager</code> instance to be tested.
     * </p>
     */
    private JDBCReviewFeedbackManager instance;

    /**
     * <p>
     * Represents the <code>DBConnectionFactory</code> used in tests.
     * </p>
     */
    private DBConnectionFactory factory;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(JDBCReviewFeedbackManagerAccTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    @Before
    public void setUp() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();

        // Get configuration
        ConfigurationObject config = persistence.loadFile("com.topcoder.management.reviewfeedback", new File(
                this.getClass().getResource(CONFIG_FILE).toURI().getPath()));

        ConfigurationObject dbConfig = config.getChild("dbConnectionFactoryConfiguration");

        factory = new DBConnectionFactoryImpl(dbConfig);

        instance = new JDBCReviewFeedbackManager(factory, "TCSCatalog", LogManager.getLog(getClass().getName()),
                CREATE_AUDIT_TYPE, UPDATE_AUDIT_TYPE, DELETE_AUDIT_TYPE);

        clearDB();
        setupDB();
    }

    /**
     * <p>
     * Cleans up the unit tests.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    @After
    public void tearDown() throws Exception {
        clearDB();
    }

    /**
     * <p>
     * Cleans up the database.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    private void clearDB() throws Exception {
        Connection conn = factory.createConnection();
        try {
            Statement statement = conn.createStatement();
            try {
                statement.addBatch("delete from 'informix'.review_feedback_detail_audit");
                statement.addBatch("delete from 'informix'.review_feedback_audit");
                statement.addBatch("delete from 'informix'.audit_action_type_lu");
                statement.addBatch("delete from 'informix'.review_feedback_detail");
                statement.addBatch("delete from 'informix'.review_feedback");
                statement.addBatch("delete from 'informix'.project");
                statement.executeBatch();
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }


    /**
     * <p>
     * Sets up the database.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    private void setupDB() throws Exception {
        Connection conn = factory.createConnection();
        try {
            Statement statement = conn.createStatement();
            try {
                // Prepare a project
                statement.addBatch(
                    "insert into 'informix'.project(project_id,project_status_id,project_category_id,"
                    + "project_studio_spec_id,create_user,create_date,modify_user,modify_date,"
                    + "tc_direct_project_id) values (1,1,1,1,'user',CURRENT,'user',CURRENT,1);");

                // The audit action type for "create": id is 1
                statement.addBatch("insert into 'informix'.audit_action_type_lu(audit_action_type_id,"
                    + "name, description, create_user, create_date, modify_user, modify_date) values "
                    + "(1,'create','create','user',CURRENT,'user',CURRENT);");
                // The audit action type for "update": id is 2
                statement.addBatch("insert into 'informix'.audit_action_type_lu(audit_action_type_id,"
                    + "name, description, create_user, create_date, modify_user, modify_date) values "
                    + "(2,'update','update','user',CURRENT,'user',CURRENT);");
                // The audit action type for "delete": id is 3
                statement.addBatch("insert into 'informix'.audit_action_type_lu(audit_action_type_id,"
                    + "name, description, create_user, create_date, modify_user, modify_date) values "
                    + "(3,'delete','delete','user',CURRENT,'user',CURRENT);");
                statement.executeBatch();
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    /**
     * <p>
     * Accuracy test for JDBCReviewFeedbackManager.
     * The create of ReviewFeedback entity should be audited correctly.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testReviewFeedback_Create_Audit() throws Exception {

        ReviewFeedback entity = this.createReviewFeedback(1, "comment");

        // Create ReviewFeedback entity
        entity = instance.create(entity, OPERATOR);

        // Check audit record exists for create
        this.checkReviewFeedbackAuditExists(entity.getId(), entity.getProjectId(),
                entity.getComment(), CREATE_AUDIT_TYPE, OPERATOR, entity.getCreateDate());

        // Check detail audit records exist for create
        for (ReviewFeedbackDetail detail : entity.getDetails()) {
            this.checkReviewFeedbackDetailAuditExists(entity.getId(), detail.getReviewerUserId(),
                    detail.getScore(), detail.getFeedbackText(), CREATE_AUDIT_TYPE, OPERATOR, entity.getCreateDate());
        }
    }

    /**
     * <p>
     * Accuracy test for JDBCReviewFeedbackManager.
     * The update of ReviewFeedback entity should be audited correctly.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testReviewFeedback_Update_Audit() throws Exception {

        ReviewFeedback entity = this.createReviewFeedback(1, "comment");

        // Create ReviewFeedback entity
        entity = instance.create(entity, OPERATOR);

        entity.setComment("new comment");
        ReviewFeedbackDetail removedDetailOne = entity.getDetails().remove(0);
        ReviewFeedbackDetail removedDetailTwo = entity.getDetails().remove(1);
        entity.getDetails().get(0).setFeedbackText("new feedback");
        entity.getDetails().add(this.createReviewFeedbackDetail(4, 4, "feedback 4"));
        instance.update(entity, MODIFIER);

        // Check audit record exists for update
        this.checkReviewFeedbackAuditExists(entity.getId(), entity.getProjectId(),
                entity.getComment(), UPDATE_AUDIT_TYPE, MODIFIER, entity.getModifyDate());

        // Check detail audit records exist
        this.checkReviewFeedbackDetailAuditExists(entity.getId(), entity.getDetails().get(0).getReviewerUserId(),
                entity.getDetails().get(0).getScore(), entity.getDetails().get(0).getFeedbackText(),
                UPDATE_AUDIT_TYPE, MODIFIER, entity.getModifyDate()); // Audit record for update
        this.checkReviewFeedbackDetailAuditExists(entity.getId(), entity.getDetails().get(1).getReviewerUserId(),
                entity.getDetails().get(1).getScore(), entity.getDetails().get(1).getFeedbackText(),
                CREATE_AUDIT_TYPE, MODIFIER, entity.getModifyDate()); // Audit record for create
        this.checkReviewFeedbackDetailAuditExists(entity.getId(), removedDetailOne.getReviewerUserId(),
                null, null, DELETE_AUDIT_TYPE, MODIFIER, entity.getModifyDate()); // Audit record for delete
        this.checkReviewFeedbackDetailAuditExists(entity.getId(), removedDetailTwo.getReviewerUserId(),
                null, null, DELETE_AUDIT_TYPE, MODIFIER, entity.getModifyDate()); // Audit record for delete
    }

    /**
     * <p>
     * Checks the review feedback audit record exists.
     * </p>
     *
     * @param reviewFeedbackId The review feedback id
     * @param projectId The project id
     * @param comment The comment
     * @param auditActionId The audit action type id
     * @param actionUser The action user
     * @param actionDate The action date
     *
     * @throws Exception to JUnit.
     */
    private void checkReviewFeedbackAuditExists(long reviewFeedbackId, long projectId, String comment,
            long auditActionId, String actionUser, Date actionDate) throws Exception {

        Connection conn = factory.createConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select 1 from 'informix'.review_feedback_audit"
                + " where review_feedback_id=? and project_id=? and comment=? and audit_action_type_id=?"
                + " and action_user=? and action_date=?");
            try {
                statement.setLong(1, reviewFeedbackId);
                statement.setLong(2, projectId);
                statement.setString(3, comment);
                statement.setLong(4, auditActionId);
                statement.setString(5, actionUser);
                statement.setTimestamp(6, new Timestamp(actionDate.getTime()));
                Assert.assertTrue("The review feedback audit record should exist", statement.executeQuery().next());
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    /**
     * <p>
     * Checks the review feedback detail audit record exists.
     * </p>
     *
     * @param reviewFeedbackId The review feedback id
     * @param reviewerUserId The reviewer user id
     * @param score The score
     * @param feedback The feedback text
     * @param auditActionId The audit action type id
     * @param actionUser The action user
     * @param actionDate The action date
     *
     * @throws Exception to JUnit.
     */
    private void checkReviewFeedbackDetailAuditExists(long reviewFeedbackId, long reviewerUserId, Integer score,
            String feedback, long auditActionId, String actionUser, Date actionDate) throws Exception {

        Connection conn = factory.createConnection();
        try {
            PreparedStatement statement = null;
            try {

                if (auditActionId == DELETE_AUDIT_TYPE) {

                    statement = conn.prepareStatement(
                        "select 1 from 'informix'.review_feedback_detail_audit"
                        + " where review_feedback_id=? and reviewer_user_id=?"
                        + " and audit_action_type_id=? and action_user=? and action_date=?");

                    statement.setLong(1, reviewFeedbackId);
                    statement.setLong(2, reviewerUserId);
                    statement.setLong(3, auditActionId);
                    statement.setString(4, actionUser);
                    statement.setTimestamp(5, new Timestamp(actionDate.getTime()));
                    Assert.assertTrue("The review feedback detail audit record should exist",
                            statement.executeQuery().next());
                } else {
                    statement = conn.prepareStatement(
                        "select 1 from 'informix'.review_feedback_detail_audit"
                        + " where review_feedback_id=? and reviewer_user_id=? and score=? and feedback_text=?"
                        + " and audit_action_type_id=? and action_user=? and action_date=?");
                    statement.setLong(1, reviewFeedbackId);
                    statement.setLong(2, reviewerUserId);
                    statement.setInt(3, score);
                    statement.setString(4, feedback);
                    statement.setLong(5, auditActionId);
                    statement.setString(6, actionUser);
                    statement.setTimestamp(7, new Timestamp(actionDate.getTime()));
                    Assert.assertTrue("The review feedback detail audit record should exist",
                            statement.executeQuery().next());
                }
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    /**
     * <p>
     * Accuracy test for JDBCReviewFeedbackManager.
     * The ReviewFeedback entity should be create/updated/retrieved/deleted correctly.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testReviewFeedback_CURD() throws Exception {
        ReviewFeedback entity = this.createReviewFeedback(1, "comment");

        // Create ReviewFeedback entity
        entity = instance.create(entity, OPERATOR);

        // The generated entity id
        long id = entity.getId();

        // Retrieve the created ReviewFeedback by id
        Assert.assertNull("Null should be returned", instance.get(Long.MAX_VALUE));

        ReviewFeedback created = instance.get(id);

        this.assertTwoReviewFeedbackEquals(entity, created);

        // Update ReviewFeedback
        created.setComment("new comment");
        created.getDetails().remove(1);
        created.getDetails().get(0).setFeedbackText("new feedback");
        created.getDetails().add(this.createReviewFeedbackDetail(4, 4, "feedback 4"));
        ReviewFeedback updated = instance.update(created, MODIFIER);

        this.assertTwoReviewFeedbackEquals(updated, instance.get(id));

        // Create another ReviewFeedback for same project 1
        ReviewFeedback another = instance.create(this.createReviewFeedback(1, "comment"), OPERATOR);

        // Retrieve ReviewFeedback by project id
        Assert.assertTrue("There should be no ReviewFeedback", instance.getForProject(Long.MAX_VALUE).isEmpty());
        List<ReviewFeedback> list = instance.getForProject(updated.getProjectId());
        Assert.assertEquals("There should be 2 ReviewFeedback", 2, list.size());

        // Sort ReviewFeedback list by id
        Comparator<ReviewFeedback> comparator = new Comparator<ReviewFeedback>() {
            public int compare(ReviewFeedback o1, ReviewFeedback o2) {
                return (int) (o1.getId() - o2.getId());
            }
        };
        Collections.sort(list, comparator);
        this.assertTwoReviewFeedbackEquals(updated, list.get(0));
        this.assertTwoReviewFeedbackEquals(another, list.get(1));

        // Delete ReviewFeedback, true should be returned
        Assert.assertTrue("ReviewFeedback should be deleted", instance.delete(id));

        // Delete ReviewFeedback again, false should be returned
        Assert.assertFalse("ReviewFeedback should have already been deleted", instance.delete(id));
    }

    /**
     * <p>
     * Asserts two ReviewFeedback equal.
     * </p>
     *
     * @param one ReviewFeedback
     * @param two ReviewFeedback
     */
    private void assertTwoReviewFeedbackEquals(ReviewFeedback one, ReviewFeedback two) {
        Assert.assertEquals("Id should equal", one.getId(), two.getId());
        Assert.assertEquals("Comment should equal", one.getComment(), two.getComment());
        Assert.assertEquals("Project Id should equal", one.getProjectId(), two.getProjectId());
        Assert.assertEquals("Create user should equal", one.getCreateUser(), two.getCreateUser());
        Assert.assertEquals("Create date should equal", one.getCreateDate(), two.getCreateDate());
        Assert.assertEquals("Modify user should equal", one.getModifyUser(), two.getModifyUser());
        Assert.assertEquals("Modify date should equal", one.getModifyDate(), two.getModifyDate());

        // Sort ReviewFeedbackDetail list by reviewer user id
        Comparator<ReviewFeedbackDetail> comparator = new Comparator<ReviewFeedbackDetail>() {
            public int compare(ReviewFeedbackDetail o1, ReviewFeedbackDetail o2) {
                return (int) (o1.getReviewerUserId() - o2.getReviewerUserId());
            }
        };
        Collections.sort(one.getDetails(), comparator);
        Collections.sort(two.getDetails(), comparator);

        // Check ReviewFeedbackDetail list
        Assert.assertEquals("Details size should equal", one.getDetails().size(), two.getDetails().size());
        for (int i = 0; i < one.getDetails().size(); i++) {
            this.assertTwoReviewFeedbackDetailEquals(one.getDetails().get(i), two.getDetails().get(i));
        }
    }


    /**
     * <p>
     * Asserts two ReviewFeedbackDetail equal.
     * </p>
     *
     * @param one ReviewFeedbackDetail
     * @param two ReviewFeedbackDetail
     */
    private void assertTwoReviewFeedbackDetailEquals(ReviewFeedbackDetail one, ReviewFeedbackDetail two) {
        Assert.assertEquals("Score should equal", one.getScore(), two.getScore());
        Assert.assertEquals("Feedback text should equal", one.getFeedbackText(), two.getFeedbackText());
        Assert.assertEquals("Reviewer user id should equal", one.getReviewerUserId(), two.getReviewerUserId());
    }

    /**
     * <p>
     * Creates an instance of <code>ReviewFeedback</code>.
     * </p>
     *
     * @param projectId The project id
     * @param comment The comment
     *
     * @return instance of <code>ReviewFeedback</code>.
     */
    private ReviewFeedback createReviewFeedback(long projectId, String comment) {
        ReviewFeedback reviewFeedback = new ReviewFeedback();
        reviewFeedback.setProjectId(projectId);
        reviewFeedback.setComment(comment);
        List<ReviewFeedbackDetail> details = new ArrayList<ReviewFeedbackDetail>();
        details.add(this.createReviewFeedbackDetail(1, 1, "feedback 1"));
        details.add(this.createReviewFeedbackDetail(2, 2, "feedback 2"));
        details.add(this.createReviewFeedbackDetail(3, 3, "feedback 3"));
        reviewFeedback.setDetails(details);
        return reviewFeedback;
    }

    /**
     * <p>
     * Creates an instance of <code>ReviewFeedbackDetail</code>.
     * </p>
     *
     * @param reviewerUserId The reviewer's user id
     * @param score The score
     * @param feedback The feedback
     *
     * @return instance of <code>ReviewFeedbackDetail</code>.
     */
    private ReviewFeedbackDetail createReviewFeedbackDetail(long reviewerUserId, int score, String feedback) {
        ReviewFeedbackDetail detail = new ReviewFeedbackDetail();
        detail.setReviewerUserId(reviewerUserId);
        detail.setScore(score);
        detail.setFeedbackText(feedback);
        return detail;
    }
}
