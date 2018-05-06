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
 * Accuracy test cases for EqualToFilter.
 * </p>
 *
 * @author zjq
 * @version 1.1
 */
public class EqualToFilterAccuracyTests extends TestCase {
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
     * the valid check for the given validators and alias.
     *
     */
    public void testisValid3() {
        ValidationResult result = equalToFilter.isValid(validators, alias);

        assertTrue("The check should be pass", result.isValid());
    }

    /**
     * the invalid check for the given validators and alias.
     *
     */
    public void testisValid4() {
    	HashMap map = new HashMap();
        map.put("age", IntegerValidator.greaterThan(101));
        ValidationResult result = equalToFilter.isValid(map, alias);

        assertFalse("The check should not be pass", result.isValid());
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
        assertEquals("The getFilterType should return Filter.EQUAL_TO_FILTER", Filter.EQUAL_TO_FILTER,
            equalToFilter.getFilterType());
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
        assertEquals("The filter List  size should be the same with the clone", cl.getValue(),
            equalToFilter.getValue());

        //name should be same
        assertEquals("The filter List  size should be the same with the clone", cl.getName(),
            equalToFilter.getName());

        //type should be same
        assertEquals("The filter List  size should be the same with the clone", cl.getFilterType(),
            equalToFilter.getFilterType());
    }
}
