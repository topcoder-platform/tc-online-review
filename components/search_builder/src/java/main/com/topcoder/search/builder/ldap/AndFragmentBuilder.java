/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.ldap;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;


/**
 * <p>
 * This SearchFragmentBuilder is used to handle building the SearchContext for the AndFilter for LDAP.
 * </p>
 *
 * <p>
 * Thread Safety: This class is thread-safe.
 * All state information is handled within the searchContext.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class AndFragmentBuilder implements SearchFragmentBuilder {
    /**
     * <p> Default Constructor.</p>
     *
     */
    public AndFragmentBuilder() {
    }

    /**
     * <p>
     * Builds the LDAP search String for an AndFilter.
     * </p>
     *
     * @param filter the Filter from which to build the search String.
     * @param searchContext the searchContext to build the search String on.
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

        if (!(filter instanceof AndFilter)) {
            throw new UnrecognizedFilterException("The filter should be an AndFilter, but a type of "
                    + filter.getClass().getName() +  ".",
                filter);
        }

        AndFilter andFilter = (AndFilter) filter;
        SearchBuilderHelper.buildLDAPAbstractAssociativeFilter(andFilter, "&",
            searchContext);
    }
}
