/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.failuretests;

import com.cronos.onlinereview.external.RatingInfo;
import com.cronos.onlinereview.external.RatingType;

import junit.framework.TestCase;

/**
 * <p>
 * This test case aggregates all test cases for <code>RatingInfo</code>.
 * </p>
 *
 * @author idx, liulike
 * @version 1.1
 */
public class RatingInfoFailureTest extends TestCase {

    /**
     * Test constructor <code>RatingInfo(RatingType, int, int, int)</code>
     * with null argument. <code>ratingType</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testRatingInfoRatingTypeIntIntInt_NullArg() {
        try {
            new RatingInfo(null, 1800, 10, 300);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>RatingInfo(RatingType, int, int, int)</code>
     * with negative argument. <code>rating</code> is negative in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testRatingInfoRatingTypeIntIntInt_NegativeArg_1() {
        try {
            new RatingInfo(RatingType.DEVELOPMENT, -1800, 10, 300);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>RatingInfo(RatingType, int, int, int)</code>
     * with negative argument. <code>numRatings</code> is negative in this
     * test. <code>IllegalArgumentException</code> is expected.
     */
    public void testRatingInfoRatingTypeIntIntInt_NegativeArg_2() {
        try {
            new RatingInfo(RatingType.DEVELOPMENT, 1800, -10, 300);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>RatingInfo(RatingType, int, int, int)</code>
     * with negative argument. <code>volatility</code> is negative in this
     * test. <code>IllegalArgumentException</code> is expected.
     */
    public void testRatingInfoRatingTypeIntIntInt_NegativeArg_3() {
        try {
            new RatingInfo(RatingType.DEVELOPMENT, 1800, 10, -300);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>RatingInfo(RatingType, int, int, int, double)</code> with null
     * argument. <code>ratingType</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testRatingInfoRatingTypeIntIntIntDouble_NullArg() {
        try {
            new RatingInfo(null, 1800, 10, 300, 0.7);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>RatingInfo(RatingType, int, int, int, double)</code> with
     * negative argument. <code>rating</code> is negative in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testRatingInfoRatingTypeIntIntIntDouble_NegativeArg_1() {
        try {
            new RatingInfo(null, -1800, 10, 300, 0.7);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>RatingInfo(RatingType, int, int, int, double)</code> with
     * negative argument. <code>numRatings</code> is negative in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testRatingInfoRatingTypeIntIntIntDouble_NegativeArg_2() {
        try {
            new RatingInfo(RatingType.DEVELOPMENT, 1800, -10, 300, 0.7);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>RatingInfo(RatingType, int, int, int, double)</code> with
     * negative argument. <code>volatility</code> is negative in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testRatingInfoRatingTypeIntIntIntDouble_NegativeArg_3() {
        try {
            new RatingInfo(RatingType.DEVELOPMENT, 1800, 10, -300, 0.7);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>RatingInfo(RatingType, int, int, int, double)</code> with
     * negative argument. <code>reliability</code> is negative in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testRatingInfoRatingTypeIntIntIntDouble_NegativeArg_4() {
        try {
            new RatingInfo(RatingType.DEVELOPMENT, 1800, 10, 300, -0.7);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

}
