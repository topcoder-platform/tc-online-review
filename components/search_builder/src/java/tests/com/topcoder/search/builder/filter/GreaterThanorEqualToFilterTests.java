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
 * Unit test cases for GreaterThanorEqualToFilter.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class GreaterThanorEqualToFilterTests extends TestCase {
    /**
     * The value of the GreaterThanorEqualToFilter for test.
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
    private GreaterThanOrEqualToFilter greaterThanOrEqualToFilter = null;

    /**
     * setUp.
     */
    protected void setUp() {
        //the value list in the inFliter
        greaterThanOrEqualToFilter = new GreaterThanOrEqualToFilter("The age",
                VALUE);
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
        assertNotNull("can not construct the instance of GreaterThanorEqualToFilter",
               greaterThanOrEqualToFilter);
    }

    /**
     * test fail for construct GreaterThanorEqualToFilter with name is null.
     *
     */
    public void testconstruct2() {
        try {
            greaterThanOrEqualToFilter = new GreaterThanOrEqualToFilter(null,
                    VALUE);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct GreaterThanorEqualToFilter with value is null.
     *
     */
    public void testconstruct3() {
        try {
            greaterThanOrEqualToFilter = new GreaterThanOrEqualToFilter("The age",
                    null);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct GreaterThanorEqualToFilter with name is empty.
     *
     */
    public void testconstruct4() {
        try {
            greaterThanOrEqualToFilter = new GreaterThanOrEqualToFilter("",
                    VALUE);
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
            greaterThanOrEqualToFilter.isValid(null, alias);
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
            greaterThanOrEqualToFilter.isValid(validators, null);
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
        ValidationResult result = greaterThanOrEqualToFilter.isValid(validators,
                alias);

        assertTrue("The check should be pass", result.isValid());
    }

    /**
     * the invalid check for the given validators and alias.
     *
     */
    public void testisValid4() {
        try {
            ValidationResult result = greaterThanOrEqualToFilter.isValid(new HashMap(),
                    alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //success
        }

        //  assertFalse("The check should not be pass", result.isValid());
    }

    /**
     * the invalid check for the given validators and alias.
     *
     */
    public void testisValid5() {
        greaterThanOrEqualToFilter = new GreaterThanOrEqualToFilter("The age",
                new Integer(101));

        ValidationResult result = greaterThanOrEqualToFilter.isValid(validators,
                alias);

        //should return invalid,for age of 101 if out of the rangle 0 - 100
        assertFalse("The check should not be pass", result.isValid());
    }

    /**
     * test the mothed getFilterType.
     *
     */
    public void testgetFilterType() {
        assertEquals("The getFilterType should return Filter.GREATER_THAN_OR_EQUAL_TO_FILTER",
            Filter.GREATER_THAN_OR_EQUAL_TO_FILTER,
            greaterThanOrEqualToFilter.getFilterType());
    }

    /**
     * test the mothed clone.
     *
     */
    public void testclone() {
        GreaterThanOrEqualToFilter cl = null;

        try {
            cl = (GreaterThanOrEqualToFilter) greaterThanOrEqualToFilter.clone();
        } catch (Exception e) {
            fail("no Exception should throw");
        }

        //value should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.value, greaterThanOrEqualToFilter.value);

        //name should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.getName(), greaterThanOrEqualToFilter.getName());

        //type should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.getFilterType(), greaterThanOrEqualToFilter.getFilterType());
    }
}
