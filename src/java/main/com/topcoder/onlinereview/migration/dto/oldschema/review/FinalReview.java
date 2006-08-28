/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.review;

/**
 * The FinalReview dto.
 *
 * @author brain_cn
 * @version 1.0
 */
public class FinalReview {
    /** Represents the final_review table name. */
    public static final String TABLE_NAME = "final_review";

    /** Represents agg_worksheet_id field name. */
    public static final String AGG_WORKSHEET_ID_NAME = "agg_worksheet_id";

    /** Represents final_review_id field name. */
    public static final String FINAL_REVIEW_ID_NAME = "final_review_id";

    /** Represents is_completed field name. */
    public static final String IS_COMPLETED_NAME = "is_completed";

    /** Represents comments field name. */
    public static final String COMMENTS_NAME = "comments";

    /** Represents is_approved field name. */
    public static final String IS_APPROVED_NAME = "is_approved";
    private int finalReviewId;
    private boolean isCompleted;
    private String comments;
    private boolean isApproved;

    /**
     * Returns the comments.
     *
     * @return Returns the comments.
     */
    public String getComments() {
        return comments;
    }

    /**
     * Set the comments.
     *
     * @param comments The comments to set.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Returns the isApproved.
     *
     * @return Returns the isApproved.
     */
    public boolean isApproved() {
        return isApproved;
    }

    /**
     * Set the isApproved.
     *
     * @param isApproved The isApproved to set.
     */
    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    /**
     * Returns the isCompleted.
     *
     * @return Returns the isCompleted.
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Set the isCompleted.
     *
     * @param isCompleted The isCompleted to set.
     */
    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
