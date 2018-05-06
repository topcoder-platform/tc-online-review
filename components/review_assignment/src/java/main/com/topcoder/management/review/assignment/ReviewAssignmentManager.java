/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationManager;
import com.topcoder.management.review.application.ReviewApplicationManagerException;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewApplicationStatus;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.application.ReviewAuctionCategory;
import com.topcoder.management.review.application.ReviewAuctionManager;
import com.topcoder.management.review.application.ReviewAuctionManagerException;
import com.topcoder.management.review.application.search.ReviewApplicationFilterBuilder;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.util.errorhandling.ExceptionUtils;
import com.topcoder.util.log.Log;
import com.topcoder.util.objectfactory.ObjectFactory;

/**
 * <p>
 * This class provides a programmatic API for review assignment.
 * </p>
 * <p>
 * It provides just a single {@link #execute()} method that uses pluggable ReviewAssignmentAlgorithm,
 * ReviewAssignmentProjectManager and ReviewAssignmentNotificationManager instances to perform various steps
 * of the assignment procedure.
 * </p>
 * <p>
 * This class performs the logging of errors and debug information using Logging Wrapper component.
 * </p>
 *
 * <p>
 * <em>Sample Configuration:</em> Refer to ReviewAssignmentManager.xml for more detail.
 * </p>
 * <p>
 * <em>Sample Code:</em>
 *
 * <pre>
 * // How to execute assignment manager.
 * ConfigurationObject ramConfig = getConfigurationObject(&quot;config/ReviewAssignmentManager.xml&quot;,
 *     ReviewAssignmentManager.class.getName());
 *
 * ReviewAssignmentManager ram = new ReviewAssignmentManager(ramConfig);
 * ram.execute();
 * </pre>
 *
 * </p>
 *
 * <p>
 * Thread Safety: This class is immutable, but not thread safe since it uses ReviewAssignmentAlgorithm,
 * ReviewAssignmentProjectManager and ReviewAssignmentNotificationManager instances that are not guaranteed to
 * be thread safe.
 * </p>
 *
 * @author gevak, zhongqiangzhang
 * @version 1.0
 */
public class ReviewAssignmentManager {
    /**
     * <p>
     * Represents the name of this class used for logging.
     * </p>
     */
    private static final String CLASS_NAME = ReviewAssignmentManager.class.getName();

    /**
     * <p>
     * Represents &quot;reviewApplicationManagerKey&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_REVIEW_APP_MANAGER = "reviewApplicationManagerKey";

    /**
     * <p>
     * Represents &quot;reviewAuctionManagerKey&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_REVIEW_AUC_MANAGER = "reviewAuctionManagerKey";

    /**
     * <p>
     * Represents &quot;projectManagerKey&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_PROJECT_MANAGER = "projectManagerKey";

    /**
     * <p>
     * Represents &quot;reviewAssignmentAlgorithmKey&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_REVIEW_ASS_ALGO = "reviewAssignmentAlgorithmKey";

    /**
     * <p>
     * Represents &quot;reviewAssignmentProjectManagerKey&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_REVIEW_ASS_PROJECT_MANAGER = "reviewAssignmentProjectManagerKey";

    /**
     * <p>
     * Represents &quot;reviewAssignmentNotificationManagerKey&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_REVIEW_ASS_NOTIFICATION_MANAGER = "reviewAssignmentNotificationManagerKey";

    /**
     * <p>
     * Represents &quot;reviewAssignmentAlgorithmConfig&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_REVIEW_ASS_ALGO_CONFIG = "reviewAssignmentAlgorithmConfig";

    /**
     * <p>
     * Represents &quot;reviewAssignmentProjectManagerConfig&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_REVIEW_ASS_PROJECT_MANAGER_CONFIG = "reviewAssignmentProjectManagerConfig";

    /**
     * <p>
     * Represents &quot;reviewAssignmentNotificationManagerConfig&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_REVIEW_ASS_NOTIFICATION_MANAGER_CONFIG =
        "reviewAssignmentNotificationManagerConfig";

    /**
     * <p>
     * Represents &quot;pendingReviewApplicationStatusId&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_PENDING_REVIEW_APP_STATUS_ID = "pendingReviewApplicationStatusId";

    /**
     * <p>
     * Represents &quot;approvedReviewApplicationStatusId&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_APPROVED_REVIEW_APP_STATUS_ID = "approvedReviewApplicationStatusId";

    /**
     * <p>
     * Represents &quot;rejectedReviewApplicationStatusId&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_REJECTED_REVIEW_APP_STATUS_ID = "rejectedReviewApplicationStatusId";

    /**
     * <p>
     * "Pending" ReviewApplicationStatus.
     * </p>
     * <p>
     * Is initialized in constructor and never changed after that. Cannot be null after initialization. Is
     * used in {@link #execute()} method.
     * </p>
     */
    private final ReviewApplicationStatus pendingReviewApplicationStatus;

