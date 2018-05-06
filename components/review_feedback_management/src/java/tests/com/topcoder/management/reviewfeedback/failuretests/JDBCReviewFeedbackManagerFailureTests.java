/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback.failuretests;

import java.io.File;
import java.util.ArrayList;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.reviewfeedback.ReviewFeedback;
import com.topcoder.management.reviewfeedback.ReviewFeedbackDetail;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManagementConfigurationException;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManagementPersistenceException;
import com.topcoder.management.reviewfeedback.impl.JDBCReviewFeedbackManager;
import com.topcoder.util.log.Log;

/**
 * <p>
 * Failure unit tests for <code>JDBCReviewFeedbackManager</code> class.
 * </p>
 *
 * @author hesibo, BlackMagic
 * @version 1.0
 */
public class JDBCReviewFeedbackManagerFailureTests {
    /**
     * <p>
     * Represents the JDBCReviewFeedbackManager instance for testing.
     * </p>
     */
    private JDBCReviewFeedbackManager instance;

    /**
     * <p>
     * Represents the ConfigurationObject instance for testing.
     * </p>
     */
    private ConfigurationObject config;

    /**
     * <p>
     * Represents the ReviewFeedback instance for testing.
     * </p>
     */
    private ReviewFeedback entity;
    /**
     * <p>
     * Represents the ReviewFeedbackDetail instance for testing.
     * </p>
     */
    private ReviewFeedbackDetail detail;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(JDBCReviewFeedbackManagerFailureTests.class);
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
        config = persistence.loadFile("com.topcoder.management.reviewfeedback",
                new File("test_files/failure/config.xml"));
        instance = new JDBCReviewFeedbackManager(config);
        entity = new ReviewFeedback();
        entity.setProjectId(1);
        entity.setComment("comment");
        entity.setDetails(new ArrayList<ReviewFeedbackDetail>());

