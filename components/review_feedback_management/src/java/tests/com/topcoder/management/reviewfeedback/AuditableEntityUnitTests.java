/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback;

import java.util.Date;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit tests for {@link AuditableEntity} class.
 * </p>
 *
 * @author sparemax
 * @version 2.0
 * @since 2.0
 */
public class AuditableEntityUnitTests extends TestCase {
    /**
     * <p>
     * Represents the <code>AuditableEntity</code> instance used in tests.
     * </p>
     */
    private AuditableEntity instance;

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(AuditableEntityUnitTests.class);
        return suite;
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Override
    protected void setUp() throws Exception {
        instance = new MockAuditableEntity();
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>AuditableEntity()</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    public void testCtor() {
        instance = new MockAuditableEntity();

        assertNull("'createUser' should be correct.", BaseUnitTest.getField(instance, "createUser"));
        assertNull("'createDate' should be correct.", BaseUnitTest.getField(instance, "createDate"));
        assertNull("'modifyUser' should be correct.", BaseUnitTest.getField(instance, "modifyUser"));
        assertNull("'modifyDate' should be correct.", BaseUnitTest.getField(instance, "modifyDate"));
    }


    /**
     * <p>
     * Accuracy test for the method <code>getCreateUser()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    public void test_getCreateUser() {
        String value = "new_value";
        instance.setCreateUser(value);

        assertEquals("'getCreateUser' should be correct.",
            value, instance.getCreateUser());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setCreateUser(String createUser)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    public void test_setCreateUser() {
        String value = "new_value";
        instance.setCreateUser(value);

        assertEquals("'setCreateUser' should be correct.",
            value, BaseUnitTest.getField(instance, "createUser"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getCreateDate()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    public void test_getCreateDate() {
        Date value = new Date();
        instance.setCreateDate(value);

        assertSame("'getCreateDate' should be correct.",
            value, instance.getCreateDate());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setCreateDate(Date createDate)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    public void test_setCreateDate() {
        Date value = new Date();
        instance.setCreateDate(value);

        assertSame("'setCreateDate' should be correct.",
            value, BaseUnitTest.getField(instance, "createDate"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getModifyUser()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    public void test_getModifyUser() {
        String value = "new_value";
        instance.setModifyUser(value);

        assertEquals("'getModifyUser' should be correct.",
            value, instance.getModifyUser());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setModifyUser(String modifyUser)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    public void test_setModifyUser() {
        String value = "new_value";
        instance.setModifyUser(value);

        assertEquals("'setModifyUser' should be correct.",
            value, BaseUnitTest.getField(instance, "modifyUser"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getModifyDate()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    public void test_getModifyDate() {
        Date value = new Date();
        instance.setModifyDate(value);

        assertSame("'getModifyDate' should be correct.",
            value, instance.getModifyDate());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setModifyDate(Date modifyDate)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    public void test_setModifyDate() {
        Date value = new Date();
        instance.setModifyDate(value);

        assertSame("'setModifyDate' should be correct.",
            value, BaseUnitTest.getField(instance, "modifyDate"));
    }
}