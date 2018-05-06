/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import java.util.Map;
import java.util.Set;

import com.cronos.onlinereview.external.RatingInfo;
import com.cronos.onlinereview.external.RatingType;
import com.cronos.onlinereview.external.UnitTestHelper;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the ExternalUserImpl class.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class ExternalUserImplUnitTest extends TestCase {

    /**
     * <p>
     * Represents the configuration file.
     * </p>
     */
    private static final String CONFIG_FILE = "SampleConfig.xml";

    /**
     * <p>
     * The default id of the userImpl.
     * </p>
     */
    private static final long DEFAULT_ID = 123;

    /**
     * <p>
     * The default handle string of the userImpl.
     * </p>
     */
    private static final String DEFAULT_HANDLE_STRING = "TCSDEVELOPER";

    /**
     * <p>
     * The default first name string of the userImpl.
     * </p>
     */
    private static final String DEFAULT_FIRST_NAME_STRING = "TCS";

    /**
     * <p>
     * The default last name string of the userImpl.
     * </p>
     */
    private static final String DEFAULT_LAST_NAME_STRING = "DEVELOPER";

    /**
     * <p>
     * The default email name string of the userImpl.
     * </p>
     */
    private static final String DEFAULT_EMAIL_STRING = "TCSDEVELOPER@gmail.com";

    /**
     * <p>
     * The default alternativeEmail name string of the userImpl.
     * </p>
     */
    private static final String DEFAULT_ALTER_EMAIL_STRING = "Alter_TCSDEVELOPER@gmail.com";

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
     * An ExternalUserImpl instance for testing.
     * </p>
     */
    private ExternalUserImpl userImpl = null;

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

        userImpl = new ExternalUserImpl(DEFAULT_ID, DEFAULT_HANDLE_STRING, DEFAULT_FIRST_NAME_STRING,
                DEFAULT_LAST_NAME_STRING, DEFAULT_EMAIL_STRING);
    }

    /**
     * <p>
     * Set userImpl to null.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        userImpl = null;
        UnitTestHelper.clearConfig();

        super.tearDown();
    }

    /**
     * <p>
     * Adds RatingInfo to UserImpl.
     * </p>
     *
     * @param ratingType
     *            the RatingType used to creates the RatingInfo instance.
     * @param needReliability
     *            if the RatingInfo instance need reliability.
     * @param reliability
     *            the reliability used to creates the RatingInfo instance.
     */
    private void addRatingInfoToUserImpl(RatingType ratingType, boolean needReliability, double reliability) {

        RatingInfo info;

        // Creates the ratingInfo.
        if (needReliability) {
            info = new RatingInfo(ratingType, DEFAULT_RATING, DEFAULT_NUMBER_RATINGS, DEFAULT_VOLATILITY, reliability);
        } else {
            info = new RatingInfo(ratingType, DEFAULT_RATING, DEFAULT_NUMBER_RATINGS, DEFAULT_VOLATILITY);
        }

        // Adds the ratingInfo to the userImpl.
        userImpl.addRatingInfo(info);
    }

    /**
     * <p>
     * Tests the accuracy of the ctor(long, String, String, String, String).
     * </p>
     * <p>
     * The ExternalUserImpl instance should be created successfully.
     * </p>
     */
    public void testCtor_LongStringStringStringString() {

        assertNotNull("ExternalUserImpl should be accurately created.", userImpl);
        assertTrue("userImpl should be instance of ExternalUserImpl.", userImpl instanceof ExternalUserImpl);
    }

    /**
     * <p>
     * Tests the accuracy of the getter getId().
     * </p>
     */
    public void testGetter_GetId() {

        assertEquals("The id should be got correctly.", DEFAULT_ID, userImpl.getId());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getHandle()().
     * </p>
     */
    public void testGetter_GetHandle() {

        assertEquals("The handle should be got correctly.", DEFAULT_HANDLE_STRING, userImpl.getHandle());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getFirstName()().
     * </p>
     */
    public void testGetter_GetFirstName() {

        assertEquals("The firstName should be got correctly.", DEFAULT_FIRST_NAME_STRING, userImpl.getFirstName());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getLastName()().
     * </p>
     */
    public void testGetter_GetLastName() {

        assertEquals("The lastName should be got correctly.", DEFAULT_LAST_NAME_STRING, userImpl.getLastName());
    }

    /**
     * <p>
     * Tests the accuracy of the getter getEmail()().
     * </p>
     */
    public void testGetter_GetEmail() {

        assertEquals("The email should be got correctly.", DEFAULT_EMAIL_STRING, userImpl.getEmail());
    }

    /**
     * <p>
     * Tests the failure of the ctor(long, String, String, String, String).
     * </p>
     * <p>
     * If the given id is negative, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_LongStringStringStringString_NegativeId() {

        try {
            new ExternalUserImpl(-1, DEFAULT_HANDLE_STRING, DEFAULT_FIRST_NAME_STRING, DEFAULT_LAST_NAME_STRING,
                    DEFAULT_EMAIL_STRING);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(long, String, String, String, String).
     * </p>
     * <p>
     * If the given handle is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_LongStringStringStringString_NullHandle() {

        try {
            new ExternalUserImpl(DEFAULT_ID, null, DEFAULT_FIRST_NAME_STRING, DEFAULT_LAST_NAME_STRING,
                    DEFAULT_EMAIL_STRING);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(long, String, String, String, String).
     * </p>
     * <p>
     * If the given handle is empty after trimmed, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_LongStringStringStringString_EmptyHandle() {

        try {
            new ExternalUserImpl(DEFAULT_ID, "  ", DEFAULT_FIRST_NAME_STRING, DEFAULT_LAST_NAME_STRING,
                    DEFAULT_EMAIL_STRING);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(long, String, String, String, String).
     * </p>
     * <p>
     * If the given firstName is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_LongStringStringStringString_NullFirstName() {

        try {
            new ExternalUserImpl(DEFAULT_ID, DEFAULT_HANDLE_STRING, null, DEFAULT_LAST_NAME_STRING,
                    DEFAULT_EMAIL_STRING);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(long, String, String, String, String).
     * </p>
     * <p>
     * If the given firstName is empty after trimmed, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_LongStringStringStringString_EmptyFirstName() {

        try {
            new ExternalUserImpl(DEFAULT_ID, DEFAULT_HANDLE_STRING, "    ", DEFAULT_LAST_NAME_STRING,
                    DEFAULT_EMAIL_STRING);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(long, String, String, String, String).
     * </p>
     * <p>
     * If the given lastName is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_LongStringStringStringString_NullLastName() {

        try {
            new ExternalUserImpl(DEFAULT_ID, DEFAULT_HANDLE_STRING, DEFAULT_FIRST_NAME_STRING, null,
                    DEFAULT_EMAIL_STRING);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(long, String, String, String, String).
     * </p>
     * <p>
     * If the given lastName is empty after trimmed, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_LongStringStringStringString_EmptyLastName() {

        try {
            new ExternalUserImpl(DEFAULT_ID, DEFAULT_HANDLE_STRING, DEFAULT_FIRST_NAME_STRING, " ",
                    DEFAULT_EMAIL_STRING);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(long, String, String, String, String).
     * </p>
     * <p>
     * If the given email is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_LongStringStringStringString_NullEmail() {

        try {
            new ExternalUserImpl(DEFAULT_ID, DEFAULT_HANDLE_STRING, DEFAULT_FIRST_NAME_STRING,
                    DEFAULT_LAST_NAME_STRING, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(long, String, String, String, String).
     * </p>
     * <p>
     * If the given email is empty after trimmed, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCtor_LongStringStringStringString_EmptyEmail() {

        try {
            new ExternalUserImpl(DEFAULT_ID, DEFAULT_HANDLE_STRING, DEFAULT_FIRST_NAME_STRING,
                    DEFAULT_LAST_NAME_STRING, "          ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the method addRatingInfo(RatingInfo).
     * </p>
     * <p>
     * Using reflection.
     * </p>
     */
    public void testAddRatingInfo_RatingInfo() {

        userImpl.addRatingInfo(ratingInfo);

        // Gets ratings Map by reflection.
        Object ratings = UnitTestHelper.getPrivateField(ExternalUserImpl.class, userImpl, "ratings");

        assertTrue("The ratings got should be instance of Map.", ratings instanceof Map);
        assertEquals("The size of the ratings Map should be 1.", 1, ((Map) ratings).size());
        assertEquals("The value in the ratings Map should be same as the ratingInfo.", ratingInfo, ((Map) ratings)
                .get(ratingInfo.getRatingType()));
    }

    /**
     * <p>
     * Tests the failure of the method addRatingInfo(RatingInfo).
     * </p>
     * <p>
     * If the given parameter is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testAddRatingInfo_RatingInfo_NullRatingInfo() {

        try {
            userImpl.addRatingInfo(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the method getRatingInfo(RatingType).
     * </p>
     */
    public void testGetRatingInfo_RatingType() {

        userImpl.addRatingInfo(ratingInfo);

        // Gets the RatingInfo using getRatingInfo.
        RatingType type = ratingInfo.getRatingType();
        RatingInfo ratingInfoGot = userImpl.getRatingInfo(type);

        assertEquals("The ratingInfoGot should be same as the ratingInfo.", ratingInfo, ratingInfoGot);
    }

    /**
     * <p>
     * Tests the accuracy of the method getRatingInfo(RatingType).
     * </p>
     * <p>
     * If there is no information for the given type, null would be returned.
     * </p>
     */
    public void testGetRatingInfo_RatingType_NonFind() {

        // Gets the RatingInfo using getRatingInfo.
        RatingType type = ratingInfo.getRatingType();
        RatingInfo ratingInfoGot = userImpl.getRatingInfo(type);

        assertNull("If there is no information for the given type, null would be returned.", ratingInfoGot);
    }

    /**
     * <p>
     * Tests the failure of the method getRatingInfo(RatingType).
     * </p>
     * <p>
     * If the given parameter is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testGetRatingInfo_RatingType_NullRatingType() {

        try {
            userImpl.getRatingInfo(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the method addAlternativeEmail(String).
     * </p>
     * <p>
     * Using reflection.
     * </p>
     */
    public void testAddAlternativeEmail_String() {

        userImpl.addAlternativeEmail(DEFAULT_ALTER_EMAIL_STRING);

        // Gets alternativeEmails Set by reflection.
        Object alternativeEmails = UnitTestHelper
                .getPrivateField(ExternalUserImpl.class, userImpl, "alternativeEmails");

        assertTrue("The alternativeEmail got should be instance of Set.", alternativeEmails instanceof Set);
        assertEquals("The size of the alternativeEmail Set should be 1.", 1, ((Set) alternativeEmails).size());
        assertEquals("The first value in the alternativeEmail Set should be same as the defaultAlterEmailString.",
                DEFAULT_ALTER_EMAIL_STRING, (((Set) alternativeEmails).toArray())[0]);
    }

    /**
     * <p>
     * Tests the failure of the method addAlternativeEmail(String).
     * </p>
     * <p>
     * If the given parameter is null, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testAddAlternativeEmail_String_NullAlternativeEmail() {

        try {
            userImpl.addAlternativeEmail(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the method addAlternativeEmail(String).
     * </p>
     * <p>
     * If the given parameter is empty after trimmed, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testAddAlternativeEmail_String_EmptyAlternativeEmail() {

        try {
            userImpl.addAlternativeEmail(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the method getAlternativeEmails().
     * </p>
     */
    public void testGetAlternativeEmails() {

        userImpl.addAlternativeEmail(DEFAULT_ALTER_EMAIL_STRING);

        // Gets the alternativeEmails using getAlternativeEmails.
        String[] alternativeEmailsGot = userImpl.getAlternativeEmails();

        assertEquals("The size of the alternativeEmailsGot array should be 1.", 1, alternativeEmailsGot.length);
        assertEquals("The first element of alternativeEmailsGot should be same as the defaultAlterEmailString.",
                DEFAULT_ALTER_EMAIL_STRING, alternativeEmailsGot[0]);
    }

    /**
     * <p>
     * Tests the accuracy of the method getAlternativeEmails().
     * </p>
     * <p>
     * If there is no alternative email addresses, empty array would be returned.
     * </p>
     */
    public void testGetAlternativeEmails_NoAlternativeEmail() {

        // Gets the alternativeEmails using getAlternativeEmails.
        String[] alternativeEmailsGot = userImpl.getAlternativeEmails();

        assertEquals("The size of the alternativeEmailsGot array should be 0, as there is no alternative email "
                + "addresses.", 0, alternativeEmailsGot.length);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignRating().
     * </p>
     */
    public void testGetDesignRating() {

        // Adds a design ratingInfo to the userImpl.
        addRatingInfoToUserImpl(RatingType.DESIGN, false, 0);

        // Gets the rating string.
        String ratingString = userImpl.getDesignRating();

        assertEquals("The ratingString should be same as the string value of the defaultRating.", Integer
                .toString(DEFAULT_RATING), ratingString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignRating().
     * </p>
     * <p>
     * If there is no design rating, N/A would be returned.
     * </p>
     */
    public void testGetDesignRating_NA() {

        // Adds the development ratingInfo, not the design ratingInfo.
        userImpl.addRatingInfo(ratingInfo);

        // Gets the rating string.
        String ratingString = userImpl.getDesignRating();

        assertEquals("The ratingString should be N/A, as there is no design rating.", "N/A", ratingString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevRating().
     * </p>
     */
    public void testGetDevRating() {

        // Adds the default development ratingInfo to the userImpl.
        userImpl.addRatingInfo(ratingInfo);

        // Gets the rating string.
        String ratingString = userImpl.getDevRating();

        assertEquals("The ratingString should be same as the string value of the defaultRating.", Integer
                .toString(DEFAULT_RATING), ratingString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevRating().
     * </p>
     * <p>
     * If there is no development rating, N/A would be returned.
     * </p>
     */
    public void testGetDevRating_NA() {

        // Gets the rating string.
        String ratingString = userImpl.getDevRating();

        assertEquals("The ratingString should be N/A, as there is no development rating.", "N/A", ratingString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignVolatility().
     * </p>
     */
    public void testGetDesignVolatility() {

        // Adds a design ratingInfo to the userImpl.
        addRatingInfoToUserImpl(RatingType.DESIGN, false, 0);

        // Gets the volatility string.
        String volatilityString = userImpl.getDesignVolatility();

        assertEquals("The volatilityString should be same as the string value of the defaultVolatility.", Integer
                .toString(DEFAULT_VOLATILITY), volatilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignVolatility().
     * </p>
     * <p>
     * If there is no design rating, N/A would be returned.
     * </p>
     */
    public void testGetDesignVolatility_NA() {

        // Adds the development ratingInfo, not the design ratingInfo.
        userImpl.addRatingInfo(ratingInfo);

        // Gets the volatility string.
        String volatilityString = userImpl.getDesignVolatility();

        assertEquals("The volatilityString should be N/A, as there is no design rating.", "N/A", volatilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevVolatility().
     * </p>
     */
    public void testGetDevVolatility() {

        // Adds the default development ratingInfo to the userImpl.
        userImpl.addRatingInfo(ratingInfo);

        // Gets the volatility string.
        String volatilityString = userImpl.getDevVolatility();

        assertEquals("The volatilityString should be same as the string value of the defaultVolatility.", Integer
                .toString(DEFAULT_VOLATILITY), volatilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevVolatility().
     * </p>
     * <p>
     * If there is no development rating, N/A would be returned.
     * </p>
     */
    public void testGetDevVolatility_NA() {

        // Gets the volatility string.
        String volatilityString = userImpl.getDevVolatility();

        assertEquals("The volatilityString should be N/A, as there is no development rating.", "N/A", volatilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignNumRatings().
     * </p>
     */
    public void testGetDesignNumRatings() {

        // Adds a design ratingInfo to the userImpl.
        addRatingInfoToUserImpl(RatingType.DESIGN, false, 0);

        // Gets the numRatings.
        int numRatings = userImpl.getDesignNumRatings();

        assertEquals("The numRatings should be same as the defaultNumberRatings.", DEFAULT_NUMBER_RATINGS, numRatings);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignNumRatings().
     * </p>
     * <p>
     * If there is no design rating, zero would be returned.
     * </p>
     */
    public void testGetDesignNumRatings_Zero() {

        // Adds the development ratingInfo, not the design ratingInfo.
        userImpl.addRatingInfo(ratingInfo);

        // Gets the numRatings.
        int numRatings = userImpl.getDesignNumRatings();

        assertEquals("The numRatings should be zero, as there is no design rating.", 0, numRatings);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevNumRatings().
     * </p>
     */
    public void testGetDevNumRatings() {

        // Adds the default development ratingInfo to the userImpl.
        userImpl.addRatingInfo(ratingInfo);

        // Gets the numRatings.
        int numRatings = userImpl.getDevNumRatings();

        assertEquals("The numRatings should be same as the defaultNumberRatings.", DEFAULT_NUMBER_RATINGS, numRatings);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevNumRatings().
     * </p>
     * <p>
     * If there is no development rating, zero would be returned.
     * </p>
     */
    public void testGetDevNumRatings_Zero() {

        // Gets the numRatings.
        int numRatings = userImpl.getDevNumRatings();

        assertEquals("The numRatings should be zero, as there is no development rating.", 0, numRatings);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignReliability().
     * </p>
     */
    public void testGetDesignReliability() {

        // Adds a design ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DESIGN, true, 0.054);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDesignReliability();

        assertEquals("The reliabilityString should be the same.", "5.40 %", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignReliability().
     * </p>
     * <p>
     * If no design rating info for this type, N/A would be returned.
     * </p>
     */
    public void testGetDesignReliability_NoDesignRating() {

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDesignReliability();

        assertEquals("The reliabilityString should be the same.", "N/A", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDesignReliability().
     * </p>
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDesignReliability_NoReliability() {

        // Adds a design ratingInfo to the userImpl, it has no reliability.
        addRatingInfoToUserImpl(RatingType.DESIGN, false, 0);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDesignReliability();

        assertEquals("The reliabilityString should be the same.", "N/A", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * If no development rating info for this type, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_NoDevRating() {

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "N/A", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * If the rating info doesn't have a reliability set, N/A would be returned.
     * </p>
     */
    public void testGetDevReliability_NoReliability() {

        // Adds the default ratingInfo to the userImpl, without the reliability.
        userImpl.addRatingInfo(ratingInfo);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "N/A", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * Gives several cases of the decimal round and format.
     * </p>
     */
    public void testGetDevReliability_Accuracy1() {

        // Adds a development ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DEVELOPMENT, true, 0.054);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "5.40 %", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * Gives several cases of the decimal round and format.
     * </p>
     */
    public void testGetDevReliability_Accuracy2() {

        // Adds a development ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DEVELOPMENT, true, 0.001);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "0.10 %", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * Gives several cases of the decimal round and format.
     * </p>
     */
    public void testGetDevReliability_Accuracy3() {

        // Adds a development ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DEVELOPMENT, true, 0.01);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "1.00 %", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * Gives several cases of the decimal round and format.
     * </p>
     */
    public void testGetDevReliability_Accuracy4() {

        // Adds a development ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DEVELOPMENT, true, 0);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "0.00 %", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * Gives several cases of the decimal round and format.
     * </p>
     */
    public void testGetDevReliability_Accuracy5() {

        // Adds a development ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DEVELOPMENT, true, 1);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "100.00 %", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * Gives several cases of the decimal round and format.
     * </p>
     */
    public void testGetDevReliability_Accuracy6() {

        // Adds a development ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DEVELOPMENT, true, 0.8213457);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "82.13 %", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * Gives several cases of the decimal round and format.
     * </p>
     */
    public void testGetDevReliability_Accuracy7() {

        // Adds a development ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DEVELOPMENT, true, 0.4999521);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "50.00 %", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * Gives several cases of the decimal round and format.
     * </p>
     */
    public void testGetDevReliability_Accuracy8() {

        // Adds a development ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DEVELOPMENT, true, 0.4999421);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "49.99 %", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * Gives several cases of the decimal round and format.
     * </p>
     */
    public void testGetDevReliability_Accuracy9() {

        // Adds a development ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DEVELOPMENT, true, 0.4999621);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "50.00 %", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * Gives several cases of the decimal round and format.
     * </p>
     */
    public void testGetDevReliability_Accuracy10() {

        // Adds a development ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DEVELOPMENT, true, 0.80009);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "80.01 %", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * Gives several cases of the decimal round and format.
     * </p>
     */
    public void testGetDevReliability_Accuracy11() {

        // Adds a development ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DEVELOPMENT, true, 0.98004147);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "98.00 %", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * Gives several cases of the decimal round and format.
     * </p>
     */
    public void testGetDevReliability_Accuracy12() {

        // Adds a development ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DEVELOPMENT, true, 0.00045);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "0.05 %", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * Gives several cases of the decimal round and format.
     * </p>
     */
    public void testGetDevReliability_Accuracy13() {

        // Adds a development ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DEVELOPMENT, true, 0.00044);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "0.04 %", reliabilityString);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDevReliability().
     * </p>
     * <p>
     * Gives several cases of the decimal round and format.
     * </p>
     */
    public void testGetDevReliability_Accuracy14() {

        // Adds a development ratingInfo to the userImpl, it has the reliability.
        addRatingInfoToUserImpl(RatingType.DEVELOPMENT, true, 0.00041);

        // Gets the reliabilityString.
        String reliabilityString = userImpl.getDevReliability();

        assertEquals("The reliabilityString should be the same.", "0.04 %", reliabilityString);
    }
}
