/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.filter;

import java.util.List;
import java.util.Map;

import com.topcoder.search.builder.ValidationResult;


/**
 * Purpose:
 * This class extends AbstractAssociativeFilter. It is used to construct AND search criterion.
 *
 * <p>Thread-safety is ensured by using a read-write lock over filters attribute</p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class AndFilter extends AbstractAssociativeFilter {
    /**
     * <p>
     * Create a new instance,by providing the augend and addend add them to the filter list.
     * </p>
     *
     *
     * @param augend a <code> Filter </code> object
     * @param addend a <code> Filter </code> object
     * @throws IllegalArgumentException if any parameter is Null
     */
    public AndFilter(Filter augend, Filter addend) {
        super(augend, addend);
    }

    /**
     * <p>Create a new instance,by providing the filters list.</p>
     *
     * @param filters to set AbstractAssociativeFilter
     * @throws IllegalArgumentException if param is null
     * @throws IllegalArgumentException if param is empty
     */
    public AndFilter(List filters) {
        super(filters);
    }

    /**
     * <p>
     * Test to see if the filter is valid according to the rules presented in the validators,
     * Check all the filters in the list.
     * </p>
     *
     *
     * @param validators a map containing <code>ObjectValidator</code> Objects
     * @return the <code>ValidationResult</code> object
     * @param alias a Map containing mapping between real names and alias names
     * @throws IllegalArgumentException if any parameter is Null
     */
    public synchronized ValidationResult isValid(Map validators, Map alias) {
        return FilterHelper.isValidAssociativeFilter(validators, alias, filters);
    }

    /**
     * <p>Get the type of the Filter.</p>
     *
     *
     * @return a integer representing the type of the Filter
     * @deprecated This method has been deprecated.
     */
    public int getFilterType() {
        return Filter.AND_FILTER;
    }

    /**
     * <p>return a clone of the object.</p>
     *
     * @return a clone of the object
     */
    public synchronized Object clone() {
        return FilterHelper.associativeFilterclone(this);
    }
}
