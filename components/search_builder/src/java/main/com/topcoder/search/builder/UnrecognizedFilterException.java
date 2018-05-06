/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * This exception is thrown if a SearchStrategy or SearchFragmentBuilder is
 * unable to recognize the provided filter.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class UnrecognizedFilterException extends SearchBuilderException {

    /**
     * <p>
     * This is the unrecognized filter. It is initialized in the constructor,
     * accessed in the getter and not changed afterwards.
     * </p>
     *
     */
    private Filter filter;

    /**
     * <p>
     * Cosntructor with custom message and the unrecognized filter.
     * </p>
     *
     * @param message
     *            a descriptive message to describe why this exception is
     *            generated
     * @param filter
     *            This is the filter that cannot be recognized.
     */
    public UnrecognizedFilterException(String message, Filter filter) {
        super(message);
        this.filter = filter;
    }

    /**
     * <p>
     * Retrieves the unrecognized filter.
     * </p>
     *
     * @return The unrecognized filter.
     */
    public Filter getFilter() {
        return filter;
    }
}