    /**
     * <p>
     * "Approved" ReviewApplicationStatus.
     * </p>
     * <p>
     * Is initialized in constructor and never changed after that. Cannot be null after initialization. Is
     * used in {@link #execute()} method.
     * </p>
     */
    private final ReviewApplicationStatus approvedReviewApplicationStatus;

    /**
     * <p>
     * "Rejected" ReviewApplicationStatus.
     * </p>
     * <p>
     * Is initialized in constructor and never changed after that. Cannot be null after initialization. Is
     * used in {@link #execute()} method.
     * </p>
     */
    private final ReviewApplicationStatus rejectedReviewApplicationStatus;

    /**
     * <p>
     * The logger used by this class for logging errors and debug information.
     * </p>
     * <p>
     * Is initialized in the constructor and never changed after that. If is null, logging is not performed.
     * Is used in {@link #execute()}.
     * </p>
     */
    private final Log log;

    /**
     * <p>
     * Manager for managing review auctions.
     * </p>
     * <p>
     * Is initialized in constructor and never changed after that. Cannot be null after initialization. Is
     * used in {@link #execute()} method.
     * </p>
     */
    private final ReviewAuctionManager reviewAuctionManager;

    /**
     * <p>
     * Manager for managing review applications.
     * </p>
     * <p>
     * Is initialized in constructor and never changed after that. Cannot be null after initialization. Is
     * used in {@link #execute()} method.
     * </p>
     */
    private final ReviewApplicationManager reviewApplicationManager;

    /**
     * <p>
     * Manager for managing projects.
     * </p>
     * <p>
     * Is initialized in constructor and never changed after that. Cannot be null after initialization. Is
     * used in {@link #execute()} method.
     * </p>
     */
    private final ProjectManager projectManager;

    /**
     * <p>
     * Algorithm of review assignment.
     * </p>
     * <p>
     * Is initialized in constructor and never changed after that. Cannot be null after initialization. Is
     * used in {@link #execute()} method.
     * </p>
     */
    private final ReviewAssignmentAlgorithm reviewAssignmentAlgorithm;

    /**
     * <p>
     * Manager for managing review assignment projects.
     * </p>
     * <p>
     * Is initialized in constructor and never changed after that. Cannot be null after initialization. Is
     * used in {@link #execute()} method.
     * </p>
     */
    private final ReviewAssignmentProjectManager reviewAssignmentProjectManager;

    /**
     * <p>
     * Manager for sending notifications.
     * </p>
     * <p>
     * Is initialized in constructor and never changed after that. Cannot be null after initialization. Is
     * used in {@link #execute()} method.
     * </p>
     */
    private final ReviewAssignmentNotificationManager reviewAssignmentNotificationManager;

