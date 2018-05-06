/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests.filter;

import com.topcoder.search.builder.ValidationResult;
import com.topcoder.search.builder.filter.*;

import com.topcoder.util.datavalidator.IntegerValidator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * Accuracy test cases for BetweenFilter.
 * </p>
 *
 * @author zjq
 * @version 1.1
 */
public class BetweenFilterAccuracyTests extends TestCase {
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
     * Verify behavior of the isLower and isUpperInclusive methods.
     */
    public void testIsLowerUpperInclusive() {
        assertTrue("limits should be inclusive", betweenFilter.isLowerInclusive());
        assertTrue("limits should be inclusive", betweenFilter.isUpperInclusive());
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
    	HashMap map = new HashMap();
        map.put("age", IntegerValidator.greaterThan(101));
        ValidationResult result = betweenFilter.isValid(map, alias);

        assertFalse("The check should not be pass", result.isValid());
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
        assertEquals("The getFilterType should return Filter.BETWEEN_FILTER", Filter.BETWEEN_FILTER,
            betweenFilter.getFilterType());
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
        assertEquals("The filter List  size should be the same with the clone", cl.getUpperThreshold(),
            betweenFilter.getUpperThreshold());

        //value lowerThreshold should be same
        assertEquals("The filter List  size should be the same with the clone", cl.getLowerThreshold(),
            betweenFilter.getLowerThreshold());

        //name should be same
        assertEquals("The filter List  size should be the same with the clone", cl.getName(),
            betweenFilter.getName());

        //type should be same
        assertEquals("The filter List  size should be the same with the clone", cl.getFilterType(),
            betweenFilter.getFilterType());
    }
}
