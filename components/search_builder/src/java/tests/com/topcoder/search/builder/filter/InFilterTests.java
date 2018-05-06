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
 * Unit test cases for OrFilter.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class InFilterTests extends TestCase {
    /**
     * The instance of inFilter for test.
     */
    private InFilter inFilter = null;

    /**
     * the map validators to check valid.
     */
    private Map validators = null;

    /**
     * the map alias to check valid.
     */
    private Map alias = null;

    /**
     * The list of values for test.
     */
    private List values = null;

    /**
     * setUp.
     */
    protected void setUp() {
        //the value list in the inFliter
        values = new ArrayList();
        values.add(new Integer(3));
        values.add(new Integer(10));
        values.add(new Integer(15));
        inFilter = new InFilter("The age", values);
        validators = new HashMap();
        validators.put("age", IntegerValidator.inRange(0, 100));
        alias = new HashMap();
        alias.put("The age", "age");
    }

    /**
     * test the construct of InFilter.
     *
     */
    public void testconstruct1() {
        assertNotNull("can not construct the instance of InFilter", inFilter);
    }

    /**
     * test fail for construct of InFilter with name is null.
     *
     */
    public void testconstruct2() {
        try {
            inFilter = new InFilter(null, new ArrayList());
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct of InFilter with value list is null.
     *
     */
    public void testconstruct3() {
        try {
            inFilter = new InFilter("age", null);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct of InFilter with name is empty.
     *
     */
    public void testconstruct4() {
        try {
            inFilter = new InFilter("", values);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct of InFilter with value list is empty.
     *
     */
    public void testconstruct5() {
        try {
            inFilter = new InFilter("age", new ArrayList());
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
            inFilter.isValid(null, alias);
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
            inFilter.isValid(validators, null);
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
        ValidationResult result = inFilter.isValid(validators, alias);

        assertTrue("The check should be pass", result.isValid());
    }

    /**
     * the invalid check for the given validators and alias.
     *
     */
    public void testisValid4() {
        try {
            ValidationResult result = inFilter.isValid(new HashMap(), alias);
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
        values.add(new Integer(101));
        inFilter = new InFilter("The age", values);

        ValidationResult result = inFilter.isValid(validators, alias);

        //should return invalid,for age of 101 if out of the rangle 0 - 100
        assertFalse("The check should not be pass", result.isValid());
    }

    /**
     * test the mothed getFilterType.
     *
     */
    public void testgetFilterType() {
        assertEquals("The getFilterType should return Filter.IN_FILTER",
            Filter.IN_FILTER, inFilter.getFilterType());
    }

    /**
     * test the mothed clone.
     *
     */
    public void testclone() {
        InFilter cl = null;

        try {
            cl = (InFilter) inFilter.clone();
        } catch (Exception e) {
            fail("no Exception should throw");
        }

        //value list should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.getList().size(), inFilter.getList().size());

        //name should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.getName(), inFilter.getName());

        //type should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.getFilterType(), inFilter.getFilterType());
    }
    /**
     * Test the method getName.
     *
     */
    public void testGetName() {
        assertEquals("The name should be same as set in cosntructor.", "The age", inFilter.getName());
    }
    /**
     * Test the method getList.
     *
     */
    public void testgetList() {
        assertEquals("The size should be 3.", 3, inFilter.getList().size());
    }
}
