/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.OrFilter;


/**
 * <p>
 * This FragmentBuilder is used to handle building the SearchContext for the OrFilter.
 * </p>
 * <p>
 * Thread Safety: This class is thread-safe. All state information is handled within the searchContext.
 * </p>
 * <p>
 * Builds according to the OrFragmentBuilder.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class OrFragmentBuilder implements SearchFragmentBuilder {
    /**
     * <p>Default constructor.</p>
     *
     */
    public OrFragmentBuilder() {
    }

    /**
     * <p>Builds according to the OrFilter.</p>
     *
     * @param filter the filter to build with.
     * @param searchContext the searchContext on which to build the filter.
     * @throws IllegalArgumentException if filter or searchContext is null.
     * @throws UnrecognizedFilterException if the filter cannot be handled by this SearchFragmentBuilder.
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

        if (!(filter instanceof OrFilter)) {
            throw new UnrecognizedFilterException("The filter should be an OrFilter, but a type of "
                    + filter.getClass().getName() +  ".",
                filter);
        }

        OrFilter orFilter = (OrFilter) filter;
        SearchBuilderHelper.buildDBAbstractAssociativeFilter(orFilter, "OR",
            searchContext);
    }
}
