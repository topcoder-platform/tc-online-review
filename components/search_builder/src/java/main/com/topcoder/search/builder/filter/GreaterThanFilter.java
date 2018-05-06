/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.filter;

import java.util.Map;

import com.topcoder.search.builder.ValidationResult;


/**
 * <p>
 * This class extends AbstractSimpeFilter class. It is used to construct sreaterThan
 * search criterion. This class also provides concrete isValid(), and getFilterType() methods.
 * </p>
 *
 * <p>
 * The class is thread-safe since it is immutable.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 *
 */
public class GreaterThanFilter extends AbstractSimpleFilter {
    /**
     * <p>Create a new instance, by setting name and value.</p>
     *
     *
     * @param name the filed name of the search criterion
     * @param value a Comparable object used to set the upperThreshold and lowerThreshold attributes
     * @throws IllegalArgumentException if any parameter is Null
     * @throws IllegalArgumentException if <code>name</code> is empty
     */
    public GreaterThanFilter(String name, Comparable value) {
        super(name, value);
        lowerThreshold = value;
    }

    /**
     * <p>
     * Check if the filter is valid according to the rules presented in the validators,
     * if the fieldname is not searchable,return fail.
     * </p>
     *
     * @param validators a map containing <code>ObjectValidator</code> Objects
     * @return the <code>ValidationResult</code> object
     * @param alias a Map containing mapping between real names and alias names
     * @throws IllegalArgumentException if any parameter is Null
     * @throws IllegalArgumentException if validators do not contain the rule for the name.
     */
    public ValidationResult isValid(Map validators, Map alias) {
        return FilterHelper.isValidSimpleFilter(validators, alias, fieldName, value, this);
    }

    /**
     * <p>Get the type of the Filter.</p>
     *
     * @return a integer representing the type of the Filter
     * @deprecated This method has been deprecated.
     */
    public int getFilterType() {
        return Filter.GREATER_THAN_FILTER;
    }

    /**
     * <p>return a clone of the object.</p>
     *
     * @return a clone of the object
     */
    public Object clone() {
        return (new GreaterThanFilter(this.fieldName, this.value));
    }
}
