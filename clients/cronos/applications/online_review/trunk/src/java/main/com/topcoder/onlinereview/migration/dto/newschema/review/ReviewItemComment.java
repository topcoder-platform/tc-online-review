/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.review;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The ReviewItemComment dto.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ReviewItemComment extends BaseDTO {
    /** Represents the review_item_comment table name. */
    public static final String TABLE_NAME = "review_item_comment";
    public static final int COMMENT_TYPE_COMMENT = 1;
    public static final int COMMENT_TYPE_RECOMMENDED = 2;
    public static final int COMMENT_TYPE_REQUIRED = 3;
    public static final int COMMENT_TYPE_APPEAL = 4;
    public static final int COMMENT_TYPE_APPEAL_RESPONSE = 5;
    public static final int COMMENT_TYPE_AGGREGATION_REVIEW_COMMENT = 7;
    public static final int COMMENT_TYPE_FINAL_REVIEW_COMMENT = 10;
    private int reviewItemCommentId;
    private int resourceId;
    private int reviewItemId;
    private int commentTypeId;
    private String content;
    private String extraInfo;
    private int sort;

    /**
     * Returns the commentTypeId.
     *
     * @return Returns the commentTypeId.
     */
    public int getCommentTypeId() {
        return commentTypeId;
    }

    /**
     * Set the commentTypeId.
     *
     * @param commentTypeId The commentTypeId to set.
     */
    public void setCommentTypeId(int commentTypeId) {
        this.commentTypeId = commentTypeId;
    }

    /**
     * Returns the content.
     *
     * @return Returns the content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content.
     *
     * @param content The content to set.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Returns the extraInfo.
     *
     * @return Returns the extraInfo.
     */
    public String getExtraInfo() {
        return extraInfo;
    }

    /**
     * Set the extraInfo.
     *
     * @param extraInfo The extraInfo to set.
     */
    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
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
     * Returns the reviewItemCommentId.
     *
     * @return Returns the reviewItemCommentId.
     */
    public int getReviewItemCommentId() {
        return reviewItemCommentId;
    }

    /**
     * Set the reviewItemCommentId.
     *
     * @param reviewItemCommentId The reviewItemCommentId to set.
     */
    public void setReviewItemCommentId(int reviewItemCommentId) {
        this.reviewItemCommentId = reviewItemCommentId;
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
}
