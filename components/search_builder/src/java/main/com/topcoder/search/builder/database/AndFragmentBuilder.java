/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.SearchBuilderHelper;

/**
 * <p>
 * This FragmentBuilder is used to handle building the SearchContext for the
 * AndFilter.
 * </p>
 *
 * <p>
 * Thread Safety: This class is thread-safe. All state information is handled within the searchContext.
 * </p>
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class AndFragmentBuilder implements SearchFragmentBuilder {

    /**
     * <p>
     * Default constructor.
     * </p>
     *
     */
    public AndFragmentBuilder() {
    }

    /**
     * <p>
     * Builds according to the AndFilter.
     * </p>
     *
     * @param filter
     *            the filter to build with.
     * @param searchContext
     *            the searchContext on which to build the filter.
     * @throws IllegalArgumentException
     *             if filter or searchContext is null.
     * @throws UnrecognizedFilterException
     *             if the filter cannot be handled by this
     *             SearchFragmentBuilder.
     */
    public void buildSearch(Filter filter, SearchContext searchContext) throws UnrecognizedFilterException {
        if (filter == null) {
            throw new IllegalArgumentException("The filter should not be null.");
        }
        if (searchContext == null) {
            throw new IllegalArgumentException("The searchContext should not be null.");
        }

        if (!(filter instanceof AndFilter)) {
            throw new UnrecognizedFilterException("The filter should be an AndFilter, but a type of "
                    + filter.getClass().getName() +  ".", filter);
        }
        AndFilter andFilter = (AndFilter) filter;
        SearchBuilderHelper.buildDBAbstractAssociativeFilter(andFilter, "AND", searchContext);
    }
}
