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
 * Unit tests for {@link TermsOfUseType} class.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class TermsOfUseTypeUnitTests {
    /**
     * <p>
     * Represents the <code>TermsOfUseType</code> instance used in tests.
     * </p>
     */
    private TermsOfUseType instance;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TermsOfUseTypeUnitTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     */
    @Before
    public void setUp() {
        instance = new TermsOfUseType();
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>TermsOfUseType()</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    @Test
    public void testCtor() {
        instance = new TermsOfUseType();

        assertEquals("'termsOfUseTypeId' should be correct.", 0, instance.getTermsOfUseTypeId());
        assertNull("'description' should be correct.", instance.getDescription());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUseTypeId()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getTermsOfUseTypeId() {
        int value = 1;
        instance.setTermsOfUseTypeId(value);

        assertEquals("'getTermsOfUseTypeId' should be correct.", value, instance.getTermsOfUseTypeId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setTermsOfUseTypeId(int termsOfUseTypeId)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setTermsOfUseTypeId() {
        int value = 1;
        instance.setTermsOfUseTypeId(value);

        assertEquals("'setTermsOfUseTypeId' should be correct.", value, TestsHelper.getField(instance,
            "termsOfUseTypeId"));
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
