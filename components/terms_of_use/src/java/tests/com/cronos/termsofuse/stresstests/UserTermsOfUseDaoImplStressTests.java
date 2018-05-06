/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.stresstests;

import com.cronos.termsofuse.dao.impl.UserTermsOfUseDaoImpl;
import com.cronos.termsofuse.model.TermsOfUse;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * <p>Stress tests for {@link UserTermsOfUseDaoImpl}.</p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Updated the INSERT_TERMS_OF_USE queries.</li>
 * </ol>
 * </p>
 *
 * @author jmn, victorsam
 * @version 1.1
 */
public class UserTermsOfUseDaoImplStressTests extends BaseStressTests {

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
     * <p>Represents the query used for retrieving the number of user terms of use.</p>
     */
    private static final String QUERY_COUNT_USER_TERMS_OF_USE =
            "select count(*) from 'informix'.user_terms_of_use_xref where user_id = %d and terms_of_use_id = %d";

    /**
     * <p>Represents the sql used for inserting the user terms of use.</p>
     */
    private static final String INSERT_USER_TERMS_OF_USE =
            "INSERT INTO 'informix'.user_terms_of_use_xref (user_id, terms_of_use_id) VALUES (1, %d)";

    /**
     * <p>Represents the sql used for inserting the ban user terms of use.</p>
     */
    private static final String INSERT_BAN_USER_TERMS_OF_USE =
            "INSERT INTO 'informix'.user_terms_of_use_ban_xref (user_id, terms_of_use_id) VALUES (1, %d)";

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private UserTermsOfUseDaoImpl instance;

