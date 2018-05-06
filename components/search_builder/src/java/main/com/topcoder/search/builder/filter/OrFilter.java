/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.filter;

import java.util.List;
import java.util.Map;

import com.topcoder.search.builder.ValidationResult;


/**
 * <p>
 * This class extends AbstractAssociativeFilter. It is used to construct OR search criterion.
 * The class is thread safe. The filter attribute is initialized by constructor,
 * and changed by addFilter() method, all other methods use it. Thus it is like a read-write structure.
 * </p>
 *
 * <p>
 * Thread-safety is ensured by using a read-write lock over filters attribute.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class OrFilter extends AbstractAssociativeFilter {
    /**
     * <p>
     * Create a new instance,by setting the first and second filter
     * add them to the list of filters.
     * </p>
     *
     * @param first a <code> Filter </code> object
     * @param second a <code> Filter </code> object
     * @throws IllegalArgumentException if any parameter is Null
     */
    public OrFilter(Filter first, Filter second) {
        super(first, second);
    }

    /**
     * <p>Create a new instance,by set the filters.</p>
     *
     * @param filters to construct the filter
     */
    public OrFilter(List filters) {
        super(filters);
    }

    /**
     * <p>Test to see if the filter is valid according to the rules presented in the validators.</p>
     *
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
        return Filter.OR_FILTER;
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
