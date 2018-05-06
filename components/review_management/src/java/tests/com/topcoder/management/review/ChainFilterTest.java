/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review;

import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.OrFilter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;

/**
 * <p>
 * This class tests the <code>ChainFilter</code> class.
 * </p>
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Fixed the code to meet the TopCoder standard.</li>
 * </ol>
 * </p>
 *
 * @author icyriver, sparemax
 * @version 1.2
 */
public class ChainFilterTest extends TestCase {
    /**
     * <p>
     * The instance of <code>EqualToFilter</code> for unit test.
     * </p>
     */
    private Filter equalFilter;

    /**
     * <p>
     * The instance of <code>LessThanFilter</code> for unit test.
     * </p>
     */
    private Filter lessThanFilter;

    /**
     * <p>
     * The instance of <code>GreaterThanFilter</code> for unit test.
     * </p>
     */
    private Filter greaterThanFilter;

    /**
     * <p>
     * The instance of <code>ChainFilter</code> for unit test.
     * </p>
     */
    private ChainFilter test;

    /**
     * <p>
     * Returns the test suite of <code>ChainFilterTest</code>.
     * </p>
     *
     * @return the test suite of <code>ChainFilterTest</code>.
     */
    public static Test suite() {
        return new TestSuite(ChainFilterTest.class);
    }

    /**
     * <p>
     * Set up the unit test environment here.
     * </p>
     */
    protected void setUp() {
        // create some filters for test.
        lessThanFilter = new LessThanFilter("reviewer", 10000L);
        greaterThanFilter = new GreaterThanFilter("project", 10001L);
        equalFilter = new EqualToFilter("committed", 1);

        // create a ChainFilter instance with equal filter.
        test = new ChainFilter(equalFilter);
    }

    /**
     * <p>
     * Basic test of <code>ChainFilter(Filter)</code> constructor.
     * </p>
     */
    public void testChainFilterCtor_Basic() {
        // check null here.
        assertNotNull("Create ChainFilter failed.", new ChainFilter(equalFilter));
    }

    /**
     * <p>
     * Detail test of <code>ChainFilter(Filter)</code> constructor. Creates a instance and get it's attributes for
     * test.
     * </p>
     */
    public void testChainFilterCtor_Detail() {
        // create the instance for test.
        test = new ChainFilter(equalFilter);

        // check null here.
        assertNotNull("Create ChainFilter failed.", test);

        // check the inner filter here.
        assertSame("The inner filter should be equalFilter.", equalFilter, test.getFilter());
        assertEquals("The inner filter's name should be: committed.", "committed", ((EqualToFilter) test.getFilter())
            .getName());
        assertEquals("The inner filter's value should be: 1.", 1, ((EqualToFilter) test.getFilter()).getValue());
    }

    /**
     * <p>
     * Tests <code>ChainFilter(Filter)</code> constructor for failure.
     * </p>
     *
     * <p>
     * It try to pass a <code>null</code> filter into the constructor, <code>IllegalArgumentException</code> is
     * expected.
     * </p>
     */
    public void testChainFilterCtorForException_Null() {
        try {
            test = new ChainFilter(null);

            // should not be here
            fail("IllegalArgumentException is expected here.");
        } catch (IllegalArgumentException e) {
            // should be here
        }
    }

    /**
     * <p>
     * Test of <code>and(Filter)</code> method with two simple filters.
     * </p>
     */
    public void testAnd_TwoFilters() {
        // construct with and operation.
        ChainFilter chainFilter = test.and(lessThanFilter);

        // check the inner filter here.
        assertTrue("The inner filter should be AndFilter.", chainFilter.getFilter() instanceof AndFilter);

        // check the filter list here.
        List<?> filters = ((AndFilter) chainFilter.getFilter()).getFilters();
        assertEquals("The inner filter list's size should be: 2.", 2, filters.size());
        assertSame("The first filter should be equalFilter.", equalFilter, filters.get(0));
        assertSame("The second filter should be lessThanFilter.", lessThanFilter, filters.get(1));
    }

    /**
     * <p>
     * Test of <code>and(Filter)</code> method with more filters.
     * </p>
     */
    public void testAnd_MoreFilters() {
        // construct with and operation.
        ChainFilter chainFilter = test.and(lessThanFilter).and(greaterThanFilter);

        // check the inner filter here.
        assertTrue("The inner filter should be AndFilter.", chainFilter.getFilter() instanceof AndFilter);

        // check the first level filter list here.
        List<?> filters1 = ((AndFilter) chainFilter.getFilter()).getFilters();
        assertEquals("The first level inner filter list's size should be: 2.", 2, filters1.size());
        assertTrue("The first filter in first level filter list should be AndFilter.",
            filters1.get(0) instanceof AndFilter);
        assertSame("The second filter in first level filter list should be greaterThanFilter.", greaterThanFilter,
            filters1.get(1));

        // check the second level filter list here.
        List<?> filters2 = ((AndFilter) filters1.get(0)).getFilters();
        assertEquals("The second level inner filter list's size should be: 2.", 2, filters2.size());
        assertSame("The first filter in second level filter list should be equalFilter.", equalFilter, filters2
            .get(0));
        assertSame("The second filter in second level filter list should be lessThanFilter.", lessThanFilter,
            filters2.get(1));
    }

