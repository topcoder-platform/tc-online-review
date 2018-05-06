/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.cronos.termsofuse.TestsHelper;

/**
 * <p>
 * Unit tests for {@link TermsOfUse} class.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Removed test cases for properties memberAgreeable and electronicallySignable.</li>
 * <li>Added test cases for the agreeabilityType property.</li>
 * </ol>
 * </p>
 *
 * @author sparemax
 * @version 1.1
 */
public class TermsOfUseUnitTests {
    /**
     * <p>
     * Represents the <code>TermsOfUse</code> instance used in tests.
     * </p>
     */
    private TermsOfUse instance;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TermsOfUseUnitTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     */
    @Before
    public void setUp() {
        instance = new TermsOfUse();
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>TermsOfUse()</code>.<br>
     * Instance should be correctly created.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Removed steps for properties memberAgreeable and electronicallySignable.</li>
     * <li>Added a step for the agreeabilityType property.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testCtor() {
        instance = new TermsOfUse();

        assertEquals("'termsOfUseId' should be correct.", 0, instance.getTermsOfUseId());
        assertEquals("'termsOfUseTypeId' should be correct.", 0, instance.getTermsOfUseTypeId());
        assertNull("'title' should be correct.", instance.getTitle());
        assertNull("'url' should be correct.", instance.getUrl());
        assertNull("'agreeabilityType' should be correct.", instance.getAgreeabilityType());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUseId()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getTermsOfUseId() {
        long value = 1;
        instance.setTermsOfUseId(value);

        assertEquals("'getTermsOfUseId' should be correct.", value, instance.getTermsOfUseId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setTermsOfUseId(long termsOfUseId)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setTermsOfUseId() {
        long value = 1;
        instance.setTermsOfUseId(value);

        assertEquals("'setTermsOfUseId' should be correct.", value, TestsHelper.getField(instance, "termsOfUseId"));
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
     * Accuracy test for the method <code>getTitle()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getTitle() {
        String value = "new_value";
        instance.setTitle(value);

        assertEquals("'getTitle' should be correct.", value, instance.getTitle());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setTitle(String title)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setTitle() {
        String value = "new_value";
        instance.setTitle(value);

        assertEquals("'setTitle' should be correct.", value, TestsHelper.getField(instance, "title"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getUrl()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    @Test
    public void test_getUrl() {
        String value = "new_value";
        instance.setUrl(value);

        assertEquals("'getUrl' should be correct.", value, instance.getUrl());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setUrl(String url)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    @Test
    public void test_setUrl() {
        String value = "new_value";
        instance.setUrl(value);

        assertEquals("'setUrl' should be correct.", value, TestsHelper.getField(instance, "url"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getAgreeabilityType()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     *
     * @since 1.1
     */
    @Test
    public void test_getAgreeabilityType() {
        TermsOfUseAgreeabilityType value = new TermsOfUseAgreeabilityType();
        instance.setAgreeabilityType(value);

        assertSame("'getAgreeabilityType' should be correct.", value, instance.getAgreeabilityType());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setAgreeabilityType(TermsOfUseAgreeabilityType agreeabilityType)</code>.<br>
     * The value should be properly set.
     * </p>
     *
     * @since 1.1
     */
    @Test
    public void test_setAgreeabilityType() {
        TermsOfUseAgreeabilityType value = new TermsOfUseAgreeabilityType();
        instance.setAgreeabilityType(value);

        assertSame("'setAgreeabilityType' should be correct.", value, TestsHelper.getField(instance,
            "agreeabilityType"));
    }
}
