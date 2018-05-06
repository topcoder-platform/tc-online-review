/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RatingInfo;
import com.cronos.onlinereview.external.RatingType;
import com.cronos.onlinereview.external.UserProjectDataStoreHelper;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Basic implementation of the <code>{@link ExternalUser}</code> interface.
 * </p>
 * <p>
 * The unique id (user id) is maintained by the super class.
 * </p>
 * <p>
 * <b>Thread Safety</b>: This class is not thread-safe.
 * </p>
 *
 * @author dplass, oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class ExternalUserImpl extends ExternalObjectImpl implements ExternalUser {

    /**
     * <p>
     * The FACTOR is used in rounding the double.
     * </p>
     */
    private static final double FACTOR = 10000.0;

    /**
     * <p>
     * The handle of this user as set in the constructor and accessed by getHandle.
     * </p>
     * <p>
     * Will never be null or empty after trim.
     * </p>
     */
    private final String handle;

    /**
     * <p>
     * The first name of this user as set in the constructor and accessed by getFirstName.
     * </p>
     * <p>
     * Will never be null or empty after trim.
     * </p>
     */
    private final String firstName;

    /**
     * <p>
     * The last name of this user as set in the constructor and accessed by getLastName.
     * </p>
     * <p>
     * Will never be null or empty after trim.
     * </p>
     */
    private final String lastName;

    /**
     * <p>
     * The primary email of this user as set in the constructor and accessed by getEmail.
     * </p>
     * <p>
     * Will never be null or empty after trim.
     * </p>
     */
    private final String email;

    /**
     * <p>
     * The set of unique alternative email addresses for this user.
     * </p>
     * <p>
     * Entries will never be null or empty after trim. Modified by addAlternativeEmail and retrieved by
     * getAlternativeEmails.
     * </p>
     */
    private final Set alternativeEmails = new HashSet();

    /**
     * <p>
     * The RatingInfo objects for this user, as modified by addRatingInfo and accessed by many of the 'getters' which
     * ultimately call getRatingInfo or one of the private methods.
     * </p>
     * <p>
     * The keys are RatingType objects, and values are RatingInfo objects. None of the keys or values will ever be null.
     * Since RatingType objects implement hashCode, there will never be duplicate key values in this map.
     * </p>
     */
    private final Map ratings = new HashMap();

    /**
     * <p>
     * Constructs this object using the given parameters.
     * </p>
     *
     * @param id
     *            the unique identifier of this user.
     * @param handle
     *            the handle of this user.
     * @param firstName
     *            the first name of this user.
     * @param lastName
     *            the last name of this user.
     * @param email
     *            the primary email address of this user.
     * @throws IllegalArgumentException
     *             if id is negative or if any other argument is <code>null</code> or empty after trim.
     */
    public ExternalUserImpl(long id, String handle, String firstName, String lastName, String email) {
        super(id);

        UserProjectDataStoreHelper.validateStringEmptyNull(email, "email");
        UserProjectDataStoreHelper.validateStringEmptyNull(handle, "handle");
        UserProjectDataStoreHelper.validateStringEmptyNull(lastName, "lastName");
        UserProjectDataStoreHelper.validateStringEmptyNull(firstName, "firstName");

        this.handle = handle;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * <p>
     * Adds the requested rating information to the map of ratings for this user.
     * </p>
     * <p>
     * If there was already a rating info with the same rating type in the map, the old value is replaced silently.
     * </p>
     *
     * @param info
     *            the rating info to add to this user's rating information.
     * @throws IllegalArgumentException
     *             if info is <code>null</code>.
     */
    public void addRatingInfo(RatingInfo info) {
        UserProjectDataStoreHelper.validateNull(info, "info");

        this.ratings.put(info.getRatingType(), info);
    }

    /**
     * <p>
     * Adds the requested alternative email address to the set of alternative email addresses for this user.
     * </p>
     * <p>
     * Since a hashSet is used, there will never be duplicates in this set.
     * </p>
     *
     * @param alternativeEmail
     *            the alternative email to add to this user's set of emails.
     * @throws IllegalArgumentException
     *             if alternativeEmail is <code>null</code> or empty after trim.
     */
    public void addAlternativeEmail(String alternativeEmail) {
        UserProjectDataStoreHelper.validateStringEmptyNull(alternativeEmail, "alternativeEmail");

        this.alternativeEmails.add(alternativeEmail);
    }

    /**
     * <p>
     * Returns the handle of this user.
     * </p>
     *
     * @return the handle of this user. Will never be null or empty after trim.
     */
    public String getHandle() {
        return this.handle;
    }

    /**
     * <p>
     * Returns the first name of this user.
     * </p>
     *
     * @return the first name of this user. Will never be null or empty after trim.
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * <p>
     * Returns the last name of this user.
     * </p>
     *
     * @return the last name of this user. Will never be null or empty after trim.
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * <p>
     * Returns the primary email address of this user.
     * </p>
     *
     * @return the primary email address of this user. Will never be null or empty after trim.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * <p>
     * Returns the alternative email addresses of this user, as added by the addAlternativeEmails method.
     * </p>
     *
     * @return the alternative email addresses of this user. Will never be null and will never have null or empty
     *         elements, but the length of the returned array may be zero to indicate no alternative email addresses.
     */
    public String[] getAlternativeEmails() {
        return (String[]) this.alternativeEmails.toArray(new String[0]);
    }

    /**
     * <p>
     * Returns the design rating of this user, as either the String representation of a non-negative integer, or N/A if
     * none.
     * </p>
     *
     * @return the design rating of this user, or N/A if there is no design rating. Will never be null or empty after
     *         trim.
     */
    public String getDesignRating() {
        // Delegates to getRating(RatingType).
        return getRating(RatingType.DESIGN);
    }

    /**
     * <p>
     * Returns the requested rating of this user using the 'ratings' map as modified by addRatingInfo.
     * </p>
     *
     * @param ratingType
     *            the type of rating information to return, e.g., design or development.
     * @return a String representation of the requested rating of this user, or N/A if there is no rating of the
     *         requested type. Will never be null or empty after trim.
     */
    private String getRating(RatingType ratingType) {
        // Gets RatingInfo from the map.
        RatingInfo ratingInfo = getRatingInfo(ratingType);

        // If no RatingInfo matches, returns N/A; else, returns the rating of the RatingInfo.
        if (ratingInfo == null) {
            return UserProjectDataStoreHelper.DEFAULT_NA_STRING;
        } else {
            return Integer.toString(ratingInfo.getRating());
        }
    }

    /**
     * <p>
     * Returns the design reliability of this user, as either the String representation of a non-negative percentage, or
     * N/A if none.
     * </p>
     *
     * @return the design reliability of this user, in "##.## %" format, or N/A if there is no design rating. Will never
     *         be null or empty after trim.
     */
    public String getDesignReliability() {
        // Delegates to getReliability(RatingType).
        return getReliability(RatingType.DESIGN);
    }

    /**
     * <p>
     * Returns the requested reliability of this user using the 'ratings' map as modified by addRatingInfo.
     * </p>
     *
     * @param ratingType
     *            the type of rating information to return, e.g., design or development.
     * @return a String representation of the requested reliability of this user, in "##.## %" format, or N/A if there
     *         is no rating of the requested type or the reliability was not set. Will never be null or empty after
     *         trim.
     */
    private String getReliability(RatingType ratingType) {
        // Gets RatingInfo from the map.
        RatingInfo ratingInfo = getRatingInfo(ratingType);

        // If no rating info for this type, or if the rating info doesn't have a reliability set,
        // return N/A; else, return the reliability and formats the number as a percentage.
        if (ratingInfo == null || !ratingInfo.hasReliability()) {
            return UserProjectDataStoreHelper.DEFAULT_NA_STRING;
        } else {
            // Gets the reliability from the RatingInfo.
            double reliability = ratingInfo.getReliability();

            // Rounds the double number first.
            reliability = Math.round(reliability * FACTOR) / FACTOR;

            // Uses DecimalFormat to format the double.
            DecimalFormat formatter = new DecimalFormat("#0.00 %");
            String reliabilityPercentage = formatter.format(reliability);

            return reliabilityPercentage;
        }
    }

    /**
     * <p>
     * Returns the design volatility of this user, as either the String representation of a non-negative integer, or N/A
     * if none.
     * </p>
     *
     * @return the design volatility of this user, or N/A if there is no design rating. Will never be null or empty
     *         after trim.
     */
    public String getDesignVolatility() {
        // Delegates to getVolatility(RatingType).
        return getVolatility(RatingType.DESIGN);
    }

    /**
     * <p>
     * Returns the requested volatility of this user using the 'ratings' map as modified by addRatingInfo.
     * </p>
     *
     * @param ratingType
     *            the type of rating information to return, e.g., design or development
     * @return a String representation of the requested volatility of this user, or N/A if there is no rating of the
     *         requested type. Will never be null or empty after trim.
     */
    private String getVolatility(RatingType ratingType) {
        // Gets RatingInfo from the map.
        RatingInfo ratingInfo = getRatingInfo(ratingType);

        if (ratingInfo == null) {
            return UserProjectDataStoreHelper.DEFAULT_NA_STRING;
        } else {
            return Integer.toString(ratingInfo.getVolatility());
        }
    }

    /**
     * <p>
     * Returns the number of design ratings of this user.
     * </p>
     *
     * @return the number of design ratings, or 0 if none. Will never be negative.
     */
    public int getDesignNumRatings() {
        // Delegates to getNumRatings(RatingType).
        return getNumRatings(RatingType.DESIGN);
    }

    /**
     * <p>
     * Returns the requested number of ratings of this user using the 'ratings' map as modified by addRatingInfo.
     * </p>
     *
     * @param ratingType
     *            the type of rating information to return, e.g., design or development.
     * @return the number of ratings of the requested type that this user has, or zero if none. Will never be negative.
     */
    private int getNumRatings(RatingType ratingType) {
        // Gets RatingInfo from the map.
        RatingInfo ratingInfo = getRatingInfo(ratingType);

        if (ratingInfo == null) {
            return 0;
        } else {
            return ratingInfo.getNumRatings();
        }
    }

    /**
     * <p>
     * Returns the development rating of this user, as either the String representation of a non-negative integer, or
     * N/A if none.
     * </p>
     *
     * @return the development rating of this user, or N/A if there is no development rating. Will never be null or
     *         empty after trim.
     */
    public String getDevRating() {
        // Delegates to getRating(RatingType).
        return getRating(RatingType.DEVELOPMENT);
    }

    /**
     * <p>
     * Returns the development reliability of this user, as either the String representation of a non-negative
     * percentage, or N/A if none.
     * </p>
     *
     * @return the development reliability of this user, in "##.## %" format, or N/A if there is no development rating.
     *         Will never be null or empty after trim.
     */
    public String getDevReliability() {
        // Delegates to getReliability(RatingType).
        return getReliability(RatingType.DEVELOPMENT);
    }

    /**
     * <p>
     * Returns the development volatility of this user, as either the String representation of a non-negative integer,
     * or N/A if none.
     * </p>
     *
     * @return the development volatility of this user, or N/A if there is no development rating. Will never be null or
     *         empty after trim.
     */
    public String getDevVolatility() {
        // Delegates to getVolatility(RatingType).
        return getVolatility(RatingType.DEVELOPMENT);
    }

    /**
     * <p>
     * Returns the number of development ratings of this user.
     * </p>
     *
     * @return the number of development ratings, or 0 if none. Will never be negative.
     */
    public int getDevNumRatings() {
        // Delegates to getNumRatings(RatingType).
        return getNumRatings(RatingType.DEVELOPMENT);
    }

    /**
     * <p>
     * Given the rating type (design or development currently are the only two options), returns the rating information
     * for this user.
     * </p>
     *
     * @param ratingType
     *            the type of rating to return.
     * @return the rating information of this user based on the given rating type, or null if there is no information
     *         for the given type.
     * @throws IllegalArgumentException
     *             if ratingType is <code>null</code>.
     */
    public RatingInfo getRatingInfo(RatingType ratingType) {
        UserProjectDataStoreHelper.validateNull(ratingType, "ratingType");

        // Gets RatingInfo from the map, and returns.
        return (RatingInfo) this.ratings.get(ratingType);
    }
}
