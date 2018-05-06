/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests.ldap;

import com.topcoder.search.builder.ldap.RangeFragmentBuilder;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.filter.Filter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;

import java.util.regex.Pattern;
import java.util.List;

/**
 * <p>An accuracy test for {@link RangeFragmentBuilder} class. Tests the methods for
 * proper handling of valid input data and producing accurate results. Passes the valid arguments to the methods and
 * verifies that either the state of the tested instance have been changed appropriately or a correct result is produced
 * by the method.</p>
 *
 * @author isv
 * @version 1.0
 * @since 1.3
 */
public class RangeFragmentBuilderAccuracyTest extends TestCase {

    /**
     * <p>A <code>Pattern</code> to be used to match the generated search string to expected value.</p>
     */
    private static Pattern SEARCH_STRING_NO_ALIAS_PATTERN_GREATER
        = Pattern.compile("\\(&[ ]*\\(![ ]*\\(attr1=1\\)\\)[ ]*\\(attr1>=1\\)\\)");

    /**
     * <p>A <code>Pattern</code> to be used to match the generated search string to expected value.</p>
     */
    private static Pattern SEARCH_STRING_ALIAS_PATTERN_GREATER
        = Pattern.compile("\\(&[ ]*\\(![ ]*\\(attr2=2\\)\\)[ ]*\\(attr2>=2\\)\\)");

    /**
     * <p>A <code>Pattern</code> to be used to match the generated search string to expected value.</p>
     */
    private static Pattern SEARCH_STRING_NO_ALIAS_PATTERN_GREATER_OR_EQUAL
        = Pattern.compile("\\(attr1>=1\\)");

    /**
     * <p>A <code>Pattern</code> to be used to match the generated search string to expected value.</p>
     */
    private static Pattern SEARCH_STRING_ALIAS_PATTERN_GREATER_OR_EQUAL = Pattern.compile("\\(attr2>=2\\)");

    /**
     * <p>A <code>Pattern</code> to be used to match the generated search string to expected value.</p>
     */
    private static Pattern SEARCH_STRING_NO_ALIAS_PATTERN_LESS
        = Pattern.compile("\\(&[ ]*\\(![ ]*\\(attr1=1\\)\\)[ ]*\\(attr1<=1\\)\\)");

    /**
     * <p>A <code>Pattern</code> to be used to match the generated search string to expected value.</p>
     */
    private static Pattern SEARCH_STRING_ALIAS_PATTERN_LESS
        = Pattern.compile("\\(&[ ]*\\(![ ]*\\(attr2=2\\)\\)[ ]*\\(attr2<=2\\)\\)");

    /**
     * <p>A <code>Pattern</code> to be used to match the generated search string to expected value.</p>
     */
    private static Pattern SEARCH_STRING_NO_ALIAS_PATTERN_LESS_OR_EQUAL = Pattern.compile("\\(attr1<=1\\)");

    /**
     * <p>A <code>Pattern</code> to be used to match the generated search string to expected value.</p>
     */
    private static Pattern SEARCH_STRING_ALIAS_PATTERN_LESS_OR_EQUAL = Pattern.compile("\\(attr2<=2\\)");

    /**
     * <p>A <code>Pattern</code> to be used to match the generated search string to expected value.</p>
     */
    private static Pattern SEARCH_STRING_NO_ALIAS_PATTERN_BETWEEN
        = Pattern.compile("\\(&[ ]*\\(attr1>=1\\)[ ]*\\(attr1<=2\\)\\)");

    /**
     * <p>A <code>Pattern</code> to be used to match the generated search string to expected value.</p>
     */
    private static Pattern SEARCH_STRING_ALIAS_PATTERN_BETWEEN
        = Pattern.compile("\\(&[ ]*\\(attr2>=4\\)[ ]*\\(attr2<=5\\)\\)");

