/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests.filter;

import com.topcoder.search.builder.ValidationResult;
import com.topcoder.search.builder.filter.*;

import junit.framework.TestCase;

import java.util.Map;


/**
 * Accuracy test cases for the AbstractSimpleFilter class.
 *
 * @author zjq
 * @version 1.1
 */
public class AbstractSimpleFilterAccuracyTests extends TestCase {
    /** A name used in the tests. */
    private static final String NAME = "Name";

    /** A value used in the tests. */
    private static final Comparable VALUE1 = "Value1";

    /** A second value used in the tests. */
    private static final Comparable VALUE2 = "Value2";

    /** A AbstractSimpleFilter to run the tests on. */
    private TestSimpleFilter filter;

    /**
     * Set up the testing environment.
     */
    public void setUp() {
        filter = new TestSimpleFilter(NAME, VALUE1, VALUE2);
    }

    /**
     * Verify behavior of the constructor(name, value). Check the fields are correctly initialized.
     */
    public void testConstructorValue() {
        filter = new TestSimpleFilter(NAME, VALUE1);

        assertEquals("Name is incorrect", NAME, filter.getName());
        assertEquals("Valie is incorrect", VALUE1, filter.getValue());
    }

    /**
     * Verify behavior of the constructor(name, upper, lower). Check if the fields are correctly initialized.
     */
    public void testConstructorUpperLower() {
        filter = new TestSimpleFilter(NAME, VALUE1, VALUE2);

        assertEquals("Name is incorrect", NAME, filter.getName());
        assertEquals("Upper value is incorrect", VALUE1, filter.getUpperThreshold());
        assertEquals("Lower value is incorrect", VALUE2, filter.getLowerThreshold());
    }

    /**
     * Verify behavior of the getName method.
     */
    public void testGetName() {
        assertEquals("Name is incorrect", NAME, filter.getName());
    }

    /**
     * Verify behavior of the getValue method.
     */
    public void testGetValue() {
        assertNull("Value is incorrect", filter.getValue());
    }

    /**
     * Verify behavior of the getUpperThreshold method.
     */
    public void testGetUpperThreshold() {
        assertEquals("Upper value is incorrect", VALUE1, filter.getUpperThreshold());
    }

    /**
     * Verify behavior of the getLowerThreshold method.
     */
    public void testGetLowerThreshold() {
        assertEquals("Lower value is incorrect", VALUE2, filter.getLowerThreshold());
    }

    /**
     * Verify behavior of the isUpperInclusive method.
     */
    public void testIsUpperInclusive() {
        assertTrue("Should not be inclusive", filter.isUpperInclusive());
    }

    /**
     * Verify behavior of the isLowerInclusive method.
     */
    public void testIsLowerInclusive() {
        assertTrue("Should not be inclusive", filter.isLowerInclusive());
    }

    /**
     * Dummy implementations of the abstract methods of the AbstractSimpleFilter class.
     */
    private class TestSimpleFilter extends AbstractSimpleFilter {
        /**
         * Constructor, simply delegates to super.
         *
         * @param name the name of the fields
         * @param value the value to compare to
         */
        public TestSimpleFilter(String name, Comparable value) {
            super(name, value);
        }

        /**
         * Constructor, simply delegates to super.
         *
         * @param name the name of the field
         * @param upper the upper bound
         * @param lower the lower bound
         */
        public TestSimpleFilter(String name, Comparable upper, Comparable lower) {
            super(name, upper, lower);
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
            return null;
        }
    }
}
