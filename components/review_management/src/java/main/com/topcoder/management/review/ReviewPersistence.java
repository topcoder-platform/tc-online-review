/*
 * Copyright (C) 2006-2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review;

import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Review;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * This interface defines the contract of managing the review entities in the persistence. It provides the persistence
 * functionalities to create, update, delete and search reviews. Additionally, application users can also add comment
 * for review and item, and get all the comment types from the persistence. The constructor of the implementation must
 * take a string argument as namespace.
 * </p>
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Added removeReview() method.</li>
 * <li>Fixed the code to meet the TopCoder standard.</li>
 * <li>Implementations of this interface are now required to be thread safe.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> Implementations of this interface are required to be thread safe when entities
 * passed to them are used by the caller in thread safe manner.
 * </p>
 *
 * @author woodjhon, urtks, saarixx, sparemax
 * @version 1.2.1
 */
public interface ReviewPersistence {
    /**
     * <p>
     * Create the review in the persistence. This method is also responsible for creating the associated Comment, and
     * Item, but not for CommentType.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the code to meet the TopCoder standard.</li>
     * </ol>
     * </p>
     *
     * @param review
     *            the Review to create
     * @param operator
     *            the operator who creates the review
     *
     * @throws IllegalArgumentException
     *                if either of arguments is null, or operator is empty string or review fails to pass the
     *                validation.
     * @throws DuplicateReviewEntityException
     *                if review id already exists.
     * @throws ReviewPersistenceException
     *                if failed to create the instance in the persistence
     */
    public void createReview(Review review, String operator) throws ReviewPersistenceException;

    /**
     * <p>
     * Update the review in the persistence. The update method is also responsible for creating, deleting, updating
     * the associated Items.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the code to meet the TopCoder standard.</li>
     * </ol>
     * </p>
     *
     * @param review
     *            the review to update
     * @param operator
     *            the operator who updates the review
     *
     * @throws IllegalArgumentException
     *                if either of arguments is null, operator is empty string, or review fails to pass the
     *                validation.
     * @throws ReviewEntityNotFoundException
     *                if given review id does not exist
     * @throws ReviewPersistenceException
     *                if any other error occurred.
     */
    public void updateReview(Review review, String operator) throws ReviewPersistenceException;

    /**
     * <p>
     * Get the review with given review id from the persistence.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the code to meet the TopCoder standard.</li>
     * </ol>
     * </p>
     *
     * @param id
     *            the review id
     *
     * @return the Review instance with given id
     *
     * @throws IllegalArgumentException
     *                if id is not positive
     * @throws ReviewEntityNotFoundException
     *                if given id does not exist in the database
     * @throws ReviewPersistenceException
     *                if any other error occurred.
     */
    public Review getReview(long id) throws ReviewPersistenceException;

    /**
     * <p>
     * Search the reviews with given search filters. If complete is false, the associated items and comments of the
     * matching review will not been retrieved. Return empty array if no review matches the filter.
     * </p>
     * <p>
     * In the version 1.0, the filter supports at most five fields:
     * <ol>
     * <li>scorecardType --- the score card type, must be java.Long type</li>
     * <li>submission --- the review submission id, must be java.Long type</li>
     * <li> projectPhase - the project phase id, must be Long type. </li>
     * <li>reviewer --- the author of the review, must be java.Long type</li>
     * <li>project --- the project id of the review, must be java.Long type</li>
     * <li>committed --- indicate if the review has been committed, must be java.lang.Integer type. Either new
     * Integer(1) representing committed, or new Integer(0), represent not committed</li>
     * </ol>
     * </p>
     *
     * @param filter
     *            the filter that specifies the search conditions
     * @param complete
     *            a boolean indicating if the complete review data is retrieved
     *
     * @return an array of matching reviews, possible empty.
     *
     * @throws IllegalArgumentException
     *                if filter is null
     * @throws ReviewPersistenceException
     *                if failed to search the reviews.
     */
    public Review[] searchReviews(Filter filter, boolean complete) throws ReviewPersistenceException;

    /**
     * <p>
     * Add comment to review with given review id. this method is responsible for creating the Comment, but not for
     * the CommentType of the given comment.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the code to meet the TopCoder standard.</li>
     * </ol>
     * </p>
     *
     * @param reviewId
     *            the review id
     * @param comment
     *            the Comment instance to add
     * @param operator
     *            the operator who adds the comment
     *
     * @throws IllegalArgumentException
     *                if comment or operator is null, reviewId is not positive, operator is empty string.
     * @throws ReviewEntityNotFoundException
     *                if reviewId does not exists
     * @throws ReviewPersistenceException
     *                if any other error occurred.
     */
    public void addReviewComment(long reviewId, Comment comment, String operator) throws ReviewPersistenceException;

    /**
     * <p>
     * Add the comment to the item with given item id. this method is responsible for creating the Comment, but not
     * for the CommentType of the given comment.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the code to meet the TopCoder standard.</li>
     * </ol>
     * </p>
     *
     * @param itemId
     *            the item id
     * @param comment
     *            the Comment to add
     * @param operator
     *            the operator who adds the comment
     *
     * @throws IllegalArgumentException
     *                if comment or operator is null, itemId is not positive, operator is empty string.
     * @throws ReviewEntityNotFoundException
     *                if itemId does not exists
     * @throws ReviewPersistenceException
     *                if any other error occurred.
     */
    public void addItemComment(long itemId, Comment comment, String operator) throws ReviewPersistenceException;

    /**
     * <p>
     * Get all the CommentType instances from the persistence. Return empty array if no type is found.
     * </p>
     *
     * @return An array containing all the CommentType instances
     *
     * @throws ReviewPersistenceException
     *                if failed to get the types
     */
    public CommentType[] getAllCommentTypes() throws ReviewPersistenceException;

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
    public void removeReview(long id, String operator) throws ReviewPersistenceException;
}
