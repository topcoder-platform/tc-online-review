/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.resource;

import java.util.Date;


/**
 * The ProjectResult DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ProjectResult {
    /** Represents the project_result table name. */
    public static final String TABLE_NAME = "project_result";

    /** Represents old_rating field name. */
    public static final String OLD_RATING_NAME = "old_rating";

    /** Represents old_reliability field name. */
    public static final String OLD_RELIABILITY_NAME = "old_reliability";

    /** Represents create_date field name. */
    public static final String CREATE_DATE_NAME = "create_date";

    /** Represents raw_score field name. */
    public static final String RAW_SCORE_NAME = "raw_score";

    /** Represents final_score field name. */
    public static final String FINAL_SCORE_NAME = "final_score";

    /** Represents placed field name. */
    public static final String PLACED_NAME = "placed";

    /** Represents user_id field name. */
    public static final String USER_ID_NAME = "user_id";

    /** Represents passed_review_ind field name. */
    public static final String PASSED_REVIEW_IND_NAME = "passed_review_ind";

    /** Represents project_id field name. */
    public static final String PROJECT_ID_NAME = "project_id";
    private int oldRating;
    private float oldReliability;
    private Date createDate;
    private float rawScore;
    private float finalScore;
    private int placed;
    private boolean passedReviewInd;

    /** TODO not sure if it's submission's submitter_id. */
    private int userId;

    /**
     * Returns the createDate.
     *
     * @return Returns the createDate.
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * Set the createDate.
     *
     * @param createDate The createDate to set.
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * Returns the finalScore.
     *
     * @return Returns the finalScore.
     */
    public float getFinalScore() {
        return finalScore;
    }

    /**
     * Set the finalScore.
     *
     * @param finalScore The finalScore to set.
     */
    public void setFinalScore(float finalScore) {
        this.finalScore = finalScore;
    }

    /**
     * Returns the oldRating.
     *
     * @return Returns the oldRating.
     */
    public int getOldRating() {
        return oldRating;
    }

    /**
     * Set the oldRating.
     *
     * @param oldRating The oldRating to set.
     */
    public void setOldRating(int oldRating) {
        this.oldRating = oldRating;
    }

    /**
     * Returns the oldReliability.
     *
     * @return Returns the oldReliability.
     */
    public float getOldReliability() {
        return oldReliability;
    }

    /**
     * Set the oldReliability.
     *
     * @param oldReliability The oldReliability to set.
     */
    public void setOldReliability(float oldReliability) {
        this.oldReliability = oldReliability;
    }

    /**
     * Returns the placed.
     *
     * @return Returns the placed.
     */
    public int getPlaced() {
        return placed;
    }

    /**
     * Set the placed.
     *
     * @param placed The placed to set.
     */
    public void setPlaced(int placed) {
        this.placed = placed;
    }

    /**
     * Returns the rawScore.
     *
     * @return Returns the rawScore.
     */
    public float getRawScore() {
        return rawScore;
    }

    /**
     * Set the rawScore.
     *
     * @param rawScore The rawScore to set.
     */
    public void setRawScore(float rawScore) {
        this.rawScore = rawScore;
    }

    /**
     * Returns the userId.
     *
     * @return Returns the userId.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Set the userId.
     *
     * @param userId The userId to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Returns the passedReviewInd.
     *
     * @return Returns the passedReviewInd.
     */
    public boolean isPassedReviewInd() {
        return passedReviewInd;
    }

    /**
     * Set the passedReviewInd.
     *
     * @param passedReviewInd The passedReviewInd to set.
     */
    public void setPassedReviewInd(boolean passedReviewInd) {
        this.passedReviewInd = passedReviewInd;
    }
}
