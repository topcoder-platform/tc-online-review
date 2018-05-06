/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests.filter;

import com.topcoder.search.builder.ValidationResult;
import com.topcoder.search.builder.filter.*;

import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.datavalidator.StringValidator;

import junit.framework.TestCase;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Accuracy test cases for AndFilter.
 * </p>
 *
 * @author zjq, isv
 * @version 1.3
 */
public class AndFilterAccuracyTests extends TestCase {
    /** A filter used in some tests. */
    private static final Filter FILTER1 = new EqualToFilter("name", "value1");

    /** A second filter used in some tests. */
    private static final Filter FILTER2 = new EqualToFilter("name", "value2");

    /**
     * A third filter used in some tests.
     *
     * @since 1.3
     */
    private static final Filter FILTER3 = new NullFilter("NullFilter");

    /** An alias map used in some tests. */
    private static final Map ALIAS = new HashMap();

    /** A Validator used in some tests. */
    private static final ObjectValidator VALIDATOR = StringValidator.startsWith("value1");

    /** A validator map used in some tests. */
    private static final Map VALIDATORS = new HashMap();

    static {
        VALIDATORS.put("name", VALIDATOR);
    }

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

    /** A AndFilter to run the tests on. */
    private AndFilter filter;

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

        List filters = new ArrayList();
        filters.add(FILTER1);

        filter = new AndFilter(filters);
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
        HashMap map = new HashMap();
        map.put("age", IntegerValidator.greaterThan(101));
        ValidationResult result = andFilter.isValid(map, alias);

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
        andFilter = new AndFilter(list);

        ValidationResult result = andFilter.isValid(validators, alias);

        //should return invalid,for age of 101 if out of the rangle 0 - 100
        assertFalse("The check should not be pass", result.isValid());
    }

    /**
     * test the mothed getFilterType.
     *
     */
    public void testGetFilterType() {
        assertEquals("The getFilterType should return Filter.AND_FILTER", Filter.AND_FILTER,
            andFilter.getFilterType());
    }

    /**
     * test the mothed clone.
     *
     */
    public void testClone() {
        AndFilter cl = null;

        try {
            cl = (AndFilter) andFilter.clone();
        } catch (Exception e) {
            fail("no Exception should throw");
        }

        assertEquals("The filter List  size should be the same with the clone", cl.getFilters().size(),
            andFilter.getFilters().size());
    }

    /**
     * Verify behavior of the isValid method. Make sure validation is correct.
     */
    public void testIsValid() {
        ValidationResult result = filter.isValid(VALIDATORS, ALIAS);
        assertTrue("Validation should have succeeded", result.isValid());

        filter.addFilter(FILTER2);
        result = filter.isValid(VALIDATORS, ALIAS);
        assertFalse("Validation should have failed", result.isValid());
        assertEquals("Incorrect filter field", "value2", ((EqualToFilter) result.getFailedFilter()).getValue());
    }

    /**
     * <p>Accuracy test. Tests the {@link AndFilter#clone()} method for proper behavior.</p>
     *
     * <p>Verifies that the method performs a deep copy of the filter.</p>
     *
     * @since 1.3
     */
    public void testClone_DeepCopy() {
        this.filter.addFilter(FILTER2);
        this.filter.addFilter(FILTER3);

        AndFilter clone = (AndFilter) this.filter.clone();
        List originalFilters = this.filter.getFilters();
        List clonedFilters = clone.getFilters();

        Assert.assertNotSame("The filters list is not cloned correctly", originalFilters, clonedFilters);
        Assert.assertEquals("The filters list is not cloned correctly", originalFilters.size(), clonedFilters.size());

        for (int i = 0; i < originalFilters.size(); i++) {
            Filter original = (Filter) originalFilters.get(i);
            Filter cloned = (Filter) clonedFilters.get(i);
            Assert.assertEquals("The filter is not cloned correctly", original.getClass(), cloned.getClass());
            Assert.assertNotSame("The filter is not cloned correctly", original, cloned);
        }
    }

}
