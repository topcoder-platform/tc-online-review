/*
 * Copyright (C) 2012, 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.reviewfeedback.BaseUnitTest;
import com.topcoder.management.reviewfeedback.ReviewFeedback;
import com.topcoder.management.reviewfeedback.ReviewFeedbackDetail;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManagementConfigurationException;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManagementEntityNotFoundException;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManagementPersistenceException;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * Unit tests for {@link JDBCReviewFeedbackManager} class.
 * </p>
 *
 * <p>
 * <em>Changes in 2.0:</em>
 * <ol>
 * <li>Updated/Added test cases to reflect the changes.</li>
 * </ol>
 * </p>
 *
 * @author amazingpig, hesibo, sparemax
 * @version 2.0
 */
public class JDBCReviewFeedbackManagerUnitTests extends BaseUnitTest {
    /**
     * <p>
     * Represents the <code>JDBCReviewFeedbackManager</code> instance used in tests.
     * </p>
     */
    private JDBCReviewFeedbackManager instance;

    /**
     * <p>
     * Represents the configuration object used in tests.
     * </p>
     */
    private ConfigurationObject config;

    /**
     * <p>
     * Represents the review feedback used in tests.
     * </p>
     *
     * @since 2.0
     */
    private ReviewFeedback reviewFeedback;

    /**
     * <p>
     * Represents the review feedback detail used in tests.
     * </p>
     *
     * @since 2.0
     */
    private ReviewFeedbackDetail reviewFeedbackDetail1;

    /**
     * <p>
     * Represents the review feedback detail used in tests.
     * </p>
     *
     * @since 2.0
     */
    private ReviewFeedbackDetail reviewFeedbackDetail2;

    /**
     * <p>
     * Represents the review feedback detail used in tests.
     * </p>
     *
     * @since 2.0
     */
    private ReviewFeedbackDetail reviewFeedbackDetail3;

    /**
     * <p>
     * Represents the operator used in tests.
     * </p>
     *
     * @since 2.0
     */
    private String operator = "operator";

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(JDBCReviewFeedbackManagerUnitTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated to initialize the new fields.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        config = getConfig();
        instance = new JDBCReviewFeedbackManager();

        reviewFeedbackDetail1 = new ReviewFeedbackDetail();
        reviewFeedbackDetail1.setReviewerUserId(126);
        reviewFeedbackDetail1.setScore(2);
        reviewFeedbackDetail1.setFeedbackText("feedback text 1");

        reviewFeedbackDetail2 = new ReviewFeedbackDetail();
        reviewFeedbackDetail2.setReviewerUserId(127);
        reviewFeedbackDetail2.setScore(3);
        reviewFeedbackDetail2.setFeedbackText("feedback text 2");

        reviewFeedbackDetail3 = new ReviewFeedbackDetail();
        reviewFeedbackDetail3.setReviewerUserId(128);
        reviewFeedbackDetail3.setScore(3);
        reviewFeedbackDetail3.setFeedbackText("feedback text 2");

        reviewFeedback = new ReviewFeedback();
        reviewFeedback.setProjectId(1);
        reviewFeedback.setComment("comment text");
        List<ReviewFeedbackDetail> details = new ArrayList<ReviewFeedbackDetail>();
        details.add(reviewFeedbackDetail1);
        details.add(reviewFeedbackDetail2);
        reviewFeedback.setDetails(details);
    }

