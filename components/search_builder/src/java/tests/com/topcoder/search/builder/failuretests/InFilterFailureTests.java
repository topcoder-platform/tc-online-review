/**
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.search.builder.failuretests;

import com.topcoder.search.builder.*;
import com.topcoder.search.builder.filter.*;
import com.topcoder.search.builder.database.*;
import com.topcoder.search.builder.ldap.*;
import junit.framework.TestCase;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Failure test cases for InFilter.
 *
 * @author WishingBone
 * @version 1.1
 */
public class InFilterFailureTests extends TestCase {

    /**
     * Create a filter for testing.
     *
     * @return a filter for testing.
     */
    private Filter createFilter() {
        List values = new ArrayList();
        values.add(new Integer(12));
        values.add(new Integer(15));
        return new InFilter("quantity", values);
    }

    /**
     * Create with null name.
     */
    public void testCreate_NullName() {
        List values = new ArrayList();
        values.add(new Integer(12));
        values.add(new Integer(15));
        try {
            new InFilter(null, values);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * Create with null values.
     */
    public void testCreate_NullValues() {
        try {
            new InFilter("quantity", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * Create with empty name.
     */
    public void testCreate_EmptyName() {
        List values = new ArrayList();
        values.add(new Integer(12));
        values.add(new Integer(15));
        try {
            new InFilter("", values);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * Create with empty values.
     */
    public void testCreate_EmptyValues() {
        try {
            new InFilter("quantity", new ArrayList());
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
