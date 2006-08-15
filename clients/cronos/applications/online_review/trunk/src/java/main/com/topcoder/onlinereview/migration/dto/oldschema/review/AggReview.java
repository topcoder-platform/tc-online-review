/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.review;

/**
 * The AggReview dto.
 *
 * @author brain_cn
 * @version 1.0
 */
public class AggReview {
    /** Represents the agg_review table name. */
    public static final String TABLE_NAME = "agg_review";

    /** Represents agg_review_id field name. */
    public static final String AGG_REVIEW_ID_NAME = "agg_review_id";

    /** Represents reviewer_id field name. */
    public static final String REVIEWER_ID_NAME = "reviewer_id";

    /** Represents agg_worksheet_id field name. */
    public static final String AGG_WORKSHEET_ID_NAME = "agg_worksheet_id";

    /** Represents agg_review_text field name. */
    public static final String AGG_REVIEW_TEXT_NAME = "agg_review_text";

    /** Represents agg_approval_id field name. */
    public static final String AGG_APPROVAL_ID_NAME = "agg_approval_id";
    private int aggReviewId;
    private int reviewerId;
    private int aggWorksheetId;
    private String aggReviewText;
    private int aggApprovalId;

    /**
     * Returns the aggApprovalId.
     *
     * @return Returns the aggApprovalId.
     */
    public int getAggApprovalId() {
        return aggApprovalId;
    }

    /**
     * Set the aggApprovalId.
     *
     * @param aggApprovalId The aggApprovalId to set.
     */
    public void setAggApprovalId(int aggApprovalId) {
        this.aggApprovalId = aggApprovalId;
    }

    /**
     * Returns the aggReviewId.
     *
     * @return Returns the aggReviewId.
     */
    public int getAggReviewId() {
        return aggReviewId;
    }

    /**
     * Set the aggReviewId.
     *
     * @param aggReviewId The aggReviewId to set.
     */
    public void setAggReviewId(int aggReviewId) {
        this.aggReviewId = aggReviewId;
    }

    /**
     * Returns the aggReviewText.
     *
     * @return Returns the aggReviewText.
     */
    public String getAggReviewText() {
        return aggReviewText;
    }

    /**
     * Set the aggReviewText.
     *
     * @param aggReviewText The aggReviewText to set.
     */
    public void setAggReviewText(String aggReviewText) {
        this.aggReviewText = aggReviewText;
    }

    /**
     * Returns the aggWorksheetId.
     *
     * @return Returns the aggWorksheetId.
     */
    public int getAggWorksheetId() {
        return aggWorksheetId;
    }

    /**
     * Set the aggWorksheetId.
     *
     * @param aggWorksheetId The aggWorksheetId to set.
     */
    public void setAggWorksheetId(int aggWorksheetId) {
        this.aggWorksheetId = aggWorksheetId;
    }

    /**
     * Returns the reviewerId.
     *
     * @return Returns the reviewerId.
     */
    public int getReviewerId() {
        return reviewerId;
    }

    /**
     * Set the reviewerId.
     *
     * @param reviewerId The reviewerId to set.
     */
    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }
}
