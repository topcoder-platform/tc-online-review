/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

import java.io.Serializable;

/**
 * <p>
 * This class encapsulates information about ratings for a particular rating type for a user.
 * </p>
 * <p>
 * It includes the actual rating, the reliability, number of ratings and volatility, for a specific user (not maintained
 * in this class) for a specific rating type (design or development.)
 * </p>
 * <p>
 * <b>Thread Safety</b>: This class is immutable and thread-safe.
 * </p>
 *
 * @author dplass, oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class RatingInfo implements Serializable {

    /**
     * <p>
     * The type of rating this object is for - design or development.
     * </p>
     * <p>
     * Sets in the constructor and accessed by getRatingType. Will never be null.
     * </p>
     */
    private final RatingType ratingType;

    /**
     * <p>
     * The rating of the given 'ratingType' phase.
     * </p>
     * <p>
     * Sets in the constructor and accessed by getRating. Will never be negative.
     * </p>
     */
    private final int rating;

    /**
     * <p>
     * The reliability of the given 'ratingType' phase.
     * </p>
     * <p>
     * Sets in the constructor and accessed by getReliability. Will never represent a negative double, but may be null
     * if no reliability is set.
     * </p>
     */
    private final Double reliability;

    /**
     * <p>
     * The number of ratings in the given 'ratingType' phase.
     * </p>
     * <p>
     * Sets in the constructor and accessed by getNumRatings. Will never be negative.
     * </p>
     */
    private final int numRatings;

    /**
     * <p>
     * The volatility in the given 'ratingType' phase.
     * </p>
     * <p>
     * Sets in the constructor and accessed by getVolatility. Will never be negative.
     * </p>
     */
    private final int volatility;

    /**
     * <p>
     * Constructs this object with the given parameters.
     * </p>
     * <p>
     * This constructor implies that there is no reliability set for the given rating type, and so the reliability
     * attribute should be set to null.
     * </p>
     *
     * @param ratingType
     *            the type of rating (e.g., design, development).
     * @param rating
     *            the actual rating in the design or development category.
     * @param numRatings
     *            the number of ratings in the design or development category.
     * @param volatility
     *            the volatility in the design or development category.
     * @throws IllegalArgumentException
     *             if ratingType is <code>null</code>, or if any other argument is negative.
     */
    public RatingInfo(RatingType ratingType, int rating, int numRatings, int volatility) {
        UserProjectDataStoreHelper.validateNull(ratingType, "ratingType");
        UserProjectDataStoreHelper.validateNegative(numRatings, "numRatings");
        UserProjectDataStoreHelper.validateNegative(volatility, "volatility");
        UserProjectDataStoreHelper.validateNegative(rating, "rating");

        this.rating = rating;
        this.ratingType = ratingType;
        this.numRatings = numRatings;
        this.volatility = volatility;
        this.reliability = null;
    }

    /**
     * <p>
     * Constructs this object with the given parameters.
     * </p>
     *
     * @param ratingType
     *            the type of rating (e.g., design, development).
     * @param rating
     *            the actual rating in the design or development category.
     * @param numRatings
     *            the number of ratings in the design or development category.
     * @param volatility
     *            the volatility in the design or development category.
     * @param reliability
     *            the reliability in the design or development category (represents 0 - 1, in another word, a percentage
     *            from 0 - 100).
     * @throws IllegalArgumentException
     *             if ratingType is <code>null</code>, or if any other argument is negative.
     */
    public RatingInfo(RatingType ratingType, int rating, int numRatings, int volatility, double reliability) {
        UserProjectDataStoreHelper.validateNull(ratingType, "ratingType");
        UserProjectDataStoreHelper.validateNegative(reliability, "reliability");
        UserProjectDataStoreHelper.validateNegative(numRatings, "numRatings");
        UserProjectDataStoreHelper.validateNegative(volatility, "volatility");
        UserProjectDataStoreHelper.validateNegative(rating, "rating");

        this.rating = rating;
        this.ratingType = ratingType;
        this.numRatings = numRatings;
        this.volatility = volatility;
        this.reliability = reliability;
    }

    /**
     * <p>
     * Returns the rating type of this object as set in the constructor.
     * </p>
     *
     * @return the type of rating (design, development) that this object encapsulates. Will never be null.
     */
    public RatingType getRatingType() {
        return this.ratingType;
    }

    /**
     * <p>
     * Returns the rating stored in this object as set in the constructor.
     * </p>
     *
     * @return the rating in the given rating type (design, development). Will never be negative.
     */
    public int getRating() {
        return this.rating;
    }

    /**
     * <p>
     * Returns the reliability stored in this object as set in the constructor.
     * </p>
     * <p>
     * If the reliability was never set (i.e., the field is null), returns null.
     * </p>
     *
     * @return the reliability in the given rating type (design, development). Will never represent a negative double,
     *         but may be null if no reliability is set.
     */
    public Double getReliability() {
        return this.reliability;
    }

    /**
     * <p>
     * Returns the existence of the reliability.
     * </p>
     * <p>
     * If the reliability was set in the constructor, true would be returned. Or if the reliability was kept null, false
     * would be returned.
     *
     * @return <code>true</code> if the reliability was set in the constructor, <code>false</code> if not.
     */
    public boolean hasReliability() {
        return reliability != null;
    }

    /**
     * <p>
     * Returns the number of ratings stored in this object as set in the constructor.
     * </p>
     *
     * @return the number of ratings in the given rating type (design, development). Will never be negative.
     */
    public int getNumRatings() {
        return this.numRatings;
    }

    /**
     * <p>
     * Returns the volatility stored in this object as set in the constructor.
     * </p>
     *
     * @return the volatility in the given rating type (design, development). Will never be negative.
     */
    public int getVolatility() {
        return this.volatility;
    }
}
