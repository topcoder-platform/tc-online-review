/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests;

import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.ValidationResult;

import java.util.Map;

/**
 * <p>A custom implementation of {@link Filter} interface to be utilized for testing purposes.</p>
 *
 * @author  isv
 * @version 1.0
 */
public class AccuracyFilter implements Filter {

    /**
     * <p>A <code>String</code> providing the message to be provided in case the filter is not valid.</p>
     */
    public static final String INVALID_FILTER_MESSAGE = "Invalid Filter";

    private boolean valid = false;

    private String name = null;

    private String value = null;

    public AccuracyFilter(boolean valid, String name, String value) {
        this.valid = valid;
        this.name = name;
        this.value = value;
    }

    /**
     * <p>Test to see if this filter is valid</p>
     *
     * @param validators a map containing <code>ObjectValidator</code> Objects
     * @param alias a Map containing mapping between real names and alias names
     * @return the <code>ValidationResult</code> object
     * @throws IllegalArgumentException if any parameter is Null
     */
    public ValidationResult isValid(Map validators, Map alias) {
        if (this.valid) {
            return ValidationResult.createValidResult();
        } else {
            return ValidationResult.createInvalidResult(INVALID_FILTER_MESSAGE, this);
        }
    }

    /**
     * <p>Get the type of the Filter.</p> << What changed >> This method is now deprecated.
     *
     * @return a integer representing the type of the Filter
     * @deprecated This method has been deprecated, because it is preferable to examine the Filter class directly
     *             instead of relying on an Integer constant.
     */
    public int getFilterType() {
        return -1;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public Object clone() {
        return null;
    }
}
