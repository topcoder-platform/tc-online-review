/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.ldap;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.TestHelper;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;
import com.topcoder.util.classassociations.ClassAssociator;

import junit.framework.TestCase;
/**
 * <p>
 * Unit test cases for RangeFragmentBuilder.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class RangeFragmentBuilderTests extends TestCase {
    /**
     * The namespace.
     */
    private static final String NS = "LDAPSearchStrategy";
    /**
     * The instatnce to test.
     */
    private RangeFragmentBuilder builder = null;
    /**
     * The AndFilter.
     */
    private Filter filter = null;
    /**
     * The invalid filter.
     */
    private Filter invalid = null;
    /**
     * The map of alias name and real name.
     *
     */
    private Map aliasMap = null;
    /**
     * The class ClassAssociator.
     */
    private ClassAssociator classAssociator = null;
    /**
     * The SearchContext.
     */
    private SearchContext searchContext = null;
    /**
     * The setUp.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        //add the configuration
        TestHelper.clearConfig();
        TestHelper.addConfig();

        builder = new RangeFragmentBuilder();
        aliasMap = new HashMap();
        aliasMap.put("sb", "searchbuild");
        aliasMap.put("sn", "sn");
        invalid = new EqualToFilter("sn", "2");
        classAssociator = SearchBuilderHelper.loadClassAssociator(NS);
        searchContext = new SearchContext(classAssociator, aliasMap);
    }

    /**
     * The tearDown of the unit test.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        //remove the configuration in tearDown
        TestHelper.clearConfig();
    }
    /**
     * Test the constructor.
     *
     */
    public void testconstructor() {
        assertNotNull("construct failed.", builder);
    }
    /**
     * Test the type.
     *
     */
    public void testtype() {
        assertTrue("A SearchFragmentBuilder instance.", builder instanceof SearchFragmentBuilder);
    }
    /**
     * The failure test of buildSearch,
     * fail for invalid parameter,
     * IllegalArgumentException should be thrown.
     * @throws Exception to JUnit
     */
    public void testbuildSearch_failure1() throws Exception {
        try {
            builder.buildSearch(null, searchContext);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of buildSearch,
     * fail for invalid parameter,
     * IllegalArgumentException should be thrown.
     * @throws Exception to JUnit
     */
    public void testbuildSearch_failure2() throws Exception {
        try {
            builder.buildSearch(filter, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of buildSearch,
     * fail for invalid parameter,
     * UnrecognizedFilterException should be thrown.
     * @throws Exception to JUnit
     */
    public void testbuildSearch_failure3() throws Exception {
        try {
            builder.buildSearch(invalid, searchContext);
            fail("UnrecognizedFilterException should be thrown.");
        } catch (UnrecognizedFilterException e) {
            //pass
        }
    }
    /**
     * The accuracy test of buildSearch.
     * @throws Exception to JUnit
     */
    public void testbuildSearch_accuracy1() throws Exception {
        filter = new GreaterThanFilter("name", "1");
        builder.buildSearch(filter, searchContext);
        assertEquals("the generated String should be same.", "(&(!(name=1))(name>=1))",
                searchContext.getSearchString().toString());
    }
    /**
     * The accuracy test of buildSearch.
     * @throws Exception to JUnit
     */
    public void testbuildSearch_accuracy2() throws Exception {
        filter = new GreaterThanOrEqualToFilter("name", "1");
        builder.buildSearch(filter, searchContext);
        assertEquals("the generated String should be same.", "(name>=1)",
                searchContext.getSearchString().toString());
    }
    /**
     * The accuracy test of buildSearch.
     * @throws Exception to JUnit
     */
    public void testbuildSearch_accuracy3() throws Exception {
        filter = new LessThanFilter("name", "1");
        builder.buildSearch(filter, searchContext);
        assertEquals("the generated String should be same.", "(&(!(name=1))(name<=1))",
                searchContext.getSearchString().toString());
    }
    /**
     * The accuracy test of buildSearch.
     * @throws Exception to JUnit
     */
    public void testbuildSearch_accuracy4() throws Exception {
        filter = new LessThanOrEqualToFilter("name", "1");
        builder.buildSearch(filter, searchContext);
        assertEquals("the generated String should be same.", "(name<=1)",
                searchContext.getSearchString().toString());
    }
    /**
     * The accuracy test of buildSearch.
     * @throws Exception to JUnit
     */
    public void testbuildSearch_accuracy5() throws Exception {
        filter = new BetweenFilter("name", "1", "100");
        builder.buildSearch(filter, searchContext);
        assertEquals("the generated String should be same.", "(&(name>=100)(name<=1))",
                searchContext.getSearchString().toString());
    }
}
