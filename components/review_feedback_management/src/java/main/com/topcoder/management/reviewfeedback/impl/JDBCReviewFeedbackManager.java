/*
 * Copyright (C) 2012, 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.PropertyTypeMismatchException;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.configuration.persistence.ConfigurationParserException;
import com.topcoder.configuration.persistence.NamespaceConflictException;
import com.topcoder.configuration.persistence.UnrecognizedFileTypeException;
import com.topcoder.configuration.persistence.UnrecognizedNamespaceException;
import com.topcoder.db.connectionfactory.ConfigurationException;
import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;
import com.topcoder.management.reviewfeedback.ReviewFeedback;
import com.topcoder.management.reviewfeedback.ReviewFeedbackDetail;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManagementConfigurationException;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManagementEntityNotFoundException;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManagementPersistenceException;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManager;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * This is a JDBC based implementation of ReviewFeedbackManager. It provides CRUD operations for managing the
 * ReviewFeedback entities in persistence and performs them using SQL queries executed with aid of JDBC. This class uses
 * TC configuration components for configuration and owns a DBConnectionFactory used for obtaining DB connections. Also
 * it performs logging via TC Logging Wrapper component as per CS 1.3.1.
 * </p>
 *
 * <p>
 * <strong>Sample Config:</strong>
 *
 * <pre>
 * &lt;?xml version="1.0"?&gt;
 * &lt;CMConfig&gt;
 *   &lt;Config name="com.topcoder.management.reviewfeedback"&gt;
 *     &lt;Property name="logName"&gt;
 *       &lt;value&gt;com.topcoder.management.reviewfeedback&lt;/value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name="dbConnectionName"&gt;
 *       &lt;value&gt;TCSCatalog&lt;/value&gt;
 *     &lt;/Property&gt;
 *
 *     &lt;Property name="dbConnectionFactoryConfiguration"&gt;
 *       &lt;!-- Configuration for DBConnectionFactoryImpl should be placed here.
 *               It must have "TCSCatalog" connection configured. --&gt;
 *         &lt;Property name="com.topcoder.db.connectionfactory.DBConnectionFactoryImpl"&gt;
 *             &lt;Property name="connections"&gt;
 *                 &lt;Property name="default"&gt;
 *                     &lt;Value&gt;TCSCatalog&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name="TCSCatalog"&gt;
 *                     &lt;Property name="producer"&gt;
 *                         &lt;Value&gt;com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer&lt;/Value&gt;
 *                     &lt;/Property&gt;
 *                     &lt;Property name="parameters"&gt;
 *                         &lt;Property name="jdbc_driver"&gt;
 *                             &lt;Value&gt;com.informix.jdbc.IfxDriver&lt;/Value&gt;
 *                         &lt;/Property&gt;
 *                         &lt;Property name="jdbc_url"&gt;
 *                             &lt;Value&gt;
 *                                 jdbc:informix-sqli://localhost:1526/test:informixserver=ol_topcoder&lt;/Value&gt;
 *                         &lt;/Property&gt;
 *                         &lt;Property name="user"&gt;
 *                             &lt;Value&gt;informix&lt;/Value&gt;
 *                         &lt;/Property&gt;
 *                         &lt;Property name="password"&gt;
 *                             &lt;Value&gt;123456&lt;/Value&gt;
 *                         &lt;/Property&gt;
 *                     &lt;/Property&gt;
 *                 &lt;/Property&gt;
 *             &lt;/Property&gt;
 *         &lt;/Property&gt;
 *     &lt;/Property&gt;
 *
 *   &lt;/Config&gt;
 *
 * &lt;/CMConfig&gt;
 *
 * </pre>
 *
 * </p>
 *
 * <p>
 *
 * <strong>Sample Usage:</strong>
 *
 * <pre>
 * // Create sample input.
 * ReviewFeedback entity = new ReviewFeedback();
 * entity.setProjectId(1);
 * entity.setComment(&quot;comment text&quot;);
 * ReviewFeedbackDetail detail = new ReviewFeedbackDetail();
 * detail.setReviewerUserId(126);
 * detail.setScore(2);
 * detail.setFeedbackText(&quot;feedback text&quot;);
 * List&lt;ReviewFeedbackDetail&gt; details = new ArrayList&lt;ReviewFeedbackDetail&gt;();
 * details.add(detail);
 * entity.setDetails(details);
 *
 * // Create DAO.
 * ReviewFeedbackManager dao = new JDBCReviewFeedbackManager();
 *
 * // Perform CRUD operations
 * // Create.
 * entity = dao.create(entity, &quot;operator&quot;);
 * System.out.println(&quot;entity.getId() = &quot; + entity.getId());
 * System.out.println(&quot;entity.getCreateUser() = &quot; + entity.getCreateUser());
 * // Update.
 * entity.getDetails().get(0).setScore(1);
 * dao.update(entity, &quot;anotherOperator&quot;);
 * // Get.
 * long id = entity.getId();
 * entity = null;
 * entity = dao.get(id);
 * System.out.println(&quot;entity.getScore() = &quot; + entity.getDetails().get(0).getScore());
 * System.out.println(&quot;entity.getModifyUser() = &quot; + entity.getModifyUser());
 *
 * // Retrieves entities with given project ID from persistence.
 * List&lt;ReviewFeedback&gt; list = dao.getForProject(1);
 * System.out.println(list.size() + &quot; ReviewFeedback entities return.&quot;);
 *
 * // Delete.
 * dao.delete(entity.getId());
 * entity = dao.get(entity.getId());
 * System.out.println(&quot;'entity == null' = &quot; + (entity == null));
 * </pre>
 *
 * </p>
 *
 * <p>
 * <em>Changes in 2.0:</em>
 * <ol>
 * <li>Documentation is updated to reflect the data model changes.</li>
 * <li>New "operator:String" argument is added to create() and update() methods in order to support auditing, and return
 * value is added to update() method.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread safety: </strong> This class is immutable and thread-safe with assumption that caller uses method
 * arguments thread safely.
 * </p>
 *
 * @author gevak, amazingpig, hesibo, sparemax
 * @version 2.0.1
 */
public class JDBCReviewFeedbackManager implements ReviewFeedbackManager {
    /**
     * It is a constant for default configuration namespace. It is used in configuration file based constructors when no
     * configuration namespace is specified by user. It is not null and not empty.
     */
    public static final String DEFAULT_CONFIGURATION_NAMESPACE = "com.topcoder.management.reviewfeedback";
    /**
     * The default log date format.
     */
    private static final String DEFAULT_LOGDATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * <p>
     * The sql to create a review feedback.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated according to the new data model.</li>
     * </ol>
     * </p>
     */
    private static final String CREATE_REVIEW_FEEDBACK = "INSERT INTO \"informix\".review_feedback"
        + " (project_id, comment, create_user, create_date, modify_user, modify_date) VALUES (?,?,?,?,?,?)";

    /**
     * The sql to create a review feedback detail.
     *
     * @since 2.0
     */
    private static final String CREATE_REVIEW_FEEDBACK_DETAIL = "INSERT INTO \"informix\".review_feedback_detail"
        + " (review_feedback_id, reviewer_user_id, score, feedback_text) VALUES (?,?,?,?)";

    /**
     * <p>
     * The sql to update a review feedback.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated according to the new data model.</li>
     * </ol>
     * </p>
     */
    private static final String UPDATE_REVIEW_FEEDBACK = "UPDATE \"informix\".review_feedback SET project_id = ?,"
        + " comment = ?, modify_user = ?, modify_date = ? WHERE review_feedback_id = ?";

