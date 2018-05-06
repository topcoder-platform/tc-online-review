/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.failuretests.impl;

import com.cronos.onlinereview.external.impl.ExternalUserImpl;

import junit.framework.TestCase;

/**
 * <p>
 * This test case aggregates all test cases for <code>ExternalUserImpl</code>.
 * </p>
 *
 * @author idx, liulike
 * @version 1.1
 */
public class ExternalUserImplFailureTest extends TestCase {
    /** An <code>ExternalUserImpl</code> instance used for tests. */
    private ExternalUserImpl impl = null;

    /**
     * Set up.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        impl = new ExternalUserImpl(1, "foobar", "foo", "bar", "foobar@topcoder.com");
    }

    /**
     * Tear down.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        impl = null;
    }

    /**
     * Test constructor
     * <code>ExternalUserImpl(long, String, String, String, String)</code>
     * with negative <code>id</code>. <code>IllegalArgumentException</code>
     * is expected.
     */
    public void testExternalUserImpl_NegativeId() {
        try {
            new ExternalUserImpl(-1, "foobar", "foo", "bar", "foobar@topcoder.com");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>ExternalUserImpl(long, String, String, String, String)</code>
     * with null argument. <code>handle</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testExternalUserImpl_NullArg_1() {
        try {
            new ExternalUserImpl(1, null, "foo", "bar", "foobar@topcoder.com");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>ExternalUserImpl(long, String, String, String, String)</code>
     * with null argument. <code>firstName</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testExternalUserImpl_NullArg_2() {
        try {
            new ExternalUserImpl(1, "foobar", null, "bar", "foobar@topcoder.com");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>ExternalUserImpl(long, String, String, String, String)</code>
     * with null argument. <code>lastName</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testExternalUserImpl_NullArg_3() {
        try {
            new ExternalUserImpl(1, "foobar", "foo", null, "foobar@topcoder.com");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>ExternalUserImpl(long, String, String, String, String)</code>
     * with null argument. <code>email</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testExternalUserImpl_NullArg_4() {
        try {
            new ExternalUserImpl(1, "foobar", "foo", "bar", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>ExternalUserImpl(long, String, String, String, String)</code>
     * with empty-string argument. <code>handle</code> is empty-string in this
     * test. <code>IllegalArgumentException</code> is expected.
     */
    public void testExternalUserImpl_EmptyStringArg_1() {
        try {
            new ExternalUserImpl(1, "", "foo", "bar", "foobar@topcoder.com");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>ExternalUserImpl(long, String, String, String, String)</code>
     * with empty-string argument. <code>firstName</code> is empty-string in
     * this test. <code>IllegalArgumentException</code> is expected.
     */
    public void testExternalUserImpl_EmptyStringArg_2() {
        try {
            new ExternalUserImpl(1, "foobar", "  ", "bar", "foobar@topcoder.com");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>ExternalUserImpl(long, String, String, String, String)</code>
     * with empty-string argument. <code>lastName</code> is empty-string in
     * this test. <code>IllegalArgumentException</code> is expected.
     */
    public void testExternalUserImpl_EmptyStringArg_3() {
        try {
            new ExternalUserImpl(1, "foobar", "foo", "\t", "foobar@topcoder.com");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>ExternalUserImpl(long, String, String, String, String)</code>
     * with empty-string argument. <code>email</code> is empty-string in this
     * test. <code>IllegalArgumentException</code> is expected.
     */
    public void testExternalUserImpl_EmptyStringArg_4() {
        try {
            new ExternalUserImpl(1, "foobar", "foo", "bar", "\n");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>addRatingInfo(RatingInfo)</code> with null
     * <code>info</code>. <code>IllegalArgumentException</code> is
     * expected.
     */
    public void testAddRatingInfo_NullArg() {
        try {
            impl.addRatingInfo(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>addAlternativeEmail(String)</code> with null
     * <code>alternativeEmail</code>. <code>IllegalArgumentException</code>
     * is expected.
     */
    public void testAddAlternativeEmail_NullArg() {
        try {
            impl.addAlternativeEmail(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>addAlternativeEmail(String)</code> with empty-string
     * <code>alternativeEmail</code>. <code>IllegalArgumentException</code>
     * is expected.
     */
    public void testAddAlternativeEmail_EmptyStringArg_1() {
        try {
            impl.addAlternativeEmail("");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>addAlternativeEmail(String)</code> with empty-string
     * <code>alternativeEmail</code>. <code>IllegalArgumentException</code>
     * is expected.
     */
    public void testAddAlternativeEmail_EmptyStringArg_2() {
        try {
            impl.addAlternativeEmail("  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>addAlternativeEmail(String)</code> with empty-string
     * <code>alternativeEmail</code>. <code>IllegalArgumentException</code>
     * is expected.
     */
    public void testAddAlternativeEmail_EmptyStringArg_3() {
        try {
            impl.addAlternativeEmail("\t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>addAlternativeEmail(String)</code> with empty-string
     * <code>alternativeEmail</code>. <code>IllegalArgumentException</code>
     * is expected.
     */
    public void testAddAlternativeEmail_EmptyStringArg_4() {
        try {
            impl.addAlternativeEmail("\n");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

}
