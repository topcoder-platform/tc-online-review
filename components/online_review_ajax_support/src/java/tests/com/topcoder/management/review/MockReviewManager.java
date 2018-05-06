/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review;

import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.MockCommentType;
import com.topcoder.management.review.data.MockReview;
import com.topcoder.management.review.data.Review;
import com.topcoder.search.builder.filter.Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mock implementation of <code>ReviewManager</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class MockReviewManager implements ReviewManager {

    /**
     * <p>A <code>Map</code> mapping the <code>String</code> method signatures to <code>Map</code>s mapping the <code>
     * String</code> names of the arguments to <code>Object</code>s representing the values of  arguments which have
     * been provided by the caller of the method.</p>
     */
    private static Map methodArguments = new HashMap();

    /**
     * <p>A <code>Throwable</code> representing the exception to be thrown from any method of the mock class.</p>
     */
    private static Throwable globalException = null;

    /**
     * Add item comment.
     * @param item the item id
     * @param comment the comment
     * @param operator the operator
     */
    public void addItemComment(long item, Comment comment, String operator) {
    }

    /**
     * Add review comment.
     * @param review the review id
     * @param comment the comment
     * @param operator the operator
     */
    public void addReviewComment(long review, Comment comment, String operator) {
    }

    /**
     * Create review.
     * @param review the review to create
     * @param operator the operator
     */
    public void createReview(Review review, String operator) {
    }

    /**
     * Get all the comment types.
     * @return all the comment types
     */
    public CommentType[] getAllCommentTypes() {
        CommentType review = new MockCommentType();
        review.setId(1);
        review.setName("Appeal");

        CommentType response = new MockCommentType();
        response.setId(2);
        response.setName("Appeal Response");
        
        CommentType comment = new MockCommentType();
        comment.setId(3);
        comment.setName("Comment");
        
        CommentType required = new MockCommentType();
        required.setId(4);
        required.setName("Required");
        
        
        CommentType recommended = new MockCommentType();
        recommended.setId(5);
        recommended.setName("Recommended");
        
        CommentType appealsResponse = new MockCommentType();
        appealsResponse.setId(6);
        appealsResponse.setName("Appeals Response");        

        return new CommentType[] {review, response,required, comment,recommended,appealsResponse};
        
    }

    /**
     * Get the review.
     * @param id the review id
     * @return the review got
     */
    public Review getReview(long id) throws ReviewManagementException {
        if (MockReviewManager.globalException != null) {
            if (MockReviewManager.globalException instanceof ReviewManagementException) {
                throw (ReviewManagementException) MockReviewManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly",
                                           MockReviewManager.globalException);
            }
        }
        if (id < 10) {
            Review review = new MockReview();
            review.setId(1);
            review.setSubmission(1);
            if (id >= 3 && id <= 6) {
                review.setAuthor(id);
            } else {
                review.setAuthor(3);
            }
            review.setScorecard(1);
            return review;
        }
        return null;
    }

    /**
     * Search reviews by filter.
     * @param filter the filter
     * @param complete whether complete or not
     * @return the reviews got
     */
    public Review[] searchReviews(Filter filter, boolean complete) {
        return null;
    }

    /**
     * Update the review.
     * @param review the review to update
     * @param operator the operator
     */
    public void updateReview(Review review, String operator) throws ReviewManagementException {
        if (MockReviewManager.globalException != null) {
            if (MockReviewManager.globalException instanceof ReviewManagementException) {
                throw (ReviewManagementException) MockReviewManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly",
                                           MockReviewManager.globalException);
            }
        }

        String methodName = "updateReview_Review_String";

        HashMap arguments = new HashMap();
        arguments.put("1", review);
        arguments.put("2", operator);
        List args = (List) MockReviewManager.methodArguments.get(methodName);
        if (args == null) {
            args = new ArrayList();
            MockReviewManager.methodArguments.put(methodName, args);
        }
        args.add(arguments);

    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is called.
     * If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockReviewManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockLog</code> so all collected method arguments, configured method results and
     * exceptions are lost.</p>
     */
    public static void releaseState() {
        MockReviewManager.globalException = null;
        MockReviewManager.methodArguments.clear();
    }

    /**
     * <p>Gets the values of the argumenta which have been passed to the specified method by the caller.</p>
     *
     * @param methodSignature a <code>String</code> uniquelly distinguishing the target method among other methods
     * @return a <code>List</code> of <code>Map</code> providing the values of the arguments on each call. which has
     *         been supplied by the caller of the specified method.
     */
    public static List getMethodArguments(String methodSignature) {
        return (List) MockReviewManager.methodArguments.get(methodSignature);
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockResourceManager</code> class.</p>
     */
    public static void init() {
    }

}
