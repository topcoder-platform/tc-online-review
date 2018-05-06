/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.cronos.termsofuse.TestsHelper;

/**
 * <p>
 * Unit tests for {@link TermsOfUseAgreeabilityType} class.
 * </p>
 *
 * @author sparemax
 * @version 1.1
 * @since 1.1
 */
public class TermsOfUseAgreeabilityTypeUnitTests {
    /**
     * <p>
     * Represents the <code>TermsOfUseAgreeabilityType</code> instance used in tests.
     * </p>
     */
    private TermsOfUseAgreeabilityType instance;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TermsOfUseAgreeabilityTypeUnitTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     */
    @Before
    public void setUp() {
        instance = new TermsOfUseAgreeabilityType();
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>TermsOfUseAgreeabilityType()</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    @Test
    public void testCtor() {
        instance = new TermsOfUseAgreeabilityType();

        assertEquals("'termsOfUseAgreeabilityTypeId' should be correct.",
            0, instance.getTermsOfUseAgreeabilityTypeId());
        assertNull("'name' should be correct.", instance.getName());
        assertNull("'description' should be correct.", instance.getDescription());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUseAgreeabilityTypeId()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getTermsOfUseAgreeabilityTypeId() {
        int value = 1;
        instance.setTermsOfUseAgreeabilityTypeId(value);

        assertEquals("'getTermsOfUseAgreeabilityTypeId' should be correct.", value, instance
            .getTermsOfUseAgreeabilityTypeId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setTermsOfUseAgreeabilityTypeId(int termsOfUseAgreeabilityTypeId)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setTermsOfUseAgreeabilityTypeId() {
        int value = 1;
        instance.setTermsOfUseAgreeabilityTypeId(value);

        assertEquals("'setTermsOfUseAgreeabilityTypeId' should be correct.", value, TestsHelper.getField(instance,
            "termsOfUseAgreeabilityTypeId"));
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
}
