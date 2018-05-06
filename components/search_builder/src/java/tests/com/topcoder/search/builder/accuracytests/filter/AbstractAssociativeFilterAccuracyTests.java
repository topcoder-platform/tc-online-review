/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests.filter;

import com.topcoder.search.builder.ValidationResult;
import com.topcoder.search.builder.filter.*;

import junit.framework.TestCase;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Accuracy test cases for the AbstractAssociativeFilter class.
 *
 * @author zjq, isv
 * @version 1.3
 */
public class AbstractAssociativeFilterAccuracyTests extends TestCase {
    /** A filter used in some tests. */
    private static final Filter FILTER1 = new EqualToFilter("name", "value1");

    /** A second filter used in some tests. */
    private static final Filter FILTER2 = new EqualToFilter("name", "value2");

    /**
     * A third filter used in some tests.
     *
     * @since 1.3
     */
    private static final Filter FILTER3 = new NullFilter("NullFilter");

    /** A AbstractAssociativeFilter to run the tests on. */
    private TestAssociativeFilter filter;

    /**
     * Set up the testing environment.
     */
    public void setUp() {
        List filters = new ArrayList();
        filters.add(FILTER1);

        filter = new TestAssociativeFilter(filters);
    }

    /**
     * Verify behavior of the constructor(Filter, Filter). Check if the filters are correctly set.
     */
    public void testConstructorFilterFilter() {
        filter = new TestAssociativeFilter(FILTER1, FILTER2);

        List filters = filter.getFilters();
        assertEquals("Filters list has incorrect size", 2, filters.size());
        assertEquals("Filters list contains incorrect filter 0", FILTER1, filters.get(0));
        assertEquals("Filters list contains incorrect filter 1", FILTER2, filters.get(1));
    }

    /**
     * Verify behavior of the constructor(List). Check if the list is correctly set.
     */
    public void testConstructorList() {
        List filters = new ArrayList();
        filters.add(FILTER1);
        filters.add(FILTER2);
        filter = new TestAssociativeFilter(filters);
        assertEquals("Filters are incorrect", filters, filter.getFilters());
    }

    /**
     * Verify behavior of the addFilter method. Check if the filter is correctly added.
     */
    public void testAddFilter() {
        filter.addFilter(FILTER2);

        List filters = filter.getFilters();
        assertEquals("Filter is not added", 2, filters.size());
        assertEquals("Incorrect filter", FILTER2, filters.get(1));
    }

    /**
     * Verify behavior of the getFilters method.
     */
    public void testGetFilters() {
        List filters = filter.getFilters();

        assertEquals("List has incorrect size", 1, filters.size());
        assertEquals("List contains invalid items", FILTER1, filters.get(0));
    }

    /**
     * <p>Accuracy test. Tests the {@link AbstractAssociativeFilter#clone()} method for proper behavior.</p>
     *
     * <p>Verifies that the method performs a deep copy of the filter.</p>
     *
     * @since 1.3
     */
    public void testClone() {
        this.filter.addFilter(FILTER2);
        this.filter.addFilter(FILTER3);

        AbstractAssociativeFilter clone = (AbstractAssociativeFilter) this.filter.clone();
        List originalFilters = this.filter.getFilters();
        List clonedFilters = clone.getFilters();

        Assert.assertNotSame("The filters list is not cloned correctly", originalFilters, clonedFilters);
        Assert.assertEquals("The filters list is not cloned correctly", originalFilters.size(), clonedFilters.size());

        for (int i = 0; i < originalFilters.size(); i++) {
            Filter original = (Filter) originalFilters.get(i);
            Filter cloned = (Filter) clonedFilters.get(i);
            Assert.assertEquals("The filter is not cloned correctly", original.getClass(), cloned.getClass());
            Assert.assertNotSame("The filter is not cloned correctly", original, cloned);
        }
    }

    /**
     * Dummy implementations of the abstract methods of the AbstractAssociativeFilter class.
     */
    private class TestAssociativeFilter extends AbstractAssociativeFilter {
        /**
         * Constructor, simply delegates to super.
         *
         * @param firstFilter the first filter
         * @param secondFilter the second filter
         */
        public TestAssociativeFilter(Filter firstFilter, Filter secondFilter) {
            super(firstFilter, secondFilter);
        }

        /**
         * Constructor, simply delegates to super.
         *
         * @param filters the filters
         */
        public TestAssociativeFilter(List filters) {
            super(filters);
        }

        /**
         * Simply returns null.
         *
         * @param validators the validators to use
         * @param alias the aliasses to use
         *
         * @return always null
         */
        public ValidationResult isValid(Map validators, Map alias) {
            return null;
        }

        /**
         * Return the type.
         *
         * @return always -1
         */
        public int getFilterType() {
            return -1;
        }

        /**
         * Make a clone.
         *
         * @return always null
         */
        public Object clone() {
            return super.clone();
        }
    }
}
