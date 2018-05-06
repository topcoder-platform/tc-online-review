/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import java.util.List;
import java.util.Map;

import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * The search strategy is responsible for connecting to the datastore, building
 * the necessary query String (if applicable) and executing it against the
 * datastore. The object returned from the search is dependent on the type of
 * datastore that is used.
 * </p>
 *
 * <p>
 * Thread Safety: - The search method should be able to consistently handle
 * multiple concurrent calls to the search method. Mutability for an
 * implementation is optional, but if it is mutable, then the user must be
 * warned of changing the object state while the SearchStrategy is deployed
 * within a SearchBundle that is actively being used.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public interface SearchStrategy {
    /**
     * <p>
     * This is the actual work method of this interface. Once this is called,
     * the implementation will perform all the necessary actions that are
     * necessary to search the datastore under the given context and constrained
     * with the provided filters. None of the passed parameters should be
     * modified by this method.
     * </p>
     *
     * @return An object that contains the results of the search. The actual
     *         object returned is implementation dependent.
     * @param context
     *            This is the context under which the search is performed.
     * @param filter
     *            The search filter which will constrain the search results.
     * @param returnFields
     *            A list of return fields to further constrain the search an
     *            empty List means that all fields in the context should be
     *            returned by the search.
     * @param aliasMap
     *            a map of strings, holding the alternate names of fields as
     *            keys and their actual values in the datastore as the
     *            respective values.
     * @throws PersistenceOperationException
     *             to wrap any exception that occurs while searching (except
     *             UnrecognizedFilterException and IAE).
     * @throws UnrecognizedFilterException if the filter can not be recognized
     * @throws IllegalArgumentException
     *             if any parameter is null, or if context is an empty trimmed
     *             String or if returnFields or aliasMap contains null or empty
     *             String parameters.
     */
    public Object search(String context, Filter filter, List returnFields, Map aliasMap)
        throws PersistenceOperationException, UnrecognizedFilterException;
}
