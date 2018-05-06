/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.algorithm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.db.connectionfactory.ConfigurationException;
import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.Helper;
import com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException;
import com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException;

/**
 * <p>
 * This class is an implementation of a review assignment algorithm. It's responsible for generating an
 * assignment (ideally an optimal assignment) of review applications to review roles.
 * </p>
 * <p>
 * This class extends MaxMetricReviewAssignmentAlgorithm, overrides {@link #prepare} method to retrieve
 * user ratings beforehand and {@link #measureQuality} method to calculate the weighted sum of ratings of assigned
 * reviewers. Thus, this class allows to find the assignment with maximum weighted sum of ratings of the assigned
 * reviewers.
 * </p>
 * <p>
 * This class performs the logging of errors and debug information using Logging Wrapper component.
 * </p>
 *
 * <p>
 * <em>Sample Configuration:</em>
 *
 * <pre>
 * &lt;?xml version="1.0"?&gt;
 * &lt;CMConfig&gt;
 * &lt;Config name="com.topcoder.management.review.assignment.algorithm.MaxSumOfRatingReviewAssignmentAlgorithm"&gt;
 * &lt;Property name="loggerName"&gt;
 * &lt;Value&gt;myLogger&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="connectionName"&gt;
 * &lt;Value&gt;informix_connection&lt;/Value&gt;
 * &lt;/Property&gt;
 *
 * &lt;Property name="dbConnectionFactoryConfig"&gt;
 * &lt;Property name="com.topcoder.db.connectionfactory.DBConnectionFactoryImpl"&gt;
 * &lt;Property name="connections"&gt;
 * &lt;Property name="informix_connection"&gt;
 * &lt;Property name="producer"&gt;
 * &lt;Value&gt;com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="parameters"&gt;
 * &lt;Property name="jdbc_driver"&gt;
 * &lt;Value&gt;com.informix.jdbc.IfxDriver&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="jdbc_url"&gt;
 * &lt;Value&gt;jdbc:informix-sqli://10.120.33.158:9088/tcs_catalog:informixserver=ol_informix1170&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="user"&gt;
 * &lt;Value&gt;informix&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="password"&gt;
 * &lt;Value&gt;topcoder&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 *
 * &lt;Property name="defaultRating"&gt;
 * &lt;Value&gt;100.0&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;/Config&gt;
 * &lt;/CMConfig&gt;
 * </pre>
 *
 * </p>
 *
 * <p>
 * Thread Safety: This class is mutable and not thread-safe.
 * </p>
 *
 * @author gevak, zhongqiangzhang, VolodymyrK
 * @version 1.1.0
 */
public class MaxSumOfRatingReviewAssignmentAlgorithm extends MaxMetricReviewAssignmentAlgorithm {

    /**
     * <p>
     * Represents the name of this class used for logging.
     * </p>
     */
    private static final String CLASS_NAME = MaxSumOfRatingReviewAssignmentAlgorithm.class.getName();

    /**
     * <p>
     * Represents the child key 'dbConnectionFactoryConfig'.
     * </p>
     */
    private static final String KEY_DBCF_CONFIG = "dbConnectionFactoryConfig";

    /**
     * <p>
     * Represents the property key 'connectionName'.
     * </p>
     */
    private static final String KEY_CONN_NAME = "connectionName";

    /**
     * Selects the reviewer ratings for all users and all past projects in the track.
     */
    private static final String SQL_SELECT_REVIEWER_RATINGS =
            "SELECT rr.user_id, rr.review_date, rr.rating " +
            "FROM reviewer_rating rr " +
            "INNER JOIN project p ON rr.project_id = p.project_id " +
            "INNER JOIN project ref_p ON ref_p.project_id = ? AND ref_p.project_category_id = p.project_category_id " +
            "ORDER BY rr.user_id, rr.review_date";

    /**
     * <p>
     * Represents the database connection factory to be used.
     * </p>
     * <p>
     * Valid values: non-null. It will be initialized in {@link #configure(ConfigurationObject)} method and
     * never changed after that. Cannot be null after initialization. It will be used in assign() method.
     * </p>
     */
    private DBConnectionFactory dbConnectionFactory;

