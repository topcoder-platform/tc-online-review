/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.stresstests;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import junit.framework.Assert;

import com.topcoder.management.review.ReviewPersistence;
import com.topcoder.management.review.ReviewPersistenceException;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Review;
import com.topcoder.search.builder.filter.Filter;




/**
 * <p>
 * Mock StressMockReviewPersistence class implements ReviewPersistence.
 * </p>
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Added the mock method removeReview.</li>
 * <li>Using lock meet the thread safe required in test.</li>
 * <li>Fixed the code to meet the TopCoder standard.</li>
 * </ol>
 * </p>
 * @author still, wz12
 * @version 1.2
 */
public final class StressMockReviewPersistence extends Assert implements ReviewPersistence {
    /**
     * The lock.
     * @since 1.2
     */
    private static Lock lock = new ReentrantLock();

    /**
     * This private map keeps the pair: method name and the number of the method call count.
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Use generic type.</li>
     * </ol>
     * </p>
     */
    private Map<String, Integer> methodCountMap = new HashMap<String, Integer>();

    /**
     * Mock createReview method.
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Use lock.</li>
     * </ol>
     * </p>
     * @param review The review.
     * @param operator The operator.
     * @see com.topcoder.management.review.ReviewPersistence#createReview
     * (com.topcoder.management.review.data.Review, java.lang.String)
     * @throws ReviewPersistenceException
     *         If failed to create the instance in the persistence.
     */
    public void createReview(Review review, String operator) throws ReviewPersistenceException {
        lock.lock();
        addMethodCount("createReview");
        lock.unlock();
    }

    /**
     * Mock updateReview method.
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Use lock.</li>
     * </ol>
     * </p>
     * @param review The review.
     * @param operator The operator.
     * @see com.topcoder.management.review.ReviewPersistence#updateReview
     * (com.topcoder.management.review.data.Review, java.lang.String)
     * @throws ReviewPersistenceException
     *         If failed to update the instance in the persistence.
     */
    public void updateReview(Review review, String operator) throws ReviewPersistenceException {
        lock.lock();
        addMethodCount("updateReview");
        lock.unlock();
    }

    /**
     * Mock getReview method.
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Use lock.</li>
     * </ol>
     * </p>
     * @see com.topcoder.management.review.ReviewPersistence#getReview(long)
     * @param id The id.
     * @return The review.
     * @throws ReviewPersistenceException
     *         If failed to get the instance in the persistence.
     */
    public Review getReview(long id) throws ReviewPersistenceException {
        lock.lock();
        addMethodCount("getReview");
        lock.unlock();
        return null;
    }

    /**
     * Mock searchReviews method.
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Use lock.</li>
     * </ol>
     * </p>
     * @see com.topcoder.management.review.ReviewPersistence#searchReviews
     * (com.topcoder.search.builder.filter.Filter, boolean)
     * @param filter The filter.
     * @param complete True if complete.
     * @return The review result.
     * @throws ReviewPersistenceException
     *         If failed to search the instance in the persistence.
     */
    public Review[] searchReviews(Filter filter, boolean complete) throws ReviewPersistenceException {
        lock.lock();
        addMethodCount("searchReviews");
        lock.unlock();
        return null;
    }

    /**
     * Mock addReviewComment method.
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Use lock.</li>
     * </ol>
     * </p>
     * @see com.topcoder.management.review.ReviewPersistence#addReviewComment
     * (long, com.topcoder.management.review.data.Comment, java.lang.String)
     * @param reviewId The reviewId.
     * @param comment The comment.
     * @param operator The operator.
     * @throws ReviewPersistenceException
     *         If failed to add review comment in the persistence.
     */
    public void addReviewComment(long reviewId, Comment comment, String operator)
        throws ReviewPersistenceException {
        lock.lock();
        addMethodCount("addReviewComment");
        lock.unlock();
    }

    /**
     * Mock addItemComment method.
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Use lock.</li>
     * </ol>
     * </p>
     * @see com.topcoder.management.review.ReviewPersistence#addItemComment
     * (long, com.topcoder.management.review.data.Comment, java.lang.String)
     * @param itemId The itemId.
     * @param comment The comment.
     * @param operator The operator.
     * @throws ReviewPersistenceException
     *         If failed to add item in the persistence.
     */
    public void addItemComment(long itemId, Comment comment, String operator) throws ReviewPersistenceException {
        lock.lock();
        addMethodCount("addItemComment");
        lock.unlock();
    }

    /**
     * Mock getAllCommentTypes method.
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Use lock.</li>
     * </ol>
     * </p>
     * @see com.topcoder.management.review.ReviewPersistence#getAllCommentTypes()
     * @return The types.
     * @throws ReviewPersistenceException
     *         If failed to get all types in the persistence.
     */
    public CommentType[] getAllCommentTypes() throws ReviewPersistenceException {
        lock.lock();
        addMethodCount("getAllCommentTypes");
        lock.unlock();
        return null;
    }

    /**
     * This method add the Integer corresponding to methodName by 1.
     * @param methodName The method name.
     */
    private void addMethodCount(String methodName) {
        Integer count = methodCountMap.get(methodName);
        if (count != null) {
            methodCountMap.put(methodName, count.intValue() + 1);
        } else {
            methodCountMap.put(methodName, 1);
        }
    }
    /**
     * This method asserts that the method with 'methodName' be called exactly 'calledTimes' times.
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Refined the second assert.</li>
     * </ol>
     * </p>
     * @param methodName The method name.
     * @param calledTimes The called Times.
     */
    public void assertMethodCalled(String methodName, int calledTimes) {
        Object count = methodCountMap.get(methodName);
        assertNotNull("Should call method '" + methodName + "'.", count);
        assertEquals("Should call method '" + methodName + "' "
            + new Integer(calledTimes) + " time(s).", count, new Integer(calledTimes));
        assertTrue("Should not call method other than '" + methodName + "'.", methodCountMap.size() == 1);
    }

    /**
     * Mock getAllCommentTypes method.
     * @see com.topcoder.management.review.ReviewPersistence#removeReview(long, string)
     * @since 1.2
     * @param id The id.
     * @param operator The operator.
     * @throws ReviewPersistenceException
     *         If failed to remove review in the persistence.
     */
    public void removeReview(long id, String operator)
        throws ReviewPersistenceException {
        lock.lock();
        addMethodCount("removeReview");
        lock.unlock();
    }
}