    /**
     * The sql to select review id.
     *
     * @since 2.0
     */
    private static final String SELECT_REVIEWER_ID_BY_FEEDBACK_ID = "SELECT reviewer_user_id"
        + " FROM \"informix\".review_feedback_detail WHERE review_feedback_id = ?";
    /**
     * The sql to update a review feedback detail.
     *
     * @since 2.0
     */
    private static final String UPDATE_REVIEW_FEEDBACK_DETAIL = "UPDATE \"informix\".review_feedback_detail"
        + " SET score = ?, feedback_text = ? WHERE review_feedback_id = ? AND reviewer_user_id = ?";

    /**
     * The sql to delete review feedback detail.
     *
     * @since 2.0
     */
    private static final String DELETE_REVIEW_FEEDBACK_DETAIL = "DELETE FROM \"informix\".review_feedback_detail"
        + " WHERE review_feedback_id = ? AND reviewer_user_id IN (%1$s)";

    /**
     * <p>
     * The sql to select a review feedback.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated according to the new data model.</li>
     * </ol>
     * </p>
     */
    private static final String SELECT_REVIEW_FEEDBACK = "SELECT \"informix\".review_feedback.review_feedback_id,"
        + " project_id, comment, create_user, create_date, modify_user, modify_date,"
        + " reviewer_user_id, score, feedback_text FROM \"informix\".review_feedback"
        + " LEFT JOIN \"informix\".review_feedback_detail"
        + " ON \"informix\".review_feedback.review_feedback_id = \"informix\".review_feedback_detail.review_feedback_id"
        + " WHERE \"informix\".review_feedback.review_feedback_id = ?";

    /**
     * <p>
     * The sql to select review feedback entities by project ID.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated according to the new data model.</li>
     * </ol>
     * </p>
     */
    private static final String SELECT_REVIEW_FEEDBACK_BY_PROJECTID = "SELECT"
        + " \"informix\".review_feedback.review_feedback_id, comment,"
        + " create_user, create_date, modify_user, modify_date, reviewer_user_id, score,"
        + " feedback_text FROM \"informix\".review_feedback LEFT JOIN \"informix\".review_feedback_detail"
        + " ON \"informix\".review_feedback.review_feedback_id = \"informix\".review_feedback_detail.review_feedback_id"
        + " WHERE \"informix\".review_feedback.project_id = ?";

    /**
     * The sql to delete review feedback audit.
     *
     * @since 2.0
     */
    private static final String DELETE_REVIEW_FEEDBACK_AUDIT_BY_FEEDBACK_ID = "DELETE FROM"
        + " \"informix\".review_feedback_audit WHERE review_feedback_id = ?";

    /**
     * The sql to delete review feedback detail audit.
     *
     * @since 2.0
     */
    private static final String DELETE_REVIEW_FEEDBACK_DETAIL_AUDIT_BY_FEEDBACK_ID = "DELETE FROM"
        + " \"informix\".review_feedback_detail_audit WHERE review_feedback_id = ?";

    /**
     * The sql to delete review feedback detail.
     *
     * @since 2.0
     */
    private static final String DELETE_REVIEW_FEEDBACK_DETAIL_BY_FEEDBACK_ID = "DELETE FROM"
        + " \"informix\".review_feedback_detail WHERE review_feedback_id = ?";

    /**
     * The sql to delete a review feedback.
     */
    private static final String DELETE_REVIEW_FEEDBACK_BY_ID = "DELETE FROM \"informix\".review_feedback"
        + " WHERE review_feedback_id = ?";

    /**
     * The sql to insert a review feedback audit.
     *
     * @since 2.0
     */
    private static final String INSERT_REVIEW_FEEDBACK_AUDIT = "INSERT INTO \"informix\".review_feedback_audit"
        + " (review_feedback_id, project_id, comment, audit_action_type_id, action_user, action_date)"
        + " VALUES (?, ?, ?, ?, ?, ?)";

    /**
     * The sql to insert a review feedback detail audit.
     *
     * @since 2.0
     */
    private static final String INSERT_REVIEW_FEEDBACK_DETAIL_AUDIT = "INSERT INTO"
        + " \"informix\".review_feedback_detail_audit (review_feedback_id, reviewer_user_id, score, feedback_text,"
        + " audit_action_type_id, action_user, action_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

    /**
     * The maximum length of the user id.
     */
    private static final int USER_ID_MAX_LEN = 10;

    /**
     * The default create audit action type id.
     *
     * @since 2.0
     */
    private static final int DEFAULT_CREATE_AUDIT_TYPE_ID = 1;

    /**
     * The default update audit action type id.
     *
     * @since 2.0
     */
    private static final int DEFAULT_UPDATE_AUDIT_TYPE_ID = 3;

    /**
     * The default delete audit action type id.
     *
     * @since 2.0
     */
    private static final int DEFAULT_DELETE_AUDIT_TYPE_ID = 2;

    /**
     * <p>
     * Represents the entrance message.
     * </p>
     *
     * <p>
     * Arguments:
     * <ol>
     * <li>the date time.</li>
     * <li>the method signature.</li>
     * <li>the input log.</li>
     * </ol>
     * </p>
     */
    private static final String MESSAGE_ENTRANCE = "%1$s INFO The method [%2$s] began. [%3$s]";

    /**
     * <p>
     * Represents the exit message.
     * </p>
     *
     * <p>
     * Arguments:
     * <ol>
     * <li>the date time.</li>
     * <li>the method signature.</li>
     * <li>the output log.</li>
     * </ol>
     * </p>
     */
    private static final String MESSAGE_EXIT = "%1$s INFO The method [%2$s] ended. [%3$s]";

    /**
     * <p>
     * Represents the error message.
     * </p>
     *
     * <p>
     * Arguments:
     * <ol>
     * <li>the date time.</li>
     * <li>the method signature.</li>
     * <li>the error message.</li>
     * </ol>
     * </p>
     */
    private static final String MESSAGE_ERROR = "%1$s ERROR Error in method [%2$s], details: %3$s";
    /**
     * Database connection factory, used by CRUD methods to obtain database connections (for performing operations in
     * the database). It is initialized in constructor and never changed afterwards. Not null.
     */
    private final DBConnectionFactory dbConnectionFactory;
    /**
     * Logger, used by CRUD methods to perform logging as per CS 1.3. If null, logging will not be performed. It is
     * initialized in constructor and never changed afterwards.
     */
    private final Log log;
    /**
     * Database connection name used (in CRUD methods) to obtain a database connection via owned DBConnectionFactory.
     * That connection will be used to perform operations in persistence. It is initialized from constructor and never
     * changed afterwards. Not null, not empty.
     */
    private final String dbConnectionName;
    /**
     * Audit action type ID for "create" action, used for auditing (in create and update operations). It is initialized
     * from constructor and never changed afterwards. Can be any value. Default value is 1 (will be set in constructor
     * if no configuration value provided).
     *
     * @since 2.0
     */
    private final long createAuditActionTypeId;
    /**
     * Audit action type ID for "update" action, used for auditing (in create and update operations). It is initialized
     * from constructor and never changed afterwards. Can be any value. Default value is 3 (will be set in constructor
     * if no configuration value provided).
     *
     * @since 2.0
     */
    private final long updateAuditActionTypeId;
    /**
     * Audit action type ID for "delete" action, used for auditing (in create and update operations). It is initialized
     * from constructor and never changed afterwards. Can be any value. Default value is 2 (will be set in constructor
     * if no configuration value provided).
     *
     * @since 2.0
     */
    private final long deleteAuditActionTypeId;

