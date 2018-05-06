/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback;

/**
 * <p>
 * Review feedback detail entity. It's a simply POJO, containing several properties (each with public getter and setter
 * with no validation) and default empty constructor. This class defines review feedback for a specific reviewer:
 * reviewer user ID, score and textual feedback.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is mutable and not thread-safe.
 * </p>
 *
 * @author gevak, sparemax
 * @version 2.0.1
 * @since 2.0
 */
public class ReviewFeedbackDetail {
    /**
     * Identity of a user to whom the review feedback is assigned. It's fully mutable, has public getter and setter. Can
     * be any value.
     */
    private long reviewerUserId;

    /**
     * Review feedback score. It's fully mutable, has public getter and setter. Can be any value.
     */
    private Integer score;

    /**
     * Review feedback text. It's fully mutable, has public getter and setter. Can be any value.
     */
    private String feedbackText;

    /**
     * Creates an instance of ReviewFeedbackDetail.
     */
    public ReviewFeedbackDetail() {
        // Empty
    }

    /**
     * Gets the identity of a user to whom the review feedback is assigned.
     *
     * @return the identity of a user to whom the review feedback is assigned.
     */
    public long getReviewerUserId() {
        return reviewerUserId;
    }

    /**
     * Sets the identity of a user to whom the review feedback is assigned.
     *
     * @param reviewerUserId
     *            the identity of a user to whom the review feedback is assigned.
     */
    public void setReviewerUserId(long reviewerUserId) {
        this.reviewerUserId = reviewerUserId;
    }

    /**
     * Gets the review feedback score.
     *
     * @return the review feedback score.
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Sets the review feedback score.
     *
     * @param score
     *            the review feedback score.
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * Gets the review feedback text.
     *
     * @return the review feedback text.
     */
    public String getFeedbackText() {
        return feedbackText;
    }

    /**
     * Sets the review feedback text.
     *
     * @param feedbackText
     *            the review feedback text.
     */
    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }
}
