/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.data;

/**
 * A mock subclass for <code>Item</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class MockItem extends Item {

    /**
     * Represents the id.
     */
    private long id;

    /**
     * Get the id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Set the id.
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get all comments.
     * @return all comments.
     */
    public Comment[] getAllComments() {
        // comment type
        CommentType type = new MockCommentType();
        type.setId(1);
        if (id == 1) {
            type.setName("Appeal");
        } else if (id == 2) {
            type.setName("Appeal Response");
        } else {
            type.setName("Submission");
        }

        // comment
        Comment comment = new MockComment();
        comment.setId(1);
        comment.setCommentType(type);


        return new Comment[] {comment};
    }

}
