/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests.database;

import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.accuracytests.AccuracyFilter;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p>A custom implementation of {@link SearchFragmentBuilder} to be used for testing purposes.</p>
 *
 * @author isv
 * @version 1.0
 * @since 1.3
 */
public class AccuracyFragmentBuilder implements SearchFragmentBuilder {

    /**
     * <p> This builds the search fragment appropriate for the given filter. This method will modify the provided
     * searchContext to reflect the results of building the search fragment appropriate for the given Filter. </p> <p>
     * The details of this method are dependent on the implementation of the class, but the following general structure
     * is suggested: - If the filter is an associative(composite) filter, then the implementation should find a way of
     * delegating each of the composing filters within to an approrpaite SearchFragmentBuilder. This may be done by
     * using the SearchContext class associator. - If the filter is a simple (atomic) filter, then the implementation
     * will modify the SearchContext in order to reflect the type and contents of the provided filter. </p>
     *
     * @param filter The filter on which to build the search fragment on.
     * @param searchContext The search context to modify and hold the results of filtering, and optionally to hold a
     * mapping of fragment ubilders to delegate to.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws UnrecognizedFilterException if this SearchFragmentBuilder is unable to properly handle the provided
     * filter.
     */
    public void buildSearch(Filter filter, SearchContext searchContext) throws UnrecognizedFilterException {
        if (!(filter instanceof AccuracyFilter)) {
            throw new UnrecognizedFilterException("Non-AccuracyFilter passed", filter);
        }
        AccuracyFilter accuracyFilter = (AccuracyFilter) filter;
        String name = searchContext.getFieldName(accuracyFilter.getName());
        if (name == null) {
            name = accuracyFilter.getName();
        }
        searchContext.getSearchString().append(name).append("=").append(accuracyFilter.getValue());
        searchContext.getBindableParameters().add(accuracyFilter.getValue());
    }
}
