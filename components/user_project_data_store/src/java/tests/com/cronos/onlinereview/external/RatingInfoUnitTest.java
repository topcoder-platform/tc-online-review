/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the RatingInfo class.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class RatingInfoUnitTest extends TestCase {

    /**
     * <p>
     * Represents the configuration file.
     * </p>
     */
    private static final String CONFIG_FILE = "SampleConfig.xml";

    /**
     * <p>
     * The Default rating used in the test.
     * </p>
     */
    private static final int DEFAULT_RATING = 1569;

    /**
     * <p>
     * The Default number ratings used in the test.
     * </p>
     */
    private static final int DEFAULT_NUMBER_RATINGS = 10;

    /**
     * <p>
     * The Default volatility used in the test.
     * </p>
     */
    private static final int DEFAULT_VOLATILITY = 494;

    /**
     * <p>
     * The Default reliability used in the test.
     * </p>
     */
    private static final double DEFAULT_RELIABILITY = 0.7333;

    /**
     * <p>
     * The Default RatingType used in the test.
     * </p>
     */
    private RatingType defaultRatingType = null;

    /**
     * <p>
     * An RatingInfo instance for testing.
     * </p>
     */
    private RatingInfo ratingInfo = null;

    /**
     * <p>
     * Initialization.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        UnitTestHelper.addConfig(CONFIG_FILE);

        defaultRatingType = RatingType.DEVELOPMENT;

        ratingInfo = new RatingInfo(defaultRatingType, DEFAULT_RATING, DEFAULT_NUMBER_RATINGS, DEFAULT_VOLATILITY);
    }

    /**
     * <p>
     * Set ratingInfo to null.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        ratingInfo = null;

        UnitTestHelper.clearConfig();

        super.tearDown();
    }

    /**
     * <p>
     * Tests the accuracy of the ctor(RatingType, int, int, int).
     * </p>
     * <p>
     * The RatingInfo instance should be created successfully.
     * </p>
     */
    public void testCtor_RatingTypeIntIntInt() {

        assertNotNull("RatingInfo should be accurately created.", ratingInfo);
        assertTrue("ratingInfo should be instance of RatingInfo.", ratingInfo instanceof RatingInfo);
    }

    /**
     * <p>
     * Tests the getters of the RatingInfo.
     * </p>
     */
    public void testGetters() {

        assertEquals("The RatingType got should be the same.", defaultRatingType, ratingInfo.getRatingType());
        assertEquals("The Rating got should be the same.", DEFAULT_RATING, ratingInfo.getRating());
        assertEquals("The Volatility got should be the same.", DEFAULT_VOLATILITY, ratingInfo.getVolatility());
        assertEquals("The NumberRatings got should be the same.", DEFAULT_NUMBER_RATINGS, ratingInfo.getNumRatings());
        assertFalse("The Reliability has not been set.", ratingInfo.hasReliability());
    }

    /**
     * <p>
     * Tests the failure of the ctor(RatingType, int, int, int).
     * </p>
     * <p>
     * If the given RatingType is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_RatingTypeIntIntInt_NullRatingType() {

        try {
            new RatingInfo(null, DEFAULT_RATING, DEFAULT_NUMBER_RATINGS, DEFAULT_VOLATILITY);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(RatingType, int, int, int).
     * </p>
     * <p>
     * If the given rating is negative, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_RatingTypeIntIntInt_NegativeRating() {

        try {
            new RatingInfo(defaultRatingType, -1, DEFAULT_NUMBER_RATINGS, DEFAULT_VOLATILITY);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(RatingType, int, int, int).
     * </p>
     * <p>
     * If the given number ratings is negative, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_RatingTypeIntIntInt_NegativeNumberRatings() {

        try {
            new RatingInfo(defaultRatingType, DEFAULT_RATING, -2, DEFAULT_VOLATILITY);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(RatingType, int, int, int).
     * </p>
     * <p>
     * If the given volatility is negative, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_RatingTypeIntIntInt_NegativeVolatility() {

        try {
            new RatingInfo(defaultRatingType, DEFAULT_RATING, DEFAULT_NUMBER_RATINGS, -3);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the ctor(RatingType, int, int, int, double).
     * </p>
     * <p>
     * The RatingInfo instance should be created successfully.
     * </p>
     */
    public void testCtor_RatingTypeIntIntIntDouble() {

        ratingInfo = new RatingInfo(defaultRatingType, DEFAULT_RATING, DEFAULT_NUMBER_RATINGS, DEFAULT_VOLATILITY,
                DEFAULT_RELIABILITY);

        assertNotNull("RatingInfo should be accurately created.", ratingInfo);
        assertTrue("ratingInfo should be instance of RatingInfo.", ratingInfo instanceof RatingInfo);

        assertEquals("The RatingType got should be the same.", defaultRatingType, ratingInfo.getRatingType());
        assertEquals("The Rating got should be the same.", DEFAULT_RATING, ratingInfo.getRating());
        assertEquals("The Volatility got should be the same.", DEFAULT_VOLATILITY, ratingInfo.getVolatility());
        assertEquals("The NumberRatings got should be the same.", DEFAULT_NUMBER_RATINGS, ratingInfo.getNumRatings());

        assertTrue("The Reliability has been set.", ratingInfo.hasReliability());
        assertEquals("The Reliability got should be the same.", new Double(DEFAULT_RELIABILITY), ratingInfo
                .getReliability());
    }

    /**
     * <p>
     * Tests the failure of the ctor(RatingType, int, int, int, double).
     * </p>
     * <p>
     * If the given RatingType is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_RatingTypeIntIntIntDouble_NullRatingType() {

        try {
            new RatingInfo(null, DEFAULT_RATING, DEFAULT_NUMBER_RATINGS, DEFAULT_VOLATILITY, DEFAULT_RELIABILITY);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(RatingType, int, int, int, double).
     * </p>
     * <p>
     * If the given rating is negative, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_RatingTypeIntIntIntDouble_NegativeRating() {

        try {
            new RatingInfo(defaultRatingType, -1, DEFAULT_NUMBER_RATINGS, DEFAULT_VOLATILITY, DEFAULT_RELIABILITY);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(RatingType, int, int, int, double).
     * </p>
     * <p>
     * If the given number ratings is negative, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_RatingTypeIntIntIntDouble_NegativeNumberRatings() {

        try {
            new RatingInfo(defaultRatingType, DEFAULT_RATING, -2, DEFAULT_VOLATILITY, DEFAULT_RELIABILITY);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(RatingType, int, int, int, double).
     * </p>
     * <p>
     * If the given volatility is negative, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_RatingTypeIntIntIntDouble_NegativeVolatility() {

        try {
            new RatingInfo(defaultRatingType, DEFAULT_RATING, DEFAULT_NUMBER_RATINGS, -3, DEFAULT_RELIABILITY);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(RatingType, int, int, int, double).
     * </p>
     * <p>
     * If the given reliability is negative, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_RatingTypeIntIntIntDouble_NegativeReliability() {

        try {
            new RatingInfo(defaultRatingType, DEFAULT_RATING, DEFAULT_NUMBER_RATINGS, DEFAULT_VOLATILITY, -0.01);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }
}