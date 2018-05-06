/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.accuracytests.impl;

import java.util.Map;
import java.util.Set;

import com.cronos.onlinereview.external.RatingInfo;
import com.cronos.onlinereview.external.RatingType;
import com.cronos.onlinereview.external.accuracytests.AccuracyHelper;
import com.cronos.onlinereview.external.impl.ExternalObjectImpl;
import com.cronos.onlinereview.external.impl.ExternalUserImpl;

/**
 * <p>
 * Tests the ExternalUserImpl class.
 * </p>
 *
 * @author lyt, restarter
 * @version 1.1
 */
public class ExternalUserImplAccuracyTest extends ExternalObjectImplAccuracyTest {
    /**
     * <p>
     * The Default reliability used in the test.
     * </p>
     */
    private static final double RELIABILITY = 0.23455;

    /**
     * <p>
     * Represents the configuration file.
     * </p>
     */
    private static final String CONFIG_FILE = "Accuracy/Config.xml";

    /**
     * <p>
     * The Default rating used in the test.
     * </p>
     */
    private static final int RATING = 1569;

    /**
     * <p>
     * The Default number ratings used in the test.
     * </p>
     */
    private static final int NUMBER_RATINGS = 10;

    /**
     * <p>
     * The Default volatility used in the test.
     * </p>
     */
    private static final int VOLATILITY = 494;

    /**
     * <p>
     * The default handle string of the userImpl.
     * </p>
     */
    private static final String HANDLE = "TCSDEVELOPER";

    /**
     * <p>
     * The default first name string of the userImpl.
     * </p>
     */
    private static final String FIRST_NAME = "TCS";

    /**
     * <p>
     * The default last name string of the userImpl.
     * </p>
     */
    private static final String LAST_NAME = "DEVELOPER";

    /**
     * <p>
     * The default email name string of the userImpl.
     * </p>
     */
    private static final String EMIAL = "TCSDEVELOPER@gmail.com";

    /**
     * <p>
     * The default alternativeEmail name string of the userImpl.
     * </p>
     */
    private static final String DEFAULT_ALTER_EMAIL = "Alter_TCSDEVELOPER@gmail.com";

    /**
     * <p>
     * A development RatingInfo instance for testing.
     * </p>
     */
    private RatingInfo devRatingInfo = null;

    /**
     * <p>
     * A design RatingInfo instance for testing.
     * </p>
     */
    private RatingInfo desRatingInfo = null;

    /**
     * <p>
     * An ExternalUserImpl instance for testing.
     * </p>
     */
    private ExternalUserImpl externalUser = null;

    /**
     * <p>
     * Initialization.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        AccuracyHelper.addConfig(CONFIG_FILE);

        devRatingInfo =
            new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, RELIABILITY);
        desRatingInfo = new RatingInfo(RatingType.DESIGN, RATING, NUMBER_RATINGS, VOLATILITY, RELIABILITY);

        externalUser = new ExternalUserImpl(ID, HANDLE, FIRST_NAME, LAST_NAME, EMIAL);
        externalObject = externalUser;
    }

    /**
     * <p>
     * tearDown.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {

        AccuracyHelper.clearConfig();
    }

    /**
     * <p>
     * Tests the accuracy of the Constructor(long, String, String, String, String).
     * </p>
     *
     * <p>
     * The ExternalUserImpl instance should be created successfully.
     * </p>
     */
    public void testConstructor_Accuracy() {
        assertTrue("userImpl should be instance of ExternalUserImpl.",
            externalUser instanceof ExternalUserImpl);
        assertEquals("Tests the accuracy of Constructor failed.", new Long(ID), AccuracyHelper
            .getPrivateField(ExternalObjectImpl.class, externalObject, "id"));
        assertEquals("Tests the accuracy of Constructor failed.", HANDLE, AccuracyHelper.getPrivateField(
            ExternalUserImpl.class, externalObject, "handle"));
        assertEquals("Tests the accuracy of Constructor failed.", FIRST_NAME, AccuracyHelper.getPrivateField(
            ExternalUserImpl.class, externalObject, "firstName"));
        assertEquals("Tests the accuracy of Constructor failed.", LAST_NAME, AccuracyHelper.getPrivateField(
            ExternalUserImpl.class, externalObject, "lastName"));
        assertEquals("Tests the accuracy of Constructor failed.", EMIAL, AccuracyHelper.getPrivateField(
            ExternalUserImpl.class, externalObject, "email"));
    }

    /**
     * <p>
     * Tests the accuracy of the method addRatingInfo(RatingInfo).
     * </p>
     *
     * <p>
     * Using reflection.
     * </p>
     */
    public void testAddRatingInfo_Accuracy() {
        Map ratings = (Map) AccuracyHelper.getPrivateField(ExternalUserImpl.class, externalUser, "ratings");
        assertTrue("Tests the accuracy of addRatingInfo(RatingInfo) failed.", ratings.isEmpty());

        // Add rating Info
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The size of the ratings Map should be 1.", 1, ratings.size());
        assertEquals("The value in the ratings Map should be same as the ratingInfo.", devRatingInfo, ratings
            .get(devRatingInfo.getRatingType()));

        // Replace
        RatingInfo newRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, 2222, 129, 99);
        externalUser.addRatingInfo(newRatingInfo);