    /**
     * Creates instance and configures it from default configuration file (ConfigurationFileManager.DEFAULT_CONFIG_PATH)
     * using default configuration namespace (DEFAULT_CONFIGURATION_NAMESPACE from this class).
     *
     * @throws ReviewFeedbackManagementConfigurationException
     *             If any error occurs or obtained configuration object contains invalid configuration.
     */
    public JDBCReviewFeedbackManager() {
        this(loadConfiguration(null, null));
    }

    /**
     * Creates instance and configures it from specified configuration file (or default file
     * ConfigurationFileManager.DEFAULT_CONFIG_PATH if no configuration file is specified) using specified configuration
     * namespace (or default DEFAULT_CONFIGURATION_NAMESPACE from this class if configuration namespace is not
     * specified).
     *
     * @param configurationNamespace
     *            Configuration namespace. If null, default value will be used. Must be non-empty string (if not null).
     * @param configurationFilename
     *            Configuration filename. If null, default value will be used. Must be non-empty string (if not null).
     * @throws IllegalArgumentException
     *             if configurationNamespace or configurationFilename is empty
     * @throws ReviewFeedbackManagementConfigurationException
     *             If any error occurs or obtained configuration object contains invalid configuration
     */
    public JDBCReviewFeedbackManager(String configurationFilename, String configurationNamespace) {
        this(loadConfiguration(configurationFilename, configurationNamespace));
    }

    /**
     * <p>
     * Creates instance and configures it using data from provided configuration object.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated implementation to parse audit action type IDs.</li>
     * </ol>
     * </p>
     *
     * @param configurationObject
     *            Configuration object. Not null.
     * @throws IllegalArgumentException
     *             If configurationObject is null
     * @throws ReviewFeedbackManagementConfigurationException
     *             If any error occurs or obtained configuration object contains invalid configuration
     */
    public JDBCReviewFeedbackManager(ConfigurationObject configurationObject) {
        checkNullIAE(configurationObject, "configurationObject");
        // Get DB connection name from configuration:
        try {
            ConfigurationObject cfg = configurationObject.getChild(DEFAULT_CONFIGURATION_NAMESPACE);
            if (cfg == null) {
                throw new ReviewFeedbackManagementConfigurationException(
                    "The configuration object should have a child named '" + DEFAULT_CONFIGURATION_NAMESPACE + "'");
            }
            dbConnectionName = cfg.getPropertyValue("dbConnectionName", String.class);
            checkNullCFE(dbConnectionName, "dbConnectionName");
            checkEmptyCFE(dbConnectionName, "dbConnectionName");
            // Create logger as per configuration:
            log = cfg.containsProperty("logName") ? LogManager.getLog(cfg.getPropertyValue("logName", String.class))
                : null;
            // Get DB connection factory configuration:
            ConfigurationObject dbConfig = cfg.getChild("dbConnectionFactoryConfiguration");
            checkNullCFE(dbConfig, "dbConfig");
            // Create DB connection factory as per configuration:
            dbConnectionFactory = new DBConnectionFactoryImpl(dbConfig);

            createAuditActionTypeId = checkAuditActionTypeId(
                cfg.getPropertyValue("createAuditActionTypeId", Long.class), DEFAULT_CREATE_AUDIT_TYPE_ID);
            updateAuditActionTypeId = checkAuditActionTypeId(
                cfg.getPropertyValue("updateAuditActionTypeId", Long.class), DEFAULT_UPDATE_AUDIT_TYPE_ID);
            deleteAuditActionTypeId = checkAuditActionTypeId(
                cfg.getPropertyValue("deleteAuditActionTypeId", Long.class), DEFAULT_DELETE_AUDIT_TYPE_ID);

        } catch (ConfigurationAccessException e) {
            throw new ReviewFeedbackManagementConfigurationException("Failed to access the configuration.", e);
        } catch (PropertyTypeMismatchException e) {
            throw new ReviewFeedbackManagementConfigurationException("Failed to get the property value.", e);
        } catch (UnknownConnectionException e) {
            throw new ReviewFeedbackManagementConfigurationException("The connection is unknown.", e);
        } catch (ConfigurationException e) {
            throw new ReviewFeedbackManagementConfigurationException("The database configuration is invalid.", e);
        }
    }

    /**
     * <p>
     * Programmatic constructor.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Added audit action type ID parameters.</li>
     * </ol>
     * </p>
     *
     * @param dbConnectionFactory
     *            Database connection factory, used by CRUD methods to obtain database connections (for performing
     *            operations in the database).
     * @param dbConnectionName
     *            Database connection name used (in CRUD methods) to obtain a database connection via owned
     *            DBConnectionFactory. That connection will be used to perform operations in persistence.
     * @param log
     *            Logger, used by CRUD methods to perform logging. If null, logging will not be performed
     * @param createAuditActionTypeId
     *            Audit action type ID for "create" action, used for auditing (in create and update operations). If
     *            null, default value will be used.
     * @param updateAuditActionTypeId
     *            Audit action type ID for "update" action, used for auditing (in create and update operations). If
     *            null, default value will be used.
     * @param deleteAuditActionTypeId
     *            Audit action type ID for "delete" action, used for auditing (in create and update operations). If
     *            null, default value will be used.
     *
     * @throws IllegalArgumentException
     *             If dbConnectionFactory is null, or dbConnectionName is null or empty
     */
    public JDBCReviewFeedbackManager(DBConnectionFactory dbConnectionFactory, String dbConnectionName, Log log,
        Long createAuditActionTypeId, Long updateAuditActionTypeId, Long deleteAuditActionTypeId) {
        checkNullIAE(dbConnectionFactory, "dbConnectionFactory");
        checkNullIAE(dbConnectionName, "dbConnectionName");
        checkEmptyIAE(dbConnectionName, "dbConnectionName");
        this.dbConnectionFactory = dbConnectionFactory;
        this.dbConnectionName = dbConnectionName;
        this.log = log;

        this.createAuditActionTypeId = checkAuditActionTypeId(createAuditActionTypeId, DEFAULT_CREATE_AUDIT_TYPE_ID);
        this.updateAuditActionTypeId = checkAuditActionTypeId(updateAuditActionTypeId, DEFAULT_UPDATE_AUDIT_TYPE_ID);
        this.deleteAuditActionTypeId = checkAuditActionTypeId(deleteAuditActionTypeId, DEFAULT_DELETE_AUDIT_TYPE_ID);
    }

