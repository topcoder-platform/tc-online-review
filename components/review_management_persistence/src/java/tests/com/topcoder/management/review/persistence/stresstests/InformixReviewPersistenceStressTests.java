/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.persistence.stresstests;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.management.review.ReviewEntityNotFoundException;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.persistence.InformixReviewPersistence;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * Stress test case of {@link InformixReviewPersistence}.
 * </p>
 * 
 * @author telly12, mumujava
 * @version 1.2
 */
public class InformixReviewPersistenceStressTests extends BaseStressTest {
    /**
     * The default namespace used to construct the test instance.
     */
    private static final String NAMESPACE = "com.topcoder.management.review.persistence.InformixReviewPersistence";
    /**
     * <p>
     * The InformixReviewPersistence instance to test.
     * </p>
     * */
    private InformixReviewPersistence instance;

    /**
     * The threadCount.
     * <p>
     * Note: To run the stress in multithreaded environment, please make sure the lock mode for all tables are ROW.
     * (Default mode is PAGE !)
     * You can use the ddl.sql provided in this directory to build the database.
     */
    private int threadCount = 10;
    /**
     * The prePopulatedReviewCount.
     */
    private int prePopulatedReviewCount = 100;
    /**
     * The reviewCount.
     */
    private int reviewCount = 100;

    /**
     * The itemCountByReview.
     */
    private int itemCountByReview = 10;

    /**
     * The commentCountByReview.
     */
    private int commentCountByReview = 10;

    /**
     * The commentCountByItem.
     */
    private int commentCountByItem = 10;
    /**
     * The current Review Id.
     */
    private long currentReviewId = 1000;

    /**
     * The current Item Id.
     */
    private long currentItemId = 11;

    /**
     * The current Comment Id.
     */
    private long currentCommentId = 11;
    /**
     * The uploadId.
     */
    private int uploadId = 11;
    /**
     * The large text.
     */
    private static final String LARGE_COMMENT;
    /**
     * The large text.
     */
    private static final String LARGE_ANSWER;
    /**
     * Initialize the LARGE_COMMENT.
     */
    static {
        byte[] bytes = new byte[4000];
        Arrays.fill(bytes, (byte) 'a');
        LARGE_COMMENT = new String(bytes);
        byte[] bytes2 = new byte[250];
        Arrays.fill(bytes2, (byte) 'b');
        LARGE_ANSWER = new String(bytes2);
    }

