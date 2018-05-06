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
 * Unit test cases for BetweenFilter.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class BetweenFilterTests extends TestCase {
    /**
     * The upper of the BetweenFilter for test.
     */
    private static final Comparable UPP = new Integer(99);

    /**
         * The lower of the BetweenFilter for test.
         */
    private static Comparable LOW = new Integer(0);

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
    private BetweenFilter betweenFilter = null;

    /**
     * setUp.
     */
    protected void setUp() {
        //the value list in the inFliter
        betweenFilter = new BetweenFilter("The age", UPP, LOW);
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
        assertNotNull("can not construct the instance of betweenFilter",
            betweenFilter);
    }

    /**
     * test fail for construct with name is null.
     *
     */
    public void testconstruct2() {
        try {
            betweenFilter = new BetweenFilter(null, UPP, LOW);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct with upper is null.
     *
     */
    public void testconstruct3() {
        try {
            betweenFilter = new BetweenFilter("The age", null, LOW);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct with lower is null.
     *
     */
    public void testconstruct4() {
        try {
            betweenFilter = new BetweenFilter("The age", UPP, null);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct with name is empty.
     *
     */
    public void testconstruct5() {
        try {
            betweenFilter = new BetweenFilter("", UPP, LOW);
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
            betweenFilter.isValid(null, alias);
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
            betweenFilter.isValid(validators, null);
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
        ValidationResult result = betweenFilter.isValid(validators, alias);

        assertTrue("The check should be pass", result.isValid());
    }

    /**
     * the invalid check for the given validators and alias.
     *
     */
    public void testisValid4() {
        try {
            ValidationResult result = betweenFilter.isValid(new HashMap(), alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //success
        }

        //      assertFalse("The check should not be pass", result.isValid());
    }

    /**
     * the invalid check for the given validators and alias.
     *
     */
    public void testisValid5() {
        betweenFilter = new BetweenFilter("The age", new Integer(101), LOW);

        ValidationResult result = betweenFilter.isValid(validators, alias);

        //should return invalid,for age of 101 if out of the rangle 0 - 100
        assertFalse("The check should not be pass", result.isValid());
    }

    /**
     * test the mothed getFilterType.
     *
     */
    public void testgetFilterType() {
        assertEquals("The getFilterType should return Filter.BETWEEN_FILTER",
            Filter.BETWEEN_FILTER, betweenFilter.getFilterType());
    }

    /**
     * test the mothed clone.
     *
     */
    public void testclone() {
        BetweenFilter cl = null;

        try {
            cl = (BetweenFilter) betweenFilter.clone();
        } catch (Exception e) {
            fail("no Exception should throw");
        }

        //value upperThreshold should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.upperThreshold, betweenFilter.upperThreshold);

        //value lowerThreshold should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.lowerThreshold, betweenFilter.lowerThreshold);

        //name should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.getName(), betweenFilter.getName());

        //type should be same
        assertEquals("The filter List  size should be the same with the clone",
            cl.getFilterType(), betweenFilter.getFilterType());
    }
}
