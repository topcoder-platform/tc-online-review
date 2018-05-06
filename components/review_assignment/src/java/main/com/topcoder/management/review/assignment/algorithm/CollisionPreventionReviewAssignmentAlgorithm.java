/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.algorithm;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.db.connectionfactory.*;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.Helper;
import com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException;
import com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException;
import com.topcoder.util.objectfactory.ObjectFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * <p>
 * This class is an implementation of a review assignment algorithm. It's responsible for generating an
 * assignment of review applications to review roles which prevents possible collisions between members who are to be
 * separated. For the allowed combination of reviewers the algorithm uses another assignment algorithm instance
 * for computing the assignment quality.
 * </p>
 * <p>
 * This class extends MaxMetricReviewAssignmentAlgorithm, overrides {@link #prepare} method to retrieve
 * groups of users that are to be separated from each other and {@link #measureQuality} method to calculate
 * the assignment quality.
 * </p>
 * <p>
 * This class performs the logging of errors and debug information using Logging Wrapper component.
 * </p>
 *
 * <p>
 * Thread Safety: This class is mutable and not thread-safe.
 * </p>
 *
 * @author VolodymyrK
 * @version 1.1.0
 */
public class CollisionPreventionReviewAssignmentAlgorithm extends MaxMetricReviewAssignmentAlgorithm {

    /**
     * <p>
     * Represents the name of this class used for logging.
     * </p>
     */
    private static final String CLASS_NAME = CollisionPreventionReviewAssignmentAlgorithm.class.getName();

    /**
     * <p>
     * Represents the child key 'dbConnectionFactoryConfig'.
     * </p>
     */
    private static final String KEY_DBCF_CONFIG = "dbConnectionFactoryConfig";

    /**
     * <p>
     * Represents &quot;baseReviewAssignmentAlgorithmKey&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_BASE_REVIEW_ASS_ALGO = "baseReviewAssignmentAlgorithmKey";

    /**
     * <p>
     * Represents &quot;baseReviewAssignmentAlgorithmConfig&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_BASE_REVIEW_ASS_ALGO_CONFIG = "baseReviewAssignmentAlgorithmConfig";

    /**
     * <p>
     * Represents the property key 'connectionName'.
     * </p>
     */
    private static final String KEY_CONN_NAME = "connectionName";

    /**
     * Selects all groups of members who are to be separated.
     */
    private static final String SQL_SELECT_MEMBER_GROUPS =
            "SELECT collaborating_group_id, user_id FROM collaborating_members";

    /**
     * Selects user IDs of all existing registrants and reviewers for the project.
     */
    private static final String SQL_SELECT_EXISTING_RESOURCES =
            "SELECT ri.value::int as user_id " +
            "FROM resource r " +
            "INNER JOIN resource_info ri ON ri.resource_id = r.resource_id and ri.resource_info_type_id = 1 " +
            "WHERE r.resource_role_id in (1,4,5,6,7) and r.project_id = ?";

    /**
     * <p>
     * Represents the database connection factory to be used.
     * </p>
     * <p>
     * Valid values: non-null. It will be initialized in {@link #configure(com.topcoder.configuration.ConfigurationObject)} method and
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
     * initialized in {@link #configure(com.topcoder.configuration.ConfigurationObject)} method and never changed after that. Cannot be
     * empty after initialization.
     * </p>
     */
    private String connectionName;

    /**
     * <p>
     * Represents the underlying review assignment algorithm implementation.
     * </p>
     * <p>
     * It will be initialized in {@link #configure(com.topcoder.configuration.ConfigurationObject)} method and never changed after that.
     * Cannot be negative after initialization.
     * </p>
     */
    private MaxMetricReviewAssignmentAlgorithm baseReviewAssignmentAlgorithm;

    /**
     * <p>
     * List of sets of user IDs representing groups of members who are to be separated.
     * </p>
     * <p>
     * It's mutable by {@link #prepare} method and then used by
     * {@link #measureQuality} method. After population by {@link #prepare} method, it will be not null, will
     * not contain nulls.
     * </p>
     */
    private List<Set<Long>> memberGroups;

    /**
     * <p>
     * Set of user IDs of those reviewers who cannot review this project because they are in the same group with
     * one of the existing project registrants or reviewers.
     * </p>
     * <p>
     * It's mutable by {@link #prepare} method and then used by
     * {@link #measureQuality} method. After population by {@link #prepare} method, it will be not null, will
     * not contain nulls.
     * </p>
     */
    private Set<Long> excludedReviewers;
    
    /**
     * Create an instance of CollisionPreventionReviewAssignmentAlgorithm.
     */
    public CollisionPreventionReviewAssignmentAlgorithm() {
    }

    /**
     * This method measures the quality of the given assignment.
     * If the given assignment creates a collision it will return a negative metric value, otherwise it will redirect
     * the call to the underlying algorithm implementation.
     *
     * @param assignment
     *            Set of approved review applications.
     * @return Quality of the given assignment.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or assignment contains null key or property is not set or retrieved
     *             rating is null.
     * @throws com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException
     *             If any error occurs.
     */
    protected double measureQuality(Set<ReviewApplication> assignment) throws ReviewAssignmentAlgorithmException {
        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME
            + ".measureQuality(Map<ReviewApplication, ReviewApplicationRole>)";
        Helper.logEntrance(this.getLog(), signature, new String[] { "assignment" },
            new Object[] { assignment });

        Helper.checkNullIAE(this.getLog(), signature, assignment, "assignment");

        List<Long> assignedUserIds = new ArrayList<Long>();
        for (ReviewApplication reviewApplication : assignment) {
            Helper.checkNullIAE(this.getLog(), signature, reviewApplication, "assignment|key");
            assignedUserIds.add(reviewApplication.getUserId());
        }

        double quality = 0;
        for (int i=0;i<assignedUserIds.size() && quality==0;i++) {
            if (excludedReviewers.contains(assignedUserIds.get(i))) {
                quality = -1;
                break;
            }

            for (Set<Long> group : memberGroups) {
                if (!group.contains(assignedUserIds.get(i))) {
                    continue;
                }
                for (int j=i+1;j<assignedUserIds.size() && quality==0;j++) {
                    if (group.contains(assignedUserIds.get(j))) {
                        quality = -1;
                        break;
                    }
                }
            }
        }
        
        if (quality==0) {
            quality = baseReviewAssignmentAlgorithm.measureQuality(assignment);
        }

        Helper.logExit(this.getLog(), signature, quality, start);
        return quality;
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
     * @throws com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException
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

        memberGroups = new ArrayList<Set<Long>>();
        excludedReviewers = new HashSet<Long>();

        if (reviewApplications.size() == 0) {
            Helper.logExit(this.getLog(), signature, null, start);
            return;
        }

        // Create connection
        Connection connection = createConnection();
        PreparedStatement stmtGroups = null, stmtResources = null;
        ResultSet rsGroups = null, rsResources = null;
        try {
            Map<Long, Set<Long>> membersMap = new HashMap<Long, Set<Long>>();
            stmtGroups = connection.prepareStatement(SQL_SELECT_MEMBER_GROUPS);
            rsGroups = stmtGroups.executeQuery();
            while (rsGroups.next()) {
                long groupId = rsGroups.getLong("collaborating_group_id");
                long userId = rsGroups.getLong("user_id");

                if (!membersMap.containsKey(groupId)) {
                    membersMap.put(groupId, new HashSet<Long>());
                }
                membersMap.get(groupId).add(userId);
            }
            memberGroups.addAll(membersMap.values());

            stmtResources = connection.prepareStatement(SQL_SELECT_EXISTING_RESOURCES);
            stmtResources.setLong(1, reviewAuction.getProjectId());
            rsResources = stmtResources.executeQuery();
            while (rsResources.next()) {
                long userId = rsResources.getLong("user_id");
                for (Set<Long> group : memberGroups) {
                    if (group.contains(userId)) {
                        for (Long otherUserId : group) {
                            if (otherUserId != userId) {
                                excludedReviewers.add(otherUserId);
                            }
                        }
                    }
                }
            }

        } catch (SQLException sqle) {
            throw new ReviewAssignmentAlgorithmException(
                "Fail to create the db Connection object due to SQLException.", sqle);
        } finally {
            // Release the resource.
            Helper.closeResultSet(this.getLog(), signature, rsGroups);
            Helper.closeResultSet(this.getLog(), signature, rsResources);
            Helper.closeStatement(this.getLog(), signature, stmtGroups);
            Helper.closeStatement(this.getLog(), signature, stmtResources);
            Helper.closeConnection(this.getLog(), signature, connection);
        }

        baseReviewAssignmentAlgorithm.prepare(reviewAuction, reviewApplications);

        Helper.logExit(this.getLog(), signature, null, start);
    }

    /**
     * This method performs all the necessary finalization operations after the all calls of measureQuality()
     * method are done. It will be called after brute force ends.
     *
     * @param reviewApplications
     *            Review applications.
     * @param finalAssignment
     *            Final assignment (the assignment finally chosen by assign() method).
     *            Set of approved review applications.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or reviewApplications contains null or finalAssignment contains
     *             null key.
     * @throws ReviewAssignmentAlgorithmException
     *             If any error occurs.
     */
    protected void finalize(List<ReviewApplication> reviewApplications,
                            Set<ReviewApplication> finalAssignment)
            throws ReviewAssignmentAlgorithmException {
        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME
                + ".finalize(List<ReviewApplication>, Map<ReviewApplication, ReviewApplicationRole>)";
        Helper.logEntrance(this.getLog(), signature, new String[] { "reviewApplications",
                "finalAssignment" }, new Object[] { reviewApplications, finalAssignment });

        baseReviewAssignmentAlgorithm.finalize(reviewApplications, finalAssignment);

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
     * @throws com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException
     *             If some error occurred when initializing an instance using the given configuration.
     */
    public void configure(ConfigurationObject config) {
        super.configure(config);

        // Create database connection factory using the extracted configuration
        this.dbConnectionFactory = createDBConnectionFactory(config);

        // Get connection name
        this.connectionName = Helper.getPropertyValue(config, KEY_CONN_NAME, false);

        // Create object factory
        ObjectFactory objectFactory = Helper.createObjectFactory(config);

        // Create the underlying review assignment algorithm instance
        this.baseReviewAssignmentAlgorithm = Helper.createObject(config, objectFactory, KEY_BASE_REVIEW_ASS_ALGO,
                MaxMetricReviewAssignmentAlgorithm.class);

        // Configure the underlying review assignment algorithm instance
        ConfigurationObject reviewAssignmentAlgorithmConfig = Helper.getChildConfig(config,
                KEY_BASE_REVIEW_ASS_ALGO_CONFIG);
        this.baseReviewAssignmentAlgorithm.configure(reviewAssignmentAlgorithmConfig);
    }

    /**
     * Creates <code>DBConnectionFactory</code> from configuration.
     *
     * @param config
     *            the configuration object.
     * @return the created <code>DBConnectionFactory</code> instance.
     * @throws com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException
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
     * @throws com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException
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