    /**
     * <p>
     * Represents the connection name.
     * </p>
     * <p>
     * Valid values: non-empty string. Could be null, if null the default connection will be used. It will be
     * initialized in {@link #configure(ConfigurationObject)} method and never changed after that. Cannot be
     * empty after initialization.
     * </p>
     */
    private String connectionName;

    /**
     * <p>
     * Represents the default review rating.
     * </p>
     * <p>
     * It will be initialized in {@link #configure(ConfigurationObject)} method and never changed after that.
     * Cannot be negative after initialization.
     * </p>
     */
    private Double defaultRating;

    /**
     * <p>
     * Contains ratings per each user.
     * </p>
     * <p>
     * Key - user ID, value - rating of that user. It's mutable by {@link #prepare} method and then used by
     * {@link #measureQuality} method. After population by {@link #prepare} method, it will be not null, will
     * not contain null key/value.
     * </p>
     */
    private Map<Long, Double> ratingPerUser;

    /**
     * <p>
     * Contains weights associated with review application roles.
     * </p>
     * <p>
     * Key - review application role ID, value - weight coefficient for that role.
     * It's mutable by {@link #prepare} method and then used by
     * {@link #measureQuality} method. After population by {@link #prepare} method, it will be not null, will
     * not contain null key/value.
     * </p>
     */
    private Map<Long, Double> roleWeights;
    
    /**
     * Create an instance of MaxSumOfRatingReviewAssignmentAlgorithm.
     */
    public MaxSumOfRatingReviewAssignmentAlgorithm() {
    }

    /**
     * This method measures the quality of the given assignment.
     *
     * @param assignment
     *            Set of approved review applications.
     * @return Quality of the given assignment.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or assignment contains null key or property is not set or retrieved
     *             rating is null.
     * @throws ReviewAssignmentAlgorithmException
     *             If any error occurs.
     */
    protected double measureQuality(Set<ReviewApplication> assignment) throws ReviewAssignmentAlgorithmException {

        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME
            + ".measureQuality(Map<ReviewApplication, ReviewApplicationRole>)";
        Helper.logEntrance(this.getLog(), signature, new String[] { "assignment" },
            new Object[] { assignment });

        Helper.checkNullIAE(this.getLog(), signature, assignment, "assignment");

        // Calculate and return the weighted sum of ratings of the assigned reviewers.
        double sumOfRatings = 0;
        for (ReviewApplication reviewApplication : assignment) {
            Helper.checkNullIAE(this.getLog(), signature, reviewApplication, "assignment|key");

            sumOfRatings += getRating(reviewApplication.getUserId()) *
                    roleWeights.get(reviewApplication.getApplicationRoleId());
        }

        Helper.logExit(this.getLog(), signature, sumOfRatings, start);
        return sumOfRatings;
    }

    /**
     * This method returns user's reviewer rating.
     *
     * @param userId
     *            User ID.
     * @return User's reviewer rating.
     */
    private double getRating(Long userId) {
        return ratingPerUser.containsKey(userId) ? ratingPerUser.get(userId) : defaultRating;
    }
    
