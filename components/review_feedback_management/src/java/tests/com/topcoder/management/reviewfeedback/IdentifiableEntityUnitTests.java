/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit tests for {@link IdentifiableEntity} class.
 * </p>
 *
 * @author sparemax
 * @version 2.0
 * @since 2.0
 */
public class IdentifiableEntityUnitTests extends TestCase {
    /**
     * <p>
     * Represents the <code>IdentifiableEntity</code> instance used in tests.
     * </p>
     */
    private IdentifiableEntity instance;

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(IdentifiableEntityUnitTests.class);
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
        instance = new MockIdentifiableEntity();
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>IdentifiableEntity()</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    public void testCtor() {
        instance = new MockIdentifiableEntity();

        assertEquals("'id' should be correct.", 0L, BaseUnitTest.getField(instance, "id"));
    }


    /**
     * <p>
     * Accuracy test for the method <code>getId()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    public void test_getId() {
        long value = 1L;
        instance.setId(value);

        assertEquals("'getId' should be correct.",
            value, instance.getId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setId(long id)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    public void test_setId() {
        long value = 1L;
        instance.setId(value);

        assertEquals("'setId' should be correct.",
            value, BaseUnitTest.getField(instance, "id"));
    }
}