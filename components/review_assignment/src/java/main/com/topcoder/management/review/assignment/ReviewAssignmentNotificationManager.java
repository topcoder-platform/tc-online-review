/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import java.util.List;
import java.util.Set;

import com.topcoder.management.project.Project;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewAuction;

/**
 * <p>
 * This manager is responsible for sending notifications related to review assignment.
 * </p>
 * <p>
 * This interface extends Configurable interface to support configuration via Configuration API component.
 * </p>
 * <p>
 * Thread Safety: Implementations of this interface are not required to be thread safe.
 * </p>
 *
 * @author gevak, zhongqiangzhang
 * @version 1.0
 */
public interface ReviewAssignmentNotificationManager extends Configurable {
    /**
     * Notifies members who were assigned to a review application role (i.e. those who have one Approved
     * review application).
     *
     * @param reviewAuction
     *            the ReviewAuction instance.
     * @param project
     *            the Project instance.
     * @param assignment
     *            Set of approved review applications.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or assignment contains null key.
     * @throws ReviewAssignmentNotificationException
     *             If any error occurs with populating and sending a notification.
     */
    public void notifyApprovedApplicants(ReviewAuction reviewAuction, Project project,
                                         Set<ReviewApplication> assignment)
        throws ReviewAssignmentNotificationException;

    /**
     * Notifies members who were not assigned to any review application role (i.e. those whose all review
     * applications are Rejected).
     *
     * @param reviewAuction
     *            the Review auction.
     * @param project
     *            the Project.
     * @param unassignedUserIds
     *            the User IDs of unassigned applicants.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or unassignedUserIds contains null.
     * @throws ReviewAssignmentNotificationException
     *             If any error occurs with populating and sending a notification.
     */
    public void notifyRejectedApplicants(ReviewAuction reviewAuction, Project project,
        List<Long> unassignedUserIds) throws ReviewAssignmentNotificationException;

    /**
     * Notifies all PMs/copilots about review assignment.
     *
     * @param reviewAuction
     *            the ReviewAuction instance.
     * @param project
     *            the Project instance.
     * @param assignment
     *            Set of approved review applications.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or assignment contains null key.
     * @throws ReviewAssignmentNotificationException
     *             If any error occurs with populating and sending a notification.
     */
    public void notifyManagers(ReviewAuction reviewAuction, Project project,
                               Set<ReviewApplication> assignment)
        throws ReviewAssignmentNotificationException;
}
