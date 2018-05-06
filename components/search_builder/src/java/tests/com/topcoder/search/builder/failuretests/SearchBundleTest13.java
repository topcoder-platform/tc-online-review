/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.failuretests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.search.builder.PersistenceOperationException;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchStrategy;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.datavalidator.NullValidator;

import junit.framework.TestCase;

/**
 * Failure tests for <code>SearchBundle</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class SearchBundleTest13 extends TestCase {

    /**
     * Represents the search bundle to test.
     */
    private SearchBundle sb;

    /**
     * Represents the fields.
     */
    private final Map FIELDS = new HashMap();

    /**
     * Represents the alias.
     */
    private final Map ALIAS = new HashMap();

    /**
     * Represents the search strategy.
     */
    private final SearchStrategy ss = new SearchStrategy() {

        public Object search(String context, Filter filter,
                List returnFields, Map aliasMap) throws PersistenceOperationException, UnrecognizedFilterException {
            return null;
        }
    };

    /**
     * Set up the environment.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();

        FIELDS.put("key", new NullValidator());
        sb = new SearchBundle("name", FIELDS, ALIAS, "context");
    }

    /**
     * Test method for SearchBundle(java.lang.String, java.util.Map, java.util.Map, java.lang.String).
     * In this case, the name is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testConstructor1NullName() {
        try {
            new SearchBundle(null, FIELDS, ALIAS, "context");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String, java.util.Map, java.util.Map, java.lang.String).
     * In this case, the name is empty.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testConstructor1EmptyName() {
        try {
            new SearchBundle(" ", FIELDS, ALIAS, "context");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String, java.util.Map, java.util.Map, java.lang.String).
     * In this case, the fields is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testConstructor1NullFields() {
        try {
            new SearchBundle("name", null, ALIAS, "context");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String, java.util.Map, java.util.Map, java.lang.String).
     * In this case, the alias is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testConstructor1NullAlias() {
        try {
            new SearchBundle("name", FIELDS, null, "context");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String, java.util.Map, java.util.Map, java.lang.String).
     * In this case, the context is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testConstructor1NullContext() {
        try {
            new SearchBundle("name", FIELDS, ALIAS, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String, java.util.Map, java.util.Map, java.lang.String).
     * In this case, the context is empty.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testConstructor1EmptyContext() {
        try {
            new SearchBundle("name", FIELDS, ALIAS, " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String, java.util.Map, java.util.Map, java.lang.String).
     * In this case, the context is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testConstructor2NullContext() {
        try {
            new SearchBundle("name", ALIAS, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String, java.util.Map, java.util.Map, java.lang.String).
     * In this case, the context is empty.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testConstructor2EmptyContext() {
        try {
            new SearchBundle("name", ALIAS, " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String, java.util.Map, java.util.Map, java.lang.String).
     * In this case, the name is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testConstructor2NullName() {
        try {
            new SearchBundle(null, FIELDS, "context");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String, java.util.Map, java.util.Map, java.lang.String).
     * In this case, the name is empty.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testConstructor2EmptyName() {
        try {
            new SearchBundle(" ", FIELDS, "context");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String, java.util.Map, java.util.Map, java.lang.String).
     * In this case, the fields is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testConstructor2NullFields() {
        try {
            new SearchBundle("name", null, "context");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String,
     * java.util.Map, java.lang.String, com.topcoder.search.builder.SearchStrategy).
     * In this case, the name is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testSearchBundle3NullName() {
        try {
            new SearchBundle(null, FIELDS, "context", ss);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String,
     * java.util.Map, java.lang.String, com.topcoder.search.builder.SearchStrategy).
     * In this case, the name is empty.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testSearchBundle3EmptyName() {
        try {
            new SearchBundle(" ", FIELDS, "context", ss);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String,
     * java.util.Map, java.lang.String, com.topcoder.search.builder.SearchStrategy).
     * In this case, the fields is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testSearchBundle3NullFields() {
        try {
            new SearchBundle("name", null, "context", ss);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String,
     * java.util.Map, java.lang.String, com.topcoder.search.builder.SearchStrategy).
     * In this case, the context is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testSearchBundle3NullContext() {
        try {
            new SearchBundle("name", FIELDS, null, ss);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String,
     * java.util.Map, java.lang.String, com.topcoder.search.builder.SearchStrategy).
     * In this case, the context is empty.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testSearchBundle3EmptyContext() {
        try {
            new SearchBundle("name", FIELDS, " ", ss);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String,
     * java.util.Map, java.lang.String, com.topcoder.search.builder.SearchStrategy).
     * In this case, the strategy is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testSearchBundle3NullStrategy() {
        try {
            new SearchBundle("name", FIELDS, " ", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String,
     * java.util.Map, java.lang.String, com.topcoder.search.builder.SearchStrategy).
     * In this case, the name is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testSearchBundle4NullName() {
        try {
            new SearchBundle(null, FIELDS, ALIAS, "context", ss);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String,
     * java.util.Map, java.lang.String, com.topcoder.search.builder.SearchStrategy).
     * In this case, the name is empty.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testSearchBundle4EmptyName() {
        try {
            new SearchBundle(" ", FIELDS, ALIAS, "context", ss);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String,
     * java.util.Map, java.lang.String, com.topcoder.search.builder.SearchStrategy).
     * In this case, the fields is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testSearchBundle4NullFields() {
        try {
            new SearchBundle("name", null, ALIAS, "context", ss);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String,
     * java.util.Map, java.lang.String, com.topcoder.search.builder.SearchStrategy).
     * In this case, the context is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testSearchBundle4NullContext() {
        try {
            new SearchBundle("name", FIELDS, ALIAS, null, ss);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String,
     * java.util.Map, java.lang.String, com.topcoder.search.builder.SearchStrategy).
     * In this case, the context is empty.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testSearchBundle4EmptyContext() {
        try {
            new SearchBundle("name", FIELDS, ALIAS, " ", ss);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String,
     * java.util.Map, java.lang.String, com.topcoder.search.builder.SearchStrategy).
     * In this case, the strategy is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testSearchBundle4NullStrategy() {
        try {
            new SearchBundle("name", FIELDS, ALIAS, " ", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundle(java.lang.String,
     * java.util.Map, java.lang.String, com.topcoder.search.builder.SearchStrategy).
     * In this case, the alias is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testSearchBundle4NullALIAS() {
        try {
            new SearchBundle("name", FIELDS, null, "context", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for search(com.topcoder.search.builder.filter.Filter).
     * In this case, the filter is null.
     * Expected : {@link IllegalArgumentException}
     * @throws Exception to JUnit
     */
    public void testSearchFilter() throws Exception {
        try {
            sb.search(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for search(com.topcoder.search.builder.filter.Filter, java.util.List).
     * In this case, the filter is null.
     * Expected : {@link IllegalArgumentException}
     * @throws Exception to JUnit
     */
    public void testSearchFilterList() throws Exception {
        try {
            sb.search(null, new ArrayList());
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for search(com.topcoder.search.builder.filter.Filter, java.util.List).
     * In this case, the list is null.
     * Expected : {@link IllegalArgumentException}
     * @throws Exception to JUnit
     */
    public void testSearchFilterNullList() throws Exception {
        try {
            sb.search(new EqualToFilter("a", "b"), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for setSearchableFields(java.util.Map).
     * In this case, the map is null.
     * Expected : {@link IllegalArgumentException}
     */
    public void testSetSearchableFieldsNullMap() {
        try {
            sb.setSearchableFields(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for setSearchableFields(java.util.Map).
     * In this case, the map is null.
     * Expected : {@link IllegalArgumentException}
     */
    public void testSetSearchableFieldsEmptyMap() {
        try {
            sb.setSearchableFields(new HashMap());
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for setSearchStrategy(com.topcoder.search.builder.SearchStrategy).
     * In this case, the strategy is null.
     * Expected : {@link IllegalArgumentException}
     */
    public void testSetSearchStrategy() {
        try {
            sb.setSearchStrategy(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

}
