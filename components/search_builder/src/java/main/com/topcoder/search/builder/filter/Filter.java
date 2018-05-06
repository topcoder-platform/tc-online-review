/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.filter;

import java.io.Serializable;
import java.util.Map;

import com.topcoder.search.builder.ValidationResult;


/**
 * <p>
 * This interface defines APIs that every implementation must adhere to.
 * It is part of the composite pattern. It is a common interface for both individual and
 * composite components so that both the individual components and composite components can be viewed uniformly.
 * It also defines a set of static final integer constants representing Filter types.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public interface Filter extends Cloneable, Serializable {
    /**
     * It is a constant, representing the Filter is a LessThanFilter.
     * which constantly equals 0.
     *
     * @deprecated it is not used any more
     */
    public static final int LESS_THAN_FILTER = 0;

    /**
     * It is a constant, representing the Filter is a LessThanOrEqualToFilter.
     * which constantly equals 1.
     *
     * @deprecated it is not used any more
     */
    public static final int LESS_THAN_OR_EQUAL_TO_FILTER = 1;

    /**
     * It is a constant, representing the Filter is a GreaterThanFilter.
     * which constantly equals 2.
     *
     * @deprecated it is not used any more
     */
    public static final int GREATER_THAN_FILTER = 2;

    /**
     * It is a constant, representing the Filter is a GreaterThanOrEqualToFilter.
     * which constantly equals 3.
     *
     * @deprecated it is not used any more
     */
    public static final int GREATER_THAN_OR_EQUAL_TO_FILTER = 3;

    /**
     * It is a constant, representing the Filter is a EqualToFilter.
     * which constantly equals 4.
     *
     * @deprecated it is not used any more
     */
    public static final int EQUAL_TO_FILTER = 4;

    /**
     * It is a constant, representing the Filter is a BetweenFilter.
     * which constantly equals 5.
     *
     * @deprecated it is not used any more
     */
    public static final int BETWEEN_FILTER = 5;

    /**
     * It is a constant, representing the Filter is a InFilter.
     * which constantly equals 6.
     *
     * @deprecated it is not used any more
     */
    public static final int IN_FILTER = 6;

    /**
     * It is a constant, representing the Filter is a AndFilter.
     * which constantly equals 7.
     *
     * @deprecated it is not used any more
     */
    public static final int AND_FILTER = 7;

    /**
     * It is a constant, representing the Filter is a OrFilter.
     * which constantly equals 8.
     *
     * @deprecated it is not used any more
     */
    public static final int OR_FILTER = 8;

    /**
     * It is a constant, representing the Filter is a NotFilter.
     * which constantly equals 9.
     *
     * @deprecated it is not used any more
     */
    public static final int NOT_FILTER = 9;
    /**
     * It is a constant, representing the Filter is a LikeFilter.
     * which constantly equals 10.
     *
     * This field is added in version 1.2.
     *
     * @deprecated it is not used any more
     */
    public static final int LIKE_FILTER = 10;

    /**
     * <p>Test to see if this filter is valid.</p>
     *
     *
     * @param validators a map containing <code>ObjectValidator</code> Objects
     * @return the <code>ValidationResult</code> object
     * @param alias a Map containing mapping between real names and alias names
     * @throws IllegalArgumentException if any parameter is Null
     */
    ValidationResult isValid(Map validators, Map alias);

    /**
     * <p>Get the type of the Filter.</p>
     *
     *
     * @return a integer representing the type of the Filter
     * @deprecated This method has been deprecated.
     */
    int getFilterType();

    /**
     *
     * return the clone of the object.
     *
     * @return a clone of the filter.
     */
    Object clone();
}
