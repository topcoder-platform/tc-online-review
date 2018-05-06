/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import java.util.List;
import java.util.Set;

import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewAuction;

/**
 * <p>
 * This interface represents a review assignment algorithm. It's responsible for generating an assignment
 * (ideally an optimal assignment) of review applications to review roles.
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
public interface ReviewAssignmentAlgorithm extends Configurable {
    /**
     * Assigns applicants to roles.
     *
     * @param reviewAuction
     *            Review auction.
     * @param reviewApplications
     *            Review applications.
     * @return Set of approved review applications.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or reviewApplications contains null.
     * @throws ReviewAssignmentAlgorithmException
     *             If any error occurs.
     */
    public Set<ReviewApplication> assign(ReviewAuction reviewAuction,
        List<ReviewApplication> reviewApplications) throws ReviewAssignmentAlgorithmException;
}