    /**
     * <p>
     * Test of <code>and(Filter)</code> method for immutable inner filter.
     * </p>
     */
    public void testAnd_Immutable() {
        // construct with and operation.
        ChainFilter chainFilter = test.and(lessThanFilter);

        // check the original inner filter.
        assertSame("The inner filter should be equalFilter.", equalFilter, test.getFilter());

        // perform and operation again.
        chainFilter = chainFilter.or(lessThanFilter);

        // check the original inner filter.
        assertSame("The inner filter should be equalFilter.", equalFilter, test.getFilter());

        // perform and operation more times.
        chainFilter = test.and(lessThanFilter).and(greaterThanFilter);

        // check the original inner filter.
        assertSame("The inner filter should be equalFilter.", equalFilter, test.getFilter());
    }

    /**
     * <p>
     * Tests <code>and(Filter)</code> method for failure.
     * </p>
     *
     * <p>
     * It try to pass a <code>null</code> filter into the method, <code>IllegalArgumentException</code> is
     * expected.
     * </p>
     */
    public void testAndForException_Null() {
        try {
            test.and(null);

            // should not be here
            fail("IllegalArgumentException is expected here.");
        } catch (IllegalArgumentException e) {
            // should be here
        }
    }

    /**
     * <p>
     * Test of <code>or(Filter)</code> method with two simple filters.
     * </p>
     */
    public void testOr_TwoFilters() {
        // construct with or operation.
        ChainFilter chainFilter = test.or(greaterThanFilter);

        // check the inner filter here.
        assertTrue("The inner filter should be OrFilter.", chainFilter.getFilter() instanceof OrFilter);

        // check the filter list here.
        List<?> filters = ((OrFilter) chainFilter.getFilter()).getFilters();
        assertEquals("The inner filter list's size should be: 2.", 2, filters.size());
        assertSame("The first filter should be equalFilter.", equalFilter, filters.get(0));
        assertSame("The second filter should be greaterThanFilter.", greaterThanFilter, filters.get(1));
    }

    /**
     * <p>
     * Test of <code>or(Filter)</code> method with more filters.
     * </p>
     */
    public void testOr_MoreFilters() {
        // construct with or operation.
        ChainFilter chainFilter = test.or(lessThanFilter).or(greaterThanFilter);

        // check the inner filter here.
        assertTrue("The inner filter should be OrFilter.", chainFilter.getFilter() instanceof OrFilter);

        // check the first level filter list here.
        List<?> filters1 = ((OrFilter) chainFilter.getFilter()).getFilters();
        assertEquals("The first level inner filter list's size should be: 2.", 2, filters1.size());
        assertTrue("The first filter in first level filter list should be OrFilter.",
            filters1.get(0) instanceof OrFilter);
        assertSame("The second filter in first level filter list should be greaterThanFilter.", greaterThanFilter,
            filters1.get(1));

        // check the second level filter list here.
        List<?> filters2 = ((OrFilter) filters1.get(0)).getFilters();
        assertEquals("The second level inner filter list's size should be: 2.", 2, filters2.size());
        assertSame("The first filter in second level filter list should be equalFilter.", equalFilter, filters2
            .get(0));
        assertSame("The second filter in second level filter list should be lessThanFilter.", lessThanFilter,
            filters2.get(1));
    }

    /**
     * <p>
     * Test of <code>or(Filter)</code> method for immutable inner filter.
     * </p>
     */
    public void testOr_Immutable() {
        // construct with or operation.
        ChainFilter chainFilter = test.or(lessThanFilter);

        // check the original inner filter.
        assertSame("The inner filter should be equalFilter.", equalFilter, test.getFilter());

        // perform or operation again.
        chainFilter = chainFilter.and(lessThanFilter);

        // check the original inner filter.
        assertSame("The inner filter should be equalFilter.", equalFilter, test.getFilter());

        // perform or operation more times.
        chainFilter = test.or(lessThanFilter).or(greaterThanFilter);

        // check the original inner filter.
        assertSame("The inner filter should be equalFilter.", equalFilter, test.getFilter());
    }

    /**
     * <p>
     * Tests <code>or(Filter)</code> method for failure.
     * </p>
     *
     * <p>
     * It try to pass a <code>null</code> filter into the method, <code>IllegalArgumentException</code> is
     * expected.
     * </p>
     */
    public void testOrForException_Null() {
        try {
            test.or(null);

            // should not be here
            fail("IllegalArgumentException is expected here.");
        } catch (IllegalArgumentException e) {
            // should be here
        }
    }

