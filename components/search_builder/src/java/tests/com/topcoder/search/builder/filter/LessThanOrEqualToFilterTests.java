/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.filter;

import com.topcoder.search.builder.ValidationResult;

import com.topcoder.util.datavalidator.IntegerValidator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * Unit test cases for LessThanOrEqualToFilter.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class LessThanOrEqualToFilterTests extends TestCase {
    /**
     * The value of the LessThanOrEqualToFilter for test.
     */
    private static final Comparable VALUE = new Integer(10);

    /**
         * the map validators to check valid.
         */
    private Map validators = null;

    /**
     * the map alias to check valid.
     */
    private Map alias = null;

    /**
     * The instance of BetweenFilter for test.
     */
    private LessThanOrEqualToFilter lessThanOrEqualToFilter = null;

    /**
     * setUp.
     */
    protected void setUp() {
        //the value list in the inFliter
        lessThanOrEqualToFilter = new LessThanOrEqualToFilter("The age", VALUE);
        validators = new HashMap();
        validators.put("age", IntegerValidator.inRange(0, 100));
        alias = new HashMap();
        alias.put("The age", "age");
    }

    /**
     * test the construct of BetweenFilter.
     *
     */
    public void testconstruct1() {
        assertNotNull("can not construct the instance of LessThanOrEqualToFilter",
            lessThanOrEqualToFilter);
    }

    /**
     * test fail for construct LessThanOrEqualToFilter with name is null.
     *
     */
    public void testconstruct2() {
        try {
            lessThanOrEqualToFilter = new LessThanOrEqualToFilter(null, VALUE);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct LessThanOrEqualToFilter with value is null.
     *
     */
    public void testconstruct3() {
        try {
            lessThanOrEqualToFilter = new LessThanOrEqualToFilter("The age",
                    null);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct LessThanOrEqualToFilter with name is empty.
     *
     */
    public void testconstruct4() {
        try {
            lessThanOrEqualToFilter = new LessThanOrEqualToFilter("", VALUE);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for check valid with validators null.
     *
     */
    public void testisValid1() {
        try {
            lessThanOrEqualToFilter.isValid(null, alias);
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
            lessThanOrEqualToFilter.isValid(validators, null);
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
        ValidationResult result = lessThanOrEqualToFilter.isValid(validators,
                alias);

        assertTrue("The check should be pass", result.isValid());
    }

    /**
     * the invalid check for the given validators and alias.
     *
     */
    public void testisValid4() {
        try {
            ValidationResult result = lessThanOrEqualToFilter.isValid(new HashMap(),
                    alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //success
        }

        //assertFalse("The check should not be pass", result.isValid());
    }

    /**
     * the invalid check for the given validators and alias.
     *
     */
    public void testisValid5() {
        lessThanOrEqualToFilter = new LessThanOrEqualToFilter("The age",
                new Integer(101));

        ValidationResult result = lessThanOrEqualToFilter.isValid(validators,
                alias);

        //should return invalid,for age of 101 if out of the rangle 0 - 100
        assertFalse("The check should not be pass", result.isValid());
    }

    /**
     * test the mothed getFilterType.
     *
     */
    public void testgetFilterType() {
        assertEquals("The getFilterType should return Filter.LESS_THAN_OR_EQUAL_TO_FILTER",
            Filter.LESS_THAN_OR_EQUAL_TO_FILTER,
            lessThanOrEqualToFilter.getFilterType());
    }

    /**
     * test the mothed clone.
     *
     */
    public void testclone() {
        LessThanOrEqualToFilter cl = null;

        try {
            cl = (LessThanOrEqualToFilter) lessThanOrEqualToFilter.clone();
        } catch (Exception e) {
            fail("no Exception should throw");
        }

        //value should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.value, lessThanOrEqualToFilter.value);

        //name should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.getName(), lessThanOrEqualToFilter.getName());

        //type should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.getFilterType(), lessThanOrEqualToFilter.getFilterType());
    }
}
