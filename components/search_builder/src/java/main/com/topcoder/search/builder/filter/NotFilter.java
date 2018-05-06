/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.filter;

import java.util.Map;

import com.topcoder.search.builder.ValidationResult;

/**
 * <p>
 * This class is a concrete implementor of the Filter interface.
 * It is used to construct NOT search criterion.
 * </p>
 *
 * <p>
 * The class is thread-safe since it is immutable
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 *
 */
public class NotFilter implements Filter {
    /**
     * It will hold the Filter to be negated filter.
     *
     */
    private final Filter filterToNegate;

    /**
     * <p>Create a new instance,by setting filterToNegate.</p>
     *
     *
     * @param filterToNegate filter to be negated.
     * @throws IllegalArgumentException if any parameter is Null
     * @throws IllegalArgumentException if the filterToNegate is equal to the notFilter itself.
     */
    public NotFilter(Filter filterToNegate) {
        if (filterToNegate == null) {
            throw new IllegalArgumentException(
                "The filter should not be null to construct NotFilter.");
        }
        //if the filterToNegate equals to the notFilter, it is illegal
        if (filterToNegate == this) {
            throw new IllegalArgumentException("The filterToNegate should not Equal to the notfilter.");
        }
        this.filterToNegate = filterToNegate;
    }

    /**
     * <p>check if the filter is valid according to the rules presented in the validators. </p>
     *
     *
     * @param validators a map containing <code>ObjectValidator</code> Objects
     * @return the <code>ValidationResult</code> object
     * @param alias a Map containing mapping between real names and alias names
     * @throws IllegalArgumentException if any parameter is Null
     */
    public ValidationResult isValid(Map validators, Map alias) {
        //Valid the filterToNegate
        ValidationResult result = filterToNegate.isValid(validators, alias);

        //invalid
        if (!result.isValid()) {
            return result;
        }

        //valid
        return ValidationResult.createValidResult();
    }

    /**
     * <p>Get the type of the Filter.</p>
     *
     * @return a integer representing the type of the Filter
     * @deprecated This method has been deprecated.
     */
    public int getFilterType() {
        return Filter.NOT_FILTER;
    }

    /**
     * <p>return the component filter.</p>
     *
     *
     * @return the filter to be negated
     */
    public Filter getFilter() {
        return (Filter) filterToNegate.clone();
    }

    /**
     * <p>return a clone of the object.</p>
     *
     * @return a clone of the object
     */
    public Object clone() {
        return new NotFilter((Filter) filterToNegate.clone());
    }
}