    /**
     * <p>
     * Cleans up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Override
    @After
    public void tearDown() throws Exception {
        instance = null;
        super.tearDown();
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>JDBCReviewFeedbackManager()</code>.<br>
     * Instance should be correctly created.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Asserted new fields createAuditActionTypeId, updateAuditActionTypeId and deleteAuditActionTypeId.</li>
     * </ol>
     * </p>
     */
    public void testCtor() {
        instance = new JDBCReviewFeedbackManager();

        assertNotNull("'dbConnectionFactory' should be correct.", getField(instance, "dbConnectionFactory"));
        assertEquals("'dbConnectionName' should be correct.", "TCSCatalog", getField(instance, "dbConnectionName"));
        assertNotNull("'log' should be correct.", getField(instance, "log"));

        assertEquals("'createAuditActionTypeId' should be correct.", 1L, getField(instance, "createAuditActionTypeId"));
        assertEquals("'updateAuditActionTypeId' should be correct.", 3L, getField(instance, "updateAuditActionTypeId"));
        assertEquals("'deleteAuditActionTypeId' should be correct.", 2L, getField(instance, "deleteAuditActionTypeId"));
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>JDBCReviewFeedbackManager(String, String)</code>.<br>
     * Instance should be correctly created.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Asserted new fields createAuditActionTypeId, updateAuditActionTypeId and deleteAuditActionTypeId.</li>
     * </ol>
     * </p>
     */
    public void testCtor2() {
        instance = new JDBCReviewFeedbackManager(null, null);

        assertNotNull("'dbConnectionFactory' should be correct.", getField(instance, "dbConnectionFactory"));
        assertEquals("'dbConnectionName' should be correct.", "TCSCatalog", getField(instance, "dbConnectionName"));
        assertNotNull("'log' should be correct.", getField(instance, "log"));

        assertEquals("'createAuditActionTypeId' should be correct.", 1L, getField(instance, "createAuditActionTypeId"));
        assertEquals("'updateAuditActionTypeId' should be correct.", 3L, getField(instance, "updateAuditActionTypeId"));
        assertEquals("'deleteAuditActionTypeId' should be correct.", 2L, getField(instance, "deleteAuditActionTypeId"));
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>JDBCReviewFeedbackManager(String, String)</code>.<br>
     * Instance should be correctly created.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Asserted new fields createAuditActionTypeId, updateAuditActionTypeId and deleteAuditActionTypeId.</li>
     * </ol>
     * </p>
     */
    public void testCtor3() {
        instance = new JDBCReviewFeedbackManager(
            "com/topcoder/configuration/persistence/ConfigurationFileManager.properties",
            "com.topcoder.management.reviewfeedback");

        assertNotNull("'dbConnectionFactory' should be correct.", getField(instance, "dbConnectionFactory"));
        assertEquals("'dbConnectionName' should be correct.", "TCSCatalog", getField(instance, "dbConnectionName"));
        assertNotNull("'log' should be correct.", getField(instance, "log"));

        assertEquals("'createAuditActionTypeId' should be correct.", 1L, getField(instance, "createAuditActionTypeId"));
        assertEquals("'updateAuditActionTypeId' should be correct.", 3L, getField(instance, "updateAuditActionTypeId"));
        assertEquals("'deleteAuditActionTypeId' should be correct.", 2L, getField(instance, "deleteAuditActionTypeId"));
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(String, String)</code>. <br>
     * The configurationFilename is empty. <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor31() throws Exception {
        try {
            new JDBCReviewFeedbackManager(" ", "com.topcoder.management.reviewfeedback");
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(String, String)</code>. <br>
     * The configurationNamespace is empty. <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor32() throws Exception {
        try {
            new JDBCReviewFeedbackManager("com/topcoder/configuration/persistence/ConfigurationFileManager.properties",
                " ");
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(String, String)</code>. <br>
     * The configurationFilename doesn't exist. <code>ReviewFeedbackManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor33() throws Exception {
        try {
            new JDBCReviewFeedbackManager("not exist", "com.topcoder.management.reviewfeedback");
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(String, String)</code>. <br>
     * The configurationNamespace doesn't exist. <code>ReviewFeedbackManagementConfigurationException</code> is
     * expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor34() throws Exception {
        try {
            new JDBCReviewFeedbackManager("com/topcoder/configuration/persistence/ConfigurationFileManager.properties",
                "not exist");
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(String, String)</code>. <br>
     * The type of the configuration file is not recognized. <code>ReviewFeedbackManagementConfigurationException</code>
     * is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor35() throws Exception {
        try {
            new JDBCReviewFeedbackManager("sqls/clear.sql", "com.topcoder.management.reviewfeedback");
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(String, String)</code>. <br>
     * The configuration file is invalid. <code>ReviewFeedbackManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor36() throws Exception {
        try {
            new JDBCReviewFeedbackManager("wrongconfigs/ConfigurationFileManager.properties", "badconfigfile");
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>JDBCReviewFeedbackManager(ConfigurationObject)</code>.<br>
     * Instance should be correctly created.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Asserted new fields createAuditActionTypeId, updateAuditActionTypeId and deleteAuditActionTypeId.</li>
     * </ol>
     * </p>
     */
    public void testCtor4() {
        instance = new JDBCReviewFeedbackManager(config);

        assertNotNull("'dbConnectionFactory' should be correct.", getField(instance, "dbConnectionFactory"));
        assertEquals("'dbConnectionName' should be correct.", "TCSCatalog", getField(instance, "dbConnectionName"));
        assertNotNull("'log' should be correct.", getField(instance, "log"));

        assertEquals("'createAuditActionTypeId' should be correct.", 1L, getField(instance, "createAuditActionTypeId"));
        assertEquals("'updateAuditActionTypeId' should be correct.", 3L, getField(instance, "updateAuditActionTypeId"));
        assertEquals("'deleteAuditActionTypeId' should be correct.", 2L, getField(instance, "deleteAuditActionTypeId"));
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>JDBCReviewFeedbackManager(ConfigurationObject)</code>.<br>
     * Instance should be correctly created.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void testCtor4_New() throws Exception {
        ConfigurationObject child = config.getChild("com.topcoder.management.reviewfeedback");
        child.setPropertyValue("createAuditActionTypeId", 4L);
        child.setPropertyValue("updateAuditActionTypeId", 5L);
        child.setPropertyValue("deleteAuditActionTypeId", 6L);

        instance = new JDBCReviewFeedbackManager(config);

        assertNotNull("'dbConnectionFactory' should be correct.", getField(instance, "dbConnectionFactory"));
        assertEquals("'dbConnectionName' should be correct.", "TCSCatalog", getField(instance, "dbConnectionName"));
        assertNotNull("'log' should be correct.", getField(instance, "log"));

        assertEquals("'createAuditActionTypeId' should be correct.", 4L, getField(instance, "createAuditActionTypeId"));
        assertEquals("'updateAuditActionTypeId' should be correct.", 5L, getField(instance, "updateAuditActionTypeId"));
        assertEquals("'deleteAuditActionTypeId' should be correct.", 6L, getField(instance, "deleteAuditActionTypeId"));
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(ConfigurationObject)</code>. <br>
     * The input is null. <code>IllegalArgumentException </code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor41() throws Exception {
        try {
            new JDBCReviewFeedbackManager(null);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(ConfigurationObject)</code>. <br>
     * The dbConnectionName is null. <code>ReviewFeedbackManagementConfigurationException </code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor42() throws Exception {
        config.getChild("com.topcoder.management.reviewfeedback").setPropertyValue("dbConnectionName", null);
        try {
            new JDBCReviewFeedbackManager(config);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(ConfigurationObject)</code>. <br>
     * The dbConnectionName is empty. <code>ReviewFeedbackManagementConfigurationException </code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor43() throws Exception {
        config.getChild("com.topcoder.management.reviewfeedback").setPropertyValue("dbConnectionName", " ");
        try {
            new JDBCReviewFeedbackManager(config);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(ConfigurationObject)</code>. <br>
     * The dbConnectionName is not an String. <code>ReviewFeedbackManagementConfigurationException </code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor44() throws Exception {
        config.getChild("com.topcoder.management.reviewfeedback").setPropertyValue("dbConnectionName", new Date());
        try {
            new JDBCReviewFeedbackManager(config);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(ConfigurationObject)</code>. <br>
     * The logName is not an String. <code>ReviewFeedbackManagementConfigurationException </code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor45() throws Exception {
        config.getChild("com.topcoder.management.reviewfeedback").setPropertyValue("logName", new Date());
        try {
            new JDBCReviewFeedbackManager(config);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(ConfigurationObject)</code>. <br>
     * The dbConfig is null. <code>ReviewFeedbackManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor46() throws Exception {
        config.removeChild("dbConnectionFactoryConfiguration");
        try {
            new JDBCReviewFeedbackManager(config);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(ConfigurationObject)</code>. <br>
     * The dbConnectionName is unknown. <code>ReviewFeedbackManagementConfigurationException </code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor47() throws Exception {
        config.getChild("dbConnectionFactoryConfiguration")
            .getChild("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl").getChild("connections")
            .setPropertyValue("default", "unknown");
        try {
            new JDBCReviewFeedbackManager(config);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(ConfigurationObject)</code>. <br>
     * The dbConnectionFactoryConfiguration is invalid. <code>ReviewFeedbackManagementConfigurationException </code> is
     * expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor48() throws Exception {
        config.getChild("dbConnectionFactoryConfiguration").clearChildren();
        try {
            new JDBCReviewFeedbackManager(config);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(ConfigurationObject)</code>. <br>
     * The JDBCReviewFeedbackManager configuration is null. <code>ReviewFeedbackManagementConfigurationException</code>
     * is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor49() throws Exception {
        config.removeChild("com.topcoder.management.reviewfeedback");
        try {
            new JDBCReviewFeedbackManager(config);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(ConfigurationObject)</code>. <br>
     * The createAuditActionTypeId is invalid. <code>ReviewFeedbackManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void testCtor410() throws Exception {
        config.getChild("com.topcoder.management.reviewfeedback").setPropertyValue("createAuditActionTypeId", "abc");
        try {
            new JDBCReviewFeedbackManager(config);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(ConfigurationObject)</code>. <br>
     * The updateAuditActionTypeId is invalid. <code>ReviewFeedbackManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void testCtor411() throws Exception {
        config.getChild("com.topcoder.management.reviewfeedback").setPropertyValue("updateAuditActionTypeId", "abc");
        try {
            new JDBCReviewFeedbackManager(config);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(ConfigurationObject)</code>. <br>
     * The deleteAuditActionTypeId is invalid. <code>ReviewFeedbackManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void testCtor412() throws Exception {
        config.getChild("com.topcoder.management.reviewfeedback").setPropertyValue("deleteAuditActionTypeId", "abc");
        try {
            new JDBCReviewFeedbackManager(config);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>JDBCReviewFeedbackManager(DBConnectionFactory, String, Log,
     * Long, Long, Long)</code>.<br>
     * Instance should be correctly created.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Asserted new fields createAuditActionTypeId, updateAuditActionTypeId and deleteAuditActionTypeId.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor5() throws Exception {
        ConfigurationObject dbConfig = config.getChild("dbConnectionFactoryConfiguration");
        DBConnectionFactory factory = new DBConnectionFactoryImpl(dbConfig);
        instance = new JDBCReviewFeedbackManager(factory, "TCSCatalog", LogManager.getLog(getClass().getName()), null,
            null, null);

        assertEquals("'dbConnectionFactory' should be correct.", factory, getField(instance, "dbConnectionFactory"));
        assertEquals("'dbConnectionName' should be correct.", "TCSCatalog", getField(instance, "dbConnectionName"));
        assertNotNull("'log' should be correct.", getField(instance, "log"));

        assertEquals("'createAuditActionTypeId' should be correct.", 1L, getField(instance, "createAuditActionTypeId"));
        assertEquals("'updateAuditActionTypeId' should be correct.", 3L, getField(instance, "updateAuditActionTypeId"));
        assertEquals("'deleteAuditActionTypeId' should be correct.", 2L, getField(instance, "deleteAuditActionTypeId"));
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>JDBCReviewFeedbackManager(DBConnectionFactory, String, Log,
     * Long, Long, Long)</code>.<br>
     * Instance should be correctly created.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void testCtor5_New() throws Exception {
        ConfigurationObject dbConfig = config.getChild("dbConnectionFactoryConfiguration");
        DBConnectionFactory factory = new DBConnectionFactoryImpl(dbConfig);
        instance = new JDBCReviewFeedbackManager(factory, "TCSCatalog", LogManager.getLog(getClass().getName()), 4L,
            5L, 6L);

        assertEquals("'dbConnectionFactory' should be correct.", factory, getField(instance, "dbConnectionFactory"));
        assertEquals("'dbConnectionName' should be correct.", "TCSCatalog", getField(instance, "dbConnectionName"));
        assertNotNull("'log' should be correct.", getField(instance, "log"));

        assertEquals("'createAuditActionTypeId' should be correct.", 4L, getField(instance, "createAuditActionTypeId"));
        assertEquals("'updateAuditActionTypeId' should be correct.", 5L, getField(instance, "updateAuditActionTypeId"));
        assertEquals("'deleteAuditActionTypeId' should be correct.", 6L, getField(instance, "deleteAuditActionTypeId"));
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(DBConnectionFactory, String, Log)</code>. <br>
     * The DBConnectionFactory is null. <code>IllegalArgumentException </code> is expected.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Added createAuditActionTypeId, updateAuditActionTypeId and deleteAuditActionTypeId parameters</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor51() throws Exception {
        try {
            new JDBCReviewFeedbackManager(null, "TCSCatalog", LogManager.getLog(getClass().getName()),
                null, null, null);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(DBConnectionFactory, String, Log)</code>. <br>
     * The dbConnectionName is null. <code>IllegalArgumentException </code> is expected.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Added createAuditActionTypeId, updateAuditActionTypeId and deleteAuditActionTypeId parameters</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor52() throws Exception {
        ConfigurationObject dbConfig = config.getChild("dbConnectionFactoryConfiguration");
        DBConnectionFactory factory = new DBConnectionFactoryImpl(dbConfig);
        try {
            new JDBCReviewFeedbackManager(factory, null, LogManager.getLog(getClass().getName()), null, null, null);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>JDBCReviewFeedbackManager(DBConnectionFactory, String, Log)</code>. <br>
     * The dbConnectionName is empty. <code>IllegalArgumentException </code> is expected.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Added createAuditActionTypeId, updateAuditActionTypeId and deleteAuditActionTypeId parameters</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testCtor53() throws Exception {
        ConfigurationObject dbConfig = config.getChild("dbConnectionFactoryConfiguration");
        DBConnectionFactory factory = new DBConnectionFactoryImpl(dbConfig);
        try {
            new JDBCReviewFeedbackManager(factory, "  ", LogManager.getLog(getClass().getName()), null, null, null);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test for the method <code>create</code>.<br>
     * The entity should be correctly persisted.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated to reflect the data model changes.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void test_create_1() throws Exception {
        // Create.
        ReviewFeedback res = instance.create(reviewFeedback, operator);

        assertTrue("The entity should have auto-generated id", res.getId() > 0);
        ReviewFeedback retrievedEntity = instance.get(res.getId());
        assertEquals("The field should be correct", reviewFeedback.getProjectId(), retrievedEntity.getProjectId());
        assertEquals("The field should be correct", reviewFeedback.getComment(), retrievedEntity.getComment());
        assertEquals("The field should be correct", operator, retrievedEntity.getCreateUser());
        assertNotNull("The field should be correct", retrievedEntity.getCreateDate());
        assertEquals("The field should be correct", operator, retrievedEntity.getModifyUser());
        assertNotNull("The field should be correct", retrievedEntity.getModifyDate());

        List<ReviewFeedbackDetail> details = res.getDetails();
        List<ReviewFeedbackDetail> retrievedDetails = retrievedEntity.getDetails();
        assertEquals("The field should be correct", 2, retrievedDetails.size());
        ReviewFeedbackDetail detail1 = details.get(0);
        ReviewFeedbackDetail retrievedDetail1 = retrievedDetails.get(0);
        ReviewFeedbackDetail detail2 = details.get(1);
        ReviewFeedbackDetail retrievedDetail2 = retrievedDetails.get(1);
        if (retrievedDetail1.getScore() > retrievedDetail2.getScore()) {
            retrievedDetail1 = retrievedDetails.get(1);
            retrievedDetail2 = retrievedDetails.get(0);
        }

        assertEquals("The field should be correct", detail1.getFeedbackText(), retrievedDetail1.getFeedbackText());
        assertEquals("The field should be correct", detail1.getReviewerUserId(), retrievedDetail1.getReviewerUserId());
        assertEquals("The field should be correct", detail1.getScore(), retrievedDetail1.getScore());

        assertEquals("The field should be correct", detail2.getFeedbackText(), retrievedDetail2.getFeedbackText());
        assertEquals("The field should be correct", detail2.getReviewerUserId(), retrievedDetail2.getReviewerUserId());
        assertEquals("The field should be correct", detail2.getScore(), retrievedDetail2.getScore());

        List<List<Object>> list = executeQuery(getConnection(), 5, "SELECT review_feedback_id,"
            + " reviewer_user_id, score, feedback_text, audit_action_type_id"
            + " FROM \"informix\".review_feedback_detail_audit ORDER BY review_feedback_id");
        assertEquals("The result should be correct", 2, list.size());
        checkResult("create", list.get(0), res.getId(), retrievedDetail1.getReviewerUserId(),
            retrievedDetail1.getScore(), retrievedDetail1.getFeedbackText(), 1L);
        checkResult("create", list.get(1), res.getId(), retrievedDetail2.getReviewerUserId(),
            retrievedDetail2.getScore(), retrievedDetail2.getFeedbackText(), 1L);

        list = executeQuery(getConnection(), 4, "SELECT review_feedback_id, project_id, comment, audit_action_type_id"
            + " FROM \"informix\".review_feedback_audit ORDER BY review_feedback_id");
        assertEquals("The result should be correct", 1, list.size());
        checkResult("create", list.get(0), res.getId(), res.getProjectId(), res.getComment(), 1L);
    }

    /**
     * <p>
     * Accuracy test for the method <code>create</code>.<br>
     * The entity should be correctly persisted.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_2() throws Exception {
        reviewFeedback.setComment(null);
        reviewFeedback.getDetails().clear();
        // Create.
        ReviewFeedback res = instance.create(reviewFeedback, operator);

        assertTrue("The entity should have auto-generated id", res.getId() > 0);
        ReviewFeedback retrievedEntity = instance.get(res.getId());
        assertEquals("The field should be correct", reviewFeedback.getProjectId(), retrievedEntity.getProjectId());
        assertEquals("The field should be correct", reviewFeedback.getComment(), retrievedEntity.getComment());
        assertEquals("The field should be correct", operator, retrievedEntity.getCreateUser());
        assertNotNull("The field should be correct", retrievedEntity.getCreateDate());
        assertEquals("The field should be correct", operator, retrievedEntity.getModifyUser());
        assertNotNull("The field should be correct", retrievedEntity.getModifyDate());

        assertEquals("The field should be correct", 0, retrievedEntity.getDetails().size());

        List<List<Object>> list = executeQuery(getConnection(), 5, "SELECT review_feedback_id,"
            + " reviewer_user_id, score, feedback_text, audit_action_type_id"
            + " FROM \"informix\".review_feedback_detail_audit");
        assertEquals("The result should be correct", 0, list.size());

        list = executeQuery(getConnection(), 4, "SELECT review_feedback_id, project_id, comment, audit_action_type_id"
            + " FROM \"informix\".review_feedback_audit");
        assertEquals("The result should be correct", 1, list.size());
        checkResult("create", list.get(0), res.getId(), res.getProjectId(), res.getComment(), 1L);
    }

    /**
     * <p>
     * Accuracy test for the method <code>create</code>.<br>
     * The entity should be correctly persisted.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_NoLogging() throws Exception {
        config.getChild("com.topcoder.management.reviewfeedback").removeProperty("logName");
        instance = new JDBCReviewFeedbackManager(config);

        reviewFeedback.setComment(null);
        reviewFeedback.getDetails().clear();
        // Create.
        ReviewFeedback res = instance.create(reviewFeedback, operator);

        assertTrue("The entity should have auto-generated id", res.getId() > 0);
        ReviewFeedback retrievedEntity = instance.get(res.getId());
        assertEquals("The field should be correct", reviewFeedback.getProjectId(), retrievedEntity.getProjectId());
        assertEquals("The field should be correct", reviewFeedback.getComment(), retrievedEntity.getComment());
        assertEquals("The field should be correct", operator, retrievedEntity.getCreateUser());
        assertNotNull("The field should be correct", retrievedEntity.getCreateDate());
        assertEquals("The field should be correct", operator, retrievedEntity.getModifyUser());
        assertNotNull("The field should be correct", retrievedEntity.getModifyDate());

        assertEquals("The field should be correct", 0, retrievedEntity.getDetails().size());

        List<List<Object>> list = executeQuery(getConnection(), 5, "SELECT review_feedback_id,"
            + " reviewer_user_id, score, feedback_text, audit_action_type_id"
            + " FROM \"informix\".review_feedback_detail_audit");
        assertEquals("The result should be correct", 0, list.size());

        list = executeQuery(getConnection(), 4, "SELECT review_feedback_id, project_id, comment, audit_action_type_id"
            + " FROM \"informix\".review_feedback_audit");
        assertEquals("The result should be correct", 1, list.size());
        checkResult("create", list.get(0), res.getId(), res.getProjectId(), res.getComment(), 1L);
    }

    /**
     * <p>
     * Failure test for the method <code>create</code> with operator is null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_operatorNull() throws Exception {
        operator = null;

        try {
            instance.create(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>create</code> with operator is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_operatorEmpty() throws Exception {
        operator = " ";

        try {
            instance.create(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>create</code> with entity is null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_entityNull() throws Exception {
        reviewFeedback = null;

        try {
            instance.create(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>create</code> with entity is null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_entityNull_NoLogging() throws Exception {
        config.getChild("com.topcoder.management.reviewfeedback").removeProperty("logName");
        instance = new JDBCReviewFeedbackManager(config);

        reviewFeedback = null;

        try {
            instance.create(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>create</code> with entity.comment is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_commentEmpty() throws Exception {
        reviewFeedback = null;

        try {
            instance.create(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>create</code> with entity.details is null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_detailsNull() throws Exception {
        reviewFeedback.setDetails(null);

        try {
            instance.create(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>create</code> with entity.details contains null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_detailsContainsNull() throws Exception {
        reviewFeedback.getDetails().add(null);

        try {
            instance.create(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>create</code> with entity.details[i].reviewerUserId is invalid.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_reviewerUserIdInvalid1() throws Exception {
        reviewFeedback.getDetails().get(0).setReviewerUserId(10000000000L);

        try {
            instance.create(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>create</code> with entity.details[i].reviewerUserId is invalid.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_reviewerUserIdInvalid2() throws Exception {
        reviewFeedback.getDetails().get(1).setReviewerUserId(reviewFeedback.getDetails().get(0).getReviewerUserId());

        try {
            instance.create(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>create</code> with entity.details[i].feedbackText is null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_feedbackTextNull() throws Exception {
        reviewFeedback.getDetails().get(0).setFeedbackText(null);

        try {
            instance.create(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>create</code> with entity.details[i].feedbackText is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_feedbackTextEmpty() throws Exception {
        reviewFeedback.getDetails().get(0).setFeedbackText(" ");

        try {
            instance.create(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>create</code> with an error has occurred.<br>
     * <code>ReviewFeedbackManagementPersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_Error1() throws Exception {
        instance = new JDBCReviewFeedbackManager(null, "baddbconfig");

        try {
            instance.create(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementPersistenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>create</code> with an error has occurred.<br>
     * <code>ReviewFeedbackManagementPersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_create_Error3() throws Exception {
        reviewFeedback.setProjectId(Long.MAX_VALUE);

        try {
            instance.create(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementPersistenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test for the method <code>update</code>.<br>
     * The entity should be correctly updated.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated to reflect the data model changes.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void test_update() throws Exception {
        // Create.
        instance.create(reviewFeedback, operator);

        ReviewFeedback entity = instance.get(reviewFeedback.getId());

        entity.setProjectId(2L);
        entity.setComment("new comment");
        List<ReviewFeedbackDetail> details = entity.getDetails();
        details.remove(0);
        ReviewFeedbackDetail detail = details.get(0);
        detail.setScore(99);
        detail.setFeedbackText("new feedback text");
        details.add(reviewFeedbackDetail3);

        instance.update(entity, operator);

        ReviewFeedback retrievedEntity = instance.get(entity.getId());
        checkUpdatedEntity(entity, retrievedEntity);

        List<List<Object>> list = executeQuery(getConnection(), 5, "SELECT review_feedback_id,"
            + " reviewer_user_id, score, feedback_text, audit_action_type_id"
            + " FROM \"informix\".review_feedback_detail_audit ORDER BY review_feedback_id");
        assertEquals("The result should be correct", 5, list.size());
        checkResult("update", list.get(0), entity.getId(), reviewFeedbackDetail1.getReviewerUserId(),
            reviewFeedbackDetail1.getScore(), reviewFeedbackDetail1.getFeedbackText(), 1L);
        checkResult("update", list.get(1), entity.getId(), reviewFeedbackDetail2.getReviewerUserId(),
            reviewFeedbackDetail2.getScore(), reviewFeedbackDetail2.getFeedbackText(), 1L);

        checkResult("update", list.get(2), entity.getId(), reviewFeedbackDetail3.getReviewerUserId(),
            reviewFeedbackDetail3.getScore(), reviewFeedbackDetail3.getFeedbackText(), 1L);
        checkResult("update", list.get(3), entity.getId(), detail.getReviewerUserId(), detail.getScore(),
            detail.getFeedbackText(), 3L);
        checkResult("update", list.get(4), entity.getId(), reviewFeedbackDetail1.getReviewerUserId(), 0, null, 2L);

        list = executeQuery(getConnection(), 4, "SELECT review_feedback_id, project_id, comment, audit_action_type_id"
            + " FROM \"informix\".review_feedback_audit ORDER BY review_feedback_id");
        assertEquals("The result should be correct", 2, list.size());
        checkResult("update", list.get(0), reviewFeedback.getId(), reviewFeedback.getProjectId(),
            reviewFeedback.getComment(), 1L);
        checkResult("update", list.get(1), entity.getId(), entity.getProjectId(), entity.getComment(), 3L);
    }

    /**
     * <p>
     * Accuracy test for the method <code>update</code>.<br>
     * The entity should be correctly updated.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_2() throws Exception {
        reviewFeedback.getDetails().clear();

        // Create.
        instance.create(reviewFeedback, operator);

        ReviewFeedback entity = instance.get(reviewFeedback.getId());

        entity.setProjectId(2L);
        entity.setComment("new comment");
        entity.getDetails().add(reviewFeedbackDetail3);

        instance.update(entity, operator);

        ReviewFeedback retrievedEntity = instance.get(entity.getId());
        assertEquals("The field should be correct", entity.getProjectId(), retrievedEntity.getProjectId());
        assertEquals("The field should be correct", entity.getComment(), retrievedEntity.getComment());
        assertEquals("The field should be correct", operator, retrievedEntity.getCreateUser());
        assertNotNull("The field should be correct", retrievedEntity.getCreateDate());
        assertEquals("The field should be correct", operator, retrievedEntity.getModifyUser());
        assertNotNull("The field should be correct", retrievedEntity.getModifyDate());

        assertEquals("The field should be correct", 1, retrievedEntity.getDetails().size());

        List<List<Object>> list = executeQuery(getConnection(), 5, "SELECT review_feedback_id,"
            + " reviewer_user_id, score, feedback_text, audit_action_type_id"
            + " FROM \"informix\".review_feedback_detail_audit ORDER BY review_feedback_id");
        assertEquals("The result should be correct", 1, list.size());
        checkResult("update", list.get(0), entity.getId(), reviewFeedbackDetail3.getReviewerUserId(),
            reviewFeedbackDetail3.getScore(), reviewFeedbackDetail3.getFeedbackText(), 1L);

        list = executeQuery(getConnection(), 4, "SELECT review_feedback_id, project_id, comment, audit_action_type_id"
            + " FROM \"informix\".review_feedback_audit ORDER BY review_feedback_id");
        assertEquals("The result should be correct", 2, list.size());
        checkResult("update", list.get(0), reviewFeedback.getId(), reviewFeedback.getProjectId(),
            reviewFeedback.getComment(), 1L);
        checkResult("update", list.get(1), entity.getId(), entity.getProjectId(), entity.getComment(), 3L);
    }

    /**
     * <p>
     * Accuracy test for the method <code>update</code>.<br>
     * The entity should be correctly updated.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_3() throws Exception {
        // Create.
        instance.create(reviewFeedback, operator);

        ReviewFeedback entity = instance.get(reviewFeedback.getId());

        entity.setProjectId(2L);
        entity.setComment("new comment");
        entity.getDetails().clear();

        instance.update(entity, operator);

        ReviewFeedback retrievedEntity = instance.get(entity.getId());
        assertEquals("The field should be correct", entity.getProjectId(), retrievedEntity.getProjectId());
        assertEquals("The field should be correct", entity.getComment(), retrievedEntity.getComment());
        assertEquals("The field should be correct", operator, retrievedEntity.getCreateUser());
        assertNotNull("The field should be correct", retrievedEntity.getCreateDate());
        assertEquals("The field should be correct", operator, retrievedEntity.getModifyUser());
        assertNotNull("The field should be correct", retrievedEntity.getModifyDate());

        assertEquals("The field should be correct", 0, retrievedEntity.getDetails().size());

        List<List<Object>> list = executeQuery(getConnection(), 5, "SELECT review_feedback_id,"
            + " reviewer_user_id, score, feedback_text, audit_action_type_id"
            + " FROM \"informix\".review_feedback_detail_audit ORDER BY review_feedback_id");
        assertEquals("The result should be correct", 4, list.size());
        checkResult("update", list.get(0), entity.getId(), reviewFeedbackDetail1.getReviewerUserId(),
            reviewFeedbackDetail1.getScore(), reviewFeedbackDetail1.getFeedbackText(), 1L);
        checkResult("update", list.get(1), entity.getId(), reviewFeedbackDetail2.getReviewerUserId(),
            reviewFeedbackDetail2.getScore(), reviewFeedbackDetail2.getFeedbackText(), 1L);

        List<Object> record1 = list.get(2);
        List<Object> record2 = list.get(3);
        if (((Number) record1.get(1)).longValue() > ((Number) record2.get(1)).longValue()) {
            record1 = list.get(3);
            record2 = list.get(2);
        }
        checkResult("update", record1, entity.getId(), reviewFeedbackDetail1.getReviewerUserId(), 0, null, 2L);
        checkResult("update", record2, entity.getId(), reviewFeedbackDetail2.getReviewerUserId(), 0, null, 2L);

        list = executeQuery(getConnection(), 4, "SELECT review_feedback_id, project_id, comment, audit_action_type_id"
            + " FROM \"informix\".review_feedback_audit ORDER BY review_feedback_id");
        assertEquals("The result should be correct", 2, list.size());
        checkResult("update", list.get(0), reviewFeedback.getId(), reviewFeedback.getProjectId(),
            reviewFeedback.getComment(), 1L);
        checkResult("update", list.get(1), entity.getId(), entity.getProjectId(), entity.getComment(), 3L);
    }

    /**
     * <p>
     * Failure test for the method <code>update</code> with operator is null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_operatorNull() throws Exception {
        operator = null;

        try {
            instance.update(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>update</code> with operator is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_operatorEmpty() throws Exception {
        operator = " ";

        try {
            instance.update(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>update</code> with entity is null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_entityNull() throws Exception {
        reviewFeedback = null;

        try {
            instance.update(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>update</code> with entity.comment is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_commentEmpty() throws Exception {
        reviewFeedback = null;

        try {
            instance.update(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>update</code> with entity.details is null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_detailsNull() throws Exception {
        reviewFeedback.setDetails(null);

        try {
            instance.update(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>update</code> with entity.details contains null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_detailsContainsNull() throws Exception {
        reviewFeedback.getDetails().add(null);

        try {
            instance.update(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>update</code> with entity.details[i].reviewerUserId is invalid.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_reviewerUserIdInvalid1() throws Exception {
        reviewFeedback.getDetails().get(0).setReviewerUserId(10000000000L);

        try {
            instance.update(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>update</code> with entity.details[i].reviewerUserId is invalid.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_reviewerUserIdInvalid2() throws Exception {
        reviewFeedback.getDetails().get(1).setReviewerUserId(reviewFeedback.getDetails().get(0).getReviewerUserId());

        try {
            instance.update(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>update</code> with entity.details[i].feedbackText is null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_feedbackTextNull() throws Exception {
        reviewFeedback.getDetails().get(0).setFeedbackText(null);

        try {
            instance.update(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>update</code> with entity.details[i].feedbackText is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_feedbackTextEmpty() throws Exception {
        reviewFeedback.getDetails().get(0).setFeedbackText(" ");

        try {
            instance.update(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>update</code> with review feedback entity with specified identity is not found
     * in persistence.<br>
     * <code>ReviewFeedbackManagementEntityNotFoundException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_EntityNotFound() throws Exception {
        reviewFeedback.setId(Long.MAX_VALUE);
        try {
            instance.update(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementEntityNotFoundException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>update</code> with an error has occurred.<br>
     * <code>ReviewFeedbackManagementPersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_Error1() throws Exception {
        instance.create(reviewFeedback, operator);

        instance = new JDBCReviewFeedbackManager(null, "baddbconfig");

        try {
            instance.update(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementPersistenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>update</code> with an error has occurred.<br>
     * <code>ReviewFeedbackManagementPersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void test_update_Error3() throws Exception {
        instance.create(reviewFeedback, operator);

        reviewFeedback.setProjectId(Long.MAX_VALUE);

        try {
            instance.update(reviewFeedback, operator);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementPersistenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test for the method <code>get</code>.<br>
     * The entity should be correctly retrieved if exists.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated to reflect the data model changes.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void test_get() throws Exception {
        // Create.
        instance.create(reviewFeedback, operator);

        ReviewFeedback retrievedEntity = instance.get(reviewFeedback.getId());
        assertEquals("The field should be correct", reviewFeedback.getProjectId(), retrievedEntity.getProjectId());
        assertEquals("The field should be correct", reviewFeedback.getComment(), retrievedEntity.getComment());
        assertEquals("The field should be correct", operator, retrievedEntity.getCreateUser());
        assertNotNull("The field should be correct", retrievedEntity.getCreateDate());
        assertEquals("The field should be correct", operator, retrievedEntity.getModifyUser());
        assertNotNull("The field should be correct", retrievedEntity.getModifyDate());

        List<ReviewFeedbackDetail> details = reviewFeedback.getDetails();
        List<ReviewFeedbackDetail> retrievedDetails = retrievedEntity.getDetails();
        assertEquals("The field should be correct", 2, retrievedDetails.size());
        ReviewFeedbackDetail detail1 = details.get(0);
        ReviewFeedbackDetail retrievedDetail1 = retrievedDetails.get(0);
        ReviewFeedbackDetail detail2 = details.get(1);
        ReviewFeedbackDetail retrievedDetail2 = retrievedDetails.get(1);
        if (retrievedDetail1.getScore() > retrievedDetail2.getScore()) {
            retrievedDetail1 = retrievedDetails.get(1);
            retrievedDetail2 = retrievedDetails.get(0);
        }

        assertEquals("The field should be correct", detail1.getFeedbackText(), retrievedDetail1.getFeedbackText());
        assertEquals("The field should be correct", detail1.getReviewerUserId(), retrievedDetail1.getReviewerUserId());
        assertEquals("The field should be correct", detail1.getScore(), retrievedDetail1.getScore());

        assertEquals("The field should be correct", detail2.getFeedbackText(), retrievedDetail2.getFeedbackText());
        assertEquals("The field should be correct", detail2.getReviewerUserId(), retrievedDetail2.getReviewerUserId());
        assertEquals("The field should be correct", detail2.getScore(), retrievedDetail2.getScore());
    }

    /**
     * <p>
     * Accuracy test for the method <code>get</code>.<br>
     * Null should returned if the entity doesn't exists.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void test_get2() throws Exception {
        ReviewFeedback got = instance.get(100L);
        assertNull("Null should returned if the entity doesn't exists.", got);
    }

    /**
     * <p>
     * Failure test for the method <code>get</code> with wrong database configuration. .<br>
     * <code>ReviewFeedbackManagementPersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void test_get11() throws Exception {
        instance = new JDBCReviewFeedbackManager(null, "baddbconfig");
        try {
            instance.get(1);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementPersistenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test for the method <code>delete</code>.<br>
     * The entity should be correctly deleted if the entity exists.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated to reflect the data model changes.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void test_delete() throws Exception {
        // Create.
        instance.create(reviewFeedback, operator);
        boolean ret = instance.delete(reviewFeedback.getId());
        assertTrue("The entity should be deleted", ret);
        ReviewFeedback got = instance.get(reviewFeedback.getId());
        assertNull("The entity should be deleted", got);

        List<List<Object>> list = executeQuery(getConnection(), 5, "SELECT review_feedback_id,"
            + " reviewer_user_id, score, feedback_text, audit_action_type_id"
            + " FROM \"informix\".review_feedback_detail_audit ORDER BY review_feedback_id");
        assertEquals("The result should be correct", 0, list.size());

        list = executeQuery(getConnection(), 4, "SELECT review_feedback_id, project_id, comment, audit_action_type_id"
            + " FROM \"informix\".review_feedback_audit ORDER BY review_feedback_id");
        assertEquals("The result should be correct", 0, list.size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>delete</code>.<br>
     * The method should return false if the entity doesn't exists.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void test_delete2() throws Exception {
        boolean ret = instance.delete(100L);
        assertFalse("The method should return false", ret);
    }

    /**
     * <p>
     * Failure test for the method <code>delete</code> with wrong database configuration. .<br>
     * <code>ReviewFeedbackManagementPersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void test_delete11() throws Exception {
        instance = new JDBCReviewFeedbackManager(null, "baddbconfig");
        try {
            instance.delete(1);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementPersistenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test for the method <code>getForProject</code>.<br>
     * Empty list should be returned.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testGetForProject1() throws Exception {
        assertEquals("No ReviewFeedback entities for projectID(10).", 0, instance.getForProject(10).size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getForProject</code>.<br>
     * The list contains 2 elements.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated to reflect the data model changes.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testGetForProject2() throws Exception {
        ReviewFeedback reviewFeedback2 = new ReviewFeedback();
        reviewFeedback2.setProjectId(1);
        reviewFeedback2.setComment("comment text");
        List<ReviewFeedbackDetail> details = new ArrayList<ReviewFeedbackDetail>();
        details.add(reviewFeedbackDetail3);
        reviewFeedback2.setDetails(details);

        instance.create(reviewFeedback, operator);
        instance.create(reviewFeedback2, operator);

        List<ReviewFeedback> list = instance.getForProject(1);
        ReviewFeedback retrievedReviewFeedback1 = list.get(0);
        ReviewFeedback retrievedReviewFeedback2 = list.get(1);
        if (retrievedReviewFeedback1.getId() > retrievedReviewFeedback2.getId()) {
            retrievedReviewFeedback1 = list.get(1);
            retrievedReviewFeedback2 = list.get(0);
        }

        assertEquals("2 ReviewFeedback entities for projectID(1).", 2, list.size());

        assertEquals("The result should be correct", reviewFeedback.getComment(),
            retrievedReviewFeedback1.getComment());
        assertEquals("The result should be correct", 1, retrievedReviewFeedback1.getProjectId());
        assertEquals("The result should be correct", 2, retrievedReviewFeedback1.getDetails().size());

        assertEquals("The result should be correct", reviewFeedback2.getComment(),
            retrievedReviewFeedback2.getComment());
        assertEquals("The result should be correct", 1, retrievedReviewFeedback2.getProjectId());
        assertEquals("The result should be correct", 1, retrievedReviewFeedback2.getDetails().size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getForProject</code>.<br>
     * The list contains 2 elements.
     * </p>
     *
     * @throws Exception
     *             to junit
     *
     * @since 2.0
     */
    public void testGetForProject3() throws Exception {
        reviewFeedback.getDetails().clear();
        instance.create(reviewFeedback, operator);

        List<ReviewFeedback> list = instance.getForProject(1);

        assertEquals("1 ReviewFeedback entity for projectID(1).", 1, list.size());
        assertEquals("The result should be correct", 0, list.get(0).getDetails().size());
    }

    /**
     * <p>
     * Failure test for the method <code>getForProject</code> with wrong database configuration. .<br>
     * <code>ReviewFeedbackManagementPersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void testGetForProjectFailure() throws Exception {
        instance = new JDBCReviewFeedbackManager(null, "baddbconfig");
        try {
            instance.getForProject(1);
            fail("An exception is expected");
        } catch (ReviewFeedbackManagementPersistenceException e) {
            // pass
        }
    }

    /**
     * Checks the updated entity.
     *
     * @param entity
     *            the entity
     * @param retrievedEntity
     *            the updated entity.
     *
     * @since 2.0
     */
    private void checkUpdatedEntity(ReviewFeedback entity, ReviewFeedback retrievedEntity) {
        assertEquals("The field should be correct", entity.getProjectId(), retrievedEntity.getProjectId());
        assertEquals("The field should be correct", entity.getComment(), retrievedEntity.getComment());
        assertEquals("The field should be correct", operator, retrievedEntity.getCreateUser());
        assertNotNull("The field should be correct", retrievedEntity.getCreateDate());
        assertEquals("The field should be correct", operator, retrievedEntity.getModifyUser());
        assertNotNull("The field should be correct", retrievedEntity.getModifyDate());

        List<ReviewFeedbackDetail> details = entity.getDetails();
        List<ReviewFeedbackDetail> retrievedDetails = retrievedEntity.getDetails();
        assertEquals("The field should be correct", 2, retrievedDetails.size());
        ReviewFeedbackDetail detail1 = details.get(0);
        ReviewFeedbackDetail retrievedDetail1 = retrievedDetails.get(0);
        ReviewFeedbackDetail detail2 = details.get(1);
        ReviewFeedbackDetail retrievedDetail2 = retrievedDetails.get(1);
        if (retrievedDetail1.getScore() < retrievedDetail2.getScore()) {
            retrievedDetail1 = retrievedDetails.get(1);
            retrievedDetail2 = retrievedDetails.get(0);
        }

        assertEquals("The field should be correct", detail1.getFeedbackText(), retrievedDetail1.getFeedbackText());
        assertEquals("The field should be correct", detail1.getReviewerUserId(), retrievedDetail1.getReviewerUserId());
        assertEquals("The field should be correct", detail1.getScore(), retrievedDetail1.getScore());

        assertEquals("The field should be correct", detail2.getFeedbackText(), retrievedDetail2.getFeedbackText());
        assertEquals("The field should be correct", detail2.getReviewerUserId(), retrievedDetail2.getReviewerUserId());
        assertEquals("The field should be correct", detail2.getScore(), retrievedDetail2.getScore());
    }
}
