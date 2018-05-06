/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.failuretests;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.search.builder.SearchContext;
import com.topcoder.util.classassociations.ClassAssociator;

import junit.framework.TestCase;

/**
 * Failure tests for <code>SearchContext</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class SearchContextTest13 extends TestCase {

    /**
     * Represents the map parameter.
     */
    private final Map map = new HashMap();

    /**
     * Represents the class associator.
     */
    private final ClassAssociator asso = new ClassAssociator();

    /**
     * Represents the context to test.
     */
    private SearchContext sc;

    /**
     * Set up the environment.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();

        sc = new SearchContext(map);
    }

    /**
     * Test method for SearchContext(java.util.Map).
     * In this case, the map is null.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testSearchContextNullMap() {
        try {
            new SearchContext(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchContext(java.util.Map).
     * In this case, the key in the map is not string.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testSearchContextNonStringKey() {
        try {
            map.put(new Object(), new Object());
            new SearchContext(map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchContext(java.util.Map).
     * In this case, the value in the map is not string.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testSearchContextNonStringValue() {
        try {
            map.put("key", new Object());
            new SearchContext(map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchContext(java.util.Map).
     * In this case, the value in the map is not string.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testSearchContextNullValue() {
        try {
            map.put("key", null);
            new SearchContext(map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchContext(java.util.Map).
     * In this case, the key in the map is empty string.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testSearchContextEmptyKey() {
        try {
            map.put(" ", "value");
            new SearchContext(map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchContext(java.util.Map).
     * In this case, the value in the map is empty string.
     * Expected exception : {@link IllegalArgumentException}.
     */
    public void testSearchContextEmptyValue() {
        try {
            map.put("key", " ");
            new SearchContext(map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchContext(com.topcoder.util.classassociations.ClassAssociator, java.util.Map).
     * In this case, the alias has null value.
     * Expected exception : IllegalArgumentException
     */
    public void testSearchContextNullValueInAlias() {
        try {
            map.put("key", null);
            new SearchContext(asso, map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchContext(com.topcoder.util.classassociations.ClassAssociator, java.util.Map).
     * In this case, the alias has non string key.
     * Expected exception : IllegalArgumentException
     */
    public void testSearchContextNonStringKeyInAlias() {
        try {
            map.put(new Object(), "value");
            new SearchContext(asso, map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchContext(com.topcoder.util.classassociations.ClassAssociator, java.util.Map).
     * In this case, the alias has non string value.
     * Expected exception : IllegalArgumentException
     */
    public void testSearchContextNonStringValueInAlias() {
        try {
            map.put("key", new Object());
            new SearchContext(asso, map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchContext(com.topcoder.util.classassociations.ClassAssociator, java.util.Map).
     * In this case, the alias has empty string value.
     * Expected exception : IllegalArgumentException
     */
    public void testSearchContextEmptyStringValueInAlias() {
        try {
            map.put("key", " ");
            new SearchContext(asso, map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchContext(com.topcoder.util.classassociations.ClassAssociator, java.util.Map).
     * In this case, the alias has empty string key.
     * Expected exception : IllegalArgumentException
     */
    public void testSearchContextEmptyStringKeyInAlias() {
        try {
            map.put(" ", "value");
            new SearchContext(asso, map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for getFragmentBuilder(com.topcoder.search.builder.filter.Filter).
     * In this case, the filter is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testGetFragmentBuilder() {
        try {
            sc.getFragmentBuilder(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for getFieldName(java.lang.String).
     * In this case, the parameter is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testGetFieldName() {
        try {
            sc.getFieldName(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for getFieldName(java.lang.String).
     * In this case, the parameter is empty.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testGetFieldNameEmptyName() {
        try {
            sc.getFieldName(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

}