        assertEquals("The size of the ratings Map should be 1.", 1, ratings.size());
        assertEquals("The value in the ratings Map should be same as the ratingInfo.", newRatingInfo, ratings
            .get(devRatingInfo.getRatingType()));
    }

    /**
     * <p>
     * Tests the accuracy of the method getRatingInfo(RatingType).
     * </p>
     */
    public void testGetRatingInfo_Accuracy1() {
        externalUser.addRatingInfo(devRatingInfo);
        assertEquals("The ratingInfoGot should be same as the ratingInfo.", devRatingInfo, externalUser
            .getRatingInfo(RatingType.DEVELOPMENT));
    }

    /**
     * <p>
     * Tests the accuracy of the method getRatingInfo(RatingType).
     * </p>
     *
     * <p>
     * If there is no information for the given type, null would be returned.
     * </p>
     */
    public void testGetRatingInfo_Accuracy2() {
        externalUser.addRatingInfo(devRatingInfo);
        assertNull("If there is no information for the given type, null would be returned.", externalUser
            .getRatingInfo(RatingType.DESIGN));
    }

    /**
     * <p>
     * Tests the accuracy of the getter getId().
     * </p>
     */
    public void test_GetId() {
        assertEquals("The id should be got correctly.", ID, externalUser.getId());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getHandle()().
     * </p>
     */
    public void test_GetHandle() {
        assertEquals("The handle should be got correctly.", HANDLE, externalUser.getHandle());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getFirstName()().
     * </p>
     */
    public void test_GetFirstName() {
        assertEquals("The firstName should be got correctly.", FIRST_NAME, externalUser.getFirstName());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getLastName()().
     * </p>
     */
    public void test_GetLastName() {
        assertEquals("The lastName should be got correctly.", LAST_NAME, externalUser.getLastName());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getEmail()().
     * </p>
     */
    public void test_GetEmail() {
        assertEquals("The email should be got correctly.", EMIAL, externalUser.getEmail());
    }

    /**
     * <p>
     * Tests the accuracy of the method addAlternativeEmail(String).
     * </p>
     *
     * <p>
     * Using reflection.
     * </p>
     */
    public void testAddAlternativeEmail_String() {
        externalUser.addAlternativeEmail(DEFAULT_ALTER_EMAIL);

        // Gets alternativeEmails Set by reflection.
        Set alternativeEmails =
            (Set) AccuracyHelper.getPrivateField(ExternalUserImpl.class, externalUser, "alternativeEmails");

        assertEquals("The size of the alternativeEmail Set should be 1.", 1, alternativeEmails.size());
        assertEquals(
            "The first value in the alternativeEmail Set should be same as the defaultAlterEmailString.",
            DEFAULT_ALTER_EMAIL, (alternativeEmails.toArray())[0]);
    }

    /**
     * <p>
     * Tests the accuracy of the method getAlternativeEmails().
     * </p>
     */
    public void testGetAlternativeEmails_Accuracy1() {
        externalUser.addAlternativeEmail(DEFAULT_ALTER_EMAIL);

        // Gets the alternativeEmails using getAlternativeEmails.
        String[] alternativeEmailsGot = externalUser.getAlternativeEmails();

        assertEquals("The size of the alternativeEmailsGot array should be 1.", 1,
            alternativeEmailsGot.length);
        assertEquals(
            "The first element of alternativeEmailsGot should be same as the defaultAlterEmailString.",
            DEFAULT_ALTER_EMAIL, alternativeEmailsGot[0]);
    }

    /**
     * <p>
     * Tests the accuracy of the method getAlternativeEmails().
     * </p>
     *
     * <p>
     * If there is no alternative email addresses, empty array would be returned.
     * </p>
     */
    public void testGetAlternativeEmails_Accuracy2() {
        // Gets the alternativeEmails using getAlternativeEmails.
        String[] alternativeEmailsGot = externalUser.getAlternativeEmails();

        assertEquals(
            "The size of the alternativeEmailsGot array should be 0, as there is no alternative email "
                + "addresses.", 0, alternativeEmailsGot.length);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignRating().
     * </p>
     */
    public void testGetDesignRating() {
        externalUser.addRatingInfo(desRatingInfo);
        assertEquals("The ratingString should be same as the string value of the defaultRating.", Integer
            .toString(RATING), externalUser.getDesignRating());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignRating().
     * </p>
     *
     * <p>
     * If there is no design rating, N/A would be returned.
     * </p>
     */
    public void testGetDesignRating_NA() {
        assertEquals("The ratingString should be N/A, as there is no design rating.", "N/A", externalUser
            .getDesignRating());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevRating().
     * </p>
     */
    public void testGetDevRating_Accuracy() {
        externalUser.addRatingInfo(devRatingInfo);
        assertEquals("The ratingString should be same as the string value of the defaultRating.", Integer
            .toString(RATING), externalUser.getDevRating());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevRating().
     * </p>
     *
     * <p>
     * If there is no development rating, N/A would be returned.
     * </p>
     */
    public void testGetDevRating_NA() {
        assertEquals("The ratingString should be N/A, as there is no development rating.", "N/A",
            externalUser.getDevRating());
        ;
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignVolatility().
     * </p>
     */
    public void testGetDesignVolatility() {
        externalUser.addRatingInfo(desRatingInfo);
        assertEquals("The volatilityString should be same as the string value of the defaultVolatility.",
            Integer.toString(VOLATILITY), externalUser.getDesignVolatility());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignVolatility().
     * </p>
     *
     * <p>
     * If there is no design rating, N/A would be returned.
     * </p>
     */
    public void testGetDesignVolatility_NA() {
        assertEquals("The volatilityString should be N/A, as there is no design rating.", "N/A", externalUser
            .getDesignVolatility());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevVolatility().
     * </p>
     */
    public void testGetDevVolatility() {
        externalUser.addRatingInfo(devRatingInfo);
        assertEquals("The volatilityString should be same as the string value of the defaultVolatility.",
            Integer.toString(VOLATILITY), externalUser.getDevVolatility());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevVolatility().
     * </p>
     *
     * <p>
     * If there is no development rating, N/A would be returned.
     * </p>
     */
    public void testGetDevVolatility_NA() {
        assertEquals("The volatilityString should be N/A, as there is no development rating.", "N/A",
            externalUser.getDevVolatility());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignNumRatings().
     * </p>
     */
    public void testGetDesignNumRatings() {
        externalUser.addRatingInfo(desRatingInfo);
        assertEquals("The numRatings should be same as the defaultNumberRatings.", NUMBER_RATINGS,
            externalUser.getDesignNumRatings());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignNumRatings().
     * </p>
     *
     * <p>
     * If there is no design rating, zero would be returned.
     * </p>
     */
    public void testGetDesignNumRatings_Zero() {
        assertEquals("The numRatings should be zero, as there is no design rating.", 0, externalUser
            .getDesignNumRatings());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevNumRatings().
     * </p>
     */
    public void testGetDevNumRatings() {
        externalUser.addRatingInfo(devRatingInfo);
        assertEquals("The numRatings should be same as the defaultNumberRatings.", NUMBER_RATINGS,
            externalUser.getDevNumRatings());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevNumRatings().
     * </p>
     *
     * <p>
     * If there is no development rating, zero would be returned.
     * </p>
     */
    public void testGetDevNumRatings_Zero() {
        assertEquals("The numRatings should be zero, as there is no development rating.", 0, externalUser
            .getDevNumRatings());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignReliability().
     * </p>
     */
    public void testGetDesignReliability_Accuracy() {
        externalUser.addRatingInfo(desRatingInfo);
        assertEquals("The reliabilityString should be the same.", "23.46 %", externalUser
            .getDesignReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignReliability().
     * </p>
     *
     * <p>
     * If no design rating info for this type, N/A would be returned.
     * </p>
     */
    public void testGetDesignReliability_NoDesignRating() {
        assertEquals("The reliabilityString should be the same.", "N/A", externalUser.getDesignReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDesignReliability_NoReliability() {
        desRatingInfo = new RatingInfo(RatingType.DESIGN, RATING, NUMBER_RATINGS, VOLATILITY);
        externalUser.addRatingInfo(desRatingInfo);
        assertEquals("The reliabilityString should be the same.", "N/A", externalUser.getDesignReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     */
    public void testGetDevReliability_Accuracy() {
        externalUser.addRatingInfo(devRatingInfo);
        assertEquals("The reliabilityString should be the same.", "23.46 %", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If no development rating info for this type, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_NoDevRating() {
        assertEquals("The reliabilityString should be the same.", "N/A", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_NoReliability() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY);
        externalUser.addRatingInfo(devRatingInfo);
        assertEquals("The reliabilityString should be the same.", "N/A", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy0() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "0.00 %", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy1() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 1);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "100.00 %", externalUser
            .getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy2() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0.01);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "1.00 %", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy3() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0.01011);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "1.01 %", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy4() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0.01024);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "1.02 %", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy5() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0.01025);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "1.03 %", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy6() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0.999);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "99.90 %", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy7() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0.9999);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "99.99 %", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy8() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0.99999);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "100.00 %", externalUser
            .getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy9() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0.89);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "89.00 %", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy10() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0.8910);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "89.10 %", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy11() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0.000);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "0.00 %", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy12() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0.099);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "9.90 %", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy13() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0.55511);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "55.51 %", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy14() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0.001);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "0.10 %", externalUser.getDevReliability());
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     *
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_Accuracy15() {
        devRatingInfo = new RatingInfo(RatingType.DEVELOPMENT, RATING, NUMBER_RATINGS, VOLATILITY, 0.31415);
        externalUser.addRatingInfo(devRatingInfo);

        assertEquals("The reliabilityString should be the same.", "31.42 %", externalUser.getDevReliability());
    }
}
