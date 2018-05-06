/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.ldap;

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
 * <p> This SearchFragmentBuilder is used to handle building the SearchContext for the GreaterThanFilter,
 * LessThanFilter, GreaterThanOrEqualToFilter, LessThanOrEqualToFilter, and BetweenFilter for LDAP. </p>
 * <p>
 * Thread Safety: - This class is thread-safe. All state information is handled within the searchContext.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class RangeFragmentBuilder implements SearchFragmentBuilder {
    /**
     * <p> Default Constructor.</p>
     *
     */
    public RangeFragmentBuilder() {
        // your code here
    }

    /**
     * <p>
     * Builds the LDAP search String for a GreaterThanFilter, LessThanFilter, GreaterThanOrEqualToFilter,
     * LessThanOrEqualToFilter, or BetweenFilter.
     * </p>
     *
     * @param filter the Filter from which to build the search String.
     * @param searchContext the searchContext to build the search String on.
     * @throws UnrecognizedFilterException
     * @throws IllegalArgumentException if either parameter is null.
     * @throws UnrecognizedFilterException if this SearchFragmentBuilder cannot recognize the filter.
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

        //if the filter is between filter, append both the lower and upper
        if (filter instanceof BetweenFilter) {
            BetweenFilter betweenFilter = (BetweenFilter) filter;

            searchContext.getSearchString().append("(&");
            SearchBuilderHelper.buildLDAPSimpleFilter(betweenFilter, ">=",
                searchContext, betweenFilter.getLowerThreshold());

            SearchBuilderHelper.buildLDAPSimpleFilter(betweenFilter, "<=",
                searchContext, betweenFilter.getUpperThreshold());
            searchContext.getSearchString().append(")");

            return;
        }

        String operator = null;

        //determine the operator by the type of the filter
        if (filter instanceof GreaterThanFilter) {
            GreaterThanFilter greaterThanFilter = (GreaterThanFilter) filter;

            //ladp not support >, so used >= without =
            searchContext.getSearchString().append("(&(!");
            SearchBuilderHelper.buildLDAPSimpleFilter(greaterThanFilter, "=",
                searchContext, greaterThanFilter.getValue());
            searchContext.getSearchString().append(")");
            SearchBuilderHelper.buildLDAPSimpleFilter(greaterThanFilter, ">=",
                searchContext, greaterThanFilter.getValue());
            searchContext.getSearchString().append(")");

            return;
        } else if (filter instanceof LessThanFilter) {
            LessThanFilter lessThanFilter = (LessThanFilter) filter;

            //ladp not support <, so used <= without =
            searchContext.getSearchString().append("(&(!");
            SearchBuilderHelper.buildLDAPSimpleFilter(lessThanFilter, "=",
                searchContext, lessThanFilter.getValue());
            searchContext.getSearchString().append(")");
            SearchBuilderHelper.buildLDAPSimpleFilter(lessThanFilter, "<=",
                searchContext, lessThanFilter.getValue());
            searchContext.getSearchString().append(")");

            return;
        } else if (filter instanceof GreaterThanOrEqualToFilter) {
            operator = ">=";
        } else if (filter instanceof LessThanOrEqualToFilter) {
            operator = "<=";
        }

        //if operator is null, means the filter is of an invalid type
        if (operator == null) {
            throw new UnrecognizedFilterException("The filter should be an Range Filter.",
                filter);
        }

        //builder the simple filter with the search context
        AbstractSimpleFilter simpleFilter = (AbstractSimpleFilter) filter;
        SearchBuilderHelper.buildLDAPSimpleFilter(simpleFilter, operator,
            searchContext, simpleFilter.getValue());
    }
}
