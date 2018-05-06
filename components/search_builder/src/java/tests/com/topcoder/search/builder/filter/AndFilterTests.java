/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.filter;

import com.topcoder.search.builder.ValidationResult;

import com.topcoder.util.datavalidator.IntegerValidator;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Unit test cases for AndFilter.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class AndFilterTests extends TestCase {
    /**
     * the filter to construct the filter list in AndFilter.
     */
    private Filter f1 = null;

    /**
     * the filter to construct the filter list in AndFilter.
     */
    private Filter f2 = null;

    /**
     * the AndFilter instance to test.
     */
    private AndFilter andFilter = null;

    /**
     * the map validators to check valid.
     */
    private Map validators = null;

    /**
     * the map alias to check valid.
     */
    private Map alias = null;

    /**
     * setUp.
     */
    public void setUp() {
        /**
         * get the filters.
         */
        f1 = new GreaterThanFilter("The age", new Integer(1));
        f2 = new EqualToFilter("The age", new Integer(10));

        List list = new ArrayList();
        list.add(f1);
        list.add(f2);
        andFilter = new AndFilter(list);
        validators = new HashMap();
        validators.put("age", IntegerValidator.inRange(0, 100));
        alias = new HashMap();
        alias.put("The age", "age");
    }

    /**
     * test the construct of AndFilter.
     *
     */
    public void testconstruct1() {
        assertNotNull("can not construct the instance of AndFilter", andFilter);
    }

    /**
     * test the construct of AndFilter.
     *
     */
    public void testconstruct2() {
        //another constructor
        andFilter = new AndFilter(f1, f2);
        assertNotNull("can not construct the instance of AndFilter", andFilter);
    }

    /**
     * test the construct with exception.
     *
     */
    public void testconstruct3() {
        try {
            f1 = new GreaterThanFilter("The age", new Integer(1));
            f2 = new EqualToFilter("The age", new Integer(10));

            List list = new ArrayList();
            list.add(f1);
            list.add(f2);
            andFilter = new AndFilter(list);
        } catch (Exception e) {
            fail("no Exception should be throw");
        }
    }

    /**
     * test the construct with exception.
     *
     */
    public void testconstruct4() {
        try {
            andFilter = new AndFilter(f1, f2);
        } catch (Exception e) {
            fail("no Exception should be throw");
        }
    }

    /**
     * test fail for check valid with validators null.
     *
     */
    public void testisValid1() {
        try {
            andFilter.isValid(null, alias);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
         * test fail for check valid with alias null.
         *
         */
    public void testisValid2() {
        try {
            andFilter.isValid(validators, null);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * the valid check for the given validators and alias.
     *
     */
    public void testisValid3() {
        ValidationResult result = andFilter.isValid(validators, alias);

        assertTrue("The check should be pass", result.isValid());
    }

    /**
     * the invalid check for the given validators and alias.
     *
     */
    public void testisValid4() {
        try {
            ValidationResult result = andFilter.isValid(new HashMap(), alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //success
        }

        // assertFalse("The check should not be pass", result.isValid());
    }

    /**
     * the invalid check for the given validators and alias.
     *
     */
    public void testisValid5() {
        f1 = new GreaterThanFilter("The age", new Integer(1));
        f2 = new EqualToFilter("The age", new Integer(10));

        Filter f3 = new EqualToFilter("The age", new Integer(101));
        List list = new ArrayList();
        list.add(f1);
        list.add(f2);
        list.add(f3);
        andFilter = new AndFilter(list);

        ValidationResult result = andFilter.isValid(validators, alias);

        //should return invalid,for age of 101 if out of the rangle 0 - 100
        assertFalse("The check should not be pass", result.isValid());
    }

    /**
     * test the mothed getFilterType.
     *
     */
    public void testgetFilterType() {
        assertEquals("The getFilterType should return Filter.AND_FILTER",
            Filter.AND_FILTER, andFilter.getFilterType());
    }

    /**
     * test the mothed clone.
     *
     */
    public void testclone() {
        AndFilter cl = null;

        try {
            cl = (AndFilter) andFilter.clone();
        } catch (Exception e) {
            fail("no Exception should throw");
        }

        assertEquals("The filter List  size should be the same with the clone",
            cl.filters.size(), andFilter.filters.size());
    }
}