    /**
     * <p>
     * Creates an instance of ReviewAssignmentManager.
     * </p>
     * <p>
     * See section 3.2 of CS for details about the configuration parameters. Please use value constraints
     * provided in that section to check whether values read from configuration object are valid.
     * </p>
     *
     * @param config
     *            - the configuration object
     *
     * @throws IllegalArgumentException
     *             if config is null.
     * @throws ReviewAssignmentConfigurationException
     *             if some error occurred when initializing an instance using the given configuration.
     *
     */
    public ReviewAssignmentManager(ConfigurationObject config) {
        ExceptionUtils.checkNull(config, null, null, "The parameter 'config' should not be null.");

        // create log
        this.log = Helper.getLog(config);

        // create object factory
        ObjectFactory objectFactory = Helper.createObjectFactory(config);

        this.reviewApplicationManager = Helper.createObject(config, objectFactory, KEY_REVIEW_APP_MANAGER,
            ReviewApplicationManager.class);

        this.reviewAuctionManager = Helper.createObject(config, objectFactory, KEY_REVIEW_AUC_MANAGER,
            ReviewAuctionManager.class);

        this.projectManager = Helper.createObject(config, objectFactory, KEY_PROJECT_MANAGER,
            ProjectManager.class);

        this.reviewAssignmentAlgorithm = Helper.createObject(config, objectFactory, KEY_REVIEW_ASS_ALGO,
            ReviewAssignmentAlgorithm.class);

        this.reviewAssignmentProjectManager = Helper.createObject(config, objectFactory,
            KEY_REVIEW_ASS_PROJECT_MANAGER, ReviewAssignmentProjectManager.class);
        this.reviewAssignmentNotificationManager = Helper.createObject(config, objectFactory,
            KEY_REVIEW_ASS_NOTIFICATION_MANAGER, ReviewAssignmentNotificationManager.class);

        ConfigurationObject reviewAssignmentAlgorithmConfig = Helper.getChildConfig(config,
            KEY_REVIEW_ASS_ALGO_CONFIG);
        this.reviewAssignmentAlgorithm.configure(reviewAssignmentAlgorithmConfig);

        ConfigurationObject reviewAssignmentProjectManagerConfig = Helper.getChildConfig(config,
            KEY_REVIEW_ASS_PROJECT_MANAGER_CONFIG);
        this.reviewAssignmentProjectManager.configure(reviewAssignmentProjectManagerConfig);

        ConfigurationObject reviewAssignmentNotificationManagerConfig = Helper.getChildConfig(config,
            KEY_REVIEW_ASS_NOTIFICATION_MANAGER_CONFIG);
        this.reviewAssignmentNotificationManager.configure(reviewAssignmentNotificationManagerConfig);

        List<ReviewApplicationStatus> reviewApplicationStatuses;
        try {
            reviewApplicationStatuses = reviewApplicationManager.getApplicationStatuses();
        } catch (ReviewApplicationManagerException e) {
            throw new ReviewAssignmentConfigurationException("Fails on accessing configuration object.", e);
        }

        Long approvedId = getLongValue(config, KEY_APPROVED_REVIEW_APP_STATUS_ID);
        Long pendingId = getLongValue(config, KEY_PENDING_REVIEW_APP_STATUS_ID);
        Long rejectedId = getLongValue(config, KEY_REJECTED_REVIEW_APP_STATUS_ID);
        if (!isIdsUnique(approvedId, pendingId, rejectedId)) {
            throw new ReviewAssignmentConfigurationException("The status ids must be unique.");
        }

        this.approvedReviewApplicationStatus = getStatus(reviewApplicationStatuses, approvedId);
        this.pendingReviewApplicationStatus = getStatus(reviewApplicationStatuses, pendingId);
        this.rejectedReviewApplicationStatus = getStatus(reviewApplicationStatuses, rejectedId);
    }

