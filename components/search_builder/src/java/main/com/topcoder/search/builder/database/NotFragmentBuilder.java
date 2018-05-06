/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.NotFilter;


/**
 * <p>
 * This FragmentBuilder is used to handle building the SearchContext for the
 * NotFilter.
 * </p>
 *
 * <p>
 * Thread Safety: This class is thread-safe. All state information is handled
 * within the searchContext.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class NotFragmentBuilder implements SearchFragmentBuilder {
    /**
     * <p>
     * Default constructor.
     * </p>
     *
     */
    public NotFragmentBuilder() {
        // your code here
    }

    /**
     * <p>
     * Builds according to the NotFilter.
     * </p>
     *
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
    public void buildSearch(Filter filter, SearchContext searchContext)
        throws UnrecognizedFilterException {
        if (filter == null) {
            throw new IllegalArgumentException("The filter should not be null.");
        }

        if (searchContext == null) {
            throw new IllegalArgumentException(
                "The searchContext should not be null.");
        }

        if (!(filter instanceof NotFilter)) {
            throw new UnrecognizedFilterException("The filter should be an NotFilter.",
                filter);
        }

        NotFilter notFilter = (NotFilter) filter;

        StringBuffer buffer = searchContext.getSearchString();

        buffer.append("NOT (");

        // build the inner filter
        Filter innerfilter = notFilter.getFilter();

        SearchFragmentBuilder builder = searchContext.getFragmentBuilder(innerfilter);

        // the can not look up the filter successfully, then throw
        // UnrecognizedFilterException
        if (builder == null) {
            throw new UnrecognizedFilterException("The filter should be an NotFilter, but a type of "
                    + filter.getClass().getName() +  ".",
                innerfilter);
        }

        builder.buildSearch(innerfilter, searchContext);

        buffer.append(")");
    }
}
