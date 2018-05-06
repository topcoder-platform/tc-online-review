/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.ldap;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.OrFilter;


/**
 * <p>
 * This SearchFragmentBuilder is used to handle building the SearchContext for
 * the OrFilter for LDAP.
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
public class OrFragmentBuilder implements SearchFragmentBuilder {
    /**
     * <p>
     * Default Constructor.
     * </p>
     *
     */
    public OrFragmentBuilder() {
    }

    /**
     * <p>
     * Builds the LDAP search String for an OrFilter.
     * </p>
     *
     * @param filter
     *            the Filter from which to build the search String.
     * @param searchContext
     *            the searchContext to build the search String on.
     * @throws IllegalArgumentException
     *             if either parameter is null.
     * @throws UnrecognizedFilterException
     *             if this SearchFragmentBuilder cannot recognize the filter.
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
        SearchBuilderHelper.buildLDAPAbstractAssociativeFilter(orFilter, "|",
            searchContext);
    }
}
