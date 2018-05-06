/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.failuretests;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.management.review.DuplicateReviewEntityException;
import com.topcoder.management.review.ReviewEntityNotFoundException;
import com.topcoder.management.review.ReviewPersistence;
import com.topcoder.management.review.ReviewPersistenceException;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Review;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanFilter;

/**
 * This class is a mock one of ReviewPersistence interface. This class is created for testing if the
 * DefaultReviewManager is correctly implemented for failure part.
 *
 * <p>
 * The purpose of this class is created to throw DuplicateReviewEntityException, ReviewPersistenceException
 * and ReviewEntityNotFoundException on purpose. These exception should be caught in the DefaultReviewManager
 * class and
 * </p>
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Added removeReview() method.</li>
 * <li>Fixed the code to meet the TopCoder standard.</li>
 * <li>Fixed some code for tests.</li>
 * </ol>
 * </p>
 *
 * @author Chenhong, victorsam
 * @version 1.2
 */
public class MySimpleReviewPersistence implements ReviewPersistence {

    /**
     * Represents the review instances in the persistence layer. The key is Long instance and the value is review
     * instance.
     */
    private Map<Long, Review> reviews = new HashMap<Long, Review>();

    /**
     * Default constructor.
     *
     */
    public MySimpleReviewPersistence() {
        // empty
    }

    /**
     * Create a new MySimpleReviewPersistence with namespace as its parameter.
     *
     * @param namespace
     *            the namespace for this class
     * @throws IllegalArgumentException
     *             if namespace is null or is empty string
     *
     */
    public MySimpleReviewPersistence(String namespace) {
        if (namespace == null) {
            throw new IllegalArgumentException("Parameter namespace should not be null.");
        }

        if (namespace.trim().length() == 0) {
            throw new IllegalArgumentException("Parameter namespace should not be empty string.");
        }

    }

    /**
     * <p>
     * Create a review in the persistence. This method will throw DuplicateReviewEntityException and
     * ReviewPersistenceException on purpose.
     * </p>
     *
     * @param review
     *            the review to be created in the persistence.
     * @param operator
     *            the operator who creates the review entity.
     *
     * @throws IllegalArgumentException
     *             if any argument is null, or operator is empty string, or review fails to pass the validation.
     * @throws DuplicateReviewEntityException
     *             if review entity id already exists in the persistence.
     * @throws ReviewPersistenceException
     *             if any other error occurred.
     */
    public void createReview(Review review, String operator) throws ReviewPersistenceException {
        if (review == null) {
            throw new IllegalArgumentException("Parameter review should not be null.");
        }

        if (operator == null) {
            throw new IllegalArgumentException("Parameter operator should not be null.");
        }

        if (operator.trim().length() == 0) {
            throw new IllegalArgumentException("Parameter operator should not be empty string.");
        }

        if (reviews.containsKey(new Long(review.getId()))) {
            throw new DuplicateReviewEntityException("Review entity id already exists.", review.getId());
        }

        // throw ReviewPersistenceException on purpose.
        if (review.getId() == 10000) {
            throw new ReviewPersistenceException("This exception is thrown on purpose.");
        }

        reviews.put(new Long(review.getId()), review);
    }

    /**
     * <p>
     * Updates the review entity in the persistence. This method will throw ReviewEntityNotFoundException and
     * ReviewPersistenceException on purpose.
     * </p>
     *
     * @param review
     *            the review entity to update into the persistence.
     * @param operator
     *            the operator who updates the review entity.
     *
     *
     * @throws IllegalArgumentException
     *             if either of arguments is null, or operator is empty string
     * @throws ReviewEntityNotFoundException
     *             if given review entity does not exist in the persistence.
     * @throws ReviewPersistenceException
     *             if any other error occurred.
     */
    public void updateReview(Review review, String operator) throws ReviewPersistenceException {
        if (review == null) {
            throw new IllegalArgumentException("Parameter review should not be null.");
        }

        if (operator == null) {
            throw new IllegalArgumentException("Parameter operator should not be null.");
        }

        if (operator.trim().length() == 0) {
            throw new IllegalArgumentException("Parameter operator should not be empty string.");
        }
        // if the given review entity does not exist in the persistence,
        // throw ReviewEntityNotFoundException.
        if (!reviews.containsKey(new Long(review.getId()))) {
            throw new ReviewEntityNotFoundException("The review entity not found.", review.getId());
        }

        // throw ReviewPersistenceException on purpose.
        if (review.getId() == 1000) {
            throw new ReviewPersistenceException("This exception is thrown on purpose.");
        }

        reviews.put(new Long(review.getId()), review);
    }

    /**
     * <p>
     * Get the review with id. This method will throw ReviewEntityNotFoundException and ReviewPersistenceException on
     * purpose.
     * </p>
     *
     * @param id
     *            the id of the review entity to be retrieved.
     *
     * @return the retrieved review entity with its id.
     *
     * @throws IllegalArgumentException
     *             if the given id is not positive.
     * @throws ReviewEntityNotFoundException
     *             if the given id does not exist in the persistence.
     * @throws ReviewPersistenceException
     *             if any other error occurred.
     */
    public Review getReview(long id) throws ReviewPersistenceException {
        if (id <= 0) {
            throw new IllegalArgumentException("Parameter id should be positive.");
        }
        // if the given review entity does not exist in the persistence,
        // throw ReviewEntityNotFoundException.
        if (!reviews.containsKey(new Long(id))) {
            throw new ReviewEntityNotFoundException("The review entity not found.", id);
        }

        // throw ReviewPersistenceException on purpose.
        if (id == 1000) {
            throw new ReviewPersistenceException("This exception is thrown on purpose.");
        }

        return null;
    }

