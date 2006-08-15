/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.review;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The ReviewComment dto.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ReviewComment extends BaseDTO {
    /** Represents the review_comment table name. */
    public static final String TABLE_NAME = "review_comment";
    private int reviewCommentId;
    private int resourceId;
    private int reviewId;
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
    	if (content == null) {
    		// TODO content cannot be null
    		this.content = " ";
    	} else {
    		this.content = content;
    	}
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
     * Returns the reviewCommentId.
     *
     * @return Returns the reviewCommentId.
     */
    public int getReviewCommentId() {
        return reviewCommentId;
    }

    /**
     * Set the reviewCommentId.
     *
     * @param reviewCommentId The reviewCommentId to set.
     */
    public void setReviewCommentId(int reviewCommentId) {
        this.reviewCommentId = reviewCommentId;
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
