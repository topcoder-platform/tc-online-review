/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

/**
 * <p>
 * This interface represents a user in the <b>User Project Data Store</b> component.
 * </p>
 * <p>
 * It stores the user name, handle, email address(es) and design and development rating information. The unique id (user
 * id) is described by the super interface.
 * </p>
 * <p>
 * <b>Thread Safety</b>: Implementations of this interface are not required to be thread-safe.
 * </p>
 *
 * @author dplass, oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public interface ExternalUser extends ExternalObject {

    /**
     * <p>
     * Returns the handle of this user.
     * </p>
     *
     * @return the handle of this user. Will never be null or empty after trim.
     */
    String getHandle();

    /**
     * <p>
     * Returns the first name of this user.
     * </p>
     *
     * @return the first name of this user. Will never be null or empty after trim.
     */
    String getFirstName();

    /**
     * <p>
     * Returns the last name of this user.
     * </p>
     *
     * @return the last name of this user. Will never be null or empty after trim.
     */
    String getLastName();

    /**
     * <p>
     * Returns the primary email address of this user.
     * </p>
     *
     * @return the primary email address of this user. Will never be null or empty after trim.
     */
    String getEmail();

    /**
     * <p>
     * Returns the alternative email addresses of this user.
     * </p>
     *
     * @return the alternative email addresses of this user. Must never be null and must never have null or empty
     *         elements, but the length of the returned array may be zero to indicate no alternative email addresses.
     */
    String[] getAlternativeEmails();

    /**
     * <p>
     * Returns the design rating of this user, as either the String representation of a non-negative integer, or "N/A"
     * if none.
     * </p>
     *
     * @return the design rating of this user, or "N/A" if there is no design rating. Must never be null or empty after
     *         trim.
     */
    String getDesignRating();

    /**
     * <p>
     * Returns the design reliability of this user, as either the String representation of a non-negative percentage, or
     * "N/A" if none.
     * </p>
     *
     * @return the design reliability of this user, in "##.## %" format, or "N/A" if there is no design rating. Must
     *         never be null or empty after trim.
     */
    String getDesignReliability();

    /**
     * <p>
     * Returns the design volatility of this user.
     * </p>
     *
     * @return the design volatility of this user, in "####" format, or "N/A" if there is no design rating. Must never
     *         be null or empty after trim.
     */
    String getDesignVolatility();

    /**
     * <p>
     * Returns the number of design ratings of this user.
     * </p>
     *
     * @return the number of design ratings, or 0 if none. Must never be negative.
     */
    int getDesignNumRatings();

    /**
     * <p>
     * Returns the development rating of this user, as either the String representation of a non-negative integer, or
     * "N/A" if none.
     * </p>
     *
     * @return the development rating of this user, or "N/A" if there is no development rating. Must never be null or
     *         empty after trim.
     */
    String getDevRating();

    /**
     * <p>
     * Returns the development reliability of this user, as either the String representation of a non-negative
     * percentage, or "N/A" if none.
     * </p>
     *
     * @return the development reliability of this user, in "##.## %" format, or "N/A" if there is no development
     *         rating. Must never be null or empty after trim.
     */
    String getDevReliability();

    /**
     * <p>
     * Returns the development volatility of this user, as either the String representation of a non-negative integer,
     * or "N/A" if none.
     * </p>
     *
     * @return the development volatility of this user, in "####" format, or "N/A" if there is no development rating.
     *         Must never be null or empty after trim.
     */
    String getDevVolatility();

    /**
     * <p>
     * Returns the number of development ratings of this user.
     * </p>
     *
     * @return the number of development ratings, or 0 if none. Must never be negative.
     */
    int getDevNumRatings();

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
    RatingInfo getRatingInfo(RatingType ratingType);
}
