/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests.ldap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;
import com.topcoder.search.builder.ldap.NullFragmentBuilder;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.filter.Filter;

import java.util.regex.Pattern;
import java.util.List;

/**
 * <p>An accuracy test for {@link com.topcoder.search.builder.ldap.NullFragmentBuilder} class. Tests the methods for
 * proper handling of valid input data and producing accurate results. Passes the valid arguments to the methods and
 * verifies that either the state of the tested instance have been changed appropriately or a correct result is produced
 * by the method.</p>
 *
 * @author isv
 * @version 1.0
 * @since 1.3
 */
public class NullFragmentBuilderAccuracyTest extends TestCase {

    /**
     * <p>A <code>Pattern</code> to be used to match the generated search string to expected value.</p>
     */
    private static Pattern SEARCH_STRING_NO_ALIAS_PATTERN = Pattern.compile("\\(![ ]*\\(attr1=\\*\\)\\)");

    /**
     * <p>A <code>Pattern</code> to be used to match the generated search string to expected value.</p>
     */
    private static Pattern SEARCH_STRING_ALIAS_PATTERN = Pattern.compile("\\(![ ]*\\(attr2=\\*\\)\\)");

    /**
     * <p>The instances of {@link NullFragmentBuilder} which are tested. These instances are initialized in {@link
     * #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private NullFragmentBuilder[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link NullFragmentBuilder} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link NullFragmentBuilder} class.
     */
    public static Test suite() {
        return new TestSuite(NullFragmentBuilderAccuracyTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new NullFragmentBuilder[1];
        this.testedInstances[0] = new NullFragmentBuilder();
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
     * <p>Accuracy test. Tests the {@link NullFragmentBuilder#buildSearch(Filter, SearchContext)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method builds the search string correctly and binds the parameters to search context
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testBuildSearch_Filter_SearchContext_NoAliasUsed() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            SearchContext searchContext = TestDataFactory.getSearchContext();
            this.testedInstances[i].buildSearch(TestDataFactory.getNullFilterWithoutAlias(), searchContext);
            String searchString = searchContext.getSearchString().toString().trim();
            assertTrue("The search string is not built correctly : " + searchString,
                       SEARCH_STRING_NO_ALIAS_PATTERN.matcher(searchString).matches());
            List bindParameters = searchContext.getBindableParameters();
            Assert.assertEquals("No parameters must be collected", 0, bindParameters.size());
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link NullFragmentBuilder#buildSearch(Filter, SearchContext)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method builds the search string correctly and binds the parameters to search context
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testBuildSearch_Filter_SearchContext_AliasUsed() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            SearchContext searchContext = TestDataFactory.getSearchContext();
            this.testedInstances[i].buildSearch(TestDataFactory.getNullFilterWithAlias(), searchContext);
            String searchString = searchContext.getSearchString().toString().trim();
            assertTrue("The search string is not built correctly : " + searchString,
                       SEARCH_STRING_ALIAS_PATTERN.matcher(searchString).matches());
            List bindParameters = searchContext.getBindableParameters();
            Assert.assertEquals("No parameters must be collected", 0, bindParameters.size());
        }
    }
}