    /**
     * <p>
     * Gets a Long property value from given configuration object.
     * </p>
     *
     * @param config
     *            the given configuration object.
     * @param key
     *            the property key.
     *
     * @return the retrieved Long property value .
     *
     * @throws ReviewAssignmentConfigurationException
     *             if the property is missing, not converting to Long.
     */
    private Long getLongValue(ConfigurationObject config, String key) {
        try {
            return Long.valueOf(Helper.getPropertyValue(config, key, true));
        } catch (NumberFormatException e) {
            throw new ReviewAssignmentConfigurationException(key + " should be converted to Long type.");
        }
    }

    /**
     * Check if the approvedId, pendingId and rejectedId are unique.
     *
     * @param approvedId
     *            the approved id.
     * @param pendingId
     *            the pending id.
     * @param rejectedId
     *            the rejected id.
     * @return true if all of them are unique, false otherwise.
     */
    private boolean isIdsUnique(Long approvedId, Long pendingId, Long rejectedId) {
        return (!approvedId.equals(pendingId)) && (!approvedId.equals(rejectedId))
            && (!pendingId.equals(rejectedId));
    }

    /**
     * Get the review application status by given id.
     *
     * @param reviewApplicationStatuses
     *            the review application statues.
     * @param id
     *            the review application status id.
     * @return the review application status
     */
    private ReviewApplicationStatus getStatus(List<ReviewApplicationStatus> reviewApplicationStatuses, Long id) {
        for (ReviewApplicationStatus status : reviewApplicationStatuses) {
            if (status.getId() == id) {
                return status;
            }
        }

        throw new ReviewAssignmentConfigurationException("Can't find review application status with id " + id);
    }

    /**
     * Executes a single review assignment operation.
     *
     * @throws ReviewAssignmentException
     *             - If any error occurs.
     *
     */
    public void execute() throws ReviewAssignmentException {
        long start = System.currentTimeMillis();
        final String signature = CLASS_NAME + ".execute()";
        Helper.logEntrance(log, signature, null, null);

        try {
            List<ReviewAuctionCategory> reviewAuctionCategories = reviewAuctionManager.getAuctionCategories();
            for (ReviewAuctionCategory reviewAuctionCategory : reviewAuctionCategories) {
                // Get the list of all open auctions for the current category
                List<ReviewAuction> reviewAuctions = reviewAuctionManager
                    .searchOpenAuctions(reviewAuctionCategory.getId());
                // For every open auction check if the assignment date has been already reached
                for (ReviewAuction reviewAuction : reviewAuctions) {
                    // if the assignment date is still in the future, skip this auction
                    if (new Date().compareTo(reviewAuction.getAssignmentDate()) >= 0) {

                        // Get the list of all pending review applications for the auction:
                        List<ReviewApplication> reviewApplications = reviewApplicationManager
                            .searchApplications(new AndFilter(ReviewApplicationFilterBuilder
                                .createAuctionIdFilter(reviewAuction.getId()), ReviewApplicationFilterBuilder
                                .createApplicationStatusIdFilter(pendingReviewApplicationStatus.getId())));

                        try {
                            // Continue working with current auction only if there
                            // is a pending review application for the auction
                            process(reviewAuction, reviewApplications);
                        } catch (ReviewAssignmentNotificationException e) {
                            // Log the exception and continue.
                            Helper.logException(log, signature, new ReviewAssignmentException(
                                "Error on sending notification: " + e.getMessage(), e));
                        }
                    }
                }
            }
        } catch (ReviewAuctionManagerException e) {
            throw Helper.logException(log, signature, new ReviewAssignmentException(
                "Error to search OpenAuctions for " + e.getMessage(), e));
        } catch (ReviewApplicationManagerException e) {
            throw Helper.logException(log, signature, new ReviewAssignmentException(
                "Error to search applications for " + e.getMessage(), e));
        } catch (NullPointerException e) {
            // If the necessary properties are lost.
            throw Helper.logException(log, signature, new ReviewAssignmentException(
                "Error to set property for " + e.getMessage(), e));
        } catch (PersistenceException e) {
            throw Helper.logException(log, signature, new ReviewAssignmentException(
                "Error to process auctions for " + e.getMessage(), e));
        }
        Helper.logExit(log, signature, null, start);
    }

