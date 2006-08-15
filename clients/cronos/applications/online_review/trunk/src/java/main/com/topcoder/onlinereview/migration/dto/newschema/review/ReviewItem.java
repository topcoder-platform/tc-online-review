/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.review;

import java.util.ArrayList;
import java.util.Collection;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The ReviewItem DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ReviewItem extends BaseDTO {
    /** Represents the review_item table name. */
    public static final String TABLE_NAME = "review_item";
    private int reviewItemId;
    private int reviewId;
    private int scorecardQuestionId;
    private int uploadId;
    private String answer;
    private int sort;
    private Collection reviewItemComments = new ArrayList();

    /**
     * Returns the answer.
     *
     * @return Returns the answer.
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Set the answer.
     *
     * @param answer The answer to set.
     */
    public void setAnswer(String answer) {
        this.answer = answer;
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
     * Returns the reviewItemId.
     *
     * @return Returns the reviewItemId.
     */
    public int getReviewItemId() {
        return reviewItemId;
    }

    /**
     * Set the reviewItemId.
     *
     * @param reviewItemId The reviewItemId to set.
     */
    public void setReviewItemId(int reviewItemId) {
        this.reviewItemId = reviewItemId;
    }

    /**
     * Returns the scorecardQuestionId.
     *
     * @return Returns the scorecardQuestionId.
     */
    public int getScorecardQuestionId() {
        return scorecardQuestionId;
    }

    /**
     * Set the scorecardQuestionId.
     *
     * @param scorecardQuestionId The scorecardQuestionId to set.
     */
    public void setScorecardQuestionId(int scorecardQuestionId) {
        this.scorecardQuestionId = scorecardQuestionId;
    }

    /**
     * Returns the sort.
     *
     * @return Returns the sort.
     */
    public int getSort() {
        return sort;
    }

    /**
     * Set the sort.
     *
     * @param sort The sort to set.
     */
    public void setSort(int sort) {
        this.sort = sort;
    }

    /**
     * Returns the uploadId.
     *
     * @return Returns the uploadId.
     */
    public int getUploadId() {
        return uploadId;
    }

    /**
     * Set the uploadId.
     *
     * @param uploadId The uploadId to set.
     */
    public void setUploadId(int uploadId) {
        this.uploadId = uploadId;
    }

    /**
     * Returns the reviewItemComments.
     *
     * @return Returns the reviewItemComments.
     */
    public Collection getReviewItemComments() {
        return reviewItemComments;
    }

    /**
     * Set the reviewItemComments.
     *
     * @param reviewItemComments The reviewItemComments to set.
     */
    public void setReviewItemComments(Collection reviewItemComments) {
        this.reviewItemComments = reviewItemComments;
    }

    /**
     * add the reviewItemComment.
     *
     * @param reviewItemComment The reviewItemComment to add.
     */
    public void addReviewItemComment(ReviewItemComment reviewItemComment) {
        reviewItemComments.add(reviewItemComment);
        reviewItemComment.setSort(reviewItemComments.size());
    }
}
