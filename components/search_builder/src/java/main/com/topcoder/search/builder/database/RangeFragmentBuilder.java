/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.AbstractSimpleFilter;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;


/**
 * <p>
 * This FragmentBuilder is used to handle building the SearchContext for the
 * GreaterThanFilter, LessThanFilter, GreaterThanOrEqualToFilter,
 * LessThanOrEqualToFilter, BetweenFilter.
 * </p>
 * <p>
 * Thread Safety: This class is thread-safe. All state information is handled
 * within the searchContext.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class RangeFragmentBuilder implements SearchFragmentBuilder {
    /**
     * <p>
     * Default constructor.
     * </p>
     *
     */
    public RangeFragmentBuilder() {
    }

    /**
     * <p>
     * Builds according to the GreaterThanFilter, LessThanFilter,
     * GreaterThanOrEqualToFilter, LessThanOrEqualToFilter.
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

        // if the filter is between filter, append both the lower and upper
        if (filter instanceof BetweenFilter) {
            BetweenFilter betweenFilter = (BetweenFilter) filter;

            SearchBuilderHelper.buildDBSimpleFilter(betweenFilter, ">=",
                searchContext, betweenFilter.getLowerThreshold());
            searchContext.getSearchString().append(" AND ");
            SearchBuilderHelper.buildDBSimpleFilter(betweenFilter, "<=",
                searchContext, betweenFilter.getUpperThreshold());

            return;
        }

        String operator = null;

        // determine the operator by the type of the filter
        if (filter instanceof GreaterThanFilter) {
            operator = ">";
        } else if (filter instanceof LessThanFilter) {
            operator = "<";
        } else if (filter instanceof GreaterThanOrEqualToFilter) {
            operator = ">=";
        } else if (filter instanceof LessThanOrEqualToFilter) {
            operator = "<=";
        }

        // if operator is null, means the filter is of an invalid type
        if (operator == null) {
            throw new UnrecognizedFilterException("The filter should be an Range Filter.",
                filter);
        }

        // builder the simple filter with the search context
        AbstractSimpleFilter simpleFilter = (AbstractSimpleFilter) filter;
        SearchBuilderHelper.buildDBSimpleFilter(simpleFilter, operator,
            searchContext, simpleFilter.getValue());
    }
}
