/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.ldap;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;

import java.util.Iterator;
import java.util.List;


/**
 * <p>
 * This SearchFragmentBuilder is used to handle building the SearchContext for
 * the InFilter for LDAP.
 * </p>
 * <p>
 * Thread Safety: This class is thread-safe. All state information is handled
 * within the searchContext.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class InFragmentBuilder implements SearchFragmentBuilder {
    /**
     * <p>
     * Default Constructor.
     * </p>
     *
     */
    public InFragmentBuilder() {
        // your code here
    }

    /**
     * <p>
     * Builds the LDAP search String for a InFilter.
     * </p>
     *
     * @param filter
     *            the Filter from which to build the search String.
     * @param searchContext the context of the searching
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

        if (!(filter instanceof InFilter)) {
            throw new UnrecognizedFilterException("The filter should be an InFilter, but a type of "
                    + filter.getClass().getName() +  ".",
                filter);
        }

        InFilter inFilter = (InFilter) filter;

        List values = inFilter.getList();

        // get the real name firstly
        String name = SearchBuilderHelper.getRealName(inFilter.getName(),
                searchContext);

        StringBuffer buffer = searchContext.getSearchString();

        // the length used to do insert
        int initLength = buffer.toString().length();

        Iterator it = values.iterator();

        // append the first value firstly
        buffer.append("(").append(name).append("=")
              .append(String.valueOf(it.next())).append(")");

        while (it.hasNext()) {
            buffer.insert(initLength, "(|");

            // append the next value
            buffer.append("(").append(name).append("=")
                  .append(String.valueOf(it.next())).append(")");
            buffer.append(")");
        }
    }
}
