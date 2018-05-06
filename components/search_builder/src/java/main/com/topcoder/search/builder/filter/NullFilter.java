/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.filter;

import com.topcoder.search.builder.ValidationResult;

import java.util.Map;


/**
 * <p>
 * This class implements the Filter interface and is used to signify a search
 * filter for Null fields. Depending on the search environment, it may be used
 * to search for explicitly Null values, or for search fields when a value is
 * not present.
 * </p>
 *
 * <p>
 * Thread Safety: This class is immutable, and therefore thread-safe.
 * </p>
 *
 * <p>
 * The class is added in version 1.3.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class NullFilter extends AbstractSimpleFilter {
    /**
     * <p>
     * Constructor that produces a Null filter adding a null constraint for the
     * provided field.
     * </p>
     *
     * @param name
     *            The name of the field on which the filter will operate.
     * @throws IllegalArgumentException
     *             if the name is null or an empty String.
     */
    public NullFilter(String name) {
        super(name, "null");
    }

    /**
     * <p>
     * This method is used to validate the Filter and make sure that the
     * parameters that have been provided are valid for the context of the given
     * Filter. In the case of the NullFilter, it will simply verify that the
     * provided field is actually searchable.
     * </p>
     * <p>
     * A field is searchable if the validator Map contains a key entry for it.
     * </p>
     *
     * @return the <code>ValidationResult</code> object
     * @param validators
     *            a map containing <code>ObjectValidator</code> Objects
     * @param alias
     *            a Map containing mapping between real names and alias names
     * @throws IllegalArgumentException
     *             if any parameter is Null
     */
    public ValidationResult isValid(Map validators, Map alias) {
        return FilterHelper.isValidSimpleFilter(validators, alias, fieldName,
            value, this);
    }

    /**
     * <p>
     * Get the type of the Filter.
     * </p>
     * <p>
     * This returns Integer.MIN_VALUE
     * </p>
     *
     * @return a integer representing the type of the Filter
     * @deprecated This method has been deprecated, because it is preferable to
     *             examine the Filter class directly instead of relying on an
     *             Integer constant.
     */
    public int getFilterType() {
        return Integer.MIN_VALUE;
    }

    /**
     * <p>
     * Return a clone of the object.
     * </p>
     *
     * @return a clone of the object
     */
    public Object clone() {
        return new NullFilter(fieldName);
    }

    /**
     * <p>
     * This overrides the getValue parent class to always return a null.
     * </p>
     *
     * @return null String always
     */
    public Comparable getValue() {
        return null;
    }
}
