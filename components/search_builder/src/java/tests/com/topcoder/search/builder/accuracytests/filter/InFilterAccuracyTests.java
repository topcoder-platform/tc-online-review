/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests.filter;

import com.topcoder.search.builder.ValidationResult;
import com.topcoder.search.builder.filter.*;

import com.topcoder.util.datavalidator.IntegerValidator;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Accuracy test cases for OrFilter.
 * </p>
 *
 * @author zjq
 * @version 1.1
 */
public class InFilterAccuracyTests extends TestCase {
    /** A name used in the tests. */
    private static final String NAME = "name";

    /** A value used in the tests. */
    private static final Comparable VALUE1 = "value1";

    /** A List of values. */
    private static final List VALUES = new ArrayList();

    static {
        VALUES.add(VALUE1);
    }

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
    List values = null;

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
     * Verify behavior of the getName method.
     */
    public void testGetName() {
        InFilter filter = new InFilter(NAME, VALUES);
        assertEquals("name is incorrect", NAME, filter.getName());
    }

    /**
     * Verify behavior of the getList method.
     */
    public void testGetList() {
        InFilter filter = new InFilter(NAME, VALUES);
        assertEquals("list is incorrect", VALUES, filter.getList());
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
    	HashMap map = new HashMap();
        map.put("age", IntegerValidator.greaterThan(101));
        ValidationResult result = inFilter.isValid(map, alias);

        assertFalse("The check should not be pass", result.isValid());
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
        assertEquals("The getFilterType should return Filter.IN_FILTER", Filter.IN_FILTER,
            inFilter.getFilterType());
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
        assertEquals("The filter List  size should be the same with the clone", cl.getList().size(),
            inFilter.getList().size());

        //name should be same
        assertEquals("The filter List  size should be the same with the clone", cl.getName(),
            inFilter.getName());

        //type should be same
        assertEquals("The filter List  size should be the same with the clone", cl.getFilterType(),
            inFilter.getFilterType());
    }
}
