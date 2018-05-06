/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.data;

/**
 * A mock subclass for <code>Review</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class MockReview extends Review {

    /**
     * The submission.
     */
    private long submission;

    /**
     * The id.
     */
    private long id;

    /**
     * The author.
     */
    private long author;

    /**
     * Get the author.
     * @return the author.
     */
    public long getAuthor() {
        return author;
    }

    /**
     * Set the author.
     * @param author the author
     */
    public void setAuthor(long author) {
        this.author = author;
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

    /**
     * Get submission.
     * @return the submission
     */
    public long getSubmission() {
        return submission;
    }

    /**
     * Set submission.
     * @param submission the submission
     */
    public void setSubmission(long submission) {
        this.submission = submission;
    }

    /**
     * Get all items.
     * @return all items
     */
    public Item[] getAllItems() {
        Item item = new MockItem();
        item.setId(1);
        item.setQuestion(1);
        item.setAnswer("Yes");
        item.addComment(new Comment(1, 1, new CommentType(1, "Appeal"), "Appeal"));

        Item item2 = new MockItem();
        item2.setId(2);
        item2.setQuestion(2);
        item2.setAnswer("4/4");
        item2.addComment(new Comment(1, 1, new CommentType(1, "Appeal"), "Appeal"));

        return new Item[] {item, item2};
    }
}
