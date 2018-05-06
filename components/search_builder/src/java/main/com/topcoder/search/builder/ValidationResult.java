/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * This class models the filter validtion result. It is used by SearchBundle class and all classes in the
 * com.topcoder.search.builder.filter package.
 * It includes the following information: is the filter valid (boolean),
 * a message and a Filter object if validation fails.
 * <p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class ValidationResult {
    /**
     * It holds the filter validation result.
     *
     */
    private final boolean isResultValid;

    /**
     * It holds the message describing why validation fails if <code>isValid</code> is false.
     *
     */
    private final String message;

    /**
     * It holds the most relevant <code>Filter</code> object which fails the validation
     * if <code>isValid</code> is false.
     *
     */
    private final Filter failedFilter;

    /**
     * <p>
     * Create a new instance,with the given isResultValid, message and filter.
     * </p>
     *
     *
     * @param isValid true if valid, false otherwise
     * @param message a string describing why the validation fails
     * @param filter the Filter that fails the validation
     */
    private ValidationResult(boolean isValid, String message, Filter filter) {
        if ((message != null) && (message.length() == 0)) {
            throw new IllegalArgumentException("The message should not empty to construct ValidationResult.");
        }

        this.isResultValid = isValid;
        this.message = message;
        this.failedFilter = filter;
    }

    /**
     * <p>
     * Create a new ValidResult object representing the result is valid.
     * </p>
     *
     * @return a new ValidtionRresult object representing the result is valid
     */
    public static synchronized ValidationResult createValidResult() {
        return new ValidationResult(true, null, null);
    }

    /**
     * <p>Create a new ValidResult object representing the result is invalid.</p>
     *
     * @param message a string describing why the validation fails
     * @param filter the Filter that fails the validation
     * @return a new ValidtionRresult object representing the result is invalid
     */
    public static synchronized ValidationResult createInvalidResult(String message, Filter filter) {
        if(message == null) {
            throw new IllegalArgumentException("The message should not be null.");
        }
        if (filter == null) {
            throw new IllegalArgumentException("The filter should not be null to createInvalidResult.");
        }

        if (message.length() == 0) {
            throw new IllegalArgumentException("The message should not be empty to createInvalidResult.");
        }

        return new ValidationResult(false, message, filter);
    }

    /**
     * getter of the message attribute of the instance.
     *
     * @return message, a descriptive string
     */
    public synchronized String getMessage() {
        return this.message;
    }

    /**
     * get of the failedFilter attribute.
     *
     *
     * @return a Filter object
     */
    public synchronized Filter getFailedFilter() {
        return (Filter) failedFilter.clone();
    }

    /**
     * get of the isValid attribute.
     *
     *
     * @return a boolean indicating if the result is valid. true if valid, false otherwise
     */
    public synchronized boolean isValid() {
        return this.isResultValid;
    }
}