        detail = new ReviewFeedbackDetail();
        detail.setReviewerUserId(1);
        detail.setScore(100);
        detail.setFeedbackText("text");
        entity.getDetails().add(detail);
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
        instance = null;
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#JDBCReviewFeedbackManager(String, String)}.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2Failure1() {
        new JDBCReviewFeedbackManager(" ", "test");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#JDBCReviewFeedbackManager(String, String)}.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2Failure2() {
        new JDBCReviewFeedbackManager("test", "\t");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#JDBCReviewFeedbackManager(String, String)}.
     * </p>
     */
    @Test(expected = ReviewFeedbackManagementConfigurationException.class)
    public void testCtor2Failure3() {
        new JDBCReviewFeedbackManager(null, "wrongNameSpace");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#JDBCReviewFeedbackManager(ConfigurationObject)}.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor3Failure1() {
        new JDBCReviewFeedbackManager(null);
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#JDBCReviewFeedbackManager(ConfigurationObject)}.
     * </p>
     */
    @Test(expected = ReviewFeedbackManagementConfigurationException.class)
    public void testCtor3Failure2() {
        new JDBCReviewFeedbackManager(new DefaultConfigurationObject("test"));
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#JDBCReviewFeedbackManager(ConfigurationObject)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = ReviewFeedbackManagementConfigurationException.class)
    public void testCtor3Failure3() throws Exception {
        config.getChild("com.topcoder.management.reviewfeedback").removeProperty("dbConnectionName");
        new JDBCReviewFeedbackManager(config);
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#JDBCReviewFeedbackManager(ConfigurationObject)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = ReviewFeedbackManagementConfigurationException.class)
    public void testCtor3Failure4() throws Exception {
        config.getChild("com.topcoder.management.reviewfeedback").setPropertyValue("dbConnectionName", "    ");
        new JDBCReviewFeedbackManager(config);
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#JDBCReviewFeedbackManager(ConfigurationObject)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = ReviewFeedbackManagementConfigurationException.class)
    public void testCtor3Failure5() throws Exception {
        config.removeChild("dbConnectionFactoryConfiguration");
        new JDBCReviewFeedbackManager(config);
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#JDBCReviewFeedbackManager(ConfigurationObject)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = ReviewFeedbackManagementConfigurationException.class)
    public void testCtor3Failure6() throws Exception {
        config.getChild("com.topcoder.management.reviewfeedback")
            .setPropertyValue("createAuditActionTypeId", "xxx");
        new JDBCReviewFeedbackManager(config);
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#JDBCReviewFeedbackManager(ConfigurationObject)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = ReviewFeedbackManagementConfigurationException.class)
    public void testCtor3Failure7() throws Exception {
        config.getChild("com.topcoder.management.reviewfeedback")
            .setPropertyValue("updateAuditActionTypeId", "xxx");
        new JDBCReviewFeedbackManager(config);
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#JDBCReviewFeedbackManager(ConfigurationObject)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = ReviewFeedbackManagementConfigurationException.class)
    public void testCtor3Failure8() throws Exception {
        config.getChild("com.topcoder.management.reviewfeedback")
            .setPropertyValue("deleteAuditActionTypeId", "xxx");
        new JDBCReviewFeedbackManager(config);
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#JDBCReviewFeedbackManager(DBConnectionFactory,
     * String, Log, Long, Long, Long)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor4Failure1() throws Exception {
        new JDBCReviewFeedbackManager(null, null, null, null, null, null);
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#JDBCReviewFeedbackManager(DBConnectionFactory,
     * String, Log, Long, Long, Long)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor4Failure2() throws Exception {
        new JDBCReviewFeedbackManager(new DBConnectionFactoryImpl(), null, null, null, null, null);
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#JDBCReviewFeedbackManager(DBConnectionFactory,
     * String, Log, Long, Long, Long)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor4Failure3() throws Exception {
        new JDBCReviewFeedbackManager(new DBConnectionFactoryImpl(), "    ", null, null, null, null);
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#create(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = ReviewFeedbackManagementPersistenceException.class)
    public void testCreateFailure1() throws Exception {
        instance.create(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#create(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFailure2() throws Exception {
        instance.create(null, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#create(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFailure3() throws Exception {
        detail.setReviewerUserId(12345678901L);
        instance.create(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#create(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFailure4() throws Exception {
        entity.setComment("\t ");
        instance.create(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#create(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFailure5() throws Exception {
        entity.setDetails(null);
        instance.create(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#create(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFailure6() throws Exception {
        entity.getDetails().add(null);
        instance.create(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#create(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFailure7() throws Exception {
        entity.getDetails().add(detail);
        instance.create(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#create(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFailure8() throws Exception {
        detail.setFeedbackText(" ");
        instance.create(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#create(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFailure9() throws Exception {
        detail.setFeedbackText(null);
        instance.create(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#create(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFailure10() throws Exception {
        instance.create(entity, null);
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#create(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFailure11() throws Exception {
        instance.create(entity, "\n\t ");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#get(long)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = ReviewFeedbackManagementPersistenceException.class)
    public void testGetFailure1() throws Exception {
        instance.get(1);
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#delete(long)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = ReviewFeedbackManagementPersistenceException.class)
    public void testDeleteFailure1() throws Exception {
        instance.delete(1);
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#update(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = ReviewFeedbackManagementPersistenceException.class)
    public void testUpdateFailure1() throws Exception {
        instance.update(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#update(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFailure2() throws Exception {
        instance.update(null, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#update(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFailure3() throws Exception {
        detail.setReviewerUserId(12345678901L);
        instance.update(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#update(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFailure4() throws Exception {
        entity.setComment("\t ");
        instance.update(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#update(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFailure5() throws Exception {
        entity.setDetails(null);
        instance.update(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#update(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFailure6() throws Exception {
        entity.getDetails().add(null);
        instance.update(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#update(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFailure7() throws Exception {
        entity.getDetails().add(detail);
        instance.update(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#update(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFailure8() throws Exception {
        detail.setFeedbackText(" ");
        instance.update(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#update(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFailure9() throws Exception {
        detail.setFeedbackText(null);
        instance.update(entity, "op");
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#update(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFailure10() throws Exception {
        instance.update(entity, null);
    }

    /**
     * <p>
     * Failure test for {@link JDBCReviewFeedbackManager#update(ReviewFeedback)}.
     * </p>
     *
     * @throws Exception to JUnit
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFailure11() throws Exception {
        instance.update(entity, "\n\t ");
    }
}
