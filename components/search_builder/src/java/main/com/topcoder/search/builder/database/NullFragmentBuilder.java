/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.NullFilter;


/**
 * <p>
 * This FragmentBuilder is used to handle building the SearchContext for the
 * NullFilter.
 * </p>
 * <p>
 * Thread Safety: - This class is thread-safe. All state information is handled
 * within the searchContext.
 * </p>
 *
 */
public class NullFragmentBuilder implements SearchFragmentBuilder {
    /**
     * <p>
     * Default constructor.
     * </p>
     *
     */
    public NullFragmentBuilder() {
    }

    /**
     * <p>
     * Builds according to the NullFilter.
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
    public void buildSearch(Filter filter, SearchContext searchContext)
        throws UnrecognizedFilterException {
        if (filter == null) {
            throw new IllegalArgumentException("The filter should not be null.");
        }

        if (searchContext == null) {
            throw new IllegalArgumentException(
                "The searchContext should not be null.");
        }

        if (!(filter instanceof NullFilter)) {
            throw new UnrecognizedFilterException("The filter should be an NullFilter, but a type of "
                    + filter.getClass().getName() +  ".",
                filter);
        }

        NullFilter nullFilter = (NullFilter) filter;

        StringBuffer buffer = searchContext.getSearchString();

        buffer.append(SearchBuilderHelper.getRealName(nullFilter.getName(), searchContext)).append(" IS NULL");
    }
}
