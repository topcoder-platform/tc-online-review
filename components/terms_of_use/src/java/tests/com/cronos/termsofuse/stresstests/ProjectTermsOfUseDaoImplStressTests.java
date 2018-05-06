/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.stresstests;

import com.cronos.termsofuse.dao.impl.ProjectTermsOfUseDaoImpl;
import com.cronos.termsofuse.model.TermsOfUse;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * <p>Stress tests for {@link ProjectTermsOfUseDaoImpl}.</p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>fixes the tests for getTermsOfUse() method.</li>
 * <li>adds tests for getTermsOfUse() method to test the support filtering of terms of use
 * groups by custom agreeability types.</li>
 * </ol>
 * </p>
 *
 * @author jmn, victorsam
 * @version 1.1
 */
public class ProjectTermsOfUseDaoImplStressTests extends BaseStressTests {

    /**
     * <p>Represents the query used for inserting the terms of use.</p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Properties memberAgreeable and electronicallySignable are removed from TermsOfUse entity.
     * and agreeabilityType property is added.</li>
     * </ol>
     * </p>
     */
    private static final String INSERT_TERMS_OF_USE = "INSERT INTO 'informix'.terms_of_use (terms_of_use_id, terms_of_use_type_id, title,"
        + " url, terms_of_use_agreeability_type_id) VALUES(%d, 1, 'title', 'url', 1)";

    /**
     * <p>Represents the query used for queering the project terms of use.</p>
     */
    private static final String SELECT_COUNT_PROJECT_TERMS_OF_USE = "select count(*) from 'informix'.project_role_terms_of_use_xref "
        + " where resource_role_id = 1 and project_id = %d";

    /**
     * <p>Represents the sql used for inserting the project terms of use.</p>
     */
    private static final String INSERT_PROJECT_TERMS_OF_USE = "INSERT INTO 'informix'.project_role_terms_of_use_xref "
        + "(project_id, resource_role_id," + " terms_of_use_id, sort_order, group_ind) VALUES (%d, 1, %d, 1, 1);";

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private ProjectTermsOfUseDaoImpl instance;

    /**
     * <p>Represents a syncroot object.</p>
     */
    private final Object syncLock = new Object();

    /**
     * <p>Represents the next resource role id</p>
     */
    private int nextResourceRoleId = 1;

    /**
     * <p>Represents the number of thread.</p>
     */
    private int threadNumber;

