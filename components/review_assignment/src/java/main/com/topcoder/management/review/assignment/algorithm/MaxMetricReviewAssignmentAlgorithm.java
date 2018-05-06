/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.Helper;
import com.topcoder.management.review.assignment.ReviewAssignmentAlgorithm;
import com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException;
import com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException;
import com.topcoder.util.errorhandling.ExceptionUtils;
import com.topcoder.util.log.Log;

/**
 * <p>
 * This is a base class for implementations of a review assignment algorithm that use metric maximization
 * approach to determine the "best" assignment. The search is brute force based.
 * The metric implementation is left for the sub classes.
 * </p>
 * <p>
 * It generates all possible assignments via brute force, lets subclass to measure the "quality" of assignment
 * via protected measureQuality() method, and picks the best assignment.
 * </p>
 * <p>
 * This class performs the logging of errors and debug information using Logging Wrapper component.
 * </p>
 * <p>
 * Thread Safety: This class is mutable and not thread-safe.
 * </p>
 *
 * @author gevak, zhongqiangzhang
 * @version 1.0
 */
public abstract class MaxMetricReviewAssignmentAlgorithm implements ReviewAssignmentAlgorithm {
    /**
     * <p>
     * Represents the name of this class used for logging.
     * </p>
     */
    private static final String CLASS_NAME = MaxMetricReviewAssignmentAlgorithm.class.getName();

    /**
     * <p>
     * The logger used by this class for logging errors and debug information.
     * </p>
     * <p>
     * Is initialized in the {@link #configure(ConfigurationObject)} method and never changed after that.
     * </p>
     * <p>
     * If is null after initialization, logging is not performed. Is used in business methods.
     * </p>
     */
    private Log log;

    /**
     * <p>
     * Set of approved review applications.
     * </p>
     * <p>
     * It is only used in assign method to run recursive function. It should never be used except in the assign
     * method and its recursive doit method.
     * </p>
     */
    private Set<ReviewApplication> assignment = null;

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
     * List of ReviewApplicationRoles like "Primary Reviewer", "Secondary Reviewer".
     * </p>
     * <p>
     * It is only used in assign method to run recursive function. It should never be used except assign and
     * its recursive function doit method.
     * </p>
     */
    private List<ReviewApplicationRole> openRoles = null;

    /**
     * <p>
     * Represents the best assignment found by the brute force so far.
     * </p>
     * <p>
     * It is only used in assign method to run recursive function. It should never be used except assign and
     * its recursive function doit method.
     * </p>
     */
    private Set<ReviewApplication> bestAssignment = null;

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
     * The maximum quality found by brute force so far.
     * </p>
     * <p>
     * It is only used in assign method to run recursive function. It should never be used expect assign and
     * its recursive function doit method.
     * </p>
     */
    private double maxQuality;

    /**
     * Create an instance of MaxMetricReviewAssignmentAlgorithm.
     */
    protected MaxMetricReviewAssignmentAlgorithm() {
    }

    /**
     * Assigns applicants to roles using a brute force search. Assumptions and constraints:
     * <p>
     * <ol>
     * <li>
     * user can't be assigned to more than 1 role position.</li>
     * <li>
     * Algorithm tries to find an assignment with maximum "quality" (which is measured via measureQuality() method,
     * which must be implemented in subclass).</li>
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
     * @throws ReviewAssignmentAlgorithmException
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

        // Let subclass prepare.
        prepare(reviewAuction, reviewApplications);

        openRoles = new ArrayList<ReviewApplicationRole>();

        // key - review application role ID, value - review applications for that role
        applicationsByRoleId = new HashMap<Long, List<ReviewApplication>>();

        // this set will contain IDs of user who have been assigned to some role
        assignedUserIds = new HashSet<Long>();

        maxQuality = 0;

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
            if (apps != null) {
                apps.add(reviewApplication);
            }
        }

        // Best assignment found yet by brute force.
        bestAssignment = new HashSet<ReviewApplication>();

        // Perform brute force search.
        assignment = new HashSet<ReviewApplication>();
        doit(0);

        // Let subclass finalize.
        finalize(reviewApplications, bestAssignment);

        Helper.logExit(this.getLog(), signature, bestAssignment, start);
        return bestAssignment;
    }

    /**
     * Recursive method that generates all possible assignments and finds the one with the best "quality".
     *
     * @param i
     *            The role's index in the openRoles list that is to be assigned on this iteration.
     * @throws ReviewAssignmentAlgorithmException
     *             If any error occurs in measure quality.
     */
    private void doit(int i) throws ReviewAssignmentAlgorithmException {
        if (i == openRoles.size()) {
            // Brute force attempt completed. Estimate the outcome.
            double curQuality = measureQuality(assignment);
            if (curQuality > maxQuality) {
                maxQuality = curQuality;
                bestAssignment = new HashSet<ReviewApplication>(assignment);
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
                assignment.add(reviewApplication);

                // Go further in the brute force.
                doit(i + 1);

                // Rollback the assignment attempt.
                assignment.remove(reviewApplication);
                assignedUserIds.remove(reviewApplication.getUserId());
            }
        }
    }

    /**
     * This method measures the quality of the given assignment.
     *
     * @param assignment
     *            Set of approved review applications.
     * @return Quality of the given assignment.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or assignment contains null key.
     * @throws ReviewAssignmentAlgorithmException
     *             If any error occurs.
     */
    protected abstract double measureQuality(Set<ReviewApplication> assignment) throws ReviewAssignmentAlgorithmException;

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
     */
    protected void prepare(ReviewAuction reviewAuction, List<ReviewApplication> reviewApplications)
        throws ReviewAssignmentAlgorithmException {
        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME + ".prepare(ReviewAuction, List<ReviewApplication>)";
        Helper.logEntrance(this.getLog(), signature, new String[] { "reviewAuction", "reviewApplications" },
            new Object[] { reviewAuction, reviewApplications });

        Helper.checkNullIAE(this.getLog(), signature, reviewAuction, "reviewAuction");
        Helper.checkListIAE(this.getLog(), signature, reviewApplications, "reviewApplications");

        // do nothing here.

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

        Helper.checkListIAE(this.getLog(), signature, reviewApplications, "reviewApplications");
        Helper.checkListIAE(this.getLog(), signature, finalAssignment, "finalAssignment|key");

        // do nothing here.

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
        ExceptionUtils.checkNull(config, null, null, "The parameter 'config' should not be null.");

        // create log
        this.log = Helper.getLog(config);
    }

    /**
     * Gets logger used by this class for logging errors and debug information.
     *
     * @return the logger used by this class for logging errors and debug information.
     */
    protected Log getLog() {
        return this.log;
    }
}
