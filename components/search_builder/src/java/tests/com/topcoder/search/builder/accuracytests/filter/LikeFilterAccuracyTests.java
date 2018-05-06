/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests.filter;

import com.topcoder.search.builder.ValidationResult;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.LikeFilter;

import com.topcoder.util.datavalidator.StringValidator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * This accuracy tests addresses the functionality provided by the <code>LikeFilter</code> class.
 * It tests the LikeFilter(String, String) and LikeFilter(String, String, char) constructors,
 * isValid(Map, Map), getFilterType(), getEscapeCharacter(), getName(), getValue() and clone()
 * methods.
 * </p>
 *
 * @author icyriver
 * @version 1.2
 */
public class LikeFilterAccuracyTests extends TestCase {
    /**
     * <p>
     * The default escape character('\') of the <code>LikeFilter</code>.
     * </p>
     */
    private static final char DEFAULTESCAPE = '\\';

    /**
     * <p>
     * The name of the <code>LikeFilter</code> instance for test.
     * </p>
     */
    private String name = "SS";

    /**
     * <p>
     * The value of the <code>LikeFilter</code> instance for test.
     * </p>
     */
    private String value = "SS:abc";

    /**
     * <p>
     * The eacape character of the <code>LikeFilter</code> instance for test.
     * </p>
     */
    private char eacapeChar = '#';

    /**
     * <p>
     * The validators map for test. Its key is the name and value is the corresponding validator.
     * </p>
     */
    private Map validatorsMap = null;

    /**
     * <p>
     * The alias map for test. Its key is the real name and the value is the corresponding alias
     * name.
     * </p>
     */
    private Map aliasMap = null;

    /**
     * <p>
     * The instance of <code>LikeFilter</code> for test.
     * </p>
     */
    private LikeFilter filter = null;

    /**
     * <p>
     * Set up the accuracy testing envionment.
     * </p>
     */
    protected void setUp() {
        // set validators map here.
        validatorsMap = new HashMap();
        validatorsMap.put("SSFilter", StringValidator.startsWith("SS:"));
        validatorsMap.put("SWFilter", StringValidator.startsWith("SW:"));
        validatorsMap.put("EWFilter", StringValidator.startsWith("EW:"));
        validatorsMap.put("WCFilter", StringValidator.startsWith("WC:"));

        // set alias map here.
        aliasMap = new HashMap();
        aliasMap.put("SS", "SSFilter");
        aliasMap.put("SW", "SWFilter");
        aliasMap.put("EW", "EWFilter");
        aliasMap.put("WC", "WCFilter");
    }

    /**
     * <p>
     * Basic test of <code>LikeFilter(String, String)</code> constructor.
     * </p>
     */
    public void testLikeFilterCtor1_Basic() {
        // check null here.
        assertNotNull("Create LikeFilter failed.", new LikeFilter(name, value));
    }

    /**
     * <p>
     * Detail test of <code>LikeFilter(String, String)</code> constructor.
     * </p>
     */
    public void testLikeFilterCtor1_Detail() {
        filter = new LikeFilter(name, value);

        // check null here.
        assertNotNull("Create LikeFilter failed.", filter);

        // check the filter type here.
        assertTrue("LikeFilter should extend Filter.", filter instanceof Filter);

        // check its attributes here.
        assertEquals("The name of LikeFilter is incorrect.", name, filter.getName());
        assertEquals("The value of LikeFilter is incorrect.", value, filter.getValue());
        assertEquals("The escape char of LikeFilter is incorrect.", DEFAULTESCAPE,
            filter.getEscapeCharacter());
        assertEquals("The filter type of LikeFilter is incorrect.", Filter.LIKE_FILTER,
            filter.getFilterType());
    }

    /**
     * <p>
     * Basic test of <code>LikeFilter(String, String, char)</code> constructor.
     * </p>
     */
    public void testLikeFilterCtor2_Basic() {
        // check null here.
        assertNotNull("Create LikeFilter failed.", new LikeFilter(name, value, eacapeChar));
    }

    /**
     * <p>
     * Detail test of <code>LikeFilter(String, String, char)</code> constructor.
     * </p>
     */
    public void testLikeFilterCtor2_Detail() {
        filter = new LikeFilter(name, value, eacapeChar);

        // check null here.
        assertNotNull("Create LikeFilter failed.", filter);

        // check the filter type here.
        assertTrue("LikeFilter should extend Filter.", filter instanceof Filter);

        // check its attributes here.
        assertEquals("The name of LikeFilter is incorrect.", name, filter.getName());
        assertEquals("The value of LikeFilter is incorrect.", value, filter.getValue());
        assertEquals("The escape char of LikeFilter is incorrect.", eacapeChar,
            filter.getEscapeCharacter());
        assertEquals("The filter type of LikeFilter is incorrect.", Filter.LIKE_FILTER,
            filter.getFilterType());
    }