    /**
     * <p>
     * Search reviews with Filter instance. This method will throw ReviewPersistenceException on purpose.
     * </p>
     *
     * @param filter
     *            the filter that specifies the search conditions.
     * @param complete
     *            a boolean variable indicating if the complete review data is retrieved.
     *
     * @return the array of matching review entities, or empty array if no review entity matches the filter.
     *
     * @throws IllegalArgumentException
     *             if the filter is null.
     * @throws ReviewPersistenceException
     *             if failed to search the review entities.
     */
    public Review[] searchReviews(Filter filter, boolean complete) throws ReviewPersistenceException {
        if (filter == null) {
            throw new IllegalArgumentException("Parameter filter should not be null.");
        }
        // throw ReviewPersistenceException on purpose.
        if (filter instanceof GreaterThanFilter) {
            throw new ReviewPersistenceException("Throw ReviewPersistenceException on purpose.");
        }

        return new Review[0];
    }

    /**
     * <p>
     * Add review comment to review with given review id. This method will do nothing, but throw
     * ReviewEntityNotFoundException and ReviewPersistenceException on purpose.
     * </p>
     *
     * @param reviewId
     *            the id of review entity.
     * @param comment
     *            the Comment instance to be added.
     * @param operator
     *            the operator who adds the comment.
     *
     * @throws IllegalArgumentException
     *             if any argument is null, or reviewId is not positive, or operator is empty string.
     * @throws ReviewEntityNotFoundException
     *             if reviewId does not exists in the persistence.
     * @throws ReviewPersistenceException
     *             if any other error occurred.
     */
    public void addReviewComment(long reviewId, Comment comment, String operator) throws ReviewPersistenceException {
        if (reviewId <= 0) {
            throw new IllegalArgumentException("Parameter reviewId should be positive.");
        }
        if (comment == null) {
            throw new IllegalArgumentException("Parameter comment should not be null.");
        }
        if (operator == null) {
            throw new IllegalArgumentException("Parameter operator should not be null.");
        }

        if (operator.trim().length() == 0) {
            throw new IllegalArgumentException("Parameter operator should not be empty string.");
        }

        // if the given review entity does not exist in the persistence,
        // throw ReviewEntityNotFoundException.
        if (!reviews.containsKey(new Long(reviewId))) {
            throw new ReviewEntityNotFoundException("The review entity not found.", reviewId);
        }

        // throw ReviewPersistenceException on purpose.
        if (reviewId == 1000) {
            throw new ReviewPersistenceException("This exception is thrown on purpose.");
        }
    }

    /**
     * <p>
     * Add Item comment. This method do nothing, but will throw ReviewEntityNotFoundException and
     * ReviewPersistenceException on purpose.
     * </p>
     *
     * @param itemId
     *            the item id.
     * @param comment
     *            the Comment instance to be added.
     * @param operator
     *            the operator who adds the comment.
     *
     * @throws IllegalArgumentException
     *             if any argument is null, or itemId is not positive, or operator is empty string.
     * @throws ReviewEntityNotFoundException
     *             if itemId does not exists in the persistence.
     * @throws ReviewPersistenceException
     *             if any other error occurred.
     */
    public void addItemComment(long itemId, Comment comment, String operator) throws ReviewPersistenceException {
        if (itemId <= 0) {
            throw new IllegalArgumentException("Parameter itemId should be positive.");
        }
        if (comment == null) {
            throw new IllegalArgumentException("Parameter comment should not be null.");
        }
        if (operator == null) {
            throw new IllegalArgumentException("Parameter operator should not be null.");
        }

        if (operator.trim().length() == 0) {
            throw new IllegalArgumentException("Parameter operator should not be empty string.");
        }

        if (itemId == 100) {
            throw new ReviewEntityNotFoundException("Throw ReviewEntityNotFoundException on purpose.", itemId);
        }

        if (itemId == 1000) {
            throw new ReviewPersistenceException("Throw ReviewPersistenceException on purpose.");
        }

    }

    /**
     * <p>
     * Get all comment types in the persistence. It will throw ReviewPersistenceException on purpose.
     * </p>
     *
     * @return the array of all <code>CommentType</code> instance from the persistence, or empty array if no comment
     *         type can be found.
     *
     * @throws ReviewPersistenceException
     *             if failed to get the comment types.
     */
    public CommentType[] getAllCommentTypes() throws ReviewPersistenceException {
        throw new ReviewPersistenceException("Throw ReviewPersistenceException on purpose.");
    }

    /**
     * Removes the review with the specified ID from persistence. All its review items, associated comments and
     * uploads are also removed.
     *
     * @param id
     *            the ID of the review to be deleted.
     * @param operator
     *            the operator who deletes the review entity.
     *
     * @throws IllegalArgumentException
     *             if the id is not positive or operator is null/empty.
     * @throws ReviewEntityNotFoundException
     *             if given ID does not exist in the database.
     * @throws ReviewPersistenceException
     *             if any other error occurred.
     *
     * @since 1.2
     */
    public void removeReview(long id, String operator) throws ReviewPersistenceException {
        if (id <= 0) {
            throw new IllegalArgumentException("Parameter id should be positive.");
        }

        if (operator == null) {
            throw new IllegalArgumentException("Parameter operator should not be null.");
        }

        if (operator.trim().length() == 0) {
            throw new IllegalArgumentException("Parameter operator should not be empty string.");
        }

        if (!reviews.containsKey(new Long(id))) {
            throw new ReviewEntityNotFoundException("The review entity not found.", id);
        }

        // throw ReviewPersistenceException on purpose.
        if (id == 1000) {
            throw new ReviewPersistenceException("This exception is thrown on purpose.");
        }
    }

}