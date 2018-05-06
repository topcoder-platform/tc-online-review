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
public class EqualToFilterTests extends TestCase {
    /**
     * The value of the EqualToFilter for test.
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
    private EqualToFilter equalToFilter = null;

    /**
     * setUp.
     */
    protected void setUp() {
        //the value list in the inFliter
        equalToFilter = new EqualToFilter("The age", VALUE);
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
        assertNotNull("can not construct the instance of EqualToFilter",
            equalToFilter);
    }

    /**
     * test fail for construct EqualToFilter with name is null.
     *
     */
    public void testconstruct2() {
        try {
            equalToFilter = new EqualToFilter(null, VALUE);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct EqualToFilter with value is null.
     *
     */
    public void testconstruct3() {
        try {
            equalToFilter = new EqualToFilter("The age", null);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct EqualToFilter with name is empty.
     *
     */
    public void testconstruct4() {
        try {
            equalToFilter = new EqualToFilter("", VALUE);
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
            equalToFilter.isValid(null, alias);
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
            equalToFilter.isValid(validators, null);
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
        ValidationResult result = equalToFilter.isValid(validators, alias);

        assertTrue("The check should be passed", result.isValid());
    }

    /**
     * the invalid check for the given validators and alias.
     *
     */
    public void testisValid4() {
        try {
            ValidationResult result = equalToFilter.isValid(new HashMap(), alias);
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
        equalToFilter = new EqualToFilter("The age", new Integer(101));

        ValidationResult result = equalToFilter.isValid(validators, alias);

        //should return invalid,for age of 101 if out of the rangle 0 - 100
        assertFalse("The check should not be pass", result.isValid());
    }

    /**
     * test the mothed getFilterType.
     *
     */
    public void testgetFilterType() {
        assertEquals("The getFilterType should return Filter.EQUAL_TO_FILTER",
            Filter.EQUAL_TO_FILTER, equalToFilter.getFilterType());
    }

    /**
     * test the mothed clone.
     *
     */
    public void testclone() {
        EqualToFilter cl = null;

        try {
            cl = (EqualToFilter) equalToFilter.clone();
        } catch (Exception e) {
            fail("no Exception should throw");
        }

        //value should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.value, equalToFilter.value);

        //name should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.getName(), equalToFilter.getName());

        //type should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.getFilterType(), equalToFilter.getFilterType());
    }
}