    /**
     * <p>
     * Basic test of the <code>isValid(Map, Map)</code> method. It test with the valid result.
     * </p>
     */
    public void testMethod_isValid_Valid() {
        filter = new LikeFilter(name, value);

        ValidationResult result = filter.isValid(validatorsMap, aliasMap);

        //the validation should be successful.
        assertTrue("The validation result should be true.", result.isValid());
    }

    /**
     * <p>
     * Basic test of the <code>isValid(Map, Map)</code> method. It test with the invalid result.
     * </p>
     */
    public void testMethod_isValid_Invalid() {
        filter = new LikeFilter("SS", "EW:abc");

        ValidationResult result = filter.isValid(validatorsMap, aliasMap);

        //the validation should be unsuccessful.
        assertFalse("The validation result should be false.", result.isValid());
    }

    /**
     * <p>
     * Basic test of the <code>getFilterType()</code> method.
     * </p>
     */
    public void testMethod_getFilterType() {
        filter = new LikeFilter(name, value);
        assertEquals("The filter type of LikeFilter is incorrect.", Filter.LIKE_FILTER,
            filter.getFilterType());
    }

    /**
     * <p>
     * Basic test of the <code>getEscapeCharacter()</code> method. It tests with the default escape
     * char.
     * </p>
     */
    public void testMethod_getEscapeCharacter_Default() {
        filter = new LikeFilter(name, value);
        assertEquals("The escape char of LikeFilter is incorrect.", DEFAULTESCAPE,
            filter.getEscapeCharacter());
    }

    /**
     * <p>
     * Basic test of the <code>getEscapeCharacter()</code> method. It tests with the set escape
     * char.
     * </p>
     */
    public void testMethod_getEscapeCharacter() {
        filter = new LikeFilter(name, value, eacapeChar);
        assertEquals("The escape char of LikeFilter is incorrect.", eacapeChar,
            filter.getEscapeCharacter());
    }

    /**
     * <p>
     * Basic test of the <code>getName()</code> method.
     * </p>
     */
    public void testMethod_getName() {
        filter = new LikeFilter(name, value);
        assertEquals("The name of LikeFilter is incorrect.", name, filter.getName());
    }

    /**
     * <p>
     * Basic test of the <code>getValue()</code> method.
     * </p>
     */
    public void testMethod_getValue() {
        filter = new LikeFilter(name, value);
        assertEquals("The value of LikeFilter is incorrect.", value, filter.getValue());
    }

    /**
     * <p>
     * Basic test of the <code>clone()</code> method. It tests with the <code>LikeFilter(String,
     * String)</code> constructor.
     * </p>
     */
    public void testMethod_clone_Ctor1() {
        filter = new LikeFilter(name, value);

        // clone a like filter here.
        LikeFilter clonedFilter = (LikeFilter) filter.clone();

        // check null here.
        assertNotNull("Clone LikeFilter failed.", clonedFilter);

        // check the filter type here.
        assertTrue("LikeFilter should extend Filter.", clonedFilter instanceof Filter);

        // check its attributes here.
        assertEquals("The name of cloned LikeFilter is incorrect.", name, clonedFilter.getName());
        assertEquals("The value of cloned LikeFilter is incorrect.", value, clonedFilter.getValue());
        assertEquals("The escape char of cloned LikeFilter is incorrect.", DEFAULTESCAPE,
            clonedFilter.getEscapeCharacter());
        assertEquals("The filter type of cloned LikeFilter is incorrect.", Filter.LIKE_FILTER,
            clonedFilter.getFilterType());
    }

    /**
     * <p>
     * Basic test of the <code>clone()</code> method. It tests with the <code>LikeFilter(String,
     * String)</code> constructor.
     * </p>
     */
    public void testMethod_clone_Ctor2() {
        filter = new LikeFilter(name, value, eacapeChar);

        // clone a like filter here.
        LikeFilter clonedFilter = (LikeFilter) filter.clone();

        // check null here.
        assertNotNull("Clone LikeFilter failed.", clonedFilter);

        // check the filter type here.
        assertTrue("LikeFilter should extend Filter.", clonedFilter instanceof Filter);

        // check its attributes here.
        assertEquals("The name of cloned LikeFilter is incorrect.", name, clonedFilter.getName());
        assertEquals("The value of cloned LikeFilter is incorrect.", value, clonedFilter.getValue());
        assertEquals("The escape char of cloned LikeFilter is incorrect.", eacapeChar,
            clonedFilter.getEscapeCharacter());
        assertEquals("The filter type of cloned LikeFilter is incorrect.", Filter.LIKE_FILTER,
            clonedFilter.getFilterType());
    }
}
