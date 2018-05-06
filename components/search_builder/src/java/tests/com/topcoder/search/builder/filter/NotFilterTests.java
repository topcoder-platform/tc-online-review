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
 * Unit test cases for notFilter.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class NotFilterTests extends TestCase {
    /**
     * the filter to construct the filter list in notFilter.
     */
    private Filter f1 = null;

    /**
     * the notFilter instance to test.
     */
    private NotFilter notFilter = null;

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
        validators = new HashMap();
        validators.put("age", IntegerValidator.inRange(0, 100));
        alias = new HashMap();
        alias.put("The age", "age");
        notFilter = new NotFilter(f1);
    }

    /**
     * test the construct of NotFilter.
     *
     */
    public void testconstruct1() {
        assertNotNull("can not construct the instance of notFilter", notFilter);
    }

    /**
     * test the construct with exception.
     *
     */
    public void testconstruct2() {
        try {
            f1 = new GreaterThanFilter("The age", new Integer(1));
            notFilter = new NotFilter(f1);
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
            notFilter.isValid(null, alias);
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
            notFilter.isValid(validators, null);
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
        ValidationResult result = notFilter.isValid(validators, alias);

        assertTrue("The check should be pass", result.isValid());
    }

    /**
     * the invalid check for the given validators and alias.
     * The mao should not be empty, which is invalid.
     *
     */
    public void testisValid4() {
        try {
            ValidationResult result = notFilter.isValid(new HashMap(), alias);
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
        Filter f3 = new EqualToFilter("The age", new Integer(101));
        notFilter = new NotFilter(f3);

        ValidationResult result = notFilter.isValid(validators, alias);

        //should return invalid,for age of 101 if out of the rangle 0 - 100
        assertFalse("The check should not be pass", result.isValid());
    }

    /**
     * test the mothed getFilterType.
     *
     */
    public void testgetFilterType() {
        assertEquals("The getFilterType should return Filter.NOT_FILTER",
            Filter.NOT_FILTER, notFilter.getFilterType());
    }

    /**
     * test the mothed clone.
     *
     */
    public void testclone() {
        NotFilter cl = null;

        try {
            cl = (NotFilter) notFilter.clone();
        } catch (Exception e) {
            fail("no Exception should throw");
        }

        assertEquals("The filter List  size should be the same with the clone",
            cl.getFilter().getFilterType(),
            notFilter.getFilter().getFilterType());
    }
}
