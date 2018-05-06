/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * This is the interface that is used to define the contract for building a
 * SearchContext out of a provided filter. Implementations usually come as a
 * 'family' to support a specific type of datastore. Each member of the family
 * will correspond to one or more of the Search Builder Filters. Adding filters
 * may entail adding additional SearchFragmentBuilders, and adding a new type of
 * datastore may require creating a new family of SearchFragmentBuilders.
 * </p>
 *
 * <p>
 * Thread Safety: Implementations are required to be thread safe. All necessary
 * state can be maintained within the SearchContext.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public interface SearchFragmentBuilder {
    /**
     * <p>
     * This builds the search fragment appropriate for the given filter. This
     * method will modify the provided searchContext to reflect the results of
     * building the search fragment appropriate for the given Filter.
     * </p>
     *
     * <p>
     * The details of this method are dependent on the implementation of the
     * class, but the following general structure is suggested:
     *
     * <li> If the filter is an associative(composite) filter, then the
     * implementation should find a way of delegating each of the composing
     * filters within to an approrpaite SearchFragmentBuilder. This may be done
     * by using the SearchContext class associator. </li>
     *
     * <li> If the filter is a simple (atomic) filter, then the implementation
     * will modify the SearchContext in order to reflect the type and contents
     * of the provided filter. </li>
     *
     * </p>
     *
     * @param filter
     *            The filter on which to build the search fragment on.
     * @param searchContext
     *            The search context to modify and hold the results of
     *            filtering, and optionally to hold a mapping of fragment
     *            ubilders to delegate to.
     * @throws IllegalArgumentException
     *             if any parameter is null.
     * @throws UnrecognizedFilterException
     *             if this SearchFragmentBuilder is unable to properly handle
     *             the provided filter.
     */
    public void buildSearch(Filter filter, SearchContext searchContext) throws UnrecognizedFilterException;
}
