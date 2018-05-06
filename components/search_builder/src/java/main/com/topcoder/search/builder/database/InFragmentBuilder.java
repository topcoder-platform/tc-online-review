/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import java.util.Iterator;
import java.util.List;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;

/**
 * <p>
 * This FragmentBuilder is used to handle building the SearchContext
 * for the InFilter.
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
public class InFragmentBuilder implements SearchFragmentBuilder {

    /**
     * <p>Default constructor.</p>
     *
     */
    public InFragmentBuilder() {
    }

    /**
     * <p>
     * Builds according to the InFilter.
     * </p>
     *
     * @param filter the filter to build with.
     * @param searchContext the searchContext on which to build the filter.
     * @throws IllegalArgumentException if filter or searchContext is null.
     * @throws UnrecognizedFilterException if the filter cannot be handled by this SearchFragmentBuilder.
     */
    public void buildSearch(Filter filter, SearchContext searchContext) throws UnrecognizedFilterException {
        if (filter == null) {
            throw new IllegalArgumentException("The filter should not be null.");
        }
        if (searchContext == null) {
            throw new IllegalArgumentException("The searchContext should not be null.");
        }

        if (!(filter instanceof InFilter)) {
            throw new UnrecognizedFilterException("The filter should be an InFilter.", filter);
        }

        InFilter inFilter = (InFilter) filter;

        StringBuffer buffer = searchContext.getSearchString();
        List params = searchContext.getBindableParameters();

        //get the real name and append to buffer
        buffer.append(SearchBuilderHelper.getRealName(inFilter.getName(), searchContext));

        buffer.append(" IN (");
        List values = inFilter.getList();
        boolean isFirst = true;

        //set the String, also the paramter, the add should be same
        for (Iterator it = values.iterator(); it.hasNext();) {
            params.add(it.next());

            if (isFirst) {
                isFirst = false;
            } else {
                buffer.append(", ");
            }
            buffer.append("?");
        }
        buffer.append(")");
    }
}
