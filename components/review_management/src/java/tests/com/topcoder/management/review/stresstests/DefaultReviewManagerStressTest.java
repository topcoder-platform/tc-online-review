/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.stresstests;


import junit.framework.TestCase;


import com.topcoder.management.review.DefaultReviewManager;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Review;

/**
 * <p>
 * Stress test of DefaultReviewManager.
 * </p>
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Added the test for method removeReview.</li>
 * <li>Added the test with Multithreading.</li>
 * <li>Fixed the code to meet the TopCoder standard.</li>
 * </ol>
 * </p>
 * @author still, wz12
 * @version 1.2
 */
public class DefaultReviewManagerStressTest extends TestCase {
    /**
     * The number of times each method will be run.
     */
    public static final int RUN_TIMES = 10000;
    /**
     * The thread number.
     * @since 1.2
     */
    private static final int THREAD_NUM = 256;
    /**
     * The number of times each Multithreading method will be run.
     * @since 1.2
     */
    private static final int RUN_TIMES_MUL = 100;
    /**
     * The ReviewManager instance.
     * @since 1.2
     */
    private ReviewManager impl;
    /**
     * Set up testing environment.
     * @throws Exception to JUnit.
     */
    public void setUp() throws Exception {
        StressTestHelper.loadConfig();
    }

    /**
     * Tear down testing environment.
     * @throws Exception to JUnit.
     */
    public void tearDown() throws Exception {
        StressTestHelper.unloadConfig();
    }

