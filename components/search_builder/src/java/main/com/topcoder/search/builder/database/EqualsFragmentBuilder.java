/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;


/**
 * <p>
 * This FragmentBuilder is used to handle building the SearchContext for the EqualsFilter.
 * </p>
 * <p> Thread Safety: This class is thread-safe. All state information is handled within the searchContext.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class EqualsFragmentBuilder implements SearchFragmentBuilder {
    /**
     * <p>Default constructor.</p>
     *
     */
    public EqualsFragmentBuilder() {
    }

    /**
     * <p>Builds according to the EqualsFilter.</p>
     * @param filter the filter to build with.
     * @param searchContext the searchContext on which to build the filter.
     * @throws UnrecognizedFilterException if the filter can not be recognized
     * @throws IllegalArgumentException if filter or searchContext is null.
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

        if (!(filter instanceof EqualToFilter)) {
            throw new UnrecognizedFilterException("The filter should be an EqualToFilter, but a type of "
                    + filter.getClass().getName() +  ".",
                filter);
        }

        EqualToFilter equalToFilter = (EqualToFilter) filter;

        SearchBuilderHelper.buildDBSimpleFilter(equalToFilter, "=",
            searchContext, equalToFilter.getValue());
    }
}
