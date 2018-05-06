/*
 * Copyright (C) 2012, 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback;

import java.util.List;

/**
 * <p>
 * Review feedback entity. It's a simply POJO, containing several properties (each with public getter and setter with no
 * validation) and default empty constructor. This class hold the general data about the review feedback: project ID,
 * general comment, creation and modification authors and timestamps. This class will also contain a (possibly empty)
 * list of associated ReviewFeedbackDetail instances. It's used as DTO for ReviewFeedbackManager.
 * </p>
 *
 * <p>
 * <em>Changes in 2.0:</em>
 * <ol>
 * <li>Updated class documentation.</li>
 * <li>Changed to extend AuditableEntity.</li>
 * <li>Removed fields: id, reviewerUserId, score, feedbackText, createUser, createDate</li>
 * <li>Added fields: comment, details</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread safety</strong>: This class is mutable and not thread-safe.
 * </p>
 *
 * @author gevak, amazingpig, sparemax
 * @version 2.0
 */
public class ReviewFeedback extends AuditableEntity {
    /**
     * Identity of a project to which this review feedback is assigned. It's fully mutable, has public getter and
     * setter. Can be any value.
     */
    private long projectId;

    /**
     * General comment. It's fully mutable, has public getter and setter. Can be any value.
     *
     * @since 2.0
     */
    private String comment;

    /**
     * Details of this review feedback. It's fully mutable, has public getter and setter. Can be any value.
     *
     * @since 2.0
     */
    private List<ReviewFeedbackDetail> details;

    /**
     * Default empty constructor.
     */
    public ReviewFeedback() {
    }

    /**
     * Gets identity of a project to which this review feedback is assigned.
     *
     * @return Identity of a project to which this review feedback is assigned.
     */
    public long getProjectId() {
        return projectId;
    }

    /**
     * Sets identity of a project to which this review feedback is assigned.
     *
     * @param projectId
     *            Identity of a project to which this review feedback is assigned.
     */
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    /**
     * Gets the general comment.
     *
     * @return the general comment.
     *
     * @since 2.0
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the general comment.
     *
     * @param comment
     *            the general comment.
     *
     * @since 2.0
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the details of this review feedback.
     *
     * @return the details of this review feedback.
     *
     * @since 2.0
     */
    public List<ReviewFeedbackDetail> getDetails() {
        return details;
    }

    /**
     * Sets the details of this review feedback.
     *
     * @param details
     *            the details of this review feedback.
     *
     * @since 2.0
     */
    public void setDetails(List<ReviewFeedbackDetail> details) {
        this.details = details;
    }
}