    /**
     * <p>Returns test suite for this class.</p>
     *
     * @return test suite for this class
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ProjectTermsOfUseDaoImplStressTests.class);
    }

    /**
     * {@inheritDoc}
     */
    public void setUp() throws Exception {
        super.setUp();

        instance = new ProjectTermsOfUseDaoImpl(loadConfiguration());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigName() {
        return "projectTermsOfUseDao";
    }

    /**
     * <p>Tests the {@link ProjectTermsOfUseDaoImpl#createProjectRoleTermsOfUse(int, int, long, int, int)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateProjectRoleTermsOfUse() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("ProjectTermsOfUseDaoImplStressTests"
            + "#testCreateProjectRoleTermsOfUse");

        int resourceRoleId = 1;
        long termsOfUseId = 1;
        int sortOrder = 1;
        int groupIndex = 1;

        StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, termsOfUseId));
        for (int i = 1; i < StressTestHelper.LOOPS; i++) {

            instance.createProjectRoleTermsOfUse(i, resourceRoleId, termsOfUseId, sortOrder, groupIndex);
            assertEquals("ProjectRoleTermsOfUse hasn't been created.", 1, StressTestHelper.queryInt(getConnection(),
                String.format(SELECT_COUNT_PROJECT_TERMS_OF_USE, i)));
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link ProjectTermsOfUseDaoImpl#createProjectRoleTermsOfUse(int, int, long, int, int)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateProjectRoleTermsOfUseMultiThreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("ProjectTermsOfUseDaoImplStressTests"
            + "#testCreateProjectRoleTermsOfUseMultiThreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        threadNumber = 1;
        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    int resourceRoleId = 1;
                    long termsOfUseId = 1;
                    int sortOrder = 1;
                    int groupIndex = 1;

                    int currentThread;

                    synchronized (syncLock) {
                        currentThread = threadNumber++;
                    }

                    for (int i = currentThread * StressTestHelper.LOOPS; i <= (currentThread + 1)
                        * StressTestHelper.LOOPS; i++) {
                        try {
                            instance.createProjectRoleTermsOfUse(i, resourceRoleId, termsOfUseId, sortOrder, groupIndex);
                            assertEquals("ProjectRoleTermsOfUse hasn't been created.", 1, StressTestHelper.queryInt(
                                getConnection(), String.format(SELECT_COUNT_PROJECT_TERMS_OF_USE, i)));
                        } catch (Exception e) {
                            fail("Error occurred during test: " + e.toString());
                        }
                    }
                }
            });
        }

        StressTestHelper.awaitTermination(executorService);

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link ProjectTermsOfUseDaoImpl#removeProjectRoleTermsOfUse(int, int, long, int)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testRemoveProjectRoleTermsOfUse() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("ProjectTermsOfUseDaoImplStressTests"
            + "#testRemoveProjectRoleTermsOfUse");

        int resourceRoleId = 1;
        long termsOfUseId = 1;
        int groupIndex = 1;

        StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, termsOfUseId));
        for (int i = 0; i < StressTestHelper.LOOPS; i++) {
            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_PROJECT_TERMS_OF_USE, i, termsOfUseId));

            instance.removeProjectRoleTermsOfUse(i, resourceRoleId, termsOfUseId, groupIndex);
            assertEquals("ProjectRoleTermsOfUse hasn't been deleted.", 0, StressTestHelper.queryInt(getConnection(),
                String.format(SELECT_COUNT_PROJECT_TERMS_OF_USE, i)));
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link ProjectTermsOfUseDaoImpl#removeProjectRoleTermsOfUse(int, int, long, int)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testRemoveProjectRoleTermsOfUseMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("ProjectTermsOfUseDaoImplStressTests"
            + "#testRemoveProjectRoleTermsOfUseMultithreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    int resourceRoleId = 1;
                    long termsOfUseId = 1;
                    int groupIndex = 1;

                    int currentThread;

                    synchronized (syncLock) {
                        currentThread = threadNumber++;
                    }

                    for (int i = currentThread * StressTestHelper.LOOPS; i <= (currentThread + 1)
                        * StressTestHelper.LOOPS; i++) {
                        try {
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_PROJECT_TERMS_OF_USE,
                                i, termsOfUseId));

                            instance.removeProjectRoleTermsOfUse(i, resourceRoleId, termsOfUseId, groupIndex);
                            assertEquals("ProjectRoleTermsOfUse hasn't been deleted.", 0, StressTestHelper.queryInt(
                                getConnection(), String.format(SELECT_COUNT_PROJECT_TERMS_OF_USE, i)));
                        } catch (Exception e) {
                            fail("Error occurred during test: " + e.toString());
                        }
                    }
                }
            });
        }

        StressTestHelper.awaitTermination(executorService);

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link ProjectTermsOfUseDaoImpl#getTermsOfUse(int, int[], int[])} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetTermsOfUse() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("ProjectTermsOfUseDaoImplStressTests"
            + "#testGetTermsOfUse");

        long termsOfUseId = 1;
        Map<Integer, List<TermsOfUse>> result;

        for (int i = 0; i < StressTestHelper.LOOPS; i++) {
            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, termsOfUseId));
            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_PROJECT_TERMS_OF_USE, i, termsOfUseId));

            result = instance.getTermsOfUse(i, 1, null);
            assertNotNull("List of TermsOfUse was null..", result);
            assertEquals("Incorrect number of TermsOfUse retrieved.", 1, result.size());

            termsOfUseId++;
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link ProjectTermsOfUseDaoImpl#getTermsOfUse(int, int[], int[])} method.</p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>make the test can be compile and run correctly.</li>
     * </ol>
     * </p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetTermsOfUseBulk1() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("ProjectTermsOfUseDaoImplStressTests"
            + "#testGetTermsOfUse");

        Map<Integer, List<TermsOfUse>> result;

        StressTestHelper.executeBatch(getConnection(), "test_files/stresstests/input/input1.sql");

        for (int i = 0; i < StressTestHelper.LOOPS; i++) {

            result = instance.getTermsOfUse(1, 1, null);
            assertNotNull("List of TermsOfUse was null.", result);
            assertEquals("Incorrect number of TermsOfUse retrieved.", 34, result.size());
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link ProjectTermsOfUseDaoImpl#getTermsOfUse(int, int[], int[])} method.</p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>make the test can be compile and run correctly.</li>
     * </ol>
     * </p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetTermsOfUseBulk2() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("ProjectTermsOfUseDaoImplStressTests"
            + "#testGetTermsOfUse");

        Map<Integer, List<TermsOfUse>> result;

        StressTestHelper.executeBatch(getConnection(), "test_files/stresstests/input/input2.sql");

        for (int i = 0; i < StressTestHelper.LOOPS; i++) {

            result = instance.getTermsOfUse(1, 1, null);
            assertNotNull("List of TermsOfUse was null.", result);
            assertEquals("Incorrect number of TermsOfUse retrieved.", 167, result.size());
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link ProjectTermsOfUseDaoImpl#getTermsOfUse(int, int[], int[])} method.</p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>make the test can be compile and run correctly.</li>
     * </ol>
     * </p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetTermsOfUseBulkMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("ProjectTermsOfUseDaoImplStressTests"
            + "#testGetTermsOfUseBulkMultithreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        StressTestHelper.executeBatch(getConnection(), "test_files/stresstests/input/input3.sql");

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    Map<Integer, List<TermsOfUse>> result;
                    int resourceRoleId;

                    synchronized (syncLock) {
                        resourceRoleId = nextResourceRoleId;
                        nextResourceRoleId++;
                    }

                    for (int i = 1; i <= StressTestHelper.LOOPS; i++) {
                        try {
                            result = instance.getTermsOfUse(1, resourceRoleId, null);
                            assertNotNull("List of TermsOfUse was null.", result);
                            assertEquals("Incorrect number of TermsOfUse retrieved.", 50, result.get(0).size());

                        } catch (Exception e) {
                            e.printStackTrace();
                            fail("Error occurred during test: " + e.toString());
                        }
                    }
                }
            });
        }

        StressTestHelper.awaitTermination(executorService);

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link ProjectTermsOfUseDaoImpl#getTermsOfUse(int, int[], int[])} method.</p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>make the test can be compile and run correctly.</li>
     * </ol>
     * </p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetTermsOfUseMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("ProjectTermsOfUseDaoImplStressTests"
            + "#testGetTermsOfUseMultithreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    int resourceRoleId = 1;
                    long termsOfUseId = 1;
                    Map<Integer, List<TermsOfUse>> result;

                    int currentThread;

                    synchronized (syncLock) {
                        currentThread = threadNumber++;
                    }

                    for (int i = currentThread * StressTestHelper.LOOPS; i <= (currentThread + 1)
                        * StressTestHelper.LOOPS; i++) {
                        try {
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                                termsOfUseId));
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_PROJECT_TERMS_OF_USE,
                                i, termsOfUseId));

                            result = instance.getTermsOfUse(i, resourceRoleId, null);
                            assertNotNull("List of TermsOfUse was null..", result);
                            assertEquals("Incorrect number of TermsOfUse retrieved.", 1, result.get(0).size());

                            termsOfUseId++;
                        } catch (Exception e) {
                            fail("Error occurred during test: " + e.toString());
                        }
                    }
                }
            });
        }

        executorService.shutdown();

        StressTestHelper.awaitTermination(executorService);

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link ProjectTermsOfUseDaoImpl#getTermsOfUse(int, int[], int[])} method.</p>
     * <p>
     * It tests the case that when filtering of terms of use groups by custom agreeability types.
     * terms of use in the group has agreeability type with the specified ID.
     * </p>
     *
     * @throws Exception if any error occurs
     * @since 1.1
     */
    @Test
    public void testGetTermsOfUse_FilterByAgreeabilityTypeIds1() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("ProjectTermsOfUseDaoImplStressTests"
            + "#testGetTermsOfUse_FilterByAgreeabilityTypeIds1");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        StressTestHelper.executeBatch(getConnection(), "test_files/stresstests/input/input3.sql");

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    Map<Integer, List<TermsOfUse>> result;
                    int resourceRoleId;

                    synchronized (syncLock) {
                        resourceRoleId = nextResourceRoleId;
                        nextResourceRoleId++;
                    }

                    for (int i = 1; i <= StressTestHelper.LOOPS; i++) {
                        try {
                            result = instance.getTermsOfUse(1, resourceRoleId, new int[] {0});
                            assertNotNull("List of TermsOfUse was null.", result);
                            assertEquals("Incorrect number of TermsOfUse retrieved.", 25, result.size());

                        } catch (Exception e) {
                            e.printStackTrace();
                            fail("Error occurred during test: " + e.toString());
                        }
                    }
                }
            });
        }

        StressTestHelper.awaitTermination(executorService);

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link ProjectTermsOfUseDaoImpl#getTermsOfUse(int, int[], int[])} method.</p>
     * <p>
     * It tests the case that when filtering of terms of use groups by custom agreeability types.
     * terms of use in the group has agreeability type with not specified ID.
     * </p>
     *
     * @throws Exception if any error occurs
     * @since 1.1
     */
    @Test
    public void testGetTermsOfUse_FilterByAgreeabilityTypeIds2() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("ProjectTermsOfUseDaoImplStressTests"
            + "#testGetTermsOfUse_FilterByAgreeabilityTypeIds2");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        StressTestHelper.executeBatch(getConnection(), "test_files/stresstests/input/input3.sql");

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    Map<Integer, List<TermsOfUse>> result;
                    int resourceRoleId;

                    synchronized (syncLock) {
                        resourceRoleId = nextResourceRoleId;
                        nextResourceRoleId++;
                    }

                    for (int i = 1; i <= StressTestHelper.LOOPS; i++) {
                        try {
                            result = instance.getTermsOfUse(1, resourceRoleId, new int[] {5});
                            assertNotNull("List of TermsOfUse was null.", result);
                            assertEquals("Incorrect number of TermsOfUse retrieved.", 0, result.size());

                        } catch (Exception e) {
                            e.printStackTrace();
                            fail("Error occurred during test: " + e.toString());
                        }
                    }
                }
            });
        }

        StressTestHelper.awaitTermination(executorService);

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link ProjectTermsOfUseDaoImpl#getTermsOfUse(int, int[], int[])} method.</p>
     * <p>
     * It tests the case that when filtering of terms of use groups by custom agreeability types.
     * terms of use in the group has agreeability type with one is not specified ID.
     * </p>
     *
     * @throws Exception if any error occurs
     * @since 1.1
     */
    @Test
    public void testGetTermsOfUse_FilterByAgreeabilityTypeIds3() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("ProjectTermsOfUseDaoImplStressTests"
            + "#testGetTermsOfUse_FilterByAgreeabilityTypeIds3");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        StressTestHelper.executeBatch(getConnection(), "test_files/stresstests/input/input3.sql");

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    Map<Integer, List<TermsOfUse>> result;
                    int resourceRoleId;

                    synchronized (syncLock) {
                        resourceRoleId = nextResourceRoleId;
                        nextResourceRoleId++;
                    }

                    for (int i = 1; i <= StressTestHelper.LOOPS; i++) {
                        try {
                            result = instance.getTermsOfUse(1, resourceRoleId, new int[] {1, 5});
                            assertNotNull("List of TermsOfUse was null.", result);
                            assertEquals("Incorrect number of TermsOfUse retrieved.", 25, result.size());

                        } catch (Exception e) {
                            e.printStackTrace();
                            fail("Error occurred during test: " + e.toString());
                        }
                    }
                }
            });
        }

        StressTestHelper.awaitTermination(executorService);

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link ProjectTermsOfUseDaoImpl#removeAllProjectRoleTermsOfUse(int)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testRemoveAllProjectRoleTermsOfUse() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("ProjectTermsOfUseDaoImplStressTests"
            + "#testRemoveAllProjectRoleTermsOfUse");

        long termsOfUseId = 1;
        for (int i = 0; i < StressTestHelper.LOOPS; i++) {
            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, termsOfUseId));
            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_PROJECT_TERMS_OF_USE, i, termsOfUseId));

            instance.removeAllProjectRoleTermsOfUse(i);

            assertEquals("ProjectRoleTermsOfUse hasn't been deleted.", 0, StressTestHelper.queryInt(getConnection(),
                String.format(SELECT_COUNT_PROJECT_TERMS_OF_USE, i)));

            termsOfUseId++;
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link ProjectTermsOfUseDaoImpl#removeAllProjectRoleTermsOfUse(int)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testRemoveAllProjectRoleTermsOfUseMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("ProjectTermsOfUseDaoImplStressTests"
            + "#testRemoveAllProjectRoleTermsOfUseMultithreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    long termsOfUseId = 1;
                    int currentThread;

                    synchronized (syncLock) {
                        currentThread = threadNumber++;
                    }

                    for (int i = currentThread * StressTestHelper.LOOPS; i <= (currentThread + 1)
                        * StressTestHelper.LOOPS; i++) {
                        try {
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                                termsOfUseId));
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_PROJECT_TERMS_OF_USE,
                                i, termsOfUseId));

                            instance.removeAllProjectRoleTermsOfUse(i);

                            assertEquals("ProjectRoleTermsOfUse hasn't been deleted.", 0, StressTestHelper.queryInt(
                                getConnection(), String.format(SELECT_COUNT_PROJECT_TERMS_OF_USE, i)));

                            termsOfUseId++;
                        } catch (Exception e) {
                            fail("Error occurred during test: " + e.toString());
                        }
                    }
                }
            });
        }

        StressTestHelper.awaitTermination(executorService);

        StressTestHelper.testExecutionCompleted(context);
    }
}
