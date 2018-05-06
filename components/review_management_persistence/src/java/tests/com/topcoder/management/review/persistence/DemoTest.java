/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.persistence;

import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.review.ReviewPersistence;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Review;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;

/**
 * This TestCase demonstrates the usage of this component.
 * @author urtks
 * @version 1.2
 */
public class DemoTest extends TestCase {

    /**
     * Aggregates all tests in this class.
     * @return test suite aggregating all tests.
     */
    public static Test suite() {
        return new TestSuite(DemoTest.class);
    }

    /**
     * Get a sample comment object to test.
     * @return a sample comment object
     */
    private Comment getSampleComment() {
        CommentType type = new CommentType();
        type.setId(1);
        type.setName("type 1");
        Comment comment = new Comment();
        comment.setAuthor(1);
        comment.setComment("comment 1");
        comment.setCommentType(type);
        return comment;
    }

    /**
     * Sets up the test environment. The configuration will be loaded.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        tearDown();
        TestHelper.setUpTest();
    }

    /**
     * Clean up the test environment. The configuration will be unloaded.
     * @throws Exception throw any exception to JUnit
     */
    protected void tearDown() throws Exception {
        TestHelper.tearDownTest();
    }

    /**
     * This method demonstrates the methods to create a InformixReviewPersistence. We have 3 methods:
     * <ol>
     * <li>Using a namespace in configuration manager.</li>
     * <li>Using the default namespace in configuration manager.</li>
     * <li>Using a db factory instance and a connection name and a search bundle.</li>
     * </ol>
     * @throws Exception throw any exception to JUnit
     */
    public void testCreation() throws Exception {
        // create the instance with the given namespace
        new InformixReviewPersistence("InformixReviewPersistence.CustomNamespace");
        // create the instance with the default namespace;
        new InformixReviewPersistence();
        // create the instance from db factory and connection name and search
        // bundle
        // create a db factory first
        DBConnectionFactory factory = TestHelper.getDBConnectionFactory();
        // then create a search bundle
        SearchBundle searchBundle = new SearchBundle("empty_search_bundle", new HashMap(), "context");
        // finally create the InformixReviewPersistence
        new InformixReviewPersistence(factory, "informix_connection", searchBundle);
    }

    /**
     * This method demonstrates the management of reviews.
     * @throws Exception throw any exception to JUnit
     */
    public void testDemo() throws Exception {
        // first create an instance of InformixReviewPersistence class
        ReviewPersistence persistence = new InformixReviewPersistence();
        // get all comment types
        CommentType[] commentTypes = persistence.getAllCommentTypes();
        // create the review in the database
        // first initialize a review object
        Review review = TestHelper.getSampleReview();
        persistence.createReview(review, "someReviewer");
        // add comment for review
        // first initialize a comment object
        Comment reviewComment = getSampleComment();
        persistence.addReviewComment(1, reviewComment, "admin");
        // add comment for item
        // first initialize a comment object
        Comment itemComment = getSampleComment();
        persistence.addItemComment(1, itemComment, "admin");
        // update the review
        review.setCommitted(true);
        persistence.updateReview(review, "someReviewer");
        // get the review
        review = persistence.getReview(1);
        // search reviews
        // search for the reviews which have been committed
        Filter filter = new EqualToFilter("committed", 1);
        Review[] reviews = persistence.searchReviews(filter, true);
        // remove review
        persistence.removeReview(review.getId(), "admin");
    }
}
