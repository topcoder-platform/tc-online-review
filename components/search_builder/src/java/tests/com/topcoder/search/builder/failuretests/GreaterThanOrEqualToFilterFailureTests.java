/**
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.search.builder.failuretests;

import junit.framework.TestCase;
import java.util.HashMap;
import java.util.Map;

import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;

/**
 * Failure test cases for GreaterThanOrEqualToFilter.
 *
 * @author WishingBone
 * @version 1.1
 */
public class GreaterThanOrEqualToFilterFailureTests extends TestCase {

    /**
     * Create a filter for testing.
     *
     * @return a filter for testing.
     */
    private Filter createFilter() {
        return new GreaterThanOrEqualToFilter("quantity", new Integer(12));
    }

    /**
     * Create with null name.
     */
    public void testCreate_NullName() {
        try {
            new GreaterThanOrEqualToFilter(null, new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * Create with null value.
     */
    public void testCreate_NullValue() {
        try {
            new GreaterThanOrEqualToFilter("name", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * Create with empty name.
     */
    public void testCreate_EmptyName() {
        try {
            new GreaterThanOrEqualToFilter("", new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
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
