/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.failuretests;

import java.util.Iterator;

import com.topcoder.management.review.ConfigurationException;
import com.topcoder.management.review.DefaultReviewManager;
import com.topcoder.management.review.DuplicateReviewEntityException;
import com.topcoder.management.review.ReviewEntityNotFoundException;
import com.topcoder.management.review.ReviewPersistence;
import com.topcoder.management.review.ReviewPersistenceException;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Review;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * Failure test for class <code>DefaultReviewManager </code>.
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Added tests for removeReview() method.</li>
 * <li>Fixed the code to meet the TopCoder standard.</li>
 * <li>Fixed the throw exception name.</li>
 * <li>Added some failure tests that are not provided in version 1.0</li>
 * </ol>
 * </p>
 *
 * @author Chenhong, victorsam
 * @version 1.2
 */
public class TestDefaultReviewManagerFailure extends TestCase {

    /**
     * Represents the DefaultReviewManager instance for test.
     */
    private DefaultReviewManager manager = null;

    /**
     * Set up the environment.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void setUp() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }

        cm.add("failuretests/config.xml");
        cm.add("failuretests/invalid1.xml");
        cm.add("failuretests/invalid2.xml");
        cm.add("failuretests/invalid3.xml");
        cm.add("failuretests/invalid4.xml");
        cm.add("failuretests/invalid5.xml");

        manager = new DefaultReviewManager();
    }

    /**
     * Tear down the environment. Remove all the namespaces in the config manager instance.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
    }

    /**
     * Test constructor <code>DefaultReviewManager(String namespace) </code>. The namespace is null, and
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDefaultReviewManagerString_1() throws Exception {
        try {
            new DefaultReviewManager((String) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>DefaultReviewManager(String namespace) </code>. The namespace is empty, and
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDefaultReviewManagerString_2() throws Exception {
        try {
            new DefaultReviewManager("");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>DefaultReviewManager(String namespace) </code>. The namespace is empty, and
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDefaultReviewManagerString_3() throws Exception {
        try {
            new DefaultReviewManager("                        ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>DefaultReviewManager(String namespace) </code>. If the config file is not valid,
     * ConfigurationException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDefaultReviewManagerString_4() throws Exception {
        try {
            new DefaultReviewManager("invalid1");
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>DefaultReviewManager(String namespace) </code>. If the config file is not valid,
     * ConfigurationException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDefaultReviewManagerString_5() throws Exception {
        try {
            new DefaultReviewManager("invalid2");
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>DefaultReviewManager(String namespace) </code>. If the config file is not valid,
     * ConfigurationException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDefaultReviewManagerString_6() throws Exception {
        try {
            new DefaultReviewManager("invalid3");
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>DefaultReviewManager(String namespace) </code>. If the config file is not valid,
     * ConfigurationException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDefaultReviewManagerString_7() throws Exception {
        try {
            new DefaultReviewManager("invalid4");
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>DefaultReviewManager(String namespace) </code>. If the config file is not valid,
     * ConfigurationException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDefaultReviewManagerString_8() throws Exception {
        try {
            new DefaultReviewManager("invalid5");
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>DefaultReviewManager(ReviewPersistence persistence) </code>. If the parameter
     * persistence is null, IllegalArgumentException should be thrown.
     *
     */
    public void testDefaultReviewManagerReviewPersistence() {
        try {
            new DefaultReviewManager((ReviewPersistence) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>void createReview(Review review, String operator) </code>.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the throw exception name.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateReview_1() throws Exception {
        Review r = new Review();
        r.setId(10);

        manager.createReview(r, "ok");

        try {
            manager.createReview(r, "secondTime");
            fail("DuplicateReviewEntityException is expected.");
        } catch (DuplicateReviewEntityException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>void createReview(Review review, String operator) </code>.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the throw exception name.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateReview_2() throws Exception {
        Review r = new Review();
        r.setId(10000);

        try {
            manager.createReview(r, "ok");
            fail("ReviewPersistenceException is expected.");
        } catch (ReviewPersistenceException e) {
            // Ok.
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#createReview(Review, String) for failure.
     * It tests the case that when review is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testCreateReview_NullReview() throws Exception {
        try {
            manager.createReview(null, "operator");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#createReview(Review, String) for failure.
     * It tests the case that when operator is empty and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testCreateReview_Emptyoperator() throws Exception {
        try {
            manager.createReview(new Review(), " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#createReview(Review, String) for failure.
     * It tests the case that when operator is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testCreateReview_Nulloperator() throws Exception {
        try {
            manager.createReview(new Review(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            //good
        }
    }

    /**
     * Test method <code>void updateReview(Review review, String operator) </code>.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the throw exception name.</li>
     * </ol>
     * </p>
     * @throws Exception to JUnit
     *
     */
    public void testUpdateReview_1() throws Exception {
        Review r = new Review();
        r.setId(999);

        try {
            manager.updateReview(r, "ok");
            fail("ReviewEntityNotFoundException is expected.");
        } catch (ReviewEntityNotFoundException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>void updateReview(Review review, String operator) </code>.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the throw exception name.</li>
     * </ol>
     * </p>
     *
     * @throws Exception to JUnit
     *
     */
    public void testUpdateReview_2() throws Exception {
        Review r = new Review();
        r.setId(1000);
        manager.createReview(r, "operator");
        try {
            manager.updateReview(r, "error");
            fail("ReviewPersistenceException is expected.");
        } catch (ReviewPersistenceException e) {
            // Ok.
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#updateReview(Review,String) for failure.
     * It tests the case that when review is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testUpdateReview_NullReview() throws Exception {
        try {
            manager.updateReview(null, "operator");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#updateReview(Review,String) for failure.
     * It tests the case that when operator is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testUpdateReview_NullOperator() throws Exception {
        try {
            manager.updateReview(new Review(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#updateReview(Review,String) for failure.
     * It tests the case that when operator is empty and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testUpdateReview_EmptyOperator() throws Exception {
        try {
            manager.updateReview(new Review(), " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * Test method <code>Review getReview(long id) </code>.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the throw exception name.</li>
     * </ol>
     * </p>
     * @throws Exception to JUnit
     */
    public void testGetReview_1() throws Exception {
        try {
            manager.getReview(199);
            fail("ReviewEntityNotFoundException is expected.");
        } catch (ReviewEntityNotFoundException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>Review getReview(long id) </code>.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the throw exception name.</li>
     * </ol>
     * </p>
     * @throws Exception to JUnit
     */
    public void testGetReview_2() throws Exception {
        // first create an review with id 1000 in the persistence.
        Review r = new Review();
        r.setId(1000);

        manager.createReview(r, "ok");

        try {
            manager.getReview(1000);
            fail("ReviewPersistenceException is expected.");
        } catch (ReviewPersistenceException e) {
            // Ok.
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#getReview(long) for failure.
     * It tests the case that when id is null zero expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testGetReview_ZeroID() throws Exception {
        try {
            manager.getReview(0);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * Test method <code>Review[] searchReviews(Filter filter, boolean complete) </code>.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the throw exception name.</li>
     * </ol>
     * </p>
     *
     */
    public void testSearchReviews() {
        Filter filter = new GreaterThanFilter("Age", new Long(50));

        try {
            manager.searchReviews(filter, true);
            fail("ReviewPersistenceException is expected.");
        } catch (ReviewPersistenceException e) {
            // Ok.
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#searchReviews(Filter,boolean) for failure.
     * It tests the case that when filter is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testSearchReviews_NullFilter() throws Exception {
        try {
            manager.searchReviews(null, true);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * Test method <code>void addReviewComment(long reviewId, Comment comment, String operator) </code>.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the throw exception name.</li>
     * </ol>
     * </p>
     * @throws Exception to JUnit
     */
    public void testAddReviewComment_1() throws Exception {
        try {
            manager.addReviewComment(100, new Comment(), "ok");
            fail("ReviewEntityNotFoundException is expected.");
        } catch (ReviewEntityNotFoundException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>void addReviewComment(long reviewId, Comment comment, String operator) </code>.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the throw exception name.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddReviewComment_2() throws Exception {
        Review r = new Review();
        r.setId(1000);
        manager.createReview(r, "ok");

        try {
            manager.addReviewComment(1000, new Comment(), "ok");
            fail("ReviewPersistenceException is expected.");
        } catch (ReviewPersistenceException e) {
            // Ok.
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#addReviewComment(long,Comment,String) for failure.
     * It tests the case that when comment is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testAddReviewComment_NullComment() throws Exception {
        try {
            manager.addReviewComment(1000, null, "ok");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#addReviewComment(long,Comment,String) for failure.
     * It tests the case that when operator is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testAddReviewComment_NullOperator() throws Exception {
        try {
            manager.addReviewComment(1000, new Comment(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#addReviewComment(long,Comment,String) for failure.
     * It tests the case that when operator is empty and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testAddReviewComment_EmptyOperator() throws Exception {
        try {
            manager.addReviewComment(1000, new Comment(), " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#addReviewComment(long,Comment,String) for failure.
     * It tests the case that when reviewId is zero and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testAddReviewComment_ZeroReviewId() throws Exception {
        try {
            manager.addReviewComment(0, new Comment(), "OK");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * Test method <code>void addItemComment(long itemId, Comment comment, String operator) </code>.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the throw exception name.</li>
     * </ol>
     * </p>
     * @throws Exception to JUnit
     */
    public void testAddItemComment_1() throws Exception {
        try {
            manager.addItemComment(100, new Comment(), "ok");
            fail("ReviewEntityNotFoundException is expected.");
        } catch (ReviewEntityNotFoundException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>void addItemComment(long itemId, Comment comment, String operator) </code>.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the throw exception name.</li>
     * </ol>
     * </p>
     */
    public void testAddItemComment_2() {
        try {
            manager.addItemComment(1000, new Comment(), "ok");
            fail("ReviewPersistenceException is expected.");
        } catch (ReviewPersistenceException e) {
            // Ok.
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#addItemComment(long,Comment,String) for failure.
     * It tests the case that when comment is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testAddItemComment_NullComment() throws Exception {
        try {
            manager.addItemComment(1000, null, "ok");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#addItemComment(long,Comment,String) for failure.
     * It tests the case that when operator is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testAddItemComment_NullOperator() throws Exception {
        try {
            manager.addItemComment(1000, new Comment(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#addItemComment(long,Comment,String) for failure.
     * It tests the case that when operator is empty and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testAddItemComment_EmptyOperator() throws Exception {
        try {
            manager.addItemComment(1000, new Comment(), " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#addItemComment(long,Comment,String) for failure.
     * It tests the case that when reviewId is zero and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testAddItemComment_ZeroReviewId() throws Exception {
        try {
            manager.addItemComment(0, new Comment(), "OK");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * Test method <code>CommentType[] getAllCommentTypes() </code>.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Fixed the throw exception name.</li>
     * </ol>
     * </p>
     *
     */
    public void testGetAllCommentTypes() {
        try {
            manager.getAllCommentTypes();
            fail("ReviewPersistenceException is expected.");
        } catch (ReviewPersistenceException e) {
            // Ok.
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#removeReview(long, String) for failure.
     * It tests the case that when id is negative and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testRemoveReview_NegativeID() throws Exception {
        try {
            manager.removeReview(-1, "operator");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#removeReview(long, String) for failure.
     * It tests the case that when id is zero and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testRemoveReview_ZeroID() throws Exception {
        try {
            manager.removeReview(0, "operator");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#removeReview(long, String) for failure.
     * It tests the case that when operator is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testRemoveReview_Nulloperator() throws Exception {
        try {
            manager.removeReview(1, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#removeReview(long, String) for failure.
     * It tests the case that when operator is empty and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testRemoveReview_Emptyoperator() throws Exception {
        try {
            manager.removeReview(1, " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#removeReview(long, String) for failure.
     * It tests the case that when id is not exist and expects ReviewEntityNotFoundException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testRemoveReview_ReviewEntityNotFoundException() throws Exception {
        try {
            manager.removeReview(100, "operator");
            fail("ReviewEntityNotFoundException expected.");
        } catch (ReviewEntityNotFoundException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultReviewManager#removeReview(long, String) for failure.
     * It tests the case that when id is 1000 and expects ReviewPersistenceException.
     * </p>
     * @throws Exception to JUnit
     * @since 1.2
     */
    public void testRemoveReview_ReviewPersistenceException() throws Exception {
        try {
            manager.removeReview(1000, "operator");
            fail("ReviewPersistenceException expected.");
        } catch (ReviewPersistenceException e) {
            //good
        }
    }
}