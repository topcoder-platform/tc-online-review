/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.accuracytests;

import com.cronos.onlinereview.external.RatingInfo;
import com.cronos.onlinereview.external.RatingType;

import junit.framework.TestCase;

import java.io.Serializable;


/**
 * <p>
 * Tests the RatingInfo class.
 * </p>
 *
 * @author lyt, restarter
 * @version 1.1
 */
public class RatingInfoAccuracyTest extends TestCase {
    /**
     * <p>
     * Represents the configuration file.
     * </p>
     */
    private static final String CONFIG_FILE = "Accuracy/Config.xml";

    /**
     * <p>
     * The Default RatingType used in the test.
     * </p>
     */
    private RatingType defaultRatingType = null;

    /**
     * <p>
     * The Default rating used in the test.
     * </p>
     */
    private final int defaultRating = 1569;

    /**
     * <p>
     * The Default number ratings used in the test.
     * </p>
     */
    private final int defaultNumberRatings = 10;

    /**
     * <p>
     * The Default volatility used in the test.
     * </p>
     */
    private final int defaultVolatility = 494;

    /**
     * <p>
     * The Default reliability used in the test.
     * </p>
     */
    private final double defaultReliability = 0.7333;

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
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        AccuracyHelper.addConfig(CONFIG_FILE);

        defaultRatingType = RatingType.DEVELOPMENT;

        ratingInfo = new RatingInfo(defaultRatingType, defaultRating, defaultNumberRatings, defaultVolatility);
    }

    /**
     * <p>tearDown.</p>
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {

    	AccuracyHelper.clearConfig();
    }

    /**
     * <p>
     * Tests the accuracy of the ctor(RatingType, int, int, int).
     * </p>
     *
     * <p>
     * The RatingInfo instance should be created successfully.
     * </p>
     */
    public void testConstructor1_Accuracy() {
        assertNotNull("RatingInfo should be accurately created.", ratingInfo);
        assertTrue("RatingInfo should be instance of RatingInfo.", ratingInfo instanceof Serializable);
        assertEquals("The RatingType got should be the same.", defaultRatingType,
            AccuracyHelper.getPrivateField(RatingInfo.class, ratingInfo, "ratingType"));
        assertEquals("The Rating got should be the same.", new Integer(defaultRating),
            AccuracyHelper.getPrivateField(RatingInfo.class, ratingInfo, "rating"));
        assertEquals("The Volatility got should be the same.", new Integer(defaultVolatility),
            AccuracyHelper.getPrivateField(RatingInfo.class, ratingInfo, "volatility"));
        assertEquals("The NumberRatings got should be the same.", new Integer(defaultNumberRatings),
            AccuracyHelper.getPrivateField(RatingInfo.class, ratingInfo, "numRatings"));
        assertNull(AccuracyHelper.getPrivateField(RatingInfo.class, ratingInfo, "reliability"));
    }

    /**
     * <p>
     * Tests the accuracy of the ctor(RatingType, int, int, int).
     * </p>
     *
     * <p>
     * The RatingInfo instance should be created successfully.
     * </p>
     */
    public void testConstructor2_Accuracy() {
        ratingInfo = new RatingInfo(defaultRatingType, defaultRating, defaultNumberRatings, defaultVolatility,
                defaultReliability);
        assertNotNull("RatingInfo should be accurately created.", ratingInfo);
        assertTrue("RatingInfo should be instance of RatingInfo.", ratingInfo instanceof Serializable);
        assertEquals("The RatingType got should be the same.", defaultRatingType,
            AccuracyHelper.getPrivateField(RatingInfo.class, ratingInfo, "ratingType"));
        assertEquals("The Rating got should be the same.", new Integer(defaultRating),
            AccuracyHelper.getPrivateField(RatingInfo.class, ratingInfo, "rating"));
        assertEquals("The Volatility got should be the same.", new Integer(defaultVolatility),
            AccuracyHelper.getPrivateField(RatingInfo.class, ratingInfo, "volatility"));
        assertEquals("The NumberRatings got should be the same.", new Integer(defaultNumberRatings),
            AccuracyHelper.getPrivateField(RatingInfo.class, ratingInfo, "numRatings"));
        assertEquals(new Double(defaultReliability), AccuracyHelper.getPrivateField(RatingInfo.class, ratingInfo, "reliability"));
    }

    /**
     * Test method for 'com.cronos.onlinereview.external.RatingInfo.getRatingType()'
     */
    public void testGetRatingType() {
        assertEquals("The RatingType got should be the same.", defaultRatingType, ratingInfo.getRatingType());
    }

    /**
     * Test method for 'com.cronos.onlinereview.external.RatingInfo.getRating()'
     */
    public void testGetRating() {
        assertEquals("The Rating got should be the same.", defaultRating, ratingInfo.getRating());
    }

    /**
     * Test method for 'com.cronos.onlinereview.external.RatingInfo.getReliability()'
     */
    public void testGetReliability() {
        ratingInfo = new RatingInfo(defaultRatingType, defaultRating, defaultNumberRatings, defaultVolatility,
                defaultReliability);
        assertEquals(new Double(defaultReliability), ratingInfo.getReliability());
    }

    /**
     * Test method for 'com.cronos.onlinereview.external.RatingInfo.hasReliability()'
     */
    public void testHasReliability() {
        assertFalse("The Reliability has not been set.", ratingInfo.hasReliability());
        ratingInfo = new RatingInfo(defaultRatingType, defaultRating, defaultNumberRatings, defaultVolatility,
                defaultReliability);
        assertTrue("The Reliability has not been set.", ratingInfo.hasReliability());
    }

    /**
     * Test method for 'com.cronos.onlinereview.external.RatingInfo.getNumRatings()'
     */
    public void testGetNumRatings() {
        assertEquals("The NumberRatings got should be the same.", defaultNumberRatings, ratingInfo.getNumRatings());
    }

    /**
     * Test method for 'com.cronos.onlinereview.external.RatingInfo.getVolatility()'
     */
    public void testGetVolatility() {
        assertEquals("The Volatility got should be the same.", defaultVolatility, ratingInfo.getVolatility());
    }
}