    /**
     * <p>
     * Creates given entity (along with its details records) in persistence.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Implementation and documentation are updated according to the new data model and new audit requirements.</li>
     * <li>New "operator:String" argument is added in order to support auditing.</li>
     * <li>Transaction handling is added.</li>
     * </ol>
     * </p>
     *
     * @param entity
     *            Entity to be created in persistence. Its original Id property will be ignored and after successful
     *            execution, it will be populated with newly generated identity. Its original audit-related properties
     *            (CreateUser, CreateDate, ModifyUser, ModifyDate) will be ignored, they all will be populated by this
     *            method. It must be not null. Its Comment property but be not empty (but may be null). Its Details
     *            property must be not null (but may be empty) and each of its elements (if any) must conform to all of
     *            the following validation rules:
     *            <ol>
     *            <li>Must be not null.</li>
     *            <li>Its ReviewerUserId property must contain not more than 10 significant decimal digits and must be
     *            unique across all entity.Details elements.</li>
     *            <li>Its FeedbackText property must be not null and not empty.</li>
     *            </ol>
     * @param operator
     *            Specifies user who is performing this operation. Must be not null and not empty.
     *
     * @return Entity created in persistence (some of its properties will be populated as per method argument
     *         documentation). Not null.
     *
     * @throws IllegalArgumentException
     *             if any argument is invalid (as per argument description above).
     * @throws ReviewFeedbackManagementPersistenceException
     *             if any issue occurs with persistence.
     */
    public ReviewFeedback create(ReviewFeedback entity, String operator)
        throws ReviewFeedbackManagementPersistenceException {
        final String signature = "JDBCReviewFeedbackManager.create";
        logEntrance(signature, new String[] {"entity", "operator"}, new Object[] {entity, operator});
        Connection conn = null;
        try {
            checkEntity(entity);
            checkNullIAE(operator, "operator");
            checkEmptyIAE(operator, "operator");

            // Get DB connection:
            conn = dbConnectionFactory.createConnection(dbConnectionName);
            conn.setAutoCommit(false);

            Date createDate = new Date();
            Timestamp currentTimestamp = new Timestamp(createDate.getTime());

            // Create feedback record
            long entityId = executeUpdate(signature, conn, true, CREATE_REVIEW_FEEDBACK, entity.getProjectId(),
                entity.getComment(), operator, currentTimestamp, operator, currentTimestamp);
            entity.setId(entityId);

            // Create feedback detail records (if any)
            List<ReviewFeedbackDetail> details = entity.getDetails();
            createDetails(signature, conn, entityId, details);

            // Create audit records:
            auditReviewFeedback(signature, conn, entity, createAuditActionTypeId, operator, currentTimestamp);
            auditReviewFeedbackDetails(signature, conn, entityId, details, createAuditActionTypeId, operator,
                currentTimestamp);

            conn.commit();

            // Populate entity properties and return the populated entity:
            entity.setCreateUser(operator);
            entity.setCreateDate(createDate);
            entity.setModifyUser(operator);
            entity.setModifyDate(createDate);

            logExit(signature, new Object[] {entity});
            return entity;
        } catch (IllegalArgumentException e) {
            throw logException(signature, e);
        } catch (DBConnectionException e) {
            throw logException(signature, new ReviewFeedbackManagementPersistenceException(
                "Failed to create the connection '" + dbConnectionName + "'", e));
        } catch (SQLException e) {
            // Roll back
            rollback(conn, signature);

            throw logException(signature, new ReviewFeedbackManagementPersistenceException(
                "Sql error occurs when creating the review feedback", e));
        } finally {
            // Close the connection
            close(conn, null, signature);
        }
    }

    /**
     * <p>
     * Updates given entity (along with associations to details records) in persistence.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Implementation and documentation are updated according to the new data model and new audit requirements.</li>
     * <li>New "operator:String" argument is added in order to support auditing.</li>
     * <li>Return value is added.</li>
     * <li>Transaction handling is added.</li>
     * </ol>
     * </p>
     *
     * @param entity
     *            Entity to be updated in persistence. Its Id property will be used to identify (find) entity in
     *            persistence. Its original audit-related properties (CreateUser, CreateDate, ModifyUser, ModifyDate)
     *            will be ignored, ModifyUser and ModifyDate will be populated by this method. It must be not null. Its
     *            Comment property but be not empty (but may be null). Its Details property must be not null (but may be
     *            empty) and each of its elements (if any) must conform to all of the following validation rules:
     *            <ol>
     *            <li>Must be not null.</li>
     *            <li>Its ReviewerUserId property must contain not more than 10 significant decimal digits and must be
     *            unique across all entity.Details elements.</li>
     *            <li>Its FeedbackText property must be not null and not empty.</li>
     *            </ol>
     * @param operator
     *            Specifies user who is performing this operation. Must be not null and not empty.
     *
     * @return Updated entity. Not null.
     *
     * @throws IllegalArgumentException
     *             if any argument is invalid (as per argument description above).
     * @throws ReviewFeedbackManagementEntityNotFoundException
     *             if review feedback entity with specified identity is not found in persistence.
     * @throws ReviewFeedbackManagementPersistenceException
     *             if any issue occurs with persistence.
     */
    public ReviewFeedback update(ReviewFeedback entity, String operator)
        throws ReviewFeedbackManagementEntityNotFoundException, ReviewFeedbackManagementPersistenceException {
        final String signature = "JDBCReviewFeedbackManager.update";
        logEntrance(signature, new String[] {"entity", "operator"}, new Object[] {entity, operator});
        Connection conn = null;
        try {
            checkEntity(entity);
            checkNullIAE(operator, "operator");
            checkEmptyIAE(operator, "operator");

            // Get DB connection:
            conn = dbConnectionFactory.createConnection(dbConnectionName);
            conn.setAutoCommit(false);

            long feedbackId = entity.getId();

            // Populate query parameters.
            Date modifyDate = new Date();
            Timestamp currentTimestamp = new Timestamp(modifyDate.getTime());

            // Update feedback record
            if (executeUpdate(signature, conn, false, UPDATE_REVIEW_FEEDBACK, entity.getProjectId(),
                entity.getComment(), operator, currentTimestamp, feedbackId) == 0) {
                throw logException(signature, new ReviewFeedbackManagementEntityNotFoundException(
                    "The review feedback entity with specified identity is not found in persistence."));
            }

            List<ReviewFeedbackDetail> createdDetails = new ArrayList<ReviewFeedbackDetail>();
            List<ReviewFeedbackDetail> updatedDetails = new ArrayList<ReviewFeedbackDetail>();
            List<ReviewFeedbackDetail> deletedDetails = new ArrayList<ReviewFeedbackDetail>();

            updateDetails(signature, conn, feedbackId, entity.getDetails(), createdDetails, updatedDetails,
                deletedDetails);

            // Create audit records:
            auditReviewFeedback(signature, conn, entity, updateAuditActionTypeId, operator, currentTimestamp);
            auditReviewFeedbackDetails(signature, conn, feedbackId, createdDetails, createAuditActionTypeId, operator,
                currentTimestamp);
            auditReviewFeedbackDetails(signature, conn, feedbackId, updatedDetails, updateAuditActionTypeId, operator,
                currentTimestamp);
            auditReviewFeedbackDetails(signature, conn, feedbackId, deletedDetails, deleteAuditActionTypeId, operator,
                currentTimestamp);

            conn.commit();

            // Update entity properties and return the updated entity:
            entity.setModifyUser(operator);
            entity.setModifyDate(modifyDate);

            logExit(signature, new Object[] {entity});
            return entity;
        } catch (IllegalArgumentException e) {
            throw logException(signature, e);
        } catch (DBConnectionException e) {
            throw logException(signature, new ReviewFeedbackManagementPersistenceException(
                "Failed to create the connection '" + dbConnectionName + "'", e));
        } catch (SQLException e) {
            // Roll back
            rollback(conn, signature);

            throw logException(signature, new ReviewFeedbackManagementPersistenceException(
                "Sql error occurs when trying to update the review feedback", e));
        } finally {
            // Close the connection
            close(conn, null, signature);
        }
    }

