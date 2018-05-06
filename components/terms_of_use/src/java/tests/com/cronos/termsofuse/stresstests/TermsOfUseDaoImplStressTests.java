/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.stresstests;

import com.cronos.termsofuse.dao.EntityNotFoundException;
import com.cronos.termsofuse.dao.TermsOfUseCyclicDependencyException;
import com.cronos.termsofuse.dao.TermsOfUsePersistenceException;
import com.cronos.termsofuse.dao.impl.TermsOfUseDaoImpl;
import com.cronos.termsofuse.model.TermsOfUse;
import com.cronos.termsofuse.model.TermsOfUseAgreeabilityType;
import com.cronos.termsofuse.model.TermsOfUseType;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * <p>Stress tests for {@link TermsOfUseDaoImpl}.</p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Added test methods for managing terms of use dependencies.</li>
 * <li>Updated queries to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.</li>
 * <li>Updated queries to support adding of TermsOfUse#agreeabilityType property.</li>
 * </ol>
 * </p>
 *
 * @author jmn, victorsam
 * @version 1.1
 */
public class TermsOfUseDaoImplStressTests extends BaseStressTests {
    /**
     * <p>Represents the sql used for selecting count from informix terms of use dependency.</p>
     * @since 1.1
     */
    private static final String SELECT_COUNT_FROM_INFORMIX_TERMS_OF_USE_DEPENDENCY = "select count(*) "
        + "from 'informix'.terms_of_use_dependency";

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
    private static final String INSERT_TERMS_OF_USE = "INSERT INTO 'informix'.terms_of_use "
        + "(terms_of_use_id, terms_of_use_type_id, title,"
        + " url, terms_of_use_agreeability_type_id) VALUES(%d, 1, 'title', 'url', 1)";

    /**
     * <p>Represents the sql query used for retrieving the count of the terms of use.</p>
     */
    private static final String SELECT_COUNT = "select count(*) from 'informix'.terms_of_use";

    /**
     * <p>Represents the sql used for inserting the TermsOfUse title.</p>
     */
    private static final String QUERY_TERMS_OF_USE_TITLE = "select title from "
        + "'informix'.terms_of_use where terms_of_use_id = %d";

    /**
     * <p>Represents the sql used for inserting the TermsOfUseType.</p>
     */
    private static final String INSERT_TERMS_OF_USE_TYPE = "INSERT INTO "
        + "'informix'.terms_of_use_type (terms_of_use_type_id," + " terms_of_use_type_desc) VALUES(%d, 'type_new')";

    /**
     * <p>Updates the terms of use text.</p>
     */
    private static final String UPDATE_TERMS_OF_USE_TEXT = "update 'informix'.terms_of_use "
        + "set terms_text = ? where terms_of_use_id = %d";

    /**
     * <p>Represents the sql query used for retrieving the TermsOfUse text.</p>
     */
    private static final String SELECT_TERMS_OF_USE_TEXT = "select terms_text from "
        + "'informix'.terms_of_use where terms_of_use_id = %d";

    /**
     * <p>Represents the sql query used for retrieving the TermsOfUseType description.</p>
     */
    private static final String SELECT_TERMS_OF_USE_TYPE_DESCRIPTION = "select terms_of_use_type_desc "
        + "from 'informix'.terms_of_use_type where terms_of_use_type_id = %d";

    /**
     * <p>Represents the instance of tested class.</p>
     */
    private TermsOfUseDaoImpl instance;

    /**
     * <p>Represents a syncroot object.</p>
     */
    private final Object syncLock = new Object();

    /**
     * <p>Represents the number of thread.</p>
     */
    private int threadNumber;

    /**
     * <p>Represents the terms of use Id.</p>
     * @since 1.1
     */
    private long termsOfUseId;

