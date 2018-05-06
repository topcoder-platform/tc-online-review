/**
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.search.builder.failuretests;

import com.topcoder.search.builder.filter.*;
import junit.framework.TestCase;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Failure test cases for AndFilter.
 *
 * @author WishingBone
 * @version 1.1
 */
public class AndFilterFailureTests extends TestCase {

    /**
     * Create a filter for testing.
     *
     * @return a filter for testing.
     */
    private AbstractAssociativeFilter createFilter() {
        return new AndFilter(new EqualToFilter("quantity", new Integer(12)), new EqualToFilter("size", new Integer(3)));
    }

    /**
     * Create with null filter1.
     */
    public void testCreate_NullFilter1() {
        try {
            new AndFilter(null, new EqualToFilter("quantity", new Integer(12)));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * Create with null filter2.
     */
    public void testCreate_NullFilter2() {
        try {
            new AndFilter(new EqualToFilter("quantity", new Integer(12)), null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * Create with null filters.
     */
    public void testCreate_NullFilters() {
        try {
            new AndFilter(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * Create with null filter.
     */
    public void testCreate_NullFilter() {
        List filters = new ArrayList();
        filters.add(null);
        try {
            new AndFilter(filters);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * Create with invalid filter.
     */
    public void testCreate_InvalidFilter() {
        List filters = new ArrayList();
        filters.add(new Object());
        try {
            new AndFilter(filters);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * addFilter() with null filter.
     */
    public void testAddFilter_NullFilter() {
        try {
            createFilter().addFilter(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * isValid() with null validators.
     */
    public void testIsValid_NullValidators() {
        try {
            createFilter().isValid(null, new HashMap());
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * isValid() with null alias.
     */
    public void testIsValid_NullAlias() {
        try {
            createFilter().isValid(new HashMap(), null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * isValid() with invalid validator.
     */
    public void testIsValid_InvalidValidator() {
        Map validators = new HashMap();
        validators.put("quantity", new Object());
        try {
            createFilter().isValid(validators, new HashMap());
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * isValid() with invalid alias.
     */
    public void testIsValid_InvalidAlias() {
        Map alias = new HashMap();
        alias.put("quantity", new Object());
        try {
            createFilter().isValid(new HashMap(), alias);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

}
