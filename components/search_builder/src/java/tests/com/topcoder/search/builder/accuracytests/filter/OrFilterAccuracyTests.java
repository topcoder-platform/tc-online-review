/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests.filter;

import com.topcoder.search.builder.ValidationResult;
import com.topcoder.search.builder.filter.*;

import com.topcoder.util.datavalidator.IntegerValidator;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Accuracy test cases for OrFilter.
 * </p>
 *
 * @author zjq
 * @version 1.1
 */
public class OrFilterAccuracyTests extends TestCase {
    /**
     * the filter to construct the filter list in OrFilter.
     */
    private Filter f1 = null;

    /**
     * the filter to construct the filter list in OrFilter.
     */
    private Filter f2 = null;

    /**
     * the OrFilter instance to test.
     */
    private OrFilter orFilter = null;

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
        orFilter = new OrFilter(list);
        validators = new HashMap();
        validators.put("age", IntegerValidator.inRange(0, 100));
        alias = new HashMap();
        alias.put("The age", "age");
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
            orFilter = new OrFilter(list);
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
            orFilter = new OrFilter(f1, f2);
        } catch (Exception e) {
            fail("no Exception should be throw");
        }
    }

    /**
     * the valid check for the given validators and alias.
     *
     */
    public void testisValid3() {
        ValidationResult result = orFilter.isValid(validators, alias);

        assertTrue("The check should be pass", result.isValid());
    }

    /**
     * the invalid check for the given validators and alias.
     *
     */
    public void testisValid4() {
    	HashMap map = new HashMap();
        map.put("age", IntegerValidator.greaterThan(101));
        ValidationResult result = orFilter.isValid(map, alias);

        assertFalse("The check should not be pass", result.isValid());
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
        orFilter = new OrFilter(list);

        ValidationResult result = orFilter.isValid(validators, alias);

        //should return invalid,for age of 101 if out of the rangle 0 - 100
        assertFalse("The check should not be pass", result.isValid());
    }

    /**
     * test the mothed getFilterType.
     *
     */
    public void testgetFilterType() {
        assertEquals("The getFilterType should return Filter.AND_FILTER", Filter.OR_FILTER,
            orFilter.getFilterType());
    }

    /**
     * test the mothed clone.
     *
     */
    public void testclone() {
        OrFilter cl = null;

        try {
            cl = (OrFilter) orFilter.clone();
        } catch (Exception e) {
            fail("no Exception should throw");
        }

        assertEquals("The filter List  size should be the same with the clone", cl.getFilters().size(),
            orFilter.getFilters().size());
    }
}
