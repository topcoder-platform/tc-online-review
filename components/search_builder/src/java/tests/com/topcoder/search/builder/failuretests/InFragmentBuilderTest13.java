/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.failuretests;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.database.InFragmentBuilder;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.InFilter;

/**
 * Failure tests for <code>InFragmentBuilder</code>.
 *
 * @author assistant
 * @version 1.3
 */
public class InFragmentBuilderTest13 extends TestCase {

    /**
     * Represents the builder to test.
     */
    private InFragmentBuilder builder = new InFragmentBuilder();

    /**
     * Test method for buildSearch(com.topcoder.search.builder.filter.Filter,
     * com.topcoder.search.builder.SearchContext).
     * In this case, the filter is null.
     * Expected : {@link IllegalArgumentException}
     * @throws Exception to JUnit
     */
    public void testBuildSearchNullFilter() throws Exception {
        try {
            builder.buildSearch(null, new SearchContext(new HashMap()));
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for buildSearch(com.topcoder.search.builder.filter.Filter,
     * com.topcoder.search.builder.SearchContext).
     * In this case, the search context is null.
     * Expected : {@link IllegalArgumentException}
     * @throws Exception to JUnit
     */
    public void testBuildSearchNullSearchContext() throws Exception {
        try {
            builder.buildSearch(new InFilter("a", new ArrayList()), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for buildSearch(com.topcoder.search.builder.filter.Filter,
     * com.topcoder.search.builder.SearchContext).
     * In this case, the filter is not an and filter.
     * Expected : UnrecognizedFilterException
     * @throws Exception to JUnit
     */
    public void testBuildSearchNonAndFilter() throws Exception {
        try {
            builder.buildSearch(new EqualToFilter("a", "a"), new SearchContext(new HashMap()));
            fail("UnrecognizedFilterException expected.");
        } catch (UnrecognizedFilterException e) {
            // should land here
        }
    }

}