    /**
     * This method performs all the necessary preparations before the calls of measureQuality() method. It
     * will be called before brute force begins.
     *
     * @param reviewAuction
     *            Review auction.
     * @param reviewApplications
     *            Review applications.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or reviewApplications contains null.
     * @throws ReviewAssignmentAlgorithmException
     *             If any error occurs.
     *
     */
    protected void prepare(ReviewAuction reviewAuction, List<ReviewApplication> reviewApplications)
        throws ReviewAssignmentAlgorithmException {

        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME + ".prepare(ReviewAuction, List<ReviewApplication>)";
        Helper.logEntrance(this.getLog(), signature, new String[] { "reviewAuction", "reviewApplications" },
            new Object[] { reviewAuction, reviewApplications });

        Helper.checkNullIAE(this.getLog(), signature, reviewAuction, "reviewAuction");
        Helper.checkListIAE(this.getLog(), signature, reviewApplications, "reviewApplications|Key");

        // Assign role weights.
        roleWeights = new HashMap<Long, Double>();
        for(int i=0;i<reviewAuction.getAuctionType().getApplicationRoles().size();i++) {
            ReviewApplicationRole role = reviewAuction.getAuctionType().getApplicationRoles().get(i);

            // Role weights are set such that they favor assignments with higher-rated reviewers occupying
            // more important roles (which are ordered by the role order_index) among the assignments with equal ratings sum.
            // Still, the role weights are almost equal so that assignments with larger ratings sum will prevail.
            roleWeights.put(role.getId(), 1.0 - i/10000.0);
        }

        // Calculate review rating for each applicant
        ratingPerUser = new HashMap<Long, Double>(); // key - user ID, value - rating of that user

        if (reviewApplications.size() == 0) {
            Helper.logExit(this.getLog(), signature, null, start);
            return;
        }

        // Create connection
        Connection connection = createConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create a prepared statement
            preparedStatement = connection.prepareStatement(SQL_SELECT_REVIEWER_RATINGS);
            preparedStatement.setLong(1, reviewAuction.getProjectId());

            // Execute the statement
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // There can be multiple records for the same user but since the records are ordered by the
                // review date in SQL, the last rating value saved to the map will be the current user's rating.
                ratingPerUser.put(resultSet.getLong("user_id"), resultSet.getDouble("rating"));
            }
        } catch (SQLException sqle) {
            throw new ReviewAssignmentAlgorithmException(
                "Fail to create the db Connection object due to SQLException.", sqle);
        } finally {
            // Release the resource.
            Helper.closeResultSet(this.getLog(), signature, resultSet);
            Helper.closeStatement(this.getLog(), signature, preparedStatement);
            Helper.closeConnection(this.getLog(), signature, connection);
        }
        
        Helper.logExit(this.getLog(), signature, null, start);
    }

    /**
     * Configures this instance with use of the given configuration object.
     *
     * @param config
     *            the configuration object
     *
     * @throws IllegalArgumentException
     *             If argument is null.
     * @throws ReviewAssignmentConfigurationException
     *             If some error occurred when initializing an instance using the given configuration.
     */
    public void configure(ConfigurationObject config) {
        super.configure(config);

        // Create database connection factory using the extracted configuration
        this.dbConnectionFactory = createDBConnectionFactory(config);

        // Get connection name
        this.connectionName = Helper.getPropertyValue(config, KEY_CONN_NAME, false);

        String defaultRatingStr = Helper.getPropertyValue(config, "defaultRating", false);
        if (null == defaultRatingStr) {
            this.defaultRating = 100.0;
        } else {
            try {
                this.defaultRating = Double.parseDouble(defaultRatingStr);
            } catch (NumberFormatException e) {
                throw new ReviewAssignmentConfigurationException(
                    "defaultRating for configuration can not be parsed.", e);
            }
            if (defaultRating < 0) {
                throw new ReviewAssignmentConfigurationException("defaultRating should not be negative.");
            }
        }
    }

    /**
     * Creates <code>DBConnectionFactory</code> from configuration.
     *
     * @param config
     *            the configuration object.
     * @return the created <code>DBConnectionFactory</code> instance.
     * @throws ReviewAssignmentConfigurationException
     *             if any error occurs when creating <code>DBConnectionFactory</code>.
     */
    private static DBConnectionFactory createDBConnectionFactory(ConfigurationObject config) {
        ConfigurationObject dbConnectionFactoryConfig = Helper.getChildConfig(config, KEY_DBCF_CONFIG);

        // Create database connection factory using the extracted configuration
        try {
            return new DBConnectionFactoryImpl(dbConnectionFactoryConfig);
        } catch (UnknownConnectionException e) {
            throw new ReviewAssignmentConfigurationException("Fails to create database connection factory.", e);
        } catch (ConfigurationException e) {
            throw new ReviewAssignmentConfigurationException("Fails to create database connection factory.", e);
        }
    }

    /**
     * <p>
     * Creates a database Connection object.
     * </p>
     *
     * @return the created database Connection object.
     *
     * @throws ReviewAssignmentAlgorithmException
     *             if fail to create the database Connection object.
     */
    private Connection createConnection() throws ReviewAssignmentAlgorithmException {
        Connection connection;

        try {
            if (connectionName == null) {
                // if the connName does not exist, use default to get the connection.
                connection = dbConnectionFactory.createConnection();
            } else {
                // use connName to get the connection.
                connection = dbConnectionFactory.createConnection(connectionName);
            }
            return connection;
        } catch (DBConnectionException dbe) {
            throw new ReviewAssignmentAlgorithmException(
                "Fail to create the db Connection object due to DBConnectionException.", dbe);
        }
    }
}
