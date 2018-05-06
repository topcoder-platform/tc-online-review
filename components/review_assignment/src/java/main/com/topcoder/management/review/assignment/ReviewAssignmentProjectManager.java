/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import java.util.Set;

import com.topcoder.management.project.Project;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewAuction;

/**
 * <p>
 * This manager is responsible for managing project resources and phases according to review assignment.
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
public interface ReviewAssignmentProjectManager extends Configurable {
    /**
     * Adds reviewers to project and extends some project phases if necessary.
     *
     * @param reviewAuction
     *            Review auction.
     * @param project
     *            the Project.
     * @param assignment
     *            Set of approved review applications.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or assignment contains null key.
     * @throws ReviewAssignmentProjectManagementException
     *             If any other error with managing projects resources and phases occurs.
     */
    public void addReviewersToProject(ReviewAuction reviewAuction, Project project,
        Set<ReviewApplication> assignment)
        throws ReviewAssignmentProjectManagementException;
}
