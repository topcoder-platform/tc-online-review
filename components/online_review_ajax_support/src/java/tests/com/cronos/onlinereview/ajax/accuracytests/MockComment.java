/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;


/**
 * Mock class.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockComment extends Comment {

	/**
     * The id.
     */
    private long id;

    /**
     * The comment type.
     */
    private CommentType commentType;

    /**
     * Get the comment type.
     * @return the comment type
     */
    public CommentType getCommentType() {
        return commentType;
    }

    /**
     * Set the comment type.
     * @param commentType the commentType
     */
    public void setCommentType(CommentType commentType) {
        this.commentType = commentType;
    }

    /**
     * Get id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Set id.
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }
}