    /**
     * The setUp of the unit test.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
        clearConfig();

        ConfigManager cm = ConfigManager.getInstance();
        cm.add("stress/config.xml");
        cm.add("logging.xml");

        StressTestHelper.executeSqlFile("test_files/stress/clearTable.sql");
        StressTestHelper.executeSqlFile("test_files/stress/insert.sql");
        instance = new InformixReviewPersistence(NAMESPACE);
        prepopulatedDB();
    }

    /**
     * The tearDown of the unit test.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        StressTestHelper.executeSqlFile("test_files/stress/clearTable.sql");
        clearConfig();
        super.tearDown();
    }

    /**
     * <p>
     * Pre-populates the database.
     * </p>
     * @throws Exception to junit
     */
    public void prepopulatedDB() throws Exception {
        for (int i = 0; i < prePopulatedReviewCount; i++) {
            Review review = getReview();
            instance.createReview(review, "operator");
        }
    }

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     * 
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(
                InformixReviewPersistenceStressTests.class);
        return suite;
    }

    /**
     * <p>
     * Stress test for CRUD operations of InformixReviewPersistenceStressTests.
     * <p>
     * The methods are run in a multi-threaded environment with a heavily load.
     * <p>
     * The load is configurable.
     * </p>
     * @throws Throwable to junit
     */
    public void test_CRUD() throws Throwable {
        final Review reviews[] = new Review[reviewCount];
        for (int i = 0; i < reviewCount; i++) {
            reviews[i] = getReview();
        }
        
        Thread thread[] = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            thread[i] = new TestThread(i) {
                public void run() {
                    try {
                        for (int i = 0; i < reviewCount; i++) {
                            if ((i % threadCount) == getIndex()) {
                                instance.createReview(reviews[i], "operator");
                                Review retrived = instance.getReview(reviews[i].getId());
                                assertEqualsReview(reviews[i], retrived);
                                
                                //clear the items and add another item
                                reviews[i].clearItems();
                                reviews[i].addItem(getItem());
                                instance.updateReview(reviews[i], "operator");
                                retrived = instance.getReview(reviews[i].getId());
                                assertEqualsReview(reviews[i], retrived);
                                
                                instance.removeReview(reviews[i].getId(), "operator");
                                try {
                                    instance.getReview(reviews[i].getId());
                                    fail("Expects ReviewEntityNotFoundException");
                                } catch (ReviewEntityNotFoundException e) {
                                    //ok, the review is removed
                                }
                            }
                        }
                    } catch (Throwable e) {
                        lastError = e;
                    }

                }
            };
            thread[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            // wait to end
            thread[i].join();
        }
        if (lastError != null) {
            throw lastError;
        }
        
        System.out.println("Run test for CRUD takes "
                + (new Date().getTime() - start) + "ms");
    }
    /**
     * <p>
     * Stress test for getAllCommentTypes().
     * <p>
     * The methods are run in a multi-threaded environment.
     * <p>
     * @throws Throwable to junit
     */
    public void test_getAllCommentTypes() throws Throwable {
        Thread thread[] = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            thread[i] = new TestThread(i) {
                public void run() {
                    try {
                        //note: the return value when the function is called the first time will be cached,
                        // it will not reflect the changes afterwards
                        // thus the actual return value may differ from 3
                        assertEquals("the result should be correct", 3, instance.getAllCommentTypes().length);
                    } catch (Throwable e) {
                        lastError = e;
                    }

                }
            };
            thread[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            // wait to end
            thread[i].join();
        }
        if (lastError != null) {
            throw lastError;
        }
        
        System.out.println("Run test for getAllCommentTypes() takes "
                + (new Date().getTime() - start) + "ms");
    }

    /**
     * Stress test for searchReviews().
     * 
     * @throws Throwable to junit
     */
    public void test_searchReviews() throws Throwable {
        final Review review = getReview();
        review.setCommitted(false);
        instance.createReview(review, "operator");
        
        Thread thread[] = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            thread[i] = new TestThread(i) {
                public void run() {
                    try {
                        //search for one review with specified id
                        Filter filter = SearchBundle.buildEqualToFilter("review_id", review.getId());
                        Review[] reviews = instance.searchReviews(filter, true);
                        assertEquals("The result value is incorrect.", 1, reviews.length);
                        //the two results should be the same
                        assertEqualsReview(reviews[0], review);

                        //search for committed review
                        filter = SearchBundle.buildEqualToFilter("committed", 1);
                        reviews = instance.searchReviews(filter, true);
                        //review.setCommitted((currentReviewId & 1) == 1);
                        assertEquals("The result value is incorrect.", (prePopulatedReviewCount) / 2, reviews.length);
                    } catch (Throwable e) {
                        lastError = e;
                    }

                }
            };
            thread[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            // wait to end
            thread[i].join();
        }
        if (lastError != null) {
            throw lastError;
        }
        
        System.out.println("Run test for searchReviews() takes "
                + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for addReviewComment().
     * <p>
     * The methods are run in a multi-threaded environment with a heavily load.
     * <p>
     * The load is configurable.
     * </p>
     * @throws Throwable to junit
     */
    public void test_addReviewComment() throws Throwable {
        final Review reviews[] = new Review[reviewCount];
        for (int i = 0; i < reviewCount; i++) {
            reviews[i] = getReview();
            instance.createReview(reviews[i], "operator");
        }
        final int addReviewCommentCount = 5;
        
        Thread thread[] = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            thread[i] = new TestThread(i) {
                public void run() {
                    try {
                        for (int i = 0; i < reviewCount; i++) {
                            if ((i % threadCount) == getIndex()) {
                                for (int j = 0; j < addReviewCommentCount; j++) {
                                    //add one comment each loop
                                    Comment comment = getComment();
                                    reviews[i].addComment(comment);
                                    instance.addReviewComment(reviews[i].getId(), comment , "operator");
                                }
                                Review retrived = instance.getReview(reviews[i].getId());
                                //the database should be consistent with objects in the memory 
                                assertEqualsReview(reviews[i], retrived);
                                //instance.removeReview(reviews[i].getId(), "operator");
                            }                            
                        }
                    } catch (Throwable e) {
                        lastError = e;
                    }

                }
            };
            thread[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            // wait to end
            thread[i].join();
        }
        if (lastError != null) {
            throw lastError;
        }
        
        System.out.println("Run test for CRUD takes "
                + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for addItemComment().
     * <p>
     * The methods are run in a multi-threaded environment with a heavily load.
     * <p>
     * The load is configurable.
     * </p>
     * @throws Throwable to junit
     */
    public void test_addItemComment() throws Throwable {
        final Review reviews[] = new Review[reviewCount];
        for (int i = 0; i < reviewCount; i++) {
            reviews[i] = getReview();
        }
        final int addItemCommentCount = 5;
        
        Thread thread[] = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            thread[i] = new TestThread(i) {
                public void run() {
                    try {
                        for (int i = 0; i < reviewCount; i++) {
                            if ((i % threadCount) == getIndex()) {
                                instance.createReview(reviews[i], "operator");
                                
                                for (int j = 0; j < addItemCommentCount; j++) {
                                    //add one comment each loop
                                    Comment comment = getComment();
                                    reviews[i].getItem(0).addComment(comment);
                                    instance.addItemComment(reviews[i].getItem(0).getId(), comment , "operator");
                                }
                                Review retrived = instance.getReview(reviews[i].getId());
                                //the database should be consistent with objects in the memory 
                                assertEqualsReview(reviews[i], retrived);
                                //instance.removeReview(reviews[i].getId(), "operator");
                            }
                        }
                    } catch (Throwable e) {
                        lastError = e;
                    }

                }
            };
            thread[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            // wait to end
            thread[i].join();
        }
        if (lastError != null) {
            throw lastError;
        }
        
        System.out.println("Run test for CRUD takes "
                + (new Date().getTime() - start) + "ms");
    }
    
    /**
     * <p>
     * Asserts two reviews are equal.
     * <p>
     * @param review one review
     * @param retrievedReview the other review
     * @throws Throwable to junit
     */
    private void assertEqualsReview(Review review, Review retrievedReview) {       
        assertEquals("The id should be same.", review.getId(), retrievedReview.getId());
        assertEquals("The creationTimestamp should be same.", review.getCreationTimestamp(), retrievedReview.getCreationTimestamp());
        assertEquals("The modificationTimestamp should be same.", review.getModificationTimestamp(), retrievedReview.getModificationTimestamp());
        assertEquals("The initialScore should be same.", review.getInitialScore(), retrievedReview.getInitialScore());
        assertEquals("The numberOfComments should be same.", review.getNumberOfComments(), retrievedReview.getNumberOfComments());
        assertEquals("The numberOfItems should be same.", review.getNumberOfItems(), retrievedReview.getNumberOfItems());
        assertEquals("The resource id should be same.", review.getAuthor(), retrievedReview.getAuthor());
        assertEquals("The submission id should be same.", review.getSubmission(), retrievedReview.getSubmission());
        assertEquals("The scorecard id should be same.", review.getScorecard(), retrievedReview.getScorecard());
        assertEquals("The score should be same.", review.getScore(), retrievedReview.getScore());
        assertEquals("The committed state should be same.", review.isCommitted(), retrievedReview.isCommitted());
        assertEquals("The create user should be 'same'.", review.getCreationUser(), retrievedReview.getCreationUser());
        assertEquals("The modify user should be 'same'.", review.getModificationUser(), retrievedReview.getModificationUser());
        assertEquals("The number of items should be same.", review.getAllItems().length,
                retrievedReview.getAllItems().length);
        assertEquals("The number of comments should be same.", review.getAllComments().length, retrievedReview
                .getAllComments().length);

        for (int i = 0; i < review.getAllItems().length; i++) {
            Item item = review.getAllItems()[i];
            assertEqualsItem(item, searchItemWithId(retrievedReview.getAllItems(), item.getId()));
        }
        for (int i = 0; i < review.getAllComments().length; i++) {
            Comment c1 = review.getAllComments()[i];
            assertEqualsComment(c1, searchCommentWithId(retrievedReview.getAllComments(), c1.getId()));
        }
    }

    /**
     * <p>
     * Asserts two items are equal.
     * <p>
     * @param item one item
     * @param retrivedItem the other item
     * @throws Throwable to junit
     */
    private void assertEqualsItem(Item item, Item retrivedItem) {
        // assert the retrieved item
        assertEquals("The id should be same.", item.getId(), retrivedItem.getId());
        assertEquals("The answer should be same.", item.getAnswer(), retrivedItem.getAnswer());
        assertEquals("The getDocument should be same.", item.getDocument(), retrivedItem.getDocument());
        assertEquals("The question should be same.", item.getQuestion(), retrivedItem.getQuestion());
        assertEquals("The comments should be same.", item.getAllComments().length, retrivedItem.getAllComments().length);

        for (int j = 0; j < item.getAllComments().length; j++) {
            Comment c1 = item.getAllComments()[j];
            assertEqualsComment(c1, searchCommentWithId(retrivedItem.getAllComments(), c1.getId()));
        }
    }

    /**
     * <p>
     * Asserts two Comments are equal.
     * <p>
     * @param c1 one Comment
     * @param c2 the other Comment
     * @throws Throwable to junit
     */
    private void assertEqualsComment(Comment c1, Comment c2) {
        // assert the retrieved item
        assertEquals("The id should be same.", c1.getId(), c2.getId());
        assertEquals("The author should be same.", c1.getAuthor(), c2.getAuthor());
        assertEquals("The extraInfo should be same.", c1.getExtraInfo(), c2.getExtraInfo());
    }

    /**
     * <p>
     * Searches the array and return the Comment with the specified id.
     * <p>
     * @param items the array
     * @param id the id
     * @return the Comment with the specified id.
     */
    private Comment searchCommentWithId(Comment[] allComments, long id) {
        for (Comment c : allComments) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }
    /**
     * <p>
     * Searches the array and return the item with the specified id.
     * <p>
     * @param items the array
     * @param id the id
     * @return the item with the specified id.
     */
    private Item searchItemWithId(Item items[], long id) {
        for (Item it : items) {
            if (it.getId() == id) {
                return it;
            }
        }
        return null;
    }

    /**
     * Remove all the namespace.
     *
     * @throws Exception to JUnit
     */
    private void clearConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator<?> it = cm.getAllNamespaces();

        while (it.hasNext()) {
            cm.removeNamespace((String) it.next());
        }
    }
    /**
     * Get a valid review instance.
     *
     * @return a review instance
     * @throws Exception to junit
     */
    private Review getReview() throws Exception {
        // set all the long properties with currentReviewId
        Review review = new Review();
        review.setAuthor(11 + currentReviewId % 10);
        review.setScorecard(11 + currentReviewId % 10);
        review.setSubmission(11 + currentReviewId % 10);
        review.setCommitted((currentReviewId & 1) == 1);
        review.setScore(new Float(currentReviewId % 10 * 5.0));

        // add one item
        for (int i = 0; i < commentCountByReview; i++) {
            review.addComment(getComment());
        }

        // add one item
        for (int i = 0; i < itemCountByReview; i++) {
            review.addItem(getItem());
        }
        currentReviewId++;

        return review;
    }

    /**
     * Get a valid item instance.
     *
     * @return an item instance
     * @throws Exception to junit
     */
    private synchronized Item getItem() throws Exception {
        Item item = new Item(currentItemId);
        item.setQuestion(11 + currentItemId % 10);
        item.setAnswer(currentItemId % 10 == 0 ? ("stress" + "answer" + currentItemId) : LARGE_ANSWER);

        for (int i = 0; i < commentCountByItem; i++) {
            item.addComment(getComment());
        }
        currentItemId++;
        item.setDocument((long)insertUpload());

        return item;
    }
    /**
     * Inserts an upload.
     *
     * @param uploadId the upload id.
     * @return the id
     * @throws Exception to JUnit
     */
    private synchronized int insertUpload() throws Exception {
        Connection conn = StressTestHelper.getConnection();
        Statement stmt = conn.createStatement();

        try {
            stmt.executeUpdate("insert into upload (upload_id) values (" + this.uploadId + ")");
        } finally {
            stmt.close();
        }
        return this.uploadId++;
    }

    /**
     * Get a comment instance.
     *
     * @return a comment instance
     */
    private Comment getComment() {
        Comment comment = new Comment(currentCommentId);
        comment.setAuthor(11 + currentCommentId % 10);
        comment.setExtraInfo("stress" + "extraInfo" + currentCommentId);
        comment.setCommentType(new CommentType(11 + currentCommentId % 10, "name" + currentCommentId));
        comment.setComment(currentCommentId % 10 == 0 ? ("stress" + "content" + currentCommentId) : LARGE_COMMENT);

        currentCommentId++;

        return comment;
    }
}
