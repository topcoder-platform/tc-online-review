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
 * Unit test cases for EqualToFilter.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class GreaterThanFilterTests extends TestCase {
    /**
     * The value of the GreaterThanFilter for test.
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
    private GreaterThanFilter greaterThanFilter = null;

    /**
     * setUp.
     */
    protected void setUp() {
        //the value list in the inFliter
        greaterThanFilter = new GreaterThanFilter("The age", VALUE);
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
        assertNotNull("can not construct the instance of GreaterThanFilter",
            greaterThanFilter);
    }

    /**
     * test fail for construct GreaterThanFilter with name is null.
     *
     */
    public void testconstruct2() {
        try {
            greaterThanFilter = new GreaterThanFilter(null, VALUE);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct GreaterThanFilter with value is null.
     *
     */
    public void testconstruct3() {
        try {
            greaterThanFilter = new GreaterThanFilter("The age", null);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct GreaterThanFilter with name is empty.
     *
     */
    public void testconstruct4() {
        try {
            greaterThanFilter = new GreaterThanFilter("", VALUE);
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
            greaterThanFilter.isValid(null, alias);
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
            greaterThanFilter.isValid(validators, null);
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
        ValidationResult result = greaterThanFilter.isValid(validators, alias);

        assertTrue("The check should be pass", result.isValid());
    }

    /**
     * the invalid check for the given validators and alias.
     * the map should not empty, which is invalid.
     */
    public void testisValid4() {
        try {
            ValidationResult result = greaterThanFilter.isValid(new HashMap(),
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
        greaterThanFilter = new GreaterThanFilter("The age", new Integer(101));

        ValidationResult result = greaterThanFilter.isValid(validators, alias);

        //should return invalid,for age of 101 if out of the rangle 0 - 100
        assertFalse("The check should not be pass", result.isValid());
    }

    /**
     * test the mothed getFilterType.
     *
     */
    public void testgetFilterType() {
        assertEquals("The getFilterType should return Filter.GREATER_THAN_FILTER",
            Filter.GREATER_THAN_FILTER, greaterThanFilter.getFilterType());
    }

    /**
     * test the mothed clone.
     *
     */
    public void testclone() {
        GreaterThanFilter cl = null;

        try {
            cl = (GreaterThanFilter) greaterThanFilter.clone();
        } catch (Exception e) {
            fail("no Exception should throw");
        }

        //value should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.value, greaterThanFilter.value);

        //name should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.getName(), greaterThanFilter.getName());

        //type should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.getFilterType(), greaterThanFilter.getFilterType());
    }
}
