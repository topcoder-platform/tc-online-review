/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;

import junit.framework.TestCase;
/**
 * The unit test of <code>UnrecognizedFilterException</code>.
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class UnrecognizedFilterExceptionTests extends TestCase {
    /**
     * The UnrecognizedFilterException instance to test.
     */
    private UnrecognizedFilterException instance = null;
    /**
     * The Filter instance to test.
     */
    private Filter filter = null;
    /**
     * The setUp.
     */
    protected void setUp() {
        filter = new EqualToFilter("a", "a");
        instance = new UnrecognizedFilterException("message", filter);
    }
    /**
     * Test the constructor.
     */
    public void testconstructor() {
        assertNotNull("cosntruct failed.", instance);
    }
    /**
     * Test the constructor.
     */
    public void testgetFilter() {
        assertNotNull("The filter is set.", instance.getFilter());
    }
}
