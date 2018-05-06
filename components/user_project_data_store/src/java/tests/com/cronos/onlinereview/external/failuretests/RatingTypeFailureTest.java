/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.failuretests;

import com.cronos.onlinereview.external.RatingType;

import junit.framework.TestCase;

/**
 * <p>
 * This test case aggregates all test cases for <code>RatingType</code>.
 * </p>
 *
 * @author idx, liulike
 * @version 1.1
 */
public class RatingTypeFailureTest extends TestCase {

    /**
     * Test method <code>getRatingType(String)</code> with null argument.
     * <code>typeName</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testGetRatingTypeString_NullArg() {
        try {
            RatingType.getRatingType(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>getRatingType(String)</code> with empty-string
     * argument. <code>typeName</code> is empty-string in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testGetRatingTypeString_EmptyStringArg_1() {
        try {
            RatingType.getRatingType("");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>getRatingType(String)</code> with empty-string
     * argument. <code>typeName</code> is empty-string in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testGetRatingTypeString_EmptyStringArg_2() {
        try {
            RatingType.getRatingType("  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>getRatingType(String)</code> with empty-string
     * argument. <code>typeName</code> is empty-string in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testGetRatingTypeString_EmptyStringArg_3() {
        try {
            RatingType.getRatingType("\t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>getRatingType(String)</code> with empty-string
     * argument. <code>typeName</code> is empty-string in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testGetRatingTypeString_EmptyStringArg_4() {
        try {
            RatingType.getRatingType("\n");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>getRatingType(int)</code> with negative
     * <code>id</code>. <code>IllegalArgumentException</code> is expected.
     */
    public void testGetRatingTypeInt_NegativeId() {
        try {
            RatingType.getRatingType(-1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>getRatingType(int)</code> with zero <code>id</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testGetRatingTypeInt_ZeroId() {
        try {
            RatingType.getRatingType(0);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

}
