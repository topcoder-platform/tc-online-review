/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.review;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The Review DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class Review extends BaseDTO {
    /** Represents the review table name. */
    public static final String TABLE_NAME = "review";
    private int reviewId;
    private int resourceId;
    private int submissionId;
    private int scorecardId;
    private boolean committed;
    private float score;
    private List reviewComments = new ArrayList();
    private List reviewItems = new ArrayList();

    /**
     * Returns the committed.
     *
     * @return Returns the committed.
     */
    public boolean isCommitted() {
        return committed;
    }

    /**
     * Set the committed.
     *
     * @param committed The committed to set.
     */
    public void setCommitted(boolean committed) {
        this.committed = committed;
    }

    /**
     * Returns the resourceId.
     *
     * @return Returns the resourceId.
     */
    public int getResourceId() {
        return resourceId;
    }

    /**
     * Set the resourceId.
     *
     * @param resourceId The resourceId to set.
     */
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Returns the reviewId.
     *
     * @return Returns the reviewId.
     */
    public int getReviewId() {
        return reviewId;
    }

    /**
     * Set the reviewId.
     *
     * @param reviewId The reviewId to set.
     */
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    /**
     * Returns the score.
     *
     * @return Returns the score.
     */
    public float getScore() {
        return score;
    }

    /**
     * Set the score.
     *
     * @param score The score to set.
     */
    public void setScore(float score) {
        this.score = score;
    }

    /**
     * Returns the scorecardId.
     *
     * @return Returns the scorecardId.
     */
    public int getScorecardId() {
        return scorecardId;
    }

    /**
     * Set the scorecardId.
     *
     * @param scorecardId The scorecardId to set.
     */
    public void setScorecardId(int scorecardId) {
        this.scorecardId = scorecardId;
    }

    /**
     * Returns the submissionId.
     *
     * @return Returns the submissionId.
     */
    public int getSubmissionId() {
        return submissionId;
    }

    /**
     * Set the submissionId.
     *
     * @param submissionId The submissionId to set.
     */
    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    /**
     * Returns the reviewComments.
     *
     * @return Returns the reviewComments.
     */
    public List getReviewComments() {
        return reviewComments;
    }

    /**
     * Set the reviewComments.
     *
     * @param reviewComments The reviewComments to set.
     */
    public void setReviewComments(List reviewComments) {
        this.reviewComments = reviewComments;
    }

    /**
     * add the reviewComment.
     *
     * @param reviewComment The reviewComment to add.
     */
    public void addReviewComment(ReviewComment reviewComment) {
        reviewComments.add(reviewComment);
        reviewComment.setSort(reviewComments.size());
    }

    /**
     * Returns the reviewItems.
     *
     * @return Returns the reviewItems.
     */
    public Collection getReviewItems() {
        return reviewItems;
    }

    /**
     * Set the reviewItems.
     *
     * @param reviewItems The reviewItems to set.
     */
    public void setReviewItems(List reviewItems) {
        this.reviewItems = reviewItems;
    }

    /**
     * add the reviewItem.
     *
     * @param reviewItem The reviewItem to add.
     */
    public void addReviewItem(ReviewItem reviewItem) {
        reviewItems.add(reviewItem);
        reviewItem.setSort(reviewItems.size());
    }
}
