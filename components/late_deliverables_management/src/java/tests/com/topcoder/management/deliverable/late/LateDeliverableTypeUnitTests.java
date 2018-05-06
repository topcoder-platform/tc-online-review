/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Unit tests for {@link LateDeliverableType} class.
 * </p>
 *
 * @author sparemax
 * @version 1.0.6
 * @since 1.0.6
 */
public class LateDeliverableTypeUnitTests {
    /**
     * <p>
     * Represents the <code>LateDeliverableType</code> instance used in tests.
     * </p>
     */
    private LateDeliverableType instance;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(LateDeliverableTypeUnitTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     */
    @Before
    public void setUp() {
        instance = new LateDeliverableType();
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>LateDeliverableType()</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    @Test
    public void testCtor() {
        instance = new LateDeliverableType();

        assertEquals("'id' should be correct.", 0L, TestsHelper.getField(instance, "id"));
        assertNull("'name' should be correct.", TestsHelper.getField(instance, "name"));
        assertNull("'description' should be correct.", TestsHelper.getField(instance, "description"));
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

        assertEquals("'setId' should be correct.", value, TestsHelper.getField(instance, "id"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getName()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getName() {
        String value = "new_value";
        instance.setName(value);

        assertEquals("'getName' should be correct.", value, instance.getName());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setName(String name)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setName() {
        String value = "new_value";
        instance.setName(value);

        assertEquals("'setName' should be correct.", value, TestsHelper.getField(instance, "name"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDescription()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getDescription() {
        String value = "new_value";
        instance.setDescription(value);

        assertEquals("'getDescription' should be correct.", value, instance.getDescription());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setDescription(String description)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setDescription() {
        String value = "new_value";
        instance.setDescription(value);

        assertEquals("'setDescription' should be correct.", value, TestsHelper.getField(instance, "description"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>toString()</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_toString_1() {
        instance.setId(1);
        instance.setName("The name");
        instance.setDescription("The description");

        String res = instance.toString();

        assertTrue("'toString' should be correct.", res.contains("id:1"));
        assertTrue("'toString' should be correct.", res.contains("name:The name"));
        assertTrue("'toString' should be correct.", res.contains("description:The description"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>toString()</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_toString_2() {
        instance = new LateDeliverableType();
        String expected = LateDeliverableType.class.getName() + "{id:0, name:null, description:null}";
        String res = instance.toString();

        assertEquals("'toString' should be correct.", expected, res);
    }
}
