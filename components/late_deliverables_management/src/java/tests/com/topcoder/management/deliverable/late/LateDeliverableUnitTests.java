/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Unit tests for {@link LateDeliverable} class.
 * </p>
 *
 * <p>
 * <em>Changes in version 1.0.6:</em>
 * <ol>
 * <li>Added test cases for type property.</li>
 * <li>Updated test cases for constructor and toString() method.</li>
 * </ol>
 * </p>
 *
 * @author sparemax
 * @version 1.0.6
 */
public class LateDeliverableUnitTests {
    /**
     * <p>
     * Represents the <code>LateDeliverable</code> instance used in tests.
     * </p>
     */
    private LateDeliverable instance;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(LateDeliverableUnitTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     */
    @Before
    public void setUp() {
        instance = new LateDeliverable();
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>LateDeliverable()</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    @Test
    public void testCtor() {
        instance = new LateDeliverable();

        assertEquals("'id' should be correct.", 0L, TestsHelper.getField(instance, "id"));
        assertNull("'type' should be correct.", TestsHelper.getField(instance, "type"));
        assertEquals("'projectId' should be correct.", 0L, TestsHelper.getField(instance, "projectId"));
        assertEquals("'projectPhaseId' should be correct.", 0L, TestsHelper.getField(instance, "projectPhaseId"));
        assertEquals("'resourceId' should be correct.", 0L, TestsHelper.getField(instance, "resourceId"));
        assertEquals("'deliverableId' should be correct.", 0L, TestsHelper.getField(instance, "deliverableId"));
        assertNull("'deadline' should be correct.", TestsHelper.getField(instance, "deadline"));
        assertNull("'createDate' should be correct.", TestsHelper.getField(instance, "createDate"));
        assertFalse("'forgiven' should be correct.", (Boolean) TestsHelper.getField(instance, "forgiven"));
        assertNull("'lastNotified' should be correct.", TestsHelper.getField(instance, "lastNotified"));
        assertNull("'delay' should be correct.", TestsHelper.getField(instance, "delay"));
        assertNull("'explanation' should be correct.", TestsHelper.getField(instance, "explanation"));
        assertNull("'response' should be correct.", TestsHelper.getField(instance, "response"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getId()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getId() {
        long value = 1;
        instance.setId(value);

        assertEquals("'getId' should be correct.", value, instance.getId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setId(long id)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setId() {
        long value = 1;
        instance.setId(value);

        assertEquals("'setId' should be correct.",
            value, TestsHelper.getField(instance, "id"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getType()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     *
     * @since 1.0.6
     */
    @Test
    public void test_getType() {
        LateDeliverableType value = new LateDeliverableType();
        instance.setType(value);

        assertSame("'getType' should be correct.", value, instance.getType());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setType(LateDeliverableType type)</code>.<br>
     * The value should be properly set.
     * </p>
     *
     * @since 1.0.6
     */
    @Test
    public void test_setType() {
        LateDeliverableType value = new LateDeliverableType();
        instance.setType(value);

        assertSame("'setType' should be correct.",
            value, TestsHelper.getField(instance, "type"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getProjectId()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getProjectId() {
        long value = 1;
        instance.setProjectId(value);

        assertEquals("'getProjectId' should be correct.", value, instance.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setProjectId(long projectId)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setProjectId() {
        long value = 1;
        instance.setProjectId(value);

        assertEquals("'setProjectId' should be correct.",
            value, TestsHelper.getField(instance, "projectId"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getProjectPhaseId()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getProjectPhaseId() {
        long value = 1;
        instance.setProjectPhaseId(value);

        assertEquals("'getProjectPhaseId' should be correct.", value, instance.getProjectPhaseId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setProjectPhaseId(long projectPhaseId)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setProjectPhaseId() {
        long value = 1;
        instance.setProjectPhaseId(value);

        assertEquals("'setProjectPhaseId' should be correct.",
            value, TestsHelper.getField(instance, "projectPhaseId"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getResourceId()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getResourceId() {
        long value = 1;
        instance.setResourceId(value);

        assertEquals("'getResourceId' should be correct.", value, instance.getResourceId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setResourceId(long resourceId)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setResourceId() {
        long value = 1;
        instance.setResourceId(value);

        assertEquals("'setResourceId' should be correct.",
            value, TestsHelper.getField(instance, "resourceId"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDeliverableId()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getDeliverableId() {
        long value = 1;
        instance.setDeliverableId(value);

        assertEquals("'getDeliverableId' should be correct.", value, instance.getDeliverableId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setDeliverableId(long deliverableId)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setDeliverableId() {
        long value = 1;
        instance.setDeliverableId(value);

        assertEquals("'setDeliverableId' should be correct.",
            value, TestsHelper.getField(instance, "deliverableId"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDeadline()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getDeadline() {
        Date value = new Date();
        instance.setDeadline(value);

        assertSame("'getDeadline' should be correct.", value, instance.getDeadline());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setDeadline(Date deadline)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setDeadline() {
        Date value = new Date();
        instance.setDeadline(value);

        assertSame("'setDeadline' should be correct.",
            value, TestsHelper.getField(instance, "deadline"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getCompensatedDeadline()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getCompensatedDeadline() {
        Date value = new Date();
        instance.setCompensatedDeadline(value);

        assertSame("'getCompensatedDeadline' should be correct.", value, instance.getCompensatedDeadline());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setCompensatedDeadline(Date compensatedDeadline)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setCompensatedDeadline() {
        Date value = new Date();
        instance.setCompensatedDeadline(value);

        assertSame("'setCompensatedDeadline' should be correct.",
            value, TestsHelper.getField(instance, "compensatedDeadline"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getCreateDate()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getCreateDate() {
        Date value = new Date();
        instance.setCreateDate(value);

        assertSame("'getCreateDate' should be correct.", value, instance.getCreateDate());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setCreateDate(Date createDate)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setCreateDate() {
        Date value = new Date();
        instance.setCreateDate(value);

        assertSame("'setCreateDate' should be correct.",
            value, TestsHelper.getField(instance, "createDate"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>isForgiven()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_isForgiven() {
        boolean value = true;
        instance.setForgiven(value);

        assertTrue("'isForgiven' should be correct.", instance.isForgiven());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setForgiven(boolean forgiven)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setForgiven() {
        boolean value = true;
        instance.setForgiven(value);

        assertTrue("'setForgiven' should be correct.", (Boolean) TestsHelper.getField(instance, "forgiven"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getLastNotified()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getLastNotified() {
        Date value = new Date();
        instance.setLastNotified(value);

        assertSame("'getLastNotified' should be correct.", value, instance.getLastNotified());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setLastNotified(Date lastNotified)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setLastNotified() {
        Date value = new Date();
        instance.setLastNotified(value);

        assertSame("'setLastNotified' should be correct.",
            value, TestsHelper.getField(instance, "lastNotified"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDelay()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getDelay() {
        Long value = 1L;
        instance.setDelay(value);

        assertSame("'getDelay' should be correct.", value, instance.getDelay());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setDelay(Long delay)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setDelay() {
        Long value = 1L;
        instance.setDelay(value);

        assertSame("'setDelay' should be correct.",
            value, TestsHelper.getField(instance, "delay"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getExplanation()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getExplanation() {
        String value = "new_value";
        instance.setExplanation(value);

        assertEquals("'getExplanation' should be correct.", value, instance.getExplanation());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setExplanation(String explanation)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setExplanation() {
        String value = "new_value";
        instance.setExplanation(value);

        assertEquals("'setExplanation' should be correct.",
            value, TestsHelper.getField(instance, "explanation"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getExplanationDate()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getExplanationDate() {
        Date value = new Date();
        instance.setExplanationDate(value);

        assertSame("'getExplanationDate' should be correct.", value, instance.getExplanationDate());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setExplanationDate(Date explanationDate)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setExplanationDate() {
        Date value = new Date();
        instance.setExplanationDate(value);

        assertSame("'setExplanationDate' should be correct.",
            value, TestsHelper.getField(instance, "explanationDate"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getResponse()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getResponse() {
        String value = "new_value";
        instance.setResponse(value);

        assertEquals("'getResponse' should be correct.", value, instance.getResponse());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setResponse(String response)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setResponse() {
        String value = "new_value";
        instance.setResponse(value);

        assertEquals("'setResponse' should be correct.",
            value, TestsHelper.getField(instance, "response"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getResponseUser()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getResponseUser() {
        String value = "12345";
        instance.setResponseUser(value);

        assertEquals("'getResponseUser' should be correct.", value, instance.getResponseUser());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setResponseUser(String responseUser)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setResponseUser() {
        String value = "12345";
        instance.setResponseUser(value);

        assertEquals("'setResponseUser' should be correct.",
            value, TestsHelper.getField(instance, "responseUser"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getCreateDate()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getResponseDate() {
        Date value = new Date();
        instance.setResponseDate(value);

        assertSame("'getCreateResponseDateld be correct.", value, instance.getResponseDate());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setResponseDate(Date responseDate)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setResponseDate() {
        Date value = new Date();
        instance.setResponseDate(value);

        assertSame("'setResponseDate' should be correct.",
            value, TestsHelper.getField(instance, "responseDate"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>toString()</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_toString_1() {
        instance = getLateDeliverable();

        String res = instance.toString();

        assertTrue("'toString' should be correct.", res.contains("id:1"));
        assertTrue("'toString' should be correct.", res.contains("lateDeliverableType:"));
        assertTrue("'toString' should be correct.", res.contains("name:The name"));
        assertTrue("'toString' should be correct.", res.contains("description:The description"));
        assertTrue("'toString' should be correct.", res.contains("projectId:6"));
        assertTrue("'toString' should be correct.", res.contains("projectPhaseId:2"));
        assertTrue("'toString' should be correct.", res.contains("resourceId:3"));
        assertTrue("'toString' should be correct.", res.contains("deliverableId:4"));
        assertTrue("'toString' should be correct.", res.contains("deadline:"));
        assertTrue("'toString' should be correct.", res.contains("compensatedDeadline:"));
        assertTrue("'toString' should be correct.", res.contains("createDate:"));
        assertTrue("'toString' should be correct.", res.contains("forgiven:true"));
        assertTrue("'toString' should be correct.", res.contains("lastNotified:"));
        assertTrue("'toString' should be correct.", res.contains("delay:5"));
        assertTrue("'toString' should be correct.", res.contains("explanation:The explanation"));
        assertTrue("'toString' should be correct.", res.contains("response:The response"));
        assertTrue("'toString' should be correct.", res.contains("responseUser:12345"));
        assertTrue("'toString' should be correct.", res.contains("responseDate:"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>toString()</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_toString_2() {
        instance = new LateDeliverable();
        String expected = LateDeliverable.class.getName()
            + "{id:0, lateDeliverableType:null, projectId:0, projectPhaseId:0, resourceId:0, deliverableId:0,"
            + " deadline:null, compensatedDeadline:null, createDate:null, forgiven:false, lastNotified:null,"
            + " delay:null, explanation:null, explanationDate:null, response:null, responseUser:null,"
            + " responseDate:null}";
        String res = instance.toString();

        assertEquals("'toString' should be correct.", expected, res);
    }

    /**
     * Creates an instance of LateDeliverable.
     *
     * @return the LateDeliverable instance.
     */
    private static LateDeliverable getLateDeliverable() {
        LateDeliverable lateDeliverable = new LateDeliverable();
        lateDeliverable.setId(1);
        LateDeliverableType type = new LateDeliverableType();
        type.setId(1);
        type.setName("The name");
        type.setDescription("The description");
        lateDeliverable.setType(type);
        lateDeliverable.setProjectId(6L);
        lateDeliverable.setProjectPhaseId(2);
        lateDeliverable.setResourceId(3);
        lateDeliverable.setDeliverableId(4);
        lateDeliverable.setDeadline(new Date());
        lateDeliverable.setCreateDate(new Date());
        lateDeliverable.setForgiven(true);
        lateDeliverable.setLastNotified(new Date());
        lateDeliverable.setDelay(5L);
        lateDeliverable.setExplanation("The explanation");
        lateDeliverable.setExplanationDate(new Date());
        lateDeliverable.setResponse("The response");
        lateDeliverable.setResponseUser("12345");
        lateDeliverable.setResponseDate(new Date());

        return lateDeliverable;
    }
}
