/**
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.search.builder.failuretests;

import com.topcoder.search.builder.filter.*;
import junit.framework.TestCase;
import java.util.HashMap;
import java.util.Map;

/**
 * Failure test cases for BetweenFilter.
 *
 * @author WishingBone
 * @version 1.1
 */
public class BetweenFilterFailureTests extends TestCase {

    /**
     * Create a filter for testing.
     *
     * @return a filter for testing.
     */
    private Filter createFilter() {
        return new BetweenFilter("quantity", new Integer(15), new Integer(12));
    }

    /**
     * Create with null name.
     */
    public void testCreate_NullName() {
        try {
            new BetweenFilter(null, new Integer(15), new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * Create with null upper.
     */
    public void testCreate_NullUpper() {
        try {
            new BetweenFilter("name", null, new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * Create with null lower.
     */
    public void testCreate_NullLower() {
        try {
            new BetweenFilter("name", new Integer(15), null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * Create with empty name.
     */
    public void testCreate_EmptyName() {
        try {
            new BetweenFilter("", new Integer(15), new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * Create with invalid range.
     */
    public void testCreate_InvalidRange() {
        try {
            new BetweenFilter("", new Integer(12), new Integer(15));
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
