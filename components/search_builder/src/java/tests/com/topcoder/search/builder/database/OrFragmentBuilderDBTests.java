/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.TestHelper;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.util.classassociations.ClassAssociator;

import junit.framework.TestCase;
/**
 * <p>
 * Unit test cases for OrFragmentBuilder.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class OrFragmentBuilderDBTests extends TestCase {
    /**
     * The namespace.
     */
    private static final String NS = "DBSearchStrategy";
    /**
     * The instatnce to test.
     */
    private OrFragmentBuilder builder = null;
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

        builder = new OrFragmentBuilder();
        aliasMap = new HashMap();
        aliasMap.put("sb", "searchbuild");
        aliasMap.put("sn", "sn");
        invalid = new EqualToFilter("sn", "2");
        Filter filter2 = new EqualToFilter("sn", "1");
        filter =  new OrFilter(invalid, filter2);
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
    public void testbuildSearch_accuracy() throws Exception {
        builder.buildSearch(filter, searchContext);
        assertEquals("the generated String should be same.", "(sn = ? OR sn = ?)",
                searchContext.getSearchString().toString());
    }
}
