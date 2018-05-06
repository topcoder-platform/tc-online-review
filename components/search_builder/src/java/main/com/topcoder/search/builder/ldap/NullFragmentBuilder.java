/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.ldap;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.NullFilter;


/**
 * <p> This SearchFragmentBuilder is used to handle building the SearchContext for the NullFilter for LDAP. </p>
 * <p> Thread Safety: This class is thread-safe. All state information is handled within the searchContext. </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class NullFragmentBuilder implements SearchFragmentBuilder {
    /**
     * <p> Default Constructor.</p>
     *
     */
    public NullFragmentBuilder() {
    }

    /**
     * <p>
     * Builds the LDAP search String for a NullFilter.
     * </p>
     *
     * <p> Implementation Notes: - If the Filter is not an NullFilter, then throw UnrecognizedFilterException.
     * - Append "(! (" to the String buffer of Search context.
     * - Append the field name of the NullFilter to the String Buffer of Search Context. - Append "=*))" to the String
     * Buffer of Search Context.
     * - Don't forget to look up the actual field name in the Search context to see if an alias was being used.
     * Use searchContext#getFieldName(filter.getName()) to look up an actual field nameand use that value if it is found,
     * otherwise use filter.getName().
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

        if (!(filter instanceof NullFilter)) {
            throw new UnrecognizedFilterException("The filter should be an NullFilter, but a type of "
                    + filter.getClass().getName() +  ".",
                filter);
        }

        NullFilter nullFilter = (NullFilter) filter;

        StringBuffer buffer = searchContext.getSearchString();

        buffer.append("(!(").append(SearchBuilderHelper.getRealName(
                nullFilter.getName(), searchContext));
        buffer.append("=*))");
    }
}