    /**
     * Stress test of the default constructor of DefaultReviewManager.
     * @throws Exception to JUnit.
     */
    public void testCtor() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES; i++) {
            assertNotNull("Failed to create DefaultReviewManager.",
                    new DefaultReviewManager());
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing DefaultReviewManager() for " + RUN_TIMES + " times costs "
                + (end - start) + "ms");
    }

    /**
     * Stress test of the namespace constructor of DefaultReviewManager.
     * @throws Exception to JUnit.
     */
    public void testCtor_namespace() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES; i++) {
            assertNotNull("Failed to create DefaultReviewManager.",
                    new DefaultReviewManager("test1"));
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing DefaultReviewManager(String) for " + RUN_TIMES + " times costs "
                + (end - start) + "ms");
    }


    /**
     * Stress test of the persistence constructor of DefaultReviewManager.
     * @throws Exception to JUnit.
     */
    public void testCtor_persistence() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES; i++) {
            assertNotNull("Failed to create DefaultReviewManager.",
                    new DefaultReviewManager(new StressMockReviewPersistence()));
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing DefaultReviewManager(ReviewPersistence) for " + RUN_TIMES + " times costs "
                + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.createReview(Review, String).
     * @throws Exception to JUnit.
     */
    public void testCreateReview() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < RUN_TIMES; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            ReviewManager manager = new DefaultReviewManager(persistence);
            manager.createReview(null, null);
            persistence.assertMethodCalled("createReview", 1);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing createReview(Review, String) for " + RUN_TIMES + " times costs "
                + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.updateReview(Review, String).
     * @throws Exception to JUnit.
     */
    public void testUpdateReview() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < RUN_TIMES; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            ReviewManager manager = new DefaultReviewManager(persistence);
            manager.updateReview(null, null);
            persistence.assertMethodCalled("updateReview", 1);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing updateReview(Review, String) for " + RUN_TIMES + " times costs "
                + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.getReview(long).
     * @throws Exception to JUnit.
     */
    public void testGetReview() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < RUN_TIMES; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            ReviewManager manager = new DefaultReviewManager(persistence);
            manager.getReview(0);
            persistence.assertMethodCalled("getReview", 1);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing getReview(long) for " + RUN_TIMES + " times costs "
                + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.searchReviews(Filter, boolean).
     * @throws Exception to JUnit.
     */
    public void testSearchReviews() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < RUN_TIMES; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            ReviewManager manager = new DefaultReviewManager(persistence);
            manager.searchReviews(null, true);
            persistence.assertMethodCalled("searchReviews", 1);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing searchReviews(Filter, boolean) for " + RUN_TIMES + " times costs "
                + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.addReviewComment(long, Comment, String).
     * @throws Exception to JUnit.
     */
    public void testAddReviewComment() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < RUN_TIMES; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            ReviewManager manager = new DefaultReviewManager(persistence);
            manager.addReviewComment(0, null, null);
            persistence.assertMethodCalled("addReviewComment", 1);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing addReviewComment(long, Comment, String) for " + RUN_TIMES + " times costs "
                + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.addItemComment(long, Comment, String).
     * @throws Exception to JUnit.
     */
    public void testAddItemComment() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < RUN_TIMES; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            ReviewManager manager = new DefaultReviewManager(persistence);
            manager.addItemComment(0, null, null);
            persistence.assertMethodCalled("addItemComment", 1);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing addItemComment(long, Comment, String) for " + RUN_TIMES + " times costs "
                + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.getAllCommentTypes().
     * @throws Exception to JUnit.
     */
    public void testGetAllCommentTypes() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < RUN_TIMES; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            ReviewManager manager = new DefaultReviewManager(persistence);
            manager.getAllCommentTypes();
            persistence.assertMethodCalled("getAllCommentTypes", 1);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing getAllCommentTypes() for " + RUN_TIMES + " times costs "
                + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.removeReview().
     * @throws Exception to JUnit.
     * @since 1.2
     */
    public void testRemoveReview() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < RUN_TIMES; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            ReviewManager manager = new DefaultReviewManager(persistence);
            manager.removeReview(i + 1, "remove");
            persistence.assertMethodCalled("removeReview", 1);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing removeReview() for " + RUN_TIMES + " times costs "
                + (end - start) + "ms");
    }

    /**
     * The class support multithreading in test.
     * @author wz12
     * @version 1.2
     * @since 1.2
     */
    class Mul extends Thread {
        /**
         * The method name.
         * @since 1.2
         */
        private String methodName;
        /**
         * Set the method Name.
         * @param methodName The method name.
         * @since 1.2
         */
        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }
        /**
         * The main method.
         * @since 1.2
         */
        public void run() {
            try {
                if (methodName.equals("createReview")) {
                    impl.createReview(new Review(), "test");
                } else if (methodName.equals("updateReview")) {
                    impl.updateReview(new Review(), "test");
                } else if (methodName.equals("getReview")) {
                    assertNull(impl.getReview(2));
                } else if (methodName.equals("searchReviews")) {
                    assertNull(impl.searchReviews(null, true));
                } else if (methodName.equals("addReviewComment")) {
                    impl.addReviewComment(2, new Comment(), "test");
                } else if (methodName.equals("addItemComment")) {
                    impl.addItemComment(3, new Comment(), "test");
                } else if (methodName.equals("getAllCommentTypes")) {
                    assertNull(impl.getAllCommentTypes());
                } else if (methodName.equals("removeReview")) {
                    impl.removeReview(2, "test");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stress test of DefaultReviewManager.createReview(Review, String).
     * @throws Exception to JUnit.
     */
    public void testCreateReview_Mul() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES_MUL; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            impl = new DefaultReviewManager(persistence);
            Thread[] threads = new Thread[THREAD_NUM];
            for (int j = 0; j < threads.length; ++j) {
                Mul mul = new Mul();
                mul.setMethodName("createReview");
                threads[j] = mul;
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].start();
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].join();
            }
            persistence.assertMethodCalled("createReview", THREAD_NUM);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing createReview(Review, String) for " + RUN_TIMES_MUL + " times with " + THREAD_NUM
            + " threads costs " + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.updateReview(Review, String).
     * @throws Exception to JUnit.
     */
    public void testUpdateReview_Mul() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES_MUL; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            impl = new DefaultReviewManager(persistence);
            Thread[] threads = new Thread[THREAD_NUM];
            for (int j = 0; j < threads.length; ++j) {
                Mul mul = new Mul();
                mul.setMethodName("updateReview");
                threads[j] = mul;
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].start();
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].join();
            }
            persistence.assertMethodCalled("updateReview", THREAD_NUM);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing updateReview(Review, String) for " + RUN_TIMES_MUL + " times with " + THREAD_NUM
            + " threads costs " + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.getReview(long).
     * @throws Exception to JUnit.
     */
    public void testGetReview_MUL() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES_MUL; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            impl = new DefaultReviewManager(persistence);
            Thread[] threads = new Thread[THREAD_NUM];
            for (int j = 0; j < threads.length; ++j) {
                Mul mul = new Mul();
                mul.setMethodName("getReview");
                threads[j] = mul;
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].start();
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].join();
            }
            persistence.assertMethodCalled("getReview", THREAD_NUM);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing getReview(long) for " + RUN_TIMES_MUL + " times with " + THREAD_NUM
            + " threads costs " + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.searchReviews(Filter, boolean).
     * @throws Exception to JUnit.
     */
    public void testSearchReviews_Mul() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < RUN_TIMES_MUL; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            impl = new DefaultReviewManager(persistence);
            Thread[] threads = new Thread[THREAD_NUM];
            for (int j = 0; j < threads.length; ++j) {
                Mul mul = new Mul();
                mul.setMethodName("searchReviews");
                threads[j] = mul;
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].start();
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].join();
            }
            persistence.assertMethodCalled("searchReviews", THREAD_NUM);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing searchReviews(Filter, boolean) for " + RUN_TIMES_MUL + " times with " + THREAD_NUM
            + " threads costs " + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.addReviewComment(long, Comment, String).
     * @throws Exception to JUnit.
     */
    public void testAddReviewComment_Mul() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < RUN_TIMES_MUL; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            impl = new DefaultReviewManager(persistence);
            Thread[] threads = new Thread[THREAD_NUM];
            for (int j = 0; j < threads.length; ++j) {
                Mul mul = new Mul();
                mul.setMethodName("addReviewComment");
                threads[j] = mul;
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].start();
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].join();
            }
            persistence.assertMethodCalled("addReviewComment", THREAD_NUM);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing addReviewComment(long, Comment, String) for "
            + RUN_TIMES_MUL  + " times with " + THREAD_NUM
            + " threads costs " + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.addItemComment(long, Comment, String).
     * @throws Exception to JUnit.
     */
    public void testAddItemComment_Mul() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < RUN_TIMES_MUL; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            impl = new DefaultReviewManager(persistence);
            Thread[] threads = new Thread[THREAD_NUM];
            for (int j = 0; j < threads.length; ++j) {
                Mul mul = new Mul();
                mul.setMethodName("addItemComment");
                threads[j] = mul;
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].start();
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].join();
            }
            persistence.assertMethodCalled("addItemComment", THREAD_NUM);
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing addItemComment(long, Comment, String) for "
            + RUN_TIMES_MUL  + " times with " + THREAD_NUM
            + " threads costs " + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.getAllCommentTypes().
     * @throws Exception to JUnit.
     */
    public void testGetAllCommentTypes_Mul() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < RUN_TIMES_MUL; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            impl = new DefaultReviewManager(persistence);
            Thread[] threads = new Thread[THREAD_NUM];
            for (int j = 0; j < threads.length; ++j) {
                Mul mul = new Mul();
                mul.setMethodName("getAllCommentTypes");
                threads[j] = mul;
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].start();
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].join();
            }
            persistence.assertMethodCalled("getAllCommentTypes", THREAD_NUM);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing getAllCommentTypes() for " + RUN_TIMES_MUL
            + " times with " + THREAD_NUM
            + " threads costs " + (end - start) + "ms");
    }

    /**
     * Stress test of DefaultReviewManager.removeReview().
     * @throws Exception to JUnit.
     * @since 1.2
     */
    public void testRemoveReview_Mul() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < RUN_TIMES_MUL; i++) {
            StressMockReviewPersistence persistence = new StressMockReviewPersistence();
            impl = new DefaultReviewManager(persistence);
            Thread[] threads = new Thread[THREAD_NUM];
            for (int j = 0; j < threads.length; ++j) {
                Mul mul = new Mul();
                mul.setMethodName("removeReview");
                threads[j] = mul;
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].start();
            }
            for (int j = 0; j < threads.length; ++j) {
                threads[j].join();
            }
            persistence.assertMethodCalled("removeReview", THREAD_NUM);
        }

        long end = System.currentTimeMillis();
        System.out.println("Testing removeReview() for " + RUN_TIMES_MUL + " times with " + THREAD_NUM
            + " threads costs " + (end - start) + "ms");
    }

}