    /**
     * <p>
     * Test of <code>not()</code> method with using it only once.
     * </p>
     */
    public void testNot_Once() {
        // construct with not operation.
        ChainFilter chainFilter = test.not();

        // check the inner filter here.
        assertTrue("The inner filter should be NotFilter.", chainFilter.getFilter() instanceof NotFilter);

        // check the filter list here.
        assertTrue("The first filter should be EqualToFilter.",
            ((NotFilter) chainFilter.getFilter()).getFilter() instanceof EqualToFilter);
    }

    /**
     * <p>
     * Test of <code>not()</code> method with using it more times.
     * </p>
     */
    public void testNot_MoreTimes() {
        // construct with not operation.
        ChainFilter chainFilter = test.not().not();

        // check the inner filter here.
        assertTrue("The inner filter should be NotFilter.", chainFilter.getFilter() instanceof NotFilter);

        // check the first level filter here.
        Filter firstLevel = ((NotFilter) chainFilter.getFilter()).getFilter();
        assertTrue("The second level filter should be NotFilter.", firstLevel instanceof NotFilter);

        // check the second level filter here.
        Filter secondLevel = ((NotFilter) firstLevel).getFilter();
        assertTrue("The second level filter should be EqualToFilter.", secondLevel instanceof EqualToFilter);
    }

    /**
     * <p>
     * Test of <code>not()</code> method for immutable inner filter.
     * </p>
     */
    public void testNot_Immutable() {
        // construct with not operation.
        ChainFilter chainFilter = test.not();

        // check the original inner filter.
        assertSame("The inner filter should be equalFilter.", equalFilter, test.getFilter());

        // perform or operation again.
        chainFilter = chainFilter.and(lessThanFilter);

        // check the original inner filter.
        assertSame("The inner filter should be equalFilter.", equalFilter, test.getFilter());

        // perform or operation more times.
        chainFilter = test.not().not();

        // check the original inner filter.
        assertSame("The inner filter should be equalFilter.", equalFilter, test.getFilter());
    }

    /**
     * <p>
     * Test of the combination of <code>and(Filter)</code>, <code>or(Filter)</code>, and <code>not()</code>
     * methods.
     * </p>
     */
    public void testAllOperations() {
        // construct with all operations.
        ChainFilter chainFilter = test.or(lessThanFilter).and(greaterThanFilter).not();

        // check the inner filter here.
        assertSame("The inner filter should be equalFilter.", equalFilter, test.getFilter());

        // check the first level filter list here.
        Filter andFilter = ((NotFilter) chainFilter.getFilter()).getFilter();
        assertTrue("The inner filter should be AndFilter.", andFilter instanceof AndFilter);

        // check the second level filter list here.
        List<?> filters1 = ((AndFilter) andFilter).getFilters();
        assertTrue("The first filter should be OrFilter.", filters1.get(0) instanceof OrFilter);
        assertTrue("The second filter should be GreaterThanFilter.", filters1.get(1) instanceof GreaterThanFilter);

        // check the third level filter list here.
        List<?> filters2 = ((OrFilter) filters1.get(0)).getFilters();
        assertEquals("The inner filter list's size should be: 2.", 2, filters2.size());
        assertTrue("The first filter should be EqualToFilter.", filters2.get(0) instanceof EqualToFilter);
        assertTrue("The second filter should be LessThanFilter.", filters2.get(1) instanceof LessThanFilter);
    }

    /**
     * <p>
     * Test of <code>getFilter()</code> method for inner filter. It tests for get the original inner filter.
     * </p>
     */
    public void testGetFilter_Original() {
        // perform with and operation.
        test.and(lessThanFilter);

        // check the original inner filter.
        assertSame("The inner filter should be equalFilter.", equalFilter, test.getFilter());

        // perform with not operation.
        test.not();

        // check the original inner filter.
        assertSame("The inner filter should be Filter.EQUAL_TO_FILTER.", equalFilter, test.getFilter());

        // perform with more operations.
        test.or(greaterThanFilter).and(test.getFilter());

        // check the original inner filter.
        assertSame("The inner filter should be equalFilter.", equalFilter, test.getFilter());
    }

    /**
     * <p>
     * Test of <code>getFilter()</code> method for inner filter. It tests for get the new inner filter.
     * </p>
     */
    public void testGetFilter_New() {
        // perform with and operation.
        ChainFilter chainFilter = test.or(lessThanFilter);

        // check the new inner filter.
        assertTrue("The inner filter should be OrFilter.", chainFilter.getFilter() instanceof OrFilter);

        // perform with not operation.
        chainFilter = chainFilter.not();

        // check the new inner filter.
        assertTrue("The inner filter should be NotFilter.", chainFilter.getFilter() instanceof NotFilter);

        // perform with more operations.
        chainFilter = chainFilter.or(greaterThanFilter).and(test.getFilter());

        // check the new inner filter.
        assertTrue("The inner filter should be AndFilter.", chainFilter.getFilter() instanceof AndFilter);
    }
}