    /**
     * <p>The instances of {@link RangeFragmentBuilder} which are tested. These instances are initialized in {@link
     * #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private RangeFragmentBuilder[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link RangeFragmentBuilder} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link RangeFragmentBuilder} class.
     */
    public static Test suite() {
        return new TestSuite(RangeFragmentBuilderAccuracyTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new RangeFragmentBuilder[1];
        this.testedInstances[0] = new RangeFragmentBuilder();
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        this.testedInstances = null;
        super.tearDown();
    }

    /**
     * <p>Accuracy test. Tests the {@link RangeFragmentBuilder#buildSearch(Filter, SearchContext)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method builds the search string correctly and binds the parameters to search context
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testBuildSearch_Filter_SearchContext_NoAliasUsed_Greater() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            SearchContext searchContext = TestDataFactory.getSearchContext();
            this.testedInstances[i].buildSearch(TestDataFactory.getGreaterThanFilterWithoutAlias(), searchContext);
            String searchString = searchContext.getSearchString().toString().trim();
            assertTrue("The search string is not built correctly : " + searchString,
                       SEARCH_STRING_NO_ALIAS_PATTERN_GREATER.matcher(searchString).matches());
            List bindParameters = searchContext.getBindableParameters();
            Assert.assertEquals("No parameters must be collected", 0, bindParameters.size());
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link RangeFragmentBuilder#buildSearch(Filter, SearchContext)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method builds the search string correctly and binds the parameters to search context
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testBuildSearch_Filter_SearchContext_AliasUsed_Greater() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            SearchContext searchContext = TestDataFactory.getSearchContext();
            this.testedInstances[i].buildSearch(TestDataFactory.getGreaterThanFilterWithAlias(), searchContext);
            String searchString = searchContext.getSearchString().toString().trim();
            assertTrue("The search string is not built correctly : " + searchString,
                       SEARCH_STRING_ALIAS_PATTERN_GREATER.matcher(searchString).matches());
            List bindParameters = searchContext.getBindableParameters();
            Assert.assertEquals("No parameters must be collected", 0, bindParameters.size());
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link RangeFragmentBuilder#buildSearch(Filter, SearchContext)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method builds the search string correctly and binds the parameters to search context
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testBuildSearch_Filter_SearchContext_NoAliasUsed_GreaterEqual() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            SearchContext searchContext = TestDataFactory.getSearchContext();
            this.testedInstances[i].buildSearch(TestDataFactory.getGreaterThanOrEqualToFilterWithoutAlias(),
                                                searchContext);
            String searchString = searchContext.getSearchString().toString().trim();
            assertTrue("The search string is not built correctly : " + searchString,
                       SEARCH_STRING_NO_ALIAS_PATTERN_GREATER_OR_EQUAL.matcher(searchString).matches());
            List bindParameters = searchContext.getBindableParameters();
            Assert.assertEquals("No parameters must be collected", 0, bindParameters.size());
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link RangeFragmentBuilder#buildSearch(Filter, SearchContext)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method builds the search string correctly and binds the parameters to search context
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testBuildSearch_Filter_SearchContext_AliasUsed_GreaterEqual() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            SearchContext searchContext = TestDataFactory.getSearchContext();
            this.testedInstances[i].buildSearch(TestDataFactory.getGreaterThanOrEqualToFilterWithAlias(),
                                                searchContext);
            String searchString = searchContext.getSearchString().toString().trim();
            assertTrue("The search string is not built correctly : " + searchString,
                       SEARCH_STRING_ALIAS_PATTERN_GREATER_OR_EQUAL.matcher(searchString).matches());
            List bindParameters = searchContext.getBindableParameters();
            Assert.assertEquals("No parameters must be collected", 0, bindParameters.size());
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link RangeFragmentBuilder#buildSearch(Filter, SearchContext)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method builds the search string correctly and binds the parameters to search context
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testBuildSearch_Filter_SearchContext_NoAliasUsed_Less() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            SearchContext searchContext = TestDataFactory.getSearchContext();
            this.testedInstances[i].buildSearch(TestDataFactory.getLessThanFilterWithoutAlias(), searchContext);
            String searchString = searchContext.getSearchString().toString().trim();
            assertTrue("The search string is not built correctly : " + searchString,
                       SEARCH_STRING_NO_ALIAS_PATTERN_LESS.matcher(searchString).matches());
            List bindParameters = searchContext.getBindableParameters();
            Assert.assertEquals("No parameters must be collected", 0, bindParameters.size());
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link RangeFragmentBuilder#buildSearch(Filter, SearchContext)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method builds the search string correctly and binds the parameters to search context
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testBuildSearch_Filter_SearchContext_AliasUsed_Less() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            SearchContext searchContext = TestDataFactory.getSearchContext();
            this.testedInstances[i].buildSearch(TestDataFactory.getLessThanFilterWithAlias(), searchContext);
            String searchString = searchContext.getSearchString().toString().trim();
            assertTrue("The search string is not built correctly : " + searchString,
                       SEARCH_STRING_ALIAS_PATTERN_LESS.matcher(searchString).matches());
            List bindParameters = searchContext.getBindableParameters();
            Assert.assertEquals("No parameters must be collected", 0, bindParameters.size());
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link RangeFragmentBuilder#buildSearch(Filter, SearchContext)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method builds the search string correctly and binds the parameters to search context
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testBuildSearch_Filter_SearchContext_NoAliasUsed_LessEqual() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            SearchContext searchContext = TestDataFactory.getSearchContext();
            this.testedInstances[i].buildSearch(TestDataFactory.getLessThanOrEqualToFilterWithoutAlias(),
                                                searchContext);
            String searchString = searchContext.getSearchString().toString().trim();
            assertTrue("The search string is not built correctly : " + searchString,
                       SEARCH_STRING_NO_ALIAS_PATTERN_LESS_OR_EQUAL.matcher(searchString).matches());
            List bindParameters = searchContext.getBindableParameters();
            Assert.assertEquals("No parameters must be collected", 0, bindParameters.size());
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link RangeFragmentBuilder#buildSearch(Filter, SearchContext)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method builds the search string correctly and binds the parameters to search context
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testBuildSearch_Filter_SearchContext_AliasUsed_LessEqual() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            SearchContext searchContext = TestDataFactory.getSearchContext();
            this.testedInstances[i].buildSearch(TestDataFactory.getLessThanOrEqualToFilterWithAlias(),
                                                searchContext);
            String searchString = searchContext.getSearchString().toString().trim();
            assertTrue("The search string is not built correctly : " + searchString,
                       SEARCH_STRING_ALIAS_PATTERN_LESS_OR_EQUAL.matcher(searchString).matches());
            List bindParameters = searchContext.getBindableParameters();
            Assert.assertEquals("No parameters must be collected", 0, bindParameters.size());
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link RangeFragmentBuilder#buildSearch(Filter, SearchContext)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method builds the search string correctly and binds the parameters to search context
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testBuildSearch_Filter_SearchContext_NoAliasUsed_Between() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            SearchContext searchContext = TestDataFactory.getSearchContext();
            this.testedInstances[i].buildSearch(TestDataFactory.getBetweenFilterWithoutAlias(), searchContext);
            String searchString = searchContext.getSearchString().toString().trim();
            assertTrue("The search string is not built correctly : " + searchString,
                       SEARCH_STRING_NO_ALIAS_PATTERN_BETWEEN.matcher(searchString).matches());
            List bindParameters = searchContext.getBindableParameters();
            Assert.assertEquals("No parameters must be collected", 0, bindParameters.size());
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link RangeFragmentBuilder#buildSearch(Filter, SearchContext)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method builds the search string correctly and binds the parameters to search context
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testBuildSearch_Filter_SearchContext_AliasUsed_Between() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            SearchContext searchContext = TestDataFactory.getSearchContext();
            this.testedInstances[i].buildSearch(TestDataFactory.getBetweenFilterWithAlias(), searchContext);
            String searchString = searchContext.getSearchString().toString().trim();
            assertTrue("The search string is not built correctly : " + searchString,
                       SEARCH_STRING_ALIAS_PATTERN_BETWEEN.matcher(searchString).matches());
            List bindParameters = searchContext.getBindableParameters();
            Assert.assertEquals("No parameters must be collected", 0, bindParameters.size());
        }
    }
}