    /**
     * <p>
     * Retrieves entity with given ID from persistence.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Implementation is updated according to the new data model.</li>
     * </ol>
     * </p>
     *
     * @param id
     *            ID of entity to retrieve.
     * @return Retrieved entity. Null if entity with specified ID is not found in persistence.
     * @throws ReviewFeedbackManagementPersistenceException
     *             If any issue occurs with persistence.
     */
    public ReviewFeedback get(long id) throws ReviewFeedbackManagementPersistenceException {
        final String signature = "JDBCReviewFeedbackManager.get";
        logEntrance(signature, new String[] {"id"}, new Object[] {id});
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // Get DB connection:
            conn = dbConnectionFactory.createConnection(dbConnectionName);
            // Create statement:
            stmt = conn.prepareStatement(SELECT_REVIEW_FEEDBACK);
            // Populate query parameters:
            stmt.setLong(1, id);
            // Execute query:
            ResultSet rs = stmt.executeQuery();

            ReviewFeedback result = null;
            if (rs.next()) {
                long projectId = rs.getLong("project_id");
                result = populateReviewFeedback(rs, projectId, id);
                List<ReviewFeedbackDetail> details = result.getDetails();
                do {
                    if (rs.getObject("reviewer_user_id") != null) {
                        details.add(populateReviewFeedbackDetail(rs));
                    }
                } while (rs.next());
            }

            logExit(signature, new Object[] {result});
            return result;
        } catch (DBConnectionException e) {
            throw logException(signature, new ReviewFeedbackManagementPersistenceException(
                "Failed to create the connection '" + dbConnectionName + "'", e));
        } catch (SQLException e) {
            throw logException(signature, new ReviewFeedbackManagementPersistenceException(
                "Sql error occurs when retrieving the review feedback with id '" + id + "'", e));
        } finally {
            // Close the connection and prepared statement
            close(conn, stmt, signature);
        }
    }

    /**
     * <p>
     * Deletes entity with given ID from persistence.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Implementation is updated according to the new data model.</li>
     * <li>Transaction handling is added.</li>
     * </ol>
     * </p>
     *
     * @param id
     *            ID of entity to delete.
     * @return true if entity was found and deleted, false if entity was not found.
     * @throws ReviewFeedbackManagementPersistenceException
     *             If any issue occurs with persistence.
     */
    public boolean delete(long id) throws ReviewFeedbackManagementPersistenceException {
        final String signature = "JDBCReviewFeedbackManager.delete";
        logEntrance(signature, new String[] {"id"}, new Object[] {id});
        Connection conn = null;
        try {
            // Get DB connection:
            conn = dbConnectionFactory.createConnection(dbConnectionName);
            conn.setAutoCommit(false);

            // Delete audit records
            executeUpdate(signature, conn, false, DELETE_REVIEW_FEEDBACK_AUDIT_BY_FEEDBACK_ID, id);

            // Delete detail audit records
            executeUpdate(signature, conn, false, DELETE_REVIEW_FEEDBACK_DETAIL_AUDIT_BY_FEEDBACK_ID, id);

            // Delete detail records
            executeUpdate(signature, conn, false, DELETE_REVIEW_FEEDBACK_DETAIL_BY_FEEDBACK_ID, id);

            // Delete feedback record
            boolean ret = (executeUpdate(signature, conn, false, DELETE_REVIEW_FEEDBACK_BY_ID, id) > 0);

            conn.commit();

            logExit(signature, new Object[] {ret});
            return ret;
        } catch (DBConnectionException e) {
            throw logException(signature, new ReviewFeedbackManagementPersistenceException(
                "Failed to create the connection '" + dbConnectionName + "'", e));
        } catch (SQLException e) {
            // Roll back
            rollback(conn, signature);

            throw logException(signature, new ReviewFeedbackManagementPersistenceException(
                "Sql error occurs when trying to delete the records", e));
        } finally {
            // Close the connection
            close(conn, null, signature);
        }
    }

    /**
     * <p>
     * Retrieves entities with given project ID from persistence.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Implementation is updated according to the new data model.</li>
     * </ol>
     * </p>
     *
     * @param projectId
     *            the project ID
     *
     * @return a list of retrieved entities
     *
     * @throws ReviewFeedbackManagementPersistenceException
     *             If any issue occurs with persistence.
     */
    public List<ReviewFeedback> getForProject(long projectId) throws ReviewFeedbackManagementPersistenceException {
        final String signature = "JDBCReviewFeedbackManager.getForProject";
        logEntrance(signature, new String[] {"projectId"}, new Object[] {projectId});
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // Get DB connection
            conn = dbConnectionFactory.createConnection(dbConnectionName);
            // Create statement
            stmt = conn.prepareStatement(SELECT_REVIEW_FEEDBACK_BY_PROJECTID);
            // Populate query parameters:
            stmt.setLong(1, projectId);
            // Execute query:
            ResultSet rs = stmt.executeQuery();

            // This map will store the retrieved review feedback entities;
            // key is ReviewFeedback.Id, value is corresponding ReviewFeedback entity
            Map<Long, ReviewFeedback> reviewFeedbacksMap = new HashMap<Long, ReviewFeedback>();

            while (rs.next()) {
                long reviewFeedbackId = rs.getLong("review_feedback_id");

                ReviewFeedback entity = reviewFeedbacksMap.get(reviewFeedbackId);
                if (entity == null) {
                    entity = populateReviewFeedback(rs, projectId, reviewFeedbackId);

                    reviewFeedbacksMap.put(reviewFeedbackId, entity);
                }

                if (rs.getObject("reviewer_user_id") != null) {
                    // Create ReviewFeedbackDetail
                    entity.getDetails().add(populateReviewFeedbackDetail(rs));
                }
            }

            List<ReviewFeedback> result = new ArrayList<ReviewFeedback>(reviewFeedbacksMap.values());
            // Return constructed entity:
            logExit(signature, new Object[] {result});

            return result;
        } catch (DBConnectionException e) {
            throw logException(signature, new ReviewFeedbackManagementPersistenceException(
                "Failed to create the connection '" + dbConnectionName + "'", e));
        } catch (SQLException e) {
            throw logException(signature, new ReviewFeedbackManagementPersistenceException(
                "Sql error occurs when trying to retrieving entities with given project ID from persistence", e));
        } finally {
            // Close the connection and prepared statement
            close(conn, stmt, signature);
        }
    }

    /**
     * <p>
     * Checks the validity of the entity.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Implementation and documentation are updated according to the new data model.</li>
     * </ol>
     * </p>
     *
     * @param entity
     *            the entity. It must be not null. Its Comment property but be not empty (but may be null). Its Details
     *            property must be not null (but may be empty) and each of its elements (if any) must conform to all of
     *            the following validation rules:
     *            <ol>
     *            <li>Must be not null.</li>
     *            <li>Its ReviewerUserId property must contain not more than 10 significant decimal digits and must be
     *            unique across all entity.Details elements.</li>
     *            <li>Its FeedbackText property must be not null and not empty.</li>
     *            </ol>
     *
     * @throws IllegalArgumentException
     *             if any argument is invalid (as per argument description above).
     */
    private static void checkEntity(ReviewFeedback entity) {
        checkNullIAE(entity, "entity");
        checkEmptyIAE(entity.getComment(), "entity.getComment");

        List<ReviewFeedbackDetail> details = entity.getDetails();
        checkNullIAE(details, "entity.getDetails()");

        Set<Long> reviewerUserIdSet = new HashSet<Long>();
        for (ReviewFeedbackDetail detail : details) {
            checkNullIAE(detail, "entity.getDetails()[i]");

            String feedbackText = detail.getFeedbackText();
            checkNullIAE(feedbackText, "entity.getDetails()[i].getFeedbackText()");
            checkEmptyIAE(feedbackText, "entity.getDetails()[i].getFeedbackText()");

            long reviewerUserId = detail.getReviewerUserId();
            if (String.valueOf(reviewerUserId).length() > USER_ID_MAX_LEN) {
                throw new IllegalArgumentException(
                    "entity.getDetails()[i].getReviewerUserId() property must contain not more than " + USER_ID_MAX_LEN
                        + " significant decimal digits");
            }
            if (reviewerUserIdSet.contains(reviewerUserId)) {
                throw new IllegalArgumentException("entity.getDetails()[i].getReviewerUserId() property"
                    + " must be unique across all entity.Details elements.");
            }
            reviewerUserIdSet.add(reviewerUserId);
        }
    }

    /**
     * Creates the review feedback detail.
     *
     * @param signature
     *            the signature
     * @param connection
     *            the connection
     * @param feedbackId
     *            the feedback id
     * @param details
     *            the details
     *
     * @throws SQLException
     *             if any error occurs
     *
     * @since 2.0
     *
     */
    private void createDetails(String signature, Connection connection, long feedbackId,
        List<ReviewFeedbackDetail> details) throws SQLException {
        if (!details.isEmpty()) {
            PreparedStatement stmt = connection.prepareStatement(CREATE_REVIEW_FEEDBACK_DETAIL);
            try {
                // Set feedback id
                stmt.setLong(1, feedbackId);
                for (ReviewFeedbackDetail detail : details) {
                    // Set detail specific parameters.
                    setParameters(stmt, 2, detail.getReviewerUserId(), detail.getScore(), detail.getFeedbackText());

                    // Execute
                    stmt.executeUpdate();
                }
            } finally {
                // Close the prepared statement
                close(null, stmt, signature);
            }
        }
    }

    /**
     * Updates the details.
     *
     * @param signature
     *            the signature
     * @param connection
     *            the connection
     * @param feedbackId
     *            the feedback ID
     * @param details
     *            the feedback details
     * @param createdDetails
     *            the created feedback details
     * @param updatedDetails
     *            the updated feedback details
     * @param deletedDetails
     *            the deleted feedback details
     *
     * @throws SQLException
     *             if any error occurs
     *
     * @since 2.0
     */
    private void updateDetails(String signature, Connection connection, long feedbackId,
        List<ReviewFeedbackDetail> details, List<ReviewFeedbackDetail> createdDetails,
        List<ReviewFeedbackDetail> updatedDetails, List<ReviewFeedbackDetail> deletedDetails) throws SQLException {

        // Retrieve reviewer user IDs
        Set<Long> oldReviewerUserIds = getReviewerIds(signature, connection, feedbackId);

        // Determine created and updated items.
        for (ReviewFeedbackDetail detail : details) {
            if (oldReviewerUserIds.remove(detail.getReviewerUserId())) {
                updatedDetails.add(detail);
            } else {
                createdDetails.add(detail);
            }
        }

        // Delete feedback detail
        if (!oldReviewerUserIds.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (long reviewerUserId : oldReviewerUserIds) {
                ReviewFeedbackDetail detail = new ReviewFeedbackDetail();
                detail.setReviewerUserId(reviewerUserId);
                deletedDetails.add(detail);

                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append("?");
            }
            String sql = String.format(DELETE_REVIEW_FEEDBACK_DETAIL, sb.toString());

            List<Object> parameters = new ArrayList<Object>();
            parameters.add(feedbackId);
            parameters.addAll(oldReviewerUserIds);

            executeUpdate(signature, connection, false, sql, parameters.toArray());
        }

        // Insert new items.
        createDetails(signature, connection, feedbackId, createdDetails);

        // Update items.
        if (!updatedDetails.isEmpty()) {
            PreparedStatement stmt = connection.prepareStatement(UPDATE_REVIEW_FEEDBACK_DETAIL);
            try {
                for (ReviewFeedbackDetail detail : updatedDetails) {
                    setParameters(stmt, 1, detail.getScore(), detail.getFeedbackText(), feedbackId,
                        detail.getReviewerUserId());

                    // Execute
                    stmt.executeUpdate();
                }
            } finally {
                // Close the prepared statement
                close(null, stmt, signature);
            }
        }
    }

    /**
     * Gets the reviewer user IDs.
     *
     * @param signature
     *            the signature
     * @param connection
     *            the connection
     * @param feedbackId
     *            the feedback ID
     *
     * @return the reviewer user IDs.
     *
     * @throws SQLException
     *             if any error occurs
     *
     * @since 2.0
     */
    private Set<Long> getReviewerIds(String signature, Connection connection, long feedbackId) throws SQLException {
        Set<Long> oldReviewerUserIds = new HashSet<Long>();
        // Retrieve reviewer user IDs.
        PreparedStatement stmt = connection.prepareStatement(SELECT_REVIEWER_ID_BY_FEEDBACK_ID);
        try {
            stmt.setLong(1, feedbackId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                oldReviewerUserIds.add(rs.getLong(1));
            }
        } finally {
            // Close the prepared statement
            close(null, stmt, signature);
        }

        return oldReviewerUserIds;
    }

    /**
     * Creates/updates record in review_feedback_audit table.
     *
     * @param signature
     *            the signature
     * @param connection
     *            DB connection. Must be not null. Not closed/committed/rolled back in this method.
     * @param entity
     *            Created/updated ReviewFeedback entity. Must be not null.
     * @param auditActionTypeId
     *            Audit action type ID.
     * @param operator
     *            Specifies user who is performing this operation. Must be not null and not empty.
     * @param actionDate
     *            Date and time when this operation is performed. Must be not null.
     *
     * @throws SQLException
     *             if any issue occurs with persistence.
     *
     * @since 2.0
     */
    private void auditReviewFeedback(String signature, Connection connection, ReviewFeedback entity,
        long auditActionTypeId, String operator, Timestamp actionDate) throws SQLException {
        // Create new record:
        PreparedStatement stmt = connection.prepareStatement(INSERT_REVIEW_FEEDBACK_AUDIT);
        try {
            // Populate parameters.
            setParameters(stmt, 1, entity.getId(), entity.getProjectId(), entity.getComment(), auditActionTypeId,
                operator, actionDate);

            // Execute
            stmt.executeUpdate();
        } finally {
            // Close the prepared statement
            close(null, stmt, signature);
        }
    }

    /**
     * Creates/updates records in review_feedback_detail_audit table.
     *
     * @param signature
     *            the signature
     * @param connection
     *            DB connection. Must be not null. Not closed/committed/rolled back in this method.
     * @param reviewFeedbackId
     *            ID of ReviewFeedback entity to which the given ReviewFeedbackDetail entities are associated.
     * @param details
     *            Created/updated/deleted ReviewFeedbackDetail entities. Must be not null and not contain null elements.
     *            Please note that in case of delete operation, the ReviewFeedbackDetail-s will have only the
     *            ReviewerUserId property populated.
     * @param auditActionTypeId
     *            Audit action type ID.
     * @param operator
     *            Specifies user who is performing this operation. Must be not null and not empty.
     * @param actionDate
     *            Date and time when this operation is performed. Must be not null.
     *
     * @throws SQLException
     *             if any issue occurs with persistence.
     *
     * @since 2.0
     */
    private void auditReviewFeedbackDetails(String signature, Connection connection, long reviewFeedbackId,
        List<ReviewFeedbackDetail> details, long auditActionTypeId, String operator, Timestamp actionDate)
        throws SQLException {
        if (details.isEmpty()) {
            return;
        }

        // Create new records:
        PreparedStatement stmt = connection.prepareStatement(INSERT_REVIEW_FEEDBACK_DETAIL_AUDIT);
        stmt.setLong(1, reviewFeedbackId);

        try {
            for (ReviewFeedbackDetail detail : details) {
                setParameters(stmt, 2, detail.getReviewerUserId(), detail.getScore(), detail.getFeedbackText(),
                    auditActionTypeId, operator, actionDate);

                // Execute
                stmt.executeUpdate();
            }
        } finally {
            // Close the prepared statement
            close(null, stmt, signature);
        }
    }

    /**
     * Executes the SQL.
     *
     * @param signature
     *            the signature
     * @param connection
     *            the connection
     * @param returnsId
     *            <code>true</code> indicating that generated key should be returned; <code>false</code> otherwise.
     * @param sql
     *            the SQL string
     * @param params
     *            the parameters
     *
     * @return the id or update count when returnsId is false
     *
     * @throws SQLException
     *             if a database access error occurs.
     *
     * @since 2.0
     */
    private long executeUpdate(String signature, Connection connection, boolean returnsId, String sql, Object... params)
        throws SQLException {
        PreparedStatement preparedStatement;
        if (returnsId) {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            preparedStatement = connection.prepareStatement(sql);
        }
        try {
            // Set parameters
            setParameters(preparedStatement, 1, params);

            // Execute
            int count = preparedStatement.executeUpdate();

            if (returnsId) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();

                return resultSet.getLong(1);
            }

            return count;
        } finally {
            // Close the prepared statement
            close(null, preparedStatement, signature);
        }
    }

    /**
     * Sets the parameters.
     *
     * @param preparedStatement
     *            the prepared statement
     * @param parameters
     *            the parameters
     * @param startIndex
     *            the start index
     *
     * @throws SQLException
     *             if any error occurs
     *
     * @since 2.0
     */
    private static void setParameters(PreparedStatement preparedStatement, int startIndex, Object... parameters)
        throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            preparedStatement.setObject(startIndex++, parameters[i]);
        }
    }

    /**
     * <p>
     * Create ReviewFeedbackDetail entity.
     * </p>
     *
     * @param rs
     *            the result set
     * @return the ReviewFeedbackDetail entity
     * @throws SQLException
     *             if error occurs when creating the ReviewFeedbackDetail entity
     *
     * @since 2.0
     */
    private ReviewFeedbackDetail populateReviewFeedbackDetail(ResultSet rs) throws SQLException {
        ReviewFeedbackDetail detail = new ReviewFeedbackDetail();
        detail.setReviewerUserId(rs.getLong("reviewer_user_id"));
        detail.setScore(rs.getInt("score"));
        detail.setFeedbackText(rs.getString("feedback_text"));

        return detail;
    }

    /**
     * <p>
     * Create ReviewFeedback entity.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Implementation is updated according to the new data model.</li>
     * </ol>
     * </p>
     *
     * @param rs
     *            the result set
     * @param projectId
     *            the project id
     * @param reviewFeedbackId
     *            the review feedback id
     *
     * @return the ReviewFeedback entity
     *
     * @throws SQLException
     *             if error occurs when creating the ReviewFeedback entity
     */
    private ReviewFeedback populateReviewFeedback(ResultSet rs, long projectId, long reviewFeedbackId)
        throws SQLException {
        // Create ReviewFeedback entity
        ReviewFeedback newEntity = new ReviewFeedback();
        newEntity.setId(reviewFeedbackId);
        newEntity.setProjectId(projectId);
        newEntity.setComment(rs.getString("comment"));
        newEntity.setCreateUser(rs.getString("create_user"));
        newEntity.setCreateDate(rs.getTimestamp("create_date"));
        newEntity.setModifyUser(rs.getString("modify_user"));
        newEntity.setModifyDate(rs.getTimestamp("modify_date"));
        newEntity.setDetails(new ArrayList<ReviewFeedbackDetail>());

        return newEntity;
    }

    /**
     * <p>
     * Rolls back the transaction.
     * </p>
     *
     * @param conn
     *            the connection
     * @param signature
     *            the method signature
     *
     * @since 2.0
     */
    private void rollback(Connection conn, String signature) {
        try {
            conn.rollback();
        } catch (SQLException e) {
            logException(signature, e);
        }
    }

    /**
     * <p>
     * Closes the statement and the connection.
     * </p>
     *
     * @param conn
     *            the connection
     * @param stmt
     *            the statement
     * @param signature
     *            the method signature
     */
    private void close(Connection conn, PreparedStatement stmt, String signature) {
        if (null != stmt) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logException(signature, e);
            }
        }
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                logException(signature, e);
            }
        }
    }

    /**
     * Loads configuration (as ConfigurationObject instance) from specified configuration file (or default file
     * ConfigurationFileManager.DEFAULT_CONFIG_PATH if no configuration file is specified) using specified configuration
     * namespace (or default DEFAULT_CONFIGURATION_NAMESPACE from this class if configuration namespace is not
     * specified).
     *
     * @param configurationNamespace
     *            Configuration namespace. If null, default value will be used. Must be non-empty string (if not null).
     * @param configurationFilename
     *            Configuration filename. If null, default value will be used. Must be non-empty string (if not null).
     * @return the configuration object
     * @throws IllegalArgumentException
     *             If any argument is invalid (as per argument description above).
     * @throws ReviewFeedbackManagementConfigurationException
     *             If any error occurs.
     */
    private static ConfigurationObject loadConfiguration(String configurationFilename, String configurationNamespace) {
        checkEmptyIAE(configurationFilename, "configurationFilename");
        checkEmptyIAE(configurationNamespace, "configurationNamespace");
        try {
            // Create configuration file manager:
            ConfigurationFileManager cfgMgr = (configurationFilename != null) ? new ConfigurationFileManager(
                configurationFilename) : new ConfigurationFileManager();
            // Load configuration from file via created configuration file manager
            ConfigurationObject cfgObject = cfgMgr
                .getConfiguration((configurationNamespace != null) ? configurationNamespace
                    : DEFAULT_CONFIGURATION_NAMESPACE);
            // Return configuration object
            return cfgObject;
        } catch (ConfigurationParserException e) {
            throw new ReviewFeedbackManagementConfigurationException("Failed to parser the configuration file", e);
        } catch (NamespaceConflictException e) {
            throw new ReviewFeedbackManagementConfigurationException("The namespace conflicts with another namespace",
                e);
        } catch (UnrecognizedFileTypeException e) {
            throw new ReviewFeedbackManagementConfigurationException(
                "The type of the configuration file is not recognized.", e);
        } catch (IOException e) {
            throw new ReviewFeedbackManagementConfigurationException(
                "I/O error occurs when loading the configuration.", e);
        } catch (UnrecognizedNamespaceException e) {
            throw new ReviewFeedbackManagementConfigurationException("The namespace is not recognized.", e);
        }
    }

    /**
     * Checks the audit action type id.
     *
     * @param auditActionTypeId
     *            the audit action type id.
     * @param defaultValue
     *            the default value
     *
     * @return the audit action type id or the default value if the id is null.
     *
     * @since 2.0
     */
    private static long checkAuditActionTypeId(Long auditActionTypeId, long defaultValue) {
        return (auditActionTypeId == null) ? defaultValue : auditActionTypeId;
    }

    /**
     * <p>
     * Checks whether the given String is empty.
     * </p>
     *
     * @param arg
     *            the String to check (can be null)
     * @param name
     *            the name of the String argument to check
     *
     * @throws IllegalArgumentException
     *             if the given string is empty
     */
    private static void checkEmptyIAE(String arg, String name) {
        if (arg != null && arg.trim().length() == 0) {
            throw new IllegalArgumentException(name + " should not be empty.");
        }
    }

    /**
     * <p>
     * Checks whether the given object is null.
     * </p>
     *
     * @param arg
     *            the object to check
     * @param name
     *            the name of the object argument to check
     *
     * @throws IllegalArgumentException
     *             if the given object is null
     */
    private static void checkNullIAE(Object arg, String name) {
        if (arg == null) {
            throw new IllegalArgumentException(name + " should not be null.");
        }
    }

    /**
     * <p>
     * Checks whether the given String is empty.
     * </p>
     *
     * @param arg
     *            the String to check (can be null)
     * @param name
     *            the name of the String argument to check
     *
     * @throws ReviewFeedbackManagementConfigurationException
     *             if the given string is empty
     */
    private static void checkEmptyCFE(String arg, String name) {
        if (arg != null && arg.trim().length() == 0) {
            throw new ReviewFeedbackManagementConfigurationException(name + " should not be empty.");
        }
    }

    /**
     * <p>
     * Checks whether the given object is null.
     * </p>
     *
     * @param arg
     *            the object to check
     * @param name
     *            the name of the object argument to check
     *
     * @throws ReviewFeedbackManagementConfigurationException
     *             if the given object is null
     */
    private static void checkNullCFE(Object arg, String name) {
        if (arg == null) {
            throw new ReviewFeedbackManagementConfigurationException(name + " should not be null.");
        }
    }

    /**
     * <p>
     * Logs for entrance into public method and parameters at <code>DEBUG</code> level.
     * </p>
     *
     * <p>
     * <em>NOTE:</em> Logging is NOT performed if log is <code>null</code>.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated to use toString(Object) method.</li>
     * </ol>
     * </p>
     *
     * @param signature
     *            the signature of the method to log.
     * @param paramNames
     *            the names of parameters to log.
     * @param paramValues
     *            the values of parameters to log.
     */
    private void logEntrance(String signature, String[] paramNames, Object[] paramValues) {
        if (log != null) {
            DateFormat format = new SimpleDateFormat(DEFAULT_LOGDATE_FORMAT);

            StringBuilder sb = new StringBuilder();
            // Log parameters
            sb.append("Input {");
            for (int i = 0; i < paramNames.length; i++) {
                sb.append(paramNames[i]).append(":").append(toString(paramValues[i]));
            }
            sb.append("}");

            log.log(Level.DEBUG, String.format(MESSAGE_ENTRANCE, format.format(new Date()), signature, sb.toString()));
        }
    }

    /**
     * <p>
     * Logs for exit from public method and return value at <code>DEBUG</code> level.
     * </p>
     *
     * <p>
     * <em>NOTE:</em> Logging is NOT performed if log is <code>null</code>.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated to use toString(Object) method.</li>
     * </ol>
     * </p>
     *
     * @param signature
     *            the signature of the method to log.
     * @param value
     *            the return value to log.
     */
    private void logExit(String signature, Object[] value) {
        if (log != null) {
            DateFormat format = new SimpleDateFormat(DEFAULT_LOGDATE_FORMAT);

            // Log return value
            String output = "Output: " + toString(value[0]);

            log.log(Level.DEBUG, String.format(MESSAGE_EXIT, format.format(new Date()), signature, output));
        }
    }

    /**
     * <p>
     * Logs the given exception and message at <code>ERROR</code> level.
     * </p>
     *
     * <p>
     * <em>NOTE:</em> Logging is NOT performed if log is <code>null</code>.
     * </p>
     *
     * @param <T>
     *            the exception type.
     * @param signature
     *            the signature of the method to log.
     * @param e
     *            the exception to log.
     *
     * @return the passed in exception.
     */
    private <T extends Throwable> T logException(String signature, T e) {
        if (log != null) {
            // Log exception at ERROR level
            DateFormat format = new SimpleDateFormat(DEFAULT_LOGDATE_FORMAT);
            log.log(Level.ERROR, String.format(MESSAGE_ERROR, format.format(new Date()), signature, getStackTrace(e)));
        }

        return e;
    }

    /**
     * <p>
     * Returns the exception stack trace string.
     * </p>
     *
     * @param cause
     *            the exception to be recorded.
     *
     * @return the exception stack trace string.
     */
    private static String getStackTrace(Throwable cause) {
        OutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);

        // Print a new line
        ps.println();
        cause.printStackTrace(ps);

        return out.toString();
    }

    /**
     * <p>
     * Converts an object to a string.
     * </p>
     *
     * @param obj
     *            the object
     *
     * @return the converted string.
     *
     * @since 2.0
     */
    private static String toString(Object obj) {
        if (obj instanceof ReviewFeedbackDetail) {
            return toString((ReviewFeedbackDetail) obj);
        } else if (obj instanceof ReviewFeedback) {
            return toString((ReviewFeedback) obj);
        } else if (obj instanceof List<?>) {
            return toString((List<?>) obj);
        }

        return String.valueOf(obj);
    }

    /**
     * <p>
     * Converts a list of objects to a string.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Changed List&lt;ReviewFeedback&gt; to List&lt;?&gt;.</li>
     * </ol>
     * </p>
     *
     * @param list
     *            a list of objects.
     *
     * @return the converted string.
     */
    private static String toString(List<?> list) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Object element : list) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(toString(element));
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * <p>
     * Converts the ReviewFeedback object to a string.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Updated according to the new data model.</li>
     * </ol>
     * </p>
     *
     * @param obj
     *            the ReviewFeedback object
     *
     * @return the converted string.
     */
    private static String toString(ReviewFeedback obj) {
        StringBuilder sb = new StringBuilder();

        sb.append("ReviewFeedback{id:").append(obj.getId()).append(", createUser:").append(obj.getCreateUser())
            .append(", createDate:").append(obj.getCreateDate()).append(", modifyUser:").append(obj.getModifyUser())
            .append(", modifyDate:").append(obj.getModifyDate()).append(", projectid:").append(obj.getProjectId())
            .append(", comment:").append(obj.getComment()).append(", details:")
            .append(toString((Object) obj.getDetails())).append("}");

        return sb.toString();
    }

    /**
     * <p>
     * Converts the ReviewFeedback object to a string.
     * </p>
     *
     * @param obj
     *            the ReviewFeedbackDetail object
     *
     * @return the converted string.
     *
     * @since 2.0
     */
    private static String toString(ReviewFeedbackDetail obj) {
        StringBuilder sb = new StringBuilder();

        sb.append("ReviewFeedbackDetail{reviewerUserId:").append(obj.getReviewerUserId()).append(", score:")
            .append(obj.getScore()).append(", feedbackText:").append(obj.getFeedbackText()).append("}");

        return sb.toString();
    }
}
