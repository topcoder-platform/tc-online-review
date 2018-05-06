/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.algorithm;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.db.connectionfactory.*;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.Helper;
import com.topcoder.management.review.assignment.ReviewAssignmentAlgorithm;
import com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException;
import com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException;
import com.topcoder.util.errorhandling.ExceptionUtils;
import com.topcoder.util.log.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * <p>
 * This is a stochastic implementation of the review assignment algorithm. The probability of an outcome
 * (i.e. assignment) is based on reviewer ratings. This implementation prevents possible collisions between
 * members who are to be separated.
 * </p>
 * <p>
 * It generates all possible assignments via brute force and for each possible distinct set of user IDs evaluates
 * the probability of this set being chosen and then randomly chooses one set of user IDs. For the chosen set of
 * user IDs the algorithm returns the "best" assignment, i.e. the one that assigns higher rated reviewers to
 * more important application roles.
 * </p>
 * <p>
 * This class performs the logging of errors and debug information using Logging Wrapper component.
 * </p>
 * <p>
 * Thread Safety: This class is mutable and not thread-safe.
 * </p>
 *
 * @author VolodymyrK
 * @version 1.0
 */
public class RandomizedRatingBasedReviewAssignmentAlgorithm implements ReviewAssignmentAlgorithm {
    /**
     * <p>
     * Represents the name of this class used for logging.
     * </p>
     */
    private static final String CLASS_NAME = RandomizedRatingBasedReviewAssignmentAlgorithm.class.getName();

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
     * <p>
     * The logger used by this class for logging errors and debug information.
     * </p>
     * <p>
     * Is initialized in the {@link #configure(com.topcoder.configuration.ConfigurationObject)} method and never changed after that.
     * </p>
     * <p>
     * If is null after initialization, logging is not performed. Is used in business methods.
     * </p>
     */
    private Log log;

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
     * Maps review application role IDs to review applications. key - review application role ID,
     * value - review applications for that role
     * </p>
     * <p>
     * It is only used in assign method to run recursive function. It should never be used except assign and
     * its recursive function doit method.
     * </p>
     */
    private Map<Long, List<ReviewApplication>> applicationsByRoleId = null;

    /**
     * <p>
     * List of ReviewApplicationRoles like ("Primary Reviewer", "Secondary Reviewer") that have open positions.
     * </p>
     */
    private List<ReviewApplicationRole> openRoles = null;

    /**
     * <p>
     * Set of approved review applications.
     * </p>
     * <p>
     * It is only used in assign method to run recursive function. It should never be used except assign and
     * its recursive function doit method.
     * </p>
     */
    Set<ReviewApplication> currentAssignment = null;

    /**
     * <p>
     * Contains IDs of users who have been assigned to some role.
     * </p>
     * <p>
     * It is only used in assign method to run recursive function. It should never be used except assign and
     * its recursive function doit method.
     * </p>
     */
    private Set<Long> assignedUserIds = null;

    /**
     * <p>
     * List of sets of user IDs representing groups of members who are to be separated.
     * </p>
     * <p>
     * It's mutable by {@link #readDataFromDB} method and then used by
     * {@link #filterCollisions} method. After population by {@link #readDataFromDB} method, it will be not null, will
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
     * It's mutable by {@link #readDataFromDB} method and then used by
     * {@link #assign} method. After population by {@link #readDataFromDB} method, it will be not null, will
     * not contain nulls.
     * </p>
     */
    private Set<Long> excludedReviewers;

    /**
     * <p>
     * Represents the base of the weight exponential function used to determine probabilities.
     * </p>
     * <p>
     * It will be initialized in {@link #configure(ConfigurationObject)} method and never changed after that.
     * Cannot be negative after initialization.
     * </p>
     */
    private Double expBase;

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
     * Key - user ID, value - rating of that user. It's mutable by {@link #readDataFromDB} method and then used by
     * {@link #assign} method. After population by {@link #readDataFromDB} method, it will be not null, will
     * not contain null key/value.
     * </p>
     */
    private Map<Long, Double> ratingPerUser;

    /**
     * <p>
     * Represents the possible outcomes for the stochastic selection. The key is set of user IDs whose applications
     * are to be approved. The value is the "best" assignment for that set of user IDs.
     * </p>
     */
    private Map<Set<Long>, Set<ReviewApplication>> outcomes = null;

    /**
     * Create an instance of RandomizedRatingBasedReviewAssignmentAlgorithm.
     */
    public RandomizedRatingBasedReviewAssignmentAlgorithm() {
    }

    /**
     * Assigns applicants to roles using a brute force search to generate all possible outcomes and then randomly
     * selects one outcome based on their probability. Assumptions and constraints:
     * <p>
     * <ol>
     * <li>user can't be assigned to more than 1 role position.</li>
     * <li>user can't be assigned to a role if it's in a collaborating group with one of the existing registrant or
     * reviewer.</li>
     * <li>two or more users can't be assigned if they are in the same collaborating group.</li>
     * <li>outcomes that are subset of other valid outcomes are not considered, meaning that algorithm tries to
     * assign as many reviewers as possible.</li>
     * </ol>
     * </p>
     *
     * @param reviewAuction
     *            Review auction.
     * @param reviewApplications
     *            Review applications.
     * @return Set of approved review applications.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or reviewApplications contains null or property is not set
     *             correctly.
     * @throws com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException
     *             If any error occurs.
     */
    public Set<ReviewApplication> assign(ReviewAuction reviewAuction,
        List<ReviewApplication> reviewApplications) throws ReviewAssignmentAlgorithmException {
        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME + ".assign(ReviewAuction, List<ReviewApplication>)";
        Helper.logEntrance(this.getLog(), signature, new String[] { "reviewAuction", "reviewApplications" },
            new Object[] { reviewAuction, reviewApplications });

        Helper.checkNullIAE(this.getLog(), signature, reviewAuction, "reviewAuction");
        Helper.checkListIAE(this.getLog(), signature, reviewApplications, "reviewApplications");

        Helper.checkNullIAE(this.getLog(), signature, reviewAuction.getAuctionType(), "reviewAuction|AuctionType");
        Helper.checkNullIAE(this.getLog(), signature, reviewAuction.getAuctionType().getApplicationRoles(),
            "reviewAuction|AuctionType|ApplicationRoles");

        // Read reviewer ratings and collaborating groups
        readDataFromDB(reviewAuction, reviewApplications);

        openRoles = new ArrayList<ReviewApplicationRole>();

        // key - review application role ID, value - review applications for that role
        applicationsByRoleId = new HashMap<Long, List<ReviewApplication>>();

        List<ReviewApplicationRole> roles = reviewAuction.getAuctionType().getApplicationRoles();
        for (int i = 0; i < roles.size(); i++) {
            if (reviewAuction.getOpenPositions().get(i) > 0) {
                Helper.checkNullIAE(this.getLog(), signature, roles.get(i),
                    "reviewAuction|AuctionType|ApplicationRoles|element");

                applicationsByRoleId.put(roles.get(i).getId(), new ArrayList<ReviewApplication>());

                for (int j = 0; j < reviewAuction.getOpenPositions().get(i); j++) {
                    openRoles.add(roles.get(i)); // here it is should be i, not j
                }
            }
        }
        for (ReviewApplication reviewApplication : reviewApplications) {
            List<ReviewApplication> apps = applicationsByRoleId.get(reviewApplication.getApplicationRoleId());
            // Exclude review applications from excluded users.
            if (!excludedReviewers.contains(reviewApplication.getUserId()) && apps != null) {
                apps.add(reviewApplication);
            }
        }

        // this set will contain IDs of user who have been assigned to some role
        assignedUserIds = new HashSet<Long>();

        // Perform brute force search to generate all possible outcomes.
        currentAssignment = new HashSet<ReviewApplication>();
        outcomes = new HashMap<Set<Long>, Set<ReviewApplication>>();
        doit(0);

        // Remove outcomes with collaborating members.
        filterCollisions();

        // Remove outcomes that are subsets of other valid outcomes.
        filterSubsets();

        // Compute total weight of all valid outcomes.
        double totalWeight = 0.0;
        for(Set<Long> userIds : outcomes.keySet()) {
            totalWeight += getWeight(userIds);
        }

        // Select one outcome randomly based on their weights.
        double rand = Math.random() * totalWeight;
        double tmpWeight = 0.0;
        Set<ReviewApplication> ret = new HashSet<ReviewApplication>();
        for(Set<Long> userIds : outcomes.keySet()) {
            tmpWeight += getWeight(userIds);
            if (rand < tmpWeight) {
                ret = outcomes.get(userIds);
                break;
            }
        }

        Helper.logExit(this.getLog(), signature, ret, start);
        return ret;
    }

    /**
     * Recursive method that generates all possible assignments.
     *
     * @param i
     *            The role's index in the openRoles list that is to be assigned on this iteration.
     * @throws com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException
     *             If any error occurs in measure quality.
     */
    private void doit(int i) throws ReviewAssignmentAlgorithmException {
        if (i == openRoles.size()) {
            // Brute force attempt completed. Save the outcome.
            if (!outcomes.containsKey(assignedUserIds)) {
                outcomes.put(new HashSet<Long>(assignedUserIds), new HashSet<ReviewApplication>(currentAssignment));
            } else {
                // Check if the new assignment is "better" than the existing one and if yes, save it for this outcome.
                if (compareAssignments(currentAssignment, outcomes.get(assignedUserIds)) > 0) {
                    outcomes.put(new HashSet<Long>(assignedUserIds), new HashSet<ReviewApplication>(currentAssignment));
                }
            }
            return;
        }

        // Try to leave the current role empty.
        doit(i + 1);

        // Try each available reviewer for the current role.
        for (ReviewApplication reviewApplication : applicationsByRoleId.get(openRoles.get(i).getId())) {
            if (!assignedUserIds.contains(reviewApplication.getUserId())) {
                // Assign the found reviewer.
                assignedUserIds.add(reviewApplication.getUserId());
                currentAssignment.add(reviewApplication);

                // Go further in the brute force.
                doit(i + 1);

                // Rollback the assignment attempt.
                currentAssignment.remove(reviewApplication);
                assignedUserIds.remove(reviewApplication.getUserId());
            }
        }
    }

    /**
     * Compares two assignments and returns 1 if the first assignment is "better", -1 if the
     * second assignment is "better" and 0 otherwise.
     * The "better" assignment is the one that assigns higher rated reviewers to more important roles. The importance of
     * roles is based on their order (the first role is the most important).
     *
     * @param a1
     *            First assignment.
     * @param a2
     *            Second assignment.
     * @return 1 if the first assignment is "better", -1 if the second assignment is "better" and 0 otherwise.
     */
    private int compareAssignments(Set<ReviewApplication> a1, Set<ReviewApplication> a2) {
        // Iterate over the list of open roles (which are already ordered by their importance) and for each role
        // compute the total sum of ratings of all reviewers assigned to that role for each of the two assignments.
        // The loop stops as soon as the two sums are different for any role.
        for(ReviewApplicationRole role : openRoles) {
            double sum1 = 0.0, sum2 = 0.0;
            for(ReviewApplication application : a1) {
                if (application.getApplicationRoleId() == role.getId()) {
                    sum1 += getRating(application.getUserId());
                }
            }
            for(ReviewApplication application : a2) {
                if (application.getApplicationRoleId() == role.getId()) {
                    sum2 += getRating(application.getUserId());
                }
            }
            if (sum1 > sum2) {
                return 1;
            } else if (sum2 > sum1) {
                return -1;
            }
        }
        
        return 0;
    }

    /**
     * Removes the outcomes having two or more assigned users that belong to at least one collaborating group.
     */
    private void filterCollisions() {
        Iterator<Map.Entry<Set<Long>, Set<ReviewApplication>>> it = outcomes.entrySet().iterator();
        for(;it.hasNext();) {
            Set<Long> userIds = it.next().getKey();
            for (Set<Long> group : memberGroups) {
                int count = 0;
                for(Long userId : userIds) {
                    if (group.contains(userId)) {
                        count++;
                    }
                }
                // If two or more users belong to the same group, remove the outcome.
                if (count > 1) {
                    it.remove();
                    break;
                }
            }
        }
    }

    /**
     * Removes the outcomes that are subsets of some other valid outcomes. For example, if there is an outcome that
     * assigns users A and B, and an outcome that assigns users A, B and C, the first outcome will be removed because
     * it's a subset of the second one.
     */
    private void filterSubsets() {
        Iterator<Map.Entry<Set<Long>, Set<ReviewApplication>>> it = outcomes.entrySet().iterator();
        for(;it.hasNext();) {
            Set<Long> set1 = it.next().getKey();
            // Iterate over all other outcomes to find out if any of them is a super-set to this one.
            for(Set<Long> set2 : outcomes.keySet()) {
                // If the size of set2 is equal to the size of set1, set1 isn't a subset of set2 unless the two
                // sets are equal.
                if (set2.size() <= set1.size()) {
                    continue;
                }
                boolean subset = true;
                for(Long userId : set1) {
                    if (!set2.contains(userId)) {
                        subset = false;
                        break;
                    }
                }
                if (subset) {
                    it.remove();
                    break;
                }
            }
        }
    }

    /**
     * This method returns the weight of the given outcome (i.e. the set of selected reviewers).
     * The weights are proportional to the probability of selecting this outcome.
     * The weight of an outcome is computed as a multiplication of individual weight of each user. Weight of each user
     * is computed as an exponential function depending on the user's reviewer rating.
     *
     * @param userIds
     *            Set of user IDs.
     * @return Weight for this outcome.
     */
    private double getWeight(Set<Long> userIds) {
        double weight = 1.0;
        for(Long userId : userIds) {
            weight *= Math.pow(expBase, getRating(userId));
        }
        return weight;
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
     * This method reads all necessary data from the DB before doing the brute-force search.
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
     */
    protected void readDataFromDB(ReviewAuction reviewAuction, List<ReviewApplication> reviewApplications)
            throws ReviewAssignmentAlgorithmException {
        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME + ".readDataFromDB(ReviewAuction, List<ReviewApplication>)";
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

        // Calculate review rating for each applicant
        ratingPerUser = new HashMap<Long, Double>(); // key - user ID, value - rating of that user

        // Create connection
        Connection connection = createConnection();
        PreparedStatement stmtGroups = null, stmtResources = null, stmtRatings = null;
        ResultSet rsGroups = null, rsResources = null, rsRatings = null;
        try {
            Map<Long, Set<Long>> membersMap = new HashMap<Long, Set<Long>>();
            stmtGroups = connection.prepareStatement(
                    "SELECT collaborating_group_id, user_id FROM collaborating_members");
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

            stmtResources = connection.prepareStatement(
                    "SELECT ri.value::int as user_id " +
                    "FROM resource r " +
                    "INNER JOIN resource_info ri ON ri.resource_id = r.resource_id and ri.resource_info_type_id = 1 " +
                    "WHERE r.resource_role_id in (1,4,5,6,7) and r.project_id = ?");
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

            // Fetch reviewer ratings
            stmtRatings = connection.prepareStatement(
                    "SELECT rr.user_id, rr.review_date, rr.rating " +
                    "FROM reviewer_rating rr " +
                    "INNER JOIN project p ON rr.project_id = p.project_id " +
                    "INNER JOIN project ref_p ON ref_p.project_id = ? AND " +
                    "                            ref_p.project_category_id = p.project_category_id " +
                    "ORDER BY rr.user_id, rr.review_date");
            stmtRatings.setLong(1, reviewAuction.getProjectId());

            // Execute the statement
            rsRatings = stmtRatings.executeQuery();
            while (rsRatings.next()) {
                // There can be multiple records for the same user but since the records are ordered by the
                // review date in SQL, the last rating value saved to the map will be the current user's rating.
                ratingPerUser.put(rsRatings.getLong("user_id"), rsRatings.getDouble("rating"));
            }

        } catch (SQLException sqle) {
            throw new ReviewAssignmentAlgorithmException(
                    "Fail to read from the DB due to SQLException.", sqle);
        } finally {
            // Release the resource.
            Helper.closeResultSet(this.getLog(), signature, rsGroups);
            Helper.closeResultSet(this.getLog(), signature, rsResources);
            Helper.closeResultSet(this.getLog(), signature, rsRatings);
            Helper.closeStatement(this.getLog(), signature, stmtGroups);
            Helper.closeStatement(this.getLog(), signature, stmtResources);
            Helper.closeStatement(this.getLog(), signature, stmtRatings);
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
     * @throws com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException
     *             If some error occurred when initializing an instance using the given configuration.
     */
    public void configure(ConfigurationObject config) {
        ExceptionUtils.checkNull(config, null, null, "The parameter 'config' should not be null.");

        // create log
        this.log = Helper.getLog(config);

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

        String expBaseStr = Helper.getPropertyValue(config, "expBase", false);
        if (null == expBaseStr) {
            this.expBase = 1.25;
        } else {
            try {
                this.expBase = Double.parseDouble(expBaseStr);
            } catch (NumberFormatException e) {
                throw new ReviewAssignmentConfigurationException(
                        "expBase for configuration can not be parsed.", e);
            }
            if (expBase <= 1.0) {
                throw new ReviewAssignmentConfigurationException("expBase should be greater than 1.0.");
            }
        }
    }

    /**
     * Gets logger used by this class for logging errors and debug information.
     *
     * @return the logger used by this class for logging errors and debug information.
     */
    protected Log getLog() {
        return this.log;
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
