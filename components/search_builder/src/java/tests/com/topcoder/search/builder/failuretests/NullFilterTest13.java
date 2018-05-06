/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.failuretests;

import java.util.HashMap;

import com.topcoder.search.builder.filter.NullFilter;

import junit.framework.TestCase;

/**
 * Failure tests for <code>NullFilter</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class NullFilterTest13 extends TestCase {

    /**
     * Represents the filter to test.
     */
    private NullFilter filter;

    /**
     * Set up the environment.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();

        filter = new NullFilter("name");
    }

    /**
     * Test method for NullFilter(java.lang.String).
     * In this case, the name is null.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testNullFilterNullName() {
        try {
            new NullFilter(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for NullFilter(java.lang.String).
     * In this case, the name is empty.
     * Expected exception : {@link IllegalArgumentException}
     */
    public void testNullFilterEmptyName() {
        try {
            new NullFilter(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for isValid(java.util.Map, java.util.Map).
     * In this case, the validator is null.
     * Expected : {@link IllegalArgumentException}
     */
    public void testIsValidNullValidators() {
        try {
            filter.isValid(null, new HashMap());
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for isValid(java.util.Map, java.util.Map).
     * In this case, the alias is null.
     * Expected : {@link IllegalArgumentException}
     */
    public void testIsValidNullAlias() {
        try {
            filter.isValid(new HashMap(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

}
