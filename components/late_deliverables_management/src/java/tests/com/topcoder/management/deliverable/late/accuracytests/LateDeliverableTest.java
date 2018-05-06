/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.deliverable.late.accuracytests;

import com.topcoder.management.deliverable.late.LateDeliverable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * <p> Accuracy test for <code>LateDeliverable</code>. </p>
 *
 * @author yuanyeyuanye, TCSDEVELOPER
 * @version 1.0
 */
public class LateDeliverableTest {

    /**
     * <p> Instance of <code>LateDeliverable</code> used for test. </p>
     */
    private LateDeliverable instance;

    /**
     * <p> Sets up test environment. </p>
     *
     * @throws Exception to jUnit.
     */
    @Before
    public void setUp() throws Exception {
        instance = new LateDeliverable();
    }

    /**
     * <p> Tears down test environment. </p>
     *
     * @throws Exception to jUnit.
     */
    @After
    public void tearDown() throws Exception {
        instance = null;
    }

    /**
     * <p> Accuracy test for the constructor. </p>
     *
     * <p> Test the constructor can construct the instance correctly.</p>
     *
     * @throws Exception to jUnit.
     */
    @Test
    public void testCtor() throws Exception {
        assertNotNull(instance);
    }

    /**
     * <p> Accuracy test for <code>getDelay()</code>. </p>
     *
     * <p> Test the getter can retrieval value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_getDelay() throws Exception {
        Long delay = 1L;
        TestHelper.setPrivateField(instance, "delay", delay);

        assertEquals(delay, instance.getDelay());
    }

    /**
     * <p> Accuracy test for setter of <code>delay</code>. </p>
     *
     * <p> Test the setter of <code>delay</code> can set new value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_setDelay() throws Exception {
        Long delay = 1L;
        instance.setDelay(delay);
        assertEquals(delay, TestHelper.getPrivateField(instance, "delay"));
    }

    /**
     * <p> Accuracy test for <code>getForgiven()</code>. </p>
     *
     * <p> Test the getter can retrieval value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_getForgiven() throws Exception {
        boolean forgiven = true;
        TestHelper.setPrivateField(instance, "forgiven", forgiven);

        assertEquals(forgiven, instance.isForgiven());
    }

    /**
     * <p> Accuracy test for setter of <code>forgiven</code>. </p>
     *
     * <p> Test the setter of <code>forgiven</code> can set new value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_setForgiven() throws Exception {
        boolean forgiven = true;
        instance.setForgiven(forgiven);
        assertEquals(forgiven, TestHelper.getPrivateField(instance, "forgiven"));
    }

    /**
     * <p> Accuracy test for <code>getId()</code>. </p>
     *
     * <p> Test the getter can retrieval value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_getId() throws Exception {
        long id = 1L;
        TestHelper.setPrivateField(instance, "id", id);

        assertEquals(id, instance.getId());
    }

    /**
     * <p> Accuracy test for setter of <code>id</code>. </p>
     *
     * <p> Test the setter of <code>id</code> can set new value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_setId() throws Exception {
        long id = 1L;
        instance.setId(id);
        assertEquals(id, TestHelper.getPrivateField(instance, "id"));
    }

    /**
     * <p> Accuracy test for <code>getLastNotified()</code>. </p>
     *
     * <p> Test the getter can retrieval value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_getLastNotified() throws Exception {
        Date lastNotified = new Date();
        TestHelper.setPrivateField(instance, "lastNotified", lastNotified);

        assertEquals(lastNotified, instance.getLastNotified());
    }

    /**
     * <p> Accuracy test for setter of <code>lastNotified</code>. </p>
     *
     * <p> Test the setter of <code>lastNotified</code> can set new value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_setLastNotified() throws Exception {
        Date lastNotified = new Date();
        instance.setLastNotified(lastNotified);
        assertEquals(lastNotified, TestHelper.getPrivateField(instance, "lastNotified"));
    }

    /**
     * <p> Accuracy test for <code>getDeliverableId()</code>. </p>
     *
     * <p> Test the getter can retrieval value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_getDeliverableId() throws Exception {
        long deliverableId = 1L;
        TestHelper.setPrivateField(instance, "deliverableId", deliverableId);

        assertEquals(deliverableId, instance.getDeliverableId());
    }

    /**
     * <p> Accuracy test for setter of <code>deliverableId</code>. </p>
     *
     * <p> Test the setter of <code>deliverableId</code> can set new value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_setDeliverableId() throws Exception {
        long deliverableId = 1L;
        instance.setDeliverableId(deliverableId);
        assertEquals(deliverableId, TestHelper.getPrivateField(instance, "deliverableId"));
    }

    /**
     * <p> Accuracy test for <code>getResourceId()</code>. </p>
     *
     * <p> Test the getter can retrieval value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_getResourceId() throws Exception {
        long resourceId = 1L;
        TestHelper.setPrivateField(instance, "resourceId", resourceId);

        assertEquals(resourceId, instance.getResourceId());
    }

    /**
     * <p> Accuracy test for setter of <code>resourceId</code>. </p>
     *
     * <p> Test the setter of <code>resourceId</code> can set new value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_setResourceId() throws Exception {
        long resourceId = 1L;
        instance.setResourceId(resourceId);
        assertEquals(resourceId, TestHelper.getPrivateField(instance, "resourceId"));
    }

    /**
     * <p> Accuracy test for <code>getCreateDate()</code>. </p>
     *
     * <p> Test the getter can retrieval value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_getCreateDate() throws Exception {
        Date createDate = new Date();
        TestHelper.setPrivateField(instance, "createDate", createDate);

        assertEquals(createDate, instance.getCreateDate());
    }

    /**
     * <p> Accuracy test for setter of <code>createDate</code>. </p>
     *
     * <p> Test the setter of <code>createDate</code> can set new value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_setCreateDate() throws Exception {
        Date createDate = new Date();
        instance.setCreateDate(createDate);
        assertEquals(createDate, TestHelper.getPrivateField(instance, "createDate"));
    }

    /**
     * <p> Accuracy test for <code>getProjectPhaseId()</code>. </p>
     *
     * <p> Test the getter can retrieval value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_getProjectPhaseId() throws Exception {
        long projectPhaseId = 1L;
        TestHelper.setPrivateField(instance, "projectPhaseId", projectPhaseId);

        assertEquals(projectPhaseId, instance.getProjectPhaseId());
    }

    /**
     * <p> Accuracy test for setter of <code>projectPhaseId</code>. </p>
     *
     * <p> Test the setter of <code>projectPhaseId</code> can set new value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_setProjectPhaseId() throws Exception {
        long projectPhaseId = 1L;
        instance.setProjectPhaseId(projectPhaseId);
        assertEquals(projectPhaseId, TestHelper.getPrivateField(instance, "projectPhaseId"));
    }

    /**
     * <p> Accuracy test for <code>getExplanation()</code>. </p>
     *
     * <p> Test the getter can retrieval value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_getExplanation() throws Exception {
        String explanation = "expected";
        TestHelper.setPrivateField(instance, "explanation", explanation);

        assertEquals(explanation, instance.getExplanation());
    }

    /**
     * <p> Accuracy test for setter of <code>explanation</code>. </p>
     *
     * <p> Test the setter of <code>explanation</code> can set new value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_setExplanation() throws Exception {
        String explanation = "expected";
        instance.setExplanation(explanation);
        assertEquals(explanation, TestHelper.getPrivateField(instance, "explanation"));
    }

    /**
     * <p> Accuracy test for <code>getResponse()</code>. </p>
     *
     * <p> Test the getter can retrieval value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_getResponse() throws Exception {
        String response = "expected";
        TestHelper.setPrivateField(instance, "response", response);

        assertEquals(response, instance.getResponse());
    }

    /**
     * <p> Accuracy test for setter of <code>response</code>. </p>
     *
     * <p> Test the setter of <code>response</code> can set new value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_setResponse() throws Exception {
        String response = "expected";
        instance.setResponse(response);
        assertEquals(response, TestHelper.getPrivateField(instance, "response"));
    }

    /**
     * <p> Accuracy test for <code>getDeadline()</code>. </p>
     *
     * <p> Test the getter can retrieval value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_getDeadline() throws Exception {
        Date deadline = new Date();
        TestHelper.setPrivateField(instance, "deadline", deadline);

        assertEquals(deadline, instance.getDeadline());
    }

    /**
     * <p> Accuracy test for setter of <code>deadline</code>. </p>
     *
     * <p> Test the setter of <code>deadline</code> can set new value correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_setDeadline() throws Exception {
        Date deadline = new Date();
        instance.setDeadline(deadline);
        assertEquals(deadline, TestHelper.getPrivateField(instance, "deadline"));
    }

}