    /**
     * Processing open auctions with pending review applications.
     *
     * @param reviewAuction
     *            the review auction.
     * @param reviewApplications
     *            the review applications.
     * @throws PersistenceException
     *             if error to get Project.
     * @throws ReviewAssignmentException
     *             if error to assign or add reviewer to project or to notify.
     * @throws ReviewApplicationManagerException
     *             if error to update application.
     */
    private void process(ReviewAuction reviewAuction, List<ReviewApplication> reviewApplications)
        throws PersistenceException, ReviewAssignmentException, ReviewApplicationManagerException {
        if (reviewApplications.size() > 0) {
            // calculate the total open review positions.
            long totalOpenPositions = 0;
            for (Long pos : reviewAuction.getOpenPositions()) {
                if (pos != null) {
                    totalOpenPositions += pos;
                }
            }
            Helper.logInfo(log, "Processing open auctions with pending review applications {"
                + "Auction ID [" + reviewAuction.getId() + "] Project ID [" + reviewAuction.getProjectId()
                + "] Total number of open review positions [" + totalOpenPositions
                + "] Total number of pending review applications [" + reviewApplications.size() + "]}.");

            // Get Project entity.
            Project project = projectManager.getProject(reviewAuction.getProjectId());

            // Call a pluggable strategy to assign review applicants to the review application roles
            Set<ReviewApplication> assignment = reviewAssignmentAlgorithm.assign(
                reviewAuction, reviewApplications);

            // log the assignment.
            for (ReviewApplication reviewApplication : assignment) {
                if (reviewApplication != null) {
                    // Get ReviewApplicationRole associated with this review application.
                    ReviewApplicationRole applicationRole = Helper.getReviewApplicationRoleByID(reviewAuction,
                            reviewApplication.getApplicationRoleId());

                    Helper.logInfo(log, "Application [" + reviewApplication.getId() + "] for user ["
                        + reviewApplication.getUserId() + "] and role ["
                        + applicationRole.getName() + "] is approved.");
                }
            }

            // Prepare a set of IDs of users who didn't get assigned to any review application role
            Set<Long> unassignedUserIds = new HashSet<Long>();
            for (ReviewApplication reviewApplication : reviewApplications) {
                unassignedUserIds.add(reviewApplication.getUserId());
            }
            // Add the members to the Online Review project
            reviewAssignmentProjectManager.addReviewersToProject(reviewAuction, project, assignment);

            boolean approvedApplicationExists = false;

            // For every review application that was assigned a review application role:
            for (ReviewApplication reviewApplication : assignment) {
                // Remove the applicant user from the set of unassigned users:
                unassignedUserIds.remove(reviewApplication.getUserId());

                // Update the review application status to "Approved":
                reviewApplication.setStatus(approvedReviewApplicationStatus);
                reviewApplicationManager.updateApplication(reviewApplication);

                approvedApplicationExists = true;
            }

            // For every review application that was not assigned a review application role, update the
            // review application status to "Rejected":
            for (ReviewApplication reviewApplication : reviewApplications) {
                if (reviewApplication.getStatus().getId() == pendingReviewApplicationStatus.getId()) {
                    reviewApplication.setStatus(rejectedReviewApplicationStatus);
                    reviewApplicationManager.updateApplication(reviewApplication);
                }
            }

            // Send notification emails to approved applicants:
            reviewAssignmentNotificationManager.notifyApprovedApplicants(reviewAuction, project, assignment);

            // For every distinct applicant that was not assigned to any review application role send a
            // notification email:
            reviewAssignmentNotificationManager.notifyRejectedApplicants(reviewAuction, project,
                new ArrayList<Long>(unassignedUserIds));

            if (approvedApplicationExists) {
                // Send a notification email to all PMs/copilots:
                reviewAssignmentNotificationManager.notifyManagers(reviewAuction, project, assignment);
            }
        }
    }
}