    /**
     * <p>Returns test suite for this class.</p>
     *
     * @return test suite for this class
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TermsOfUseDaoImplStressTests.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        instance = new TermsOfUseDaoImpl(loadConfiguration());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigName() {
        return "termsOfUseDao";
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#createTermsOfUse(TermsOfUse, String)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateTermsOfUse() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl#testCreateTermsOfUse");

        TermsOfUse termsOfUse = createTermsOfUse();
        String termsText = loadTermsOfUseText();
        for (int i = 0; i < StressTestHelper.LOOPS; i++) {

            instance.createTermsOfUse(termsOfUse, termsText);
            assertEquals("Invalid count of TermsOfUse.", i + 1,
                StressTestHelper.queryInt(getConnection(), SELECT_COUNT));
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#createTermsOfUse(TermsOfUse, String)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateTermsOfUseMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl"
            + "#testCreateTermsOfUseMultithreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    try {
                        TermsOfUse termsOfUse;
                        String termsText = loadTermsOfUseText();
                        for (int i = 0; i < StressTestHelper.LOOPS; i++) {

                            termsOfUse = createTermsOfUse();
                            instance.createTermsOfUse(termsOfUse, termsText);

                        }
                    } catch (TermsOfUsePersistenceException e) {
                        fail("Error occurred during test: " + e.toString());
                    } catch (IOException e) {
                        fail("Error occurred during test: " + e.toString());
                    }
                }
            });
        }

        StressTestHelper.awaitTermination(executorService);

        assertEquals("Invalid count of TermsOfUse.", StressTestHelper.THREADS * StressTestHelper.LOOPS,
            StressTestHelper.queryInt(getConnection(), SELECT_COUNT));

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#updateTermsOfUse(TermsOfUse)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testUpdateTermsOfUse() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl#testUpdateTermsOfUse");

        TermsOfUse termsOfUse;
        for (int i = 1; i <= StressTestHelper.LOOPS; i++) {
            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, i));
            termsOfUse = createTermsOfUse();
            termsOfUse.setTermsOfUseId(i);
            termsOfUse.setTitle("new title");
            instance.updateTermsOfUse(termsOfUse);
            assertEquals("The TermsOfUse hasn't been updated.", "new title", StressTestHelper.queryString(
                getConnection(), String.format(QUERY_TERMS_OF_USE_TITLE, i)));
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#updateTermsOfUse(TermsOfUse)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testUpdateTermsOfUseMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl"
            + "#testUpdateTermsOfUseMultithreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        String termsText = loadTermsOfUseText();
                        TermsOfUse termsOfUse;
                        for (int i = 0; i < StressTestHelper.LOOPS; i++) {

                            termsOfUse = createTermsOfUse();
                            instance.createTermsOfUse(termsOfUse, termsText);
                            termsOfUse.setTitle("new title");
                            instance.updateTermsOfUse(termsOfUse);
                            assertEquals("The TermsOfUse hasn't been updated.", "new title",
                                StressTestHelper.queryString(getConnection(), String.format(QUERY_TERMS_OF_USE_TITLE,
                                    termsOfUse.getTermsOfUseId())));
                        }
                    } catch (TermsOfUsePersistenceException e) {
                        fail("Error occurred during test: " + e.toString());
                    } catch (SQLException e) {
                        fail("Error occurred during test: " + e.toString());
                    } catch (EntityNotFoundException e) {
                        fail("Error occurred during test: " + e.toString());
                    } catch (Exception e) {
                        fail("Error occurred during test: " + e.toString());
                    }
                }
            });
        }

        StressTestHelper.awaitTermination(executorService);

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#getTermsOfUse(long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetTermsOfUse() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl#testGetTermsOfUse");

        TermsOfUse result;
        for (int i = 1; i <= StressTestHelper.LOOPS; i++) {

            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, i));
            result = instance.getTermsOfUse(i);
            assertTermsOfUse(result, i);
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#deleteTermsOfUse(long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testDeleteTermsOfUse() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl#testDeleteTermsOfUse");

        for (int i = 1; i <= StressTestHelper.LOOPS; i++) {

            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, i));
            instance.deleteTermsOfUse(i);
            assertEquals("The terms of use hasn't been deleted.", 0, StressTestHelper.queryInt(getConnection(),
                SELECT_COUNT));
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#deleteTermsOfUse(long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testDeleteTermsOfUseMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl"
            + "#testDeleteTermsOfUseMultithreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        threadNumber = 1;

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        int currentThread;

                        synchronized (syncLock) {
                            currentThread = threadNumber++;
                        }

                        for (int i = currentThread * StressTestHelper.LOOPS; i <= (currentThread + 1)
                            * StressTestHelper.LOOPS; i++) {

                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, i));
                            instance.deleteTermsOfUse(i);
                        }
                    } catch (SQLException e) {
                        fail("Error occurred during test: " + e.toString());
                    } catch (TermsOfUsePersistenceException e) {
                        fail("Error occurred during test: " + e.toString());
                    } catch (EntityNotFoundException e) {
                        fail("Error occurred during test: " + e.toString());
                    } catch (Exception e) {
                        fail("Error occurred during test: " + e.toString());
                    }
                }
            });
        }

        StressTestHelper.awaitTermination(executorService);

        assertEquals("Not all terms of use has been deleted.", 0, StressTestHelper.queryInt(getConnection(),
            SELECT_COUNT));

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#getTermsOfUseByTypeId(int)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetTermsOfUseByTypeId() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl#testGetTermsOfUseByTypeId");

        List<TermsOfUse> result;
        for (int i = 1; i <= StressTestHelper.LOOPS; i++) {

            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, i));
            result = instance.getTermsOfUseByTypeId(1);
            for (TermsOfUse termsOfUse : result) {
                assertTermsOfUse(termsOfUse, termsOfUse.getTermsOfUseId());
            }
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#getAllTermsOfUse()}  method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetAllTermsOfUse() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl#testGetAllTermsOfUse");

        List<TermsOfUse> result;
        for (int i = 1; i <= StressTestHelper.LOOPS; i++) {

            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, i));
            result = instance.getAllTermsOfUse();
            for (TermsOfUse termsOfUse : result) {
                assertTermsOfUse(termsOfUse, termsOfUse.getTermsOfUseId());
            }
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#getTermsOfUseType(int)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetTermsOfUseType() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl#testGetTermsOfUseType");

        int termsOfUseType = 1;
        TermsOfUseType result;
        for (int i = 0; i < StressTestHelper.LOOPS; i++) {

            result = instance.getTermsOfUseType(termsOfUseType);
            assertEquals("Invalid TermsOfUseType property value.", 1, result.getTermsOfUseTypeId());
            assertEquals("Invalid TermsOfUseType property value.", "type1", result.getDescription());
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#updateTermsOfUseType(TermsOfUseType)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testUpdateTermsOfUseType() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl#testUpdateTermsOfUseType");

        TermsOfUseType termsOfUseType;
        int offset = 5;
        for (int i = 1 + offset; i <= StressTestHelper.LOOPS + offset; i++) {
            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE_TYPE, i));
            termsOfUseType = createTermsOfUseType();
            termsOfUseType.setTermsOfUseTypeId(i);
            termsOfUseType.setDescription("new_description");
            instance.updateTermsOfUseType(termsOfUseType);
            assertEquals("The TermsOfUseType hasn't been updated.", "new_description", StressTestHelper.queryString(
                getConnection(), String.format(SELECT_TERMS_OF_USE_TYPE_DESCRIPTION, i)));
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#getTermsOfUseText(long)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetTermsOfUseText() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl#testGetTermsOfUseText");

        String text = loadTermsOfUseText();
        String result;
        for (int i = 1; i <= StressTestHelper.LOOPS; i++) {

            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, i));
            StressTestHelper.setBlob(getConnection(), String.format(UPDATE_TERMS_OF_USE_TEXT, i), text);
            result = instance.getTermsOfUseText(i);
            assertEquals("Invalid TermsOfUse text.", text, result);
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#setTermsOfUseText(long, String)} method.</p>
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testSetTermsOfUseText() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl#testSetTermsOfUseText");

        String text = loadTermsOfUseText();
        for (int i = 1; i <= StressTestHelper.LOOPS; i++) {

            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, i));
            instance.setTermsOfUseText(i, text);
            assertEquals("The TermsOfUse has invalid text.", text, StressTestHelper.getBlob(getConnection(),
                String.format(SELECT_TERMS_OF_USE_TEXT, i)));
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#createDependencyRelationship(long, long)} method.</p>
     *
     * @throws Exception if any error occurs
     * @since 1.1
     */
    @Test
    public void testCreateDependencyRelationship() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl"
            + "#testCreateDependencyRelationship");

        long id = 1;
        for (int i = 0; i < StressTestHelper.LOOPS; i++) {
            long dependencyTermsOfUseId = id;
            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, dependencyTermsOfUseId));
            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                dependencyTermsOfUseId + 1));

            instance.createDependencyRelationship(++id, dependencyTermsOfUseId);

            assertEquals("Failed to create dependency relationship correctly.", i + 1, StressTestHelper.queryInt(
                getConnection(), SELECT_COUNT_FROM_INFORMIX_TERMS_OF_USE_DEPENDENCY));
            ++id;
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#createDependencyRelationship(long, long)} method.</p>
     *
     * @throws Exception if any error occurs
     * @since 1.1
     */
    @Test
    public void testCreateDependencyRelationshipMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl"
            + "#testCreateDependencyRelationshipMultithreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    long id = 1;
                    int currentThread;

                    synchronized (syncLock) {
                        currentThread = threadNumber++;
                    }

                    try {
                        for (int i = currentThread * StressTestHelper.LOOPS; i <= (currentThread + 1)
                            * StressTestHelper.LOOPS; i++) {

                            long dependencyTermsOfUseId = id;
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                                dependencyTermsOfUseId));
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                                dependencyTermsOfUseId + 1));

                            instance.createDependencyRelationship(++id, dependencyTermsOfUseId);

                            assertEquals("Failed to create dependency relationship correctly.", i + 1,
                                StressTestHelper.queryInt(getConnection(),
                                    SELECT_COUNT_FROM_INFORMIX_TERMS_OF_USE_DEPENDENCY));
                            ++id;
                        }
                    } catch (Exception e) {
                        fail("Error occurred during test: " + e.toString());
                    }
                }
            });
        }

        StressTestHelper.awaitTermination(executorService);

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>
     * Tests the {@link TermsOfUseDaoImpl#createDependencyRelationship(long, long)} method.
     * It tests the case when A->B exists, B->C, C->A won't be created at the same time.
     * </p>
     *
     * @throws Exception if any error occurs
     * @since 1.1
     */
    @Test
    public void testCreateDependencyRelationshipMultithreaded2() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl"
            + "#testCreateDependencyRelationshipMultithreaded2");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {

                    long id = 1;
                    int currentThread;

                    synchronized (syncLock) {
                        currentThread = threadNumber++;
                    }

                    for (int i = currentThread * StressTestHelper.LOOPS; i <= (currentThread + 1)
                        * StressTestHelper.LOOPS; i++) {

                        try {
                            long dependencyTermsOfUseId = id;
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                                dependencyTermsOfUseId));
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                                dependencyTermsOfUseId + 1));
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                                dependencyTermsOfUseId + 2));

                            instance.createDependencyRelationship(dependencyTermsOfUseId, dependencyTermsOfUseId + 1);
                            instance.createDependencyRelationship(dependencyTermsOfUseId + 1,
                                dependencyTermsOfUseId + 2);
                            instance.createDependencyRelationship(dependencyTermsOfUseId + 2, dependencyTermsOfUseId);

                            fail("TermsOfUseCyclicDependencyException expected.");
                        } catch (TermsOfUseCyclicDependencyException e) {
                            // good
                            id = id + 4;
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
     * <p>Tests the {@link TermsOfUseDaoImpl#getDependencyTermsOfUse(long)} method.</p>
     *
     * @throws Exception if any error occurs
     * @since 1.1
     */
    @Test
    public void testGetDependencyTermsOfUse() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl#testGetDependencyTermsOfUse");

        long dependentTermsOfUseId = 1;
        StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, 1));

        for (int i = 0; i < StressTestHelper.LOOPS; i++) {

            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                dependentTermsOfUseId + 1));

            instance.createDependencyRelationship(1, ++dependentTermsOfUseId);

            List<TermsOfUse> terms = instance.getDependencyTermsOfUse(1);

            assertEquals("Failed to get dependency relationship correctly.", i + 1, terms.size());
            ++dependentTermsOfUseId;
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#getDependentTermsOfUse(long)} method.</p>
     *
     * @throws Exception if any error occurs
     * @since 1.1
     */
    @Test
    public void testGetDependentTermsOfUse() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl#testGetDependentTermsOfUse");

        long dependencyTermsOfUseId = 1;
        StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, 1));

        for (int i = 0; i < StressTestHelper.LOOPS; i++) {

            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                dependencyTermsOfUseId + 1));

            instance.createDependencyRelationship(++dependencyTermsOfUseId, 1);

            List<TermsOfUse> terms = instance.getDependentTermsOfUse(1);

            assertEquals("Failed to get dependent relationship correctly.", i + 1, terms.size());
            ++dependencyTermsOfUseId;
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#deleteDependencyRelationship(long, long)} method.</p>
     *
     * @throws Exception if any error occurs
     * @since 1.1
     */
    @Test
    public void testDeleteDependencyRelationship() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl#testDeleteDependencyRelationship");
        StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, 1));
        for (int i = 1; i <= StressTestHelper.LOOPS; i++) {

            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, i + 1));

            instance.createDependencyRelationship(1, i + 1);
            instance.deleteDependencyRelationship(1, i + 1);

            assertEquals("Failed to delete dependency relationship correctly.", 0, StressTestHelper.queryInt(
                getConnection(), SELECT_COUNT_FROM_INFORMIX_TERMS_OF_USE_DEPENDENCY));
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#deleteAllDependencyRelationshipsForDependent(long)} method.</p>
     *
     * @throws Exception if any error occurs
     * @since 1.1
     */
    @Test
    public void testDeleteDependencyRelationshipMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl"
            + "#testDeleteDependencyRelationshipMultithreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        threadNumber = 1;
        termsOfUseId = 1;
        StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, 1));

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        int currentThread;

                        synchronized (syncLock) {
                            currentThread = threadNumber++;
                        }

                        for (int i = currentThread * StressTestHelper.LOOPS; i <= (currentThread + 1)
                            * StressTestHelper.LOOPS; i++) {
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                                termsOfUseId + 1));

                            long dependencyTermsOfUseId = ++termsOfUseId;
                            instance.createDependencyRelationship(1, dependencyTermsOfUseId);
                            instance.deleteDependencyRelationship(1, dependencyTermsOfUseId);
                        }
                    } catch (Exception e) {
                        fail("Error occurred during test: " + e.toString());
                    }
                }
            });
        }

        StressTestHelper.awaitTermination(executorService);

        assertEquals("Failed to delete dependency relationship correctly.", 0, StressTestHelper.queryInt(
            getConnection(), SELECT_COUNT_FROM_INFORMIX_TERMS_OF_USE_DEPENDENCY));

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#deleteAllDependencyRelationshipsForDependent(long)} method.</p>
     *
     * @throws Exception if any error occurs
     * @since 1.1
     */
    @Test
    public void testDeleteAllDependencyRelationshipsForDependent() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl"
            + "#testDeleteAllDependencyRelationshipsForDependent");

        long dependentTermsOfUseId = 1;
        StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, 1));

        for (int i = 0; i < StressTestHelper.LOOPS; i++) {

            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                dependentTermsOfUseId + 1));

            instance.createDependencyRelationship(1, ++dependentTermsOfUseId);

            instance.deleteAllDependencyRelationshipsForDependent(1);

            assertEquals("Failed to delete all dependency relationships for dependent correctly.", 0,
                StressTestHelper.queryInt(getConnection(), SELECT_COUNT_FROM_INFORMIX_TERMS_OF_USE_DEPENDENCY));
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#deleteAllDependencyRelationshipsForDependent(long)} method.</p>
     *
     * @throws Exception if any error occurs
     * @since 1.1
     */
    @Test
    public void testDeleteAllDependencyRelationshipsForDependentMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl"
            + "#testDeleteAllDependencyRelationshipsForDependent");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        threadNumber = 1;
        termsOfUseId = 1;
        StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, 1));

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        int currentThread;

                        synchronized (syncLock) {
                            currentThread = threadNumber++;
                        }

                        for (int i = currentThread * StressTestHelper.LOOPS; i <= (currentThread + 1)
                            * StressTestHelper.LOOPS; i++) {
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                                termsOfUseId + 1));

                            instance.createDependencyRelationship(1, ++termsOfUseId);

                            instance.deleteAllDependencyRelationshipsForDependent(1);
                        }
                    } catch (Exception e) {
                        fail("Error occurred during test: " + e.toString());
                    }
                }
            });
        }

        StressTestHelper.awaitTermination(executorService);

        assertEquals("Failed to delete all dependency relationships for dependent correctly.", 0,
            StressTestHelper.queryInt(getConnection(), SELECT_COUNT_FROM_INFORMIX_TERMS_OF_USE_DEPENDENCY));

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#deleteAllDependencyRelationshipsForDependency(long)} method.</p>
     *
     * @throws Exception if any error occurs
     * @since 1.1
     */
    @Test
    public void testDeleteAllDependencyRelationshipsForDependency() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl"
            + "#testDeleteAllDependencyRelationshipsForDependency");

        long dependencyTermsOfUseId = 1;
        StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, 1));

        for (int i = 0; i < StressTestHelper.LOOPS; i++) {

            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                dependencyTermsOfUseId + 1));

            instance.createDependencyRelationship(++dependencyTermsOfUseId, 1);

            instance.deleteAllDependencyRelationshipsForDependency(1);

            assertEquals("Failed to delete all dependency relationships for dependency correctly.", 0,
                StressTestHelper.queryInt(getConnection(), SELECT_COUNT_FROM_INFORMIX_TERMS_OF_USE_DEPENDENCY));
        }

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Tests the {@link TermsOfUseDaoImpl#deleteAllDependencyRelationshipsForDependent(long)} method.</p>
     *
     * @throws Exception if any error occurs
     * @since 1.1
     */
    @Test
    public void testDeleteAllDependencyRelationshipsForDependencyMultithreaded() throws Exception {

        StressTestHelper.StressTestContext context = StressTestHelper.testExecutionStarted("TermsOfUseDaoImpl"
            + "#testDeleteAllDependencyRelationshipsForDependencyMultithreaded");

        ExecutorService executorService = Executors.newFixedThreadPool(StressTestHelper.THREADS);

        threadNumber = 1;
        termsOfUseId = 1;
        StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE, 1));

        for (int i = 0; i < StressTestHelper.THREADS; i++) {
            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        int currentThread;

                        synchronized (syncLock) {
                            currentThread = threadNumber++;
                        }

                        for (int i = currentThread * StressTestHelper.LOOPS; i <= (currentThread + 1)
                            * StressTestHelper.LOOPS; i++) {
                            StressTestHelper.executeUpdate(getConnection(), String.format(INSERT_TERMS_OF_USE,
                                termsOfUseId + 1));

                            instance.createDependencyRelationship(++termsOfUseId, 1);

                            instance.deleteAllDependencyRelationshipsForDependency(1);
                        }
                    } catch (Exception e) {
                        fail("Error occurred during test: " + e.toString());
                    }
                }
            });
        }

        StressTestHelper.awaitTermination(executorService);

        assertEquals("Failed to delete all dependency relationships for dependency correctly.", 0,
            StressTestHelper.queryInt(getConnection(), SELECT_COUNT_FROM_INFORMIX_TERMS_OF_USE_DEPENDENCY));

        StressTestHelper.testExecutionCompleted(context);
    }

    /**
     * <p>Creates new instance of {@link TermsOfUse} class.</p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Properties memberAgreeable and electronicallySignable are removed from TermsOfUse entity.
     * and agreeabilityType property is added.</li>
     * </ol>
     * </p>
     *
     * @return the new instance of {@link TermsOfUse}
     */
    private TermsOfUse createTermsOfUse() {

        TermsOfUse termsOfUse = new TermsOfUse();
        termsOfUse.setTitle("test");
        termsOfUse.setTermsOfUseTypeId(1);
        termsOfUse.setUrl("test_url");
        TermsOfUseAgreeabilityType agreeabilityType = new TermsOfUseAgreeabilityType();
        agreeabilityType.setTermsOfUseAgreeabilityTypeId(1);
        termsOfUse.setAgreeabilityType(agreeabilityType);
        return termsOfUse;
    }

    /**
     * <p>Asserts the given {@link TermsOfUse} instance.</p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Properties memberAgreeable and electronicallySignable are removed from TermsOfUse entity.
     * and agreeabilityType property is added.</li>
     * </ol>
     * </p>
     *
     * @param termsOfUse the {@link TermsOfUse} instance
     * @param id         the identifier
     */
    private void assertTermsOfUse(TermsOfUse termsOfUse, long id) {
        assertEquals("Invalid TermsOfUse property value.", id, termsOfUse.getTermsOfUseId());
        assertEquals("Invalid TermsOfUse property value.", 1, termsOfUse.getTermsOfUseTypeId());
        assertEquals("Invalid TermsOfUse property value.", "title", termsOfUse.getTitle());
        assertEquals("Invalid TermsOfUse property value.", "url", termsOfUse.getUrl());
        assertEquals("Invalid TermsOfUse property value.", 1,
            termsOfUse.getAgreeabilityType().getTermsOfUseAgreeabilityTypeId());
    }

    /**
     * <p>Creates new instance of {@link TermsOfUseType} class.</p>
     *
     * @return the new instance of {@link TermsOfUseType}
     */
    private TermsOfUseType createTermsOfUseType() {

        return new TermsOfUseType();
    }

    /**
     * <p>Loads the terms of use from text.</p>
     *
     * @return the terms of use text
     *
     * @throws IOException if any exception occurs when reading file content
     */
    private String loadTermsOfUseText() throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        String line;
        try {
            bufferedReader = new BufferedReader(new FileReader("test_files/stresstests/term_text.txt"));

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            return stringBuilder.toString();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    // ignores exception
                }
            }
        }
    }
}