    /**
     * <p>Represents a syncroot object.</p>
     */
    private final Object syncLock = new Object();

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
        return new JUnit4TestAdapter(UserTermsOfUseDaoImplStressTests.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        instance = new UserTermsOfUseDaoImpl(loadConfiguration());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigName() {
        return "userTermsOfUseDao";
    }

    /**
     * <p>Tests the {@link UserTermsOfUseDaoImpl#removeUserTermsOfUse(long, long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateUserTermsOfUse() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted(
                "UserTermsOfUseDaoImpl#testCreateUserTermsOfUse");

        long userId = 1;
        for (int i = 1; i <= StressTestHelper.LOOPS; i++) {

            StressTestHelper.executeUpdate(getConnection(), String.format(
                    INSERT_TERMS_OF_USE, i));
            instance.createUserTermsOfUse(userId, i);
            assertEquals("UserTerms of use hasn't been created.", 1,
                    StressTestHelper.queryInt(getConnection(),
                            String.format(QUERY_COUNT_USER_TERMS_OF_USE, userId, i)));

            userId++;
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link UserTermsOfUseDaoImpl#removeUserTermsOfUse(long, long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateUserTermsOfUseMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted(
                "UserTermsOfUseDaoImpl#testCreateUserTermsOfUseMultithreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        threadNumber = 1;

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    long userId = 1;

                    int currentThread;

                    synchronized (syncLock) {
                        currentThread = threadNumber++;
                    }

                    for (int i = currentThread * StressTestHelper.LOOPS;
                         i <= (currentThread + 1) * StressTestHelper.LOOPS; i++) {
                        try {
                            StressTestHelper.executeUpdate(getConnection(), String.format(
                                    INSERT_TERMS_OF_USE, i));
                            instance.createUserTermsOfUse(userId, i);
                            assertEquals("UserTerms of use hasn't been created.", 1,
                                    StressTestHelper.queryInt(getConnection(),
                                            String.format(QUERY_COUNT_USER_TERMS_OF_USE, userId, i)));

                            userId++;
                        } catch (Exception e) {
                            fail("Error occurred during test: " + e.toString());
                        }
                    }
                }
            });
        }

        executorService.shutdown();

        while (!executorService.isTerminated()) {
            executorService.awaitTermination(30, TimeUnit.SECONDS);


            StressTestHelper.testExecutionCompleted(context);
        }
    }

    /**
     * <p>Tests the {@link UserTermsOfUseDaoImpl#removeUserTermsOfUse(long, long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testRemoveUserTermsOfUse() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted(
                "UserTermsOfUseDaoImpl#testRemoveUserTermsOfUse");

        long userId = 1;
        for (int i = 1; i <= StressTestHelper.LOOPS; i++) {
            StressTestHelper.executeUpdate(getConnection(), String.format(
                    INSERT_TERMS_OF_USE, i));
            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_USER_TERMS_OF_USE, i));

            instance.removeUserTermsOfUse(userId, i);

            assertEquals("UserTerms of use hasn't been created.", 0,
                    StressTestHelper.queryInt(getConnection(),
                            String.format(QUERY_COUNT_USER_TERMS_OF_USE, userId, i)));
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link UserTermsOfUseDaoImpl#removeUserTermsOfUse(long, long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testRemoveUserTermsOfUseMultithread() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted(
                "ProjectTermsOfUseDaoImplStressTests#testRemoveUserTermsOfUseMultithread");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        threadNumber = 1;

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    long userId = 1;

                    int currentThread;

                    synchronized (syncLock) {
                        currentThread = threadNumber++;
                    }

                    for (int i = currentThread * StressTestHelper.LOOPS;
                         i <= (currentThread + 1) * StressTestHelper.LOOPS; i++) {
                        try {
                            StressTestHelper.executeUpdate(getConnection(), String.format(
                                    INSERT_TERMS_OF_USE, i));
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_USER_TERMS_OF_USE, i));

                            instance.removeUserTermsOfUse(userId, i);

                            assertEquals("UserTerms of use hasn't been created.", 0,
                                    StressTestHelper.queryInt(getConnection(),
                                            String.format(QUERY_COUNT_USER_TERMS_OF_USE, userId, i)));
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
     * <p>Tests the {@link UserTermsOfUseDaoImpl#getTermsOfUseByUserId(long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetTermsOfUseByUserId() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted(
                "UserTermsOfUseDaoImpl#testGetTermsOfUseByUserId");

        long userId = 1;
        List<TermsOfUse> result;
        for (int i = 1; i <= StressTestHelper.LOOPS; i++) {
            StressTestHelper.executeUpdate(getConnection(), String.format(
                    INSERT_TERMS_OF_USE, i));
            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_USER_TERMS_OF_USE, i));

            result = instance.getTermsOfUseByUserId(userId);

            assertEquals("Incorrect number of terms of use retrieved for user.", i, result.size());
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link UserTermsOfUseDaoImpl#getTermsOfUseByUserId(long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetTermsOfUseByUserIdMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted(
                "UserTermsOfUseDaoImpl#testGetTermsOfUseByUserIdMultithreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        threadNumber = 1;

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    long userId = 1;
                    List<TermsOfUse> result;

                    int currentThread;

                    synchronized (syncLock) {
                        currentThread = threadNumber++;
                    }

                    for (int i = currentThread * StressTestHelper.LOOPS;
                         i <= (currentThread + 1) * StressTestHelper.LOOPS; i++) {
                        try {
                            StressTestHelper.executeUpdate(getConnection(), String.format(
                                    INSERT_TERMS_OF_USE, i));
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_USER_TERMS_OF_USE, i));

                            result = instance.getTermsOfUseByUserId(userId);

                            assertEquals("Incorrect number of terms of use retrieved for user.", i, result.size());
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
     * <p>Tests the {@link UserTermsOfUseDaoImpl#getUsersByTermsOfUseId(long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetUsersByTermsOfUseId() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted(
                "UserTermsOfUseDaoImpl#testGetUsersByTermsOfUseId");

        long userId = 1;
        List<Long> result;
        for (int i = 0; i < StressTestHelper.LOOPS; i++) {
            StressTestHelper.executeUpdate(getConnection(), String.format(
                    INSERT_TERMS_OF_USE, i));
            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_USER_TERMS_OF_USE, i));

            result = instance.getUsersByTermsOfUseId(i);
            assertEquals("Incorrect number of users retrieved.", 1, result.size());
            assertEquals("Incorrect user id.", userId, (long) result.get(0));
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link UserTermsOfUseDaoImpl#getUsersByTermsOfUseId(long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetUsersByTermsOfUseIdMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted(
                "ProjectTermsOfUseDaoImplStressTests#testGetUsersByTermsOfUseIdMultithreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        threadNumber = 1;

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    long userId = 1;
                    List<Long> result;

                    int currentThread;

                    synchronized (syncLock) {
                        currentThread = threadNumber++;
                    }

                    for (int i = currentThread * StressTestHelper.LOOPS;
                         i <= (currentThread + 1) * StressTestHelper.LOOPS; i++) {
                        try {
                            StressTestHelper.executeUpdate(getConnection(), String.format(
                                    INSERT_TERMS_OF_USE, i));
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_USER_TERMS_OF_USE, i));

                            result = instance.getUsersByTermsOfUseId(i);
                            assertEquals("Incorrect number of users retrieved.", 1, result.size());
                            assertEquals("Incorrect user id.", userId, (long) result.get(0));
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
     * <p>Tests the {@link UserTermsOfUseDaoImpl#hasTermsOfUse(long, long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testHasTermsOfUse() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted(
                "UserTermsOfUseDaoImpl#testHasTermsOfUse");

        long userId = 1;
        for (int i = 0; i < StressTestHelper.LOOPS; i++) {
            StressTestHelper.executeUpdate(getConnection(), String.format(
                    INSERT_TERMS_OF_USE, i));
            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_USER_TERMS_OF_USE, i));

            assertTrue("Method hasTermsOfUse returned invalid result.", instance.hasTermsOfUse(userId, i));
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link UserTermsOfUseDaoImpl#hasTermsOfUse(long, long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testHasTermsOfUseMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted(
                "UserTermsOfUseDaoImpl#testHasTermsOfUseMultithreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        threadNumber = 1;

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    long userId = 1;

                    int currentThread;

                    synchronized (syncLock) {
                        currentThread = threadNumber++;
                    }

                    for (int i = currentThread * StressTestHelper.LOOPS;
                         i <= (currentThread + 1) * StressTestHelper.LOOPS; i++) {
                        try {
                            StressTestHelper.executeUpdate(getConnection(), String.format(
                                    INSERT_TERMS_OF_USE, i));
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_USER_TERMS_OF_USE, i));

                            assertTrue("Method hasTermsOfUse returned invalid result.",
                                    instance.hasTermsOfUse(userId, i));
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
     * <p>Tests the {@link UserTermsOfUseDaoImpl#hasTermsOfUseBan(long, long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testHasTermsOfUseBan() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted(
                "UserTermsOfUseDaoImpl#testHasTermsOfUseBan");

        long userId = 1;
        for (int i = 0; i < StressTestHelper.LOOPS; i++) {
            StressTestHelper.executeUpdate(getConnection(), String.format(
                    INSERT_TERMS_OF_USE, i));
            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_BAN_USER_TERMS_OF_USE, i));

            assertTrue("Method hasTermsOfUse returned invalid result.", instance.hasTermsOfUseBan(userId, i));
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link UserTermsOfUseDaoImpl#hasTermsOfUseBan(long, long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testHasTermsOfUseBanMultithread() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted(
                "UserTermsOfUseDaoImpl#testHasTermsOfUseBanMultithread");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        threadNumber = 1;

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    long userId = 1;

                    int currentThread;

                    synchronized (syncLock) {
                        currentThread = threadNumber++;
                    }

                    for (int i = currentThread * StressTestHelper.LOOPS;
                         i <= (currentThread + 1) * StressTestHelper.LOOPS; i++) {
                        try {
                            StressTestHelper.executeUpdate(getConnection(), String.format(
                                    INSERT_TERMS_OF_USE, i));
                            StressTestHelper.executeUpdate(getConnection(),
                                    String.format(INSERT_BAN_USER_TERMS_OF_USE, i));

                            assertTrue("Method hasTermsOfUse returned invalid result.",
                                    instance.hasTermsOfUseBan(userId, i));
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
