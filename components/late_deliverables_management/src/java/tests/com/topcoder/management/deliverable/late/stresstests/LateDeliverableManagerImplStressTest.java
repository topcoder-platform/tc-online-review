/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late.stresstests;

import java.util.List;

import org.junit.Test;

import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl;
import com.topcoder.management.deliverable.late.search.LateDeliverableFilterBuilder;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.OrFilter;

import junit.framework.TestCase;

/**
 * Stress test for LateDeliverableManagerImpl class.
 * Changes in 1.0.6: add new column 'late_deliverable_type_id' support.
 *
 * @author TCSDEVELOPER, gjw99
 * @version 1.0.6
 * @since 1.0
 */
public class LateDeliverableManagerImplStressTest extends TestCase {
    /**
     * Represents the instance of LateDeliverableManagerImpl used in test.
     */
    private LateDeliverableManagerImpl instance;

    /**
     * Represents the exception thrown by threads.
     */
    private Throwable error;

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void setUp() throws Exception {
        StressTestHelper.addConfig();
        StressTestHelper.addTestData();

        instance = new LateDeliverableManagerImpl(StressTestHelper.getLateDeliverableManagerImplConfig());
        error = null;
    }

    /**
     * <p>
     * Cleans up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void tearDown() throws Exception {
        StressTestHelper.clearTestData();
        StressTestHelper.closeConnection();
        StressTestHelper.removeConfig();
    }

    /**
     * Stress test for retrieve(long lateDeliverableId) method. It will search by some filters.
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testRetrieve1() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            LateDeliverable lateDeliverable = instance.retrieve(30000);
            assertEquals("retrieve is incorrect.", 30000, lateDeliverable.getId());
            assertEquals("retrieve is incorrect.", 101, lateDeliverable.getProjectPhaseId());
            assertEquals("retrieve is incorrect.", 1001, lateDeliverable.getResourceId());
            assertEquals("retrieve is incorrect.", 50, lateDeliverable.getDeliverableId());
            assertEquals("retrieve is incorrect.", "exp", lateDeliverable.getExplanation());
            assertEquals("retrieve is incorrect.", "resp", lateDeliverable.getResponse());
        }
        long spent = System.currentTimeMillis() - start;
        System.out.println("Run DatabaseLateDeliverablePersistence#retrieve 100 times , spent " + spent + "ms.");
    }

    /**
     * Stress test for retrieve(long lateDeliverableId) method. It will search by some filters.
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testRetrieve2() throws Exception {
        Thread[] threads = new Thread[10];
        long start = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread() {
                /**
                 * Do the retrieve.
                 */
                public void run() {
                    try {
                        for (int i = 0; i < 10; i++) {
                            LateDeliverable lateDeliverable = instance.retrieve(30000);
                            assertEquals("retrieve is incorrect.", 30000, lateDeliverable.getId());
                            assertEquals("retrieve is incorrect.", 101, lateDeliverable.getProjectPhaseId());
                            assertEquals("retrieve is incorrect.", 1001, lateDeliverable.getResourceId());
                            assertEquals("retrieve is incorrect.", 50, lateDeliverable.getDeliverableId());
                            assertEquals("retrieve is incorrect.", "exp", lateDeliverable.getExplanation());
                            assertEquals("retrieve is incorrect.", "resp", lateDeliverable.getResponse());
                        }
                    } catch (Throwable e) {
                        error = e;
                    }
                }
            };
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        assertNull("No exception should be thrown.", error);
        long spent = System.currentTimeMillis() - start;
        System.out
            .println("DatabaseLateDeliverablePersistence#retrieve 100 times multi-thread, spent " + spent + "ms.");
    }

    /**
     * Stress test for searchAllLateDeliverables(Filter filter) method. It will search by some filters.
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables1() throws Exception {
        Filter filter = new OrFilter(LateDeliverableFilterBuilder.createProjectCategoryIdFilter(1),
            LateDeliverableFilterBuilder.createProjectStatusIdFilter(1));
        filter = new AndFilter(filter, LateDeliverableFilterBuilder.createProjectIdFilter(1));
        filter = new AndFilter(filter, LateDeliverableFilterBuilder.createForgivenFilter(true));
        filter = new AndFilter(filter, LateDeliverableFilterBuilder.createDeliverableIdFilter(55));

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            List<LateDeliverable> list = instance.searchAllLateDeliverables(filter);

            assertEquals("'searchAllLateDeliverables' should be correct.", 1, list.size());
            LateDeliverable lateDeliverable = list.get(0);
            assertEquals("searchAllLateDeliverables is incorrect.", 30002, lateDeliverable.getId());
            assertEquals("searchAllLateDeliverables is incorrect.", 101, lateDeliverable.getProjectPhaseId());
            assertEquals("searchAllLateDeliverables is incorrect.", 1001, lateDeliverable.getResourceId());
            assertEquals("searchAllLateDeliverables is incorrect.", 55, lateDeliverable.getDeliverableId());
            assertEquals("searchAllLateDeliverables is incorrect.", "exp", lateDeliverable.getExplanation());
            assertEquals("searchAllLateDeliverables is incorrect.", "resp", lateDeliverable.getResponse());
        }
        long spent = System.currentTimeMillis() - start;
        System.out.println("Run DatabaseLateDeliverablePersistence#searchAllLateDeliverables 100 times , spent "
            + spent + "ms.");
    }

    /**
     * Stress test for searchAllLateDeliverables(Filter filter) method. It will search by some filters.
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables2() throws Exception {
        Filter filter = new OrFilter(LateDeliverableFilterBuilder.createProjectCategoryIdFilter(1),
            LateDeliverableFilterBuilder.createProjectStatusIdFilter(1));
        filter = new AndFilter(filter, LateDeliverableFilterBuilder.createProjectIdFilter(1));
        filter = new AndFilter(filter, LateDeliverableFilterBuilder.createForgivenFilter(true));
        filter = new AndFilter(filter, LateDeliverableFilterBuilder.createDeliverableIdFilter(55));
        final Filter threadFileter = filter;

        Thread[] threads = new Thread[10];
        long start = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread() {
                /**
                 * Do the search.
                 */
                public void run() {
                    try {
                        for (int i = 0; i < 10; i++) {
                            List<LateDeliverable> list = instance.searchAllLateDeliverables(threadFileter);

                            assertEquals("'searchAllLateDeliverables' should be correct.", 1, list.size());
                            LateDeliverable lateDeliverable = list.get(0);
                            assertEquals("searchAllLateDeliverables is incorrect.", 30002, lateDeliverable.getId());
                            assertEquals("searchAllLateDeliverables is incorrect.", 101, lateDeliverable
                                .getProjectPhaseId());
                            assertEquals("searchAllLateDeliverables is incorrect.", 1001, lateDeliverable
                                .getResourceId());
                            assertEquals("searchAllLateDeliverables is incorrect.", 55, lateDeliverable
                                .getDeliverableId());
                            assertEquals("searchAllLateDeliverables is incorrect.", "exp", lateDeliverable
                                .getExplanation());
                            assertEquals("searchAllLateDeliverables is incorrect.", "resp", lateDeliverable
                                .getResponse());
                        }
                    } catch (Throwable e) {
                        error = e;
                    }
                }
            };
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        assertNull("No exception should be thrown.", error);
        long spent = System.currentTimeMillis() - start;
        System.out
            .println("DatabaseLateDeliverablePersistence#searchAllLateDeliverables 100 times multi-thread, spent "
                + spent + "ms.");
    }

    /**
     * Stress test for searchAllLateDeliverables(Filter filter) method. Returns many records.
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables3() throws Exception {
        Filter filter = LateDeliverableFilterBuilder.createProjectCategoryIdFilter(1);

        long start = System.currentTimeMillis();

        List<LateDeliverable> list = instance.searchAllLateDeliverables(filter);

        assertEquals("'searchAllLateDeliverables' should be correct.", 8184, list.size());
        long spent = System.currentTimeMillis() - start;
        System.out.println("Run DatabaseLateDeliverablePersistence#searchAllLateDeliverables 1 times , spent " + spent
            + "ms.");
    }

    /**
     * Stress test for searchAllLateDeliverables(Filter filter) method. Returns many records.
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables4() throws Exception {
        Filter filter = new AndFilter(LateDeliverableFilterBuilder.createProjectCategoryIdFilter(2),
            LateDeliverableFilterBuilder.createProjectStatusIdFilter(1));

        long start = System.currentTimeMillis();
        List<LateDeliverable> list = instance.searchAllLateDeliverables(filter);

        assertEquals("'searchAllLateDeliverables' should be correct.", 1169, list.size());
        long spent = System.currentTimeMillis() - start;
        System.out.println("Run DatabaseLateDeliverablePersistence#searchAllLateDeliverables 1 times , spent " + spent
            + "ms.");
    }

    /**
     * Stress test for searchAllLateDeliverables(Filter filter) method. It will search by some filters.
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables5() throws Exception {
        Filter filter = new AndFilter(LateDeliverableFilterBuilder.createProjectCategoryIdFilter(2),
            LateDeliverableFilterBuilder.createProjectStatusIdFilter(1));
        final Filter threadFileter = filter;

        Thread[] threads = new Thread[10];
        long start = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread() {
                /**
                 * Do the search.
                 */
                public void run() {
                    try {
                        for (int i = 0; i < 5; i++) {
                            List<LateDeliverable> list = instance.searchAllLateDeliverables(threadFileter);

                            assertEquals("'searchAllLateDeliverables' should be correct.", 1169, list.size());
                        }
                    } catch (Throwable e) {
                        error = e;
                    }
                }
            };
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        assertNull("No exception should be thrown.", error);
        long spent = System.currentTimeMillis() - start;
        System.out.println("DatabaseLateDeliverablePersistence#searchAllLateDeliverables 53 times multi-thread, spent "
            + spent + "ms.");
    }

    /**
     * Stress test for searchAllLateDeliverables(Filter filter) method. It will search by some filters.
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables6() throws Exception {
        Filter filter = new OrFilter(LateDeliverableFilterBuilder.createProjectCategoryIdFilter(1),
            LateDeliverableFilterBuilder.createProjectStatusIdFilter(1));
        filter = new AndFilter(filter, LateDeliverableFilterBuilder.createLateDeliverableTypeIdFilter(1));
        filter = new AndFilter(filter, LateDeliverableFilterBuilder.createForgivenFilter(true));
        filter = new AndFilter(filter, LateDeliverableFilterBuilder.createDeliverableIdFilter(55));

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            List<LateDeliverable> list = instance.searchAllLateDeliverables(filter);

            assertEquals("'searchAllLateDeliverables' should be correct.", 1, list.size());
            LateDeliverable lateDeliverable = list.get(0);
            assertEquals("searchAllLateDeliverables is incorrect.", 30002, lateDeliverable.getId());
            assertEquals("searchAllLateDeliverables is incorrect.", 101, lateDeliverable.getProjectPhaseId());
            assertEquals("searchAllLateDeliverables is incorrect.", 1001, lateDeliverable.getResourceId());
            assertEquals("searchAllLateDeliverables is incorrect.", 55, lateDeliverable.getDeliverableId());
            assertEquals("searchAllLateDeliverables is incorrect.", "exp", lateDeliverable.getExplanation());
            assertEquals("searchAllLateDeliverables is incorrect.", "resp", lateDeliverable.getResponse());
        }
        long spent = System.currentTimeMillis() - start;
        System.out.println("Run DatabaseLateDeliverablePersistence#searchAllLateDeliverables 100 times , spent "
            + spent + "ms.");
    }

    /**
     * Stress test for searchRestrictedLateDeliverables(Filter filter, long userId) method. It will search by some
     * filters.
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testSearchRestrictedLateDeliverables1() throws Exception {
        Filter filter = new OrFilter(LateDeliverableFilterBuilder.createProjectCategoryIdFilter(1),
            LateDeliverableFilterBuilder.createProjectStatusIdFilter(1));
        filter = new AndFilter(filter, LateDeliverableFilterBuilder.createDeliverableIdFilter(53));
        filter = new AndFilter(filter, LateDeliverableFilterBuilder.createForgivenFilter(true));

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            List<LateDeliverable> list = instance.searchRestrictedLateDeliverables(filter, 3);
            assertEquals("'searchRestrictedLateDeliverables' should be correct.", 1, list.size());
            LateDeliverable lateDeliverable = list.get(0);
            assertEquals("searchRestrictedLateDeliverables is incorrect.", 30001, lateDeliverable.getId());
            assertEquals("searchRestrictedLateDeliverables is incorrect.", 101, lateDeliverable.getProjectPhaseId());
            assertEquals("searchRestrictedLateDeliverables is incorrect.", 1003, lateDeliverable.getResourceId());
            assertEquals("searchRestrictedLateDeliverables is incorrect.", 53, lateDeliverable.getDeliverableId());
            assertEquals("searchRestrictedLateDeliverables is incorrect.", "exp", lateDeliverable.getExplanation());
            assertEquals("searchRestrictedLateDeliverables is incorrect.", "resp", lateDeliverable.getResponse());
        }
        long spent = System.currentTimeMillis() - start;
        System.out.println("Run DatabaseLateDeliverablePersistence#searchRestrictedLateDeliverables 100 times , spent "
            + spent + "ms.");
    }

    /**
     * Stress test for searchRestrictedLateDeliverables(Filter filter, long userId) method. It will search by some
     * filters.
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testSearchRestrictedLateDeliverables2() throws Exception {
        Filter filter = new OrFilter(LateDeliverableFilterBuilder.createProjectCategoryIdFilter(1),
            LateDeliverableFilterBuilder.createProjectStatusIdFilter(1));
        filter = new AndFilter(filter, LateDeliverableFilterBuilder.createDeliverableIdFilter(53));
        filter = new AndFilter(filter, LateDeliverableFilterBuilder.createForgivenFilter(true));
        final Filter threadFileter = filter;

        Thread[] threads = new Thread[10];
        long start = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread() {
                /**
                 * Do the search.
                 */
                public void run() {
                    try {
                        for (int i = 0; i < 10; i++) {
                            List<LateDeliverable> list = instance.searchRestrictedLateDeliverables(threadFileter, 3);
                            assertEquals("'searchRestrictedLateDeliverables' should be correct.", 1, list.size());
                            LateDeliverable lateDeliverable = list.get(0);
                            assertEquals("searchRestrictedLateDeliverables is incorrect.", 30001, lateDeliverable
                                .getId());
                            assertEquals("searchRestrictedLateDeliverables is incorrect.", 101, lateDeliverable
                                .getProjectPhaseId());
                            assertEquals("searchRestrictedLateDeliverables is incorrect.", 1003, lateDeliverable
                                .getResourceId());
                            assertEquals("searchRestrictedLateDeliverables is incorrect.", 53, lateDeliverable
                                .getDeliverableId());
                            assertEquals("searchRestrictedLateDeliverables is incorrect.", "exp", lateDeliverable
                                .getExplanation());
                            assertEquals("searchRestrictedLateDeliverables is incorrect.", "resp", lateDeliverable
                                .getResponse());
                        }
                    } catch (Throwable e) {
                        error = e;
                    }
                }
            };
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        assertNull("No exception should be thrown.", error);
        long spent = System.currentTimeMillis() - start;
        System.out
            .println("DatabaseLateDeliverablePersistence#searchRestrictedLateDeliverables 100 times multi-thread, spent "
                + spent + "ms.");
    }

    /**
     * Stress test for searchAllLateDeliverables(Filter filter) method. Test late user ID.
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testSearchRestrictedLateDeliverables3() throws Exception {
        Filter filter = new OrFilter(LateDeliverableFilterBuilder.createProjectCategoryIdFilter(1),
            LateDeliverableFilterBuilder.createProjectStatusIdFilter(1));

        long start = System.currentTimeMillis();

        List<LateDeliverable> list = instance.searchRestrictedLateDeliverables(filter, 1);

        assertEquals("'searchAllLateDeliverables' should be correct.", 5846, list.size());
        // the properties has checked at atestSearchRestrictedLateDeliverables1 and
        // atestSearchRestrictedLateDeliverables2
        long spent = System.currentTimeMillis() - start;
        System.out.println("Run DatabaseLateDeliverablePersistence#searchAllLateDeliverables 1 times , spent " + spent
            + "ms.");
    }

    /**
     * Stress test for searchAllLateDeliverables(Filter filter) method. Test manager user ID.
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testSearchRestrictedLateDeliverables4() throws Exception {
        Filter filter = new OrFilter(LateDeliverableFilterBuilder.createProjectCategoryIdFilter(1),
            LateDeliverableFilterBuilder.createProjectStatusIdFilter(1));

        long start = System.currentTimeMillis();

        List<LateDeliverable> list = instance.searchRestrictedLateDeliverables(filter, 2);

        assertEquals("'searchAllLateDeliverables' should be correct.", 10521, list.size());
        // the properties has checked at atestSearchRestrictedLateDeliverables1 and
        // atestSearchRestrictedLateDeliverables2
        long spent = System.currentTimeMillis() - start;
        System.out.println("Run DatabaseLateDeliverablePersistence#searchAllLateDeliverables 1 times , spent " + spent
            + "ms.");
    }

    /**
     * Stress test for searchAllLateDeliverables(Filter filter) method. Test TC Direct.
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testSearchRestrictedLateDeliverables5() throws Exception {
        Filter filter = new OrFilter(LateDeliverableFilterBuilder.createProjectCategoryIdFilter(1),
            LateDeliverableFilterBuilder.createProjectStatusIdFilter(1));

        long start = System.currentTimeMillis();

        List<LateDeliverable> list = instance.searchRestrictedLateDeliverables(filter, 3);

        assertEquals("'searchAllLateDeliverables' should be correct.", 5846, list.size());
        // the properties has checked at atestSearchRestrictedLateDeliverables1 and
        // atestSearchRestrictedLateDeliverables2
        long spent = System.currentTimeMillis() - start;
        System.out.println("Run DatabaseLateDeliverablePersistence#searchAllLateDeliverables 1 times , spent " + spent
            + "ms.");
    }
}